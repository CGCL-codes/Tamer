package org.bungeni.translators.configurations.steps;

import org.bungeni.translators.interfaces.XSLTStep;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * The Step Object is the basic unit of the translations. A configuration contains several steps
 * that are performed sequentially.
 */
public class OAXSLTStep implements XSLTStep {

    private String URI;

    private String name;

    private Integer position;

    private String preProcess;

    private boolean preProcessState = false;

    private String postProcess;

    private boolean postProcessState = false;

    /**
     * Create a new Step object with a given name, href and position.
     * @param aName the name of the Step
     * @param aURI the href of the Step
     * @param aPosition the position of the Step
     */
    public OAXSLTStep(NamedNodeMap attrs) {
        this.name = attrs.getNamedItem("name").getNodeValue();
        this.URI = attrs.getNamedItem("href").getNodeValue();
        this.position = Integer.parseInt(attrs.getNamedItem("step").getNodeValue());
        Node postproc = attrs.getNamedItem("postproc");
        Node preproc = attrs.getNamedItem("preproc");
        if (postproc != null) {
            this.postProcessState = true;
            this.postProcess = postproc.getNodeValue();
        }
        if (preproc != null) {
            this.preProcessState = true;
            this.preProcess = preproc.getNodeValue();
        }
    }

    /**
     * Used to get the name of the Step
     * @return the name of the Step
     */
    public String getName() {
        String aName = this.name;
        return aName;
    }

    /**
     * Used to get the href of the Step
     * @return the href of the Step
     */
    public String getHref() {
        String aHref = this.URI;
        return aHref;
    }

    /**
     * Used to get the position of the Step. The step of the configurations
     * are resolved performing all the Steps of the configurations. The step
     * with the lowest position number is performed firstly.
     * @return the position of the Step
     */
    public Integer getPosition() {
        Integer aPosition = this.position;
        return aPosition;
    }

    public String getPreProc() {
        return this.preProcess;
    }

    public String getPostProc() {
        return this.postProcess;
    }

    public boolean hasPreProc() {
        return this.preProcessState;
    }

    public boolean hasPostProc() {
        return this.postProcessState;
    }
}
