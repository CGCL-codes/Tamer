package org.tigr.cloe.model.facade.datastoreFacade.dao.tdbDao;

import java.io.Reader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.sql.Date;
import java.util.List;
import org.tigr.cloe.model.facade.datastoreFacade.dao.AssemblyDAO;
import org.tigr.cloe.model.facade.datastoreFacade.dao.DAOException;
import org.tigr.common.Feature;
import org.tigr.seq.seqdata.INonUniqueUnitigFeature;
import org.tigr.seq.tdb.TDBConnection;
import org.tigr.seq.tdb.TDBUtil;
import org.tigr.seq.tdb.seqdata.TDBSequence;
import org.tigr.seq.util.CancelHelper;

public class TDBAssemblyDAO implements AssemblyDAO {

    private QueryFilterBehavior queryFilter;

    public TDBAssemblyDAO(QueryFilterBehavior queryFilter) {
        this.queryFilter = queryFilter;
    }

    public List<Integer> getSequenceIDsContainedInAssembly(int assemblyID, String project, Integer startConstraint, Integer endConstraint) throws DAOException {
        int start = (startConstraint == null ? Integer.MIN_VALUE : startConstraint.intValue());
        int end = (endConstraint == null ? Integer.MAX_VALUE : endConstraint.intValue());
        List<Integer> seqIds = fetchLinearSequenceIDs(assemblyID, project, start, end);
        if (this.isCircular(assemblyID, project) && endConstraint != null) {
            seqIds.addAll(this.fetchAssemblyOverhangSequenceIDs(assemblyID, project, end));
        }
        return seqIds;
    }

    /**
     * @param assemblyID
     * @param project
     * @param start
     * @param end
     * @return
     * @throws SQLException
     * @throws TdbDaoException
     */
    private List<Integer> fetchLinearSequenceIDs(int assemblyID, String project, int start, int end) throws TdbDaoException {
        try {
            PreparedStatement preparedStatement = TDBUtil.getPreparedStatement(TDBConnection.getInstance(), queryFilter.filterQuery(AssemblyQueries.QUERY_SEQUENCE_IDS_IN_ASSEMBLY), project);
            preparedStatement.setInt(1, assemblyID);
            preparedStatement.setInt(2, TDBSequence.MAX_READ_LENGTH);
            preparedStatement.setInt(3, start);
            preparedStatement.setInt(4, end);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs == null) {
                preparedStatement.close();
                throw new TdbDaoException("Error null resultSet when trying to query seq names in assembly " + assemblyID + " from Datastore ");
            }
            List<Integer> seqNamesList = new ArrayList<Integer>();
            while (rs.next()) {
                if (CancelHelper.isCancelled()) {
                    throw new TdbDaoException("User Cancelled operation");
                }
                seqNamesList.add(rs.getInt(1));
            }
            rs.close();
            preparedStatement.close();
            return seqNamesList;
        } catch (SQLException e) {
            throw new TdbDaoException("Error trying to query seq names in assembly " + assemblyID + " from Datastore ", e);
        }
    }

    private List<Integer> fetchAssemblyOverhangSequenceIDs(int assemblyID, String project, int end) throws TdbDaoException {
        try {
            PreparedStatement preparedStatement = TDBUtil.getPreparedStatement(TDBConnection.getInstance(), queryFilter.filterQuery(AssemblyQueries.QUERY_OVERHANG_SEQUENCE_IDS_IN_ASSEMBLY), project);
            preparedStatement.setInt(1, assemblyID);
            preparedStatement.setInt(2, end);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs == null) {
                preparedStatement.close();
                throw new TdbDaoException("Error null resultSet when trying to query overhang seq names in assembly " + assemblyID + " from Datastore ");
            }
            List<Integer> seqNamesList = new ArrayList<Integer>();
            while (rs.next()) {
                if (CancelHelper.isCancelled()) {
                    throw new TdbDaoException("User Cancelled operation");
                }
                seqNamesList.add(rs.getInt(1));
            }
            rs.close();
            preparedStatement.close();
            return seqNamesList;
        } catch (SQLException e) {
            throw new TdbDaoException("Error trying to query overhang seq names in assembly " + assemblyID + " from Datastore ", e);
        }
    }

    public Date getAssemblyModDate(int assemblyID, String project) throws DAOException {
        PreparedStatement statement;
        try {
            statement = TDBUtil.getPreparedStatement(TDBConnection.getInstance(), queryFilter.filterQuery(AssemblyQueries.QUERY_MOD_DATE), project);
            statement.setInt(1, assemblyID);
            statement.executeQuery();
            ResultSet rs = statement.getResultSet();
            if (rs == null) {
                statement.close();
                throw new TdbDaoException("Error null result set when trying to query assembly mod_date for assembly " + assemblyID);
            }
            if (rs.next()) {
                Date modDate = rs.getDate(1);
                if (rs.wasNull()) {
                    rs.close();
                    statement.close();
                    throw new TdbDaoException("Error mod_date for for assembly " + assemblyID + "  was null");
                }
                rs.close();
                statement.close();
                return modDate;
            }
            rs.close();
            statement.close();
            throw new TdbDaoException("Error no result when trying to query assembly mod_date for assembly " + assemblyID);
        } catch (SQLException e) {
            throw new TdbDaoException("Error trying to query assembly mod_date for assembly " + assemblyID, e);
        }
    }

    public int getWholeGappedSize(int assemblyID, String project) throws DAOException {
        PreparedStatement statement;
        try {
            statement = TDBUtil.getPreparedStatement(TDBConnection.getInstance(), queryFilter.filterQuery(AssemblyQueries.QUERY_WHOLE_GAPPED_SIZE), project);
            statement.setInt(1, assemblyID);
            statement.executeQuery();
            ResultSet rs = statement.getResultSet();
            if (rs == null) {
                statement.close();
                throw new TdbDaoException("Error null result set when trying to query assembly whole gapped size for assembly " + assemblyID);
            }
            if (rs.next()) {
                int gappedSize = rs.getInt(1);
                if (rs.wasNull()) {
                    rs.close();
                    statement.close();
                    throw new TdbDaoException("Error gappedSize for for assembly " + assemblyID + "  was null");
                }
                rs.close();
                statement.close();
                return gappedSize;
            }
            rs.close();
            statement.close();
            throw new TdbDaoException("Error no result when trying to query assembly whole gapped size for assembly " + assemblyID);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new TdbDaoException("Error trying to query assembly whole gapped size for assembly " + assemblyID, e);
        }
    }

    public int getWholeUngappedSize(int assemblyID, String project) throws DAOException {
        PreparedStatement statement;
        try {
            statement = TDBUtil.getPreparedStatement(TDBConnection.getInstance(), queryFilter.filterQuery(AssemblyQueries.QUERY_WHOLE_UNGAPPED_SIZE), project);
            statement.setInt(1, assemblyID);
            statement.executeQuery();
            ResultSet rs = statement.getResultSet();
            if (rs == null) {
                statement.close();
                throw new TdbDaoException("Error null result set when trying to query assembly whole ungapped size for assembly " + assemblyID);
            }
            if (rs.next()) {
                int ungappedSize = rs.getInt(1);
                if (rs.wasNull()) {
                    rs.close();
                    statement.close();
                    throw new TdbDaoException("Error ungappedSize for for assembly " + assemblyID + "  was null");
                }
                rs.close();
                statement.close();
                return ungappedSize;
            }
            rs.close();
            statement.close();
            throw new TdbDaoException("Error no result when trying to query assembly whole ungapped size for assembly " + assemblyID);
        } catch (SQLException e) {
            throw new TdbDaoException("Error trying to query assembly whole ungapped size for assembly " + assemblyID, e);
        }
    }

    public int getNumberOfSequencesInAssembly(int assemblyID, String project) throws DAOException {
        PreparedStatement statement;
        try {
            statement = TDBUtil.getPreparedStatement(TDBConnection.getInstance(), queryFilter.filterQuery(AssemblyQueries.QUERY_SEQ_NUM), project);
            statement.setInt(1, assemblyID);
            statement.executeQuery();
            ResultSet rs = statement.getResultSet();
            if (rs == null) {
                statement.close();
                throw new TdbDaoException("Error null result set when trying to query number of sequences in assembly " + assemblyID);
            }
            if (rs.next()) {
                int seqNum = rs.getInt(1);
                if (rs.wasNull()) {
                    rs.close();
                    statement.close();
                    throw new TdbDaoException("Error seq# for  assembly " + assemblyID + "  was null");
                }
                rs.close();
                statement.close();
                return seqNum;
            }
            rs.close();
            statement.close();
            throw new TdbDaoException("Error no result when trying to query assembly seq# for assembly " + assemblyID);
        } catch (SQLException e) {
            throw new TdbDaoException("Error trying to query assembly seq# for assembly " + assemblyID, e);
        }
    }

    public boolean isAssemblyCurrent(int assemblyID, String project) throws DAOException {
        boolean ret = false;
        PreparedStatement statement;
        try {
            statement = TDBUtil.getPreparedStatement(TDBConnection.getInstance(), queryFilter.filterQuery(AssemblyQueries.QUERY_ASSEMBLY_IS_CURRENT), project);
            statement.setInt(1, assemblyID);
            statement.executeQuery();
            ResultSet rs = statement.getResultSet();
            if (rs == null) {
                statement.close();
                throw new TdbDaoException("Error null result set returned when querying if assembly is current");
            }
            if (!rs.next()) {
                ret = false;
            } else {
                ret = true;
            }
            rs.close();
            statement.close();
            return ret;
        } catch (SQLException e) {
            throw new TdbDaoException("Error when querying if assembly is current", e);
        }
    }

    public boolean assemblyExists(int assemblyID, String project) throws TdbDaoException {
        boolean ret = false;
        PreparedStatement statement;
        try {
            statement = TDBUtil.getPreparedStatement(TDBConnection.getInstance(), queryFilter.filterQuery(AssemblyQueries.QUERY_ASSEMBLY_EXISTS), project);
            statement.setInt(1, assemblyID);
            statement.executeQuery();
            ResultSet rs = statement.getResultSet();
            if (rs == null) {
                statement.close();
                throw new TdbDaoException("Error null result set returned when querying if assembly exists");
            }
            if (!rs.next()) {
                ret = false;
            } else {
                ret = true;
            }
            rs.close();
            statement.close();
            return ret;
        } catch (SQLException e) {
            throw new TdbDaoException("Error when querying if assembly exists", e);
        }
    }

    public boolean isCircular(int assemblyID, String project) throws TdbDaoException {
        PreparedStatement statement = null;
        boolean ret = false;
        ResultSet rs = null;
        try {
            statement = TDBUtil.getPreparedStatement(TDBConnection.getInstance(), queryFilter.filterQuery(AssemblyQueries.QUERY_ASSEMBLY_IS_CIRCULAR), project);
            statement.setInt(1, assemblyID);
            statement.executeQuery();
            rs = statement.getResultSet();
            if (rs == null) {
                throw new TdbDaoException("Error null result set returned when querying if assembly is circular");
            }
            if (!rs.next()) {
                throw new TdbDaoException("Error empty result set returned when querying if assembly is circular");
            }
            ret = rs.getBoolean(1);
            if (rs.wasNull()) {
                throw new TdbDaoException("Error null value returned when querying if assembly is circular");
            }
            return ret;
        } catch (SQLException e) {
            throw new TdbDaoException("Error when querying if assembly is circular", e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException ignored) {
            }
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ignored) {
            }
        }
    }

    public String getGappedConsensusData(int assemblyID, String project, int start, int length) throws DAOException {
        ResultSet rs = null;
        PreparedStatement statement = null;
        try {
            statement = TDBUtil.getPreparedStatement(TDBConnection.getInstance(), queryFilter.filterQuery(AssemblyQueries.QUERY_GAPPED_CONSENSUS), project);
            statement.setInt(1, assemblyID);
            statement.executeQuery();
            rs = statement.getResultSet();
            if (rs == null) {
                throw new TdbDaoException("Error null result set when querying assembly lsequence");
            }
            if (!rs.next()) {
                throw new TdbDaoException("Error no result set when querying  assembly lsequence");
            }
            Reader reader = rs.getCharacterStream(1);
            char[] lsequenceArray = new char[length];
            reader.skip(start);
            int numRead = reader.read(lsequenceArray, 0, length);
            reader.close();
            if (numRead < length) {
                Arrays.fill(lsequenceArray, numRead, length, '-');
            }
            return new String(lsequenceArray);
        } catch (Exception e) {
            throw new TdbDaoException("Error when querying  assembly lsequence", e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getUngappedConsensusData(int assemblyID, String project, int start, int length) throws DAOException {
        try {
            PreparedStatement statement = TDBUtil.getPreparedStatement(TDBConnection.getInstance(), queryFilter.filterQuery(AssemblyQueries.QUERY_UNGAPPED_CONSENSUS), project);
            statement.setInt(1, assemblyID);
            statement.executeQuery();
            ResultSet rs = statement.getResultSet();
            if (rs == null) {
                statement.close();
                throw new TdbDaoException("Error null result set when querying  assembly.sequence");
            }
            if (!rs.next()) {
                rs.close();
                statement.close();
                throw new TdbDaoException("Error no result set when querying  assembly.sequence");
            }
            String seqAsString = rs.getString(1);
            if (seqAsString == null) {
                rs.close();
                statement.close();
                throw new TdbDaoException("Error sequence field is null for assembly " + assemblyID);
            }
            rs.close();
            statement.close();
            return seqAsString.substring(start, start + length);
        } catch (SQLException e) {
            throw new TdbDaoException("Error when querying assembly.sequence", e);
        }
    }

    public List<Feature> getAssemblyFeatures(int assemblyID, String project, int start, int end) throws TdbDaoException {
        try {
            PreparedStatement statement = TDBUtil.getPreparedStatement(TDBConnection.getInstance(), queryFilter.filterQuery(AssemblyQueries.QUERY_ASM_FEATURES), project);
            statement.setInt(1, assemblyID);
            statement.setInt(2, start);
            statement.setInt(3, start);
            statement.setInt(4, end);
            statement.setInt(5, end);
            ResultSet rs = statement.executeQuery();
            if (rs == null) {
                statement.close();
                throw new TdbDaoException("Error null result set when querying assembly features");
            }
            List<Feature> asmFeatures = new ArrayList<Feature>();
            while (rs.next()) {
                int end5 = rs.getInt(1);
                int end3 = rs.getInt(2);
                int featID = rs.getInt(3);
                int parentID = rs.getInt(4);
                String featureType = rs.getString(5);
                String featName = rs.getString(6);
                boolean closedNUU = false;
                boolean openNUU = INonUniqueUnitigFeature.OPEN_NONUNIQUE_UNITIG_FEATURE_TYPE.equals(featureType);
                if (!openNUU) {
                    closedNUU = INonUniqueUnitigFeature.CLOSED_NONUNIQUE_UNITIG_FEATURE_TYPE.equals(featureType);
                }
                Feature feature;
                if (openNUU) {
                    feature = new Feature(featName, Feature.Type.NUU, featID, end5, end3, parentID);
                } else if (closedNUU) {
                    feature = new Feature(featName, Feature.Type.CLOSEDNUU, featID, end5, end3, parentID);
                } else {
                    feature = new Feature(featName, Feature.Type.UNKOWN, featID, end5, end3, parentID);
                }
                asmFeatures.add(feature);
            }
            rs.close();
            statement.close();
            return asmFeatures;
        } catch (SQLException e) {
            throw new TdbDaoException("Error when querying assembly features", e);
        }
    }

    public boolean assemblyIsLatest(int assemblyID, String project) throws DAOException {
        try {
            PreparedStatement statement = TDBUtil.getPreparedStatement(TDBConnection.getInstance(), queryFilter.filterQuery(AssemblyQueries.QUERY_ASSEMBLY_NOT_LATEST), project);
            statement.setInt(1, assemblyID);
            ResultSet rs = statement.executeQuery();
            if (rs == null) {
                statement.close();
                throw new DAOException("null result when querying if assembly is latest ");
            }
            boolean ret = !rs.next();
            rs.close();
            statement.close();
            return ret;
        } catch (SQLException e) {
            throw new DAOException("error trying to query if assembly is latest :\n" + e.getMessage(), e);
        }
    }

    public boolean assemblyContainsTrashSequences(int assemblyID, String project) throws DAOException {
        try {
            PreparedStatement statement = TDBUtil.getPreparedStatement(TDBConnection.getInstance(), queryFilter.filterQuery(AssemblyQueries.QUERY_ASSEMBLY_HAS_TRASH_SEQUENCES), project);
            statement.setInt(1, assemblyID);
            ResultSet rs = statement.executeQuery();
            if (rs == null) {
                statement.close();
                throw new DAOException("null result when querying if assembly has trash sequence ");
            }
            boolean ret = rs.next();
            rs.close();
            statement.close();
            return ret;
        } catch (SQLException e) {
            throw new DAOException("error trying to query if assembly has trash sequence :\n" + e.getMessage(), e);
        }
    }
}
