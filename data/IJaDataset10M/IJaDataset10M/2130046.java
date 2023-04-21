package commonapp.widget;

import common.log.Log;
import common.log.LogMessageType;
import commonapp.gui.IconFactory;
import commonapp.gui.MessageManager;
import javax.swing.ImageIcon;

/**
   This is a table row status object used to render table row status.
*/
public class RowStatus {

    /** Information-only status. */
    public static final LogMessageType INFO = Log.INFO;

    /** Warning status. */
    public static final LogMessageType WARNING = Log.WARNING;

    /** Error status. */
    public static final LogMessageType ERROR = Log.ERROR;

    /** Verbose status. */
    public static final LogMessageType VERBOSE = Log.VERBOSE;

    /** Program fault status. */
    public static final LogMessageType FAULT = Log.FAULT;

    /** Message type. */
    private LogMessageType myType = null;

    /** Status title. */
    private String myTitle = null;

    /** Status tip and message title for cell edit pop-up. */
    private String myTip = null;

    /** Status message key for cell edit pop-up. */
    private String myMessageKey = null;

    /** Status icon cell renderer. */
    private ImageIcon myIcon = null;

    /** Status message for cell edit pop-up. */
    private String myMessage = null;

    /**
     Constructs a new RowStatus.

     @param theType the status type.

     @param theTitle the status title used as a sort key.

     @param theTip the status tip and pop-up title.

     @param theIconName the cell rendered/cell editor button icon name.

     @param theMessageKey the pop-up message key.
  */
    public RowStatus(LogMessageType theType, String theTitle, String theTip, String theIconName, String theMessageKey) {
        this(theType, theTitle, theTip, IconFactory.main.getIcon(theIconName, IconFactory.SIZE_TREE), theMessageKey);
    }

    /**
     Private constructor.  Used by getRowStatus.

     @param theType the status type.

     @param theTitle the status title used as a sort key.

     @param theTip the status tip and pop-up title.

     @param theIcon the cell rendered/cell editor button icon.

     @param theMessageKey the pop-up message key.
  */
    private RowStatus(LogMessageType theType, String theTitle, String theTip, ImageIcon theIcon, String theMessageKey) {
        myType = theType;
        myTitle = theTitle;
        myTip = theTip;
        myIcon = theIcon;
        myMessageKey = theMessageKey;
    }

    /**
     Gets a new row status with a specific status message.

     @param theMessage the status message.
  */
    public RowStatus getRowStatus(String theMessage) {
        RowStatus status = new RowStatus(myType, myTitle, myTip, myIcon, myMessageKey);
        status.myMessage = theMessage;
        return status;
    }

    /**
     Returns the status title.

     @return the status title.
  */
    @Override
    public String toString() {
        return myTitle;
    }

    /**
     Returns the cell editor/renderer icon.

     @return the cell editor/renderer icon.
  */
    public ImageIcon getIcon() {
        return myIcon;
    }

    /**
     Returns the pop-up message key.

     @return the pop-up message key.
  */
    public String getMessageKey() {
        return myMessageKey;
    }

    /**
     Returns the pop-up message title.

     @return the pop-up message title/renderer tooltip.
  */
    public String getTip() {
        return myTip;
    }

    public void println() {
        Log.main.println(myType, getMessage());
    }

    /**
     Displays the pop-up message.
  */
    public void displayMessage() {
        MessageManager.main.displayMessage(getMessageKey(), getTip(), getMessage());
    }

    /**
     Returns the pop-up message.

     @return the pop-up message.
  */
    public String getMessage() {
        String message = myMessage;
        if (message == null) {
            message = myTip;
            if (message == null) {
                message = myTitle;
            }
        }
        return message;
    }
}
