/******************************************************************************
 *
 * Copyright (c) 1998,99 by Mindbright Technology AB, Stockholm, Sweden.
 *                 www.mindbright.se, info@mindbright.se
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *****************************************************************************
 * $Author: nallen $
 * $Date: 2001/11/12 16:31:13 $
 * $Name:  $
 *****************************************************************************/
package mindbright.application;

import java.io.*;

import java.applet.Applet;

import java.awt.*;
import java.awt.event.*;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Hashtable;
import java.util.Enumeration;

import mindbright.ssh.*;
import mindbright.security.*;
import mindbright.terminal.*;

public class MindTerm extends Applet implements Runnable {

  static Properties paramTermProps = new Properties();
  static Properties paramSSHProps  = new Properties();

  public static String javaVersion = "<unknown>";
  public static String javaVendor  = "<unknown>";
  public static String osName      = "<unknown>";
  public static String osArch      = "<unknown>";
  public static String osVersion   = "<unknown>";

  Frame                frame;
  TerminalWin          term;
  SSHInteractiveClient client;
  SSHInteractiveClient sshClone;
  SSHStdIO             console;
  Thread               clientThread;

  boolean    mergedTermProps;
  Properties sshProps;
  Properties termProps;

  String[]   cmdLineArgs;

  String  commandLine = null;
  String  sshHomeDir  = null;
  String  propsFile   = null;

  boolean usePopMenu = false;
  boolean haveMenus  = true;
  boolean haveGUI    = true;
  boolean cmdsh      = false;
  boolean quiet      = true;

  boolean helpinfo   = true;
  String  startupMsg = null;

  boolean doSCP        = false;
  boolean recursiveSCP = false;
  boolean toRemote     = true;
  int     firstArg     = 0;

  boolean autoSaveProps = true;
  boolean autoLoadProps = true;

  boolean savePasswords = false;

  int     popButtonNum = 3;

  boolean isClosing  = false;

  // !!!
  boolean     separateFrame = true;
  boolean     weAreAnApplet = false;

  static Hashtable terminals = new Hashtable();

  static synchronized boolean isLastTerminal() {
    return terminals.isEmpty();
  }

  static synchronized void addTerminal(MindTerm mindterm) {
    terminals.put(mindterm, mindterm);
  }

  static synchronized void removeTerminal(MindTerm mindterm) {
    terminals.remove(mindterm);
  }

  public MindTerm() {
    super();
    this.sshProps  = paramSSHProps;
    this.termProps = paramTermProps;
    addTerminal(this);
  }

  public MindTerm(Properties sshProps, Properties termProps) {
    this.sshProps  = sshProps;
    this.termProps = termProps;
    addTerminal(this);
  }

  public static void main(String[] argv) {
    MindTerm controller    = new MindTerm(paramSSHProps, paramTermProps);
    controller.cmdLineArgs = argv;
    
    try {
      controller.getApplicationParams();
    } catch (Exception e) {
	System.out.println("Error: " + e.getMessage());
	System.exit(1);
    }

    try {
      controller.run();
    } catch (Exception e) {
      System.out.println("Error, please mail below stack-trace to mats@mindbright.se");
      e.printStackTrace();
    }
  }

  public void init() {
	  /*if(SSH.NETSCAPE_SECURITY_MODEL) {
      try {
	netscape.security.PrivilegeManager.enablePrivilege("TerminalEmulator");
      } catch (netscape.security.ForbiddenTargetException e) {
	// !!!
      }
	   }*/
    weAreAnApplet  = true;
    autoSaveProps  = false;
    autoLoadProps  = false;
    savePasswords  = false;

    getAppletParams();
    (new Thread(this)).start();
  }

  public void run() {
    try {
      if(sshClone != null) {
	client = new SSHInteractiveClient(sshClone);
	sshClone = null;
      } else {
	  // Default when running single command is not to allocate a PTY
	  //
	  if(commandLine != null && sshProps.getProperty("forcpty") == null) {
	      sshProps.put("forcpty", "false");
	  }

	  SSHPropertyHandler propsHandler = new SSHPropertyHandler(sshProps);

	  if(propsFile != null) {
	      // !!! REMOVE (todo: fix password!)
	      try {
		  propsHandler = SSHPropertyHandler.fromFile(propsFile, "");
	      } catch (SSHClient.AuthFailException e) {
		  throw new Exception("Sorry, can only use passwordless settings files for now");
	      }
	      propsHandler.mergeProperties(sshProps);
	  }

	  client = new SSHInteractiveClient(quiet, cmdsh, propsHandler);
      }

      console = (SSHStdIO)client.getConsole();

      if(SSHInteractiveClient.licensed) {
	  client.wantHelpInfo       = helpinfo;
	  client.customStartMessage = startupMsg;
      }

      // If we loaded a specific property-file we merge the termProps from there
      //
      if(client.getPropertyHandler().getInitTerminalProperties() != null) {
	Properties newTermProps =
	    new Properties(client.getPropertyHandler().getInitTerminalProperties());
	if(termProps != null && !termProps.isEmpty()) {
	  Enumeration enum = termProps.keys();
	  while(enum.hasMoreElements()) {
	    String name = (String)enum.nextElement();
	    newTermProps.put(name, termProps.getProperty(name));
	  }
	  mergedTermProps = true;
	}
	termProps = newTermProps;
      }

      // First we initialize the GUI if we have one (to be able to set
      // properties to terminal
      //
      if(haveGUI) {
	initGUI();
	console.setTerminal(term);
	console.setOwnerContainer(frame);
	console.setOwnerName(SSH.VER_MINDTERM);
	console.updateTitle();
	try {
	  while(!frame.isShowing())
	    Thread.sleep(50);
	} catch(InterruptedException e) {
	    // !!!
	}

	// !!! When we are in the Panel of the Applet we don't get a componentShown event
	// so we have to "emulate" it since we depend on it to be correctly drawn
	//
	if(!separateFrame) {
	  term.emulateComponentShown();
	}
      }

      client.printCopyright();

      client.getPropertyHandler().setSSHHomeDir(sshHomeDir);
      client.getPropertyHandler().setAutoSaveProps(autoLoadProps);
      client.getPropertyHandler().setAutoLoadProps(autoSaveProps);
      client.getPropertyHandler().setSavePasswords(savePasswords);
      client.updateMenus();

      if(commandLine != null) {
	if(!doSCP) {
	  client.doSingleCommand(commandLine, false, 0);
	} else {
	  if((cmdLineArgs.length - firstArg) < 2)
	      throw new Exception("scp must have at least two arguments (<source> <destination>)");
	  String[] fileList = new String[cmdLineArgs.length - firstArg - 1];
	  String  target, source = commandLine.substring(0, commandLine.lastIndexOf(' '));
	  for(int i = firstArg; i < cmdLineArgs.length - 1; i++) {
	      fileList[i - firstArg] = cmdLineArgs[i];
	  }
	  target = cmdLineArgs[cmdLineArgs.length - 1];
	  String srvHost = client.getPropertyHandler().getSrvHost();
	  int    srvPort = client.getPropertyHandler().getSrvPort();
	  SSHSCP scp = new SSHSCP(srvHost, srvPort, client.getPropertyHandler(),
				  new File("."), SSH.DEBUG, recursiveSCP);
	  if(SSH.DEBUG) {
	      scp.setInteractor(client);
	      scp.setIndicator(new SSHSCPStdoutIndicator());
	  }
	  if(toRemote) {
	      scp.copyToRemote(fileList, target);
	  } else {
	      scp.copyToLocal(target, source);
	  }
	}
      } else {
	try {
	  clientThread = new Thread(client);
	  clientThread.start();
	  clientThread.join();
	} catch(InterruptedException e) {
	  // !!!
	}
      }

    } catch (IllegalArgumentException ae) {
      if(client != null)
	client.alert(ae.getMessage());
      System.out.println(ae.getMessage());
    } catch (FileNotFoundException fe) {
      System.out.println("Settings-file not found: " + fe.getMessage());
    } catch (Exception e) {
      if(client != null)
	client.alert("Error: "  + e.getMessage());
      System.out.println("Error: " + e.getMessage());
      if(SSH.DEBUGMORE) {
	System.out.println("Please send the below stack-trace to mats@mindbright.se");
	e.printStackTrace();
      }
    }

    windowClosing(null);
    if(isLastTerminal())
      doExit();
  }

  public void getAppletParams() {
    String    name;
    String    value;
    String    param;
    int       i;

    try {
      separateFrame = (new Boolean(getParameter("sepframe"))).booleanValue();
    } catch (Exception e) {
      separateFrame = true;
    }
    try {
      SSH.DEBUG = (new Boolean(getParameter("verbose"))).booleanValue();
    } catch (Exception e) {
      SSH.DEBUG = false;
    }

    try {
      SSH.DEBUGMORE = (new Boolean(getParameter("debug"))).booleanValue();
      SSH.DEBUG = SSH.DEBUGMORE;
    } catch (Exception e) {
    }

    try {
      quiet = (new Boolean(getParameter("quiet"))).booleanValue();
    } catch (Exception e) {
      quiet = true;
    }

    try {
      savePasswords = (new Boolean(getParameter("savepasswords"))).booleanValue();
    } catch (Exception e) {
      savePasswords = false;
    }

    try {
      cmdsh = (new Boolean(getParameter("cmdsh"))).booleanValue();
    } catch (Exception e) {
      cmdsh = false;
    }

    try {
      helpinfo = (new Boolean(getParameter("helpinfo"))).booleanValue();
    } catch (Exception e) {
      helpinfo = true;
    }

    startupMsg = getParameter("startmsg");

    param = getParameter("menus");
    if(param != null) {
      if(param.equals("no"))
	haveMenus = false;
      else if(param.startsWith("pop")) {
	getPopupButtonNumber(param);
	usePopMenu = true;
      }
    }

    param = getParameter("autoprops");
    if(param != null) {
      if(param.equals("save")) {
	autoSaveProps = true;
	autoLoadProps = false;
      } else if(param.equals("load")) {
	autoSaveProps = false;
	autoLoadProps = true;
      } else if(param.equals("both")) {
	autoSaveProps = true;
	autoLoadProps = true;
      }
    }

    sshHomeDir  = getParameter("sshhome");
    propsFile   = getParameter("propsfile");
    commandLine = getParameter("commandline");

    getDefaultParams();

    for(i = 0; i < SSHPropertyHandler.defaultPropDesc.length; i++) {
      name  = SSHPropertyHandler.defaultPropDesc[i][SSHPropertyHandler.PROP_NAME];
      value = getParameter(name);
      if(value != null)
	paramSSHProps.put(name, value);
    }
    i = 0;
    while((value = getParameter("local" + i)) != null) {
      paramSSHProps.put("local" + i, value);
      i++;
    }
    i = 0;
    while((value = getParameter("remote" + i)) != null) {
      paramSSHProps.put("remote" + i, value);
      i++;
    }

    for(i = 0; i < TerminalDefProps.defaultPropDesc.length; i++) {
      name  = TerminalDefProps.defaultPropDesc[i][TerminalDefProps.PROP_NAME];
      value = getParameter(name);
      if(value != null)
	termProps.put(name, value);
    }

    param = getParameter("appletbg");
    if(param != null) {
	Color c;
	try {
	    c = TerminalWin.getTermColor(param);
	} catch (IllegalArgumentException e) {
	    try {
		c = TerminalWin.getTermRGBColor(param);
	    } catch (Throwable t) {
		c = null;
		// !!!
	    }
	}
	if(c != null)
	    this.setBackground(c);
    }
  }

  public void getApplicationParams() throws Exception {
    String    name;
    String    value;
    int       numOfOpts;
    int       i;

    // First we check the MindTerm options (i.e. not the ssh/terminal-properties)
    //
    try {
      for(i = 0; i < cmdLineArgs.length; i++) {
	String arg = cmdLineArgs[i];
	if(!arg.startsWith("--"))
	  break;
	switch(arg.charAt(2)) {
	case 'h':
	  sshHomeDir = cmdLineArgs[++i];
	  break;
	case 'f':
	  propsFile = cmdLineArgs[++i];
	  break;
	case 'c':
	  cmdsh = true;
	  break;
	case 'd':
	  haveGUI = false;
	  break;
	case 'm': {
	  String typ = cmdLineArgs[++i];
	  if(typ.equals("no"))
	    haveMenus = false;
	  else if(typ.startsWith("pop")) {
	    getPopupButtonNumber(typ);
	    usePopMenu = true;
	  } else
	    throw new Exception("value of '--m' must be 'no', 'pop1', 'pop2', or 'pop3'");
	  break;
	}
	case 'p': {
	  String typ = cmdLineArgs[++i];
	  if(typ.equals("save")) {
	    autoSaveProps = true;
	  } else if(typ.equals("load")) {
	    autoLoadProps = true;
	  } else if(typ.equals("both")) {
	    autoSaveProps = true;
	    autoLoadProps = true;
	  } else if(typ.equals("none")) {
	    autoSaveProps = false;
	    autoLoadProps = false;
	  } else
	    throw new Exception("value of '--p' must be 'save', 'load', 'both', or 'none'");
	  break;
	}
	case 'q':
	    String val = cmdLineArgs[++i];
	    if(val.equalsIgnoreCase("true") || val.equalsIgnoreCase("false")) {
		quiet = Boolean.valueOf(val).booleanValue();
	    } else {
		throw new Exception("value of '--q' must be 'true' or 'false'");
	    }
	    break;
	case 'r':
	  recursiveSCP = true;
	  break;
	case 's':
	  haveGUI = false;
	  doSCP   = true;
	  String direction = cmdLineArgs[++i];
	  if(direction.equalsIgnoreCase("toremote")) {
	      toRemote = true;
	  } else if(direction.equalsIgnoreCase("tolocal")) {
	      toRemote = false;
	  } else {
	      throw new Exception("value of '--s' must be 'toremote' or 'tolocal'");
	  }
	  break;
	case 'v':
	  System.out.println("verbose mode selected...");
	  SSH.DEBUG = true;
	  break;
	case 'x':
	  savePasswords = true;
	  break;
	case 'V':
	  System.out.println(SSH.VER_MINDTERM);
	  System.out.println("SSH protocol version " + SSH.SSH_VER_MAJOR + "." + SSH.SSH_VER_MINOR);
	  System.exit(0);
	  break;
	case 'D':
	  SSH.DEBUG     = true;
	  SSH.DEBUGMORE = true;
	  break;
	case '?':
	  printHelp();
	  System.exit(0);
	default:
	  throw new Exception("unknown parameter '" + arg + "'");
	}
      }
    } catch (Exception e) {
      printHelp();
      throw e;
    }

    getDefaultParams();

    numOfOpts = i;
    for(i = numOfOpts; i < cmdLineArgs.length; i += 2) {
      name = cmdLineArgs[i];
      if((name.charAt(0) != '-') || ((i + 1) == cmdLineArgs.length))
	break;
      name  = name.substring(1);
      value = cmdLineArgs[i + 1];
      if(SSHPropertyHandler.isProperty(name))
	paramSSHProps.put(name, value);
      else if(TerminalDefProps.isProperty(name))
	paramTermProps.put(name, value);
      else
	System.out.println("Unknown property '" + name + "'");
    }

    if(i < cmdLineArgs.length) {
      firstArg = i;
      commandLine = "";
      for(; i < cmdLineArgs.length; i++) {
	commandLine += cmdLineArgs[i] + " ";
      }
      commandLine = commandLine.trim();
    }
  }

  void printHelp() {
    System.out.println("usage: MindTerm [options] [properties] [command]");
    System.out.println("Options:");
    System.out.println("  --c            Enable local command-shell.");
    System.out.println("  --d            No terminal-window, only dumb command-line and port-forwarding.");
    System.out.println("  --f <file>     Use settings from the given file.");
    System.out.println("  --h dir        Name of the MindTerm home-dir (default: ~/mindterm/).");
    System.out.println("  --m <no | pop | popN>");
    System.out.println("                 Use no menus or popup (on mouse-button N) menu instead of menubar.");
    System.out.println("  --p <save | load | both | none>");
    System.out.println("                 Sets automatic save/load flags for property-files.");
    System.out.println("  --q <true | false>");
    System.out.println("                 Quiet; don't query for server/username if given.");
    System.out.println("  --v            Verbose; display verbose messages.");
    System.out.println("  --x            Save passwords in property-files.");
    System.out.println("  --D            Debug; display extra debug info.");
    System.out.println("  --V            Version; display version number only.");
    System.out.println("  --?            Help; display this help.");
  }

  void getPopupButtonNumber(String param) {
    if(param.length() == 4) {
      try {
	popButtonNum = Integer.valueOf(param.substring(3)).intValue();
	if(popButtonNum < 1 || popButtonNum > 3)
	  popButtonNum = 3;
      } catch (NumberFormatException e) {
	// !!!
      }
    }
  }

  void getDefaultParams() {
    try {
      if(sshHomeDir == null) {
	String hDir = System.getProperty("user.home");
	if(hDir == null)
	  hDir = System.getProperty("user.dir");
	if(hDir == null)
	  hDir = System.getProperty("java.home");
	sshHomeDir = (hDir + File.separator + "mindterm" + File.separator);
      }
    } catch (Throwable t) {
      // !!!
    }
    if(weAreAnApplet)
      paramSSHProps.put("server", getCodeBase().getHost());
    try {
      if(!quiet)
	paramSSHProps.put("usrname", System.getProperty("user.name", ""));
    } catch (Throwable t) {
      // !!!
    }
    try {
      javaVersion = System.getProperty("java.version");
      javaVendor  = System.getProperty("java.vendor");
      osName      = System.getProperty("os.name");
      osArch      = System.getProperty("os.arch");
      osVersion   = System.getProperty("os.version");
    } catch (Throwable t) {
      // !!!
    }

  }

  public void initGUI() {
    Container container;
    MenuBar   menubar = null;
    if(separateFrame) {
      frame = new Frame();
      frame.addWindowListener(new WindowAdapter() {
	public void windowClosing(WindowEvent e)  { MindTerm.this.windowClosing(e); }
	public void windowDeiconified(WindowEvent e)  { term.requestFocus(); }
      });
      container = frame;
      if(haveMenus && !usePopMenu) {
	menubar = new MenuBar();
	frame.setMenuBar(menubar);
	frame.addNotify();
	frame.validate();
      }
    } else {
      Component comp = this;
      do {
	comp = comp.getParent();
      } while(!(comp instanceof Frame));
      frame = (Frame)comp;
      container = this;
    }

    term = new TerminalWin(frame, new TerminalXTerm(), termProps);

    if(mergedTermProps)
      term.setPropsChanged(true);

    if(haveMenus) {
      SSHMenuHandler      menus;
      TerminalMenuHandler tmenus;
      try {
	  Class c;
	  c = Class.forName("mindbright.ssh.SSHMenuHandlerFull");
	  menus = (SSHMenuHandler)c.newInstance();
	  menus.init(this, client, frame, term);
	  c = Class.forName("mindbright.terminal.TerminalMenuHandlerFull");
	  tmenus = (TerminalMenuHandler)c.newInstance();
	  tmenus.setTerminalWin(term);
	  term.setMenus(tmenus);
	  client.setMenus(menus);
	  if(menubar == null) {
	      PopupMenu popupmenu = term.getPopupMenu("MindTerm Menu");
	      menus.preparePopupMenu(popupmenu);
	      menus.setPopupButton(popButtonNum);
	  } else {
	      menus.prepareMenuBar(menubar);
	  }
	  tmenus.setTerminalMenuListener(menus);
      } catch (Throwable t) {
	  System.out.println("Full menus can't be enabled since classes are missing");
	  term.setMenus(null);
	  client.setMenus(null);
      }
    }

    container.setLayout(new BorderLayout());
    container.add(term.getPanelWithScrollbar(), BorderLayout.CENTER);

    frame.pack();
    frame.show();

    term.requestFocus();
  }

  public synchronized void windowClosing(WindowEvent e) {
    if(isClosing)
      return;
    isClosing = true;

    if(!confirmClose()) {
      isClosing = false;
      return;
    }

    if(separateFrame && haveGUI && frame != null) {
      frame.dispose();
    }

    if(clientThread != null && clientThread.isAlive())
      clientThread.stop();

    removeTerminal(this);
  }

  public void doExit() {
      System.out.println("Thank you for using MindTerm...");
      if(!separateFrame && term != null) {
	  int i;
	  try {
	      term.cursorSetPos(term.rows() - 1, 0, false);
	      for(i = 0; i < term.rows(); i++) {
		  term.write("\n\r");
		  Thread.sleep(50);
	      }
	      term.cursorSetPos(0, 0, false);
	      for(i = 0; i < term.rows() - 1; i++) {
		  term.write(".\n\r");
		  Thread.sleep(50);
	      }
	      term.write("Thank you for using MindTerm...");
	      for(i = 0; i < term.rows() - 1; i++) {
		  term.write("\n\r");
		  Thread.sleep(50);
	      }
	      term.cursorSetPos(2, 0, false);
	      term.setAttribute(TerminalWin.ATTR_BOLD, true);
	      term.write("Visit <http://www.mindbright.se/mindterm> for more information.");
	      term.cursorSetPos(term.rows() - 1, term.cols() - 1, false);
	  } catch (Exception ee) {
	      // !!!
	  }
      }
      if(SSH.secureRandom != null && SSH.secureRandom().updater != null && SSH.secureRandom().updater.isAlive())
	SSH.secureRandom().updater.stop();
      if(!weAreAnApplet) {
	System.exit(0);
      }
  }

  boolean confirmedClose = false;
  public boolean confirmClose() {
    if(client != null && !confirmedClose) {
      try {
	client.getPropertyHandler().checkSave();
      } catch(IOException ee) {
	client.alert("Error saving settings: " + ee.getMessage());
      }
      if(client.isOpened() &&
	 !client.askConfirmation("Do you really want to disconnect from " +
				 client.getPropertyHandler().getProperty("server") + "?", false))
	confirmedClose = false;
      else
	confirmedClose = true;
    }
    return confirmedClose;
  }

  void initParams(MindTerm mindterm) {
    this.sshHomeDir     = mindterm.sshHomeDir;
    this.propsFile      = mindterm.propsFile;
    this.usePopMenu     = mindterm.usePopMenu;
    this.haveMenus      = mindterm.haveMenus;
    this.haveGUI        = mindterm.haveGUI;
    this.cmdsh          = mindterm.cmdsh;
    this.quiet          = mindterm.quiet;
    this.separateFrame  = true;
    this.weAreAnApplet  = mindterm.weAreAnApplet;
    this.autoLoadProps  = mindterm.autoLoadProps;
    this.popButtonNum   = mindterm.popButtonNum;
  }

  public void cloneWindow() {
    MindTerm mindterm = new MindTerm(this.sshProps, this.termProps);
    mindterm.initParams(this);
    mindterm.sshClone = this.client;
    (new Thread(mindterm)).start();
  }

  public void newWindow() {
    MindTerm mindterm = new MindTerm(paramSSHProps, paramTermProps);
    mindterm.initParams(this);
    (new Thread(mindterm)).start();
  }

  public void close() {
    if(!confirmClose())
      return;
    clientThread.stop();
  }

  public void exit() {
    if(!confirmClose())
      return;
    Enumeration enum = terminals.elements();
    while(enum.hasMoreElements()) {
      MindTerm mt = (MindTerm)enum.nextElement();
      if(mt.clientThread != null) {
	mt.clientThread.stop();
      }
    }
  }

}

