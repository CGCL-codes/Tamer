package de.jformular.interfaces;

import de.jformular.FormularError;
import de.jformular.FormularField;
import de.jformular.xmlbinding.formularconfiguration.ResourceTextType;
import java.util.List;

/**
 * Interface declaration
 * @author Frank Dolibois, fdolibois@itzone.de, http://www.itzone.de
 * @version $Id: FormularErrors.java,v 1.11 2002/10/14 14:02:27 fdolibois Exp $
 */
public interface FormularErrors {

    static final String COMMON_ERROR = "COMMONERROR";

    /**
     */
    void addErrors(FormularErrors errors);

    /**
     */
    void addErrors(List errors);

    /**
     */
    void addError(FormularError error);

    /**
     * add field error String
     */
    void addError(String key, String error);

    /**
     * add field error ErrorMessage
     */
    void addError(String key, ResourceTextType error);

    /**
     * add field error String
     */
    void addError(String key, String error, FormularField field);

    /**
     * add field error ErrorMessage
     */
    void addError(String key, ResourceTextType error, FormularField field);

    /**
      * add common error
      */
    void addError(String error);

    /**
     * add common error
     */
    void addError(ResourceTextType error);

    /**
     */
    void removeError(FormularError error);

    /**
     */
    void removeErrors(String key);

    /**
     */
    void removeErrors(FormularField field);

    /**
     */
    boolean hasErrors();

    /**
     */
    boolean hasErrors(String key);

    /**
     */
    boolean hasErrors(FormularField field);

    /**
     */
    List getErrors(String key);

    /**
     */
    List getErrors(FormularField field);

    /**
     */
    List getErrors();
}
