package fr.cnes.sitools.forms.builder;

import java.util.ArrayList;
import java.util.List;
import fr.cnes.sitools.dataset.model.Operator;
import fr.cnes.sitools.dataset.model.Predicat;
import fr.cnes.sitools.forms.PredicatBuilder;
import fr.cnes.sitools.forms.components.FormObject;

/**
 * Numeric field form
 * @author AKKA
 *
 */
public final class NumericFieldForm extends PredicatBuilder {

    /**
   * Get the list of predicates
   * @return a list of predicates
   */
    @Override
    public List<Predicat> getPredicat() {
        List<Predicat> predicats = new ArrayList<Predicat>(1);
        Predicat predicat = new Predicat();
        if (checkValues(getFormObject())) {
            predicat.setLeftAttribute(getFormObject().getColumns().get(0));
            predicat.setNbOpenedParanthesis(0);
            predicat.setNbClosedParanthesis(0);
            predicat.setCompareOperator(Operator.EQ.value());
            predicat.setRightValue(getFormObject().getValues()[0]);
        }
        predicats.add(predicat);
        return predicats;
    }

    /**
   * Check values in the form
   * @param formObject the form to check
   * @return true if values agree
   */
    private boolean checkValues(FormObject formObject) {
        String[] values = formObject.getValues();
        return (values.length == 1);
    }
}
