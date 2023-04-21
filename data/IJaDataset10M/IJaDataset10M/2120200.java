package jpatch.boundary;

import java.awt.*;
import java.awt.event.*;
import jpatch.entity.*;

public class MotionCurveDisplay extends VirtualCanvas {

    AnimObject animObject;

    int X = -1;

    public MotionCurveDisplay(SmartScrollPane smartScrollPane) {
        super(smartScrollPane);
        init();
    }

    public void init() {
        width = viewWidth = MainFrame.getInstance().getAnimation().getEnd() - MainFrame.getInstance().getAnimation().getStart() + 1;
        if (MainFrame.getInstance().getSelection() != null && MainFrame.getInstance().getSelection().getHotObject() instanceof AnimObject) animObject = (AnimObject) MainFrame.getInstance().getSelection().getHotObject();
        height = viewHeight = 1;
        if (animObject != null) {
            MotionCurveSet motionCurveSet = MainFrame.getInstance().getAnimation().getCurvesetFor(animObject);
            height = motionCurveSet.motionCurveList.size();
            viewHeight = Math.min(3, height);
        }
        top = viewTop = 0;
        left = viewLeft = MainFrame.getInstance().getAnimation().getStart() - 0.5f;
    }

    public void paint(Graphics g) {
        int pixelHeight = parent.getCanvasHeight();
        int pixelWidth = parent.getCanvasWidth();
        g.setClip(0, 0, pixelWidth, pixelHeight);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, pixelWidth, pixelHeight);
        g.setColor(Color.WHITE);
        float pos = MainFrame.getInstance().getAnimation().getPosition();
        MotionCurveSet motionCurveSet = MainFrame.getInstance().getAnimation().getCurvesetFor(animObject);
        float y = top;
        if (motionCurveSet != null) {
            for (int c = 0, nc = motionCurveSet.motionCurveList.size(); c < nc; c++) {
                MotionCurve motionCurve = (MotionCurve) motionCurveSet.motionCurveList.get(c);
                int yy = (int) (pixelHeight * ((y - viewTop) / viewHeight) + (double) pixelHeight / viewHeight * 0.05);
                int h = (int) ((double) pixelHeight / viewHeight * 0.9);
                if (yy > -h && yy < pixelHeight + h) {
                    if (false) g.setColor(new Color(0x007700)); else g.setColor(new Color(0x0000cc));
                    g.fillRect(0, yy, pixelWidth, h);
                }
                y++;
            }
        }
        g.setColor(Color.BLACK);
        float frameWidth = (float) pixelWidth / viewWidth;
        if (frameWidth > 4) {
            for (int x = 0, f = 0; x < pixelWidth; x++) {
                float t = viewLeft + (float) x * viewWidth / (float) pixelWidth;
                if ((int) t > f) {
                    f = (int) Math.round(t);
                    g.drawLine(x - 1, 0, x - 1, pixelHeight);
                }
            }
        }
        y = top;
        MotionKey selectedKey = null;
        if (motionCurveSet != null) {
            for (int c = 0, nc = motionCurveSet.motionCurveList.size(); c < nc; c++) {
                MotionCurve motionCurve = (MotionCurve) motionCurveSet.motionCurveList.get(c);
                int yy = (int) (pixelHeight * ((y - viewTop) / viewHeight) + (double) pixelHeight / viewHeight * 0.05);
                int h = (int) ((double) pixelHeight / viewHeight * 0.9);
                if (yy > -h && yy < pixelHeight + h) {
                    if (motionCurve instanceof MotionCurve.Float) {
                        if (false) g.setColor(new Color(0x33cc33)); else g.setColor(new Color(0x7777ff));
                        for (int x = 0; x < pixelWidth; x++) {
                            float t = viewLeft + (float) x * viewWidth / (float) pixelWidth;
                            float min = ((MotionCurve.Float) motionCurve).getMin();
                            float max = ((MotionCurve.Float) motionCurve).getMax();
                            float value = ((MotionCurve.Float) motionCurve).getFloatAt(t);
                            value = clamp(value, min, max);
                            int yv = yy + (int) (h * (1.0 - ((value - min) / (max - min))));
                            int y0 = yy + (int) (h * (1.0 - ((0.0 - min) / (max - min))));
                            g.drawLine(x, y0, x, yv);
                        }
                    }
                    for (int k = 0, nk = motionCurve.getKeyCount(); k < nk; k++) {
                        MotionKey mk = motionCurve.getKey(k);
                        int x = (int) ((mk.getPosition() - viewLeft) * (float) pixelWidth / (float) viewWidth);
                        if (mk instanceof MotionKey.Float) {
                            float value = ((MotionKey.Float) mk).getFloat();
                            float min = ((MotionCurve.Float) motionCurve).getMin();
                            float max = ((MotionCurve.Float) motionCurve).getMax();
                            value = clamp(value, min, max);
                            int ym = yy + (int) (h * (1.0 - (value - min) / (max - min)));
                            g.setColor((mk.getPosition() == pos) ? Color.WHITE : Color.ORANGE);
                            g.fillRect(x - 2, ym - 2, 5, 5);
                            if (mk == selectedKey) {
                                g.setColor(Color.YELLOW);
                                g.drawRect(x - 4, ym - 4, 8, 8);
                            }
                        } else {
                            int ym = yy + h / 2;
                            if (mk instanceof MotionKey.Color3f) {
                                g.setColor(((MotionKey.Color3f) mk).getColor3f().get());
                                g.fillRect(x - 4, ym - 4, 9, 9);
                                g.setColor(Color.WHITE);
                                g.drawRect(x - 5, ym - 5, 10, 10);
                                if (mk == selectedKey) {
                                    g.setColor(Color.YELLOW);
                                    g.drawRect(x - 6, ym - 6, 12, 12);
                                }
                            } else {
                                g.setColor((mk.getPosition() == pos) ? Color.WHITE : Color.ORANGE);
                                g.fillRect(x - 2, ym - 2, 5, 5);
                                if (mk == selectedKey) {
                                    g.setColor(Color.YELLOW);
                                    g.drawRect(x - 4, ym - 4, 8, 8);
                                }
                            }
                        }
                    }
                    g.setColor(Color.WHITE);
                    g.drawString(motionCurve.getName() + " [" + motionCurve.getInterpolationMethod().toString() + "]", 4, yy + 16);
                }
                y++;
            }
        }
        X = (int) ((pos - viewLeft) * (float) pixelWidth / viewWidth);
        g.setColor(Color.RED);
        g.drawLine(X, 0, X, pixelHeight - 1);
    }

    public void mousePressed(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
            MotionCurveSet motionCurveSet = MainFrame.getInstance().getAnimation().getCurvesetFor(animObject);
            int pixelHeight = parent.getCanvasHeight();
            int pixelWidth = parent.getCanvasWidth();
            float t = Math.round(viewLeft + (float) mouseEvent.getX() * viewWidth / (float) parent.getCanvasWidth());
            float y = top;
            loop: if (motionCurveSet != null) {
                for (int c = 0, nc = motionCurveSet.motionCurveList.size(); c < nc; c++) {
                    MotionCurve motionCurve = (MotionCurve) motionCurveSet.motionCurveList.get(c);
                    int yy = (int) (pixelHeight * ((y - viewTop) / viewHeight) + (double) pixelHeight / viewHeight * 0.05);
                    int h = (int) ((double) pixelHeight / viewHeight * 0.9);
                    if (yy > -h && yy < pixelHeight + h) {
                        for (int k = 0, nk = motionCurve.getKeyCount(); k < nk; k++) {
                            MotionKey mk = motionCurve.getKey(k);
                            int x = (int) ((mk.getPosition() - viewLeft) * (float) pixelWidth / (float) viewWidth);
                            int ym;
                            if (mk instanceof MotionKey.Float) {
                                float value = ((MotionKey.Float) mk).getFloat();
                                float min = ((MotionCurve.Float) motionCurve).getMin();
                                float max = ((MotionCurve.Float) motionCurve).getMax();
                                value = clamp(value, min, max);
                                ym = yy + (int) (h * (1.0 - (value - min) / (max - min)));
                            } else ym = yy + h / 2;
                            if (mouseEvent.getX() > x - 6 && mouseEvent.getX() < x + 6 && mouseEvent.getY() > ym - 6 && mouseEvent.getY() < ym + 6) {
                                t = mk.getPosition();
                                break loop;
                            }
                        }
                    }
                    y++;
                }
            }
        } else if (mouseEvent.getButton() == MouseEvent.BUTTON3) {
            MotionCurveSet motionCurveSet = MainFrame.getInstance().getAnimation().getCurvesetFor(animObject);
            int pixelHeight = parent.getCanvasHeight();
            float y = top;
            if (motionCurveSet != null) {
                for (int c = 0, nc = motionCurveSet.motionCurveList.size(); c < nc; c++) {
                    MotionCurve motionCurve = (MotionCurve) motionCurveSet.motionCurveList.get(c);
                    int yy = (int) (pixelHeight * ((y - viewTop) / viewHeight) + (double) pixelHeight / viewHeight * 0.05);
                    int h = (int) ((double) pixelHeight / viewHeight * 0.9);
                    if (mouseEvent.getY() >= yy && mouseEvent.getY() <= yy + h) {
                        if (motionCurve.getInterpolationMethod() == MotionCurve.CUBIC) motionCurve.setInterpolationMethod(MotionCurve.LINEAR); else if (motionCurve.getInterpolationMethod() == MotionCurve.LINEAR) motionCurve.setInterpolationMethod(MotionCurve.CUBIC);
                    }
                    y++;
                }
            }
        }
        parent.repaint();
    }

    private float clamp(float v, float min, float max) {
        return Math.min(max, Math.max(min, v));
    }
}
