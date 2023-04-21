package tests.blackboxTests;

import business.management.News;
import business.management.User;
import junit.framework.TestCase;

public class NewsTest extends TestCase {

    public static void main(String[] args) {
    }

    public NewsTest(String arg0) {
        super(arg0);
    }

    /**
     * Tries to get and to set an author into News.
     * @author Lew Palm
     * @date 10.05.2006
     * @testID NT001
     */
    public void testGetAuthor() {
        News testNews = new News();
        User testUser1 = new User();
        User testUser2 = new User();
        testNews.setAuthor(testUser1);
        assertTrue(testNews.getAuthor() == testUser1);
        testNews.setAuthor(testUser2);
        assertTrue(testNews.getAuthor() == testUser2);
    }

    /**
     * Tries to get and to set an author into News.
     * @author Lew Palm
     * @date 10.05.2006
     * @testID NT002
     */
    public void testSetAuthor() {
        News testNews = new News();
        User testUser1 = new User();
        User testUser2 = new User();
        testNews.setAuthor(testUser1);
        assertTrue(testNews.getAuthor() == testUser1);
        testNews.setAuthor(testUser2);
        assertTrue(testNews.getAuthor() == testUser2);
    }

    /**
     * This should test the methods getSubject and setSubject of News.
     * @author Lew Palm
     * @date 10.05.2006
     * @testID NT003
     */
    public void testGetSubject() {
        News testNews = new News();
        String testSubj1 = "Tulpen und Narzissen";
        String testSubj2 = "Guns And Trucks";
        testNews.setSubject(testSubj1);
        assertTrue(testSubj1.equals(testNews.getSubject()));
        testNews.setSubject(testSubj2);
        assertTrue(testSubj2.equals(testNews.getSubject()));
    }

    /**
     * This should test the methods getSubject and setSubject of News.
     * @author Lew Palm
     * @date 10.05.2006
     * @testID NT004
     */
    public void testSetSubject() {
        News testNews = new News();
        String testSubj1 = "Tulpen und Narzissen";
        String testSubj2 = "Guns And Trucks";
        testNews.setSubject(testSubj1);
        assertTrue(testSubj1.equals(testNews.getSubject()));
        testNews.setSubject(testSubj2);
        assertTrue(testSubj2.equals(testNews.getSubject()));
    }

    /**
     * This should test the methods getText and setText of News.
     * @author Lew Palm
     * @date 10.05.2006
     * @testID NT005
     */
    public void testGetText() {
        News testNews = new News();
        String testText1 = "Ist total langweilig.";
        String testText2 = "Fakten, Fakten, Fakten";
        testNews.setText(testText1);
        assertTrue(testText1.equals(testNews.getText()));
        testNews.setText(testText2);
        assertTrue(testText2.equals(testNews.getText()));
    }

    /**
     * This should test the methods getText and setText of News.
     * @author Lew Palm
     * @date 10.05.2006
     * @testID NT006
     */
    public void testSetText() {
        News testNews = new News();
        String testText1 = "Ist total langweilig.";
        String testText2 = "Fakten, Fakten, Fakten";
        testNews.setText(testText1);
        assertTrue(testText1.equals(testNews.getText()));
        testNews.setText(testText2);
        assertTrue(testText2.equals(testNews.getText()));
    }
}
