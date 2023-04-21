package org.simulator.sbml.astnode;

import org.sbml.jsbml.ASTNode;
import org.sbml.jsbml.Species;
import org.simulator.sbml.ValueHolder;

/**
 * 
 * @author Roland Keller
 * @version $Rev: 22 $
 * @since 1.0
 */
public class SpeciesValue extends ASTNodeObject {

    protected Species s;

    protected String id;

    protected ValueHolder valueHolder;

    protected boolean isSetInitialAmount;

    protected boolean hasOnlySubstanceUnits;

    protected boolean isSetInitialConcentration;

    protected int position;

    protected int compartmentPosition;

    protected boolean zeroSpatialDimensions;

    /**
   * 
   * @param interpreter
   * @param node
   * @param s
   * @param valueHolder
   */
    public SpeciesValue(ASTNodeInterpreterWithTime interpreter, ASTNode node, Species s, ValueHolder valueHolder, int position, int compartmentPosition, boolean zeroSpatialDimensions) {
        super(interpreter, node);
        this.s = s;
        this.id = s.getId();
        this.valueHolder = valueHolder;
        this.isSetInitialAmount = s.isSetInitialAmount();
        this.isSetInitialConcentration = s.isSetInitialConcentration();
        this.hasOnlySubstanceUnits = s.getHasOnlySubstanceUnits();
        this.position = position;
        this.compartmentPosition = compartmentPosition;
        this.zeroSpatialDimensions = zeroSpatialDimensions;
    }

    @Override
    protected void computeDoubleValue() {
        double compartmentValue = valueHolder.getCurrentValueOf(compartmentPosition);
        if ((compartmentValue == 0d) || zeroSpatialDimensions) {
            doubleValue = valueHolder.getCurrentValueOf(position);
        } else if (isSetInitialAmount && !hasOnlySubstanceUnits) {
            doubleValue = valueHolder.getCurrentValueOf(position) / compartmentValue;
        } else if (isSetInitialConcentration && hasOnlySubstanceUnits) {
            doubleValue = valueHolder.getCurrentValueOf(position) * compartmentValue;
        } else {
            doubleValue = valueHolder.getCurrentValueOf(position);
        }
    }
}
