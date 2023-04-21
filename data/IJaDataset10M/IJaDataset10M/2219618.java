package tm.subWindowPkg;

import java.awt.*;

public class SubWinLayout implements LayoutManager {

    public SubWinLayout() {
    }

    public void addLayoutComponent(String name, Component comp) {
        ;
    }

    public void layoutContainer(Container parent) {
    }

    public Dimension minimumLayoutSize(Container parent) {
        Dimension d = new Dimension(41, 41);
        return d;
    }

    public Dimension preferredLayoutSize(Container parent) {
        Dimension d = new Dimension(341, 341);
        return d;
    }

    public void removeLayoutComponent(Component comp) {
        ;
    }
}
