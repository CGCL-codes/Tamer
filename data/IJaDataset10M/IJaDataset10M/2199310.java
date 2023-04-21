package shared.utils;

import java.io.File;
import java.util.*;
import shared.utils.Log;
import shared.configloader.Algorithm;
import shared.configloader.ConfigParser;

public class Config {

    private String configFile;

    private String inputFile;

    private String outputFile;

    private String stopWordsFile;

    private Vector<Algorithm> algorithms = new Vector<Algorithm>();

    private Vector<String> choices = new Vector<String>();

    private String dbname;

    private String dbuser;

    private String dbpassword;

    private String dbconnection;

    private String dbdriver;

    public Vector<Algorithm> getAlgorithms() {
        return algorithms;
    }

    public void setAlgorithms(Vector<Algorithm> algorithms) {
        this.algorithms = algorithms;
    }

    public Algorithm getSpecificAlgorithm(String algName) {
        for (Algorithm currentAlgorithm : this.algorithms) {
            if (currentAlgorithm.getName().equals(algName)) {
                return currentAlgorithm;
            }
        }
        return null;
    }

    public String getConfigFile() {
        return configFile;
    }

    public void setConfigFile(String configFile) {
        this.configFile = configFile;
    }

    public String getDbdriver() {
        return dbdriver;
    }

    public void setDbdriver(String dbdriver) {
        this.dbdriver = dbdriver;
    }

    public String getDbconnection() {
        return dbconnection;
    }

    public Config(String path) throws Exception {
        Log.info("Loading config");
        this.configFile = path;
        this.loadConfig();
    }

    public void loadConfig() throws Exception {
        File file = new File(configFile);
        if (file.exists()) {
            Log.info("Found configuration file: " + file.getAbsolutePath());
            ConfigParser cp = new ConfigParser(file, this);
            cp.parseDataBaseValues();
            cp.parseAlgorithms();
            cp.parseChoices();
            cp.parseFilePaths();
        } else {
            Log.error("Missing config file. looking for: " + file.getAbsolutePath());
        }
    }

    public void addAlgorithm(Algorithm alg) {
        this.algorithms.add(alg);
    }

    public String getDbname() {
        return dbname;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    public String getDbpassword() {
        return dbpassword;
    }

    public void setDbpassword(String dbpassword) {
        this.dbpassword = dbpassword;
    }

    public String getDbuser() {
        return dbuser;
    }

    public void setDbuser(String dbuser) {
        this.dbuser = dbuser;
    }

    public String getInputFile() {
        return inputFile;
    }

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public boolean searchAlgorithm(String algname) {
        for (Algorithm alg : algorithms) {
            if (alg.getName().equals(algname)) return true;
        }
        return false;
    }

    public void setDbconnection(String dbconnection) {
        this.dbconnection = dbconnection;
    }

    public String getStopWordsFile() {
        return stopWordsFile;
    }

    public void setStopWordsFile(String stopWordsFile) {
        this.stopWordsFile = stopWordsFile;
    }

    public Vector<String> getChoices() {
        return choices;
    }

    public void setChoices(Vector<String> choices) {
        this.choices = choices;
    }

    public void addChoice(String choice) {
        this.choices.add(choice);
    }
}
