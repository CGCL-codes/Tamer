package net.woodstock.rockapi.validation;

public interface Validator {

    String REGEX_EMAIL = "[a-zA-Z0-9\\.-_]{2,}@[a-zA-Z0-9\\.-]{2,}\\.[a-zA-Z0-9\\.-]{2,4}";

    String REGEX_URL_WEB = "(http|https|ftp|ftps)://[a-zA-Z0-9\\.-_]{2,}\\.[a-zA-Z0-9\\.-_]{2,4}(:[0-9]{2,5})?(/|(/.*)?)";

    ValidationResult validate(ValidationContext context) throws ValidationException;
}
