package seta.minecredit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Files {

    static String mainDirectory = "plugins/MineCredit";

    static File configs = new File(mainDirectory + File.separator + "config.dat");

    static Properties config = new Properties();

    static File langs = new File(mainDirectory + File.separator + "lang" + File.separator + "en.dat");

    static Properties lang = new Properties();

    static File credits = new File(mainDirectory + File.separator + "credits.dat");

    static Properties credit = new Properties();

    public void loadContent() {
        this.loadCredit();
        try {
            FileInputStream inStream = new FileInputStream(configs);
            config.load(inStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.loadLanguage(this.getConfKey("language"));
    }

    public Properties getCredit() {
        return credit;
    }

    public boolean hasCredit(String key) {
        if (credit.containsKey(key)) {
            return true;
        } else {
            return false;
        }
    }

    public FileOutputStream getCreditOut() {
        try {
            return new FileOutputStream(credits);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String parse(String text) {
        return text.replace("&RED;", "" + ChatColor.RED + "").replace("&AQUA;", "" + ChatColor.AQUA + "").replace("&YELLOW;", "" + ChatColor.YELLOW + "").replace("&GREEN;", "" + ChatColor.GREEN + "").replace("&DARK_GREEN;", "" + ChatColor.DARK_GREEN + "").replace("&PINK;", "" + ChatColor.LIGHT_PURPLE + "").replace("&GOLD;", "" + ChatColor.GOLD + "").replace("&BLUE;", "" + ChatColor.BLUE + "").replace("&BLACK;", "" + ChatColor.BLACK + "").replace("&PURPLE;", "" + ChatColor.DARK_PURPLE + "").replace("&WHITE;", "" + ChatColor.WHITE + "").replace("&GRAY;", "" + ChatColor.GRAY + "");
    }

    public String getConfKey(String key) {
        return config.getProperty(key);
    }

    public String getCreditKey(String key) {
        return credit.getProperty(key);
    }

    public String getLangKey(String key) {
        return this.parse(lang.getProperty(key));
    }

    public String getLangKey(String key, String asd) {
        return this.parse(lang.getProperty(key)).replace("%1", asd);
    }

    public String getLangKey(String key, String asd, String asd2) {
        return this.parse(lang.getProperty(key)).replace("%1", asd).replace("%2", asd2);
    }

    public boolean loadLanguage(String lanx) {
        langs = new File(mainDirectory + File.separator + "lang" + File.separator + lanx + ".dat");
        if (langs.exists()) {
            FileInputStream blangss;
            try {
                blangss = new FileInputStream(langs);
                lang = new Properties();
                lang.load(blangss);
                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean loadCredit() {
        if (credits.exists()) {
            FileInputStream blangss;
            try {
                blangss = new FileInputStream(credits);
                credit = new Properties();
                credit.load(blangss);
                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    public void reload() {
        this.loadCredit();
        this.loadConfig();
        this.loadLanguage(this.getConfKey("language"));
    }

    public boolean loadConfig() {
        if (configs.exists()) {
            FileInputStream blangss;
            try {
                blangss = new FileInputStream(configs);
                config = new Properties();
                config.load(blangss);
                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    public void createInit() {
        this.createDir();
        this.createConfig();
        this.createLanguage();
        this.createCredits();
    }

    public void createDir() {
        File mainDir = new File(mainDirectory);
        File langDir = new File(mainDirectory + File.separator + "lang");
        if (!mainDir.exists()) {
            mainDir.mkdir();
        }
        if (!langDir.exists()) {
            langDir.mkdir();
        }
    }

    public void createConfig() {
        if (!configs.exists()) {
            try {
                configs.createNewFile();
                FileOutputStream out = new FileOutputStream(configs);
                config.store(out, "MineCredit config file");
                out.flush();
                out.close();
                this.writeInitConfig();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void createLanguage() {
        if (!langs.exists()) {
            try {
                langs.createNewFile();
                FileOutputStream out = new FileOutputStream(langs);
                lang.store(out, "MineCredit EN language file");
                out.flush();
                out.close();
                this.writeInitLangEN();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void createCredits() {
        if (!credits.exists()) {
            try {
                credits.createNewFile();
                FileOutputStream out = new FileOutputStream(langs);
                credit.store(out, "MineCredit Credit file");
                out.flush();
                out.close();
                this.writeInitLangEN();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void writeInitConfig() {
        FileOutputStream out;
        try {
            out = new FileOutputStream(configs);
            config.put("language", "en");
            config.put("maxcredit", "600");
            config.put("badcredit", "1000");
            config.put("interest", "6");
            config.put("interesttime", "12:00");
            config.put("lastinterest", "0");
            config.put("enableshortcut", "true");
            config.put("enableadminshortcut", "true");
            config.put("usepermission", "true");
            config.put("bad_grayout", "true");
            config.put("bad_onfire", "true");
            config.put("remind", "true");
            config.put("remindtime", "60");
            config.store(out, "MineCredit config file");
            out.close();
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeInitLangEN() {
        FileOutputStream out;
        try {
            out = new FileOutputStream(langs);
            lang.put("usage", "&AQUA;Usage:&YELLOW;");
            lang.put("amount", "&PINK;AMOUNT");
            lang.put("optval", "&PINK;OPTION VALUE");
            lang.put("creditinfo", "&YELLOW;Credit Info");
            lang.put("nocredit", "&RED;You dont have any open credits");
            lang.put("granted", "&GREEN;Youre Credit about &AQUA;%1 &GREEN;was granted!");
            lang.put("grantedinfo", "&YELLOW;You now have a credit about &AQUA;%1 &DARK_GREEN; [Money: &AQUA;%2&DARK_GREEN;]");
            lang.put("notgranted", "&RED;Youre Credit about &AQUA;%1 &RED;was not granted!");
            lang.put("notgrantedinfo", "&YELLOW;You may already have a Credit or its to Big!");
            lang.put("notgrantedinfo2", "&YELLOW;Youre Credit is: &AQUA;%2 - &YELLOW;Maximal Credit is: &AQUA;%1");
            lang.put("interesttime", "&YELLOW;The &AQUA;Credit institude &YELLOW;calculates the daily interests");
            lang.put("interestplayer", "&YELLOW;Youre credit is now: &AQUA;%1 &DARK_GREEN;[raised: &AQUA;%2&DARK_GREEN;]");
            lang.put("yourecredit", "&YELLOW;Youre credit is: &AQUA;%1 &YELLOW;of maximal &AQUA;%2");
            lang.put("yourecredit2", "&YELLOW;The credit will start hurting at &AQUA;%1");
            lang.put("yourecredit3", "&YELLOW;Interest Rates are: &GREEN;%1%,&YELLOW; per 24h");
            lang.put("yourecreditmax", "&YELLOW;Maximal credit amount is: &AQUA;%1");
            lang.put("interestrate", "&YELLOW;Current Interest rate is: &AQUA;%1%");
            lang.put("cmd_pay", "&YELLOW;Pay back youre credits");
            lang.put("cmd_get", "&YELLOW;Request a credit");
            lang.put("cmd_info", "&YELLOW;Info's about running credits");
            lang.put("cmd_max", "&YELLOW;The maximal amount you can credit");
            lang.put("cmd_bad", "&YELLOW;The amount when a credit starts hurting");
            lang.put("cmd_rate", "&YELLOW;The current interest rate");
            lang.put("nomoney", "&RED;You dont have enough money to do that");
            lang.put("paybackall", "&YELLOW;Paid last &GREEN;%1 &YELLOW;from youre credit back! &GOLD;- &YELLOW;Money left: &GREEN;%2");
            lang.put("payback", "&YELLOW;New credit balance: &AQUA;%1&GOLD; - &YELLOW;Money left: &GREEN;%2");
            lang.put("alsocan", "&AQUA;You also can use &PINK;%1 &AQUA;instead of &PINK;%2");
            lang.put("acmd_set", "&YELLOW;Set MineCredit options");
            lang.put("acmd_options", "&YELLOW;Show MineCredit options");
            lang.put("acmd_reload", "&YELLOW;Show MineCredit options");
            lang.put("reload", "&GREEN;Successfully reloaded MineCredit!");
            lang.put("creditoptions", "&YELLOW;MineCredit Options");
            lang.put("opt_language", "&YELLOW;Set Language file from MineCredit/lang");
            lang.put("opt_interest", "&YELLOW;Set interests");
            lang.put("opt_maxcredit", "&YELLOW;Set maximal credit");
            lang.put("opt_badcredit", "&YELLOW;Set to much credit");
            lang.put("opt_interesttime", "&YELLOW;Set time to calc interests &GRAY;H:M");
            lang.put("opt_grayout", "&YELLOW;Gray out users with Bad Credit Amount");
            lang.put("opt_onfire", "&YELLOW;Burn bad talking users a bit (half heart)");
            lang.put("opt_enableshortcut", "&YELLOW;Toggle /c shortcut");
            lang.put("opt_enableadminshortcut", "&YELLOW;Toggle /ca shortcut");
            lang.put("opt_usepermission", "&YELLOW;Toggle Permission support");
            lang.put("opt_remind", "&YELLOW;Remind the user if he has an bad amount");
            lang.put("opt_remindtime", "&YELLOW;Minutes how often to remind");
            lang.put("unknownoption", "&RED;Unknown Option");
            lang.put("optionset", "&GREEN;Option &PINK;'%1'&GREEN; set to: &AQUA;%2");
            lang.put("remind", "&YELLOW;Its time to pay youre credit back!");
            lang.put("remind_f", "&YELLOW;Dont you hate burning everytime talking? :P");
            lang.put("remind_g", "&YELLOW;Dont you hate being the gray guy? :P");
            lang.put("remind_fg", "&YELLOW;Dont you hate burning everytime talking and being gray? :P");
            lang.put("remind_n", "&YELLOW;Dont be mad :P");
            lang.put("remindcredit", "&YELLOW;Youre credit is: &AQUA;%1&YELLOW; All over &AQUA;%2&YELLOW; is to much");
            lang.store(out, "MineCredit EN language file");
            out.close();
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean hasConfigKey(String key) {
        if (config.containsKey(key)) {
            return true;
        } else {
            return false;
        }
    }

    public void setConfig(String confix, String value) {
        FileOutputStream out;
        try {
            out = new FileOutputStream(configs);
            config.put(confix, value);
            config.store(out, "Credit File");
            out.close();
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUserCredit(String user, String string) {
        FileOutputStream out;
        try {
            out = new FileOutputStream(credits);
            credit.put(user, string);
            credit.store(out, "Credit File");
            out.close();
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addUserCredit(String user, String string) {
        int ouc = Integer.parseInt(this.getCreditKey(user)) + Integer.parseInt(string);
        FileOutputStream out;
        try {
            out = new FileOutputStream(credits);
            credit.put(user, ouc + "");
            credit.store(out, "Credit File");
            out.close();
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getLastInterest() {
        return Integer.parseInt(config.getProperty("lastinterest"));
    }

    public void setLastInterest(int last) {
        FileOutputStream out;
        try {
            out = new FileOutputStream(configs);
            config.put("lastinterest", last + "");
            config.store(out, "MineCredit config file");
            out.close();
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void calcInterest() {
        Bukkit.getServer().broadcastMessage(this.getLangKey("interesttime"));
        for (Enumeration e = credit.keys(); e.hasMoreElements(); ) {
            String key = (String) e.nextElement();
            double value = Integer.parseInt(credit.getProperty(key));
            double raise = (value / 100) * Integer.parseInt(this.getConfKey("interest"));
            double newval = value + raise;
            credit.setProperty(key, newval + "");
            if (Bukkit.getServer().getPlayer(key).isOnline()) {
                Bukkit.getServer().getPlayer(key).sendMessage(this.getLangKey("interestplayer", newval + "", raise + ""));
            }
        }
        FileOutputStream out;
        try {
            out = new FileOutputStream(credits);
            config.store(out, "MineCredit credit file");
            out.close();
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean playerBad(String player) {
        if (this.hasCredit(player) && (Integer.parseInt(this.getCreditKey(player)) >= Integer.parseInt(this.getConfKey("badcredit")))) {
            return true;
        } else {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public void remind() {
        for (Enumeration e = credit.keys(); e.hasMoreElements(); ) {
            String key = (String) e.nextElement();
            int value = Integer.parseInt(credit.getProperty(key));
            if (Bukkit.getServer().getPlayer(key).isOnline() && (value >= Integer.parseInt(this.getConfKey("badcredit")))) {
                Bukkit.getServer().getPlayer(key).sendMessage(this.getLangKey("remind"));
                if (this.getConfKey("bad_onfire").equals("true") && this.getConfKey("bad_grayout").equals("true")) {
                    Bukkit.getServer().getPlayer(key).sendMessage(this.getLangKey("remind_fg"));
                } else if (this.getConfKey("bad_onfire").equals("true")) {
                    Bukkit.getServer().getPlayer(key).sendMessage(this.getLangKey("remind_f"));
                } else if (this.getConfKey("bad_grayout").equals("true")) {
                    Bukkit.getServer().getPlayer(key).sendMessage(this.getLangKey("remind_g"));
                } else {
                    Bukkit.getServer().getPlayer(key).sendMessage(this.getLangKey("remind_n"));
                }
                Bukkit.getServer().getPlayer(key).sendMessage(this.getLangKey("remindcredit", value + "", this.getConfKey("badcredit") + ""));
            }
        }
    }
}
