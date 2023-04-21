package org.datanucleus.api.jdo.metadata;

import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.metadata.DiscriminatorMetadata;
import javax.jdo.metadata.InheritanceMetadata;
import javax.jdo.metadata.JoinMetadata;
import org.datanucleus.metadata.DiscriminatorMetaData;
import org.datanucleus.metadata.InheritanceMetaData;
import org.datanucleus.metadata.JoinMetaData;

/**
 * Implementation of JDO InheritanceMetadata object.
 */
public class InheritanceMetadataImpl extends AbstractMetadataImpl implements InheritanceMetadata {

    public InheritanceMetadataImpl(InheritanceMetaData internal) {
        super(internal);
    }

    public InheritanceMetaData getInternal() {
        return (InheritanceMetaData) internalMD;
    }

    public String getCustomStrategy() {
        org.datanucleus.metadata.InheritanceStrategy str = getInternal().getStrategy();
        if (str != org.datanucleus.metadata.InheritanceStrategy.NEW_TABLE && str != org.datanucleus.metadata.InheritanceStrategy.SUBCLASS_TABLE && str != org.datanucleus.metadata.InheritanceStrategy.SUPERCLASS_TABLE && str != null) {
            return str.toString();
        }
        return null;
    }

    public DiscriminatorMetadata getDiscriminatorMetadata() {
        DiscriminatorMetaData internalDismd = getInternal().getDiscriminatorMetaData();
        if (internalDismd == null) {
            return null;
        }
        DiscriminatorMetadataImpl dismd = new DiscriminatorMetadataImpl(internalDismd);
        dismd.parent = this;
        return dismd;
    }

    public JoinMetadata getJoinMetadata() {
        JoinMetaData internalJoinmd = getInternal().getJoinMetaData();
        if (internalJoinmd == null) {
            return null;
        }
        JoinMetadataImpl joinmd = new JoinMetadataImpl(internalJoinmd);
        joinmd.parent = this;
        return joinmd;
    }

    public InheritanceStrategy getStrategy() {
        org.datanucleus.metadata.InheritanceStrategy str = getInternal().getStrategy();
        if (str == org.datanucleus.metadata.InheritanceStrategy.NEW_TABLE) {
            return InheritanceStrategy.NEW_TABLE;
        } else if (str == org.datanucleus.metadata.InheritanceStrategy.SUBCLASS_TABLE) {
            return InheritanceStrategy.SUBCLASS_TABLE;
        } else if (str == org.datanucleus.metadata.InheritanceStrategy.SUPERCLASS_TABLE) {
            return InheritanceStrategy.SUPERCLASS_TABLE;
        }
        return InheritanceStrategy.UNSPECIFIED;
    }

    public DiscriminatorMetadata newDiscriminatorMetadata() {
        DiscriminatorMetaData internalDismd = getInternal().newDiscriminatorMetadata();
        DiscriminatorMetadataImpl dismd = new DiscriminatorMetadataImpl(internalDismd);
        dismd.parent = this;
        return dismd;
    }

    public JoinMetadata newJoinMetadata() {
        JoinMetaData internalJoinmd = getInternal().newJoinMetadata();
        JoinMetadataImpl joinmd = new JoinMetadataImpl(internalJoinmd);
        joinmd.parent = this;
        return joinmd;
    }

    public InheritanceMetadata setCustomStrategy(String str) {
        getInternal().setStrategy(str);
        return this;
    }

    public InheritanceMetadata setStrategy(InheritanceStrategy str) {
        if (str == InheritanceStrategy.NEW_TABLE) {
            getInternal().setStrategy(org.datanucleus.metadata.InheritanceStrategy.NEW_TABLE);
        } else if (str == InheritanceStrategy.SUBCLASS_TABLE) {
            getInternal().setStrategy(org.datanucleus.metadata.InheritanceStrategy.SUBCLASS_TABLE);
        } else if (str == InheritanceStrategy.SUPERCLASS_TABLE) {
            getInternal().setStrategy(org.datanucleus.metadata.InheritanceStrategy.SUPERCLASS_TABLE);
        }
        return this;
    }
}
