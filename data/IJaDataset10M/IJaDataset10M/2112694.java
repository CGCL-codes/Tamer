// This is copyrighted source file, part of Rakiura eMbot package. 
// See the file LICENSE for copyright information and the terms and conditions
// for copying, distributing and modifying Rakiura eMbot package.
// @copyright@

package org.rakiura.mbot;

/**/
import java.net.*;
import java.io.*;
import java.util.*;

/**
 * EPassConnectionThread.java<br><br>
 * Created: Thu Jul 15 12:38:56 1999
 *
 *@author Mariusz Nowostawski
 *@version $Revision: 1.2 $
 */
public class EPassConnectionThread extends BasicConnection {
 
  private boolean listening;
  private Engine engine;
  private boolean secondaryMaster;

  /**/
  public EPassConnectionThread(Engine engine) {
    hostPort = engine.MasterPort;
    hostName = engine.MasterHost;
    this.engine = engine;
    listening = true;
    secondaryMaster = false;
  }
  /**/
  public EPassConnectionThread(String host, Integer port, Engine engine) {
    this.hostPort = port.intValue();
    this.hostName = host;
    this.engine = engine;
    listening = true;
    secondaryMaster = false;
  }
    
  /**/
  public void setSecondaryMaster(){ secondaryMaster = true; }

  /**/
  public void run() {
    try {
      socket = new Socket(hostName, hostPort);
      out = new ObjectOutputStream(socket.getOutputStream());
      in = new ObjectInputStream(socket.getInputStream());
      System.out.print("Connecting to master "+hostName+":"+hostPort+"  ...... ");

      /// loging into the IRC server
      String line = (String)in.readObject();//read welcome
      out.writeObject(engine.getNick());//write nick
      out.writeObject(new Integer(engine.EDCCport));//write port
      out.writeObject(engine.EDCCpass);//write pass
      out.flush();
      line = (String)in.readObject();//read confirmation

      System.out.println(" ok!");

      if(!secondaryMaster){
        synchronized(this){
          out.writeObject(".give_userlist");
          out.flush();
          engine.readUsersObject(in);
          out.writeObject(".give_hostlist");
          out.flush();
          String o = (String)in.readObject();
          while(!o.startsWith("=endOfHostList")){
            String ohost = o;
            Integer oport = (Integer)in.readObject();
            engine.etcpServer.connectTo(ohost, oport);
            o = (String)in.readObject();
          }
          System.out.println("Bots data uploaded.");
        }
      }
      while(active && line != null){
        line = (String)in.readObject();//read request

        // userlist request
        if(line.startsWith(".give_userlist")){
          engine.writeUsersObject(out);
        }else
          // userlist update needed
          if(line.startsWith("=update_userlist")){
            synchronized(this){
              out.writeObject(".give_userlist");
              out.flush();
              engine.readUsersObject(in);
            }
          }else
            //
            if(line.startsWith("=action ")){
              engine.actionsSet.add(line.substring(8));
            }else   
              // hostlist request
              if(line.startsWith(".give_hostlist")){
                synchronized(this){
                  Enumeration enum = engine.etcpServer.getAllConnectionsExceptMe(this);
                  if(enum!=null)
                    while(enum.hasMoreElements()){
                      BasicConnection b = (BasicConnection)enum.nextElement();
                      out.writeObject(b.getHostName());
                      out.writeObject(new Integer(b.getHostPort()));
                    }
                  out.writeObject("=endOfHostList");
                }

                System.out.println("Bot hosts/ports uploaded.");
              }else
                if(line.startsWith("=update_hostlist")){
                  synchronized(this){
                    out.writeObject(".give_hostlist");
                    out.flush();
                    String o = (String)in.readObject();
                    while(!o.startsWith("=endOfHostList")){
                      String ohost = o;
                      Integer oport = (Integer)in.readObject();
                      engine.etcpServer.connectTo(ohost, oport);
                      o = (String)in.readObject();
                    }
                    System.out.println("Bots data uploaded.");
                  }
                }else
                  // .ex command
                  if(line.startsWith(".ex ")){
                    String action = line.substring(4);
                    engine.writeLine(action);
                  }  
      }

      engine.etcpServer.removeMaster(this);
      engine.switchToMode(1);
      System.out.println("Switching to mode 1... closed connection to master.");
    } catch (UnknownHostException e) {
      System.out.println("Don't know about hostname of another eMbot.. check configuration file.");
      e.printStackTrace();
      engine.etcpServer.removeMaster(this);
      engine.switchToMode(1);
      return;
    } catch (IOException e) {
      System.out.println("I/O problem during the connection to another eMbot.... connection closed.\nThe other eMbot is going down, lost TCP handle.\nI am switching to Master Mode.");
      // e.printStackTrace();
      System.out.println(e.getMessage());
      engine.etcpServer.removeMaster(this);
      engine.switchToMode(1);
      return;
    } catch (Exception e) {
      System.out.println("Couldn't connect for some reasons to another eMbot...");
      engine.etcpServer.removeMaster(this);
      engine.switchToMode(1);
      return;
    }
  }
    
  public void setListening(boolean b){ listening = b; }

} // EPassConnectionThread
//////////////////// end of file ////////////////////
