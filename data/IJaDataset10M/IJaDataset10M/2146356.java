package com.chessclub.bettingbot;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Map.Entry;
import org.chessworks.common.javatools.io.FileHelper;

public class Main {

    public static final String versionNumber = "7.35";

    public static final String versionDate = "September 27, 2010";

    /**
	 * The path to the file on disk where the configured bot settings are located. The path defaults to "BettingBot.properties", but can be changed by
	 * setting the "bettingbot.settingsFile" system property on the command-line: "-Dbettingbot.settingsFile=myFile.properties".
	 */
    public static final String SETTINGS_FILE = System.getProperty("bettingbot.settingsFile", "BettingBot.properties");

    public static Properties loadSettingsFile(String settingsFile) {
        Properties configuredSettings = FileHelper.loadExternalPropertiesFile(settingsFile, null);
        Properties systemProperties = System.getProperties();
        configuredSettings.putAll(systemProperties);
        return configuredSettings;
    }

    public static void main(String[] args) {
        String settingsFile = (args.length > 0) ? args[0] : SETTINGS_FILE;
        Properties settings = loadSettingsFile(settingsFile);
        Main bot = new Main();
        bot.setConnectionSettings(settings);
        bot.setAdmins(settings);
        bot.setMiscSettings(settings);
        bot.start();
    }

    public final Assistant myAssi;

    public final BettingBot myBB;

    public Main() {
        myBB = new BettingBot(this);
        myAssi = new Assistant(this);
    }

    public void setConnectionSettings(Properties settings) {
        String bbLogin = settings.getProperty("bettingbot.loginName", "BettingBot");
        String bbPassword = settings.getProperty("bettingbot.password", "unknown");
        String asLogin = settings.getProperty("bbassistent.loginName", "BBAssistent");
        String asPassword = settings.getProperty("bbassistent.password", "unknown");
        String host = settings.getProperty("chessclub.host", "chessclub.com");
        String portString = settings.getProperty("chessclub.port", "5001");
        String bettingChannelString = settings.getProperty("bettingbot.bettingChannel", "280");
        String jinChannelString = settings.getProperty("bettingbot.jinChannel", "282");
        String stocksChannelString = settings.getProperty("bettingbot.stocksChannel", "109");
        int port = Integer.decode(portString);
        int bettingChannel = Integer.decode(bettingChannelString);
        int stocksChannel = Integer.decode(stocksChannelString);
        int jinChannel = Integer.decode(jinChannelString);
        System.out.println("Connection Settings:");
        System.out.println("bettingbot.loginName  = " + bbLogin);
        System.out.println("bbassistent.loginName = " + asLogin);
        System.out.println("chessclub.host        = " + host);
        System.out.println("chessclub.port        = " + port);
        System.out.println();
        myBB.setLoginName(bbLogin);
        myBB.setLoginPass(bbPassword);
        myBB.setServerName(host);
        myBB.setServerPort(port);
        myBB.setBotAssistantName(asLogin);
        myBB.setBettingBotChannel(bettingChannel);
        myBB.setJinChannel(jinChannel);
        myBB.setStocksChannel(stocksChannel);
        myAssi.setLoginName(asLogin);
        myAssi.setLoginPass(asPassword);
        myAssi.setServerName(host);
        myAssi.setServerPort(port);
        myAssi.setBettingBotName(bbLogin);
        myAssi.setBettingBotChannel(bettingChannel);
    }

    private void setMiscSettings(Properties settings) {
        String featuredString = settings.getProperty("bettingbot.slotFeature", "true");
        boolean slotsFeature = featuredString.equalsIgnoreCase("true");
        myBB.setSlotFeature(slotsFeature);
    }

    public void setAdmins(Properties settings) {
        List<String> programmers = new ArrayList<String>();
        List<String> admins = new ArrayList<String>();
        for (Entry<Object, Object> entry : settings.entrySet()) {
            Object key = entry.getKey();
            if (key instanceof String) {
                String name = (String) key;
                if (name.startsWith("bettingbot.programmer.")) {
                    String value = (String) entry.getValue();
                    programmers.add(value);
                } else if (name.startsWith("bettingbot.manager.")) {
                    String value = (String) entry.getValue();
                    admins.add(value);
                }
            }
        }
        System.out.println("Programmers:");
        for (String name : programmers) {
            System.out.print("\t");
            System.out.println(name);
        }
        System.out.println();
        System.out.println("Admins:");
        for (String name : admins) {
            System.out.print("\t");
            System.out.println(name);
        }
        System.out.println();
        myBB.setAdminHandles(admins);
        myBB.setProgrammerHandles(programmers);
    }

    public void start() {
        System.out.println("Starting BettingBot");
        myBB.start();
        System.out.println("Starting BBAssistent");
        myAssi.start();
        System.out.println();
    }
}
