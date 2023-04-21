package uk.ac.lkl.client;

import java.util.List;
import com.google.gwt.user.client.ui.HTML;
import uk.ac.lkl.common.util.value.IntegerValue;
import uk.ac.lkl.migen.system.expresser.model.ExpresserModel;
import uk.ac.lkl.migen.system.expresser.model.tiednumber.TiedNumberExpression;

public class ComputersModelTiedNumberValues extends ExpresserVerticalPanel {

    private ExpresserModel model;

    public ComputersModelTiedNumberValues(ExpresserModel model) {
        super();
        this.model = model;
        updateDisplay();
        setStylePrimaryName("expresser-computers-model-values-panel");
    }

    protected void updateDisplay() {
        clear();
        List<TiedNumberExpression<IntegerValue>> tiedNumbers = model.getUnlockedNumbers();
        if (tiedNumbers.isEmpty()) {
            return;
        }
        add(new HTML(Expresser.messagesBundle.UnlockedNumbers()));
        boolean bindingsFound = false;
        for (TiedNumberExpression<?> t : tiedNumbers) {
            @SuppressWarnings("unchecked") TiedNumberExpression<IntegerValue> tiedNumber = (TiedNumberExpression<IntegerValue>) t;
            if (tiedNumber.isNamed()) {
                if (!bindingsFound) {
                    bindingsFound = true;
                }
                add(new HTML(TiedNumberExpression.UNLOCKED_BACKGROUND_HTML + tiedNumber.getName() + "</font>&nbsp;is&nbsp;" + tiedNumber.getValue().toString()));
            }
        }
        boolean valuesFound = false;
        for (TiedNumberExpression<?> tiedNumber : tiedNumbers) {
            if (!tiedNumber.isNamed() && !tiedNumber.isLocked()) {
                if (!valuesFound) {
                    valuesFound = true;
                    add(new HTML("Unnamed:&nbsp;"));
                }
                add(new HTML(tiedNumber.toHTMLString() + "&nbsp;"));
            }
        }
    }
}
