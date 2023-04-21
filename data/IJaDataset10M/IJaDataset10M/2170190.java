package org.kaiec.timeanalysis;

import java.util.List;
import java.util.logging.Logger;
import org.openide.util.Lookup;
import org.semtinel.core.analysis.icdiff.ICDiffAnalysis;
import org.semtinel.core.analysis.icdiff.IICAnalysis;
import org.semtinel.core.analysis.icdiff.IICAnalysisFactory;
import org.semtinel.core.data.api.AnnotationSet;
import org.semtinel.core.data.api.Concept;
import org.semtinel.core.data.api.ConceptScheme;
import org.semtinel.core.experiments.api.AbstractAnalysis;
import org.semtinel.core.experiments.api.AnalysisFactory;
import org.semtinel.core.experiments.api.Explanation;
import org.semtinel.core.experiments.api.Registry;
import org.semtinel.core.register.RegisterDefinition;
import org.semtinel.core.register.RegisterSet;
import org.semtinel.core.register.SimpleMappingRegisterSetWrapper;

/**
 *
 * @author kai
 */
public class SetBasedICDiffAnalysis extends ICDiffAnalysis {

    private Logger log = Logger.getLogger(getClass().getName());

    private SetBasedICAnalysis informationContentAnalysis, informationContentBase;

    private IICAnalysis intrinsicInformationContent;

    private AnalysisFactory factory;

    public SetBasedICDiffAnalysis(AnalysisFactory factory) {
        super(factory);
        this.factory = factory;
        Registry reg = Lookup.getDefault().lookup(Registry.class);
        SetBasedICAnalysisFactory ana = SetBasedICAnalysisFactory.getFactory();
        this.informationContentAnalysis = ana.getInstance();
        this.informationContentBase = ana.getInstance();
        for (AnalysisFactory ana2 : reg.getAnalysisFactoryList(IICAnalysisFactory.FLAVOR)) {
            this.intrinsicInformationContent = (IICAnalysis) ana2.getInstance();
            break;
        }
    }

    public float getValue(Concept concept, RegisterSet registerSet) {
        return getValue(concept, null, registerSet);
    }

    public float getValue(Concept concept, Explanation explanation, RegisterSet registerSet) {
        SimpleMappingRegisterSetWrapper rsBase = new SimpleMappingRegisterSetWrapper(registerSet);
        rsBase.addMapping(RegisterDefinition.ANNOTATION_SET_SERIES, RegisterDefinition.ANNOTATION_SET_SERIES_BASE);
        List<AnnotationSet> annotationSets = registerSet.getRegister(RegisterDefinition.ANNOTATION_SET_SERIES).getValues();
        List<AnnotationSet> bases = registerSet.getRegister(RegisterDefinition.ANNOTATION_SET_SERIES_BASE).getValues();
        Explanation expAnalysis = null, expBase = null;
        String baseString = "ic(base)";
        if (bases == null) baseString = "iic";
        if (explanation != null) {
            explanation.getInputMap().put("concept", concept.getPrefLabel().getText());
            if (annotationSets != null) {
                int count = 1;
                for (AnnotationSet as : annotationSets) {
                    explanation.getInputMap().put("annotation_set_" + count++, as.getName());
                }
            }
            if (bases != null) {
                int count = 1;
                for (AnnotationSet as : bases) {
                    explanation.getInputMap().put("annotation_set_base_" + count++, as.getName());
                }
            }
            expAnalysis = new Explanation("ic_analysis");
            expBase = new Explanation(baseString);
        }
        float ic = informationContentAnalysis.getValue(concept, expAnalysis, registerSet);
        float icBase;
        if (bases == null) icBase = intrinsicInformationContent.getValue(concept, expBase, registerSet); else icBase = informationContentBase.getValue(concept, expBase, rsBase);
        if (explanation != null) {
            explanation.getSubnodes().add(expAnalysis);
            explanation.getSubnodes().add(expBase);
            explanation.getExtensionMap().put("ic(analysis)", Float.toString(ic));
            explanation.getExtensionMap().put(baseString, Float.toString(icBase));
            explanation.setText("The IC Diff is calculated as  ic(analysis) - " + baseString);
            explanation.setResult(Float.toString(ic - icBase));
        }
        return ic - icBase;
    }

    public Explanation getExplanation(Concept concept, RegisterSet registerSet) {
        Explanation explanation = new Explanation("IC Difference");
        getValue(concept, explanation, registerSet);
        return explanation;
    }

    public void populate(ConceptScheme scheme, AnnotationSet as) {
        return;
    }

    public AnalysisFactory getFactory() {
        return factory;
    }

    public boolean hasRequiredData(RegisterSet registerSet) {
        Object o = registerSet.getRegister(RegisterDefinition.CONCEPT_SCHEME).getValue();
        Object o2 = registerSet.getRegister(RegisterDefinition.ANNOTATION_SET_SERIES).getValue();
        return o != null && o2 != null;
    }

    public boolean needsPopulation(RegisterSet registerSet) {
        return false;
    }

    public void populate(RegisterSet registerSet) {
        return;
    }

    public boolean supportsPopulation() {
        return false;
    }
}
