package org.jmlspecs.jml6.core.test.ast;

import static org.junit.Assert.fail;
import junit.framework.Assert;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.jmlspecs.javacontract.JC;
import org.jmlspecs.javacontract.specAthon1rev.VisibilityModifier;
import org.jmlspecs.jml6.core.ast.JmlAstMatcher;
import org.jmlspecs.jml6.core.ast.JmlClause;
import org.jmlspecs.jml6.core.ast.JmlDivergesClause;
import org.jmlspecs.jml6.core.ast.JmlEnsuresClause;
import org.jmlspecs.jml6.core.ast.JmlMethodSpecification;
import org.jmlspecs.jml6.core.ast.JmlModelProgramClause;
import org.jmlspecs.jml6.core.ast.JmlModifiesClause;
import org.jmlspecs.jml6.core.ast.JmlRequiresClause;
import org.jmlspecs.jml6.core.ast.JmlSignalsClause;
import org.jmlspecs.jml6.core.ast.JmlSignalsOnlyClause;
import org.jmlspecs.jml6.core.ast.JmlSpecCase;
import org.jmlspecs.jml6.core.ast.JmlSpecCaseBody;
import org.jmlspecs.jml6.core.javacontract.InvalidJavaException;
import org.jmlspecs.jml6.core.javacontract.JavaContractFactory;
import org.junit.Before;
import org.junit.Test;

public class JmlAstMatcherTest {

    JmlAstMatcher matcher;

    JavaContractFactory jcf = new JavaContractFactory("Counter");

    @Before
    public void setUp() throws Exception {
        this.matcher = new JmlAstMatcher();
    }

    @Test
    public void testMatchJmlDivergesClauseObject() throws InvalidJavaException {
        JmlDivergesClause jd1 = new JmlDivergesClause(false, null);
        Assert.assertTrue(jd1.subtreeMatch(matcher, jd1));
        Assert.assertFalse(jd1.subtreeMatch(matcher, getJmlDiveresClause()));
        Assert.assertTrue(getJmlDiveresClause().subtreeMatch(matcher, getJmlDiveresClause()));
    }

    @Test
    public void testMatchJmlEnsuresClauseObject() throws InvalidJavaException {
        JmlEnsuresClause c1 = new JmlEnsuresClause(false, null);
        Assert.assertTrue(c1.subtreeMatch(matcher, c1));
        Assert.assertFalse(c1.subtreeMatch(matcher, getJmlEnsuresClause()));
        Assert.assertTrue(getJmlEnsuresClause().subtreeMatch(matcher, getJmlEnsuresClause()));
    }

    @Test
    public void testMatchJmlMethodSpecificationObject() throws InvalidJavaException {
        JmlMethodSpecification jms1 = new JmlMethodSpecification(false, null, null, null);
        JmlSpecCase[] specCase = { getJmlSpecCase() };
        JmlMethodSpecification jms2 = new JmlMethodSpecification(false, specCase, null, null);
        Assert.assertTrue(jms1.subtreeMatch(matcher, jms1));
        Assert.assertFalse(jms1.subtreeMatch(matcher, jms2));
        Assert.assertTrue(jms2.subtreeMatch(matcher, jms2));
    }

    @Test
    public void testMatchJmlModelProgramObject() {
        final String METHOD_DECLARATION_STRING_1 = "public int get() { return 3; }", METHOD_DECLARATION_STRING_2 = "public double get() { return 4; }";
        JmlModelProgramClause jmpc1 = getModelProgramExpression(METHOD_DECLARATION_STRING_1);
        JmlModelProgramClause jmpc2 = getModelProgramExpression(METHOD_DECLARATION_STRING_2);
        Assert.assertFalse("Different JmlModelProgramClause matched.", jmpc1.subtreeMatch(matcher, jmpc2));
        Assert.assertTrue("Identical JmlModelProgramClause did not match.", jmpc1.subtreeMatch(matcher, jmpc1));
    }

    private JmlModelProgramClause getModelProgramExpression(String method) {
        MethodDeclaration md = null;
        try {
            ASTParser parser = ASTParser.newParser(AST.JLS3);
            parser.setSource(method.toCharArray());
            parser.setKind(ASTParser.K_CLASS_BODY_DECLARATIONS);
            TypeDeclaration td = (TypeDeclaration) parser.createAST(null);
            md = (MethodDeclaration) td.bodyDeclarations().get(0);
        } catch (Exception e) {
            fail("Test method declaration could be parsed correctly: " + e.getMessage());
        }
        ExpressionStatement es = null;
        try {
            es = jcf.createModelProgram(VisibilityModifier.PUBLIC, "int x = 1+1;", md);
        } catch (InvalidJavaException e) {
            fail("Failed to parse body.");
        }
        ClassInstanceCreation cic = (ClassInstanceCreation) es.getExpression();
        return new JmlModelProgramClause(cic);
    }

    @Test
    public void testMatchJmlModifiesClauseObject() throws InvalidJavaException {
        JmlModifiesClause jmc1 = new JmlModifiesClause(false, (Expression) null);
        Assert.assertTrue(jmc1.subtreeMatch(matcher, jmc1));
        Assert.assertFalse(jmc1.subtreeMatch(matcher, getJmlModifiesClause()));
        Assert.assertTrue(getJmlModifiesClause().subtreeMatch(matcher, getJmlModifiesClause()));
    }

    @Test
    public void testMatchJmlRequiresClauseObject() throws InvalidJavaException {
        JmlRequiresClause jrc1 = new JmlRequiresClause(false, (Expression) null);
        Assert.assertTrue(jrc1.subtreeMatch(matcher, jrc1));
        Assert.assertFalse(jrc1.subtreeMatch(matcher, getJmlRequiresClause()));
        Assert.assertTrue(getJmlRequiresClause().subtreeMatch(matcher, getJmlRequiresClause()));
    }

    @Test
    public void testMatchJmlSignalsClauseObject() throws InvalidJavaException {
        JmlSignalsClause jsc1 = new JmlSignalsClause(false, (Expression) null);
        Assert.assertTrue(jsc1.subtreeMatch(matcher, jsc1));
        Assert.assertFalse(jsc1.subtreeMatch(matcher, getJmlSignalsClause()));
        Assert.assertTrue(getJmlSignalsClause().subtreeMatch(matcher, getJmlSignalsClause()));
    }

    @Test
    public void testMatchJmlSignalsOnlyClauseObject() throws InvalidJavaException {
        JmlSignalsOnlyClause jsc1 = new JmlSignalsOnlyClause(false, (Expression) null);
        Assert.assertTrue(jsc1.subtreeMatch(matcher, jsc1));
        Assert.assertFalse(jsc1.subtreeMatch(matcher, getJmlSignalsOnlyClause()));
        Assert.assertTrue(getJmlSignalsOnlyClause().subtreeMatch(matcher, getJmlSignalsOnlyClause()));
    }

    @Test
    public void testMatchJmlSpecCaseObject() throws InvalidJavaException {
        JmlSpecCase jsc1 = new JmlSpecCase(JC.PUBLIC, false, null);
        Assert.assertTrue(jsc1.subtreeMatch(matcher, jsc1));
        Assert.assertFalse(jsc1.subtreeMatch(matcher, getJmlSpecCase()));
        Assert.assertTrue(getJmlSpecCase().subtreeMatch(matcher, getJmlSpecCase()));
    }

    @Test
    public void testMatchJmlSpecCaseBodyObject() throws InvalidJavaException {
        JmlSpecCaseBody body = new JmlSpecCaseBody(null, null, null);
        Assert.assertTrue(body.subtreeMatch(matcher, body));
        Assert.assertFalse(body.subtreeMatch(matcher, getSpecCaseBody()));
    }

    private JmlRequiresClause getJmlRequiresClause() throws InvalidJavaException {
        return new JmlRequiresClause(false, jcf.createRequires("x > 0").getExpression());
    }

    private JmlEnsuresClause getJmlEnsuresClause() throws InvalidJavaException {
        return new JmlEnsuresClause(false, jcf.createEnsures("$old(i) != 13").getExpression());
    }

    private JmlDivergesClause getJmlDiveresClause() throws InvalidJavaException {
        ExpressionStatement expStmt = jcf.createDiverges("true");
        return new JmlDivergesClause(false, expStmt.getExpression());
    }

    private JmlModifiesClause getJmlModifiesClause() throws InvalidJavaException {
        ExpressionStatement expStmt = jcf.createModifies("x");
        return new JmlModifiesClause(false, expStmt.getExpression());
    }

    private JmlSignalsClause getJmlSignalsClause() throws InvalidJavaException {
        return new JmlSignalsClause(true, jcf.createSignals("Exception", "e != null").getExpression());
    }

    private JmlSpecCase getJmlSpecCase() throws InvalidJavaException {
        return new JmlSpecCase(JC.PUBLIC, false, getSpecCaseBody());
    }

    private JmlSpecCaseBody getSpecCaseBody() throws InvalidJavaException {
        JmlClause[] clauses = { getJmlEnsuresClause(), getJmlRequiresClause(), getJmlDiveresClause(), getJmlModifiesClause() };
        return new JmlSpecCaseBody(null, null, clauses);
    }

    private JmlSignalsOnlyClause getJmlSignalsOnlyClause() throws InvalidJavaException {
        return new JmlSignalsOnlyClause(false, jcf.createSignalsOnly("Exception").getExpression());
    }
}
