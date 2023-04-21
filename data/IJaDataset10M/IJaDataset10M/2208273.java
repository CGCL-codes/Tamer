package edu.ucla.stat.SOCR.analyses.example;

import java.lang.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.util.StringTokenizer;
import edu.ucla.stat.SOCR.analyses.data.Data;
import edu.ucla.stat.SOCR.analyses.model.AnalysisType;

/** Example data generator for ANOVA & other statistical applets. */
public class TwoPairedSignTestExamples extends ExampleData {

    public String[][] example = new String[1][1];

    public String[] columnNames = new String[1];

    String newln = System.getProperty("line.separator");

    public JTable dataTable;

    private final String DOT = ".";

    private static String dataSource = "";

    public static boolean[] availableExamples = TwoPairedTExamples.availableExamples;

    /** Constructor method for simple data generation for regression/correlation tests*/
    public TwoPairedSignTestExamples() {
        this.dataTable = (new TwoPairedTExamples()).getRandomExample();
    }

    /** Constructor method for sampel data for ANOVA analysis.*/
    public TwoPairedSignTestExamples(int analysisType, int exampleID) {
        this.dataTable = (new TwoPairedTExamples(analysisType, exampleID)).getExample(exampleID);
    }

    /** returns a JTable object containing the Example Data */
    public JTable getExample() {
        return dataTable;
    }

    public static String getExampleSource() {
        return dataSource;
    }
}
