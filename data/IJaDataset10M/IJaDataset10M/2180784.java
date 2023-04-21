package jp.seraph.jsade.core;

/**
 * TODO このクラスの値、実は不安なので、ちゃんと確かめること
 */
public class DefaultFieldContext implements FieldContext {

    /**
     * @see jp.seraph.jsade.core.FieldContext#getGoalWidth()
     */
    public double getGoalWidth() {
        return 8;
    }

    /**
     * @see jp.seraph.jsade.core.FieldContext#getLength()
     */
    public double getLength() {
        return 12;
    }

    /**
     * @see jp.seraph.jsade.core.FieldContext#getWidth()
     */
    public double getWidth() {
        return 2;
    }
}
