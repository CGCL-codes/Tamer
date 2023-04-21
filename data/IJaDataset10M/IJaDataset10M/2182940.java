package org.compiere.apps;

import java.awt.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import org.compiere.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *  Info Dialog Management
 *
 *  @author 	Jorg Janke
 *  @version 	$Id: ADialog.java,v 1.2 2006/07/30 00:51:27 jjanke Exp $
 */
public final class ADialog {

    /** Show ADialogADialog - if false use JOptionPane  */
    public static boolean showDialog = true;

    /**	Logger			*/
    private static CLogger log = CLogger.getCLogger(ADialog.class);

    /**
	 *	Show plain message
	 *	@param	WindowNo	Number of Window
	 *  @param  c           Container (owner)
	 *	@param	clearHeading	Translated Title of window
	 *	@param	clearMessage	Translated message
	 *	@param	clearText		Additional message
	 */
    public static void info(int WindowNo, Container c, String clearHeading, String clearMessage, String clearText) {
        log.info(clearHeading + ": " + clearMessage + " " + clearText);
        String out = clearMessage;
        if (clearText != null && !clearText.equals("")) out += "\n" + clearText;
        Window parent = Env.getParent(c);
        if (parent == null) parent = Env.getWindow(WindowNo);
        if (showDialog && parent != null) {
            if (parent instanceof JFrame) new ADialogDialog((JFrame) parent, clearHeading, out, JOptionPane.INFORMATION_MESSAGE); else new ADialogDialog((JDialog) parent, clearHeading, out, JOptionPane.INFORMATION_MESSAGE);
        } else JOptionPane.showMessageDialog(parent, out + "\n", clearHeading, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
	 *	Show message with info icon
	 *	@param	WindowNo	Number of Window
	 *  @param  c           Container (owner)
	 *	@param	AD_Message	Message to be translated
	 *	@param	msg			Additional message
	 */
    public static void info(int WindowNo, Container c, String AD_Message, String msg) {
        log.info(AD_Message + " - " + msg);
        Properties ctx = Env.getCtx();
        StringBuffer out = new StringBuffer();
        if (AD_Message != null && !AD_Message.equals("")) out.append(Msg.getMsg(ctx, AD_Message));
        if (msg != null && msg.length() > 0) out.append("\n").append(msg);
        Window parent = Env.getParent(c);
        if (parent == null) parent = Env.getWindow(WindowNo);
        if (showDialog && parent != null) {
            if (parent instanceof JFrame) new ADialogDialog((JFrame) parent, Env.getHeader(ctx, WindowNo), out.toString(), JOptionPane.INFORMATION_MESSAGE); else new ADialogDialog((JDialog) parent, Env.getHeader(ctx, WindowNo), out.toString(), JOptionPane.INFORMATION_MESSAGE);
        } else JOptionPane.showMessageDialog(parent, out.toString() + "\n", Env.getHeader(ctx, WindowNo), JOptionPane.INFORMATION_MESSAGE);
    }

    /**
	 *	Show message with info icon
	 *	@param	WindowNo	Number of Window
	 *  @param  c           Container (owner)
	 *	@param	AD_Message	Message to be translated
	 */
    public static void info(int WindowNo, Container c, String AD_Message) {
        info(WindowNo, c, AD_Message, null);
    }

    /**************************************************************************
	 *	Display warning with warning icon
	 *	@param	WindowNo	Number of Window
	 *  @param  c           Container (owner)
	 *	@param	AD_Message	Message to be translated
	 *	@param	msg			Additional message
	 */
    public static void warn(int WindowNo, Container c, String AD_Message, String msg) {
        log.info(AD_Message + " - " + msg);
        Properties ctx = Env.getCtx();
        StringBuffer out = new StringBuffer();
        if (AD_Message != null && !AD_Message.equals("")) out.append(Msg.getMsg(ctx, AD_Message));
        if (msg != null && msg.length() > 0) out.append("\n").append(msg);
        Window parent = Env.getParent(c);
        if (parent == null) parent = Env.getWindow(WindowNo);
        if (showDialog && parent != null) {
            if (parent instanceof JFrame) new ADialogDialog((JFrame) parent, Env.getHeader(ctx, WindowNo), out.toString(), JOptionPane.WARNING_MESSAGE); else new ADialogDialog((JDialog) parent, Env.getHeader(ctx, WindowNo), out.toString(), JOptionPane.WARNING_MESSAGE);
        } else JOptionPane.showMessageDialog(parent, out.toString() + "\n", Env.getHeader(ctx, WindowNo), JOptionPane.WARNING_MESSAGE);
    }

    /**
	 *	Display warning with warning icon
	 *	@param	WindowNo	Number of Window
	 *  @param  c           Container (owner)
	 *	@param	AD_Message	Message to be translated
	 */
    public static void warn(int WindowNo, Container c, String AD_Message) {
        warn(WindowNo, c, AD_Message, null);
    }

    /**************************************************************************
	 *	Display error with error icon
	 *	@param	WindowNo	Number of Window
	 *  @param  c           Container (owner)
	 *	@param	AD_Message	Message to be translated
	 *	@param	msg			Additional message
	 */
    public static void error(int WindowNo, Container c, String AD_Message, String msg) {
        log.info(AD_Message + " - " + msg);
        if (CLogMgt.isLevelFinest()) Trace.printStack();
        Properties ctx = Env.getCtx();
        StringBuffer out = new StringBuffer();
        if (AD_Message != null && !AD_Message.equals("")) out.append(Msg.getMsg(ctx, AD_Message));
        if (msg != null && msg.length() > 0) out.append("\n").append(msg);
        Window parent = Env.getParent(c);
        if (parent == null) parent = Env.getWindow(WindowNo);
        if (showDialog && parent != null) {
            if (parent instanceof JFrame) new ADialogDialog((JFrame) parent, Env.getHeader(ctx, WindowNo), out.toString(), JOptionPane.ERROR_MESSAGE); else if (parent instanceof JDialog) new ADialogDialog((JDialog) parent, Env.getHeader(ctx, WindowNo), out.toString(), JOptionPane.ERROR_MESSAGE);
        } else JOptionPane.showMessageDialog(Env.getWindow(WindowNo), out.toString() + "\n", Env.getHeader(ctx, WindowNo), JOptionPane.ERROR_MESSAGE);
    }

    /**
	 *	Display error with error icon
	 *	@param	WindowNo	Number of Window
	 *  @param  c           Container (owner)
	 *	@param	AD_Message	Message to be translated
	 */
    public static void error(int WindowNo, Container c, String AD_Message) {
        error(WindowNo, c, AD_Message, null);
    }

    /**************************************************************************
	 *	Ask Question with question icon and (OK) (Cancel) buttons
	 *	@param	WindowNo	Number of Window
	 *  @param  c           Container (owner)
	 *	@param	AD_Message	Message to be translated
	 *	@param	msg			Additional clear text message
	 *	@return true, if OK
	 */
    public static boolean ask(int WindowNo, Container c, String AD_Message, String msg) {
        log.info(AD_Message + " - " + msg);
        Properties ctx = Env.getCtx();
        StringBuffer out = new StringBuffer();
        if (AD_Message != null && !AD_Message.equals("")) out.append(Msg.getMsg(ctx, AD_Message));
        if (msg != null && msg.length() > 0) out.append("\n").append(msg);
        Window parent = Env.getParent(c);
        if (parent == null) parent = Env.getWindow(WindowNo);
        boolean retValue = false;
        if (showDialog && parent != null) {
            if (parent instanceof JFrame) {
                ADialogDialog d = new ADialogDialog((JFrame) parent, Env.getHeader(ctx, WindowNo), out.toString(), JOptionPane.QUESTION_MESSAGE);
                retValue = d.getReturnCode() == ADialogDialog.A_OK;
            } else {
                ADialogDialog d = new ADialogDialog((JDialog) parent, Env.getHeader(ctx, WindowNo), out.toString(), JOptionPane.QUESTION_MESSAGE);
                retValue = d.getReturnCode() == ADialogDialog.A_OK;
            }
        } else {
            Object[] optionsOC = { Util.cleanAmp(Msg.getMsg(ctx, "OK")), Util.cleanAmp(Msg.getMsg(ctx, "Cancel")) };
            int i = JOptionPane.showOptionDialog(parent, out.toString() + "\n", Env.getHeader(ctx, WindowNo), JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, optionsOC, optionsOC[0]);
            retValue = i == JOptionPane.YES_OPTION;
        }
        return retValue;
    }

    /**
	 *	Ask Question with question icon and (OK) (Cancel) buttons
	 *	@param	WindowNo	Number of Window
	 *  @param  c           Container (owner)
	 *	@param	AD_Message	Message to be translated
	 *	@return true, if OK
	 */
    public static boolean ask(int WindowNo, Container c, String AD_Message) {
        return ask(WindowNo, c, AD_Message, null);
    }

    /**************************************************************************
	 *	Display parsed development info Message string
	 *	@param	WindowNo	Number of parent window (if zero, no parent window)
	 *  @param  c           Container (owner)
	 *	@param	ParseString	String to be parsed
	 */
    public static void clear(int WindowNo, Container c, String ParseString) {
        log.info("Dialog.clear: " + ParseString);
        Properties ctx = Env.getCtx();
        String parse = Env.parseContext(ctx, WindowNo, ParseString, false);
        if (parse.length() == 0) parse = "ERROR parsing: " + ParseString;
        Window parent = Env.getParent(c);
        if (parent == null) parent = Env.getWindow(WindowNo);
        if (showDialog && parent != null) {
            if (parent instanceof JFrame) new ADialogDialog((JFrame) parent, Env.getHeader(ctx, WindowNo), "=> " + parse, JOptionPane.INFORMATION_MESSAGE); else new ADialogDialog((JDialog) parent, Env.getHeader(ctx, WindowNo), "=> " + parse, JOptionPane.INFORMATION_MESSAGE);
        } else JOptionPane.showMessageDialog(parent, "=> " + parse + "\n", Env.getHeader(ctx, WindowNo), JOptionPane.INFORMATION_MESSAGE);
    }

    /**
	 *	Display parsed development info Message string <x> if condition is true
	 *	@param	WindowNo	Number of parent window (if zero, no parent window)
	 *  @param  c           Container (owner)
	 *	@param	ParseString	Parsed Message
	 *	@param	condition	to print must be true and debugging enabled
	 */
    public static void clear(int WindowNo, Container c, String ParseString, boolean condition) {
        if (!condition) return;
        clear(WindowNo, c, ParseString);
        if (WindowNo == 0) log.log(Level.SEVERE, "WIndowNo == 0");
    }

    /**
	 *	Display parsed development info Message string
	 *	@param	ParseString	String to be parsed
	 *  @deprecated
	 */
    public static void clear(String ParseString) {
        clear(0, null, ParseString);
    }

    /*************************************************************************

	/**
	 * Create Support EMail
	 * @param owner owner
	 * @param subject subkect
	 * @param message message
	 */
    public static void createSupportEMail(Dialog owner, String subject, String message) {
        log.config("ADialog.createSupportEMail");
        String to = Adempiere.getSupportEMail();
        MUser from = MUser.get(Env.getCtx(), Env.getAD_User_ID(Env.getCtx()));
        StringBuffer myMessage = new StringBuffer(message);
        myMessage.append("\n");
        CLogMgt.getInfo(myMessage);
        CLogMgt.getInfoDetail(myMessage, Env.getCtx());
        ModelValidationEngine.get().getInfoDetail(myMessage, Env.getCtx());
        EMailDialog emd = new EMailDialog(owner, Msg.getMsg(Env.getCtx(), "EMailSupport"), from, to, "Support: " + subject, myMessage.toString(), null);
    }

    /**
	 *	Create Support EMail
	 *	@param owner owner
	 *  @param subject subkect
	 *  @param message message
	 */
    public static void createSupportEMail(Frame owner, String subject, String message) {
        log.config("ADialog.createSupportEMail");
        String to = Adempiere.getSupportEMail();
        MUser from = MUser.get(Env.getCtx(), Env.getAD_User_ID(Env.getCtx()));
        StringBuffer myMessage = new StringBuffer(message);
        myMessage.append("\n");
        CLogMgt.getInfo(myMessage);
        CLogMgt.getInfoDetail(myMessage, Env.getCtx());
        ModelValidationEngine.get().getInfoDetail(myMessage, Env.getCtx());
        EMailDialog emd = new EMailDialog(owner, Msg.getMsg(Env.getCtx(), "EMailSupport"), from, to, "Support: " + subject, myMessage.toString(), null);
    }

    /**************************************************************************
	 *	Beep
	 */
    public static void beep() {
        Toolkit.getDefaultToolkit().beep();
    }
}
