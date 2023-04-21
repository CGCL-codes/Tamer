package net.firstpartners.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;
import net.firstpartners.drools.FileRuleLoader;
import net.firstpartners.drools.SpreadSheetRuleRunner;
import net.firstpartners.drools.data.RuleSource;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.drools.compiler.DroolsParserException;

public class Rp2CommandLine {

    private static final String EXCEL_LOG_WORKSHEET_NAME = "log";

    private static final Logger log = Logger.getLogger(Rp2CommandLine.class.getName());

    private static final SpreadSheetRuleRunner commonUtils = new SpreadSheetRuleRunner(new FileRuleLoader());

    /**
	 * Usage from command line java -jar [jarName.jar]
	 * net.firstpartners.rp2.Rp2CommandLine ExcelFile OutputFile rulesfile1
	 * rulesfile2 ...
	 * 
	 * @param args
	 * @throws IOException
	 * @throws DroolsParserException
	 */
    public static void main(String[] args) throws Exception {
        if ((args == null) || (args.length < 3)) {
            log.info("Usage: java -jar [jarName.jar] net.firstpartners.rp2.Rp2CommandLine ExcelFile OutputFile rulesfile1 rulesfile2 ...");
            return;
        }
        String excelFile = args[0];
        String outputFileName = args[1];
        RuleSource ruleArgs = convertSourceToRuleArgs(args);
        FileInputStream excelInput = new FileInputStream(excelFile);
        HSSFWorkbook wb = commonUtils.callRules(excelInput, ruleArgs, EXCEL_LOG_WORKSHEET_NAME);
        deleteOutputFileIfExists(outputFileName);
        FileOutputStream excelOutput = new FileOutputStream(outputFileName);
        wb.write(excelOutput);
    }

    /**
	 * Get the 3rd and subsequent argument passed from the command line - these
	 * are the rule file names
	 * 
	 * @param commandLineArgs
	 * @return
	 */
    private static RuleSource convertSourceToRuleArgs(String[] commandLineArgs) {
        String[] ruleFileLocations = null;
        if (commandLineArgs.length > 2) {
            ruleFileLocations = new String[commandLineArgs.length - 2];
            for (int a = 0; a < ruleFileLocations.length; a++) {
                ruleFileLocations[a] = commandLineArgs[a + 2];
            }
        }
        RuleSource ruleSource = new RuleSource();
        ruleSource.setRulesLocation(ruleFileLocations);
        return ruleSource;
    }

    /**
	 * Delete the output file if it already exists
	 * 
	 * @param outputFile
	 */
    private static void deleteOutputFileIfExists(String outputFileName) {
        File outputFile = new File(outputFileName);
        if (outputFile.exists()) {
            outputFile.delete();
        }
    }
}
