package de.uniba.wiai.kinf.pw.projects.lillytab.reasoner.abox;

import de.uniba.wiai.kinf.pw.projects.lillytab.abox.IABox;
import de.uniba.wiai.kinf.pw.projects.lillytab.abox.IABoxNode;
import de.uniba.wiai.kinf.pw.projects.lillytab.terms.IDLClassReference;
import de.uniba.wiai.kinf.pw.projects.lillytab.terms.IDLClassExpression;
import de.uniba.wiai.kinf.pw.projects.lillytab.terms.IDLTermFactory;
import de.uniba.wiai.kinf.pw.projects.lillytab.terms.impl.DLTermFactory;
import de.uniba.wiai.kinf.pw.projects.lillytab.terms.util.SimpleKRSSParser;
import java.text.ParseException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Less than basic test cases for the lillytab Reasoner
 *
 * @author Peter Wullinger <peter.wullinger@uni-bamberg.de>
 */
public class ReasonerOntologyTest {

    private IDLTermFactory<String, String, String> _termFactory = new DLTermFactory<String, String, String>();

    private ABox<String, String, String> _abox;

    private Reasoner<String, String, String> _reasoner;

    private SimpleKRSSParser _parser;

    public ReasonerOntologyTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        LogManager.getLogManager().reset();
        Logger.getLogger("").setLevel(Level.ALL);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        Logger.getLogger("").addHandler(handler);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        _reasoner = new Reasoner<String, String, String>();
        _parser = new SimpleKRSSParser(_termFactory);
        _abox = new ABox<String, String, String>(_termFactory);
    }

    @After
    public void tearDown() {
        _reasoner = null;
        _abox = null;
        _parser = null;
    }

    @Test(expected = InconsistentOntologyException.class)
    public void testSimpleInconsistentStoneDamageOntology() throws ParseException, ReasonerException {
        IDLClassReference<String, String, String> SpatialObject = _termFactory.getDLClassReference("S");
        IDLClassReference<String, String, String> Stone = _termFactory.getDLClassReference("S");
        IDLClassReference<String, String, String> Ashlar = _termFactory.getDLClassReference("A");
        IDLClassReference<String, String, String> Damage = _termFactory.getDLClassReference("D");
        IDLClassReference<String, String, String> StoneDamage = _termFactory.getDLClassReference("SD");
        IDLClassReference<String, String, String> MetalDamage = _termFactory.getDLClassReference("MD");
        IABoxNode<String, String, String> aks1 = _abox.getOrAddNamedNode("aks1", false);
        IABoxNode<String, String, String> d1 = _abox.getOrAddNamedNode("d1", false);
        Set<IDLClassExpression<String, String, String>> dlDesc = new HashSet<IDLClassExpression<String, String, String>>();
        dlDesc.add(_parser.parse("(implies SO (some hasDamage _Thing_))"));
        dlDesc.add(_parser.parse("(not (and SD MD))"));
        dlDesc.add(_parser.parse("(implies A S)"));
        dlDesc.add(_parser.parse("(implies S (only hasDamage SD))"));
        _abox.getTBox().addAll(dlDesc);
        aks1.getTerms().add(Ashlar);
        d1.getTerms().add(MetalDamage);
        aks1.getSuccessors().put("hasDamage", d1.getNodeID());
        _reasoner.checkConsistency(_abox);
    }

    @Test
    public void testSimpleConsistentStoneDamageOntology() throws ParseException, ReasonerException {
        SimpleKRSSParser parser = new SimpleKRSSParser(_termFactory);
        IDLClassReference<String, String, String> SpatialObject = _termFactory.getDLClassReference("S");
        IDLClassReference<String, String, String> Stone = _termFactory.getDLClassReference("S");
        IDLClassReference<String, String, String> Ashlar = _termFactory.getDLClassReference("A");
        IDLClassReference<String, String, String> Damage = _termFactory.getDLClassReference("D");
        IDLClassReference<String, String, String> StoneDamage = _termFactory.getDLClassReference("SD");
        IDLClassReference<String, String, String> MetalDamage = _termFactory.getDLClassReference("MD");
        IABoxNode<String, String, String> aks1 = _abox.getOrAddNamedNode("aks1", false);
        IABoxNode<String, String, String> d1 = _abox.getOrAddNamedNode("d1", false);
        Set<IDLClassExpression<String, String, String>> dlDesc = new HashSet<IDLClassExpression<String, String, String>>();
        dlDesc.add(parser.parse("(implies SO (some hasDamage _Thing_))"));
        dlDesc.add(parser.parse("(not (and SD MD))"));
        dlDesc.add(parser.parse("(implies A S)"));
        dlDesc.add(parser.parse("(implies S (only hasDamage SD))"));
        _abox.getTBox().addAll(dlDesc);
        aks1.getTerms().add(Ashlar);
        d1.getTerms().add(StoneDamage);
        aks1.getSuccessors().put("hasDamage", d1.getNodeID());
        _reasoner.checkConsistency(_abox);
    }
}
