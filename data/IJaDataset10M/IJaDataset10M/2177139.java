package net.sf.l2j;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public final class ExternalConfig {

    protected static final Logger _log = Logger.getLogger(Config.class.getName());

    /** Properties file for Character Configurations */
    public static final String GRANDBOSS_CONFIG_FILE = "./config/bosses/Grandboss.properties";

    /** Extneral Config Settings **/
    public static int load = 0;

    /** GrandBoss Settings **/
    public static int Antharas_Wait_Time;

    public static int Valakas_Wait_Time;

    public static int Interval_Of_Antharas_Spawn;

    public static int Random_Of_Antharas_Spawn;

    public static int Interval_Of_Valakas_Spawn;

    public static int Random_Of_Valakas_Spawn;

    public static int Interval_Of_Baium_Spawn;

    public static int Random_Of_Baium_Spawn;

    public static int Interval_Of_Core_Spawn;

    public static int Random_Of_Core_Spawn;

    public static int Interval_Of_Orfen_Spawn;

    public static int Random_Of_Orfen_Spawn;

    public static int Interval_Of_QueenAnt_Spawn;

    public static int Random_Of_QueenAnt_Spawn;

    public static int Interval_Of_Zaken_Spawn;

    public static int Random_Of_Zaken_Spawn;

    public static int Interval_Of_Frintezza_Spawn;

    public static int Random_Of_Frintezza_Spawn;

    public static int Interval_Of_Sailren_Spawn;

    public static int Random_Of_Sailren_Spawn;

    public static int HPH_FIXINTERVALOFHALTER;

    public static int HPH_RANDOMINTERVALOFHALTER;

    public static int HPH_APPTIMEOFHALTER;

    public static int HPH_ACTIVITYTIMEOFHALTER;

    public static int HPH_FIGHTTIMEOFHALTER;

    public static int HPH_CALLROYALGUARDHELPERCOUNT;

    public static int HPH_CALLROYALGUARDHELPERINTERVAL;

    public static int HPH_INTERVALOFDOOROFALTER;

    public static int HPH_TIMEOFLOCKUPDOOROFALTAR;

    public static boolean FWS_ENABLESINGLEPLAYER;

    public static int FWS_FIXINTERVALOFSAILRENSPAWN;

    public static int FWS_RANDOMINTERVALOFSAILRENSPAWN;

    public static int FWS_INTERVALOFNEXTMONSTER;

    public static int FWS_ACTIVITYTIMEOFMOBS;

    public static void loadconfig() {
        InputStream is = null;
        if (load == 1) return;
        try {
            Properties grandbossSettings = new Properties();
            is = new FileInputStream(new File(GRANDBOSS_CONFIG_FILE));
            grandbossSettings.load(is);
            Antharas_Wait_Time = Integer.parseInt(grandbossSettings.getProperty("AntharasWaitTime", "30"));
            if (Antharas_Wait_Time < 3 || Antharas_Wait_Time > 60) Antharas_Wait_Time = 30;
            Antharas_Wait_Time = Antharas_Wait_Time * 60000;
            Valakas_Wait_Time = Integer.parseInt(grandbossSettings.getProperty("ValakasWaitTime", "20"));
            if (Valakas_Wait_Time < 3 || Valakas_Wait_Time > 60) Valakas_Wait_Time = 20;
            Valakas_Wait_Time = Valakas_Wait_Time * 60000;
            Interval_Of_Antharas_Spawn = Integer.parseInt(grandbossSettings.getProperty("IntervalOfAntharasSpawn", "192"));
            if (Interval_Of_Antharas_Spawn < 1 || Interval_Of_Antharas_Spawn > 480) Interval_Of_Antharas_Spawn = 192;
            Interval_Of_Antharas_Spawn = Interval_Of_Antharas_Spawn * 3600000;
            Random_Of_Antharas_Spawn = Integer.parseInt(grandbossSettings.getProperty("RandomOfAntharasSpawn", "145"));
            if (Random_Of_Antharas_Spawn < 1 || Random_Of_Antharas_Spawn > 192) Random_Of_Antharas_Spawn = 145;
            Random_Of_Antharas_Spawn = Random_Of_Antharas_Spawn * 3600000;
            Interval_Of_Valakas_Spawn = Integer.parseInt(grandbossSettings.getProperty("IntervalOfValakasSpawn", "192"));
            if (Interval_Of_Valakas_Spawn < 1 || Interval_Of_Valakas_Spawn > 480) Interval_Of_Valakas_Spawn = 192;
            Interval_Of_Valakas_Spawn = Interval_Of_Valakas_Spawn * 3600000;
            Random_Of_Valakas_Spawn = Integer.parseInt(grandbossSettings.getProperty("RandomOfValakasSpawn", "145"));
            if (Random_Of_Valakas_Spawn < 1 || Random_Of_Valakas_Spawn > 192) Random_Of_Valakas_Spawn = 145;
            Random_Of_Valakas_Spawn = Random_Of_Valakas_Spawn * 3600000;
            Interval_Of_Baium_Spawn = Integer.parseInt(grandbossSettings.getProperty("IntervalOfBaiumSpawn", "121"));
            if (Interval_Of_Baium_Spawn < 1 || Interval_Of_Baium_Spawn > 480) Interval_Of_Baium_Spawn = 121;
            Interval_Of_Baium_Spawn = Interval_Of_Baium_Spawn * 3600000;
            Random_Of_Baium_Spawn = Integer.parseInt(grandbossSettings.getProperty("RandomOfBaiumSpawn", "8"));
            if (Random_Of_Baium_Spawn < 1 || Random_Of_Baium_Spawn > 192) Random_Of_Baium_Spawn = 8;
            Random_Of_Baium_Spawn = Random_Of_Baium_Spawn * 3600000;
            Interval_Of_Core_Spawn = Integer.parseInt(grandbossSettings.getProperty("IntervalOfCoreSpawn", "27"));
            if (Interval_Of_Core_Spawn < 1 || Interval_Of_Core_Spawn > 480) Interval_Of_Core_Spawn = 27;
            Interval_Of_Core_Spawn = Interval_Of_Core_Spawn * 3600000;
            Random_Of_Core_Spawn = Integer.parseInt(grandbossSettings.getProperty("RandomOfCoreSpawn", "47"));
            if (Random_Of_Core_Spawn < 1 || Random_Of_Core_Spawn > 192) Random_Of_Core_Spawn = 47;
            Random_Of_Core_Spawn = Random_Of_Core_Spawn * 3600000;
            Interval_Of_Orfen_Spawn = Integer.parseInt(grandbossSettings.getProperty("IntervalOfOrfenSpawn", "28"));
            if (Interval_Of_Orfen_Spawn < 1 || Interval_Of_Orfen_Spawn > 480) Interval_Of_Orfen_Spawn = 28;
            Interval_Of_Orfen_Spawn = Interval_Of_Orfen_Spawn * 3600000;
            Random_Of_Orfen_Spawn = Integer.parseInt(grandbossSettings.getProperty("RandomOfOrfenSpawn", "41"));
            if (Random_Of_Orfen_Spawn < 1 || Random_Of_Orfen_Spawn > 192) Random_Of_Orfen_Spawn = 41;
            Random_Of_Orfen_Spawn = Random_Of_Orfen_Spawn * 3600000;
            Interval_Of_QueenAnt_Spawn = Integer.parseInt(grandbossSettings.getProperty("IntervalOfQueenAntSpawn", "19"));
            if (Interval_Of_QueenAnt_Spawn < 1 || Interval_Of_QueenAnt_Spawn > 480) Interval_Of_QueenAnt_Spawn = 19;
            Interval_Of_QueenAnt_Spawn = Interval_Of_QueenAnt_Spawn * 3600000;
            Random_Of_QueenAnt_Spawn = Integer.parseInt(grandbossSettings.getProperty("RandomOfQueenAntSpawn", "35"));
            if (Random_Of_QueenAnt_Spawn < 1 || Random_Of_QueenAnt_Spawn > 192) Random_Of_QueenAnt_Spawn = 35;
            Random_Of_QueenAnt_Spawn = Random_Of_QueenAnt_Spawn * 3600000;
            Interval_Of_Zaken_Spawn = Integer.parseInt(grandbossSettings.getProperty("IntervalOfZakenSpawn", "19"));
            if (Interval_Of_Zaken_Spawn < 1 || Interval_Of_Zaken_Spawn > 480) Interval_Of_Zaken_Spawn = 19;
            Interval_Of_Zaken_Spawn = Interval_Of_Zaken_Spawn * 3600000;
            Random_Of_Zaken_Spawn = Integer.parseInt(grandbossSettings.getProperty("RandomOfZakenSpawn", "35"));
            if (Random_Of_Zaken_Spawn < 1 || Random_Of_Zaken_Spawn > 192) Random_Of_Zaken_Spawn = 35;
            Random_Of_Zaken_Spawn = Random_Of_Zaken_Spawn * 3600000;
            Interval_Of_Frintezza_Spawn = Integer.parseInt(grandbossSettings.getProperty("IntervalOfFrintezzaSpawn", "121"));
            if (Interval_Of_Frintezza_Spawn < 1 || Interval_Of_Frintezza_Spawn > 480) Interval_Of_Frintezza_Spawn = 121;
            Interval_Of_Frintezza_Spawn = Interval_Of_Frintezza_Spawn * 3600000;
            Random_Of_Frintezza_Spawn = Integer.parseInt(grandbossSettings.getProperty("RandomOfFrintezzaSpawn", "8"));
            if (Random_Of_Frintezza_Spawn < 1 || Random_Of_Frintezza_Spawn > 192) Random_Of_Frintezza_Spawn = 8;
            Random_Of_Frintezza_Spawn = Random_Of_Frintezza_Spawn * 3600000;
            HPH_FIXINTERVALOFHALTER = Integer.parseInt(grandbossSettings.getProperty("FixIntervalOfHalter", "172800"));
            if (HPH_FIXINTERVALOFHALTER < 300 || HPH_FIXINTERVALOFHALTER > 864000) HPH_FIXINTERVALOFHALTER = 172800;
            HPH_RANDOMINTERVALOFHALTER = Integer.parseInt(grandbossSettings.getProperty("RandomIntervalOfHalter", "86400"));
            if (HPH_RANDOMINTERVALOFHALTER < 300 || HPH_RANDOMINTERVALOFHALTER > 864000) HPH_RANDOMINTERVALOFHALTER = 86400;
            HPH_APPTIMEOFHALTER = Integer.parseInt(grandbossSettings.getProperty("AppTimeOfHalter", "20"));
            if (HPH_APPTIMEOFHALTER < 5 || HPH_APPTIMEOFHALTER > 60) HPH_APPTIMEOFHALTER = 20;
            HPH_ACTIVITYTIMEOFHALTER = Integer.parseInt(grandbossSettings.getProperty("ActivityTimeOfHalter", "21600"));
            if (HPH_ACTIVITYTIMEOFHALTER < 7200 || HPH_ACTIVITYTIMEOFHALTER > 86400) HPH_ACTIVITYTIMEOFHALTER = 21600;
            HPH_FIGHTTIMEOFHALTER = Integer.parseInt(grandbossSettings.getProperty("FightTimeOfHalter", "7200"));
            if (HPH_FIGHTTIMEOFHALTER < 7200 || HPH_FIGHTTIMEOFHALTER > 21600) HPH_FIGHTTIMEOFHALTER = 7200;
            HPH_CALLROYALGUARDHELPERCOUNT = Integer.parseInt(grandbossSettings.getProperty("CallRoyalGuardHelperCount", "6"));
            if (HPH_CALLROYALGUARDHELPERCOUNT < 1 || HPH_CALLROYALGUARDHELPERCOUNT > 6) HPH_CALLROYALGUARDHELPERCOUNT = 6;
            HPH_CALLROYALGUARDHELPERINTERVAL = Integer.parseInt(grandbossSettings.getProperty("CallRoyalGuardHelperInterval", "10"));
            if (HPH_CALLROYALGUARDHELPERINTERVAL < 1 || HPH_CALLROYALGUARDHELPERINTERVAL > 60) HPH_CALLROYALGUARDHELPERINTERVAL = 10;
            HPH_INTERVALOFDOOROFALTER = Integer.parseInt(grandbossSettings.getProperty("IntervalOfDoorOfAlter", "5400"));
            if (HPH_INTERVALOFDOOROFALTER < 60 || HPH_INTERVALOFDOOROFALTER > 5400) HPH_INTERVALOFDOOROFALTER = 5400;
            HPH_TIMEOFLOCKUPDOOROFALTAR = Integer.parseInt(grandbossSettings.getProperty("TimeOfLockUpDoorOfAltar", "180"));
            if (HPH_TIMEOFLOCKUPDOOROFALTAR < 60 || HPH_TIMEOFLOCKUPDOOROFALTAR > 600) HPH_TIMEOFLOCKUPDOOROFALTAR = 180;
            FWS_ENABLESINGLEPLAYER = Boolean.parseBoolean(grandbossSettings.getProperty("EnableSinglePlayer", "False"));
            FWS_FIXINTERVALOFSAILRENSPAWN = Integer.parseInt(grandbossSettings.getProperty("FixIntervalOfSailrenSpawn", "1440"));
            if (FWS_FIXINTERVALOFSAILRENSPAWN < 5 || FWS_FIXINTERVALOFSAILRENSPAWN > 2880) FWS_FIXINTERVALOFSAILRENSPAWN = 1440;
            FWS_FIXINTERVALOFSAILRENSPAWN = FWS_FIXINTERVALOFSAILRENSPAWN * 60000;
            FWS_RANDOMINTERVALOFSAILRENSPAWN = Integer.parseInt(grandbossSettings.getProperty("RandomIntervalOfSailrenSpawn", "1440"));
            if (FWS_RANDOMINTERVALOFSAILRENSPAWN < 5 || FWS_RANDOMINTERVALOFSAILRENSPAWN > 2880) FWS_RANDOMINTERVALOFSAILRENSPAWN = 1440;
            FWS_RANDOMINTERVALOFSAILRENSPAWN = FWS_RANDOMINTERVALOFSAILRENSPAWN * 60000;
            FWS_INTERVALOFNEXTMONSTER = Integer.parseInt(grandbossSettings.getProperty("IntervalOfNextMonster", "1"));
            if (FWS_INTERVALOFNEXTMONSTER < 1 || FWS_INTERVALOFNEXTMONSTER > 10) FWS_INTERVALOFNEXTMONSTER = 1;
            FWS_INTERVALOFNEXTMONSTER = FWS_INTERVALOFNEXTMONSTER * 60000;
            FWS_ACTIVITYTIMEOFMOBS = Integer.parseInt(grandbossSettings.getProperty("ActivityTimeOfMobs", "120"));
            if (FWS_ACTIVITYTIMEOFMOBS < 1 || FWS_ACTIVITYTIMEOFMOBS > 120) FWS_ACTIVITYTIMEOFMOBS = 120;
            FWS_ACTIVITYTIMEOFMOBS = FWS_ACTIVITYTIMEOFMOBS * 60000;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Error("Failed to Load " + GRANDBOSS_CONFIG_FILE + " File.");
        }
        load = 1;
    }
}
