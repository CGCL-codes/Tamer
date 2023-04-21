package be.stijn.moviez.handler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import be.stijn.moviez.beans.MovieBean;
import be.stijn.moviez.dom.Movie;

public class PersisterLink extends MovieHandlerChain {

    private MovieBean movieBean;

    @Override
    public void handle(Movie movie) {
        getMovieBean().save(movie);
        super.handle(movie);
    }

    private MovieBean getMovieBean() {
        try {
            if (movieBean != null) {
                return movieBean;
            }
            Context ctx = getInitialContext();
            return (MovieBean) ctx.lookup("RemoteMovieBean");
        } catch (Exception e) {
            throw new PersisterLinkException(e);
        }
    }

    private Context getInitialContext() throws NamingException, FileNotFoundException, IOException {
        Properties p = new Properties();
        InputStream stream = PersisterLink.class.getResourceAsStream("../../../../jndi.properties");
        assert stream != null : "jndi.properties file not found";
        p.load(stream);
        return new InitialContext(p);
    }
}
