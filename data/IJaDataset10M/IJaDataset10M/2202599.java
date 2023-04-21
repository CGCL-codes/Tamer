package com.googlecode.yoohoo.xmppcore.protocol.stanza.error;

import com.googlecode.yoohoo.xmppcore.protocol.XmlLangText;

public class JidMalformed extends StanzaError {

    public JidMalformed() {
        this(null);
    }

    public JidMalformed(String text) {
        this(text, null);
    }

    public JidMalformed(String text, String xmlLang) {
        super(ErrorType.MODIFY, "jid-malformed", text == null ? null : new XmlLangText(text, xmlLang));
    }
}
