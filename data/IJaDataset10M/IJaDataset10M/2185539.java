package org.parallelj.mda.controlflow.diagram.extension.providers;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParser;
import org.parallelj.mda.controlflow.diagram.edit.parts.MultipleInstanceTaskNameEditPart;
import org.parallelj.mda.controlflow.diagram.edit.parts.StepNameEditPart;
import org.parallelj.mda.controlflow.diagram.edit.parts.TransitionPredicateInfoEditPart;
import org.parallelj.mda.controlflow.diagram.extension.parsers.EResultFormatter;
import org.parallelj.mda.controlflow.diagram.extension.parsers.EStructuralFeatureChain;
import org.parallelj.mda.controlflow.diagram.extension.parsers.ExtendedMessageFormatParser;
import org.parallelj.mda.controlflow.diagram.providers.ControlFlowParserProvider;
import org.parallelj.mda.controlflow.model.controlflow.ControlFlowPackage;

/**
 * Extended ControlFlowParserProvider, for creating the Extended Edit Parts's
 * IParsers.
 * 
 * @author Atos Worldline
 * @see ControlFlowParserProvider for more information
 * 
 */
public class ExtendedControlFlowParserProvider extends ControlFlowParserProvider {

    private EResultFormatter removePackageTransformer;

    /**
	 * Used EPackage instance
	 */
    private ControlFlowPackage cFlowPackage;

    /**
	 * Created new Extended ControlFlowParserProvider
	 */
    public ExtendedControlFlowParserProvider() {
        this.cFlowPackage = ControlFlowPackage.eINSTANCE;
        this.removePackageTransformer = new EResultFormatter() {

            public Object format(Object o) {
                if (o instanceof String) {
                    String s = (String) o;
                    int packageLastDot = s.lastIndexOf(".");
                    int count = s.length();
                    if (packageLastDot > -1 && count > packageLastDot) {
                        String taskName = s.substring(packageLastDot + 1).trim();
                        return taskName;
                    }
                }
                return o;
            }
        };
    }

    @Override
    protected IParser getParser(int visualID) {
        switch(visualID) {
            case StepNameEditPart.VISUAL_ID:
                return this.getStepName_5003ExtendedParser();
            case MultipleInstanceTaskNameEditPart.VISUAL_ID:
                return this.getMultipleInstanceTaskName_5017ExtendedParser();
            case TransitionPredicateInfoEditPart.VISUAL_ID:
                return this.getTransitionPredicateInfo_6001ExtendedParser();
        }
        return super.getParser(visualID);
    }

    /**
	 * Extended Parser for Step names
	 */
    private IParser stepName_5003ExtendedParser;

    /**
	 * @return Extended Parser for Step names
	 */
    private IParser getStepName_5003ExtendedParser() {
        if (this.stepName_5003ExtendedParser == null) {
            EStructuralFeatureChain value0 = new EStructuralFeatureChain(new EReference[] {}, this.cFlowPackage.getFlowElement_Name());
            EStructuralFeatureChain value1 = new EStructuralFeatureChain(new EReference[] { this.cFlowPackage.getStep_Task() }, this.cFlowPackage.getTask_Name(), this.removePackageTransformer);
            ExtendedMessageFormatParser parser = new ExtendedMessageFormatParser(new EStructuralFeatureChain[] { value0, value1 }, new EStructuralFeatureChain[] { value0 });
            parser.setViewPattern("{0} -> {1}");
            parser.setEditorPattern("{0}");
            parser.setEditPattern("{0}");
            this.stepName_5003ExtendedParser = parser;
        }
        return this.stepName_5003ExtendedParser;
    }

    /**
	 * Extended Parser for MIT names
	 */
    private IParser multipleInstanceTaskName_5017ExtendedParser;

    /**
	 * @return Extended Parser for MIT names
	 */
    private IParser getMultipleInstanceTaskName_5017ExtendedParser() {
        if (this.multipleInstanceTaskName_5017ExtendedParser == null) {
            EStructuralFeatureChain value0 = new EStructuralFeatureChain(new EReference[] {}, this.cFlowPackage.getTask_Name());
            EStructuralFeatureChain value1 = new EStructuralFeatureChain(new EReference[] { this.cFlowPackage.getMultipleInstanceTask_Instance() }, this.cFlowPackage.getTask_Name(), this.removePackageTransformer);
            ExtendedMessageFormatParser parser = new ExtendedMessageFormatParser(new EStructuralFeatureChain[] { value0, value1 }, new EStructuralFeatureChain[] { value0 });
            parser.setViewPattern("{0} -> {1}");
            parser.setEditorPattern("{0}");
            parser.setEditPattern("{0}");
            this.multipleInstanceTaskName_5017ExtendedParser = parser;
        }
        return this.multipleInstanceTaskName_5017ExtendedParser;
    }

    /**
	 * Extended Parser for Transition's predicate info
	 */
    private IParser transitionPredicateInfo_6001ExtendedParser;

    /**
	 * @return Extended Parser for Transition's predicate info
	 */
    private IParser getTransitionPredicateInfo_6001ExtendedParser() {
        if (this.transitionPredicateInfo_6001ExtendedParser == null) {
            EStructuralFeatureChain value0 = new EStructuralFeatureChain(new EReference[] { this.cFlowPackage.getTransition_Predicate() }, this.cFlowPackage.getPredicate_Name());
            ExtendedMessageFormatParser parser = new ExtendedMessageFormatParser(new EStructuralFeatureChain[] { value0 }, new EStructuralFeatureChain[] { value0 });
            parser.setViewPattern("({0})");
            parser.setEditorPattern("{0}");
            parser.setEditPattern("{0}");
            this.transitionPredicateInfo_6001ExtendedParser = parser;
        }
        return this.transitionPredicateInfo_6001ExtendedParser;
    }
}
