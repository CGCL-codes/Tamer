package org.datanucleus.store.odf.fieldmanager;

import org.datanucleus.ClassLoaderResolver;
import org.datanucleus.metadata.AbstractClassMetaData;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.metadata.EmbeddedMetaData;
import org.datanucleus.metadata.Relation;
import org.datanucleus.store.ExecutionContext;
import org.datanucleus.store.ObjectProvider;
import org.datanucleus.store.odf.ODFUtils;
import org.odftoolkit.odfdom.doc.table.OdfTableRow;

/**
 * FieldManager to handle the store information for an embedded persistable object into ODF.
 */
public class StoreEmbeddedFieldManager extends StoreFieldManager {

    AbstractMemberMetaData embeddedMetaData;

    public StoreEmbeddedFieldManager(ObjectProvider sm, OdfTableRow row, AbstractMemberMetaData mmd, boolean insert) {
        super(sm, row, insert);
        this.embeddedMetaData = mmd;
    }

    protected int getColumnIndexForMember(int memberNumber) {
        return ODFUtils.getColumnPositionForFieldOfEmbeddedClass(memberNumber, embeddedMetaData);
    }

    public void storeObjectField(int fieldNumber, Object value) {
        ExecutionContext ec = op.getExecutionContext();
        ClassLoaderResolver clr = ec.getClassLoaderResolver();
        EmbeddedMetaData emd = embeddedMetaData.getEmbeddedMetaData();
        AbstractMemberMetaData[] emb_mmd = emd.getMemberMetaData();
        AbstractMemberMetaData mmd = emb_mmd[fieldNumber];
        int relationType = mmd.getRelationType(clr);
        if (Relation.isRelationSingleValued(relationType) && mmd.isEmbedded()) {
            Class embcls = mmd.getType();
            AbstractClassMetaData embcmd = ec.getMetaDataManager().getMetaDataForClass(embcls, clr);
            if (embcmd != null) {
                ObjectProvider embSM = null;
                if (value != null) {
                    embSM = ec.findObjectProviderForEmbedded(value, op, mmd);
                } else {
                    embSM = ec.newObjectProviderForEmbedded(mmd, embcmd, op, fieldNumber);
                }
                embSM.provideFields(embcmd.getAllMemberPositions(), new StoreEmbeddedFieldManager(embSM, row, mmd, insert));
                return;
            }
        }
        storeObjectFieldInCell(fieldNumber, value, mmd, clr);
    }
}
