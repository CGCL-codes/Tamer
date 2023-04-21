package de.chdev.artools.loga.worker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import de.chdev.artools.loga.controller.ILogController;
import de.chdev.artools.loga.controller.MainController;

public class ParseWorker {

    private Reader reader;

    private Map<String, ILogController> controllerMap;

    private final MainController mainController;

    public ParseWorker(Reader reader, MainController mainController) {
        this.reader = reader;
        this.mainController = mainController;
        this.controllerMap = mainController.getControllerMap();
    }

    public void run() {
        try {
            BufferedReader bufReader = new BufferedReader(reader);
            String line = bufReader.readLine();
            int lineNumber = 1;
            String logType = "";
            String logTypeBak = "";
            while (line != null) {
                if (line.length() > 5) {
                    logType = line.substring(0, 6);
                } else if (logTypeBak != null && logTypeBak.length() > 0) {
                    logType = logTypeBak;
                } else {
                    System.out.println("Error in line " + lineNumber + ": " + line);
                }
                int result = chooseController(logType, line, lineNumber);
                if (result >= 0) {
                    logTypeBak = logType;
                } else if (logTypeBak != null && logTypeBak.length() > 0 && !logTypeBak.equals(logType)) {
                    chooseController(logTypeBak, line, lineNumber);
                } else {
                    System.out.println("Error in line " + lineNumber + ": " + line);
                }
                line = bufReader.readLine();
                lineNumber++;
            }
            mainController.runPostProcessing();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int chooseController(String logType, String line, int lineNumber) {
        int result = -1;
        if (controllerMap.containsKey(logType)) {
            result = controllerMap.get(logType).setLogLine(line, lineNumber);
        }
        return result;
    }
}
