package icreate.mans;

public class TypeFiller extends DefaultFiller {

    protected Class cl;

    /** Creates a new instance of TypeFiller */
    public TypeFiller(Class cl) {
        this.cl = cl;
    }

    /** returns whether component is OK to be added
     * @return true
     */
    public boolean isOk() {
        return (cl.isAssignableFrom(this.dobj.getClass()));
    }

    /** returns whether to fill a range
     * @return true
     */
    public boolean hasRange() {
        return false;
    }
}
