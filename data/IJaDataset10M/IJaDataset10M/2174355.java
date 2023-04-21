package org.openware.job.examples.movie.test;

import java.util.Iterator;
import java.util.Date;
import junit.framework.*;
import org.openware.job.data.PersistentManager;
import org.openware.job.examples.movie.persist.Movie;
import org.openware.job.examples.movie.persist.User;
import org.openware.job.examples.movie.persist.Role;
import org.openware.job.examples.movie.persist.Function;
import org.openware.job.examples.movie.persist.Person;
import org.openware.job.examples.movie.persist.Genre;
import org.openware.job.examples.movie.persist.Opinion;
import org.openware.job.examples.movie.persist.MovieGenre;
import org.openware.job.examples.movie.persist.ClassInfo;

/**
 * Tests job relationships
 *
 * @author Karen S. Yang
 * @version 1.0
 */
public class RelationshipTest extends TestCase {

    private PersistentManager pmanager;

    private User reviewer;

    private Person actor;

    private Movie movie;

    private Role critic;

    private Role reader;

    private Function writeFunc;

    private Function readFunc;

    private Function readFunc2;

    private Opinion opinion;

    public RelationshipTest(String name, PersistentManager pmanager) throws Exception {
        super(name);
        this.pmanager = pmanager;
    }

    /**
     * Set up fixtures. Create reviewer user. Create actor. Create
     * movie. Create a Critic role who can read and write opinions. Create
     * a Reader role who can read opinions. Create an opinion.
     *
     */
    protected void setUp() {
        try {
            reviewer = createUser();
            actor = createPerson();
            movie = createMovie();
            critic = createRole("Critic", "write opinions");
            reader = createRole("Reader", "read opinions");
            writeFunc = createFunction("Write", "write opinions", critic);
            readFunc = createFunction("Read", "read opinions", reader);
            readFunc2 = createFunction("Read", "read opinions", critic);
            opinion = createOpinion("I love this movie!", 4);
            pmanager.save();
        } catch (Exception e) {
            e.printStackTrace();
            fail("setup failed");
        } finally {
            pmanager.revert();
        }
    }

    /**
     * executed after every test to free resources in setUp
     */
    protected void tearDown() {
        try {
            opinion.remove(true);
            reviewer.remove(true);
            actor.remove(true);
            movie.remove(true);
            critic.remove(true);
            reader.remove(true);
            pmanager.save();
        } catch (Exception e) {
            e.printStackTrace();
            fail("tearDown failed");
        } finally {
            pmanager.revert();
        }
    }

    /**
     * Give the reviewer the role of critic. Associate the reviewer to the
     * opinion we created in setUp.  Associate the opinion to the movie.
     * Check that the relationships were set up correctly.
     */
    public void testRelationship1() {
        User reviewer2 = null;
        Movie movie2 = null;
        Iterator iter = null;
        Opinion op2 = null;
        try {
            reviewer.setRole(critic);
            opinion.setUser(reviewer);
            opinion.setMovie(movie);
            pmanager.save();
            reviewer2 = (User) MovieTest.load(pmanager, User.class, "Username='" + reviewer.getUsername() + "'");
            assertEquals("Critic", reviewer2.getRole().getName());
            movie2 = (Movie) MovieTest.load(pmanager, Movie.class, "Name='" + movie.getName() + "'");
            iter = movie2.getOpinions();
            if (iter == null || !iter.hasNext()) {
                fail("expecting movie to have opinions");
            }
            op2 = (Opinion) iter.next();
            assertEquals(opinion.getText(), op2.getText());
            assertEquals(movie2.getName(), op2.getMovie().getName());
        } catch (Exception e) {
            e.printStackTrace();
            fail("fail testRelationship1");
        }
    }

    /*** Private functions for set up ****/
    private Movie createMovie() throws Exception {
        Movie movie = new Movie(pmanager);
        movie.setName("Shallow Hal");
        movie.setYear(2001);
        movie.setCountry("USA");
        movie.setLanguage("English");
        return movie;
    }

    private Person createPerson() throws Exception {
        Person person = new Person(pmanager);
        person.setFirstName("Jack");
        person.setLastName("Black");
        person.setSex("Male");
        return person;
    }

    private User createUser() throws Exception {
        User user = new User(pmanager);
        user.setFirstName("Joe");
        user.setLastName("Blogg");
        user.setSex("Male");
        user.setUsername("joedude");
        user.setPassword("blahblah");
        return user;
    }

    private Role createRole(String name, String desc) throws Exception {
        Role role = new Role(pmanager);
        role.setName(name);
        role.setDescription(desc);
        return role;
    }

    private Function createFunction(String name, String desc, Role role) throws Exception {
        Function func = new Function(pmanager);
        func.setName(name);
        func.setDescription(desc);
        func.setRole(role);
        return func;
    }

    private Opinion createOpinion(String text, float rating) throws Exception {
        Opinion opinion = new Opinion(pmanager);
        opinion.setText(text);
        opinion.setRating(rating);
        opinion.setDateOffered(new Date(System.currentTimeMillis()));
        return opinion;
    }
}
