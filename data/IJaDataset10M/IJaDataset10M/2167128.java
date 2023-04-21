package commonapp.widget;

import commonapp.datadef.WidgetDef;
import commonapp.gui.FontAttr;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
   This is the runtime instance class of a text text area label widget.
*/
public class WidgetTextAreaLabel extends JTextArea implements Widget {

    /** Serialization ID. */
    private static final long serialVersionUID = 0L;

    /** Widget definition. */
    private WidgetDef myDefinition = null;

    /** Font used for rendering. */
    private static FontAttr ourFontAttr = FontAttr.BASE;

    /**
     Constructs a new WidgetTextAreaLabel.
  */
    public WidgetTextAreaLabel() {
    }

    /**
     Gets the font attribute associated with this widget.  This method is part
     of the {@link Widget} interface.

     @return the font attribute or null if no attribute has been set.
  */
    public FontAttr getFontAttr() {
        return ourFontAttr;
    }

    /**
     Gets the widget definition associated with this widget.  This method is
     part of the {@link Widget} interface.

     @return the widget definition or null if no definition has been set.
  */
    public WidgetDef getDefinition() {
        return myDefinition;
    }

    /**
     Sets the widget definition associated with this widget.  This method is
     part of the {@link Widget} interface.

     @param theDefinition the widget definition.
  */
    public void setDefinition(WidgetDef theDefinition) {
        myDefinition = theDefinition;
        String key = myDefinition.getAttr(WidgetDef.A_MESSAGE_KEY);
        String text = "";
        if (key == null || key.equals("")) {
            text = myDefinition.getAttr(WidgetDef.A_TEXT);
        } else {
        }
        JPanel p = new JPanel();
        setBackground(p.getBackground());
        setEditable(false);
        setLineWrap(true);
        setWrapStyleWord(true);
        setText(text.trim());
        setCaretPosition(0);
        WidgetSupport.main.setAttr(this, WidgetSupport.WIDGET_WIDTH, 1.6);
    }
}
