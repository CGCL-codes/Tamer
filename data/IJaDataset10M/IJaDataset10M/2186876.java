package tr.data;

import au.com.thinkingrock.tr.resource.Resource;
import java.awt.Component;
import java.awt.EventQueue;
import java.io.File;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import org.openide.windows.WindowManager;
import tr.appl.Constants;
import tr.datastore.DataStore;
import tr.datastore.DataStoreLookup;
import tr.datastore.xstream.XStreamDataStore;
import tr.model.Data;
import tr.model.DataLookup;
import tr.util.Utils;
import tr.util.UtilsFile;

/**
 * SaveAs action.
 *
 * @author Jeremy Moore (jimoore@netspace.net.au)
 */
public final class SaveAsAction extends CallableSystemAction {

    private static final Logger LOG = Logger.getLogger("tr.data");

    /** Constructs a new instance. */
    public SaveAsAction() {
        super();
        enableDisable();
        Lookup.Result r = DataLookup.instance().lookup(new Lookup.Template(Data.class));
        r.addLookupListener(new LookupListener() {

            public void resultChanged(LookupEvent lookupEvent) {
                enableDisable();
            }
        });
    }

    @Override
    protected String iconResource() {
        return Resource.DataSaveAs;
    }

    private void enableDisable() {
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                Data data = (Data) DataLookup.instance().lookup(Data.class);
                setEnabled(data != null);
            }
        });
    }

    /** Save the current datastore as another file. */
    public void performAction() {
        DataStore ds = (DataStore) DataStoreLookup.instance().lookup(DataStore.class);
        if (ds == null) {
            LOG.severe("Datastore was not found.");
            return;
        }
        saveData(ds);
        JFileChooser chooser = new JFileChooser();
        String[] extns = XStreamDataStore.FILE_EXTENSIONS;
        FileFilter filter = new FileFilterImpl(org.openide.util.NbBundle.getMessage(SaveAsAction.class, "tr.xstream.datafiles"), extns, true);
        chooser.setFileFilter(filter);
        chooser.setSelectedFile(new File(ds.getPath()));
        Component p = WindowManager.getDefault().getMainWindow();
        int returnVal = chooser.showDialog(p, org.openide.util.NbBundle.getMessage(SaveAsAction.class, "save.as"));
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getPath();
            String extn = UtilsFile.getExtension(path);
            if (!Utils.in(extn, extns)) {
                path = UtilsFile.setExtension(path, "trx");
            }
            File file = new File(path);
            if (file.exists()) {
                String t = org.openide.util.NbBundle.getMessage(SaveAsAction.class, "save.as");
                String m = org.openide.util.NbBundle.getMessage(SaveAsAction.class, "confirm.replace.file");
                int r = JOptionPane.showConfirmDialog(p, m, t, JOptionPane.YES_NO_OPTION);
                if (r != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            LOG.info("Save As: " + path);
            ds.setPath(path);
            ds.setChanged(true);
            saveData(ds);
            WindowManager.getDefault().getMainWindow().setTitle(Constants.TITLE + " " + path);
        }
    }

    private void saveData(DataStore ds) {
        try {
            ds.store();
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
    }

    public String getName() {
        return NbBundle.getMessage(SaveAction.class, "CTL_SaveAsAction");
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}
