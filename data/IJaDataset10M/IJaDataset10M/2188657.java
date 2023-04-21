package be.stijn.moviez.beans.impl;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import be.stijn.moviez.beans.MovieBean;
import be.stijn.moviez.beans.SortOrder;
import be.stijn.moviez.dom.Image;
import be.stijn.moviez.dom.Movie;

@Stateless(mappedName = "RemoteMovieBean")
public class MovieBeanImpl implements MovieBean {

    @PersistenceContext(unitName = "moviez")
    private EntityManager em;

    @Override
    public List<Movie> findMovies() {
        TypedQuery<Movie> query = em.createNamedQuery(Movie.FIND_ALL, Movie.class);
        return query.getResultList();
    }

    @Override
    public Image findImageById(long id) {
        return em.find(Image.class, id);
    }

    @Override
    public List<Movie> findMovies(int max, int first, SortOrder sortOrder) {
        TypedQuery<Movie> query;
        if (SortOrder.ASCENDING.equals(sortOrder)) {
            query = em.createNamedQuery(Movie.FIND_ALL_YEAR_ASCENDING, Movie.class);
        } else {
            query = em.createNamedQuery(Movie.FIND_ALL_YEAR_DESCENDING, Movie.class);
        }
        query.setFirstResult(first);
        query.setMaxResults(max);
        return query.getResultList();
    }

    @Override
    public List<Movie> findMovies(int max, int first, SortOrder sortOrder, String titleFilter) {
        TypedQuery<Movie> query;
        if (SortOrder.ASCENDING.equals(sortOrder)) {
            query = em.createNamedQuery(Movie.FILTER_TITLE_YEAR_ASCENDING, Movie.class);
        } else {
            query = em.createNamedQuery(Movie.FILTER_TITLE_YEAR_DESCENDING, Movie.class);
        }
        query.setFirstResult(first);
        query.setMaxResults(max);
        query.setParameter("title", "%" + titleFilter + "%");
        return query.getResultList();
    }

    @Override
    public int countMovies() {
        TypedQuery<Long> query = em.createNamedQuery(Movie.COUNT_ALL, Long.class);
        return query.getSingleResult().intValue();
    }

    @Override
    public void save(Movie movie) {
        em.persist(movie);
    }
}
