package passreminder.ui;

import java.net.InetAddress;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import passreminder.Util;
import passreminder.model.SyncFile;

public class SyncFileTableLabelProvider implements ITableLabelProvider {

    public String getColumnText(Object obj, int i) {
        return ((SyncFile) obj).filename;
    }

    public void addListener(ILabelProviderListener ilabelproviderlistener) {
    }

    public void dispose() {
    }

    public boolean isLabelProperty(Object obj, String s) {
        return false;
    }

    public void removeListener(ILabelProviderListener ilabelproviderlistener) {
    }

    public Image getColumnImage(Object obj, int num) {
        try {
            String hostname = InetAddress.getLocalHost().getHostName();
            if (((SyncFile) obj).hostname.equals(hostname)) return Util.getImageRegistry().get("OK");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Util.getImageRegistry().get("KO");
    }
}
