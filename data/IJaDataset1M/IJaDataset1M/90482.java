package jade.jademx.config.jademx.onto;

import jade.content.Concept;
import jade.util.leap.Iterator;
import jade.util.leap.LinkedList;
import jade.util.leap.List;

/**
 * Class to represent an argument to an agent in the JademxConfigOntology.
 * @author David Bernstein, <a href="http://www.caboodlenetworks.com"
 *  >Caboodle Networks, Inc.</a>
 */
public class ConfigRuntime implements Concept {

    /** list of options of type String */
    private List platforms = null;

    /**
     * Constructor 
     */
    public ConfigRuntime() {
    }

    /**
     * add one platform at a time
     * @param platform platform to add
     */
    public void addPlatform(ConfigPlatform platform) {
        if (null == platforms) {
            platforms = new LinkedList();
        }
        platforms.add(platform);
    }

    /**
     * @return Returns the options.
     */
    public List getPlatforms() {
        return platforms;
    }

    /**
     * @param options The options to set.
     */
    public void setPlatforms(List options) {
        this.platforms = options;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Runtime[");
        if (null != platforms) {
            Iterator optionI = platforms.iterator();
            boolean first = true;
            while (optionI.hasNext()) {
                if (first) {
                    first = false;
                } else {
                    sb.append(",");
                }
                sb.append(optionI.next().toString());
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
