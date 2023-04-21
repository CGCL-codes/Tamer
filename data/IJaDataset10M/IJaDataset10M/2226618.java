package uk.ac.ebi.venndiagram.core.impl;

import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;
import org.apache.log4j.Logger;
import uk.ac.ebi.pride.model.implementation.core.ExperimentImpl;
import uk.ac.ebi.pride.model.implementation.mzData.CvParamImpl;
import uk.ac.ebi.pride.rdbms.ojb.controller.ExperimentQuery;
import uk.ac.ebi.pride.rdbms.ojb.controller.IdentificationQuery;
import uk.ac.ebi.pride.rdbms.ojb.controller.OJBController;
import uk.ac.ebi.pride.rdbms.ojb.controller.PridePersistenceException;
import uk.ac.ebi.venndiagram.VennDiagramConstants;
import uk.ac.ebi.venndiagram.core.interfaces.DiagramDataSource;
import uk.ac.ebi.venndiagram.core.interfaces.SetElement;
import java.util.*;

/**
 * This class is an implementation of the DiagramDataSource interface.
 * This class is designed to query pride for identifications for a given experiment for creating the venn diagrams
 * <p/>
 * Date: 18-Jul-2006
 * Time: 17:55:23
 *
 * @author: Sebastian Klie
 */
public class PrideDataSource implements DiagramDataSource {

    private static Logger logger = Logger.getLogger(PrideDataSource.class);

    private String dbAlias;

    private Long userId = null;

    private static int REFRESH_PERIOD = 21600;

    private static GeneralCacheAdministrator admin = PrideDataSourceCache.getInstance().getCache();

    public static final Comparator DEFAULT_COMPARATOR = new PrideSetElementComparator();

    private Comparator comparator = DEFAULT_COMPARATOR;

    /**
     * constructor that provides configuration and data access information
     *
     * @param dbAlias - the dbalias string to connect to
     * @param userId  - the userId (long) of the connected user, or null if not connected
     */
    public PrideDataSource(String dbAlias, Long userId) {
        this.dbAlias = dbAlias;
        this.userId = userId;
    }

    /**
     * This method return for a given experiment (accession) a set of PrideSetElement objects,
     * depending on the logged-in user private experiments will be stripped out.
     * This method uses the
     * uk.ac.ebi.pride.rdbms.ojb.controller.IdentificationQuery.getIdentificationReportByExperimentAccession()
     * report Query.
     *
     * @param classifier an pride experiment accession
     * @return Set a Set of PrideSetElement Objects which represent a subset of the information of a pride identifications
     *         see class: PrideSetElemnt for details
     */
    public Set<SetElement> getComparableElements(String classifier) {
        logger.info("PrideDataSource: Trying to get Experiments, for Acession [" + classifier + "]");
        HashSet<SetElement> comparableElements = null;
        boolean updated = false;
        try {
            comparableElements = (HashSet<SetElement>) admin.getFromCache(classifier, REFRESH_PERIOD);
            logger.info("...retrieved from cache.");
        } catch (NeedsRefreshException nre) {
            OJBController ojbcontroller = null;
            HashMap<String, SetElement> workingMap = new HashMap<String, SetElement>();
            Collection<Object[]> pride_identification;
            try {
                ojbcontroller = new OJBController(dbAlias);
                IdentificationQuery identQuery = ojbcontroller.getIdentificationQuery();
                pride_identification = identQuery.getIdentificationReportByExperimentAccession(classifier, userId, true);
                logger.debug("collectionQuerySize (before dropping duplicates): " + pride_identification.size());
                if (!pride_identification.isEmpty()) {
                    Object[] ident;
                    for (Iterator<Object[]> it = pride_identification.iterator(); it.hasNext(); ) {
                        ident = it.next();
                        if (workingMap.get((String) ident[1]) == null) {
                            PrideSetElement prideElement = new PrideSetElement();
                            if (ident[0] != null) {
                                prideElement.setIdentificationId(ident[0].toString());
                            }
                            if (ident[1] != null) {
                                prideElement.setProteinAccession(ident[1].toString());
                            }
                            if (ident[2] != null) {
                                prideElement.setProteinVersion(ident[2].toString());
                            } else {
                                prideElement.setProteinVersion("");
                            }
                            if (ident[3] != null) {
                                prideElement.setDatabase(ident[3].toString());
                            }
                            prideElement.addMappingData(identQuery.getMappingDataByIdentificationId(new Long(ident[0].toString())));
                            workingMap.put(prideElement.getProteinAccession(), prideElement);
                        } else {
                            continue;
                        }
                    }
                    comparableElements = new HashSet<SetElement>();
                    comparableElements.addAll(workingMap.values());
                    logger.debug("collectionQuerySize (after dropping duplicates): " + comparableElements.size());
                    admin.putInCache(classifier, comparableElements);
                    updated = true;
                } else {
                    logger.debug("pride_experiments Collection is empty");
                }
            } catch (PridePersistenceException e) {
                logger.error("PridePersistenceException: ", e);
            } finally {
                if (ojbcontroller != null) {
                    ojbcontroller.close();
                }
            }
        } finally {
            if (!updated) {
                admin.cancelUpdate(classifier);
            }
        }
        return comparableElements;
    }

    /**
     * The Method gathers a subset of the metadata of a pride_experiment like the sample description and the
     * experiment title
     *
     * @param classifier a String which is an experiment accession
     * @return Map a Map which contains metadata for the given (classifier) Experiment
     *         keys: "expTitle"     -> value: an experiment title, a String
     *         "sampleDesc"   -> value: a Collection wich contains all sampleDescription cvParams as Strings
     */
    public Map getMetadataForExperiment(String classifier) {
        OJBController ojbcontroller = null;
        Map expMetaData = new HashMap();
        Collection experiments = null;
        try {
            ojbcontroller = new OJBController(dbAlias);
            ExperimentQuery expquery = ojbcontroller.getExperimentQuery();
            experiments = expquery.getExperimentsByExperimentAccession(classifier, null, false);
        } catch (PridePersistenceException e) {
            logger.error("PridePersistenceException: ", e);
        } finally {
            if (ojbcontroller != null) {
                ojbcontroller.close();
            }
        }
        ExperimentImpl experiment;
        if (experiments != null && experiments.size() == 1) {
            Iterator it = experiments.iterator();
            experiment = (ExperimentImpl) it.next();
            expMetaData.put(VennDiagramConstants.KEY_FOR_EXP_METADATA_EXP_TITLE, experiment.getTitle());
            Collection sampleCvParamstmp = experiment.getMzData().getSampleDescriptionCvParams();
            Iterator it2 = sampleCvParamstmp.iterator();
            Collection sampleCvParams = new ArrayList();
            CvParamImpl cvParam;
            while (it2.hasNext()) {
                cvParam = (CvParamImpl) it2.next();
                sampleCvParams.add(cvParam.getCVLookup() + ": " + cvParam.getName());
            }
            expMetaData.put(VennDiagramConstants.KEY_FOR_EXP_METADATA_SAMPLE_DESC, sampleCvParams);
        } else {
            expMetaData.put("expTitle", "PRIVATE DATA");
            expMetaData.put("sampleDesc", new ArrayList());
        }
        return expMetaData;
    }

    public void setComparator(Comparator comparator) {
        this.comparator = comparator;
    }

    public Comparator getComparator() {
        return comparator;
    }
}
