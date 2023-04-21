package org.datanucleus.jdo.metadata;

import javax.jdo.annotations.ForeignKeyAction;
import javax.jdo.metadata.ColumnMetadata;
import javax.jdo.metadata.EmbeddedMetadata;
import javax.jdo.metadata.ForeignKeyMetadata;
import javax.jdo.metadata.IndexMetadata;
import javax.jdo.metadata.UniqueMetadata;
import javax.jdo.metadata.ValueMetadata;
import org.datanucleus.metadata.ColumnMetaData;
import org.datanucleus.metadata.EmbeddedMetaData;
import org.datanucleus.metadata.ForeignKeyMetaData;
import org.datanucleus.metadata.IndexMetaData;
import org.datanucleus.metadata.UniqueMetaData;
import org.datanucleus.metadata.ValueMetaData;

/**
 * Implementation of JDO ValueMetadata object.
 */
public class ValueMetadataImpl extends AbstractMetadataImpl implements ValueMetadata {

    public ValueMetadataImpl(ValueMetaData internal) {
        super(internal);
    }

    public ValueMetaData getInternal() {
        return (ValueMetaData) internalMD;
    }

    public String getColumn() {
        return getInternal().getColumnName();
    }

    public ColumnMetadata[] getColumns() {
        ColumnMetaData[] internalColmds = getInternal().getColumnMetaData();
        if (internalColmds == null) {
            return null;
        }
        ColumnMetadataImpl[] colmds = new ColumnMetadataImpl[internalColmds.length];
        for (int i = 0; i < colmds.length; i++) {
            colmds[i] = new ColumnMetadataImpl(internalColmds[i]);
            colmds[i].parent = this;
        }
        return colmds;
    }

    public ValueMetadata setColumn(String name) {
        getInternal().setColumnName(name);
        return this;
    }

    public ColumnMetadata newColumnMetadata() {
        ColumnMetaData internalColmd = getInternal().newColumnMetaData();
        ColumnMetadataImpl colmd = new ColumnMetadataImpl(internalColmd);
        colmd.parent = this;
        return colmd;
    }

    public ForeignKeyAction getDeleteAction() {
        org.datanucleus.metadata.ForeignKeyAction fk = getInternal().getDeleteAction();
        if (fk == org.datanucleus.metadata.ForeignKeyAction.CASCADE) {
            return ForeignKeyAction.CASCADE;
        } else if (fk == org.datanucleus.metadata.ForeignKeyAction.DEFAULT) {
            return ForeignKeyAction.DEFAULT;
        } else if (fk == org.datanucleus.metadata.ForeignKeyAction.NONE) {
            return ForeignKeyAction.NONE;
        } else if (fk == org.datanucleus.metadata.ForeignKeyAction.NULL) {
            return ForeignKeyAction.NULL;
        } else if (fk == org.datanucleus.metadata.ForeignKeyAction.RESTRICT) {
            return ForeignKeyAction.RESTRICT;
        }
        return ForeignKeyAction.UNSPECIFIED;
    }

    public ValueMetadata setDeleteAction(ForeignKeyAction fk) {
        if (fk == ForeignKeyAction.CASCADE) {
            getInternal().setDeleteAction(org.datanucleus.metadata.ForeignKeyAction.CASCADE);
        } else if (fk == ForeignKeyAction.DEFAULT) {
            getInternal().setDeleteAction(org.datanucleus.metadata.ForeignKeyAction.DEFAULT);
        } else if (fk == ForeignKeyAction.NONE) {
            getInternal().setDeleteAction(org.datanucleus.metadata.ForeignKeyAction.NONE);
        } else if (fk == ForeignKeyAction.NULL) {
            getInternal().setDeleteAction(org.datanucleus.metadata.ForeignKeyAction.NULL);
        } else if (fk == ForeignKeyAction.RESTRICT) {
            getInternal().setDeleteAction(org.datanucleus.metadata.ForeignKeyAction.RESTRICT);
        }
        return this;
    }

    public ForeignKeyAction getUpdateAction() {
        org.datanucleus.metadata.ForeignKeyAction fk = getInternal().getUpdateAction();
        if (fk == org.datanucleus.metadata.ForeignKeyAction.CASCADE) {
            return ForeignKeyAction.CASCADE;
        } else if (fk == org.datanucleus.metadata.ForeignKeyAction.DEFAULT) {
            return ForeignKeyAction.DEFAULT;
        } else if (fk == org.datanucleus.metadata.ForeignKeyAction.NONE) {
            return ForeignKeyAction.NONE;
        } else if (fk == org.datanucleus.metadata.ForeignKeyAction.NULL) {
            return ForeignKeyAction.NULL;
        } else if (fk == org.datanucleus.metadata.ForeignKeyAction.RESTRICT) {
            return ForeignKeyAction.RESTRICT;
        }
        return ForeignKeyAction.UNSPECIFIED;
    }

    public ValueMetadata setUpdateAction(ForeignKeyAction fk) {
        if (fk == ForeignKeyAction.CASCADE) {
            getInternal().setUpdateAction(org.datanucleus.metadata.ForeignKeyAction.CASCADE);
        } else if (fk == ForeignKeyAction.DEFAULT) {
            getInternal().setUpdateAction(org.datanucleus.metadata.ForeignKeyAction.DEFAULT);
        } else if (fk == ForeignKeyAction.NONE) {
            getInternal().setUpdateAction(org.datanucleus.metadata.ForeignKeyAction.NONE);
        } else if (fk == ForeignKeyAction.NULL) {
            getInternal().setUpdateAction(org.datanucleus.metadata.ForeignKeyAction.NULL);
        } else if (fk == ForeignKeyAction.RESTRICT) {
            getInternal().setUpdateAction(org.datanucleus.metadata.ForeignKeyAction.RESTRICT);
        }
        return this;
    }

    public ForeignKeyMetadata getForeignKeyMetadata() {
        ForeignKeyMetaData internalFkmd = getInternal().getForeignKeyMetaData();
        if (internalFkmd == null) {
            return null;
        }
        ForeignKeyMetadataImpl fkmd = new ForeignKeyMetadataImpl(internalFkmd);
        fkmd.parent = this;
        return fkmd;
    }

    public ForeignKeyMetadata newForeignKeyMetadata() {
        ForeignKeyMetaData internalFkmd = getInternal().newForeignKeyMetaData();
        ForeignKeyMetadataImpl fkmd = new ForeignKeyMetadataImpl(internalFkmd);
        fkmd.parent = this;
        return fkmd;
    }

    public UniqueMetadata getUniqueMetadata() {
        UniqueMetaData internalUnimd = getInternal().getUniqueMetaData();
        if (internalUnimd == null) {
            return null;
        }
        UniqueMetadataImpl unimd = new UniqueMetadataImpl(internalUnimd);
        unimd.parent = this;
        return unimd;
    }

    public UniqueMetadata newUniqueMetadata() {
        UniqueMetaData internalUnimd = getInternal().newUniqueMetaData();
        UniqueMetadataImpl unimd = new UniqueMetadataImpl(internalUnimd);
        unimd.parent = this;
        return unimd;
    }

    public IndexMetadata getIndexMetadata() {
        IndexMetaData internalIdxmd = getInternal().getIndexMetaData();
        if (internalIdxmd == null) {
            return null;
        }
        IndexMetadataImpl idxmd = new IndexMetadataImpl(internalIdxmd);
        idxmd.parent = this;
        return idxmd;
    }

    public IndexMetadata newIndexMetadata() {
        IndexMetaData internalIdxmd = getInternal().newIndexMetaData();
        IndexMetadataImpl idxmd = new IndexMetadataImpl(internalIdxmd);
        idxmd.parent = this;
        return idxmd;
    }

    public EmbeddedMetadata getEmbeddedMetadata() {
        EmbeddedMetaData internalEmbmd = getInternal().getEmbeddedMetaData();
        if (internalEmbmd == null) {
            return null;
        }
        EmbeddedMetadataImpl embmd = new EmbeddedMetadataImpl(internalEmbmd);
        embmd.parent = this;
        return embmd;
    }

    public EmbeddedMetadata newEmbeddedMetadata() {
        EmbeddedMetaData internalEmbmd = getInternal().newEmbeddedMetaData();
        EmbeddedMetadataImpl embmd = new EmbeddedMetadataImpl(internalEmbmd);
        embmd.parent = this;
        return embmd;
    }

    public String getTable() {
        return null;
    }

    public ValueMetadata setTable(String name) {
        return null;
    }
}
