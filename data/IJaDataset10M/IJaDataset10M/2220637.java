package mil.army.usace.ehlschlaeger.digitalpopulations.censusgen;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import mil.army.usace.ehlschlaeger.digitalpopulations.censusgen.filerelationship.AttributeMap;
import mil.army.usace.ehlschlaeger.digitalpopulations.censusgen.filerelationship.Regions;
import mil.army.usace.ehlschlaeger.rgik.core.CSVTable;
import mil.army.usace.ehlschlaeger.rgik.core.DataException;
import mil.army.usace.ehlschlaeger.rgik.core.GISClass;
import mil.army.usace.ehlschlaeger.rgik.core.Reclass;
import mil.army.usace.ehlschlaeger.rgik.util.FileUtil;
import mil.army.usace.ehlschlaeger.rgik.util.ObjectUtil;

/**
 * Wrapper for a region map and its table, plus the spec for what we're intended
 * to do with them.
 * 
 * @author William R. Zwicky
 */
public class RegionData {

    /** File names and interesting columns. */
    public Regions spec;

    /** Contents of region map identified by spec. */
    public GISClass map;

    /** Contents of region table identified by spec. */
    public CSVTable table;

    /**
     * Full number of households present in region table. The households PUMS
     * table will only contain a sampling of these.
     */
    public int aggregateHouseholds;

    /**
     * Total number of people present in region table. The population PUMS table
     * will only contain a sampling of these.
     */
    public int aggregatePopulation;

    /**
     * Construct blank instance.
     */
    public RegionData() {
    }

    /**
     * Init this object with data described by 'spec'. Call validate() after
     * this.
     * 
     * @param dataDir
     *            base directory of input files
     * @param regionSpec
     *            input files and info
     * @param errors
     *            receiver of problems we've discovered
     * 
     * @throws IOException
     *             on any file error
     */
    protected void loadThis(File dataDir, Regions regionSpec, List<String> errors) throws IOException {
        this.spec = regionSpec;
        String tractsMapName = FileUtil.resolve(dataDir, regionSpec.map).getPath();
        map = GISClass.loadEsriAscii(tractsMapName);
        if (ObjectUtil.isBlank(regionSpec.table)) {
            table = null;
        } else {
            String tractsTableName = FileUtil.resolve(dataDir, regionSpec.table).getPath();
            table = new CSVTable(tractsTableName);
            String newColumnName = "row_number";
            int keyCol = table.addKeyColumn(newColumnName);
            map = Reclass.reclass(map, table, regionSpec.key, newColumnName);
            regionSpec.key = newColumnName;
            assert map.getMinimumValue() == 0;
            aggregateHouseholds = 0;
            if (!ObjectUtil.isBlank(regionSpec.households)) {
                int col = table.findColumn(regionSpec.households);
                for (int i = table.getRowCount() - 1; i >= 0; i--) {
                    String hiar = table.getStringAt(i, col);
                    aggregateHouseholds += Integer.parseInt(hiar);
                }
            }
            aggregatePopulation = 0;
            if (!ObjectUtil.isBlank(regionSpec.population)) {
                int col = table.findColumn(regionSpec.population);
                for (int i = table.getRowCount() - 1; i >= 0; i--) {
                    String np = table.getStringAt(i, col);
                    try {
                        aggregatePopulation += Integer.parseInt(np);
                    } catch (NumberFormatException e) {
                        errors.add(String.format("%s in row %s in region file %s", ObjectUtil.getMessage(e), table.getStringAt(i, keyCol), tractsTableName));
                    }
                }
            }
        }
    }

    /**
     * Perform some consistency checks on the loaded region data: total houses
     * vs. vacant houses vs. number of occupants.  Call this after load().
     * 
     * @param errors
     *            receiver of problems we've discovered
     */
    protected void validate(ArrayList<String> errs) {
        boolean hoh_ok = !ObjectUtil.isBlank(spec.households);
        boolean pop_ok = !ObjectUtil.isBlank(spec.population);
        int hohcol = -1, vaccol = -1, popcol = -1;
        if (hoh_ok) {
            hohcol = table.findColumn(spec.households);
            vaccol = table.findColumn(spec.vacancies);
        }
        if (pop_ok) {
            popcol = table.findColumn(spec.population);
        }
        if (table != null) {
            int keycol = table.findColumn(spec.key);
            for (int r = 0; r < table.getRowCount(); r++) {
                int nh = 0, nv = 0, np = 0;
                String sk = table.getStringAt(r, keycol);
                if (hoh_ok) {
                    String sh = table.getStringAt(r, hohcol);
                    String sv = table.getStringAt(r, vaccol);
                    nh = ObjectUtil.parseInt(sh, 0);
                    nv = ObjectUtil.parseInt(sv, 0);
                }
                if (pop_ok) {
                    String sp = table.getStringAt(r, popcol);
                    np = ObjectUtil.parseInt(sp, 0);
                }
                if (hoh_ok) {
                    if (nv > nh) errs.add(String.format("Region %s has more vacancies than households.", sk));
                    if (pop_ok) {
                        if (np > 0 && nh <= nv) errs.add(String.format("Region %s contains people, but also reports all households as vacant.", sk));
                        if (np == 0 && nh - nv > 0) errs.add(String.format("Region %s is vacant, but not all households are vacant.", sk));
                    }
                }
            }
        }
    }

    /**
     * 
     * @param spec
     * @param dataDir
     * @return
     * @throws DataException
     *             on any logic error
     */
    public static RegionData load(Regions spec, File dataDir) throws IOException {
        ArrayList<String> errors = new ArrayList<String>();
        RegionData data = new RegionData();
        data.loadThis(dataDir, spec, errors);
        data.validate(errors);
        if (errors.size() > 0) {
            String msg = "Errors while loading attributes:\n  ";
            msg += ObjectUtil.join(errors, "\n  ");
            throw new DataException(msg);
        }
        return data;
    }

    /**
     * 
     * @param spec
     * @param dataDir
     * @return
     * @throws IOException
     * @throws DataException
     *             on any logic error
     */
    public static RegionData load(AttributeMap spec, File dataDir) throws IOException {
        ArrayList<String> errors = new ArrayList<String>();
        Regions rspec = new Regions(spec.id, spec.map, spec.table, spec.key);
        RegionData data = new RegionData();
        data.loadThis(dataDir, rspec, errors);
        data.validate(errors);
        if (errors.size() > 0) {
            String msg = "Errors while loading attributes:\n  ";
            msg += ObjectUtil.join(errors, "\n  ");
            throw new DataException(msg);
        }
        return data;
    }
}
