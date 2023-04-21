package com.liferay.portlet.journal.util;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.StringPool;
import java.util.Locale;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xml.utils.SAXSourceLocator;
import org.apache.xml.utils.WrappedRuntimeException;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * <a href="JournalXslErrorListener.java.html"><b><i>View Source</i></b></a>
 *
 * @author Raymond Augé
 *
 */
public class JournalXslErrorListener implements ErrorListener {

    public JournalXslErrorListener(long companyId, Locale locale) {
        _companyId = companyId;
        _locale = locale;
    }

    public void error(TransformerException exception) throws TransformerException {
        setLocation(exception);
        throw exception;
    }

    public void fatalError(TransformerException exception) throws TransformerException {
        setLocation(exception);
        throw exception;
    }

    public void warning(TransformerException exception) throws TransformerException {
        setLocation(exception);
        throw exception;
    }

    public void setLocation(Throwable exception) {
        SourceLocator locator = null;
        Throwable cause = exception;
        Throwable rootCause = null;
        while (cause != null) {
            if (cause instanceof SAXParseException) {
                locator = new SAXSourceLocator((SAXParseException) cause);
                rootCause = cause;
            } else if (cause instanceof TransformerException) {
                SourceLocator causeLocator = ((TransformerException) cause).getLocator();
                if (causeLocator != null) {
                    locator = causeLocator;
                    rootCause = cause;
                }
            }
            if (cause instanceof TransformerException) {
                cause = ((TransformerException) cause).getCause();
            } else if (cause instanceof WrappedRuntimeException) {
                cause = ((WrappedRuntimeException) cause).getException();
            } else if (cause instanceof SAXException) {
                cause = ((SAXException) cause).getException();
            } else {
                cause = null;
            }
        }
        _message = rootCause.getMessage();
        if (locator != null) {
            _lineNumber = locator.getLineNumber();
            _columnNumber = locator.getColumnNumber();
            StringBuilder sb = new StringBuilder();
            sb.append(LanguageUtil.get(_companyId, _locale, "line"));
            sb.append(" #");
            sb.append(locator.getLineNumber());
            sb.append("; ");
            sb.append(LanguageUtil.get(_companyId, _locale, "column"));
            sb.append(" #");
            sb.append(locator.getColumnNumber());
            sb.append("; ");
            _location = sb.toString();
        } else {
            _location = StringPool.BLANK;
        }
    }

    public String getLocation() {
        return _location;
    }

    public String getMessage() {
        return _message;
    }

    public String getMessageAndLocation() {
        return _message + " " + _location;
    }

    public int getLineNumber() {
        return _lineNumber;
    }

    public int getColumnNumber() {
        return _columnNumber;
    }

    private long _companyId;

    private Locale _locale;

    private String _location;

    private String _message;

    private int _lineNumber;

    private int _columnNumber;
}
