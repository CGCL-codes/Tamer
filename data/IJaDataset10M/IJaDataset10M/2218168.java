package ch.isbiel.oois.infospeaker.command;

import java.awt.GraphicsConfiguration;
import java.util.Map;
import javax.jms.TopicConnection;
import javax.swing.JFrame;
import javax.swing.tree.DefaultMutableTreeNode;
import ch.oois.common.gui.command.Command;
import ch.isbiel.oois.common.service.ServiceLocator;
import ch.oois.common.gui.InfoType;
import ch.isbiel.oois.infospeaker.HistoryListGUI;

/**
 * 
 * 
 * @author $Author$
 * @version $Revision$
 */
public class DisplayHistoryListCommand implements Command {

    private final Map _properties;

    private final Map _defaults;

    private final ServiceLocator _sl;

    private final TopicConnection _tc;

    private final GraphicsConfiguration _gc;

    private final DefaultMutableTreeNode _dmtn;

    public DisplayHistoryListCommand(Map properties, Map defaults, ServiceLocator sl, TopicConnection tc, GraphicsConfiguration gc, DefaultMutableTreeNode dmtn) {
        _properties = properties;
        _defaults = defaults;
        _sl = sl;
        _tc = tc;
        _gc = gc;
        _dmtn = dmtn;
    }

    /**
   * @see ch.isbiel.oois.common.command.Command#execute()
   */
    public void execute() {
        int type = ((Integer) _properties.get(InfoType.KEY)).intValue();
        JFrame gui = null;
        gui = (JFrame) new HistoryListGUI(_properties, _defaults, _sl, _tc, _gc, _dmtn);
        if (gui != null) {
            gui.show();
        }
    }
}
