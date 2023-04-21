package org.gbif.checklistbank.imports;

import org.gbif.checklistbank.Constants;
import org.gbif.checklistbank.jdbc.DataAccessException;
import org.gbif.checklistbank.jdbc.JdbcTemplate;
import org.gbif.checklistbank.model.Checklist;
import org.gbif.checklistbank.service.ChecklistService;
import org.gbif.checklistbank.service.impl.PgSqlBaseService;
import org.gbif.checklistbank.utils.PgSqlUtils;
import org.gbif.ecat.cfg.DataDirConfig;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.Date;
import javax.sql.DataSource;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public abstract class ImportBasePgSql extends PgSqlBaseService {

    private final DecimalFormat speedFormat = new DecimalFormat("0.00 rec/s");

    protected static final Marker SUMMARY = MarkerFactory.getMarker("SUMMARY");

    protected final ChecklistService checklistService;

    protected final Checklist checklist;

    protected final boolean isNub;

    protected final String IMPORT_SCHEMA;

    protected final String IMP_TBL_CLASSIFICATION;

    protected final String IMP_TBL_DESCRIPTION;

    protected final String IMP_TBL_DISTRIBUTION;

    protected final String IMP_TBL_IDENTIFIER;

    protected final String IMP_TBL_IMAGES;

    protected final String IMP_TBL_REFERENCES;

    protected final String IMP_TBL_RAW_USAGE;

    protected final String IMP_TBL_SPECIES_DATA;

    protected final String IMP_TBL_SPECIMEN;

    protected final String IMP_TBL_USAGE;

    protected final String IMP_TBL_VERNACULAR;

    public static final String COLUMNS_DESCRIPTION = "usage_fk,description,type_fk,language,source_fk,creator,contributor,license";

    public static final String COLUMNS_DISTRIBUTION = "usage_fk, area_fk,country, occurrence_status_fk,threat_status_fk,establishment_means_fk,appendix_cites_fk, start_day_of_year,end_day_of_year, start_year,end_year, source_fk, remarks";

    public static final String COLUMNS_IDENTIFIER = "usage_fk,identifier,format_fk,type_fk";

    public static final String COLUMNS_IMAGES = "usage_fk,url,thumb_url,link,title,description,spatial,license,creator,created";

    public static final String COLUMNS_REFERENCES = "usage_fk,citation_fk";

    public static final String COLUMNS_RAW_USAGE = "usage_fk,scientific_name,remarks,json,created";

    public static final String COLUMNS_SPECIES_DATA = "usage_fk,marine,terrestrial,extinct,hybrid,living_period,age_in_days,size_in_millimeter,mass_in_gram,life_form_fk,habitat_fk";

    public static final String COLUMNS_SPECIMEN = "usage_fk,citation_fk,type_status_fk,type_designated_by_fk,type_designation_type_fk,scientific_name,taxon_rank_fk,occurrence_id,institution_code,collection_code,catalog_number,locality,recorded_by,source_fk,verbatim_event_date,verbatim_label,verbatim_longitude,verbatim_latitude";

    public static final String COLUMNS_VERNACULAR = "usage_fk,name_string_fk,language,life_stage_fk,sex_fk,organism_part_fk,area_fk";

    private long startTime;

    private long lastLogTime;

    protected ImportBasePgSql(DataDirConfig cfg, DataSource ds, JdbcTemplate jdbcTemplate, Checklist checklist, ChecklistService checklistService) {
        super(cfg, ds, jdbcTemplate);
        this.checklistService = checklistService;
        this.checklist = checklist;
        if (checklist.getId().equals(Constants.NUB_CHECKLIST_ID)) {
            isNub = true;
        } else {
            isNub = false;
        }
        IMPORT_SCHEMA = importSchema(checklist.getId());
        IMP_TBL_CLASSIFICATION = IMPORT_SCHEMA + "." + ClassificationGeneratorPgSql.CLASSIFICATION_TABLE;
        IMP_TBL_DESCRIPTION = IMPORT_SCHEMA + ".description";
        IMP_TBL_DISTRIBUTION = IMPORT_SCHEMA + ".distribution";
        IMP_TBL_IDENTIFIER = IMPORT_SCHEMA + ".identifier";
        IMP_TBL_IMAGES = IMPORT_SCHEMA + ".image";
        IMP_TBL_RAW_USAGE = IMPORT_SCHEMA + ".raw_usage";
        IMP_TBL_REFERENCES = IMPORT_SCHEMA + ".name_usage_references";
        IMP_TBL_SPECIES_DATA = IMPORT_SCHEMA + ".species_data";
        IMP_TBL_SPECIMEN = IMPORT_SCHEMA + ".specimen";
        IMP_TBL_USAGE = IMPORT_SCHEMA + ".name_usage";
        IMP_TBL_VERNACULAR = IMPORT_SCHEMA + ".vernacular_name";
    }

    public static String importSchema(Integer checklistId) {
        return "import" + checklistId;
    }

    protected void addIndex(Connection conn, String... attributes) {
        String sql = PgSqlUtils.addIndexSql(IMP_TBL_USAGE, attributes);
        log.debug("Adding index: " + sql);
        try {
            jdbcTemplate.executeUpdate(conn, sql);
        } catch (DataAccessException e) {
            log.debug("Could not add index. Error: " + e.getMessage(), e);
        }
    }

    public void createImportSchema() {
        execute("DROP schema IF EXISTS " + IMPORT_SCHEMA + " cascade");
        execute("CREATE SCHEMA " + IMPORT_SCHEMA);
        execute("create table " + IMP_TBL_DESCRIPTION + " (like public.description including defaults excluding constraints excluding indexes)");
        execute("create table " + IMP_TBL_DISTRIBUTION + " (like public.distribution including defaults excluding constraints excluding indexes)");
        execute("create table " + IMP_TBL_IDENTIFIER + " (like public.identifier including defaults excluding constraints excluding indexes)");
        execute("create table " + IMP_TBL_IMAGES + " (like public.image including defaults excluding constraints excluding indexes)");
        execute("create table " + IMP_TBL_RAW_USAGE + " (like public.raw_usage including defaults excluding constraints excluding indexes)");
        execute("create table " + IMP_TBL_REFERENCES + " (like public.name_usage_references including defaults excluding constraints excluding indexes)");
        execute("create table " + IMP_TBL_SPECIES_DATA + " (like public.species_data including defaults excluding constraints excluding indexes)");
        execute("create table " + IMP_TBL_SPECIMEN + " (like public.specimen including defaults excluding constraints excluding indexes)");
        execute("create table " + IMP_TBL_VERNACULAR + " (like public.vernacular_name including defaults excluding constraints excluding indexes)");
        if (isNub) {
            execute("create table " + IMP_TBL_USAGE + " (like public.name_usage including defaults excluding constraints excluding indexes)");
        } else {
            execute("create table " + IMP_TBL_USAGE + " (like public.templ_name_usage including defaults including constraints including indexes)");
        }
        log.debug("import schema " + IMPORT_SCHEMA + " initialised.");
    }

    protected String getAverageSpeed(long records) {
        return getAverageSpeed(startTime, System.currentTimeMillis(), records);
    }

    protected String getAverageSpeed(long start, long end, long records) {
        double speed = ((double) records) / ((double) (end - start)) * 1000.0d;
        return speedFormat.format(speed);
    }

    public long getLastLogTime() {
        return lastLogTime;
    }

    public long getStartTime() {
        return startTime;
    }

    protected void logBreakpoint(String task) {
        long now = new Date().getTime();
        log.info("Finished " + task + " in " + (now - lastLogTime) + " msec (=" + timeInMinutes(now - lastLogTime) + "s)");
        lastLogTime = now;
    }

    protected void removeIndex(String... attributes) {
        String sql = PgSqlUtils.removeIndexSql("name_usage_import", attributes);
        log.debug("Removing index: " + sql);
        execute(sql);
    }

    protected void setLogBreakpoint() {
        lastLogTime = new Date().getTime();
    }

    protected void startTimeLogging() {
        startTime = new Date().getTime();
        lastLogTime = startTime;
    }

    protected long timeInMinutes(long msec) {
        return (msec) / 60000l;
    }
}
