package net.sf.pim.mail;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import jp.gr.java_conf.roadster.net.pop.MessageIndex;
import net.sf.component.config.ConfigHelper;
import net.sf.pim.mail.reader.FolderViewer;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class MailPlugin extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "net.sf.mywork.mail";

    private static MailPlugin plugin;

    public static final String MAIL_INDEX_FILE = "mail.idx";

    public static final String MAIL_READ_FILE = "read.idx";

    private StoreFacade store;

    public static final String INBOX = "INBOX";

    public static final String OUTBOX = "OUTBOX";

    public static final String TRASHBOX = "TRASHBOX";

    public static final String ARCHIEBOX = "ARCHIEBOX";

    public static final String SEARCHBOX = "SEARCHBOX";

    public static final String INBOX_NAME = "收件箱";

    public static final String OUTBOX_NAME = "发件箱";

    public static final String TRASHBOX_NAME = "垃圾箱";

    public static final String ARCHIEBOX_NAME = "档案箱";

    public static final String SEARCHBOX_NAME = "搜索箱";

    private static Map<String, String> folderName;

    static {
        folderName = new HashMap<String, String>(10);
        folderName.put(MailPlugin.INBOX, MailPlugin.INBOX_NAME);
        folderName.put(MailPlugin.OUTBOX, MailPlugin.OUTBOX_NAME);
        folderName.put(MailPlugin.TRASHBOX, MailPlugin.TRASHBOX_NAME);
        folderName.put(MailPlugin.ARCHIEBOX, MailPlugin.ARCHIEBOX_NAME);
        folderName.put(MailPlugin.SEARCHBOX, MailPlugin.SEARCHBOX_NAME);
        folderName.put(MailPlugin.INBOX_NAME, MailPlugin.INBOX);
        folderName.put(MailPlugin.OUTBOX_NAME, MailPlugin.OUTBOX);
        folderName.put(MailPlugin.TRASHBOX_NAME, MailPlugin.TRASHBOX);
        folderName.put(MailPlugin.ARCHIEBOX_NAME, MailPlugin.ARCHIEBOX);
        folderName.put(MailPlugin.SEARCHBOX_NAME, MailPlugin.SEARCHBOX);
    }

    /**
	 * The constructor
	 */
    public MailPlugin() {
        plugin = this;
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);
    }

    public void loadStoreAndCache() {
        try {
            store = new StoreFacade(ConfigHelper.getStringArrayProperty("mail.address"), ConfigHelper.getStringArrayProperty("mail.pop3"), ConfigHelper.getStringArrayProperty("mail.smtp"));
            {
                HashMap headersMap = new HashMap();
                FileInputStream fis = new FileInputStream(ConfigHelper.getDataHome() + "/data/mailbox/" + MAIL_INDEX_FILE);
                ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(fis, 1024));
                headersMap = (HashMap) ois.readObject();
                ois.close();
                MessageIndex.setHEADERS_MAP(headersMap);
            }
            {
                HashSet readSet = new HashSet();
                FileInputStream fis = new FileInputStream(ConfigHelper.getDataHome() + "/data/mailbox/" + MAIL_READ_FILE);
                ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(fis, 1024));
                readSet = (HashSet) ois.readObject();
                ois.close();
                FolderViewer.setReadSet(readSet);
            }
        } catch (FileNotFoundException fex) {
            ;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop(BundleContext context) throws Exception {
        plugin = null;
        store.close();
        HashMap headersMap = MessageIndex.getHEADERS_MAP();
        if (!headersMap.isEmpty()) {
            FileOutputStream fos = new FileOutputStream(ConfigHelper.getDataHome() + "/data/mailbox/" + MAIL_INDEX_FILE);
            ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(fos, 1024));
            oos.writeObject(headersMap);
            oos.close();
        }
        Set readSet = FolderViewer.getReadSet();
        if (!readSet.isEmpty()) {
            FileOutputStream fos = new FileOutputStream(ConfigHelper.getDataHome() + "/data/mailbox/" + MAIL_READ_FILE);
            ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(fos, 1024));
            oos.writeObject(readSet);
            oos.close();
        }
        super.stop(context);
    }

    /**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
    public static MailPlugin getDefault() {
        return plugin;
    }

    /**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }

    public StoreFacade getStore() {
        return store;
    }

    public static String convertFolderName(String name) {
        return folderName.get(name);
    }
}
