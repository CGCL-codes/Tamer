package net.sourceforge.ck2httt.cv;

import java.io.File;
import java.io.IOException;
import net.sourceforge.ck2httt.ck.Analyzer;
import net.sourceforge.ck2httt.ck.Characters;
import net.sourceforge.ck2httt.ck.County;
import net.sourceforge.ck2httt.eu3.EU3Wars;
import net.sourceforge.ck2httt.eu3.EU3Wars.Event;
import net.sourceforge.ck2httt.pxAnalyzer.PXTree.BaseField;
import net.sourceforge.ck2httt.pxAnalyzer.PXTree.Field;
import net.sourceforge.ck2httt.pxAnalyzer.PXTree.StructField;
import net.sourceforge.ck2httt.utils.Dates;

public class CvWars {

    private static String __destPath;

    public static void setOutputPath(String path) throws IOException {
        __destPath = path;
        File dir = new File(path);
        if (dir.exists()) {
        } else if (!dir.mkdirs()) throw new IOException("Unable to create directory : " + dir);
    }

    private static void convertWars() {
        StructField global = Analyzer.__root.getStruct("globaldata");
        for (Field<?> fx : global._data) {
            if (!fx.name().equals("war")) continue;
            StructField f = (StructField) fx;
            Characters attacker = Characters.search(f.getStruct("attacker"));
            Characters defender = Characters.search(f.getStruct("defender"));
            if (attacker == null || defender == null) continue;
            CvCountry att = CvCountry.search(attacker._tag);
            CvCountry def = CvCountry.search(defender._tag);
            Field<?>[] battlesX = f.getAll("battle_result");
            EU3Wars.Event[] battles = new EU3Wars.Event[0];
            if (null != battlesX) {
                battles = new EU3Wars.Event[battlesX.length];
                for (int i = 0; i < battlesX.length; i++) {
                    Field<?> batX = battlesX[i];
                    StructField bat = (StructField) batX;
                    BaseField prov = (BaseField) bat.getBase("province");
                    String batName = County.getCountyName(prov.get());
                    String batLocation = "1";
                    BaseField aggAttX = (BaseField) bat.getBase("aggressor_is_attacker");
                    boolean aggAtt = "yes".equals(aggAttX.get());
                    BaseField attWonX = (BaseField) bat.getBase("attacker_won");
                    boolean attWon = "yes".equals(attWonX);
                    boolean batLoss = !(aggAtt && attWon);
                    StructField endDate = bat.getStruct("enddate");
                    short year = Short.parseShort(endDate.getBase("year").get());
                    byte month = Dates.getMonth(endDate.getBase("month").get());
                    byte day = Byte.parseByte(endDate.getBase("day").get());
                    int batAttInf = 1000;
                    int batAttCav = 1000;
                    int batAttArt = 0;
                    int batAttLossPercent = 50;
                    int batDefInf = 1000;
                    int batDefCav = 1000;
                    int batDefArt = 0;
                    int batDefLossPercent = 50;
                    battles[i] = new Event(year, month, day, batName, batLocation, batAttInf, batAttCav, batAttArt, batAttLossPercent, batDefInf, batDefCav, batDefArt, batDefLossPercent, batLoss);
                }
            }
            if (att == null || att._eu3Tag == null || def == null || def._eu3Tag == null) continue;
            EU3Wars.addWar(att._eu3Tag, def._eu3Tag, battles);
            System.out.format("war : %s against %s (%d battles)\n", att._eu3Tag, def._eu3Tag, battles.length);
        }
    }

    public static void preWriteAll() throws IOException {
        convertWars();
    }

    public static void writeAll() throws IOException {
        EU3Wars.write(__destPath + File.separatorChar + Analyzer.getSaveFile());
    }
}
