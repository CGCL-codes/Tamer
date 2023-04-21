package org.bresearch.websec.app;

import java.io.File;
import org.bresearch.websec.utils.FileUtil;
import org.bresearch.websec.utils.botlist.report.IReport;
import org.bresearch.websec.utils.botlist.report.ReportDocument;
import org.bresearch.websec.utils.botlist.report.ReportModule;
import org.bresearch.websec.utils.botlist.report.ReportTermsDocument;
import org.bresearch.websec.utils.botlist.report.ReportTermsModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Analyze file.
 * 
 * @author bbrown
 *
 */
public class StatsFileApp {

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("usage: StatsFileApp <input document>");
            throw new RuntimeException("Invalid arguments, please provide the input text file to process.  usage: StatsFileApp <input document>");
        }
        System.out.println("Loading file, " + args[0]);
        FileUtil util = new FileUtil();
        String data = util.readLinesRaw(new File(args[0]));
        final Injector injector = Guice.createInjector(new ReportModule(data, true));
        final IReport report = injector.getInstance(ReportDocument.class);
        final Injector injectorTerms = Guice.createInjector(new ReportTermsModule(data));
        final ReportTermsDocument reportTerms = injectorTerms.getInstance(ReportTermsDocument.class);
        reportTerms.setNumTopWords(2000);
        System.out.println();
        System.out.println(report.toReport());
        System.out.println(reportTerms.toReport());
        System.out.println("Done");
    }
}
