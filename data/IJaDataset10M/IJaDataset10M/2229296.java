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
/* RCS $Id: MulticastSocketServer.java,v 1.3 2002/02/04 13:51:40 lizellaman Exp $
 * $Log: MulticastSocketServer.java,v $
 * Revision 1.3  2002/02/04 13:51:40  lizellaman
 * Remove all references to past product names (or)
 * Add PublicAPI for creating Rendezvous Sessions
 *
 * Revision 1.2  2002/01/29 20:50:17  lizellaman
 * Added LoggingInterface, modified the PropertiesInterface implementation
 *
 * Revision 1.1.1.1  2001/10/23 13:37:19  lizellaman
 * initial sourceforge release
 *
 */

package org.datashare;

import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.Hashtable;
import java.util.Enumeration;
import java.net.InetAddress;
import java.io.IOException;

/**
 * We need one of these for each Multicast connection that clients might use
 * @date March 01, 2001
 * @author Charles Wood
 * @version 1.0
 */
public class MulticastSocketServer extends Thread implements SocketServerInterface
   {
   DataReceiverInterface dri = null;
   MulticastSocket socket = null;
   DatagramPacket sndPacket = null;
   DatagramPacket rcvPacket = null;
   boolean running = true;
   Hashtable connections = new Hashtable();
   InetAddress myIpAddress;  // the multicast IP address
   String keyValue;
   boolean active = false;
   int priority;
   int port;  // normally the server port, it is the Multicast port for this channel here

   public MulticastSocketServer(InetAddress myIpAddress, DataReceiverInterface dri, int port, int priority)
      {
      SessionUtilities.getLoggingInterface().debugMsg(SessionUtilities.getLoggingInterface().DEBUG,
         SessionUtilities.getLoggingInterface().NETWORK,
         "MulticastSocketServer for IP:port " + myIpAddress + ":" + port);
      this.dri = dri;
      this.port = port;
      this.priority = priority;
      this.myIpAddress = myIpAddress;
      keyValue = "MULTICAST:"+port;
      this.setName("DataShare.SocketServer."+keyValue);
      createSocket(true);
      if(SessionUtilities.getVerbose())
         {
         try{
            SessionUtilities.getLoggingInterface().debugMsg(SessionUtilities.getLoggingInterface().DEBUG,
               SessionUtilities.getLoggingInterface().NETWORK,
               "Multicast socket max receive size is " + socket.getReceiveBufferSize() + "\n" +
               "Multicast socket max send size is " + socket.getSendBufferSize());
            }
         catch(Exception e)
            {
            }
         }
      }

   /**
    * actually creates the socket
    */
   private void
   createSocket(boolean firsttime)
      {
      SessionUtilities.getLoggingInterface().debugMsg(SessionUtilities.getLoggingInterface().DEBUG,
         SessionUtilities.getLoggingInterface().NETWORK,
         (firsttime?"Creating":"Re-creating") + this.getName() );
      try{
         socket = new MulticastSocket(port); // set local port to same as server port
         socket.joinGroup(myIpAddress);
         socket.setReceiveBufferSize(99999);
         socket.setSendBufferSize(99999);
         }
      catch(Exception e)
         {
         e.printStackTrace();
         }
      }

   /**
    * returns true if ready to receive connections
    */
   public boolean getReady()
      {
      return active;
      }

   /**
    * return our local port
    */
   public int getLocalPort()
      {
      return port;
      }

   /**
    * returns our keyValue
    */
   public String getKeyValue()
      {
      return this.keyValue;
      }

   /**
    * this method is where we receive data and establish new 'connections'
    */
   public void run()
      {
      String udpKey = "";
      SessionUtilities.getLoggingInterface().debugMsg(SessionUtilities.getLoggingInterface().DEBUG,
         SessionUtilities.getLoggingInterface().NETWORK,
         "Setting " + this.keyValue + " to priority " +  priority);
      setPriority(priority);
      while(running)
         {
         try{
            DatagramPacket packet = new DatagramPacket(new byte [10001], 10001);
            active = true;
            udpKey = "";
            socket.receive(packet);  // blocks until client connects
            // create unique (to this class) key for 'connection'
            // note that packet getAddress() for a received Multicast packet is the real IP address?! (not Multicast IP address)
            udpKey = packet.getAddress().getHostAddress() + ":" + packet.getPort();
            SessionUtilities.getLoggingInterface().debugMsg(SessionUtilities.getLoggingInterface().DEBUG,
               SessionUtilities.getLoggingInterface().NETWORK,
               "Recvd Multicast length " + packet.getLength() + " from " + udpKey );
            // do not need to forward packets for Multicast
            if(connections.containsKey(udpKey))
               {
               MultiCastSocket handler = (MultiCastSocket)connections.get(udpKey);
               handler.newData(packet);
               }
            else
               {
               // new client!
               MultiCastSocket newHandler = new MultiCastSocket(this, socket, packet, dri, myIpAddress);
               connections.put(udpKey, newHandler);
               dri.newConnection(newHandler);  // now our new socket is known to the DataReceiverAdapter
               newHandler.newData(packet);  // now send this data to handler
               }
            Thread.currentThread().yield();
            } // end of try
         catch(IOException ioe)
            {
            if(running)  // ignore any errors if we have decided to shutdown
               {
               boolean found = false;
               SessionUtilities.getLoggingInterface().debugMsg(SessionUtilities.getLoggingInterface().WARNING,
                  SessionUtilities.getLoggingInterface().NETWORK,
                  "Problems ("+ioe+") receiving data over Multicast socket " + udpKey);
               if(!udpKey.equals("")) // try to shutdown just the connection that had problems...
                  {
                  if(connections.containsKey(udpKey))
                     {
                     found = true;
                     MulticastSocket handler = (MulticastSocket)connections.get(udpKey);
                     handler.close();
                     }
                  }
               if(!found)
                  {
                  SessionUtilities.getLoggingInterface().debugMsg(SessionUtilities.getLoggingInterface().WARNING,
                     SessionUtilities.getLoggingInterface().NETWORK,
                     "MulticastSocketServer Problem that was not traced to a known socket:");
                  ioe.printStackTrace();
                  //close();
                  }
               }
            }
         catch(Exception e)
            {
            e.printStackTrace();
            close();
            }
         }  // end of the while loop
      SessionUtilities.getLoggingInterface().debugMsg(SessionUtilities.getLoggingInterface().DEBUG,
         SessionUtilities.getLoggingInterface().NETWORK,
         "Thread " + getName() + " has stopped (scheduled)");
      }

   /**
    * close the socket server, stops any thread, and notifies the listener
    */
   public void close()
      {
      SessionUtilities.getLoggingInterface().debugMsg(SessionUtilities.getLoggingInterface().DEBUG,
         SessionUtilities.getLoggingInterface().NETWORK,
         "removing socket server " + keyValue);
      try{
         running = false;
         socket.leaveGroup(myIpAddress);
         socket.close();
         dri.lostServerSocket(keyValue);
         // close all the spawned sockets
         for(Enumeration enum = connections.elements(); enum.hasMoreElements();)
            {
            MultiCastSocket mcs = (MultiCastSocket)enum.nextElement();
            mcs.close();
            }
         }
      catch(Exception e)
         {
         e.printStackTrace();
         }
      }

   /**
    * called by socket when it closes itself
    */
   public void closeSocket(MultiCastSocket socket)
      {
      String udpKey = socket.getRemoteIP().getHostAddress()+":"+socket.getLocalPort();
      SessionUtilities.getLoggingInterface().debugMsg(SessionUtilities.getLoggingInterface().DEBUG,
         SessionUtilities.getLoggingInterface().NETWORK,
         "removing/closing socket " + socket.getKeyValue());
      connections.remove(udpKey);
      }

   }
