package be.avezoete.boris.logfilesearcher;

import java.io.IOException;
import java.util.Date;
import be.avezoete.boris.logfilesearcher.filter.filters.ClassParseFilter;
import be.avezoete.boris.logfilesearcher.filter.filters.DateRangeParseFilter;
import be.avezoete.boris.logfilesearcher.line.KNX3GWLineParser;

public class ParseKNX3GW {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        FilesParser parser = new FilesParser(new KNX3GWLineParser());
        parser.addFilter(new DateRangeParseFilter(new Date(107, 05, 11, 7, 42), new Date(107, 05, 11, 8, 01)));
        parser.addFilter(new ClassParseFilter("COM6", false, false));
        try {
            parser.parse("D:\\KRM8\\SRT-JVAS-6YGCMH_TD-3809_KNL 8.3 outrage\\KXCorba+Logs+11-06-2007\\KXCorbaLevel_FromFRONTEND_ToKXSERVER2.log");
            parser.orderResult(LogLine.DATE_COMPARATOR);
            parser.translate(new RegexReplaceLogLineMessageTranslator("/", " "));
            parser.printResult();
            parser.printResultToFile("c:\\parser.out.log");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
