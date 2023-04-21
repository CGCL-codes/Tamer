package org.cesar.flip.flipg.model;

public class FeatureExpressionToComponentAssociation {

    /**
	 * Component to be associated.
	 */
    private IComponent component;

    /**
	 * Feature or Expression to be associated.
	 */
    private IExpression featureExpression;

    public FeatureExpressionToComponentAssociation(IExpression featureExpression) {
        this.featureExpression = featureExpression;
    }

    public FeatureExpressionToComponentAssociation() {
    }

    /**
	 * Returns the component associated.
	 * 
	 * @return The component associated.
	 */
    public IComponent getComponent() {
        return this.component;
    }

    /**
	 * Sets the component associated.
	 * 
	 * @param component
	 *            The component associated.
	 */
    public void setComponent(IComponent component) {
        this.component = component;
    }

    /**
	 * Returns the feature or expression associated.
	 * 
	 * @return The feature associated.
	 */
    public IExpression getFeatureExpression() {
        return this.featureExpression;
    }

    /**
	 * Sets the feature or expression associated.
	 * 
	 * @param feature
	 *            The feature associated.
	 */
    public void setFeatureExpression(IExpression featureExpression) {
        this.featureExpression = featureExpression;
    }
}
