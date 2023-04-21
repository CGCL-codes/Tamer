package com.moneydance.modules.features.mdcsvimporter;

import com.moneydance.modules.features.mdcsvimporter.formats.CustomReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author miki and Stan Towianski
 */
public final class Settings {

    static HashMap<String, CustomReaderData> ReaderConfigsHM = null;

    static HashMap<String, TransactionReader> ReaderHM = null;

    static Properties currentProps = new Properties();

    private static File getFilename() {
        File moneydanceHome = new File(System.getProperty("user.home"), ".moneydance");
        return new File(moneydanceHome, "mdcsvimporter.props");
    }

    private static Properties load() throws IOException {
        currentProps = new Properties();
        InputStream is;
        try {
            is = new FileInputStream(getFilename());
        } catch (FileNotFoundException ex) {
            return currentProps;
        }
        try {
            currentProps.load(is);
        } finally {
            is.close();
        }
        return currentProps;
    }

    private static void save(Properties props) throws IOException {
        OutputStream os = new FileOutputStream(getFilename());
        try {
            props.store(os, "MDCSVImporter - Moneydance CSV Importer");
        } finally {
            os.close();
            load();
        }
    }

    public static String get(boolean loadProps, String name) {
        try {
            if (loadProps) {
                load();
            }
            return currentProps.getProperty(name);
        } catch (IOException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static String get(boolean loadProps, String name, String defaultValue) {
        String retVal = get(loadProps, name);
        if (retVal == null) {
            return defaultValue;
        }
        return retVal;
    }

    public static void set(String name, String value) {
        try {
            Properties props = load();
            setOnly(props, name, value);
            save(props);
        } catch (IOException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void setOnly(Properties props, String name, String value) {
        String oldValue = props.getProperty(name);
        if ((oldValue != null && oldValue.equals(value)) || (value != null && value.equals(oldValue))) {
            return;
        }
        props.setProperty(name, value);
    }

    public static boolean getBoolean(boolean loadProps, String name) {
        return getBoolean(loadProps, name, false);
    }

    public static boolean getBoolean(boolean loadProps, String name, boolean defaultValue) {
        String value = get(loadProps, name);
        if (value == null) {
            return defaultValue;
        }
        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("1")) {
            return true;
        } else {
            return false;
        }
    }

    public static void setBoolean(String name, boolean value) {
        set(name, value ? "true" : "false");
    }

    public static void setYesNo(String name, boolean value) {
        set(name, value ? "yes" : "no");
    }

    public static int getInteger(boolean loadProps, String name) {
        return getInteger(loadProps, name, 0);
    }

    public static int getInteger(boolean loadProps, String name, int defaultValue) {
        String value = get(loadProps, name);
        if (value == null) {
            return defaultValue;
        }
        return Integer.parseInt(value);
    }

    public static void setInteger(String name, int value) {
        set(name, Integer.toString(value));
    }

    public static HashMap<String, CustomReaderData> createReaderConfigsHM() {
        ReaderConfigsHM = new HashMap<String, CustomReaderData>();
        ReaderHM = new HashMap<String, TransactionReader>();
        try {
            Properties props = load();
            for (Enumeration enu = props.propertyNames(); enu.hasMoreElements(); ) {
                String key = (String) enu.nextElement();
                System.out.println("props key =" + key + "=");
                if (key.startsWith("reader:") && key.endsWith(".Name")) {
                    String readerName = key.replaceAll("reader\\:(.*)\\..*", "reader:$1");
                    System.err.println("readerName >" + readerName + "<");
                    CustomReaderData customReaderData = new CustomReaderData();
                    customReaderData.setReaderName(props.getProperty(readerName + ".Name"));
                    customReaderData.setFieldSeparatorChar(getInteger(false, readerName + ".FieldSeparator", ','));
                    customReaderData.setDateFormatString(props.getProperty(readerName + ".DateFormatString"));
                    customReaderData.setHeaderLines(getInteger(false, readerName + ".HeaderLines", 0));
                    customReaderData.setFooterLines(getInteger(false, readerName + ".FooterLines", 0));
                    customReaderData.setAmountCurrencyChar(getInteger(false, readerName + ".AmountCurrencyChar", '$'));
                    customReaderData.setAmountDecimalSignChar(getInteger(false, readerName + ".AmountDecimalSignChar", '.'));
                    customReaderData.setAmountGroupingSeparatorChar(getInteger(false, readerName + ".AmountGroupingSeparatorChar", ','));
                    customReaderData.setAmountFormat(props.getProperty(readerName + ".AmountFormat"));
                    customReaderData.setImportReverseOrderFlg(getBoolean(false, readerName + ".ImportReverseOrderFlag", false));
                    customReaderData.setDataTypesList(new ArrayList<String>(Arrays.asList(props.getProperty(readerName + ".DataTypesList").split("[\\[\\],]"))));
                    customReaderData.setEmptyFlagsList(new ArrayList<String>(Arrays.asList(props.getProperty(readerName + ".EmptyFlagsList").split("[\\[\\],]"))));
                    int max = customReaderData.getDataTypesList().size();
                    for (int c = 1; c < max; c++) {
                        customReaderData.getDataTypesList().set(c - 1, customReaderData.getDataTypesList().get(c).trim());
                        customReaderData.getEmptyFlagsList().set(c - 1, customReaderData.getEmptyFlagsList().get(c).trim());
                    }
                    System.err.println("props readerName =" + customReaderData.getReaderName() + "=");
                    System.err.println("props getFieldSeparatorChar() =" + customReaderData.getFieldSeparatorChar() + "=");
                    System.err.println("props getDateFormatString() =" + customReaderData.getDateFormatString() + "=");
                    System.err.println("props getHeaderLines() =" + customReaderData.getHeaderLines() + "=");
                    System.err.println("props getDataTypesList() =" + customReaderData.getDataTypesList() + "=");
                    System.err.println("props getEmptyFlagsList() =" + customReaderData.getEmptyFlagsList() + "=");
                    ReaderConfigsHM.put(props.getProperty(readerName + ".Name"), customReaderData);
                    CustomReader customReader = new CustomReader(customReaderData);
                    ReaderHM.put(props.getProperty(readerName + ".Name"), customReader);
                    customReader.createSupportedDateFormats(customReaderData.getDateFormatString());
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return ReaderConfigsHM;
    }

    public static HashMap<String, TransactionReader> getReaderHM() {
        return ReaderHM;
    }

    public static void setCustomReaderConfig(CustomReaderData customReaderData) {
        try {
            Properties props = load();
            setOnly(props, "reader:" + customReaderData.getReaderName() + ".Name", customReaderData.getReaderName());
            setOnly(props, "reader:" + customReaderData.getReaderName() + ".HeaderLines", Integer.toString(customReaderData.getHeaderLines()));
            setOnly(props, "reader:" + customReaderData.getReaderName() + ".FooterLines", Integer.toString(customReaderData.getFooterLines()));
            setOnly(props, "reader:" + customReaderData.getReaderName() + ".FieldSeparator", Integer.toString(customReaderData.getFieldSeparatorChar()));
            setOnly(props, "reader:" + customReaderData.getReaderName() + ".DateFormatString", customReaderData.getDateFormatString());
            setOnly(props, "reader:" + customReaderData.getReaderName() + ".DataTypesList", customReaderData.getDataTypesList().toString());
            setOnly(props, "reader:" + customReaderData.getReaderName() + ".EmptyFlagsList", customReaderData.getEmptyFlagsList().toString());
            setOnly(props, "reader:" + customReaderData.getReaderName() + ".AmountCurrencyChar", Integer.toString(customReaderData.getAmountCurrencyChar()));
            setOnly(props, "reader:" + customReaderData.getReaderName() + ".AmountDecimalSignChar", Integer.toString(customReaderData.getAmountDecimalSignChar()));
            setOnly(props, "reader:" + customReaderData.getReaderName() + ".AmountGroupingSeparatorChar", Integer.toString(customReaderData.getAmountGroupingSeparatorChar()));
            setOnly(props, "reader:" + customReaderData.getReaderName() + ".AmountFormat", customReaderData.getAmountFormat());
            setOnly(props, "reader:" + customReaderData.getReaderName() + ".ImportReverseOrderFlag", Boolean.toString(customReaderData.getImportReverseOrderFlg()));
            save(props);
        } catch (IOException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void removeCustomReaderConfig(CustomReaderData customReaderData) {
        try {
            Properties props = load();
            props.remove("reader:" + customReaderData.getReaderName() + ".Name");
            props.remove("reader:" + customReaderData.getReaderName() + ".HeaderLines");
            props.remove("reader:" + customReaderData.getReaderName() + ".FooterLines");
            props.remove("reader:" + customReaderData.getReaderName() + ".FieldSeparator");
            props.remove("reader:" + customReaderData.getReaderName() + ".DateFormatString");
            props.remove("reader:" + customReaderData.getReaderName() + ".DataTypesList");
            props.remove("reader:" + customReaderData.getReaderName() + ".EmptyFlagsList");
            props.remove("reader:" + customReaderData.getReaderName() + ".FooterLines");
            props.remove("reader:" + customReaderData.getReaderName() + ".AmountCurrencyChar");
            props.remove("reader:" + customReaderData.getReaderName() + ".AmountDecimalSignChar");
            props.remove("reader:" + customReaderData.getReaderName() + ".AmountGroupingSeparatorChar");
            props.remove("reader:" + customReaderData.getReaderName() + ".AmountFormat");
            props.remove("reader:" + customReaderData.getReaderName() + ".ImportReverseOrderFlag");
            save(props);
        } catch (IOException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
