/**
 * @author Masoom Shaikh
 * masoomshaikh@users.sourceforge.net
 */
package chatclient;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import java.io.*;
import java.util.*;
import common.*;

public class ClientAppFrame
    extends JFrame
    implements ActionListener, MessageReceiver
{
  private MessageReader clientReader;
  private Socket serverAddress = null;
  private ChatRoom chatRoom;
  private String username, password;

  private JList chatList = new JList();
  private JScrollPane chatListSP = new JScrollPane();
  private JLabel status = new JLabel();
  private JMenuBar menuBar = new JMenuBar();
  private JMenuItem login, logoff;
  /**
   * connectionMode can assume only two values viz. JCConstants.CSM_USER_LOGIN
   * and  JCConstants.CSM_REGISTER_USERNAME.This is used by method connect() to
   * ascertain whether we are logging in or registring with server.
   */
  private int connectionMode;
  /**
   * This boolean value specifies whether we are connected or not.
   */
  private boolean connected;
  /**
   * This boolean value specifies whether we are logged in or not.
   */
  private boolean userLogged;
  /**
   * currOpenJCF stands for Current Open JavaChatFrame
   * It maintains an list of currently open ChatFrames
   * e.g. user might be chatting with no. of recepients at a given time.
   * (key,value)=(recepient,ChatFrame)
   */
  private Hashtable currOpenCF = new Hashtable();

  public ClientAppFrame(String str)
  {
    super(str);
    Container cp = getContentPane();
    cp.setLayout(new BorderLayout());
    addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent we)
      {
        int choice = JOptionPane.showConfirmDialog(null,
            "Are you sure you want to exit",
            "Exit",
            JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION)
        {
          disconnect();
          System.exit(0);
        }
        chatRoom.setVisible(true);
      }
    });
    initMenuBar();
    setJMenuBar(menuBar);

    initChatJList();
    cp.add(chatListSP, BorderLayout.CENTER);
    cp.add(status, BorderLayout.SOUTH);
    logoff.setEnabled(false);
    connected = false;
    setLocation(350, 100);
    setSize(370, 570);
    setIconImage(new ImageIcon("resources\\CLIENT.GIF").getImage());
    setVisible(true);
    showStatus("Not Connected");
  }

  private void initChatJList()
  {
    chatList.addMouseListener(new MouseAdapter()
    {
      public void mouseClicked(MouseEvent me)
      {
        if (me.getClickCount() == 2)
        {
          String recepient = (String) chatList.getSelectedValue();
          /**
           * initially even if nothing is visible in user list (i.e. JList is
           * empty) double clicking produces ChatFrame
           * this is undesirable and we are preventing the same below by
           * checkingthst if recepient is null or not.
           */
          if (recepient == null)
          {
            return;
          }
          openNewCF(recepient);
        }
      }
    });
    chatList.setModel(new DefaultListModel());
    chatListSP.getViewport().add(chatList);
  }

  public static void main(String args[])
  {
    ClientAppFrame frame = new ClientAppFrame("JavaChat Client Frame");
  }

  /**
   * This function notifies the entries in currOpenCF whenever the recepient
   * for that window goes offline.
   * .i.e. whenever chatList is updated this function checks
   * to se that the users for whom we have open windows(i.e. entries in
   * currOpenCF)are still online.For those who are not online we will insert
   * appropriate text indicating them that the user has looged off.
   */
  private void notify_currOpenCF()
  {
    DefaultListModel model = (DefaultListModel) chatList.getModel();
    Enumeration enum = currOpenCF.keys();
    while (enum.hasMoreElements())
    {
      String usr = (String) enum.nextElement();
      if (!model.contains(usr))
      {
        ChatFrame fr = (ChatFrame) currOpenCF.get(usr);
        fr.appendToLogTA("*******************************************", true);
        fr.appendToLogTA("*****" + usr.toUpperCase() +
                         " HAS LEFT JavaChat*****", true);
        fr.appendToLogTA("*******************************************", true);
      }
    }
  }

  /**
   * This Method is called by ChatFrame to notify client.
   * @param msg contains message type information and message itself.
   * @see chatclient.ChatFrame
   */
  public void notifyClient(Message msg)
  {
    System.out.println("ChatFrame ---> CLIENT");
    msg.printMessage();
    switch (msg.type)
    {
      case JCConstants.CM_REMOVE_ENTRY:
        currOpenCF.remove(msg.recepient);
        break;
      case JCConstants.CM_FORWARD_MESSAGE_TO_RECEPIENT:
        /**
         * prepare Message Object to send to recepient
         */
        int type = JCConstants.CSM_FORWARD_MESSAGE_TO_RECEPIENT;
        Message ms = new Message(type, null, msg.message,
                                 username, msg.recepient, null);
        sendToServer(ms);
        break;
        case JCConstants.CR_FORWARD_MESSAGE:
          sendToServer(msg);
          break;
    }
  }
  /**
   * This function comes handy when we are required to change Look And Feel of
   * Application.
   * @param str specifies the LookAndFeel class name to be loaded to change
   * LooAndFeel
   */
  private void setUI(String str)
  {
    try
    {
      UIManager.setLookAndFeel(str);
      SwingUtilities.updateComponentTreeUI(this);
      repaint();
    }
    catch (UnsupportedLookAndFeelException ex)
    {
      JOptionPane.showMessageDialog(this, "UnsupportedLookAndFeelException",
                                    "Exception",
                                    JOptionPane.OK_OPTION);
    }
    catch (IllegalAccessException ex)
    {
      JOptionPane.showMessageDialog(this, "IllegalAccessException", "Exception",
                                    JOptionPane.OK_OPTION);
    }
    catch (InstantiationException ex)
    {
      JOptionPane.showMessageDialog(this, "InstantiationException", "Exception",
                                    JOptionPane.OK_OPTION);
    }
    catch (ClassNotFoundException ex)
    {
      JOptionPane.showMessageDialog(this, "ClassNotFoundException", "Exception",
                                    JOptionPane.OK_OPTION);
    }
  }

  private void showStatus(String str)
  {
    status.setText(str);
  }

  private void initMenuBar()
  {
    JMenuItem tmp;
    ButtonGroup group = new ButtonGroup();

    JMenu loginMenu = new JMenu("Login");
    JMenu themeMenu = new JMenu("Theme");
    JMenu windowMenu = new JMenu("Window");
    menuBar.add(loginMenu);
    menuBar.add(windowMenu);
    menuBar.add(themeMenu);
    loginMenu.setMnemonic('L');
    themeMenu.setMnemonic('T');
    windowMenu.setMnemonic('W');

    login = loginMenu.add(new JMenuItem("Login..."));
    login.setMnemonic('L');
    login.addActionListener(this);

    logoff = loginMenu.add(new JMenuItem("Log off..."));
    logoff.setMnemonic('O');
    logoff.addActionListener(this);

    tmp = loginMenu.add(new JMenuItem("Change user..."));
    tmp.setMnemonic('C');
    tmp.addActionListener(this);

    tmp = loginMenu.add(new JMenuItem("Register..."));
    tmp.setMnemonic('R');
    tmp.addActionListener(this);

    loginMenu.addSeparator();
    tmp = loginMenu.add(new JMenuItem("Exit"));
    tmp.setMnemonic('x');
    tmp.addActionListener(this);

    tmp = windowMenu.add(new JMenuItem("Show Room"));
    tmp.setMnemonic('S');
    tmp.addActionListener(this);

    tmp = windowMenu.add(new JMenuItem("Hide Room"));
    tmp.setMnemonic('H');
    tmp.addActionListener(this);

    tmp = windowMenu.add(new JMenuItem("Hide Client"));
    tmp.setMnemonic('C');
    tmp.addActionListener(this);

    tmp = themeMenu.add(new JRadioButtonMenuItem("Windows"));
    group.add(tmp);
    tmp.setMnemonic('W');
    tmp.addActionListener(this);

    tmp = themeMenu.add(new JRadioButtonMenuItem("Motif"));
    group.add(tmp);
    tmp.setMnemonic('M');
    tmp.addActionListener(this);

    tmp = themeMenu.add(new JRadioButtonMenuItem("Metal"));
    group.add(tmp);
    tmp.setSelected(true);
    tmp.setMnemonic('E');
    tmp.addActionListener(this);
  }

  public void actionPerformed(ActionEvent ae)
  {
    String command = ae.getActionCommand();
    if (command.equals("Login..."))
    {
      JavaChatLogin loginDialog = new JavaChatLogin(this,
          "Login Window", "Registered Users");
      if (loginDialog.getPressedButton().equals("Cancel"))
      {
        if (!connected)
        {
          int tp = JCConstants.CSM_USER_CANCELED_LOGIN;
          Message msg = new Message(tp, null, null, null, null, null);
          sendToServer(msg);
        }
        return;
      }
      username = loginDialog.getUsername();
      password = loginDialog.getPassword();
      setTitle("JavaChat Client: " + username);
      logoff.setEnabled(true);
      login.setEnabled(false);
      connectionMode = JCConstants.CSM_USER_LOGIN;
      connected = connect(loginDialog.getAddress());
      userLogged = (connected) ? true : false;
    }
    if (command.equals("Log off..."))
    {
      int choice = JOptionPane.showConfirmDialog(this,
          "Are you sure you want to log off",
          "Log off",
          JOptionPane.YES_NO_OPTION);
      if (choice == JOptionPane.YES_OPTION)
      {
        connected = disconnect();
        userLogged = false;
        logoff.setEnabled(false);
        login.setEnabled(true);
      }
    }
    if (command.equals("Change user..."))
    {
      JavaChatLogin loginDialog = new JavaChatLogin(this,
          "Login Window", "Login with different name");
      if (loginDialog.getPressedButton().equals("Cancel"))
      {
        if (!connected)
        {
          int tp = JCConstants.CSM_USER_CANCELED_LOGIN;
          Message msg = new Message(tp, null, null, null, null, null);
          sendToServer(msg);
        }
        return;
      }
      connected = disconnect();
      userLogged = (connected) ? true : false;
      username = loginDialog.getUsername();
      password = loginDialog.getPassword();
      setTitle("JavaChat Client: " + username);
      logoff.setEnabled(true);
      login.setEnabled(false);
      connectionMode = JCConstants.CSM_USER_LOGIN;
      connected = connect(loginDialog.getAddress());
      userLogged = (connected) ? true : false;
    }
    if (command.equals("Register..."))
    {
      JavaChatLogin loginDialog = new JavaChatLogin(this,
          "Login Window", "Register New User");
      if (loginDialog.getPressedButton().equals("Cancel"))
      {
        if (!connected)
        {
          int tp = JCConstants.CSM_USER_CANCELED_LOGIN;
          Message msg = new Message(tp, null, null, null, null, null);
          sendToServer(msg);
        }
        return;
      }
      connected = disconnect();
      username = loginDialog.getUsername();
      password = loginDialog.getPassword();
      setTitle("JavaChat Client: " + username);
      logoff.setEnabled(true);
      login.setEnabled(false);
      connectionMode = JCConstants.CSM_REGISTER_USERNAME;
      connected = connect(loginDialog.getAddress());
      userLogged = (connected) ? true : false;
    }
    if(command.equals("Show Room"))
    {
      if(chatRoom == null)
        return;
      chatRoom.setVisible(true);
    }
    if(command.equals("Hide Room"))
    {
      if(chatRoom == null)
        return;
      chatRoom.setVisible(false);
    }
    if(command.equals("Hide Client"))
    {
      if(chatRoom == null)
        return;
      chatRoom.setVisible(true);
      setVisible(false);
    }
    if (command.equals("Exit"))
    {
      int choice = JOptionPane.showConfirmDialog(this,
          "Are you sure you want to exit",
          "Exit",
          JOptionPane.YES_NO_OPTION);
      if (choice == JOptionPane.YES_OPTION)
      {
        disconnect();
        System.exit(0);
      }
    }
    if (command.equals("Windows"))
    {
      setUI("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
    }
    if (command.equals("Motif"))
    {
      setUI("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
    }
    if (command.equals("Metal"))
    {
      setUI("javax.swing.plaf.metal.MetalLookAndFeel");
    }
  }

  /**
   * This function connects to server and spawns a MessageReader thread so that
   * we can read Message objects from server.It also sends a Message object to
   * server specifiyng whether we want to login or register as a new user.This
   * decision is taken based on the value of loginMode
   * @see chatclient.ClientAppFrame.connectionMode
   * @param address connects to address specified
   * @return returns true if connected sucessfully.
   */
  private boolean connect(String address)
  {
    if (connected)
    {
      return true;
    }
    if (address.equals(""))
    {
      address = "127.0.0.1";
    }
    try
    {
      serverAddress = new Socket(InetAddress.getByName(address),
                                 JCConstants.SERVER_LISTENING_PORT);
      chatRoom = new ChatRoom(username);
      chatRoom.client = this;
    }
    catch (ConnectException cxp)
    {
      /**
       * ConnectException occurs means,server is not running.Let the user know
       * the same via a JOptionPane.
       */
      JOptionPane.showMessageDialog(this, "Error:Server is not running",
                                    "Error", JOptionPane.ERROR_MESSAGE);
      logoff.setEnabled(false);
      login.setEnabled(true);
      showStatus("Server is not running");
      return false;
    }
    catch (NoRouteToHostException ex1)
    {
      ex1.printStackTrace(System.out);
      return false;
    }
    catch (UnknownHostException ex2)
    {
      ex2.printStackTrace(System.out);
      return false;
    }

    catch (IOException ex3)
    {
      ex3.printStackTrace(System.out);
      return false;
    }
    /**
     * Dispatch a Thread to read messages from Server
     */
    clientReader = new MessageReader(this, serverAddress);
    /**
     * OK,now we are connected to server.
     * we are requesting to server to send us chatList.
     * prepare a Message Object and send it to server requesting logged
     * user list,only username is required,and of course 'msg.type' too.
     */
    Message msg = new Message(connectionMode, null, null,
                              username, null, password);
    sendToServer(msg);
    showStatus("connected");
    return true;
  }

  /**
   * This method is used to send a Message object to server.
   * @param m sends Message m to server
   * @return returns true if message sent sucessfuly.
   */
  private boolean sendToServer(Message m)
  {
    try
    {
      ObjectOutputStream out = new ObjectOutputStream(serverAddress.
          getOutputStream());
      out.writeObject(m);
      return true;
    }
    catch (IOException ex)
    {
      ex.printStackTrace(System.out);
      return false;
    }
  }

  /**
   * This method disconnects client from server by sending a Message object to
   * server.It also kills the thread spawned previously by connect()
   * @return returns false on sucessfull disconnection
   */
  private boolean disconnect()
  {
    if (!connected)
    {
      return false;
    }
    /**
     * We will close chatRoom frame before we quit connection.
     */
    chatRoom.dispose();
    /**
     * we must clear the user visible list.
     */
    DefaultListModel model = (DefaultListModel) chatList.getModel();
    model.clear();
    /**
     * we must clear our currOpenCF also and close all ChatFrame which are open
     */
    Iterator iter = currOpenCF.values().iterator();
    while (iter.hasNext())
    {
      ChatFrame fr = (ChatFrame) iter.next();
      fr.dispose();
    }
    currOpenCF.clear();
    /**
     * we have choosen to disconnect,so if are logged in then we prepare a Log
     * off Message and notify Server of the same.
     */
    if (userLogged)
    {
      Message msg = new Message(JCConstants.CSM_USER_LOGOFF, null, null,
                                username, null, null);
      sendToServer(msg);
    }
    /**
     * we will stop the clienReader Thread before disconnecting.
     * and close ChatRoom as well.
     */
    clientReader.stop();
    chatRoom.dispose();
    /**
     * Now we will have to close open Socket to server,as below.
     */
    try
    {
      serverAddress.close();
      showStatus("Not connected");
      return false;
    }
    catch (IOException ex)
    {
      System.err.println("ClienAppFrame:376" + ex.toString());
      showStatus("Failed to disconnect");
      return true;
    }
  }

  /**
   * NOTE: It is due to this function we are able to make ourselves
   * available in ChatFrame.Because to achieve this we need to pass our
   * reference to ChatFrame which can be done by using Java keyword 'this'
   * ,and that is something we could not do in actionPerformed() method,since
   *  the actionPerformed() method is member function of a anonymous class
   * and 'this' will reference to an instance of that anonymous class.
   * @param rec the recepient.
   */
  private void openNewCF(String rec)
  {
    ChatFrame chatframe;
    /**
     * First we make sure that ChatFrame is not already open,if it is then
     * simply return or else open a new one(i.e. if it is not open).
     */
    chatframe = (ChatFrame) currOpenCF.get(rec);
    if (chatframe != null)
    {
      return;
    }
    /**
     * we failed to find an open ChatFrame,so we will open a new one.
     * make an entry in Hashtable currOpenJCF and finally
     * make ourselves available in chatframe so that it
     * can notify us when it closes,by calling removeEntry()
     */
    chatframe = new ChatFrame(username, rec);
    currOpenCF.put(rec, chatframe);
    chatframe.client = this;
  }

  /**
   * This method is called when a new message is received by ClientReader.It
   * holds all the logic of the application.All the data structures are
   * updated by this method.
   * @param msg contains information regarding type as well as other parameters.
   * @param soc The Socket address of Server,from where this message was read.
   * @see common.Message
   * @see chatclient.ClientReader
   */
  public void receiveMessage(Message msg, Socket soc)
  {
    System.out.println("SERVER ---> CLIENT");
    msg.printMessage();
    /**
     * Server has been shutdown, or server has logged us off(i.e. deleted us)
     * or we have logged on a different machine.
     */
    if(msg.type == JCConstants.CR_FORWARD_MESSAGE)
    {
      if(username.equals(msg.username))
        return;
      chatRoom.appendToLogTA(msg,false);
      return;
    }
    if ( (msg.type == JCConstants.CSM_SERVER_SHUTDOWN) ||
        (msg.type == JCConstants.CSM_USER_LOGOFF) ||
        (msg.type == JCConstants.CSM_USER_LOGGED_ON_DIFFERENT_MACHINE))
    {
      DefaultListModel model = (DefaultListModel) chatList.getModel();
      Enumeration enum = model.elements();
      while (enum.hasMoreElements())
      {
        try
        {
          String usr = (String) enum.nextElement();
          ChatFrame frame = null;
          frame = (ChatFrame) currOpenCF.get(usr);
          frame.dispose();
        }
        catch (NullPointerException ex1)
        {
          ex1.printStackTrace(System.out);
        }
      }
      chatRoom.dispose();
      clientReader.stop();
      model.removeAllElements();
      currOpenCF.clear();
      connected = false;
      userLogged = false;
      logoff.setEnabled(false);
      login.setEnabled(true);
      String prompt = null;
      switch (msg.type)
      {
        case JCConstants.CSM_USER_LOGOFF:
          prompt = "JavaChat Server Logged You Off !";
          break;
        case JCConstants.CSM_SERVER_SHUTDOWN:
          prompt = "Server Was Shutdown";
          break;
        case JCConstants.CSM_USER_LOGGED_ON_DIFFERENT_MACHINE:
          prompt =
              "You Have Been Logged Off As You Have Logged On A Different Machine";
          break;
      }
      JOptionPane.showMessageDialog(this, prompt, "Error",
                                    JOptionPane.ERROR_MESSAGE);
      showStatus("Not connected");
    }
    if (msg.type == JCConstants.CSM_ERROR_BAD_PASSWORD)
    {

      JavaChatLogin loginDialog = new JavaChatLogin(this, "Error in Login",
          "Invalid Password");
      if (loginDialog.getPressedButton().equals("Cancel"))
      {
        int tp = JCConstants.CSM_USER_CANCELED_LOGIN;
        Message cancel = new Message(tp, null, null, null, null, null);
        sendToServer(cancel);
        login.setEnabled(true);
        logoff.setEnabled(false);
        return;
      }
      username = loginDialog.getUsername();
      password = loginDialog.getPassword();
      userLogged = false;
      connected = disconnect();
      connectionMode = JCConstants.CSM_USER_LOGIN;
      connected = connect(loginDialog.getAddress());
    }
    if (msg.type == JCConstants.CSM_ERROR_UNKNOWN_USERNAME)
    {

      JavaChatLogin loginDialog = new JavaChatLogin(this, "Error in Login",
          "Username " + username + " does not exist");
      if (loginDialog.getPressedButton().equals("Cancel"))
      {
        int tp = JCConstants.CSM_USER_CANCELED_LOGIN;
        Message cancel = new Message(tp, null, null, null, null, null);
        sendToServer(cancel);
        login.setEnabled(true);
        logoff.setEnabled(false);
        return;
      }
      username = loginDialog.getUsername();
      password = loginDialog.getPassword();
      userLogged = false;
      connected = disconnect();
      connectionMode = JCConstants.CSM_USER_LOGIN;
      connected = connect(loginDialog.getAddress());
    }
    /**
     * server will not allow user to login,unless user provides a unbound and
     * registered username as well as a valid password.
     */
    if (msg.type == JCConstants.CSM_ERROR_USERNAME_ALREADY_BOUND)
    {
      JavaChatLogin loginDialog = new JavaChatLogin(this, "Error in Login",
          "Username already in use");
      if (loginDialog.getPressedButton().equals("Cancel"))
      {
        int tp = JCConstants.CSM_USER_CANCELED_LOGIN;
        Message cancel = new Message(tp, null, null, null, null, null);
        sendToServer(cancel);
        login.setEnabled(true);
        logoff.setEnabled(false);
        return;
      }
      username = loginDialog.getUsername();
      password = loginDialog.getPassword();
      setTitle("JavaChat Client: " + username);
      logoff.setEnabled(true);
      login.setEnabled(false);
      userLogged = false;
      connected = disconnect();
      connectionMode = JCConstants.CSM_USER_LOGIN;
      connected = connect(loginDialog.getAddress());
    }
    /**
     * message from Server to update our chatList.
     */
    if (msg.type == JCConstants.CSM_UPDATE_USER_LIST)
    {
      /**
       * first we will update our list and then we will notify for who is
       * logged in or logged out
       */
      userLogged = true;
      DefaultListModel model = (DefaultListModel) chatList.getModel();
      model.clear();
      Iterator iter = msg.userList.iterator();
      while (iter.hasNext())
      {
        model.addElement(iter.next());
      }
      notify_currOpenCF();
    }
    /**
     * we have received a message from an online user
     */
    if (msg.type == JCConstants.CSM_FORWARD_MESSAGE_TO_RECEPIENT)
    {
      /**
       * obtain the ChatFrame corresponding to this user
       */
      try
      {
        ChatFrame frame = (ChatFrame) currOpenCF.get(msg.username);
        /**
         * we found a open window to the recepient, append the received message
         * into this frame.
         */
        frame.appendToLogTA(msg.message, false);
      }
      catch (NullPointerException ex)
      {
        /**
         * NullPointerException has been thrown,this means msg.recepient is
         * not in our currOpenCF(i.e. we don't hava a open window with him).
         * This means a online user has invited us to chat with him,so open
         * a ChatFrame for the same.We must have this.username=msg.recepient,
         * since we are recepient for the sender.
         */
        ChatFrame fra = new ChatFrame(username, msg.username);
        /**
         * Make ourselves avalable in this newly created ChatFrame.This is
         * very important or else it will not be able to communicate
         * back to us.We do the same below.
         */
        fra.client = this;
        fra.appendToLogTA(msg.message, false);
        /**
         * update our chatList(JList) as well as currOpenCF(Hashtable).
         */
        currOpenCF.put(msg.username, fra);
        DefaultListModel model = (DefaultListModel) chatList.getModel();
        /**
         * add only if recepient = msg.username is not present in JList.
         */
        if (!model.contains(msg.username))
        {
          model.addElement(msg.username);
        }
      }
    }
  }
}
