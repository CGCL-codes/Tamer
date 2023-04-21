package org.pprun.hjpetstore.service.validator;

import org.pprun.hjpetstore.domain.Address;
import org.pprun.hjpetstore.domain.Order;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * @author Juergen Hoeller
 * @author <a href="mailto:quest.run@gmail.com">pprun</a>
 */
public class OrderValidator implements Validator {

    private final Validator addressValidator;

    public OrderValidator(Validator addressValidator) {
        if (addressValidator == null) {
            throw new IllegalArgumentException("The supplied [Validator] is required and must not be null.");
        }
        if (!addressValidator.supports(Address.class)) {
            throw new IllegalArgumentException("The supplied [Validator] must support the validation of [Address] instances.");
        }
        this.addressValidator = addressValidator;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Order.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        validateCreditCard((Order) obj, errors);
        validateBillingAddress((Order) obj, errors);
        validateShippingAddress((Order) obj, errors);
    }

    public void validateCreditCard(Order order, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "cardNumber", "required", new String[] { "FAKE (!) credit card number" });
        ValidationUtils.rejectIfEmpty(errors, "expireDate", "required", new String[] { "Expiry date" });
        ValidationUtils.rejectIfEmpty(errors, "cardType", "required", new String[] { "Card type" });
    }

    public void validateBillingAddress(Order order, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "billToFirstname", "required", new String[] { "Billing Info: first name" });
        ValidationUtils.rejectIfEmpty(errors, "billToLastname", "required", new String[] { "Billing Info: last name" });
        errors.setNestedPath("order.billAddress");
        ValidationUtils.invokeValidator(this.addressValidator, order.getBillAddress(), errors);
    }

    public void validateShippingAddress(Order order, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "shipToFirstname", "required", new String[] { "Shipping Info: first name" });
        ValidationUtils.rejectIfEmpty(errors, "shipToLastname", "required", new String[] { "Shipping Info: last name" });
        errors.setNestedPath("order.shipAddress");
        ValidationUtils.invokeValidator(this.addressValidator, order.getShipAddress(), errors);
    }
}
