package rs.mgifos.mosquito.impl.pdm;

/**
 *
 * @author <a href="mailto:nikola.petkov@gmail.com">Nikola Petkov &lt;nikola.petkov@gmail.com&gt;</a>
 */
public class PrecType {

    private String jClass;

    private int length = 0;

    /**
     *  
     */
    public PrecType() {
    }

    /**
     * @param aLength
     * @param aJClass
     */
    public PrecType(int aLength, String aJClass) {
        length = aLength;
        jClass = aJClass;
    }

    /**
     * @return type.
     */
    public String getJClass() {
        return jClass;
    }

    /**
     * @return len.
     */
    public int getLength() {
        return length;
    }

    /**
     * @param aType
     *            The type to set.
     */
    public void setJClass(String aType) {
        jClass = aType;
    }

    /**
     * @param aLength
     *            The length to set.
     */
    public void setLength(int aLength) {
        length = aLength;
    }
}
