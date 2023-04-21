package net.jfipa.xml.fipa;

import net.jfipa.xml.ParserA;
import net.jfipa.xml.Token;
import net.jfipa.xml.XMLEntityA;

public class SimpleMessageElement extends XMLEntityA {

    protected StringBuffer _tagName;

    protected StringBuffer _hrefParameter;

    protected StringBuffer _dataValue;

    public SimpleMessageElement() {
        setEmpty();
        _tagName = new StringBuffer();
        _hrefParameter = new StringBuffer();
        _dataValue = new StringBuffer();
    }

    public void setHrefParameter(String hrefParameter) {
        _hrefParameter.setLength(0);
        _hrefParameter.append(hrefParameter);
    }

    public String getHrefParameter() {
        return _hrefParameter.toString();
    }

    public void setDataValue(String dataValue) {
        _dataValue.setLength(0);
        _dataValue.append(dataValue);
    }

    public String getDataValue() {
        return _dataValue.toString();
    }

    public void parse(ParserA parser) {
        try {
            System.out.println(" ==> SimpleMessageElement.parse()");
            String tagName = _tagName.toString();
            ParserA currentParser = parser;
            currentParser.accept(Token.IDENT, tagName);
            if (currentParser.getCurrentKind() == Token.IDENT) {
                String[] currentParamPair = currentParser.parseTagParam(Token.IDENT);
                if (currentParamPair[0].equals("href")) {
                    _hrefParameter.append(currentParamPair[1]);
                } else {
                    System.err.println("SimpleMessageElement.parse() - expected href=\" ... \"");
                }
            }
            currentParser.acceptStop(Token.GT);
            currentParser.acceptTagData(_dataValue);
            currentParser.accept(Token.CTSTART);
            currentParser.accept(Token.IDENT, tagName);
            currentParser.accept(Token.GT);
            setNotEmpty();
            System.out.println(" <== SimpleMessageElement.parse()");
        } catch (Exception e) {
            System.err.println("SimpleMessageElement.parse() failed, exception: " + e.toString());
        }
    }

    protected final void setTagName(String value) {
        _tagName.append(value);
    }

    public final StringBuffer getTagName() {
        return _tagName;
    }

    public final void clear() {
        _hrefParameter.setLength(0);
        _dataValue.setLength(0);
        setEmpty();
    }

    public final String toString() {
        return createXML("");
    }

    public String createXML(String prefixSpace) {
        if (isEmpty() != true) {
            if (_xmlStringBuffer != null) {
                _xmlStringBuffer.setLength(0);
            } else {
                _xmlStringBuffer = new StringBuffer();
            }
            String newSpace = prefixSpace + " ";
            if (_tagName != null && _tagName.length() > 0) {
                _xmlStringBuffer.append(prefixSpace);
                _xmlStringBuffer.append("<");
                _xmlStringBuffer.append(_tagName.toString());
                if (_hrefParameter != null && _hrefParameter.length() > 0) {
                    _xmlStringBuffer.append(" href=\"");
                    _xmlStringBuffer.append(_hrefParameter.toString());
                    _xmlStringBuffer.append("\" ");
                }
                _xmlStringBuffer.append(">\n");
                _xmlStringBuffer.append(newSpace);
                _xmlStringBuffer.append(_dataValue);
                _xmlStringBuffer.append("\n");
                _xmlStringBuffer.append(prefixSpace);
                _xmlStringBuffer.append("</");
                _xmlStringBuffer.append(_tagName);
                _xmlStringBuffer.append(">\n");
                return _xmlStringBuffer.toString();
            }
        }
        return "";
    }
}
