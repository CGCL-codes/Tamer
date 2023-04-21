package Repository.Entities;

import static org.junit.Assert.*;
import java.util.Iterator;
import java.util.LinkedList;
import org.junit.Assert;
import org.junit.Test;

/**
 * Class that represents the Authors Information
 * @author G09
 * @version 0.5.1
 * @since 0.4
 */
public class AuthorsInfoJUnit {

    AuthorsInfo testAI = new AuthorsInfo();

    /**
	 * Test method for {@link Repository.Entities.AuthorsInfo#AuthorsInfo()}.
	 */
    @Test
    public void testAuthorsInfo() {
        testAI = new AuthorsInfo();
        Assert.assertNotNull(testAI);
    }

    /**
	 * Test method for {@link Repository.Entities.AuthorsInfo#addAuthor(Repository.Entities.IAuthorInfo)}.
	 */
    @Test
    public void testAddAuthor() {
        AuthorInfo ai = new AuthorInfo();
        ai.setFirstName("G09");
        testAI.addAuthor(ai);
        Assert.assertEquals(ai, testAI.author.getFirst());
    }

    /**
	 * Test method for {@link Repository.Entities.AuthorsInfo#getAuthorsIterator()}.
	 */
    @Test
    public void testGetAuthorsIterator() {
        LinkedList<IAuthorInfo> lista = new LinkedList<IAuthorInfo>();
        Iterator<IAuthorInfo> iter = lista.descendingIterator();
        testAI.author = lista;
        Assert.assertEquals(iter.getClass(), testAI.getAuthorsIterator().getClass());
    }
}
