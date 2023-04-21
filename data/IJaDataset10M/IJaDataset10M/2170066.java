package de.tudresden.inf.lat.jcel.coreontology.axiom;

import java.util.List;

/**
 * An object of this class is a factory to create any normalized axiom.
 * 
 * @author Julian Mendez
 */
public class NormalizedIntegerAxiomFactoryImpl implements NormalizedIntegerAxiomFactory {

    /**
	 * Constructs a new normalized axiom factory.
	 */
    public NormalizedIntegerAxiomFactoryImpl() {
    }

    @Override
    public FunctObjectPropAxiom createFunctObjectPropAxiom(int prop) {
        return new FunctObjectPropAxiom(prop);
    }

    @Override
    public GCI0Axiom createGCI0Axiom(int subCl, int superCl) {
        return new GCI0Axiom(subCl, superCl);
    }

    @Override
    public GCI1Axiom createGCI1Axiom(List<Integer> leftClList, int rightCl) {
        return new GCI1Axiom(leftClList, rightCl);
    }

    @Override
    public GCI2Axiom createGCI2Axiom(int leftCl, int rightProp, int rightCl) {
        return new GCI2Axiom(leftCl, rightProp, rightCl);
    }

    @Override
    public GCI3Axiom createGCI3Axiom(int leftProp, int leftCl, int rightCl) {
        return new GCI3Axiom(leftProp, leftCl, rightCl);
    }

    @Override
    public NominalAxiom createNominalAxiom(int classId, int individualId) {
        return new NominalAxiom(classId, individualId);
    }

    @Override
    public RangeAxiom createRangeAxiom(int prop, int cl) {
        return new RangeAxiom(prop, cl);
    }

    @Override
    public RI1Axiom createRI1Axiom(int prop) {
        return new RI1Axiom(prop);
    }

    @Override
    public RI2Axiom createRI2Axiom(int leftProp, int rightProp) {
        return new RI2Axiom(leftProp, rightProp);
    }

    @Override
    public RI3Axiom createRI3Axiom(int leftLeftProp, int leftRightProp, int rightProp) {
        return new RI3Axiom(leftLeftProp, leftRightProp, rightProp);
    }
}
