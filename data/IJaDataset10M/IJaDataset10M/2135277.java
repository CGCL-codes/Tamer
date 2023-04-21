package de.fraunhofer.isst.axbench.eastadlinterface.operations.converter;

import de.fraunhofer.isst.eastadl.datatypes.EADatatype;
import de.fraunhofer.isst.eastadl.datatypes.EADatatypePrototype;

public interface IEADatatypePrototypeParameterConverter {

    void setName(EADatatypePrototype eaDatatypePrototype, String name);

    void unsetName(EADatatypePrototype eaDatatypePrototype, String name);

    void setType(EADatatypePrototype eaDatatypePrototype, EADatatype eaDatatype);

    void unsetType(EADatatypePrototype eaDatatypePrototype, EADatatype eaDatatype);
}
