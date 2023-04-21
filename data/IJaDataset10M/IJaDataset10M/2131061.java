package org.colombbus.tangara.ide.view.cmdline2;

import org.colombbus.tangara.ide.model.MessagesFactory;
import org.colombbus.tangara.util.bundle.TypedResourceBundle;

/**
 * Main class used by components to get internationalized messages.
 * 
 * @version $Id: Messages.java 81 2009-07-03 16:21:00Z swip $
 * @author Aurelien Bourdon <aurelien.bourdon@gmail.com>
 */
class Messages {

    /** Typed resource bundle use for print or get internationalized resources */
    public static final TypedResourceBundle RESOURCE_BUNDLE;

    static {
        String baseName = Messages.class.getPackage().getName() + ".messages";
        RESOURCE_BUNDLE = MessagesFactory.createResourceBundle(baseName);
    }
}
