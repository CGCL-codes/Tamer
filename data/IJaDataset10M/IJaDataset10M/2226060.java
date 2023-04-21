package dk.andsen.hp41;

import java.io.File;
import java.text.DecimalFormat;
import dk.andsen.hp41.types.DseIsg;
import dk.andsen.utils.Utils;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Environment;

/**
 * @author andsen
 *
 */
public class Calculator {

    public boolean GroupingSeparator = true;

    public boolean DecimalSeparatorPeriod = true;

    public static final int FORMAT_FIX = 1;

    public static final int FORMAT_SCI = 2;

    public static final int FORMAT_ENG = 3;

    /**
	 * Holds the a41 x-register
	 */
    public Double x = new Double(0.0);

    /**
	 * Holds the a41 y-register
	 */
    public Double y = new Double(0.0);

    /**
	 * Holds the a41 z-register
	 */
    public Double z = new Double(0.0);

    /**
	 * Holds the a41 z-register
	 */
    public Double t = new Double(0.0);

    /**
	 * Holds the a41 lastx-register
	 */
    private Double lastx = new Double(0.0);

    /**
	 * Holds the programme currently in focus
	 */
    public String currentPrg;

    /**
	 * True if there is a message to display 
	 */
    public boolean message = false;

    /**
	 * True if a41 should display registers on screen
	 */
    public boolean debug = true;

    /**
	 * True if a41 should display a toast message after every keyboard command 
	 */
    public boolean showcmd = false;

    /**
	 * True if a41 shall display a toast messages while SST- / BST'ing 
	 */
    public boolean STToast = true;

    /**
	 * Holds the ALPHA string
	 */
    public String alpha = "";

    /**
	 * true if in alpha mode
	 */
    public boolean alphamode = false;

    /**
	 * Hold the current format mode
	 */
    public int formatMode = FORMAT_FIX;

    /**
	 * Hold the formatting precition
	 */
    public int formatPrecition = 4;

    /**
	 * True if enter might be needed 
	 */
    public boolean enterNeeded = true;

    private int sumRegStart = 11;

    private static int numRegs = 21;

    private static int numARegs = 21;

    private static int numFlags = 21;

    private static int numFlagsStore = 11;

    private Double regs[] = new Double[numRegs];

    private String aregs[] = new String[numARegs];

    private boolean[] flags = new boolean[numFlags];

    private SQLHelper funcData;

    private SQLiteDatabase db;

    public String msg;

    public final int DEG = 0;

    public final int RAD = 1;

    public final int GRD = 2;

    public int anglemode = DEG;

    private Context context;

    private boolean _logging;

    Calculator(SharedPreferences settings, Context cont, SQLiteDatabase database) {
        _logging = Prefs.getLogging(cont);
        context = cont;
        x = new Double(settings.getString("x", "0.0"));
        y = new Double(settings.getString("y", "0.0"));
        z = new Double(settings.getString("z", "0.0D"));
        t = new Double(settings.getString("t", "0.0D"));
        lastx = new Double(settings.getString("Lastx", "0.0D"));
        alpha = settings.getString("a", "");
        sumRegStart = settings.getInt("sumRegStart", 11);
        int i;
        for (i = 0; i < numRegs; i++) {
            regs[i] = new Double(settings.getString("Reg" + i, "0.0D"));
        }
        for (i = 0; i < numARegs; i++) {
            aregs[i] = settings.getString("AReg" + i, "");
        }
        for (i = 0; i < numFlagsStore; i++) {
            flags[i] = settings.getBoolean("Flag" + i, false);
        }
        anglemode = settings.getInt("AngleMode", DEG);
        currentPrg = settings.getString("CurPrg", null);
        alphamode = settings.getBoolean("AlphaMode", false);
        formatMode = settings.getInt("FormatMode", FORMAT_FIX);
        formatPrecition = settings.getInt("FormatPrec", 4);
        GroupingSeparator = settings.getBoolean("GroupingSeparator", true);
        DecimalSeparatorPeriod = settings.getBoolean("DecimalSeparatorPeriod", true);
        funcData = new SQLHelper(context);
        debug = Prefs.getDebug(cont);
        showcmd = Prefs.getShowCmd(cont);
        STToast = Prefs.getSTToast(cont);
        db = database;
    }

    /**
	 * Save registers and close the Calculator 
	 * @param editor Used to save the registers
	 */
    public void close(SharedPreferences.Editor editor) {
        editor.putString("x", x.toString());
        editor.putString("y", y.toString());
        editor.putString("z", z.toString());
        editor.putString("t", t.toString());
        editor.putString("lastxt", lastx.toString());
        editor.putString("a", alpha);
        editor.putInt("sumRegStart", sumRegStart);
        int i;
        for (i = 0; i < numRegs; i++) {
            editor.putString("Reg" + i, regs[i].toString());
        }
        for (i = 0; i < numARegs; i++) {
            editor.putString("AReg" + i, aregs[i]);
        }
        for (i = 0; i < numFlagsStore; i++) {
            editor.putBoolean("Flag" + i, flags[i]);
        }
        editor.putInt("AngleMode", anglemode);
        editor.putString("CurPrg", currentPrg);
        editor.putBoolean("AlphaMode", alphamode);
        editor.putInt("FormatMode", formatMode);
        editor.putInt("FormatPrec", formatPrecition);
        editor.putBoolean("GroupingSeparator", GroupingSeparator);
        editor.putBoolean("DecimalSeparatorPeriod", DecimalSeparatorPeriod);
        editor.commit();
        db.close();
        try {
            this.finalize();
        } catch (Throwable e) {
            Utils.showException(e.toString(), context);
            e.printStackTrace();
        }
    }

    /**
	 * Handle all functions without arguments
	 * @param cmd The name of the command to be executed
	 *TODO Should this also handles programmes?
	 */
    public boolean xeq(String cmd) {
        boolean ret = true;
        boolean noEnterNeeded = false;
        message = false;
        Double res = x;
        Double save;
        if (cmd.equals("-")) {
            res = y - x;
            stackdown();
        } else if (cmd.equals("%")) {
            res = y / 100 * x;
        } else if (cmd.equals("%CH")) {
            res = ((x - y) * 100) / y;
        } else if (cmd.equals("*")) {
            res = y * x;
            stackdown();
        } else if (cmd.equals("/")) {
            res = y / x;
            stackdown();
        } else if (cmd.equals("+")) {
            res = y + x;
            stackdown();
        } else if (cmd.equals("1/X")) {
            res = 1 / x;
        } else if (cmd.equals("10↑X")) {
            res = Math.pow(10D, x);
        } else if (cmd.equals("ABS")) {
            res = Math.abs(x);
        } else if (cmd.equals("ACOS")) {
            res = Math.acos(res);
            res = radToCurrentAngleMode(res);
        } else if (cmd.equals("ADV")) {
        } else if (cmd.equals("AOFF")) {
            alphamode = false;
        } else if (cmd.equals("AON")) {
            alphamode = true;
        } else if (cmd.equals("APPEND")) {
        } else if (cmd.equals("ASIN")) {
            res = Math.asin(res);
            res = radToCurrentAngleMode(res);
        } else if (cmd.equals("ATAN")) {
            res = Math.atan(res);
            res = radToCurrentAngleMode(res);
        } else if (cmd.equals("AVIEW")) {
            msg = alpha;
            message = true;
        } else if (cmd.equals("ASFH")) {
        } else if (cmd.equals("BCK")) {
            res = 0D;
        } else if (cmd.equals("BEEP")) {
            final MediaPlayer mMediaPlayer;
            try {
                mMediaPlayer = MediaPlayer.create(context, R.raw.beep);
                mMediaPlayer.setLooping(false);
                mMediaPlayer.start();
                mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {

                    public void onCompletion(MediaPlayer arg0) {
                    }
                });
            } catch (Exception e) {
            }
        } else if (cmd.equals("CHS")) {
            res = -x;
        } else if (cmd.equals("CLA")) {
            alpha = "";
        } else if (cmd.equals("CLD")) {
        } else if (cmd.equals("CLRG")) {
            int i;
            for (i = 0; i < numRegs; i++) {
                regs[i] = 0D;
            }
        } else if (cmd.equals("CLST")) {
            res = 0D;
            y = 0D;
            z = 0D;
            t = 0D;
        } else if (cmd.equals("CLΣ")) {
            regs[sumRegStart] = 0.0D;
            regs[sumRegStart + 1] = 0.0D;
            regs[sumRegStart + 2] = 0.0D;
            regs[sumRegStart + 3] = 0.0D;
            regs[sumRegStart + 4] = 0.0D;
            regs[sumRegStart + 5] = 0.0D;
        } else if (cmd.equals("CLX")) {
            res = 0D;
        } else if (cmd.equals("COS")) {
            res = currentAngleModeToRad(x);
            res = Math.cos(res);
        } else if (cmd.equals("DEC")) {
            try {
                Integer ix = x.intValue();
                res = new Double(Integer.parseInt(ix.toString(), 8));
            } catch (Exception e) {
                msg = "DATA ERROR";
                message = true;
            }
        } else if (cmd.equals("DEG")) {
            anglemode = DEG;
        } else if (cmd.equals("D-R")) {
            res = Math.toRadians(x);
        } else if (cmd.equals("E↑X")) {
            res = Math.exp(x);
        } else if (cmd.equals("E↑X-1")) {
            res = Math.expm1(x);
        } else if (cmd.equals("ENTER")) {
            enter(true, false);
            noEnterNeeded = true;
        } else if (cmd.equals("FACT")) {
            int n = x.intValue();
            if (x > 69) {
                msg = "OUT OF RANGE";
                message = true;
                return true;
            }
            res = 1D;
            for (int i = 2; i <= n; i++) res *= i;
        } else if (cmd.equals("FRC")) {
            res = x - Math.rint(x);
        } else if (cmd.equals("GRAD")) {
            anglemode = GRD;
        } else if (cmd.equals("HMS")) {
            HmsUtils time = new HmsUtils(x, false);
            Utils.logD(time.toString(), _logging);
            res = time.getdHMS();
        } else if (cmd.equals("HMS-")) {
            HmsUtils time = new HmsUtils(y, true);
            Utils.logD(time.toString(), _logging);
            time.subDHMS(x);
            Utils.logD(time.toString(), _logging);
            res = time.getdHMS();
            stackdown();
        } else if (cmd.equals("HMS+")) {
            HmsUtils time = new HmsUtils(y, true);
            Utils.logD(time.toString(), _logging);
            time.addDHMS(x);
            Utils.logD(time.toString(), _logging);
            res = time.getdHMS();
        } else if (cmd.equals("HR")) {
            HmsUtils time = new HmsUtils(x, true);
            Utils.logD(time.toString(), _logging);
            res = time.getdHour();
        } else if (cmd.equals("INT")) {
            res = Math.rint(x - 0.5);
        } else if (cmd.equals("LASTX")) {
            enter(false, false);
            res = lastx;
        } else if (cmd.equals("LN")) {
            res = Math.log(x);
        } else if (cmd.equals("LN1+X")) {
            res = Math.log1p(x);
        } else if (cmd.equals("LOG")) {
            res = Math.log10(x);
        } else if (cmd.equals("MEAN")) {
            res = regs[sumRegStart] / regs[sumRegStart + 5];
            y = regs[sumRegStart + 2] / regs[sumRegStart + 5];
        } else if (cmd.equals("MOD")) {
            res = y - (Math.round((y / x)) * x);
        } else if (cmd.equals("OCT")) {
            String octal = Integer.toOctalString(x.intValue());
            res = new Double(octal);
        } else if (cmd.equals("PACK")) {
            message = true;
            msg = "No need to pack :-)";
        } else if (cmd.equals("PI")) {
            enter(false, false);
            res = Math.PI;
        } else if (cmd.equals("R-P")) {
            res = Math.sqrt(x * x + y * y);
            y = radToCurrentAngleMode(Math.acos(x / res));
        } else if (cmd.equals("PROMPT")) {
        } else if (cmd.equals("R↑")) {
            save = x;
            res = t;
            y = x;
            z = y;
            t = save;
        } else if (cmd.equals("RAD")) {
            anglemode = RAD;
        } else if (cmd.equals("R-D")) {
            res = Math.toDegrees(x);
        } else if (cmd.equals("RDN")) {
            save = x;
            res = y;
            y = z;
            z = t;
            t = save;
        } else if (cmd.equals("RND")) {
            String val;
            String nulls = "";
            for (int i = 0; i < formatPrecition; i++) nulls += "0";
            DecimalFormat df = null;
            String format = null;
            switch(formatMode) {
                case Calculator.FORMAT_FIX:
                    format = "########0." + nulls;
                    df = new DecimalFormat(format);
                    if (df.format(x).equals(df.format(0.0)) && x != 0.0D) format = "0." + nulls + "E0";
                    Utils.logD(format, _logging);
                    df = new DecimalFormat(format);
                    break;
                case Calculator.FORMAT_SCI:
                    format = "0." + nulls + "E0";
                    df = new DecimalFormat(format);
                    break;
                case Calculator.FORMAT_ENG:
                    format = "##0." + nulls + "E0";
                    df = new DecimalFormat(format);
                    break;
                default:
                    df = new DecimalFormat("#.0000");
            }
            val = df.format(x);
            val = val.replaceAll(",", ".");
            res = new Double(val);
        } else if (cmd.equals("P-R")) {
            res = Math.cos(currentAngleModeToRad(y)) * x;
            y = Math.sqrt(x * x - res * res);
        } else if (cmd.equals("S↑")) {
            save = t;
            t = z;
            z = y;
            y = x;
            res = save;
        } else if (cmd.equals("SDEV")) {
            res = Math.sqrt((regs[sumRegStart + 1] - regs[sumRegStart] * regs[sumRegStart] / regs[sumRegStart + 5]) / (regs[sumRegStart + 5] - 1));
        } else if (cmd.equals("SIGN")) {
            if (x < 0) res = -1D; else res = 1D;
        } else if (cmd.equals("SIN")) {
            res = currentAngleModeToRad(x);
            res = Math.sin(res);
        } else if (cmd.equals("SQRT")) {
            res = Math.sqrt(x);
        } else if (cmd.equals("Σ-")) {
            regs[sumRegStart] -= x;
            regs[sumRegStart + 1] -= x * x;
            regs[sumRegStart + 2] -= y;
            regs[sumRegStart + 3] -= y * y;
            regs[sumRegStart + 4] -= x * y;
            regs[sumRegStart + 5] -= 1;
            res = regs[5];
        } else if (cmd.equals("Σ+")) {
            regs[sumRegStart] += x;
            regs[sumRegStart + 1] += x * x;
            regs[sumRegStart + 2] += y;
            regs[sumRegStart + 3] += y * y;
            regs[sumRegStart + 4] += x * y;
            regs[sumRegStart + 5] += 1;
            res = regs[sumRegStart + 5];
        } else if (cmd.equals("TAN")) {
            res = currentAngleModeToRad(x);
            res = Math.tan(res);
        } else if (cmd.equals("X↑2")) {
            res = x * x;
        } else if (cmd.equals("Y↑X")) {
            res = Math.pow(y, x);
            stackdown();
        } else if (cmd.equals("X<>Y")) {
            save = x;
            res = y;
            y = save;
        } else {
            Utils.logD("Command not found: " + cmd, _logging);
            ret = false;
        }
        if (message) {
        } else {
            lastx = x;
            x = res;
        }
        if (!noEnterNeeded) enterNeeded = true;
        return ret;
    }

    /**
	 * Convert the degree in rad to th current degree mode
	 * @param res
	 * @return
	 */
    private Double radToCurrentAngleMode(Double res) {
        if (anglemode == DEG) res = Math.toDegrees(res); else if (anglemode == GRD) res = (400 * Math.toDegrees(res)) / 360;
        return res;
    }

    private Double currentAngleModeToRad(Double res) {
        if (anglemode == DEG) res = Math.toRadians(res); else if (anglemode == GRD) res = Math.toRadians((360 * res) / 400);
        return res;
    }

    /**
	 * Handle all functions with integer arguments. What about indirect call?
	 * Argument / new xeq?
	 * @param cmd
	 * @param arg
	 */
    public void xeq(String cmd, int arg) {
        message = false;
        Double res;
        res = x;
        lastx = x;
        x = res;
    }

    /**
	 * Handle all functions with indirect addressing
	 * @param cmd
	 * @param arg
	 * @param indirect
	 */
    public void xeq(String cmd, Integer arg, boolean indirect, String dot) {
        message = false;
        Double res;
        res = x;
        Integer reg = arg;
        Integer dotRegVal = null;
        Double dotVal = null;
        if (dot != null) {
            if (dot.equals("X")) {
                dotRegVal = x.intValue();
                dotVal = x;
            } else if (dot.equals("Y")) {
                dotRegVal = y.intValue();
                dotVal = y;
            } else if (dot.equals("Z")) {
                dotRegVal = z.intValue();
                dotVal = z;
            } else if (dot.equals("T")) {
                dotRegVal = t.intValue();
                dotVal = t;
            } else if (dot.equals("L")) {
                dotRegVal = lastx.intValue();
                dotVal = lastx;
            }
        }
        if (indirect) {
            if (dot != null) arg = dotRegVal; else reg = regs[arg].intValue();
            if ((reg != null) && ((reg < 0) || (reg > numRegs - 1))) {
                msg = "Invalid register!";
                message = true;
                return;
            }
        }
        if (cmd.equals("ARCL")) {
            if ((!indirect) && (dot != null)) {
                alpha = dotVal.toString();
            } else alpha = aregs[reg];
        } else if (cmd.equals("ASTO")) {
            aregs[reg] = alpha;
        } else if (cmd.equals("CF")) {
            if (reg <= numRegs) flags[reg] = false; else if (reg == 28) DecimalSeparatorPeriod = false; else if (reg == 29) GroupingSeparator = false; else {
                msg = "No such flag";
                message = true;
            }
        } else if (cmd.equals("ENG")) {
            formatMode = FORMAT_ENG;
            formatPrecition = arg;
        } else if (cmd.equals("FIX")) {
            formatMode = FORMAT_FIX;
            formatPrecition = arg;
        } else if (cmd.equals("RCL")) {
            if ((!indirect) && (dot != null)) {
                res = dotVal;
            } else if ((indirect) && (dot != null)) {
                res = regs[dotRegVal];
            } else res = regs[reg];
            enter(false, false);
        } else if (cmd.equals("SCI")) {
            formatMode = FORMAT_SCI;
            formatPrecition = arg;
        } else if (cmd.equals("SF")) {
            if (reg <= numRegs) flags[reg] = true; else if (reg == 28) DecimalSeparatorPeriod = true; else if (reg == 29) GroupingSeparator = true; else {
                msg = "No such flag";
                message = true;
            }
        } else if (cmd.equals("STO")) {
            if ((!indirect) && (dot != null)) {
                regTo(dot, dotVal);
            } else if ((indirect) && (dot != null)) {
                if (dotRegVal < 0 || dotRegVal > numRegs - 1) {
                    msg = "Invalid register!";
                    message = true;
                } else regs[dotRegVal] = x;
            } else {
                if (reg < 0 || reg > numRegs - 1) {
                    msg = "Invalid register!";
                    message = true;
                } else regs[reg] = x;
            }
        } else if (cmd.equals("ST-")) {
            if ((!indirect) && (dot != null)) {
                regTo(dot, dotVal - x);
            } else if ((indirect) && (dot != null)) {
                if (dotRegVal < 0 || dotRegVal > numRegs - 1) {
                    msg = "Invalid register!";
                    message = true;
                } else regs[dotRegVal] = x;
            } else {
                if (reg < 0 || reg > numRegs - 1) {
                    msg = "Invalid register!";
                    message = true;
                } else regs[reg] -= x;
            }
        } else if (cmd.equals("ST*")) {
            if ((!indirect) && (dot != null)) {
                regTo(dot, dotVal * x);
            } else if ((indirect) && (dot != null)) {
                if (dotRegVal < 0 || dotRegVal > numRegs - 1) {
                    msg = "Invalid register!";
                    message = true;
                } else regs[dotRegVal] = x;
            } else {
                if (reg < 0 || reg > numRegs - 1) {
                    msg = "Invalid register!";
                    message = true;
                } else regs[reg] *= x;
            }
        } else if (cmd.equals("ST/")) {
            if ((!indirect) && (dot != null)) {
                regTo(dot, dotVal / x);
            } else if ((indirect) && (dot != null)) {
                if (dotRegVal < 0 || dotRegVal > numRegs - 1) {
                    msg = "Invalid register!";
                    message = true;
                } else regs[dotRegVal] = x;
            } else {
                if (reg < 0 || reg > numRegs - 1) {
                    msg = "Invalid register!";
                    message = true;
                } else regs[reg] /= x;
            }
        } else if (cmd.equals("ST+")) {
            if ((!indirect) && (dot != null)) {
                regTo(dot, dotVal + x);
            } else if ((indirect) && (dot != null)) {
                if (dotRegVal < 0 || dotRegVal > numRegs - 1) {
                    msg = "Invalid register!";
                    message = true;
                } else regs[dotRegVal] = x;
            } else {
                if (reg < 0 || reg > numRegs - 1) {
                    msg = "Invalid register!";
                    message = true;
                } else regs[reg] += x;
            }
        } else if (cmd.equals("ΣREG")) {
        } else if (cmd.equals("X<>")) {
            double save = x;
            res = regs[reg];
            regs[reg] = save;
        } else if (cmd.equals("TONE")) {
        } else if (cmd.equals("VIEW")) {
            msg = regs[arg].toString();
            message = true;
        }
        enterNeeded = true;
        lastx = x;
        x = res;
    }

    /**
	 * Save the dotValue Double value to the register specified in dot String
	 * @param dot Specifies which stack register to save the dotVal in
	 * @param dotVal the Double value to save in a stack register
	 */
    private void regTo(String dot, Double dotVal) {
        if (dot.equals("X")) x = dotVal; else if (dot.equals("Y")) y = dotVal; else if (dot.equals("Z")) z = dotVal; else if (dot.equals("T")) t = dotVal; else if (dot.equals("L")) lastx = dotVal;
    }

    /**
	 * Handle all functions with string arguments
	 * @param cmd
	 * @param arg
	 */
    public void xeq(String cmd, String arg) {
        message = false;
        if (cmd.equals("CLP")) {
            boolean test;
            test = funcData.deleteProgram(db, arg);
            if (!test) {
                message = true;
                msg = context.getString(R.string.NoSuchProgram);
            } else {
                message = true;
                msg = arg + " deleted";
            }
        } else if (cmd.equals("COPY")) {
            File path = Environment.getExternalStorageDirectory();
            File programDirectory = new File(path.getAbsolutePath() + "/a41");
            File fileName = new File(programDirectory + "/" + arg + ".a41");
            DBProgramImportExport ix = new DBProgramImportExport(context, fileName.getAbsolutePath());
            if (ix.importProgram()) {
                message = true;
                msg = "Programme " + arg + " loaded";
            } else {
                message = true;
                msg = "Programme " + arg + " not found";
            }
            enterNeeded = true;
        }
    }

    /**
	 * Execute all function with string and integer arguments
	 * @param cmd
	 * @param function
	 * @param key
	 * @param shifted
	 */
    public void xeq(String cmd, String function, int key, boolean shifted) {
        message = false;
        if (cmd.equals("ASN")) if (!funcData.asn(db, function, key, shifted)) {
            msg = "NONEEXSISTENT";
            message = true;
        }
    }

    /**
	 * All test functions without arguments
	 * @param cmd the name of the test function
	 * @return true if test result is true
	 */
    public boolean test(String cmd) {
        if (cmd.equals("X<=0?")) {
            if (x <= 0) {
                msg = "YES";
            } else {
                msg = "NO";
            }
        } else if (cmd.equals("X<=Y?")) {
            if ((x < y) || (x.toString().equals(y.toString()))) {
                msg = "YES";
            } else {
                msg = "NO";
            }
        } else if (cmd.equals("X<Y?")) {
            if (x < y) {
                msg = "YES";
            } else {
                msg = "NO";
            }
        } else if (cmd.equals("X=0?")) {
            if (x == 0) {
                msg = "YES";
            } else {
                msg = "NO";
            }
        } else if (cmd.equals("X=Y?")) {
            if (x.toString().equals(y.toString())) {
                msg = "YES";
            } else {
                msg = "NO";
            }
        } else if (cmd.equals("X>Y?")) {
            if (x > y) {
                msg = "YES";
            } else {
                msg = "NO";
            }
        } else if (cmd.equals("X<0?")) {
            if (x < 0) {
                msg = "YES";
            } else {
                msg = "NO";
            }
        } else if (cmd.equals("X≠0?")) {
            if (x != 0) {
                msg = "YES";
            } else {
                msg = "NO";
            }
        } else if (cmd.equals("X≠Y?")) {
            if (x != y) {
                msg = "YES";
            } else {
                msg = "NO";
            }
        }
        message = true;
        enterNeeded = true;
        if (msg.equals("NO")) return false; else return true;
    }

    /**
	 * Test if a register is a valid register
	 * @param flag
	 * @return true if the flag is valid otherwise false 
	 */
    private boolean validFlag(int flag) {
        boolean ret = false;
        if ((((flag >= 0) && (flag < numRegs)) || (flag == 28) || (flag == 29))) {
            ret = true;
        }
        return ret;
    }

    /**
	 * All test functions with integer arguments
	 * @param cmd
	 * @param arg
	 * @return
	 */
    public boolean test(String cmd, int arg, boolean indirect, String dot) {
        boolean flag;
        message = false;
        int reg = arg;
        int dotVal = 0;
        if (dot != null) {
            if (dot.equals("X")) {
                dotVal = x.intValue();
            } else if (dot.equals("Y")) {
                dotVal = y.intValue();
            } else if (dot.equals("Z")) {
                dotVal = z.intValue();
            } else if (dot.equals("T")) {
                dotVal = t.intValue();
            } else if (dot.equals("L")) {
                dotVal = lastx.intValue();
            }
        }
        if (indirect) {
            if (dot != null) reg = dotVal; else reg = regs[arg].intValue();
        }
        if (!validFlag(reg)) {
            msg = "Invalid register!";
            message = true;
            return false;
        }
        if (cmd.equals("FC?")) {
            if (reg == 28) {
                flag = DecimalSeparatorPeriod;
            } else if (reg == 29) {
                flag = GroupingSeparator;
            } else {
                flag = flags[reg];
            }
            if (flag == false) msg = "YES"; else msg = "NO";
        } else if (cmd.equals("FC?C")) {
            if (reg == 28) {
                flag = DecimalSeparatorPeriod;
                DecimalSeparatorPeriod = false;
            } else if (reg == 29) {
                flag = GroupingSeparator;
                GroupingSeparator = false;
            } else {
                flag = flags[reg];
            }
            flag = flags[reg];
            if (flag == false) msg = "YES"; else msg = "NO";
            if (arg <= numFlags) flags[arg] = false;
        } else if (cmd.equals("FS?")) {
            if (reg == 28) {
                flag = DecimalSeparatorPeriod;
            } else if (reg == 29) {
                flag = GroupingSeparator;
            } else {
                flag = flags[reg];
            }
            if (flag == false) msg = "NO"; else msg = "YES";
        } else if (cmd.equals("DSE")) {
            DseIsg counter = getDseIsg(regs[reg]);
            counter.dec();
            boolean ret = true;
            if (counter.getCount() <= counter.getLimit()) {
                ret = false;
            }
            regs[reg] = setDseIsg(counter);
            return ret;
        } else if (cmd.equals("ISG")) {
            DseIsg counter;
            try {
                counter = getDseIsg(regs[reg]);
                counter.inc();
                boolean ret = true;
                if (counter.getCount() > counter.getLimit()) {
                    ret = false;
                }
                regs[reg] = setDseIsg(counter);
                return ret;
            } catch (Exception e) {
                Utils.showException(e.toString(), context);
            }
        }
        message = true;
        enterNeeded = true;
        if (msg.equals("NO")) return false; else return true;
    }

    /**
	 * Increase or decrease a DseIsg structure
	 * @param counter the DseIsg structure
	 * @return a Double with the DSE ISG value 
	 */
    private Double setDseIsg(DseIsg counter) {
        int itmp;
        String stmp;
        itmp = counter.getLimit();
        if (itmp < 10) stmp = "00" + itmp; else if (itmp < 100) stmp = "0" + itmp; else stmp = "" + itmp;
        String val = counter.getCount() + "." + stmp;
        itmp = counter.getStep();
        if (itmp < 10) stmp = "0" + itmp; else stmp = "" + itmp;
        val += stmp;
        Double tmp = new Double(val);
        return tmp;
    }

    /**
	 * Decode the value of a DSE or ISG register
	 * @param val Value of a DSE or ISG register
	 * @return a DseIsg structure with decoded values
	 */
    private DseIsg getDseIsg(Double val) {
        DseIsg tmp = new DseIsg();
        String str = val.toString();
        String test = str.substring(0, str.indexOf("."));
        tmp.setCount(new Integer(test).intValue());
        test = str.substring(str.indexOf(".") + 1, str.indexOf(".") + 4);
        tmp.setLimit(new Integer(test).intValue());
        test = str.substring(str.indexOf(".") + 4, str.indexOf(".") + 6);
        tmp.setStep(new Integer(test).intValue());
        return tmp;
    }

    /**
	 * All test functions with string arguments. Any???
	 * @param cmd Name of the function to be called
	 * @param arg Its String argument
	 * @return
	 */
    public boolean test(String cmd, String arg) {
        message = true;
        enterNeeded = true;
        return false;
    }

    /**
	 * move down the z and t register after e.g. +
	 */
    private void stackdown() {
        y = z;
        z = t;
    }

    /**
	 * The ENTER function called by the ENTER key or before e.g. RCL
	 */
    public void enter(boolean forceEnter, boolean enterStillNeeded) {
        if (forceEnter || enterNeeded) {
            t = z;
            z = y;
            y = x;
        }
        if (enterStillNeeded) {
            enterNeeded = true;
        } else {
            enterNeeded = false;
        }
    }

    public boolean test(String cmd, int arg) {
        enterNeeded = true;
        return false;
    }
}
