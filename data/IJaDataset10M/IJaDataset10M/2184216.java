package es.optsicom.lib.tablecreator;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import es.optsicom.lib.InstanceDescription;
import es.optsicom.lib.analysis.table.ComplexTable;
import es.optsicom.lib.analysis.table.NumberFormat.NumberType;
import es.optsicom.lib.experiment.Event;
import es.optsicom.lib.experiment.IMExecutions;
import es.optsicom.lib.experiment.IMExecutionsSaverLoader;
import es.optsicom.lib.tablecreator.LastEventRP.Source;
import es.optsicom.lib.util.RelativizeMode;
import es.optsicom.lib.util.SummarizeMode;

/**
 * This class builds a table per instance using {@link TableCreator} to build each table.
 * 
 * @author Patxi
 *
 */
public class MultipleTableCreator {

    private File resultsDir;

    public MultipleTableCreator(File resultsDir) {
        this.resultsDir = resultsDir;
    }

    /**
	 * Loads the experiment results and creates a {@link ComplexTable} per instance. 
	 * @return A mapping between each instance and its corresponding complex table
	 */
    public Map<InstanceDescription, ComplexTable> buildTables() {
        return buildTables(RelativizeMode.EXPERIMENT);
    }

    /**
	 * Loads the experiment results and creates a {@link ComplexTable} per instance. 
	 * @return A mapping between each instance and its corresponding complex table
	 */
    public Map<InstanceDescription, ComplexTable> buildTables(RelativizeMode relativizeMode) {
        try {
            List<IMExecutions> executionsList = IMExecutionsSaverLoader.getInstance().loadIMExecutionsList(resultsDir);
            Map<InstanceDescription, ComplexTable> instanceTables = new HashMap<InstanceDescription, ComplexTable>();
            for (IMExecutions imExecs : executionsList) {
                TableCreator tableCreator = new TableCreator();
                tableCreator.setImExcesList(Arrays.asList(imExecs));
                tableCreator.addSimpleRawProcessor(new LastEventRP(Event.OBJ_VALUE_EVENT), new SummarizeColumn(SummarizeMode.AVERAGE, NumberType.DECIMAL), new DevMethodComp(), new NumBestMethodComp(), new ScoreMethodComp());
                tableCreator.addSimpleRawProcessor(new LastEventRP(Event.FINISH_TIME_EVENT).setSource(Source.TIMESTAMP), new SummarizeColumn(SummarizeMode.AVERAGE, NumberType.DECIMAL));
                ComplexTable table = tableCreator.buildTable(relativizeMode);
                instanceTables.put(imExecs.getInstanceDescription(), table);
            }
            return instanceTables;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        throw new Error();
    }
}
