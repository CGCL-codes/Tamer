package org.ocd.cmdmgr.jnlp;

import org.ocd.cmdmgr.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

/**
 *
 * @author $Author: drichan $
 * @author ocd_dino - ocd_dino@users.sourceforge.net (initial author)
 * @version $Revision: 1.3 $
 * @since $Date: 2002/08/09 02:41:37 $
 */
public class JNLPCmdFactory implements ICmdFactory {

    public static final String NAME = "JNLP Application";

    public static final String DEFAULT_LARGE_ICON = "images/jnlpapp32.png";

    public static final String DEFAULT_SMALL_ICON = "images/jnlpapp16.png";

    private static JNLPCmdFactory singleton;

    private ImageIcon largeIcon;

    private ImageIcon smallIcon;

    /** Creates new BrowserTaskFactory */
    public JNLPCmdFactory() {
        if (singleton == null) singleton = this;
        largeIcon = new ImageIcon(ICmdFactory.class.getResource(DEFAULT_LARGE_ICON));
        smallIcon = new ImageIcon(ICmdFactory.class.getResource(DEFAULT_SMALL_ICON));
    }

    /**
   * Get the Singleton Instance
   */
    public static JNLPCmdFactory getSingleton() {
        if (singleton != null) return singleton; else return new JNLPCmdFactory();
    }

    /**
   * Get the Name of this Factory
   */
    public String getName() {
        return NAME;
    }

    /**
   * Get the Large Icon for this Factory
   */
    public Icon getLargeIcon() {
        return largeIcon;
    }

    /**
   * Get the Small Icon for this Factory
   */
    public Icon getSmallIcon() {
        return smallIcon;
    }

    /**
   * Get an Action from this Factory
   * @param pCmd to get Action for
   * @return ICmdAction
   * @throws InvalidCmdException if the pCmd Object is not supported
   * @throws ActionCreationException if there was an error creating the Action
   */
    public ICmdAction getCmdAction(Object pCmd) throws InvalidCmdException, ActionCreationException {
        if (pCmd instanceof URL) {
            return new JNLPCmdAction((URL) pCmd);
        } else if (pCmd instanceof String) {
            String _jnlp = (String) pCmd;
            if (_jnlp != null && _jnlp.endsWith(".jnlp")) {
                try {
                    return new JNLPCmdAction(new URL(_jnlp));
                } catch (MalformedURLException urlExp) {
                    throw new InvalidCmdException(urlExp.getMessage());
                }
            }
        }
        throw new InvalidCmdException("Not a JNLP URL");
    }

    /**
   * Return true if this Factory can create an for the given Command
   * @param pCmd to check
   * @return true if the pCmd is supported.
   */
    public boolean isSupported(Object pCmd) {
        return ((pCmd instanceof URL || pCmd instanceof String) && pCmd.toString().endsWith(".jnlp"));
    }
}
