package org.easypeas.testobject;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.easypeas.annotations.Accessor;
import org.easypeas.annotations.EasyPea;
import org.easypeas.annotations.View;

@EasyPea(name = "books")
public class Books {

    @Accessor(name = "defaultBook")
    public Book defaultBook() {
        Book b = new Book();
        b.title = "War and Peace";
        return b;
    }

    @EasyPea(name = "book")
    public static class Book {

        public String title;

        @View(name = "index")
        public void index(HttpServletRequest request, HttpServletResponse response, Map<String, Object> results) {
            results.put("title", title);
        }
    }
}
