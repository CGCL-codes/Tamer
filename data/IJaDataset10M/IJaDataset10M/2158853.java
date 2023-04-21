package net.sourceforge.btthud.engine;

import net.sourceforge.btthud.data.*;
import javax.swing.*;
import java.util.*;
import net.sourceforge.btthud.util.*;

public class MUParse {

    private final JTextPaneWriter textPaneWriter;

    private final BulkStyledDocument doc;

    private final ANSIEmulation emulation = new ANSIEmulation();

    MUData data = null;

    MUPrefs prefs = null;

    MUCommands commands = null;

    String sessionKey;

    String hudInfoStart = new String("#HUD:");

    /**
	 * Constructor.
	 */
    public MUParse(final JTextPaneWriter textPaneWriter, final MUData data, final MUPrefs prefs) {
        this.textPaneWriter = textPaneWriter;
        doc = (BulkStyledDocument) textPaneWriter.getDocument();
        this.data = data;
        this.prefs = prefs;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String newKey) {
        sessionKey = newKey;
        hudInfoStart = new String("#HUD:" + sessionKey + ":").intern();
    }

    public void setCommands(MUCommands commands) {
        this.commands = commands;
    }

    /**
	 * Check to see if a line needs to be matched, then insert it into the
	 * document.
	 *
	 * @param l The line we are parsing
	 */
    void parseLine(final String l) {
        if (l == null) return;
        try {
            if (matchHudInfoCommand(l)) return;
            final List<ANSIEmulation.StyledString> styledLine = emulation.parseLine(l, data.mainWindowMuted);
            matchForCommandSending(l);
            if (!data.mainWindowMuted) {
                for (final ANSIEmulation.StyledString element : styledLine) {
                    printStyledString(element);
                }
                textPaneWriter.println();
            }
        } catch (final Exception e) {
            System.out.println("Error: parseLine: " + e);
        }
    }

    private void printStyledString(final ANSIEmulation.StyledString str) {
        final String style = str.style;
        final String text = str.text;
        if (!textPaneWriter.hasStyle(style)) {
            textPaneWriter.addStyle(style, ANSIEmulation.getStyle(style));
        }
        textPaneWriter.print(text, style);
    }

    /**
	 * Just like parseLine, but designed for messages from the HUD. These
	 * don't need to be matched, so we can save ourselves some CPU time.
	 *
	 * @param l The line that we are parsing
	 */
    public void messageLine(String l) {
        textPaneWriter.println();
        textPaneWriter.println(l, BulkStyledDocument.STYLE_HUD_MESSAGE);
    }

    public boolean matchForCommandSending(String l) {
        if (l.startsWith("Pos changed to ") && data.hudRunning) commands.forceTactical();
        return false;
    }

    public boolean matchHudInfoCommand(String l) {
        if (l.startsWith(hudInfoStart)) {
            synchronized (data) {
                data.lastDataTime = System.currentTimeMillis();
                StringTokenizer st = new StringTokenizer(l);
                String firstWord = st.nextToken();
                StringTokenizer st2 = new StringTokenizer(firstWord, ":");
                st2.nextToken();
                st2.nextToken();
                String whichCommand = st2.nextToken().intern();
                StringBuffer restOfCommandBuf = new StringBuffer(st.nextToken());
                while (st.hasMoreTokens()) {
                    restOfCommandBuf.append(" ");
                    restOfCommandBuf.append(st.nextToken());
                }
                String restOfCommand = restOfCommandBuf.toString().intern();
                if (data.hudStarted && data.hudRunning && (whichCommand == "???" || restOfCommand == "Not in a BattleTech unit")) {
                    data.hudRunning = false;
                    messageLine("*** Display suspended: " + restOfCommand + " ****");
                }
                if (data.hudStarted && data.hudRunning && (restOfCommand == "You are destroyed!")) {
                    data.hudRunning = false;
                    data.hudStarted = false;
                    commands.endTimers();
                    messageLine("*** Display Stopped: Unit Destroyed ***");
                }
                if (whichCommand == "GS") parseHudInfoGS(restOfCommand); else if (whichCommand == "C") parseHudInfoC(restOfCommand); else if (whichCommand == "CB") parseHudInfoCB(restOfCommand); else if (whichCommand == "T") {
                    String subCommand = st2.nextToken().intern();
                    if (subCommand == "S#") parseHudInfoTS(restOfCommand); else if (subCommand == "L#") parseHudInfoTL(restOfCommand); else if (subCommand == "D#") parseHudInfoTD(restOfCommand);
                } else if (whichCommand == "SGI") parseHudInfoSGI(restOfCommand); else if (whichCommand == "CO") parseHudInfoCO(restOfCommand); else if (whichCommand == "AS") parseHudInfoAS(restOfCommand); else if (whichCommand == "OAS") parseHudInfoOAS(restOfCommand); else if (whichCommand == "KEY") return true; else if (whichCommand == "WL") parseHudInfoWL(restOfCommand); else if (whichCommand == "WE") parseHudInfoWE(restOfCommand); else if (whichCommand == "AM") parseHudInfoAM(restOfCommand); else messageLine("> Unrecognized HUDINFO data: " + whichCommand);
                return true;
            }
        } else if (l.startsWith("#HUD hudinfo")) {
            parseHudInfoVersion(l);
            return true;
        } else if (l.startsWith("#HUD:")) {
            return true;
        }
        return false;
    }

    /**
      * Parse a HUDINFO version number.
      * @param l The entire string line.
      */
    public void parseHudInfoVersion(String l) {
        StringTokenizer st = new StringTokenizer(l);
        st.nextToken();
        st.nextToken();
        st.nextToken();
        StringTokenizer st2 = new StringTokenizer(st.nextToken(), ".");
        data.setHudInfoMajorVersion(Integer.parseInt(st2.nextToken()));
        data.setHudInfoMinorVersion(Integer.parseInt(st2.nextToken()));
    }

    /**
      * Parse a string that represents 'general status.'
      * @param l The data from the hudinfo command, minus the header with the key.
      */
    public void parseHudInfoGS(String l) {
        try {
            StringTokenizer st = new StringTokenizer(l, ",");
            MUMyInfo info = data.myUnit;
            String tempStr;
            if (info == null) info = new MUMyInfo();
            info.id = st.nextToken();
            final int hex_x = Integer.parseInt(st.nextToken());
            final int hex_y = Integer.parseInt(st.nextToken());
            final int hex_z = Integer.parseInt(st.nextToken());
            info.heading = Integer.parseInt(st.nextToken());
            info.desiredHeading = Integer.parseInt(st.nextToken());
            info.speed = Float.parseFloat(st.nextToken());
            info.desiredSpeed = Float.parseFloat(st.nextToken());
            info.heat = Integer.parseInt(st.nextToken());
            info.heatDissipation = Integer.parseInt(st.nextToken());
            tempStr = st.nextToken().intern();
            if (tempStr != "-") info.fuel = Integer.parseInt(tempStr);
            info.verticalSpeed = Float.parseFloat(st.nextToken());
            info.desiredVerticalSpeed = Float.parseFloat(st.nextToken());
            final float rtc = Float.parseFloat(st.nextToken());
            final int ibtc = Integer.parseInt(st.nextToken());
            info.position.setFromCenterLocation(hex_x, hex_y, hex_z, rtc, ibtc);
            tempStr = st.nextToken().intern();
            if (tempStr != "-") info.turretHeading = Integer.parseInt(tempStr);
            if (st.hasMoreTokens()) info.status = st.nextToken(); else info.status = "";
            if (data.hiSupportsOwnJumpInfo()) {
                tempStr = st.nextToken().intern();
                if (tempStr != "-") {
                    info.jumping = true;
                    info.jumpTargetX = Integer.parseInt(tempStr);
                    info.jumpTargetY = Integer.parseInt(st.nextToken());
                }
            }
            String sTk = "";
            while (st.hasMoreTokens()) {
                sTk = st.nextToken();
            }
            if (sTk.length() > 0) {
                int i, iSize;
                MUUnitInfo uiUnit;
                iSize = 0;
                if (data.contacts.size() > 0) iSize = data.contacts.size();
                for (i = 0; i < iSize; ++i) {
                    uiUnit = data.contacts.get(i);
                    if (uiUnit.id.equals(sTk)) {
                        uiUnit.target = true;
                        data.myUnit.setTargettedUnit(sTk);
                    } else uiUnit.target = false;
                }
            }
        } catch (Exception e) {
            System.out.println("Error: parseHudInfoGS: " + e);
        }
    }

    /**
      * Parse a string which represents 'static general information' - usually 1 time only.
      * @param l The string, minus the header.
      */
    public void parseHudInfoSGI(String l) {
        StringTokenizer st = new StringTokenizer(l, ",");
        MUMyInfo info = data.myUnit;
        String tempStr;
        if (info == null) info = new MUMyInfo();
        info.type = st.nextToken();
        if (info.type.equals("I")) prefs.hudinfoTacHeight = 5; else prefs.hudinfoTacHeight = 40;
        if (st.hasMoreTokens()) {
            String ref = st.nextToken();
            if (ref.compareToIgnoreCase(info.ref) != 0 && info.ref.length() > 1) {
                messageLine("*** Unit Change Detected - Refreshing Data ***");
                data.clearData();
            }
            info.ref = ref;
            info.name = st.nextToken();
            info.walkSpeed = Float.parseFloat(st.nextToken());
            info.runSpeed = Float.parseFloat(st.nextToken());
            info.backSpeed = Float.parseFloat(st.nextToken());
            info.maxVerticalSpeed = Float.parseFloat(st.nextToken());
            tempStr = st.nextToken().intern();
            if (tempStr != "-") info.maxFuel = Integer.parseInt(tempStr);
            info.heatSinks = Integer.parseInt(st.nextToken());
            if (st.hasMoreTokens()) info.advTech = st.nextToken();
            if (data.hudRunning == false) {
                messageLine("*** Display Resumed ***");
                data.hudRunning = true;
                commands.forceTactical();
                data.lastDataTime = 0;
            }
        }
    }

    /**
     * Parse a string which represents conditions/weather information
     * @param l The string, minus the header.
     */
    public void parseHudInfoCO(String l) {
        StringTokenizer st = new StringTokenizer(l, ",");
        MUWeather weather = data.weather;
        String tempStr;
        if (weather == null) weather = new MUWeather();
        tempStr = st.nextToken().intern();
        if (tempStr == "D") weather.light = MUWeather.LIGHT_DAY; else if (tempStr == "T") weather.light = MUWeather.LIGHT_DAWN_DUSK; else if (tempStr == "N") weather.light = MUWeather.LIGHT_NIGHT;
        weather.visibility = Integer.parseInt(st.nextToken());
        weather.gravity = Integer.parseInt(st.nextToken());
        weather.ambientTemperature = Integer.parseInt(st.nextToken());
        tempStr = st.nextToken();
        if (tempStr.contains("V")) weather.isVacuum = true;
        if (tempStr.contains("U")) weather.isUnderground = true;
        if (tempStr.contains("D")) weather.isDark = true;
    }

    /**
      * Parse a string which represents a single contact.
      * @param l The contact information string, minus the header.
      */
    public void parseHudInfoC(String l) {
        if (l == "Done") return;
        try {
            StringTokenizer st = new StringTokenizer(l, ",");
            MUUnitInfo con = new MUUnitInfo(prefs);
            String tempStr;
            con.id = st.nextToken();
            con.friend = Character.isLowerCase(con.id.charAt(0));
            con.target = false;
            con.arc = st.nextToken();
            tempStr = st.nextToken().intern();
            if (tempStr == "PS") {
                con.primarySensor = true;
                con.secondarySensor = true;
            } else if (tempStr == "P") {
                con.primarySensor = true;
                con.secondarySensor = false;
            } else if (tempStr == "S") {
                con.primarySensor = false;
                con.secondarySensor = true;
            } else {
                con.primarySensor = false;
                con.secondarySensor = false;
            }
            if (data.hiSupportsAllArgumentHudinfo()) con.type = st.nextToken(); else {
                if (!con.primarySensor && !con.secondarySensor) {
                    if (!con.primarySensor && !con.secondarySensor) con.type = tempStr; else con.type = st.nextToken();
                }
            }
            con.name = st.nextToken().intern();
            if (con.name == "-") con.name = "Unknown";
            final int hex_x = Integer.parseInt(st.nextToken());
            final int hex_y = Integer.parseInt(st.nextToken());
            final int hex_z = Integer.parseInt(st.nextToken());
            con.range = Float.parseFloat(st.nextToken());
            con.bearing = Integer.parseInt(st.nextToken());
            con.speed = Float.parseFloat(st.nextToken());
            con.verticalSpeed = Float.parseFloat(st.nextToken());
            con.heading = Integer.parseInt(st.nextToken());
            tempStr = st.nextToken().intern();
            if (tempStr != "-") {
                con.jumpHeading = Integer.parseInt(tempStr);
                con.jumping = true;
            } else {
                con.jumpHeading = 0;
                con.jumping = false;
            }
            final float rtc = Float.parseFloat(st.nextToken());
            final int ibtc = Integer.parseInt(st.nextToken());
            con.position.setFromCenterLocation(hex_x, hex_y, hex_z, rtc, ibtc);
            con.weight = Integer.parseInt(st.nextToken());
            con.apparentHeat = Integer.parseInt(st.nextToken());
            if (data.hiSupportsAllArgumentHudinfo()) {
                tempStr = st.nextToken();
                if (tempStr.equals("-")) con.status = ""; else con.status = new String(tempStr);
            } else {
                if (st.hasMoreTokens()) con.status = st.nextToken(); else con.status = "";
            }
            con.target = false;
            if (con != null && data.myUnit != null) {
                String sX = data.myUnit.getTargettedUnit();
                if (sX != null) {
                    if (sX.equals(con.id)) con.target = true;
                }
            }
            data.newContact(con);
        } catch (Exception e) {
            System.out.println("Error: parseHudInfoC: " + e);
        }
    }

    public void parseHudInfoCB(String l) {
        if (l == "Done") return;
        try {
            StringTokenizer st = new StringTokenizer(l, ",");
            MUBuildingInfo building = new MUBuildingInfo();
            building.type = "i";
            building.friend = true;
            building.arc = st.nextToken();
            building.name = st.nextToken();
            final int hex_x = Integer.parseInt(st.nextToken());
            final int hex_y = Integer.parseInt(st.nextToken());
            final int hex_z = Integer.parseInt(st.nextToken());
            final float rtc = Float.parseFloat(st.nextToken());
            final int ibtc = Integer.parseInt(st.nextToken());
            building.position.setFromCenterLocation(hex_x, hex_y, hex_z, rtc, ibtc);
            building.cf = Integer.parseInt(st.nextToken());
            building.maxCf = Integer.parseInt(st.nextToken());
            if (st.hasMoreTokens()) building.status = st.nextToken();
            building.id = building.name + building.position.getHexX() + building.position.getHexY();
            data.newContact(building);
        } catch (Exception e) {
            System.out.println("Error: parseHudInfoCB: " + e);
        }
    }

    protected int tacSX, tacSY, tacEX, tacEY;

    /**
     * Parse a string which represents tactical information. (TS = tactical start)
     * @param l A single line of the tactical info.
     */
    public void parseHudInfoTS(String l) {
        StringTokenizer st = new StringTokenizer(l, ",");
        tacSX = Integer.parseInt(st.nextToken());
        tacSY = Integer.parseInt(st.nextToken());
        tacEX = Integer.parseInt(st.nextToken());
        tacEY = Integer.parseInt(st.nextToken());
        if (data.hiSupportsExtendedMapInfo()) {
            data.mapId = st.nextToken();
            data.mapName = st.nextToken();
            data.mapVersion = st.nextToken();
            if (st.hasMoreTokens()) data.mapLOSOnly = st.nextToken().equals("l");
        }
    }

    /**
      * Parse a string which represents tactical information. (TD = tactical done)
      * @param l A single line of the tactical info.
      */
    public void parseHudInfoTD(String l) {
        data.setTerrainChanged(true);
    }

    /**
     * Parse a string which represents armor status.
     * @param l A single line of the armor status.
     */
    public void parseHudInfoAS(String l) {
        StringTokenizer st = new StringTokenizer(l, ",");
        MUMyInfo info = data.myUnit;
        if (info == null) info = new MUMyInfo();
        String location = st.nextToken().intern();
        String f, i, r;
        if (location == "Done") return;
        f = st.nextToken().intern();
        r = st.nextToken().intern();
        i = st.nextToken().intern();
        if (f != "-") info.armor[MUUnitInfo.indexForSection(location)].f = Integer.parseInt(f);
        if (r != "-") info.armor[MUUnitInfo.indexForSection(location)].r = Integer.parseInt(r);
        if (i != "-") info.armor[MUUnitInfo.indexForSection(location)].i = Integer.parseInt(i);
    }

    /**
     * Parse a string which represents original armor status.
     * @param l A single line of the original armor status.
     */
    public void parseHudInfoOAS(String l) {
        StringTokenizer st = new StringTokenizer(l, ",");
        MUMyInfo info = data.myUnit;
        if (info == null) info = new MUMyInfo();
        String location = st.nextToken().intern();
        String f, i, r;
        if (location == "Done") return;
        f = st.nextToken().intern();
        r = st.nextToken().intern();
        i = st.nextToken().intern();
        if (f != "-") info.armor[MUUnitInfo.indexForSection(location)].of = Integer.parseInt(f); else info.armor[MUUnitInfo.indexForSection(location)].of = 0;
        if (r != "-") info.armor[MUUnitInfo.indexForSection(location)].or = Integer.parseInt(r); else info.armor[MUUnitInfo.indexForSection(location)].or = 0;
        if (i != "-") info.armor[MUUnitInfo.indexForSection(location)].oi = Integer.parseInt(i); else info.armor[MUUnitInfo.indexForSection(location)].oi = 0;
    }

    /**
     * Parse a string which represents tactical information. (TL = tactical line)
     * @param l A single line of the tactical info.
     */
    public void parseHudInfoTL(String l) {
        StringTokenizer st = new StringTokenizer(l, ",");
        int thisY = Integer.parseInt(st.nextToken());
        String tacData = st.nextToken();
        for (int i = 0; i <= tacEX - tacSX; i++) {
            char terrTypeChar = tacData.charAt(2 * i);
            char terrElevChar = tacData.charAt(2 * i + 1);
            int terrElev;
            if (Character.isDigit(terrElevChar)) terrElev = Character.digit(terrElevChar, 10); else terrElev = 0;
            if ((terrTypeChar == '~' || terrTypeChar == '-') && terrElev != 0) terrElev = -terrElev;
            if (terrTypeChar != '?') data.setHex(tacSX + i, thisY, terrTypeChar, terrElev); else {
                String hashkey = String.valueOf(tacSX + i) + " " + String.valueOf(thisY);
                data.LOSinfo.put(hashkey, (Boolean) false);
            }
        }
    }

    /**
     * Parse a string which represents a weapon information string (a specific weapon on our own unit)
     * @param l A single line of the weapon info.
     */
    public void parseHudInfoWE(String l) {
        if (l == "Done") return;
        StringTokenizer st = new StringTokenizer(l, ",");
        MUUnitWeapon w = new MUUnitWeapon();
        w.number = Integer.parseInt(st.nextToken());
        w.typeNumber = Integer.parseInt(st.nextToken());
        w.quality = Integer.parseInt(st.nextToken());
        w.loc = st.nextToken();
        w.status = st.nextToken();
        w.fireMode = st.nextToken();
        w.ammoType = st.nextToken();
        data.myUnit.newUnitWeapon(w);
    }

    /**
     * Parse a string which represents our unit's ammo status
     * @param l A single line of the ammo info.
     */
    public void parseHudInfoAM(String l) {
        if (l == "Done") return;
        StringTokenizer st = new StringTokenizer(l, ",");
        MUUnitAmmo a = new MUUnitAmmo();
        a.number = Integer.parseInt(st.nextToken());
        a.weaponTypeNumber = Integer.parseInt(st.nextToken());
        a.ammoMode = st.nextToken();
        a.roundsRemaining = Integer.parseInt(st.nextToken());
        a.roundsOriginal = Integer.parseInt(st.nextToken());
        data.myUnit.newUnitAmmo(a);
    }

    /**
     * Parse a string which represents a weapon information string (not a particular unit's weapon)
     * @param l A single line of the weapon info.
     */
    public void parseHudInfoWL(String l) {
        if (l == "Done") return;
        StringTokenizer st = new StringTokenizer(l, ",");
        MUWeapon w = new MUWeapon();
        w.typeNumber = Integer.parseInt(st.nextToken());
        w.name = st.nextToken();
        w.minRange = Integer.parseInt(st.nextToken());
        w.shortRange = Integer.parseInt(st.nextToken());
        w.medRange = Integer.parseInt(st.nextToken());
        w.longRange = Integer.parseInt(st.nextToken());
        w.minRangeWater = Integer.parseInt(st.nextToken());
        w.shortRangeWater = Integer.parseInt(st.nextToken());
        w.medRangeWater = Integer.parseInt(st.nextToken());
        w.longRangeWater = Integer.parseInt(st.nextToken());
        w.criticalSize = Integer.parseInt(st.nextToken());
        w.weight = Integer.parseInt(st.nextToken());
        w.damage = Integer.parseInt(st.nextToken());
        w.recycle = Integer.parseInt(st.nextToken());
        if (data.hiSupportsWLHeatInfo()) {
            w.fireModes = st.nextToken();
            w.ammoModes = st.nextToken();
            w.damageType = st.nextToken();
            w.heat = Integer.parseInt(st.nextToken());
        }
        MUUnitInfo.newWeapon(w);
    }
}
