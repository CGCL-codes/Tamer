package com.kni.etl.ketl.transformation;

import org.w3c.dom.Node;
import com.kni.etl.ketl.exceptions.KETLThreadException;
import com.kni.etl.ketl.smp.ETLThreadManager;

/**
 * The Class DynamicTransformation.
 */
public final class DynamicTransformation extends ETLTransformation {

    /**
	 * Instantiates a new dynamic transformation.
	 * 
	 * @param pXMLConfig
	 *            the XML config
	 * @param pPartitionID
	 *            the partition ID
	 * @param pPartition
	 *            the partition
	 * @param pThreadManager
	 *            the thread manager
	 * 
	 * @throws KETLThreadException
	 *             the KETL thread exception
	 */
    public DynamicTransformation(Node pXMLConfig, int pPartitionID, int pPartition, ETLThreadManager pThreadManager) throws KETLThreadException {
        super(pXMLConfig, pPartitionID, pPartition, pThreadManager);
    }

    @Override
    protected void close(boolean success, boolean jobFailed) {
    }

    @Override
    protected String getVersion() {
        return "$LastChangedRevision: 491 $";
    }
}
