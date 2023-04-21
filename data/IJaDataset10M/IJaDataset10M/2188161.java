package com.acwebsitedesign.uRateDrugs.domain;

import org.springframework.validation.Errors;

public class ReviewValidator {

    @SuppressWarnings("unchecked")
    public boolean supports(Class clazz) {
        return Review.class.equals(clazz);
    }

    public void validate(Object object, Errors e) {
        Review review = (Review) object;
        String reasonForTaking = review.getReasonForTaking();
        String reviewText = review.getReviewText();
        if (reasonForTaking == null || "".equals(reasonForTaking)) {
            e.rejectValue("reasonForTaking", "reasonForTaking.empty");
        } else if (reviewText == null || "".equals(reviewText)) {
            e.rejectValue("reviewText", "reviewText.empty");
        }
    }
}
