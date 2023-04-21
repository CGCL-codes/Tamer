/* ----- BEGIN LICENSE BLOCK -----
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is the DataShare server.
 *
 * The Initial Developer of the Original Code is
 * Ball Aerospace & Technologies Corp, Fairborn, Ohio
 * Portions created by the Initial Developer are Copyright (C) 2001
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s): Charles Wood <cwood@ball.com>
 *
 * ----- END LICENSE BLOCK ----- */
/* RCS $Id: SessionUtilities.java,v 1.5 2002/08/08 17:29:52 lizellaman Exp $
 * $Log: SessionUtilities.java,v $
 * Revision 1.5  2002/08/08 17:29:52  lizellaman
 * changed the way image locations are specified
 *
 * Revision 1.4  2002/02/04 13:51:40  lizellaman
 * Remove all references to past product names (or)
 * Add PublicAPI for creating Rendezvous Sessions
 *
 * Revision 1.3  2002/01/29 20:50:17  lizellaman
 * Added LoggingInterface, modified the PropertiesInterface implementation
 *
 * Revision 1.2  2002/01/20 23:26:29  lizellaman
 * add command line parameter that causes an plain DataShareObject to be sent to 'inactive' TCP connections after X milliseconds on inactivity
 *
 * Revision 1.1.1.1  2001/10/23 13:37:18  lizellaman
 * initial sourceforge release
 *
 */

package org.datashare;

import org.datashare.objects.DefaultObjectInfo;
import org.datashare.objects.ServerInfo;
import org.datashare.client.DataShareConnection;
import org.datashare.plugins.LoggingManager.LoggingAdapter;

import java.applet.Applet;
import java.applet.AudioClip;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Container;
import java.awt.MediaTracker;
import java.awt.event.AdjustmentListener;
import java.awt.event.AdjustmentEvent;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Date;
import java.util.Vector;
import java.text.SimpleDateFormat;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.JTree;
import javax.swing.JScrollPane;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;

import java.awt.Toolkit;
import java.awt.Component;
import java.net.URL;
import java.net.MalformedURLException;

/**
 * This is the repository for the methods that represent common activities for
 * Rendezvous.  All methods/attributes in that class are static.
 *
 * @author Charles Wood
 * @version 1.0
 */
public class SessionUtilities
   {
   
   static
      {
      setLoggingInterface(new LoggingAdapter());
      }
   
   public static int SocketRcvDataPriority = Thread.NORM_PRIORITY;
   public static int SocketRcvCmdPriority = Thread.NORM_PRIORITY+1;
   public static int SocketXmtRelativePriority = 1; // xmit thread relative to rcv thread

   /**
    * server machine name/IP-address
    */
   private static String servername;

   /**
    * default location for HC images,
    * should be prepended to the image name prior to retrieval.
    */
   public static String imageRoot = "";

   /**
    * this is where we save a reference to the image to use when we cannot find a user's image.
    * A URL was choosen because the image can then be loaded like all the other user images.
    */
   public static URL missingPersonURL;

   /**
    * server machine IP-address
    */
   private static String serverIP;

   /**
    * the userName for our user (who should have gone through a password acceptance)
    */
   private static String userName;              // parameter name is userName

   /**
    * our user's full name
    */
   private static String userFullName;          // parameter name is userFullName

   /**
    * the user's ID that enables us to find them in the system.  Note, if this value
    * is set to null, the userName and userFullName will not be used.
    */
   private static int userID;                   // parameter name is userID

   /**
    * the user's Enterprise ID that is needed so we can find them in the system.  Note
    * that if this value is set to null, the userName and userFullName will not be
    * used.
    */
   private static int enterpriseID;             // parameter name is enterpriseID

   /**
    * set to the initial applet instance, may be null for applications
    */
   private static Applet ourApplet = null;

   /**
    * available for HC functions that need an externally supplied width value
    */
   private static int    width; // set from width parameter at run time

   /**
    * available for HC functions that need an externally supplied height value
    */
   private static int    height; // set from width parameter at run time

   /**
    * set to true if the instance was started as an application, false if started
    * as an applet.  Should be set to true my the main() method.
    */
   private static boolean isApplication = false;

   /**
    * the command status connection for our client
    */
   private static DataShareConnection commandStatusConnection = null;

   /**
    * contains all the clients we have seen for this VM, for each client we have a
    * String that represents the client's imageURL indexed by the clientKey
    */
   private static Hashtable clientImages = new Hashtable();

   private static FifoQueue fifoQueue = new FifoQueue();  // used for JScrollPane scrollBar adjustment listener timer

   static Hashtable scrollPaneHashtable = new Hashtable();

   /**
    * indicates if we should use lots of System.out.print (when true), or fewer (when false)
    */
   private static boolean verbose = false;

   private static LoggingInterface logIF;
   
   /**
    * sets the logging interface that we will use instead of doing System.out calls.  this
    * will provide more control over what actually gets to the console without having to
    * remove all the System.out calls.
    */
   public static void 
   setLoggingInterface(LoggingInterface loggingInterface)
      {
      logIF = loggingInterface;
      }
   
   /**
    * returns the logging interface that we will use instead of doing System.out calls.
    */
   public static LoggingInterface
   getLoggingInterface()
      {
      return logIF;
      }
   
   /**
    * sets the timeout value in milliseconds for all TCP connections.  This is used when we have
    * a DataShare server that is sitting behind a firewall/router that disconnects inactive connections.
    * Setting it to zero means connections do not timeout.  If we use a non-zero value, a timeout will
    * cause the server to send a KEEPALIVE packet to the connection so it is no longer inactive.  The
    * timeout value should be less than the time the firewall/router uses to determine that connections
    * are inactive (or you won't send the KEEPALIVE packet before you get disconnected by the firewall)
    */
   private static int TCPSocketReadTimeout = 0;  // don't timeout is default;

   /**
    * accessor for TCPSocketReadTimeout
    *
    * @return current timeout value in milliseconds
    */
   public static int
   getTCPSocketReadTimeout()
      {
      return TCPSocketReadTimeout;
      }

   /**
    * sets the TCPSocketReadTimeout attribute
    *
    * @param newTimeoutValue set into TCPSocketReadTimeout
    */
   public static void
   setTCPSocketReadTimeout(int newTimeoutValue)
      {
      TCPSocketReadTimeout = newTimeoutValue;
      getLoggingInterface().debugMsg(getLoggingInterface().DEBUG,
         getLoggingInterface().NETWORK,
         "TCPSocketReadTimeout has been set to " + newTimeoutValue);
      }

   /**
    * accessor for the verbose flag
    *
    * @return false if user wants fewer console printouts, true otherwise
    */
   public static boolean
   getVerbose()
      {
      return verbose;
      }

   /**
    * sets the verbose attribute
    *
    * @param newValue set to true to select lots of details on the console, false otherwise
    */
   public static void
   setVerbose(boolean newValue)
      {
      verbose = newValue;
      getLoggingInterface().debugMsg(getLoggingInterface().DEBUG,
         getLoggingInterface().GENERALSTATUS,
         "System verbose mode has been set to " + newValue);
      }

   /**
    * accessor for the server machine name
    *
    * @return name for the server machine
    */
   public static String
   getServername()
      {
      return servername;
      }

   /**
    * Sets the server name; this should be used by the class that reads the
    * command line parameters (for applications) or the applet code to set this
    * value so that all other classes will be able to use getServername() when the
    * server name is needed.
    *
    * @param newServername the name or IP address of the server
    *
    */
   public static void
   setServername(String newServername)
      {
      getLoggingInterface().debugMsg(getLoggingInterface().DEBUG,
         getLoggingInterface().GENERALSTATUS,
         "Server name set to " + newServername);
      servername = newServername;
      }

   /**
    * Serializes an object on the client side, avoids the problem we had of trying to
    * send a true object through the server if the server did not have a classpath to the object.
    * This way, the server only has to know that it is a byte[].  Convert back to an object
    * via the retrieveObject() method.
    */
   public static byte[]
   convertObjectToByteArray(Object object) throws IOException
      {
      ByteArrayOutputStream baos = new ByteArrayOutputStream(5000);  // automatically extends if needed
      ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(baos));
      oos.flush();
      oos.writeObject(object);
      oos.flush();
      return baos.toByteArray();
      }

   /**
    * Converts a byte[] back to an object.  This method should only be used on objects that were
    * previously converted to the byte[] by the convertObjectToByteArray() method.
    */
   public static Object
   retrieveObject(byte[] objectBytes) throws IOException, ClassNotFoundException
      {
      ByteArrayInputStream bais = new ByteArrayInputStream(objectBytes);
      ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(bais));
      return ois.readObject();
      }

   /**
    * utility method, allows the calling method to delay the specified number of milliseconds
    *
    * @param milliseconds number of milliseconds to delay
    */
   public static void
   delay(int milliseconds)
      {
      try{Thread.sleep(milliseconds);}
      catch(Exception ee){}
      }

   /**
    * returns a string for time that looks good!
    *
    *
    * Symbol   Meaning                 Presentation        Example
    * ------   -------                 ------------        -------
    * G        era designator          (Text)              AD
    * y        year                    (Number)            1996
    * M        month in year           (Text & Number)     July & 07
    * d        day in month            (Number)            10
    * h        hour in am/pm (1~12)    (Number)            12
    * H        hour in day (0~23)      (Number)            0
    * m        minute in hour          (Number)            30
    * s        second in minute        (Number)            55
    * S        millisecond             (Number)            978
    * E        day in week             (Text)              Tuesday
    * D        day in year             (Number)            189
    * F        day of week in month    (Number)            2 (2nd Wed in July)
    * w        week in year            (Number)            27
    * W        week in month           (Number)            2
    * a        am/pm marker            (Text)              PM
    * k        hour in day (1~24)      (Number)            24
    * K        hour in am/pm (0~11)    (Number)            0
    * z        time zone               (Text)              Pacific Standard Time
    * '        escape for text         (Delimiter)
    * ''       single quote            (Literal)           '
    *
    * The count of pattern letters determine the format.
    *
    * (Text): 4 or more pattern letters--use full form, < 4--use short or abbreviated form if one exists.
    * (Number): the minimum number of digits. Shorter numbers are zero-padded to this amount.
    *           Year is handled specially; that is, if the count of 'y' is 2, the Year will be truncated to 2 digits.
    * (Text & Number): 3 or over, use text, otherwise use number.
    * Any characters in the pattern that are not in the ranges of ['a'..'z'] and ['A'..'Z'] will be
    *   treated as quoted text. For instance, characters like ':', '.', ' ', '#' and '@' will appear in the
    *   resulting time text even they are not embraced within single quotes.
    */
   public static String
   getTimeString(Date date)
      {
      String timeString = "";
      try{
         // Create a SimpleDateFormat object with the time pattern specified
         SimpleDateFormat daytimeFormat = new SimpleDateFormat("EE MM/dd/yyyy HH:mm:ss (zz)");
         // Create the special time format...
         timeString = daytimeFormat.format(date);
         }
      catch(Exception e)
         {
         e.printStackTrace();
         }
      return timeString;
      }

   public static String
   getShortTimeString(Date date)
      {
      String timeString = "";
      try{
         // Create a SimpleDateFormat object with the time pattern specified
         SimpleDateFormat daytimeFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
         // Create the special time format...
         timeString = daytimeFormat.format(date);
         }
      catch(Exception e)
         {
         e.printStackTrace();
         }
      return timeString;
      }

   public static String
   getAllSessionsHelpString()
      {
      return "<p>'<i><b>All Sessions</b></i>' on the server are listed here.<br>" +
             "<lu><li>Expand '<i>All Sessions</i>' to see them," +
             "or double click with the mouse to show/hide the sessions.</li>" +
             "<li>Right-click a Session name to join the session," +
             "or right-click its Channels to join the Session.</li>" +
             "<li>Right-click '<i>All Sessions</i>' to create a new session.</li></lu>" +
             "";
      }
   /**
    * accessor for the isApplication flag
    *
    * @return false if instance started as an applet, true if started as an
    *     application
    */
   public static boolean
   getIsApplication()
      {
      return isApplication;
      }

   /**
    * sets the isApplication attribute
    *
    * @param newValue newValue to be set into the isApplication flag
    */
   public static void
   setIsApplication(boolean newValue)
      {
      isApplication = newValue;
      }

   /**
    * accessor for the main applet instance
    *
    * @return the main applet instance
    */
   public static Applet
   getApplet()
      {
      getLoggingInterface().debugMsg(getLoggingInterface().DEBUG,
         getLoggingInterface().GENERALSTATUS,
         "Retrieving Applet (" + ourApplet + ")");
      return ourApplet;
      }

   /**
    * sets the main applet instance
    *
    * @param applet a reference to the main applet instance
    */
   public static void
   setApplet(Applet applet)
      {
      getLoggingInterface().debugMsg(getLoggingInterface().DEBUG,
         getLoggingInterface().GENERALSTATUS,
         "Applet set to " + applet);
      ourApplet = applet;
      }

   /**
    * accessor for the userName parameter value
    *
    * @return userName value
    */
   public static String
   getUserName()
      {
      return userName;
      }

   /**
    * sets the userName value
    *
    * @param newUserName userName value
    */
   public static void
   setUserName(String newUserName)
      {
      getLoggingInterface().debugMsg(getLoggingInterface().DEBUG,
         getLoggingInterface().GENERALSTATUS,
         "userName set to " + newUserName);
      userName = newUserName;
      }

   /**
    * accessor for the userFullName parameter value
    *
    * @return userFullName value
    */
   public static String
   getUserFullName()
      {
      return userFullName;
      }

   /**
    * sets the userFullName value
    *
    * @param newUserFullName userFullName value
    */
   public static void
   setUserFullName(String newUserFullName)
      {
      getLoggingInterface().debugMsg(getLoggingInterface().DEBUG,
         getLoggingInterface().GENERALSTATUS,
         "userFullName set to " + newUserFullName);
      userFullName = newUserFullName;
      }

   /**
    * accessor for the userID value (Client's unique ID number)
    *
    * @return the userID value
    */
   public static int
   getUserID()
      {
      return userID;
      }

   /**
    * sets the userID value
    *
    * @param newUserID userID value
    */
   public static void
   setUserID(int newUserID)
      {
      getLoggingInterface().debugMsg(getLoggingInterface().DEBUG,
         getLoggingInterface().GENERALSTATUS,
         "userID set to " + newUserID);
      userID = newUserID;
      }

   /**
    * accessor for the enterpriseID value (Client's enterprise ID number)
    *
    * @return the enterpriseID value
    */
   public static int
   getEnterpriseID()
      {
      return enterpriseID;
      }

   /**
    * sets the enterpriseID value
    *
    * @param newEnterpriseID enterpriseID value
    */
   public static void
   setEnterpriseID(int newEnterpriseID)
      {
      getLoggingInterface().debugMsg(getLoggingInterface().DEBUG,
         getLoggingInterface().GENERALSTATUS,
         "enterpriseID set to " + newEnterpriseID);
      enterpriseID = newEnterpriseID;
      }

   /**
    * accessor for the width value (a generic attribute)
    *
    * @return the width value
    */
   public static int
   getWidth()
      {
      return width;
      }

   /**
    * sets the width value
    *
    * @param newWidth width value
    */
   public static void
   setWidth(int newWidth)
      {
      getLoggingInterface().debugMsg(getLoggingInterface().DEBUG,
         getLoggingInterface().GENERALSTATUS,
         "width set to " + newWidth);
      width = newWidth;
      }

   /**
    * accessor for the height value (a generic attribute)
    *
    * @return the height value
    */
   public static int
   getHeight()
      {
      return height;
      }

   /**
    * sets the height value
    *
    * @param newHeight height value
    */
   public static void
   setHeight(int newHeight)
      {
      getLoggingInterface().debugMsg(getLoggingInterface().DEBUG,
         getLoggingInterface().GENERALSTATUS,
         "height set to " + newHeight);
      height = newHeight;
      }

   /**
    * accessor for the server machine IP address
    *
    * @return IP address for the server machine
    */
   public static String
   getServerIP()
      {
      return serverIP;
      }

   /**
    * Sets the server IP address; this should be used by the class that reads the
    * command line parameters (for applications) or the applet code to set this
    * value so that all other classes will be able to use getServerIP() when the
    * server name is needed.
    *
    * @param newServername IP address of the server
    *
    */
   public static void
   setServerIP(String newServerIP)
      {
      getLoggingInterface().debugMsg(getLoggingInterface().DEBUG,
         getLoggingInterface().GENERALSTATUS,
         "Server IP set to " + newServerIP);
      serverIP = newServerIP;
      }

   /**
    * Sets the command status connection for this client (used to communicate with
    * the server)
    */
   public static void
   setCommandStatusConnection(DataShareConnection thisCommandStatusConnection)
      {
      commandStatusConnection = thisCommandStatusConnection;
      }

   /**
    * Retreives the command status connection for this client
    */
   public static DataShareConnection
   getCommandStatusConnection()
      {
      return commandStatusConnection;
      }

   /**
    * returns an imageIcon (from the jar file if called from an applet)
    * Note that the image name should be an absolute filename, for example:
    * /com/ball/ads/hc/images/my.gif, and that the FILE NAMES ARE CASE SENSITIVE.
    *
    * @param container a container that is loaded so it's class loader can be used to
    *     retrieve the image
    * @param name the filename of the image
    * @return the specified imageIcon
    */
   public static ImageIcon
   getImageIcon(Container container, String name)
      {
      ImageIcon imageIcon = null;
      Image image = null;
      image = getMyImage(container, name);
      if(image != null)
         {
         imageIcon = new ImageIcon(image);
         }
      return imageIcon;
      }

   /**
    * returns an imageIcon (from the jar file if called from an applet)
    * Note that the image name should be an absolute filename, for example:
    * /com/ball/ads/hc/images/my.gif, and that the FILE NAMES ARE CASE SENSITIVE.
    *
    * @param frame a frame that is loaded so it's class loader can be used to
    *     retrieve the image
    * @param name the filename of the image
    * @return the specified imageIcon
    */
   public static ImageIcon
   getImageIcon(Frame frame, String name)
      {
      ImageIcon imageIcon = null;
      Image image = null;
      image = getMyImage((Container)frame, name);
      if(image != null)
         {
         imageIcon = new ImageIcon(image);
         }
      return imageIcon;
      }


   /**
    * this method will load an image from jar files (and possibly other resources
    * that the classloader knows about).  Note that the image name should be an
    * absolute filename, for example: /com/ball/ads/hc/images/my.gif
    *
    * @param container used to load a resource
    * @param imageName the file name of the image
    * @return the specified image
    */
   public static Image
   getMyImage(Container container, String imageName)
      {
      if( imageName == null )
         return null;
      Image image = null;
      byte[] imageBytes = null;
      try
         {
         // get first file with this name...
         //imageName = container.getClass().getClassLoader().getResource(imageName).getFile();

         InputStream in = container.getClass().getResourceAsStream(imageName);
         int length = in.available();
         getLoggingInterface().debugMsg(getLoggingInterface().DEBUG,
            getLoggingInterface().GENERALSTATUS,
            "found image " + imageName + " that is " + length + " bytes long");
         imageBytes = new byte[length];
         in.read( imageBytes );
         Toolkit toolkit = Toolkit.getDefaultToolkit();
         image = toolkit.createImage( imageBytes );
         }
      catch( Exception exc )
         {
         getLoggingInterface().debugMsg(getLoggingInterface().WARNING,
            getLoggingInterface().GENERALSTATUS,
            exc +" getting resource " +imageName );
//         exc.printStackTrace();
         return null;
         }
      return image;
      }

   /**
    * sets the frame's Icon to our standard Icon
    *
    * @param frame
    */
   public static void
   setFrameIcon(Frame frame)
      {
      if(frame != null)
         {
         Image k2Icon = SessionUtilities.getMyImage(frame, SessionUtilities.imageRoot + "smallK2.gif");
         frame.setIconImage(k2Icon);
         }
      }


   /**
    * determines what type of object is to be inserted, then inserts it at as the last
    * child at the appropriate place.
    *
    * @param doi the object to be inserted into the tree
    */
   public static void
   insertNode(DefaultObjectInfo doi,
              DefaultMutableTreeNode sessionNode,
              DefaultMutableTreeNode clientNode,
              DefaultTreeModel model,
              String myClientClass)
      {
      if (doi.getOriginalType().equals(DefaultObjectInfo.CLIENTTYPE) )
         addSpecialClient(doi, clientNode, model, myClientClass);
      else if (doi.getOriginalType().equals(DefaultObjectInfo.SESSIONTYPE) )
         addSession(doi, sessionNode, model, myClientClass);
      else if (doi.getOriginalType().equals(DefaultObjectInfo.CHANNELTYPE) )
         addChannel(doi, sessionNode, model, myClientClass);
      else if (doi.getOriginalType().equals(DefaultObjectInfo.CONSUMERTYPE) )
         addConsumer(doi, sessionNode, model, myClientClass);
      }

   /**
    * adds a SpecialClient to our treeNode
    *
    * @param doi the client object to be inserted
    */
   protected static void
   addSpecialClient(DefaultObjectInfo doi, DefaultMutableTreeNode clientNode, DefaultTreeModel model, String myClientClass)
      {
      getLoggingInterface().debugMsg(getLoggingInterface().DEBUG,
         getLoggingInterface().CLIENT,
         "adding a client to our tree->" + doi.toString());
      DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(doi);
      if(clientNode != null)  // sometimes we get updates before we get setup
         {
         // if client is our class, just stick it in the clientNode
         if(doi.getClientClass().equals(myClientClass))
            {
            insertNodeInto(newNode,clientNode,clientNode.getChildCount(),model);
            }
         else // we must put client into the clientClass sub node...
            {
            // first find if we have a subnode of the correct clientClass...
            DefaultMutableTreeNode clientClassNode = clientNode.getNextNode();
            while(clientClassNode != null)
               {
               // is this node the correct one?
               DefaultObjectInfo oi = (DefaultObjectInfo)clientClassNode.getUserObject();
               if(oi.getClientClass().equals(doi.getClientClass()))  // may also need to test if subclient type
                  {
                  // found the node we want to add the client to
                  insertNodeInto(newNode,clientClassNode, clientClassNode.getChildCount(), model);
                  break;
                  }
               else
                  clientClassNode = clientClassNode.getNextSibling(); // try the next node
               }
            if(clientClassNode == null) // did not already exist in our tree, so create it
               {
               // not our class and we have not subclient nodes, create one!
               DefaultObjectInfo newDoi = new DefaultObjectInfo(doi.getClientClass(), DefaultObjectInfo.SUBCLIENTTYPE, doi.getClientClass(), doi.getClientClass() + " clients");
               DefaultMutableTreeNode newClientNode = new DefaultMutableTreeNode(newDoi);
               insertNodeInto(newNode, newClientNode, 0, model);
               insertNodeInto(newClientNode, clientNode, clientNode.getChildCount(), model);
               }
            }
         }

      // save a possibly new client imageURL in our table
      if(doi.getOriginalType().equals(DefaultObjectInfo.CLIENTTYPE))
         {
         SessionUtilities.setUserImageURL(doi.getKeyValue(), doi.getName(), doi.getImageURL());
         }
      else
         {
         getLoggingInterface().debugMsg(getLoggingInterface().DEBUG,
            getLoggingInterface().CLIENT,
            "AddSpecialClient()-> Found something other than a client in our clientNode->" + doi.getOriginalType());
         }
      }

   /**
    * adds a Session to our treeNode
    *
    * @param doi the object to be inserted
    */
   protected static void
   addSession(DefaultObjectInfo doi, DefaultMutableTreeNode sessionNode, DefaultTreeModel model, String myClientClass)
      {
      getLoggingInterface().debugMsg(getLoggingInterface().DEBUG,
         getLoggingInterface().SESSION,
         "adding a Session to our tree->" + doi.toString());
      DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(doi);
      if(sessionNode != null)  // sometimes we get updates before we get setup
         {
         if(doi.getClientClass().equals(myClientClass))
            insertNodeInto(newNode,sessionNode,sessionNode.getChildCount(),model);
         else // we must put session into the clientClass sub node...
            {
            // first find if we have a subnode of the correct clientClass...
            DefaultMutableTreeNode clientClassNode = null;
            for(Enumeration enum = sessionNode.children(); enum.hasMoreElements();)
               {
               DefaultMutableTreeNode nextNode = (DefaultMutableTreeNode)enum.nextElement();
               DefaultObjectInfo oi = (DefaultObjectInfo)nextNode.getUserObject();
               // is this node the correct one?
               if(oi.getOriginalType().equals(DefaultObjectInfo.SUBSESSIONTYPE) &&
                  oi.getClientClass().equals(doi.getClientClass()))
                  {
                  // found the node we want to add the client to
                  insertNodeInto(newNode, nextNode, nextNode.getChildCount(), model);
                  clientClassNode = nextNode;  // indicate that we found the correct node
                  }
               }
            if(clientClassNode == null) // did not already exist in our tree, so create it
               {
               // not our class and we have not subclient nodes, create one!
               DefaultObjectInfo newDoi = new DefaultObjectInfo(doi.getClientClass(), DefaultObjectInfo.SUBSESSIONTYPE, doi.getClientClass(), doi.getClientClass() + " clients");
               DefaultMutableTreeNode newSessionNode = new DefaultMutableTreeNode(newDoi);
               insertNodeInto(newNode, newSessionNode, 0, model);
               insertNodeInto(newSessionNode, sessionNode, sessionNode.getChildCount(), model);
               }
            }
         }
      }

   /**
    * adds a Channel to a Session in our treeNode
    *
    * @param doi the object to be inserted
    */
   protected static void
   addChannel(DefaultObjectInfo doi, DefaultMutableTreeNode sessionNode, DefaultTreeModel model, String clientClass)
      {
      getLoggingInterface().debugMsg(getLoggingInterface().DEBUG,
         getLoggingInterface().SESSION,
         "adding Channel " + doi.getKeyValue() + " to Session " + doi.getParentKeyValue());
      // first determine the session...
      if(doi.getParentKeyValue() != null) // make sure the parent session is provided
         {
         DefaultMutableTreeNode dmtn = searchForSessionInTree(doi.getParentKeyValue(), sessionNode);
         if(dmtn != null)
            {
            // insert this channel as the Session's last child
            insertNodeInto(new DefaultMutableTreeNode(doi), dmtn, dmtn.getChildCount(), model);
            }
         else
            {
            getLoggingInterface().debugMsg(getLoggingInterface().DEBUG,
              getLoggingInterface().SESSION,
              "Could not locate a Channel's Session in our tree: time to View/Refresh");
            }
         }
      else
         {
         getLoggingInterface().debugMsg(getLoggingInterface().DEBUG,
            getLoggingInterface().SESSION,
            "This Channel has no parent Session value");
         }
      }

   /**
    * adds a Consumer to a Channel that is in a Session in our treeNode
    *
    * @param doi the object to be inserted
    */
   protected static void
   addConsumer(DefaultObjectInfo consumer, DefaultMutableTreeNode sessionNode, DefaultTreeModel model, String myClientClass)
      {
      // first find our Session, then the Channel, then add the Consumer
      if(consumer.getGrandParentKeyValue() != null) // make sure the parent session is provided
         {
         DefaultMutableTreeNode sessiondmtn = searchForSessionInTree(consumer.getGrandParentKeyValue(), sessionNode);
         if(sessiondmtn != null)
            {
            DefaultMutableTreeNode channeldmtn = searchForChannelInTree(consumer.getGrandParentKeyValue(), consumer.getParentKeyValue(), sessionNode);
            if(channeldmtn != null)
               {
               // determine if consumer already in tree...
               DefaultMutableTreeNode consumerDmtn = searchForConsumerInTree(consumer.getGrandParentKeyValue(), consumer.getParentKeyValue(), consumer.getKeyValue(), sessionNode);
               if(consumerDmtn != null)
                  {
                  DefaultObjectInfo oldConsumer = (DefaultObjectInfo)consumerDmtn.getUserObject();

                  getLoggingInterface().debugMsg(getLoggingInterface().DEBUG,
                     getLoggingInterface().CLIENT,
                     "Removing " + (oldConsumer.getActive()?"active":"inactive") + " Consumer " + consumer.getKeyValue());
                  // overwrite entry in tree with replacement, so first remove previous entry...
                  removeConsumer(oldConsumer,sessionNode,model);
                  }
               getLoggingInterface().debugMsg(getLoggingInterface().DEBUG,
                  getLoggingInterface().CLIENT,
                  "Adding " + (consumer.getActive()?"active":"inactive") + " Consumer " + consumer.getKeyValue());
               // insert this Consumer as the Channel's last child
               insertNodeInto(new DefaultMutableTreeNode(consumer), channeldmtn, channeldmtn.getChildCount(), model);
               }
            else
               {
               getLoggingInterface().debugMsg(getLoggingInterface().WARNING,
                  getLoggingInterface().CLIENT,
                  "Could not locate a Consumer's Channel in our tree: time to View/Refresh");
               }
            }
         else
            {
            getLoggingInterface().debugMsg(getLoggingInterface().DEBUG,
               getLoggingInterface().CLIENT,
               "Could not locate a Consumer's Session in our tree: time to View/Refresh");
            }
         }
      else
         {
         getLoggingInterface().debugMsg(getLoggingInterface().DEBUG,
            getLoggingInterface().CLIENT,
            "This Consumer has no parent Session value");
         }
      }

   /**
    * finds a Session with the same keyValue as the one specified,
    * returns null otherwise.
    *
    * @param sessionKeyValue key value for the Session, all Session names must be unique
    * @param sessionNode the top level of all Sessions, used as the starting point for the search
    * @return the node that corresponds to the requested Session
    */
   public static DefaultMutableTreeNode
   searchForSessionInTree(String sessionKeyValue, DefaultMutableTreeNode sessionNode)
      {
      DefaultMutableTreeNode retValue = null;
      if(sessionNode != null)  // starts out empty, so check for it
         {
         // need to check all nodes, not just children because other client's Sessions will be in subsessiontype folders
         for(Enumeration sessions = sessionNode.depthFirstEnumeration(); sessions.hasMoreElements();)
            {
            DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) sessions.nextElement();
            DefaultObjectInfo session = (DefaultObjectInfo) dmtn.getUserObject();
            // skip subsessiontype folders...
            if(session.getOriginalType().equals(DefaultObjectInfo.SESSIONTYPE) &&
               session.getKeyValue().equals(sessionKeyValue))
               {
               retValue = dmtn;
               break; // stop the for-loop
               }
            }
         }
      if(retValue == null)
         {
         getLoggingInterface().debugMsg(getLoggingInterface().DEBUG,
            getLoggingInterface().SESSION,
            "searchForSessionInTree unable to find Session->" + sessionKeyValue);
         }
      return retValue;
      }

   /**
    * finds a Client with the same keyValue as the one specified,
    * returns null otherwise.
    *
    * @param clientKeyValue the key value for the Client
    * @return the node for the requested Client
    */
   protected static DefaultMutableTreeNode
   searchForClientInTree(String clientKeyValue, DefaultMutableTreeNode clientNode)
      {
      DefaultMutableTreeNode retValue = null;
      if(clientNode != null)
         {
         // need to look at all nodes, not just children because some clients will be in their clientClass folders
         for(Enumeration clients = clientNode.depthFirstEnumeration(); clients.hasMoreElements();)
            {
            DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)clients.nextElement();
            DefaultObjectInfo client = (DefaultObjectInfo)dmtn.getUserObject();
            // skip over the subclienttype nodes
            if(client.getOriginalType().equals(DefaultObjectInfo.CLIENTTYPE) &&
               client.getKeyValue().equals(clientKeyValue))
               {
               retValue = dmtn;
               break; // stop the for-loop
               }
            }
         }
      if(retValue == null)
         {
         getLoggingInterface().debugMsg(getLoggingInterface().DEBUG,
            getLoggingInterface().CLIENT,
            "searchForClientInTree unable to find Client->" + clientKeyValue);
         }
      return retValue;
      }

   /**
    * finds a Channel with the same keyValue as the one specified by looking in the
    * Session with the same keyValue as the one specified as the parentSession.
    * returns null otherwise.
    *
    * @param sessionKeyValue key value for the Session for this Channel
    * @param channelKeyValue key value for the Channel
    * @return the node for the requested Channel
    */
   public static DefaultMutableTreeNode
   searchForChannelInTree(String sessionKeyValue, String channelKeyValue, DefaultMutableTreeNode sessionNode)
      {
      DefaultMutableTreeNode retValue = null;
      DefaultMutableTreeNode session = null;
      session = searchForSessionInTree(sessionKeyValue, sessionNode);
      if(session != null)
         {
         for(Enumeration channels = session.children(); channels.hasMoreElements();)
            {
            DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)channels.nextElement();
            DefaultObjectInfo channel = (DefaultObjectInfo)dmtn.getUserObject();
            if(channel.getKeyValue().equals(channelKeyValue))
               {
               retValue = dmtn;
               break; // stop the for-loop
               }
            }
         if(retValue == null)
            {
            getLoggingInterface().debugMsg(getLoggingInterface().DEBUG,
               getLoggingInterface().SESSION,
               "could not find the specified Channel in our tree");
            }
         }
      else
         {
         getLoggingInterface().debugMsg(getLoggingInterface().DEBUG,
            getLoggingInterface().SESSION,
            "searchForChannelInTree unable to find parent Session->" + sessionKeyValue);
         }
      return retValue;
      }

   /**
    * finds all Channels in the parentSession.
    * returns null otherwise.
    *
    * @param sessionKeyValue key value for the Session
    * @param sessionNode treeNode that contains all the Sessions
    * @return the channelKeyValues for all Channels in the Session
    */
   public static DefaultMutableTreeNode[]
   getAllChannelsInSession(String sessionKeyValue, DefaultMutableTreeNode sessionNode)
      {
      DefaultMutableTreeNode[] retValue = new DefaultMutableTreeNode[0];
      DefaultMutableTreeNode session = null;
      Vector channelVec = new Vector();
      session = searchForSessionInTree(sessionKeyValue, sessionNode);
      if(session != null)
         {
         for(Enumeration channels = session.children(); channels.hasMoreElements();)
            {
            DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)channels.nextElement();
            channelVec.add(dmtn);
            }
         if(channelVec.size() > 0)
            {
            retValue = new DefaultMutableTreeNode[channelVec.size()];
            for(int x=0; x<channelVec.size(); x++)
               retValue[x] = (DefaultMutableTreeNode)channelVec.elementAt(x);
            }
         else
            {
            getLoggingInterface().debugMsg(getLoggingInterface().DEBUG,
               getLoggingInterface().SESSION,
               "could not find any Channels in our tree");
            }
         }
      else
         {
         getLoggingInterface().debugMsg(getLoggingInterface().DEBUG,
            getLoggingInterface().SESSION,
            "searchForAllChanneslInTree unable to find parent Session->" + sessionKeyValue);
         }
      return retValue;
      }

   /**
    * finds a Consumer with the same keyValue as the one specified by looking in the
    * Session with the same keyValue as the one specified by the sessionKeyValue
    * and the Channel with the same keyValue as the one specified by the enteKeyValue.
    * returns null otherwise.
    *
    * @param sessionKeyValue key value for the Session to look in
    * @param channelKeyValue key value for the Channel to look in
    * @param consumerKeyValue key value for the Consumer to find
    * @return the node for the requested Consumer
    */
   public static DefaultMutableTreeNode
   searchForConsumerInTree(String sessionKeyValue, String channelKeyValue, String consumerKeyValue, DefaultMutableTreeNode sessionNode)
      {
      DefaultMutableTreeNode retValue = null;
      DefaultMutableTreeNode channel = null;
      channel = searchForChannelInTree(sessionKeyValue, channelKeyValue, sessionNode);
      if(channel != null)
         {
         for(Enumeration consumers = channel.children(); consumers.hasMoreElements();)
            {
            DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)consumers.nextElement();
            DefaultObjectInfo consumer = (DefaultObjectInfo)dmtn.getUserObject();
            if(consumer.getKeyValue().equals(consumerKeyValue))
               {
               retValue = dmtn;
               break; // stop the for-loop
               }
            }
         if(retValue == null)
            {
            getLoggingInterface().debugMsg(getLoggingInterface().DEBUG,
               getLoggingInterface().CLIENT,
               "could not find the specified Consumer in our tree");
            }
         }
      else
         {
         getLoggingInterface().debugMsg(getLoggingInterface().DEBUG,
            getLoggingInterface().CLIENT,
            "searchForConsumerInTree unable to find parent Session/Channel->" + sessionKeyValue+"/"+channelKeyValue);
         }
      return retValue;
      }

   /**
    * removes a specialClient from our treeNode.
    *
    * @param doi contains info about the SpecialClient to remove
    */
   protected static void
   removeSpecialClient(DefaultObjectInfo doi, DefaultMutableTreeNode clientNode, DefaultTreeModel model)
      {
      DefaultMutableTreeNode dmtn = searchForClientInTree(doi.getKeyValue(), clientNode);
      if(dmtn != null)
         {
         getLoggingInterface().debugMsg(getLoggingInterface().DEBUG,
            getLoggingInterface().CLIENT,
            "now removing special client " + doi.getKeyValue());
         model.removeNodeFromParent(dmtn);

         // now look at all of the clientNode to see if there are any SUBCLIENTTYPE folder that are empty
         for(Enumeration enum = clientNode.children(); enum.hasMoreElements(); )
            {
            DefaultMutableTreeNode childNode = (DefaultMutableTreeNode)enum.nextElement();
            DefaultObjectInfo doi2 = (DefaultObjectInfo)childNode.getUserObject();
            // is it a subclienttype node?
            if(doi2.getOriginalType().equals(DefaultObjectInfo.SUBCLIENTTYPE))
               {
               // is it empty
               if(childNode.isLeaf())
                  {
                  getLoggingInterface().debugMsg(getLoggingInterface().DEBUG,
                     getLoggingInterface().CLIENT,
                     "removing empty node-> " + childNode.toString());
                  model.removeNodeFromParent(childNode);
                  }
               }
            }
         }
      }

   /**
    * removes a Session from our treeNode.
    *
    * @param doi contains info about the Sesison to remove
    */
   protected static void
   removeSession(DefaultObjectInfo doi, DefaultMutableTreeNode sessionNode, DefaultTreeModel model)
      {
      DefaultMutableTreeNode dmtn = searchForSessionInTree(doi.getKeyValue(), sessionNode);
      if(dmtn != null)
         {
         getLoggingInterface().debugMsg(getLoggingInterface().DEBUG,
            getLoggingInterface().SESSION,
            "now removing Session " + doi.getKeyValue());
         model.removeNodeFromParent(dmtn);
         // now look at all of the sessionNode to see if there are any SUBSESSIONTYPE folder that are empty
         for(Enumeration enum = sessionNode.children(); enum.hasMoreElements(); )
            {
            DefaultMutableTreeNode childNode = (DefaultMutableTreeNode)enum.nextElement();
            DefaultObjectInfo doi2 = (DefaultObjectInfo)childNode.getUserObject();
            // is it a subsessiontype node?
            if(doi2.getOriginalType().equals(DefaultObjectInfo.SUBSESSIONTYPE))
               {
               // is it empty
               if(childNode.isLeaf())
                  {
                  getLoggingInterface().debugMsg(getLoggingInterface().DEBUG,
                     getLoggingInterface().SESSION,
                     "removing empty node-> " + childNode.toString());
                  model.removeNodeFromParent(childNode);
                  }
               }
            }
         }
      else
         {
         getLoggingInterface().debugMsg(getLoggingInterface().DEBUG,
            getLoggingInterface().SESSION,
            "removeSession() was unable to find the Session to remove it");
         }
      }

   /**
    * removes a Consumer from our treeNode
    *
    * @param doi contains info about the Consumer to remove
    */
   protected static void
   removeConsumer(DefaultObjectInfo doi, DefaultMutableTreeNode sessionNode, DefaultTreeModel model)
      {
      // first we must find the Session, then the Channel, then the Consumer...
      DefaultMutableTreeNode dmtn = searchForConsumerInTree(doi.getGrandParentKeyValue(),
                                                doi.getParentKeyValue(),doi.getKeyValue(),
                                                sessionNode);
      if(dmtn != null)
         {
         getLoggingInterface().debugMsg(getLoggingInterface().DEBUG,
            getLoggingInterface().CLIENT,
            "Removing Consumer " +doi.getKeyValue()+" from Session " +doi.getGrandParentKeyValue()+", Channel "+doi.getParentKeyValue());
         model.removeNodeFromParent(dmtn);
         }
      }

   /**
    * removes a Channel from our treeNode
    *
    * @param doi contains info about the Channel to remove
    */
   protected static void
   removeChannel(DefaultObjectInfo doi, DefaultMutableTreeNode sessionNode, DefaultTreeModel model)
      {
      DefaultMutableTreeNode dmtn = searchForChannelInTree(doi.getParentKeyValue(), doi.getKeyValue(), sessionNode);
      if(dmtn != null)
         {
         getLoggingInterface().debugMsg(getLoggingInterface().DEBUG,
            getLoggingInterface().SESSION,
            "Removing Channel " + doi.getKeyValue() + " from Session "+ doi.getParentKeyValue());
         model.removeNodeFromParent(dmtn);
         }
      else
         {
         getLoggingInterface().debugMsg(getLoggingInterface().DEBUG,
            getLoggingInterface().SESSION,
            "removeChannel() was unable to find the Channel to remove it");
         }
      }

   /**
    * removes the specified node from our tree
    *
    * @param doi contains info about the node to remove
    */
   public static void
   removeNode( DefaultObjectInfo doi,
               DefaultMutableTreeNode sessionNode,
               DefaultMutableTreeNode clientNode,
               DefaultTreeModel model,
               String myClientClass)
      {
      // do logic to this node in our tree...may need to use parent and grandparent
      if (doi.getOriginalType().equals(DefaultObjectInfo.CLIENTTYPE) )
         removeSpecialClient(doi, clientNode, model);
      else if (doi.getOriginalType().equals(DefaultObjectInfo.SESSIONTYPE) )
         removeSession(doi, sessionNode, model);
      else if (doi.getOriginalType().equals(DefaultObjectInfo.CHANNELTYPE) )
         removeChannel(doi, sessionNode, model);
      else if (doi.getOriginalType().equals(DefaultObjectInfo.CONSUMERTYPE) )
         removeConsumer(doi, sessionNode, model);
      }

   /**
    * inserts the specified node into the specified parent node at the specified index
    *
    * @param node node to be inserted
    * @param parentNode node which will receive the node
    * @param index location in parentNode at which to insert the new node
    */
   protected static void
   insertNodeInto( DefaultMutableTreeNode node, DefaultMutableTreeNode parentNode, int index, DefaultTreeModel model)
      {
      model.insertNodeInto(node, parentNode, index);
      }


   /**
    * returns true if Consumer is found in any Channel in this Session
    */
   public static boolean
   isConsumerInSession(DefaultMutableTreeNode sessionNode, String consumerKey)
      {
      boolean finished = false;
      boolean retValue = false;
      try
         {
         for(Enumeration channels = sessionNode.children(); channels.hasMoreElements() && !finished;)
            {
            DefaultMutableTreeNode channelNode = (DefaultMutableTreeNode)channels.nextElement();
            for(Enumeration consumers = channelNode.children(); consumers.hasMoreElements();)
               {
               DefaultMutableTreeNode consumerNode = (DefaultMutableTreeNode)consumers.nextElement();
               DefaultObjectInfo consumer = (DefaultObjectInfo)consumerNode.getUserObject();
               if(consumer.getKeyValue().equals(consumerKey))
                  {
                  retValue = true;
                  finished = true;
                  break;
                  }
               }
            }
         }
      catch(Exception e)
         {
         e.printStackTrace();
         }
      return retValue;
      }

   /**
    * used when an image for a user is to be saved
    */
   public static void
   setUserImageURL(String clientUniqueName, String clientName, String url)
      {
      clientUniqueName = clientUniqueName.toLowerCase();
      clientName = clientName.toLowerCase();

      // don't put a client into our table more than once by same name
      if(!clientImages.containsKey(clientUniqueName) )
         {
         // sometimes people don't like to put in the full URL name...
         if(!url.equals("") && !url.toLowerCase().startsWith("http:"))
            url = "http://" + getServerIP() + url;
         getLoggingInterface().debugMsg(getLoggingInterface().DEBUG,
            getLoggingInterface().CLIENT,
            "Adding imageURL for " + clientUniqueName + " --> " + url);
         clientImages.put(clientUniqueName, url);
         }
      if(!clientImages.containsKey(clientName) )
         {
         // sometimes people don't like to put in the full URL name...
         if(!url.equals("") && !url.toLowerCase().startsWith("http:"))
            url = "http://" + getServerIP() + url;
         getLoggingInterface().debugMsg(getLoggingInterface().DEBUG,
            getLoggingInterface().CLIENT,
            "Adding imageURL for " + clientName + " --> " + url);
         clientImages.put(clientName, url);
         }
      }

   /**
    * used to retrieve the URL for a client's image, we must have had the client in our tree
    * and they must have an image loaded on the server;
    * @return URL string for image or empty string if URL not available
    */
   public static String
   getUserImageURL(String clientName)
      {
      clientName = clientName.toLowerCase();
      String url = "";
      if(clientImages.containsKey(clientName))
         url = (String)clientImages.get(clientName);
      return url;
      }

   /**
    * used to retrieve the Image of a client, we must have had the client in our tree and they
    * must have an imgage loaded on the server;
    */
   public static Image
   getUserImage(String clientName)
      {
      clientName = clientName.toLowerCase();
      Image image = null;
      String imageURL = "";
      Toolkit toolkit = Toolkit.getDefaultToolkit();
      try{
         imageURL = getUserImageURL(clientName);  // look in list of collaborants...
         if(!imageURL.equals(""))
            {
            getLoggingInterface().debugMsg(getLoggingInterface().DEBUG,
               getLoggingInterface().CLIENT,
               "Image for " + clientName + " is at " + imageURL);
            image = toolkit.getImage(new URL(imageURL));
            }
         else
            {
            getLoggingInterface().debugMsg(getLoggingInterface().DEBUG,
               getLoggingInterface().CLIENT,
               "getUserImage() could not find the image for " + clientName);
            image = toolkit.getImage(SessionUtilities.missingPersonURL);
            }
         }
      catch (MalformedURLException e)
         {
         System.err.println("getUserImage(" + clientName + ") URL no good: " + imageURL);
         image = toolkit.getImage(SessionUtilities.missingPersonURL);
         }

      return image;
      }

   /**
    * finds clientName from clientKey
    * @param clientKey the clientKeyValue of a client
    * @param clientNode the node that contains all SpecialClients
    * @return the clientName that corresponds to the specified clientKeyValue, or empty string if not found
    */
   public static String
   findClientName(String clientKey, DefaultMutableTreeNode clientNode)
      {
      DefaultMutableTreeNode dmtn = searchForClientInTree(clientKey, clientNode);
      DefaultObjectInfo doi = (DefaultObjectInfo)dmtn.getUserObject();
      String clientName = doi.getName();
      return clientName;
      }

   /**
    * finds the first Frame going back through a Container's ancestry, returns null if
    * no Frame is found.  Useful when we need a modal dialog and have no reference to a parent frame.
    */
   public static Frame
   getParentFrame(Container container)
      {
      Frame retValue = null;
      boolean finished = false;
      while(!finished)
         {
         try{
            retValue = (Frame)container;
            finished = true;
            }
         catch(ClassCastException cce)
            {
            container = container.getParent();
            }
         catch(Exception e)
            {
            finished = true;
            }
         }
      return retValue;
      }

   /**
    * This method will allow any JScrollPane to repaint 250 mseconds after scrollBar adjustments
    * made by a user have stopped.  This is done so that the artifacts of scrolling (garbled text/images)
    * gets cleaned up after every scroll pane is moved (adjusted)
    */
   public static void
   scrollPaneDroppingsFixer(JScrollPane scrollPane)
      {
      final JScrollPane pane = scrollPane;

      pane.getHorizontalScrollBar().addAdjustmentListener
         (
         new AdjustmentListener()
            {
            // Invoked when the value of the adjustable (JScrollBar) has changed.
            public void adjustmentValueChanged(AdjustmentEvent e)
               {
               if(pane.getHorizontalScrollBar().isShowing())
                  fifoQueue.write(pane);
               }
            }
         );
      pane.getVerticalScrollBar().addAdjustmentListener
         (
         new AdjustmentListener()
            {
            // Invoked when the value of the adjustable (JScrollBar) has changed.
            public void adjustmentValueChanged(AdjustmentEvent e)
               {
               if(pane.getVerticalScrollBar().isShowing())
                  fifoQueue.write(pane);
               }
            }
         );
      }

   /**
    * makes fifoQueue a thread and provides a consumer method for any objects put into
    * the FIFO.  Used here to provide a way to determine when a user has stopped making
    * JScrollBar adjustments on any JScrollPane for which scrollPaneDroppingsFixer was called.
    */
   static
      {
      fifoQueue.setConsumer
         (
         new FifoConsumer()
            {
            /**
             * called when data is available from the FIFO
             */
            public void newFifoDataAvailable(Object object)
               {
               handleNewScrollBarAdjustment( (JScrollPane)object);
               }
            }
         );
      }

   /**
    * called when a scrollBar adjustment has been made on a JScrollPane for which
    * scrollPaneDroppingsFixer was called.
    */
   synchronized private static void
   handleNewScrollBarAdjustment(JScrollPane pane)
      {
      // check to see if we already have a timer thread for this scrollPane
      JScrollBarTimerThread jsbtt = (JScrollBarTimerThread)scrollPaneHashtable.get(pane);

      if(jsbtt != null)
         {
         jsbtt.anotherAdjustmentMade();  // cancel any scheduled GUI update if it has not happened yet
         }
      scrollPaneHashtable.put(pane, new JScrollBarTimerThread(pane));  // schedule a GUI update...
      }
   }

/////////////////////////////////////////////////

/**
 * A one-shot timer, releases after .250 seconds, checks to see if it was stopped
 * (by calling the anotherAdjustmentMade method), if not, it updates the JScrollPane.
 */
class JScrollBarTimerThread extends Thread
   {
   boolean running = true;
   JScrollPane pane;

   public JScrollBarTimerThread(JScrollPane pane)
      {
      this.pane = pane;
      this.setName("JScrollBarTimerThread");
      this.start();
      }

   /**
    * should be called when another adjustment is made so we can cancel the update
    * for the prior adjustment
    */
   public void anotherAdjustmentMade()
      {
      running = false;
      }

   public void run()
      {
      SessionUtilities.delay(250);  // wait 1/4 second
      if(running)
         {
         pane.repaint();  // cause JScrollPane to update
         }
      }


   }
