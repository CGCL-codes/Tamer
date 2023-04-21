package com.rapidminer.operator.preprocessing.ie.features;

import java.util.Iterator;
import java.util.List;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.preprocessing.ie.features.tools.PreprocessOperatorImpl;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeCategory;
import com.rapidminer.parameter.ParameterTypeInt;
import com.rapidminer.parameter.ParameterTypeString;
import com.rapidminer.tools.OperatorService;

/**
 * This operator is used to convert a parsetree into a pruned parsetree in order to focus on special
 * elements in the tree.
 * 
 * @author Martin Had
 * @version $Id
 *
 */
public class TreeShapeProcessing extends PreprocessOperatorImpl {

    public static final String PARAMETER_TREESTRING = "treeStringAttributeName";

    public static final String PARAMETER_FEATURELIST = "featureAttributeList";

    public static final String PARAMETER_FEATUREDEPTH = "featureDepth";

    public static final String PARAMETER_PRUNINGMODE = "pruningMode";

    /** The combinations which can be used in the CompositeKernel / myKLR. */
    public static final String[] PRUNING_TYPES = { "None", "SPT", "Incl. Verb", "TopNode" };

    public TreeShapeProcessing(OperatorDescription description) {
        super(description);
        this.setParameter(PARAMETER_NAME, "treeShape");
        this.setParameter(PARAMETER_LENGTH, "-1");
        this.setParameter(PARAMETER_POSITION, "0");
    }

    @Override
    public List<ParameterType> getParameterTypes() {
        List<ParameterType> params = super.getParameterTypes();
        ParameterType t;
        Iterator<ParameterType> iter = params.iterator();
        while (iter.hasNext()) {
            t = iter.next();
            if (t.getKey().equals(PARAMETER_NAME)) {
                t = new ParameterTypeString(PreprocessOperatorImpl.PARAMETER_NAME, "RelationTree", false);
            }
        }
        params.add(new ParameterTypeString(PARAMETER_TREESTRING, "The tree string"));
        params.add(new ParameterTypeString(PARAMETER_FEATURELIST, "The list of attributes to be included in the tree, separated by ,"));
        ParameterType type = new ParameterTypeInt(PARAMETER_FEATUREDEPTH, "Position of the features, counted up from the leafs", 0, 10, 0);
        type.setExpert(false);
        params.add(type);
        type = new ParameterTypeCategory(PARAMETER_PRUNINGMODE, "The Shape of the Pruned Tree", PRUNING_TYPES, 0);
        type.setExpert(false);
        params.add(type);
        return params;
    }

    @Override
    public PreprocessOperatorImpl create() throws Exception {
        return (PreprocessOperatorImpl) OperatorService.createOperator("TreeShapePreprocessing");
    }
}
