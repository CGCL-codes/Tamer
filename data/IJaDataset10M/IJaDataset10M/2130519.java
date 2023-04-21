package edu.rice.cs.drjava.model.compiler;

import java.io.IOException;
import java.util.List;
import edu.rice.cs.drjava.model.OpenDefinitionsDocument;

/** Interface for all compiler functionality in the model.  The compilation process itself can be monitored through
  * the CompilerListener interface.  The four primary uses of this interface will be to manage listeners, to trigger
  * compilation of (a) document(s), to handle the results, and to manage available compilers.
  * 
  * @version $Id: CompilerModel.java 5379 2010-08-27 02:53:50Z mgricken $
  */
public interface CompilerModel {

    /** Returns the lock used to prevent simultaneous compilation and JUnit testing */
    public Object getCompilerLock();

    /** Add a CompilerListener to the model.
    * @param listener a listener that reacts to compiler events
    */
    public void addListener(CompilerListener listener);

    /** Remove a CompilerListener from the model.  If the listener is not currently listening to this model, this method
    * has no effect.
    * @param listener a listener that reacts to compiler events
    */
    public void removeListener(CompilerListener listener);

    /** Removes all CompilerListeners from this model. */
    public void removeAllListeners();

    /** Compiles all documents, which requires that the documents be saved first.
    * @throws IOException if a filesystem-related problem prevents compilation
    */
    public void compileAll() throws IOException;

    /** Compiles all documents in the project source tree, which requires that the documents be saved first.
    * @throws IOException if a filesystem-related problem prevents compilation
    */
    public void compileProject() throws IOException;

    /** Compiles the specified documents which must be saved first.
    * @param docs the documents to be compiled
    * @throws IOException if a filesystem-related problem prevents compilation
    */
    public void compile(List<OpenDefinitionsDocument> docs) throws IOException;

    /** Compiles a single document which must be saved first.
    * @param doc the document to be compiled
    * @throws IOException if a filesystem-related problem prevents compilation
    */
    public void compile(OpenDefinitionsDocument doc) throws IOException;

    /** Gets the CompilerErrorModel representing the last compile. */
    public CompilerErrorModel getCompilerErrorModel();

    /** Gets the total number of current errors. */
    public int getNumErrors();

    /** Resets the compiler error state to have no errors. */
    public void resetCompilerErrors();

    /** Returns all registered compilers that are actually available.  If there are none,
   * the result is {@link NoCompilerAvailable#ONLY}.
   */
    public Iterable<CompilerInterface> getAvailableCompilers();

    /** Gets the compiler that is the "active" compiler.
   *
   * @see #setActiveCompiler
   */
    public CompilerInterface getActiveCompiler();

    /** Sets which compiler is the "active" compiler.
    *
    * @param compiler Compiler to set active.
    * @throws IllegalArgumentException  If the compiler is not in the list of available compilers
    *
    * @see #getActiveCompiler
    */
    public void setActiveCompiler(CompilerInterface compiler);

    /** Add a compiler to the active list */
    public void addCompiler(CompilerInterface compiler);

    /** Gets the LanguageLevelStackTraceMapper from the model */
    public LanguageLevelStackTraceMapper getLLSTM();
}
