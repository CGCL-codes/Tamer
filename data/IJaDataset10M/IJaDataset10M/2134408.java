package org.databene.dbsanity.report;

import static org.databene.dbsanity.report.ReportUtil.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.databene.commons.FileUtil;
import org.databene.commons.NameComparator;
import org.databene.dbsanity.html.HtmlFileImage;
import org.databene.dbsanity.html.HtmlFileReference;
import org.databene.dbsanity.html.HtmlPrintWriter;
import org.databene.dbsanity.html.Renderable;
import org.databene.dbsanity.html.RenderableText;
import org.databene.dbsanity.model.SanityCheck;
import org.databene.dbsanity.model.CheckHolder;
import org.databene.dbsanity.model.SuiteHolder;
import org.databene.dbsanity.model.SanityCheckSuite;
import org.databene.dbsanity.model.SanityCheckVerdict;

/**
 * Provides an overview of a DB sanity sub suite.<br/><br/>
 * Created: 08.11.2010 17:51:11
 * @since 0.4
 * @author Volker Bergmann
 * @author Yibo Wang
 */
public class SubSuiteModule extends AbstractReportModule {

    private boolean condensed;

    private int recursionDepth;

    public SubSuiteModule(int recursionDepth, boolean condensed) {
        this.condensed = condensed;
        this.recursionDepth = recursionDepth;
    }

    @Override
    public int getDashboardColSpan(ReportScope scope) {
        return 2;
    }

    public void renderDashboardView(ReportScope scope, SanityCheckSuite suite, HtmlPrintWriter out) {
        try {
            FileUtil.ensureDirectoryExists(suite.getReportFolder());
            ReportUtil.copyResourceFiles(suite.getReportFolder(), style);
            renderGroupAndCheckModules(suite, recursionDepth, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        super.close();
    }

    private void renderGroupAndCheckModules(SanityCheckSuite suite, int depth, HtmlPrintWriter out) {
        if (suite instanceof CheckHolder && containsApplicableCheck((CheckHolder) suite)) renderCheckListModule((CheckHolder) suite, out);
        if (suite instanceof SuiteHolder && containsSuitesWithApplicableChecks((SuiteHolder) suite)) renderGroupModule((SuiteHolder) suite, depth, out);
    }

    private boolean containsApplicableCheck(CheckHolder checkHolder) {
        for (SanityCheck check : checkHolder.getChecks()) if (check.getVerdict() != SanityCheckVerdict.NOT_APPLICABLE) return true;
        return false;
    }

    public boolean containsSubSuiteHolders(SuiteHolder parent) {
        for (SuiteHolder childSuite : parent.getChildSuites(SuiteHolder.class)) if (childSuite.countChecks() > 0) return true;
        return false;
    }

    protected void renderGroupModule(SuiteHolder suiteHolder, int depth, HtmlPrintWriter out) {
        String suiteName = suiteHolder.getName();
        String subGroups = localize("sub_groups");
        String title = (suiteName != null ? suiteName + " - " + subGroups : subGroups);
        out.startModule(title);
        renderSubSuitesTable(suiteHolder, out);
        if (depth != 0) {
            out.printLineBreak();
            for (SanityCheckSuite childSuite : suiteHolder.getChildSuites()) renderGroupAndCheckModules(childSuite, depth - 1, out);
        }
        out.endModule();
    }

    protected void renderCheckListModule(CheckHolder checkHolder, HtmlPrintWriter out) {
        String suiteName = checkHolder.getName();
        String checks = localize("checks");
        String title = (suiteName != null && !"checks".equalsIgnoreCase(suiteName) ? suiteName + " - " + checks : checks);
        out.startModule(title);
        renderCheckTable(checkHolder, out);
        out.endModule();
    }

    private boolean containsSuitesWithApplicableChecks(SuiteHolder suiteHolder) {
        for (SanityCheckSuite childSuite : suiteHolder.getChildSuites()) if (childSuite.countApplicableChecks() > 0) return true;
        return false;
    }

    private void renderSubSuitesTable(SuiteHolder parent, HtmlPrintWriter out) {
        out.startTable("width", "100%", "cellpadding", "3", "border", "0");
        out.startTableHead();
        out.renderRow(headerCell(localize("verdict")).withWidth("5%").withClass("trow").centered(), headerCell(localize("group")).withWidth("20%").withClass("trow").leftAligned(), headerCell(localize("defects")).withWidth("5%").withClass("trow").rightAligned(), headerCell(localize("verdicts")).withWidth("70%").withClass("trow").rightAligned());
        out.endTableHead();
        out.startTableBody();
        List<SanityCheckSuite> childSuites = new ArrayList<SanityCheckSuite>(parent.getChildSuites());
        Collections.sort(childSuites, new NameComparator());
        int maxTestCount = 0;
        for (SanityCheckSuite childSuite : childSuites) maxTestCount = Math.max(getTestCount(childSuite), maxTestCount);
        for (SanityCheckSuite child : childSuites) {
            HtmlFileImage verdictImage = context.verdictImage(child.getVerdict());
            Renderable ref = fileOrFolderRef(child);
            Renderable defectCount = (child.getDefectCount() > 0 ? new RenderableText(context.formatLong(child.getDefectCount())) : nonBreakableSpace());
            Renderable summaryBar = summaryBar(child, (100 * getTestCount(child) / (double) maxTestCount) + "%");
            out.renderRow(cell(verdictImage).centered(), cell(ref).leftAligned(), cell(defectCount).withClass("defect").rightAligned(), cell(summaryBar).withWidth("75%").leftAligned());
        }
        out.endTableBody();
        out.endTable();
    }

    private Renderable fileOrFolderRef(SanityCheckSuite target) {
        return new HtmlFileReference(target.getName(), new File(target.getReportFolder(), "index.html"));
    }

    private void renderCheckTable(SanityCheckSuite child, HtmlPrintWriter out) {
        out.startTable("width", "100%", "cellpadding", "3", "border", "0", "cellspacing", "0");
        out.renderRow(headerCell("Verdict").withWidth("5%").withClass("trow").centered(), headerCell("Check").withWidth("75%").withClass("trow").leftAligned(), headerCell("Defects").withWidth("10%").withClass("trow").rightAligned(), headerCell("Duration").withWidth("10%").withClass("trow").rightAligned());
        for (SanityCheck check : ((CheckHolder) child).getChecks()) {
            SanityCheckVerdict verdict = check.getVerdict();
            if (verdict != SanityCheckVerdict.NOT_APPLICABLE && (!condensed || verdict == SanityCheckVerdict.FAILED || verdict == SanityCheckVerdict.ERROR)) {
                HtmlFileImage verdictImage = context.verdictImage(verdict);
                String note = (check.getExecutionNote() != null ? " (" + check.getExecutionNote() + ')' : "");
                RenderableText label = new RenderableText(check.getName() + note);
                HtmlFileReference docOrDefectFileRef = docOrDefectFileRef(check, label);
                int time = check.getExecutionTime();
                out.renderRow(cell(verdictImage).withClass("trow").centered(), cell(docOrDefectFileRef).withClass("trow").leftAligned(), cell(context.longRenderable(check.getDefectCount(), nonBreakableSpace())).withClass("trow_defect").rightAligned(), cell(time > 0 ? context.formatLong(time) + " ms" : "").withClass("trow").rightAligned());
            }
        }
        out.endTable();
    }
}
