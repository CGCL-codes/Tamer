package net.tourbook.chart;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

/**
 * Chart tooltip until version 11.7
 */
public class ToolTipV1 {

    /**
	 * maximum width in pixel for the width of the tooltip
	 */
    private static final int MAX_TOOLTIP_WIDTH = 500;

    private Chart _chart;

    private Shell _toolTipShell;

    private Composite _toolTipContainer;

    private Label _toolTipTitle;

    private Label _toolTipLabel;

    private ChartToolTipInfo _toolTipInfo;

    private Listener _toolTipListener;

    private final int[] _toolTipEvents = new int[] { SWT.MouseExit, SWT.MouseHover, SWT.MouseMove, SWT.MouseDown, SWT.DragDetect };

    private int _toolTipHoverSerieIndex;

    private int _toolTipHoverValueIndex;

    private int _hoveredBarSerieIndex;

    private int _hoveredBarValueIndex;

    public ToolTipV1(final Chart chart) {
        _chart = chart;
        _toolTipListener = new Listener() {

            public void handleEvent(final Event event) {
                switch(event.type) {
                    case SWT.MouseHover:
                    case SWT.MouseMove:
                        if (toolTip30Update(event.x, event.y)) {
                            break;
                        }
                    case SWT.MouseExit:
                    case SWT.MouseDown:
                        toolTip20Hide();
                        break;
                }
            }
        };
    }

    void dispose() {
        if (_toolTipShell != null) {
            toolTip20Hide();
            for (final int toolTipEvent : _toolTipEvents) {
                _chart.removeListener(toolTipEvent, _toolTipListener);
            }
            _toolTipShell.dispose();
            _toolTipShell = null;
            _toolTipContainer = null;
        }
    }

    void toolTip10Show(final int x, final int y, final int hoveredBarSerieIndex, final int hoveredBarValueIndex) {
        _hoveredBarSerieIndex = hoveredBarSerieIndex;
        _hoveredBarValueIndex = hoveredBarValueIndex;
        if (_toolTipShell == null) {
            _toolTipShell = new Shell(_chart.getShell(), SWT.ON_TOP | SWT.TOOL);
            final Display display = _toolTipShell.getDisplay();
            final Color infoColorBackground = display.getSystemColor(SWT.COLOR_INFO_BACKGROUND);
            final Color infoColorForeground = display.getSystemColor(SWT.COLOR_INFO_FOREGROUND);
            _toolTipContainer = new Composite(_toolTipShell, SWT.NONE);
            GridLayoutFactory.fillDefaults().extendedMargins(2, 5, 2, 3).applyTo(_toolTipContainer);
            _toolTipContainer.setBackground(infoColorBackground);
            _toolTipContainer.setForeground(infoColorForeground);
            {
                _toolTipTitle = new Label(_toolTipContainer, SWT.LEAD);
                _toolTipTitle.setBackground(infoColorBackground);
                _toolTipTitle.setForeground(infoColorForeground);
                _toolTipTitle.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.DIALOG_FONT));
                _toolTipLabel = new Label(_toolTipContainer, SWT.LEAD | SWT.WRAP);
                _toolTipLabel.setBackground(infoColorBackground);
                _toolTipLabel.setForeground(infoColorForeground);
            }
            for (final int toolTipEvent : _toolTipEvents) {
                _chart.addListener(toolTipEvent, _toolTipListener);
            }
        }
        if (toolTip30Update(x, y)) {
            _toolTipShell.setVisible(true);
        } else {
            toolTip20Hide();
        }
    }

    void toolTip20Hide() {
        if (_toolTipShell == null || _toolTipShell.isDisposed()) {
            return;
        }
        if (_toolTipShell.isVisible()) {
            _toolTipInfo.setReposition(true);
            _toolTipShell.setVisible(false);
        }
    }

    private boolean toolTip30Update(final int x, final int y) {
        final ChartToolTipInfo tooltipInfo = toolTip40GetInfo(x, y);
        if (tooltipInfo == null) {
            return false;
        }
        if (tooltipInfo.isDisplayed()) {
            if (tooltipInfo.isReposition() || toolTip60IsWrongPositioned()) {
                toolTip50SetPosition();
            }
            return true;
        }
        final String toolTipLabel = tooltipInfo.getLabel();
        final String toolTipTitle = tooltipInfo.getTitle();
        if (toolTipLabel.trim().equals(_toolTipLabel.getText().trim()) && toolTipTitle.trim().equals(_toolTipTitle.getText().trim())) {
            return true;
        }
        if (toolTipTitle != null) {
            _toolTipTitle.setText(toolTipTitle);
            _toolTipTitle.pack(true);
            _toolTipTitle.setVisible(true);
        } else {
            _toolTipTitle.setVisible(false);
        }
        _toolTipLabel.setText(toolTipLabel);
        GridDataFactory.fillDefaults().hint(SWT.DEFAULT, SWT.DEFAULT).applyTo(_toolTipLabel);
        _toolTipLabel.pack(true);
        Point containerSize = _toolTipContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
        if (containerSize.x > MAX_TOOLTIP_WIDTH) {
            GridDataFactory.fillDefaults().hint(MAX_TOOLTIP_WIDTH, SWT.DEFAULT).applyTo(_toolTipLabel);
            _toolTipLabel.pack(true);
            containerSize = _toolTipContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
        }
        _toolTipContainer.setSize(containerSize);
        _toolTipShell.pack(true);
        final Rectangle area = _toolTipShell.getClientArea();
        _toolTipContainer.setSize(area.width, area.height);
        toolTip50SetPosition();
        return true;
    }

    private ChartToolTipInfo toolTip40GetInfo(final int x, final int y) {
        if (_hoveredBarSerieIndex != -1) {
            final IChartInfoProvider toolTipInfoProvider = (IChartInfoProvider) _chart.getChartDataModel().getCustomData(ChartDataModel.BAR_TOOLTIP_INFO_PROVIDER);
            if (toolTipInfoProvider != null) {
                if (_toolTipHoverSerieIndex == _hoveredBarSerieIndex && _toolTipHoverValueIndex == _hoveredBarValueIndex) {
                    if (_toolTipInfo != null) {
                        _toolTipInfo.setIsDisplayed(true);
                    }
                } else {
                    _toolTipHoverSerieIndex = _hoveredBarSerieIndex;
                    _toolTipHoverValueIndex = _hoveredBarValueIndex;
                    _toolTipInfo = toolTipInfoProvider.getToolTipInfo(_hoveredBarSerieIndex, _hoveredBarValueIndex);
                }
                return _toolTipInfo;
            }
        }
        _toolTipHoverSerieIndex = -1;
        _toolTipHoverValueIndex = -1;
        return null;
    }

    /**
	 * Position the tooltip and ensure that it is not located off the screen.
	 */
    private void toolTip50SetPosition() {
        final Point cursorLocation = _chart.getDisplay().getCursorLocation();
        final int cursorHeight = 21;
        final Point tooltipSize = _toolTipShell.getSize();
        final Rectangle monitorRect = _chart.getMonitor().getBounds();
        final Point pt = new Point(cursorLocation.x, cursorLocation.y + cursorHeight + 2);
        pt.x = Math.max(pt.x, monitorRect.x);
        if (pt.x + tooltipSize.x > monitorRect.x + monitorRect.width) {
            pt.x = monitorRect.x + monitorRect.width - tooltipSize.x;
        }
        if (pt.y + tooltipSize.y > monitorRect.y + monitorRect.height) {
            pt.y = cursorLocation.y - 2 - tooltipSize.y;
        }
        _toolTipShell.setLocation(pt);
    }

    /**
	 * Check if the tooltip is too far away from the cursor position
	 * 
	 * @return Returns <code>true</code> when the cursor is too far away
	 */
    private boolean toolTip60IsWrongPositioned() {
        final Point cursorLocation = _chart.getDisplay().getCursorLocation();
        final Point toolTipLocation = _toolTipShell.getLocation();
        final int cursorAreaLength = 50;
        final Rectangle cursorArea = new Rectangle(cursorLocation.x - cursorAreaLength, cursorLocation.y - cursorAreaLength, 2 * cursorAreaLength, 2 * cursorAreaLength);
        if (cursorArea.contains(toolTipLocation)) {
            return false;
        } else {
            return true;
        }
    }
}
