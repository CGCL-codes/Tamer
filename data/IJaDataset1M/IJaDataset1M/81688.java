package net.grinder.scriptengine.jython;

import java.util.ArrayList;
import java.util.List;
import net.grinder.common.GrinderProperties;
import net.grinder.engine.common.EngineException;
import net.grinder.engine.common.ScriptLocation;
import net.grinder.scriptengine.DCRContext;
import net.grinder.scriptengine.Instrumenter;
import net.grinder.scriptengine.ScriptEngineService;
import net.grinder.scriptengine.jython.instrumentation.dcr.Jython22Instrumenter;
import net.grinder.scriptengine.jython.instrumentation.dcr.Jython25Instrumenter;
import net.grinder.scriptengine.jython.instrumentation.traditional.TraditionalJythonInstrumenter;
import net.grinder.util.FileExtensionMatcher;
import net.grinder.util.weave.WeavingException;

/**
 * Jython {@link ScriptEngineService} implementation.
 *
 * @author Philip Aston
 */
public final class JythonScriptEngineService implements ScriptEngineService {

    private final FileExtensionMatcher m_pyFileMatcher = new FileExtensionMatcher(".py");

    private final boolean m_forceDCRInstrumentation;

    private final DCRContext m_dcrContext;

    /**
   * Constructor.
   *
   * @param properties Properties.
   * @param dcrContext DCR context.
   * @param scriptLocation Script location.
   */
    public JythonScriptEngineService(GrinderProperties properties, DCRContext dcrContext, ScriptLocation scriptLocation) {
        m_forceDCRInstrumentation = properties.getBoolean("grinder.dcrinstrumentation", false) || !m_pyFileMatcher.accept(scriptLocation.getFile());
        m_dcrContext = dcrContext;
    }

    /**
   * Constructor used when DCR is unavailable.
   */
    public JythonScriptEngineService() {
        m_dcrContext = null;
        m_forceDCRInstrumentation = false;
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public List<Instrumenter> createInstrumenters() throws EngineException {
        final List<Instrumenter> instrumenters = new ArrayList<Instrumenter>();
        if (!m_forceDCRInstrumentation) {
            try {
                instrumenters.add(new TraditionalJythonInstrumenter());
            } catch (EngineException e) {
            } catch (VerifyError e) {
            }
        }
        if (m_dcrContext != null) {
            if (instrumenters.size() == 0) {
                try {
                    instrumenters.add(new Jython25Instrumenter(m_dcrContext));
                } catch (WeavingException e) {
                    instrumenters.add(new Jython22Instrumenter(m_dcrContext));
                }
            }
        }
        return instrumenters;
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public ScriptEngine createScriptEngine(ScriptLocation script) throws EngineException {
        if (m_pyFileMatcher.accept(script.getFile())) {
            return new JythonScriptEngine(script);
        }
        return null;
    }
}
