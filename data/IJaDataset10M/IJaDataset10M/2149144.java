package booksandfilms.client;

import java.util.ArrayList;
import java.util.Map;
import booksandfilms.client.entities.Author;
import booksandfilms.client.entities.Book;
import booksandfilms.client.entities.Director;
import booksandfilms.client.entities.Film;
import booksandfilms.client.entities.Media;
import booksandfilms.client.entities.Quote;
import booksandfilms.client.entities.Topic;
import booksandfilms.client.entities.UserAccount;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface QueryServiceAsync {

    void getAllAuthors(AsyncCallback<Map<Long, Author>> callback);

    void getAllDirectors(AsyncCallback<Map<Long, Director>> callback);

    void getAuthorById(Long id, AsyncCallback<Author> callback);

    void getDirectorById(Long id, AsyncCallback<Director> callback);

    void getBookById(Long id, AsyncCallback<Book> callback);

    void getFilmById(Long id, AsyncCallback<Film> callback);

    void getAllUsers(AsyncCallback<ArrayList<UserAccount>> callback);

    void getUserById(Long id, AsyncCallback<UserAccount> callback);

    void getAllTopics(AsyncCallback<ArrayList<Topic>> callback);

    void getTopicById(Long id, AsyncCallback<Topic> callback);

    void getQuoteById(Long id, AsyncCallback<Quote> callback);

    void getBookData(AsyncCallback<ArrayList<Map<Long, Media>>> callback);

    void getFilmData(AsyncCallback<ArrayList<Map<Long, Media>>> callback);

    void getQuoteData(AsyncCallback<Map<Long, Media>> callback);
}
