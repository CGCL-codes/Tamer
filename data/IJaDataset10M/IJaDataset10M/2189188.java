package org.pubcurator.analyzers.abner.config;

import java.util.ArrayList;
import java.util.List;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.resource.metadata.ConfigurationParameterSettings;
import org.eclipse.swt.widgets.Shell;
import org.pubcurator.uima.config.Category;
import org.pubcurator.uima.config.IAnalyzerConfiguration;
import org.pubcurator.uima.definitions.PredefinedCategories;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * @author Kai Schlamp (schlamp@gmx.de)
 *
 */
public class AbnerAnalyzerConfiguration implements IAnalyzerConfiguration {

    private XStream xstream;

    public AbnerAnalyzerConfiguration() {
        xstream = new XStream(new DomDriver());
        xstream.setClassLoader(this.getClass().getClassLoader());
    }

    @Override
    public void initialize(AnalysisEngineDescription description) {
    }

    @Override
    public String initializeSettings(ConfigurationParameterSettings parameterSettings) {
        AbnerAnalyzerSettings analyzerSettings = new AbnerAnalyzerSettings();
        return serizalizeSettings(analyzerSettings);
    }

    @Override
    public String editSettings(Shell parent, String settings) {
        AbnerAnalyzerSettings analyzerSettings = deserializeSettings(settings);
        return serizalizeSettings(analyzerSettings);
    }

    @Override
    public Category[] getCategories(String settings) {
        List<Category> categories = new ArrayList<Category>();
        categories.add(PredefinedCategories.getCategories().get(PredefinedCategories.GENE));
        categories.add(PredefinedCategories.getCategories().get(PredefinedCategories.PROTEIN));
        categories.add(PredefinedCategories.getCategories().get(PredefinedCategories.CELL));
        categories.add(PredefinedCategories.getCategories().get(PredefinedCategories.RNA));
        return categories.toArray(new Category[0]);
    }

    @Override
    public boolean isConfigurable(String settings) {
        return false;
    }

    @Override
    public boolean isConfigurationRequired(String settings) {
        return false;
    }

    @Override
    public ConfigurationParameterSettings updateParameters(String settings, ConfigurationParameterSettings parameterSettings) {
        return parameterSettings;
    }

    private AbnerAnalyzerSettings deserializeSettings(String settings) {
        return (AbnerAnalyzerSettings) xstream.fromXML(settings);
    }

    private String serizalizeSettings(AbnerAnalyzerSettings analyzerSettings) {
        return xstream.toXML(analyzerSettings);
    }
}
