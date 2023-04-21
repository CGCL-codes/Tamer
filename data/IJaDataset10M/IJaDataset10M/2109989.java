package jxl.write.biff;

/**
 * Exception thrown when attempting to copy a workbook which contains 
 * additional property sets
 */
public class CopyAdditionalPropertySetsException extends JxlWriteException {

    /**
   * Constructor
   */
    public CopyAdditionalPropertySetsException() {
        super(copyPropertySets);
    }
}
