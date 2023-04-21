package org.mov.chart;

import java.awt.*;
import java.util.*;
import org.mov.util.*;

public class GraphTools {

    public static void renderLine(Graphics g, BasicGraphDataSource source, int xoffset, int yoffset, float horizontalScale, float verticalScale, float bottomLineValue, Vector dates) {
        int x, y;
        int lastX = -1, lastY = -1;
        Float value;
        TradingDate date;
        Iterator iterator = dates.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            date = (TradingDate) iterator.next();
            if (date.compareTo(source.getStartDate()) < 0) {
                i++;
                continue;
            }
            if (date.compareTo(source.getEndDate()) > 0) break;
            value = source.getValue(date);
            if (value != null) {
                x = (int) (xoffset + horizontalScale * i);
                y = yoffset - scaleAndFitPoint(value.floatValue(), bottomLineValue, verticalScale);
                if (lastX != -1) g.drawLine(x, y, lastX, lastY); else g.drawLine(x, y, x, y);
                lastX = x;
                lastY = y;
            }
            i++;
        }
    }

    public static void renderBar(Graphics g, BasicGraphDataSource source, int xoffset, int yoffset, float horizontalScale, float verticalScale, float bottomLineValue, Vector dates) {
        int x2, y1, y2;
        int x1 = -1;
        Float value;
        float floatValue;
        TradingDate date;
        Iterator iterator = dates.iterator();
        int i = 0;
        y2 = yoffset - scaleAndFitPoint(0, bottomLineValue, verticalScale);
        while (iterator.hasNext()) {
            date = (TradingDate) iterator.next();
            if (date.compareTo(source.getStartDate()) < 0) {
                i++;
                continue;
            }
            if (date.compareTo(source.getEndDate()) > 0) break;
            value = source.getValue(date);
            if (value == null) floatValue = 0; else floatValue = value.floatValue();
            x2 = (int) (xoffset + horizontalScale * i);
            y1 = yoffset - scaleAndFitPoint(floatValue, bottomLineValue, verticalScale);
            if (x1 != -1) g.fillRect(x1, y1, Math.abs(x2 - x1) + 1, Math.abs(y2 - y1));
            x1 = x2 + 1;
            i++;
        }
    }

    public static int scaleAndFitPoint(float point, float offset, float scale) {
        return (int) ((point - offset) * scale);
    }

    public static HashMap createAnnotations(BasicGraphDataSource graph1, BasicGraphDataSource graph2) {
        HashMap annotations = new HashMap();
        TradingDate today = (TradingDate) graph1.getStartDate().clone();
        Float todayGraph1Value, todayGraph2Value;
        Float yesterdayGraph1Value, yesterdayGraph2Value;
        do {
            yesterdayGraph1Value = graph1.getValue(today);
            yesterdayGraph2Value = graph2.getValue(today);
            today.next(1);
        } while ((yesterdayGraph1Value == null || yesterdayGraph2Value == null) && today.compareTo(graph1.getEndDate()) <= 0);
        while (today.compareTo(graph1.getEndDate()) <= 0) {
            todayGraph1Value = graph1.getValue(today);
            todayGraph2Value = graph2.getValue(today);
            if (todayGraph1Value != null && todayGraph2Value != null) {
                if (todayGraph1Value.compareTo(todayGraph2Value) > 0 && yesterdayGraph1Value.compareTo(yesterdayGraph2Value) <= 0) annotations.put((TradingDate) today.clone(), "Buy"); else if (todayGraph1Value.compareTo(todayGraph2Value) < 0 && yesterdayGraph1Value.compareTo(yesterdayGraph2Value) >= 0) annotations.put((TradingDate) today.clone(), "Sell");
                yesterdayGraph1Value = todayGraph1Value;
                yesterdayGraph2Value = todayGraph2Value;
            }
            today.next(1);
        }
        return annotations;
    }
}
