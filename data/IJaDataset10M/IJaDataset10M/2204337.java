package videothek;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Pr�fungsaufgabe Videothek
 * Hilfsklasse zum Einlesen der m�glichen Genres
 * @author Rudolf Radlbauer
 *
 */
public class Config {

    private static List<String> genres;

    public static List<String> getGenres() throws IOException {
        if (genres == null) {
            genres = new ArrayList<String>();
            InputStream stream = Config.class.getResourceAsStream("genres.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String genre = reader.readLine();
            while (genre != null) {
                if (genre.trim().length() > 0) genres.add(genre);
                genre = reader.readLine();
            }
            reader.close();
            stream.close();
        }
        return genres;
    }
}
