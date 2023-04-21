package xetbotv2.libs;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.jibble.pircbot.Colors;
import xetbotv2.libs.xCalendar;

/**
	gestisce le citazioni: apre la connessione col db, prende i dati e li sputa.
*/
public class xQuote {

    /** this is the current ResultSet */
    private ResultSet rs;

    /** mantain connection with db */
    private xSQL db;

    /** this is true if the last executed query has given at least one row of results */
    private boolean hasResults;

    /** maintain results */
    private String[] result;

    /**
		default constructor: creates connection with given db
		@param jdbcurl a jdbc-formatted url to your database
	*/
    public xQuote(String jdbcurl) {
        this.db = new xSQL(jdbcurl);
        this.db.openConnection();
        this.hasResults = false;
    }

    /**
		returns a formatted string containning number quotes of given author
		@param author the autor of the quote you want
		@param number the number of quotes you want
		@returns formatted string of the quotes
	*/
    public void getQuoteFromAuthor(String author, String number) {
        String query = "select * from quote where author like '%" + xSQL.checkSQLSyntax(author) + "%' order by rand() limit 0," + xSQL.checkSQLSyntax(number);
        this.runquery(query);
        this.formatResults();
    }

    /**
		returns a formatted string containning number quotes containning keyword
		@param keyword the keyword you wanna extract
		@param number the number of quotes you want
		@returns formatted string of the quotes
	*/
    public void getQuoteFromKey(String keyword, String number) {
        String query = "select * from quote where quote like '%" + xSQL.checkSQLSyntax(keyword) + "%' ORDER BY rand( ) LIMIT 0 , " + xSQL.checkSQLSyntax(number);
        this.runquery(query);
        this.formatResults();
    }

    /**
		returns a formatted string containning number quotes of given 
		author containning given keyword
		@param author the autor of the quote you want
		@param keyword the keyword you wanna extract
		@param number the number of quotes you want
		@returns formatted string of the quotes
	*/
    public void getQuoteFromAuthorAndKey(String author, String keyword, String number) {
        String query = "select * from quote where author like '%" + xSQL.checkSQLSyntax(author) + "%' and quote like '%" + xSQL.checkSQLSyntax(keyword) + "%' order by rand() limit 0," + xSQL.checkSQLSyntax(number);
        this.runquery(query);
        this.formatResults();
    }

    /**
		returns a number of random quotes
		@param number the number of quotes you want
		@returns a number of random quotes
	*/
    public void getNRandomQuote(String number) {
        String query = "select * from quote order by rand() limit 0," + xSQL.checkSQLSyntax(number);
        this.runquery(query);
        this.formatResults();
    }

    /**
		returns a single quote in any specific order
		@returns a single quote in any specific order
	*/
    public void getRandomQuote() {
        String query = "select * from quote order by rand() limit 0,1";
        this.runquery(query);
        this.formatResults();
    }

    /**
		returns last inserted quote
		@returns last inserted quote
	*/
    public void getLastQuote() {
        String query = "select * from quote where id=(SELECT MAX(id) from quote)";
        this.runquery(query);
        this.formatResults();
    }

    /**
		returns the current number of quotes present in database
		@returns the current number of quotes present in database
	*/
    public String numberOfQuotes() {
        String query = "select MAX(id) from quote";
        this.runquery(query);
        return this.db.getResultString("MAX(id)");
    }

    /**
		add a quote with given params
		@param author the autor of the quote you wanna insert
		@param quote wath he\she\it said
		@param timestamp when he\she\it have talked
		
	*/
    public void addQuote(String author, String quote) {
        this.db.executeQuery("INSERT INTO `quote` (`author`,`quote`) VALUES ('" + xSQL.checkSQLSyntax(author) + "','" + xSQL.checkSQLSyntax(quote) + "')");
    }

    /**
		runs the given query
		@param query the SQL query
	*/
    private void runquery(String query) {
        this.hasResults = this.db.runQuery(query);
        this.rs = db.getResults();
    }

    /**
		extract and format the resultset. Edit this if you wann translate your quote-bot
		@returns a human-readable string of current results
	*/
    private void formatResults() {
        if (!this.hasResults) return;
        this.result = new String[xetbotv2.modules.XetBotQuoteModule.MAX_NUMBER_OF_QUOTES];
        for (int i = 0; i < xetbotv2.modules.XetBotQuoteModule.MAX_NUMBER_OF_QUOTES; i++) this.result[i] = "";
        int i = 0;
        boolean hasMoreResults = true;
        while (hasMoreResults) {
            try {
                xCalendar cal = new xCalendar(rs.getTimestamp("timestamp").getTime());
                this.result[i] += Colors.BOLD + Colors.RED + rs.getString("author") + Colors.NORMAL + " il " + Colors.BOLD + Colors.DARK_GREEN + cal.getFormattedTime() + " " + Colors.BROWN + cal.getFormattedDate() + Colors.NORMAL + " ha detto: " + Colors.REVERSE + rs.getString("quote") + " " + Colors.NORMAL;
                hasMoreResults = rs.next();
                i++;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public String getResult(int index) {
        if (index < xetbotv2.modules.XetBotQuoteModule.MAX_NUMBER_OF_QUOTES) return result[index];
        return null;
    }

    public boolean hasResults() {
        return this.hasResults;
    }
}
