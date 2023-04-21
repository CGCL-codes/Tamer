package commonapp.gui;

import commonapp.datacon.Struc;
import java.awt.Container;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;

/**
   This class implements a data panel for an application frame.
*/
public class IDataPanel extends BaseHandler {

    /** GUI component that contains the panel data. */
    private JPanel myPanel;

    /**
     Constructs a new IDataPanel.

     <p>This version of the constructor is for panels that do not use an event
     handler OR for panels for which the event handler is specified in the data
     dictionary entry for the panel.

     @param thePanel the container for the data.

     @param theData the data for the panel.
  */
    public IDataPanel(JPanel thePanel, Struc theData) {
        myPanel = thePanel;
        PanelActionHandler.set(theData, thePanel);
    }

    /**
     Constructs a new IDataPanel.

     <p>This version of the constructor is for panels for which the event
     handler is specified explicitly.

     @param thePanel the container for the data.

     @param theData the data for the panel.

     @param theHandler the {@link SEventHandler} for the panel.
  */
    public IDataPanel(JPanel thePanel, Struc theData, SEventHandler theHandler) {
        myPanel = thePanel;
        PanelActionHandler.set(theData, thePanel, theHandler);
    }

    /**
     Gets the GUI component associated with this object as a Container.  This
     method is required by the {@link BaseHandler} base class.

     @return the GUI Container associated with this object.
  */
    @Override
    public Container getContainer() {
        return myPanel;
    }

    /**
     Forwards the specified action event to the event handler associated with
     this panel.

     @param theEvent the {@link ActionEvent} to be performed.

     @return true if the event was handled; false otherwise.
  */
    public boolean performAction(ActionEvent theEvent) {
        boolean handled = false;
        PanelActionHandler handler = PanelActionHandler.getHandler(myPanel);
        if (handler != null) {
            String command = theEvent.getActionCommand();
            handled = handler.performAction(command);
        }
        return handled;
    }
}
