package neissmodel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import neissmodel.data_access.CSVFile;
import neissmodel.data_access.DataSource;
import neissmodel.data_access.DataSourceFactory;
import neissmodel.data_access.DerbyDatabase;

/**
 * This isn't strictly a model, it is used to link and aggregate data after
 * a model has been run. It looks for a population file output by a
 * population reconstruction model (e.g. <code>BirkinPRM</code> and then
 * performs a few operations:
 * <ol>
 * <li>Runs a Fortran script that links the population file to a BHPS (British
 * Household Panel Surver) data file, creating individuals with many more
 * attributes.</li>
 * <li>Collects the household and poulation data into a single csv file.</li>
 * <li>Aggregates the resulting files to the output area and creates a csv
 * file of the results.</li>
 * </ol>
 *
 * 
 * @author Nick Malleson
 * @see neissmodel.BirkinPRM
 *
 */
public class BHPSLinker extends NeissModel {

    /** The location of population data files. Once a population is created
     * by the prm it will be put in here. */
    protected static String DATA_DIR = null;

    /** The location of dynamic data files */
    protected static String DYNAMIC_MODEL_ROOT_DIR = null;

    private static String CENSUS_DATA_TABLE_NAME = null;

    private static String DISTRICT_COLUMN = null;

    private static String WARD_COLUMN = null;

    private static String OUTPUT_AREA_COLUMN = null;

    /** The field to aggregate on **/
    protected NeissModelParameter<String> fieldToAggregate;

    /** The file with the individual population in */
    protected NeissModelParameter<String> populationFile;

    /** The list of regions used to generate the population file */
    protected NeissModelParameter<String> regionNames;

    /** A list of regions, generated from the region names */
    private List<String> regionList;

    /** Assume the population is output area resolution (i.e. PRM output),
     *  otherwise will have to work at ward level. This is set when the population
     * file is analysed. */
    protected boolean useOutputArea = true;

    /** Pattern to match different parts of the oa code (e.g. 00DAXF0002). Will
     * split into groups for region (00DA), ward (XF) and OA number (0002) */
    public static Pattern OA_CODE_REGEX = Pattern.compile("(\\d\\d\\w\\w)" + "(\\w\\w)" + "(\\d\\d\\d\\d)", Pattern.COMMENTS);

    /** Colum header that represents the colum used to store area (Ward or OA)
     * codes */
    protected static final String AREA_CODE = "Area_code";

    /** A list of area codes, generated when we need to find the area
     * (ward or OA) that a population file is referring to. */
    private List<String> areaCodes = null;

    public BHPSLinker() {
        super();
    }

    @Override
    protected void populateParameterList() {
        if (this.parameters == null) {
            this.parameters = new ArrayList<NeissModelParameter<?>>();
            this.fieldToAggregate = new NeissModelParameter<String>(PCONST.FIELD_TO_AGGREGATE, Arrays.asList(AGGREGATE_FIELDS));
            this.regionNames = new NeissModelParameter<String>(PCONST.REGION_NAME, "LIST OF REGIONS!");
            List<String> popFiles = BirkinDynamicModel.findPopulationDataFiles(BHPSLinker.DATA_DIR, true);
            if (popFiles == null || popFiles.size() == 0) {
                this.populationFile = new NeissModelParameter<String>(PCONST.POP_FILE, "NO POP DATA");
                this.addToMessages("BHPSLinker could not find any population data files in " + BHPSLinker.DATA_DIR, false);
            } else {
                File rootDataDir = new File(BHPSLinker.DATA_DIR);
                Pattern p = Pattern.compile("(....)HSAR\\d\\d\\.OUT");
                Matcher m = null;
                List<String> sarRegions = new ArrayList<String>();
                for (String file : rootDataDir.list()) {
                    m = p.matcher(file);
                    if (m.matches()) {
                        sarRegions.add(m.group(1));
                    }
                }
                List<String> sarAndPop = new ArrayList<String>();
                for (String rgn : popFiles) {
                    if (sarRegions.contains(rgn.substring(0, 4))) {
                        sarAndPop.add(rgn);
                    }
                }
                if (sarAndPop.size() > 0) {
                    this.populationFile = new NeissModelParameter<String>(PCONST.POP_FILE, sarAndPop);
                } else {
                    this.populationFile = new NeissModelParameter<String>(PCONST.POP_FILE, "NO SAR DATA");
                    this.addToMessages("BHPSLinker found data files (" + sarAndPop.toString() + ") but none have associated SAR data.\n", false);
                }
            }
            this.parameters.add(this.populationFile);
            this.parameters.add(this.regionNames);
            this.parameters.add(this.fieldToAggregate);
        }
    }

    @Override
    protected void setupInputDataSources() {
        StringBuilder msg = new StringBuilder();
        DataSource derby = DataSourceFactory.createDataSource("neiss_model_data", DerbyDatabase.class, msg);
        if (derby == null) {
            this.addToMessages("BHPSLinker could not create Derby data source: " + msg + "\n", true);
        } else {
            this.addDataSource(derby);
        }
    }

    @Override
    protected void init() {
        BHPSLinker.DYNAMIC_MODEL_ROOT_DIR = NeissModel.getProperty("BirkinDynamicModel.DYNAMIC_MODEL_ROOT_DIR", true);
        BHPSLinker.DATA_DIR = NeissModel.getProperty(PCONST.USER_DIR, true);
        BHPSLinker.CENSUS_DATA_TABLE_NAME = NeissModel.getProperty("BirkinPRM.CENSUS_DATA_TABLE_NAME", true);
        BHPSLinker.DISTRICT_COLUMN = NeissModel.getProperty("BirkinPRM.DISTRICT_COLUMN", true);
        BHPSLinker.WARD_COLUMN = NeissModel.getProperty("BirkinDynamicModel.WARD_COLUMN", true);
        BHPSLinker.OUTPUT_AREA_COLUMN = NeissModel.getProperty("BirkinPRM.OUTPUT_AREA_COLUMN", true);
    }

    @Override
    public boolean runModel(List<NeissModelParameter<?>> inParams) {
        this.addToMessages("BHPSLinker.runModel() starting\n", false);
        this.populateParameterList();
        this.addToMessages(this.setParameterValues(inParams), false);
        this.regionList = DistrictHash.extractDistricts(this.regionNames.getValue());
        String dataName = DistrictHash.hashDistricts(regionList);
        if (dataName == null || dataName.length() != 4) {
            this.addToMessages("BHPSLinker could not create a data name (hash) from the region(s): " + this.regionNames.toString() + ". Data name is: " + dataName == null ? "null" : dataName, true);
            return false;
        }
        this.addToMessages("BHPSLinker has input region names '" + this.regionNames.getValue() + "', which gives regions " + this.regionList.toString() + "and hash " + dataName, false);
        List<File> toDelete = new ArrayList<File>();
        try {
            File combinedFile = this.linkToBHPS(toDelete, dataName);
            this.addToMessages("Re-reading linked file so it can be aggregated.\n", false);
            StringBuilder msg = new StringBuilder();
            DataSource linkedData = DataSourceFactory.createDataSource(combinedFile.getAbsolutePath(), CSVFile.class, msg);
            if (linkedData == null) {
                this.addToMessages("Problem re-reading combined file. Messages are: " + msg + "\n", true);
                return false;
            }
            msg = new StringBuilder();
            List<List<Object>> data = linkedData.getValues(null, new String[] { AREA_CODE, this.fieldToAggregate.getValue() }, null, null, new String[] { AREA_CODE }, null, msg);
            if (data == null) {
                this.addToMessages("Error reading linked csv file. Messages are: " + msg + "\n", true);
                return false;
            }
            this.addToMessages("Successfully re-read lined file. Now aggregating on " + this.fieldToAggregate.getValue() + "\n", false);
            List<Object> uniqueVals = new ArrayList<Object>();
            for (Object o : data.get(1)) {
                if (!uniqueVals.contains(o)) {
                    uniqueVals.add(o);
                }
            }
            this.addToMessages("Found these unique values: " + uniqueVals.toString() + "\n", false);
            List<List<Object>> aggregatedData = new ArrayList<List<Object>>();
            for (int i = 0; i < uniqueVals.size() + 1; i++) {
                aggregatedData.add(new ArrayList<Object>());
            }
            String code = (String) data.get(0).get(0);
            aggregatedData.get(0).add(code);
            int[] groupTotals = new int[uniqueVals.size()];
            for (int x = 0; x < groupTotals.length; x++) {
                groupTotals[x] = 0;
            }
            for (int i = 0; i < data.get(0).size(); i++) {
                if (!data.get(0).get(i).toString().equals(code)) {
                    for (int x = 0; x < groupTotals.length; x++) {
                        aggregatedData.get(x + 1).add(groupTotals[x]);
                        groupTotals[x] = 0;
                    }
                    code = data.get(0).get(i).toString();
                    aggregatedData.get(0).add(code);
                }
                boolean foundGroup = false;
                for (int x = 0; x < uniqueVals.size(); x++) {
                    if (uniqueVals.get(x).equals(data.get(1).get(i))) {
                        groupTotals[x] += 1;
                        foundGroup = true;
                        break;
                    }
                }
                if (!foundGroup) {
                    this.addToMessages("BHPSLinker internal error: no aggregate group found for " + "value: " + data.get(1).get(i) + ". Groups are: " + uniqueVals.toString(), true);
                    return false;
                }
                if (i == data.get(0).size() - 1) {
                    for (int x = 0; x < groupTotals.length; x++) {
                        aggregatedData.get(x + 1).add(groupTotals[x]);
                        groupTotals[x] = 0;
                    }
                }
            }
            String yr = this.populationFile.getValue().substring(6, 8);
            File aggregatedFile = new File(BHPSLinker.DYNAMIC_MODEL_ROOT_DIR + "/" + dataName + yr + "_" + this.fieldToAggregate.getValue() + "_AGG.csv");
            this.addToMessages("Finished aggregating data, writing it out to: " + aggregatedFile.getAbsolutePath() + "\n", false);
            BufferedWriter w = new BufferedWriter(new FileWriter(aggregatedFile));
            w.write("OA_Code,");
            for (int i = 0; i < aggregatedData.size() - 1; i++) {
                w.write(this.fieldToAggregate.getValue() + "_" + uniqueVals.get(i) + ((i == aggregatedData.size() - 2) ? "" : ","));
            }
            w.write("\n");
            String s = "";
            for (int i = 0; i < aggregatedData.get(0).size(); i++) {
                s = "";
                for (int j = 0; j < aggregatedData.size(); j++) {
                    s += (aggregatedData.get(j).get(i) + ((j == aggregatedData.size() - 1) ? "" : ","));
                }
                w.write(s + "\n");
            }
            w.close();
            File copiedAggFile = new File(BHPSLinker.DATA_DIR + aggregatedFile.getName());
            File copiedCombinedFile = new File(BHPSLinker.DATA_DIR + combinedFile.getName());
            this.addToMessages("BHPSLinker Copying results to user data directory:" + "\n\t" + aggregatedFile.getAbsolutePath() + "->" + copiedAggFile.getAbsolutePath() + "\n\t" + combinedFile.getAbsolutePath() + "->" + copiedCombinedFile.getAbsolutePath(), false);
            FileHelper.copy(aggregatedFile, copiedAggFile);
            FileHelper.copy(combinedFile, copiedCombinedFile);
            toDelete.add(aggregatedFile);
            toDelete.add(combinedFile);
            if (NeissModelMain.saveExternally()) {
                this.addToMessages("BHPSLinker will attempt to save the results file '" + copiedAggFile.getName() + "' externally for user '" + NeissModelMain.getUserName() + "'", false);
                boolean copyAgg = NeissModel.saveResultsExternally(NeissModelMain.getUserName(), xmlDoc, copiedAggFile, "BHPSLinker aggregate file: " + copiedAggFile.getName());
                if (!(copyAgg)) {
                    this.addToMessages("Error: could not save the results externally, see logs " + "for details.\n", true);
                }
            }
            for (NeissModelParameter<?> p : this.parameters) {
                this.xmlDoc.addParameter(p.getName(), p.getType(), p.getValue().toString());
            }
            this.xmlDoc.addResultsInformation(PCONST.AGGREGATED_FILE, copiedAggFile.getName());
            this.xmlDoc.addResultsInformation("Combined Population File " + copiedCombinedFile.getName(), copiedCombinedFile.getName());
        } catch (IOException ex) {
            this.addToMessages("IOException trying to read/write population file or bhps file: " + ex.getMessage() + "\n", true);
            return false;
        }
        this.addToMessages("BHPS linker completed. Removing old data files:\n", false);
        for (File f : toDelete) {
            this.addToMessages("\t" + f.getAbsolutePath() + "\n", false);
            f.delete();
        }
        this.xmlDoc.setSuccessFailure(true);
        return true;
    }

    /**
     * Link the population file to the BHPS
     *
     * @param toDelete An optional list that will be appended with files that can be
     * deleted afterwards (deletion not done by the method).
     * @return The linked file or null if there was a problem
     */
    protected File linkToBHPS(List<File> toDelete, String dataName) throws FileNotFoundException, IOException {
        File popFileOrig = new File(BHPSLinker.DATA_DIR + this.populationFile.getValue());
        if (!checkfile(popFileOrig, false)) {
            return null;
        }
        File sarFileOrig = new File(BHPSLinker.DATA_DIR + dataName + "HSAR" + getYear() + ".OUT");
        if (!checkfile(sarFileOrig, false)) {
            return null;
        }
        File popFileCopied = new File(BHPSLinker.DYNAMIC_MODEL_ROOT_DIR + this.populationFile.getValue());
        File sarFileCopied = new File(BHPSLinker.DYNAMIC_MODEL_ROOT_DIR + dataName + "HSAR" + getYear() + ".OUT");
        File newFile = null, oldFile = null;
        try {
            oldFile = popFileOrig;
            newFile = popFileCopied;
            FileHelper.copy(oldFile, newFile);
            oldFile = sarFileOrig;
            newFile = sarFileCopied;
            FileHelper.copy(oldFile, newFile);
        } catch (FileNotFoundException ex) {
            this.addToMessages("BHPSLinker.runModel() could not copy a file " + "from " + oldFile.getAbsolutePath() + " to " + newFile.getAbsolutePath() + "FileNotFound: " + ex.getMessage(), true);
            throw ex;
        } catch (IOException ex) {
            this.addToMessages("BHPSLinker.runModel() could not copy a file " + "from " + oldFile.getAbsolutePath() + " to " + newFile.getAbsolutePath() + "IOEception: " + ex.getMessage(), true);
            throw ex;
        }
        if (!checkfile(popFileCopied, false)) {
            return null;
        }
        toDelete.add(popFileCopied);
        toDelete.add(sarFileCopied);
        this.addToMessages("BHPSLinker has copied files to working dir: " + popFileCopied.getAbsolutePath() + " and " + sarFileCopied.getAbsolutePath() + "\n", false);
        File paramsFile = new File(BHPSLinker.DYNAMIC_MODEL_ROOT_DIR + "CONTROL.txt");
        if (!checkfile(paramsFile, true)) {
            return null;
        }
        this.addToMessages("BHPSLinker has checked that required files exist, now getting the " + "number of areas from the population file.", false);
        try {
            int numAreas = getNumAreasFromPopFile(popFileCopied);
            if (numAreas == -1) {
                return null;
            }
            this.addToMessages("Found " + numAreas + " areas in the population file. Writing " + "this to the parameters file ('" + paramsFile + "')\n", false);
            BufferedWriter writer = new BufferedWriter(new FileWriter(paramsFile));
            writer.write(getYear() + "\n");
            writer.write(-1 + "\n");
            writer.write(dataName + "\n");
            writer.write(numAreas + "\n");
            writer.close();
        } catch (IOException ex) {
            this.addToMessages("IOException trying to write parameters file (" + paramsFile.getAbsolutePath() + "): " + ex.getMessage() + "\n", true);
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        StringBuilder commandOutput = new StringBuilder();
        this.addToMessages("Running BHPS linker script (maptube2)...", false);
        int retVal = FileHelper.runCommand(BHPSLinker.DYNAMIC_MODEL_ROOT_DIR + "/maptube2", new File(BHPSLinker.DYNAMIC_MODEL_ROOT_DIR), commandOutput, true);
        this.addToMessages(commandOutput + "\n", false);
        if (retVal != 0) {
            String msg = ("Error running bhps linker script (maptube2) (return value '" + retVal + "'). Not continuing. See previous messages for an explanation.\n");
            this.addToMessages(msg, true);
            return null;
        }
        File bhpsFile = new File(BHPSLinker.DYNAMIC_MODEL_ROOT_DIR + "/" + dataName + "BHPS" + getYear() + ".OUT");
        if (!checkfile(bhpsFile, false)) {
            return null;
        }
        toDelete.add(bhpsFile);
        this.addToMessages("BHPS linking script ran successfully and the output " + "file (" + bhpsFile.getAbsolutePath() + ") was generated.\n", false);
        Pattern pop_individual = Pattern.compile(BirkinPRM.INDIVIDUAL_REGEX);
        Pattern bhps_area = Pattern.compile("\\s*? \\d+? \\s+? \\d+", Pattern.COMMENTS);
        Pattern bhps_household = Pattern.compile("\\s*? \\d+? \\s+? \\d+? \\s+? \\d+? \\s+? \\d+? \\s+? \\d+", Pattern.COMMENTS);
        File combinedFile = new File(BHPSLinker.DYNAMIC_MODEL_ROOT_DIR + "/" + dataName + "LINKED" + getYear() + ".OUT");
        if (!combinedFile.exists()) {
            combinedFile.createNewFile();
        }
        if (!this.checkfile(combinedFile, true)) {
            return null;
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(combinedFile));
        this.addToMessages("Writing bhps/population linked file to '" + combinedFile.getAbsolutePath() + "'.\n", false);
        String header = "";
        for (String s : Headers.POPULATION.HOUSEHOLD) {
            header += (s + ",");
        }
        for (String s : Headers.BHPS.HOUSEHOLD) {
            header += (s + ",");
        }
        for (String s : Headers.POPULATION.INDIVIDUAL) {
            header += (s + ",");
        }
        for (String s : Headers.BHPS.INDIVIDUAL) {
            header += (s + ",");
        }
        header += ("Household_Count, " + AREA_CODE);
        header += "\n";
        writer.write(header);
        this.addToMessages("Have written headers.\n", false);
        String household = "";
        String individual = "";
        BufferedReader popReader = new BufferedReader(new FileReader(popFileCopied));
        BufferedReader bhpsReader = new BufferedReader(new FileReader(bhpsFile));
        boolean error = false;
        String AreaCode = "XXXX";
        int householdCount = 0;
        int lineCount = 0;
        String bhpsLine = bhpsReader.readLine(), popLine = popReader.readLine();
        this.addToMessages("Joining population and bhps data.\n", false);
        readFiles: while ((popLine != null) && (bhpsLine != null)) {
            if (bhps_area.matcher(bhpsLine).matches()) {
                householdCount = 0;
                int areaNum;
                try {
                    areaNum = Integer.parseInt(bhpsLine.trim().split("\\s+")[0]);
                } catch (NumberFormatException e) {
                    this.addToMessages("Could not parse OA number from bhps file. Line in file: " + bhpsLine + ". Message: " + e.getMessage(), true);
                    return null;
                }
                AreaCode = this.generateOACode(areaNum, this.useOutputArea);
                if (AreaCode == null) {
                    this.addToMessages("Bhps file could not generate an OA Code from the " + "OA number and regions " + this.regionNames.toString() + ".\n", true);
                    error = true;
                    break readFiles;
                }
                bhpsLine = bhpsReader.readLine();
                if (bhpsLine == null) {
                    this.addToMessages("Bhps file terminated too early\n", true);
                    error = true;
                    break readFiles;
                }
            }
            if (bhps_household.matcher(bhpsLine).matches() && !pop_individual.matcher(popLine).matches()) {
                householdCount += 1;
                household = "";
                String popHouse[] = popLine.trim().split("\\s+");
                for (String s : popHouse) {
                    household += (s + ", ");
                }
                if (!this.useOutputArea) {
                    household += ("na" + ", ");
                }
                String bhpsHouse[] = bhpsLine.trim().split("\\s+");
                for (String s : bhpsHouse) {
                    household += (s + ", ");
                }
            } else if (!bhps_household.matcher(bhpsLine).matches() && pop_individual.matcher(popLine).matches()) {
                individual = "";
                String popIndiv[] = popLine.trim().split("\\s+");
                for (String s : popIndiv) {
                    individual += (s + ", ");
                }
                String bhpsIndiv[] = bhpsLine.trim().split("\\s+");
                for (String s : bhpsIndiv) {
                    individual += (s + ", ");
                }
                writer.write(household + individual);
                writer.write(householdCount + "," + AreaCode);
                writer.write("\n");
            } else {
                this.addToMessages("Error reading individual and bhps files, " + "one line is matching a house, the other an individual." + "\nBHPS household: " + bhps_household.matcher(bhpsLine).matches() + " '" + bhpsLine + "'\nPopulation individual: " + pop_individual.matcher(popLine).matches() + " '" + popLine + "'", true);
                error = true;
                break readFiles;
            }
            popLine = popReader.readLine();
            bhpsLine = bhpsReader.readLine();
            if (((popLine == null) && (bhpsLine != null)) || ((bhpsLine == null) && (popLine != null))) {
                this.addToMessages("Error reading bhps and population files, the " + (popLine == null ? "population" : "bhps") + " terminated too soon.\n", true);
                error = true;
                break readFiles;
            }
            if (lineCount == 0) {
                this.addToMessages("Read lines:", false);
            }
            if (lineCount % 500000 == 0) {
                this.addToMessages(lineCount + ", ", false);
            }
            lineCount++;
        }
        writer.close();
        popReader.close();
        bhpsReader.close();
        if (error) {
            this.addToMessages("There was an error reading the bhps and population files, see previous messages.", true);
            return null;
        }
        this.addToMessages("Finished linking BHPS and population files: " + combinedFile.getAbsolutePath() + ".\n", false);
        return combinedFile;
    }

    /** Checks a file exists and can be read or written
     *
     * @param bhpsFile
     * @param readWrite if true check it can be read and written, otherwise just read.
     * @return
     */
    private boolean checkfile(File inFile, boolean readWrite) {
        if ((inFile.exists() && inFile.canRead())) {
            if (readWrite && !inFile.canWrite()) {
                this.addToMessages("File (" + inFile.getAbsolutePath() + ") does exist but " + "cannot be written\n", true);
                return false;
            } else {
                return true;
            }
        } else {
            this.addToMessages("File (" + inFile.getAbsolutePath() + ") does not exist or " + "cannot be read\n", true);
            return false;
        }
    }

    /**
     * Read the end of the population file looking for the number of areas
     * (output areas or wards, depending on the resolution).
     * @param popFile
     * @return
     */
    private int getNumAreasFromPopFile(File popFile) {
        int numAreas = -1;
        Vector<String> tail = FileHelper.tail(popFile.getAbsolutePath(), 20);
        Pattern p = Pattern.compile(BirkinPRM.HOUSEHOLD_REGEX);
        Matcher m;
        for (int i = 0; i < tail.size(); i++) {
            String line = tail.get(i);
            m = p.matcher(line);
            if (m.matches()) {
                String areaText = m.group(5);
                if (areaText == null) {
                    areaText = m.group(1);
                    this.useOutputArea = false;
                }
                try {
                    numAreas = Integer.parseInt(areaText);
                } catch (NumberFormatException ex) {
                    this.addToMessages("NumberFormatException trying to find the number of " + "areas in the population file: " + ex.getMessage() + "\n", true);
                    return -1;
                }
                break;
            }
        }
        if (numAreas == -1) {
            this.addToMessages("getNumAreaFromPopFile: error getting the number of areas " + "from the file: " + popFile.getAbsolutePath() + ", could not find the line " + "which holds the total number of areas. Next message will give the text " + "that was read.", true);
            this.addToMessages(tail.toString(), true);
        }
        this.addToMessages("getNumAreasFromPopFile() is working at the " + (useOutputArea ? "OA" : "Ward") + " level. Found " + numAreas + " areas.", false);
        return numAreas;
    }

    /**
     * Method to generates a full OA/Ward code from the area number. It needs the
     * region name(s) to query the census database and get the OA/Ward names.
     * It uses the <code>regionNames</code> parameter to construct the DB query.
     *
     * @param areaNum The number the area (e.g. the 13th ward).
     * @param useOA Whether to use OAs (true) or wards (false)
     * @return The area code or null if there's a problem.
     */
    private String generateOACode(int areaNum, boolean useOA) throws FileNotFoundException, IOException {
        if (this.areaCodes == null) {
            String column = useOA ? BHPSLinker.OUTPUT_AREA_COLUMN : BHPSLinker.WARD_COLUMN;
            StringBuilder resultsOutput = new StringBuilder();
            for (DataSource ds : this.getDataSources()) {
                List<List<Object>> results = ds.getValues(BHPSLinker.CENSUS_DATA_TABLE_NAME, new String[] { column }, new String[] { BHPSLinker.DISTRICT_COLUMN }, new Object[] { this.regionList }, new String[] { BHPSLinker.OUTPUT_AREA_COLUMN }, DataSource.SEARCH_TYPE.EXACT, resultsOutput);
                if (results == null || results.size() == 0 || results.get(0).size() == 0) {
                    this.addToMessages("BHPSLinker.generateOACode(): no districts found in datasource" + ds.toString() + "\n\t" + resultsOutput, false);
                    continue;
                }
                LinkedHashMap<String, Object> uniqueAreas = new LinkedHashMap<String, Object>();
                for (Object o : results.get(0)) {
                    String area = (String) o;
                    if (!uniqueAreas.containsKey(area)) {
                        uniqueAreas.put(area, null);
                    }
                }
                this.areaCodes = new ArrayList<String>();
                for (String area : uniqueAreas.keySet()) {
                    this.areaCodes.add(area);
                }
                break;
            }
        }
        if (this.areaCodes == null) {
            this.addToMessages("BHPSLinker.generateOACode() for some reason the area code list " + "hasn't been generated. Regions: " + this.regionList.toString() + "\n", true);
            this.regionNames = new NeissModelParameter<String>("Region name", "NO INPUT DATA");
        }
        return this.areaCodes.get(areaNum - 1);
    }

    /**
     * Parse the region name from the population file
     */
    protected String getYear() {
        return this.populationFile.getValue().substring(6, 8);
    }

    private boolean printedOACodeError = false;

    /** Simple class to store headers in the population and bhps files */
    static final class Headers {

        /** The headers of the population file */
        static final class POPULATION {

            static final String[] HOUSEHOLD = new String[] { "Ward", "Num_Individuals", "House_Tenure", "OA" };

            static final String[] INDIVIDUAL = new String[] { "Age", "Gender", "Marital_Status", "HRP_flag", "Health_status", "Social_Class", "Ethnicity" };
        }

        /** The headers of the BHPS file */
        static final class BHPS {

            static final String[] AREA = new String[] { "OA", "Num_Households" };

            static final String[] HOUSEHOLD = new String[] { "OA", "SAR_Record", "BHPS_Main_Person", "Household_Size", "Age_Main_Person" };

            static final String[] INDIVIDUAL = new String[] { "OA", "SAR_Record", "Person_No", "BHPS_Record", "Age", "Marital_Status", "Gender", "Ethnicity", "Occupation", "Health_Status", "Visits_to_GP", "Social_Caring", "Breast_cancer_screening", "Smoking", "Hours of caring", "Diabetes", "Income" };
        }
    }

    /** Hard-coded fields that the linked file can be aggregated on (build from the above
     * lists in Headers class. */
    static final String[] AGGREGATE_FIELDS = new String[] { "Age", "Gender", "Marital_Status", "HRP_flag", "Health_status", "Social_Class", "Ethnicity", "Occupation", "Visits_to_GP", "Social_Caring", "Breast_cancer_screening", "Smoking", "Hours of caring", "Diabetes" };
}
