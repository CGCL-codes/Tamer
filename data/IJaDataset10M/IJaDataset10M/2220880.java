package vbullmin.cli;

import vbullmin.*;

/**
 * Command line interface for vbullmin 
 * @author Onur Aslan
 */
public class CLI {

    public static void main(String[] args) {
        System.out.println("CLI doesn't supporting in this version");
        System.exit(0);
        Options.Initialize(args);
        DB db = new DB();
        db.createDb();
        URLs url = new URLs();
        url.addUrl(Options.url, "/archive/index.php/", Options.username, Options.password, Options.patternsfile);
        while (url.next()) {
            Patterns.getPatterns(url.pattern);
            Bot bot = new Bot(db, url.url, "/archive/index.php/");
            if (url.username != null && url.password != null) new Login(url.username, url.password);
            bot.forums();
            bot.forumsPageCount();
            bot.topics();
            bot.posts();
        }
        Exporter exporter = new Exporter(db);
        exporter.forums();
        exporter.topics();
        exporter.posts();
        exporter.close();
        db.close();
    }
}
