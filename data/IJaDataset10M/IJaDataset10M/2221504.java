package com.googlecode.dbyoutil.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeSet;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.collections.MapIterator;
import org.apache.commons.collections.map.ListOrderedMap;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import com.googlecode.dbyoutil.db.xml2java.DataEnvelope;
import com.googlecode.dbyoutil.db.xml2java.DataSetElement;
import com.googlecode.dbyoutil.db.xml2java.SAX2Reader;
import com.googlecode.dbyoutil.db.xml2java.XMLElement;
import com.googlecode.dbyoutil.tool.StringUtil;
import com.googlecode.dbyoutil.transformer.SQLClient;

public class StoreDB {

    public static String input = "oracle.jdbc.driver.OracleDriver jdbc:oracle:thin:@192.168.30.223:1521:HELIOMOD4 HLMODUSR hl4sr_Oy ../sql/analyze.txt 11";

    private static boolean alert;

    private static String currentDirAbsolutePath;

    private static ListOrderedMap sqlMap = new ListOrderedMap();

    private static ListOrderedMap tableMap = new ListOrderedMap();

    private static String timeFilename = "time.ser";

    private static String freqFilename = "freq.ser";

    private static long tempFreq = 1;

    private static boolean sqlLoaded;

    private static boolean tableLoaded;

    private static ListOrderedMap handleSQLTime(String sql, long time) {
        ListOrderedMap old = loadTime();
        if (!sqlLoaded && old != null && old.size() > 0) {
            sqlMap = old;
            tableLoaded = true;
        }
        sqlMap.put(sql, time);
        sortMap(sqlMap);
        Iterator it = sqlMap.keySet().iterator();
        StringBuffer sb = new StringBuffer();
        while (it.hasNext()) {
            Object key = it.next();
            Object value = sqlMap.get(key);
            sb.append(key).append("\t").append(value).append("\n");
        }
        try {
            String currentDirAbsolutePath = new File(".").getCanonicalPath() + System.getProperty("file.separator");
            String finalSummaryFile = currentDirAbsolutePath + "summarysql.txt";
            StringUtil.writeIntoFile(finalSummaryFile, sb.toString(), false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        saveTime(sqlMap);
        return sqlMap;
    }

    private static ListOrderedMap handleSQLTableFrequency(String stream) {
        StringBuffer tempSb = new StringBuffer();
        if (stream != null) {
            ListOrderedMap old = loadFrequency();
            if (!tableLoaded && old != null && old.size() > 0) {
                tableMap = old;
                tableLoaded = true;
            }
            sortMap(tableMap);
            WordCount wc = new WordCount(stream);
            wc.countWords();
            try {
                Iterator keys = wc.keySet().iterator();
                String key = null;
                while (keys.hasNext()) {
                    key = (String) keys.next();
                    if (key != null && !"".equals(key.trim()) && key.toUpperCase().indexOf("_TB") > -1) {
                        tempFreq = (long) wc.getCounter(key).read() + tempFreq;
                        tableMap.put(key.toUpperCase(), tempFreq);
                    }
                }
                keys = tableMap.keySet().iterator();
                while (keys.hasNext()) {
                    key = (String) keys.next();
                    tempSb.append(key.toUpperCase()).append("\t").append(tableMap.get(key)).append('\n');
                    System.out.println(tempSb.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        saveFrequency(tableMap);
        String currentTableFrequencyFile = currentDirAbsolutePath + "tablefrequency.txt";
        StringUtil.writeIntoFile(currentTableFrequencyFile, tempSb.toString(), false);
        tempSb.setLength(0);
        return tableMap;
    }

    private static void sortMap(ListOrderedMap map) {
        if (map != null) {
            List mapKeys = new ArrayList(map.keySet());
            List mapValues = new ArrayList(map.values());
            map.clear();
            TreeSet sortedSet = new TreeSet(mapValues);
            Object[] sortedArray = sortedSet.toArray();
            int size = sortedArray.length;
            for (int i = size; i > 0; ) {
                map.put(mapKeys.get(mapValues.indexOf(sortedArray[--i])), sortedArray[i]);
            }
        }
    }

    private static void saveTime(ListOrderedMap sqlMap) {
        FileOutputStream fos = null;
        ObjectOutputStream out = null;
        try {
            fos = new FileOutputStream(timeFilename);
            out = new ObjectOutputStream(fos);
            out.writeObject(sqlMap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ListOrderedMap loadTime() {
        FileInputStream fis = null;
        ObjectInputStream in = null;
        try {
            fis = new FileInputStream(timeFilename);
            in = new ObjectInputStream(fis);
            sqlMap = (ListOrderedMap) in.readObject();
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return sqlMap;
    }

    private static void saveFrequency(ListOrderedMap tableMap) {
        FileOutputStream fos = null;
        ObjectOutputStream out = null;
        try {
            fos = new FileOutputStream(freqFilename);
            out = new ObjectOutputStream(fos);
            out.writeObject(tableMap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ListOrderedMap loadFrequency() {
        FileInputStream fis = null;
        ObjectInputStream in = null;
        try {
            fis = new FileInputStream(freqFilename);
            in = new ObjectInputStream(fis);
            tableMap = (ListOrderedMap) in.readObject();
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return tableMap;
    }

    private static String[] getSQL(DataEnvelope results) {
        DataSetElement dataset = null;
        try {
            dataset = results.getDataSetElement();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        XMLElement current = null;
        String sql = dataset.getRow1().getSqlText().getPCData();
        String time = dataset.getRow1().getElapsedSeconds().getPCData();
        handleSQLTime(sql, Long.valueOf(time.trim()));
        handleSQLTableFrequency(sql);
        return null;
    }

    public static DataEnvelope parseXML(String text) {
        DataEnvelope results = null;
        try {
            SAX2Reader s = new SAX2Reader();
            results = (DataEnvelope) s.parse(new InputSource(new StringReader(text)));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }

    public static void main1(String[] args) {
        handleSQLTableFrequency(args[0]);
    }

    public static void main(String[] args) {
        String s[] = input.split(" ");
        SQLClient.main(s);
        String text = SQLClient.getStat();
        if (text != null) {
            int ind = text.indexOf("ELAPSED_SECONDS");
            if (ind > -1) {
                alert = true;
                try {
                    currentDirAbsolutePath = new File(".").getCanonicalPath() + System.getProperty("file.separator");
                    String currentSQLTimeFile = currentDirAbsolutePath + "sql.txt";
                    StringUtil.writeIntoFile(currentSQLTimeFile, text, true);
                    StoreDB.getSQL(StoreDB.parseXML(text));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean isAlert() {
        return alert;
    }

    public static ListOrderedMap getSqlMap() {
        return sqlMap;
    }

    public static void setSqlMap(ListOrderedMap sqlMap) {
        StoreDB.sqlMap = sqlMap;
    }
}
