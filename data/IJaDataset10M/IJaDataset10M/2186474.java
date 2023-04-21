package org.gwtoolbox.bean.client.validation.messages;

import com.google.gwt.i18n.client.Constants;

/**
 * @author Uri Boness
 */
public interface ValidationMessages extends Constants {

    @DefaultStringValue("Must be false")
    String javax_validation_constraints_AssertFalse_message();

    @DefaultStringValue("Must be true")
    String javax_validation_constraints_AssertTrue_message();

    @DefaultStringValue("Decimal max")
    String javax_validation_constraints_DecimalMax_message();

    @DefaultStringValue("Decimal min")
    String javax_validation_constraints_DecimalMin_message();

    @DefaultStringValue("Size must be between {min} and {max}")
    String javax_validation_constraints_Size_message();

    @DefaultStringValue("Numeric value out of bounds (<{integer} digits>.<{fraction} digits> expected)")
    String javax_validation_constraints_Digits_message();

    @DefaultStringValue("Must be greater than or equal to {value}")
    String javax_validation_constraints_Min_message();

    @DefaultStringValue("Must be less than or equal to {value}")
    String javax_validation_constraints_Max_message();

    @DefaultStringValue("Must not be null")
    String javax_validation_constraints_NotNull_message();

    @DefaultStringValue("Must be null")
    String javax_validation_constraints_Null_message();

    @DefaultStringValue("Must be a past date")
    String javax_validation_constraints_Past_message();

    @DefaultStringValue("Must be a future date")
    String javax_validation_constraints_Future_message();

    @DefaultStringValue("Doesn't follow the pattern {regexp}")
    String javax_validation_constraints_Pattern_message();

    @DefaultStringValue("Invalid email")
    String org_gwtoolbox_bean_client_validation_constraint_Email_message();

    @DefaultStringValue("Invalid URL")
    String org_gwtoolbox_bean_client_validation_constraint_HttpUrl_message();

    @DefaultStringValue("Must not be empty")
    String org_gwtoolbox_bean_client_validation_constraint_NotEmpty_message();

    @DefaultStringValue("Must be greater than or equal to {min} and less than or equal to {max}")
    String org_gwtoolbox_bean_client_validation_constraint_NumericRange_message();
}
