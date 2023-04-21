package com.patientis.model.clinical;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.patientis.ejb.clinical.FormTypeMissingDefinitionException;
import com.patientis.framework.controls.exceptions.ISValidateControlException;
import com.patientis.model.common.Converter;
import com.patientis.model.common.DateTimeModel;
import com.patientis.model.common.DisplayModel;
import com.patientis.model.order.OrderTypeTransitionModel;
import com.patientis.model.reference.ActionReference;
import com.patientis.model.reference.FormDefinitionReference;
import com.patientis.model.reference.FormGroupReference;

/**
 * FormType
 * 
 */
public class FormTypeModel extends FormTypeDataModel {

    private static final long serialVersionUID = 1L;

    /**
	 * Default constructor
	 */
    public FormTypeModel() {
    }

    /**
	 * @see com.patientis.model.common.BaseModel#getDisplayListText()
	 */
    @Override
    public String getDisplayListText() {
        return getFormTypeRef().getDisplay();
    }

    /**
	 * 
	 * @return
	 */
    public int getMaxScreenSequence() {
        int max = 0;
        for (FormTypeScreenModel screen : getScreens()) {
            if (screen.getScreenSequence() > max) {
                max = screen.getScreenSequence();
            }
        }
        return max;
    }

    /**
	 * Returns a random item if none exists it is added to the set.
	 */
    @Override
    public FormTypeScreenModel giveFormTypeScreen() {
        if (getScreens().size() > 1) {
            List<FormTypeScreenModel> screens = new ArrayList<FormTypeScreenModel>();
            screens.addAll(getScreens());
            Collections.sort(screens);
            return screens.get(0);
        } else {
            return super.giveFormTypeScreen();
        }
    }

    /**
	 * 
	 * @return
	 */
    public List<FormTypeScreenModel> getSortedScreens() {
        List<FormTypeScreenModel> screens = new ArrayList<FormTypeScreenModel>();
        screens.addAll(getScreens());
        Collections.sort(screens);
        return screens;
    }

    @Override
    public int compareTo(Object o) {
        FormTypeModel formType = (FormTypeModel) o;
        int comparison = Converter.compareTo(getFormFolderRef(), formType.getFormFolderRef());
        if (comparison == 0) {
            return Converter.compareTo(getFormTypeRef(), formType.getFormTypeRef());
        } else {
            return comparison;
        }
    }

    /**
	 * 
	 * @return
	 */
    public FormModel newForm() {
        FormModel form = new FormModel();
        form.setFormTypeRef(getFormTypeRef());
        form.setFormDt(DateTimeModel.getNow());
        if (Converter.isNotEmpty(getLabel())) {
            form.setTitle(getLabel());
        } else {
            form.setTitle(getFormTypeRef().getDisplay());
        }
        return form;
    }

    /**
	 * 
	 * @return
	 */
    public String getLabelOrDisplay() {
        String label = getLabel();
        if (Converter.isEmpty(label)) {
            label = getFormTypeRef().getShortDisplay();
        }
        return label;
    }

    /**
	 * @see com.patientis.model.clinical.FormTypeDataModel#validateDataModel()
	 */
    @Override
    public void validateDataModel() throws ISValidateControlException {
        super.validateDataModel();
        if (getFormDefinitionRef().isNew()) {
            setFormDefinitionRef(new DisplayModel(FormDefinitionReference.SYSTEMFORMSAVEONLY.getRefId()));
        }
    }

    /**
	 * 
	 * @return
	 */
    public long getFormTypeRefId() {
        return getFormTypeRef().getId();
    }
}
