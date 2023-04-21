package nlp.lang.he.morph.erel.awt;

import java.awt.Button;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Point;

/**
 * Layouts components in a grid, like a GridLayout, with one exception: each
 * component is resized to it's PREFERRED size (in the dimension with more
 * components) and not to equal size.
 */
public class FairGridLayout extends GridLayout {

    private byte first_layout;

    /**
	 * set this variable to "System.err" (or another print stream) if you want
	 * to debug this class
	 */
    public static java.io.PrintStream log_stream = nlp.lang.he.morph.erel.io.NullPrintStream.instance;

    public static final byte DONT_LAYOUT = -1;

    public static final byte LAYOUT_BY_ROWS = 0;

    public static final byte LAYOUT_BY_COLS = 1;

    public FairGridLayout(int rows, int cols, byte first_layout) {
        super(rows, cols);
        this.first_layout = first_layout;
    }

    public FairGridLayout(int rows, int cols) {
        super(rows, cols);
        this.first_layout = cols >= rows ? LAYOUT_BY_ROWS : LAYOUT_BY_COLS;
    }

    protected void layoutByRows(Container parent) {
        log_stream.println("layout by rows");
        Point pos = new Point(0, 0);
        for (int row = 0; row < getRows(); ++row) {
            pos.x = 0;
            for (int col = 0; col < getColumns(); ++col) {
                int i = row * getColumns() + col;
                if (i >= parent.countComponents()) return;
                Component cur_component = parent.getComponent(i);
                Dimension cur_size = cur_component.preferredSize();
                cur_size.height = cur_component.size().height;
                cur_component.resize(cur_size);
                pos.y = cur_component.location().y;
                cur_component.move(pos.x, pos.y);
                pos.x += cur_size.width;
            }
        }
    }

    protected void layoutByCols(Container parent) {
        log_stream.println("layout by cols");
        Point pos = new Point(0, 0);
        for (int col = 0; col < getColumns(); ++col) {
            pos.y = 0;
            for (int row = 0; row < getRows(); ++row) {
                int i = row * getColumns() + col;
                if (i >= parent.countComponents()) return;
                Component cur_component = parent.getComponent(i);
                cur_component.show();
                Dimension cur_size = cur_component.preferredSize();
                cur_size.width = cur_component.size().width;
                cur_component.resize(cur_size);
                pos.x = cur_component.location().x;
                cur_component.move(pos.x, pos.y);
                pos.y += cur_size.height;
            }
        }
    }

    public void layoutContainer(Container parent) {
        super.layoutContainer(parent);
        if (first_layout == LAYOUT_BY_ROWS) layoutByRows(parent); else if (first_layout == LAYOUT_BY_COLS) layoutByCols(parent);
    }

    /**
	 * a test-driver for FairGridLayout
	 */
    public static class Test extends Frame {

        public Test() {
            setLayout(new FairGridLayout(3, 4));
            add(new Button("1 in a row"));
            add(new Empty());
            add(new Empty());
            add(new Empty());
            add(new Empty());
            add(new Button("2 in a "));
            add(new Button("row"));
            add(new Empty());
            add(new Button("4"));
            add(new Button("in"));
            add(new Button("a"));
            add(new Button("row"));
        }

        public static void main(String[] args) {
            Frame me = new FairGridLayout.Test();
            me.pack();
            me.show();
        }

        public boolean action(Event e, Object arg) {
            if (e.target instanceof Button) {
                Button target = (Button) e.target;
                target.setLabel(target.getLabel() + "x");
                hide();
                pack();
                show();
                return true;
            }
            return false;
        }
    }
}
