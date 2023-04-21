package propasm.p32;

/**
 * NEG - 29
 * D <- -S
 * 
 * @author cbiffle
 *
 */
public class NegOp extends AbstractBinaryOp {

    @Override
    public int getOpcode() {
        return 0x29;
    }
}
