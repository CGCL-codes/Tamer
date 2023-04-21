package pl.psnc.dl.ege.types;

import java.util.List;

/**
 * Class describes converting path object - 
 * a sequence of converter actions.
 * <br/>
 * Each instance of this class serves as input for chained conversions
 * executed through EGE implementation.
 * 
 * @author mariuszs
 */
public class ConversionsPath {

    private final List<ConversionAction> path;

    /**
	 * Constructor. 
	 * Assigned path value should`t be 'null'.
	 * 
	 * @param path list of conversion actions.
	 */
    public ConversionsPath(List<ConversionAction> path) {
        if (path == null) throw new IllegalArgumentException();
        this.path = path;
    }

    /**
	 * Returns list of <code>ConverterAction</code>.
	 * 
	 * @return Returns list of <code>ConverterAction</code>
	 */
    public List<ConversionAction> getPath() {
        return this.path;
    }

    /**
	 * Method returns input data type of
	 * this sequence of conversions.
	 * 
	 * @return input data type
	 */
    public DataType getInputDataType() {
        return (DataType) path.get(0).getConversionInputType();
    }

    /**
	 * Method returns output data type of 
	 * this sequence of conversions.
	 * 
	 * @return output data type
	 */
    public DataType getOutputDataType() {
        return (DataType) path.get(path.size() - 1).getConversionOutputType();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof ConversionsPath)) {
            return false;
        }
        ConversionsPath p = (ConversionsPath) o;
        if (p.getPath() == null || this.getPath() == null) {
            return false;
        }
        int size = p.getPath().size();
        if (size != this.getPath().size()) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            ConversionAction ca = p.getPath().get(i);
            ConversionAction ca2 = this.getPath().get(i);
            if (ca != null && !ca.equals(ca2)) {
                return false;
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hashCode = 7;
        if (path != null) {
            for (ConversionAction ca : path) {
                hashCode = hashCode + (ca == null ? 0 : ca.hashCode());
            }
        }
        return hashCode;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int size = path.size();
        for (int i = 0; i < size; i++) {
            sb.append(path.get(i));
            if (i < (size - 1)) {
                sb.append("->");
            }
        }
        return sb.toString();
    }
}
