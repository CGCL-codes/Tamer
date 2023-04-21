package com.erinors.tapestry.tapdoc.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.apache.hivemind.Resource;

/**
 * Represents a Tapestry component.
 * 
 * @author Norbert Sándor
 */
public final class Component implements Serializable {

    private static final long serialVersionUID = 6364149630969526661L;

    private boolean allowBody;

    public boolean getAllowBody() {
        return allowBody;
    }

    public void setAllowBody(boolean allowBody) {
        this.allowBody = allowBody;
    }

    private boolean allowInformalParameters;

    public boolean getAllowInformalParameters() {
        return allowInformalParameters;
    }

    public void setAllowInformalParameters(boolean allowInformalParameters) {
        this.allowInformalParameters = allowInformalParameters;
    }

    private String componentClassName;

    public String getComponentClassName() {
        return componentClassName;
    }

    public void setComponentClassName(String componentClassName) {
        this.componentClassName = componentClassName;
    }

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String htmlAddon;

    public String getHtmlAddon() {
        return htmlAddon;
    }

    public void setHtmlAddon(String htmlAddon) {
        this.htmlAddon = htmlAddon;
    }

    private String liveDemoUrl;

    public String getLiveDemoUrl() {
        return liveDemoUrl;
    }

    public void setLiveDemoUrl(String liveDemoUrl) {
        this.liveDemoUrl = liveDemoUrl;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private Set<Parameter> parameters = new HashSet<Parameter>();

    public Set<Parameter> getParameters() {
        return parameters;
    }

    private Set<String> reservedParameters = new HashSet<String>();

    public Set<String> getReservedParameters() {
        return reservedParameters;
    }

    private Set<String> seeAlsoClasses = new HashSet<String>();

    public Set<String> getSeeAlsoClasses() {
        return seeAlsoClasses;
    }

    public void addSeeAlsoClass(String className) {
        seeAlsoClasses.add(className);
    }

    private Set<String> seeAlsoComponents = new HashSet<String>();

    public Set<String> getSeeAlsoComponents() {
        return seeAlsoComponents;
    }

    public void addSeeAlsoComponent(String component) {
        seeAlsoComponents.add(component);
    }

    private Set<String> seeAlsoLinks = new HashSet<String>();

    public Set<String> getSeeAlsoLinks() {
        return seeAlsoLinks;
    }

    public void addSeeAlsoLink(String link) {
        seeAlsoLinks.add(link);
    }

    public boolean getHasSeeAlsoReferences() {
        return getSeeAlsoClasses().size() > 0 || getSeeAlsoComponents().size() > 0 || getSeeAlsoLinks().size() > 0;
    }

    private String visualSample;

    public String getVisualSample() {
        return visualSample;
    }

    public void setVisualSample(String visualSample) {
        this.visualSample = visualSample;
    }

    /**
     * Enumeration of the possible component visualities.
     */
    public enum Visuality {

        Unknown, Visual, VisualOrNonvisual, Nonvisual
    }

    ;

    private Visuality visuality = Visuality.Unknown;

    public Visuality getVisuality() {
        return visuality;
    }

    public void setVisuality(Visuality visuality) {
        this.visuality = visuality;
    }

    public void setVisual(String visualSample) {
        setVisuality(Visuality.Visual);
        setVisualSample(visualSample);
    }

    public void setVisualOrNonvisual(String visualSample) {
        setVisuality(Visuality.VisualOrNonvisual);
        setVisualSample(visualSample);
    }

    public void setNonvisual() {
        setVisuality(Visuality.Nonvisual);
    }

    private boolean deprecated;

    public boolean isDeprecated() {
        return deprecated;
    }

    public void setDeprecated(boolean deprecated) {
        this.deprecated = deprecated;
    }

    private boolean global;

    public boolean isGlobal() {
        return global;
    }

    public void setGlobal(boolean global) {
        this.global = global;
    }

    private Resource specificationLocation;

    public Resource getSpecificationLocation() {
        return specificationLocation;
    }

    public void setSpecificationLocation(Resource specificationLocation) {
        this.specificationLocation = specificationLocation;
    }
}
