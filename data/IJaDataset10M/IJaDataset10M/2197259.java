package it.haefelinger.flaka;

import it.haefelinger.flaka.el.EL;
import it.haefelinger.flaka.util.Groovenizer;
import it.haefelinger.flaka.util.GroovenizerFactory;
import it.haefelinger.flaka.util.Static;
import it.haefelinger.flaka.util.TextReader;
import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

/**
 * Load EL functions into EL instance.
 * 
 * @author merzedes
 * @since 1.3
 * 
 */
public class ELLoad extends Task {

    protected String ns;

    protected String text;

    protected String type = "text/groovy";

    /**
   * Define how to interpret the (inlined) text.
   * 
   * type | meaning class | text is a list of classes, one per line. groovy |
   * text is inlined groovy source code
   * 
   * @param type
   */
    public void setType(String type) {
        this.type = type;
    }

    public void setNS(String ns) {
        this.ns = ns;
    }

    /**
   * Experimental feature for adding inline functions.
   * 
   * @param text
   */
    public void addText(String text) {
        this.text = text;
    }

    public void execute() throws BuildException {
        Project project;
        project = this.getProject();
        try {
            if (this.type.matches("(?i)\\s*file/class\\s*")) {
                EL el = Static.el(project);
                String line;
                TextReader tr = new TextReader();
                tr.setText(this.text);
                while ((line = tr.readLine()) != null) {
                    line = project.replaceProperties(line);
                    line = Static.elresolve(project, line);
                    el.sourceFunctions(this.ns, Static.trim2(line, null));
                }
            }
            if (this.type.matches("(?i)\\s*file/groovy\\s*")) {
                EL el = Static.el(project);
                Class cz;
                String line;
                TextReader tr = new TextReader();
                tr.setText(this.text);
                while ((line = tr.readLine()) != null) {
                    line = project.replaceProperties(line);
                    line = Static.elresolve(project, line);
                    Groovenizer grvnzr = GroovenizerFactory.newInstance();
                    cz = grvnzr.parse(new File(Static.trim2(line, "")));
                    el.sourceFunctions(this.ns, cz);
                }
            }
            if (this.text != null && this.type.matches("(?i)\\s*text/groovy\\s*")) {
                Class cz;
                EL el;
                String text;
                text = project.replaceProperties(this.text);
                text = Static.elresolve(project, text);
                el = Static.el(project);
                if (el != null) {
                    Groovenizer grvnzr = GroovenizerFactory.newInstance();
                    cz = grvnzr.parse(text);
                    el.sourceFunctions(this.ns, cz);
                }
            }
        } catch (SecurityException e) {
            throw new BuildException(e);
        } catch (ClassNotFoundException e) {
            throw new BuildException(e);
        } catch (Exception e) {
            throw new BuildException(e);
        }
    }
}
