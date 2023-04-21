package net.sf.genedator.plugin;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Random;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import net.sf.genedator.plugin.utils.PluginCategory;
import net.sf.genedator.plugin.utils.PluginUtils;
import net.sf.genedator.plugin.utils.SimplePluginDataGenerator;

/**
 *
 * @author Michał Wilczek
 */
public class EANGenerator extends SimplePluginDataGenerator {

    private static int PREFIXES[][] = { { 000, 139 }, { 200, 290 }, { 300, 380 }, { 383, 383 }, { 385, 385 }, { 387, 387 }, { 389, 389 }, { 400, 440 }, { 450, 459 }, { 490, 499 }, { 460, 471 }, { 474, 482 }, { 484, 489 }, { 500, 509 }, { 520, 521 }, { 528, 531 }, { 535, 535 }, { 539, 549 }, { 560, 560 }, { 569, 579 }, { 590, 590 }, { 594, 594 }, { 599, 601 }, { 603, 604 }, { 608, 609 }, { 611, 611 }, { 613, 613 }, { 615, 616 }, { 618, 619 }, { 621, 622 }, { 624, 629 }, { 640, 649 }, { 690, 695 }, { 700, 709 }, { 729, 746 }, { 750, 750 }, { 754, 755 }, { 759, 771 }, { 773, 773 }, { 775, 775 }, { 777, 780 }, { 784, 784 }, { 786, 786 }, { 789, 790 }, { 800, 850 }, { 858, 860 }, { 865, 880 }, { 884, 885 }, { 888, 888 }, { 890, 890 }, { 893, 893 }, { 896, 896 }, { 899, 919 }, { 930, 949 }, { 955, 955 }, { 958, 958 } };

    public static final int[] EAN13_SCALES = { 1, 3, 1, 3, 1, 3, 1, 3, 1, 3, 1, 3 };

    public static final int[] EAN8_SCALES = { 3, 1, 3, 1, 3, 1, 3 };

    public static final String MODE = "MODE";

    public static final int EAN8 = 8;

    public static final int EAN13 = 13;

    private JRadioButton ean8Radio;

    private JRadioButton ean13Radio;

    private int selectedEan;

    private int selectedScales[];

    public EANGenerator() {
        super.init();
        JPanel pluginPanel = new JPanel();
        pluginPanel.setLayout(new GridLayout(0, 1));
        ean13Radio = new JRadioButton("EAN 13");
        ean13Radio.setAlignmentX(Component.LEFT_ALIGNMENT);
        ean13Radio.setSelected(true);
        ean8Radio = new JRadioButton("EAN 8");
        ean8Radio.setAlignmentX(Component.LEFT_ALIGNMENT);
        ButtonGroup radioGroup = new ButtonGroup();
        radioGroup.add(ean13Radio);
        radioGroup.add(ean8Radio);
        pluginPanel.add(ean13Radio);
        pluginPanel.add(ean8Radio);
        mainPanel.add(pluginPanel);
    }

    @Override
    public String getAboutInfo() {
        return "Allows to generate random EAN 13 or EAN 8 code.";
    }

    @Override
    public String getName() {
        return "EAN code generator";
    }

    @Override
    public String getVersion() {
        return "2.0";
    }

    @Override
    public void readConfigParameters() {
        super.readConfigParameters();
        getPluginConfiguration().setParameter(MODE, ean8Radio.isSelected() ? EAN8 : EAN13);
    }

    @Override
    public void firstStep() {
        selectedEan = getPluginConfiguration().getInt(MODE, EAN13);
        selectedScales = (selectedEan == EAN13) ? EAN13_SCALES : EAN8_SCALES;
    }

    @Override
    public String getAuthor() {
        return "Michał Wilczek";
    }

    @Override
    public String getCategory() {
        return PluginCategory.NUMERICAL;
    }

    @Override
    public String getHelpPagePath() {
        return "help.html";
    }

    @Override
    public long getUniqueAmount() {
        return Long.MAX_VALUE;
    }

    @Override
    public String getRandomValue() {
        Random rand = new Random();
        byte[] ean = new byte[selectedEan];
        int prefixRange[] = PREFIXES[getRandom().nextInt(PREFIXES.length)];
        int prefix = prefixRange[0] + getRandom().nextInt(prefixRange[1] - prefixRange[0] + 1);
        for (int i = 2; i > -1; i--) {
            ean[i] = (byte) (prefix % 10);
            prefix /= 10;
        }
        for (int i = 3; i < ean.length - 1; i++) {
            ean[i] = (byte) rand.nextInt(10);
        }
        calcChecksum(ean);
        return PluginUtils.byteToString(ean);
    }

    private void calcChecksum(byte[] ean) {
        int sum = 0;
        for (int i = 0; i < ean.length - 1; i++) {
            sum += ean[i] * selectedScales[i];
        }
        int checksum = (10 - (sum % 10)) % 10;
        ean[selectedScales.length] = (byte) checksum;
    }
}
