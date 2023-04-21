package net.sf.dynxform.form;

import net.sf.dynxform.container.DefinitionProvider;
import net.sf.dynxform.form.schema.Form;
import org.exolab.castor.xml.Unmarshaller;
import java.io.BufferedReader;
import java.io.FileReader;

/**
 * net.sf.dynxform.form Mar 24, 2004 6:01:39 PM andreyp
 * Copyright (c) net.sf.dynxform All Rights Reserved
 * 
 * @author andreyp
 */
public final class FileFormCreator implements FormCreator {

    private static final DefinitionProvider definitionProvider = DefinitionProvider.getInstance();

    public final Form createSingleForm(final String formId) throws Exception {
        final Form definition = (Form) Unmarshaller.unmarshal(Form.class, new BufferedReader(new FileReader(definitionProvider.getFormUrl(formId).getFile())));
        return definition;
    }
}
