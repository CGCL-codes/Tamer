package net.sf.zorobot.ext.db.mysql;

import java.sql.*;
import net.sf.zorobot.core.ZorobotSystem;
import net.sf.zorobot.util.JapaneseString;
import org.jdom.*;
import org.jdom.input.*;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import java.util.*;

public class MySqlKanjiImport {

    Connection conn;

    public static void main(String args[]) {
        MySqlKanjiImport importer = new MySqlKanjiImport();
        try {
            importer.prepareTable();
            importer.process("dict/kanjidic2.xml");
        } catch (OutOfMemoryError oom) {
            ZorobotSystem.error("Out of memory, try to use java option -Xmx256M or bigger");
        } catch (Exception e) {
            ZorobotSystem.error("Couldn't import kanjidic");
        }
    }

    public MySqlKanjiImport() {
        conn = MySqlFactory.getConnection(ZorobotSystem.props);
    }

    public void prepareTable() throws Exception {
        Statement stmt = conn.createStatement();
        try {
            stmt.executeUpdate("DELETE FROM kanji");
        } catch (Exception e) {
            try {
                stmt.execute("CREATE TABLE `kanji` (" + "  `id` smallint(6) unsigned NOT NULL," + "  `kanji` char(1) NOT NULL," + "  `uni1` mediumint(8) unsigned NOT NULL," + "  `uni2` mediumint(8) unsigned default NULL," + "  `grade` tinyint(3) unsigned default NULL," + "  `radical` tinyint(3) unsigned default NULL," + "  `freq` smallint(5) unsigned default NULL," + "  `stroke` tinyint(3) unsigned default NULL," + "  `multi_on` varchar(200) default NULL," + "  `multi_kun` varchar(200) default NULL," + "  `multi_mean` varchar(400) default NULL," + "  `multi_nanori` varchar(400) default NULL," + "  PRIMARY KEY  (`id`)," + "  KEY `kanji` (`kanji`)," + "  KEY `grade` (`grade`)," + "  KEY `freq` (`freq`)," + "  KEY `stroke` (`stroke`)," + "  KEY `multi_on` (`multi_on`(16))," + "  KEY `multi_kun` (`multi_kun`(16))," + "  KEY `multi_mean` (`multi_mean`(16))" + ") ENGINE=MyISAM DEFAULT CHARSET=ucs2 ROW_FORMAT=DYNAMIC;");
            } catch (Exception ee) {
                ZorobotSystem.exception(ee);
                ZorobotSystem.error("Couldn't create table kanji, " + ee.getMessage());
            }
        }
        try {
            stmt.executeUpdate("DELETE FROM kun");
        } catch (Exception e) {
            try {
                stmt.execute("CREATE TABLE `kun` (" + "  `id` smallint(5) unsigned NOT NULL," + "  `kun` varchar(50) NOT NULL," + "  PRIMARY KEY  (`id`,`kun`)," + "  KEY `id` (`id`)," + "  KEY `kun` (`kun`)" + ") ENGINE=MyISAM DEFAULT CHARSET=ucs2 ROW_FORMAT=DYNAMIC;");
            } catch (Exception ee) {
                ZorobotSystem.exception(ee);
                ZorobotSystem.error("Couldn't create table kun, " + ee.getMessage());
            }
        }
        try {
            stmt.executeUpdate("DELETE FROM ont");
        } catch (Exception e) {
            try {
                stmt.execute("CREATE TABLE `ont` (" + "  `id` smallint(5) unsigned NOT NULL," + "  `ont` varchar(50) NOT NULL," + "  PRIMARY KEY  (`id`,`ont`)," + "  KEY `id` (`id`)," + "  KEY `ont` (`ont`)" + ") ENGINE=MyISAM DEFAULT CHARSET=ucs2 ROW_FORMAT=DYNAMIC;");
            } catch (Exception ee) {
                ZorobotSystem.exception(ee);
                ZorobotSystem.error("Couldn't create table ont, " + ee.getMessage());
            }
        }
        try {
            stmt.executeUpdate("DELETE FROM mean");
        } catch (Exception e) {
            try {
                stmt.execute("CREATE TABLE `mean` (" + "  `id` smallint(5) unsigned NOT NULL," + "  `mean` varchar(200) NOT NULL," + "  PRIMARY KEY  (`id`,`mean`)," + "  KEY `id` (`id`)," + "  KEY `mean` (`mean`)" + ") ENGINE=MyISAM DEFAULT CHARSET=ucs2 ROW_FORMAT=DYNAMIC;");
            } catch (Exception ee) {
                ZorobotSystem.exception(ee);
                ZorobotSystem.error("Couldn't create table mean, " + ee.getMessage());
            }
        }
        try {
            stmt.executeUpdate("DELETE FROM nanori");
        } catch (Exception e) {
            try {
                stmt.execute("CREATE TABLE `nanori` (" + "  `id` smallint(5) unsigned NOT NULL," + "  `nanori` varchar(50) NOT NULL," + "  PRIMARY KEY  (`id`,`nanori`)," + "  KEY `id` (`id`)," + "  KEY `nanori` (`nanori`)" + ") ENGINE=MyISAM DEFAULT CHARSET=ucs2 ROW_FORMAT=DYNAMIC;");
            } catch (Exception ee) {
                ZorobotSystem.exception(ee);
                ZorobotSystem.error("Couldn't create table nanori, " + ee.getMessage());
            }
        }
        try {
            stmt.executeUpdate("DELETE FROM koreanr");
        } catch (Exception e) {
            try {
                stmt.execute("CREATE TABLE `koreanr` (" + "  `id` smallint(5) unsigned NOT NULL default '0'," + "  `koreanr` varchar(50) NOT NULL," + "  PRIMARY KEY  (`id`,`koreanr`)," + "  KEY `id` (`id`)" + ") ENGINE=MyISAM DEFAULT CHARSET=ucs2 ROW_FORMAT=DYNAMIC;");
            } catch (Exception ee) {
                ZorobotSystem.exception(ee);
                ZorobotSystem.error("Couldn't create table ont, " + ee.getMessage());
            }
        }
        try {
            stmt.executeUpdate("DELETE FROM pinyin");
        } catch (Exception e) {
            try {
                stmt.execute("CREATE TABLE `pinyin` (" + "  `id` smallint(5) unsigned NOT NULL default '0'," + "  `pinyin` varchar(50) NOT NULL," + "  PRIMARY KEY  (`id`,`pinyin`)," + "  KEY `id` (`id`)" + ") ENGINE=MyISAM DEFAULT CHARSET=ucs2 ROW_FORMAT=DYNAMIC;");
            } catch (Exception ee) {
                ZorobotSystem.exception(ee);
                ZorobotSystem.error("Couldn't create table pinyin, " + ee.getMessage());
            }
        }
        stmt.close();
    }

    public void process(String fileName) {
        try {
            System.out.println("Updating kanji database...");
            PreparedStatement insertKanji, insertKun, insertOn, insertMean, insertNanori, insertPinyin, insertKoreanr;
            insertKanji = conn.prepareStatement("INSERT INTO kanji(id, kanji, uni1, uni2, grade, radical, freq, stroke, multi_on, multi_kun, multi_mean, multi_nanori) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            insertKun = conn.prepareStatement("INSERT INTO kun VALUES (?, ?)");
            insertOn = conn.prepareStatement("INSERT INTO ont VALUES (?, ?)");
            insertMean = conn.prepareStatement("INSERT INTO mean VALUES (?, ?)");
            insertNanori = conn.prepareStatement("INSERT INTO nanori VALUES (?, ?)");
            insertPinyin = conn.prepareStatement("INSERT INTO pinyin VALUES (?, ?)");
            insertKoreanr = conn.prepareStatement("INSERT INTO koreanr VALUES (?, ?)");
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(fileName);
            Element root = doc.getRootElement();
            List lChara = root.getChildren("character");
            Iterator iter = lChara.iterator();
            int a = 0;
            while (iter.hasNext()) {
                a++;
                if ((a & 0x00ff) == 0) System.out.println(a);
                Element chara = (Element) iter.next();
                ArrayList<String> onReading = new ArrayList<String>();
                ArrayList<String> kunReading = new ArrayList<String>();
                ArrayList<String> Meaning = new ArrayList<String>();
                ArrayList<String> Nanori = new ArrayList<String>();
                String kanji = chara.getChildTextTrim("literal");
                if (kanji.length() > 1) {
                    for (int i = 0; i < kanji.length(); i++) {
                    }
                }
                ;
                Element eRadical = chara.getChild("radical");
                List lRadical = eRadical.getChildren("rad_value");
                Iterator liter = lRadical.iterator();
                String radical = null;
                while (liter.hasNext()) {
                    Element eRadVal = (Element) liter.next();
                    if ("classical".equals(eRadVal.getAttributeValue("rad_type"))) {
                        radical = eRadVal.getText();
                    }
                }
                Element eMisc = chara.getChild("misc");
                String grade = eMisc.getChildText("grade");
                String freq = eMisc.getChildText("freq");
                String sCount = eMisc.getChildText("stroke_count");
                Element eReadMean = chara.getChild("reading_meaning");
                if (eReadMean != null) {
                    Element eRmGroup = eReadMean.getChild("rmgroup");
                    if (eRmGroup != null) {
                        List lReading = eRmGroup.getChildren("reading");
                        Iterator riter = lReading.iterator();
                        while (riter.hasNext()) {
                            Element eReading = (Element) riter.next();
                            if ("ja_on".equals(eReading.getAttributeValue("r_type"))) {
                                String on = JapaneseString.toKatakana(eReading.getTextTrim());
                                if (!onReading.contains(on)) onReading.add(on);
                                try {
                                    insertOn.setInt(1, a);
                                    insertOn.setString(2, JapaneseString.toKatakana(on));
                                    insertOn.executeUpdate();
                                } catch (MySQLIntegrityConstraintViolationException e) {
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if ("ja_kun".equals(eReading.getAttributeValue("r_type"))) {
                                String kun = JapaneseString.toHiragana(eReading.getText());
                                if (!kunReading.contains(kun)) kunReading.add(kun);
                                try {
                                    insertKun.setInt(1, a);
                                    insertKun.setString(2, JapaneseString.toHiragana(kun));
                                    insertKun.executeUpdate();
                                } catch (MySQLIntegrityConstraintViolationException e) {
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if ("pinyin".equals(eReading.getAttributeValue("r_type"))) {
                                String kun = eReading.getText();
                                try {
                                    insertPinyin.setInt(1, a);
                                    insertPinyin.setString(2, kun);
                                    insertPinyin.executeUpdate();
                                } catch (MySQLIntegrityConstraintViolationException e) {
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if ("korean_r".equals(eReading.getAttributeValue("r_type"))) {
                                String kun = eReading.getText();
                                try {
                                    insertKoreanr.setInt(1, a);
                                    insertKoreanr.setString(2, kun);
                                    insertKoreanr.executeUpdate();
                                } catch (MySQLIntegrityConstraintViolationException e) {
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        List lMeaning = eRmGroup.getChildren("meaning");
                        Iterator miter = lMeaning.iterator();
                        while (miter.hasNext()) {
                            Element eMeaning = (Element) miter.next();
                            if (eMeaning.getAttribute("m_lang") == null) {
                                String mean = eMeaning.getText();
                                if (!Meaning.contains(mean)) Meaning.add(mean);
                                try {
                                    insertMean.setInt(1, a);
                                    insertMean.setString(2, mean);
                                    insertMean.executeUpdate();
                                } catch (MySQLIntegrityConstraintViolationException e) {
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    List lNanori = eReadMean.getChildren("nanori");
                    Iterator niter = lNanori.iterator();
                    while (niter.hasNext()) {
                        Element eNanori = (Element) niter.next();
                        String nano = JapaneseString.toHiragana(eNanori.getText());
                        if (!Nanori.contains(nano)) Nanori.add(nano);
                        try {
                            insertNanori.setInt(1, a);
                            insertNanori.setString(2, JapaneseString.toHiragana(nano));
                            insertNanori.executeUpdate();
                        } catch (MySQLIntegrityConstraintViolationException e) {
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                String oo = "", kk = "", mm = "", nn = "";
                for (int i = 0; i < onReading.size(); i++) {
                    if (i > 0) oo += ", ";
                    oo += (String) onReading.get(i);
                }
                for (int i = 0; i < kunReading.size(); i++) {
                    if (i > 0) kk += ", ";
                    kk += (String) kunReading.get(i);
                }
                for (int i = 0; i < Meaning.size(); i++) {
                    if (i > 0) mm += ", ";
                    mm += (String) Meaning.get(i);
                }
                for (int i = 0; i < Nanori.size(); i++) {
                    if (i > 0) nn += ", ";
                    nn += (String) Nanori.get(i);
                }
                if (oo.equals("")) oo = "-";
                if (kk.equals("")) kk = "-";
                if (mm.equals("")) mm = "-";
                if (nn.equals("")) nn = "-";
                try {
                    insertKanji.setInt(1, a);
                    if (kanji.length() > 1) {
                        insertKanji.setString(2, "?");
                        insertKanji.setInt(3, kanji.charAt(0));
                        insertKanji.setInt(4, kanji.charAt(1));
                    } else {
                        insertKanji.setString(2, kanji);
                        insertKanji.setInt(3, kanji.charAt(0));
                        insertKanji.setString(4, null);
                    }
                    insertKanji.setString(5, grade);
                    insertKanji.setString(6, radical);
                    insertKanji.setString(7, freq);
                    insertKanji.setString(8, sCount);
                    insertKanji.setString(9, oo);
                    insertKanji.setString(10, kk);
                    insertKanji.setString(11, mm);
                    insertKanji.setString(12, nn);
                    insertKanji.executeUpdate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            insertKanji.close();
            insertKun.close();
            insertOn.close();
            insertMean.close();
            insertNanori.close();
            insertPinyin.close();
            insertKoreanr.close();
            conn.close();
            System.out.println("COMPLETED");
        } catch (Exception e) {
            System.out.println("FAILED");
            e.printStackTrace();
        }
    }

    private static String byteArrayToHexString(byte in[]) {
        byte ch = 0x00;
        int i = 0;
        if (in == null || in.length <= 0) return null;
        String pseudo[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };
        StringBuffer out = new StringBuffer(in.length * 2);
        while (i < in.length) {
            ch = (byte) (in[i] & 0xF0);
            ch = (byte) (ch >>> 4);
            ch = (byte) (ch & 0x0F);
            out.append(pseudo[(int) ch]);
            ch = (byte) (in[i] & 0x0F);
            out.append(pseudo[(int) ch]);
            i++;
        }
        String rslt = new String(out);
        return rslt;
    }
}
