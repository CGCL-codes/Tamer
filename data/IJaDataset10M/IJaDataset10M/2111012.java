package com.hp.hpl.jena.assembler.assemblers;

import com.hp.hpl.jena.assembler.*;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.rdf.model.impl.MemoryModelGetter;
import com.hp.hpl.jena.vocabulary.RDF;

public class ModelSourceAssembler extends AssemblerBase {

    public Object open(Assembler a, Resource root, Mode irrelevant) {
        checkType(root, JA.ModelSource);
        return root.hasProperty(RDF.type, JA.RDBModelSource) ? (ModelGetter) createRDBGetter(getConnection(a, root)) : new MemoryModelGetter();
    }

    protected ModelGetter createRDBGetter(ConnectionDescription c) {
        return ModelFactory.createModelRDBMaker(c.getConnection());
    }

    private ConnectionDescription getConnection(Assembler a, Resource root) {
        return (ConnectionDescription) a.open(getRequiredResource(root, JA.connection));
    }
}
