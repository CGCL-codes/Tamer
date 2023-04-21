package jnocatan;

import java.awt.event.*;
import java.util.*;

/**
 * JnoCatanResourceListener
 *
 * @author  Don Seiler <don@NOSPAM.seiler.us>
 * @version $Id: JnoCatanResourceListener.java,v 1.1 2004/10/03 02:59:47 rizzo Exp $
 * @since   0.1.0
 */
public interface JnoCatanResourceListener extends EventListener {

    void resourceChanged(String resource, String opdesc, Object objval);
}
