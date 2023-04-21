package org.aiotrade.platform.core.ui.netbeans.windows;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.Action;
import javax.swing.JPopupMenu;
import javax.swing.JViewport;
import javax.swing.Timer;
import org.aiotrade.charting.view.ChartViewContainer;
import org.aiotrade.charting.view.ChartingController;
import org.aiotrade.charting.view.ChartingControllerFactory;
import org.aiotrade.platform.core.analysis.chartview.RealtimeChartViewContainer;
import org.aiotrade.math.timeseries.descriptor.AnalysisContents;
import org.aiotrade.util.swing.JScrollView;
import org.aiotrade.platform.core.sec.Sec;
import org.aiotrade.platform.core.ui.netbeans.actions.SwitchCandleOhlcAction;
import org.aiotrade.platform.core.ui.netbeans.actions.SwitchCalendarTradingTimeViewAction;
import org.aiotrade.platform.core.ui.netbeans.actions.ZoomInAction;
import org.aiotrade.platform.core.ui.netbeans.actions.ZoomOutAction;
import org.aiotrade.charting.laf.LookFeel;
import org.openide.util.actions.SystemAction;
import org.openide.windows.Mode;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * This class implements serializbale by inheriting TopComponent, but should
 * overide writeExternal() and readExternal() to implement own serializable
 * instead of via transient modifies.
 *
 * @NOTICE:
 * when run/debug modules in NetBeans' IDE, the module will be
 * reloaded always, thus, when moduel has been disable but the reloading
 * procedure still not finished yet, deserialization will fail and throws
 * exception. So, it's better to test serialization out of the IDE.
 *
 * @author Caoyuan Deng
 */
public class RealtimeChartsTopComponent extends TopComponent {

    private static List<WeakReference<RealtimeChartsTopComponent>> instanceRefs = new ArrayList<WeakReference<RealtimeChartsTopComponent>>();

    private WeakReference<RealtimeChartsTopComponent> ref;

    private static final int SCROLL_SPEED_THROTTLE = 2400;

    /** The Mode this component will live in. */
    private static final String MODE = "realtimeCharts";

    private String s_id = "RealtimeCharts";

    private String symbol;

    private List<Sec> secs = new ArrayList<Sec>();

    private List<ChartViewContainer> viewContainers = new ArrayList<ChartViewContainer>();

    private MouseAdapter myMouseAdapter;

    private JPopupMenu popup;

    private JViewport viewPort;

    private JScrollView scrollView;

    private Timer scrollTimer;

    private ScrollTimerListener scrollTimerListener;

    private boolean reallyClosed;

    public RealtimeChartsTopComponent() {
        ref = new WeakReference<RealtimeChartsTopComponent>(this);
        instanceRefs.add(ref);
        initComponent();
        scrollTimerListener = new ScrollTimerListener();
        scrollTimer = new Timer(SCROLL_SPEED_THROTTLE, scrollTimerListener);
        scrollTimer.setInitialDelay(0);
    }

    private void initComponent() {
        setName("Watch List RealTime Charts");
        scrollView = new JScrollView(this, viewContainers);
        scrollView.setBackground(LookFeel.getCurrent().backgroundColor);
        setLayout(new BorderLayout());
        add(scrollView, BorderLayout.CENTER);
        popup = new JPopupMenu();
        popup.add(SystemAction.get(SwitchCandleOhlcAction.class));
        popup.add(SystemAction.get(SwitchCalendarTradingTimeViewAction.class));
        popup.add(SystemAction.get(ZoomInAction.class));
        popup.add(SystemAction.get(ZoomOutAction.class));
        myMouseAdapter = new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                showPopup(e);
            }

            public void mousePressed(MouseEvent e) {
                showPopup(e);
            }

            public void mouseReleased(MouseEvent e) {
                showPopup(e);
            }
        };
        addMouseListener(myMouseAdapter);
        setFocusable(true);
    }

    public void watch(Sec sec, AnalysisContents contents) {
        if (!secs.contains(sec)) {
            ChartingController controller = ChartingControllerFactory.createInstance(sec.getTickerSer(), contents);
            ChartViewContainer viewContainer = controller.createChartViewContainer(RealtimeChartViewContainer.class, this);
            viewContainer.setInteractive(false);
            secs.add(sec);
            viewContainers.add(viewContainer);
            scrollView.add(viewContainer);
            scrollView.repaint();
        }
        scrollTimer.stop();
        scrollTimerListener.startScrollTimerIfNecessary();
    }

    public void unWatch(Sec sec) {
        int idx = secs.indexOf(sec);
        if (idx != -1) {
            secs.remove(idx);
            ChartViewContainer viewContainer = viewContainers.get(idx);
            scrollView.remove(viewContainer);
            viewContainers.remove(idx);
        }
    }

    public Collection<ChartViewContainer> getViewContainers() {
        return viewContainers;
    }

    private void showPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            popup.show(this, e.getX(), e.getY());
        }
    }

    public void open() {
        Mode mode = WindowManager.getDefault().findMode(MODE);
        mode.dockInto(this);
        scrollTimer.stop();
        scrollTimerListener.startScrollTimerIfNecessary();
        super.open();
    }

    protected void componentActivated() {
        super.componentActivated();
    }

    public void setReallyClosed(boolean b) {
        this.reallyClosed = b;
    }

    @Override
    protected void componentClosed() {
        scrollTimer.stop();
        if (reallyClosed) {
            super.componentClosed();
        } else {
            TopComponent win = WindowManager.getDefault().findTopComponent("RealtimeWatchList");
            if (win.isOpened()) {
            } else {
                super.componentClosed();
            }
        }
    }

    protected String preferredID() {
        return s_id;
    }

    public int getPersistenceType() {
        return this.PERSISTENCE_NEVER;
    }

    public Action[] getActions() {
        Action[] actions = super.getActions();
        Action[] newActions = new Action[actions.length + 1];
        for (int i = 0; i < actions.length; i++) {
            newActions[i] = actions[i];
        }
        newActions[actions.length] = SystemAction.get(SwitchCandleOhlcAction.class);
        return newActions;
    }

    @Override
    protected final void finalize() throws Throwable {
        if (myMouseAdapter != null) {
            removeMouseListener(myMouseAdapter);
        }
        instanceRefs.remove(ref);
        super.finalize();
    }

    public static List<WeakReference<RealtimeChartsTopComponent>> getInstanceRefs() {
        return instanceRefs;
    }

    public static RealtimeChartsTopComponent getInstance() {
        RealtimeChartsTopComponent instance = null;
        if (instanceRefs.size() == 0) {
            instance = new RealtimeChartsTopComponent();
        } else {
            instance = instanceRefs.get(0).get();
        }
        if (!instance.isOpened()) {
            instance.open();
        }
        return instance;
    }

    /**
     * Listener for timer events.
     */
    private class ScrollTimerListener implements ActionListener {

        public void startScrollTimerIfNecessary() {
            if (viewContainers.size() <= 0 || scrollTimer.isRunning()) {
                return;
            }
            scrollTimer.start();
        }

        public void actionPerformed(ActionEvent e) {
            if (RealtimeChartsTopComponent.this.getMousePosition() == null) {
                scrollView.setBackground(LookFeel.getCurrent().backgroundColor);
                scrollView.scrollByPicture(1);
            }
        }
    }
}
