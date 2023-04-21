package ch.ethz.dcg.spamato.filter.nubay;

import java.sql.*;
import ch.ethz.dcg.spamato.db.utils.DBObject;
import ch.ethz.dcg.spamato.filter.nubay.bayes.NuBayToken;

public class DBToken implements DBObject<NuBayToken> {

    private static final DBTokenMatcher DEFAULT_MATCHER = new DefaultDBTokenMatcher();

    DBTokenMatcher matcher;

    public DBToken() {
        this(DBToken.DEFAULT_MATCHER);
    }

    public DBToken(DBTokenMatcher matcher) {
        this.matcher = matcher;
    }

    public NuBayToken getObject(ResultSet resultSet) throws SQLException {
        return DBToken.get(resultSet, getMatcher());
    }

    public static NuBayToken get(ResultSet resultSet) throws SQLException {
        return DBToken.get(resultSet, DBToken.DEFAULT_MATCHER);
    }

    public static NuBayToken get(ResultSet resultSet, DBTokenMatcher matcher) throws SQLException {
        String name = resultSet.getString("name");
        int ham = resultSet.getInt("ham");
        int spam = resultSet.getInt("spam");
        double spamProbability = resultSet.getDouble("probability");
        long timestamp = resultSet.getTimestamp("date").getTime();
        NuBayToken token = new NuBayToken(name, ham, spam, spamProbability, timestamp);
        token = matcher.matches(token) ? token : null;
        return token;
    }

    public DBTokenMatcher getMatcher() {
        return matcher;
    }
}

interface DBTokenMatcher {

    public boolean matches(NuBayToken token);
}

class DefaultDBTokenMatcher implements DBTokenMatcher {

    public boolean matches(NuBayToken token) {
        return true;
    }
}
