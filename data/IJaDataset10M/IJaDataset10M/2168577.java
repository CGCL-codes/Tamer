package org.pubcurator.uima.config;

import java.util.Map;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.resource.metadata.ConfigurationParameterDeclarations;
import org.apache.uima.resource.metadata.ConfigurationParameterSettings;
import org.apache.uima.resource.metadata.NameValuePair;
import org.eclipse.swt.widgets.Shell;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * @author Kai Schlamp (schlamp@gmx.de)
 *
 */
public abstract class DefaultAnalyzerConfiguration implements IAnalyzerConfiguration {

    private XStream xstream;

    private AnalysisEngineDescription description;

    public DefaultAnalyzerConfiguration() {
        xstream = new XStream(new DomDriver());
        xstream.setClassLoader(this.getClass().getClassLoader());
    }

    @Override
    public void initialize(AnalysisEngineDescription description) {
        this.description = description;
    }

    @Override
    public boolean isConfigurable(String settings) {
        return settings != null && !settings.isEmpty();
    }

    @Override
    public boolean isConfigurationRequired(String settings) {
        return false;
    }

    @Override
    public String initializeSettings(ConfigurationParameterSettings parameterSettings) {
        ParameterValuesStore store = new ParameterValuesStore();
        for (NameValuePair pair : parameterSettings.getParameterSettings()) {
            store.setValue(pair.getName(), pair.getValue());
        }
        for (String group : parameterSettings.getSettingsForGroups().keySet()) {
            for (NameValuePair pair : parameterSettings.getSettingsForGroups().get(group)) {
                store.setValue(group, pair.getName(), pair.getValue());
            }
        }
        return serizalizeAnalyzerSettings(store);
    }

    @Override
    public String editSettings(Shell parent, String settings) {
        ParameterValuesStore store = deserializeAnalyzerSettings(settings);
        ConfigurationParameterDeclarations declarations = description.getAnalysisEngineMetaData().getConfigurationParameterDeclarations();
        DefaultAnalyzerConfigurationDialog dialog = new DefaultAnalyzerConfigurationDialog(parent, declarations, store, this);
        dialog.open();
        return serizalizeAnalyzerSettings(store);
    }

    @Override
    public ConfigurationParameterSettings updateParameters(String settings, ConfigurationParameterSettings parameterSettings) {
        ParameterValuesStore store = deserializeAnalyzerSettings(settings);
        Map<String, Object> noGroupParameters = store.noGroupParameterValues;
        for (String key : noGroupParameters.keySet()) {
            parameterSettings.setParameterValue(key, noGroupParameters.get(key));
        }
        for (String group : store.groupParameterValues.keySet()) {
            Map<String, Object> groupParameters = store.groupParameterValues.get(group);
            for (String key : groupParameters.keySet()) {
                parameterSettings.setParameterValue(group, key, groupParameters.get(key));
            }
        }
        return parameterSettings;
    }

    private ParameterValuesStore deserializeAnalyzerSettings(String settings) {
        return (ParameterValuesStore) xstream.fromXML(settings);
    }

    private String serizalizeAnalyzerSettings(ParameterValuesStore store) {
        return xstream.toXML(store);
    }

    @Override
    public Category[] getCategories(String settings) {
        ParameterValuesStore store = deserializeAnalyzerSettings(settings);
        return getCategories(store);
    }

    public abstract Category[] getCategories(ParameterValuesStore store);

    public abstract ParameterPolicy getParameterPolicy(String group, String parameterName);
}
