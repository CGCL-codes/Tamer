package de.excrawler.server.Server;

/**
 * Static Strings for ex-crawler server help commands
 * @author Yves Hoppe <info at yves-hoppe.de>
 * @author Karpouzas George <www.webnetsoft.gr>
 */
public class CrawlerHelp extends Thread {

    public static final String BASICHELP = "Type \"status\" for the current status of the crawler\n" + "Type \"URL: http://www.yoururl.com\" for adding a new URL to the crawler\n" + "Type \"UPDATE: http://www.yoururl.com\" to update a website entry\n" + "Type \"LOGIN: username password\" to get login into your account\n" + "Type \"help\" to see this output.\n" + "Type \"exit\" to close the connection";

    public static final String ADVANCEDHELP = "There are many new commands + basic commands, see the manpage for a overview.\n" + "Type \"help <command>\" to see the help for an command\n" + "Type \"help\" or \"lhelp\" to see this output\n" + "Type \"bhelp\" or \"basichelp\" to see the basic commands (for none users)\n" + "Type \"HELP: <LEVEL>\" for a list of commands for the given user access level (1-9)\n" + "Type \"ME\" to get some informations about your account\n" + "Type \"LOGOUT\" to go back to normal mode \n" + "Type \"EXIT\" to close the connection\n" + "Type \"INFO: <url>\" to see some informations about the url\n" + "Type \"PASSWORD: <old> <new>\" to change your password (USERLEVEL 3+)\n" + "Type \"VERBOSEINFO: <url>\" to get informations about URL (USERLEVEL 7+)\n" + "Type \"UNAME\" to get some informations about the crawler and server (USERLEVEL 7+)\n" + "Type \"CONFIG\" to get the Ex-Crawler Config Options (USERLEVEL 7+)\n" + "Type \"WHO\" for a list of logged in users (USERLEVEL 8+)\n" + "Type \"USERINFO: <name> or <id> or <ip>\" for some infos about the user (USERLEVEL 8+)\n" + "Type \"STOP\" to stop the crawler (USERLEVEL 8+)\n" + "Type \"RESTART\" to restart the crawler (USERLEVEL 8+)\n" + "Type \"START\" to start the crawler (USERLEVEL 8+)\n" + "Type \"SHUTDOWN\" to exit and close the crawler - including this server (USERLEVEL 9+)\n";

    public static final String STATUS_HELP = "---Help for \"STATUS\" command---\n" + "Displays a condensed block of status infomrations about the crawler\n" + "For example:\n" + "Ex-Crawler v0.1.5 Alpha (Agent Brown)\n" + "Running since 10 days, 01:30, Current memory: 423 MB\n" + "Admin: USER @ user.com\n" + "OS: Linux x86_64\n\n" + "Loaded Plugins:\n" + "CacheClean 0.1.1 Alpha\n" + "Stegdetect 0.1.5 Alpha\n\n" + "Thread Overview:\n" + "Webcrawler status: crawling xyz.com\n" + "Imagecrawler status: crawling xyz.com/xy.jpg\n" + "Download WebWorker: downloads xyz.com\n" + "Download ImageWorker: downloads xycv.com\n" + "\n" + "The first line tells you the server Name and it's version.\n" + "The second since when the crawler is running and how much memory it uses\n" + "Admin: Tells you who runs the server\n" + "OS: Tells you which operating system is running.\n" + "Loaded plugins, shows you a list of loaded plugins\n" + "In Thread Overview you get an overview over the running threads and what their status\n";

    public static final String URL_HELP = "---Help for \"URL:\" command---\n" + "Inserts a new url address into the crawler queue\n" + "For example: \n" + "\"URL: http://www.excrawler.de\" inserts the url http://www.ex-crawler.de into the crawler\n" + "Please notice that if the address is already in the crawler, you need \n\n" + "to use the \"UPDATE:\" command";

    public static final String LOGIN_HELP = "---Help for \"URL:\" command---\n" + "With this command you can login into the server with your user account\n" + "for higher user levels, more commands and more\n\n" + "For example: \n" + "Login: user 12345\n" + "";
}
