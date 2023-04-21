package com.jedifact.syntax;

import static com.jedifact.common.EdifactLogger.parse;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import com.jedifact.token.Segment;
import com.jedifact.util.Validate;

public class EdifactDatatype extends EdifactObject {

    protected static String[] NO_ERRORS = new String[0];

    private Datatype datatype;

    public EdifactDatatype(String type, int min, int max) {
        super(type);
        Validate.notNull(type);
        this.minimumCardinality = min;
        this.maximumCardinality = max;
        this.datatype = createDatatype(type);
    }

    private Datatype createDatatype(String type) {
        if ("N".equals(type)) return new N();
        if ("AN".equals(type)) return new AN();
        if ("A".equals(type)) return new A();
        throw new IllegalArgumentException("Not a legal datatype: '" + type + "'");
    }

    public EdifactDatatype copy() {
        return new EdifactDatatype(datatype.toString(), min(), max());
    }

    @Override
    public void onInit() {
    }

    public void validate(Segment segment, int componentNumber, int repetitionNumber, int dataelementNumber) {
        String value = segment.getValue(componentNumber, repetitionNumber, dataelementNumber);
        parse.debug("Validating value=\"" + value + "\" complies with " + this);
        String[] lengthErrors = datatype.validateLength(segment, value);
        String[] patternErrors = datatype.validatePattern(segment, value);
        for (String err : lengthErrors) {
            error(err, segment, componentNumber, repetitionNumber, dataelementNumber);
        }
        for (String err : patternErrors) {
            error(err, segment, componentNumber, repetitionNumber, dataelementNumber);
        }
        if (lengthErrors.length == 0 && patternErrors.length == 0) {
            parse.debug("Validation OK");
        }
    }

    public String toString() {
        return datatype + "(" + min() + ".." + max() + ")";
    }

    abstract class Datatype {

        protected Pattern pattern;

        Datatype(String pattern) {
            this.pattern = Pattern.compile(pattern);
        }

        public String[] validatePattern(Segment segment, String value) {
            if (!pattern.matcher(value).matches()) {
                return new String[] { "37" };
            } else {
                return NO_ERRORS;
            }
        }

        public String[] validateLength(Segment segment, String value) {
            int length = length(segment, value);
            if (length < minimumCardinality) {
                return new String[] { "40" };
            } else if (length > maximumCardinality) {
                return new String[] { "39" };
            } else {
                return NO_ERRORS;
            }
        }

        abstract int length(Segment segment, String value);

        public String toString() {
            return getClass().getSimpleName();
        }
    }

    /**
	 * 
	 *
	 */
    class N extends Datatype {

        private Map<Character, Pattern> patterns = new HashMap<Character, Pattern>();

        N() {
            super("(\\-)?([0-9])*(\\,[0-9]+)?");
        }

        @Override
        public String[] validatePattern(Segment segment, String value) {
            char decnot = segment.getServiceAdvice().getDecimalNotation();
            Pattern pattern = patterns.get(Character.valueOf(decnot));
            if (pattern == null) {
                pattern = Pattern.compile("(\\-)?([0-9])*(\\" + new String(new char[] { decnot }) + "[0-9]+)?");
                patterns.put(Character.valueOf(decnot), pattern);
            }
            super.pattern = pattern;
            String[] otherErrors = super.validatePattern(segment, value);
            if (otherErrors == NO_ERRORS) {
                for (int i = 0; i < value.length(); i++) {
                    if (value.charAt(i) == decnot) {
                        if (i == 0 || (i == 1 && value.startsWith("-"))) {
                            return new String[] { "38" };
                        }
                    }
                }
            }
            return otherErrors;
        }

        @Override
        int length(Segment segment, String value) {
            int length = value.length();
            char decnot = segment.getServiceAdvice().getDecimalNotation();
            for (int i = 0; i < value.length(); i++) {
                if (value.charAt(i) == decnot) {
                    length--;
                }
            }
            if (value.startsWith("-")) {
                length--;
            }
            return length;
        }
    }

    /**
	 * 
	 *
	 */
    class A extends Datatype {

        A() {
            super("[\\x20-\\x2F\\x3A-\\x7E \\xA0-\\xFF]*");
        }

        @Override
        int length(Segment segment, String value) {
            int length = value.length();
            char relnot = segment.getServiceAdvice().getReleaseIndicator();
            boolean releaseOn = false;
            for (int i = 0; i < value.length(); i++) {
                if (value.charAt(i) == relnot) {
                    if (!releaseOn) {
                        length--;
                        releaseOn = true;
                    }
                } else {
                    releaseOn = false;
                }
            }
            return length;
        }
    }

    /**
	 * 
	 *
	 */
    class AN extends Datatype {

        AN() {
            super("[\\x20-\\x7E\\xA0-\\xFF]*");
        }

        @Override
        int length(Segment segment, String value) {
            int length = value.length();
            char relnot = segment.getServiceAdvice().getReleaseIndicator();
            boolean releaseOn = false;
            for (int i = 0; i < value.length(); i++) {
                if (value.charAt(i) == relnot) {
                    if (!releaseOn) {
                        length--;
                        releaseOn = true;
                    }
                } else {
                    releaseOn = false;
                }
            }
            return length;
        }
    }
}
