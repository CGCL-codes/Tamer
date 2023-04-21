package uk.ac.ebi.pride.chart.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.pride.chart.graphics.implementation.PrideChart;
import uk.ac.ebi.pride.chart.graphics.implementation.PrideChartFactory;
import uk.ac.ebi.pride.chart.model.implementation.*;
import uk.ac.ebi.pride.chart.utils.StringUtils;
import uk.ac.ebi.pride.engine.SearchEngineType;
import uk.ac.ebi.pride.term.CvTermReference;
import uk.ac.ebi.pride.util.NumberUtilities;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;

/**
 * <p> Database Access Controller. </p>
 *
 * @author Antonio Fabregat
 *         Date: 05-jul-2010
 *         Time: 12:30:35
 */
public class DBAccessController {

    /**
     * Database connection object
     */
    private Connection DBConnection = null;

    private static final Logger logger = LoggerFactory.getLogger(DBAccessController.class);

    /**
     * <p> Creates an instance of this DBAccessController object using an existing database connection object</p>
     *
     * @param DBConnection Database connection object
     */
    public DBAccessController(Connection DBConnection) {
        this.DBConnection = DBConnection;
    }

    public DBAccessController() {
        this("");
    }

    /**
     * <p> Creates an instance of this DBAccessController object</p>
     */
    public DBAccessController(String dbAlias) {
        Properties properties = new Properties();
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("database.properties");
        try {
            properties.load(is);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
        }
        dbAlias = dbAlias.equals("") ? properties.getProperty("alias") : dbAlias;
        String url_connection = properties.getProperty("protocol") + ':' + properties.getProperty("subprotocol") + ':' + dbAlias;
        logger.debug("Connecting to " + url_connection);
        try {
            DBConnection = DriverManager.getConnection(url_connection, properties.getProperty("user"), properties.getProperty("password"));
        } catch (SQLException err) {
            logger.error(err.getMessage(), err);
        }
    }

    public List<PrecursorData> precursorMassData(String accessionNumber) {
        List<PrecursorData> precursorDataList = new ArrayList<PrecursorData>();
        try {
            String query = "select exp.accession, spec.spectrum_id, param.value " + "from pride_experiment exp, mzdata_mz_data mz, mzdata_spectrum spec, " + "     mzdata_precursor prec, mzdata_ion_selection_param param " + "where mz.accession_number = ? and " + "      exp.accession = mz.accession_number and " + "      mz.mz_data_id = spec.mz_data_id and " + "      spec.spectrum_id = prec.spectrum_id and " + "      prec.precursor_id = param.parent_element_fk and " + "      spec.ms_level = 2 and " + "      (param.accession = 'MS:1000744' or param.accession = 'PSI:1000040')";
            PreparedStatement st = DBConnection.prepareStatement(query);
            st.setString(1, accessionNumber);
            ResultSet rs = st.executeQuery();
            boolean is_empty = true;
            while (rs.next()) {
                is_empty = false;
                try {
                    precursorDataList.add(new PrecursorData(rs.getString("accession"), rs.getString("spectrum_id"), rs.getString("value")));
                } catch (PrecursorDataException e) {
                    logger.error(e.getMessage(), e);
                }
            }
            rs.close();
            if (is_empty) {
                logger.debug("No Precursor Mass Delta values for " + accessionNumber);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return precursorDataList;
    }

    public List<PrecursorData> precursorChargeData(String accessionNumber) {
        List<PrecursorData> precursorDataList = new ArrayList<PrecursorData>();
        try {
            PreparedStatement st = DBConnection.prepareStatement("select exp.accession, spec.spectrum_id, param.value " + "from pride_experiment exp, mzdata_mz_data mz, mzdata_spectrum spec, " + "     mzdata_precursor prec, mzdata_ion_selection_param param " + "where mz.accession_number = ? and " + "      exp.accession = mz.accession_number and " + "      mz.mz_data_id = spec.mz_data_id and " + "      spec.spectrum_id = prec.spectrum_id and " + "      prec.precursor_id = param.parent_element_fk and " + "      spec.ms_level = 2 and " + "      (param.accession = 'MS:1000041' or param.accession = 'PSI:1000041') ");
            st.setString(1, accessionNumber);
            ResultSet rs = st.executeQuery();
            boolean is_empty = true;
            while (rs.next()) {
                is_empty = false;
                try {
                    precursorDataList.add(new PrecursorData(rs.getString("accession"), rs.getString("spectrum_id"), rs.getString("value")));
                } catch (PrecursorDataException e) {
                }
            }
            rs.close();
            if (is_empty) {
                logger.debug("No Precursor Charge Data for " + accessionNumber);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return precursorDataList;
    }

    public Map<String, Boolean> precursorSpectrumIdentified(String accessionNumber) {
        Map<String, Boolean> spectrumReferences = new HashMap<String, Boolean>();
        Set<String> identifiedSet = getIdentifiedPeptidesReferences(accessionNumber);
        try {
            PreparedStatement st = DBConnection.prepareStatement("select spec.spectrum_id, spec.spectrum_identifier " + "from pride_experiment exp, mzdata_mz_data mz, mzdata_spectrum spec " + "where exp.accession = ? and " + "      exp.accession = mz.accession_number and " + "      mz.mz_data_id = spec.mz_data_id");
            st.setString(1, accessionNumber);
            ResultSet rs = st.executeQuery();
            boolean is_empty = true;
            while (rs.next()) {
                is_empty = false;
                String accession = rs.getString("spectrum_id");
                String identifier = rs.getString("spectrum_identifier");
                boolean identified = identifiedSet.contains(identifier);
                spectrumReferences.put(accession, identified);
            }
            rs.close();
            if (is_empty) {
                logger.debug("No precursor SpectrumIdentified data for " + accessionNumber);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return spectrumReferences;
    }

    private Set<String> getIdentifiedPeptidesReferences(String accessionNumber) {
        Set<String> set = new HashSet<String>();
        try {
            PreparedStatement st = DBConnection.prepareStatement("select pep.spectrum_ref " + "from pride_experiment exp, pride_identification ide, pride_peptide pep " + "where exp.accession = ? and " + "      exp.experiment_id = ide.experiment_id and " + "      ide.identification_id = pep.identification_id");
            st.setString(1, accessionNumber);
            ResultSet rs = st.executeQuery();
            boolean is_empty = true;
            while (rs.next()) {
                is_empty = false;
                String spectrum_ref = rs.getString("spectrum_ref");
                set.add(spectrum_ref);
            }
            rs.close();
            if (is_empty) {
                logger.debug("No Identified Peptide references for " + accessionNumber);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return set;
    }

    public PeakListBasicInfo peakListMzInfo(String accessionNumber) {
        PeakListBasicInfo peakListBasicInfo = null;
        try {
            PreparedStatement st = DBConnection.prepareStatement("select s.spectrum_id, c.data_endian, a.binary_array_id, c.data_precision " + "from mzdata_mz_data m, mzdata_spectrum s, mzdata_binary_array c, mzdata_base_64_data a " + "where m.mz_data_id=s.mz_data_id and " + "   c.binary_array_id=s.mz_array_binary_id and " + "   c.binary_array_id=a.binary_array_id and " + "   m.accession_number = ? and " + "   s.ms_level= 2 " + "group by a.binary_array_id " + "order by s.spectrum_id ");
            st.setString(1, accessionNumber);
            ResultSet rs = st.executeQuery();
            Map<Integer, Integer> mzDataList = new HashMap<Integer, Integer>();
            String de = null;
            String dp = null;
            boolean is_empty = true;
            while (rs.next()) {
                is_empty = false;
                mzDataList.put(rs.getInt("spectrum_id"), rs.getInt("binary_array_id"));
                if (de == null) {
                    de = rs.getString("data_endian");
                    dp = rs.getString("data_precision");
                }
            }
            if (de != null) peakListBasicInfo = new PeakListBasicInfo(mzDataList, de, dp);
            rs.close();
            if (is_empty) {
                logger.debug("No peak list info for " + accessionNumber);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return peakListBasicInfo;
    }

    public PeakListBasicInfo peakListIntensityInfo(String accessionNumber) {
        PeakListBasicInfo peakListBasicInfo = null;
        try {
            PreparedStatement st = DBConnection.prepareStatement("select s.spectrum_id, c.data_endian, a.binary_array_id, c. data_precision " + "from mzdata_mz_data m, mzdata_spectrum s, mzdata_binary_array c, mzdata_base_64_data a " + "where m.mz_data_id=s.mz_data_id and " + "   c.binary_array_id=s.inten_array_binary_id and " + "   c.binary_array_id=a.binary_array_id and " + "   m.accession_number = ? and " + "   s.ms_level= 2 " + "group by a.binary_array_id " + "order by s.spectrum_id ");
            st.setString(1, accessionNumber);
            ResultSet rs = st.executeQuery();
            Map<Integer, Integer> intensityDataList = new HashMap<Integer, Integer>();
            String de = null;
            String dp = null;
            boolean is_empty = true;
            while (rs.next()) {
                is_empty = false;
                intensityDataList.put(rs.getInt("spectrum_id"), rs.getInt("binary_array_id"));
                if (de == null) {
                    de = rs.getString("data_endian");
                    dp = rs.getString("data_precision");
                }
            }
            if (de != null) peakListBasicInfo = new PeakListBasicInfo(intensityDataList, de, dp);
            rs.close();
            if (is_empty) {
                logger.debug("No peak list intesity data for " + accessionNumber);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return peakListBasicInfo;
    }

    /**
     * Returns the binary chartData for each one of the array_binary_id passed in the parameter
     *
     * @param array_binary_ids a list of array_binary_id to get the associated chunks
     * @return the chunks for each one of the array_binary_id passed in the parameter
     */
    public Map<Integer, String> getBinaryData(List<Integer> array_binary_ids) {
        if (array_binary_ids.size() == 0) return null;
        Map<Integer, String> dataMap = new HashMap<Integer, String>();
        String arrayBinaryIDs = StringUtils.implode(array_binary_ids, ",");
        try {
            PreparedStatement st = DBConnection.prepareStatement("SELECT binary_array_id, base_64_data " + "FROM mzdata_base_64_data " + "WHERE binary_array_id IN (" + arrayBinaryIDs + ")");
            ResultSet rs = st.executeQuery();
            boolean is_empty = true;
            while (rs.next()) {
                is_empty = false;
                dataMap.put(rs.getInt("binary_array_id"), rs.getString("base_64_data"));
            }
            rs.close();
            if (is_empty) {
                logger.debug("No Binary Data for " + arrayBinaryIDs);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return dataMap;
    }

    public List<ProteinPeptide> getProteinsIdentification(String accessionNumber) {
        List<ProteinPeptide> proteinsPeptides = new ArrayList<ProteinPeptide>();
        Map<Integer, PeptideScore> peptideScores = getPeptideScore(accessionNumber);
        try {
            PreparedStatement st = DBConnection.prepareStatement("select ide.identification_id protein_ID, pep.peptide_id, pep.sequence, pep.spectrum_ref " + "from pride_identification ide, pride_peptide pep , pride_experiment exp " + "where exp.accession = ? AND " + "   ide.experiment_id = exp.experiment_id AND " + "   ide.identification_id = pep.identification_id ");
            st.setString(1, accessionNumber);
            ResultSet rs = st.executeQuery();
            List<Integer> spectrumRefs = new ArrayList<Integer>();
            List<Integer> peptideIDs = new ArrayList<Integer>();
            boolean is_empty = true;
            while (rs.next()) {
                is_empty = false;
                int peptideID = rs.getInt("peptide_ID");
                int spectrumRef = rs.getInt("spectrum_ref");
                spectrumRefs.add(spectrumRef);
                peptideIDs.add(peptideID);
                DBProteinPeptide dbpp = new DBProteinPeptide(rs.getInt("protein_ID"), peptideID, rs.getString("sequence"), spectrumRef);
                if (peptideScores.containsKey(peptideID)) dbpp.setPeptideScore(peptideScores.get(peptideID));
                proteinsPeptides.add(dbpp);
            }
            rs.close();
            if (is_empty) {
                logger.debug("No Protein Identification data for " + accessionNumber);
            }
            if (spectrumRefs.size() > 0 && peptideIDs.size() > 0) {
                Map<Integer, Integer> spectrumsIDByRefs = getSpectrumsIDByRefs(spectrumRefs, accessionNumber);
                Map<Integer, Double> monoMassMod = getPeptidesMonoMassModification(peptideIDs);
                for (ProteinPeptide pp : proteinsPeptides) {
                    DBProteinPeptide dbPP = (DBProteinPeptide) pp;
                    int spectrumRef = dbPP.getSpectrumRef();
                    if (spectrumsIDByRefs.keySet().contains(spectrumRef)) dbPP.setSpectrumID(spectrumsIDByRefs.get(spectrumRef));
                    int peptideID = dbPP.getPeptideID();
                    if (monoMassMod.keySet().contains(peptideID)) dbPP.setPtmMass(monoMassMod.get(peptideID));
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return proteinsPeptides;
    }

    private Map<Integer, Integer> getSpectrumsIDByRefs(List<Integer> spectrum_refs, String accessionNumber) {
        Map<Integer, Integer> spectrumsRefs = new HashMap<Integer, Integer>();
        String spectrumRefs = StringUtils.implode(spectrum_refs, ",");
        try {
            PreparedStatement st = DBConnection.prepareStatement("select ms.spectrum_identifier, ms.spectrum_id " + "from mzdata_spectrum ms, mzdata_mz_data mz " + "where mz.accession_number = ? and " + "   mz.mz_data_id = ms.mz_data_id and " + "   ms.spectrum_identifier in (" + spectrumRefs + ")");
            st.setString(1, accessionNumber);
            ResultSet rs = st.executeQuery();
            boolean is_empty = true;
            while (rs.next()) {
                is_empty = false;
                int spectrum_ref = rs.getInt("ms.spectrum_identifier");
                int spectrum_id = rs.getInt("ms.spectrum_id");
                spectrumsRefs.put(spectrum_ref, spectrum_id);
            }
            rs.close();
            if (is_empty) {
                logger.debug("No Spectrum references for " + accessionNumber);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return spectrumsRefs;
    }

    private Map<Integer, Double> getPeptidesMonoMassModification(List<Integer> peptides_id) {
        Map<Integer, Double> modificationsMass = new HashMap<Integer, Double>();
        String peptidesIDs = StringUtils.implode(peptides_id, ",");
        try {
            PreparedStatement st = DBConnection.prepareStatement("select m.peptide_id, d.mass_delta_value " + "from pride_modification m, pride_mass_delta d " + "where m.peptide_id in (" + peptidesIDs + ") and " + "   m.modification_id = d.modification_id and " + "   d.classname = ?");
            st.setString(1, "uk.ac.ebi.pride.rdbms.ojb.model.core.MonoMassDeltaBean");
            ResultSet rs = st.executeQuery();
            boolean is_empty = true;
            while (rs.next()) {
                is_empty = false;
                int peptideID = rs.getInt("peptide_id");
                double massDelta = rs.getDouble("mass_delta_value");
                if (modificationsMass.keySet().contains(peptideID)) {
                    double value = modificationsMass.get(peptideID);
                    modificationsMass.put(peptideID, value + massDelta);
                } else {
                    modificationsMass.put(peptideID, massDelta);
                }
            }
            rs.close();
            if (is_empty) {
                logger.debug("No Peptide Mass delta data for " + peptidesIDs);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return modificationsMass;
    }

    private Map<Integer, PeptideScore> getPeptideScore(String accessionNumber) {
        Map<Integer, PeptideScore> map = new HashMap<Integer, PeptideScore>();
        try {
            PreparedStatement st = DBConnection.prepareStatement("SELECT peptide_id, ppp.accession, value " + "FROM pride_peptide_param as ppp join pride_peptide on(parent_element_fk=peptide_id)" + "     join pride_identification using(identification_id) " + "     join pride_experiment using(experiment_id) " + "WHERE pride_experiment.accession=?");
            st.setString(1, accessionNumber);
            ResultSet rs = st.executeQuery();
            boolean is_empty = true;
            while (rs.next()) {
                is_empty = false;
                Integer peptide_id = rs.getInt("peptide_id");
                String accession = rs.getString("accession");
                Number value;
                try {
                    String sValue = rs.getString("value");
                    if (!NumberUtilities.isNumber(sValue)) continue;
                    value = Double.valueOf(sValue);
                } catch (NumberFormatException e) {
                    logger.error(e.getMessage(), e);
                    continue;
                }
                PeptideScore peptideScore;
                if (map.containsKey(peptide_id)) {
                    peptideScore = map.get(peptide_id);
                } else {
                    peptideScore = new PeptideScore();
                    map.put(peptide_id, peptideScore);
                }
                CvTermReference ref = CvTermReference.getCvRefByAccession(accession);
                SearchEngineType se = SearchEngineType.getByCvTermReference(ref);
                if (se != null) peptideScore.addPeptideScore(se, ref, value);
            }
            rs.close();
            if (is_empty) {
                logger.debug("No peptide score data for " + accessionNumber);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return map;
    }

    public int getExperimentID(String accessionNumber) {
        int experimentID = -1;
        try {
            PreparedStatement st = DBConnection.prepareStatement("select experiment_id from pride_experiment where accession = ?");
            st.setString(1, accessionNumber);
            ResultSet rs = st.executeQuery();
            if (rs.next()) experimentID = rs.getInt("experiment_id");
            rs.close();
            if (experimentID == -1) {
                logger.debug("No experimentID for " + accessionNumber);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return experimentID;
    }

    public boolean storeChartData(int experimentID, int type, String data) {
        boolean retValue = false;
        try {
            PreparedStatement st = DBConnection.prepareStatement("insert into pride_chart_data(experiment_id, chart_type, intermediate_data)" + "values (?, ?, ?)" + "on duplicate key update intermediate_data = ?");
            st.setInt(1, experimentID);
            st.setInt(2, type);
            st.setString(3, data);
            st.setString(4, data);
            int res = st.executeUpdate();
            st.close();
            if (res == 1) {
                retValue = true;
            } else {
                logger.debug("No chart data stored for " + experimentID);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return retValue;
    }

    public List<PrideChart> getChartData(int experimentID) {
        List<PrideChart> list = new ArrayList<PrideChart>();
        try {
            PreparedStatement st = DBConnection.prepareStatement("select chart_type, intermediate_data from pride_chart_data where experiment_id = ? and intermediate_data is not null order by chart_type");
            st.setInt(1, experimentID);
            ResultSet rs = st.executeQuery();
            boolean is_empty = true;
            while (rs.next()) {
                is_empty = false;
                int type = rs.getInt("chart_type");
                String jsonData = rs.getString("intermediate_data");
                PrideChart prideChart = PrideChartFactory.getChart(type, jsonData);
                list.add(prideChart);
            }
            rs.close();
            if (is_empty) {
                logger.debug("No intermediate data for " + experimentID);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return list;
    }

    public List<String> getAllExperimentsAccessionNumber() {
        List<String> experimentAccessionNumbers = new ArrayList<String>();
        try {
            PreparedStatement st = DBConnection.prepareStatement("select accession from pride_experiment");
            ResultSet rs = st.executeQuery();
            boolean is_empty = true;
            while (rs.next()) {
                is_empty = false;
                experimentAccessionNumbers.add(rs.getString("accession"));
            }
            rs.close();
            if (is_empty) {
                logger.debug("No Accession data in Pride!?!?");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        Collections.sort(experimentAccessionNumbers, new Comparator<String>() {

            @Override
            public int compare(String o, String o1) {
                int i = Integer.valueOf(o);
                int i1 = Integer.valueOf(o1);
                return i - i1;
            }
        });
        return experimentAccessionNumbers;
    }
}
