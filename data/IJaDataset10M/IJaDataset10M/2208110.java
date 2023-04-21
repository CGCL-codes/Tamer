package cn.collin.commons.codec;

/**
 * idea from com.sun.portal.desktop.encode
 * 
 * This class contains a utility method for encoding a xml text string so that
 * it may be parsed
 * 
 * @author collin.code@gmail.com
 * @author Administrator
 * 
 */
public class XMLEncoder {

    /**
	 * escapes xml-reserved characters
	 */
    public String encode(String text) {
        StringBuffer escaped = new StringBuffer();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            switch(c) {
                case '<':
                    escaped.append("&#60;");
                    continue;
                case '>':
                    escaped.append("&#62;");
                    continue;
                case '&':
                    escaped.append("&#38;");
                    continue;
                case '\'':
                    escaped.append("&#39;");
                    continue;
                case '"':
                    escaped.append("&#34;");
                    continue;
                default:
                    escaped.append(c);
                    continue;
            }
        }
        return escaped.toString();
    }
}
