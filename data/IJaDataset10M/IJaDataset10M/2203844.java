package spider.crawl;

import spider.util.Redirections;

/**
 * Example code for running a blog
 * make sure you put a valid e-mail address
 *
 * @author Gautam Pant
 */
public class Tester {

    public static String email = "echintha@cs.indiana.edu";

    public static void main(String[] args) {
        String[] urls = new String[2];
        urls[0] = "http://hot.blogrolling.com/";
        urls[1] = "http://www.bloglines.com/blog/chinthaka";
        int maxPages = 100;
        String data = "Data";
        long startTime = System.currentTimeMillis();
        BasicCrawler bf = new BasicCrawler(urls, maxPages, data);
        bf.setMaxThreads(1);
        bf.setMaxFrontier(-1);
        bf.setStorageFile("history.txt");
        bf.setStatFile("statistics.txt");
        Globals.setMail(email);
        bf.startCrawl();
        long endTime = System.currentTimeMillis();
        long total = endTime - startTime;
        System.out.println("Total Time: " + total);
        Redirections.toFile("redirect.txt");
    }
}
