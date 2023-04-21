package com.m4f.docs.impl;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.util.List;
import java.util.logging.Logger;
import com.google.api.gbase.client.FeedURLFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.spreadsheet.Column;
import com.google.gdata.data.spreadsheet.Data;
import com.google.gdata.data.spreadsheet.Field;
import com.google.gdata.data.spreadsheet.Header;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.RecordEntry;
import com.google.gdata.data.spreadsheet.RecordFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.TableEntry;
import com.google.gdata.data.spreadsheet.TableFeed;
import com.google.gdata.data.spreadsheet.Worksheet;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import com.m4f.business.model.Vote;
import com.m4f.docs.ifc.ISpreadsheetService;

public class GoogleSpreadsheetService implements ISpreadsheetService {

    /** Our view of Google Spreadsheets as an authenticated Google user. */
    private SpreadsheetService service;

    /** A factory that generates the appropriate feed URLs. */
    private FeedURLFactory factory;

    /** The URL of the list feed for the selected spreadsheet. */
    private URL listFeedUrl;

    /** The URL of the table feed for the selected spreadsheet. */
    private URL tablesFeedUrl;

    /** The URL of the record feed for the selected spreadsheet. */
    private URL recordsFeedUrl;

    private static final Logger LOGGER = Logger.getLogger(GoogleSpreadsheetService.class.getName());

    public GoogleSpreadsheetService(String applicationId, String username, String password) throws AuthenticationException {
        this.service = new SpreadsheetService(applicationId);
        service.setUserCredentials(username, password);
        this.factory = FeedURLFactory.getDefault();
    }

    public void createVotesTable(Long companyId, List<Vote> votes, boolean countRecords) throws IOException, ServiceException, Exception {
        URL metafeedUrl = new URL("https://spreadsheets.google.com/feeds/spreadsheets/private/full");
        SpreadsheetFeed feed = service.getFeed(metafeedUrl, SpreadsheetFeed.class);
        List<SpreadsheetEntry> spreadsheets = feed.getEntries();
        SpreadsheetEntry entry = null, spreadsheet = null;
        for (int i = 0; i < spreadsheets.size(); i++) {
            entry = spreadsheets.get(i);
            if (entry.getTitle().getPlainText().equals("" + companyId)) {
                spreadsheet = entry;
            }
        }
        if (spreadsheet == null) {
            throw new Exception("Company with id " + companyId + "not exist");
        }
        URL spreadsheetUrl = new java.net.URL(spreadsheet.getSpreadsheetLink().getHref());
        String baseUrl = new java.net.URL(spreadsheetUrl.getProtocol() + "://" + spreadsheetUrl.getHost()).toString();
        tablesFeedUrl = new java.net.URL(baseUrl + "/feeds/" + spreadsheet.getKey() + "/tables");
        TableFeed tableFeed = service.getFeed(tablesFeedUrl, TableFeed.class);
        LOGGER.info("TableEntry: " + tableFeed.getEntries().size());
        if (tableFeed.getEntries().size() == 0) {
            TableEntry tableEntry = new TableEntry();
            tableEntry.setTitle(new PlainTextConstruct("Votes table"));
            tableEntry.setWorksheet(new Worksheet("Hoja 1"));
            tableEntry.setHeader(new Header(1));
            Data tableData = new Data();
            tableData.setNumberOfRows(0);
            tableData.setStartIndex(2);
            tableData.addColumn(new Column("A", "email"));
            tableData.addColumn(new Column("B", "Release date"));
            tableEntry.setData(tableData);
            tableEntry = service.insert(tablesFeedUrl, tableEntry);
        }
        this.recordsFeedUrl = new java.net.URL(baseUrl + "/feeds/" + spreadsheet.getKey() + "/records/0");
        LOGGER.info("Records Feed url: " + this.recordsFeedUrl.toString());
        if (countRecords) {
            RecordFeed recordFeed = service.getFeed(this.recordsFeedUrl, RecordFeed.class);
            int recordSize = recordFeed.getEntries().size();
            LOGGER.info("++Number of records: " + recordSize);
            Field email, releaseDate;
            RecordEntry record;
            Vote vote;
            for (int i = recordSize; i < votes.size(); i++) {
                vote = votes.get(i);
                record = new RecordEntry();
                email = new Field(null, "email", vote.getEmail());
                record.addField(email);
                if (vote.getReleaseDate() != null) {
                    releaseDate = new Field(null, "Release date", DateFormat.getInstance().format(vote.getReleaseDate()));
                } else {
                    releaseDate = new Field(null, "Release date", "----");
                }
                record.addField(releaseDate);
                service.insert(this.recordsFeedUrl, record);
                LOGGER.info("Added!");
            }
        } else {
            Field email, releaseDate;
            RecordEntry record;
            for (Vote vote : votes) {
                record = new RecordEntry();
                email = new Field(null, "email", vote.getEmail());
                record.addField(email);
                if (vote.getReleaseDate() != null) {
                    releaseDate = new Field(null, "Release date", DateFormat.getInstance().format(vote.getReleaseDate()));
                } else {
                    releaseDate = new Field(null, "Release date", "----");
                }
                record.addField(releaseDate);
                service.insert(this.recordsFeedUrl, record);
                LOGGER.info("Added!");
            }
        }
    }
}
