package gui.model;

import com.trolltech.qt.core.QRectF;
import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QPainterPath;
import com.trolltech.qt.gui.QStyleOptionGraphicsItem;
import com.trolltech.qt.gui.QWidget;

public class PolygonEventItem extends EnvironmentEventItem {

    private double size;

    public PolygonEventItem() {
        this(10.0);
    }

    public PolygonEventItem(double size) {
        this.size = size;
    }

    protected void doDeepCloneFrom(EventItem origin) {
        PolygonEventItem item = (PolygonEventItem) origin;
        this.size = item.size;
    }

    public double size() {
        return size;
    }

    public void setSize(double newSize) {
        size = newSize;
        update();
    }

    @Override
    public QPainterPath shape() {
        QPainterPath path = new QPainterPath();
        path.addEllipse(new QRectF(-size, -size, size * 2, size * 2));
        return path;
    }

    @Override
    public QRectF boundingRect() {
        return new QRectF(-size, -size, size * 2, size * 2);
    }

    @Override
    public void paint(QPainter painter, QStyleOptionGraphicsItem arg1, QWidget arg2) {
        painter.setBrush(new QBrush(new QColor(64, 64, 255, 128)));
        painter.drawEllipse(new QRectF(-size, -size, size * 2, size * 2));
    }
}
