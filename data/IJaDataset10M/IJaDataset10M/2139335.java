package se.bluebrim.crud.tutorial.server;

import static org.junit.Assert.assertTrue;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import se.bluebrim.crud.tutorial.Artist;
import se.bluebrim.crud.tutorial.ServiceLocator;

public class ArtistDaoTest {

    Artist artist;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Resource[] overridingFiles = { new ClassPathResource("application-context.properties") };
        ServiceLocator.init("application-context.xml", overridingFiles);
    }

    @After
    public void tearDown() throws Exception {
        if (artist != null) ServiceLocator.getArtistDao().removeArtist(artist.getId());
    }

    @Test
    public void testAdd() throws Exception {
        artist = new Artist(-1, "Miles Davis", "Famous jazz musician");
        int id = ServiceLocator.getArtistDao().addArtist(artist);
        artist.setId(id);
        assertTrue(artist.getId() > -1);
    }
}
