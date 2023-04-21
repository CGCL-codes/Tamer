package de.tudresden.inf.lat.jcel.ontology.axiom.extension;

import de.tudresden.inf.lat.jcel.coreontology.axiom.NormalizedIntegerAxiomFactory;
import de.tudresden.inf.lat.jcel.coreontology.axiom.NormalizedIntegerAxiomFactoryImpl;
import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerEntityManager;
import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerEntityManagerImpl;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.ComplexIntegerAxiomFactory;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.ComplexIntegerAxiomFactoryImpl;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerDataTypeFactory;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerDataTypeFactoryImpl;

/**
 * An object of this class can create all the objects in an ontology.
 * 
 * @author Julian Mendez
 */
public class IntegerOntologyObjectFactoryImpl implements IntegerOntologyObjectFactory {

    private final ComplexIntegerAxiomFactory complexAxiomFactory = new ComplexIntegerAxiomFactoryImpl();

    private final IntegerDataTypeFactory dataTypeFactory = new IntegerDataTypeFactoryImpl();

    private final IntegerEntityManager idGenerator = new IntegerEntityManagerImpl();

    private final NormalizedIntegerAxiomFactory normalizedAxiomFactory = new NormalizedIntegerAxiomFactoryImpl();

    /**
	 * Constructs a new ontology object factory.
	 */
    public IntegerOntologyObjectFactoryImpl() {
    }

    @Override
    public ComplexIntegerAxiomFactory getComplexAxiomFactory() {
        return this.complexAxiomFactory;
    }

    @Override
    public IntegerDataTypeFactory getDataTypeFactory() {
        return this.dataTypeFactory;
    }

    @Override
    public IntegerEntityManager getEntityManager() {
        return this.idGenerator;
    }

    @Override
    public NormalizedIntegerAxiomFactory getNormalizedAxiomFactory() {
        return this.normalizedAxiomFactory;
    }
}
