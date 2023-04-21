package reports.cites;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dao.OccurrenceRecordDAO;
import org.gbif.portal.dao.RelationshipAssertionDAO;
import org.gbif.portal.dao.TaxonConceptDAO;
import org.gbif.portal.dao.TaxonNameDAO;
import org.gbif.portal.model.RelationshipAssertion;
import org.gbif.portal.model.TaxonConcept;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Generates some stats based on the CITES plants and animals lists of names
 * @author trobertson
 */
public class CitesReportTAB {

    protected static Log log = LogFactory.getLog(CitesReportTAB.class);

    protected TaxonNameDAO taxonNameDAO;

    protected TaxonConceptDAO taxonConceptDAO;

    protected OccurrenceRecordDAO occurrenceRecordDAO;

    protected RelationshipAssertionDAO relationshipAssertionDAO;

    public void run() throws IOException {
        run("animals.txt");
    }

    protected void run(String filename) throws IOException {
        log.info("Starting CITES report on: " + filename);
        InputStream is = getClass().getResourceAsStream(filename);
        BufferedReader bf = new BufferedReader(new InputStreamReader(is));
        String line = bf.readLine();
        List<String> unmodified_namesI = new LinkedList<String>();
        List<String> unmodified_namesII = new LinkedList<String>();
        List<String> unmodified_namesIII = new LinkedList<String>();
        while (line != null) {
            line = bf.readLine();
            String g = getDelimittedPart(line, "\t", 0);
            String sp = getDelimittedPart(line, "\t", 1);
            String ssp = getDelimittedPart(line, "\t", 2);
            String type = getDelimittedPart(line, "\t", 3);
            String name = buildName(g, sp, ssp);
            if (StringUtils.equalsIgnoreCase("I", type)) {
                unmodified_namesI.add(name);
            } else if (StringUtils.equalsIgnoreCase("II", type)) {
                unmodified_namesII.add(name);
            } else if (StringUtils.equalsIgnoreCase("III", type)) {
                unmodified_namesIII.add(name);
            }
        }
        List<String> namesI = format(unmodified_namesI);
        unmodified_namesI = null;
        List<String> namesII = format(unmodified_namesII);
        unmodified_namesII = null;
        List<String> namesIII = format(unmodified_namesIII);
        unmodified_namesIII = null;
        List<NameReport> report = reportOnNames(namesI);
        summarise("I", report);
        report = reportOnNames(namesII);
        summarise("II", report);
        report = reportOnNames(namesIII);
        summarise("III", report);
    }

    protected List<String> format(List<String> names) {
        Set<String> namesToIgnore = new HashSet<String>();
        String lastName = "x";
        for (String name : names) {
            if (name.startsWith(lastName)) {
                namesToIgnore.add(lastName);
            }
            lastName = name;
        }
        List<String> results = new LinkedList<String>();
        lastName = "x";
        for (String name : names) {
            if (!namesToIgnore.contains(name) && !StringUtils.equals(lastName, name)) {
                results.add(name);
            }
            lastName = name;
        }
        return results;
    }

    protected static String getDelimittedPart(String line, String seperator, int zeroBasedIndex) {
        if (line == null || seperator == null || zeroBasedIndex < 0) {
            return null;
        }
        String[] tokens = line.split(seperator);
        if (tokens != null && tokens.length > zeroBasedIndex) {
            return tokens[zeroBasedIndex];
        }
        return null;
    }

    protected String buildName(String genus, String species, String subspecies) {
        if (StringUtils.isNotBlank(subspecies)) {
            return genus + " " + species + " " + subspecies;
        } else {
            return genus + " " + species;
        }
    }

    protected List<NameReport> reportOnNames(List<String> names) {
        List<NameReport> report = new LinkedList<NameReport>();
        String topName = "";
        int topCount = 0;
        for (String name : names) {
            List<TaxonConcept> concepts = taxonConceptDAO.getTaxonConcepts(name, 1);
            for (TaxonConcept concept : concepts) {
                NameReport nameReport = new NameReport(name);
                if (!concept.isAccepted()) {
                    nameReport.setSynonym(true);
                    List<RelationshipAssertion> assertions = relationshipAssertionDAO.getRelationshipAssertionsForFromConcept(concept.getId());
                    int synonymOccurrenceCount = 0;
                    for (RelationshipAssertion assertion : assertions) {
                        if (assertion.getRelationshipType() == 4) {
                            long acceptedConcept = assertion.getToConceptId();
                            synonymOccurrenceCount += occurrenceRecordDAO.countByNubId(acceptedConcept);
                        }
                    }
                    nameReport.setExpandedSynonomyOccurrenceCount(synonymOccurrenceCount);
                }
                int occurrenceCount = occurrenceRecordDAO.countByNubId(concept.getId());
                nameReport.setOccurrenceCount(occurrenceCount);
                int geoOccurrenceCount = occurrenceRecordDAO.countByNubIdGeospatial(concept.getId());
                nameReport.setGeospatialoccurrenceCount(geoOccurrenceCount);
                if (topCount < occurrenceCount) {
                    topCount = occurrenceCount;
                    topName = name;
                }
                report.add(nameReport);
            }
        }
        log.info(topName + " has the most records: " + topCount);
        return report;
    }

    protected void summarise(String type, List<NameReport> report) {
        log.info("Type: " + type);
        int occurrenceCount = 0;
        int geoOccurrenceCount = 0;
        int synonomyOccurrenceCount = 0;
        int synonomyNameCount = 0;
        for (NameReport nameReport : report) {
            occurrenceCount += nameReport.getOccurrenceCount();
            if (nameReport.isSynonym) {
                synonomyNameCount++;
                synonomyOccurrenceCount += nameReport.getExpandedSynonomyOccurrenceCount();
            }
            occurrenceCount += nameReport.getOccurrenceCount();
            geoOccurrenceCount += nameReport.getGeospatialoccurrenceCount();
        }
        log.info("  name count: " + report.size());
        log.info("  occurrence count: " + occurrenceCount);
        log.info("  geospatial occurrence count: " + geoOccurrenceCount);
        log.info("  synonymy name count: " + synonomyNameCount);
        log.info("  occurrence count of the accepted names: " + synonomyOccurrenceCount);
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        String[] cites = { "classpath*:org/gbif/portal/**/applicationContext-*.xml", "classpath*:reports/cites/applicationContext-cites.xml" };
        ApplicationContext context = new ClassPathXmlApplicationContext(cites);
        CitesReportTAB app = (CitesReportTAB) context.getBean("citesReportTAB");
        try {
            app.run();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        System.exit(0);
    }

    public TaxonNameDAO getTaxonNameDAO() {
        return taxonNameDAO;
    }

    public void setTaxonNameDAO(TaxonNameDAO taxonNameDAO) {
        this.taxonNameDAO = taxonNameDAO;
    }

    public TaxonConceptDAO getTaxonConceptDAO() {
        return taxonConceptDAO;
    }

    public void setTaxonConceptDAO(TaxonConceptDAO taxonConceptDAO) {
        this.taxonConceptDAO = taxonConceptDAO;
    }

    public OccurrenceRecordDAO getOccurrenceRecordDAO() {
        return occurrenceRecordDAO;
    }

    public void setOccurrenceRecordDAO(OccurrenceRecordDAO occurrenceRecordDAO) {
        this.occurrenceRecordDAO = occurrenceRecordDAO;
    }

    public RelationshipAssertionDAO getRelationshipAssertionDAO() {
        return relationshipAssertionDAO;
    }

    public void setRelationshipAssertionDAO(RelationshipAssertionDAO relationshipAssertionDAO) {
        this.relationshipAssertionDAO = relationshipAssertionDAO;
    }
}
