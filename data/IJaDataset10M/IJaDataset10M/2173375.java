package fr.bibiche.test.tvixieAddOn;

import static junit.framework.Assert.*;
import java.io.File;
import org.junit.Test;
import fr.bibiche.tvixieAddOn.bean.ListString;
import fr.bibiche.tvixieAddOn.bean.MovieInfo;
import fr.bibiche.tvixieAddOn.exceptions.TechnicalException;
import fr.bibiche.tvixieAddOn.parser.TvixieInfosInterface;

/**
 * <b>Auteur : </b> FT/NCPI/DPS/DDP <br>
 * <b>Fichier : </b> TvixieInfosInterfaceTest.java <b>du projet</b> TvixieAddOn
 * <br>
 * <b>Date de création :</b> 23 avr. 10 <br>
 * <b>Description : </b>Tests sur l'interface avec les fichiers MovieInfo.tvixie<br>
 */
public class TvixieInfosInterfaceTest {

    /**
     * Test de création/mise à jour d'info
     */
    @Test
    public void createMovieInfo() {
        MovieInfo movie = new MovieInfo();
        movie.setTitle("titre");
        movie.setActors(new ListString());
        movie.getActors().getString().add("test1");
        movie.getActors().getString().add("test2");
        try {
            TvixieInfosInterface tii = new TvixieInfosInterface();
            tii.setMovieInfo(movie, System.out);
        } catch (TechnicalException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
     * test de récupérations des infos
     */
    @Test
    public void getMovieInfo() {
        try {
            TvixieInfosInterface tii = new TvixieInfosInterface();
            MovieInfo info = tii.getMovieInfo(new File(getClass().getResource("/MovieInfo.tvixie").getPath()));
            assertEquals("titre", info.getTitle());
        } catch (TechnicalException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
}
