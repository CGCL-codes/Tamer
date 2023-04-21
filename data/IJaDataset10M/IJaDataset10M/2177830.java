package uk.ac.aber.Blockmation.Actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import uk.ac.aber.Blockmation.DrawingPad;
import uk.ac.aber.Blockmation.MainFrame;

/**
 * 
 * @author tom
 */
public class ActionPointTool extends AbstractAction {

    private MainFrame mainFrame;

    private String path = "../icons/ChangeShapeLine.gif";

    public ActionPointTool(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public void actionPerformed(ActionEvent e) {
        mainFrame.getPad().setTool(DrawingPad.POINT);
    }

    public void setImage() {
        CreateImageIcon createImageIcon = new CreateImageIcon();
        putValue(SMALL_ICON, createImageIcon.create(path));
    }
}
