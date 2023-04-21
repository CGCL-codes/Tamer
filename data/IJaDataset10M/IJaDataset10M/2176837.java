package xetbotv2.libs;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
	manage list of autojoin channels
*/
public class xAutojoinChannels {

    /** this is the current ResultSet */
    private ResultSet rs;

    /** mantain connection with db */
    private xSQL db;

    /** this is true if the last executed query has given at least one row of results */
    private boolean hasResults;

    /**
		default constructor: creates connection with given db
		@param jdbcurl a jdbc-formatted url to your database
	*/
    public xAutojoinChannels(String jdbcurl) {
        this.db = new xSQL(jdbcurl);
        this.init();
    }

    /**
		inizializes objects, connects to db, execs query, 
		memorizes results and set rs pointer to first element
	*/
    private void init() {
        this.db.openConnection();
        String query = "select channel from autojoinchannels where 1";
        this.hasResults = this.db.runQuery(query);
        this.rs = this.db.getResults();
        try {
            this.rs.first();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
		returns channel to join and move rs pointer to next element
		or empty string if there are no more channels
		@returns a channel to join
	*/
    public String getChannel() {
        if (!this.hasResults) return "";
        String result = "";
        try {
            result = rs.getString("channel");
            this.hasResults = rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean hasResults() {
        return this.hasResults;
    }

    public static void main(String[] args) {
        xAutojoinChannels asd = new xAutojoinChannels("jdbc:mysql://localhost/ircbot?user=root&password=texrulez");
        while (asd.hasResults()) System.out.println(asd.getChannel());
    }
}
