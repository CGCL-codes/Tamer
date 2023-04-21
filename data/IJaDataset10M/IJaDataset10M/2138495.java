package radui;

import javax.microedition.lcdui.Graphics;

public class ScrollView extends View {

    private View view_ = null;

    private int offsety_ = 0;

    private int maxoffsety_ = 0;

    private int step_;

    private ScrollTheme st_;

    public ScrollView(View v, int step, ScrollTheme st) {
        view_ = v;
        step_ = step;
        st_ = st;
    }

    public void paint(Graphics g, int w, int h) {
        if (view_ != null) maxoffsety_ = view_.getHeight(w - st_.width) - h;
        int tx = g.getTranslateX();
        int ty = g.getTranslateY();
        int cx = g.getClipX();
        int cy = g.getClipY();
        int cw = g.getClipWidth();
        int ch = g.getClipHeight();
        int offsety = offsety_;
        g.translate(0, -offsety);
        g.setClip(cx, offsety, cw - st_.width, h);
        if (view_ != null) view_.paint(g, w - st_.width, h + offsety);
        g.translate(0, offsety);
        g.setClip(cx, cy, cw, ch);
        util.Debug.assert2(g.getTranslateX() == tx, "ScrollView::paint translation x error " + tx + " " + g.getTranslateX());
        util.Debug.assert2(g.getTranslateY() == ty, "ScrollView::paint translation y error " + ty + " " + g.getTranslateY());
        int oldColor = g.getColor();
        g.setColor(st_.colorBack);
        g.fillRect(w - st_.width, 0, st_.width, h);
        g.setColor(st_.color);
        int start = (int) (offsety / (double) (maxoffsety_ + h) * h);
        int end = (int) (h / (double) (maxoffsety_ + h) * h);
        g.fillRect(w - st_.width, start, st_.width, end);
        g.setColor(oldColor);
    }

    public void keyPressed(ScreenCanvas sc, int keyCode) {
        if (view_ != null) view_.keyPressed(sc, keyCode);
        int gameAction = sc.getGameAction(keyCode);
        switch(gameAction) {
            case ScreenCanvas.UP:
                offsety_ -= step_;
                if (offsety_ < 0) offsety_ = 0;
                sc.repaint();
                break;
            case ScreenCanvas.DOWN:
                offsety_ += step_;
                if (offsety_ >= maxoffsety_) offsety_ = maxoffsety_;
                sc.repaint();
                break;
        }
    }

    public void keyRepeated(ScreenCanvas sc, int keyCode) {
        keyPressed(sc, keyCode);
    }

    public int getMaxWidth() {
        if (view_ != null) return view_.getMaxWidth(); else return super.getMaxWidth();
    }

    public int getMaxHeight() {
        if (view_ != null) return view_.getMaxHeight(); else return super.getMaxHeight();
    }
}
