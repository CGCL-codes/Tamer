package it.diamonds;

import it.diamonds.engine.AbstractEngine;
import it.diamonds.engine.Point;
import it.diamonds.engine.Rectangle;
import it.diamonds.engine.video.Drawable;
import it.diamonds.engine.video.Sprite;
import it.diamonds.engine.video.Texture;
import java.util.Vector;

public final class Number implements Drawable {

    public static final int DIGIT_WIDTH = 16;

    public static final int DIGIT_HEIGHT = 24;

    private Texture texture;

    private Vector<Sprite> digits;

    private int value = 0;

    private Point origin;

    private Number(String textureName, Point origin) {
        texture = new Texture(textureName);
        digits = new Vector<Sprite>();
        for (int i = 0; i < 10; i++) {
            int x = (i % 5) * DIGIT_WIDTH;
            int y = (i / 5) * DIGIT_HEIGHT;
            digits.add(new Sprite(0, 0, new Rectangle(x, y, x + DIGIT_WIDTH, y + DIGIT_HEIGHT), texture));
        }
        setOrigin(origin);
    }

    public static Number create16x24(float x, float y) {
        return new Number("gfx/common/score_16x24", new Point(x, y));
    }

    public Sprite getDigit(int index) {
        return digits.get(index);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Point getOrigin() {
        return origin;
    }

    private void setOrigin(Point point) {
        origin = point;
    }

    public Point getPointFromPosition(int position) {
        return new Point(origin.getX() + position * DIGIT_WIDTH, origin.getY());
    }

    public void draw(AbstractEngine engine) {
        boolean leadingZeroes = true;
        for (int position = 0; position < 8; ++position) {
            int curDigit = Digit.extract(value, 8 - position);
            if (0 == curDigit && leadingZeroes) {
                continue;
            }
            leadingZeroes = false;
            float x = origin.getX() + position * DIGIT_WIDTH;
            float y = origin.getY();
            drawDigit(engine, curDigit, x, y);
        }
    }

    private void drawDigit(AbstractEngine engine, int digit, float x, float y) {
        Sprite digitSprite = digits.get(digit);
        digitSprite.setPos(x, y);
        digitSprite.draw(engine);
    }
}
