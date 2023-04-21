package net.sf.refactorit.test.refactorings;

import net.sf.refactorit.classmodel.BinCIType;
import net.sf.refactorit.classmodel.BinMethod;
import net.sf.refactorit.classmodel.BinTypeRef;
import net.sf.refactorit.classmodel.Project;
import net.sf.refactorit.classmodel.statements.BinStatement;
import net.sf.refactorit.classmodel.statements.BinTryStatement;
import net.sf.refactorit.classmodel.statements.BinTryStatement.CatchClause;
import net.sf.refactorit.test.Utils;
import net.sf.refactorit.ui.module.wherecaught.WhereCaughtModel;
import net.sf.refactorit.ui.treetable.BinTreeTableNode;
import org.apache.log4j.Category;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class WhereCaughtTest extends TestCase {

    /** Logger instance. */
    private static final Category cat = Category.getInstance(WhereCaughtTest.class.getName());

    public WhereCaughtTest() {
        super(WhereCaughtTest.class.getName());
    }

    public static Test suite() {
        final TestSuite suite = new TestSuite(WhereCaughtTest.class);
        suite.setName("WhereCaughtTest");
        return suite;
    }

    public String getTemplate() {
        return "WhereCaught/<test_name>/<in_out>";
    }

    /**
   * Tests bug #2213: WhereCaught doesn't recognize NullPointerExceptions
   */
    public void testBug2213() throws Exception {
        cat.info("Testing bug #2213");
        final Project project = Utils.createTestRbProject(Utils.getTestProjects().getProject("bug #2213"));
        project.getProjectLoader().build();
        BinTypeRef testTypeRef = project.getTypeRefForName("Test");
        cat.debug("Run Where Caught refactoring on null");
        final BinCIType binCIType = testTypeRef.getBinCIType();
        BinMethod[] declaredMethods;
        declaredMethods = binCIType.getAccessibleMethods("main", binCIType);
        BinStatement[] catchStatement = declaredMethods[0].getBody().getStatements();
        BinTryStatement tryStatement = (BinTryStatement) catchStatement[0];
        CatchClause[] catches = tryStatement.getCatches();
        declaredMethods = binCIType.getAccessibleMethods("exceptionalMethod", binCIType);
        BinStatement[] throwStatement = declaredMethods[0].getBody().getStatements();
        WhereCaughtModel model = new WhereCaughtModel(project, throwStatement[0]);
        BinTreeTableNode tableRoot = (BinTreeTableNode) model.getRoot();
        BinTreeTableNode child = (BinTreeTableNode) tableRoot.getChildAt(0);
        child = (BinTreeTableNode) child.getChildAt(0);
        child = (BinTreeTableNode) child.getChildAt(0);
        assertTrue("Comparing get model node with the real node", child.getBin() == catches[0]);
        cat.info("SUCCESS");
    }
}
