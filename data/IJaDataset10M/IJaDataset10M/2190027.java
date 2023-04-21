package com.googlecode.bdoc.report;

import com.googlecode.bdoc.BDocConfig;
import com.googlecode.bdoc.doc.domain.BDoc;
import com.googlecode.bdoc.doc.report.BDocMacroHelper;

public class ProjectInfoFrame extends AbstractBDocReportContent {

    public ProjectInfoFrame(BDoc bdoc, BDocConfig bdocConfig) {
        super("project_info_frame.ftl", bdocConfig);
        put("bdoc", bdoc);
        put("project", bdoc.getProject());
        put("bdocMacroHelper", new BDocMacroHelper(bdocConfig));
    }
}
