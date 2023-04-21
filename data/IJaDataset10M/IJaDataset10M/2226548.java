package commonapp.gui;

import java.util.ArrayList;

/**
   This interface defines the methods required of tree node user objects and
   the events used by those objects.
*/
public interface TreeDataNode extends SEventHandler {

    /**
     Sent to a TreeDataNode to request the IWindow be closed.
     Arguments:
     <ul>
     <li>0 - The IWindow to be closed.
     </ul>
  */
    public static final SEvent.Type CLOSE_WINDOW = new SEvent.Type("WINDOW.CLOSE", 1, IWindow.class);

    /**
     Sent to a TreeDataNode when the user requests the internal window be
     closed using the close window button.  If this event IS NOT consumed,
     the window closing operation proceeds.

     Arguments:
     <ul>
     <li>0 - The IWindow to be closed.
     </ul>
  */
    public static final SEvent.Type USER_CLOSE_WINDOW = new SEvent.Type("USER.WINDOW.CLOSE", 1, IWindow.class);

    /**
     Sent from IWindow to a TreeDataNode to request a node be selected.
     Arguments:
     <ul>
     <li>0 - The IWindow that wraps the window making the request.
     </ul>
  */
    public static final SEvent.Type SELECT_NODE = new SEvent.Type("NODE.SELECT", 1, IWindow.class);

    /**
     Sent from ITree to a TreeDataNode to indicate a node has been selected.
     This event requires no arguments.
  */
    public static final SEvent.Type SELECTED_NODE = new SEvent.Type("NODE.SELECTED", 0);

    /**
     Sent to a TreeDataNode to request the TOC file be updated.
     This event requires no arguments.
  */
    public static final SEvent.Type UPDATE_TOC = new SEvent.Type("UPDATE_TOC", 0);

    /**
     Sent from IWindow to a TreeDataNode to signal that a new window was opened.
     Arguments:
     <ul>
     <li>0 - The IWindow that wraps the window opened.
     </ul>
  */
    public static final SEvent.Type WINDOW_OPENED = new SEvent.Type("WINDOW.OPENED", 1, IWindow.class);

    /**
     Sent from IWindow to a TreeDataNode to signal that the window was closed.
     Arguments:
     <ul>
     <li>0 - The IWindow that wraps the window closed.
     </ul>
  */
    public static final SEvent.Type WINDOW_CLOSED = new SEvent.Type("WINDOW.CLOSED", 1, IWindow.class);

    /**
     Sets the ITree node to be associated with this data node.

     @param theNode the ITree node to be associated with this data node.
  */
    public void setITreeNode(ITreeNode theNode);

    /**
     Gets the ITree node associated with this data node.

     @return the ITree node associated with this data node.
  */
    public ITreeNode getITreeNode();

    /**
     Gets a list of menu actions that are currently disabled for this
     element.

     @return a list of menu action that are currently disabled for this
     element.
  */
    public DisableList getDisableList();

    /**
     Gets the node type.  The returned value is used to limit of multiselect
     tree operation, i.e., only nodes with the same type value as the first
     selection will be operated on.  The returned value will be used in the
     multiselection confirm dialog, when needed.

     @return a node type, a String representation of the type of data
     encapsulated by the node, e.g., a workbook, sitelist, project, highway,
     intersection, etc.
  */
    public String getNodeType();

    /**
     Gets the number of sub-nodes contained within this user node.

     @return the number of sub-nodes contained with this user node.
  */
    public int getSubNodeCount();

    /**
     Gets the list of user sub-nodes contained within this user node.

     @return the list of sub-nodes contained with this user node.
  */
    public ArrayList<TreeDataNode> getSubNodes();

    /**
     Gets the value of an attribute of the user node associated with the
     specified attribute name.

     @param theAttrName the attribute name.

     @return the attribute value or an empty string if the attribute has no
     value.
  */
    public String getAttr(String theAttrName);

    /**
     Gets the value of the node tool tip text for the tree.

     @return the value of the tree node tool tip text.
  */
    public String getNodeToolTip();

    /**
     Gets the name of a menu definition for the current pop-up menu item for
     the node.

     @return the name of a menu definition for the pop-up menu items.
  */
    public String getMenuName();

    /**
     Gets the name of the icon used to render the node.

     @return the name of an icon used to render the node.
  */
    public String getIconName();
}
