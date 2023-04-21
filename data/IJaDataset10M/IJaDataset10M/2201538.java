package pcgen.gui.generator;

/**
 *
 * @author Connor Petty <cpmeister@users.sourceforge.net>
 */
public interface MutableRollMethod extends RollMethod, Mutable {

    public void setAssignable(boolean assign);

    public void setDiceExpression(int index, String expression);
}
