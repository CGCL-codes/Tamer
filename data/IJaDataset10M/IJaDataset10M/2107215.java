package ca.uwaterloo.gp.fmp;

import org.eclipse.emf.common.util.EList;

/**
 * @author Chang Hwan Peter Kim <chpkim@swen.uwaterloo.ca>, 
 *         Michal Antkiewicz <mantkiew@swen.uwaterloo.ca>
 * @model abstract="true"
 */
public interface Clonable extends Node {

    /**
	 * @model defaultValue="UNDECIDED"
	 */
    ConfigState getState();

    /**
	 * Sets the value of the '{@link ca.uwaterloo.gp.fmp.Clonable#getState <em>State</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>State</em>' attribute.
	 * @see ca.uwaterloo.gp.fmp.ConfigState
	 * @see #getState()
	 * @generated
	 */
    void setState(ConfigState value);

    /**
	 * @model type="Clonable" transient="true" containment="false" opposite="prototype"
	 */
    EList getClones();

    /**
	 * @model opposite="clones"
	 */
    Clonable getPrototype();

    /**
	 * Sets the value of the '{@link ca.uwaterloo.gp.fmp.Clonable#getPrototype <em>Prototype</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Prototype</em>' reference.
	 * @see #getPrototype()
	 * @generated
	 */
    void setPrototype(Clonable value);

    /**
	 * Returns the caridnality to be shown for checkbox view.  Takes clones, but not state, into account.
	 * @return
	 */
    int[] getCheckboxViewCardinality();

    /**
	 * Returns the cardinality to be shown for featuremodel view.  Takes clones and state into account.
	 * This should be used for configuring.
	 */
    int[] getFeatureModelViewCardinality();

    /**
	 * Determines whether or not this node is an optional node. Determines this by 
	 * seeing if the checkbox view cardinality is [0..1]
	 * @return
	 */
    boolean isOptional();

    /**
	 * Determines whether or not this node is a truly optional node, as the product of cardinality of itself
	 * and all its parents is [0..1].
	 * @return
	 */
    boolean isAccumulatedOptional();
}
