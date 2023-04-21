package menu;

import java.awt.Graphics;
import world.ImageManager;
import board.Constants;

public class ModeMainMenu extends MenuMode {

    public ModeMainMenu(MenuController controller) {
        super(controller);
    }

    @Override
    public void drawMode(Graphics g) {
        drawHead(g, this.getImage(Constants.IMAGE_JTRES3D));
        MenuImage referenceImg = ImageManager.getInstance().getImage(Constants.COMMAND_CONTINUE);
        int x = Constants.LAYOUT_CENTER_WIDTH - referenceImg.getWidth() / 2;
        int y = Constants.LAYOUT_BORDER_TOP_CONTENT;
        for (Button button : this.getButtons()) {
            if (button.isActive()) {
                button.draw(g, x, y);
                y += referenceImg.getHeight();
            }
        }
    }
}
