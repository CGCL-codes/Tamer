package es.optsicom.lib.experiment.analysis.test;

import java.io.File;
import java.io.IOException;
import es.optsicom.lib.analysis.table.ComplexTable;
import es.optsicom.lib.analysis.table.SheetTable;
import es.optsicom.lib.analysis.table.SimpleSheetCreator;
import es.optsicom.lib.analysis.table.TextTabSheetFormatter;
import es.optsicom.lib.experiment.Event;
import es.optsicom.lib.tablecreator.DevMethodComp;
import es.optsicom.lib.tablecreator.LastEventRP;
import es.optsicom.lib.tablecreator.NumBestMethodComp;
import es.optsicom.lib.tablecreator.ScoreMethodComp;
import es.optsicom.lib.tablecreator.TableCreator;

public class TableCalculatorTest {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        TableCreator tabCalc = new TableCreator();
        File resultsDir = new File("/media/sda3/Data/ws/workspaceTesis/MaxMinDiversityProblem/ExpTable4a");
        tabCalc.loadResultsDir(resultsDir);
        tabCalc.addSimpleRawProcessor(new LastEventRP(Event.OBJ_VALUE_EVENT), new DevMethodComp(), new NumBestMethodComp(), new ScoreMethodComp());
        tabCalc.addSimpleRawProcessor(new LastEventRP(Event.OBJ_VALUE_EVENT).setTimelimit(10000), new DevMethodComp(), new NumBestMethodComp(), new ScoreMethodComp());
        ComplexTable table = tabCalc.buildTable();
        SheetTable st = new SimpleSheetCreator().createSheet(table);
        TextTabSheetFormatter f = new TextTabSheetFormatter();
        f.format(st);
        System.out.println(f.getFormattedTable());
    }
}
