package code.google.jcustomize;

import java.io.File;
import javax.servlet.jsp.el.ELException;
import javax.servlet.jsp.el.ExpressionEvaluator;
import javax.servlet.jsp.el.FunctionMapper;
import javax.servlet.jsp.el.VariableResolver;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

class MkdirAction extends AbstractAction {

    private String path;

    boolean perform(ConfigSource cs, ExpressionEvaluator ee, VariableResolver vr, FunctionMapper fm) {
        if (path == null) return false;
        try {
            String pathVal = path.indexOf("${") < 0 ? path : (String) ee.evaluate(path, String.class, vr, fm);
            File f = new File(pathVal);
            return f.mkdirs();
        } catch (ELException e) {
            e.printStackTrace();
        }
        return false;
    }

    void parse(Element action) {
        super.parse(action);
        for (Node n = action.getFirstChild(); n != null; n = n.getNextSibling()) {
            if (n.getNodeType() != Node.TEXT_NODE) continue;
            String s = n.getNodeValue();
            if (s == null || (s = s.trim()).length() == 0) continue;
            path = s;
            break;
        }
    }

    boolean skip(ConfigSource cs, ExpressionEvaluator ee, VariableResolver vr, FunctionMapper fm) {
        return super.skip(cs, ee, vr, fm) || path == null || path.length() == 0;
    }
}
