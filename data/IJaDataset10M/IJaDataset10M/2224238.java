package org.mcisb.beacon.pierre;

import java.util.*;
import pedro.security.*;
import pedro.util.*;
import pierre.model.*;
import pierre.reports.*;
import pierre.system.*;

/**
 * 
 * @author Neil Swainston
 */
public abstract class AbstractDataRepository extends pierre.db.AbstractDataRepository {

    /**
	 * 
	 */
    protected static final String ACTION = "action";

    /**
	 * 
	 */
    protected static final String SHOW = "show";

    /**
	 * 
	 */
    protected static final String ID = "show";

    /**
	 * 
	 */
    protected static final String DEPTH = "depth";

    /**
	 * 
	 */
    protected DatabaseOperations databaseOperations;

    /**
	 * 
	 */
    private final ResourceBundle resourceBundle = ResourceBundle.getBundle("org.mcisb.pierre.messages");

    /**
	 * 
	 */
    private final String queryLanguage;

    /**
	 * Constructor.
	 * 
	 * @param queryLanguage e.g. SQL or XQuery
	 */
    public AbstractDataRepository(final String queryLanguage) {
        this.queryLanguage = queryLanguage;
        clearDataBases();
    }

    /** 
	 * Sets the <code>parameters</code> taken from the Pierre Browser Configuration Tool parameters.
	 * 
	 * <p>
	 * Some of these parameters contain the values required to set up the database connection.
	 * </p>
	 * <p>
	 * As such, if a new implementation of <code>DatabaseOperations</code> is to be used, this method is the recommended
	 * method in which to construct the new implementation of <code>DatabaseOperations</code>, and to set it to the
	 * <code>databaseOperations</code> member variable.
	 * </p>
	 * 
	 * @see org.mcisb.pierre.DatabaseOperations
	 * @see pierre.db.DataRepository#setParameters(pedro.util.Parameter[])
	 */
    public void setParameters(Parameter[] parameters) throws Exception {
        super.setParameters(parameters);
        addDataBase(dbCollectionName, queryLanguage);
    }

    /** 
	 * Called by the Browse feature.
	 * 
	 * <p>
	 * Takes <code>browseAttributes</code> specified in the Pierre Browser Configuration Tool
	 * and returns <code>TupleProvider[]</code> from
	 * <code>org.mcisb.pierre.DatabaseOperations#getTupleProviders(java.lang.String,java.lang.String,java.util.List)</code>.
	 * </p>
	 * 
	 * @see org.mcisb.pierre.DatabaseOperations#getTupleProviders(java.lang.String,java.lang.String,java.util.List)
	 * @see pierre.db.DataRepository#getDataSetSummaries(java.lang.String[], pedro.security.User)
	 */
    public TupleProvider[] getDataSetSummaries(String[] browseAttributes, User user) {
        if (authenticateUser(user)) {
            try {
                final String SUMMARY = resourceBundle.getString("AbstractDataRepository.summary");
                final List fields = new ArrayList(Arrays.asList(browseAttributes));
                return databaseOperations.getTupleProviders(SUMMARY, SUMMARY, fields);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return (new TupleProvider[0]);
    }

    public String[] getExistingValues(String recordClassName, String fieldName, String schemaContext, User user) {
        if (authenticateUser(user)) {
            try {
                final List fieldNamesRequested = new ArrayList();
                fieldNamesRequested.add(recordClassName + "." + fieldName);
                final List values = databaseOperations.getValues(fieldNamesRequested);
                final String[] existingValues = new String[values.size()];
                for (int i = 0; i < values.size(); i++) {
                    final int FIRST = 0;
                    existingValues[i] = (String) ((List) values.get(i)).get(FIRST);
                }
                return existingValues;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return (new String[0]);
    }

    /**
	 * Called by the Simple Search and Advanced Search features.
	 * 
	 * @see org.mcisb.pierre.DatabaseOperations#getTuples(pierre.model.QueryFeature)
	 * @see pierre.db.DataRepository#execute(pierre.model.QueryFeature, pierre.system.DeploymentForm, pedro.security.User)
	 */
    public Report execute(QueryFeature queryFeature, DeploymentForm deploymentForm, User user) {
        Report report = null;
        if (authenticateUser(user)) {
            try {
                final Tuple[] tuples = databaseOperations.getTuples(queryFeature);
                if (tuples.length == 0) {
                    return new NoResultsFoundReport(deploymentForm);
                }
                report = new ExtendedTupleReport(deploymentForm, queryFeature.getName(), queryFeature.getName(), tuples);
                ((ExtendedTupleReport) report).setTimeStamp(getTimeStamp());
                ((ExtendedTupleReport) report).setUserID(user.getID());
            } catch (Exception e) {
                e.printStackTrace();
                report = new ErrorReport(deploymentForm);
                ((ErrorReport) report).addError(e);
            }
        } else {
            report = new ErrorReport(deploymentForm);
        }
        report.setIdentifier(generateReportIdentifier());
        caterReportForDeploymentForm(report, deploymentForm);
        return report;
    }

    /** 
	 * Called by hyperlinked items in Pierre deployment.
	 * 
	 * @see org.mcisb.pierre.DatabaseOperations#getTupleProviders(java.lang.String, java.lang.String, java.lang.String, int)
	 * @see pierre.db.DataRepository#execute(pierre.reports.LinkObject, pierre.system.DeploymentForm, pedro.security.User)
	 */
    public Report execute(LinkObject linkObject, DeploymentForm deploymentForm, User user) {
        Report report = null;
        if (authenticateUser(user)) {
            try {
                String actionType = linkObject.getParameterValue(ACTION);
                TupleProvider[] tupleProviders = null;
                if (actionType.equals(SHOW)) {
                    final String LINK = resourceBundle.getString("AbstractDataRepository.link");
                    tupleProviders = databaseOperations.getTupleProviders(LINK, LINK, linkObject.getParameterValue(ID), Integer.parseInt(linkObject.getParameterValue(DEPTH)));
                }
                if (tupleProviders == null || tupleProviders.length == 0) {
                    report = new NoResultsFoundReport(deploymentForm);
                } else {
                    report = new DefaultTupleReport(deploymentForm, tupleProviders);
                    ((DefaultTupleReport) report).setTimeStamp(getTimeStamp());
                    ((DefaultTupleReport) report).setUserID(user.getID());
                }
            } catch (Exception e) {
                report = new ErrorReport(deploymentForm);
                ((ErrorReport) report).addError(e);
            }
        } else {
            report = new ErrorReport(deploymentForm);
        }
        report.setIdentifier(generateReportIdentifier());
        caterReportForDeploymentForm(report, deploymentForm);
        return report;
    }

    /** 
	 * Called by the Expert Search feature.
	 * 
	 * <p>
	 * Executes the <code>query</code>, which will be in the native query language of the database, e.g. SQL or XQuery.
	 * </p>
	 * 
	 * @see org.mcisb.pierre.DatabaseOperations#getTuples(java.lang.String, java.lang.String, java.lang.String)
	 * @see pierre.db.DataRepository#execute(java.lang.String, java.lang.String, pierre.system.DeploymentForm, pedro.security.User)
	 */
    public Report execute(String dataBase, String query, DeploymentForm deploymentForm, User user) {
        Report report = null;
        if (authenticateUser(user)) {
            try {
                final String RECORD_CLASS_NAME = "Expert Search results";
                Tuple[] tuples = databaseOperations.getTuples(query, RECORD_CLASS_NAME, RECORD_CLASS_NAME);
                if (tuples.length == 0) {
                    return new NoResultsFoundReport(deploymentForm);
                }
                report = createDefaultReport(deploymentForm, resourceBundle.getString("AbstractDataRepository.reportPresentationName"), RECORD_CLASS_NAME, tuples, user);
            } catch (Exception e) {
                report = new ErrorReport(deploymentForm);
                ((ErrorReport) report).addError(e);
            }
        } else {
            report = new ErrorReport(deploymentForm);
        }
        report.setIdentifier(generateReportIdentifier());
        caterReportForDeploymentForm(report, deploymentForm);
        return report;
    }

    /**
	 * Default behaviour is to return true.
	 * 
	 * Essentially authenticates ALL users.
	 * 
	 * @param user
	 * @return boolean indicating user authentication
	 * @see pierre.db.DataRepository#authenticateUser(pedro.security.User)
	 */
    public boolean authenticateUser(final User user) {
        return true;
    }

    /** 
	 * Returns array of ReportFileFormat containing ReportFileFormat.HTML_FORMAT, ReportFileFormat.TEXT_FORMAT, ReportFileFormat.CSV_FORMAT, ReportFileFormat.XML_FORMAT.
	 * 
	 * @return ReportFileFormat[] containing ReportFileFormat.HTML_FORMAT, ReportFileFormat.TEXT_FORMAT, ReportFileFormat.CSV_FORMAT, ReportFileFormat.XML_FORMAT
	 * @see pierre.db.DataRepository#getSupportedReportFileFormats()
	 */
    public ReportFileFormat[] getSupportedReportFileFormats() {
        final ReportFileFormat[] reportFileFormats = new ReportFileFormat[4];
        reportFileFormats[0] = ReportFileFormat.HTML_FORMAT;
        reportFileFormats[1] = ReportFileFormat.TEXT_FORMAT;
        reportFileFormats[2] = ReportFileFormat.CSV_FORMAT;
        reportFileFormats[3] = ReportFileFormat.XML_FORMAT;
        return reportFileFormats;
    }

    /**
	 * Helper method returning the value of the supplied parameter name.
	 *
	 * @param parameters
	 * @param name
	 * @return String parameter value, or null if the parameter is not present
	 */
    protected String getValue(final Parameter[] parameters, final String name) {
        for (int i = 0; i < parameters.length; i++) {
            final Parameter parameter = parameters[i];
            if (parameter.getName().equals(name)) {
                return parameter.getValue();
            }
        }
        return null;
    }

    /**
	 * This method helps adapt the report for the web.
	 * 
	 * We have noticed that the XML_FORMAT works for every form of deployment except the web.
	 * 
	 * This may be due to the way the web pages are autogenerated.  
	 * 
	 * @param report
	 * @param deploymentForm
	 */
    protected void caterReportForDeploymentForm(Report report, DeploymentForm deploymentForm) {
        if (deploymentForm.equals(DeploymentForm.WEB_APPLICATION)) {
            final Collection formatsAppropriateForWeb = new ArrayList();
            final ReportFileFormat[] reportFileFormats = report.getSupportedReportFileFormats();
            for (int i = 0; i < reportFileFormats.length; i++) {
                if (!reportFileFormats[i].equals(ReportFileFormat.XML_FORMAT)) {
                    formatsAppropriateForWeb.add(reportFileFormats[i]);
                }
            }
            final ReportFileFormat[] revisedReportFileFormats = (ReportFileFormat[]) formatsAppropriateForWeb.toArray(new ReportFileFormat[formatsAppropriateForWeb.size()]);
            report.setSupportedReportFileFormats(revisedReportFileFormats);
        }
    }
}
