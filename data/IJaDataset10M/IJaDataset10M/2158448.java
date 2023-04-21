package com.googlecode.g2re.servlet;

import com.googlecode.g2re.PDFReportBuilder;

/**
 *
 * @author Brad Rydzewski
 */
public class PDFReportView implements ServletView {

    public static final PDFReportView INSTANCE = new PDFReportView();

    protected PDFReportView() {
    }

    @Override
    public void build(ServletViewArgs args) {
        try {
            String fileName = args.getReportName() + ".pdf";
            String contentType = "application/pdf";
            args.getResponse().setContentType(contentType);
            args.getResponse().setHeader("Content-Disposition", "attachment; filename=" + fileName);
            PDFReportBuilder.build(args.getReportFile(), args.getParams(), args.getOutputStream());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
