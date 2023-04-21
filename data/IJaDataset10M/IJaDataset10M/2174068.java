package com.memomics.cytoscape_plugin.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class CSVParser {

    public static void main(String[] args) {
        BufferedReader bReader = null;
        try {
            bReader = new BufferedReader(new FileReader(new File("C:\\temp\\test2.csv")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line = null;
        int lineNumber = 1;
        try {
            while ((line = bReader.readLine()) != null) {
                System.out.println("" + lineNumber + ":\t" + line);
                String[] parseLine = parseLine(line);
                System.out.println("Parsed:");
                for (int i = 0; i < parseLine.length; i++) {
                    System.out.println("\t" + i + ":\t" + parseLine[i]);
                }
                lineNumber++;
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public static String[] parseLine(String line) {
        LinkedList<String> correctValues = new LinkedList<String>();
        String[] split = line.split(",");
        boolean stringOpened = false;
        String tempString = "";
        System.out.println("\tsplittedValues:");
        for (int i = 0; i < split.length; i++) {
            String chunk = split[i];
            System.out.println("\t\t" + i + ":\t" + chunk);
            boolean containsQuotation = chunk.contains("\"");
            if (!containsQuotation) {
                if (stringOpened) {
                    tempString = tempString + "," + chunk;
                } else {
                    correctValues.add(chunk);
                }
            } else {
                String[] split1 = (chunk + " ").split("\"");
                int allMArks = split1.length - 1;
                String[] split2 = chunk.split("\"\"");
                int badMarks = (split2.length - 1) * 2;
                int correctMarks = allMArks - badMarks;
                String lastString = split1[split1.length - 1];
                if (correctMarks == 2) {
                    if (split1[0].trim().equals("") && lastString.trim().equals("")) {
                        String whole = "";
                        for (int j = 1; j < split1.length - 1; j++) {
                            whole = whole + split1[j];
                        }
                        correctValues.add(whole);
                    } else {
                        System.err.println("Incorrect chunk");
                    }
                } else {
                    if (correctMarks == 1) {
                        if (split1[0].trim().equals("")) {
                            stringOpened = true;
                            for (int j = 1; j < split1.length - 2; j++) {
                                String string = split1[j];
                                if (string.equals("")) {
                                    string = "\"";
                                }
                                tempString = tempString + string;
                            }
                            tempString = tempString + lastString.subSequence(0, lastString.length() - 1);
                            tempString = tempString + ",";
                        } else if (lastString.trim().equals("")) {
                            for (int j = 0; j < split1.length - 1; j++) {
                                String string = split1[j];
                                if (string.equals("")) {
                                    string = "\"";
                                }
                                tempString = tempString + string;
                            }
                            stringOpened = false;
                            correctValues.add(tempString);
                            tempString = "";
                        }
                    } else {
                        tempString = tempString + chunk.replaceAll("\"\"", "\"");
                    }
                }
            }
        }
        return correctValues.toArray(new String[] {});
    }
}
