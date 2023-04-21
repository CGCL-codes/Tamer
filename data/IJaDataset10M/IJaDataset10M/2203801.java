package graphicsMode;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionListener;
import javab.bling.BImageButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

public class BSplitPaneUI extends BasicSplitPaneUI {

    private ActionListener listener;

    private static final int DIVIDER_BUFFER = 5;

    public BSplitPaneUI(ActionListener listener) {
        this.listener = listener;
    }

    /**
     * Changes the Default Divider
     * Thank you open source JDK :)
     */
    protected void installDefaults() {
        super.installDefaults();
        Component leftC = splitPane.getLeftComponent();
        Component rightC = splitPane.getRightComponent();
        int lastLocation = splitPane.getDividerLocation();
        if (leftC != null) splitPane.setLeftComponent(null);
        if (rightC != null) splitPane.setRightComponent(null);
        splitPane.remove(divider);
        super.divider = new BSplitPaneDivider(this, listener);
        divider.setBasicSplitPaneUI(this);
        Border b = divider.getBorder();
        if (b == null || !(b instanceof UIResource)) {
            divider.setBorder(UIManager.getBorder("SplitPaneDivider.border"));
        }
        divider.setDividerSize(splitPane.getDividerSize());
        dividerSize = divider.getDividerSize();
        splitPane.add(divider, JSplitPane.DIVIDER);
        splitPane.add(nonContinuousLayoutDivider, BasicSplitPaneUI.NON_CONTINUOUS_DIVIDER, splitPane.getComponentCount());
        splitPane.setLeftComponent(leftC);
        splitPane.setRightComponent(rightC);
        splitPane.add(divider, JSplitPane.DIVIDER);
        splitPane.setDividerLocation(lastLocation);
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                splitPane.setDividerSize(((BSplitPaneDivider) divider).go.getPreferredSize().height + DIVIDER_BUFFER);
            }
        });
    }

    private static class BSplitPaneDivider extends BasicSplitPaneDivider {

        private BImageButton beginning, go, pause, stop, end;

        private BSplitPaneDivider(BasicSplitPaneUI ui, ActionListener listener) {
            super(ui);
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            JPanel holder = new JPanel();
            holder.setLayout(new BoxLayout(holder, BoxLayout.X_AXIS));
            holder.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            beginning = new BImageButton("beginning", "Beginning", listener);
            go = new BImageButton("go", "GO!", listener);
            pause = new BImageButton("pause", "Pause", listener);
            stop = new BImageButton("stop", "Stop", listener);
            end = new BImageButton("end", "End", listener);
            pause.setVisible(false);
            holder.add(beginning);
            holder.add(go);
            holder.add(pause);
            holder.add(end);
            add(Box.createHorizontalGlue());
            add(holder);
            add(Box.createHorizontalGlue());
        }
    }

    public BImageButton getBeginningButton() {
        return ((BSplitPaneDivider) divider).beginning;
    }

    public BImageButton getGoButton() {
        return ((BSplitPaneDivider) divider).go;
    }

    public BImageButton getPauseButton() {
        return ((BSplitPaneDivider) divider).pause;
    }

    public BImageButton getStopButton() {
        return ((BSplitPaneDivider) divider).stop;
    }

    public BImageButton getEndButton() {
        return ((BSplitPaneDivider) divider).end;
    }
}
