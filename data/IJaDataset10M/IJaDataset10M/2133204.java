package org.quantumleaphealth.transform;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Collection;
import java.util.InvalidPropertiesFormatException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.quantumleaphealth.model.trial.Site;
import org.quantumleaphealth.model.trial.TrialEvent;

/**
 * Transforms trial data from remote or local files to a database.
 * 
 * @author Tom Bechtold
 * @version 2009-03-26
 */
public class TrialDataDatabaseTransformer {

    /**
	 * Aggregates the trial data.
	 */
    private final TrialDataAggregator aggregator;

    /**
	 * Parses the trial data.
	 */
    private final TrialDataFileParser parser;

    /**
	 * Reporting engine.
	 */
    private final TrialDataDatabaseReporter reporter;

    /**
	 * Identifier quote string or <code>null</code> if not supported.
	 */
    private final String identifierQuoteString;

    /**
	 * Logger for debugging purposes is registered to this class' name
	 */
    private static final Logger LOGGER = Logger.getLogger(TrialDataDatabaseTransformer.class.getName());

    /**
	 * Name of system property to control whether or not tables are transformed
	 * after parsing data
	 */
    private static final String PROPERTY_TRANSFORM_TABLES = "org.quantumleaphealth.transform.transformTables";

    /**
	 * Specifies that the following parameter is a remote file URL
	 */
    private static final String PARAMETER_REMOTE = "-r";

    /**
	 * Register aggregator to PDQ parser and register reporter to aggregator.
	 * 
	 * @param url
	 *            database connection url, including all parameters needed to
	 *            connect (e.g., user, password, etc.)
	 * @throws IllegalArgumentException
	 *             if <code>url</code> is <code>null</code> or the connection
	 *             cannot be made or the statements cannot be executed
	 */
    public TrialDataDatabaseTransformer(String url) throws IllegalArgumentException {
        aggregator = new TrialDataAggregator();
        PhysicianDataQueryStreamingParser pdqParser = new PhysicianDataQueryStreamingParser();
        pdqParser.addTrialParseListener(aggregator);
        parser = new TrialDataFileParser(pdqParser);
        reporter = new TrialDataDatabaseReporter(url);
        identifierQuoteString = reporter.getIdentifierQuoteString();
        aggregator.addTrialDataReporter(reporter);
    }

    /**
	 * Transforms and reports domestic site data. This method should only be run
	 * after the parsing is completed.
	 * 
	 * @param transformerSiteDomestic
	 *            transformer for domestic site
	 * @return the number of sites reported
	 */
    private int reportSites(TransformerSiteDomestic transformerSiteDomestic) {
        Collection<Site> sites = aggregator.getSites();
        if (sites == null) return 0;
        if (transformerSiteDomestic != null) for (Site site : sites) transformerSiteDomestic.transform(site);
        reporter.outputSites(sites.iterator());
        return sites.size();
    }

    /**
	 * Reports contact data. This method should only be run after the parsing is
	 * completed.
	 */
    private void reportContacts() {
        reporter.outputContacts(aggregator.getContacts());
    }

    /**
	 * Transforms data from PDQ to public tables.
	 * 
	 * @throws SQLException
	 *             if a database error occurs
	 */
    private void transformTables() throws SQLException {
        Date eventDate = new Date(new java.util.Date().getTime());
        transformTable("DELETE FROM trialsite WHERE (screenergroup_id IS null)", "Remove old non-BCT.org trialsites");
        transformTable("DELETE FROM bct_trialsite", "Remove old archived BCT.org trialsites");
        transformTable("INSERT INTO bct_trialsite (trial_id, site_id, screenergroup_id, bctcontact_id, notificationemailaddress, lastmodified) " + "SELECT trial_id, site_id, screenergroup_id, bctcontact_id, notificationemailaddress, lastmodified FROM trialsite", "Archive BCT.org trialsites");
        transformTable("DELETE FROM trialsite", "Remove BCT.org trialsites");
        transformTable("DELETE FROM trial WHERE (lastmodified IS null)", "Remove old non-BCT.org trials");
        PreparedStatement sourceStatement = null;
        ResultSet sourceResultSet = null;
        PreparedStatement targetStatement = null;
        ResultSet targetResultSet = null;
        PreparedStatement insertTrialEventStatement = null;
        boolean insertResult = false;
        long start = System.currentTimeMillis();
        try {
            sourceStatement = reporter.getPreparedStatement("SELECT lastregistered FROM pdq.pdq_trial WHERE id=?", ResultSet.CONCUR_READ_ONLY);
            targetStatement = reporter.getPreparedStatement("SELECT id, open, lastregistered, listed, primaryid FROM trial ORDER BY id", ResultSet.CONCUR_UPDATABLE);
            insertTrialEventStatement = reporter.getPreparedStatement("INSERT INTO trialevent (trial_id, eventdate, eventtype, primaryid) VALUES (?, ?, ?, ?)", ResultSet.CONCUR_READ_ONLY);
            targetResultSet = targetStatement.executeQuery();
            int count = 0;
            while (targetResultSet.next()) {
                boolean dirty = false;
                long trialId = targetResultSet.getLong(1);
                boolean opened = targetResultSet.getBoolean(2);
                Date lastRegistered = targetResultSet.getDate(3);
                boolean listed = targetResultSet.getBoolean(4);
                String primaryId = targetResultSet.getString(5);
                sourceStatement.clearParameters();
                sourceStatement.setLong(1, trialId);
                sourceResultSet = sourceStatement.executeQuery();
                boolean open = sourceResultSet.next();
                String eventType = null;
                if (open != opened) {
                    targetResultSet.updateBoolean(2, open);
                    LOGGER.finest("trial #" + trialId + (open ? " opened" : " closed"));
                    dirty = true;
                    eventType = open ? TrialEvent.OPENED : TrialEvent.CLOSED;
                }
                if (open) {
                    Date lastRegisteredNew = sourceResultSet.getDate(1);
                    if ((lastRegistered != null) ? !lastRegistered.equals(lastRegisteredNew) : (lastRegisteredNew != null)) {
                        if (lastRegisteredNew != null) targetResultSet.updateDate(3, lastRegisteredNew); else targetResultSet.updateNull(3);
                        LOGGER.finest("trial #" + trialId + " registered " + lastRegistered + " -> " + lastRegisteredNew);
                        if (eventType == null) eventType = TrialEvent.UPDATED;
                        dirty = true;
                    }
                }
                if (dirty) {
                    targetResultSet.updateRow();
                    LOGGER.log(Level.FINER, "Updated ('" + eventType + "') " + (listed ? "listed trial #" : "unlisted trial #") + trialId);
                    count++;
                    insertTrialEventStatement.clearParameters();
                    insertTrialEventStatement.setLong(1, trialId);
                    insertTrialEventStatement.setDate(2, eventDate);
                    insertTrialEventStatement.setString(3, eventType);
                    insertTrialEventStatement.setString(4, primaryId);
                    insertResult = insertTrialEventStatement.execute();
                    if (!insertResult) {
                        if (insertTrialEventStatement.getUpdateCount() != 1) LOGGER.log(Level.FINER, "Unable to insert TrialEvent record for trial# " + trialId);
                    }
                }
                sourceResultSet.close();
                sourceResultSet = null;
            }
            LOGGER.fine("Updated " + count + " last-registered and open status of BCT.org trials in " + Long.toString(System.currentTimeMillis() - start) + "ms");
        } finally {
            try {
                if (sourceStatement != null) {
                    sourceStatement.close();
                    sourceStatement = null;
                }
            } catch (Throwable throwable) {
                LOGGER.log(Level.WARNING, "Could not close source statement", throwable);
            }
            try {
                if (targetResultSet != null) {
                    targetResultSet.close();
                    targetResultSet = null;
                }
            } catch (Throwable throwable) {
                LOGGER.log(Level.WARNING, "Could not close target resultset", throwable);
            }
            try {
                if (targetStatement != null) {
                    targetStatement.close();
                    targetStatement = null;
                }
            } catch (Throwable throwable) {
                LOGGER.log(Level.WARNING, "Could not close target statement", throwable);
            }
            try {
                if (insertTrialEventStatement != null) {
                    insertTrialEventStatement.close();
                    insertTrialEventStatement = null;
                }
            } catch (Throwable throwable) {
                LOGGER.log(Level.WARNING, "Could not close insert statement", throwable);
            }
        }
        transformTable("INSERT INTO trialevent (trial_id, eventdate, eventtype, primaryid) SELECT id, date_trunc('day', current_timestamp), '" + TrialEvent.OPENED + "', primaryid" + " FROM pdq.pdq_trial WHERE (id NOT IN (SELECT id FROM trial)) AND (id NOT IN (SELECT trial_id FROM trialevent))", "Record new non-BCT trial opened events");
        transformTable("INSERT INTO trial (id, registry, open, primaryid, " + quotedIdentifier("type") + ", phase, sponsor, " + quotedIdentifier("name") + ", title, purpose, treatmentplan, procedures, burden, duration, followup, linkmarshaled, eligibilitycriteriamarshaled, eligibilitycriterialastmodified, lastbatchmatched, lastmodified, lastregistered, listed) " + "SELECT id, registry, true, primaryid, 0, phase, sponsor, " + quotedIdentifier("name") + ",  title, purpose, treatmentplan, 'n/a', 'n/a', 'n/a', 'n/a', null, eligibilitycriteriamarshaled, null, null, null, lastregistered, false " + "FROM pdq.pdq_trial WHERE (id NOT IN (SELECT id FROM trial))", "Add new non-BCT.org trials with some 'n/a' fields as unlisted");
        transformTable("DELETE FROM site", "Remove old sites");
        transformTable("INSERT INTO site (id, " + quotedIdentifier("name") + ", city, politicalsubunitname, countrycode, postalcode, " + "latitude, latitudesine, latitudecosine, longitude, longituderadians) " + "SELECT id, " + quotedIdentifier("name") + ", city, politicalsubunitname, countrycode, postalcode, " + "latitude, latitudesine, latitudecosine, longitude, longituderadians FROM pdq.pdq_site", "Add new sites");
        transformTable("DELETE FROM screenergroup_site WHERE site_id NOT IN (SELECT id FROM site)", "Remove old sites from screener groups");
        transformTable("DELETE FROM contact WHERE (id NOT IN (SELECT bctcontact_id FROM bct_trialsite)) OR (id IN (SELECT id FROM pdq.pdq_contact))", "Remove old non-BCT.org and non-new contacts");
        transformTable("INSERT INTO contact (id, title, givenname, middleinitial, surname, prefix, generationsuffix,  professionalsuffix, phone, email) " + "SELECT id, title, givenname, middleinitial, surname, prefix, generationsuffix,  professionalsuffix, phone, email FROM pdq.pdq_contact", "Add new contacts");
        transformTable("INSERT INTO trialsite (trial_id, site_id, registeredcontact_id, principalinvestigator_id) " + "SELECT trial_id, site_id, registeredcontact_id, principalinvestigator_id FROM pdq.pdq_trialsite", "Add new trialsites");
        start = System.currentTimeMillis();
        try {
            sourceStatement = reporter.getPreparedStatement("SELECT screenergroup_id, bctcontact_id, notificationemailaddress, lastmodified, trial_id, site_id FROM bct_trialsite ORDER BY screenergroup_id, site_id, trial_id", ResultSet.CONCUR_READ_ONLY);
            targetStatement = reporter.getPreparedStatement("UPDATE trialsite SET screenergroup_id=?, bctcontact_id=?, notificationemailaddress=?, lastmodified=? WHERE (trial_id=?) AND (site_id=?)", ResultSet.CONCUR_READ_ONLY);
            sourceResultSet = sourceStatement.executeQuery();
            int count = 0;
            while (sourceResultSet.next()) {
                targetStatement.clearParameters();
                long screenergroup_id = sourceResultSet.getLong(1);
                if (sourceResultSet.wasNull()) targetStatement.setNull(1, Types.BIGINT); else targetStatement.setLong(1, screenergroup_id);
                long bctcontact_id = sourceResultSet.getLong(2);
                if (sourceResultSet.wasNull()) targetStatement.setNull(2, Types.BIGINT); else targetStatement.setLong(2, bctcontact_id);
                String notificationemailaddress = sourceResultSet.getString(3);
                if (sourceResultSet.wasNull()) targetStatement.setNull(3, Types.VARCHAR); else targetStatement.setString(3, notificationemailaddress);
                Timestamp lastModified = sourceResultSet.getTimestamp(4);
                if (sourceResultSet.wasNull()) targetStatement.setNull(4, Types.TIMESTAMP); else targetStatement.setTimestamp(4, lastModified);
                long trial_id = sourceResultSet.getLong(5);
                if (sourceResultSet.wasNull()) throw new SQLException("Trial id not found in bct_trialsite");
                targetStatement.setLong(5, trial_id);
                long site_id = sourceResultSet.getLong(6);
                if (sourceResultSet.wasNull()) throw new SQLException("Site id not found in bct_trialsite");
                targetStatement.setLong(6, site_id);
                int updateCount = targetStatement.executeUpdate();
                if (updateCount == 1) {
                    LOGGER.finest("Updated BCT group# " + screenergroup_id + " site#" + site_id + " trial#" + trial_id + " last modified " + lastModified);
                    count++;
                } else if (updateCount != 0) throw new SQLException(updateCount + " trialsite rows were updated for trial#" + trial_id + " and site#" + site_id);
            }
            LOGGER.fine("Updated " + count + " BCT.org trialsites in " + Long.toString(System.currentTimeMillis() - start) + "ms");
        } finally {
            try {
                if (sourceResultSet != null) sourceResultSet.close();
            } catch (Throwable throwable) {
                LOGGER.log(Level.WARNING, "Could not close source resultset", throwable);
            }
            try {
                if (sourceStatement != null) sourceStatement.close();
            } catch (Throwable throwable) {
                LOGGER.log(Level.WARNING, "Could not close source statement", throwable);
            }
            try {
                if (targetStatement != null) targetStatement.close();
            } catch (Throwable throwable) {
                LOGGER.log(Level.WARNING, "Could not close target statement", throwable);
            }
        }
    }

    /**
	 * @param identifier
	 *            the identifier
	 * @return a quoted identifier
	 * @see #identifierQuoteString
	 */
    private String quotedIdentifier(String identifier) throws SQLException {
        if (identifierQuoteString == null) throw new SQLException("Cannot quote identifiers in " + identifier);
        return identifier == null ? null : identifierQuoteString + identifier + identifierQuoteString;
    }

    /**
	 * Executes an SQL statement and returns the number of records updated. This
	 * method logs the description, record count and elapsed time.
	 * 
	 * @param sql
	 *            the sql statement that uses double-quote character <tt>"</tt> to
	 *            escape identifiers
	 * @param description
	 *            the description of the statement
	 * @return the number of records updated or 0 if none
	 * @throws IllegalArgumentException
	 *             if <code>sql</code> is <code>null</code>
	 * @throws SQLException
	 *             if <code>sql</code> contains quoted identifiers and the JDBC
	 *             driver does not support them
	 */
    private int transformTable(String sql, String description) throws IllegalArgumentException, SQLException {
        if (sql != null) sql = sql.trim();
        if ((sql == null) || (sql.length() == 0)) throw new IllegalArgumentException("statement not provided '" + sql + '\'');
        Statement statement = null;
        try {
            long start = System.currentTimeMillis();
            statement = reporter.getStatement();
            int count = statement.executeUpdate(sql);
            if (count > 0) description += " (" + count + ')';
            LOGGER.fine(description + " {" + Long.toString(System.currentTimeMillis() - start) + "ms}");
            return count;
        } catch (Throwable throwable) {
            throw new SQLException("Could not execute " + sql, throwable);
        } finally {
            try {
                statement.close();
            } catch (Throwable throwable) {
                LOGGER.log(Level.WARNING, "Could not close statement " + sql, throwable);
            }
        }
    }

    /**
	 * Parses one or more XML files from a local or remote file source, stores
	 * the data into a database and transforms the data to other tables. If a
	 * file argument is not an XML, GZip or Zip file or directory then log a
	 * warning. If each remote file is identical to its local copy then this
	 * method does nothing.
	 * 
	 * @param databaseURL
	 *            a valid database url
	 * @param filenames
	 *            the locations of the files or directories to parse; if a
	 *            location is remote, then the following parameter specifies the
	 *            local file to update
	 * @param transformTables
	 *            whether or not to transform tables after loading data
	 * @throws IllegalArgumentException
	 *             if no files or database URL specified
	 * @throws SQLException
	 *             if a database error occurs
	 */
    private static void transform(String databaseURL, String[] filenames, boolean transformTables) throws IllegalArgumentException, SQLException {
        if ((filenames == null) || (filenames.length == 0)) throw new IllegalArgumentException("no files specified");
        if ((databaseURL == null) || (databaseURL.trim().length() == 0)) throw new IllegalArgumentException("no database URL specified");
        List<File> files = new LinkedList<File>();
        boolean remote = false;
        boolean updated = false;
        for (int parameterIndex = 0; parameterIndex < filenames.length; parameterIndex++) {
            if (PARAMETER_REMOTE.equalsIgnoreCase(filenames[parameterIndex])) {
                remote = true;
                parameterIndex += 2;
                if (parameterIndex >= filenames.length) throw new IllegalArgumentException("No local file specified for file #" + parameterIndex);
                try {
                    if (ImportURL.downloadFile(filenames[parameterIndex - 1], filenames[parameterIndex])) updated = true;
                } catch (IOException ioException) {
                    LOGGER.log(Level.SEVERE, "Could not download " + filenames[parameterIndex - 1] + " into " + filenames[parameterIndex], ioException);
                    continue;
                }
            }
            File file = new File(filenames[parameterIndex]);
            if (file.isDirectory() || file.canRead()) files.add(file); else LOGGER.log(Level.WARNING, "Skipping unreadable local location " + file);
        }
        if (files.isEmpty()) {
            LOGGER.config("No local files found within " + Arrays.toString(filenames));
            return;
        }
        if (remote && !updated) {
            LOGGER.config("No remote files updated within" + Arrays.toString(filenames));
            return;
        }
        LOGGER.config("Identified local files " + files);
        TrialDataDatabaseTransformer transformer = null;
        GeocoderDatabase geocoder = null;
        boolean transformCompleted = false;
        try {
            transformer = new TrialDataDatabaseTransformer(databaseURL);
            LOGGER.config("Set up database transformer");
            geocoder = new GeocoderDatabase(databaseURL, "SELECT postalcode, latitude, longitude, longituderadians, latitudesine, latitudecosine FROM postalcodegeocoding ORDER BY postalcode ASC, population2000 DESC", 99999);
            LOGGER.config("Set up database geocoder");
            int fileCount = 0;
            for (File file : files) fileCount += transformer.parser.parse(file);
            LOGGER.fine("Parsed " + fileCount + " files");
            int siteCount = transformer.reportSites(new TransformerSiteDomestic(geocoder));
            LOGGER.fine("Reported " + siteCount + " sites");
            transformer.reportContacts();
            LOGGER.fine("Reported contacts");
            if ((fileCount == 0) || (siteCount == 0)) LOGGER.warning("No files parsed or sites found so tables not transformed"); else if (!transformTables) LOGGER.config("Tables not transformed upon user request"); else {
                transformer.reporter.getConnection().setAutoCommit(false);
                transformer.transformTables();
                transformer.reporter.getConnection().commit();
                transformCompleted = true;
                LOGGER.fine("Transformed tables");
            }
        } finally {
            try {
                if ((transformer != null) && (transformer.reporter != null)) {
                    if (!transformCompleted) transformer.reporter.getConnection().rollback();
                    transformer.reporter.close();
                }
            } catch (Throwable throwable) {
                LOGGER.log(Level.WARNING, "Cannot close transformer's reporter", throwable);
            }
        }
    }

    /**
	 * This method sends an email to alert the Help Desk that an error occurred during the
	 * transformation process. BCT-421
	 * 
	 * @author Tom Maple
	 * @throws MessagingException 
	 * @throws IOException 
	 * @throws InvalidPropertiesFormatException 
	 */
    private static void sendErrorEmail(Throwable throwable) throws MessagingException, InvalidPropertiesFormatException, IOException {
        Properties propsFromFile = new Properties();
        FileInputStream propertiesIn = null;
        try {
            propertiesIn = new FileInputStream("error-email-info.properties");
            propsFromFile.load(propertiesIn);
            Properties props = new Properties();
            props.setProperty("mail.transport.protocol", propsFromFile.getProperty("smtpTransportProtocol"));
            props.setProperty("mail.smtp.host", propsFromFile.getProperty("smtpHost"));
            props.setProperty("mail.smtp.port", propsFromFile.getProperty("smtpPort"));
            Session session = Session.getDefaultInstance(props, null);
            Message msg = new MimeMessage(session);
            InternetAddress addressFrom = new InternetAddress(propsFromFile.getProperty("fromAddress"));
            msg.setFrom(addressFrom);
            InternetAddress[] addressTo = new InternetAddress[1];
            InternetAddress[] addressCC = new InternetAddress[1];
            InternetAddress[] addressBCC = new InternetAddress[1];
            String toAddr = propsFromFile.getProperty("toAddress");
            if ((toAddr == null) || (toAddr.isEmpty())) return;
            addressTo[0] = new InternetAddress(toAddr);
            msg.setRecipients(Message.RecipientType.TO, addressTo);
            String ccAddr = propsFromFile.getProperty("ccAddress");
            if ((ccAddr != null) && (!ccAddr.isEmpty())) {
                addressCC[0] = new InternetAddress(ccAddr);
                msg.setRecipients(Message.RecipientType.CC, addressCC);
            }
            String bccAddr = propsFromFile.getProperty("bccAddress");
            if ((bccAddr != null) && (!bccAddr.isEmpty())) {
                addressBCC[0] = new InternetAddress(bccAddr);
                msg.setRecipients(Message.RecipientType.BCC, addressBCC);
            }
            msg.setSubject(propsFromFile.getProperty("subject"));
            msg.setContent("Trouble in PDQ Data Transform: " + throwable.toString(), "text/plain");
            Transport.send(msg);
        } finally {
        }
    }

    /**
	 * Parses one or more XML files from a local or remote file source, stores
	 * the data into a database and transforms the data to other tables. If a
	 * file argument is not an XML, GZip or Zip file or directory then log a
	 * warning. If each remote file is identical to its local copy then this
	 * method does nothing.
	 * 
	 * @param arguments
	 *            the first argument is a valid database url; the following
	 *            arguments identify the locations of the files or directories
	 *            to parse; if a location is remote, then the following
	 *            parameter specifies the local file to update
	 */
    public static void main(String[] arguments) {
        if (arguments.length < 2) {
            LOGGER.severe("Usage: databaseURL [[" + PARAMETER_REMOTE + " remoteFile] inputFile]...");
            return;
        }
        try {
            transform(arguments[0], Arrays.copyOfRange(arguments, 1, arguments.length), Boolean.getBoolean(PROPERTY_TRANSFORM_TABLES));
        } catch (Throwable throwable) {
            LOGGER.log(Level.SEVERE, "Could not transform", throwable);
            try {
                sendErrorEmail(throwable);
            } catch (Throwable throwable2) {
                LOGGER.log(Level.SEVERE, "Could not notify anyone");
            }
        }
    }
}
