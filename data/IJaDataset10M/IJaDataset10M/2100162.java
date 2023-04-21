package jswat.testa;

/**
 * Test code for the LineBreakpointTest.
 *
 * @author  Nathan Fiedler
 */
public class SourceNameTestCode {

    public static void packageA() {
        System.out.println();
    }

    public static void main(String[] args) {
        packageA();
        jswat.test.SourceNameTestCode.packageB();
    }
}
