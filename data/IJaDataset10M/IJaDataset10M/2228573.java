package spidex.posterSearcher;

import java.util.List;
import javax.swing.ImageIcon;
import spidex.FilmInfoInterface;

public interface PosterSearcher {

    /**
     * Function creates query to film database for posters and infos about films
     * 
     * @param name string describes film which should be find
     * @return List of founded films
     */
    public List<FilmInfoInterface> findFilm(String name);

    /**
     * Function for getting other details about specific movie
     * @param info inicial film info
     */
    public void findInformation(FilmInfoInterface info);

    /**
     * Function finds and returns (also store in info) poster of film
     * @param info information about specific film
     * @return poster of this film
     */
    public ImageIcon getImage(FilmInfoInterface info);
}
