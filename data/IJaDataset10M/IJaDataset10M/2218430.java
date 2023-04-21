package com.techm.gisv.cqp.analyzers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import com.techm.gisv.cqp.builders.ResultsBuilder;
import com.techm.gisv.cqp.events.Event;
import com.techm.gisv.cqp.framework.CQPProcessor;
import com.techm.gisv.cqp.framework.CQPProcessor.PatternMatchType;
import com.techm.gisv.cqp.util.Constants;
import com.techm.gisv.cqp.util.PreferencesHelper;

/**
 * This base analyzer class , all the common methos will get
 * implemented here
 * 
 */
public abstract class BaseAnalyzer implements Analyzer {

    /**
     * Gives the status of analyzer supports the subprofiles
     * 
     * @return true/false
     */
    public boolean isSubProfileSupported() {
        return false;
    }

    protected ResultsBuilder resultsBuilder = null;

    protected CQPProcessor processor = null;

    protected IProject project = null;

    protected String id = null;

    protected Map<String, String> attributes = new HashMap<String, String>();

    protected IConfigurationElement[] configExtensions = null;

    /**
     * gets result builder for the analyzer
     * 
     * @return ResultsBuilder analysers result builder
     */
    public ResultsBuilder getResultsBuilder() {
        return this.resultsBuilder;
    }

    /**
     * Gets the analyzers processor
     * 
     * @return CQPProcessor - analyzers processor
     */
    public CQPProcessor getProcessor() {
        return processor;
    }

    /**
     * sets the current analyzers processor
     * 
     * @param processor analyzers processor
     */
    public void setProcessor(CQPProcessor processor) {
        this.processor = processor;
    }

    /**
     * sets the current analyzers result builder
     * 
     * @param resultsBuilder analyzers result builder
     */
    public void setResultsBuilder(ResultsBuilder resultsBuilder) {
        this.resultsBuilder = resultsBuilder;
    }

    /**
     * gets analysers id
     * 
     * @return analyzers id
     */
    public String getId() {
        return id;
    }

    /**
     * sets the current analyzers id
     * 
     * @param id analyzers id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * creates analyzers property page contents
     * 
     * @param project project
     * @param parent Composite
     * @return Control control
     */
    public Control createPropertyPageContents(IProject project, Composite parent) {
        this.project = project;
        return parent;
    }

    /**
     * gets the project for analyzer
     * 
     * @return IProject
     */
    public IProject getProject() {
        return project;
    }

    /**
     * sets the project for analyzer
     * 
     * @param project IProject
     */
    public void setProject(IProject project) {
        this.project = project;
    }

    /**
     * gets the analyzers preference from store
     * 
     * @param key preference key
     * @return preference value
     */
    public String getPreference(String key) {
        return PreferencesHelper.getPreference(project, key);
    }

    /**
     * sets the analyzers preference into the store
     * 
     * @param key preference key
     * @param value preference value
     */
    public void setPreference(String key, String value) {
        PreferencesHelper.setPreference(project, key, value);
    }

    /**
     * gets the selected project profile
     * 
     * @param project IProject
     * @return IConfigurationElement
     */
    public IConfigurationElement getSelectedProfile(IProject project) {
        String id = PreferencesHelper.getPreference(project, getPreferenceId());
        IConfigurationElement profile = null;
        if (id != null && id.trim().length() > 0) {
            for (int i = 0; i < configExtensions.length; i++) {
                if (configExtensions[i].getAttribute(Constants.ID).equals(id)) {
                    profile = configExtensions[i];
                }
            }
        } else {
            profile = configExtensions[0];
        }
        return profile;
    }

    /**
     * gets the project pattern match types for the profiles
     * 
     * @param project IProject
     * @return PatternMatchType
     */
    public PatternMatchType getPatternMatchType(IProject project) {
        return getPatternMatchType(getSelectedProfile(project));
    }

    /**
     * gets the project pattern match type for the preference
     * 
     * @param element IConfigurationElement
     * @return PatternMatchType
     */
    public PatternMatchType getPatternMatchType(IConfigurationElement element) {
        PatternMatchType matchType = PatternMatchType.INCLUDE;
        if (element != null && element.getAttribute(Constants.INCLUDES) != null) {
            matchType = PatternMatchType.INCLUDE;
        } else if (element != null && element.getAttribute(Constants.EXCLUDES) != null) {
            matchType = PatternMatchType.EXCLUDE;
        }
        return matchType;
    }

    /**
     * gets the analyzed extentions for the profiles type preferences
     * 
     * @param project IProject
     * @return analyzed extentions
     */
    public String[] getAnalyzedExtensions(IProject project) {
        return getAnalyzedExtensions(getSelectedProfile(project));
    }

    /**
     * gets analyzed extentions for the extention
     * 
     * @param element IConfigurationElement
     * @return analyzed extentions
     */
    public String[] getAnalyzedExtensions(IConfigurationElement element) {
        List<String> extensions = new ArrayList<String>();
        if (element != null) {
            String patterns = element.getAttribute(Constants.INCLUDES);
            if (patterns == null) {
                patterns = element.getAttribute(Constants.EXCLUDES);
            }
            if (patterns != null) {
                StringTokenizer tokenizer = new StringTokenizer(patterns, Constants.COMMA);
                while (tokenizer.hasMoreTokens()) {
                    String token = tokenizer.nextToken().trim();
                    if (token.indexOf(Constants.DOT) != -1) {
                        token = token.substring(token.indexOf(Constants.DOT) + 1);
                    }
                    extensions.add(token);
                }
            }
        } else {
            extensions.add(Constants.JAVA);
        }
        return (String[]) extensions.toArray(new String[0]);
    }

    /**
     * Creates composite group layout for all the analyzers preference
     * 
     * @param parent Composite
     * @param label label
     * @return Group
     */
    protected Group createGroup(Composite parent, String label) {
        Group group = new Group(parent, SWT.NULL);
        group.setLayout(new GridLayout(3, false));
        group.setLayoutData(new GridData(GridData.FILL_BOTH));
        group.setText(label);
        return group;
    }

    /**
     * notifies the listeners
     * 
     * @param event Event
     */
    public void raiseEvent(Event event) {
        this.processor.notifyListeners(event);
    }

    /**
     * Gives the number of profiles for the analyzer
     * 
     * @param project IProject
     * @return number of profiles
     */
    public int getProfilesSize(IProject project) {
        return 0;
    }

    /**
     * Gives the stats of the analyzer
     * 
     * @return stats of errors
     */
    public Map<String, Integer> getStat() {
        List<IResource> resouceResults = this.getResultsBuilder().getResourceResults();
        String markerId = this.getMarkerId();
        Map<String, Integer> statistics = new HashMap<String, Integer>();
        String markerAttribute = Constants.MARKER_ATTRIBURTE;
        for (int i = 0; i < resouceResults.size(); i++) {
            seggregateErrors(resouceResults.get(i), markerId, markerAttribute, statistics);
        }
        return statistics;
    }

    /**
     * @param resource IResource
     * @param markerId String
     * @param markerAttribute String
     * @param statistics Map<String, Integer>
     */
    protected abstract void seggregateErrors(IResource resource, String markerId, String markerAttribute, Map<String, Integer> statistics);

    /** 
     * @see com.techm.gisv.cqp.analyzers.Analyzer#getAttribute(java.lang.String)
     * @param key String
     * @return String
     */
    public String getAttribute(String key) {
        return attributes.get(key);
    }

    /**
     * @see com.techm.gisv.cqp.analyzers.Analyzer#setAttribute(java.lang.String, java.lang.String)
     * @param key String
     * @param value String 
     */
    public void setAttribute(String key, String value) {
        attributes.put(key, value);
    }

    /** 
     * @see com.techm.gisv.cqp.analyzers.Analyzer#getMarkerId()
     * 
     * @return String
     */
    public final String getMarkerId() {
        return processor.getAnalyzer().getAttribute(Constants.MARKER_ID);
    }

    /** 
     * @see com.techm.gisv.cqp.analyzers.Analyzer#setMarkerId(java.lang.String)
     * @param markerId String 
     */
    public final void setMarkerId(String markerId) {
        processor.getAnalyzer().setAttribute(Constants.MARKER_ID, markerId);
    }

    /** 
     * @see com.techm.gisv.cqp.analyzers.Analyzer#getConfigExtensions()
     * @return IConfigurationElement[]
     */
    public final IConfigurationElement[] getConfigExtensions() {
        return configExtensions;
    }

    /** 
     * @see com.techm.gisv.cqp.analyzers.Analyzer#setConfigExtensions(org.eclipse.core.runtime.IConfigurationElement[])
     * @param extensions IConfigurationElement[
     */
    public final void setConfigExtensions(IConfigurationElement[] extensions) {
        this.configExtensions = extensions;
    }

    /**
     * gets analyzer name
     * 
     * @return analyzer name
     */
    public final String getName() {
        return getAttribute(Constants.NAME) + " Analyzer";
    }

    /**
     * gets preference tab name
     * 
     * @return preference tab name
     */
    public final String getPreferencesTabName() {
        return getAttribute(Constants.NAME);
    }

    /**
     * @return String
     */
    public final String getGroupName() {
        return getAttribute(Constants.NAME) + " Preferences";
    }

    /**
     * @return String
     */
    public final String getDisableCheckBoxName() {
        return "Disable " + getAttribute(Constants.NAME);
    }

    /**
     * gets preference ID
     * 
     * @return preference id
     */
    public final String getPreferenceId() {
        return getAttribute(Constants.CONFIG_EXTENSION_ID) + ".selectedId";
    }

    /**
     * @return String disable preference id
     */
    public final String getDisablePreferenceId() {
        return getAttribute(Constants.ID) + ".disable";
    }
}
