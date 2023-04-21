package commonapp.widget;

/**
   This interface is used to identify the Widget objects that may be associated
   with a StrucWidget, e.g., a WidgetButtonTable and a WidgetTable.
*/
public interface StrucWidget extends DataWidget {

    /**
     Gets the name of the Struc associated with this widget.

     @return the name of the Struc associated with this widget.
  */
    String getStrucName();
}
