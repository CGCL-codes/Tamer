package praktikumid.k10.p14;

/**
 * @author Ago
 *
 */
public class Twitter {

    private String location;

    public Twitter(String location) {
        this.location = location;
    }

    public void run() {
        GoogleGeoCoder ggc = new GoogleGeoCoder();
        CachedGeoCoder cgc = new CachedGeoCoder(ggc, "kohad.csv");
        Coordinates c = cgc.getCoordinates(location);
        c.setPopulation(1000);
        TwitterApi twitterApi = new TwitterApi();
        Tweet[] tweets = twitterApi.getTweets(c);
        for (Tweet t : tweets) {
            System.out.println(t);
        }
    }

    public static void run2(String location) {
        GoogleGeoCoder ggc = new GoogleGeoCoder();
        CachedGeoCoder cgc = new CachedGeoCoder(ggc, "kohad.csv");
        Coordinates c = cgc.getCoordinates(location);
        c.setPopulation(1000);
        TwitterApi twitterApi = new TwitterApi();
        Tweet[] tweets = twitterApi.getTweets(c);
        for (Tweet t : tweets) {
            System.out.println(t);
        }
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        if (args.length == 0) {
            printHelp();
        } else {
            String location = args[0];
            Twitter t = new Twitter(location);
            t.run();
        }
    }

    private static void printHelp() {
    }
}
