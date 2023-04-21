package org.las.crawler;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Controller {

    private FetchQueue queue;

    private final int crawl_depth;

    private final List<Pattern> forbidden_patterns;

    private final List<Pattern> allowed_patterns;

    public Controller(String seed, int depth) {
        this.queue = new FetchQueue();
        this.crawl_depth = this.current_depth = depth;
        this.forbidden_patterns = new ArrayList<Pattern>();
        this.allowed_patterns = new ArrayList<Pattern>();
        if (seed != null) {
            URLEntity urlEntity = new URLEntity(seed, 1);
            queue.enQueue(urlEntity);
        }
        String forbidden_regex_str = Config.getStringProperty("crawler.url_forbidden_regex", "");
        String allow_regex_str = Config.getStringProperty("crawler.url_allowed_regex", "");
        if (forbidden_regex_str != null) {
            for (String forbidden_regex : forbidden_regex_str.split(";")) {
                forbidden_patterns.add(Pattern.compile(forbidden_regex));
            }
        }
        if (allow_regex_str != null) {
            for (String allowed_regex : allow_regex_str.split(";")) {
                allowed_patterns.add(Pattern.compile(allowed_regex));
            }
        }
    }

    public Controller(List<String> seedList, int depth) {
        this.queue = new FetchQueue();
        this.crawl_depth = this.current_depth = depth;
        this.forbidden_patterns = new ArrayList<Pattern>();
        this.allowed_patterns = new ArrayList<Pattern>();
        if (seedList != null) {
            for (String seed : seedList) {
                URLEntity urlEntity = new URLEntity(seed, 1);
                queue.enQueue(urlEntity);
            }
        }
        String forbidden_regex_str = Config.getStringProperty("crawler.url_forbidden_regex", "");
        String allow_regex_str = Config.getStringProperty("crawler.url_allowed_regex", "");
        if (forbidden_regex_str != null) {
            for (String forbidden_regex : forbidden_regex_str.split(";")) {
                forbidden_patterns.add(Pattern.compile(forbidden_regex));
            }
        }
        if (allow_regex_str != null) {
            for (String allowed_regex : allow_regex_str.split(";")) {
                allowed_patterns.add(Pattern.compile(allowed_regex));
            }
        }
    }

    public Controller(String seed, int depth, List<String> forbidden_regex_list, List<String> allowed_regex_list) {
        this.queue = new FetchQueue();
        this.crawl_depth = this.current_depth = depth;
        this.forbidden_patterns = new ArrayList<Pattern>();
        this.allowed_patterns = new ArrayList<Pattern>();
        if (seed != null) {
            URLEntity urlEntity = new URLEntity(seed, 1);
            queue.enQueue(urlEntity);
        }
        if (forbidden_regex_list != null) {
            for (String forbidden_regex : forbidden_regex_list) {
                forbidden_patterns.add(Pattern.compile(forbidden_regex));
            }
        }
        if (allowed_regex_list != null) {
            for (String allowed_regex : allowed_regex_list) {
                allowed_patterns.add(Pattern.compile(allowed_regex));
            }
        }
    }

    public Controller(List<String> seedList, int depth, List<String> forbidden_regex_list, List<String> allowed_regex_list) {
        this.queue = new FetchQueue();
        this.crawl_depth = this.current_depth = depth;
        this.forbidden_patterns = new ArrayList<Pattern>();
        this.allowed_patterns = new ArrayList<Pattern>();
        if (seedList != null) {
            for (String start_url : seedList) {
                URLEntity urlEntity = new URLEntity(start_url, 1);
                queue.enQueue(urlEntity);
            }
        }
        if (forbidden_regex_list != null) {
            for (String forbidden_regex : forbidden_regex_list) {
                forbidden_patterns.add(Pattern.compile(forbidden_regex));
            }
        }
        if (allowed_regex_list != null) {
            for (String allowed_regex : allowed_regex_list) {
                allowed_patterns.add(Pattern.compile(allowed_regex));
            }
        }
    }

    public void addSeed(String url) {
        URLEntity urlEntity = new URLEntity(url, current_depth);
        queue.jumpQueue(urlEntity);
    }

    public void start() {
        Crawler crawler = new Crawler(this);
        crawler_list.add(crawler);
        crawler.start();
    }

    public void start(int n) {
        for (int i = 0; i < n; i++) {
            Crawler crawler = new Crawler(this);
            crawler_list.add(crawler);
            crawler.start();
        }
    }

    public void pause() {
        for (Crawler crawler : crawler_list) {
            if (crawler.isAlive()) {
                try {
                    crawler.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void resume() {
        for (Crawler crawler : crawler_list) {
            if (crawler.isAlive()) {
                crawler.notify();
            }
        }
    }

    public void stop() {
        for (Crawler crawler : crawler_list) {
            if (crawler.isAlive()) {
                crawler.interrupt();
            }
        }
    }

    public FetchQueue getQueue() {
        return queue;
    }

    public int getCrawl_depth() {
        return crawl_depth;
    }

    public List<Pattern> getForbidden_patterns() {
        return forbidden_patterns;
    }

    public List<Pattern> getAllowed_patterns() {
        return allowed_patterns;
    }

    public int getCurrent_depth() {
        return current_depth;
    }

    public int waitingDepthJump() {
        return current_depth;
    }

    public synchronized boolean isFinish() {
        int counter = 0;
        for (Crawler crawler : crawler_list) {
            if (crawler.getSignal() > 0) {
                counter++;
            }
        }
        if (counter < crawler_list.size()) {
            return false;
        } else {
            return true;
        }
    }

    protected void handlePage(final PageEntity page) {
        page.print();
    }

    private volatile int current_depth = 1;

    private List<Crawler> crawler_list = new ArrayList<Crawler>();
}
