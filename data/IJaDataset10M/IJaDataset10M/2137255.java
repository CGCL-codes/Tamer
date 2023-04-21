package com.netflexitysolutions.amazonws.sdb.orm.metadata;

/**
 * @author netflexity
 *
 * Contains all the objects and attributes that belong to 
 * &lt;set&gt; or &lt;bag&gt; with one-to-many XML element.
 */
public class ManyToManyMetadata extends CollectionMetadata {

    /**
	 * 
	 */
    public ManyToManyMetadata() {
        setLazy(true);
    }

    @Override
    public <T> String toSimpleDB(T record) {
        ItemMetadata manyToManyItemMetadata = getItemMetadata().getMapping().getItemMetadataByClass(getJavaClass());
        assert (manyToManyItemMetadata != null);
        IdMetadata primaryKeyMetadata = manyToManyItemMetadata.getId();
        assert (primaryKeyMetadata != null);
        return primaryKeyMetadata.toSimpleDB(record);
    }
}
