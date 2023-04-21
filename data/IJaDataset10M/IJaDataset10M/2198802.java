package net.sf.kmoviecataloger.persistence;

import java.util.List;
import java.util.Map;
import net.sf.kmoviecataloger.core.Label;
import net.sf.kmoviecataloger.core.Loan;
import net.sf.kmoviecataloger.core.Movie;
import net.sf.kmoviecataloger.core.User;
import net.sf.kmoviecataloger.core.listeners.CatalogerListener;

/**
 *
 * @author sergiolopes
 */
public class XMLEngine implements IPersistence {

    private Map<String, Movie> movies;

    private Map<String, Loan> loans;

    private Map<String, User> users;

    private Map<String, Label> labels;

    public void openCatalog() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void closeCatalog() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void saveCatalog() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void saveAs(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void exportCatalog() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void importCatalog() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void compactCatalog() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean addListener(CatalogerListener l) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean removeListener(CatalogerListener l) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean addMovie(Movie movie) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean removeMovie(Movie movie) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Movie> getAllMovies() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Movie> searchMovie(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean addUser(User user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean removeUser(User user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<User> getAllUsers() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<User> searchUser(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean addLabel(Label label) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean removeLabel(Label label) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Label> getAllLabels() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Label> searchLabel(String text) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean makeLoan(User user, List<Movie> movies) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean checkInMovies(User user, List<Movie> movies) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Loan> getAllLoans() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Loan> searchLoan(User user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Loan> searchLoan(Movie movie) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void reorderMovies(int order) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void importMovies() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean addLoan(Loan loan) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
