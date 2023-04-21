package org.databene.benerator.engine.parser.xml;

import java.util.List;
import java.util.Set;
import org.databene.benerator.engine.Statement;
import org.databene.benerator.engine.statement.IfStatement;
import org.databene.benerator.engine.statement.SequentialStatement;
import org.databene.commons.CollectionUtil;
import org.databene.commons.xml.XMLUtil;
import org.databene.script.Expression;
import static org.databene.benerator.engine.DescriptorConstants.*;
import static org.databene.benerator.engine.parser.xml.DescriptorParserUtil.*;
import org.w3c.dom.Element;

/**
 * Parses an &lt;if&gt; element.<br/><br/>
 * Created: 19.02.2010 09:07:51
 * @since 0.6.0
 * @author Volker Bergmann
 */
public class IfParser extends AbstractBeneratorDescriptorParser {

    private static final Set<String> STRICT_CHILDREN = CollectionUtil.toSet(EL_THEN, EL_ELSE, EL_COMMENT);

    public IfParser() {
        super(EL_IF, CollectionUtil.toSet(ATT_TEST), null);
    }

    @Override
    public Statement doParse(Element ifElement, Statement[] parentPath, BeneratorParseContext context) {
        Expression<Boolean> condition = parseBooleanExpressionAttribute(ATT_TEST, ifElement);
        Element[] thenElements = XMLUtil.getChildElements(ifElement, false, "then");
        if (thenElements.length > 1) syntaxError("Multiple <then> elements", ifElement);
        Element thenElement = (thenElements.length == 1 ? thenElements[0] : null);
        Element[] elseElements = XMLUtil.getChildElements(ifElement, false, "else");
        if (elseElements.length > 1) syntaxError("Multiple <else> elements", ifElement);
        Element elseElement = (elseElements.length == 1 ? elseElements[0] : null);
        List<Statement> thenStatements = null;
        List<Statement> elseStatements = null;
        IfStatement ifStatement = new IfStatement(condition);
        Statement[] ifPath = context.createSubPath(parentPath, ifStatement);
        if (elseElement != null) {
            if (thenElement == null) syntaxError("'else' without 'then'", elseElement);
            thenStatements = context.parseChildElementsOf(thenElement, ifPath);
            elseStatements = context.parseChildElementsOf(elseElement, ifPath);
            assertThenElseChildren(ifElement);
        } else if (thenElement != null) {
            thenStatements = context.parseChildElementsOf(thenElement, ifPath);
        } else thenStatements = context.parseChildElementsOf(ifElement, ifPath);
        ifStatement.setThenStatement(new SequentialStatement(thenStatements));
        ifStatement.setElseStatement(new SequentialStatement(elseStatements));
        return ifStatement;
    }

    private static void assertThenElseChildren(Element ifElement) {
        for (Element child : XMLUtil.getChildElements(ifElement)) {
            if (!STRICT_CHILDREN.contains(child.getNodeName())) syntaxError("Illegal child element: ", child);
        }
    }
}
