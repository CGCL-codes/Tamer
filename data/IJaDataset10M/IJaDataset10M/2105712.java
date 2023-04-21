package macaw.persistenceLayer.production;

import java.sql.Connection;
import java.util.ArrayList;
import macaw.businessLayer.*;
import macaw.system.Log;
import macaw.system.MacawException;
import macaw.system.SessionProperties;

public class ProductionRetrievalService implements MacawRetrievalAPI {

    private SQLConnectionManager sqlConnectionManager;

    private SQLUserManager userManager;

    private SQLChangeEventManager changeEventManager;

    private SQLListChoiceManager listChoiceManager;

    private SQLVariableManager variableManager;

    private SQLBasketManager basketManager;

    private SQLValueLabelManager valueLabelsManager;

    private SQLOntologyTermManager ontologyTermManager;

    private SQLSupportingDocumentsManager documentsManager;

    private SQLStudyMetaDataManager studyMetaDataManager;

    private MacawSecurityAPI securityValidationService;

    private Log log;

    public ProductionRetrievalService(SessionProperties sessionProperties) throws MacawException {
        log = sessionProperties.getLog();
        sqlConnectionManager = new SQLConnectionManager(sessionProperties);
        changeEventManager = new SQLChangeEventManager(sessionProperties.getLog());
        userManager = new SQLUserManager(changeEventManager, sqlConnectionManager);
        securityValidationService = userManager;
        valueLabelsManager = new SQLValueLabelManager(changeEventManager);
        valueLabelsManager.setLog(log);
        ontologyTermManager = new SQLOntologyTermManager(changeEventManager);
        ontologyTermManager.setLog(log);
        listChoiceManager = new SQLListChoiceManager(changeEventManager);
        listChoiceManager.setLog(log);
        documentsManager = new SQLSupportingDocumentsManager(changeEventManager);
        documentsManager.setLog(log);
        variableManager = new SQLVariableManager(changeEventManager, listChoiceManager, ontologyTermManager, documentsManager);
        variableManager.setLog(log);
        basketManager = new SQLBasketManager(changeEventManager, variableManager);
        studyMetaDataManager = new SQLStudyMetaDataManager(sessionProperties);
    }

    public User getUserFromID(User user, String userID) {
        Connection connection = null;
        try {
            checkValidUser(user);
            connection = sqlConnectionManager.getConnection();
            return userManager.getUserFromID(connection, user, userID);
        } catch (MacawException exception) {
            log.logException(exception);
            return null;
        } finally {
            sqlConnectionManager.releaseConnection(connection);
        }
    }

    public ArrayList<User> getUnverifiedUsers(User user) {
        Connection connection = null;
        try {
            checkValidUser(user);
            connection = sqlConnectionManager.getConnection();
            return userManager.getUnverifiedUsers(connection, user);
        } catch (MacawException exception) {
            log.logException(exception);
            ArrayList<User> emptyUserList = new ArrayList<User>();
            return emptyUserList;
        } finally {
            sqlConnectionManager.releaseConnection(connection);
        }
    }

    public ArrayList<User> getUsers(User admin) {
        try {
            checkValidAdministrator(admin);
            return userManager.getUsers(admin);
        } catch (MacawException exception) {
            log.logException(exception);
            ArrayList<User> emptyUserList = new ArrayList<User>();
            return emptyUserList;
        }
    }

    public User getUserFromEmail(User user, String email) {
        Connection connection = null;
        try {
            checkValidUser(user);
            connection = sqlConnectionManager.getConnection();
            return userManager.getUserFromEmail(connection, user, email);
        } catch (MacawException exception) {
            log.logException(exception);
            return null;
        } finally {
            sqlConnectionManager.releaseConnection(connection);
        }
    }

    public ArrayList<Category> getCategories(User user) {
        Connection connection = null;
        try {
            connection = sqlConnectionManager.getConnection();
            checkValidUser(user);
            return listChoiceManager.getCategories(connection, user);
        } catch (MacawException exception) {
            log.logException(exception);
            return null;
        } finally {
            sqlConnectionManager.releaseConnection(connection);
        }
    }

    public ArrayList<DataSensitivityLevel> getDataSensitivityLevels(User user) {
        Connection connection = null;
        try {
            checkValidUser(user);
            connection = sqlConnectionManager.getConnection();
            return listChoiceManager.getDataSensitivityLevels(connection);
        } catch (MacawException exception) {
            log.logException(exception);
            return null;
        } finally {
            sqlConnectionManager.releaseConnection(connection);
        }
    }

    /**
	 * Methods for managing cleaning states
	 */
    public ArrayList<CleaningState> getCleaningStates(User user) {
        Connection connection = null;
        try {
            checkValidUser(user);
            connection = sqlConnectionManager.getConnection();
            return listChoiceManager.getCleaningStates(connection, user);
        } catch (MacawException exception) {
            log.logException(exception);
            return null;
        } finally {
            sqlConnectionManager.releaseConnection(connection);
        }
    }

    public ArrayList<ValueLabel> getValueLabels(User user, String variableName) {
        Connection connection = null;
        try {
            checkValidUser(user);
            connection = sqlConnectionManager.getConnection();
            return valueLabelsManager.getValueLabels(connection, user, variableName);
        } catch (MacawException exception) {
            log.logException(exception);
            return null;
        } finally {
            sqlConnectionManager.releaseConnection(connection);
        }
    }

    public ArrayList<OntologyTerm> getOntologyTerms(User user, String variableName) {
        Connection connection = null;
        try {
            checkValidUser(user);
            connection = sqlConnectionManager.getConnection();
            return variableManager.getAssociatedOntologyTerms(connection, user, variableName);
        } catch (MacawException exception) {
            log.logException(exception);
            return null;
        } finally {
            sqlConnectionManager.releaseConnection(connection);
        }
    }

    public ArrayList<OntologyTerm> getOntologyTerms(User user, Variable variable) {
        Connection connection = null;
        try {
            checkValidUser(user);
            connection = sqlConnectionManager.getConnection();
            return variableManager.getAssociatedOntologyTerms(connection, user, variable);
        } catch (MacawException exception) {
            log.logException(exception);
            return null;
        } finally {
            sqlConnectionManager.releaseConnection(connection);
        }
    }

    public ArrayList<SupportingDocument> getSupportingDocuments(User user, String variableName) {
        Connection connection = null;
        try {
            checkValidUser(user);
            connection = sqlConnectionManager.getConnection();
            return variableManager.getAssociatedSupportingDocuments(connection, user, variableName);
        } catch (MacawException exception) {
            log.logException(exception);
            return null;
        } finally {
            sqlConnectionManager.releaseConnection(connection);
        }
    }

    /**
	 * Methods for data libraries
	 */
    public ArrayList<AliasFilePath> getAliasFilePaths(User user) {
        Connection connection = null;
        try {
            checkValidUser(user);
            connection = sqlConnectionManager.getConnection();
            return listChoiceManager.getAliasFilePaths(connection);
        } catch (MacawException exception) {
            log.logException(exception);
            return null;
        } finally {
            sqlConnectionManager.releaseConnection(connection);
        }
    }

    public AliasFilePath getAliasFilePath(User user, String cardNumber) {
        Connection connection = null;
        try {
            checkValidUser(user);
            connection = sqlConnectionManager.getConnection();
            return listChoiceManager.getAliasFilePath(connection, cardNumber);
        } catch (MacawException exception) {
            log.logException(exception);
            return null;
        } finally {
            sqlConnectionManager.releaseConnection(connection);
        }
    }

    public ArrayList<AliasFilePath> getAliasFilePathsMatchingName(User user, String regularExpression) {
        Connection connection = null;
        try {
            checkValidUser(user);
            connection = sqlConnectionManager.getConnection();
            return listChoiceManager.getAliasFilePathsMatchingName(connection, regularExpression);
        } catch (MacawException exception) {
            log.logException(exception);
            return null;
        } finally {
            sqlConnectionManager.releaseConnection(connection);
        }
    }

    public ArrayList<Category> getCategoriesForVariable(User user, String variableName) {
        Connection connection = null;
        try {
            checkValidUser(user);
            connection = sqlConnectionManager.getConnection();
            return variableManager.getCategoriesForVariable(connection, variableName);
        } catch (MacawException exception) {
            log.logException(exception);
            ArrayList<Category> emptyList = new ArrayList<Category>();
            return emptyList;
        } finally {
            sqlConnectionManager.releaseConnection(connection);
        }
    }

    public ArrayList<VariableSummary> getVariableSummariesForCategory(User user, String categoryName) {
        Connection connection = null;
        try {
            checkValidUser(user);
            connection = sqlConnectionManager.getConnection();
            return variableManager.getVariableSummariesForCategory(connection, categoryName);
        } catch (MacawException exception) {
            log.logException(exception);
            ArrayList<VariableSummary> emptyList = new ArrayList<VariableSummary>();
            return emptyList;
        } finally {
            sqlConnectionManager.releaseConnection(connection);
        }
    }

    public String[] getVariableNames(User user) {
        Connection connection = null;
        try {
            checkValidUser(user);
            connection = sqlConnectionManager.getConnection();
            return variableManager.getVariableNames(connection, user);
        } catch (MacawException exception) {
            log.logException(exception);
            return (new String[0]);
        } finally {
            sqlConnectionManager.releaseConnection(connection);
        }
    }

    public Variable getVariable(User user, String variableName) {
        Connection connection = null;
        try {
            checkValidUser(user);
            connection = sqlConnectionManager.getConnection();
            return variableManager.getVariable(connection, variableName);
        } catch (MacawException exception) {
            log.logException(exception);
            return null;
        } finally {
            sqlConnectionManager.releaseConnection(connection);
        }
    }

    public StudyMetaData getStudyMetaData(User user) {
        Connection connection = null;
        try {
            checkValidUser(user);
            return studyMetaDataManager.getStudyMetaData(connection, user);
        } catch (MacawException exception) {
            log.logException(exception);
            return null;
        }
    }

    private void checkValidUser(User user) throws MacawException {
        securityValidationService.validateUser(user);
    }

    private void checkValidAdministrator(User user) throws MacawException {
        securityValidationService.validateAdministrator(user);
    }
}
