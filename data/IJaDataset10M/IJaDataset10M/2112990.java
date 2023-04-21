package de.mogwai.common.client.binding.validator;

import de.mogwai.common.client.binding.PropertyAdapter;

public class ValidationError {

    private String message;

    public ValidationError(PropertyAdapter aAdapter, String aMessage) {
        message = aMessage;
    }

    @Override
    public String toString() {
        return message;
    }
}
