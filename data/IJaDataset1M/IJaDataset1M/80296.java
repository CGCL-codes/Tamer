package jorgan.gui.construct.layout;

import java.awt.Point;
import java.util.List;
import javax.swing.Icon;
import jorgan.disposition.Displayable;
import jorgan.gui.console.View;
import bias.Configuration;

/**
 * The Layout for views.
 */
public abstract class ViewLayout {

    private String name;

    private Icon icon;

    protected ViewLayout() {
        Configuration config = Configuration.getRoot().get(getClass());
        config.read(this);
    }

    /**
	 * Get the name of this layout.
	 * 
	 * @return name
	 */
    public String getName() {
        return name;
    }

    /**
	 * Get the icon of this layout.
	 * 
	 * @return icon
	 */
    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAlign() {
        return true;
    }

    /**
	 * Layout the given views.
	 * 
	 * @param pressed
	 *            the pressed view
	 * @param views
	 *            the selected views to layout
	 */
    public void layout(View<? extends Displayable> pressed, List<View<? extends Displayable>> views) {
        init(pressed, views);
        for (int s = 0; s < views.size(); s++) {
            View<? extends Displayable> view = views.get(s);
            visit(view, s);
        }
    }

    protected void changePosition(View<? extends Displayable> view, int x, int y) {
        view.getContainer().setLocation(view, new Point(x, y));
    }

    protected void init(View<? extends Displayable> pressed, List<View<? extends Displayable>> views) {
    }

    protected void visit(View<? extends Displayable> view, int index) {
    }
}
