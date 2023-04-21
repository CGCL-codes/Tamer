package passreminder.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import ostermiller.BadDelimiterException;
import ostermiller.CSVParser;
import passreminder.DBManager;
import passreminder.Extensions;
import passreminder.I18Nable;
import passreminder.Messages;
import passreminder.ModelManager;
import passreminder.PRException;
import passreminder.PassReminder;
import passreminder.PreferenceConstants;
import passreminder.PreferenceManager;
import passreminder.Recents;
import passreminder.UIManager;
import passreminder.model.Group;
import passreminder.model.Item;
import passreminder.ui.ImportDialog;
import passreminder.ui.MasterDialog;
import passreminder.ui.QuickMasterDialog;

public class OpenAction extends Action implements DropTargetListener, I18Nable {

    private boolean concat;

    private boolean importmode;

    public OpenAction() {
        this(false, false);
    }

    public OpenAction(boolean importmode, boolean concat) {
        this.importmode = importmode;
        this.concat = concat;
        if (concat) setImageDescriptor(ImageDescriptor.createFromURL(getClass().getResource("concat.gif"))); else {
            if (importmode) setImageDescriptor(ImageDescriptor.createFromURL(getClass().getResource("import.gif"))); else setImageDescriptor(ImageDescriptor.createFromURL(getClass().getResource("open.gif")));
        }
        updateLanguage();
    }

    public void run() {
        String f = null;
        FileDialog fdialog = new FileDialog(PassReminder.getInstance().getShell(), SWT.OPEN);
        if (importmode) {
            fdialog.setFilterExtensions(new String[] { Extensions.STAR_DOT_CSV });
            fdialog.setFilterNames(new String[] { Messages.getString("import.ext.csv_import", Extensions.STAR_DOT_CSV) });
        } else {
            fdialog.setFilterExtensions(new String[] { Extensions.STAR_DOT_PR });
            fdialog.setFilterNames(new String[] { Messages.getString("export.ext.passreminder", Extensions.STAR_DOT_PR) });
        }
        f = fdialog.open();
        if (f == null) {
            PassReminder.getInstance().setStatus(Messages.getString("open.action.error.file_opening"));
            PassReminder.getInstance().refreshTitle();
            return;
        }
        if (f.endsWith(Extensions.DOT_PR)) openFile(f, concat, concat); else if (f.endsWith(Extensions.DOT_CSV)) {
            ImportDialog importDialog = new ImportDialog(PassReminder.getInstance().getShell(), Messages.getString("import.prompt"));
            if (importDialog.open() == Window.CANCEL) {
                PassReminder.getInstance().setStatus(Messages.getString("action_cancelled"));
                PassReminder.getInstance().refreshTitle();
                return;
            }
            try {
                int[] count = importCSV(f, importDialog.getSeparator(), importDialog.getColumns(), true);
                PassReminder.getInstance().setStatus(Messages.getString("import.status.ok", "" + count[0], "" + count[1]));
                if ((count[0] + count[1]) > 0) {
                    ((Action) PassReminder.getInstance().actionManager.get("save")).setEnabled(true);
                    UIManager.getInstance().refresh();
                }
            } catch (Exception e) {
                e.printStackTrace();
                PassReminder.getInstance().setStatus(e.getMessage());
                QuickMasterDialog.showError(Messages.getString("title.error"), e.getMessage());
                return;
            }
        }
        ((Action) PassReminder.getInstance().actionManager.get("moveToGroup")).run();
    }

    /**
	 * Only for the open at the application start
	 */
    public void openFileAtStart(String f) {
        if (f == null || f.trim().length() == 0) f = Recents.getLastOpenFile();
        openFile(f, false, false);
    }

    private int[] importCSV(String f, char sep, String[] properties, boolean jumpFirstLine) throws IOException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchFieldException, BadDelimiterException, PRException {
        FileInputStream fin = new FileInputStream(f);
        CSVParser cvs = new CSVParser(fin, sep);
        String[][] values = cvs.getAllValues();
        int itemCount = 1;
        int groupCount = 0;
        int start = jumpFirstLine ? 1 : 0;
        for (int i = start; i < values.length; i++) {
            Item item = new Item();
            if (values[i].length < properties.length) throw new PRException(Messages.getString("error.import.bad_file", "" + itemCount), new ArrayIndexOutOfBoundsException());
            for (int j = 0; j < properties.length; j++) {
                String props = properties[j].trim().toLowerCase();
                if (props.equals("group_name")) {
                    Group group = ModelManager.getInstance().toGroup(values[i][j]);
                    if (group == null) {
                        group = ModelManager.getInstance().newGroup(values[i][j]);
                        ModelManager.getInstance().addGroup(ModelManager.groupData, group);
                        groupCount++;
                    }
                    item.groupId = group.id;
                } else {
                    if (props.equals("service")) props = "name";
                    Field widthField;
                    Class c = item.getClass();
                    widthField = c.getField(props);
                    widthField.set(item, values[i][j]);
                }
            }
            item.id = DBManager.nextId();
            UIManager.getInstance().addItem(item);
            itemCount++;
        }
        cvs.close();
        return new int[] { --itemCount, groupCount };
    }

    private void openFile(String fileS, boolean concat, boolean warn) {
        if (fileS == null || fileS.trim().length() == 0) {
            PassReminder.getInstance().setStatus(Messages.getString("open.action.status.no_file"));
            PassReminder.getInstance().refreshTitle();
            return;
        }
        File fileToOpen = new File(fileS);
        boolean msFilenotfound = false;
        if (PassReminder.MEDIA_SUPPORT.equals("MEMORY_STICK")) {
            if (new File(new File(fileS).getName()).exists()) fileToOpen = new File(new File(fileS).getName()); else msFilenotfound = true;
        }
        if (!fileToOpen.exists() || msFilenotfound) {
            QuickMasterDialog.showError(Messages.getString("title.be_careful"), Messages.getString("open.action.filenotfound", fileToOpen.getName()));
            return;
        }
        MasterDialog masterDialog = new MasterDialog(PassReminder.getInstance().getShell(), Messages.getString("open.action.master.prompt", fileToOpen.getName()), false, !PreferenceManager.getInstance().getPreferenceStore().getBoolean(PreferenceConstants.MASTER_SHOW_PASSWORD));
        if (masterDialog.open() == Window.CANCEL) {
            PassReminder.getInstance().setStatus(Messages.getString("action_cancelled"));
            PassReminder.getInstance().refreshTitle();
            return;
        }
        String pass = masterDialog.getConfirmPassword();
        try {
            long openTime = 0;
            if (!concat) {
                DBManager.getInstance().close();
                DBManager.getInstance().setPassword(pass);
                UIManager.getInstance().refresh();
                openTime = DBManager.getInstance().readMain(fileToOpen);
                UIManager.getInstance().refresh();
                ((Action) PassReminder.getInstance().actionManager.get("save")).setEnabled(false);
            }
            Recents.update(fileToOpen.getAbsolutePath());
            if (concat) {
                if (warn) {
                    MessageBox msg = new MessageBox(PassReminder.getInstance().getShell(), SWT.OK | SWT.ICON_WARNING);
                    msg.setText(Messages.getString("title.be_careful"));
                    msg.setMessage(Messages.getString("open.action.warning.if_save", fileToOpen.getAbsolutePath()));
                    msg.open();
                    ((Action) PassReminder.getInstance().actionManager.get("save")).setEnabled(true);
                }
            } else ((Action) PassReminder.getInstance().actionManager.get("save")).setEnabled(false);
            PassReminder.getInstance().setStatus(Messages.getString("open.action.status.ok", DBManager.getInstance().iListMain.size()) + "  -  " + PassReminder.WEBSITE + " - " + openTime + "ms");
            UIManager.getInstance().folderTreeViewer.getTree().setFocus();
            UIManager.getInstance().folderTreeViewer.expandAll();
            PassReminder.getInstance().refreshTitle();
            if (fileS.endsWith("#")) {
                QuickMasterDialog.showWarning(Messages.getString("title.be_careful"), Messages.getString("open.message.warning_backup"));
                ((Action) PassReminder.getInstance().actionManager.get("saveAs")).run();
            }
        } catch (PRException e) {
            e.printStackTrace();
            PassReminder.getInstance().setStatus(e.getMessage());
            if (!fileS.endsWith("#")) {
                MessageBox msg = new MessageBox(PassReminder.getInstance().getShell(), SWT.YES | SWT.NO | SWT.ICON_ERROR);
                msg.setText(Messages.getString("title.error"));
                msg.setMessage(e.getMessage() + "\n" + Messages.getString("open.action.backup", fileS + "#"));
                if (msg.open() == SWT.YES) {
                    openFile(fileS + "#", concat, warn);
                    return;
                }
            } else QuickMasterDialog.showError(Messages.getString("title.error"), e.getMessage());
            UIManager.getInstance().refresh();
            ((Action) PassReminder.getInstance().actionManager.get("save")).setEnabled(true);
        } catch (Exception e) {
            QuickMasterDialog.showError(Messages.getString("title.error"), e.getMessage());
        }
    }

    public void dragEnter(DropTargetEvent event) {
        if (PreferenceManager.getInstance().getPreferenceStore().getBoolean(PreferenceConstants.DND_FILES)) PassReminder.getInstance().setStatus(Messages.getString("open.action.is_dragging")); else PassReminder.getInstance().setStatus(Messages.getString("open.action.drag_disabled"));
    }

    public void dragLeave(DropTargetEvent event) {
        if (PreferenceManager.getInstance().getPreferenceStore().getBoolean(PreferenceConstants.DND_FILES)) PassReminder.getInstance().setStatus(Messages.getString("action_cancelled"));
    }

    public void dragOperationChanged(DropTargetEvent event) {
    }

    public void dragOver(DropTargetEvent event) {
    }

    public void drop(DropTargetEvent event) {
        if (PreferenceManager.getInstance().getPreferenceStore().getBoolean(PreferenceConstants.DND_FILES)) {
            String[] strings = null;
            if (FileTransfer.getInstance().isSupportedType(event.currentDataType)) {
                strings = (String[]) event.data;
                if (strings == null || strings.length == 0) {
                    PassReminder.getInstance().setStatus(Messages.getString("open.action.error.drop"));
                    return;
                }
                openFile(strings[0], false, false);
                for (int i = 1; i < strings.length; i++) {
                    openFile(strings[i], true, i == strings.length - 1);
                }
            }
        } else PassReminder.getInstance().setStatus(PassReminder.WEBSITE);
    }

    public void dropAccept(DropTargetEvent event) {
    }

    public void updateLanguage() {
        if (concat) {
            this.setText(Messages.getString("open.action.add.text"));
            this.setToolTipText(Messages.getString("open.action.add.ttt"));
        } else {
            if (importmode) {
                this.setText(Messages.getString("open.action.import.text"));
                this.setToolTipText(Messages.getString("open.action.import.ttt"));
            } else {
                this.setText(Messages.getString("open.action.text"));
                this.setToolTipText(Messages.getString("open.action.ttt"));
            }
        }
    }
}
