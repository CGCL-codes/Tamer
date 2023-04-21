package org.bitdrive.file.movies.impl.local;

import org.bitdrive.core.logging.api.Level;
import org.bitdrive.core.logging.api.Logger;
import org.bitdrive.file.core.api.MimeLookup;
import org.bitdrive.file.movies.api.Movie;
import org.bitdrive.file.movies.api.MovieMetaDataService;
import org.bitdrive.file.movies.impl.remote.RemoteMovie;
import org.bitdrive.file.movies.impl.remote.RemoteMovieFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class LocalMovie extends RemoteMovie {

    public String originalName;

    public ArrayList<LocalMovieFile> localFiles;

    public ArrayList<LocalMovieFile> localSubtitles;

    public LocalMovie() {
    }

    public LocalMovie(Logger logger, MimeLookup mimeLookup, String title, String originalName, LinkedList<String> localFiles, LinkedList<String> localSubtitles) {
        this.name = title;
        this.originalName = originalName;
        this.localFiles = new ArrayList<LocalMovieFile>();
        this.localSubtitles = new ArrayList<LocalMovieFile>();
        this.files = new ArrayList<RemoteMovieFile>();
        this.subtitles = new ArrayList<RemoteMovieFile>();
        Collections.sort(localFiles);
        Collections.sort(localSubtitles);
        for (String s : localFiles) {
            try {
                this.localFiles.add(new LocalMovieFile(s, mimeLookup));
            } catch (Exception e) {
                logger.log(Level.WARNING, "LocalMovie.constructor: Failed to add movie file", e);
            }
        }
        for (String s : localSubtitles) {
            try {
                this.localSubtitles.add(new LocalMovieFile(s, mimeLookup));
            } catch (Exception e) {
                logger.log(Level.WARNING, "LocalMovie.constructor: Failed to add movie subtitle file", e);
            }
        }
        files.addAll(this.localFiles);
        subtitles.addAll(this.localSubtitles);
    }

    public boolean getMetadata(MovieMetaDataService metaService) {
        Movie movie = metaService.search(name);
        if (movie == null) return false;
        copyMetaData(movie);
        return true;
    }

    public boolean getFullMetadata(MovieMetaDataService metaService) {
        Movie movie;
        try {
            if (this.id <= 0) {
                if (!getMetadata(metaService)) return false;
            }
            movie = metaService.get(this.id);
        } catch (Exception e) {
            movie = null;
        }
        if (movie == null || !movie.isMetadataPresent()) {
            try {
                movie = metaService.get(localFiles.get(0).file);
            } catch (Exception e) {
                movie = null;
            }
            if (movie == null) return false;
        }
        copyMetaData(movie);
        return true;
    }

    @Override
    public String toString() {
        StringBuilder out;
        out = new StringBuilder();
        out.append(super.toString());
        if (localFiles != null) {
            out.append("\t files:\n");
            for (LocalMovieFile f : localFiles) {
                out.append(f.toString());
                out.append("\t\t---------------------------\n");
            }
        }
        if (localSubtitles != null) {
            out.append("\t subtitles:\n");
            for (LocalMovieFile f : localSubtitles) {
                out.append(f.toString());
                out.append("\t\t---------------------------\n");
            }
        }
        return out.toString();
    }
}
