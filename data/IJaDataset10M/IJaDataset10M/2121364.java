package net.lukemurphey.nsia.scan;

import java.sql.*;
import java.net.*;
import java.util.*;
import net.lukemurphey.nsia.Application;
import net.lukemurphey.nsia.MaxMinCount;
import net.lukemurphey.nsia.NameIntPair;
import net.lukemurphey.nsia.NoDatabaseConnectionException;
import net.lukemurphey.nsia.scan.Definition.Severity;
import net.lukemurphey.nsia.scan.ScanRule.ScanResultLoadFailureException;

public class HttpDefinitionScanResult extends ScanResult {

    private URL url;

    private Vector<DefinitionMatch> definitionMatches;

    private String contentType;

    public HttpDefinitionScanResult(ScanResultCode resultcode, Timestamp timeOfScan, URL url, long scanRuleId) {
        super(resultcode, timeOfScan);
        super.ruleId = scanRuleId;
        this.scanTime = timeOfScan;
        this.url = url;
        this.definitionMatches = new Vector<DefinitionMatch>();
        this.deviations = definitionMatches.size();
    }

    public HttpDefinitionScanResult(ScanResultCode resultcode, Timestamp timeOfScan, URL url, DefinitionMatch[] definitionMatches, long scanRuleId) {
        super(resultcode, timeOfScan);
        super.ruleId = scanRuleId;
        this.scanTime = timeOfScan;
        this.url = url;
        this.definitionMatches = new Vector<DefinitionMatch>();
        for (DefinitionMatch match : definitionMatches) {
            if (match != null) {
                this.definitionMatches.add(match);
            }
        }
        this.deviations = this.definitionMatches.size();
    }

    public HttpDefinitionScanResult(ScanResultCode resultcode, Timestamp timeOfScan, URL url, DefinitionMatch[] signatureMatches, long scanRuleId, String contentType) {
        super(resultcode, timeOfScan);
        super.ruleId = scanRuleId;
        this.scanTime = timeOfScan;
        this.url = url;
        this.definitionMatches = new Vector<DefinitionMatch>();
        for (DefinitionMatch match : signatureMatches) {
            if (match != null) {
                this.definitionMatches.add(match);
            }
        }
        this.deviations = this.definitionMatches.size();
        this.contentType = contentType;
    }

    public HttpDefinitionScanResult(ScanResultCode resultcode, Timestamp timeOfScan, URL url, DefinitionMatch signatureMatch, long scanRuleId) {
        super(resultcode, timeOfScan);
        super.ruleId = scanRuleId;
        this.scanTime = timeOfScan;
        this.url = url;
        this.definitionMatches = new Vector<DefinitionMatch>();
        if (signatureMatch != null) {
            this.definitionMatches.add(signatureMatch);
        }
        this.deviations = this.definitionMatches.size();
    }

    public HttpDefinitionScanResult(ScanResultCode resultcode, Timestamp timeOfScan, URL url, DefinitionMatch signatureMatch, long scanRuleId, String contentType) {
        super(resultcode, timeOfScan);
        super.ruleId = scanRuleId;
        this.scanTime = timeOfScan;
        this.url = url;
        this.definitionMatches = new Vector<DefinitionMatch>();
        if (signatureMatch != null) {
            this.definitionMatches.add(signatureMatch);
        }
        this.deviations = this.definitionMatches.size();
        this.contentType = contentType;
    }

    public HttpDefinitionScanResult(ScanResultCode resultcode, Timestamp timeOfScan, URL url, Vector<DefinitionMatch> signatureMatches, long scanRuleId) {
        super(resultcode, timeOfScan);
        super.ruleId = scanRuleId;
        this.scanTime = timeOfScan;
        this.url = url;
        this.definitionMatches = new Vector<DefinitionMatch>();
        this.definitionMatches.addAll(signatureMatches);
        this.deviations = this.definitionMatches.size();
        this.deviations = signatureMatches.size();
    }

    public HttpDefinitionScanResult(ScanResultCode resultcode, Timestamp timeOfScan, URL url, Vector<DefinitionMatch> signatureMatches, long scanRuleId, String contentType) {
        super(resultcode, timeOfScan);
        super.ruleId = scanRuleId;
        this.scanTime = timeOfScan;
        this.url = url;
        this.definitionMatches = new Vector<DefinitionMatch>();
        this.definitionMatches.addAll(signatureMatches);
        this.deviations = signatureMatches.size();
        this.contentType = contentType;
    }

    @Override
    public String getRuleType() {
        return HttpDefinitionScanRule.RULE_TYPE;
    }

    @Override
    public String getSpecimenDescription() {
        return url.toString();
    }

    public URL getUrl() {
        return url;
    }

    public String getContentType() {
        return contentType;
    }

    /**
	 * Retrieves a count of the rule severities triggered for the given scan result.
	 * @param parentScanResultID
	 * @return
	 * @throws SQLException
	 * @throws NoDatabaseConnectionException
	 */
    public static Hashtable<Definition.Severity, Integer> getSignatureMatchSeverities(long parentScanResultID) throws SQLException, NoDatabaseConnectionException {
        return getSignatureMatchSeverities(Application.getApplication(), parentScanResultID);
    }

    /**
	 * Retrieves a list of the rules that were matched during the given scan.
	 * @param parentScanResultID
	 * @return
	 * @throws SQLException
	 * @throws NoDatabaseConnectionException
	 */
    public static Vector<NameIntPair> getSignatureMatches(long parentScanResultID) throws SQLException, NoDatabaseConnectionException {
        return getSignatureMatches(Application.getApplication(), parentScanResultID);
    }

    /**
	 * Retrieves a list of the rules that were matched during the given scan.
	 * @param application
	 * @param parentScanResultID
	 * @return
	 * @throws SQLException
	 * @throws NoDatabaseConnectionException
	 */
    public static Vector<NameIntPair> getSignatureMatches(Application application, long parentScanResultID) throws SQLException, NoDatabaseConnectionException {
        if (application == null) {
            throw new NoDatabaseConnectionException();
        }
        Vector<NameIntPair> sigMatches = new Vector<NameIntPair>();
        Connection connection = null;
        PreparedStatement matchedSignatureStatement = null;
        ResultSet result = null;
        try {
            connection = application.getDatabaseConnection(Application.DatabaseAccessType.SCANNER);
            matchedSignatureStatement = connection.prepareStatement("select MatchedRule.RuleName, count(*) from SignatureScanResult inner join ScanResult on ScanResult.ScanResultID = SignatureScanResult.ScanResultID inner join MatchedRule on MatchedRule.ScanResultID = SignatureScanResult.ScanResultID where ParentScanResultID = ? group by MatchedRule.RuleName order by count(*) desc");
            matchedSignatureStatement.setLong(1, parentScanResultID);
            result = matchedSignatureStatement.executeQuery();
            while (result.next()) {
                String name;
                int count;
                name = result.getString(1);
                count = result.getInt(2);
                sigMatches.add(new NameIntPair(name, count));
            }
        } finally {
            if (matchedSignatureStatement != null) {
                matchedSignatureStatement.close();
            }
            if (result != null) {
                result.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return sigMatches;
    }

    /**
	 * Retrieves a count of the rule severities triggered for the given scan result. 
	 * @param application
	 * @param parentScanResultID
	 * @return
	 * @throws SQLException
	 * @throws NoDatabaseConnectionException
	 */
    public static Hashtable<Definition.Severity, Integer> getSignatureMatchSeverities(Application application, long parentScanResultID) throws SQLException, NoDatabaseConnectionException {
        if (application == null) {
            throw new NoDatabaseConnectionException();
        }
        int low = 0;
        int medium = 0;
        int high = 0;
        Connection connection = null;
        PreparedStatement matchedSignatureStatement = null;
        ResultSet result = null;
        try {
            connection = application.getDatabaseConnection(Application.DatabaseAccessType.SCANNER);
            matchedSignatureStatement = connection.prepareStatement("select Severity, count(*) as Instances from ScanResult inner join MatchedRule on MatchedRule.ScanResultID = ScanResult.ScanResultID where ParentScanResultID = ? group by Severity");
            matchedSignatureStatement.setLong(1, parentScanResultID);
            result = matchedSignatureStatement.executeQuery();
            while (result.next()) {
                int sev = result.getInt("Severity");
                if (sev == Definition.Severity.LOW.ordinal()) {
                    low = result.getInt("Instances");
                } else if (sev == Definition.Severity.MEDIUM.ordinal()) {
                    medium = result.getInt("Instances");
                } else if (sev == Definition.Severity.HIGH.ordinal()) {
                    high = result.getInt("Instances");
                }
            }
        } finally {
            if (matchedSignatureStatement != null) {
                matchedSignatureStatement.close();
            }
            if (result != null) {
                result.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        Hashtable<Definition.Severity, Integer> results = new Hashtable<Definition.Severity, Integer>();
        results.put(Definition.Severity.LOW, Integer.valueOf(low));
        results.put(Definition.Severity.MEDIUM, Integer.valueOf(medium));
        results.put(Definition.Severity.HIGH, Integer.valueOf(high));
        return results;
    }

    public DefinitionMatch[] getDefinitionMatches() {
        DefinitionMatch[] matches = new DefinitionMatch[this.definitionMatches.size()];
        definitionMatches.toArray(matches);
        return matches;
    }

    /**
	 * Causes the scan result to archive the specimen to the database and remove the copy from memory. This method
	 * should be used when the scan result is still needed but the data will no longer be actively used.
	 * 
	 * The method returns the identifier of the specimen in the archive.
	 * @return Identifier of the specimen archive (in the database)
	 */
    public int archive() {
        return -1;
    }

    public HttpDefinitionScanResult[] getFindings(String signatureName, String contentType, int startResultID, int count, boolean getBefore) {
        return null;
    }

    public static class SignatureScanResultFilter {

        private String contentType = null;

        private String signatureName = null;

        public SignatureScanResultFilter(String contentType, String signatureName) {
            this.contentType = contentType;
            this.signatureName = signatureName;
        }

        public SignatureScanResultFilter() {
        }

        public String getContentType() {
            return contentType;
        }

        public String getSignatureName() {
            return signatureName;
        }
    }

    /**
	 * Retrieve the scan results associated with the given filter and parameters.
	 * @param parentScanResultID
	 * @param resultsAfter
	 * @param filter
	 * @param count
	 * @param application
	 * @return
	 * @throws ScanResultLoadFailureException
	 * @throws SQLException
	 * @throws NoDatabaseConnectionException
	 */
    public static HttpDefinitionScanResult[] loadScanResults(long parentScanResultID, boolean resultsAfter, SignatureScanResultFilter filter, int count, Application application) throws ScanResultLoadFailureException, SQLException, NoDatabaseConnectionException {
        return loadScanResults(parentScanResultID, -1, resultsAfter, filter, count, application);
    }

    /**
	 * Determine the maximum, minimum identifier and the count of items matching the given the filter.
	 * @param parentScanResultID
	 * @param filter
	 * @param application
	 * @return
	 * @throws ScanResultLoadFailureException
	 * @throws SQLException
	 * @throws NoDatabaseConnectionException
	 */
    public static MaxMinCount getScanResultInfo(long parentScanResultID, SignatureScanResultFilter filter, Application application) throws SQLException, NoDatabaseConnectionException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        int max = -1;
        int min = -1;
        int count = 0;
        try {
            connection = application.getDatabaseConnection(Application.DatabaseAccessType.SCANNER);
            if (filter.getContentType() != null && filter.getContentType().length() > 0 && filter.getSignatureName() != null) {
                statement = connection.prepareStatement("select max(ScanResult.ScanResultID), min(ScanResult.ScanResultID), count(ScanResult.ScanResultID) from SignatureScanResult inner join ScanResult on SignatureScanResult.ScanResultID = ScanResult.ScanResultID inner join MatchedRule on MatchedRule.ScanResultID = ScanResult.ScanResultID where ContentType = ? and RuleName = ? and ParentScanResultID = ? group by ScanResult.ParentScanResultID");
                statement.setString(1, filter.getContentType());
                statement.setString(2, filter.getSignatureName());
                statement.setLong(3, parentScanResultID);
            } else if ((filter.getContentType() == null || filter.getContentType().length() == 0) && filter.getSignatureName() != null) {
                if (filter.getContentType() != null) {
                    statement = connection.prepareStatement("select max(ScanResult.ScanResultID), min(ScanResult.ScanResultID), count(ScanResult.ScanResultID) from SignatureScanResult inner join ScanResult on SignatureScanResult.ScanResultID = ScanResult.ScanResultID inner join MatchedRule on MatchedRule.ScanResultID = ScanResult.ScanResultID where ContentType is null and RuleName = ? and ParentScanResultID = ? group by ScanResult.ParentScanResultID");
                } else {
                    statement = connection.prepareStatement("select max(ScanResult.ScanResultID), min(ScanResult.ScanResultID), count(ScanResult.ScanResultID) from SignatureScanResult inner join ScanResult on SignatureScanResult.ScanResultID = ScanResult.ScanResultID inner join MatchedRule on MatchedRule.ScanResultID = ScanResult.ScanResultID where RuleName = ? and ParentScanResultID = ? group by ScanResult.ParentScanResultID");
                }
                statement.setString(1, filter.getSignatureName());
                statement.setLong(2, parentScanResultID);
            } else if (filter.getContentType() != null && filter.getContentType().length() > 0 && filter.getSignatureName() == null) {
                statement = connection.prepareStatement("select max(ScanResult.ScanResultID), min(ScanResult.ScanResultID), count(ScanResult.ScanResultID) from SignatureScanResult inner join ScanResult on SignatureScanResult.ScanResultID = ScanResult.ScanResultID where ContentType = ? and ParentScanResultID = ? group by ScanResult.ParentScanResultID");
                statement.setString(1, filter.getContentType());
                statement.setLong(2, parentScanResultID);
            } else {
                if (filter.getContentType() != null && filter.getContentType().length() == 0) {
                    statement = connection.prepareStatement("select max(ScanResult.ScanResultID), min(ScanResult.ScanResultID), count(ScanResult.ScanResultID) from SignatureScanResult inner join ScanResult on SignatureScanResult.ScanResultID = ScanResult.ScanResultID where ParentScanResultID = ? and ContentType is null group by ScanResult.ParentScanResultID");
                } else {
                    statement = connection.prepareStatement("select max(ScanResult.ScanResultID), min(ScanResult.ScanResultID), count(ScanResult.ScanResultID) from SignatureScanResult inner join ScanResult on SignatureScanResult.ScanResultID = ScanResult.ScanResultID where ParentScanResultID = ? group by ScanResult.ParentScanResultID");
                }
                statement.setLong(1, parentScanResultID);
            }
            result = statement.executeQuery();
            while (result.next()) {
                max = result.getInt(1);
                min = result.getInt(2);
                count = result.getInt(3);
            }
            return new MaxMinCount(max, min, count);
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (result != null) {
                result.close();
            }
        }
    }

    /**
	 * Retrieve the scan results associated with the given filter and parameters.
	 * @param parentScanResultID
	 * @param start
	 * @param resultsAfter
	 * @param filter
	 * @param count
	 * @param application
	 * @return
	 * @throws ScanResultLoadFailureException
	 * @throws SQLException
	 * @throws NoDatabaseConnectionException
	 */
    public static HttpDefinitionScanResult[] loadScanResults(long parentScanResultID, long start, boolean getResultsBefore, SignatureScanResultFilter filter, int count, Application application) throws ScanResultLoadFailureException, SQLException, NoDatabaseConnectionException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        Vector<HttpDefinitionScanResult> results = new Vector<HttpDefinitionScanResult>();
        long currentRule = -1;
        try {
            connection = application.getDatabaseConnection(Application.DatabaseAccessType.SCANNER);
            String queryAdd;
            if (start > -1 && getResultsBefore) {
                queryAdd = " and ScanResult.ScanResultID < ?";
            } else if (start > -1) {
                queryAdd = " and ScanResult.ScanResultID > ?";
            } else {
                queryAdd = "";
            }
            String sort;
            if (getResultsBefore == true) {
                sort = " order by ScanResult.ScanResultID desc";
            } else {
                sort = " order by ScanResult.ScanResultID asc";
            }
            if (filter.getContentType() != null && filter.getContentType().length() > 0 && filter.getSignatureName() != null) {
                statement = connection.prepareStatement("select ScanResult.ScanResultID from SignatureScanResult inner join ScanResult on SignatureScanResult.ScanResultID = ScanResult.ScanResultID inner join MatchedRule on MatchedRule.ScanResultID = ScanResult.ScanResultID where ContentType = ? and RuleName = ? and ParentScanResultID = ? " + queryAdd + " group by ScanResult.ScanResultID " + sort);
                statement.setString(1, filter.getContentType());
                statement.setString(2, filter.getSignatureName());
                statement.setLong(3, parentScanResultID);
                if (start > -1) {
                    statement.setLong(4, start);
                }
            } else if ((filter.getContentType() == null || filter.getContentType().length() == 0) && filter.getSignatureName() != null) {
                if (filter.getContentType() != null) {
                    statement = connection.prepareStatement("select ScanResult.ScanResultID from SignatureScanResult inner join ScanResult on SignatureScanResult.ScanResultID = ScanResult.ScanResultID inner join MatchedRule on MatchedRule.ScanResultID = ScanResult.ScanResultID where ContentType is null and RuleName = ? and ParentScanResultID = ? " + queryAdd + " group by ScanResult.ScanResultID" + sort);
                } else {
                    statement = connection.prepareStatement("select ScanResult.ScanResultID from SignatureScanResult inner join ScanResult on SignatureScanResult.ScanResultID = ScanResult.ScanResultID inner join MatchedRule on MatchedRule.ScanResultID = ScanResult.ScanResultID where RuleName = ? and ParentScanResultID = ? " + queryAdd + " group by ScanResult.ScanResultID" + sort);
                }
                statement.setString(1, filter.getSignatureName());
                statement.setLong(2, parentScanResultID);
                if (start > -1) {
                    statement.setLong(3, start);
                }
            } else if (filter.getContentType() != null && filter.getContentType().length() > 0 && filter.getSignatureName() == null) {
                statement = connection.prepareStatement("select ScanResult.ScanResultID from SignatureScanResult inner join ScanResult on SignatureScanResult.ScanResultID = ScanResult.ScanResultID where ContentType = ? and ParentScanResultID = ? " + queryAdd + " group by ScanResult.ScanResultID" + sort);
                statement.setString(1, filter.getContentType());
                statement.setLong(2, parentScanResultID);
                if (start > -1) {
                    statement.setLong(3, start);
                }
            } else {
                if (filter.getContentType() != null && filter.getContentType().length() == 0) {
                    statement = connection.prepareStatement("select ScanResult.ScanResultID from SignatureScanResult inner join ScanResult on SignatureScanResult.ScanResultID = ScanResult.ScanResultID where ParentScanResultID = ? and ContentType is null " + queryAdd + " group by ScanResult.ScanResultID" + sort);
                } else {
                    statement = connection.prepareStatement("select ScanResult.ScanResultID from SignatureScanResult inner join ScanResult on SignatureScanResult.ScanResultID = ScanResult.ScanResultID where ParentScanResultID = ? " + queryAdd + " group by ScanResult.ScanResultID" + sort);
                }
                statement.setLong(1, parentScanResultID);
                if (start > -1) {
                    statement.setLong(2, start);
                }
            }
            statement.setMaxRows(count);
            result = statement.executeQuery();
            while (result.next()) {
                currentRule = result.getInt(1);
                results.add((HttpDefinitionScanResult) ScanResultLoader.getScanResult(currentRule));
            }
        } catch (SQLException e) {
            throw new ScanRule.ScanResultLoadFailureException("Rule " + currentRule + " could not be loaded", e);
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (result != null) {
                result.close();
            }
        }
        HttpDefinitionScanResult[] resultsArray = new HttpDefinitionScanResult[results.size()];
        if (getResultsBefore == true) {
            for (int c = 0; c < resultsArray.length; c++) {
                resultsArray[resultsArray.length - 1 - c] = results.get(c);
            }
        } else {
            results.toArray(resultsArray);
        }
        return resultsArray;
    }

    /**
	 * Adds a broken link signature match for the given URL. 
	 * @param url
	 */
    protected void addBrokenLink(String url) {
        this.deviations++;
        this.definitionMatches.add(new DefinitionMatch(MetaDefinition.BROKEN_LINK.createNewWithMessage(" to: " + url)));
    }

    /**
	 * Get the signature matches associated with the given scan result.
	 * @param scanResultID
	 * @param application
	 * @return
	 * @throws NoDatabaseConnectionException
	 * @throws SQLException
	 */
    protected static Vector<DefinitionMatch> loadSignatureMatches(long scanResultID, Application application) throws NoDatabaseConnectionException, SQLException {
        Connection connection = null;
        PreparedStatement matchedSignatureStatement = null;
        ResultSet result = null;
        Vector<DefinitionMatch> signatureMatches = new Vector<DefinitionMatch>();
        try {
            connection = application.getDatabaseConnection(Application.DatabaseAccessType.SCANNER);
            matchedSignatureStatement = connection.prepareStatement("Select * from MatchedRule where ScanResultID = ?");
            matchedSignatureStatement.setLong(1, scanResultID);
            result = matchedSignatureStatement.executeQuery();
            while (result.next()) {
                String signatureName = result.getString("RuleName");
                String message = result.getString("RuleMessage");
                int signatureId = result.getInt("RuleID");
                int detectStart = result.getInt("MatchStart");
                int detectLength = result.getInt("MatchLength");
                Severity severity = Severity.UNDEFINED;
                int severityId = result.getInt("Severity");
                for (int c = 0; c < Severity.values().length; c++) {
                    if (Severity.values()[c].ordinal() == severityId) {
                        severity = Severity.values()[c];
                    }
                }
                DefinitionMatch sigMatch = new DefinitionMatch(signatureName, message, severity, signatureId, detectStart, detectLength);
                signatureMatches.add(sigMatch);
            }
            return signatureMatches;
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (matchedSignatureStatement != null) {
                matchedSignatureStatement.close();
            }
            if (result != null) {
                result.close();
            }
        }
    }

    /**
	 * Get the maximum severity of the definition matches.
	 * @return
	 */
    public Severity getMaxSeverity() {
        Severity severity = Severity.LOW;
        DefinitionMatch[] matches = getDefinitionMatches();
        for (int c = 0; c < matches.length; c++) {
            if (matches[c].getSeverity() == Severity.HIGH) {
                return Severity.HIGH;
            } else if (matches[c].getSeverity() == Severity.MEDIUM) {
                severity = Severity.MEDIUM;
            }
        }
        return severity;
    }

    protected void loadFromScanResult(ResultSet findingResult, Application application) throws SQLException, NoDatabaseConnectionException, ScanResultLoadFailureException, MalformedURLException {
        String url = findingResult.getString("URL");
        this.url = new URL(url);
        this.contentType = findingResult.getString("ContentType");
        this.ruleId = findingResult.getLong("ScanRuleID");
        if (this.scanResultId <= -1) {
            this.scanResultId = findingResult.getLong("ScanResultID");
        }
        this.definitionMatches = loadSignatureMatches(this.scanResultId, application);
    }

    protected static HttpDefinitionScanResult loadFromDatabase(long scanRuleId, long scanResultId, ScanResultCode resultCode, Timestamp scanTime, int deviations) throws SQLException, NoDatabaseConnectionException, ScanResultLoadFailureException {
        Application application = Application.getApplication();
        Connection connection = null;
        PreparedStatement matchedSignatureStatement = null;
        ResultSet result = null;
        PreparedStatement findingStatement = null;
        ResultSet findingResult = null;
        Vector<DefinitionMatch> signatureMatches = new Vector<DefinitionMatch>();
        try {
            connection = application.getDatabaseConnection(Application.DatabaseAccessType.SCANNER);
            matchedSignatureStatement = connection.prepareStatement("Select * from MatchedRule where ScanResultID = ?");
            matchedSignatureStatement.setLong(1, scanResultId);
            result = matchedSignatureStatement.executeQuery();
            while (result.next()) {
                String signatureName = result.getString("RuleName");
                String message = result.getString("RuleMessage");
                int signatureId = result.getInt("RuleID");
                int detectStart = result.getInt("MatchStart");
                int detectLength = result.getInt("MatchLength");
                Severity severity = Severity.UNDEFINED;
                int severityId = result.getInt("Severity");
                for (int c = 0; c < Severity.values().length; c++) {
                    if (Severity.values()[c].ordinal() == severityId) {
                        severity = Severity.values()[c];
                    }
                }
                DefinitionMatch sigMatch = new DefinitionMatch(signatureName, message, severity, signatureId, detectStart, detectLength);
                signatureMatches.add(sigMatch);
            }
            findingStatement = connection.prepareStatement("Select * from SignatureScanResult where ScanResultID = ?");
            findingStatement.setLong(1, scanResultId);
            findingResult = findingStatement.executeQuery();
            if (findingResult.next()) {
                String url = findingResult.getString("URL");
                String contentType = findingResult.getString("ContentType");
                HttpDefinitionScanResult scanResult = new HttpDefinitionScanResult(resultCode, scanTime, new URL(url), signatureMatches, scanRuleId);
                scanResult.contentType = contentType;
                scanResult.deviations = deviations;
                scanResult.ruleId = scanRuleId;
                scanResult.scanResultId = scanResultId;
                return scanResult;
            } else {
                throw new ScanResultLoadFailureException("The finding for the given scan result was not found in the database");
            }
        } catch (MalformedURLException e) {
            throw new ScanResultLoadFailureException("The URL associated with the scan result is invalid", e);
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (matchedSignatureStatement != null) {
                matchedSignatureStatement.close();
            }
            if (result != null) {
                result.close();
            }
            if (findingStatement != null) {
                findingStatement.close();
            }
        }
    }

    /**
	 * Save the result to the database and link it to the scan result identifier.
	 * @param connection
	 * @param scanResultId
	 * @return
	 * @throws SQLException
	 */
    public long saveLinkedResultToDatabase(Connection connection, long scanResultId) throws SQLException {
        if (connection == null) return -1;
        if (scanResultId == -1) return -1;
        PreparedStatement statement = null;
        PreparedStatement matchedRulesStatement = null;
        try {
            statement = connection.prepareStatement("Insert into SignatureScanResult (ScanResultID, URL) values (?, ?)");
            statement.setLong(1, scanResultId);
            statement.setString(2, this.url.toString());
            matchedRulesStatement = connection.prepareStatement("Insert into MatchedRule (ScanResultID, RuleName, RuleMessage, RuleID, MatchStart, MatchLength, Severity) values (?, ?, ?, ?, ?, ?, ?)");
            if (definitionMatches != null) {
                Iterator<DefinitionMatch> matches = definitionMatches.iterator();
                while (matches.hasNext()) {
                    DefinitionMatch match = matches.next();
                    matchedRulesStatement.setLong(1, scanResultId);
                    matchedRulesStatement.setString(2, match.getDefinitionName());
                    matchedRulesStatement.setString(3, match.getMessage());
                    matchedRulesStatement.setInt(4, match.getDefinitionID());
                    matchedRulesStatement.setInt(5, match.getDetectStart());
                    matchedRulesStatement.setInt(6, match.getDetectLength());
                    matchedRulesStatement.setInt(7, match.getSeverity().ordinal());
                    matchedRulesStatement.executeUpdate();
                }
            }
            long httpResultId = statement.executeUpdate();
            if (httpResultId < 0) return -1;
            return httpResultId;
        } finally {
            if (statement != null) statement.close();
            if (matchedRulesStatement != null) matchedRulesStatement.close();
        }
    }

    @Override
    public long saveToDatabase(Connection connection, long scanRuleId) throws SQLException {
        if (connection == null) return -1;
        long scanResultId = saveToDatabaseInitial(connection, scanRuleId, HttpDefinitionScanRule.RULE_TYPE);
        if (scanResultId == -1) return -1;
        PreparedStatement statement = null;
        PreparedStatement matchedRulesStatement = null;
        try {
            statement = connection.prepareStatement("Insert into SignatureScanResult (ScanResultID, URL, ContentType) values (?, ?, ?)");
            statement.setLong(1, scanResultId);
            statement.setString(2, url.toString());
            statement.setString(3, contentType);
            long httpResultId = statement.executeUpdate();
            if (httpResultId < 0) return -1;
            matchedRulesStatement = connection.prepareStatement("Insert into MatchedRule (ScanResultID, RuleName, RuleMessage, RuleID, MatchStart, MatchLength, Severity) values (?, ?, ?, ?, ?, ?, ?)");
            if (definitionMatches != null) {
                Iterator<DefinitionMatch> matches = definitionMatches.iterator();
                while (matches.hasNext()) {
                    DefinitionMatch match = matches.next();
                    matchedRulesStatement.setLong(1, scanResultId);
                    matchedRulesStatement.setString(2, match.getDefinitionName());
                    matchedRulesStatement.setString(3, match.getMessage());
                    matchedRulesStatement.setInt(4, match.getDefinitionID());
                    matchedRulesStatement.setInt(5, match.getDetectStart());
                    matchedRulesStatement.setInt(6, match.getDetectLength());
                    matchedRulesStatement.setInt(7, match.getSeverity().ordinal());
                    matchedRulesStatement.executeUpdate();
                }
            }
            saveToDatabaseFinalize(connection, scanResultId, getDeviations(), scanRuleId);
            return httpResultId;
        } finally {
            if (statement != null) statement.close();
            if (matchedRulesStatement != null) matchedRulesStatement.close();
        }
    }
}
