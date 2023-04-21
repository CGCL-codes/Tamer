package de.hu_berlin.sam.mmunit.coverage.analyzer.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EcorePackage;
import de.hu_berlin.sam.mmunit.MMUnitPackage;
import de.hu_berlin.sam.mmunit.coverage.CoveragePackage;
import de.hu_berlin.sam.mmunit.coverage.analyzer.AnalyzerPackage;
import de.hu_berlin.sam.mmunit.coverage.analyzer.InitialAnalysisStep;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Initial Analysis Step</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class InitialAnalysisStepImpl extends QvtTransformationImpl implements InitialAnalysisStep {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    protected InitialAnalysisStepImpl() {
        super();
        getQvtProcessor().setInplace(false);
        getQvtProcessor().getMetamodels().add(EcorePackage.eINSTANCE);
        getQvtProcessor().getMetamodels().add(CoveragePackage.eINSTANCE);
        getQvtProcessor().getMetamodels().add(MMUnitPackage.eINSTANCE);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return AnalyzerPackage.Literals.INITIAL_ANALYSIS_STEP;
    }
}
