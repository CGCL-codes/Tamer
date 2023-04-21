package com.kni.etl.ketl.writer;

import java.util.HashMap;
import java.util.HashSet;
import org.w3c.dom.Node;
import com.kni.etl.ketl.ETLInPort;
import com.kni.etl.ketl.exceptions.KETLThreadException;
import com.kni.etl.ketl.exceptions.KETLWriteException;
import com.kni.etl.ketl.smp.DefaultWriterCore;
import com.kni.etl.ketl.smp.ETLThreadManager;

/**
 * The Class ExceptionWriter.
 * 
 * @author nwakefield To change the template for this generated type comment go
 *         to Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and
 *         Comments
 */
public class ExceptionWriter extends ETLWriter implements DefaultWriterCore {

    @Override
    protected String getVersion() {
        return "$LastChangedRevision: 491 $";
    }

    /**
	 * Instantiates a new exception writer.
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
    public ExceptionWriter(Node pXMLConfig, int pPartitionID, int pPartition, ETLThreadManager pThreadManager) throws KETLThreadException {
        super(pXMLConfig, pPartitionID, pPartition, pThreadManager);
        if (pPartition > 1) {
            throw new KETLThreadException("Exception writer cannot be run in parallel, or multiple exceptions will be generated, please set FLOWTYPE=\"FANIN\". Requested partitions: " + pPartition, this);
        }
    }

    /** The input name map. */
    private final HashMap mInputNameMap = new HashMap();

    @Override
    public int initialize(Node pXmlConfig) throws KETLThreadException {
        int res = super.initialize(pXmlConfig);
        if (res != 0) return res;
        HashSet hs = new HashSet();
        hs.add("MESSAGE");
        for (int i = 0; i < this.mInPorts.length; i++) {
            if (hs.contains(this.mInPorts[i].mstrName) == false) {
                com.kni.etl.dbutils.ResourcePool.LogMessage(this, com.kni.etl.dbutils.ResourcePool.INFO_MESSAGE, "Invalid input name of " + this.mInPorts[i].mstrName + " will be ignored, it has to be one of " + java.util.Arrays.toString(hs.toArray()));
            }
            this.mInputNameMap.put(this.mInPorts[i].mstrName, i);
        }
        if (this.mInputNameMap.containsKey("MESSAGE") == false) throw new KETLThreadException("MESSAGE input must be specified", this);
        return 0;
    }

    @Override
    protected void close(boolean success, boolean jobFailed) {
    }

    public int putNextRecord(Object[] pInputRecords, Class[] pExpectedDataTypes, int pRecordWidth) throws KETLWriteException {
        String strMessage;
        ETLInPort port = this.mInPorts[(Integer) this.mInputNameMap.get("MESSAGE")];
        Object o = port.isConstant() ? port.getConstantValue() : pInputRecords[port.getSourcePortIndex()];
        if (o == null) throw new KETLWriteException("MESSAGE cannot be NULL");
        strMessage = o.toString();
        throw new ForcedException(strMessage);
    }
}
