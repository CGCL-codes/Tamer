package jppl.console;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import jppl.console.grammar.LookaheadItem;
import jppl.console.grammar.Rule;
import jppl.console.grammar.Symbols;
import jppl.console.symbols.GrammarSymbol;
import jppl.console.symbols.NonterminalSymbol;
import jppl.console.symbols.SymbolEnd;
import jppl.console.symbols.SymbolEpsilon;
import jppl.console.symbols.TerminalSymbol;
import jppl.core.testUtils.TestFunctions;
import jppl.core.testUtils.rules.DummyGrammarRule1;
import jppl.core.testUtils.rules.DummyGrammarRule2;
import jppl.core.testUtils.rules.DummyGrammarRule3;
import jppl.core.testUtils.rules.DummyGrammarRule4;
import jppl.core.testUtils.rules.DummyGrammarRule5;
import jppl.core.testUtils.symbols.DummyNonterm_A;
import jppl.core.testUtils.symbols.DummyNonterm_B;
import jppl.core.testUtils.symbols.DummyNonterm_C;
import jppl.core.testUtils.symbols.DummyTerm_a;
import jppl.core.testUtils.symbols.DummyTerm_b;
import jppl.core.testUtils.symbols.DummyTerm_c;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author rolf
 */
public class GrammarFirstTest {

    private static final Logger log = Logger.getLogger(GrammarFirstTest.class);

    private GrammarParser parser;

    private TerminalSymbol dummyTerm_a;

    private TerminalSymbol dummyTerm_b;

    private TerminalSymbol dummyTerm_c;

    private GrammarSymbol symbolEpsilon;

    private NonterminalSymbol dummyNonterm_A;

    private NonterminalSymbol dummyNonterm_B;

    private NonterminalSymbol dummyNonterm_C;

    private Rule dummyGrammarRule1;

    private Rule dummyGrammarRule2;

    private Rule dummyGrammarRule3;

    private Rule dummyGrammarRule4;

    private Rule dummyGrammarRule5;

    @Before
    public void setUp() {
        parser = new GrammarParser();
        dummyTerm_a = new DummyTerm_a();
        dummyTerm_b = new DummyTerm_b();
        dummyTerm_c = new DummyTerm_c();
        symbolEpsilon = new SymbolEpsilon();
        dummyNonterm_A = new DummyNonterm_A();
        dummyNonterm_B = new DummyNonterm_B();
        dummyNonterm_C = new DummyNonterm_C();
        dummyGrammarRule1 = new DummyGrammarRule1();
        dummyGrammarRule2 = new DummyGrammarRule2();
        dummyGrammarRule3 = new DummyGrammarRule3();
        dummyGrammarRule4 = new DummyGrammarRule4();
        dummyGrammarRule5 = new DummyGrammarRule5();
        parser.addRule(dummyGrammarRule1);
        parser.addRule(dummyGrammarRule2);
        parser.addRule(dummyGrammarRule3);
        parser.addRule(dummyGrammarRule4);
        parser.addRule(dummyGrammarRule5);
    }

    @Test
    public void testFirstTerminal() {
        log.info("testing first(...) with a terminal");
        parser.calculateFirst();
        Symbols symbols = new Symbols();
        symbols.addSymbol(dummyTerm_a);
        Symbols returnedSymbols = parser.first(symbols);
        assertTrue("Returned symbols were too few or too many", returnedSymbols.size() == 1);
        TestFunctions.assertInCollection(returnedSymbols.getSymbols(), dummyTerm_a);
    }

    @Test
    public void testFirstRuleWithTerminal() {
        log.info("testing first(...) with a rule that gives a terminal directly");
        parser.calculateFirst();
        Symbols symbols = new Symbols();
        symbols.addSymbol(dummyNonterm_C);
        Symbols returnedSymbols = parser.first(symbols);
        assertTrue("Returned symbols were too few or too many", returnedSymbols.size() == 1);
        TestFunctions.assertInCollection(returnedSymbols.getSymbols(), dummyTerm_c);
    }

    @Test
    public void testFirstRuleWithTerminalAndEpsilon() {
        log.info("testing first(...) with rules that have terminal and epsilon");
        parser.calculateFirst();
        Symbols symbols = new Symbols();
        symbols.addSymbol(dummyNonterm_B);
        Symbols returnedSymbols = parser.first(symbols);
        assertTrue("Returned symbols were too few or too many", returnedSymbols.size() == 2);
        TestFunctions.assertInCollection(returnedSymbols.getSymbols(), dummyTerm_b);
        TestFunctions.assertInCollection(returnedSymbols.getSymbols(), symbolEpsilon);
    }

    @Test
    public void testFirstRuleForSingleEpsilon() {
        log.info("testing first(...) with only epsilon");
        parser.calculateFirst();
        Symbols symbols = new Symbols();
        symbols.addSymbol(symbolEpsilon);
        Symbols returnedSymbols = parser.first(symbols);
        assertTrue("Returned symbols were too few or too many", returnedSymbols.size() == 1);
        TestFunctions.assertInCollection(returnedSymbols.getSymbols(), symbolEpsilon);
    }

    @Test
    public void testFirstRuleForEpsilon() {
        log.info("testing first(...) with epsilon and a symbol");
        parser.calculateFirst();
        Symbols symbols = new Symbols();
        symbols.addSymbol(symbolEpsilon);
        symbols.addSymbol(dummyTerm_a);
        Symbols returnedSymbols = parser.first(symbols);
        assertTrue("Returned symbols were too few or too many", returnedSymbols.size() == 1);
        TestFunctions.assertInCollection(returnedSymbols.getSymbols(), dummyTerm_a);
    }

    @Test
    public void testFirstRuleWithNonterm() {
        log.info("testing first(...) with a rule that has a nonterminal");
        parser.calculateFirst();
        Symbols symbols = new Symbols();
        symbols.addSymbol(dummyNonterm_A);
        Symbols returnedSymbols = parser.first(symbols);
        assertTrue("Returned symbols were too few or too many", returnedSymbols.size() == 3);
        TestFunctions.assertInCollection(returnedSymbols.getSymbols(), symbolEpsilon);
        TestFunctions.assertInCollection(returnedSymbols.getSymbols(), dummyTerm_a);
        TestFunctions.assertInCollection(returnedSymbols.getSymbols(), dummyTerm_b);
    }

    @Test
    public void testConcatenate() {
        log.info("testing concatenate(...)");
        Collection<Symbols> symbolCol = new ArrayList<Symbols>();
        Symbols symColl1 = new Symbols();
        Symbols symColl2 = new Symbols();
        Symbols sym = new Symbols();
        Symbols resultSymbols1 = new Symbols();
        Symbols resultSymbols2 = new Symbols();
        symColl1.addSymbol(dummyNonterm_A);
        symColl1.addSymbol(dummyTerm_a);
        symColl2.addSymbol(dummyNonterm_B);
        sym.addSymbol(dummyTerm_b);
        sym.addSymbol(dummyTerm_c);
        resultSymbols1.addSymbol(dummyTerm_b);
        resultSymbols1.addSymbol(dummyTerm_c);
        resultSymbols1.addSymbol(dummyNonterm_A);
        resultSymbols1.addSymbol(dummyTerm_a);
        resultSymbols2.addSymbol(dummyTerm_b);
        resultSymbols2.addSymbol(dummyTerm_c);
        resultSymbols2.addSymbol(dummyNonterm_B);
        symbolCol.add(symColl1);
        symbolCol.add(symColl2);
        Collection<Symbols> result = parser.concatenate(sym, symbolCol);
        TestFunctions.assertInCollection(result, resultSymbols1);
        TestFunctions.assertInCollection(result, resultSymbols2);
    }

    @Test
    public void testConcatenateForSymbols() {
        log.info("testing concatenateForSymbols(...)");
        Collection<GrammarSymbol> symbolColl = new ArrayList<GrammarSymbol>();
        Symbols sym = new Symbols();
        sym.addSymbol(dummyTerm_b);
        sym.addSymbol(dummyTerm_c);
        symbolColl.add(dummyNonterm_A);
        symbolColl.add(dummyTerm_a);
        symbolColl.add(dummyNonterm_B);
        Symbols resultSymbols1 = new Symbols();
        Symbols resultSymbols2 = new Symbols();
        Symbols resultSymbols3 = new Symbols();
        resultSymbols1.addSymbol(dummyTerm_b);
        resultSymbols1.addSymbol(dummyTerm_c);
        resultSymbols1.addSymbol(dummyNonterm_A);
        resultSymbols2.addSymbol(dummyTerm_b);
        resultSymbols2.addSymbol(dummyTerm_c);
        resultSymbols2.addSymbol(dummyTerm_a);
        resultSymbols3.addSymbol(dummyTerm_b);
        resultSymbols3.addSymbol(dummyTerm_c);
        resultSymbols3.addSymbol(dummyNonterm_B);
        Collection<Symbols> result = parser.concatenateForSymbols(sym, symbolColl);
        TestFunctions.assertInCollection(result, resultSymbols1);
        TestFunctions.assertInCollection(result, resultSymbols2);
        TestFunctions.assertInCollection(result, resultSymbols3);
    }

    @Test
    public void testFirstForCollection() {
        log.info("testing firstForCollection(...)");
        Collection<Symbols> symbols = new ArrayList<Symbols>();
        Symbols symbols1 = new Symbols();
        Symbols symbols2 = new Symbols();
        symbols1.addSymbol(dummyNonterm_A);
        symbols2.addSymbol(dummyNonterm_C);
        Collection<Symbols> coll = new ArrayList<Symbols>();
        coll.add(symbols1);
        coll.add(symbols2);
        Symbols returnedSymbols = parser.first(coll);
        TestFunctions.assertInCollection(returnedSymbols.getSymbols(), symbolEpsilon);
        TestFunctions.assertInCollection(returnedSymbols.getSymbols(), dummyTerm_a);
        TestFunctions.assertInCollection(returnedSymbols.getSymbols(), dummyTerm_b);
        TestFunctions.assertInCollection(returnedSymbols.getSymbols(), dummyTerm_c);
    }

    private void assertSymbolInCollection(Collection<GrammarSymbol> collection, GrammarSymbol expected) {
        assertNotNull("Collection is empty", collection);
        Iterator<GrammarSymbol> symbolIterator = collection.iterator();
        while (symbolIterator.hasNext()) {
            GrammarSymbol currentSymbol = symbolIterator.next();
            if (expected.getClass().isAssignableFrom(currentSymbol.getClass())) return;
        }
        fail("Expected object not in collection");
    }

    @Ignore
    @Test
    public void testCreateLookaheadItemRule1() {
        log.info("testing createLookaheadItems(...) for rule 1");
        parser.createLookaheadItems();
        LookaheadItem lookaheadItem1 = new LookaheadItem();
        lookaheadItem1.setLeftSide(dummyNonterm_A);
        lookaheadItem1.addRightSymbol(dummyNonterm_B);
        lookaheadItem1.addLookaheadSymbol(new SymbolEnd());
        lookaheadItem1.setPosition(0);
        assertLookaheadInCollection(parser.items, lookaheadItem1);
        lookaheadItem1.setPosition(1);
        assertLookaheadInCollection(parser.items, lookaheadItem1);
    }

    @Ignore
    @Test
    public void testCreateLookaheadItemRule2() {
        log.info("testing createLookaheadItems(...) for rule 2");
        parser.createLookaheadItems();
        LookaheadItem lookaheadItem1 = new LookaheadItem();
        lookaheadItem1.setLeftSide(dummyNonterm_A);
        lookaheadItem1.addRightSymbol(dummyTerm_a);
        lookaheadItem1.addLookaheadSymbol(new SymbolEnd());
        lookaheadItem1.setPosition(0);
        assertLookaheadInCollection(parser.items, lookaheadItem1);
        lookaheadItem1.setPosition(1);
        assertLookaheadInCollection(parser.items, lookaheadItem1);
    }

    @Ignore
    @Test
    public void testCreateLookaheadItemRule3() {
        log.info("testing createLookaheadItems(...) for rule 3");
        parser.createLookaheadItems();
        LookaheadItem lookaheadItem1 = new LookaheadItem();
        lookaheadItem1.setLeftSide(dummyNonterm_B);
        lookaheadItem1.addRightSymbol(dummyTerm_b);
        lookaheadItem1.addLookaheadSymbol(new SymbolEnd());
        lookaheadItem1.setPosition(0);
        assertLookaheadInCollection(parser.items, lookaheadItem1);
        lookaheadItem1.setPosition(1);
        assertLookaheadInCollection(parser.items, lookaheadItem1);
    }

    @Ignore
    @Test
    public void testCreateLookaheadItemRule4() {
        log.info("testing createLookaheadItems(...) for rule 4");
        parser.createLookaheadItems();
        LookaheadItem lookaheadItem1 = new LookaheadItem();
        lookaheadItem1.setLeftSide(dummyNonterm_B);
        lookaheadItem1.addRightSymbol(new SymbolEpsilon());
        lookaheadItem1.addLookaheadSymbol(new SymbolEnd());
        lookaheadItem1.setPosition(0);
        assertLookaheadInCollection(parser.items, lookaheadItem1);
        lookaheadItem1.setPosition(1);
        assertLookaheadInCollection(parser.items, lookaheadItem1);
    }

    private void assertLookaheadInCollection(Collection<LookaheadItem> collection, LookaheadItem expectedItem) {
        assertNotNull("Collection is empty", collection);
        Iterator<LookaheadItem> symbolIterator = collection.iterator();
        while (symbolIterator.hasNext()) {
            LookaheadItem currentItem = symbolIterator.next();
            if (currentItem.equals(expectedItem)) return;
        }
        fail("Expected lookahead item not in collection");
    }
}
