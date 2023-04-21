package jpicedt.graphic.event;

import java.util.EventListener;

/**
 * Interface for an observer to receive help messages posted by an EditorKit
 * @author Sylvain Reynal
 * @version $Id: HelpMessageListener.java,v 1.6 2011/07/23 05:23:12 vincentb1 Exp $
 * @since jpicedt 1.3.2
 */
public interface HelpMessageListener extends EventListener {

    /** called by the sourcing EditorKit when posting an help-message */
    public void helpMessagePosted(HelpMessageEvent e);
}
