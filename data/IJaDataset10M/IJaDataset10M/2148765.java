package propasm.p32;

/**
 * DJNZ - 39
 * Decrement D.
 * If D is not zero, jump to S.
 * 
 * @author cbiffle
 *
 */
public class DjnzOp extends AbstractBinaryOp {

    @Override
    public int getOpcode() {
        return 0x39;
    }
}
