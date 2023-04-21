package org.columba.mail.resourceloader;

import javax.swing.ImageIcon;
import org.columba.core.resourceloader.ImageLoader;

public class MailImageLoader {

    public static ImageIcon getIcon(String name) {
        return ImageLoader.getIcon("org/columba/mail/icons", name, false);
    }

    public static ImageIcon getSmallIcon(String name) {
        return ImageLoader.getIcon("org/columba/mail/icons", name, true);
    }
}
