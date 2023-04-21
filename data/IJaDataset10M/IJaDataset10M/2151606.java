package tms.client.filter;

import tms.client.controls.ExtendedCheckBox;
import tms.client.i18n.TMSConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;

/**
 * Panel containing the AND & OR check boxes. 
 * It is used to specify the inclusion of multiple defined filter constraints.
 * 
 * @author Wildrich Fourie
 */
public class AndOrCheckboxesPanel extends HorizontalPanel {

    private static final TMSConstants constants = GWT.create(TMSConstants.class);

    private ExtendedCheckBox<Boolean> andCheckbox;

    private ExtendedCheckBox<Boolean> orCheckbox;

    public AndOrCheckboxesPanel() {
        this.setSpacing(10);
        this.setHorizontalAlignment(ALIGN_CENTER);
        andCheckbox = new ExtendedCheckBox<Boolean>(constants.filter_and(), true);
        andCheckbox.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                orCheckbox.setChecked(!andCheckbox.isChecked());
            }
        });
        orCheckbox = new ExtendedCheckBox<Boolean>(constants.filter_or(), false);
        orCheckbox.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                andCheckbox.setChecked(!orCheckbox.isChecked());
            }
        });
        this.add(andCheckbox);
        this.add(orCheckbox);
    }

    /**
	 * Gets the value selected for this set of checkboxes.
	 * @return
	 * <li> True = AND
	 * <li> False = OR
	 */
    protected boolean getValue() {
        return andCheckbox.isChecked();
    }
}
