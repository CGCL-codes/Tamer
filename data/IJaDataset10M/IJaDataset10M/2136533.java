package apollo.dataadapter.chado.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.*;
import org.bdgp.util.ProgressEvent;
import apollo.dataadapter.chado.ChadoAdapter;
import apollo.dataadapter.Region;
import apollo.datamodel.CurationSet;
import apollo.datamodel.FeatureSet;
import apollo.datamodel.SequenceI;
import apollo.datamodel.SeqFeatureI;
import apollo.datamodel.StrandedFeatureSet;
import apollo.datamodel.Transcript;

/**
 * Implementation of AbstractChadoInstance for TIGR's chado databases.
 *
 * @author Jonathan Crabtree
 * @version $Revision: 1.18 $ $Date: 2007-12-06 01:40:42 $ $Author: gk_fan $
 */
public class TigrChadoInstance extends AbstractChadoInstance {

    protected static final Logger logger = LogManager.getLogger(TigrChadoInstance.class);

    public TigrChadoInstance() {
    }

    TigrChadoInstance(JdbcChadoAdapter jdbcChadoAdapter) {
        super(jdbcChadoAdapter);
    }

    public String getGeneNameField() {
        return "uniquename";
    }

    public String getTranscriptNameField() {
        return "uniquename";
    }

    public String getCdsSql(FeatureLocImplementation featLocImp) throws RelationshipCVException {
        return getCdsSql(featLocImp, false);
    }

    public String getPredictedCdsSql(FeatureLocImplementation featLocImp, ChadoProgram[] chadoPrgs) throws RelationshipCVException {
        return getCdsSql(featLocImp, true);
    }

    public String getAnalysisType(long analysisId, String program, String programversion, String sourcename) {
        return program;
    }

    /** returns target name as species + targetChadoName, unless align type is SNP,
      where query name is used for target name */
    public String getTargetName(String targetChadoName, String species, String alignType) {
        if (alignType.equals("SNP")) return null;
        if (species.equals("not known")) return targetChadoName;
        return species + " " + targetChadoName;
    }

    /** tigrs feature type */
    public String getFeatureType(String alignType, String program, String programversion, String targetSp, String sourcename, String featProp) {
        if ((alignType != null) && (alignType.equals("SNP"))) {
            return "SNP" + featProp != null ? " - " + featProp : "";
        }
        if (program.startsWith("aat_") || program.endsWith("/pasagf") || program.equals("nap")) {
            return program + " " + sourcename;
        }
        String type = program;
        if (programversion != null) type = type + " " + programversion;
        if (targetSp != null) type = type + " " + targetSp;
        return type;
    }

    /**
   * This should come from config 
   */
    public String getFeatureCVName() {
        if (super.getFeatureCVName() != null) return super.getFeatureCVName();
        return "TIGR Ontology";
    }

    public CurationSet getCurationSet(ChadoAdapter adapter, String seqType, String seqId) {
        super.clear();
        Connection conn = getConnection();
        if (conn == null) return null;
        CurationSet cset = null;
        StrandedFeatureSet results = getResultStrandedFeatSet();
        StrandedFeatureSet annotations = getAnnotStrandedFeatSet();
        cset = new CurationSet();
        long seqFeatId = getChadoAdapter().getFeatureId(conn, seqType, seqId);
        FeatureLocImplementation featLocImp = new FeatureLocImplementation(seqFeatId, haveRedundantFeatureLocs(), conn);
        setTopFeatLoc(featLocImp);
        logger.info("retrieving sequence and annotations for " + seqType + " sequence " + seqId);
        adapter.fireProgressEvent(new ProgressEvent(this, new Double(50.0), "Retrieving " + seqType + " sequence " + seqId));
        SequenceI seq = featLocImp.retrieveSequence(getChadoAdapter());
        cset.setRefSequence(seq);
        cset.setName(seq.getName());
        cset.setChromosome(seq.getName());
        adapter.fireProgressEvent(new ProgressEvent(this, new Double(100.0), "Retrieving " + seqType + " sequence " + seqId));
        cset.setStart(1);
        cset.setEnd(seq.getLength());
        logger.info("retrieved " + seq.getLength() + " bp for " + seqId);
        Double zero = new Double(0.0);
        Double hero = new Double(100.0);
        ChadoProgram[] oneLevels = getOneLevelResultPrograms();
        String m1 = "Retrieving 1-level results";
        logger.debug("retrieving 1-level results");
        adapter.fireProgressEvent(new ProgressEvent(this, zero, m1));
        getChadoAdapter().addOneLevelResults();
        adapter.fireProgressEvent(new ProgressEvent(this, hero, m1));
        logger.debug("done retrieving 1-level results");
        logger.debug("retrieving 1-level annotations");
        boolean getFeatProps = true;
        boolean getSynonyms = true;
        boolean getDbXRefs = true;
        getAnnotations(conn, seq, annotations, featLocImp, getFeatProps, getSynonyms, getDbXRefs, adapter);
        logger.debug("done retrieving 1-level annotations");
        logger.debug("retrieving gene models");
        adapter.fireProgressEvent(new ProgressEvent(this, zero, "Retrieving gene predictions"));
        ChadoProgram[] predictionPrograms = getGenePredictionPrograms();
        getChadoAdapter().addGenePredictionResults(conn, predictionPrograms, results, featLocImp, seq);
        adapter.fireProgressEvent(new ProgressEvent(this, hero, "Retrieving gene predictions"));
        logger.debug("done retrieving gene models");
        if (getCopyGeneModelsIntoResultTier()) {
            String ctname = "Copy_of_Annotation";
            logger.debug("copying gene models into '" + ctname + "' tier");
            getChadoAdapter().copyAnnotatedGenesIntoResultTier(annotations, results, ctname, seq);
            logger.debug("done copying gene models into '" + ctname + "' tier");
        }
        String primaryScoreColumn = getChadoAdapter().getAnalysisFeatureIdentityField();
        boolean getTgtSeqSep = false;
        boolean getAlignSeqs = false;
        boolean setTargetSeqs = true;
        boolean getTargetSeqDescriptions = true;
        boolean joinWithFeatureProp = false;
        ChadoProgram[] hitProgs = getSearchHitPrograms();
        String progNameList = concatProgramNames(hitProgs);
        String m = "Retrieving " + progNameList + " results";
        logger.debug("retrieving search hits for: " + progNameList);
        ProgressEvent p = new ProgressEvent(this, zero, m);
        adapter.fireProgressEvent(p);
        boolean makeSeqsLazy = true;
        getChadoAdapter().addSearchHits(conn, hitProgs, setTargetSeqs, getTgtSeqSep, makeSeqsLazy, getTargetSeqDescriptions, joinWithFeatureProp, seq, results, primaryScoreColumn, getAlignSeqs, featLocImp);
        adapter.fireProgressEvent(new ProgressEvent(this, hero, "Retrieved " + progNameList + " results"));
        logger.debug("done retrieving search hits for: " + progNameList);
        logger.info(seqId + " loaded");
        cset.setAnnots(annotations);
        cset.setResults(results);
        try {
            conn.close();
        } catch (SQLException sqle) {
            logger.error("failed to close JDBC Connection", sqle);
        }
        return cset;
    }

    /** Explicitly set translation start and stop from cds */
    public void setTranslationStartAndStop(Transcript trans, ChadoCds cds) {
        boolean calculateEnd = false;
        boolean retval = trans.setTranslationStart(cds.getStart(), calculateEnd);
        if (!retval) {
            logger.error("setTranslationStart(" + cds.getStart() + ", false) failed for " + trans);
        }
        trans.setTranslationEnd(cds.getTranslationEnd());
    }

    /** Explicitly set translation start and stop from cds */
    public void setTranslationStartAndStop(FeatureSet trans, ChadoCds cds) {
        boolean calculateEnd = false;
        boolean retval = trans.setTranslationStart(cds.getStart(), calculateEnd);
        if (!retval) {
            logger.error("setTranslationStart(" + cds.getStart() + ", false) failed for " + trans);
        }
        trans.setTranslationEnd(cds.getTranslationEnd());
    }

    public CurationSet getCurationSetInRange(ChadoAdapter adapter, String seqType, Region region) {
        logger.error("getCurationSetInRange() called with seqType=" + seqType);
        throw new UnsupportedOperationException("TigrChadoInstance.getCurationSetInRange()");
    }

    private SchemaVersion getChadoVersion() {
        return getChadoAdapter().getChadoVersion();
    }

    private boolean haveRedundantFeatureLocs() {
        return true;
    }

    private String concatProgramNames(ChadoProgram[] names) {
        String msg = "";
        if (names == null) return msg;
        int size = names.length;
        for (int i = 0; i < size; i++) {
            msg += names[i].getProgram();
            if (i < size - 2) msg += ", "; else if (i < size - 1) msg += " and ";
        }
        return msg;
    }

    private String getCdsSql(FeatureLocImplementation featLocImp, boolean isAnalysis) throws RelationshipCVException {
        String fminCol = getChadoVersion().getFMinCol();
        String fmaxCol = getChadoVersion().getFMaxCol();
        String subjFeatCol = getChadoVersion().getSubjFeatCol();
        String objFeatCol = getChadoVersion().getObjFeatCol();
        Long producedByCvId = getProducedByCVTermId();
        return "SELECT cds.uniquename AS cds_name, " + "       trans.uniquename AS transcript_uniquename, " + "       trans.residues AS transcript_seq," + "       cdsloc." + fminCol + " AS fmin, " + "       cdsloc." + fmaxCol + " AS fmax, " + "       cdsloc.is_fmin_partial, " + "       cdsloc.is_fmax_partial, " + "       cdsloc.strand, " + "       prot.uniquename AS protein_name, " + "       prot.residues AS protein_seq " + "FROM featureloc cdsloc, feature cds, feature_relationship cds2trans, " + "     feature trans, feature_relationship cds2prot, feature prot " + "WHERE cdsloc.srcfeature_id = " + featLocImp.getContainingFeatureId() + " " + "AND cdsloc.feature_id = cds.feature_id " + "AND cds.type_id = " + getFeatureCVTermId("CDS") + " " + "AND cds.is_analysis = " + (isAnalysis ? "1" : "0") + " " + "AND cds.feature_id = cds2trans." + subjFeatCol + " " + "AND cds2trans." + objFeatCol + " = trans.feature_id " + "AND cds2trans.type_id = " + producedByCvId + " " + "AND trans.type_id = " + getFeatureCVTermId("transcript") + " " + "AND cds.feature_id = cds2prot." + objFeatCol + " " + "AND cds2prot.type_id = " + producedByCvId + " " + "AND cds2prot." + subjFeatCol + " = prot.feature_id " + "AND prot.type_id = " + getPolypeptideCVTermId();
    }
}
