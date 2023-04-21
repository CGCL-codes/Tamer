package at.ssw.coco.lib.model.atgAst;

/**
 * Data Structure for Regions in a string consisting of offset and length;
 * 
 * @author Martin Preinfalk <martin.preinfalk@students.jku.at>
 */
public class AtgAstRegion {

    /**
	 * offset of a node in ATG-File
	 */
    private int offset;

    /**
	 * length of a node in ATG-File 
	 */
    private int length;

    /**
	 * Default Constructor
	 * 
	 * offset and length initalized with -1
	 */
    public AtgAstRegion() {
        super();
        this.offset = -1;
        this.length = -1;
    }

    /**
	 * Constructor 
	 * 
	 * @param offset of a node in ATG-File
	 * @param length of a node in ATG-File
	 */
    public AtgAstRegion(int offset, int length) {
        super();
        this.offset = offset;
        this.length = length;
    }

    /**
	 * Getter - offset of a node in ATG-File
	 * @return
	 */
    public int getOffset() {
        return offset;
    }

    /**
	 * Setter - offset of a node in ATG-File
	 * @param offset
	 */
    public void setOffset(int offset) {
        this.offset = offset;
    }

    /**
	 * Getter - length of a node in ATG-File
	 * @return
	 */
    public int getLength() {
        return length;
    }

    /**
	 * Setter - length of a node in ATG-File
	 * @param length
	 */
    public void setLength(int length) {
        this.length = length;
    }

    /**
	 * sets length of node to end - offset, 
	 * be careful to set offset to the right value first!
	 * @param end
	 */
    public void setEnd(int end) {
        length = end - offset;
    }

    /**
	 * @return string representation of Region
	 */
    public String toString() {
        return "[offset: " + offset + ", length: " + length + "]";
    }
}
