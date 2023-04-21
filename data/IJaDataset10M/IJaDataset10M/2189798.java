package toxTree.cramer2;

import java.io.File;
import java.io.FileInputStream;
import junit.framework.TestCase;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.isomorphism.matchers.QueryAtomContainer;
import toxTree.core.IDecisionCategory;
import toxTree.core.IDecisionResult;
import toxTree.exceptions.DecisionMethodException;
import toxTree.exceptions.DecisionResultException;
import toxTree.logging.TTLogger;
import toxTree.query.FunctionalGroups;
import toxTree.tree.DefaultCategory;
import ambit2.core.io.IteratingDelimitedFileReader;
import cramer2.CramerRulesWithExtensions;

public class MunroTest extends TestCase {

    protected CramerRulesWithExtensions rules;

    protected static TTLogger logger = new TTLogger(MunroTest.class);

    public MunroTest(String arg0) {
        super(arg0);
        TTLogger.configureLog4j(true);
        try {
            rules = new CramerRulesWithExtensions();
        } catch (DecisionMethodException x) {
            fail();
        }
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    protected void testFile(String filename, IDecisionCategory category) {
        logger.error("Should be\t", category);
        try {
            IteratingDelimitedFileReader reader = new IteratingDelimitedFileReader(new FileInputStream(new File(filename)));
            IDecisionResult result = rules.createDecisionResult();
            int ok = 0;
            int records = 0;
            int emptyMolecules = 0;
            int applyError = 0;
            QueryAtomContainer sulphonate = FunctionalGroups.sulphonate(null, false);
            while (reader.hasNext()) {
                result.clear();
                Object o = reader.next();
                if (o instanceof IAtomContainer) {
                    IAtomContainer a = (IAtomContainer) o;
                    try {
                        result.classify(a);
                        if (!result.getCategory().equals(category)) {
                            if ((category.getID() == 3) && (FunctionalGroups.hasGroup(a, sulphonate))) {
                                ok++;
                            } else logger.error("\"" + a.getProperty("NAME"), "\"\t", result.getCategory(), "\t", result.explain(false));
                        } else ok++;
                    } catch (DecisionResultException x) {
                        applyError++;
                    }
                }
                records++;
            }
            logger.error(category);
            logger.error("Processed\t", records);
            logger.error("Successfull\t", ok);
            logger.error("Empty\t", emptyMolecules);
            logger.error("Error when applying rules\t", applyError);
            logger.error("");
            assertTrue(records > 0);
            assertEquals(records - applyError, ok);
            assertEquals(emptyMolecules, 0);
            assertEquals(applyError, 0);
        } catch (Exception x) {
            x.printStackTrace();
            fail();
        }
    }

    public void testMunroClass1() {
        IDecisionCategory c = rules.getCategories().getCategory(new DefaultCategory("", 1));
        testFile("toxTree/data/Munro/munro-1.csv", c);
    }

    public void testMunroClass2() {
        IDecisionCategory c = rules.getCategories().getCategory(new DefaultCategory("", 2));
        testFile("toxTree/data/Munro/munro-2.csv", c);
    }

    public void testMunroClass3() {
        IDecisionCategory c = rules.getCategories().getCategory(new DefaultCategory("", 3));
        testFile("toxTree/data/Munro/munro-3.csv", c);
    }
}
