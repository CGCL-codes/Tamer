package org.kablink.teaming.servlet.administration;

import org.kablink.teaming.domain.Definition;
import org.kablink.util.Validator;

public class ExportDefinitionDownloadController extends ZipDownloadController {

    @Override
    protected String getFilename() {
        return "definitions.zip";
    }

    @Override
    protected NamedDocument getDocumentForId(String defId) {
        Definition def = getDefinitionModule().getDefinition(defId);
        String name = def.getName();
        if (Validator.isNull(name)) name = def.getTitle();
        return new NamedDocument(name, getDefinitionModule().getDefinitionAsXml(def));
    }
}
