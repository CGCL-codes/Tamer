package de.uka.ilkd.pp;

/** Thrown by many methods of {@link Layouter} to indicate illegal
 * usage.  This exception indicates that the sequence of
 * method calls was illegal, i.e.  more blocks were ended than begun,
 * the Layouter is closed before all blocks are ended, a break occurs
 * outside of any block, etc.
 *
 * <p>This is a RuntimeException, and if it is ever thrown, it means
 * that there is a mistake in the program using the {@code Layouter} 
 * class.
 */
public class UnbalancedBlocksException extends IllegalStateException {

    private static final long serialVersionUID = 5086204740022528272L;

    public UnbalancedBlocksException() {
        super();
    }

    public UnbalancedBlocksException(String s) {
        super(s);
    }
}
