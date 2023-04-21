package de.fraunhofer.isst.axbench.eastadlinterface.operations.relation;

import de.fraunhofer.isst.axbench.eastadlinterface.Env;
import de.fraunhofer.isst.axbench.eastadlinterface.util.UuidGenerator;
import de.fraunhofer.isst.eastadl.datatypes.DatatypesFactory;
import de.fraunhofer.isst.eastadl.datatypes.EADatatypePrototype;
import de.fraunhofer.isst.eastadl.functionmodeling.FunctionClientServerInterface;
import de.fraunhofer.isst.eastadl.functionmodeling.Operation;

public class EADatatypePrototypeReturnValueCreator {

    private Env env;

    public EADatatypePrototypeReturnValueCreator(Env e) {
        env = e;
    }

    public EADatatypePrototype createNewEADatatypePrototypeReturnValue(FunctionClientServerInterface eaInterface, Operation eaOperation) {
        EADatatypePrototype eaPrototype = DatatypesFactory.eINSTANCE.createEADatatypePrototype();
        eaPrototype.setUuid(UuidGenerator.genUuid());
        env.getAdapterFactory().adapt_asReturnValue(eaPrototype, eaInterface, eaOperation);
        return eaPrototype;
    }
}
