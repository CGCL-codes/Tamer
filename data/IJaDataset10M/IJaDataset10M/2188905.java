package sourceforge.shinigami.util;

import java.awt.Component;
import java.util.List;
import java.util.LinkedList;

public final class FlowManager {

    private List<ComponentScope> line = new LinkedList<ComponentScope>();

    private static final int DEFAULT_LINE_HEIGHT = 20;

    private static final int DEFAULT_LINE_TAB = 5;

    private static final int DEFAULT_SPACE_BETWEEN_COMPONENTS = 4;

    private static final int DEFAULT_SPACE_BETWEEN_LINES = 2;

    private FlowPanel host;

    private int xcaret = DEFAULT_LINE_TAB;

    private int ycaret = 5;

    private Alignment linePosition = Alignment.LEFT;

    public static enum Alignment {

        LEFT, CENTER, RIGHT
    }

    public void setAlignment(Alignment a) {
        this.linePosition = a;
    }

    FlowManager(FlowPanel host) {
        this.host = host;
    }

    public synchronized void add(Component c) {
        add(c, c.getPreferredSize().width, DEFAULT_LINE_HEIGHT);
    }

    public synchronized void add(Component c, int width) {
        add(c, width, DEFAULT_LINE_HEIGHT);
    }

    public synchronized void add(Component c, int width, int height) {
        line.add(new ComponentScope(c, width, height));
    }

    final synchronized void dispose() {
        this.host = null;
    }

    public synchronized void nextLine() {
        int lineWidth = 0;
        if (this.linePosition != Alignment.LEFT) {
            for (int i = 0; i < line.size(); i++) {
                lineWidth += line.get(i).WIDTH;
                if (line.size() > (i + 1)) lineWidth += DEFAULT_SPACE_BETWEEN_COMPONENTS;
            }
        }
        if (this.linePosition == Alignment.LEFT) xcaret = DEFAULT_LINE_TAB; else if (this.linePosition == Alignment.CENTER) xcaret = (host.getActualWidth() / 2) - (lineWidth / 2); else if (this.linePosition == Alignment.RIGHT) xcaret = host.getActualWidth() - lineWidth - DEFAULT_LINE_TAB;
        while (!line.isEmpty()) {
            ComponentScope cs = line.remove(0);
            if (cs.HOST != null) {
                cs.HOST.setBounds(xcaret, ycaret, cs.WIDTH, cs.HEIGHT);
                host.add(cs.HOST);
                xcaret += cs.WIDTH + DEFAULT_SPACE_BETWEEN_COMPONENTS;
            } else {
                xcaret += cs.WIDTH;
                ycaret += cs.HEIGHT;
            }
        }
        ycaret += DEFAULT_LINE_HEIGHT + DEFAULT_SPACE_BETWEEN_LINES;
    }

    public final void addX(int x) {
        line.add(new ComponentScope(null, x, 0));
    }

    public final void addY(int y) {
        line.add(new ComponentScope(null, 0, y));
    }

    private static final class ComponentScope {

        final Component HOST;

        final int WIDTH;

        final int HEIGHT;

        ComponentScope(Component host, int width, int height) {
            this.HOST = host;
            this.WIDTH = width;
            this.HEIGHT = height;
        }
    }
}
