package com.cyrusinnovation.prefontaine.listener;

import com.cyrusinnovation.prefontaine.file.FileSystemFacade;
import com.cyrusinnovation.prefontaine.file.RegularFile;
import com.cyrusinnovation.prefontaine.file.Writer;
import com.cyrusinnovation.prefontaine.result.ReportResultHolder;
import com.cyrusinnovation.prefontaine.result.SingleTestResult;
import com.cyrusinnovation.prefontaine.result.TestResult;
import com.cyrusinnovation.prefontaine.result.report.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class AggregateReportListener implements RunnerEventListener {

    private final FileSystemFacade fs;

    private final ReportResultHolder resultHolder;

    private final Writer errorWriter;

    public static final String RESULTS_FILE_NAME = "fit-results.html";

    public AggregateReportListener(FileSystemFacade fileSystemFacade, ReportResultHolder reportResultHolder, Writer writer) {
        this.fs = fileSystemFacade;
        this.resultHolder = reportResultHolder;
        this.errorWriter = writer;
    }

    public void onFileStarted(RegularFile file) {
    }

    public void onFileCompleted(RegularFile file, TestResult testResult) {
        resultHolder.addResult(new SingleTestResult(fs.pathRelativeToRoot(file), testResult.getSummaryValue()));
    }

    public void onRunStarted() {
    }

    public void onRunCompleted() {
        final String outputRoot = fs.getOutputRoot();
        final String outputFilePath = outputRoot + RESULTS_FILE_NAME;
        final HtmlReportBuilder builder = new HtmlReportBuilderImpl(resultHolder, new HtmlReportTableBuilder(new HtmlReportRowBuilderImpl(outputRoot, new URIBuilderImpl())));
        final File outputFile = new File(outputFilePath);
        PrintWriter resultFileWriter = null;
        try {
            outputFile.createNewFile();
            resultFileWriter = new PrintWriter(outputFile);
            builder.buildHtml(resultFileWriter, fs);
            String fileURI = new URIBuilderImpl().buildFileURIForPath(outputFilePath);
            errorWriter.println("View results: " + fileURI);
        } catch (IOException e) {
            errorWriter.printError("Failed to create output file " + outputFile);
            errorWriter.printError("Reason: " + e.getMessage());
        } finally {
            if (resultFileWriter != null) resultFileWriter.close();
        }
    }
}
