package eu.soa4all.execution.soaprest.adapter.parser.actions;

import java.util.ArrayList;
import eu.soa4all.execution.soaprest.adapter.parser.mismatches.DataTypeMergeMismatch;
import eu.soa4all.execution.soaprest.adapter.parser.mismatches.Mismatch;

/**
 * Class MergeDataType Class that performs a data type merge adaption
 */
public class MergeDataType {

    ArrayList<Object> clientRequests;

    DataTypeMergeMismatch mismatch;

    MappingFunction mappingFunction;

    ArrayList<Mismatch> auxMismatches;

    /**
	 * Constructor wiht client request as input
	 * @param clientRequest the client request
	 * @param mismatch the mismatch to adapt
	 */
    public MergeDataType(ArrayList<Object> clientRequests, DataTypeMergeMismatch mismatch, ArrayList<Mismatch> auxMismatches, MappingFunction mappingFunction) {
        this.clientRequests = clientRequests;
        this.mismatch = mismatch;
        this.mappingFunction = mappingFunction;
        this.auxMismatches = auxMismatches;
    }

    /**
	 * Perform the requested adaption
	 * @return the adapted request
	 */
    public Object makeAdaption(Mismatch mismatch) {
        RenameDataType rdt = new RenameDataType(clientRequests, auxMismatches, mappingFunction);
        Object adaptedDataType = rdt.makeAdaption("partial");
        return adaptedDataType;
    }
}
