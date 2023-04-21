package com.googlecode.yoohoo.xmppcore.utils;

import com.googlecode.yoohoo.xmppcore.protocol.IError;
import com.googlecode.yoohoo.xmppcore.protocol.XmlLangText;
import com.googlecode.yoohoo.xmppcore.protocol.XmppProtocolException;

public abstract class ErrorUtils {

    public static XmppProtocolException createProtocolException(String text, IError error) {
        error.setText(new XmlLangText(text));
        return new XmppProtocolException(error);
    }
}
