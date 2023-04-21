package com.googlecode.xmpplib;

import java.io.InputStream;
import java.io.Reader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import com.googlecode.xmpplib.provider.AuthenticationController;
import com.googlecode.xmpplib.provider.MessageController;
import com.googlecode.xmpplib.provider.RosterController;
import com.googlecode.xmpplib.provider.impl.SimpleAuthenticationController;
import com.googlecode.xmpplib.provider.impl.SimpleMessageController;
import com.googlecode.xmpplib.provider.impl.SimpleRosterController;

public abstract class XmppFactory {

    public abstract XmlPullParser createXmlPullParser(InputStream inputStream) throws XmlPullParserException;

    public abstract XmlPullParser createXmlPullParser(Reader reader) throws XmlPullParserException;

    public XmlPullParser resetXmlPullParser(XmlPullParser oldXmlPullParser, InputStream inputStream) throws XmlPullParserException {
        return createXmlPullParser(inputStream);
    }

    public XmlPullParser resetXmlPullParser(XmlPullParser oldXmlPullParser, Reader reader) throws XmlPullParserException {
        return createXmlPullParser(reader);
    }

    public AuthenticationController createAuthenticationController() {
        return new SimpleAuthenticationController();
    }

    public RosterController createRosterController() {
        return new SimpleRosterController();
    }

    public MessageController createMessageController() {
        return new SimpleMessageController();
    }
}
