package org.asoft.magnus.spring;

import java.beans.PropertyEditorSupport;
import org.asoft.sapiente.impl.SapienteResource;

/**
 * Jan 13, 2009
 * 
 * @author Alex
 */
public class SapienteResourcePropertyEditor extends PropertyEditorSupport {

    public void setAsText(String text) {
        SapienteResource res = new SapienteResource(text);
        setValue(res);
    }
}
