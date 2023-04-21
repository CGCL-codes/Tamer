package org.apache.xmlbeans.impl.regex;

import org.apache.xmlbeans.impl.common.XMLChar;
import java.util.HashMap;
import java.util.Map;

public class SchemaRegularExpression extends RegularExpression {

    private SchemaRegularExpression(String pattern) {
        super(pattern, "X");
    }

    public static RegularExpression forPattern(String s) {
        SchemaRegularExpression tre = (SchemaRegularExpression) knownPatterns.get(s);
        if (tre != null) return tre;
        return new RegularExpression(s, "X");
    }

    static final Map knownPatterns = buildKnownPatternMap();

    private static Map buildKnownPatternMap() {
        Map result = new HashMap();
        result.put("\\c+", new SchemaRegularExpression("\\c+") {

            public boolean matches(String s) {
                return XMLChar.isValidNmtoken(s);
            }
        });
        result.put("\\i\\c*", new SchemaRegularExpression("\\i\\c*") {

            public boolean matches(String s) {
                return XMLChar.isValidName(s);
            }
        });
        result.put("[\\i-[:]][\\c-[:]]*", new SchemaRegularExpression("[\\i-[:]][\\c-[:]]*") {

            public boolean matches(String s) {
                return XMLChar.isValidNCName(s);
            }
        });
        return result;
    }
}
