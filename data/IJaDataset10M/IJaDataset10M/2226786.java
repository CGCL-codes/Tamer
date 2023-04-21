package polr.client.ui.base;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.gui.GUIContext;
import polr.client.ui.base.Label;

public class ProgressBar extends Label {

    private int value;

    private int minVal;

    private int maxVal;

    public ProgressBar(int min, int max) {
        super();
        value = maxVal;
        minVal = min;
        maxVal = max;
        setOpaque(true);
    }

    public void setString(String string) {
        setText(string);
    }

    public String getString() {
        return getText();
    }

    public double getPercentComplete() {
        double percent = (getValue() - getMinimum()) / (getMaximum() - getMinimum());
        return percent;
    }

    public int getMinimum() {
        return minVal;
    }

    public void setMinimum(int minVal) {
        this.minVal = minVal;
    }

    public int getMaximum() {
        return maxVal;
    }

    public void setMaximum(int maxVal) {
        this.maxVal = maxVal;
    }

    public void setValue(int curVal) {
        value = curVal;
    }

    public int getValue() {
        return value;
    }

    @Override
    public void render(GUIContext ctx, Graphics g) {
        super.render(ctx, g);
        g.setColor(getForeground());
        int barWidth = (int) (value * (getWidth() / (maxVal - minVal)));
        g.fillRect(getAbsoluteX(), getAbsoluteY(), (barWidth > getWidth() ? getWidth() : barWidth), getHeight() - 1);
        if (getText() != null && !getText().equals("")) g.drawString(getText(), getAbsoluteX() + ((getWidth() / 2) - (getFont().getWidth(getText()) / 2)), getAbsoluteY() + ((getHeight() / 2) - (getFont().getHeight(getText()) / 2)));
    }
}
