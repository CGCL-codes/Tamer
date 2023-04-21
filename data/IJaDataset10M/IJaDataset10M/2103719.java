package bebop.controller;

import java.util.Vector;
import com.ibm.bsf.BSFManager;
import bebop.model.ModelManager;

/**
 * The StriptController executes a script written in a
 * Bean Scripting Frameowrk supported scripting language.
 *
 * <p>In order for this class to work, the BSF JAR file
 * must be in the CLASSPATH, along with any JAR files for
 * associated BSF-scripting languages.
 *
 * <p>For more info on the BSF, consult the Bean
 * Scripting Framework homepage: 
 * <a href="http://www-124.ibm.com/developerworks/projects/bsf">
 * http://www-124.ibm.com/developerworks/projects/bsf
 * </a>
 *
 */
public class ScriptController extends Controller {

    private String as = "modelManager";

    private String name = "controller";

    private String language = "javascript";

    private String script = null;

    /**
	 * Executes a specific application behavior upon the
	 * Model.  In the ScriptController, execute() evaluates
	 * a BSF-supported script. After the script evaluates,
	 * the result is cast to a String and returned.
	 *
	 *
	 * @param  modelManager the application's Model
	 *
	 * @throws Exception    if the Controller's actions result
	 *                      in an error, an Exception of the
	 *                      appropriate type is thrown.
	 *
	 */
    public String execute(ModelManager modelManager) throws Exception {
        String script = getScript();
        if (script != null) {
            BSFManager bsfMgr = new BSFManager();
            bsfMgr.declareBean(getAs(), modelManager, ModelManager.class);
            Object rc = bsfMgr.eval(getLanguage(), "", 0, 0, script);
            if (rc != null) {
                return rc.toString();
            } else {
                throw new Exception("Controller failed to return a response code.");
            }
        } else {
            throw new Exception("Controller does not define a script.");
        }
    }

    /**
	 * Gets the name of the variable to expose the
	 * ModelManager as. If none is  specified,
	 * 'modelManager' is used as a default.
	 *
	 */
    public String getAs() {
        return as;
    }

    /**
	 * Gets the scripting language that should be used to
	 * evaluate the script. If none is  specified,
	 * 'javascript' is used as a default.
	 *
	 */
    public String getLanguage() {
        return language;
    }

    /**
	 * Gets the String script to be evaluated.
	 *
	 */
    public String getScript() {
        return script;
    }

    /**
	 * Sets the name of the variable to expose the
	 * ModelManager as.
	 *
	 */
    public void setAs(String as) {
        if (as != null) {
            this.as = as;
        }
    }

    /**
	 * Sets the scripting language that should be used to
	 * evaluate the script.
	 *
	 */
    public void setLanguage(String language) {
        if (language != null) {
            this.language = language;
        }
    }

    /**
	 * Sets the String script to be evaluated.
	 *
	 */
    public void setScript(String script) {
        this.script = script;
    }
}
