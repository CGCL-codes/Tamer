package mnemosyne.core.testObjects;

/**
 * Demos a known problem with clone method overrides.
 *
 * @version $Id: NaughtyCloneObject.java,v 1.1.1.1 2004/08/07 06:42:04 charlesblaxland Exp $
 */
public class NaughtyCloneObject {

    public TestObj2 obj = new TestObj2();

    public Object clone() throws CloneNotSupportedException {
        NaughtyCloneObject clone = (NaughtyCloneObject) super.clone();
        clone.obj = new TestObj2();
        return clone;
    }
}
