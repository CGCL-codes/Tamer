package org.iptc.nar.core.model.conceptitem;

import org.iptc.nar.core.entity.ConceptType;
import org.iptc.nar.core.model.AnyItemType;
import org.iptc.nar.core.model.ContentMetadataType;

public class ConceptItem extends AnyItemType {

    private ContentMetadataType m_contentMeta;

    private ConceptType m_concept;

    public ConceptType getConcept() {
        return m_concept;
    }

    public void setConcept(ConceptType concept) {
        this.m_concept = concept;
    }

    @Override
    public ContentMetadataType getContentMeta() {
        return m_contentMeta;
    }

    public void setContentMeta(ContentMetadataType contentMetadata) {
        this.m_contentMeta = contentMetadata;
    }
}
