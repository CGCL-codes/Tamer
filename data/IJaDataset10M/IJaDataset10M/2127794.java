package edu.ucdavis.genomics.metabolomics.binbase.algorythm.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import edu.ucdavis.genomics.metabolomics.binbase.bci.server.types.Experiment;
import edu.ucdavis.genomics.metabolomics.binbase.bci.server.types.ExperimentClass;
import edu.ucdavis.genomics.metabolomics.binbase.bci.server.types.ExperimentSample;
import edu.ucdavis.genomics.metabolomics.exception.DataStoreException;
import edu.ucdavis.genomics.metabolomics.util.SQLObject;
import edu.ucdavis.genomics.metabolomics.util.status.Report;

/**
 * uses classic sql to store and check results
 * 
 * @author wohlgemuth
 */
public class SQLResultCreator extends SQLObject implements ResultCreator {

    private Report report;

    private PreparedStatement checkForSamples;

    private PreparedStatement insertResult;

    private PreparedStatement insertResultLink;

    private PreparedStatement checkForResult;

    private PreparedStatement deleteExistingResult;

    private PreparedStatement deleteExistingResultLink;

    private PreparedStatement nextResultId;

    private PreparedStatement nextResultLinkI;

    private PreparedStatement staticCheckForResultLink;

    public SQLResultCreator(Connection c, Report report) {
        this.setConnection(c);
        this.report = report;
    }

    /**
	 * checks if the result already exist and returns the id or -1
	 * 
	 * @param exp
	 * @return
	 * @throws SQLException
	 */
    protected Integer resultExist(Experiment exp) throws SQLException {
        this.checkForResult.setString(1, exp.getId());
        ResultSet next = this.checkForResult.executeQuery();
        int id;
        if (next.next()) {
            id = next.getInt(1);
            next.close();
            return id;
        }
        next.close();
        return -1;
    }

    /**
	 * creates a new result in the database
	 */
    public int createResultDefinition(Experiment exp) throws Exception {
        logger.debug("got experiment: " + exp);
        int resultId = resultExist(exp);
        logger.debug("internal id is: " + resultId);
        if (resultId != -1) {
            logger.debug("dropping existing result");
            dropResult(resultId);
        } else {
            logger.debug("requesting new result id");
            resultId = getNextResultId();
            logger.debug("generated id is: " + resultId);
        }
        Collection<Integer> sampleIds = getSampleIds(exp.getClasses());
        insertResult.setInt(1, resultId);
        insertResult.setString(2, exp.getId());
        insertResult.setString(3, exp.getId());
        insertResult.setString(4, exp.getId());
        insertResult.setNull(5, Types.VARCHAR);
        logger.debug("created new result in table");
        insertResult.execute();
        logger.debug("created links to sample table");
        for (Integer sample : sampleIds) {
            this.insertResultLink.setInt(1, resultId);
            this.insertResultLink.setInt(2, sample);
            this.insertResultLink.setInt(3, nextResultLinkId());
            this.insertResultLink.setNull(4, Types.VARCHAR);
            this.insertResultLink.execute();
        }
        logger.debug("done with creation");
        return resultId;
    }

    /**
	 * gets the next sequence id
	 * 
	 * @param statement
	 * @return
	 * @throws SQLException
	 */
    private int getNextResultId() throws SQLException {
        ResultSet result = this.nextResultId.executeQuery();
        result.next();
        int id = result.getInt(1);
        result.close();
        return id;
    }

    /**
	 * returns the next link id
	 * @return
	 * @throws SQLException
	 */
    private int getNextResultLinkId() throws SQLException {
        ResultSet result = this.nextResultLinkI.executeQuery();
        result.next();
        int id = result.getInt(1);
        result.close();
        return id;
    }

    /**
	 * calculates the next link id
	 * @return
	 * @throws Exception
	 */
    private int nextResultLinkId() throws Exception {
        int id = getNextResultLinkId();
        while (isValidId(id) == false) {
            logger.debug("link id is already in use, creating a new one: " + id);
            id = getNextResultLinkId();
        }
        logger.debug("validated link id is: " + id);
        return id;
    }

    /**
	 * checks that the link id is valid
	 * @param id
	 * @return
	 * @throws SQLException
	 */
    private boolean isValidId(int id) throws SQLException {
        this.staticCheckForResultLink.setInt(1, id);
        ResultSet result = this.staticCheckForResultLink.executeQuery();
        boolean exist = result.next();
        result.close();
        return !exist;
    }

    /**
	 * dropes the existing result
	 */
    public void dropResult(int resultId) throws Exception {
        logger.debug("dropping: " + resultId);
        this.deleteExistingResult.setInt(1, resultId);
        this.deleteExistingResultLink.setInt(1, resultId);
        logger.debug("dropped result: " + this.deleteExistingResult.execute());
        logger.debug("dropped link: " + this.deleteExistingResultLink.execute());
    }

    public boolean readyForExport(Experiment exp) throws Exception {
        ExperimentClass[] clazzes = exp.getClasses();
        getSampleIds(clazzes);
        return true;
    }

    /**
	 * returns all the samples ids
	 * 
	 * @param clazzes
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 * @throws DataStoreException
	 */
    private Collection<Integer> getSampleIds(ExperimentClass[] clazzes) throws SQLException, Exception, DataStoreException {
        Collection<Integer> result = new ArrayList<Integer>();
        for (ExperimentClass clazz : clazzes) {
            for (ExperimentSample sample : clazz.getSamples()) {
                sample.setName(sample.getName().replace(':', '_'));
                this.checkForSamples.setString(1, sample.getName());
                ResultSet next = this.checkForSamples.executeQuery();
                if (next.next() == false) {
                    next.close();
                    if (sample.getName().matches(".*_[0-9].*") == false) {
                        throw new Exception("couldnt find sample: " + sample.getName());
                    } else {
                        boolean found = false;
                        for (int i : new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }) {
                            this.checkForSamples.setString(1, sample.getName() + "_" + i);
                            next = this.checkForSamples.executeQuery();
                            if (next.next()) {
                                result.add(next.getInt(1));
                                found = true;
                                i = 10;
                            }
                            next.close();
                        }
                        if (found == false) {
                            throw new DataStoreException("sample not found: " + sample.getName() + " checked for _0 ... _9");
                        }
                    }
                } else {
                    result.add(next.getInt(1));
                    next.close();
                }
            }
        }
        return result;
    }

    public Report getReport() {
        return report;
    }

    @Override
    protected void prepareStatements() throws Exception {
        this.checkForSamples = this.getConnection().prepareStatement(SQL_CONFIG.getValue(CLASS + ".checkForSamples"));
        this.deleteExistingResult = this.getConnection().prepareStatement(SQL_CONFIG.getValue(CLASS + ".deleteExistingResult"));
        this.deleteExistingResultLink = this.getConnection().prepareStatement(SQL_CONFIG.getValue(CLASS + ".deleteExistingResultLink"));
        this.insertResult = this.getConnection().prepareStatement(SQL_CONFIG.getValue(CLASS + ".insertResult"));
        this.insertResultLink = this.getConnection().prepareStatement(SQL_CONFIG.getValue(CLASS + ".insertResultLink"));
        this.nextResultId = this.getConnection().prepareStatement(SQL_CONFIG.getValue(CLASS + ".nextResultId"));
        this.nextResultLinkI = this.getConnection().prepareStatement(SQL_CONFIG.getValue(CLASS + ".nextResultLinkId"));
        this.checkForResult = this.getConnection().prepareStatement(SQL_CONFIG.getValue(CLASS + ".checkForResult"));
        this.staticCheckForResultLink = this.getConnection().prepareStatement("SELECT id from result_link where id = ?");
        super.prepareStatements();
    }
}
