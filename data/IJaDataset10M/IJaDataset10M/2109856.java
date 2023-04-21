package com.inspiresoftware.lib.dto.geda.assembler.meta;

/**
 * Metadata specific to field pipes.
 * 
 * @author DPavlov
 */
public interface FieldPipeMetadata extends PipeMetadata {

    /**
	 * @return converter key to use.
	 */
    String getConverterKey();

    /**
	 * @return true if field has {@link com.inspiresoftware.lib.dto.geda.annotations.DtoParent} annotation
	 */
    boolean isChild();

    /**
	 * @return specified for child=true to use as PK for parent
	 */
    String getParentEntityPrimaryKeyField();

    /**
	 * @return key for entity retriever.
	 */
    String getEntityRetrieverKey();
}
