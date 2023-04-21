package imdbfolder.gui;

import imdbfolder.data.Movie;
import imdbfolder.data.MovieSelectionMap;
import imdbfolder.data.SortMode;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;

public class MovieListController {

    private final MovieListPanel view;

    public MovieListPanel getView() {
        return view;
    }

    public MovieListController() {
        this.view = new MovieListPanel(this);
    }

    private Hashtable<File, UnknownMovieController> unknownMovieControllers;

    private Hashtable<File, MovieController> knownMovieControllers;

    private SortMode sortMode = SortMode.ALPHABETICALLY;

    public void loadFolder(File selectedFolder) {
        unknownMovieControllers = new Hashtable<File, UnknownMovieController>();
        knownMovieControllers = new Hashtable<File, MovieController>();
        this.view.clearFiles();
        for (File file : selectedFolder.listFiles()) {
            if (file.getName().equals(".imdbfolder")) {
                continue;
            }
            Movie movie = MovieSelectionMap.getInstance().getMovie(file);
            if (movie != null) {
                MovieController controller = new MovieController(this, movie);
                knownMovieControllers.put(file, controller);
            } else {
                UnknownMovieController controller = new UnknownMovieController(file, this);
                unknownMovieControllers.put(file, controller);
            }
        }
        displayList();
        for (Entry<File, UnknownMovieController> entry : unknownMovieControllers.entrySet()) {
            entry.getValue().runSearch(false, true);
        }
    }

    public void selectMovie(File file, Movie movie) {
        UnknownMovieController unknownMovieController = unknownMovieControllers.get(file);
        if (unknownMovieController != null) {
            unknownMovieControllers.remove(file);
            MovieController movieController = new MovieController(this, movie);
            this.view.showKnownMovie(movieController.getView(), unknownMovieController.getView());
            movieController.updateIfNecessary();
            knownMovieControllers.put(file, movieController);
        }
    }

    public void clearKnownMovie(Movie movie, MoviePanel moviePanel) {
        File movieFile = new File(movie.getFileName());
        knownMovieControllers.remove(movieFile);
        UnknownMovieController controller = new UnknownMovieController(movieFile, this);
        this.view.showUnknownMovie(controller.getView(), moviePanel);
        unknownMovieControllers.put(movieFile, controller);
        controller.runSearch(true, false);
    }

    private String searchText = "";

    public void displayList() {
        view.clearAll();
        for (Entry<File, UnknownMovieController> entry : unknownMovieControllers.entrySet()) {
            File file = entry.getKey();
            boolean isMatch = file.getName().toLowerCase().indexOf(searchText) != -1;
            if (isMatch) {
                view.showUnknownMovie(entry.getValue().getView(), null);
            }
        }
        List<MovieController> knownMovieControllersSorted = getKnownMovieControllersSorted();
        for (MovieController controller : knownMovieControllersSorted) {
            if (controller.getMovie().matchesText(searchText)) {
                view.showKnownMovie(controller.getView(), null);
            }
        }
        view.revalidateContents();
    }

    private List<MovieController> getKnownMovieControllersSorted() {
        List<MovieController> list = new ArrayList<MovieController>();
        for (Entry<File, MovieController> entry : knownMovieControllers.entrySet()) {
            list.add(entry.getValue());
        }
        Collections.sort(list, sortMode.getComparator());
        return list;
    }

    public void setSearchText(String text) {
        searchText = text.toLowerCase();
    }

    public void setSortMode(SortMode sortMode) {
        this.sortMode = sortMode;
    }
}
