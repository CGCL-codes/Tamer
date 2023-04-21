package dmitrygusev.ratings.services;

import java.util.ArrayList;
import java.util.List;
import dmitrygusev.io.StringUtils;
import dmitrygusev.ratings.model.Movie;
import dmitrygusev.ratings.model.MoviePack;

public class MoviePacker {

    private List<MoviePack> packs = new ArrayList<MoviePack>();

    public List<MoviePack> packMovies(List<Movie> movies) {
        for (Movie movie : movies) {
            MoviePack pack = findPack(movie);
            if (pack == null) {
                pack = new MoviePack(movie);
                packs.add(pack);
            } else {
                pack.addMovie(movie);
            }
        }
        return packs;
    }

    private MoviePack findPack(Movie movie) {
        for (MoviePack pack : packs) {
            for (Movie m : pack.getMovies()) {
                if (safeEquals(m.getYear(), movie.getYear()) && (safeEquals(m.getTitle(), movie.getTitle()) || safeEquals(m.getTitle(), movie.getTitle2()) || safeEquals(m.getTitle2(), movie.getTitle()) || safeEquals(m.getTitle2(), movie.getTitle2()))) {
                    return pack;
                }
            }
        }
        return null;
    }

    private static boolean safeEquals(String a, String b) {
        if (StringUtils.isNullOrEmpty(a) || StringUtils.isNullOrEmpty(b)) {
            return false;
        }
        return Matcher.titlesAreTheSame(a, b);
    }

    public List<MoviePack> getResultedPacks() {
        return packs;
    }
}
