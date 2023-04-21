package net.sf.komodo.core.blackboard;

import java.util.List;

/**
 * The Blackboard hides the interfaces used by Komodo to
 * persist AST nodes, Symbol Table nodes, derived facts, validation rules, and
 * other basic information about a project or the system.
 * 
 */
public interface Blackboard {

    /**
	 * Start a transaction within the DAO. Each thread can only have a single
	 * execution open at any given time.
	 * 
	 * @return a handle for the transaction
	 * @throws BlackboardException
	 *             if the transaction could not be created. see the individual
	 *             exception for a specific cause
	 */
    public BlackboardTransaction startTransaction() throws BlackboardException;

    /**
	 * Commit a transaction.
	 * 
	 * @param transaction
	 *            the handle of the transaction to commit.
	 * @throws BlackboardException
	 *             if the transaction could not be committed.
	 */
    public void commitTransaction(BlackboardTransaction transaction) throws BlackboardException;

    /**
	 * Open the Scoreboard. This may create a new scoreboard, or open a new
	 * scoreboard, depending on the situation and implementation. Additionally,
	 * the user name and password may or may not be required by the Scoreboard.
	 * 
	 * @param username
	 *            the name of the user accessing the Scoreboard
	 * @param password
	 *            the password of the user accessing the Scoreboard
	 * @throws BlackboardException
	 *             if the Scoreboard could not be opened. See the specific error
	 *             message for more details
	 */
    public void open(String username, String password) throws BlackboardException;

    /**
	 * Close the Scoreboard. Further use of the instance will result in an
	 * error.
	 * 
	 * @throws BlackboardException
	 *             if the Scoreboard could not be closed. See the specific
	 *             implementation and exception for more details
	 */
    public void close() throws BlackboardException;

    /**
	 * Get a project from the Scoreboard by the name of the project.
	 * @param projectName the name of the project
	 * @return the Project, or null if it does not exist
	 * @throws BlackboardException if there was a system error when reading from the Scoreboard
	 */
    public Project getProject(String projectName) throws BlackboardException;

    /**
	 * Save the project into the Scoreboard
	 * @param project the project to save
	 * @throws BlackboardException if there was a system error when saving to the Scoreboard
	 */
    public void saveProject(Project project) throws BlackboardException;

    /**
	 * Delete a project from the Scoreboard along with all related data.
	 * @param project The project to delete from the Scoreboard
	 * @throws BlackboardException if there was a system error when deleting the Project
	 */
    public void deleteProject(String projectName) throws BlackboardException;

    /**
	 * Clear the Scoreboard of all project information.
	 * @throws BlackboardException if there was an error when clearing the Project
	 */
    public void clear() throws BlackboardException;

    /**
	 * Get the list of files associated with a Project
	 * @param project the project to find files for
	 * @return a list, possibly empty, of the files within the project
	 * @throws BlackboardException if there was a system error when reading from the Scoreboard
	 */
    public List<SourceFile> getFiles(Project project) throws BlackboardException;

    /**
	 * Get the content of the SourceFile.  The content for a SourceFile will not exist until 
	 * it has been read in for the first time; in otherwords if no Plugin looks at a file the
	 * content will not be stored in the Scoreboard.	
	 * @param file the SourceFile to get the content for.
	 * @return the content of the SourceFile as bytes, or null if the content is not in the Scoreboard
	 * @throws BlackboardException if an exception occurs while attempting to retrieve the content from the Scoreboard
	 */
    public byte[] getSourceFileContent(SourceFile file) throws BlackboardException;

    /**
	 * Set the content for the SourceFile $file.
	 * @param file the sourcefile the content is generated for
	 * @param content the content of the source file
	 * @throws BlackboardException if the content could not be saved into the Scoreboard
	 */
    public void setSourceFileContent(SourceFile file, byte[] content) throws BlackboardException;

    /**
	 * Get an AST node by the id.
	 * @param id the id of the node
	 * @return the node with id $id, or null if that node did not exist
	 * @throws BlackboardException if there was a system error when reading the AST
	 */
    public AstNode getAST(int id) throws BlackboardException;

    /**
	 * Get the ASTs for a file.
	 * @param file the file to get all of the ASTs for
	 * @throws BlackboardException if there was a system error when reading the AST
	 */
    public List<AstNode> getASTs(SourceFile file) throws BlackboardException;

    /**
	 * Get all of the ASTs for a given language from the Project.
	 * @param project
	 * @param languages
	 * @return
	 * @throws BlackboardException
	 */
    public List<AstNode> getAsts(Project project, List<String> languages) throws BlackboardException;

    /**
	 * Get all of the ASTs from a project.
	 * @param project the project to get all of the ASTs for
	 * @return 
	 * @throws BlackboardException
	 */
    public List<AstNode> getASTs(Project project) throws BlackboardException;

    /**
	 * Save an AST node to the Scoreboard
	 * @param node the AST node to save
	 * @throws BlackboardException if there was a system error when saving the AST
	 */
    public void saveAst(AstNode node) throws BlackboardException;

    /**
	 * Attach an AST node to a ProjectFile within the Scoreboard
	 * @param project the project that the file belongs to
	 * @param file the ProjectFile to attach the AST node to
	 * @param node the node to attach
	 * @throws BlackboardException if there was a system error when saving the AST
	 */
    public void attachAST(Project project, SourceFile file, AstNode node) throws BlackboardException;

    /**
	 * Add SourceFiles to a project so that tasks can use or affect them.
	 * @param project the project to add the SourceFiles to
	 * @param files the files to add to the project
	 * @throws BlackboardException
	 */
    public void addSourceFiles(Project project, List<SourceFile> files) throws BlackboardException;

    /**
	 * Determine if this Scoreboard can have SQL queries executed against it
	 * @return true if the Scoreboard can have SQL executed against it
	 */
    public boolean isQueryable() throws BlackboardException;

    /**
	 * Get all of the facts for a project, regardless of type or severity.
	 * @param project
	 * @return
	 */
    public List<Fact> getAllFacts(Project project) throws BlackboardException;

    /**
	 * Get all of the facts of a particular type from a project.
	 * @param project the project to get facts for
	 * @param factType the type of fact to get
	 * @throws BlackboardException if there was a system error when reading the ASTs
	 * @return a list, possibly empty, of all of the facts of type $factType
	 */
    public List<Fact> getAllFacts(Project project, FactType factType) throws BlackboardException;

    /**
	 * Get the file that an AST node is attached to.
	 * @param node an AST node
	 * @return the file that this node was parsed or derived from
	 */
    public SourceFile getFile(AstNode node) throws BlackboardException;

    /**
	 * Save a list of Facts against a project.
	 * @param project
	 * @param facts
	 */
    public void saveFacts(Project project, List<Fact> facts) throws BlackboardException;

    /**
	 * Create a new GeneratedFile in a Project.
	 * @param project the Project to create the GeneratedFile in
	 * @param file the file to create
	 * @throws BlackboardException if an internal error occurred.  See the error message and implementation documentation
	 *                             for more details.
	 */
    public void createGeneratedFile(Project project, GeneratedTextFile file) throws BlackboardException;

    /**
	 * Save the changes to a GeneratedFile in the Project to the Scoreboard.
	 * @param project the project the GeneratedFile belongs to
	 * @param file the GeneratedFile to save
	 * @throws BlackboardException if an internal error occurred.  See the error message and implementation documentation
	 *                             for more details.
	 */
    public void saveGeneratedFile(Project project, GeneratedTextFile file) throws BlackboardException;

    /**
	 * Delete a GeneratedFile in a Project from the Scoreboard.
	 * @param project the project the GeneratedFile belongs to
	 * @param file the GeneratedFile to save
	 * @throws BlackboardException if an internal error occurred.  See the error message and implementation documentation
	 *                             for more details.
	 */
    public void deleteGeneratedFile(Project project, GeneratedTextFile file) throws BlackboardException;

    /**
	 * Get all of the GeneratedFiles for a Project.
	 * @param project the Project to get the GeneratedFiles from
	 * @return a List, possibly empty, of GeneratedFiles
	 * @throws BlackboardException if an internal error occurred.  See the error message and implementation documentation
	 *                             for more details.
	 */
    public List<GeneratedTextFile> getGeneratedFiles(Project project) throws BlackboardException;

    /**
	 * Get all of the GeneratedFiles for a Project in a certain path.
	 * @param project the Project to get the GeneratedFiles from
	 * @param path the path to filter the GeneratedFiles for
	 * @return a List, possibly empty, of GeneratedFiles
	 * @throws BlackboardException if an internal error occurred.  See the error message and implementation documentation
	 *                             for more details.
	 */
    public List<GeneratedTextFile> getGeneratedFiles(Project project, String path) throws BlackboardException;

    /**
	 * Save a Scope into the Project in a certain path.
	 * @param scope
	 */
    public void saveScope(Project project, Scope scope);

    public Scope getScopeForAst(Project project, long astId);

    public void getScopes(Project project, SourceFile sourceFile);

    public Scope getScope(Project project, long scopeId);

    public List<SymbolBinding> getBindingsForAst(Project project, long scopeId);

    public SymbolDeclaration getSymbolDeclaration(Project project, long declarationId);

    public SymbolBinding getSymbolBinding(Project project, long bindingId);
}
