package org.exist.http.sleepy.annotations;

import java.util.Map;
import org.exist.xquery.value.StringValue;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import org.exist.xquery.Annotation;
import org.exist.xquery.LiteralValue;
import org.exist.xquery.XPathException;
import org.exist.xquery.value.AtomicValue;
import org.exist.xquery.value.Type;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import static org.exist.http.sleepy.annotations.RESTAnnotation.AnnotationName.path;

/**
 *
 * @author Adam Retter <adam.retter@googlemail.com>
 */
public class PathAnnotation extends AbstractRESTAnnotation {

    private static final char URI_PATH_SEGMENT_DELIMITER = '/';

    /**
     * URI path segment valid characters from RFC 3986
     * 
     * http://labs.apache.org/webarch/uri/rfc/rfc3986.html#collected-abnf
     * 
     * pchar         = unreserved / pct-encoded / sub-delims / ":" / "@"
     * unreserved    = ALPHA / DIGIT / "-" / "." / "_" / "~"
     * pct-encoded   = "%" HEXDIG HEXDIG
     * sub-delims    = "!" / "$" / "&" / "'" / "(" / ")" / "*" / "+" / "," / ";" / "="
     */
    private static final String unreservedRegExp = "[A-Za-z0-9\\-\\._~]";

    private static final String pctEncodedRegExp = "%[A-F0-9]{2}";

    private static final String subDelimsRegExp = "[!\\$&'\\(\\)\\*\\+,;=]";

    private static final String cPcharRegExp = "(" + unreservedRegExp + "|" + pctEncodedRegExp + "|" + subDelimsRegExp + "|[\\:@]" + ")+";

    private static final String ncPcharRegExp = "(?:" + unreservedRegExp + "|" + pctEncodedRegExp + "|" + subDelimsRegExp + "|[\\:@]" + ")+";

    private static final String pathSegmentRegExp = "(?:" + ncPcharRegExp + "|" + fnParamRegExp + ")";

    private static final Pattern ptnPathSegment = Pattern.compile(pathSegmentRegExp);

    private final Matcher mchPathSegment = ptnPathSegment.matcher("");

    private static final String pathRegExp = "^(?:" + URI_PATH_SEGMENT_DELIMITER + "?" + pathSegmentRegExp + ")+$";

    private static final Pattern ptnPath = Pattern.compile(pathRegExp);

    private final Matcher mchPath = ptnPath.matcher("");

    private final PathMatcherAndGroupParamNames pathMatcherAndGroupParamNames;

    private int pathSegmentCount = -1;

    protected PathAnnotation(Annotation annotation) throws RESTAnnotationException {
        super(annotation, path);
        this.pathMatcherAndGroupParamNames = parsePath();
    }

    public boolean matchesPath(final String path) {
        final Matcher m = getPathMatcherAndParamIndicies().getPathMatcher();
        m.reset(path);
        return m.matches();
    }

    public Map<String, StringValue> extractPathParameters(final String path) {
        final Map<String, StringValue> pathParamNameAndValues = new HashMap<String, StringValue>();
        final Matcher m = getPathMatcherAndParamIndicies().getPathMatcher();
        m.reset(path);
        if (m.matches()) {
            for (int i = 1; i <= m.groupCount(); i++) {
                final String paramName = getPathMatcherAndParamIndicies().getFnParamNameForGroup(i);
                final String paramValue = m.group(i);
                pathParamNameAndValues.put(paramName, new StringValue(paramValue));
            }
        }
        return pathParamNameAndValues;
    }

    public int getPathSegmentCount() {
        return pathSegmentCount;
    }

    private PathMatcherAndGroupParamNames getPathMatcherAndParamIndicies() {
        return pathMatcherAndGroupParamNames;
    }

    private void setPathSegmentCount(int pathSegmentCount) {
        this.pathSegmentCount = pathSegmentCount;
    }

    private PathMatcherAndGroupParamNames parsePath() throws RESTAnnotationException {
        final LiteralValue[] annotationValue = getAnnotation().getValue();
        if (annotationValue.length != 1) {
            throw new RESTAnnotationException(RESTAnnotationErrorCodes.RQST0001);
        }
        final AtomicValue pathValue = annotationValue[0].getValue();
        if (pathValue.getType() != Type.STRING) {
            throw new RESTAnnotationException(RESTAnnotationErrorCodes.RQST0002);
        }
        try {
            final String pathStr = pathValue.getStringValue();
            if (pathStr.isEmpty()) {
                throw new RESTAnnotationException(RESTAnnotationErrorCodes.RQST0003);
            }
            mchPath.reset(pathStr);
            if (!mchPath.matches()) {
                throw new RESTAnnotationException(RESTAnnotationErrorCodes.RQST0004);
            }
            final StringBuilder thisPathExprRegExp = new StringBuilder();
            final List<String> pathFnParams = new ArrayList<String>();
            mchPathSegment.reset(pathStr);
            int segmentCount = 0;
            final Map<Integer, String> groupParamNames = new HashMap<Integer, String>();
            int groupCount = 0;
            while (mchPathSegment.find()) {
                final String pathSegment = pathStr.substring(mchPathSegment.start(), mchPathSegment.end());
                mtcFnParameter.reset(pathSegment);
                thisPathExprRegExp.append(URI_PATH_SEGMENT_DELIMITER);
                if (mtcFnParameter.matches()) {
                    final String fnParamName = mtcFnParameter.replaceFirst("$1");
                    pathFnParams.add(fnParamName);
                    thisPathExprRegExp.append("(");
                    thisPathExprRegExp.append(ncPcharRegExp);
                    thisPathExprRegExp.append(")");
                    groupParamNames.put(++groupCount, fnParamName);
                } else {
                    thisPathExprRegExp.append("(?:");
                    thisPathExprRegExp.append(Pattern.quote(pathSegment));
                    thisPathExprRegExp.append(")");
                }
                segmentCount++;
            }
            setPathSegmentCount(segmentCount);
            checkFnDeclaresParameters(getAnnotation().getFunctionSignature(), pathFnParams);
            final Pattern ptnThisPath = Pattern.compile(thisPathExprRegExp.toString());
            final Matcher mtcThisPath = ptnThisPath.matcher("");
            return new PathMatcherAndGroupParamNames(mtcThisPath, groupParamNames);
        } catch (XPathException xpe) {
            throw new RESTAnnotationException(RESTAnnotationErrorCodes.RQST0004, xpe);
        }
    }

    private class PathMatcherAndGroupParamNames {

        final Matcher pathMatcher;

        final Map<Integer, String> groupParamNames;

        public PathMatcherAndGroupParamNames(Matcher pathMatcher, Map<Integer, String> groupParamNames) {
            this.pathMatcher = pathMatcher;
            this.groupParamNames = groupParamNames;
        }

        public Matcher getPathMatcher() {
            return pathMatcher;
        }

        public String getFnParamNameForGroup(int groupId) {
            return groupParamNames.get(groupId);
        }
    }
}
