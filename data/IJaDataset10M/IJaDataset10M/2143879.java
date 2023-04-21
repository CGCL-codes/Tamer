package mil.army.usace.ehlschlaeger.digitalpopulations.censusgen;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import mil.army.usace.ehlschlaeger.digitalpopulations.LandcoverPopulationDensity;
import mil.army.usace.ehlschlaeger.digitalpopulations.PumsHousehold;
import mil.army.usace.ehlschlaeger.digitalpopulations.PumsPopulation;
import mil.army.usace.ehlschlaeger.digitalpopulations.PumsQuery;
import mil.army.usace.ehlschlaeger.digitalpopulations.censusgen.filerelationship.AttributeMap;
import mil.army.usace.ehlschlaeger.digitalpopulations.censusgen.filerelationship.FileRelationship;
import mil.army.usace.ehlschlaeger.digitalpopulations.censusgen.filerelationship.LanduseCombination;
import mil.army.usace.ehlschlaeger.digitalpopulations.censusgen.filerelationship.Regions;
import mil.army.usace.ehlschlaeger.digitalpopulations.censusgen.filerelationship.Trait;
import mil.army.usace.ehlschlaeger.digitalpopulations.censusgen.filerelationship.Trait.Type;
import mil.army.usace.ehlschlaeger.digitalpopulations.censusgen.fittingcriteria.FittingCriteria;
import mil.army.usace.ehlschlaeger.rgik.core.CSVTable;
import mil.army.usace.ehlschlaeger.rgik.core.ClassCrop;
import mil.army.usace.ehlschlaeger.rgik.core.Count;
import mil.army.usace.ehlschlaeger.rgik.core.CumulativeDistributionFunction;
import mil.army.usace.ehlschlaeger.rgik.core.DataException;
import mil.army.usace.ehlschlaeger.rgik.core.GISClass;
import mil.army.usace.ehlschlaeger.rgik.core.GISLattice;
import mil.army.usace.ehlschlaeger.rgik.core.Match;
import mil.army.usace.ehlschlaeger.rgik.core.Proportion;
import mil.army.usace.ehlschlaeger.rgik.core.RGIS;
import mil.army.usace.ehlschlaeger.rgik.core.SpatialStatistic;
import mil.army.usace.ehlschlaeger.rgik.core.TractSpatialStatistic;
import mil.army.usace.ehlschlaeger.rgik.core.TransformAttributes2double;
import mil.army.usace.ehlschlaeger.rgik.util.FileUtil;
import mil.army.usace.ehlschlaeger.rgik.util.LogUtil;
import mil.army.usace.ehlschlaeger.rgik.util.ObjectUtil;
import mil.army.usace.ehlschlaeger.rgik.util.TimeTracker;
import org.apache.commons.collections.primitives.ArrayIntList;
import org.apache.commons.collections.primitives.IntList;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.ICsvListReader;
import org.supercsv.prefs.CsvPreference;

/**
 * Generates the pieces of the census synthesizer from a relationship file.
 * Encapsulates the "prepareData" phase of {@link ConflatePumsQueryWithTracts}.
 * <p>
 * Copyright <a href="http://faculty.wiu.edu/CR-Ehlschlaeger2/">Charles R.
 * Ehlschlaeger</a>, work: 309-298-1841, fax: 309-298-3003, This software is
 * freely usable for research and educational purposes. Contact C. R.
 * Ehlschlaeger for permission for other purposes. Use of this software requires
 * appropriate citation in all published and unpublished documentation.
 * 
 * @author William R. Zwicky
 */
public class DataPreparer {

    protected static Logger log = ConflatePumsQueryWithTracts.log;

    /**
     * If true, we'll trim all unused columns from populations table to save
     * memory. Note that output files will be similarly truncated.
     */
    public boolean trimPopulationColumns = false;

    protected FittingCriteria crit;

    protected FileRelationship rel;

    protected File dataDir;

    protected GISClass landuseMap;

    protected Map<Integer, String> landuseClasses;

    protected HashMap<String, RegionData> regionDatas;

    protected RegionData primaryRegion;

    /** Single-attribute goal maps. */
    protected HashMap<String, RegionData> attributeMaps;

    /** Column names from household table. */
    protected CSVTable householdSchema;

    /** Column names from population table. */
    protected CSVTable populationSchema;

    /** Our source of random numbers. */
    protected Random random = new Random();

    /**
     * Create helper from fitting criteria and its linked relationship spec.
     * 
     * @param crit fitting criteria spec
     * @param dataDir directory where all input files can be found
     * 
     * @throws IOException on any error loading data files
     */
    public DataPreparer(FittingCriteria crit, File dataDir) throws IOException {
        this.crit = crit;
        this.rel = crit.relationship;
        this.dataDir = dataDir;
        if (!ObjectUtil.isBlank(rel.landuse.map)) {
            loadLanduse();
            TimeTracker.finished("Loading land-use maps");
        }
        loadRegions();
        loadAttributeMaps();
        TimeTracker.finished("Loading goal maps");
    }

    /**
     * Create helper from a relationship spec.  Functions that require
     * fitting criteria spec won't work.
     * 
     * @param rel relationship spec
     * @param dataDir directory where all input files can be found
     * 
     * @throws IOException on any error loading data files
     */
    public DataPreparer(FileRelationship rel, File dataDir) throws IOException {
        this.crit = null;
        this.rel = rel;
        this.dataDir = dataDir;
        if (!ObjectUtil.isBlank(rel.landuse.map)) {
            loadLanduse();
            TimeTracker.finished("Loading land-use maps");
        }
        loadRegions();
        loadAttributeMaps();
        TimeTracker.finished("Loading goal maps");
    }

    /**
     * Change our source of random numbers.
     * 
     * @param source
     *            new random number generator
     */
    public void setRandomSource(Random source) {
        random = source;
    }

    public FittingCriteria getFittingCriteria() {
        return crit;
    }

    public GISClass getLanduseMap() {
        return landuseMap;
    }

    /**
     * Returns the schema (list of column names) for the last household table we
     * loaded. WARNING: Not valid until after loadPumsHouseholds() has been
     * called.
     * 
     * @return households table column names
     */
    public CSVTable getHouseholdSchema() {
        return householdSchema;
    }

    /**
     * Returns the schema (list of column names) for the last population table
     * we loaded. WARNING: Not valid until after loadPumsPopulation() has been
     * called.
     * 
     * @return population table column names
     */
    public CSVTable getPopulationSchema() {
        return populationSchema;
    }

    /**
     * Load and pre-process the land-use map. The file is as specified in the
     * relationship file, and the cell values are remapped as specified.
     * 
     * @throws IOException on any file error
     */
    protected void loadLanduse() throws IOException {
        String file = FileUtil.resolve(dataDir, rel.landuse.map).getPath();
        landuseMap = GISClass.loadEsriAscii(file);
        landuseClasses = new HashMap<Integer, String>();
        BitSet mapped = new BitSet();
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
        for (LanduseCombination combo : rel.landuse.combos) {
            for (Integer source : LanduseCombination.makeIntSet(combo.classes)) {
                if (mapped.get(source)) throw new DataException(String.format("Land-use class %d has already been mapped to %d.", source, map.get(source)));
                map.put(source, combo.target);
                mapped.set(source);
            }
            landuseClasses.put(combo.target, combo.desc);
        }
        Set<Integer> vacancies;
        if (rel.landuse.vacant != null && rel.landuse.vacant.classes != null) {
            vacancies = LanduseCombination.makeIntSet(rel.landuse.vacant.classes);
            for (Integer source : vacancies) {
                mapped.set(source);
            }
        } else {
            vacancies = new HashSet<Integer>();
        }
        BitSet used = new BitSet();
        for (int r = landuseMap.getNumberRows() - 1; r >= 0; r--) {
            for (int c = landuseMap.getNumberColumns() - 1; c >= 0; c--) {
                if (landuseMap.isNoData(r, c) == false) {
                    int klass = landuseMap.getCellValue(r, c);
                    used.set(klass);
                    Integer target = map.get(klass);
                    if (target == null) {
                        if (vacancies.contains(klass)) {
                            landuseMap.setNoData(r, c, true);
                        }
                    } else {
                        landuseMap.setCellValue(r, c, target.intValue());
                    }
                }
            }
        }
        used.andNot(mapped);
        if (used.cardinality() > 0) throw new DataException("Land-use map contains classes that have not been remapped: " + used);
    }

    /**
     * Load region map and table sets, and validate everything.
     * 
     * @throws IOException on any file error
     * @throws DataException
     *             on any logic error
     */
    protected void loadRegions() throws IOException {
        regionDatas = new HashMap<String, RegionData>();
        primaryRegion = null;
        for (Regions spec : rel.regions) {
            RegionData data = RegionData.load(spec, dataDir);
            regionDatas.put(spec.id, data);
            if (!ObjectUtil.isBlank(spec.vacancies) || !ObjectUtil.isBlank(spec.households) || !ObjectUtil.isBlank(spec.population)) {
                if (primaryRegion != null) throw new DataException("Only one <regions> element can specify vacancies/households/population."); else primaryRegion = data;
            }
        }
        if (primaryRegion == null) throw new DataException("One <regions> element must specify vacancies/households/population.");
    }

    /**
     * Load extra attribute maps.
     * 
     * @throws IOException on any file error
     * @throws DataException
     *             on any logic error
     */
    protected void loadAttributeMaps() throws IOException {
        attributeMaps = new HashMap<String, RegionData>();
        if (rel.attributeMaps != null && rel.attributeMaps.size() > 0) {
            for (AttributeMap spec : rel.attributeMaps) {
                RegionData data = RegionData.load(spec, dataDir);
                attributeMaps.put(spec.id, data);
            }
        }
    }

    /**
     * Sort people from PUMS data into their households.
     * 
     * @param houses objects to receive people
     * @param persons people to move into houses
     */
    public void populateHouseholds(List<PumsHousehold> houses, List<PumsPopulation> persons) {
        if (ObjectUtil.isBlank(rel.population.household)) return;
        HashMap<String, ArrayList<PumsPopulation>> houseMap = new HashMap<String, ArrayList<PumsPopulation>>();
        for (PumsHousehold house : houses) {
            houseMap.put(house.getID(), new ArrayList<PumsPopulation>());
        }
        for (PumsPopulation person : persons) {
            ArrayList<PumsPopulation> popList = houseMap.get(person.getHohID());
            if (popList == null) throw new DataException("Can't find household with ID: " + person);
            popList.add(person);
        }
        for (PumsHousehold house : houses) {
            ArrayList<PumsPopulation> peops = houseMap.get(house.getID());
            if (peops.size() == 0) {
                house.setMembersOfHousehold(null);
            } else {
                PumsPopulation[] ary = new PumsPopulation[peops.size()];
                ary = peops.toArray(ary);
                house.setMembersOfHousehold(ary);
                house.setAttributeValue(rel.households.members, ary.length);
            }
        }
    }

    /**
     * @return the file we will load PUMS household data from
     */
    public File getPumsHouseholdsFile() {
        return FileUtil.resolve(dataDir, rel.households.table);
    }

    /**
     * Load and parse PUMS household table directly into PumsHousehold objects.
     * We don't want to load the full table into a CSVTable as it's too big.
     * 
     * @return list of PumsHousehold objects, one for each row in the file
     * @throws IOException on any file error
     */
    public List<PumsHousehold> loadPumsHouseholds() throws IOException {
        File hohFile = getPumsHouseholdsFile();
        DataException.verifyNotBlank(hohFile, "Households table is required for DP to run.");
        String csvFile = hohFile.getPath();
        CSVTable schema = new CSVTable();
        ICsvListReader reader = new CsvListReader(new FileReader(csvFile), CsvPreference.STANDARD_PREFERENCE);
        try {
            String[] head = reader.getCSVHeader(true);
            for (String key : head) schema.addColumn(key);
            boolean has_key = !ObjectUtil.isBlank(rel.households.key);
            long auto_key = 1;
            int key_idx = -1;
            if (has_key) key_idx = schema.findColumn(rel.households.key);
            ArrayList<PumsHousehold> houses = new ArrayList<PumsHousehold>();
            for (; ; ) {
                List<String> line = reader.read();
                if (line == null) break;
                int[] attribs = new int[line.size()];
                for (int v = 0; v < line.size(); v++) {
                    attribs[v] = parseInt(line.get(v));
                }
                PumsHousehold pumsHouse;
                if (has_key) {
                    String key = line.get(key_idx);
                    pumsHouse = new PumsHousehold(schema, attribs, key);
                } else {
                    String key = Long.toString(auto_key);
                    pumsHouse = new PumsHousehold(schema, attribs, key);
                    auto_key += 1;
                }
                houses.add(pumsHouse);
            }
            householdSchema = schema;
            return houses;
        } finally {
            reader.close();
        }
    }

    /**
     * @return the file we will load PUMS population data from, or null if none provided
     */
    public File getPumsPopulationFile() {
        if (!ObjectUtil.isBlank(rel.population.table)) return FileUtil.resolve(dataDir, rel.population.table); else return null;
    }

    /**
     * Load and parse PUMS population table directly into PumsPopulation objects.
     * We don't want to load the full table into a CSVTable as it's too big.
     * 
     * @return list of PumsPopulation objects, one for each row in the file
     * @throws IOException on any file error
     */
    public List<PumsPopulation> loadPumsPopulation() throws IOException {
        ArrayList<PumsPopulation> peops = new ArrayList<PumsPopulation>();
        File csvFile = getPumsPopulationFile();
        if (csvFile == null) return peops;
        CSVTable schema = new CSVTable();
        ICsvListReader reader = new CsvListReader(new FileReader(csvFile), CsvPreference.STANDARD_PREFERENCE);
        try {
            String[] head = reader.getCSVHeader(true);
            for (String key : head) schema.addColumn(key);
            int hohidx = schema.findColumn(rel.population.household);
            for (; ; ) {
                List<String> line = reader.read();
                if (line == null) break;
                String hid = line.get(hohidx);
                PumsPopulation pumsPerson = newPumsPopulation(hid, schema, line);
                peops.add(pumsPerson);
            }
            populationSchema = schema;
            return peops;
        } finally {
            reader.close();
        }
    }

    protected PumsPopulation newPumsPopulation(String hohID, CSVTable schema, List<String> attributeValues) {
        int[] values = new int[attributeValues.size()];
        for (int v = 0; v < attributeValues.size(); v++) {
            values[v] = parseInt(attributeValues.get(v));
        }
        return new PumsPopulation(hohID, schema, values);
    }

    /**
     * Load PUMS households table directly into a CSVTable without processing.
     * Not recommended; memory consumption can be large.
     */
    public CSVTable loadPumsHouseholdCSV() throws IOException {
        String file = getPumsHouseholdsFile().getPath();
        CSVTable householdsTable = new CSVTable(file);
        householdSchema = CSVTable.createEmpty(householdsTable);
        return householdsTable;
    }

    /**
     * Load PUMS population table directly into a CSVTable without processing.
     * Not recommended; memory consumption can be large.
     */
    public CSVTable loadPumsPopulationCSV() throws IOException {
        String file = getPumsPopulationFile().getPath();
        CSVTable populationTable;
        if (trimPopulationColumns && crit != null) {
            Set<String> cols = crit.getRefdPopCols();
            populationTable = new CSVTable(file, cols);
        } else populationTable = new CSVTable(file);
        populationSchema = CSVTable.createEmpty(populationTable);
        return populationTable;
    }

    /**
     * Build or load PDF as requested by crit file.
     * 
     * @return population density map
     * @throws IOException on any error loading data files
     */
    public GISLattice makePDF(RegionData region) throws IOException {
        if (rel.popdensity != null) {
            if (rel.popdensity.getMap() != null) {
                File f = crit.findFile(rel.popdensity.getMap());
                LogUtil.detail(log, "Loading population density from " + f);
                GISLattice pdf = GISLattice.loadEsriAscii(f);
                pdf = LandcoverPopulationDensity.createPDF(region.map, pdf);
                return pdf;
            } else if (rel.popdensity.getLanduse() != null) {
                File f = crit.findFile(rel.popdensity.getLanduse());
                LogUtil.detail(log, "Building population density from " + f);
                CSVTable densities = new CSVTable(f.getAbsolutePath());
                int klassCol = densities.findColumn(LandcoverPopulationDensity.TABLE_LANDUSE_CLASS);
                int densCol = densities.findColumn(LandcoverPopulationDensity.TABLE_LANDUSE_DENSITY);
                Map<Integer, Double> valueMap = new HashMap<Integer, Double>();
                for (int r = 0; r < densities.getRowCount(); r++) {
                    if (densities.getStringAt(r, 0).startsWith("#")) continue;
                    int k = Integer.parseInt(densities.getStringAt(r, klassCol));
                    double v = Double.parseDouble(densities.getStringAt(r, densCol));
                    valueMap.put(k, v);
                }
                GISLattice pdf = LandcoverPopulationDensity.createPDF(landuseMap, valueMap);
                return pdf;
            } else throw new DataException("<popdensity> doesn't specify any file.");
        } else {
            DataException.verifyNotNull(landuseMap, "Land-use map is required to compute population density.");
            DataException.verifyNotNull(region.map, "Region map is required to compute population density.");
            DataException.verifyNotNull(region.table, "Region attribute table is required to compute population density.");
            DataException.verifyNotBlank(region.spec.key, "<regions key> is required to compute population density.");
            DataException.verifyNotBlank(region.spec.households, "<regions key> is required to compute population density.");
            LandcoverPopulationDensity lpd = new LandcoverPopulationDensity(landuseMap, landuseClasses, region.map, region.table, region.spec.key, region.spec.households);
            GISLattice pdf = lpd.createPDF();
            File f = null;
            LogUtil.cr(log);
            f = new File(RGIS.getOutputFolder(), "LandcoverPopulationDensity-coverage.csv").getCanonicalFile();
            try {
                lpd.writeLandPercentTable(f);
                LogUtil.result(log, "NOTE: Wrote LPD land-percent table to %s", f);
            } catch (IOException e) {
                log.log(Level.WARNING, "Can't create LPD data file " + f, e);
            }
            f = new File(RGIS.getOutputFolder(), "LandcoverPopulationDensity-input.csv").getCanonicalFile();
            try {
                lpd.writeSourceTable(f);
                LogUtil.result(log, "NOTE: Wrote LPD input table to %s", f);
            } catch (IOException e) {
                log.log(Level.WARNING, "Can't create LPD data file " + f, e);
            }
            f = new File(RGIS.getOutputFolder(), "LandcoverPopulationDensity-landuse.csv").getCanonicalFile();
            try {
                lpd.writeSolutionTable(f);
                LogUtil.result(log, "NOTE: Wrote LPD land-use table to %s", f);
            } catch (IOException e) {
                log.log(Level.WARNING, "Can't create LPD data file " + f, e);
            }
            f = new File(RGIS.getOutputFolder(), "LandcoverPopulationDensity-map.asc").getCanonicalFile();
            try {
                pdf.writeAsciiEsri(f);
                LogUtil.result(log, "NOTE: Wrote LPD map to %s", f);
            } catch (IOException e) {
                log.log(Level.WARNING, "Can't create LPD data file " + f, e);
            }
            f = new File(RGIS.getOutputFolder(), "LandcoverPopulationDensity-population.csv").getCanonicalFile();
            try {
                lpd.writePopulationTable(f);
                LogUtil.result(log, "NOTE: Wrote LPD class-population table to %s", f);
            } catch (IOException e) {
                log.log(Level.WARNING, "Can't create LPD data file " + f, e);
            }
            return pdf;
        }
    }

    /**
     * Build CumulativeDistributionFunction objects to help place households.
     * Each CDF will cause random households to favor one region modulated by
     * land-use types (i.e. cells outside region will have probability zero;
     * cells inside will have probabilities determined by the population density
     * of the land-use code in that cell.)
     * <P>
     * Only region keys that actually appear in the region map and have residents
     * will be populated in the result.
     * 
     * @return one CDF for each region in regionMap
     * @throws IOException on any error loading data files
     */
    public Map<Integer, CumulativeDistributionFunction> makeLanduseCDF(RegionData region) throws IOException {
        GISLattice pdf = makePDF(region);
        int keycol = region.table.findColumn(region.spec.key);
        LogUtil.detail(log, "\nBuilding CDF maps:");
        HashMap<Integer, CumulativeDistributionFunction> cdfMaps = new HashMap<Integer, CumulativeDistributionFunction>();
        int nextDone = 5;
        for (int r = 0; r < region.table.getRowCount(); r++) {
            int doneValue = (int) Math.floor(100.0 * r / region.table.getRowCount());
            if (doneValue >= nextDone) {
                LogUtil.detail(log, "  Progress: " + doneValue + "% done. " + r + " out of " + region.table.getRowCount() + " regions.");
                nextDone += 5;
            }
            int key = Integer.parseInt(region.table.getStringAt(r, keycol));
            ClassCrop cc = new ClassCrop(key, key);
            GISClass classMap = cc.crop(region.map);
            if (classMap != null) {
                GISLattice smallPDF = pdf.extractWhere(classMap, classMap);
                CumulativeDistributionFunction cdf = CumulativeDistributionFunction.createNormalized(smallPDF);
                if (cdf != null) cdfMaps.put(key, cdf); else LogUtil.detail(log, "NOTE: Region %d land use classes have zero population density.", key);
            }
        }
        LogUtil.detail(log, "CDF maps complete.");
        return cdfMaps;
    }

    protected Trait[] makeAutomaticTraits(RegionData region) {
        DataException.verifyNotBlank(region.spec.households, "<regions households> is mandatory.");
        List<Trait> mantra = new ArrayList<Trait>();
        boolean hoh_ok = !ObjectUtil.isBlank(region.spec.vacancies);
        boolean pop_ok = !ObjectUtil.isBlank(region.spec.population);
        Trait trait1 = new Trait();
        trait1.id = "Auto-1";
        trait1.desc = "Absolute Households";
        trait1.regionTable = region.spec.id;
        trait1.regionTrait = region.spec.households;
        trait1.regionTotal = "1";
        trait1.pumsTraitTable = Type.HOUSEHOLDS;
        trait1.pumsTraitField = "1";
        trait1.pumsTotalTable = null;
        mantra.add(trait1);
        if (hoh_ok && pop_ok) {
            DataException.verifyNotBlank(rel.households.members, "<regions vacancies> was provided, so <households members> is required as well.");
            Trait trait2 = new Trait();
            trait2.id = "Auto-2";
            trait2.desc = "Vacant Households";
            trait2.regionTable = region.spec.id;
            trait2.regionTrait = region.spec.vacancies;
            trait2.regionTotal = region.spec.households;
            trait2.pumsTraitTable = Type.HOUSEHOLDS;
            trait2.pumsTraitField = rel.households.members;
            trait2.pumsTraitSelect = "0";
            trait2.pumsTotalTable = Type.HOUSEHOLDS;
            trait2.pumsTotalField = "1";
            mantra.add(trait2);
        }
        if (pop_ok) {
            Trait trait3 = new Trait();
            trait3.id = "Auto-3";
            trait3.desc = "Absolute Population";
            trait3.regionTable = region.spec.id;
            trait3.regionTrait = region.spec.population;
            trait3.regionTotal = "1";
            trait3.pumsTraitTable = Type.HOUSEHOLDS;
            trait3.pumsTraitField = rel.households.members;
            trait3.pumsTotalTable = null;
            mantra.add(trait3);
        }
        return mantra.toArray(new Trait[mantra.size()]);
    }

    /**
     * Find best region or attribute map for a trait.
     * 
     * @param trait object to examine
     * 
     * @return region data requested by attribute 'regionTable'
     * 
     * @throws DataException if region data or trait is bad
     */
    protected RegionData findGoalMap(Trait trait) {
        RegionData rgn;
        if (regionDatas.size() == 0) throw new DataException("No region maps are available."); else if (trait.regionTable == null) {
            if (regionDatas.size() == 1) rgn = regionDatas.values().iterator().next(); else throw new DataException(String.format("Project has %d region maps; trait must specify which to use: %s", regionDatas.size(), trait));
        } else {
            rgn = regionDatas.get(trait.regionTable);
            if (rgn == null) rgn = attributeMaps.get(trait.attribute);
            if (rgn == null) throw new DataException("No goal map available for " + trait);
        }
        return rgn;
    }

    /**
     * Build statistics goal object from a trait. Scans current region map and
     * table, and computes target values for each region. Can be used to analyze
     * the quality of an accumulation object.
     * <P>
     * Note this method must be synchronized with makeAccumStat() so the two
     * methods make matched pairs of objects.
     * 
     * @param trait
     *            specification for goals
     * 
     * @return an object with goal values prepared
     */
    public SpatialStatistic makeGoalStat(Trait trait) {
        RegionData rgn = findGoalMap(trait);
        if (!ObjectUtil.isBlank(trait.regionTrait)) {
            DataException.verifyNotBlank(rgn.spec.key, "Attribute \"key\" is missing from <regions> element.");
            DataException.verifyNotBlank(trait.regionTotal, "regionTotal is missing in " + trait);
            TractSpatialStatistic goal;
            try {
                double fixedTot = Double.parseDouble(trait.regionTotal);
                if (fixedTot != 1.0) throw new DataException("The only constant regionTotal supports is 1.0: " + trait);
                Count c = Count.createGoal(rgn.map, rgn.table, rgn.spec.key, trait.regionTrait);
                c.setLabel(trait.desc);
                goal = c;
            } catch (NumberFormatException e) {
                Proportion p = Proportion.createGoal(rgn.map, rgn.table, rgn.spec.key, trait.regionTrait, trait.regionTotal);
                p.setLabel(trait.desc);
                goal = p;
            }
            return goal;
        } else if (!ObjectUtil.isBlank(trait.attribute)) {
            Match m = Match.createGoal();
            if (!ObjectUtil.isBlank(trait.desc)) m.setLabel(trait.desc);
            return m;
        } else {
            throw new DataException("No goal specified in trait: " + trait);
        }
    }

    /**
     * Build statistics accumulation object from a trait. Computes values for an
     * arrangement of households, and can be compared to a goal object to
     * determine quality of arrangement.
     * <P>
     * Note this method must be synchronized with makeGoalStat() so the two
     * methods make matched pairs of objects.
     * 
     * @param trait
     *            specification for statistics
     * 
     * @return an object that computes and maintains statistics for sets of
     *         PumsHousehold objects
     */
    public SpatialStatistic makeAccumStat(Trait trait) {
        if (!ObjectUtil.isBlank(trait.regionTrait)) {
            TransformAttributes2double numer, denom;
            numer = new PumsTraitGetter(trait, householdSchema, populationSchema);
            denom = PumsTotalGetter.make(trait, householdSchema, populationSchema);
            RegionData rgn = findGoalMap(trait);
            TractSpatialStatistic stat;
            if (denom == null) {
                Count c = Count.createStat(rgn.map, numer);
                c.setLabel(trait.desc);
                stat = c;
            } else {
                Proportion p = Proportion.createStat(rgn.map, numer, denom);
                p.setLabel(trait.desc);
                stat = p;
            }
            return stat;
        } else if (!ObjectUtil.isBlank(trait.attribute)) {
            RegionData map = findGoalMap(trait);
            boolean src = (trait.pumsTraitTable == Type.POPULATION);
            Match m = Match.createStat(map.map, trait.attributeSelect, src, trait.pumsTraitField, trait.pumsTraitSelect);
            if (!ObjectUtil.isBlank(trait.desc)) m.setLabel(trait.desc);
            return m;
        } else {
            throw new DataException("No goal specified in trait: " + trait);
        }
    }

    /**
     * Build query that selects all people and households fitting the weighting
     * criteria.
     * 
     * @param crit fitting criteria from which query will be built
     * @return new query object
     */
    public PumsQuery makePumsQuery() {
        Set<Trait> keyTraits = crit.traitWeights.keySet();
        BitSet isPops = new BitSet();
        IntList attributes = new ArrayIntList();
        IntList minValues = new ArrayIntList();
        IntList maxValues = new ArrayIntList();
        for (Trait trait : keyTraits) {
            boolean isPop = (trait.pumsTraitTable == Type.POPULATION);
            int attribute;
            if (isPop) {
                attribute = populationSchema.findColumn(trait.pumsTraitField);
            } else {
                attribute = householdSchema.findColumn(trait.pumsTraitField);
            }
            String selector = null;
            if (trait.pumsTraitSelect != null) selector = trait.pumsTraitSelect; else selector = trait.pumsTraitContinuous;
            if (selector == null) selector = ""; else selector = selector.trim();
            if (selector.length() > 0) {
                String[] ranges = selector.split("\\s|,");
                for (String range : ranges) {
                    if (range.length() > 0) {
                        int p = range.indexOf('-');
                        try {
                            if (p < 0) {
                                isPops.set(attributes.size(), isPop);
                                attributes.add(attribute);
                                int first = Integer.parseInt(range);
                                minValues.add(first);
                                maxValues.add(first);
                            } else {
                                isPops.set(attributes.size(), isPop);
                                attributes.add(attribute);
                                int first = Integer.parseInt(range.substring(0, p));
                                int last = Integer.parseInt(range.substring(p + 1));
                                minValues.add(first);
                                maxValues.add(last);
                            }
                        } catch (NumberFormatException e) {
                            throw new IllegalArgumentException("Illegal range spec \"" + range + "\"");
                        } catch (IndexOutOfBoundsException e) {
                            throw new IllegalArgumentException("Illegal range spec \"" + range + "\"");
                        }
                    }
                }
            } else {
                isPops.set(attributes.size(), isPop);
                attributes.add(attribute);
                minValues.add(Integer.MIN_VALUE);
                maxValues.add(Integer.MAX_VALUE);
            }
        }
        PumsQuery pq = new PumsQuery();
        pq.addAndQuery(ObjectUtil.toArray(isPops, attributes.size()), attributes.toArray(), minValues.toArray(), maxValues.toArray());
        return pq;
    }

    /**
     * Convert string to int: if blank or invalid, becomes NODATA. If float, is
     * rounded probabilistically. (i.e. if string is "8.2", there's an 80%
     * chance that 8 will be returned, and a 20% chance of 9. Likewise, "-8.2"
     * can yield -8 or -9.)
     * 
     * @param sval
     *            string form of value
     * @return integer form of value
     */
    protected int parseInt(String sval) {
        int ival = PumsHousehold.NODATA_VALUE;
        if (!ObjectUtil.isBlank(sval)) {
            try {
                double dval = Double.parseDouble(sval);
                int ipart = (int) dval;
                double fpart = dval - ipart;
                if (fpart == 0) ival = (int) dval; else ival = ipart + (random.nextDouble() >= Math.abs(fpart) ? 0 : (int) Math.signum(fpart));
            } catch (NumberFormatException e) {
            }
        }
        return ival;
    }
}
