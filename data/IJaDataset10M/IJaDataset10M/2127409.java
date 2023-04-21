package com.em.janus.controller;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.em.janus.dao.DAOFactory;
import com.em.janus.model.Book;
import com.em.janus.model.sections.Section;
import com.em.janus.model.sorting.BookTitleComparator;
import com.em.janus.template.TemplateController;

/**
 * Servlet implementation class ListBookSections
 */
@WebServlet(description = "lists the alphabetical sections for the books", urlPatterns = { "/book_sections", "/book_sections.xml", "/book_sections.html" })
public class ListBookSectionsController extends JanusController {

    private static final long serialVersionUID = 1L;

    @Override
    protected void janusAction(HttpServletRequest request, HttpServletResponse response, Writer out, String mode) throws ServletException, IOException {
        Set<Book> books = DAOFactory.INSTANCE.getDAO(Book.class).get();
        Map<String, Section<Book>> sections = Section.generateAlphabeticalSections();
        Section<Book> other = new Section<Book>();
        other.setName("Starting with numbers or special characters");
        other.setId("OTHER");
        for (Book book : books) {
            String value = book.getSortTitle();
            String first = value.substring(0, 1);
            Section<Book> target = sections.get(first);
            if (target == null) {
                other.getContents().add(book);
            } else {
                target.getContents().add(book);
            }
        }
        Comparator<Book> bookComparator = new BookTitleComparator();
        for (Section<Book> section : sections.values()) {
            Collections.sort(section.getContents(), bookComparator);
        }
        List<Section<Book>> bookSections = new ArrayList<Section<Book>>(sections.values());
        Collections.sort(bookSections);
        if (other.getContents().size() > 0) {
            Collections.sort(other.getContents(), bookComparator);
            bookSections.add(0, other);
        }
        Map<String, Object> elements = new HashMap<String, Object>();
        elements.put("sections", bookSections);
        elements.put("mode", mode);
        TemplateController.INSTANCE.process(out, elements, "xml/book_sections.ftl");
    }
}
