package system.cpu;

/**
 * 
 * 
 */
public abstract class IFormat extends Instruction {

    /** CPU register usead as operand */
    protected int rs;

    /** CPU register that receives the result */
    protected int rd;

    /** */
    protected int immediate;

    public int getImmediate() {
        return this.immediate;
    }

    public void setImmediate(int immediate) {
        this.immediate = immediate;
    }

    public int getRs() {
        return this.rs;
    }

    public void setRs(int rs) {
        this.rs = rs;
    }

    public int getRd() {
        return this.rd;
    }

    public void setRd(int rd) {
        this.rd = rd;
    }
}
