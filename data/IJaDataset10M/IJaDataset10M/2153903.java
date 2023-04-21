package com.digitalinksecurities.erinyes.util;

import com.digitalinksecurities.erinyes.MqJmsBaseBean;
import com.digitalinksecurities.erinyes.MqJmsClientBean;
import com.digitalinksecurities.erinyes.MqJmsServerBean;

/**
 * Insert the type's description here.
 * Creation date: (4/17/2001 10:04:29 AM)
 * @author: 
 */
class JmsMenuBar extends java.awt.MenuBar implements java.awt.event.ActionListener, java.awt.event.ItemListener, java.beans.PropertyChangeListener {

    private java.awt.Frame frame;

    private java.awt.MenuItem miFile, miFile0, miFile1, miFile2, miFile3, miFile4;

    private java.awt.MenuItem miTools0, miTools1;

    private java.awt.MenuItem miHelp0;

    private java.awt.CheckboxMenuItem miOptions0, miOptions1;

    private MqJmsBaseBean mjbb;

    private MqJmsServerBean mjsb;

    private String sendQ$;

    private String recQ$;

    private String secureObjLocation$, secureObjPassphrase$;

    InputDialogBox inBox;

    /**
	  * The type of messaging this application will perform.
	  * 0 JMS Message Type (Default)
	  * 1 MQ Message Type
	  */
    private int clientType = 0;

    /**
 * JmsMenuBar constructor comment.
 */
    public JmsMenuBar() {
        super();
        java.awt.MenuItem exitMI;
        java.awt.Menu m = new java.awt.Menu("File");
        miFile0 = new java.awt.MenuItem("Connect to Jms Server");
        m.add(miFile0);
        m.add("-");
        exitMI = new java.awt.MenuItem("Exit", new java.awt.MenuShortcut(java.awt.event.KeyEvent.VK_X));
        exitMI.setActionCommand("Exit");
        m.add(exitMI);
        m.addActionListener(this);
        add(m);
    }

    /**
 * JmsMenuBar constructor comment.
 */
    public JmsMenuBar(java.awt.Frame f) {
        super();
        frame = f;
        java.awt.MenuItem exitMI;
        java.awt.Menu mFile = new java.awt.Menu("File");
        java.awt.Menu mTools = new java.awt.Menu("Tools");
        java.awt.Menu mOptions = new java.awt.Menu("Options");
        java.awt.Menu mHelp = new java.awt.Menu("Help");
        miFile = new java.awt.MenuItem("Open");
        mFile.add(miFile);
        miFile0 = new java.awt.MenuItem("Connect to Jms Server");
        mFile.add(miFile0);
        miFile1 = new java.awt.MenuItem("Disconnect from Jms Server");
        miFile1.setEnabled(false);
        mFile.add(miFile1);
        miFile2 = new java.awt.MenuItem("Sending Queue");
        miFile2.setEnabled(false);
        mFile.add(miFile2);
        miFile3 = new java.awt.MenuItem("Receiving Queue");
        miFile3.setEnabled(false);
        mFile.add(miFile3);
        mFile.add("-");
        exitMI = new java.awt.MenuItem("Exit", new java.awt.MenuShortcut(java.awt.event.KeyEvent.VK_X));
        exitMI.setActionCommand("Exit");
        mFile.add(exitMI);
        miTools0 = new java.awt.MenuItem("Send Message");
        miTools0.setEnabled(false);
        mTools.add(miTools0);
        miOptions0 = new java.awt.CheckboxMenuItem("Server Mode");
        mOptions.add(miOptions0);
        miHelp0 = new java.awt.MenuItem("About");
        mHelp.add(miHelp0);
        mFile.addActionListener(this);
        mTools.addActionListener(this);
        miOptions0.addItemListener(this);
        mHelp.addActionListener(this);
        add(mFile);
        add(mTools);
        add(mOptions);
        add(mHelp);
        inBox = new InputDialogBox(frame);
    }

    /**
	 * Invoked when an action occurs.
	 */
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        if ("Connect to Jms Server".equals(evt.getActionCommand())) {
            inBox.setTitle("Enter in a Queue Manager name");
            inBox.setnLabelText("Blank indicates: local Queue Manager or file context factory");
            displayDialog(inBox, "");
            String qMgr$ = inBox.getInputFieldValue();
            if (!qMgr$.equals("Cancel")) {
                if (qMgr$.startsWith("cn=")) {
                    java.util.Hashtable ht = this.createDircontextHashtable(inBox);
                    if (ht == null) {
                        return;
                    } else {
                        mjbb = new MqJmsBaseBean();
                        mjbb.setDirCtx(ht);
                        mjbb.setQmgr(qMgr$);
                    }
                } else {
                    mjbb = new MqJmsBaseBean();
                    mjbb.setQmgr(qMgr$);
                }
                try {
                    mjbb.addPropertyChangeListener(this);
                } catch (java.util.TooManyListenersException e) {
                    setStatusBarText(e.getMessage());
                }
                miFile0.setEnabled(false);
                miFile1.setEnabled(true);
                if (!miOptions0.getState()) {
                    miFile2.setEnabled(true);
                }
                miFile3.setEnabled(true);
                try {
                    javax.jms.ConnectionMetaData cmd = mjbb.getQconn().getMetaData();
                    setStatusBarText("Jms Server info: " + cmd.toString());
                } catch (javax.jms.JMSException e) {
                    setStatusBarText(e.getMessage());
                }
            } else {
                return;
            }
        } else if ("Disconnect from Jms Server".equals(evt.getActionCommand())) {
            setStatusBarText("");
            miFile0.setEnabled(true);
            miFile1.setEnabled(false);
            miFile2.setEnabled(false);
            miFile3.setEnabled(false);
            miTools0.setEnabled(false);
            if (mjbb != null) {
                mjbb.closeMqObjs();
                mjbb = null;
            }
            if (mjsb != null) {
                mjsb.closeMqObjs();
                mjsb = null;
            }
        } else if ("Open".equals(evt.getActionCommand())) {
            java.awt.FileDialog fd = new java.awt.FileDialog(frame, null, java.awt.FileDialog.LOAD);
            fd.show();
            String path$ = fd.getDirectory() + fd.getFile();
            try {
                ((TwcJmsTestHarness) frame).getTxtArea().setText(returnFileAsString(path$));
            } catch (java.io.IOException e) {
                setStatusBarText(e.getMessage());
            }
        } else if ("Sending Queue".equals(evt.getActionCommand())) {
            inBox.setTitle("Sending Queue");
            inBox.setnLabelText("Enter in a queue to send messages to.");
            displayDialog(inBox, "KRDEAN.DEFAULT.LOCAL.QUEUE");
            String sQ$ = inBox.getInputFieldValue();
            if (!sQ$.equals("Cancel") & !sQ$.equals("")) {
                sendQ$ = sQ$;
                miTools0.setEnabled(true);
                setStatusBarText("Sending messages to: " + sQ$);
            }
        } else if ("Receiving Queue".equals(evt.getActionCommand())) {
            inBox.setTitle("Queue Receiver");
            if (miOptions0.getState()) {
                inBox.setnLabelText("Enter in a queue to receive messages from.");
            } else {
                inBox.setnLabelText("Blank: a temporary receiver queue will be used.");
            }
            displayDialog(inBox, "KRDEAN.DEFAULT.LOCAL.QUEUE");
            String rQ$ = inBox.getInputFieldValue();
            if (!rQ$.equals("Cancel")) {
                recQ$ = rQ$;
                if (miOptions0.getState()) {
                    mjsb = new MqJmsServerBean(mjbb);
                    mjsb.setQrec_name(rQ$);
                }
            }
        } else if ("Send Message".equals(evt.getActionCommand())) {
            if (mjbb != null & sendQ$ != null) {
                MqJmsClientBean mjcb = new MqJmsClientBean(mjbb);
                mjcb.setQsend_name(sendQ$);
                if (recQ$ != null) {
                    mjcb.setQrec_name(recQ$);
                }
                mjcb.setMsg(((TwcJmsTestHarness) frame).getTxtArea().getText());
                if (recQ$ != null) {
                    ((TwcJmsTestHarness) frame).getTxtArea().setText("");
                    ((TwcJmsTestHarness) frame).getTxtArea().setText(mjcb.getMsg(5000));
                }
            }
        } else if ("About".equals(evt.getActionCommand())) {
            ((TwcJmsTestHarness) frame).getTxtArea().setText("About");
        } else {
            if (mjbb != null) {
                mjbb.closeMqObjs();
            }
            frame.dispose();
            System.exit(0);
        }
    }

    /**
 * Insert the method's description here.
 * Creation date: (4/17/2001 1:48:53 PM)
 * @return java.util.Hashtable
 * @param param com.thewiringco.jms.apps.InputDialogBox
 */
    private java.util.Hashtable createDircontextHashtable(InputDialogBox iBox) {
        java.util.Hashtable contextEnv = new java.util.Hashtable();
        iBox.setTitle("Enter in the Context's initial context factory.");
        iBox.setnLabelText("Blank: 'com.sun.jndi.ldap.LdapCtxFactory' will be used.");
        displayDialog(iBox, "");
        String iBox$ = iBox.getInputFieldValue();
        if (!iBox$.equals("Cancel")) {
            if (iBox$ == null || iBox$.equals("")) {
                contextEnv.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            } else {
                contextEnv.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY, iBox$);
            }
        } else {
            return null;
        }
        iBox.setTitle("Enter in the Context's provider url.");
        iBox.setnLabelText("Example: 'ldap://192.168.1.10:636'");
        displayDialog(iBox, "ldap://192.168.1.10:389");
        iBox$ = iBox.getInputFieldValue();
        if (!iBox$.equals("Cancel") & !iBox$.equals("")) {
            if (iBox$.indexOf("636") != -1) {
                contextEnv.put(javax.naming.Context.SECURITY_PROTOCOL, "ssl");
            }
            contextEnv.put(javax.naming.Context.PROVIDER_URL, iBox$);
        } else {
            return null;
        }
        iBox.setTitle("Enter in the Context's security authentication.");
        iBox.setnLabelText("Blank: 'simple' will be used.");
        displayDialog(iBox, "");
        iBox$ = iBox.getInputFieldValue();
        if (!iBox$.equals("Cancel")) {
            if (iBox$ == null || iBox$.equals("")) {
                contextEnv.put(javax.naming.Context.SECURITY_AUTHENTICATION, "simple");
            } else {
                contextEnv.put(javax.naming.Context.SECURITY_AUTHENTICATION, iBox$);
            }
        } else {
            return null;
        }
        iBox.setTitle("LDAP Security Property");
        iBox.setnLabelText("Enter in the Context's security principal.");
        displayDialog(iBox, "cn=Admin,o=twc");
        iBox$ = iBox.getInputFieldValue();
        if (!iBox$.equals("Cancel") & !iBox$.equals("")) {
            contextEnv.put(javax.naming.Context.SECURITY_PRINCIPAL, iBox$);
        } else {
            return null;
        }
        iBox.setTitle("LDAP Security Property");
        iBox.setnLabelText("Enter in the Context's security credentials.");
        displayDialog(iBox, "nitwit");
        iBox$ = iBox.getInputFieldValue();
        if (!iBox$.equals("Cancel") & !iBox$.equals("")) {
            contextEnv.put(javax.naming.Context.SECURITY_CREDENTIALS, iBox$);
        } else {
            return null;
        }
        contextEnv.put(javax.naming.Context.REFERRAL, "throw");
        return contextEnv;
    }

    /**
 * This method displays the an input dialog box obj to allow users
 * to enter in information
 * Creation date: (5/19/00 1:41:43 PM)
 */
    private void displayDialog(InputDialogBox idb, String txt$) {
        try {
            idb.setModal(true);
            idb.getinputField().setText(txt$);
            idb.getinputField().requestFocus();
            idb.addWindowListener(new java.awt.event.WindowAdapter() {

                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }

                ;
            });
            idb.setVisible(true);
        } catch (Throwable exception) {
            System.err.println("Exception occurred in main() of java.awt.Dialog");
            exception.printStackTrace(System.out);
        }
    }

    /**
 * Insert the method's description here.
 * Creation date: (4/19/2001 1:36:54 PM)
 * @param evt java.awt.event.ItemEvent
 */
    public void itemStateChanged(java.awt.event.ItemEvent evt) {
        Object src = evt.getSource();
        String inBxTitle$ = "Jms Test Harness";
        if (miOptions0.getState()) {
            inBxTitle$ = inBxTitle$ + " Server Mode";
            frame.setTitle(inBxTitle$.trim());
            miFile.setEnabled(false);
            miFile2.setEnabled(false);
        } else {
            inBxTitle$ = inBxTitle$ + " Client Mode";
            frame.setTitle(inBxTitle$.trim());
            miFile.setEnabled(true);
        }
    }

    public void propertyChange(java.beans.PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("msg")) {
            String msg$ = (String) evt.getNewValue();
            String replyToQ$ = (String) evt.getOldValue();
            if (replyToQ$ != null) {
                setStatusBarText("Attempting to redirect msg to " + replyToQ$);
                MqJmsClientBean mqcb = new MqJmsClientBean(mjbb);
                mqcb.setQsend_name(replyToQ$);
                mqcb.setMsg(msg$);
            }
            ((TwcJmsTestHarness) frame).getTxtArea().setText(msg$);
        } else if (evt.getPropertyName().equals("fatal_jms_ex")) {
            ((TwcJmsTestHarness) frame).getTxtArea().setText(evt.getNewValue().toString());
            if (mjbb != null) {
                mjbb.closeMqObjs();
            }
            if (mjsb != null) {
                mjsb.closeMqObjs();
            }
        } else if (evt.getPropertyName().equals("warn_jms_ex")) {
            setStatusBarText(evt.getNewValue().toString());
        }
    }

    /**
 * Insert the method's description here.
 * Creation date: (4/18/2001 10:14:38 AM)
 * @return java.lang.String
 * @param filedest java.lang.String
 * @exception java.io.FileNotFoundException The exception description.
 */
    private String returnFileAsString(String filedest) throws java.io.IOException {
        java.lang.StringBuffer sBuff = new java.lang.StringBuffer();
        try {
            java.io.BufferedReader inFile = new java.io.BufferedReader(new java.io.FileReader(new java.io.File(filedest)));
            String str$;
            while ((str$ = inFile.readLine()) != null) {
                sBuff.append(str$ + "\n");
            }
        } catch (java.io.FileNotFoundException e) {
            ;
        }
        return sBuff.toString();
    }

    /**
 * Insert the method's description here.
 * Creation date: (4/18/2001 10:28:33 AM)
 * @param statusTxt$ java.lang.String
 */
    private void setStatusBarText(String statusTxt$) {
        ((TwcJmsTestHarness) frame).getStatusField().setText(statusTxt$);
    }
}
