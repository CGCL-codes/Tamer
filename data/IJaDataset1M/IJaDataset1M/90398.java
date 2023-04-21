package net.sf.genedator.plugin;

import java.awt.GridLayout;
import java.util.HashSet;
import java.util.Set;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import net.sf.genedator.plugin.exceptions.UniqueDataGeneratingException;
import net.sf.genedator.plugin.loaders.IJarResources;
import net.sf.genedator.plugin.utils.PluginUtils;

/**
 *
 * @author Michał Wilczek wilczek256@gmail.com
 */
public abstract class SimplePluginDataGenerator implements PluginDataGenerator {

    protected JPanel mainPanel;

    protected JPanel pluginPanel;

    protected JLabel uniqueLabel;

    protected JLabel nullRatioLabel;

    protected JSlider nullRatioSlider;

    protected JCheckBox uniqueCheckbox;

    private SimplePluginConfigurationAdapter config;

    private IJarResources jarResources;

    public SimplePluginDataGenerator() {
        config = new SimplePluginConfigurationAdapter() {
        };
        init();
    }

    public SimplePluginDataGenerator(SimplePluginConfigurationAdapter pluginConf) {
        config = pluginConf;
        init();
    }

    protected void init() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        JPanel defaultOptionsPanel = new JPanel();
        defaultOptionsPanel.setLayout(new GridLayout(0, 2));
        uniqueCheckbox = new JCheckBox();
        uniqueLabel = new JLabel("Unique:");
        defaultOptionsPanel.add(uniqueLabel);
        defaultOptionsPanel.add(uniqueCheckbox);
        nullRatioLabel = new JLabel("Null ratio %:");
        nullRatioSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
        nullRatioSlider.setPaintTicks(true);
        nullRatioSlider.setPaintLabels(true);
        nullRatioSlider.setMinorTickSpacing(10);
        nullRatioSlider.setMajorTickSpacing(20);
        defaultOptionsPanel.add(nullRatioLabel);
        defaultOptionsPanel.add(nullRatioSlider);
        pluginPanel = new JPanel();
        mainPanel.add(defaultOptionsPanel);
        mainPanel.add(pluginPanel);
    }

    @Override
    public void readConfigParameters() {
        config.setParameter(SimplePluginConfigurationAdapter.NULL_RATIO, nullRatioSlider.getValue());
        config.setParameter(SimplePluginConfigurationAdapter.UNIQUE, uniqueCheckbox.isSelected());
    }

    public JPanel getPanel() {
        return pluginPanel;
    }

    @Override
    public JPanel getConfigPanel() {
        return mainPanel;
    }

    public SimplePluginConfigurationAdapter getConfig() {
        return config;
    }

    @Override
    public void setJarResources(IJarResources ijr) {
        jarResources = ijr;
    }

    @Override
    public IJarResources getJarResources() {
        return jarResources;
    }

    @Override
    public String[] generate(int numberOfRecords) {
        String[] result = new String[numberOfRecords];
        beforeGenerate(result);
        boolean unique = config.getBoolean(SimplePluginConfigurationAdapter.UNIQUE);
        int[] nullIndexes = PluginUtils.getNullIndexes(numberOfRecords, config.getInt(SimplePluginConfigurationAdapter.NULL_RATIO));
        if (unique && numberOfRecords - nullIndexes.length > getUniqueAmount()) {
            throw new UniqueDataGeneratingException("Cannot generate " + numberOfRecords + " of unique records\n for " + getName());
        }
        int nullIndexPos = 0;
        boolean nullRemain = nullIndexes.length == 0 ? false : true;
        Set<String> uniqueValues = new HashSet<String>(numberOfRecords);
        for (int i = 0; i < numberOfRecords; i++) {
            if (nullRemain && nullIndexes[nullIndexPos] == i) {
                result[i] = "NULL";
                if (nullIndexPos + 1 == nullIndexes.length) {
                    nullRemain = false;
                } else {
                    nullIndexPos++;
                }
                continue;
            }
            do {
                result[i] = getRandom();
            } while (unique && !uniqueValues.add(result[i]));
        }
        if (unique) {
            uniqueValues.toArray(result);
            uniqueValues = null;
        }
        afterGenerate(result);
        return result;
    }

    @Override
    public String[] generate(int numberOfRecords, int step) {
        return generate(numberOfRecords);
    }

    /** calculate amount of records which are unique */
    public abstract long getUniqueAmount();

    /** random value based on plugin type */
    public abstract String getRandom();

    /** this method is executed before generation */
    public void beforeGenerate(String[] records) {
    }

    ;

    /** this method is executed after generation */
    public void afterGenerate(String[] records) {
    }

    ;
}
