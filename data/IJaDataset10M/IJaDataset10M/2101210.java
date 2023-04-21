package org.columba.mail.gui.table.plugins;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import org.columba.core.resourceloader.ImageLoader;
import org.columba.mail.gui.table.model.MessageNode;
import org.columba.mail.message.ColumbaHeader;
import org.columba.mail.resourceloader.MailImageLoader;
import org.columba.mail.util.MailResourceLoader;
import org.columba.ristretto.message.Flags;

public class StatusRenderer extends DefaultLabelRenderer {

    boolean bool;

    ImageIcon image2;

    ImageIcon image3;

    ImageIcon image5;

    ImageIcon image6;

    ImageIcon image7;

    public StatusRenderer() {
        super();
        setHorizontalAlignment(SwingConstants.CENTER);
        image2 = MailImageLoader.getSmallIcon("message-mail-replied.png");
        image3 = ImageLoader.getSmallIcon("user-trash.png");
        image5 = MailImageLoader.getSmallIcon("message-mail-read.png");
        image6 = MailImageLoader.getSmallIcon("message-mail-unread.png");
        image7 = MailImageLoader.getSmallIcon("edit.png");
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (value == null) {
            setIcon(null);
            return this;
        }
        setText("");
        if (value instanceof String) {
            System.out.println("statusrenderer-> instanceof String not expected");
            return this;
        }
        Flags flags = ((ColumbaHeader) ((MessageNode) value).getHeader()).getFlags();
        if (flags.getDeleted()) {
            setIcon(image3);
            setToolTipText(MailResourceLoader.getString("header", "column", "expunged"));
        } else if (flags.getAnswered()) {
            setIcon(image2);
            setToolTipText(MailResourceLoader.getString("header", "column", "answered"));
        } else if (flags.getDraft()) {
            setIcon(image7);
            setToolTipText(MailResourceLoader.getString("header", "column", "draft"));
        } else if (!flags.getSeen()) {
            setIcon(image6);
            setToolTipText(MailResourceLoader.getString("header", "column", "unread"));
        } else if (flags.getSeen()) {
            setIcon(image5);
            setToolTipText(MailResourceLoader.getString("header", "column", "read"));
        } else {
            setIcon(null);
        }
        return this;
    }
}
