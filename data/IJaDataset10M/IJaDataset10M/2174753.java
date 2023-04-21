package openvend.xml;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import openvend.main.OvLog;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.logging.Log;

/**
 * Contains the data of a XML processing instruction.<p/>
 * 
 * @author Thomas Weckert
 * @version $Revision: 1.5 $
 * @since 1.0
 */
public class OvXmlProcessingInstruction {

    private static Log log = OvLog.getLog(OvXmlProcessingInstruction.class);

    public static final String PI_ATTRIB_HREF = "href";

    public static final String PI_ATTRIB_TYPE = "type";

    public static final String PI_ATTRIB_CHARSET = "charset";

    private String target;

    private Set metaData;

    private Map params;

    public OvXmlProcessingInstruction(String target, String data) throws Exception {
        super();
        this.target = target;
        this.metaData = new HashSet();
        this.params = new HashMap();
        parse(data);
    }

    protected void parse(String inputData) throws Exception {
        if (StringUtils.isEmpty(inputData)) {
            return;
        }
        inputData = inputData.trim();
        char[] chars = inputData.toCharArray();
        char quoteChar = '\"';
        char previousChar = chars[0];
        char currentChar = chars[0];
        StrBuilder name = new StrBuilder();
        StrBuilder value = new StrBuilder();
        boolean inName = true;
        boolean inValue = false;
        for (int i = 0; i < chars.length; i++) {
            currentChar = chars[i];
            if (currentChar == '=') {
                if (isWhite(previousChar)) {
                    String errorMessage = "Error parsing XML processing instruction with target '" + target + "': illegal whitespace character at position " + i;
                    if (log.isErrorEnabled()) {
                        log.error(errorMessage);
                    }
                    throw new Exception(errorMessage);
                }
                previousChar = currentChar;
                continue;
            }
            if (isQuote(currentChar)) {
                if (inName) {
                    inName = false;
                    inValue = true;
                    quoteChar = currentChar;
                    previousChar = currentChar;
                    continue;
                }
                if (inValue) {
                    if (currentChar != quoteChar) {
                        value.append(currentChar);
                        previousChar = currentChar;
                        continue;
                    } else {
                        inName = true;
                        inValue = false;
                        params.put(name.toString(), value.toString());
                        name = new StrBuilder();
                        value = new StrBuilder();
                        previousChar = currentChar;
                        continue;
                    }
                }
            }
            if (isWhite(currentChar)) {
                if (inName) {
                    if (previousChar == '=') {
                        String errorMessage = "Error parsing XML processing instruction with target '" + target + "': illegal whitespace character at position " + i;
                        if (log.isErrorEnabled()) {
                            log.error(errorMessage);
                        }
                        throw new Exception(errorMessage);
                    }
                    if (!isWhite(previousChar) && !isQuote(previousChar)) {
                        metaData.add(name.toString());
                        name = new StrBuilder();
                    }
                    previousChar = currentChar;
                    continue;
                }
                if (inValue) {
                    value.append(currentChar);
                    previousChar = currentChar;
                    continue;
                }
            }
            if (!isWhite(currentChar)) {
                if (inName) {
                    if (isQuote(previousChar)) {
                        String errorMessage = "Error parsing XML processing instruction with target '" + target + "': whitespace character expected at position " + i;
                        if (log.isErrorEnabled()) {
                            log.error(errorMessage);
                        }
                        throw new Exception(errorMessage);
                    }
                    name.append(currentChar);
                    previousChar = currentChar;
                    continue;
                }
                if (inValue) {
                    value.append(currentChar);
                    previousChar = currentChar;
                    continue;
                }
            }
        }
        if (inName && name.length() > 0) {
            metaData.add(name.toString());
        }
    }

    protected boolean isQuote(char c) {
        return (c == '\"' || c == '\'');
    }

    protected boolean isWhite(char c) {
        return Character.isWhitespace(c);
    }

    public String getTarget() {
        return target;
    }

    public Set getMetaData() {
        return metaData;
    }

    public Map getParams() {
        return params;
    }
}
