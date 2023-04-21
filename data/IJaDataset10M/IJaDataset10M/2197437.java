package ontool.app.designer;

import java.awt.Color;
import java.awt.event.MouseEvent;
import ontool.app.modelview.SuperplaceView;
import ontool.model.SuperplaceModel;

public class SuperplaceCreator extends Creator {

    /**
     *  Description of the Method
     *
     *@param  e  Description of Parameter
     */
    public void create(MouseEvent e) {
        SuperplaceModel spm = new SuperplaceModel(parent, genName("superplace"), null);
        model = spm;
        spm.setPosition(drawArea.adjustGrid(e.getX()), drawArea.adjustGrid(e.getY()));
        new SuperplaceView(drawArea, spm);
        app.showStatus("Superplace created", Color.BLACK);
    }
}
