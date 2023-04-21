package net.sf.gwoc.provider;

import java.text.DateFormat;
import net.sf.gwoc.client.Application;
import net.sf.gwoc.gwapi.GWSoapHelper;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import com.novell.groupwise.ws.Mail;

public class MsgListViewLabelProvider extends LabelProvider implements ITableLabelProvider {

    public String getColumnText(Object obj, int index) {
        Mail m = (Mail) obj;
        try {
            if (index == 0) {
                return GWSoapHelper.getName(m);
            }
            if (index == 1) return m.getSubject();
            if (index == 2) return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(m.getDelivered().toGregorianCalendar().getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public Image getColumnImage(Object obj, int index) {
        if (index == 0) return getImage(obj); else return null;
    }

    public Image getImage(Object obj) {
        if (obj instanceof Mail) {
            Mail mail = (Mail) obj;
            if (mail.getStatus().isOpened()) return AbstractUIPlugin.imageDescriptorFromPlugin(Application.class.getPackage().getName(), "icons/mailOpened.gif").createImage(); else return AbstractUIPlugin.imageDescriptorFromPlugin(Application.class.getPackage().getName(), "icons/mail.gif").createImage();
        }
        return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
    }
}
