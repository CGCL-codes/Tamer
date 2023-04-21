package org.exist.xquery.functions;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.exist.dom.QName;
import org.exist.xquery.Cardinality;
import org.exist.xquery.Function;
import org.exist.xquery.FunctionSignature;
import org.exist.xquery.Module;
import org.exist.xquery.XPathException;
import org.exist.xquery.XQueryContext;
import org.exist.xquery.value.BooleanValue;
import org.exist.xquery.value.Item;
import org.exist.xquery.value.Sequence;
import org.exist.xquery.value.SequenceType;
import org.exist.xquery.value.Type;

/**
 * Implements the fn:matches() function.
 * 
 * Based on the jakarta ORO package for regular expression support.
 * 
 * @author Wolfgang Meier (wolfgang@exist-db.org)
 */
public class FunMatches extends Function {

    public static final FunctionSignature signatures[] = { new FunctionSignature(new QName("matches", Module.BUILTIN_FUNCTION_NS), "Returns true if the first argument string matches the regular expression specified " + "by the second argument.", new SequenceType[] { new SequenceType(Type.STRING, Cardinality.ZERO_OR_ONE), new SequenceType(Type.STRING, Cardinality.EXACTLY_ONE) }, new SequenceType(Type.BOOLEAN, Cardinality.ZERO_OR_ONE)), new FunctionSignature(new QName("matches", Module.BUILTIN_FUNCTION_NS), "Returns true if the first argument string matches the regular expression specified " + "by the second argument.", new SequenceType[] { new SequenceType(Type.STRING, Cardinality.ZERO_OR_ONE), new SequenceType(Type.STRING, Cardinality.EXACTLY_ONE), new SequenceType(Type.STRING, Cardinality.EXACTLY_ONE) }, new SequenceType(Type.BOOLEAN, Cardinality.ZERO_OR_ONE)) };

    protected Perl5Compiler compiler = new Perl5Compiler();

    protected Perl5Matcher matcher = new Perl5Matcher();

    protected String prevPattern = null;

    protected Pattern pat = null;

    protected int prevFlags = -1;

    /**
	 * @param context
	 */
    public FunMatches(XQueryContext context, FunctionSignature signature) {
        super(context, signature);
    }

    public Sequence eval(Sequence contextSequence, Item contextItem) throws XPathException {
        Sequence stringArg = getArgument(0).eval(contextSequence, contextItem);
        if (stringArg.getLength() == 0) return Sequence.EMPTY_SEQUENCE;
        String string = stringArg.getStringValue();
        String pattern = getArgument(1).eval(contextSequence, contextItem).getStringValue();
        int flags = 0;
        if (getSignature().getArgumentCount() == 3) flags = parseFlags(getArgument(2).eval(contextSequence, contextItem).getStringValue());
        try {
            if (prevPattern == null || (!pattern.equals(prevPattern)) || flags != prevFlags) pat = compiler.compile(pattern, flags);
            prevPattern = pattern;
            prevFlags = flags;
            if (matcher.matches(string, pat)) return BooleanValue.TRUE; else return BooleanValue.FALSE;
        } catch (MalformedPatternException e) {
            throw new XPathException("Invalid regular expression: " + e.getMessage(), e);
        }
    }

    protected static final int parseFlags(String s) throws XPathException {
        int flags = 0;
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            switch(ch) {
                case 'm':
                    flags |= Perl5Compiler.MULTILINE_MASK;
                    break;
                case 'i':
                    flags |= Perl5Compiler.CASE_INSENSITIVE_MASK;
                    break;
                default:
                    throw new XPathException("Invalid regular expression flag: " + ch);
            }
        }
        return flags;
    }
}
