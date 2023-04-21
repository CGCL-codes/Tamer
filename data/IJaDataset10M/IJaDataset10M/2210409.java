package uk.co.cocking.getinline2.e2e.test;

import static org.hamcrest.Matchers.containsString;
import uk.co.cocking.getinline2.Runner;
import uk.co.cocking.getinline2.builder.Builder;
import uk.co.cocking.getinline2.pipeline.Consumer;

public class Researcher {

    public static String NEWS_FILE_NAME = "news.csv";

    public static void main(String[] args) throws Exception {
        Consumer<String> pipeline = Builder.SCB.retrieveUrls().readRssItems().selectIfField("description", containsString("to")).formatAsCsv().writeToFile(NEWS_FILE_NAME).build();
        System.exit(new Runner<String>(pipeline).run("http://localhost:8123/"));
    }
}
