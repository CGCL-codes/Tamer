package com.netx.data;

import java.util.List;
import com.netx.generics.collections.ImmutableList;

public final class RelationMetaData extends MetaData {

    private final EntityMetaData _holder;

    private final EntityMetaData _related;

    RelationMetaData(String name, EntityMetaData holder, EntityMetaData related, List<String[]> constraints, Database d) {
        super(name, constraints, d);
        _holder = holder;
        _related = related;
    }

    public EntityMetaData getHolderEntity() {
        return _holder;
    }

    public EntityMetaData getRelatedEntity() {
        return _related;
    }

    ImmutableList<DataSink> getReplicationSinks() {
        return _holder.getReplicationSinks();
    }
}
