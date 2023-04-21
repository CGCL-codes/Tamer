package yahoofinance.data;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import org.apache.log4j.Logger;
import org.hibernate.annotations.IndexColumn;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.SimpleNodeIterator;
import yahoofinance.data.model.BalanceSheetDate;
import yahoofinance.filter.TableFilter;

/**
 * A balance sheet represents all of the data found on the Yahoo! Finance
 * Balance Sheet page.
 * 
 * @author Charlie Meyer <charlie@charliemeyer.net>
 */
@Entity
public class BalanceSheet implements YahooFinancePage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private int type;

    private String symbol;

    @OneToMany(cascade = { CascadeType.ALL })
    @JoinTable(name = "BalanceSheetBalanceSheetDates", joinColumns = @JoinColumn(name = "balance_sheet_id"), inverseJoinColumns = @JoinColumn(name = "balance_sheet_date_id"))
    @IndexColumn(name = "PersonPhones_list_index")
    private List<BalanceSheetDate> balanceSheetDates;

    @Transient
    private static final Logger logger = Logger.getLogger(BalanceSheet.class);

    @Transient
    public static final int BALANCE_SHEET_ANNUAL = 1;

    @Transient
    public static final int BALANCE_SHEET_QUARTERLY = 2;

    /**
	 * 2 Argument Constructor, loads this BalanceSheet with current data parsed
	 * from Yahoo Finance
	 * 
	 * @param symbol
	 *            the symbol to load data for
	 * @param type
	 *            the type of BalanceSheet, either quarterly or Annual
	 */
    public BalanceSheet(final String symbol, final int type) {
        setSymbol(symbol);
        setType(type);
        balanceSheetDates = new ArrayList<BalanceSheetDate>();
        loadData();
    }

    public BalanceSheet() {
    }

    /**
	 * gets the BalanceSheetDate objects for this BalanceSheet
	 * 
	 * @return a List of BalanceSheetDate objects
	 */
    public List<BalanceSheetDate> getBalanceSheetDates() {
        return balanceSheetDates;
    }

    @Override
    public void loadData() {
        try {
            String url = "";
            if (type == BalanceSheet.BALANCE_SHEET_ANNUAL) {
                url = "http://finance.yahoo.com/q/bs?s=" + symbol + "&annual";
            }
            if (type == BalanceSheet.BALANCE_SHEET_QUARTERLY) {
                url = "http://finance.yahoo.com/q/bs?s=" + symbol;
            }
            final Parser parser = new Parser(url);
            final NodeList list = parser.parse(new TableFilter());
            final SimpleNodeIterator iterator = list.elements();
            while (iterator.hasMoreNodes()) {
                final TableTag table = (TableTag) iterator.nextNode();
                final String classStr = table.getAttribute("class");
                if (classStr != null && classStr.equals("yfnc_tabledata1")) {
                    parseOuterTable(table);
                }
            }
        } catch (final ParserException pe) {
            logger.error("Error Parsing Balance Sheet", pe);
        }
    }

    private void parseOuterTable(final TableTag table) {
        final TableRow[] rows = table.getRows();
        final TableRow row = rows[0];
        final TableColumn[] columns = row.getColumns();
        final TableColumn column = columns[0];
        final TableTag innerTable = (TableTag) column.getChild(0);
        parseInnerTable(innerTable);
    }

    private void parseInnerTable(final TableTag table) {
        final TableRow[] rows = table.getRows();
        final TableRow header = rows[0];
        final TableColumn[] headerColumns = header.getColumns();
        for (int i = 1; i < headerColumns.length; i++) {
            final Node dateNode = headerColumns[i].childAt(1);
            final String date = dateNode.toHtml();
            final BalanceSheetDate balSheetDate = new BalanceSheetDate(date);
            balanceSheetDates.add(balSheetDate);
        }
        for (int i = 1; i < rows.length; i++) {
            parseRow(rows[i]);
        }
    }

    private void parseRow(final TableRow row) {
        if ((row.getColumnCount() == 6 && type == BalanceSheet.BALANCE_SHEET_QUARTERLY) || (row.getColumnCount() == 5 && type == BalanceSheet.BALANCE_SHEET_ANNUAL)) {
            final TableColumn[] columns = row.getColumns();
            final TableColumn keyColumn = columns[1];
            String key = "";
            if (keyColumn.getChildCount() == 1) {
                key = keyColumn.getChildrenHTML();
            } else {
                key = keyColumn.getChild(1).getText();
            }
            for (int i = 2; i < columns.length; i++) {
                final BalanceSheetDate date = balanceSheetDates.get(i - 2);
                String number = columns[i].getChildrenHTML().replace(",", "").replace("&nbsp;", "").replace("<b>", "").replace("</b>", "");
                if (number.contains("-")) {
                    number = "0";
                }
                if (number.startsWith("(")) {
                    number = "-" + number.substring(1, number.length() - 1);
                }
                if (number.startsWith("$")) {
                    number = number.substring(1);
                }
                date.put(key, Double.parseDouble(number));
            }
        } else if ((row.getColumnCount() == 5 && type == BalanceSheet.BALANCE_SHEET_QUARTERLY) || (row.getColumnCount() == 4 && type == BalanceSheet.BALANCE_SHEET_ANNUAL)) {
            final TableColumn[] columns = row.getColumns();
            final TableColumn keyColumn = columns[0];
            String key = "";
            if (keyColumn.getChildCount() == 1) {
                key = keyColumn.getChildrenHTML();
            } else {
                key = keyColumn.getChild(1).getText();
            }
            for (int i = 1; i < columns.length; i++) {
                final BalanceSheetDate date = balanceSheetDates.get(i - 1);
                String number = columns[i].getChildrenHTML().replace(",", "").replace("&nbsp;", "").replace("<b>", "").replace("</b>", "");
                if (number.contains("-")) {
                    number = "0";
                }
                if (number.startsWith("(")) {
                    number = "-" + number.substring(1, number.length() - 1);
                }
                if (number.startsWith("$")) {
                    number = number.substring(1);
                }
                if (number.startsWith("-$")) {
                    number = "-" + number.substring(2);
                }
                date.put(key, Double.parseDouble(number));
            }
        }
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(final String symbol) {
        this.symbol = symbol;
    }

    public long getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public void setType(final int type) {
        this.type = type;
    }

    @Override
    public void setId(final long id) {
        this.id = id;
    }

    public void setBalanceSheetDates(final List<BalanceSheetDate> balanceSheetDates) {
        this.balanceSheetDates = balanceSheetDates;
    }
}
