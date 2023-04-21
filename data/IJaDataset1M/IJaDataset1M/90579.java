package royere.cwi.framework.edit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import royere.cwi.framework.RoyereMessage;
import royere.cwi.structure.Element;
import royere.cwi.util.ArrayListHelper;
import org.apache.log4j.*;
import royere.cwi.util.PickData;
import royere.cwi.util.PickEvent;

/**
 * This class represents an open or close of one or more 
 * metanodes. 
 *
 * @author yugen
 */
public class OpenCloseMessage extends EditMessage {

    /** Debug object.  Logs data to various channels. */
    private static Logger logger = Logger.getLogger("royere.cwi.framework.edit.OpenCloseMessage");

    public static final int ACTION_OPEN = 0;

    public static final int ACTION_CLOSE = 1;

    private int action = ACTION_OPEN;

    /** 
     * Constructor
     */
    public OpenCloseMessage(Object source, Object data, int action) {
        super(source, data);
        this.action = action;
    }

    /** 
     * Constructor  
     */
    public OpenCloseMessage(PickEvent pe, int action) {
        this(pe.getSource(), pe.getData(), action);
    }

    public int getAction() {
        return action;
    }

    public PickData getPickData() {
        return (PickData) getData();
    }

    public void setPickData(PickData pd) {
        this.data = pd;
    }

    public RoyereMessage[] getAutomaticInverse() {
        OpenCloseMessage inv = new OpenCloseMessage(this.source, this.data, opposite(this.action));
        inv.setGraph(getGraph());
        return new RoyereMessage[] { inv };
    }

    /**
     * Remove any elements from our PickData that will not change
     * status as a result of executing this message.  For example,
     * if a metanode is already closed, and this message's action
     * would close the metanode, then we don't want to include it in the
     * PickData; if we do, then Edit->Undo will open it.  
     *
     * @exception NoApplicableElementsException if every element in the PickData
     * would be unaffected by executing this message.
     */
    public void discardUnchangedElements(ArrayList unchangedElements) throws NoApplicableElementsException {
        PickData pd = getPickData();
        if (pd == null) {
            throw new NoApplicableElementsException();
        }
        pd.removeElements(unchangedElements);
        if (pd.toElementArray().length == 0) {
            throw new NoApplicableElementsException();
        }
    }

    /**
     * Open -> Close
     * Close -> Open
     */
    public int opposite(int action) {
        switch(action) {
            case ACTION_OPEN:
                return ACTION_CLOSE;
            case ACTION_CLOSE:
                return ACTION_OPEN;
        }
        logger.log(Priority.ERROR, "opposite(): Unrecognized action");
        return 0;
    }

    public String toCommandString() {
        return buildCommandString(getActionString(), getPickData());
    }

    public String toString() {
        String s = "[" + getActionString() + "]";
        s += " " + super.toString();
        return s;
    }

    public String getActionString() {
        switch(action) {
            case ACTION_OPEN:
                return "OPEN";
            case ACTION_CLOSE:
                return "CLOSE";
        }
        throw new RuntimeException("Unknown action");
    }

    /**
     * Short description of this edit action.    
     *
     * @see royere.cwi.appl.Feedback
     */
    public String feedbackSynopsis() {
        switch(action) {
            case ACTION_OPEN:
                return "Opening";
            case ACTION_CLOSE:
                return "Closing";
        }
        return super.feedbackSynopsis();
    }
}
