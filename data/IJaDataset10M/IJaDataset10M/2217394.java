package org.exist.xquery.functions.system;

import java.io.File;
import java.util.Date;
import org.exist.dom.QName;
import org.exist.memtree.MemTreeBuilder;
import org.exist.xquery.Cardinality;
import org.exist.xquery.FunctionSignature;
import org.exist.xquery.XPathException;
import org.exist.xquery.XQueryContext;
import org.exist.xquery.value.DateTimeValue;
import org.exist.xquery.value.FunctionParameterSequenceType;
import org.exist.xquery.value.NodeValue;
import org.exist.xquery.value.Sequence;
import org.exist.xquery.value.SequenceType;
import org.exist.xquery.value.Type;

/**
 * @author Evgeny Gazdovsky <gazdovsky@gmail.com>
 */
public class GetLibInfoFunction extends LibFunction {

    public static final FunctionSignature signatures[] = { new FunctionSignature(new QName("get-lib-info", SystemModule.NAMESPACE_URI, SystemModule.PREFIX), "Return name and size requested lib file from exist's libs", new SequenceType[] { new FunctionParameterSequenceType("name", Type.STRING, Cardinality.EXACTLY_ONE, "The name of the lib (jar file) from '$EXIST_HOME', '$EXIST_HOME/lib/core', " + "'$EXIST_HOME/lib/optional' and '$EXIST_HOME/lib/user' or " + "'WEB-INF/lib' (if eXist is worrking in servlet container environment). " + "The name can contain mask kind of 'name-%latest%.jar' for getting latest version of lib.") }, new FunctionParameterSequenceType("result", Type.NODE, Cardinality.ZERO_OR_ONE, "Info about lib: szie, last modified time")) };

    public GetLibInfoFunction(XQueryContext context, FunctionSignature signature) {
        super(context, signature);
    }

    public Sequence eval(Sequence[] args, Sequence contextSequence) throws XPathException {
        Sequence result = Sequence.EMPTY_SEQUENCE;
        String name = args[0].itemAt(0).getStringValue();
        File lib = getLib(name);
        if (lib != null) {
            MemTreeBuilder builder = context.getDocumentBuilder();
            builder.startDocument();
            builder.startElement(new QName("lib", null, null), null);
            builder.addAttribute(new QName("name", null, null), lib.getName());
            Long sizeLong = lib.length();
            String sizeString = Long.toString(sizeLong);
            builder.addAttribute(new QName("size", null, null), sizeString);
            builder.addAttribute(new QName("modified", null, null), new DateTimeValue(new Date(lib.lastModified())).getStringValue());
            builder.endElement();
            result = (NodeValue) builder.getDocument().getDocumentElement();
        }
        return result;
    }
}
