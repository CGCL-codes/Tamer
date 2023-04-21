package de.fraunhofer.isst.axbench.axlang.elements.featuremodel;

import java.util.Collection;
import de.fraunhofer.isst.axbench.axlang.api.AbstractAXLangElement;
import de.fraunhofer.isst.axbench.axlang.utilities.ReferenceKind;
import de.fraunhofer.isst.axbench.axlang.utilities.Role;
import de.fraunhofer.isst.axbench.axlang.utilities.Selection;
import de.fraunhofer.isst.axbench.axlang.utilities.ValidType;
import de.fraunhofer.isst.axbench.axlang.utilities.attributes.Cardinality;

/** 
 * @brief @c Configuration models a specific configuration of a feature model.
 * 
 * @par Referenced elements
 * - <strong>1</strong> feature model (FeatureModel)
 * - <strong>0..*</strong> selected features (Feature)
 * - <strong>0..*</strong> deselected features (Feature)
 * 
 * @author ekleinod
 * @version 0.7.2
 * @since 0.7.0
 */
public class Configuration extends AbstractAXLangElement {

    /**
	 * @brief Constructor.
	 */
    public Configuration() {
        super();
        setValidElementTypes(ReferenceKind.REFERENCE, new ValidType(Role.FEATUREMODEL, new Cardinality(1, 1)), new ValidType(Role.SELECTEDFEATURE, new Cardinality(0, Cardinality.INFINITY)), new ValidType(Role.DESELECTEDFEATURE, new Cardinality(0, Cardinality.INFINITY)));
    }

    /**
	 * @brief getter method for the reference feature model
	 * @return the reference feature model
	 */
    public FeatureModel getFeatureModel() {
        return (FeatureModel) getReference(Role.FEATUREMODEL);
    }

    /**
	 * @brief getter method for the collection of referenced selected features
	 * @return the collection of referenced selected features
	 */
    @SuppressWarnings("unchecked")
    public Collection<Feature> getSelectedFeatures() {
        return (Collection<Feature>) getReferences(Role.SELECTEDFEATURE);
    }

    /**
	 * @brief getter method for the collection of referenced selected features
	 * @return the collection of referenced selected features
	 */
    @SuppressWarnings("unchecked")
    public Collection<Feature> getDeSelectedFeatures() {
        return (Collection<Feature>) getReferences(Role.DESELECTEDFEATURE);
    }

    /**
	 * @brief Returns selection for the given feature with computation.
	 * 
	 * @param axlFeature given feature
	 * @return selection
	 */
    public Selection getSelection(Feature axlFeature) {
        return getSelection(axlFeature, true);
    }

    /**
	 * @brief Returns selection for the given feature.
	 * 
	 * This method computes the selected and deselected features.
	 * Cardinalities are interpreted as follows:
	 * - open subfeatures are selected if
	 *   \#all - (\#sel + \#desel) <= minimum cardinality - \#sel
	 * - open subfeatures are deselected if
	 *   \#all - (\#sel + \#desel) <= (\#all - maximum cardinality) - \#desel
	 *
	 * @todo check computation
	 * 
	 * @param axlFeature given feature
	 * @param bCompute compute selection? (true == compute; false == no computation)
	 * @return selection
	 */
    public Selection getSelection(Feature axlFeature, boolean bCompute) {
        if (getReferences(Role.SELECTEDFEATURE).contains(axlFeature)) {
            return Selection.YES;
        }
        if (getReferences(Role.DESELECTEDFEATURE).contains(axlFeature)) {
            return Selection.NO;
        }
        if (bCompute) {
            if (axlFeature.getParent() instanceof Feature) {
                Feature axlParent = (Feature) axlFeature.getParent();
                if (axlParent.getCardinality() != null) {
                    int iSelected = 0;
                    int iDeSelected = 0;
                    Collection<Feature> cllFeatures = axlParent.getFeatures();
                    for (Feature theSubFeature : cllFeatures) {
                        if (getSelection(theSubFeature, false) == Selection.YES) {
                            iSelected++;
                        }
                        if (getSelection(theSubFeature, false) == Selection.NO) {
                            iDeSelected++;
                        }
                    }
                    if ((cllFeatures.size() - (iSelected + iDeSelected)) <= (axlParent.getCardinality().getLowerBound() - iSelected)) {
                        return Selection.YES;
                    }
                    if ((cllFeatures.size() - (iSelected + iDeSelected)) <= (cllFeatures.size() - axlParent.getCardinality().getUpperBound() - iDeSelected)) {
                        return Selection.NO;
                    }
                }
            }
        }
        return Selection.OPEN;
    }
}
