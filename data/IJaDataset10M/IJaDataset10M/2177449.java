package com.germinus.xpression.content_editor.model;

import com.germinus.xpression.cms.contents.ContentTypeDefinitions;
import com.germinus.xpression.cms.model.ContentTypes;

/**
 * Here are the common methods to manage the editor.
 *
 * @author Diego Palmeira
 * @version $Revision$
 */
public class EditorManager {

    public static String obtainCreationFieldSet(Long typeId) {
        ContentTypeDefinitions defs = ContentTypes.getDefinitions();
        String creationFieldSetId = defs.getContentTypeDefinition(typeId).getCreationFieldSet();
        return creationFieldSetId;
    }
}
