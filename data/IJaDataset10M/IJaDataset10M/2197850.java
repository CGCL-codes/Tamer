package weka.gui.scripting.event;

import weka.gui.scripting.Script;
import java.util.EventObject;

/**
 * Event that gets sent when a script is executed.
 * 
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision: 5142 $
 */
public class ScriptExecutionEvent extends EventObject {

    /** for serialization. */
    private static final long serialVersionUID = -8357216611114356632L;

    /**
   * Defines the type of event.
   * 
   * @author  fracpete (fracpete at waikato dot ac dot nz)
   * @version $Revision: 5142 $
   */
    public enum Type {

        /** started execution. */
        STARTED, /** finished normal. */
        FINISHED, /** finished with error. */
        ERROR, /** got stopped by user. */
        STOPPED
    }

    /** the type of event. */
    protected Type m_Type;

    /** optional additional information. */
    protected Object m_Additional;

    /**
   * Initializes the event.
   * 
   * @param source	the script that triggered the event
   * @param type	the type of finish
   */
    public ScriptExecutionEvent(Script source, Type type) {
        this(source, type, null);
    }

    /**
   * Initializes the event.
   * 
   * @param source	the script that triggered the event
   * @param type	the type of finish
   * @param additional	additional information, can be null
   */
    public ScriptExecutionEvent(Script source, Type type, Object additional) {
        super(source);
        m_Type = type;
        m_Additional = additional;
    }

    /**
   * Returns the script that triggered the event.
   * 
   * @return		the script
   */
    public Script getScript() {
        return (Script) getSource();
    }

    /**
   * Returns the type of event.
   * 
   * @return		the type
   */
    public Type getType() {
        return m_Type;
    }

    /**
   * Returns whether additional information is available.
   * 
   * @return		true if additional information is available
   * @see		#getAdditional()
   */
    public boolean hasAdditional() {
        return (m_Additional != null);
    }

    /**
   * Returns the additional information.
   * 
   * @return		the additional information, can be null
   */
    public Object getAdditional() {
        return m_Additional;
    }
}
