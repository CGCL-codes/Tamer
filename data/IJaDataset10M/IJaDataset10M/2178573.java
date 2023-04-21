package calclipse.caldron.gui.theme.themes.corona;

import calclipse.core.disp.DefaultDisplayFactory;
import calclipse.lib.lcd.display.Display;
import calclipse.lib.lcd.display.DisplayScrollPane;

/**
 * This factory installs a
 * {@link calclipse.caldron.gui.theme.themes.corona.CoronaDisplayScrollPaneUI}
 * on the scroll pane.
 * @author T. Sommerland
 */
public class CoronaDisplayFactory extends DefaultDisplayFactory {

    private final CoronaImageBundle images;

    public CoronaDisplayFactory(final CoronaImageBundle images) {
        this.images = images;
    }

    @Override
    protected DisplayScrollPane createScrollPane(final Display display) {
        final DisplayScrollPane scrollPane = super.createScrollPane(display);
        scrollPane.setUI(new CoronaDisplayScrollPaneUI(images));
        return scrollPane;
    }
}
