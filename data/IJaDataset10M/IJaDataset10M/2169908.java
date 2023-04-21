package org.atlantal.impl.cms.content;

import org.atlantal.api.cms.content.Content;
import org.atlantal.api.cms.content.FormContent;
import org.atlantal.api.cms.id.FormContentId;

/**
 * <p>Titre : Atlantal Framework</p>
 * <p>Description : </p>
 * <p>Copyright : Copyright (c) 2001-2002</p>
 * <p>Soci�t� : Mably Multim�dia</p>
 * @author Fran�ois MASUREL
 * @version 1.0
 */
public class FormContentInstance extends AggregationBufferedContentInstance implements FormContent {

    /**
     * Constructor
     */
    public FormContentInstance() {
    }

    /**
     * @return new content instance
     */
    public Content newInstance() {
        return new FormContentInstance();
    }

    /**
     * @return Form content id
     */
    public FormContentId getContentId() {
        return (FormContentId) getId();
    }
}
