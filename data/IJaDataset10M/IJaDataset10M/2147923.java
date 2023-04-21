package units;

import com.dustedpixels.ooops.Inline;

/**
 * @author micapolos@gmail.com (Michal Pociecha-Los)
 */
public final class Nand {

    public interface Context {

        int getInput1();

        int getInput2();

        void setOutput(int value);
    }

    @Inline
    private final And and;

    @Inline
    private final Not not;

    private int andOut;

    public Nand(final Context context) {
        And.Context andContext = new And.Context() {

            public int getInput1() {
                return context.getInput1();
            }

            public int getInput2() {
                return context.getInput2();
            }

            public void setOutput(int value) {
                andOut = value;
            }
        };
        Not.Context notContext = new Not.Context() {

            public int getInput() {
                return andOut;
            }

            public void setOutput(int value) {
                context.setOutput(value);
            }
        };
        this.and = new And(andContext);
        this.not = new Not(notContext);
    }

    public void update() {
        and.update();
        not.update();
    }
}
