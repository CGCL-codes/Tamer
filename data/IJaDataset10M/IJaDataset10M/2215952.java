package icebird.compiler.ncomp.vs;

/**
 * @author Sergey Shulepoff[Knott]
 */
public final class StackLocation extends Location {

    /**
	 * @param name
	 */
    public StackLocation() {
        super("Stack");
    }

    public int getWeight() {
        return 5;
    }
}
