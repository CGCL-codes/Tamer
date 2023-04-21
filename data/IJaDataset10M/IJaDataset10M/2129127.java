package cb.jdynamite.analyser;

import gnu.regexp.*;
import cb.jdynamite.ITemplateDocument;

public class MixedElement implements ITemplateElement {

    private String definition;

    public MixedElement(String content) {
        definition = content;
    }

    public String getValue(ITemplateDocument rootDocument) {
        RE varRegExp = rootDocument.getAnalyser().getVariableRegExp();
        REMatchEnumeration matchEnum = varRegExp.getMatchEnumeration(definition);
        if (!matchEnum.hasMoreElements()) {
            return definition;
        }
        StringBuffer result = new StringBuffer();
        int constantStart = 0;
        int endMatch;
        while (matchEnum.hasMoreElements()) {
            REMatch match = matchEnum.nextMatch();
            int matchLength = match.getEndIndex() - match.getStartIndex();
            String key = (match.toString()).substring(1, matchLength - 1);
            result.append(definition.substring(constantStart, match.getStartIndex()));
            String variable = rootDocument.getVariable(key);
            if (variable == null) {
                variable = match.toString();
                System.err.println("Warning : no value set for \"" + match + "\" variable !");
            }
            result.append(variable);
            constantStart = match.getEndIndex();
        }
        result.append(definition.substring(constantStart));
        return result.toString();
    }

    public String getDefinition(int depth) {
        StringBuffer def = new StringBuffer();
        for (int indent = 0; indent < depth; indent++) {
            def.append("   ");
        }
        def.append("MixedElement: ");
        def.append(definition.replace('\n', ' '));
        def.append('\n');
        return def.toString();
    }
}
