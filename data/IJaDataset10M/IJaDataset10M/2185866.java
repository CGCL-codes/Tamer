package testPackage1;

/** Not a Javadoc-fixme. */
public class Sample1 {

    private int x;

    /** FIXME: Javadoc-fixme. */
    public void x() {
        return x;
    }

    /** Javadoc not added to the FIXME because it is multiline 
  */
    public void y() {
    }

    /** Javadoc IS added to the FIXME because it is single line */
    public void z() {
    }

    private char c;

    private char d;
}
