package net.asfun.jangod.lib.tag;

import java.io.IOException;
import net.asfun.jangod.base.ResourceManager;
import net.asfun.jangod.interpret.InterpretException;
import net.asfun.jangod.interpret.JangodInterpreter;
import net.asfun.jangod.lib.Tag;
import net.asfun.jangod.tree.Node;
import net.asfun.jangod.tree.NodeList;
import net.asfun.jangod.util.HelperStringTokenizer;

/**
 * {% include 'sidebar.html' %}
 * {% include var_fileName %}
 * @author anysome
 *
 */
public class IncludeTag implements Tag {

    final String TAGNAME = "include";

    @Override
    public String interpreter(NodeList carries, String helpers, JangodInterpreter interpreter) throws InterpretException {
        String[] helper = new HelperStringTokenizer(helpers).allTokens();
        if (helper.length != 1) {
            throw new InterpretException("Tag 'include' expects 1 helper >>> " + helper.length);
        }
        String templateFile = interpreter.resolveString(helper[0]);
        try {
            String fullName = ResourceManager.getFullName(templateFile, interpreter.getWorkspace(), interpreter.getConfiguration().getWorkspace());
            Node node = interpreter.getApplication().getParseResult(fullName, interpreter.getConfiguration().getEncoding());
            JangodInterpreter child = interpreter.clone();
            child.assignRuntimeScope(JangodInterpreter.INSERT_FLAG, true, 1);
            return child.render(node);
        } catch (IOException e) {
            throw new InterpretException(e.getMessage());
        }
    }

    @Override
    public String getEndTagName() {
        return null;
    }

    @Override
    public String getName() {
        return TAGNAME;
    }
}
