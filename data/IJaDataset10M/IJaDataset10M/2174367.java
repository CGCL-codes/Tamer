package org.adapit.wctoolkit.tool.design;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.adapit.wctoolkit.infrastructure.DefaultApplicationFrame;
import org.adapit.wctoolkit.infrastructure.configuration.DefaultConfigurationLoader;
import org.adapit.wctoolkit.infrastructure.events.actions.DefaultAbstractApplicationAction;
import org.adapit.wctoolkit.infrastructure.events.actions.xmi.ImportXMIAction;
import org.adapit.wctoolkit.infrastructure.events.actions.xml.XmlProjHistoryFileImporter;
import org.adapit.wctoolkit.infrastructure.menu.DefaultEditMenu;
import org.adapit.wctoolkit.infrastructure.menu.DefaultFileMenu;
import org.adapit.wctoolkit.infrastructure.menu.DefaultMenuBar;
import org.adapit.wctoolkit.infrastructure.menu.DefaultViewMenu;
import org.adapit.wctoolkit.infrastructure.util.UIUtil;
import org.adapit.wctoolkit.uml.ext.core.IElement;
import com.l2fprod.common.swing.JTaskPane;
import com.l2fprod.common.swing.JTaskPaneGroup;

public final class WorkCASEApplicationFrame extends DefaultApplicationFrame {

    private static final long serialVersionUID = 13412641461L;

    private JFileChooser fc;

    @SuppressWarnings({ "rawtypes" })
    protected Vector loadedJars = new Vector();

    public WorkCASEApplicationFrame(String title) {
        super(title, false);
        initialize();
        addGlassPane();
    }

    public static WorkCASEApplicationFrame getInstance() {
        return (WorkCASEApplicationFrame) instances.get(WorkCASEApplicationFrame.class);
    }

    @Override
    protected void initialize() {
        super.initialize();
        Image image = getIcon("/icons/imgs/timeicon.png");
        super.setIconImage(image);
        this.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                exit();
                if (exitStatus == ExitStatus.CANCEL) {
                    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                } else {
                    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                }
            }
        });
        List<DefaultAbstractApplicationAction> xmiImportActions = DefaultAbstractApplicationAction.getActionInstances(ImportXMIAction.class);
        if (xmiImportActions != null && xmiImportActions.size() > 0) {
            for (DefaultAbstractApplicationAction aaction : xmiImportActions) {
                if (aaction instanceof ImportXMIAction) {
                    @SuppressWarnings("unused") ImportXMIAction aimp = (ImportXMIAction) aaction;
                }
            }
        } else {
            logger.debug("Import xmi actions is empty");
        }
        setVisible(true);
    }

    @Override
    protected JPanel getButtonPanel() {
        if (buttonPanel == null) {
            buttonPanel = new JPanel();
            buttonPanel.setLayout(new BorderLayout());
            buttonPanel.add(getDefaultToolBar(), BorderLayout.WEST);
            buttonPanel.add(new JPanel(), BorderLayout.CENTER);
        }
        return buttonPanel;
    }

    @Override
    protected DefaultMenuBar getTopMenuBar() {
        if (topMenuBar == null) {
            topMenuBar = new DefaultMenuBar(this);
            topMenuBar.add(getFileMenu());
            topMenuBar.add(new DefaultViewMenu());
            topMenuBar.add(new DefaultEditMenu());
        }
        return topMenuBar;
    }

    private DefaultFileMenu fileMenu;

    private DefaultFileMenu getFileMenu() {
        if (fileMenu == null) {
            fileMenu = new DefaultFileMenu();
        }
        return fileMenu;
    }

    public JFileChooser getFc() {
        if (fc == null) {
            fc = new JFileChooser();
            fc.setLocation(UIUtil.getScreenCenter(fc));
        }
        return fc;
    }

    @SuppressWarnings("serial")
    @Override
    protected JTaskPane getTaskPane() {
        if (taskPane == null) {
            taskPane = new JTaskPane();
            glassPane.add("Center", new JScrollPane(taskPane));
            JTaskPaneGroup group = new JTaskPaneGroup();
            group.setTitle(messages.getMessage("OpenLocalProject"));
            taskPane.add(group);
            Vector<String> filePaths = XmlProjHistoryFileImporter.getInstance().getFilePaths();
            if (filePaths != null) {
                Iterator<String> it = filePaths.iterator();
                while (it.hasNext()) {
                    final String filePath = it.next();
                    if (!DefaultApplicationFrame.getInstance().getOpenedFilesStack().contains(filePath)) DefaultApplicationFrame.getInstance().getOpenedFilesStack().add(filePath);
                    String slash = "\\";
                    if (filePath.indexOf(slash) <= 0) slash = "/";
                    Action groupAction = new AbstractAction(filePath.substring(filePath.lastIndexOf(slash) + 1, filePath.length())) {

                        private ImportXMIAction imp;

                        public void actionPerformed(java.awt.event.ActionEvent e) {
                            if (imp == null) imp = ImportXMIAction.createImportXMIAction(DefaultApplicationFrame.getInstance(), filePath);
                            imp.actionPerformed(e);
                        }
                    };
                    group.add(groupAction);
                }
            }
            super.setHelpInfo(taskPane);
        }
        return taskPane;
    }

    public static void main(String args[]) {
        new WorkCASEApplicationFrame("");
    }

    @SuppressWarnings("unchecked")
    public static TreeMap getLoadedTransformers() {
        return DefaultConfigurationLoader.loadedTransformers;
    }

    @SuppressWarnings("unchecked")
    public static void setLoadedTransformers(TreeMap loadedTransformers) {
        DefaultConfigurationLoader.loadedTransformers = loadedTransformers;
    }

    @Override
    public String getOpenedFile(IElement element) {
        return openedFiles.get(element);
    }
}
