package shiva.domain.exception.validation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Paulo Vitor
 * @author Roberto Su
 * 
 * @description
 *
 */
public class ValidationException extends Exception {

    private List<InvalidAttributeValueException> attributesException;

    /**
	 * 
	 */
    private static final long serialVersionUID = -4946213609638635327L;

    public ValidationException() {
        super();
        attributesException = new ArrayList<InvalidAttributeValueException>();
    }

    public ValidationException(String mensagem) {
        super(mensagem);
        attributesException = new ArrayList<InvalidAttributeValueException>();
    }

    /**
	 * 
	 * @param ex
	 */
    public void addAttributeException(InvalidAttributeValueException ex) {
        this.attributesException.add(ex);
    }

    /**
	 * 
	 * @return
	 */
    public InvalidAttributeValueException getNextAttributeException() {
        if (this.attributesException.size() == 0) {
            return null;
        }
        return this.attributesException.get(0);
    }

    /**
	 * 
	 * @return
	 */
    public List<InvalidAttributeValueException> getAttributesException() {
        return this.attributesException;
    }
}
