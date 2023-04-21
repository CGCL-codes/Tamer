package net.sf.zorobot.ext.db.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import net.sf.zorobot.core.ZorobotSystem;
import net.sf.zorobot.ext.db.KanjiDAO;
import net.sf.zorobot.ext.db.KanjiDAOFactory;
import net.sf.zorobot.util.JapaneseCharacter;
import net.sf.zorobot.util.KReading;
import net.sf.zorobot.util.ReadingAnalyzer;

public class MySqlExWordBuilder {

    Connection conn;

    KanjiDAO kanjiDAO = KanjiDAOFactory.getDAO();

    public static void main(String[] args) {
        try {
            MySqlExWordBuilder ew = new MySqlExWordBuilder();
            ew.prepareTable();
            ew.rebuildExWord();
            ZorobotSystem.info("Example words completed");
        } catch (Exception e) {
            ZorobotSystem.exception(e);
            ZorobotSystem.error("Couldn't build example words");
        }
    }

    public MySqlExWordBuilder() throws Exception {
        conn = MySqlFactory.getConnection(ZorobotSystem.props);
    }

    public void prepareTable() throws Exception {
        Statement stmt = conn.createStatement();
        try {
            stmt.execute("DELETE FROM exword");
        } catch (Exception e) {
            try {
                stmt.execute("CREATE TABLE `exword` (" + "  `kanji` char(1) default NULL," + "  `word` varchar(64) default NULL," + "  `kanjipos` tinyint(4) default NULL," + "  `type` tinyint(4) default NULL," + "  `readingtype` tinyint(4) default NULL," + "  `morph` tinyint(4) default NULL," + "  `fullreading` varchar(64) default NULL," + "  `reading` varchar(32) default NULL," + "  `rposstart` tinyint(4) default NULL," + "  `rposend` tinyint(4) default NULL," + "  `mingrade` tinyint(4) default NULL," + "  `maxgrade` tinyint(4) default NULL," + "  `len` tinyint(4) default NULL," + "  KEY `kanji` (`kanji`,`reading`,`mingrade`,`maxgrade`)" + ") ENGINE=MyISAM DEFAULT CHARSET=ucs2 ROW_FORMAT=DYNAMIC;");
            } catch (Exception ee) {
                ZorobotSystem.exception(ee);
                ZorobotSystem.error("Couldn't create table exword, " + ee.getMessage());
            }
        }
    }

    public void rebuildExWord() {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT word, reading FROM jword WHERE reading <> ''");
            ResultSet rs = ps.executeQuery();
            int counter = 0;
            while (rs.next()) {
                String word = rs.getString(1);
                String reading = rs.getString(2);
                boolean isKanji[] = new boolean[word.length()];
                int kanjiGrade[] = new int[isKanji.length];
                int kj = 0;
                for (int i = 0; i < isKanji.length; i++) {
                    isKanji[i] = JapaneseCharacter.isKanji(word.charAt(i));
                    if (isKanji[i]) {
                        kj++;
                        kanjiGrade[i] = kanjiDAO.getKanjiGrade(word.substring(i, i + 1));
                    }
                }
                if (kj > 0) {
                    System.out.println(counter++);
                    KReading[] result = ReadingAnalyzer.analyzeReadingStub(word, reading, kanjiDAO);
                    if (result != null) {
                        int rpos = 0;
                        for (int i = 0; i < result.length; i++) {
                            if (isKanji[i]) {
                                String kanji = word.substring(i, i + 1);
                                int type = 0;
                                if (kj < result.length) type = 1;
                                int morph = 0;
                                if (!result[i].reading.equals(result[i].regularReading)) morph = 1;
                                int minKanjiGrade = 10;
                                int maxKanjiGrade = 1;
                                if (kj == 1) {
                                    minKanjiGrade = maxKanjiGrade = 0;
                                } else {
                                    for (int j = 0; j < kanjiGrade.length; j++) {
                                        if (j != i) {
                                            if (kanjiGrade[j] > 0) {
                                                if (kanjiGrade[j] < minKanjiGrade) minKanjiGrade = kanjiGrade[j];
                                                if (kanjiGrade[j] > maxKanjiGrade) maxKanjiGrade = kanjiGrade[j];
                                            }
                                        }
                                    }
                                }
                                addExWord(kanji, word, i, type, result[i].type, morph, reading, result[i].regularReading, rpos, rpos + result[i].reading.length(), minKanjiGrade, maxKanjiGrade, word.length());
                            }
                            rpos += result[i].reading.length();
                        }
                    }
                }
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            ZorobotSystem.exception(e);
        }
    }

    public boolean addExWord(String kanji, String word, int kanjipos, int type, int readingtype, int morph, String fullreading, String reading, int rposstart, int rposend, int mingrade, int maxgrade, int len) {
        boolean toReturn = false;
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO exword VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, kanji);
            ps.setString(2, word);
            ps.setInt(3, kanjipos);
            ps.setInt(4, type);
            ps.setInt(5, readingtype);
            ps.setInt(6, morph);
            ps.setString(7, fullreading);
            ps.setString(8, reading);
            ps.setInt(9, rposstart);
            ps.setInt(10, rposend);
            ps.setInt(11, mingrade);
            ps.setInt(12, maxgrade);
            ps.setInt(13, len);
            int a = ps.executeUpdate();
            if (a == 1) toReturn = true;
            ps.close();
        } catch (Exception e) {
            ZorobotSystem.exception(e);
        }
        return toReturn;
    }
}
