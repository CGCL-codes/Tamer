package org.swiftgantt.android.ui;

import org.swiftgantt.android.AndroidCanvas;
import org.swiftgantt.core.adapter.GanttColor;
import org.swiftgantt.core.adapter.GanttRectangle;
import org.swiftgantt.core.layout.AccurateTaskRenderer;
import org.swiftgantt.core.layout.TaskRenderer;
import android.graphics.Canvas;
import android.util.Log;

/**
 * 
 * @author Yuxing Wang
 *
 */
public class ChartView extends BaseView {

    protected ChartView chartView = null;

    protected TaskRenderer taskRenderer = null;

    public ChartView(AndGanttContext context) {
        super(context);
    }

    /**
	 * TODO 这个方法的实现其实和Swing版本的ChartViewUI基本差不多，是不是可以考虑抽象出来？
	 */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.taskRenderer = AccurateTaskRenderer.getRenderer(super.context);
        Log.i(this.getClass().getSimpleName(), "Paint Gantt Chart View");
        int width = 0;
        width = context.getTotalGanttSteps() * context.getConfig().getTimeUnitWidth();
        int height = super.getHeight();
        AndroidCanvas g = new AndroidCanvas(canvas);
        GanttRectangle rec = new GanttRectangle(0, 0, width, height);
        super.timeAxis.paintChartScale(g, this, rec);
        int rowHeight = super.context.getConfig().getGanttChartRowHeight();
        GanttRectangle rec2 = new GanttRectangle(0, 0, width, height - rowHeight * 2);
        this.taskRenderer.paint(g, this, rec2);
        g.setColor(GanttColor.black);
        g.drawRect(0, 0, width - 1, height - 1);
        Log.d(this.getClass().getSimpleName(), "Paint ChartViewUI done");
    }
}
