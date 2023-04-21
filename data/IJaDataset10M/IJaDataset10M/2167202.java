package drcube.test.activities.testclasses;

import drcube.activities.MainMenu;
import drcube.views.CanvasView;

/**
 * Testklasse der MainMenu-Activity.
 * 
 * @author simbuerg
 *
 */
public class MainMenuTest extends MainMenu {

    /**
	 * @see drcube.activities.FormActivity#getFormView()
	 */
    public CanvasView getFormView() {
        return super.getCanvasView();
    }
}
