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
 * The Original Code is the Rendezvous client.
 *
 * The Initial Developer of the Original Code is
 * Ball Aerospace & Technologies Corp, Fairborn, Ohio
 * Portions created by the Initial Developer are Copyright (C) 2001
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s): Charles Wood <cwood@ball.com>
 *                 Bart Carlton <bcarlton@ball.com>
 *
 * ----- END LICENSE BLOCK ----- */
/* RCS $Id: AppShareServer.java,v 1.2 2002/01/07 13:09:55 lizellaman Exp $
 * $Log: AppShareServer.java,v $
 * Revision 1.2  2002/01/07 13:09:55  lizellaman
 * changed to account for creation of DataShare client package
 *
 * Revision 1.1.1.1  2001/10/23 13:43:54  lizellaman
 * initial sourceforge release
 *
 */

package org.rendezvous.appshare;

//import javax.swing.*;
//import javax.swing.border.*;
//import java.awt.*;
//import java.awt.event.*;
import java.util.*;

/**
 * Class AppShareServer
 * This Class is a class for the catpuring window content for application
 * sharing.  It allows the user to capture areas or windows and encodes
 * images into JPeg for better throughput to clients
 *
 * This is a public Class, therefore visiblity of this object is to
 * any other object in any package.
 */
public class AppShareServer {

   static Object object = new Object();
   static boolean nativeClassLoaded = false;
   static boolean threadCompleted = false;

   // get the native library loaded
   static ////////////////////////////////////////////////
      {
      try{
         // put into Thread so errors don't kill main Thread
         final Runnable loadTheLibrary = new Runnable()
            {
            public void run()
               {
               Thread.currentThread().setName("AppShareServerLibraryLoaderThread");
               try{
                  System.loadLibrary("AppShareServer");
                  }
               catch(UnsatisfiedLinkError ule)
                  {
                  //System.setProperty("java.library.path", "" + System.getProperty("java.library.path"));
                  java.net.URL libLocation = object.getClass().getClassLoader().getSystemResource("AppShareServer.class");
                  System.out.println("is AppShareServer.class located at " + libLocation + "?");

                  Properties properties = System.getProperties();
                  for(Enumeration enum = properties.keys(); enum.hasMoreElements();)
                     {
                     String name = (String)enum.nextElement();
                     System.out.println("Name = " + name + ", Value = " + properties.get(name));
                     }

                  throw ule;
                  }
               nativeClassLoaded = true;
               threadCompleted = true;
               }
            };
         Thread shortLivedThread = new Thread(loadTheLibrary);
         shortLivedThread.start();
         Thread.currentThread().yield();
         }
      catch (Exception e)
         {
         nativeClassLoaded = false;
         System.out.println(e);
         }
      }


   /**
    * Method 'getApplicationWindowNames' returns list of all the window names
    *
    * This is a public method, therefore visiblity of this method is to
    * any object in any package.
    *
    * @return Returns type String[] list of all the window names
    *
    * @author Ty W. Hayden
    * @version 1.0
    */
   public String[] getApplicationWindowNames()
      {
      String[] s = null;
      try{
         StringTokenizer st = new StringTokenizer( getApplicationWindowNames2(),"~");
         Vector v = new Vector();
         while (st.hasMoreTokens()) v.addElement( st.nextToken() );
         s=new String[v.size()];
         v.copyInto(s);
         }
      catch(Exception e)
         {
         nativeClassLoaded = false;
         System.out.println(e);
         s = new String[0];
         }
      return s;
      }

    /**
     * Method 'captureScreen' returns a byte array containing captured image
     *
     * This is a public method, therefore visiblity of this method is to
     * any object in any package.
     *
     * @param x location x (type: int)
     * @param y location y (type: int)
     * @param width (type: int)
     * @param height (type: int)
     * @param retWidth return width of image (type: int)
     * @param retHeight return height of image (type: int)
     * @param quality scale 0-100 (type: int)
     * @return Returns type byte[] byte array of jpeg image
     *
     * @author Ty W. Hayden
     * @version 1.0
     */
    public native byte[] captureScreen(int x,int y, int width, int height, int retWidth, int retHeight, int quality);

    /**
     * Method 'captureApplication' returns a byte array containing captured image for a given application
     *
     * This is a public method, therefore visiblity of this method is to
     * any object in any package.
     *
     * @param windowID window id of application to capture (type: int)
     * @param scale scale factor to scale image (type: double)
     * @param quality scale 0-100 (type: int)
     * @return Returns type native byte array of jpeg image
     *
     * @author Ty W. Hayden
     * @version 1.0
     */
    public native byte[] captureApplication(int windowID, double scale, int quality);

    /**
     * Method 'getApplicationWindowNames' returns a string with all window names in it separted by ~
     *
     * This is a public method, therefore visiblity of this method is to
     * any object in any package.
     *
     * @return Returns type native list of application window names
     *
     * @author Ty W. Hayden
     * @version 1.0
     */
    public native String getApplicationWindowNames2();

    /**
     * Method 'getApplicationWindowID' returns a id of window by the application's window name
     *
     * This is a public method, therefore visiblity of this method is to
     * any object in any package.
     *
     * @param windowName name of window to get id for(type: String)
     * @return Returns type int id of window
     *
     * @author Ty W. Hayden
     * @version 1.0
     */
    public native int getApplicationWindowID(String windowName);

    /**
     * Method 'toFront' brings the given window id to the front
     *
     * This is a public method, therefore visiblity of this method is to
     * any object in any package.
     *
     * @param windowID id of window to gain focus (type: int)
     *
     * @author Ty W. Hayden
     * @version 1.0
     */
    public native void toFront(int windowID);

    /**
     * Method 'getApplicationWindowName' returns name of window given it's id
     *
     * This is a public method, therefore visiblity of this method is to
     * any object in any package.
     *
     * @param windowID id of window (type: int)
     * @return Returns type String name of the window
     *
     * @author Ty W. Hayden
     * @version 1.0
     */
    public native String getApplicationWindowName(int windowID);
}
