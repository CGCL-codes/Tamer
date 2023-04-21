package name.angoca.db2sa.interfaze;

import name.angoca.db2sa.AbstractDB2SAException;
import name.angoca.db2sa.ParameterNullException;
import name.angoca.db2sa.core.lexical.impl.ImplementationLexicalAnalyzer;
import name.angoca.db2sa.interfaze.model.ReturnOptions;

/**
 * This class is the interface with other upper layers. This is the only class
 * that the other layers have to use.
 * <p>
 * <b>Control Version</b>
 * <p>
 * <ul>
 * <li>0.0.1 Class creation.</li>
 * <li>0.0.2</li>
 * <li>0.1.0 Organized.</li>
 * <li>0.1.1 Executer as a singleton.</li>
 * <li>0.2.0 Execution state.</li>
 * <li>0.2.1 Enum.</li>
 * <li>1.0.0 Moved to version 1.</li>
 * <li>1.0.1 Validate parameters.</li>
 * <li>1.0.2 Fix a problem when raising exception.</li>
 * <li>1.0.3 Executer modified.</li>
 * <li>1.0.4 Strings externalized.</li>
 * <li>1.0.5 Assert.</li>
 * <li>1.1.0 Execute method deleted.</li>
 * </ul>
 * 
 * @author Andres Gomez Casanova <a
 *         href="mailto:a n g o c a at y a h o o dot c o m">(AngocA)</a>
 * @version 1.1.0 2009-09-30
 */
public final class InterfaceCore {

    /**
     * Process the phrase written by the user and prints its result.
     * 
     * @param phrase
     *            Phrase to analyze.
     * @return An object that represents the command completed or the options
     *         for the current command.
     * @throws AbstractDB2SAException
     *             There is a problem in the application.
     */
    public static ReturnOptions analyzePhrase(final String phrase) throws AbstractDB2SAException {
        if (phrase == null) {
            throw new ParameterNullException("phrase");
        }
        final ReturnOptions ret = ImplementationLexicalAnalyzer.getInstance().processPhrase(phrase);
        assert ret != null;
        return ret;
    }

    /**
     * Hidden default constructor.
     */
    private InterfaceCore() {
    }
}
