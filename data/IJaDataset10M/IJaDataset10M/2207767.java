package edu.ucdavis.genomics.metabolomics.binbase.algorythm.Import.quality;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import edu.ucdavis.genomics.metabolomics.binbase.bci.server.types.ExperimentClass;
import edu.ucdavis.genomics.metabolomics.binbase.bci.server.types.ExperimentSample;
import edu.ucdavis.genomics.metabolomics.exception.BinBaseException;
import edu.ucdavis.genomics.metabolomics.util.SQLObject;

/**
 * generates experimentclasses for us based on the quality service in the lab
 * like reaction blanks method blanks quality controls and so on
 * 
 * should be called by a timer ones a day so we get sure that we import our quality data
 * 
 * @author wohlgemuth
 * @version Dec 5, 2005
 * 
 */
public class QualityClassGenerator extends SQLObject {

    /**
	 * how we generate our search pattern is also used for the name of the class
	 */
    DateFormat format = new SimpleDateFormat("yyMMdd");

    /**
	 * get the pattern for the given id
	 */
    private PreparedStatement pattern;

    /**
	 * find samples matching to the given day
	 */
    private PreparedStatement sampleNames;

    public QualityClassGenerator() {
        super();
    }

    /**
	 * generates the experiment class for the given date it includes
	 * 
	 * @author wohlgemuth
	 * @version Dec 5, 2005
	 * @param date
	 * @return
	 * @throws BinBaseException 
	 */
    public ExperimentClass generate(Date date) throws BinBaseException {
        try {
            List qc = findQualityControls(date);
            List reaction = findReactionBlanks(date);
            List method = findMethodBlanks(date);
            int size = qc.size() + reaction.size() + method.size();
            ExperimentClass exp = new ExperimentClass();
            ExperimentSample sample[] = new ExperimentSample[size];
            int i = 0;
            Iterator it = qc.iterator();
            while (it.hasNext()) {
                sample[i] = (ExperimentSample) it.next();
                i++;
            }
            it = reaction.iterator();
            while (it.hasNext()) {
                sample[i] = (ExperimentSample) it.next();
                i++;
            }
            it = method.iterator();
            while (it.hasNext()) {
                sample[i] = (ExperimentSample) it.next();
                i++;
            }
            exp.setSamples(sample);
            exp.setId("quality " + format.format(date));
            return exp;
        } catch (SQLException e) {
            throw new BinBaseException(e);
        }
    }

    /**
	 * finds all the reaction blanks from the given date
	 * 
	 * @author wohlgemuth
	 * @version Dec 5, 2005
	 * @param date
	 * @return
	 * @throws SQLException
	 */
    private List findReactionBlanks(Date date) throws SQLException {
        this.pattern.setInt(1, 4);
        ResultSet res = pattern.executeQuery();
        res.next();
        String pattern = res.getString(1);
        res.close();
        return findByPattern(pattern, date);
    }

    /**
	 * finds all the method blanks
	 * 
	 * @author wohlgemuth
	 * @version Dec 5, 2005
	 * @param date
	 * @return
	 * @throws SQLException
	 */
    private List findMethodBlanks(Date date) throws SQLException {
        this.pattern.setInt(1, 1);
        ResultSet res = pattern.executeQuery();
        res.next();
        String pattern = res.getString(1);
        res.close();
        return findByPattern(pattern, date);
    }

    /**
	 * finds all the quality controls
	 * 
	 * @author wohlgemuth
	 * @version Dec 5, 2005
	 * @param date
	 * @return
	 * @throws SQLException
	 */
    private List findQualityControls(Date date) throws SQLException {
        this.pattern.setInt(1, 2);
        ResultSet res = pattern.executeQuery();
        res.next();
        String pattern = res.getString(1);
        res.close();
        return findByPattern(pattern, date);
    }

    /**
	 * return data wish fits to a given pattern
	 * 
	 * @author wohlgemuth
	 * @version Dec 5, 2005
	 * @param pattern
	 * @return
	 * @throws SQLException
	 */
    private List findByPattern(String pattern, Date date) throws SQLException {
        this.sampleNames.setString(1, pattern);
        this.sampleNames.setString(2, format.format(date) + ".*");
        ResultSet result = sampleNames.executeQuery();
        Vector data = new Vector();
        while (result.next()) {
            ExperimentSample sample = new ExperimentSample();
            sample.setName(result.getString(1));
            sample.setId(result.getString(1));
            data.add(sample);
        }
        result.close();
        return data;
    }

    protected void prepareStatements() throws Exception {
        super.prepareStatements();
        this.sampleNames = this.getConnection().prepareStatement(SQL_CONFIG.getValue(CLASS + ".sampleNames"));
        this.pattern = this.getConnection().prepareStatement(SQL_CONFIG.getValue(CLASS + ".pattern"));
    }
}
