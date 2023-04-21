package rcpforms.generators;

import java.util.*;
import net.sf.rcpforms.widget.generator.model.*;
import net.sf.rcpforms.widgetwrapper.wrapper.event.*;

public class ColumnItemsTemplate extends net.sf.rcpforms.widget.generator.BaseGeneratorTemplate {

    protected static String nl;

    public static synchronized ColumnItemsTemplate create(String lineSeparator) {
        nl = lineSeparator;
        ColumnItemsTemplate result = new ColumnItemsTemplate();
        nl = null;
        return result;
    }

    public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;

    protected final String TEXT_1 = "package ";

    protected final String TEXT_2 = ";" + NL + " " + NL + "import net.sf.rcpforms.widgetwrapper.wrapper.event.*;" + NL + "import org.eclipse.swt.SWT;" + NL + "import ";

    protected final String TEXT_3 = ";";

    protected final String TEXT_4 = NL + "import ";

    protected final String TEXT_5 = ";";

    protected final String TEXT_6 = NL + NL + NL + "/**" + NL + " * <h3 style='color: #D22'><span style='font-size: 18pt'>NON-API</span>, not intended to user or subclassed!</h3> " + NL + " * <p>" + NL + " * <ul><li><i>This class is generated, do not alter it manually!!!</i></li></ul>" + NL + " * <p>" + NL + " */" + NL + "public abstract class ";

    protected final String TEXT_7 = NL + "{" + NL + "" + NL + "" + NL + "\tpublic ";

    protected final String TEXT_8 = "() {" + NL + "\t   super();" + NL + "\t}" + NL + "\t" + NL + "\tpublic abstract ";

    protected final String TEXT_9 = " ";

    protected final String TEXT_10 = ";" + NL + "\t" + NL + "\t" + NL + "\t";

    protected final String TEXT_11 = NL + "\t" + NL + "\t";

    protected final String TEXT_12 = " ";

    protected final String TEXT_13 = " ";

    protected final String TEXT_14 = "( ";

    protected final String TEXT_15 = " ) {" + NL + "\t\t";

    protected final String TEXT_16 = " ";

    protected final String TEXT_17 = ".";

    protected final String TEXT_18 = "( ";

    protected final String TEXT_19 = " );" + NL + "\t}" + NL + "\t";

    protected final String TEXT_20 = NL + "}" + NL + " ";

    protected final String TEXT_21 = NL;

    public String generate(Object argument) {
        final StringBuffer stringBuffer = new StringBuffer();
        AClass model = (AClass) argument;
        stringBuffer.append(TEXT_1);
        stringBuffer.append(model.packageName);
        stringBuffer.append(TEXT_2);
        stringBuffer.append(model.swtClass.getName());
        stringBuffer.append(TEXT_3);
        for (String imp : model.getAllImports()) {
            stringBuffer.append(TEXT_4);
            stringBuffer.append(imp);
            stringBuffer.append(TEXT_5);
        }
        stringBuffer.append(TEXT_6);
        stringBuffer.append(model.className);
        stringBuffer.append(TEXT_7);
        stringBuffer.append(model.className);
        stringBuffer.append(TEXT_8);
        stringBuffer.append(model.swtClass.getSimpleName());
        stringBuffer.append(TEXT_9);
        stringBuffer.append(model.swtGetter);
        stringBuffer.append(TEXT_10);
        for (AMethod method : model.getMethodPatterns()) {
            stringBuffer.append(TEXT_11);
            stringBuffer.append(method.visibility);
            stringBuffer.append(TEXT_12);
            stringBuffer.append(method.returnType != null ? method.returnType : "void");
            stringBuffer.append(TEXT_13);
            stringBuffer.append(method.name);
            stringBuffer.append(TEXT_14);
            stringBuffer.append(method.argDeclaration);
            stringBuffer.append(TEXT_15);
            stringBuffer.append(method.returnType != null ? "return" : "");
            stringBuffer.append(TEXT_16);
            stringBuffer.append(model.swtGetter);
            stringBuffer.append(TEXT_17);
            stringBuffer.append(method.name);
            stringBuffer.append(TEXT_18);
            stringBuffer.append(method.argPassing);
            stringBuffer.append(TEXT_19);
        }
        stringBuffer.append(TEXT_20);
        stringBuffer.append(TEXT_21);
        return stringBuffer.toString();
    }
}
