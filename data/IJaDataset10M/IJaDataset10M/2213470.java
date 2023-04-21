package ar.com.oddie.core.entities.mappers;

import ar.com.oddie.core.entities.Document;
import ar.com.oddie.persistence.metadata.DataMapper;

public class DocumentDataMapper extends DataMapper {

    public DocumentDataMapper() {
        super(Document.class);
    }

    @Override
    protected void configureMappedAttributes() {
        this.addMappedKeyAttribute("id", Long.class);
        this.addMappedAttribute("path", String.class);
        this.addMappedAttribute("title", String.class);
        this.addMappedAttribute("body", String.class);
    }
}
