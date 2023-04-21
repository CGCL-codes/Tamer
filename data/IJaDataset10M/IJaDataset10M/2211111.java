package ldap;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.swing.ImageIcon;
import org.wings.SComponent;
import org.wings.STable;
import org.wings.externalizer.ExternalizeManager;

public class AttributesCellRenderer extends SelectableTableCellRenderer {

    public SComponent getTableCellRendererComponent(STable table, Object value, boolean isSelected, int row, int col) {
        if (value instanceof Attribute) {
            Attribute attribute = (Attribute) value;
            try {
                if (attribute.get().getClass().getName().equals("[B")) {
                    if (attribute.getID().equals("jpegPhoto")) {
                        String src = null;
                        ImageIcon icon = new ImageIcon((byte[]) attribute.get());
                        ExternalizeManager ext = table.getSession().getExternalizeManager();
                        if (ext != null) {
                            src = ext.externalize(icon);
                        }
                        if (src != null) {
                            StringBuffer buffer = new StringBuffer();
                            buffer.append("<img src=\"").append(src).append("\"");
                            buffer.append(" width=\"").append(icon.getIconWidth()).append("\"");
                            buffer.append(" height=\"").append(icon.getIconHeight()).append("\"");
                            buffer.append(" border=\"0\" />");
                            value = buffer.toString();
                        } else value = "-";
                    } else if (attribute.getID().equals("userPassword")) {
                        value = "-";
                    }
                } else {
                    StringBuffer buffer = new StringBuffer();
                    for (int i = 0; i < attribute.size(); i++) {
                        if (i > 0) buffer.append(", ");
                        buffer.append(attribute.get(i).toString());
                    }
                    value = buffer.toString();
                }
            } catch (NamingException e) {
                System.err.println(e.getMessage());
                e.printStackTrace(System.err);
            }
        }
        return super.getTableCellRendererComponent(table, value, isSelected, row, col);
    }
}
