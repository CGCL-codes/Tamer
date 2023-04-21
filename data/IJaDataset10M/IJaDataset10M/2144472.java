package org.modyna.modyna.model.elements;

import com.jgoodies.validation.ValidationResult;

/**
 * Provides an adapter from the GUI editor to the LevelUIBean. Adapted from the
 * OrderModel in the tutorial of Karsten Lentsch's validation framework. See
 * www.jgoodies.com for details.
 * 
 * @author Dr. Rupert Rebentisch
 */
public class LevelUIBeanModel extends QuantityUIBeanModel {

    private static final long serialVersionUID = 6207678657455663308L;

    public LevelUIBeanModel(LevelUIBean levelUIBean) {
        super(levelUIBean);
    }

    protected void updateValidationResult() {
        LevelUIBean levelUIBean = (LevelUIBean) getBean();
        ValidationResult result = new LevelUIBeanValidator(levelUIBean).validate(levelUIBean);
        validationResultModel.setResult(result);
    }
}
