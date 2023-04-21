package xml;

import javax.swing.*;
import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.Rectangle;
import xml.completion.*;
import xml.completion.ElementDecl.AttributeDecl;

/** displays an icon corresponding to the kind of item and a preview for entities. */
public class XmlListCellRenderer extends JPanel implements ListCellRenderer {

    public static final XmlListCellRenderer INSTANCE = new XmlListCellRenderer();

    public static final ImageIcon COMMENT_ICON = new ImageIcon(XmlListCellRenderer.class.getResource("/xml/Comment.png"));

    public static final ImageIcon CDATA_ICON = new ImageIcon(XmlListCellRenderer.class.getResource("/xml/CDATA.png"));

    public static final ImageIcon ELEMENT_ICON = new ImageIcon(XmlListCellRenderer.class.getResource("/xml/Element.png"));

    public static final ImageIcon EMPTY_ELEMENT_ICON = new ImageIcon(XmlListCellRenderer.class.getResource("/xml/EmptyElement.png"));

    public static final ImageIcon INTERNAL_ENTITY_ICON = new ImageIcon(XmlListCellRenderer.class.getResource("/xml/InternalEntity.png"));

    public static final ImageIcon EXTERNAL_ENTITY_ICON = new ImageIcon(XmlListCellRenderer.class.getResource("/xml/ExternalEntity.png"));

    public static final ImageIcon ID_ICON = new ImageIcon(XmlListCellRenderer.class.getResource("/xml/ID.png"));

    private DefaultListCellRenderer left;

    private DefaultListCellRenderer right;

    public XmlListCellRenderer() {
        left = new DefaultListCellRenderer();
        right = new DefaultListCellRenderer();
        setLayout(new BorderLayout());
        right.setHorizontalAlignment(SwingConstants.TRAILING);
        add(left, BorderLayout.LINE_START);
        add(right, BorderLayout.LINE_END);
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        left.getListCellRendererComponent(list, null, index, isSelected, cellHasFocus);
        right.getListCellRendererComponent(list, null, index, isSelected, cellHasFocus);
        setBackground(left.getBackground());
        setBorder(left.getBorder());
        left.setBorder(null);
        right.setBorder(null);
        if (value instanceof Comment) {
            left.setIcon(COMMENT_ICON);
            left.setText("!--");
        } else if (value instanceof CDATA) {
            left.setIcon(CDATA_ICON);
            left.setText("![CDATA[");
        } else if (value instanceof ClosingTag) {
            left.setIcon(ELEMENT_ICON);
            left.setText("/" + ((ClosingTag) value).name);
        } else if (value instanceof ElementDecl) {
            ElementDecl element = (ElementDecl) value;
            left.setIcon(element.empty ? EMPTY_ELEMENT_ICON : ELEMENT_ICON);
            left.setText(element.name);
        } else if (value instanceof AttributeDecl) {
            AttributeDecl ad = (AttributeDecl) value;
            left.setText(ad.name);
        } else if (value instanceof EntityDecl) {
            EntityDecl entity = (EntityDecl) value;
            left.setIcon(entity.type == EntityDecl.INTERNAL ? INTERNAL_ENTITY_ICON : EXTERNAL_ENTITY_ICON);
            String entityValue = entity.value;
            if (entityValue == null) {
                entityValue = entity.publicId;
                if (entityValue == null) entityValue = entity.systemId;
            }
            if (entityValue == null) {
                right.setText("");
            } else {
                if (entityValue.length() > 60) {
                    entityValue = entityValue.substring(0, 30) + "..." + entityValue.substring(entityValue.length() - 20);
                }
                right.setText("    " + entityValue);
            }
            left.setText(entity.name);
        } else if (value instanceof IDDecl) {
            left.setIcon(ID_ICON);
            left.setText(value.toString());
        } else {
            left.setText(value.toString());
            left.setIcon(null);
        }
        return this;
    }

    /** @return	the name of the tag, comment, etc.*/
    public String getMainText() {
        return left.getText();
    }

    /** @return	the preview of the entity or empty string */
    public String getValueText() {
        return right.getText();
    }

    /** Overridden for performance reasons. */
    public void repaint() {
    }

    /** Overridden for performance reasons. */
    public void repaint(long tm, int x, int y, int width, int height) {
    }

    /** Overridden for performance reasons. */
    public void repaint(Rectangle r) {
    }

    /** Overridden for performance reasons. */
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
    }

    /** Overridden for performance reasons. */
    public void firePropertyChange(String propertyName, byte oldValue, byte newValue) {
    }

    /** Overridden for performance reasons. */
    public void firePropertyChange(String propertyName, char oldValue, char newValue) {
    }

    /** Overridden for performance reasons. */
    public void firePropertyChange(String propertyName, short oldValue, short newValue) {
    }

    /** Overridden for performance reasons. */
    public void firePropertyChange(String propertyName, int oldValue, int newValue) {
    }

    /** Overridden for performance reasons. */
    public void firePropertyChange(String propertyName, long oldValue, long newValue) {
    }

    /** Overridden for performance reasons. */
    public void firePropertyChange(String propertyName, float oldValue, float newValue) {
    }

    /** Overridden for performance reasons. */
    public void firePropertyChange(String propertyName, double oldValue, double newValue) {
    }

    /** Overridden for performance reasons. */
    public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
    }

    public static class ClosingTag {

        public String name;

        public ClosingTag(String name) {
            this.name = name;
        }
    }

    public static class Comment {
    }

    public static class CDATA {
    }
}
