package com.peterhi.client.ui;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import com.peterhi.client.ui.constants.ImageConstants;

public class ClassMemberCanvas extends Canvas implements PaintListener, ImageConstants {

    private ClassMemberBean bean;

    public ClassMemberCanvas(Composite parent, int style) {
        super(parent, style);
        addPaintListener(this);
    }

    public ClassMemberBean getBean() {
        return bean;
    }

    public void setBean(ClassMemberBean bean) {
        this.bean = bean;
    }

    public void paintControl(PaintEvent e) {
        if (bean == null) {
            return;
        }
        String name = truncateChars(bean.getName());
        Image image = bean.getImage();
        int mid = 0;
        int begin = 0;
        if (image != null) {
            mid = (getSize().y - image.getBounds().height) / 2;
            begin = image.getBounds().width + 2 * mid;
            e.gc.drawImage(image, mid, mid);
        }
        Point pt;
        if (name != null) {
            e.gc.drawString(name, begin, 0);
            pt = e.gc.stringExtent(name);
        } else {
            pt = e.gc.stringExtent("!");
        }
        if (bean.isTalking()) {
            e.gc.drawImage(talk16, 0, 0);
        }
        int y = pt.y;
        if (bean.isMicEnabled()) {
            if (bean.isMuted()) {
                e.gc.drawImage(mutemic16, begin, y);
            } else {
                e.gc.drawImage(enablemic16, begin, y);
            }
        } else {
            e.gc.drawImage(disablemic16, begin, y);
        }
        if (bean.isTextEnabled()) {
            if (bean.isSquelched()) {
                e.gc.drawImage(unsquelchtext16, begin + 18, y);
            } else {
                e.gc.drawImage(enabletext16, begin + 18, y);
            }
        } else {
            e.gc.drawImage(disabletext16, begin + 18, y);
        }
    }

    private String truncateChars(String s) {
        int limit = Math.min(s.length(), 12);
        String ret = s.substring(0, limit);
        if (limit != s.length()) {
            ret += "...";
        }
        return ret;
    }
}
