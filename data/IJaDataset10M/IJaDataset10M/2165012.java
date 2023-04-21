package net.sf.genedator.plugin;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Random;
import javax.swing.*;
import net.sf.genedator.plugin.utils.PluginCategory;
import net.sf.genedator.plugin.utils.SimplePluginDataGenerator;
import net.sf.genedator.plugin.utils.PluginUtils;

/**
 * @author Anca Zapuc
 * Class which computes the CNP number.
 * A CNP number has the following meaning:
 * SAALLZZJJNNNC
 */
public class RandomCNPGenerator extends SimplePluginDataGenerator {

    public static final String BOTH = "BOTH";

    public static final String WOMEN = "WOMEN";

    public static final String MEN = "MEN";

    public static final String SEX_TYPE = "SEX_TYPE";

    public static final int[] SCALES = { 2, 7, 9, 1, 4, 6, 3, 5, 8, 2, 7, 9 };

    private JComboBox sexComboBox;

    private JLabel sexLabel;

    private String selectedSex;

    public RandomCNPGenerator() {
        JPanel pluginPanel = new JPanel();
        pluginPanel.setLayout(new GridLayout(0, 2));
        sexComboBox = new JComboBox();
        sexComboBox.addItem("Men");
        sexComboBox.addItem("Women");
        sexComboBox.addItem("Both");
        sexLabel = new JLabel("Sex:");
        FlowLayout fl = new FlowLayout();
        fl.setAlignment(FlowLayout.LEFT);
        JPanel comboPanel = new JPanel(fl);
        comboPanel.add(sexComboBox);
        pluginPanel.add(sexLabel);
        pluginPanel.add(comboPanel);
        mainPanel.add(pluginPanel);
    }

    public String getAboutInfo() {
        return "Allows to generate random CNP number.";
    }

    public String getName() {
        return "Random CNP Generator";
    }

    public String getVersion() {
        return "2.0";
    }

    @Override
    public void readConfigParameters() {
        super.readConfigParameters();
        selectedSex = sexComboBox.getSelectedItem().toString().toUpperCase();
        getPluginConfiguration().setParameter(SEX_TYPE, selectedSex);
    }

    /**
     * Creates the CNP number
     */
    @Override
    public String getRandomValue() {
        Random rand = new Random();
        byte[] cnp = new byte[13];
        int aa = rand.nextInt(100);
        int yearPrefix = rand.nextInt(3);
        int ll = (rand.nextInt(12) + 1);
        int zz = rand.nextInt(getMaxDay(ll, aa + getFullYear(yearPrefix))) + 1;
        int jj = rand.nextInt(46) + 1;
        int s = computeSexCode(yearPrefix, selectedSex);
        int nnn = rand.nextInt(1000) + 1;
        intToByteArray(cnp, s, 0, 1);
        intToByteArray(cnp, aa, 1, 2);
        intToByteArray(cnp, ll, 3, 2);
        intToByteArray(cnp, zz, 5, 2);
        intToByteArray(cnp, jj, 7, 2);
        intToByteArray(cnp, nnn, 9, 3);
        int sum = 0;
        for (int i = 0; i < SCALES.length; i++) {
            sum += cnp[i] * SCALES[i];
        }
        cnp[12] = (byte) ((sum % 11) == 10 ? 1 : sum % 11);
        return PluginUtils.byteToString(cnp);
    }

    private static int getMaxDay(int month, int fullYear) {
        boolean leapYear = PluginUtils.isLeapYear(fullYear);
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
            return 31;
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            return 30;
        } else if (leapYear) {
            return 29;
        } else {
            return 28;
        }
    }

    private static int computeSexCode(int prefix, String sex) {
        int sexCode = 1;
        switch(prefix) {
            case 0:
                if (MEN.equals(sex)) {
                    sexCode = 3;
                } else if (WOMEN.equals(sex)) sexCode = 4; else sexCode = generateNumberFromInterval(3, 4);
                break;
            case 1:
                if (MEN.equals(sex)) {
                    sexCode = 1;
                } else if (WOMEN.equals(sex)) sexCode = 2; else sexCode = generateNumberFromInterval(1, 2);
                break;
            case 2:
                if (MEN.equals(sex)) {
                    sexCode = 5;
                } else if (WOMEN.equals(sex)) sexCode = 6; else sexCode = generateNumberFromInterval(5, 6);
                break;
        }
        return sexCode;
    }

    private static int generateNumberFromInterval(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }

    private static int getFullYear(int prefix) {
        int result = 0;
        switch(prefix) {
            case 0:
                result = 1800;
                break;
            case 1:
                result = 1900;
                break;
            case 2:
                result = 2000;
                break;
        }
        return result;
    }

    public static boolean isValid(String str) {
        char[] chars = str.toCharArray();
        int[] values = new int[chars.length];
        for (int c = 0; c < chars.length; c++) {
            values[c] = chars[c] - 48;
        }
        if (values[0] < 1 || values[0] > 7) {
            return false;
        }
        int month = (values[3] * 10 + values[4]) % 20;
        int prefix = values[3] * 10 + values[4] - month;
        int year = values[1] * 10 + values[2] + getFullYear(prefix);
        int day = values[5] * 10 + values[6];
        if (month < 1 || month > 12) {
            return false;
        }
        int maxDay = getMaxDay(month, year);
        if (maxDay < 1 || day > maxDay) {
            return false;
        }
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            sum += values[i] * RandomCNPGenerator.SCALES[i];
        }
        int valid = sum % 11;
        if (valid == 10) {
            valid = 1;
        }
        int checksum = chars[12] - 48;
        return checksum == valid;
    }

    @Override
    public long getUniqueAmount() {
        if (selectedSex.equals(BOTH)) return 920409840L; else return 920409840L / 2;
    }

    @Override
    public void firstStep() {
        selectedSex = getPluginConfiguration().getString(SEX_TYPE, BOTH);
    }

    @Override
    public String getAuthor() {
        return "Anca Zapuc";
    }

    @Override
    public String getCategory() {
        return PluginCategory.NUMERICAL;
    }

    @Override
    public String getHelpPagePath() {
        return "help.html";
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
}
