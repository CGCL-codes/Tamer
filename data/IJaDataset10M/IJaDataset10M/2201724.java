package org.deft.repository.junit;

import java.util.LinkedList;
import java.util.List;
import org.deft.repository.ast.Token;
import org.deft.repository.ast.TokenNode;
import org.deft.repository.ast.annotation.SubGroup;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SubGroupTest {

    private TokenNode tnPublic, tnVoid, tnIdent, tnLparen, tnRparen, tnLbrace, tnIdent2, tnLparen2, tnInt, tnRparen2, tnSemi, tnRbrace;

    private List<TokenNode> tokenNodeList;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        makeExampleNodes();
        addExampleNodesToList();
    }

    @After
    public void tearDown() throws Exception {
    }

    private void makeExampleNodes() {
        tnPublic = new TokenNode("PUBLIC", new Token(1, 1, 1, "public"));
        tnVoid = new TokenNode("VOID", new Token(1, 8, 8, "void"));
        tnIdent = new TokenNode("IDENTIFIER", new Token(1, 13, 13, "meth"));
        tnLparen = new TokenNode("LPAREN", new Token(1, 17, 17, "("));
        tnRparen = new TokenNode("RPAREN", new Token(1, 18, 18, ")"));
        tnLbrace = new TokenNode("LBRACE", new Token(1, 20, 20, "{"));
        tnIdent2 = new TokenNode("IDENTIFIER", new Token(5, 5, 29, "doSomething"));
        tnLparen2 = new TokenNode("LPAREN", new Token(5, 16, 40, "("));
        tnInt = new TokenNode("INTLITERAL", new Token(5, 17, 41, "20"));
        tnRparen2 = new TokenNode("RPAREN", new Token(5, 19, 43, ")"));
        tnSemi = new TokenNode("SEMICOLON", new Token(5, 20, 44, ";"));
        tnRbrace = new TokenNode("RBRACE", new Token(6, 1, 46, "}"));
    }

    private void addExampleNodesToList() {
        tokenNodeList = new LinkedList<TokenNode>();
        tokenNodeList.add(tnPublic);
        tokenNodeList.add(tnVoid);
        tokenNodeList.add(tnIdent);
        tokenNodeList.add(tnLparen);
        tokenNodeList.add(tnRparen);
        tokenNodeList.add(tnLbrace);
        tokenNodeList.add(tnIdent2);
        tokenNodeList.add(tnLparen2);
        tokenNodeList.add(tnInt);
        tokenNodeList.add(tnRparen2);
        tokenNodeList.add(tnSemi);
        tokenNodeList.add(tnRbrace);
    }

    @Test
    public void test_isStartIsEnd() {
        SubGroup sg1 = new SubGroup(true, true);
        SubGroup sg2 = new SubGroup(false, false);
        SubGroup sg3 = new SubGroup(true, true, 1, 1, 1);
        SubGroup sg4 = new SubGroup(false, false, 1, 1, 1);
        Assert.assertEquals(sg1.isStart(), true);
        Assert.assertEquals(sg1.isEnd(), true);
        Assert.assertEquals(sg2.isStart(), false);
        Assert.assertEquals(sg2.isEnd(), false);
        Assert.assertEquals(sg3.isStart(), true);
        Assert.assertEquals(sg3.isEnd(), true);
        Assert.assertEquals(sg4.isStart(), false);
        Assert.assertEquals(sg4.isEnd(), false);
    }

    @Test
    public void test_isEmpty() {
        SubGroup sg = new SubGroup(true, true);
        Assert.assertTrue(sg.isEmpty());
        sg.addTokenNode(tokenNodeList.get(0));
        Assert.assertFalse(sg.isEmpty());
    }

    @Test
    public void test_positionOfEmptySubGroup() {
        SubGroup sg = new SubGroup(true, true, 5, 10, 23);
        Assert.assertEquals(sg.getStartLine(), 5);
        Assert.assertEquals(sg.getEndLine(), 5);
        Assert.assertEquals(sg.getStartCol(), 10);
        Assert.assertEquals(sg.getEndCol(), 10);
        Assert.assertEquals(sg.getOffset(), 23);
        Assert.assertEquals(sg.getLength(), 0);
    }

    @Test
    public void test_positionOfNonEmptySubGroup() {
        SubGroup sg = new SubGroup(true, true, 5, 10, 23);
        for (TokenNode tn : tokenNodeList) {
            sg.addTokenNode(tn);
        }
        Token tFirst = tokenNodeList.get(0).getToken();
        Token tLast = tokenNodeList.get(tokenNodeList.size() - 1).getToken();
        Assert.assertEquals(sg.getStartLine(), tFirst.getLine());
        Assert.assertEquals(sg.getEndLine(), tLast.getLine());
        Assert.assertEquals(sg.getStartCol(), tFirst.getCol());
        Assert.assertEquals(sg.getEndCol(), tLast.getEndCol());
        Assert.assertEquals(sg.getOffset(), tFirst.getOffset());
        Assert.assertEquals(sg.getLength(), tLast.getOffset() + tLast.getLength() - tFirst.getOffset());
    }

    @Test
    public void test_getFirstTokenNodeOfEmptySubGroup() {
        SubGroup sg = new SubGroup(true, true);
        TokenNode tnFirst = sg.getFirstTokenNode();
        Assert.assertNull(tnFirst);
    }

    @Test
    public void test_getLastTokenNodeOfEmptySubGroup() {
        SubGroup sg = new SubGroup(true, true);
        TokenNode tnLast = sg.getLastTokenNode();
        Assert.assertNull(tnLast);
    }

    @Test
    public void test_getFirstTokenNodeOfNonEmptySubGroup() {
        SubGroup sg = new SubGroup(true, true);
        for (TokenNode tn : tokenNodeList) {
            sg.addTokenNode(tn);
        }
        TokenNode tnFirst = sg.getFirstTokenNode();
        Assert.assertEquals(tnFirst, tokenNodeList.get(0));
    }

    @Test
    public void test_getLastTokenNodeOfNonEmptySubGroup() {
        SubGroup sg = new SubGroup(true, true);
        for (TokenNode tn : tokenNodeList) {
            sg.addTokenNode(tn);
        }
        TokenNode tnLast = sg.getLastTokenNode();
        Assert.assertEquals(tnLast, tokenNodeList.get(tokenNodeList.size() - 1));
    }

    @Test
    public void test_getTokenNodeCount() {
        SubGroup sg = new SubGroup(true, true);
        Assert.assertEquals(sg.getTokenNodeCount(), 0);
        for (TokenNode tn : tokenNodeList) {
            sg.addTokenNode(tn);
        }
        Assert.assertEquals(sg.getTokenNodeCount(), tokenNodeList.size());
    }
}
