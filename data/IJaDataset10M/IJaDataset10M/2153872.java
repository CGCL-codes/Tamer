package net.sourceforge.chaperon.process.simple.internal;

/**
 * @author <a href="mailto:stephan@apache.org">Stephan Michels</a>
 * @version CVS $Id: GotoAction.java,v 1.1 2005/01/07 16:12:48 benedikta Exp $
 */
public class GotoAction {

    public final String symbol;

    public final boolean kernel;

    public final State state;

    public final GotoAction next;

    public GotoAction(String symbol, boolean kernel, State state, GotoAction next) {
        if (symbol == null) throw new IllegalArgumentException("Symbol is null");
        if (state == null) throw new IllegalArgumentException("State is null");
        this.symbol = symbol;
        this.kernel = kernel;
        this.state = state;
        this.next = next;
    }

    public boolean equals(Object o) {
        if (o instanceof GotoAction) {
            GotoAction gotoAction = (GotoAction) o;
            return (symbol.equals(gotoAction.symbol)) && (kernel == gotoAction.kernel) && (state == gotoAction.state);
        }
        return false;
    }
}
