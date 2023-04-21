package jwebapp;

class ValidationData {

    private String validationId, type, arguments, errorMessage;

    ValidationData(String validationId, String type, String arguments, String errorMessage) {
        this.validationId = validationId;
        this.type = type;
        this.arguments = arguments;
        this.errorMessage = errorMessage;
    }

    String getValidationId() {
        return validationId;
    }

    String getType() {
        return type;
    }

    String getArguments() {
        return arguments;
    }

    String getErrorMessage() {
        return errorMessage;
    }
}
