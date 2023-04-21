package macaw.businessLayer;

import java.util.ArrayList;
import macaw.io.VariableFileFormat;
import macaw.system.MacawChangeEvent;
import macaw.system.MacawException;

/**
 * Macaw is a data curation application that stores and retrieves its data via methods of this interface.  
 * MacawCurationAPI is designed to hide much of the details about data persistence from the GUI
 * code. 
 * <p>
 * Parameters for many of the API methods come from data container classes that are defined in the Macaw Data Model.  The concepts
 * such as {@link macaw.businessLayer.Variable}, {@link macaw.businessLayer.RawVariable}, {@link macaw.businessLayer.DerivedVariable}, {@link macaw.businessLayer.Category}, and {@link macaw.businessLayer.ValueLabel} represent ideas related to the domain
 * addressed by the NSHD archive.  The application packages form data into instances of these classes and submits them to an implementation
 * of MacawDatabase.  
 * <p>
 * Macaw anticipates three main implementations:
 * <ul>
 * <li>a Production SQL Database (see {@link macaw.persistenceLayer.production.ProductionCurationService} </li>
 * <li>an in-memory database ( see {@link macaw.persistenceLayer.demo.DemonstrationCurationService} </li>
 * <li>a Test SQL Database, which may be either one of the other two, but loaded with data that is intended to be used to test methods more than demonstrate features.</li>
 * </ul>
 * 
 * The {@link macaw.persistenceLayer.production.ProductionCurationService} will be the SQL database that holds the data about all NSHD variables that will
 * be advertised using SWIFT.  A Test SQL database will be created which uses the same relational schema but the records
 * it contains will be designed to provide a minimal test data set that can be used in automated testing
 * efforts.
 * 
 * <p>
 * The API has been designed to anticipate key operations that would be involved with retrieving or committing
 * changes to a relational database.  However, the abstraction is also designed to permit the development of a {@link macaw.persistenceLayer.demo.DemonstrationCurationService}
 * that is managed entirely in-memory using collections of data objects instantiated from the Application Model.
 * The 'database' is designed to hold a minimal number of records that can demonstrate to domain users the features
 * provided by Macaw.  The in-memory database is used to allow the LHA to run a demonstration version of
 * the application on a memory stick without the need for MySQL to be installed on a client machine and without relying
 * on a database filled with sensitive data about case studies.
 *
 * <hr>
 * Copyright 2011 Medical Research Council Unit for Lifelong Health and Ageing
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.  
 * <hr>
 *
 * @author Kevin Garwood (kgarwood@users.sourceforge.net)
 * @version 1.0	
 */
public interface MacawCurationAPI {

    public void addRawVariable(User user, RawVariable rawVariable) throws MacawException;

    public void deleteRawVariables(User user, ArrayList<RawVariable> rawVariables) throws MacawException;

    public void updateRawVariable(User user, RawVariable rawVariable) throws MacawException;

    public int getRawVariableIdentifier(User user, RawVariable rawVariable) throws MacawException;

    public void addDerivedVariable(User user, DerivedVariable derivedVariable) throws MacawException;

    public void deleteDerivedVariables(User user, ArrayList<DerivedVariable> derivedVariables) throws MacawException;

    public void updateDerivedVariable(User user, DerivedVariable derivedVariable) throws MacawException;

    public int getDerivedVariableIdentifier(User user, DerivedVariable derivedVariable) throws MacawException;

    public Variable getVariable(User user, String variableName) throws MacawException;

    public ArrayList<ValueLabel> getValueLabels(User user, Variable variable) throws MacawException;

    public void addValueLabels(User user, Variable variable, ArrayList<ValueLabel> valueLabels) throws MacawException;

    public void updateValueLabels(User user, Variable variable, ArrayList<ValueLabel> valueLabels) throws MacawException;

    public void deleteValueLabels(User user, Variable variable, ArrayList<ValueLabel> valueLabels) throws MacawException;

    public int getValueLabelIdentifier(User user, Variable variable, ValueLabel valueLabel) throws MacawException;

    public ArrayList<SupportingDocument> getAllSupportingDocuments(User user) throws MacawException;

    public ArrayList<SupportingDocument> getSupportingDocuments(User user, Variable variable) throws MacawException;

    public void addSupportingDocument(User user, SupportingDocument supportingDocument) throws MacawException;

    public void updateSupportingDocument(User user, SupportingDocument supportingDocument) throws MacawException;

    public void deleteSupportingDocuments(User user, ArrayList<SupportingDocument> supportingDocuments) throws MacawException;

    public int getSupportingDocumentIdentifier(User user, SupportingDocument supportingDocument) throws MacawException;

    public void associateSupportingDocumentsWithVariable(User user, Variable variable, ArrayList<SupportingDocument> supportingDocument) throws MacawException;

    public void disassociateSupportingDocumentsFromVariable(User user, Variable variable, ArrayList<SupportingDocument> supportingDocuments) throws MacawException;

    public ArrayList<Variable> getSourceVariables(User user, DerivedVariable derivedVariable) throws MacawException;

    public void associateSourceVariables(User user, DerivedVariable derivedVariable, ArrayList<Variable> sourceVariablesToAdd) throws MacawException;

    /**
	 * removes references to either {@link macaw.businessLayer.RawVariable} or {@link macaw.businessLayer.DerivedVariable} variables that
	 * were used to create a given derivedVariable
	 * @param user
	 * @param derivedVariable contains source variables to delete
	 * @param sourceVariablesToDelete the raw or derived variables that should no longer be associated with <code>derivedVariable</code>.
	 * @throws MacawException
	 */
    public void disassociateSourceVariables(User user, DerivedVariable derivedVariable, ArrayList<Variable> sourceVariablesToDelete) throws MacawException;

    /**
	 * retrieves a collection of unique years associated with at least one variable in the data repository.
	 * @param user
	 * @throws MacawException
	 */
    public String[] getStudyYears(User user) throws MacawException;

    /**
	 * retrieves a collection of all variables managed by the system.  These variables will include 
	 * instances of {@link macaw.businessLayer.RawVariable}s and {@link macaw.businessLayer.DerivedVariable}.  Note
	 * that only summary data are included:  
	 * @param user
	 * @throws MacawException
	 */
    public ArrayList<VariableSummary> getSummaryDataForAllVariables(User user) throws MacawException;

    public Variable getCompleteVariableData(User user, VariableSummary variableSummary) throws MacawException;

    public ArrayList<VariableSummary> filterVariableSummaries(User user, String searchText, String year, String category, VariableTypeFilter variableTypeFilter) throws MacawException;

    public ArrayList<SupportingDocument> filterSupportingDocuments(User user, String documentTitle, String documentCode) throws MacawException;

    public ArrayList<OntologyTerm> filterOntologyTerms(User user, String term, String description) throws MacawException;

    /**
	 * 
	 * @param user
	 * @return a collection of categories used to classify NSHD variables.  eg: "General Health" and
	 * "Education".
	 * @throws MacawException
	 */
    public ArrayList<Category> getCategories(User user) throws MacawException;

    public void addCategory(User user, Category category) throws MacawException;

    public void updateCategory(User user, Category category) throws MacawException;

    public void deleteCategories(User user, ArrayList<Category> categories) throws MacawException;

    public int getCategoryIdentifier(User user, Variable variable, Category category) throws MacawException;

    public ArrayList<CleaningState> getCleaningStates(User user) throws MacawException;

    public void addCleaningState(User user, CleaningState cleaningState) throws MacawException;

    public void updateCleaningState(User user, CleaningState cleaningState) throws MacawException;

    public void deleteCleaningStates(User user, ArrayList<CleaningState> cleaningStates) throws MacawException;

    public int getCleaningStateIdentifier(User user, Variable variable, CleaningState cleaningState) throws MacawException;

    public ArrayList<DataSensitivityLevel> getDataSensitivityLevels(User user) throws MacawException;

    public void addDataSensitivityLevel(User user, DataSensitivityLevel dataSensitivityLevel) throws MacawException;

    public void updateDataSensitivityLevel(User user, DataSensitivityLevel dataSensitivityLevel) throws MacawException;

    public void deleteDataSensitivityLevels(User user, ArrayList<DataSensitivityLevel> dataSensitivityLevels) throws MacawException;

    public int getDataSensitivityLevelIdentifier(User user, Variable variable, DataSensitivityLevel dataSensitivityLevel) throws MacawException;

    public ArrayList<MetaDataSensitivityLevel> getMetaDataSensitivityLevels(User user) throws MacawException;

    public void addMetaDataSensitivityLevel(User user, MetaDataSensitivityLevel metaDataSensitivityLevel) throws MacawException;

    public void updateMetaDataSensitivityLevel(User user, MetaDataSensitivityLevel metaDataSensitivityLevel) throws MacawException;

    public void deleteMetaDataSensitivityLevels(User user, ArrayList<MetaDataSensitivityLevel> metaDataSensitivityLevels) throws MacawException;

    public int getMetaDataSensitivityLevelIdentifier(User user, Variable variable, MetaDataSensitivityLevel metaDataSensitivityLevel) throws MacawException;

    public ArrayList<AliasFilePath> getAliasFilePaths(User user) throws MacawException;

    public void addAliasFilePath(User user, AliasFilePath aliasFilePath) throws MacawException;

    public void updateAliasFilePath(User user, AliasFilePath aliasFilePath) throws MacawException;

    public void deleteAliasFilePaths(User user, ArrayList<AliasFilePath> aliasFilePaths) throws MacawException;

    public int getAliasFilePathIdentifier(User user, Variable variable, AliasFilePath aliasFilePath) throws MacawException;

    public String getFilePathFromAlias(User user, String currentAlias) throws MacawException;

    public ArrayList<OntologyTerm> getAllOntologyTerms(User user) throws MacawException;

    public ArrayList<OntologyTerm> getOntologyTerms(User user, Variable variable) throws MacawException;

    public void addOntologyTerm(User user, OntologyTerm ontologyTerm) throws MacawException;

    public void updateOntologyTerm(User user, OntologyTerm ontologyTerm) throws MacawException;

    public void deleteOntologyTerms(User user, ArrayList<OntologyTerm> ontologyTerms) throws MacawException;

    public int getOntologyTermIdentifier(User user, OntologyTerm ontologyTerm) throws MacawException;

    public void associateOntologyTermsWithVariable(User user, Variable variable, ArrayList<OntologyTerm> ontologyTerms) throws MacawException;

    public void disassociateOntologyTermsFromVariable(User user, Variable variable, ArrayList<OntologyTerm> ontologyTerms) throws MacawException;

    public ArrayList<MacawChangeEvent> getChangeHistoryForVariable(User user, Variable variable) throws MacawException;

    public ArrayList<MacawChangeEvent> getChangeHistoryForSupportingDocument(User user, SupportingDocument supportingDocument) throws MacawException;

    public ArrayList<MacawChangeEvent> getChangeHistoryForValueLabels(User user, Variable variable) throws MacawException;

    public ArrayList<MacawChangeEvent> getChangeHistoryForListChoices(User user) throws MacawException;

    public ArrayList<MacawChangeEvent> getChangeHistoryByUser(User user) throws MacawException;

    public ArrayList<User> getUsers(User admin) throws MacawException;

    public void addUser(User admin, User user) throws MacawException;

    public void updateUser(User admin, User user) throws MacawException;

    public void deleteUsers(User admin, ArrayList<User> usersToDelete) throws MacawException;

    public int getUserIdentifier(User admin, User user) throws MacawException;

    public User getUserFromID(User admin, String userID) throws MacawException;

    public boolean isAdministrator(User admin) throws MacawException;

    public void checkValidUser(User user) throws MacawException;

    public void checkValidAdministrator(User administrator) throws MacawException;

    public void clear(User admin) throws MacawException;

    public void clearAllChanges(User admin) throws MacawException;

    public ArrayList<MacawChangeEvent> getAllChanges(User admin) throws MacawException;

    public Variable getOriginalVariable(User user, Variable variable) throws MacawException;

    public Variable getAlternativeVariable(User user, Variable targetVariable) throws MacawException;

    public void setAlternativeVariable(User user, Variable targetVariable, Variable alternativeVariable) throws MacawException;

    public Basket getMyVariablesBasket(User user) throws MacawException;

    public void addBasket(User user, Basket basket) throws MacawException;

    public void deleteBasket(User user, Basket basket) throws MacawException;

    public void updateBasket(User user, Basket basket) throws MacawException;

    public ArrayList<BasketVariableReference> addBasketVariableReferences(User user, Basket basket, ArrayList<BasketVariableReference> basketVariableReferences) throws MacawException;

    public void updateBasketVariableReferences(User user, Basket basket, ArrayList<BasketVariableReference> basketVariableReferences) throws MacawException;

    public void deleteBasketVariableReferences(User user, Basket basket, ArrayList<BasketVariableReference> basketVariableReferences) throws MacawException;

    public Basket getBasket(User user, String basketName) throws MacawException;

    public String getBasketXML(User user, Basket basket, VariableFileFormat format) throws MacawException;

    public ArrayList<MacawChangeEvent> getChangeHistoryByBasket(User user, String basketName) throws MacawException;

    public ArrayList<Basket> getBaskets(User user) throws MacawException;

    public StudyMetaData getStudyMetaData(User user) throws MacawException;

    public void setStudyMetaData(User user, StudyMetaData studyMetaData) throws MacawException;

    public void addPhaseMetaData(User user, PhaseMetaData phaseMetaData) throws MacawException;

    public void updatePhaseMetaData(User user, PhaseMetaData phaseMetaData) throws MacawException;

    public void deletePhaseMetaData(User user, ArrayList<PhaseMetaData> phaseMetaDataRecords) throws MacawException;

    public ArrayList<PhaseMetaData> getAllPhaseMetaData(User user) throws MacawException;

    public int getPhaseMetaDataIdentifier(User user, PhaseMetaData phaseMetaData) throws MacawException;

    public void associatePhaseMetaDataWithVariable(User user, Variable variable, ArrayList<PhaseMetaData> phaseMetaDataRecords) throws MacawException;

    public void disassociatePhaseMetaDataFromVariable(User user, Variable variable, ArrayList<PhaseMetaData> phaseMetaDataRecords) throws MacawException;

    public void addDataCollectionEvent(User user, DataCollectionEvent dataCollectionEvent) throws MacawException;

    public void updateDataCollectionEvent(User user, DataCollectionEvent dataCollectionEvent) throws MacawException;

    public void deleteDataCollectionEvents(User user, ArrayList<DataCollectionEvent> dataCollectionEvents) throws MacawException;

    public ArrayList<DataCollectionEvent> getAllDataCollectionEvents(User user) throws MacawException;

    public int getDataCollectionEventIdentifier(User user, DataCollectionEvent dataCollectionEvent) throws MacawException;

    public void massAdd(User user, Variable sampleVariable, ArrayList<Variable> variablesToUpdate) throws MacawException;

    public void massUpdate(User user, Variable sampleVariable, ArrayList<Variable> variablesToUpdate) throws MacawException;
}
