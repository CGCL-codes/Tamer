package xdoclet.gwt.tagshandler;

import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;
import xdoclet.XDocletException;
import xdoclet.tagshandler.AbstractProgramElementTagsHandler;
import xjavadoc.XParameter;

public class JNIParameterTagsHandler extends AbstractProgramElementTagsHandler {

    public JNIParameterTagsHandler() {
    }

    public String JNIParameterPrototype(Properties attributes) throws XDocletException {
        Collection parameters;
        parameters = getCurrentMethod().getParameters();
        StringBuffer sbuf = new StringBuffer();
        String type = null;
        for (Iterator i = parameters.iterator(); i.hasNext(); ) {
            XParameter parameter = (XParameter) i.next();
            type = getMethodParamTypeFor(parameter);
            if (type == null) throw new XDocletException("FATAL:" + parameter.getName());
            sbuf.append(convertTypeToJNI(type));
        }
        String result = sbuf.toString();
        return result;
    }

    public static String toJavaScriptType(Properties attributes) throws XDocletException {
        String typeName = attributes.getProperty("type", "").trim();
        return typeName.replace('.', '_');
    }

    public static String convertTypeToJNI(String type) {
        String indexer = "";
        int p = type.indexOf("[");
        if (p >= 0) {
            indexer = type.substring(p).replace(']', ' ').trim();
            type = type.substring(0, p);
        }
        if (type.equals("boolean")) return indexer + "Z"; else if (type.equals("byte")) return indexer + "B"; else if (type.equals("char")) return indexer + "C"; else if (type.equals("short")) return indexer + "S"; else if (type.equals("int")) return indexer + "I"; else if (type.equals("long")) return indexer + "J"; else if (type.equals("float")) return indexer + "F"; else if (type.equals("double")) return indexer + "D"; else {
            return indexer + "L" + type.replace('.', '/') + ";";
        }
    }

    public static String getMethodParamTypeFor(XParameter param) {
        return param.getType().getQualifiedName() + param.getDimensionAsString();
    }
}
