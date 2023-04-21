package org.jcryptool.crypto.flexiprovider.operations.xml.keys;

import org.jcryptool.crypto.keys.IKeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jdom.Element;

@SuppressWarnings("serial")
public class KeyElement extends Element {

    public KeyElement(IKeyStoreAlias alias) {
        super("Key");
        setText(alias.getAliasString());
    }

    public KeyElement(Element keyElement) {
        super("Key");
        setText(keyElement.getText());
    }

    public KeyStoreAlias getAlias() {
        return new KeyStoreAlias(getText());
    }
}
