package org.rendezvous;

import javax.swing.ImageIcon;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import java.applet.Applet;
import java.awt.Component;
import java.awt.Container;
import org.datashare.objects.DefaultObjectInfo;
import org.datashare.objects.DSObjectInfoInterface;
import org.datashare.SessionUtilities;

/**
 * Responsible for providing the images we use with our tree structure that contains Special
 * clients, Sessions, Channels, and Consumers.
 *
 * @author Charles Wood
 * @version 1.0
 */
public class SMRenderer extends DefaultTreeCellRenderer {

    /**
    * the image to use for the Session
    *
    */
    ImageIcon sessionIcon;

    /**
    * the image to use for the Session that are private
    *
    */
    ImageIcon sessionPrivateIcon;

    /**
    * the image to use for the Client
    *
    */
    ImageIcon clientIcon;

    /**
    * the image to use for the Channel
    *
    */
    ImageIcon channelIcon;

    /**
    * The root image
    *
    */
    ImageIcon collaborationIcon;

    /**
    * the image to use for the node that contains all the Sessions
    *
    */
    ImageIcon allSessionsIcon;

    /**
    * the image to use for the node that contains all the SpecialClients
    *
    */
    ImageIcon specialClientsIcon;

    /**
    * the image to use for Consumers who are active in a channel
    *
    */
    ImageIcon consumerIcon;

    /**
    * the image to use for Consumers who are not active in a channel
    *
    */
    ImageIcon consumerInactiveIcon;

    /**
    * set to true if no Exceptions were noted while loading (not always reliable)
    *
    */
    boolean iconLoadingSucceeded = false;

    /**
    * Constructor, loads all the images only
    *
    * @param frame used to provide a resource loader to load the images
    */
    public SMRenderer(Container frame) {
        try {
            allSessionsIcon = SessionUtilities.getImageIcon(frame, SessionUtilities.imageRoot + "AllSessions.gif");
            specialClientsIcon = SessionUtilities.getImageIcon(frame, SessionUtilities.imageRoot + "People.gif");
            sessionIcon = SessionUtilities.getImageIcon(frame, SessionUtilities.imageRoot + "sessions.gif");
            clientIcon = SessionUtilities.getImageIcon(frame, SessionUtilities.imageRoot + "client.gif");
            channelIcon = SessionUtilities.getImageIcon(frame, SessionUtilities.imageRoot + "Channels.gif");
            collaborationIcon = SessionUtilities.getImageIcon(frame, SessionUtilities.imageRoot + "collaboration.gif");
            consumerIcon = SessionUtilities.getImageIcon(frame, SessionUtilities.imageRoot + "client.gif");
            consumerInactiveIcon = SessionUtilities.getImageIcon(frame, SessionUtilities.imageRoot + "clientFaded.gif");
            sessionPrivateIcon = SessionUtilities.getImageIcon(frame, SessionUtilities.imageRoot + "privateSession.gif");
            iconLoadingSucceeded = true;
        } catch (Exception e) {
            System.out.println("could not load icon files");
            e.printStackTrace();
        }
    }

    /**
    * this method will set the icon to the correct image once the node type has been determined
    *
    * @param tree supplied by calling instance
    * @param value supplied by calling instance
    * @param sel supplied by calling instance
    * @param expanded supplied by calling instance
    * @param leaf supplied by calling instance
    * @param row supplied by calling instance
    * @param hasFocus supplied by calling instance
    * @return component specific for the specified node
    */
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        if (iconLoadingSucceeded) {
            if (isItThisType(value, DSObjectInfoInterface.CLIENTTYPE)) setIcon(clientIcon); else if (isItPrivateSession(value)) setIcon(sessionPrivateIcon); else if (isItThisType(value, DSObjectInfoInterface.SESSIONTYPE)) setIcon(sessionIcon); else if (isItThisType(value, DSObjectInfoInterface.CHANNELTYPE)) setIcon(channelIcon); else if (isItThisType(value, DSObjectInfoInterface.CONSUMERTYPE)) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                DefaultObjectInfo nodeInfo = (DefaultObjectInfo) (node.getUserObject());
                if (nodeInfo.getActive()) setIcon(consumerIcon); else setIcon(consumerInactiveIcon);
            } else if (isItThisType(value, DSObjectInfoInterface.ALLSESSIONS)) setIcon(allSessionsIcon); else if (isItThisType(value, DSObjectInfoInterface.COLLABORANTS)) setIcon(specialClientsIcon); else if (isItThisType(value, DSObjectInfoInterface.COLLABORATION)) setIcon(collaborationIcon); else if (isItThisType(value, DSObjectInfoInterface.ARCHIVES)) ; else {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                DefaultObjectInfo nodeInfo = (DefaultObjectInfo) (node.getUserObject());
            }
        }
        return this;
    }

    /**
    * method that determines if this is a private Session
    *
    * @param value the object to be compared
    * @return true if parameters match, false otherwise
    */
    protected boolean isItPrivateSession(Object value) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        DefaultObjectInfo nodeInfo = (DefaultObjectInfo) (node.getUserObject());
        if (nodeInfo.getOriginalType().equals(DSObjectInfoInterface.SESSIONTYPE) && nodeInfo.getPrivate()) return true; else return false;
    }

    /**
    * method that determines if 'value' is of type 'typeToTestFor'
    *
    * @param value the object to be compared
    * @param typeToTestFor the type to be compared to the object
    * @return true if parameters match, false otherwise
    */
    protected boolean isItThisType(Object value, String typeToTestFor) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        DefaultObjectInfo nodeInfo = (DefaultObjectInfo) (node.getUserObject());
        if (nodeInfo.getOriginalType().equals(typeToTestFor)) return true; else return false;
    }
}
