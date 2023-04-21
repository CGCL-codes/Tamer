package edu.ucdavis.genomics.metabolomics.binbase.algorythm.matching;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import org.apache.log4j.Logger;
import org.jdom.Element;
import edu.ucdavis.genomics.metabolomics.binbase.algorythm.AlgorythmHandler;
import edu.ucdavis.genomics.metabolomics.binbase.algorythm.filter.DefaultFilter;
import edu.ucdavis.genomics.metabolomics.binbase.algorythm.filter.MassSpecFilter;
import edu.ucdavis.genomics.metabolomics.binbase.algorythm.matching.similarity.SpectraLimiter;
import edu.ucdavis.genomics.metabolomics.binbase.algorythm.result.ResultHandler;
import edu.ucdavis.genomics.metabolomics.binbase.algorythm.thread.ExecutorsServiceFactory;
import edu.ucdavis.genomics.metabolomics.binbase.algorythm.validate.ValidateSpectra;
import edu.ucdavis.genomics.metabolomics.binbase.diagnostics.DiagnosticsService;
import edu.ucdavis.genomics.metabolomics.binbase.diagnostics.DiagnosticsServiceFactory;
import edu.ucdavis.genomics.metabolomics.exception.ConfigurationException;
import edu.ucdavis.genomics.metabolomics.util.SQLObject;
import edu.ucdavis.genomics.metabolomics.util.SQLable;
import edu.ucdavis.genomics.metabolomics.util.config.Configable;
import edu.ucdavis.genomics.metabolomics.util.statistics.collecting.RuntimeStatistics;
import edu.ucdavis.genomics.metabolomics.util.statistics.collecting.RuntimeStatisticsFactory;
import edu.ucdavis.genomics.metabolomics.util.statistics.collecting.StatisticSupport;

/**
 * @author wohlgemuth
 */
public abstract class AbstractMatching extends SQLObject implements Matchable, SQLable, Annotation, StatisticSupport {

    protected Logger logger = Logger.getLogger(getClass());

    /**
	 * DOCUMENT ME!
	 * 
	 * @uml.property name="algorythemHandler"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
    private AlgorythmHandler algorythemHandler;

    protected void setAlgorythemHandler(AlgorythmHandler algorythemHandler) {
        this.algorythemHandler = algorythemHandler;
    }

    private RuntimeStatistics collector = null;

    /**
	 * needed to ensure that the spectra are compatible to the library
	 */
    private SpectraLimiter limiter = null;

    /**
	 * enth?lt alle anotierten spektre
	 */
    private Collection<Map<String, Object>> assigned;

    /**
	 * enth?lt alle bins
	 */
    private Collection<Map<String, Object>> bins;

    /**
	 * DOCUMENT ME!
	 */
    private Collection<Map<String, Object>> unknowns;

    /**
	 * definiert den filter welche spektren nur abgerufen werden sollen
	 * 
	 * @uml.property name="filter"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
    private MassSpecFilter unknownFilter = new DefaultFilter();

    private MassSpecFilter binFilter = new DefaultFilter();

    /**
	 * DOCUMENT ME!
	 */
    private PreparedStatement libraryStatement = null;

    /**
	 * DOCUMENT ME!
	 */
    private PreparedStatement unknownStatement = null;

    /**
	 * DOCUMENT ME!
	 * 
	 * @uml.property name="resultHandler"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
    private ResultHandler resultHandler;

    /**
	 * DOCUMENT ME!
	 */
    private int binSize;

    /**
	 * DOCUMENT ME!
	 */
    private int sampleId;

    private Element config;

    /**
	 * @see edu.ucdavis.genomics.metabolomics.binbase.algorythm.matching.Matchable#setAlgorythmHandler(edu.ucdavis.genomics.metabolomics.binbase.binlib.algorythm.AlgorythmHandler)
	 */
    public void setAlgorythmHandler(AlgorythmHandler handler) {
        if (handler != null) {
            logger.info("handler: " + handler);
            handler.setConnection(this.getConnection());
            this.algorythemHandler = handler;
        } else {
            logger.warn("trying to set a handler but it's null!");
        }
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * 
	 * @return DOCUMENT ME!
	 * @uml.property name="assigned"
	 */
    @Override
    public final Collection<Map<String, Object>> getAssigned() {
        if (assigned == null) {
            assigned = new ArrayList<Map<String, Object>>();
        }
        return assigned;
    }

    /**
	 * loads the bins from the system is not already loaded
	 */
    public final Collection<Map<String, Object>> getBins() {
        if (bins == null) {
            try {
                logger.info("loading bin collection in memory");
                bins = loadBins();
                binSize = bins.size();
                logger.debug("loaded bins: " + binSize);
            } catch (SQLException e) {
                logger.error("Exception at bin loading", e);
                bins = new ArrayList<Map<String, Object>>();
            }
        } else {
            logger.debug("bin collection was already initialized");
        }
        return bins;
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @param filter
	 *            DOCUMENT ME!
	 * @uml.property name="filter"
	 */
    public final void setUnknownFilter(MassSpecFilter filter) {
        logger.info("setting unknown filter: " + binFilter.getClass());
        this.unknownFilter = filter;
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 * @uml.property name="filter"
	 */
    public final MassSpecFilter getUnknownFilter() {
        return unknownFilter;
    }

    /**
	 * @return Returns the binSize.
	 * @uml.property name="binSize"
	 */
    public int getBinSize() {
        return binSize;
    }

    /**
	 * @return
	 */
    public static boolean isEnableBestRiMode() {
        return true;
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @param config
	 *            DOCUMENT ME!
	 */
    public void setConfig(Element config) {
    }

    /**
	 * @see edu.ucdavis.genomics.metabolomics.binbase.algorythm.matching.Matchable#setResultHandler(edu.ucdavis.genomics.metabolomics.binbase.binlib.algorythm.result.ResultHandler)
	 * @uml.property name="resultHandler"
	 */
    public void setResultHandler(ResultHandler handler) {
        this.resultHandler = handler;
    }

    /**
	 * @see edu.ucdavis.genomics.metabolomics.binbase.algorythm.matching.Matchable#setSampleId(int)
	 * @uml.property name="sampleId"
	 */
    public void setSampleId(int id) {
        this.sampleId = id;
        this.bins = null;
        this.unknowns = null;
        this.assigned = null;
    }

    /**
	 * @return Returns the sampleId.
	 * @uml.property name="sampleId"
	 */
    public int getSampleId() {
        return sampleId;
    }

    /**
	 * loads the unknowns if not already done so
	 * 
	 * @return
	 */
    public Collection<Map<String, Object>> getUnknowns() {
        if (unknowns == null) {
            try {
                logger.debug("load unknowns");
                unknowns = loadUnknows();
                logger.debug("loaded unknown: " + unknowns.size());
            } catch (SQLException e) {
                logger.error("Exception at unknown loading", e);
                unknowns = new ArrayList<Map<String, Object>>();
            }
        }
        return unknowns;
    }

    /**
	 * @see edu.ucdavis.genomics.metabolomics.binbase.algorythm.matching.Matchable#flush()
	 */
    public void flush() {
        this.bins = null;
        this.unknowns = null;
        this.assigned = null;
    }

    /**
	 * @return Returns the algorythemHandler.
	 * @uml.property name="algorythemHandler"
	 */
    public AlgorythmHandler getAlgorythemHandler() {
        return algorythemHandler;
    }

    /**
	 * @param bins
	 *            The bins to set.
	 * @uml.property name="bins"
	 */
    protected void setBins(Collection<Map<String, Object>> bins) {
        this.bins = bins;
    }

    /**
	 * l?dt das resultset in eine List von maps
	 * 
	 * @param statement
	 * @return
	 * @throws SQLException
	 */
    protected final Collection<Map<String, Object>> getData(PreparedStatement statement) throws SQLException {
        logger.debug("start loading data");
        ResultSet result = statement.executeQuery();
        Collection<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        while (result.next()) {
            Map<String, Object> map = new HashMap<String, Object>();
            ResultSetMetaData meta = result.getMetaData();
            int column = meta.getColumnCount();
            for (int i = 0; i < column; i++) {
                map.put(meta.getColumnName(i + 1), result.getString(i + 1));
            }
            String spectra = (String) map.get("spectra");
            spectra = limiter.limitSpectra(spectra);
            map.put("spectra", spectra);
            map.put("CALCULATED_SPECTRA", ValidateSpectra.convert(map.get("spectra").toString()));
            meta = null;
            list.add(map);
        }
        result.close();
        logger.debug("finished loading data");
        return list;
    }

    /**
	 * @return Returns the libraryStatement.
	 * @uml.property name="libraryStatement"
	 */
    protected PreparedStatement getLibraryStatement() {
        return libraryStatement;
    }

    /**
	 * @return Returns the resultHandler.
	 * @uml.property name="resultHandler"
	 */
    protected ResultHandler getResultHandler() {
        return resultHandler;
    }

    /**
	 * @return Returns the unknownStatement.
	 * @uml.property name="unknownStatement"
	 */
    protected PreparedStatement getUnknownStatement() {
        return unknownStatement;
    }

    /**
	 * add an unknown to the assigned list and remove it from the unknown list
	 * 
	 * @param map
	 */
    protected void addAssigned(Map<String, Object> map) {
        this.getAssigned().add(map);
        boolean unknownRemoved = this.getUnknowns().remove(map);
        if (unknownRemoved == false) {
            int spectraId = Integer.parseInt(map.get("spectra_id").toString());
            getDiagnosticsService().diagnosticActionFailed(spectraId, this.getClass(), "remove spectra from unknowns", "the map to be removed was not found in the list of unknowns! This is a bug!", new Object[] {});
        }
    }

    /**
	 * l?dt die bins aus der datenbank
	 * 
	 * @return
	 * @throws SQLException
	 */
    protected Collection<Map<String, Object>> loadBins() throws SQLException {
        Date begin = getCollector().generateDate();
        try {
            Collection<Map<String, Object>> data = this.getData(this.libraryStatement);
            if (binFilter instanceof DefaultFilter) {
                logger.debug("bin data are not filtered");
                return data;
            } else {
                logger.debug("filter data with: " + binFilter);
                final Collection<Map<String, Object>> result = new ArrayList<Map<String, Object>>(data.size());
                ExecutorService service = ExecutorsServiceFactory.createService();
                int i = 0;
                for (final Map<String, Object> o : data) {
                    i = i + 1;
                    final int x = i;
                    Runnable run = new Runnable() {

                        public void run() {
                            if (binFilter.accept(o)) {
                                result.add(o);
                            }
                        }
                    };
                    if (isMultithreaded()) {
                        service.execute(run);
                    } else {
                        run.run();
                    }
                }
                ExecutorsServiceFactory.shutdownService(service);
                return result;
            }
        } finally {
            collector.collectSample(sampleId, begin, collector.generateDate(), "load bin spectra", this.getStatisticalGroup());
        }
    }

    /**
	 * l?dt die unbekannten aus der datenbank
	 * 
	 * @return
	 * @throws SQLException
	 * @throws ConfigurationException
	 */
    @SuppressWarnings("unchecked")
    protected Collection<Map<String, Object>> loadUnknows() throws SQLException {
        Date begin = getCollector().generateDate();
        try {
            Date begin2 = getCollector().generateDate();
            this.unknownStatement.setInt(1, this.sampleId);
            Collection<Map<String, Object>> list = this.getData(this.unknownStatement);
            Element elementValues = Configable.CONFIG.getElement("values.matching");
            Collection<Element> elementValuesList = elementValues.getChildren("leco");
            if (list.isEmpty() == false) {
                String version = "no version defiend";
                Map<String, Object> spectra = list.iterator().next();
                if (spectra.get("leco") != null) {
                    version = spectra.get("leco").toString();
                }
                Element element = null;
                Element failback = null;
                for (Element current : elementValuesList) {
                    if (current.getAttributeValue("version").equals(version)) {
                        element = current;
                    } else if (current.getAttributeValue("version").equals("default")) {
                        failback = current;
                    }
                }
                if (element == null) {
                    element = failback;
                }
                if (element == null) {
                    logger.warn("can't find any matching configurations!");
                }
                this.setHandlerConfiguration(element);
            } else {
                logger.warn("no massspecs found for this bin!");
            }
            if (unknownFilter instanceof DefaultFilter) {
                logger.info("unknowns are not filtered");
                return list;
            } else {
                logger.info("unknowns are filtered using: " + unknownFilter);
                Collection<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
                for (Map<String, Object> o : list) {
                    if (unknownFilter.accept(o)) {
                        result.add(o);
                    }
                }
                collector.collectSample(sampleId, begin2, collector.generateDate(), "load unknown spectra", getStatisticalGroup());
                return result;
            }
        } finally {
            collector.collectSample(sampleId, begin, collector.generateDate(), "load unknown spectra", this.getStatisticalGroup());
        }
    }

    /**
	 * @see edu.ucdavis.genomics.metabolomics.util.SQLObject#prepareStatements()
	 */
    protected void prepareStatements() throws Exception {
        super.prepareStatements();
        this.bins = null;
        this.assigned = null;
        this.unknowns = null;
        this.libraryStatement = this.getConnection().prepareStatement(SQL_CONFIG.getValue(CLASS + ".libraryStatement"));
        this.unknownStatement = this.getConnection().prepareStatement(SQL_CONFIG.getValue(CLASS + ".unknownStatement"));
        this.collector = RuntimeStatisticsFactory.newInstance().createStatistics(getConnection());
        this.limiter = SpectraLimiter.createLimiter(Configable.CONFIG.getElement("similarity.range"), this.getConnection().getMetaData().getUserName().toLowerCase());
    }

    /**
	 * remove this specific bin, because its already assigned to an unknown and
	 * not longer available
	 * 
	 * @param map
	 */
    protected void removeBin(Map<String, Object> map) {
        this.getBins().remove(map);
    }

    public Element getHandlerConfiguration() {
        return config;
    }

    public void setHandlerConfiguration(Element element) {
        this.config = element;
        this.getResultHandler().setConfiguration(config);
    }

    public MassSpecFilter getBinFilter() {
        return binFilter;
    }

    public void setBinFilter(MassSpecFilter binFilter) {
        logger.info("setting bin filter: " + binFilter.getClass());
        this.binFilter = binFilter;
    }

    /**
	 * displays map details
	 * 
	 * @param bin
	 */
    @SuppressWarnings("unchecked")
    public void displayMapDetails(Map<String, Object> bin) {
        if (logger.isDebugEnabled()) {
            Iterator keySet = bin.keySet().iterator();
            while (keySet.hasNext()) {
                Object key = keySet.next();
                Object value = bin.get(key);
                if (value instanceof Map) {
                    logger.debug(key + ": was map, sorry no debug informations available, use a debugger...");
                }
                if (value instanceof Collection) {
                    logger.debug(key + ": was a collection, sorry no debug informations available, use a debugger...");
                } else if (key.toString().equals("spectra")) {
                    logger.debug(key + ": was a spectra, sorry no debug informations available, use a debugger...");
                } else {
                    logger.debug(key + ":" + bin.get(key));
                }
            }
            logger.debug("\n");
        }
    }

    /**
	 * saves the assignd peaks to the database
	 * 
	 * @throws Exception
	 */
    protected void saveAssignmentToDatabase() throws Exception {
        for (Map<String, Object> o : this.getAssigned()) {
            int spectraId = Integer.parseInt(o.get("spectra_id").toString());
            getDiagnosticsService().diagnosticActionSuccess(spectraId, this.getClass(), "assigning matched bins", "assigning the stored spectra to the database", new Object[] { getResultHandler().getClass().getSimpleName() });
            this.getResultHandler().assignBin(o);
        }
    }

    @Override
    public DiagnosticsService getDiagnosticsService() {
        return service;
    }

    private DiagnosticsService service = DiagnosticsServiceFactory.newInstance().createService();
}
