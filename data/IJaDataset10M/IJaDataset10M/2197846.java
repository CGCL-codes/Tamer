package dmitrygusev.ratings.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.Test;
import dmitrygusev.io.IOUtils;

public class TestPattern {

    @Test
    public void testKinopoiskSuggestedFilmsPattern() {
        String input = "<a class=\"all\" href=\"/level/1/film/460264/sr/1/\">Название фильма</a>,&nbsp;<a href=\"/level/10/m_act[year]/2009/\" class=orange>2009</a></td> >... </font>";
        Pattern p = KinopoiskSource.KINOPOISK_SUGGESTED_MOVIES_PATTERN;
        Matcher m = p.matcher(input);
        assertTrue(m.find());
        assertEquals("460264", m.group(1));
        assertEquals("Название фильма", m.group(2));
        assertEquals("2009", m.group(3));
    }

    @Test
    public void testKinopoiskRatingPattern() {
        String input = "<a href=\"/level/83/film/45568/\" class=\"continue\" style=\"background: url(/images/dot_or.gif) 0 93% repeat-x; font-weight: normal !important; text-decoration: none\">6.333<span style=\"font:100 14px tahoma, verdana\">&nbsp;&nbsp;3</span></a>";
        Pattern p = KinopoiskSource.KINOPOISK_RATING_PATTERN;
        Matcher m = p.matcher(input);
        assertTrue(m.find());
        assertEquals("6.333", m.group(1));
        assertEquals("3", m.group(2).replaceAll("&nbsp;", ""));
    }

    @Test
    public void testKinopoiskAwaitingRating() {
        String input = "<span id=\"await_percent\" class=\"perc\">66%</span>\r\n" + "<span>3220</span>";
        Pattern p = KinopoiskSource.KINOPOISK_AWAIT_RATING_PATTERN;
        Matcher m = p.matcher(input);
        assertTrue(m.find());
        assertEquals("66%", m.group(1));
        assertEquals("3220", m.group(2));
    }

    @Test
    public void testKinopoiskAwaitingWithNeighbours() throws Exception {
        String charset = "windows-1251";
        String input = IOUtils.readToEnd("test/java/420224.htm", charset);
        Pattern p = KinopoiskSource.KINOPOISK_AWAIT_RATING_PATTERN;
        Matcher m = p.matcher(input);
        assertTrue(m.find());
        assertEquals("66%", m.group(1));
        assertEquals("3220", m.group(2));
    }

    @Test
    public void testKinopoiskTitle() throws Exception {
        String charset = "windows-1251";
        String input = IOUtils.readToEnd("test/java/kinopoisk-search-terminator.htm", charset);
        Pattern p = KinopoiskSource.KINOPOISK_SUGGESTED_MOVIES_PATTERN;
        Matcher m = p.matcher(input);
        assertTrue(m.find());
        assertEquals("507", m.group(1));
        assertEquals("Терминатор", m.group(2));
        assertEquals("1984", m.group(3));
        assertEquals("Terminator, The", m.group(4));
    }

    @Test
    public void testKinopoiskIMBDRating() throws Exception {
        String charset = "windows-1251";
        String input = IOUtils.readToEnd("test/java/507.htm", charset);
        Pattern p = KinopoiskSource.KINOPOISK_IMDB_RATING_PATTERN;
        Matcher m = p.matcher(input);
        assertTrue(m.find());
        assertEquals("8.10", m.group(1));
        assertEquals("153242", m.group(2).replaceAll(" ", ""));
    }

    @Test
    public void testKinopoiskRatingPattern2() throws Exception {
        String input = IOUtils.readToEnd("test/java/507.htm", "windows-1251");
        Pattern p = KinopoiskSource.KINOPOISK_RATING_PATTERN;
        Matcher m = p.matcher(input);
        assertTrue(m.find());
        assertEquals("8.089", m.group(1));
        assertEquals("9767", m.group(2).replaceAll("&nbsp;", ""));
    }

    @Test
    public void testKinopoiskDetailsTitle() throws Exception {
        String input = IOUtils.readToEnd("test/java/507.htm", "windows-1251");
        Pattern p = KinopoiskSource.KINOPOISK_DETAILS_TITLE_PATTERN;
        Matcher m = p.matcher(input);
        assertTrue(m.find());
        assertEquals("Терминатор", m.group(1));
    }

    @Test
    public void testKinopoiskDetailsTitle2() throws Exception {
        String input = IOUtils.readToEnd("test/java/507.htm", "windows-1251");
        Pattern p = KinopoiskSource.KINOPOISK_DETAILS_TITLE2_PATTERN;
        Matcher m = p.matcher(input);
        assertTrue(m.find());
        assertEquals("The Terminator", m.group(1));
    }

    @Test
    public void testKinopoiskDetailsYear() throws Exception {
        String input = IOUtils.readToEnd("test/java/507.htm", "windows-1251");
        Pattern p = KinopoiskSource.KINOPOISK_DETAILS_YEAR_PATTERN;
        Matcher m = p.matcher(input);
        assertTrue(m.find());
        assertEquals("1984", m.group(1));
    }

    @Test
    public void testKinopoiskDetailsCode() throws Exception {
        String input = IOUtils.readToEnd("test/java/507.htm", "windows-1251");
        Pattern p = KinopoiskSource.KINOPOISK_DETAILS_CODE_PATTERN;
        Matcher m = p.matcher(input);
        assertTrue(m.find());
        assertEquals("507", m.group(1));
    }

    @Test
    public void testKinokopilkaMainValuePattern() throws Exception {
        String input = IOUtils.readToEnd("test/java/2873-bolshe-chem-lyubov.htm", "utf-8");
        Pattern p = KinokopilkaSource.KINOKOPILKA_MAIN_RATING_PATTERN;
        Matcher m = p.matcher(input);
        assertTrue(m.find());
        assertEquals("8.5", m.group(1));
        assertEquals("43", m.group(2));
    }

    @Test
    public void testKinokopilkaIMDBValuePattern() throws Exception {
        String input = IOUtils.readToEnd("test/java/2873-bolshe-chem-lyubov.htm", "utf-8");
        Pattern p = KinokopilkaSource.KINOKOPILKA_IMDB_RATING_PATTERN;
        Matcher m = p.matcher(input);
        assertTrue(m.find());
        assertEquals("6.3", m.group(1));
    }

    @Test
    public void testKinokopilkaYearPattern() throws Exception {
        String input = IOUtils.readToEnd("test/java/2873-bolshe-chem-lyubov.htm", "utf-8");
        Pattern p = KinokopilkaSource.KINOKOPILKA_YEAR_PATTERN;
        Matcher m = p.matcher(input);
        assertTrue(m.find());
        assertEquals("2005", m.group(2));
    }

    @Test
    public void testKinokopilkaMainValuePattern2() throws Exception {
        String input = IOUtils.readToEnd("test/java/46-terminator.htm", "utf-8");
        Pattern p = KinokopilkaSource.KINOKOPILKA_MAIN_RATING_PATTERN;
        Matcher m = p.matcher(input);
        assertTrue(m.find());
        assertEquals("9.5", m.group(1));
        assertEquals("197", m.group(2));
    }

    @Test
    public void testKinokopilkaIMDBValuePattern2() throws Exception {
        String input = IOUtils.readToEnd("test/java/46-terminator.htm", "utf-8");
        Pattern p = KinokopilkaSource.KINOKOPILKA_IMDB_RATING_PATTERN;
        Matcher m = p.matcher(input);
        assertTrue(m.find());
        assertEquals("8.1", m.group(1).toString());
    }

    @Test
    public void testKinokopilkaYearPattern2() throws Exception {
        String input = IOUtils.readToEnd("test/java/46-terminator.htm", "utf-8");
        Pattern p = KinokopilkaSource.KINOKOPILKA_YEAR_PATTERN;
        Matcher m = p.matcher(input);
        assertTrue(m.find());
        assertEquals("1984", m.group(2));
    }

    @Test
    public void testKinokopilkaComments() throws Exception {
        String input = IOUtils.readToEnd("test/java/46-terminator.htm", "utf-8");
        Pattern p = KinokopilkaSource.KINOKOPILKA_COMMENTS_COUNT_PATTERN;
        Matcher m = p.matcher(input);
        assertTrue(m.find());
        assertEquals("201", m.group(1));
    }

    @Test
    public void testKinopoiskComments() throws Exception {
        String input = IOUtils.readToEnd("test/java/507.htm", "windows-1251");
        Pattern p = KinopoiskSource.KINOPOISK_COMMENTS_COUNT_PATTERN;
        Matcher m = p.matcher(input);
        assertTrue(m.find());
        assertEquals("34", m.group(1));
    }

    @Test
    public void testKinokopilkaMainPic() throws Exception {
        String input = IOUtils.readToEnd("test/java/46-terminator.htm", "utf-8");
        Pattern p = KinokopilkaSource.KINOKOPILKA_COVER_PATTERN;
        Matcher m = p.matcher(input);
        assertTrue(m.find());
        assertEquals("/system/images/movies/covers/000/000/046/46_large.jpg?1248529378", m.group(1));
    }

    @Test
    public void testKinokopoiskCover() throws Exception {
        String input = IOUtils.readToEnd("test/java/507.htm", "windows-1251");
        Pattern p = KinopoiskSource.KINOPOISK_COVER_PATTERN;
        Matcher m = p.matcher(input);
        assertTrue(m.find());
        assertEquals("/images/film/507.jpg", m.group(1));
    }
}
