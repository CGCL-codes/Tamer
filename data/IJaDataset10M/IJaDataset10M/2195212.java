package edu.gsbme.wasabi.UI.Validation.Message;

import org.eclipse.jface.dialogs.IMessageProvider;

public class IllegalCharacters extends MessageTemplate {

    public IllegalCharacters() {
        key = "IllegalCharacters";
        message = "Illegal Characters. Alphabets and Numbers only";
        type = IMessageProvider.ERROR;
    }
}
