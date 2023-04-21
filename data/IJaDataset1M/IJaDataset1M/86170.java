package org.wingx.beans.beaneditor;

import java.awt.event.*;
import java.beans.*;
import org.wings.*;
import org.wingx.beans.*;

/**
 * Text Field Editor Adapter
 *
 * @author Michael Reinsch, mreinsch@to.com
 * @version $Revision: 178 $
 */
public class STextFieldEditorAdapter extends STextComponentEditorAdapter implements BeanEditorConstants {

    public STextFieldEditorAdapter() {
        textComponent = new STextField();
        textComponent.addTextListener(this);
        setComponent(textComponent);
    }

    public void init(PropertyDescriptor descriptor) {
        STextField field = (STextField) textComponent;
        Integer val;
        if ((val = (Integer) descriptor.getValue(COLUMNS)) != null) field.setColumns(val.intValue());
        if ((val = (Integer) descriptor.getValue(LENGTH)) != null) field.setMaxColumns(val.intValue());
    }
}
