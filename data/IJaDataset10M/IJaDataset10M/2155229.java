package picoevo.debug;

import picoevo.core.evolution.EvaluationOperator_Individual;
import picoevo.ext.representation.Element_Bit;
import picoevo.ext.representation.Individual_SimpleIndividual;
import picoevo.toolbox.Display;

public class EvaluationOperator_MaxOne_ElementList_Bit extends EvaluationOperator_Individual {

    public EvaluationOperator_MaxOne_ElementList_Bit() {
        super();
    }

    public EvaluationOperator_MaxOne_ElementList_Bit(String __name) {
        super(__name);
    }

    @Override
    public void evaluate(Object __o) {
        super.evaluate(__o);
        try {
            Individual_SimpleIndividual individual = (Individual_SimpleIndividual) __o;
            double maxOne = 0;
            for (int i = 0; i != individual.getElementListSize(); i++) if (((Element_Bit) individual.getElementAt(i)).getValue() == true) maxOne++;
            individual.setFitness(maxOne / individual.getElementListSize());
        } catch (java.lang.ClassCastException e) {
            Display.error("" + this.getClass().getName() + "::evaluate(.) - Class cast exception");
        }
    }
}
