package ircam.jmax.editors.bpf;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.undo.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import ircam.jmax.*;
import ircam.jmax.fts.*;
import ircam.jmax.toolkit.*;
import ircam.jmax.editors.bpf.tools.*;

/**
* The graphic component containing the tracks of a Sequence.
 */
public class BpfPanel extends JPanel implements Editor, BpfDataListener, ListSelectionListener, ScrollManager {

    FtsBpfObject bpfData;

    EditorContainer itsContainer;

    Box trackPanel;

    JScrollBar itsTimeScrollbar;

    Geometry geometry;

    BpfToolManager manager;

    public final int INITIAL_ZOOM = 20;

    public static final int MINIMUM_TIME = 10000;

    public static Color violetColor = new Color(102, 102, 153);

    public static Font rulerFont = new Font("SansSerif", Font.PLAIN, 10);

    BpfEditor editor;

    /**
    * Constructor based on a SequenceDataModel containing the tracks to edit.
   */
    public BpfPanel(EditorContainer container, FtsBpfObject data) {
        itsContainer = container;
        setDoubleBuffered(false);
        bpfData = data;
        bpfData.addBpfListener(this);
        {
            geometry = new Geometry();
            geometry.setXZoom(20);
            geometry.setYZoom(300);
            geometry.setYTransposition(136);
        }
        manager = new BpfToolManager(new BpfTools());
        Tool arrow = manager.getToolByName("edit tool");
        manager.activate(arrow, null);
        setLayout(new BorderLayout());
        JPanel container_panel = new JPanel();
        container_panel.setLayout(new BorderLayout());
        container_panel.setPreferredSize(new Dimension(BpfWindow.DEFAULT_WIDTH, BpfWindow.DEFAULT_HEIGHT));
        container_panel.setSize(BpfWindow.DEFAULT_WIDTH, BpfWindow.DEFAULT_HEIGHT);
        editor = new BpfEditor(geometry, bpfData, manager);
        editor.setBorder(new EtchedBorder());
        editor.getGraphicContext().setFrame(itsContainer.getFrame());
        editor.getGraphicContext().setScrollManager(this);
        container_panel.add(editor, BorderLayout.CENTER);
        manager.addContextSwitcher(new WindowContextSwitcher(editor.getGraphicContext().getFrame(), editor.getGraphicContext()));
        editor.getGraphicContext().getSelection().addListSelectionListener(this);
        geometry.addZoomListener(new ZoomListener() {

            public void zoomChanged(float zoom, float oldZoom) {
                repaint();
                if ((editor.getSize().width > 0) && (zoom != oldZoom)) resizePanelToLastPoint();
            }
        });
        addComponentListener(new ComponentAdapter() {

            public void componentResized(ComponentEvent e) {
                repaint();
                if (editor.getSize().width > 0) resizePanelToLastPoint();
            }
        });
        int totalTime = (int) editor.getGraphicContext().getAdapter().getInvX(BpfWindow.DEFAULT_WIDTH);
        itsTimeScrollbar = new JScrollBar(Scrollbar.HORIZONTAL, 0, totalTime, 0, totalTime);
        itsTimeScrollbar.setVisible(false);
        itsTimeScrollbar.setUnitIncrement(10);
        itsTimeScrollbar.setBlockIncrement(1000);
        itsTimeScrollbar.addAdjustmentListener(new AdjustmentListener() {

            public void adjustmentValueChanged(AdjustmentEvent e) {
                int currentTime = e.getValue();
                geometry.setXTransposition(-currentTime);
            }
        });
        container_panel.add(itsTimeScrollbar, BorderLayout.SOUTH);
        container_panel.validate();
        add(container_panel, BorderLayout.CENTER);
        validate();
    }

    public void setContainer(EditorContainer cont) {
        itsContainer = cont;
        editor.getGraphicContext().setFrame(itsContainer.getFrame());
        editor.resetListDialog();
        manager.addContextSwitcher(new WindowContextSwitcher(editor.getGraphicContext().getFrame(), editor.getGraphicContext()));
    }

    /**
    * called when the database is changed: BpfDataListener interface
   */
    public void pointAdded(int index) {
        resizePanelToPointTime(bpfData.getPointAt(index));
    }

    public void pointsDeleted(int index, int size) {
    }

    public void pointChanged(int oldIndex, int newIndex, float newTime, float newValue) {
        if (oldIndex != newIndex) resizePanelToPointTime(bpfData.getPointAt(newIndex));
    }

    public void pointsChanged() {
    }

    public void cleared() {
    }

    public void nameChanged(String name) {
    }

    private void resizePanelToPointTime(BpfPoint point) {
        int evtTime = (int) (point.getTime());
        resizePanelToTime(evtTime);
    }

    private void resizePanelToTime(int time) {
        int maxVisibleTime = getMaximumVisibleTime();
        int maximumTime = getMaximumTime();
        if (time > maximumTime) {
            resizePanelToLastPoint();
            itsTimeScrollbar.setValue(time);
        } else if ((time > maxVisibleTime) || (time < -geometry.getXTransposition())) itsTimeScrollbar.setValue(time);
    }

    private void resizePanelToLastPoint() {
        BpfPoint lastPoint = bpfData.getLastPoint();
        int time = 0;
        int timeWindow = editor.getGraphicContext().getTimeWindow();
        if (lastPoint != null) time = (int) (lastPoint.getTime() + BpfAdapter.DX / geometry.getXZoom());
        if (time < timeWindow) time = timeWindow;
        itsTimeScrollbar.setVisible((time > timeWindow));
        itsTimeScrollbar.setValue(0);
        itsTimeScrollbar.setVisibleAmount(timeWindow);
        itsTimeScrollbar.setMaximum(time);
    }

    public Frame getFrame() {
        return itsContainer.getFrame();
    }

    public FtsBpfObject getFtsObject() {
        return bpfData;
    }

    public BpfEditor getBpfEditor() {
        return editor;
    }

    public EditorContainer getEditorContainer() {
        return itsContainer;
    }

    public void close(boolean doCancel) {
        ((FtsObjectWithEditor) bpfData).closeEditor();
        bpfData.requestDestroyEditor();
    }

    public void save() {
    }

    public void saveAs() {
    }

    public void print() {
    }

    /**
    * ListSelectionListener interface
   */
    public void valueChanged(ListSelectionEvent e) {
        BpfSelection sel = editor.getGraphicContext().getSelection();
        if (sel.size() == 1) {
            BpfPoint pt = (BpfPoint) sel.getSelected().nextElement();
            makeVisible(pt);
        }
    }

    public boolean pointIsVisible(BpfPoint point) {
        int time = (int) point.getTime();
        int startTime = -geometry.getXTransposition();
        int endTime = geometry.sizeToMsec(geometry, getSize().width) - 1;
        return ((time > startTime) && (time < endTime));
    }

    public boolean pointIsVisible(int x, int y) {
        Rectangle r = itsContainer.getViewRectangle();
        return ((x > r.x) && (x < r.x + r.width));
    }

    public boolean pointIsScrollable(int x, int y) {
        return !((editor.getGraphicContext().getLogicalTime() == 0) && (x <= 0));
    }

    private int scrollingDelta = 10;

    private int scrolledDelta = 2;

    public int scrollBy(int x, int y) {
        Rectangle r = itsContainer.getViewRectangle();
        if (x < r.x) {
            if (itsTimeScrollbar.getValue() - scrollingDelta > 0) {
                itsTimeScrollbar.setValue(itsTimeScrollbar.getValue() - scrollingDelta);
                return -scrolledDelta;
            } else return 0;
        } else {
            if (x > r.x + r.width) {
                int value = itsTimeScrollbar.getValue() + scrollingDelta;
                if (value > itsTimeScrollbar.getMaximum() - itsTimeScrollbar.getVisibleAmount()) itsTimeScrollbar.setMaximum(itsTimeScrollbar.getMaximum() + scrollingDelta);
                itsTimeScrollbar.setValue(value);
                return scrolledDelta;
            } else return 0;
        }
    }

    public void makeVisible(BpfPoint pt) {
        int time = (int) pt.getTime();
        int startTime = -geometry.getXTransposition();
        int endTime = geometry.sizeToMsec(geometry, getSize().width) - 1;
        if ((time < startTime) || (time > endTime)) itsTimeScrollbar.setValue(time);
    }

    public boolean isScrollbarVisible() {
        return itsTimeScrollbar.isVisible();
    }

    public int getMaximumVisibleTime() {
        return geometry.sizeToMsec(geometry, getSize().width) - 1;
    }

    public int getMaximumTime() {
        int maxTransp = -(itsTimeScrollbar.getMaximum() - itsTimeScrollbar.getVisibleAmount());
        int size = getSize().width - BpfAdapter.DX;
        return (int) ((size) / geometry.getXZoom() - maxTransp);
    }

    public void scrollIfNeeded(int time) {
        resizePanelToTime(time);
    }

    public void scrollToValue(int value) {
        itsTimeScrollbar.setValue(value);
    }

    public Rectangle getViewRectangle() {
        return getBpfEditor().getBounds();
    }
}
