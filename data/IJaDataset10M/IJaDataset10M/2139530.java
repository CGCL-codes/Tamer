package hudson.zipscript.parser.template.element.lang.variable.adapter;

import hudson.zipscript.parser.Constants;
import hudson.zipscript.parser.context.Context;
import hudson.zipscript.parser.context.ExtendedContext;
import hudson.zipscript.parser.exception.ExecutionException;
import hudson.zipscript.parser.template.data.ParsingSession;
import hudson.zipscript.parser.template.element.Element;
import hudson.zipscript.parser.template.element.lang.variable.format.CustomDateFormatter;
import hudson.zipscript.parser.template.element.lang.variable.format.CustomNumberFormatter;
import hudson.zipscript.parser.template.element.lang.variable.format.DefinedDateFormatter;
import hudson.zipscript.parser.template.element.lang.variable.format.DefinedNumberFormatter;
import hudson.zipscript.parser.template.element.lang.variable.format.Formatter;
import hudson.zipscript.parser.template.element.lang.variable.special.SpecialMethod;
import hudson.zipscript.parser.template.element.lang.variable.special.date.JSDateSpecialMethod;
import hudson.zipscript.parser.template.element.lang.variable.special.date.JSDateTimeSpecialMethod;
import hudson.zipscript.parser.template.element.lang.variable.special.macroinstance.BooleanValueSpecialMethod;
import hudson.zipscript.parser.template.element.lang.variable.special.macroinstance.ObjectValueSpecialMethod;
import hudson.zipscript.parser.template.element.lang.variable.special.map.KeysSpecialMethod;
import hudson.zipscript.parser.template.element.lang.variable.special.map.ValuesSpecialMethod;
import hudson.zipscript.parser.template.element.lang.variable.special.number.CeilingSpecialMethod;
import hudson.zipscript.parser.template.element.lang.variable.special.number.FloorSpecialMethod;
import hudson.zipscript.parser.template.element.lang.variable.special.number.RoundSpecialMethod;
import hudson.zipscript.parser.template.element.lang.variable.special.object.IsBooleanSpecialMethod;
import hudson.zipscript.parser.template.element.lang.variable.special.object.IsDateSpecialMethod;
import hudson.zipscript.parser.template.element.lang.variable.special.object.IsMapSpecialMethod;
import hudson.zipscript.parser.template.element.lang.variable.special.object.IsNumberSpecialMethod;
import hudson.zipscript.parser.template.element.lang.variable.special.object.IsSequenceSpecialMethod;
import hudson.zipscript.parser.template.element.lang.variable.special.object.IsStringSpecialMethod;
import hudson.zipscript.parser.template.element.lang.variable.special.object.IsXMLSpecialMethod;
import hudson.zipscript.parser.template.element.lang.variable.special.object.StringSpecialMethod;
import hudson.zipscript.parser.template.element.lang.variable.special.object.ToIntSpecialMethod;
import hudson.zipscript.parser.template.element.lang.variable.special.object.VoidSpecialMethod;
import hudson.zipscript.parser.template.element.lang.variable.special.sequence.AddFirstSpecialMethod;
import hudson.zipscript.parser.template.element.lang.variable.special.sequence.AddLastSpecialMethod;
import hudson.zipscript.parser.template.element.lang.variable.special.sequence.ContainsSpecialMethod;
import hudson.zipscript.parser.template.element.lang.variable.special.sequence.FirstSpecialMethod;
import hudson.zipscript.parser.template.element.lang.variable.special.sequence.LastSpecialMethod;
import hudson.zipscript.parser.template.element.lang.variable.special.sequence.LengthSpecialMethod;
import hudson.zipscript.parser.template.element.lang.variable.special.string.HTMLSpecialMethod;
import hudson.zipscript.parser.template.element.lang.variable.special.string.HumpbackCaseSpecialMethod;
import hudson.zipscript.parser.template.element.lang.variable.special.string.IsLowerCaseSpecialMethod;
import hudson.zipscript.parser.template.element.lang.variable.special.string.IsUpperCaseSpecialMethod;
import hudson.zipscript.parser.template.element.lang.variable.special.string.JSSpecialMethod;
import hudson.zipscript.parser.template.element.lang.variable.special.string.LPadSpecialMethod;
import hudson.zipscript.parser.template.element.lang.variable.special.string.LowerCaseSpecialMethod;
import hudson.zipscript.parser.template.element.lang.variable.special.string.LowerFirstSpecialMethod;
import hudson.zipscript.parser.template.element.lang.variable.special.string.RPadSpecialMethod;
import hudson.zipscript.parser.template.element.lang.variable.special.string.RTFSpecialMethod;
import hudson.zipscript.parser.template.element.lang.variable.special.string.SplitSpecialMethod;
import hudson.zipscript.parser.template.element.lang.variable.special.string.URLSpecialMethod;
import hudson.zipscript.parser.template.element.lang.variable.special.string.UpperCaseSpecialMethod;
import hudson.zipscript.parser.template.element.lang.variable.special.string.UpperFirstSpecialMethod;
import hudson.zipscript.parser.template.element.lang.variable.special.string.XMLSpecialMethod;
import hudson.zipscript.parser.template.element.lang.variable.special.xml.XPathSpecialMethod;
import hudson.zipscript.parser.template.element.lang.xml.DocumentObjectAdapter;
import hudson.zipscript.parser.template.element.lang.xml.NodeHashAdapter;
import hudson.zipscript.parser.template.element.lang.xml.NodeListSequenceAdapter;
import hudson.zipscript.parser.template.element.lang.xml.NodeObjectAdapter;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class StandardVariableAdapterFactory implements VariableAdapterFactory {

    public HashAdapter getHashAdapter(Object map) {
        if (map instanceof Map) return JavaHashAdapter.INSTANCE; else if (map instanceof Context) return ContextMapAdapter.INSTANCE; else if (map instanceof Node) {
            return NodeHashAdapter.INSTANCE;
        }
        return null;
    }

    public ObjectAdapter getObjectAdapter(Object object) {
        if (object instanceof Document) return DocumentObjectAdapter.INSTANCE; else if (object instanceof Node) return NodeObjectAdapter.INSTANCE;
        return new JavaObjectAdapter();
    }

    public SequenceAdapter getSequenceAdapter(Object sequence) {
        if (sequence instanceof Object[]) return ObjectArrayAdapter.INSTANCE; else if (sequence instanceof List) return ListAdapter.INSTANCE; else if (sequence instanceof Set) return SetAdapter.INSTANCE; else if (sequence instanceof Collection) return CollectionAdapter.INSTANCE; else if (sequence instanceof Iterator) return IteratorAdapter.INSTANCE; else if (sequence instanceof NodeList) return NodeListSequenceAdapter.INSTANCE; else return null;
    }

    public SpecialMethod getSpecialMethod(String method, Element[] parameters, ParsingSession parsingSession, Element element) {
        try {
            if (method.equals("void")) return VoidSpecialMethod.INSTANCE; else if (method.equals("isDate")) return IsDateSpecialMethod.INSTANCE; else if (method.equals("isNumber")) return IsNumberSpecialMethod.INSTANCE; else if (method.equals("isString")) return IsStringSpecialMethod.INSTANCE; else if (method.equals("isBoolean")) return IsBooleanSpecialMethod.INSTANCE; else if (method.equals("isSequence")) return IsSequenceSpecialMethod.INSTANCE; else if (method.equals("isHash")) return IsMapSpecialMethod.INSTANCE; else if (method.equals("isXML")) return IsXMLSpecialMethod.INSTANCE; else if (method.equals("objectValue")) return new ObjectValueSpecialMethod(element); else if (method.equals("booleanValue")) return new BooleanValueSpecialMethod(); else if (method.equals("toInt")) return ToIntSpecialMethod.INSTANCE;
            SpecialMethod sm = getStringEscapingStringMethod(method, parsingSession);
            if (null != sm) return sm; else if (method.equals("leftPad")) return new LPadSpecialMethod(parameters); else if (method.equals("rightPad")) return new RPadSpecialMethod(parameters); else if (method.equals("split")) return new SplitSpecialMethod(parameters); else if (method.equals("isLowerCase")) return IsLowerCaseSpecialMethod.INSTANCE; else if (method.equals("isUpperCase")) return IsUpperCaseSpecialMethod.INSTANCE; else if (method.equals("round")) return RoundSpecialMethod.INSTANCE; else if (method.equals("ceiling")) return CeilingSpecialMethod.INSTANCE; else if (method.equals("floor")) return FloorSpecialMethod.INSTANCE; else if (method.equals("jsDate")) return JSDateSpecialMethod.INSTANCE; else if (method.equals("jsDateTime")) return JSDateTimeSpecialMethod.INSTANCE; else if (method.equals("xpath")) return new XPathSpecialMethod(parameters); else if (method.equals("size")) return new LengthSpecialMethod(); else if (method.equals("first")) return new FirstSpecialMethod(); else if (method.equals("addFirst")) return new AddFirstSpecialMethod(parameters); else if (method.equals("last")) return new LastSpecialMethod(); else if (method.equals("addLast")) return new AddLastSpecialMethod(parameters); else if (method.equals("contains")) return new ContainsSpecialMethod(parameters);
            if (method.equals("keys")) return new KeysSpecialMethod(); else if (method.equals("values")) return new ValuesSpecialMethod();
            return null;
        } catch (ExecutionException e) {
            e.setElement(element);
            throw e;
        }
    }

    public SpecialMethod getStringEscapingStringMethod(String method, ParsingSession session) {
        if (method.equals("upperFirst")) return getStringSpecialMethod(null, UpperFirstSpecialMethod.INSTANCE); else if (method.equals("lowerFirst")) return getStringSpecialMethod(null, LowerFirstSpecialMethod.INSTANCE); else if (method.equals("lowerCase")) return getStringSpecialMethod(null, LowerCaseSpecialMethod.INSTANCE); else if (method.equals("humpbackCase")) return getStringSpecialMethod(null, HumpbackCaseSpecialMethod.INSTANCE); else if (method.equals("upperCase")) return getStringSpecialMethod(null, UpperCaseSpecialMethod.INSTANCE); else if (method.equals("html")) return getStringSpecialMethod(null, HTMLSpecialMethod.INSTANCE); else if (method.equals("js")) return getStringSpecialMethod(null, JSSpecialMethod.INSTANCE); else if (method.equals("rtf")) return getStringSpecialMethod(null, RTFSpecialMethod.INSTANCE); else if (method.equals("url")) return getStringSpecialMethod(null, new URLSpecialMethod(session)); else if (method.equals("xml")) return getStringSpecialMethod(null, XMLSpecialMethod.INSTANCE); else return null;
    }

    private SpecialMethod getStringSpecialMethod(Object source, SpecialMethod specialMethod) {
        if (source instanceof String) return specialMethod; else return new StringSpecialMethod(specialMethod);
    }

    public String getDefaultGetterMethod(Object obj) {
        return "get";
    }

    public Formatter getFormatter(String format, String formatFunction, Object source, ExtendedContext context) {
        if (source instanceof Date) {
            if (null != format) {
                return new CustomDateFormatter(format, context.getLocale());
            } else {
                return new DefinedDateFormatter(formatFunction, context.getLocale());
            }
        } else if (source instanceof Number) {
            if (null != format) {
                return new CustomNumberFormatter(format, context.getLocale());
            } else {
                return new DefinedNumberFormatter(formatFunction, context.getLocale());
            }
        }
        return null;
    }

    private static String[] reservedContextAttributes = new String[] { Constants.GLOBAL, Constants.RESOURCE, Constants.UNIQUE_ID };

    public String[] getReservedContextAttributes() {
        return reservedContextAttributes;
    }
}
