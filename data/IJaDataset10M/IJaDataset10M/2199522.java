package net.sf.genedator;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JProgressBar;
import net.sf.genedator.plugin.PluginDataGenerator;
import net.sf.genedator.writers.DataWriter;

public class GeneratorRunner {

    private static final int MAX_RECORDS_OF_GENERATED_CHUNK = 1000000;

    public static void run(DataWriter writer, int numOfRecords, PluginDataGenerator... dataGenerators) {
        int numOfGenerators = dataGenerators.length;
        int numOfElements = numOfGenerators * numOfRecords;
        if (numOfElements > MAX_RECORDS_OF_GENERATED_CHUNK) {
            int numOfRecordsInSingleIteration = MAX_RECORDS_OF_GENERATED_CHUNK / numOfGenerators;
            int numberOfSteps = numOfRecords / numOfRecordsInSingleIteration;
            int restOfRecords = numOfRecords - (numOfRecordsInSingleIteration * numberOfSteps);
            for (int i = 0; i < numberOfSteps; i++) {
                List<String[]> columns = new ArrayList<String[]>();
                String[] column = null;
                for (int j = 0; j < numOfGenerators; j++) {
                    column = dataGenerators[j].generate(numOfRecordsInSingleIteration, (i + 1));
                    columns.add(column);
                }
                List<String[]> list = getRecordsFromColumns(columns);
                writer.saveData(list);
            }
            List<String[]> columns = new ArrayList<String[]>();
            String[] column = null;
            for (int j = 0; j < numOfGenerators; j++) {
                column = dataGenerators[j].generate(restOfRecords, (numberOfSteps + 1));
                columns.add(column);
            }
            List<String[]> list = getRecordsFromColumns(columns);
            writer.saveData(list);
        } else {
            System.out.println("Generating...");
            generate(writer, numOfRecords, dataGenerators);
        }
    }

    public static void run(DataWriter writer, int numOfRecords, JProgressBar progressBar, PluginDataGenerator... dataGenerators) {
        int numOfGenerators = dataGenerators.length;
        int numOfElements = numOfGenerators * numOfRecords;
        if (numOfElements > MAX_RECORDS_OF_GENERATED_CHUNK) {
            int numOfRecordsInSingleIteration = MAX_RECORDS_OF_GENERATED_CHUNK / numOfGenerators;
            int numberOfSteps = numOfRecords / numOfRecordsInSingleIteration;
            int restOfRecords = numOfRecords - (numOfRecordsInSingleIteration * numberOfSteps);
            progressBar.setMaximum(numberOfSteps);
            for (int i = 0; i < numberOfSteps; i++) {
                List<String[]> columns = new ArrayList<String[]>();
                String[] column = null;
                for (int j = 0; j < numOfGenerators; j++) {
                    column = dataGenerators[j].generate(numOfRecordsInSingleIteration, (i + 1));
                    columns.add(column);
                }
                List<String[]> list = getRecordsFromColumns(columns);
                writer.saveData(list);
                progressBar.setValue(i);
            }
            List<String[]> columns = new ArrayList<String[]>();
            String[] column = null;
            for (int j = 0; j < numOfGenerators; j++) {
                column = dataGenerators[j].generate(restOfRecords, (numberOfSteps + 1));
                columns.add(column);
            }
            List<String[]> list = getRecordsFromColumns(columns);
            writer.saveData(list);
            progressBar.setValue(numberOfSteps);
        } else {
            System.out.println("Generating...");
            generate(writer, numOfRecords, progressBar, dataGenerators);
        }
    }

    private static void generate(DataWriter writer, int numOfRecords, PluginDataGenerator... dataGenerators) {
        int numOfGens = dataGenerators.length;
        List<String[]> columns = new ArrayList<String[]>();
        String[] column = null;
        for (int j = 0; j < numOfGens; j++) {
            column = dataGenerators[j].generate(numOfRecords);
            columns.add(column);
        }
        List<String[]> list = getRecordsFromColumns(columns);
        writer.saveData(list);
    }

    private static void generate(DataWriter writer, int numOfRecords, JProgressBar progressBar, PluginDataGenerator... dataGenerators) {
        int numOfGens = dataGenerators.length;
        List<String[]> columns = new ArrayList<String[]>();
        String[] column = null;
        int part = numOfRecords / numOfGens;
        int val = 0;
        progressBar.setValue(val);
        for (int j = 0; j < numOfGens; j++) {
            column = dataGenerators[j].generate(numOfRecords);
            columns.add(column);
            val += part;
            progressBar.setValue(val);
        }
        progressBar.setValue(numOfRecords);
        List<String[]> list = getRecordsFromColumns(columns);
        writer.saveData(list);
    }

    private static List<String[]> getRecordsFromColumns(List<String[]> columns) {
        int len = columns.get(0).length;
        int recSize = columns.size();
        List<String[]> records = new ArrayList<String[]>(len);
        for (int i = 0; i < len; i++) {
            String[] record = new String[recSize];
            for (int j = 0; j < recSize; j++) {
                record[j] = columns.get(j)[i];
            }
            records.add(record);
        }
        return records;
    }
}
