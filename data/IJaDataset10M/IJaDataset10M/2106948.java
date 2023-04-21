package es.nom.morenojuarez.modulipse.core.lang.ast;

import static es.nom.morenojuarez.modulipse.core.lang.Modula2TokenTypes.COMMA;
import static es.nom.morenojuarez.modulipse.core.lang.Modula2TokenTypes.ELSE;
import static es.nom.morenojuarez.modulipse.core.lang.Modula2TokenTypes.END;
import static es.nom.morenojuarez.modulipse.core.lang.Modula2TokenTypes.PIPE;
import java.util.ArrayList;
import java.util.List;
import antlr.collections.AST;
import es.nom.morenojuarez.modulipse.core.lang.Modula2AST;
import es.nom.morenojuarez.modulipse.core.lang.Modula2TokenTypes;

/**
 * 
 */
public class Case implements Node {

    private List<CaseLabel> caseLabels = new ArrayList<CaseLabel>();

    private List<Statement> statements = new ArrayList<Statement>();

    public static Case create(Modula2AST ast) {
        if (ast != null) {
            Case node = new Case();
            AST currentAST = ast;
            CaseLabel label;
            label = CaseLabel.create((Modula2AST) currentAST);
            node.getCaseLabels().add(label);
            currentAST = label.getLastASTNode().getNextSibling();
            while (currentAST.getType() == COMMA) {
                currentAST = currentAST.getNextSibling();
                label = CaseLabel.create((Modula2AST) currentAST);
                node.getCaseLabels().add(label);
                currentAST = label.getLastASTNode().getNextSibling();
            }
            currentAST = currentAST.getNextSibling();
            while (currentAST != null && currentAST.getType() != PIPE && currentAST.getType() != ELSE && currentAST.getType() != END) {
                node.getStatements().add(Statement.create((Modula2AST) currentAST));
                currentAST = ((Modula2AST) currentAST).findFirstSiblingOfType(Modula2TokenTypes.SEMICOLON);
                if (currentAST != null) {
                    currentAST = currentAST.getNextSibling();
                }
            }
            return node;
        }
        return null;
    }

    public List<Statement> getStatements() {
        return statements;
    }

    public List<CaseLabel> getCaseLabels() {
        return caseLabels;
    }

    public String toFormattedString() {
        final String statementSeparator = ";\n";
        StringBuffer formattedString = new StringBuffer(getCaseLabels().get(0).toFormattedString());
        for (int i = 1; i < getCaseLabels().size(); i++) {
            formattedString.append(", " + getCaseLabels().get(i));
        }
        formattedString.append(" : ");
        for (Statement statement : getStatements()) {
            formattedString.append(statement.toFormattedString() + statementSeparator);
        }
        int stringSize = formattedString.length();
        formattedString.delete(stringSize - statementSeparator.length(), stringSize);
        return formattedString.toString();
    }
}
