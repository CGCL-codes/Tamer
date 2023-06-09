package jshm.scraper;

import org.htmlparser.tags.*;
import jshm.exceptions.ScraperException;

public interface TieredTabularDataHandler {

    public jshm.scraper.DataTable getDataTable();

    public TieredTabularDataExtractor.InvalidChildCountStrategy getInvalidChildCountStrategy();

    public boolean ignoreNewData();

    public void handleHeaderRow(TableRow tr) throws ScraperException;

    public void handleTierRow(String tierName) throws ScraperException;

    public void handleDataRow(String[][] data) throws ScraperException;
}
