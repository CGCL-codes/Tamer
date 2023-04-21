package moviedb.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.List;
import moviedb.model.Actor;
import moviedb.model.Movie;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

public class MovieDBDAOTest {

    private MovieDBDAO movieDAO;

    private Movie m1, m2, m3, m4;

    private Actor a1, a2, quoteActor, otherActor;

    private static int quoteActor_id;

    private static int movie4id;

    private String xmlQueryResult = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<resultset statement=\"SELECT id, title, year_released FROM movie " + "\n\tWHERE genre='Comedy' AND actor='Actor 1'\">\n" + "  <row>\n" + "    <id>1</id>\n" + "    <title>Title '1</title>\n" + "    <year>1970</year>\n" + "  </row>\n" + "  <row>\n" + "    <id>2</id>\n" + "    <title>Title 2</title>\n" + "    <year>1972</year>\n" + "  </row>\n</resultset>";

    private String movie1xml = "<?xml version=\"1.0\"?>\n" + "<Movie><Title>Title '1</Title><Year>1970</Year>" + "\n<Genres><Genre>Comedy</Genre>" + "\n<Genre>Drama</Genre></Genres>" + "\n<Cast>" + "\n<Actor><FirstName>Actor</FirstName><LastName>1</LastName>" + "\n</Actor></Cast></Movie>";

    private String movie2xml = "<?xml version=\"1.0\"?>\n" + "<Movie><Title>Title 2</Title><Year>1972</Year>" + "\n<Genres><Genre>Comedy</Genre>" + "\n<Genre>Romance</Genre></Genres>" + "\n<Cast>" + "\n<Actor><FirstName>Actor</FirstName><LastName>1</LastName></Actor>" + "\n<Actor><FirstName>Actor</FirstName><LastName>2</LastName></Actor>" + "\n</Cast></Movie>";

    private String movie3xml = "<?xml version=\"1.0\"?>\n" + "<Movie><Title>Title 3</Title><Year>1985</Year>" + "\n<Genres><Genre>Animation</Genre>" + "\n<Genre>Sci-Fi</Genre></Genres>" + "\n<Cast>" + "\n<Actor><FirstName>Actor</FirstName><LastName>1</LastName></Actor>" + "\n<Actor><FirstName>Actor</FirstName><LastName>2</LastName></Actor>" + "\n</Cast></Movie>";

    private String movie4xml = "<?xml version=\"1.0\"?>\n" + "<Movie><Title>Title 4</Title><Year>2000</Year>" + "\n<Genres><Genre>Comedy</Genre></Genres>" + "\n<Cast>" + "\n<Actor><FirstName>Actor</FirstName><LastName>2</LastName></Actor>" + "\n<Actor><FirstName>OtherActor</FirstName><LastName>1</LastName></Actor>" + "\n<Actor><FirstName>Actor</FirstName><LastName>O'Test</LastName></Actor>" + "\n</Cast></Movie>";

    @Before
    public void setUp() throws Exception {
        movieDAO = new MovieDBDAO();
        a1 = new Actor();
        a1.setName("Actor 1");
        a2 = new Actor();
        a2.setName("Actor 2");
        otherActor = new Actor();
        otherActor.setName("OtherActor 1");
        quoteActor = new Actor();
        quoteActor.setName("Actor O'Test");
        m1 = new Movie();
        m1.setTitle("Title '1");
        m1.setYear("1970");
        m1.addGenre("Drama");
        m1.addGenre("Comedy");
        m1.addDirector("Director O'Test");
        m1.addActor(a1);
        m1.setXML(movie1xml);
        m2 = new Movie();
        m2.setTitle("Title 2");
        m2.setYear("1972");
        m2.addGenre("Romance");
        m2.addGenre("Comedy");
        m2.addActor(a1);
        m2.addActor(a2);
        m2.setXML(movie2xml);
        m3 = new Movie();
        m3.setTitle("Title 3");
        m3.setYear("1985");
        m3.addGenre("Sci-Fi");
        m3.addGenre("Animation");
        m3.addGenre("Children's");
        m3.addDirector("Director");
        m3.addActor(a1);
        m3.addActor(a2);
        m3.setXML(movie3xml);
        m4 = new Movie();
        m4.setTitle("Title 4");
        m4.setYear("2000");
        m4.addGenre("Comedy");
        m4.addGenre("Sci-Fi");
        m4.addActor(a2);
        m4.addActor(otherActor);
        m4.addActor(quoteActor);
        m4.setXML(movie4xml);
    }

    @Test
    public void testInsertMovie() {
        try {
            assertTrue(-1 != movieDAO.insertMovie(m1));
            assertTrue(-1 != movieDAO.insertMovie(m2));
            assertTrue(-1 != movieDAO.insertMovie(m3));
            movie4id = movieDAO.insertMovie(m4);
        } catch (DAOException e) {
            fail("Unexpected DAOException");
        }
        assertTrue(-1 != movie4id);
    }

    @Test
    public void testInsertMovieNoXML() {
        Movie noXML = new Movie();
        noXML.setTitle("No XML here");
        noXML.setYear("1940/II");
        try {
            assertTrue(-1 != movieDAO.insertMovie(noXML));
        } catch (DAOException e) {
            fail("Unexpected DAOException");
        }
    }

    @Test
    public void testInsertActorWithQuoteInName() {
        try {
            quoteActor_id = movieDAO.insertActor(quoteActor);
        } catch (DAOException e) {
            fail("Unexpected DAOException");
        }
        System.out.println("quoteActor id: " + quoteActor_id);
        assertTrue(-1 != quoteActor_id);
    }

    /**
	 * This test depends on the previous test running beforehand.
	 */
    @Test
    public void testInsertDupActor() {
        Actor a4 = new Actor();
        a4.setName("Actor O'Test");
        try {
            assertEquals(quoteActor_id, movieDAO.insertActor(a4));
        } catch (DAOException e) {
            fail("Unexpected DAOException");
        }
    }

    /**
	 * Depends on the first test in this class running beforehand.
	 */
    @Test
    public void testGetMovies() {
        List<Movie> movies = null;
        try {
            movies = movieDAO.getMovies();
        } catch (DAOException e) {
            fail("Unexpected DAOException");
        }
        assertEquals(5, movies.size());
    }

    @Test
    public void testInsertActorNotInCast() {
        Actor newActor = new Actor();
        newActor.setName("New Actor");
        Movie stubMovie4 = new Movie();
        stubMovie4.setTitle("Title 4");
        stubMovie4.setYear("2000");
        newActor.addToFilms(stubMovie4);
        try {
            movieDAO.insertActor(newActor);
        } catch (DAOException e) {
            System.out.println(e.getMessage());
            fail("Unexpected DAOException");
        }
        try {
            assertTrue("'New Actor' should be in movie4 XML", movieDAO.isActorInCast(newActor, 4));
        } catch (DAOException e) {
            fail("Unexpected DAOException");
        }
    }

    /**
	 * Reads XML content from movie table if using MySQL. Otherwise if using
	 * Derby the association table 'acts_in' is consulted.
	 */
    @Test
    public void testIsActorInCast() {
        try {
            int actor_id = movieDAO.insertActor(otherActor);
            otherActor.setId(actor_id);
            assertTrue("'OtherActor 1' should be in movie4 XML", movieDAO.isActorInCast(otherActor, 4));
        } catch (DAOException e) {
            fail("Unexpected DAOException");
        }
    }

    @Test
    public void testIsActorWithQuoteInNameInCast() {
        try {
            int actor_id = movieDAO.insertActor(quoteActor);
            quoteActor.setId(actor_id);
            assertTrue("'Actor O'Test' should be in movie4 XML", movieDAO.isActorInCast(quoteActor, 4));
        } catch (DAOException e) {
            fail("Unexpected DAOException");
        }
    }

    @Test
    public void testGetMoviesByGenreAndActor() {
        Monitor mon = null;
        mon = MonitorFactory.start("getMoviesSQL");
        List<Movie> movies = null;
        try {
            movies = movieDAO.getMoviesByGenreAndActor("Comedy", "Actor 1");
        } catch (DAOException e) {
            fail("Unexpected DAOException");
        }
        mon.stop();
        System.out.println(mon);
        assertEquals(2, movies.size());
        assertEquals("Title '1", movies.get(0).getTitle());
        assertEquals("Title 2", movies.get(1).getTitle());
    }

    @Test
    public void testGetMoviesAsXMLByGenreAndActor() {
        Monitor mon = null;
        mon = MonitorFactory.start("getMoviesXML");
        String movieXML = null;
        try {
            movieXML = movieDAO.getMoviesAsXMLByGenreAndActor("Comedy", "Actor 1");
        } catch (DAOException e) {
            fail("Unexpected DAOException");
        }
        mon.stop();
        System.out.println(mon);
        assertEquals(xmlQueryResult, movieXML);
    }

    String actualData_XMLresult = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "\n<resultset statement=\"SELECT id, title, year_released FROM movie " + "\n\tWHERE genre='Comedy' AND actor='Meg Ryan'\">" + "\n  <row>" + "\n    <id>249</id>" + "\n    <title>When Harry Met Sally...</title>" + "\n    <year>1989</year>" + "\n  </row>" + "\n  <row>" + "\n    <id>305</id>" + "\n    <title>Sleepless in Seattle</title>" + "\n    <year>1993</year>" + "\n  </row>" + "\n  <row>" + "\n    <id>445</id>" + "\n    <title>You've Got Mail</title>" + "\n    <year>1998</year>" + "\n  </row>" + "\n</resultset>";

    /**
	 * This test depends on actual data from the problem domain existing in the
	 * database. To use this test comment out the following @Ignore annotation.
	 */
    @Ignore
    @Test
    public void testGetMoviesAsXMLActualData() {
        Monitor mon = null;
        mon = MonitorFactory.start("getMoviesXMLfromData");
        String movieXML = null;
        try {
            movieXML = movieDAO.getMoviesAsXMLByGenreAndActor("Comedy", "Meg Ryan");
        } catch (DAOException e) {
            fail("Unexpected DAOException");
        }
        mon.stop();
        System.out.println(mon);
        assertEquals(actualData_XMLresult, movieXML);
    }
}
