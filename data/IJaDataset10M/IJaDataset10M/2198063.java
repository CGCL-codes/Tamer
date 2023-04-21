package commonapp.widget;

import commonapp.datadef.WidgetDef;
import commonapp.gui.FontAttr;

/**
   This interface defines the basic methods required for a widget-defined
   object.
*/
public interface Widget {

    /**
     Gets the widget definition associated with this widget.

     @return the widget definition or null if no definition has been set.
  */
    WidgetDef getDefinition();

    /**
     Gets the font attribute associated with this widget.

     @return the font attribute or null if no attribute has been set.
  */
    FontAttr getFontAttr();

    /**
     Sets the widget definition associated with this widget.

     @param theDefinition the widget definition.
  */
    void setDefinition(WidgetDef theDefinition);
}
