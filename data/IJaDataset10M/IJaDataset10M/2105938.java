package com.umc.collector;

import org.apache.log4j.Logger;
import com.umc.beans.MovieFile;
import com.umc.plugins.Allocine;
import com.umc.plugins.IMDB;

public class MovieInfosOnlineFrench extends MovieInfosOnlineCine {

    public static MovieFile getMovieInfosOnline(MovieFile mf, Logger log) {
        IMDB pluginIMDB = new IMDB("France");
        Allocine pluginCine = new Allocine();
        return MovieInfosOnlineCine.getMovieInfosOnline(mf, pluginIMDB, pluginCine, log);
    }
}
