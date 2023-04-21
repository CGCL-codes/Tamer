package menu;

import engine.GUI;
import graphics.MusicGameImage;
import java.awt.Point;
import java.awt.event.KeyEvent;

public class ModeSelection extends MenuSection {

    private int selection = 0;

    private MusicGameImage bigMenuBack, smallMenuBack;

    /**
	 * Constructs the mode selection menu.
	 * 
	 * @param previous the previous menu (main menu).
	 */
    public ModeSelection(MenuSection previous) {
        initialize();
        GUI gui = GUI.getInstance();
        this.bigMenuBack = createBigBackground(gui);
        this.smallMenuBack = createSmallBackground(gui);
        setTitle("Kies een spel");
        int dx = (int) (this.xscale * 325.0);
        int dy = (int) (this.yscale * 300.0);
        int sx = (gui.getWidth() - dx) / 2;
        int sy = (int) (this.yscale * 355.0);
        for (int i = 0; i < 4; i++) {
            int x = sx + (i % 2) * dx;
            int y = sy + (i > 1 ? 1 : 0) * dy;
            this.buttons.add(new ModeButton(i, new Point(x, y), this.xscale, this.yscale));
        }
        int buttonHeight = (int) Math.round(90.0 * this.yscale);
        this.buttons.add(new FreeButton("Terug", previous, new Point(gui.getWidth() / 2, this.smallMenuBack.getButtonY()), buttonHeight));
        ((HighlightButton) this.buttons.get(0)).setHighlight(true);
    }

    @Override
    public void controlEvent(int controller, int action) {
        switch(action) {
            case KeyEvent.VK_LEFT:
                highlightButton(-1);
                break;
            case KeyEvent.VK_RIGHT:
                highlightButton(1);
                break;
            case KeyEvent.VK_UP:
                highlightButton(-2);
                break;
            case KeyEvent.VK_DOWN:
                highlightButton(2);
                break;
            case KeyEvent.VK_BACK_SPACE:
                this.buttons.get(this.buttons.size() - 1).action();
                break;
            case KeyEvent.VK_ENTER:
                this.buttons.get(selection).action();
                break;
        }
    }

    @Override
    protected void drawSpecifics() {
        GUI gui = GUI.getInstance();
        gui.addMenuImage(this.bigMenuBack);
        gui.addMenuImage(this.smallMenuBack);
    }

    /**
	 * Sets the button at buttonIndex highlighted.
	 * 
	 * @param buttonChange the difference in buttons.
	 */
    private void highlightButton(int buttonChange) {
        int buttonIndex = (4 + this.selection + buttonChange) % 4;
        ((HighlightButton) this.buttons.get(this.selection)).setHighlight(false);
        ((HighlightButton) this.buttons.get(buttonIndex)).setHighlight(true);
        this.selection = buttonIndex;
    }

    @Override
    protected void updateSectionSpecifics() {
    }
}
