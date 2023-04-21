package net.sf.genedator.plugin;

import java.awt.GridLayout;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import net.sf.genedator.plugin.exceptions.UniqueDataGeneratingException;
import net.sf.genedator.plugin.loaders.IJarResources;
import net.sf.genedator.plugin.utils.PluginUtils;

/**
 *
 * @author Michał Wilczek
 */
public abstract class SimpleCodeGenerator implements PluginDataGenerator {

    public static final String NULL_RATIO = "NULL_RATIO";

    public static final String UNIQUE = "UNIQUE";

    public static final String UNIQUE_AMOUNT = "UNIQUE_AMOUNT";

    public static final String UNIQUE_AMOUNT_MODIFIED = "UNIQUE_AMOUNT_MODIFIED";

    private JPanel panel;

    private JCheckBox uniqueCheckbox;

    private JSlider nullRatioSlider;

    private JLabel uniqueLabel;

    private JLabel nullRatioLabel;

    protected SimplePluginConfigurationAdapter pluginConfiguration;

    public SimpleCodeGenerator() {
        pluginConfiguration = new SimplePluginConfigurationAdapter() {
        };
        pluginConfiguration.setParameter(UNIQUE_AMOUNT, Long.MAX_VALUE);
        pluginConfiguration.setParameter(UNIQUE_AMOUNT_MODIFIED, Long.MAX_VALUE);
        panel = new JPanel();
        uniqueCheckbox = new JCheckBox();
        nullRatioSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
        nullRatioSlider.setPaintTicks(true);
        nullRatioSlider.setPaintLabels(true);
        nullRatioSlider.setMinorTickSpacing(10);
        nullRatioSlider.setMajorTickSpacing(20);
        uniqueLabel = new JLabel("Unique:");
        nullRatioLabel = new JLabel("Null ratio %:");
        panel.setLayout(new GridLayout(0, 2));
        panel.add(uniqueLabel);
        panel.add(uniqueCheckbox);
        panel.add(nullRatioLabel);
        panel.add(nullRatioSlider);
    }

    @Override
    public String[] generate(int numberOfRecords) {
        int nullRatio = pluginConfiguration.getInt(NULL_RATIO);
        long uniqueAmount = pluginConfiguration.getLong(UNIQUE_AMOUNT_MODIFIED);
        boolean unique = pluginConfiguration.getBoolean(UNIQUE);
        int[] nullIndexes = PluginUtils.getNullIndexes(numberOfRecords, nullRatio);
        if (unique && numberOfRecords - nullIndexes.length > uniqueAmount) throw new UniqueDataGeneratingException("Cannot generate " + numberOfRecords + " of unique records\n for " + getName());
        String[] result = new String[numberOfRecords];
        int nullIndexPos = 0;
        boolean nullRemain = true;
        if (nullIndexes.length == 0) nullRemain = false;
        if (unique) {
            Set<String> uniqueValues = new HashSet<String>(numberOfRecords);
            for (int i = 0; i < numberOfRecords; i++) {
                if (nullRemain && nullIndexes[nullIndexPos] == i) {
                    result[i] = "NULL";
                    if (nullIndexPos + 1 == nullIndexes.length) nullRemain = false; else nullIndexPos++;
                    continue;
                }
                do {
                    result[i] = getRandom();
                } while (!uniqueValues.add(result[i]) && unique);
            }
            uniqueValues.toArray(result);
            uniqueValues = null;
        } else {
            for (int i = 0; i < numberOfRecords; i++) {
                if (nullRemain && nullIndexes[nullIndexPos] == i) {
                    result[i] = "NULL";
                    if (nullIndexPos + 1 == nullIndexes.length) nullRemain = false; else nullIndexPos++;
                    continue;
                }
                result[i] = getRandom();
            }
        }
        return result;
    }

    @Override
    public String[] generate(int numberOfRecords, int step) {
        return generate(numberOfRecords);
    }

    @Override
    public String getAuthor() {
        return "Michał Wilczek";
    }

    @Override
    public JPanel getConfigPanel() {
        return panel;
    }

    @Override
    public IJarResources getJarResources() {
        return null;
    }

    @Override
    public void readConfigParameters() {
        pluginConfiguration.setParameter(NULL_RATIO, (int) nullRatioSlider.getValue());
        pluginConfiguration.setParameter(UNIQUE, uniqueCheckbox.isSelected());
    }

    public static String byteToString(byte[] arr) {
        StringBuilder sb = new StringBuilder();
        for (byte b : arr) {
            sb.append(b);
        }
        return sb.toString();
    }

    /**
     * Inserts given number into byte array, starting from startIndex - specified digits amount.
     * @param array array to modify
     * @param number number to insert
     * @param startIndex index where number will be inserted into array
     * @param digits how much digits from given number should be inserted
     */
    public static void intToByteArray(byte[] array, int number, int startIndex, int digits) {
        for (int i = startIndex + digits - 1; i >= startIndex; i--) {
            array[i] = (byte) (number % 10);
            number /= 10;
        }
    }

    @Override
    public void setJarResources(IJarResources ijr) {
    }

    public PluginConfiguration getPluginConfiguration() {
        return pluginConfiguration;
    }

    public abstract String getRandom();

    public abstract boolean isValid(String str);
}
