package com.turnpage;

import android.graphics.Point;

/**
 * �����ڼ���3��ı��������
 * @author Jason
 * @since: 2011-4-11
 */
public class Bezier {

    /**
	 * ���ڱ�������ĵ�����
	 */
    private Point[] points;

    /**
	 * ÿһ�������ֵ
	 */
    private double step = 0.02;

    private Point p0;

    private Point p1;

    private Point p2;

    /**
	 * ����3����
	 * @param p0
	 * @param p1
	 * @param p2
	 */
    public Bezier(Point p0, Point p1, Point p2) {
        super();
        this.p0 = p0;
        this.p1 = p1;
        this.p2 = p2;
    }

    /**
	 * ���㱴��������ϵĵ�
	 * @author Jason
	 * @since:2011-4-11
	 */
    public void calculate() {
        if (p0 == null || p1 == null || p2 == null) {
            return;
        }
        int count = (int) (1 / step);
        if ((count * step) > 1) {
            count = count + 1;
        }
        points = new Point[count];
        Point point = null;
        int i = 0;
        for (double t = 0.0; t < 1; t += step, i++) {
            point = new Point();
            point.x = (int) ((1 - t) * (1 - t) * p0.x + 2 * (1 - t) * t * p1.x + t * t * p2.x);
            point.y = (int) ((1 - t) * (1 - t) * p0.y + 2 * (1 - t) * t * p1.y + t * t * p2.y);
            if (i >= count) {
                break;
            }
            points[i] = point;
        }
    }

    public double getStep() {
        return step;
    }

    /**
	 * ���õ�֮�䲽��
	 * @author Jason
	 * @since:2011-4-11
	 * @param step ֵΪ0.0001~0.9999 ������Χ����ʹ��Ĭ��ֵ0.02
	 */
    public void setSetp(double step) {
        if (step >= 0.0001 && step <= 0.9999) {
            this.step = step;
        }
    }

    /**
	 * ��ȡ�����ı�������ߵ�
	 * @author Jason
	 * @since:2011-4-11
	 * @return
	 */
    public Point[] getPoints() {
        if (points == null) {
            calculate();
        }
        return points;
    }

    /**
	 * ��ȡ��������ߵ��е�
	 * @author Jason
	 * @since:2011-4-15
	 * @return
	 */
    public Point getCenterPoint() {
        if (points == null) {
            calculate();
        }
        if (points == null) {
            return null;
        }
        return points[points.length / 2];
    }
}
