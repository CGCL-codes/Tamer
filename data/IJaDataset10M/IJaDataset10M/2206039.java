package openrpg2.common.dice;

/**
 * Creates an add expression, which is either one expression plus
 * another, or else one integer expression minus another
 * @author markt
 */
public class AddExpr extends BinaryExpr {

    private DiceExpr x, y;

    private char join;

    /**
     * Creates a new instance of AddExpr
     * @param a The left hand parameter
     * @param b The right hand parameter
     * @param j The type of operation ('+' or '-')
     */
    public AddExpr(DiceExpr a, DiceExpr b, char j) {
        super(a, b, j);
        x = a;
        y = b;
        join = j;
        if (j != '+' && j != '-') throw new IllegalArgumentException("Add Expression must be '+' or '-'");
        if (j == '-' && !(x.isInt() && y.isInt())) throw new IllegalArgumentException("Cannot subtract non integer expressions");
    }

    /**
     * Rolls any dice found in the expression
     */
    public void roll() {
        super.roll();
        if (join == '+') {
            if (isInt()) value = new IntValue(x.getIntValue() + y.getIntValue()); else value = new StrValue(x.getStrValue() + y.getStrValue());
        } else value = new IntValue(x.getIntValue() - y.getIntValue());
    }
}
