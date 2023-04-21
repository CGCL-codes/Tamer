package prototype3;

import java.awt.Dimension;
import java.awt.Scrollbar;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.ListSelectionModel;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import processing.core.*;

/**
 *
 * @author Nantia
 */
public class proGraph extends PApplet {

    PApplet parent;

    PImage img;

    ListSelectionModel listSelectionModel;

    Random randomGenerator = new Random();

    RAMDirectory idx;

    Scrollbar sH, sV;

    double[][] mPoints = null;

    String[][] tweetDetails = null;

    String[][] tweetList = null;

    Integer[][] myPoints = null;

    Integer[][] thePoints = null;

    String[] conTweets = null;

    String[] months = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec" };

    String[] days = { "1st", "2nd", "3rd", "4th", "5th", "6th", "7th", "8th", "9th", "10th", "11th", "12th", "13th", "14th", "15th", "16th", "17th", "18th", "19th", "20th", "21st", "22th", "23th", "24th", "25th", "26th", "27th", "28th", "29th", "30th", "31st" };

    String[] hours = { "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "24:00" };

    Integer[] col1 = null;

    Integer[][] myGraphs = null;

    ArrayList<String> wordsIn = new ArrayList<String>();

    public static ArrayList<String> User;

    public static ArrayList<String> Date;

    public static ArrayList<String> Picture;

    public static ArrayList<String> Tweet;

    public static int month;

    public static int day;

    int i, k;

    int x = 35;

    int x1 = 13;

    int x2 = 17;

    int z = 0;

    int z1 = 0;

    int z2 = 0;

    public static int myMonth;

    public static int myDay;

    public static int myHour;

    public static int myX;

    int myY;

    String word;

    public proGraph(Integer[] col1, ArrayList<String> wordsIn, Integer[][] myGraphs, String[][] tweetDetails, int month, int day) {
        this.col1 = col1;
        this.myGraphs = myGraphs;
        this.wordsIn = wordsIn;
        this.tweetDetails = tweetDetails;
        proGraph.month = month;
        proGraph.day = day;
    }

    @Override
    public void setup() {
        size(850, 260);
        noLoop();
        frameRate(60);
    }

    @Override
    public void redraw() {
        myPoints = tranPoints(myGraphs, wordsIn);
        thePoints = graphPoints(myPoints, wordsIn);
        background(255, 255, 240);
        if (myGraphs.length == 12) {
            years();
        } else if (myGraphs.length == 31) {
            months();
        } else if (myGraphs.length == 24) {
            days();
        }
    }

    @Override
    public void draw() {
        myPoints = tranPoints(myGraphs, wordsIn);
        thePoints = graphPoints(myPoints, wordsIn);
        background(255, 255, 240);
        if (myGraphs.length == 12) {
            years();
        } else if (myGraphs.length == 31) {
            months();
        } else if (myGraphs.length == 24) {
            days();
        }
    }

    public void days() {
        int q = 10;
        for (i = (wordsIn.size() - 1); i >= 0; i--) {
            x2 = 17;
            fill(0, 0, 255);
            stroke(0, 0, col1[i]);
            strokeWeight(5);
            line(10, q, 30, q);
            text(wordsIn.get(i), 35, q + 2);
            fill(0, 0, col1[i]);
            stroke(5, 185, 150);
            strokeWeight(1);
            beginShape();
            curveVertex(0, 240);
            curveVertex(0, 240);
            for (k = 0; k < myPoints.length; k++) {
                curveVertex(x2, (thePoints[k][i] - (myPoints[k][i])));
                x2 = x2 + 35;
            }
            curveVertex(x2 - 13, 240);
            curveVertex(x2 - 13, 240);
            endShape();
            q = q + 10;
        }
        for (i = 0; i < 25; i++) {
            line(z2, 240, z2, 250);
            if (i == 5 || i == 11 || i == 17 || i == 23) {
                line(z2, 240, z2, 255);
                text(hours[i], z2 + 2, 255);
            }
            z2 = z2 + 35;
        }
    }

    public void months() {
        int q = 10;
        for (i = (wordsIn.size() - 1); i >= 0; i--) {
            x1 = 13;
            fill(0, 0, 255);
            stroke(0, 0, col1[i]);
            strokeWeight(5);
            line(10, q, 30, q);
            text(wordsIn.get(i), 35, q + 2);
            fill(0, 0, col1[i]);
            stroke(5, 185, 150);
            strokeWeight(1);
            beginShape();
            curveVertex(0, 240);
            curveVertex(0, 240);
            for (k = 0; k < myPoints.length; k++) {
                curveVertex(x1, (thePoints[k][i] - (myPoints[k][i])));
                x1 = x1 + 27;
            }
            curveVertex(x1 - 13, 240);
            curveVertex(x1 - 13, 240);
            endShape();
            q = q + 10;
        }
        for (i = 0; i < 32; i++) {
            line(z1, 240, z1, 250);
            if (i == 6 || i == 13 || i == 20 || i == 27) {
                line(z1, 240, z1, 255);
                text(days[i], z1 + 2, 255);
            }
            z1 = z1 + 27;
        }
    }

    public void years() {
        int q = 10;
        for (i = (wordsIn.size() - 1); i >= 0; i--) {
            x = 35;
            fill(0, 0, 255);
            stroke(0, 0, col1[i]);
            strokeWeight(5);
            line(10, q, 30, q);
            text(wordsIn.get(i), 35, q + 2);
            fill(0, 0, col1[i]);
            stroke(5, 185, 150);
            strokeWeight(1);
            beginShape();
            curveVertex(0, 240);
            curveVertex(0, 240);
            for (k = 0; k < myPoints.length; k++) {
                curveVertex(x, (thePoints[k][i] - (myPoints[k][i])));
                x = x + 70;
            }
            curveVertex(x - 35, 240);
            curveVertex(x - 35, 240);
            endShape();
            q = q + 10;
        }
        for (i = 0; i < 13; i++) {
            line(z, 240, z, 250);
            if (i == 0 || i == 3 || i == 6 || i == 9) {
                line(z, 240, z, 255);
                text(months[i], z + 35, 255);
                textAlign(CENTER);
            }
            z = z + 70;
        }
    }

    @Override
    public void mouseClicked() {
        myMonth = 0;
        myDay = 0;
        myHour = 0;
        User = new ArrayList<String>();
        Picture = new ArrayList<String>();
        Date = new ArrayList<String>();
        Tweet = new ArrayList<String>();
        tweetList = null;
        for (i = 0; i < col1.length; i++) {
            if (color(0, 0, col1[i]) == get(mouseX, mouseY)) {
                word = wordsIn.get(i);
                try {
                    idx = new RAMDirectory();
                    Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_33);
                    IndexWriter writer = new IndexWriter(idx, analyzer, IndexWriter.MaxFieldLength.UNLIMITED);
                    for (i = 0; i < tweetDetails.length; i++) {
                        writer.addDocument(createDocument(tweetDetails[i][0], tweetDetails[i][1], tweetDetails[i][2], tweetDetails[i][3]));
                    }
                    writer.optimize();
                    writer.close();
                    Searcher searcher = new IndexSearcher(idx);
                    tweetList = search(searcher, word, tweetDetails.length);
                    searcher.close();
                } catch (Exception e) {
                    System.out.println("Error: " + e.toString() + " " + e.getMessage());
                }
                if (myGraphs.length == 12) {
                    myMonth = getMonth(mouseX);
                    myDay = 0;
                    myHour = 0;
                } else if (myGraphs.length == 31) {
                    myDay = getDay(mouseX);
                    myMonth = 0;
                    myHour = 0;
                } else if (myGraphs.length == 24) {
                    myHour = getHour(mouseX);
                    myDay = 0;
                    myMonth = 0;
                }
            }
        }
    }

    public int getMonth(int mX) {
        int month1 = 0;
        int m = 0;
        for (i = 0; i < 12; i++) {
            if (mX > m && mX < (m + 70)) {
                month1 = i + 1;
            }
            m = m + 70;
        }
        return month1;
    }

    public int getDay(int mX) {
        int day1 = 0;
        int m = 0;
        for (i = 0; i < 31; i++) {
            if (mX > m && mX < (m + 27)) {
                day1 = i + 1;
            }
            m = m + 27;
        }
        return day1;
    }

    public int getHour(int mX) {
        int hour1 = 0;
        int m = 0;
        for (i = 0; i < 24; i++) {
            if (mX > m && mX < (m + 35)) {
                hour1 = i;
            }
            m = m + 35;
        }
        return hour1;
    }

    private Integer[][] tranPoints(Integer[][] points, ArrayList<String> wCount) {
        Integer[][] newPoints = new Integer[points.length][wCount.size()];
        int w = 100 / wCount.size();
        for (i = 0; i < wCount.size(); i++) {
            int max = points[0][i];
            for (k = 0; k < points.length; k++) {
                if (points[k][i] > max) {
                    max = points[k][i];
                }
            }
            for (k = 0; k < points.length; k++) {
                if (points[k][i] == 0) {
                    newPoints[k][i] = 0;
                } else {
                    newPoints[k][i] = points[k][i] * w / max;
                }
            }
        }
        return newPoints;
    }

    private Integer[][] graphPoints(Integer[][] points, ArrayList<String> wCount) {
        Integer[][] newPoints = new Integer[points.length][wCount.size()];
        for (i = 0; i < wCount.size(); i++) {
            if (i == 0) {
                for (k = 0; k < points.length; k++) {
                    newPoints[k][i] = 240;
                }
            } else {
                for (k = 0; k < points.length; k++) {
                    int temp = newPoints[k][i - 1] - points[k][i - 1];
                    newPoints[k][i] = temp;
                }
            }
        }
        return newPoints;
    }

    private static Document createDocument(String user, String date, String picture, String content) {
        Document doc = new Document();
        doc.add(new Field("user", user, Field.Store.YES, Field.Index.NO));
        doc.add(new Field("date", date, Field.Store.YES, Field.Index.NO));
        doc.add(new Field("picture", picture, Field.Store.YES, Field.Index.NO));
        doc.add(new Field("content", content, Field.Store.YES, Field.Index.ANALYZED));
        return doc;
    }

    private String[][] search(Searcher searcher, String queryString, int tweetLength) throws ParseException, IOException {
        ArrayList<String> myTweet = new ArrayList<String>();
        ArrayList<String> myUser = new ArrayList<String>();
        ArrayList<String> myPicture = new ArrayList<String>();
        ArrayList<String> myDate = new ArrayList<String>();
        String[][] myResult;
        QueryParser parser = new QueryParser(Version.LUCENE_33, "content", new StandardAnalyzer(Version.LUCENE_33));
        Query query = parser.parse(queryString);
        TopScoreDocCollector collector = TopScoreDocCollector.create(5 * tweetLength, false);
        searcher.search(query, collector);
        ScoreDoc[] hits = collector.topDocs().scoreDocs;
        int hitCount = collector.getTotalHits();
        myResult = new String[hitCount][6];
        if (hitCount == 0) {
            System.out.println("No matches were found for \"" + queryString + "\"");
        } else {
            for (i = 0; i < hitCount; i++) {
                ScoreDoc scoreDoc = hits[i];
                int docId = scoreDoc.doc;
                Document doc = searcher.doc(docId);
                myUser.add(doc.get("user"));
                myDate.add(doc.get("date"));
                myPicture.add(doc.get("picture"));
                myTweet.add(doc.get("content"));
            }
            for (i = 0; i < myTweet.size(); i++) {
                myResult[i][0] = myUser.get(i);
                myResult[i][1] = myDate.get(i);
                myResult[i][2] = myPicture.get(i);
                myResult[i][3] = myTweet.get(i);
            }
        }
        return myResult;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(850, 270);
    }
}
