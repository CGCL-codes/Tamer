package es.optsicom.lib.analysis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import es.optsicom.lib.analysis.table.AnalysisException;
import es.optsicom.lib.analysis.table.Cell;
import es.optsicom.lib.analysis.table.CompactSheetCreator;
import es.optsicom.lib.analysis.table.ComplexTable;
import es.optsicom.lib.analysis.table.NumberFormat;
import es.optsicom.lib.analysis.table.SheetTable;
import es.optsicom.lib.analysis.table.SimpleSheetCreator;
import es.optsicom.lib.analysis.table.TextSpaceSheetFormatter;
import es.optsicom.lib.analysis.table.TextTabSheetFormatter;
import es.optsicom.lib.analysis.table.NumberFormat.NumberType;
import es.optsicom.lib.experiment.Event;
import es.optsicom.lib.experiment.Execution;
import es.optsicom.lib.experiment.IMExecutions;
import es.optsicom.lib.experiment.IMExecutionsSaverLoader;
import es.optsicom.lib.experiment.MethodDescription;
import es.optsicom.lib.tablecreator.Alias;
import es.optsicom.lib.tablecreator.BestKnownValueProvider;
import es.optsicom.lib.tablecreator.DevMethodComp;
import es.optsicom.lib.tablecreator.GapRP;
import es.optsicom.lib.tablecreator.LastEventRP;
import es.optsicom.lib.tablecreator.NumBestMethodComp;
import es.optsicom.lib.tablecreator.ScoreMethodComp;
import es.optsicom.lib.tablecreator.SummarizeColumn;
import es.optsicom.lib.tablecreator.TableColumn;
import es.optsicom.lib.tablecreator.TableCreator;
import es.optsicom.lib.tablecreator.LastEventRP.Source;
import es.optsicom.lib.tablecreator.filter.ElementFilter;
import es.optsicom.lib.util.BestMode;
import es.optsicom.lib.util.RelativizeMode;
import es.optsicom.lib.util.Strings;
import es.optsicom.lib.util.SummarizeMode;

public class CommonExpAnalysis {

    public static void showComparationMethodTable(File resultsDir) {
        showComparationMethodTable(resultsDir, BestMode.MAX_IS_BEST);
    }

    /**
	 * @param resultsDir
	 * @param max_is_best
	 */
    public static void showComparationMethodTable(File resultsDir, BestMode max_is_best) {
        showComparationMethodTable(resultsDir, max_is_best, null, null);
    }

    public static void showTempLineMethodTable(File resultsDir, BestMode problemType) throws IOException, ClassNotFoundException {
        TempEvolutionFigureTableCreator tempEvo = new TempEvolutionFigureTableCreator();
        tempEvo.setResultsDir(resultsDir);
        tempEvo.setProblemType(problemType);
        ComplexTable table = tempEvo.createComplexTable();
        SheetTable sheet = new SimpleSheetCreator().createSheet(table);
        TextTabSheetFormatter f = new TextTabSheetFormatter();
        f.format(sheet);
        System.out.println(f.getFormattedTable());
    }

    public static void showTempLineMethodTable(File resultsDir, BestMode problemType, RelativizeMode relativizeMode) throws IOException, ClassNotFoundException {
        TempEvolutionFigureTableCreator tempEvo = new TempEvolutionFigureTableCreator();
        tempEvo.setProblemType(problemType);
        tempEvo.setResultsDir(resultsDir);
        ComplexTable table = tempEvo.createComplexTable(relativizeMode);
        SheetTable sheet = new SimpleSheetCreator().createSheet(table);
        TextTabSheetFormatter f = new TextTabSheetFormatter();
        f.format(sheet);
        System.out.println(f.getFormattedTable());
    }

    public static void showTempLineMethodTable(File resultsDir, BestMode problemType, long maxTime, long stepWidth, List<Alias> methodAliases) throws IOException, ClassNotFoundException {
        TempEvolutionFigureTableCreator tempEvo = new TempEvolutionFigureTableCreator();
        tempEvo.setProblemType(problemType);
        tempEvo.setMaxTime(maxTime);
        tempEvo.setStepWidth(stepWidth);
        tempEvo.setMethodAliases(methodAliases);
        tempEvo.setResultsDir(resultsDir);
        ComplexTable table = tempEvo.createComplexTable();
        SheetTable sheet = new SimpleSheetCreator().createSheet(table);
        TextTabSheetFormatter f = new TextTabSheetFormatter();
        f.format(sheet);
        System.out.println(f.getFormattedTable());
        TableCreator tabCalc = new TableCreator();
        tabCalc.loadResultsDir(resultsDir);
    }

    public static void showMethodNames(File resultDir) {
        try {
            IMExecutionsSaverLoader loader = IMExecutionsSaverLoader.getInstance();
            for (MethodDescription method : loader.loadIMExecutionsList(resultDir).get(0).getExecsMap().keySet()) {
                System.out.println("Method Id: " + method.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showComparationMethodTable(File resultsDir, BestMode problemType, List<Alias> rowAliases, List<Alias> colAliases) {
        showComparationMethodTable(resultsDir, problemType, rowAliases, colAliases, null, null);
    }

    public static void showComparationMethodTable(File resultsDir, BestMode problemType, List<Alias> rowAliases, List<Alias> colAliases, ElementFilter instanceFilter, ElementFilter methodFilter) {
        showComparationMethodTable(resultsDir, problemType, rowAliases, colAliases, instanceFilter, methodFilter, SummarizeMode.MAX, RelativizeMode.EXPERIMENT, null);
    }

    public static void showComparationMethodTable(File resultsDir, BestMode problemType, List<Alias> rowAliases, List<Alias> colAliases, ElementFilter instanceFilter, ElementFilter methodFilter, SummarizeMode summarizeMode, RelativizeMode relativizeMode) {
        showComparationMethodTable(resultsDir, problemType, rowAliases, colAliases, instanceFilter, methodFilter, summarizeMode, relativizeMode, null);
    }

    public static void showComparationMethodTable(File resultsDir, BestMode problemType, List<Alias> rowAliases, List<Alias> colAliases, ElementFilter instanceFilter, ElementFilter methodFilter, SummarizeMode summarizeMode, RelativizeMode relativizeMode, BestKnownValueProvider bkvProvider) {
        try {
            TableCreator tabCalc = new TableCreator();
            tabCalc.loadResultsDir(resultsDir);
            tabCalc.setBkvProvider(bkvProvider);
            LastEventRP lastEventRP = new LastEventRP(Event.OBJ_VALUE_EVENT);
            lastEventRP.setSummarizeMode(summarizeMode);
            tabCalc.addSimpleRawProcessor(lastEventRP, new DevMethodComp(problemType), new NumBestMethodComp(problemType), new ScoreMethodComp(problemType));
            tabCalc.addSimpleRawProcessor(new LastEventRP(Event.FINISH_TIME_EVENT).setSource(Source.TIMESTAMP), new SummarizeColumn(SummarizeMode.AVERAGE, NumberType.TIME));
            tabCalc.setMethodFilter(methodFilter);
            tabCalc.setInstanceFilter(instanceFilter);
            ComplexTable table = tabCalc.buildTable(relativizeMode);
            if (rowAliases != null) {
                table.setRowAliases(rowAliases);
            }
            if (colAliases != null) {
                table.setColumnAliases(colAliases);
            }
            SheetTable st = new SimpleSheetCreator().createSheet(table);
            TextTabSheetFormatter f2 = new TextTabSheetFormatter();
            f2.format(st);
            System.out.println(f2.getFormattedTable());
        } catch (Exception e) {
            throw new AnalysisException(e);
        }
    }

    public static void showAllInstancesExecs(File dir) {
        try {
            IMExecutionsSaverLoader loader = IMExecutionsSaverLoader.getInstance();
            for (IMExecutions imExecs : loader.loadIMExecutionsList(dir)) {
                System.out.println("Instance: " + imExecs.getInstanceDescription().getId());
                for (Collection<Execution> execs : imExecs.getExecsMap().values()) {
                    showResults(execs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showAllInstancesExecsBestValues(File dir) {
        try {
            IMExecutionsSaverLoader loader = IMExecutionsSaverLoader.getInstance();
            for (IMExecutions imExecs : loader.loadIMExecutionsList(dir)) {
                System.out.println("Instance: " + imExecs.getInstanceDescription().getId());
                for (Collection<Execution> execs : imExecs.getExecsMap().values()) {
                    showResultsBestValue(execs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showAllInstancesExecsUpperBounds(File dir) {
        try {
            IMExecutionsSaverLoader loader = IMExecutionsSaverLoader.getInstance();
            for (IMExecutions imExecs : loader.loadIMExecutionsList(dir)) {
                System.out.println("Instance: " + imExecs.getInstanceDescription().getId());
                for (Collection<Execution> execs : imExecs.getExecsMap().values()) {
                    showResultsUpperBound(execs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showBestValueByMethodAndInstance(File dir, long millis) {
        showBestValueByMethodAndInstance(dir, millis, null);
    }

    public static void showBestValueByMethodAndInstance(File dir, long millis, List<Alias> methodAliases) {
        try {
            IMExecutionsSaverLoader loader = IMExecutionsSaverLoader.getInstance();
            List<IMExecutions> imExecutionsList = loader.loadIMExecutionsList(dir);
            for (MethodDescription methodDescription : imExecutionsList.get(0).getMethods()) {
                showBestValueByMethodAndInstance(dir, methodDescription.getId(), millis, methodAliases);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void showBestValueByMethodAndInstance(File dir, String methodName, long timelimit) {
        showBestValueByMethodAndInstance(dir, methodName, timelimit, null);
    }

    public static void showBestValueByMethodAndInstance(File dir, String methodName, long timelimit, List<Alias> methodAliases) {
        try {
            IMExecutionsSaverLoader loader = IMExecutionsSaverLoader.getInstance();
            List<IMExecutions> imExecutionsList = loader.loadIMExecutionsList(dir);
            List<String> instanceOrder = naturalOrdering(imExecutionsList);
            if (methodName == null) {
                methodName = imExecutionsList.get(0).getMethods().get(0).getId();
            }
            String methodAlias = methodName;
            if (methodAliases != null) {
                for (Alias alias : methodAliases) {
                    if (alias.getFrom().equals(methodName)) {
                        methodAlias = alias.getTo();
                        break;
                    }
                }
            }
            System.out.println("Method: " + methodAlias);
            SheetTable sheet = new SheetTable(imExecutionsList.size() + 5, 5);
            int baseCell = 3;
            sheet.setCell(baseCell, 0, new Cell("Instance"));
            sheet.setCell(baseCell, 1, new Cell("Value"));
            for (IMExecutions imExecs : imExecutionsList) {
                int numCell = instanceOrder.indexOf(imExecs.getInstanceDescription().getId()) + baseCell + 1;
                sheet.setCell(numCell, 0, new Cell(imExecs.getInstanceDescription().getId()));
                Execution exec = null;
                if (methodName == null) {
                    exec = imExecs.getExecsMap().values().iterator().next().get(0);
                } else {
                    for (Map.Entry<MethodDescription, List<Execution>> entry : imExecs.getExecsMap().entrySet()) {
                        if (entry.getKey().getId().equals(methodName)) {
                            exec = entry.getValue().get(0);
                            break;
                        }
                    }
                }
                if (timelimit != -1) {
                    Event objValueEvent = exec.getLastEventNotAfter(Event.OBJ_VALUE_EVENT, timelimit);
                    if (objValueEvent.getTimestamp() <= timelimit) {
                        sheet.setCell(numCell, 1, new Cell(objValueEvent.getValue(), new NumberFormat(NumberType.DECIMAL, 4)));
                    }
                } else {
                    if (exec != null) {
                        Event objValueEvent = exec.getLastEvent(Event.OBJ_VALUE_EVENT);
                        sheet.setCell(numCell, 1, new Cell(objValueEvent.getValue(), new NumberFormat(NumberType.DECIMAL, 4)));
                    }
                }
            }
            TextTabSheetFormatter sf2 = new TextTabSheetFormatter();
            sf2.format(sheet);
            System.out.println(sf2.getFormattedTable());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showAllInstancesValues(File dir) {
        try {
            IMExecutionsSaverLoader loader = IMExecutionsSaverLoader.getInstance();
            List<IMExecutions> imExecutionsList = loader.loadIMExecutionsList(dir);
            for (MethodDescription methodDescription : imExecutionsList.get(0).getMethods()) {
                showAllInstancesValues(dir, methodDescription.getId());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void showAllInstancesValues(File dir, String methodName) {
        try {
            IMExecutionsSaverLoader loader = IMExecutionsSaverLoader.getInstance();
            List<IMExecutions> imExecutionsList = loader.loadIMExecutionsList(dir);
            List<String> instanceOrder = naturalOrdering(imExecutionsList);
            if (methodName == null) {
                methodName = imExecutionsList.get(0).getMethods().get(0).getId();
            }
            System.out.println("Method: " + methodName);
            System.out.println();
            SheetTable sheet = new SheetTable(imExecutionsList.size() + 1, 5);
            sheet.setCell(0, 0, new Cell("Instance"));
            sheet.setCell(0, 1, new Cell("Solution"));
            sheet.setCell(0, 2, new Cell("Value"));
            sheet.setCell(0, 3, new Cell("CPU Time"));
            for (IMExecutions imExecs : imExecutionsList) {
                int numCell = instanceOrder.indexOf(imExecs.getInstanceDescription().getId()) + 1;
                sheet.setCell(numCell, 0, new Cell(imExecs.getInstanceDescription().getId()));
                sheet.setCell(numCell, 4, new Cell(imExecs.getInstanceDescription().getProperties().toString()));
                Execution exec = null;
                if (methodName == null) {
                    exec = imExecs.getExecsMap().values().iterator().next().get(0);
                } else {
                    for (Map.Entry<MethodDescription, List<Execution>> entry : imExecs.getExecsMap().entrySet()) {
                        if (entry.getKey().getId().equals(methodName)) {
                            exec = entry.getValue().get(0);
                            break;
                        }
                    }
                }
                Event objValueEvent = exec.getLastEvent(Event.OBJ_VALUE_EVENT);
                sheet.setCell(numCell, 1, new Cell(exec.getLastEventValue(Event.SOLUTION_EVENT)));
                sheet.setCell(numCell, 2, new Cell(objValueEvent.getValue(), new NumberFormat(NumberType.DECIMAL, 4)));
                sheet.setCell(numCell, 3, new Cell(objValueEvent.getTimestamp()));
            }
            TextSpaceSheetFormatter sf = new TextSpaceSheetFormatter();
            sf.format(sheet);
            System.out.println(sf.getFormattedTable());
            TextTabSheetFormatter sf2 = new TextTabSheetFormatter();
            sf2.format(sheet);
            System.out.println(sf2.getFormattedTable());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * @param imExecutionsList
	 * @return
	 */
    public static List<String> naturalOrdering(List<IMExecutions> imExecutionsList) {
        List<String> instanceOrder = new ArrayList<String>();
        for (IMExecutions imExecs : imExecutionsList) {
            instanceOrder.add(imExecs.getInstanceDescription().getId());
        }
        Collections.sort(instanceOrder, Strings.getNaturalComparator());
        return instanceOrder;
    }

    public static void showResults(Collection<Execution> execs) {
        for (Execution exec : execs) {
            System.out.println(exec);
        }
    }

    public static void showResultsBestValue(Collection<Execution> execs) {
        for (Execution exec : execs) {
            Double value = (Double) exec.getLastEventValue(Event.OBJ_VALUE_EVENT);
            System.out.format(exec.getMethod() + "\t%d\r\n", (long) value.doubleValue());
        }
    }

    public static void showResultsUpperBound(Collection<Execution> execs) {
        for (Execution exec : execs) {
            Double value = (Double) exec.getLastEventValue("upperBound");
            System.out.format(exec.getMethod() + "\t%d\r\n", (long) value.doubleValue());
        }
    }

    public static void showAnalysisTable(String objValueEvent, File dir, ElementFilter methodFilter, SummarizeMode max) throws IOException, ClassNotFoundException {
        TableCreator tabCalc = new TableCreator();
        tabCalc.loadResultsDir(dir);
        tabCalc.addSimpleRawProcessor(new LastEventRP(Event.OBJ_VALUE_EVENT), new DevMethodComp(), new NumBestMethodComp(), new ScoreMethodComp());
        tabCalc.setMethodFilter(methodFilter);
        ComplexTable table = tabCalc.buildTable();
        CompactSheetCreator csc = new CompactSheetCreator();
        SheetTable sheet = csc.createSheet(table);
        TextSpaceSheetFormatter sf = new TextSpaceSheetFormatter();
        sf.format(sheet);
        System.out.println(sf.getFormattedTable());
    }

    public static void showExactTimesTable(File dir) {
        try {
            IMExecutionsSaverLoader loader = IMExecutionsSaverLoader.getInstance();
            List<IMExecutions> imExecutionsList = loader.loadIMExecutionsList(dir);
            List<String> instanceOrder = new ArrayList<String>();
            for (IMExecutions imExecs : imExecutionsList) {
                instanceOrder.add(imExecs.getInstanceDescription().getId());
            }
            Collections.sort(instanceOrder, Strings.getNaturalComparator());
            SheetTable sheet = new SheetTable(imExecutionsList.size() + 1, 10);
            sheet.setCell(0, 0, new Cell("Instance"));
            sheet.setCell(0, 1, new Cell("Value"));
            int numRow = 1;
            List<String> methodOrder = new ArrayList<String>();
            for (MethodDescription md : imExecutionsList.get(0).getMethods()) {
                sheet.setCell(0, numRow, new Cell(md.getId()));
                methodOrder.add(md.getId());
                numRow += 2;
            }
            for (IMExecutions imExecs : imExecutionsList) {
                int numCell = instanceOrder.indexOf(imExecs.getInstanceDescription().getId()) + 1;
                sheet.setCell(numCell, 0, new Cell(imExecs.getInstanceDescription().getId()));
                for (Entry<MethodDescription, List<Execution>> md : imExecs.getExecsMap().entrySet()) {
                    numRow = 1 + 2 * methodOrder.indexOf(md.getKey().getId());
                    Execution exec = md.getValue().get(0);
                    Event objValueEvent = exec.getLastEvent(Event.OBJ_VALUE_EVENT);
                    sheet.setCell(numCell, numRow, new Cell(objValueEvent.getTimestamp()));
                    sheet.setCell(numCell, numRow + 1, new Cell(objValueEvent.getValue(), new NumberFormat(NumberType.DECIMAL, 4)));
                }
            }
            TextSpaceSheetFormatter sf = new TextSpaceSheetFormatter();
            sf.format(sheet);
            System.out.println(sf.getFormattedTable());
            TextTabSheetFormatter sf2 = new TextTabSheetFormatter();
            sf2.format(sheet);
            System.out.println(sf2.getFormattedTable());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showExactTable(File resultsDir, BestMode problemType) {
        showExactTable(resultsDir, problemType, null, null);
    }

    public static void showExactTable(File resultsDir, BestMode bestMode, List<Alias> methodAlias, List<Alias> rowAliases) {
        showExactTable(resultsDir, bestMode, null, methodAlias, rowAliases, false, true, true);
    }

    public static void showExactTable(File resultsDir, BestMode bestMode, ElementFilter iFilter, List<Alias> methodAlias, List<Alias> rowAliases) {
        showExactTable(resultsDir, bestMode, iFilter, methodAlias, rowAliases, false, true, true);
    }

    public static void showExactTable(File resultsDir, BestMode bestMode, ElementFilter iFilter, List<Alias> methodAlias, List<Alias> rowAliases, boolean transposed, boolean showRowTitles, boolean showColumnTitles) {
        try {
            TableCreator tabCalc = new TableCreator();
            tabCalc.loadResultsDir(resultsDir);
            if (iFilter != null) {
                tabCalc.setInstanceFilter(iFilter);
            }
            tabCalc.addMultipleRawProcessor(new GapRP(), new TableColumn[] { new SummarizeColumn(SummarizeMode.AVERAGE, NumberType.PERCENT) }, new TableColumn[] { new SummarizeColumn(SummarizeMode.SUM, NumberType.INTEGER) });
            tabCalc.addSimpleRawProcessor(new LastEventRP(Event.FINISH_TIME_EVENT).setSource(Source.TIMESTAMP), new SummarizeColumn(SummarizeMode.AVERAGE, NumberType.DECIMAL));
            ComplexTable table = tabCalc.buildTable();
            table.setRowAliases(methodAlias);
            table.setColumnAliases(rowAliases);
            SimpleSheetCreator simpleSheetCreator = new SimpleSheetCreator();
            simpleSheetCreator.setShowColumnTitles(showColumnTitles);
            simpleSheetCreator.setShowRowTitles(showRowTitles);
            SheetTable st = simpleSheetCreator.createSheet(table);
            TextTabSheetFormatter f2 = new TextTabSheetFormatter();
            f2.setNumTabsIdentation(2);
            f2.format(st);
            System.out.println(f2.getFormattedTable());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
