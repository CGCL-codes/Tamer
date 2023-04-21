package com.pp.cameleon.services.document.validator;

import com.pp.cameleon.api.document.dto.Exercice;
import com.pp.cameleon.api.document.dto.Keypoint;
import com.pp.cameleon.services.common.validator.AbstractValidator;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;

/**
 * User: P. Lalonde
 * Date: 2008-01-23
 */
public class ExerciceKeypointValidator extends AbstractValidator<Exercice> {

    /**
     * Will validate the object and appends the errors at the end of the list.
     *
     * @param toValidate The object to validate.
     * @param errorMsgs  The list of errors.
     * @return true if valid, otherwise, false.
     */
    public boolean isValid(Exercice toValidate, List<String> errorMsgs) {
        boolean isValid = true;
        Set keypoints = toValidate.getKeypoints();
        List<String> uniqueKeys = new ArrayList<String>();
        for (Object keypoint : keypoints) {
            Keypoint key = (Keypoint) keypoint;
            if (!uniqueKeys.contains(key.getName())) {
                uniqueKeys.add(key.getName());
            } else {
                errorMsgs.add(getMessage("validation.exercice.duplicate.keypoint") + "[" + key.getName() + "]");
                isValid = false;
            }
        }
        return isValid;
    }
}
