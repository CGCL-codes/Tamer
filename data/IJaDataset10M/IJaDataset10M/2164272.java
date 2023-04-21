package deduced.validator;

/**
 * StringValidator
 * 
 * @author Duff
 */
public class StringValidator extends AbstractValidator {

    /**
     * 
     */
    private static final long serialVersionUID = -3871525550636650169L;

    /** _minimumLenght */
    private final int _minimumLenght;

    /** _maximumLenght */
    private final int _maximumLenght;

    /** _isMultiLine */
    private final boolean _isMultiLine;

    /**
     * constructor for StringValidator
     * 
     * @param minimumLenght
     * @param maximumLenght
     * @param isMultiLine
     */
    public StringValidator(int minimumLenght, int maximumLenght, boolean isMultiLine) {
        _minimumLenght = minimumLenght;
        _maximumLenght = maximumLenght;
        _isMultiLine = isMultiLine;
    }

    /**
     * (non-Javadoc)
     * 
     * @see deduced.validator.Validator#getErrorString(java.lang.Object)
     */
    public String getErrorString(Object value) {
        String retVal = null;
        String string = (String) value;
        int length = string.length();
        if (length < _minimumLenght) {
            retVal = "String Lenght " + length + " is smaller than " + _minimumLenght;
        } else if (length > _maximumLenght) {
            retVal = "String Lenght " + length + " is bigger than " + _maximumLenght;
        } else if (!_isMultiLine && string.indexOf('\n') != -1) {
            retVal = "String more than one line";
        }
        return retVal;
    }

    /**
     * @return Returns the isMultiLine.
     */
    public boolean isMultiLine() {
        return _isMultiLine;
    }

    /**
     * @return Returns the maximumLenght.
     */
    public int getMaximumLenght() {
        return _maximumLenght;
    }

    /**
     * @return Returns the minimumLenght.
     */
    public int getMinimumLenght() {
        return _minimumLenght;
    }

    /**
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        boolean firstAdded = false;
        String retVal = "";
        if (_minimumLenght != 0) {
            if (firstAdded) {
                retVal += " and ";
            }
            retVal += "the string lenght must be bigger or equal than " + _minimumLenght;
            firstAdded = true;
        }
        if (_maximumLenght != Integer.MAX_VALUE) {
            if (firstAdded) {
                retVal += " and ";
            }
            retVal += "the string lenght must be smaller or equal than " + _maximumLenght;
            firstAdded = true;
        }
        if (isMultiLine()) {
            if (firstAdded) {
                retVal += " and ";
            }
            retVal += "the string must contain only one line";
            firstAdded = true;
        }
        return retVal;
    }
}
