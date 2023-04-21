package pl.edu.amu.wmi.ztg.grapher.ui.actions;

import java.awt.event.ActionEvent;
import java.net.URL;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import pl.edu.amu.wmi.ztg.grapher.GrapherMessages;

/**
 * 
 * @author Ireneusz Spinalski
 * 
 */
public class CutAction extends AbstractAction {

    /**
     * 
     */
    private static final long serialVersionUID = 784827872891725853L;

    /**
     * Creates new instance of CutAction. Sets name to Cut and icon to cut.gif.
     */
    public CutAction() {
        super(GrapherMessages.getString("CutAction.0"));
        URL cutUrl = getClass().getClassLoader().getResource("pl/edu/amu/wmi/ztg/grapher/ui/resources/cut.gif");
        ImageIcon cutIcon = new ImageIcon(cutUrl);
        this.putValue(AbstractAction.SMALL_ICON, cutIcon);
    }

    /**
     * @param event
     */
    public void actionPerformed(ActionEvent event) {
    }

    ;
}
