package net.sf.refactorit.test.metrics;

import net.sf.refactorit.classmodel.Project;
import net.sf.refactorit.metrics.MetricsAction;
import net.sf.refactorit.metrics.MetricsModel;
import net.sf.refactorit.options.GlobalOptions;
import net.sf.refactorit.test.RwRefactoringTestUtils;
import net.sf.refactorit.test.Utils;
import net.sf.refactorit.test.refactorings.RefactoringTestCase;
import net.sf.refactorit.ui.DialogManager;
import net.sf.refactorit.ui.NullDialogManager;
import net.sf.refactorit.ui.options.profile.Profile;
import net.sf.refactorit.ui.treetable.writer.HtmlTableFormat;
import net.sf.refactorit.ui.treetable.writer.TableLayout;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import junit.framework.Test;
import junit.framework.TestSuite;

public class HtmlOutputTest extends RefactoringTestCase {

    public HtmlOutputTest(String name) {
        super(name);
    }

    public static Test suite() {
        final TestSuite suite = new TestSuite(HtmlOutputTest.class);
        suite.setName("Metrics");
        return suite;
    }

    protected void tearDown() {
        DialogManager.setInstance(new NullDialogManager());
    }

    public String getTemplate() {
        return "<extra_name>Metrics/<test_name>/<in_out>";
    }

    public void testBug1604() throws Throwable {
        GlobalOptions.setOption("separator.decimal", ".");
        GlobalOptions.setOption("separator.grouping", "");
        Project in = getInitialProject();
        in.getProjectLoader().build();
        MetricsModel mm = new MetricsModel(MetricsAction.getDefaultColumnNames(), MetricsAction.getDefaultActionIndexes());
        mm.getState().setProfile(Profile.createDefaultMetrics());
        mm.populate(in, in.getDefaultPackage());
        File outFile = File.createTempFile("metrics_", ".html");
        PrintWriter pw = new PrintWriter(new FileWriter(outFile));
        pw.print(TableLayout.getClipboardText(new HtmlTableFormat(), mm, "Metrics for the whole project"));
        pw.close();
        File expected = new File(resolveTestName(Utils.getTestProjectsDirectory().getAbsolutePath() + '/', false) + "/expected.html");
        assertSame(expected, outFile);
    }

    private void assertSame(File expected, File output) throws Exception {
        RwRefactoringTestUtils.compareWithDiff("", expected, output);
    }
}
