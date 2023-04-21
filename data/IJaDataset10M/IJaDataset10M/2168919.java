package pkg;

/**
 * Test var args.
 */
public class VarArgs {

    /**
     * @param i a param with var args.
     */
    public void methodWithVarArgs(int... i) {
    }

    /**
     * @param i a regular parameter.
     * @param t a param with var args.
     */
    public void methodWithParamAndVarArgs(int i, TypeParameters... t) {
    }

    /**
     * @param i a param with var args.
     */
    public void singleArrayVarArg(int[]... i) {
    }

    /**
     * @param i a param with var args.
     */
    public void doubleArrayVarArgs(int[][]... i) {
    }
}
