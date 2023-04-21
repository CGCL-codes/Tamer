package boogle.gui.controllers;

import boogle.Boogle;
import boogle.entities.Author;
import boogle.entities.Book;
import boogle.entities.Comment;
import boogle.entities.Rating;
import boogle.entities.User;
import boogle.entities.managers.AuthorManager;
import boogle.entities.managers.BookManager;
import boogle.entities.managers.BookReservedManager;
import boogle.gui.MainForm;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author paly
 */
public class BookTabController {

    private MainForm mainForm;

    DefaultTableModel model = new DefaultTableModel();

    List<Book> list = new ArrayList<Book>();

    private Integer selectetRow = -1;

    /**
     * construcotr
     * @param mainForm
     */
    public BookTabController(MainForm mainForm) {
        this.mainForm = mainForm;
    }

    /**
     * initialize books table with data in list
     * @param list
     */
    public void initBooks(List<Book> list) {
        this.list = list;
        model = new DefaultTableModel();
        String[] columns = { "ID", "Name", "Authors", "ISBN", "Publisher", "Rating", "Count" };
        mainForm.getBooksTable().setModel(model);
        model.setColumnIdentifiers(columns);
        for (Book b : list) {
            Vector v = new Vector();
            v.add(b.getId());
            v.add(b.getName());
            v.add(b.getAuthorCollection().toString());
            v.add(b.getIsbn());
            v.add(b.getPublisher());
            v.add(b.getRating());
            v.add(b.getCount());
            model.addRow(v);
        }
    }

    /**
     * return book object that is selected in table
     * @param rowNumber
     * @return Book book
     */
    public Book getSelectedBook(int rowNumber) {
        this.selectetRow = rowNumber;
        return list.get(rowNumber);
    }

    /**
     * updating view/edit form when book in list is selected
     * @param Book b
     */
    public void updateForm(Book b) {
        mainForm.getBookTitleField().setText(b.getName());
        if (b == null) {
            b = new Book();
        }
        if (b.getName() != null) {
            mainForm.getBookDateField().setText(Integer.toString(b.getReleaseDate()));
        }
        mainForm.getBookDescriptionField().setText(b.getDescription());
        mainForm.getBookIsbnField().setText(b.getIsbn());
        mainForm.getBookPublisherField().setText(b.getPublisher());
        DefaultListModel authModel = new DefaultListModel();
        int[] autors = new int[b.getAuthorCollection().size()];
        for (Author a : AuthorManager.getAll()) {
            authModel.addElement(a);
        }
        if (b.getName() != null) {
            mainForm.getBookAuthorsList().setModel(authModel);
        }
        mainForm.getBookCountSpinner().setValue(b.getCount());
        int counter = 0;
        for (Author a : b.getAuthorCollection()) {
            mainForm.getBookAuthorsList().setSelectedValue(a, false);
            autors[counter] = mainForm.getBookAuthorsList().getSelectedIndex();
            counter++;
        }
        mainForm.getBookAuthorsList().setSelectedIndices(autors);
        DefaultListModel comModel = new DefaultListModel();
        for (Comment c : b.getCommentCollection()) {
            comModel.addElement(c);
        }
        Boolean isAdmin = Boogle.getInstance().getUser().getRole().equals("admin");
        Boolean isReservedForUser = BookReservedManager.getByUser(Boogle.getInstance().getUser()).contains(b);
        Rating testRating = new Rating();
        testRating.setBook(b);
        testRating.setUser(Boogle.getInstance().getUser());
        Boolean isRatedByUser = b.getRatingCollection().contains(testRating);
        mainForm.getCommentList().setModel(comModel);
        mainForm.getBookCommentButton().setEnabled(true);
        mainForm.getBookRateButton().setEnabled(!isRatedByUser);
        mainForm.getBookReserveButton().setEnabled(!isReservedForUser);
        mainForm.getBookInsertButton().setEnabled(isAdmin);
        mainForm.getBookSaveButton().setEnabled(isAdmin);
        mainForm.getBookDeleteButton().setEnabled(isAdmin);
    }

    /**
     * saving book recieved as parameter by calling BookManager
     * @param bs
     * @throws java.lang.Exception
     */
    public void saveBook(Book bs) throws Exception {
        bs.setIsbn(mainForm.getBookIsbnField().getText());
        bs.setName(mainForm.getBookTitleField().getText());
        bs.setDescription(mainForm.getBookDescriptionField().getText());
        bs.setPublisher(mainForm.getBookPublisherField().getText());
        List<Author> auth = new ArrayList<Author>();
        for (Object o : mainForm.getBookAuthorsList().getSelectedValues()) {
            Author a = (Author) o;
            auth.add(a);
        }
        bs.setAuthorCollection(auth);
        bs.setCount((Integer.parseInt(mainForm.getBookCountSpinner().getValue().toString())));
        if (!mainForm.getBookDateField().getText().equals("")) bs.setReleaseDate(Integer.parseInt(mainForm.getBookDateField().getText()));
        bs.validate();
        BookManager.update(bs);
        initBooks(BookManager.getAll());
    }

    /**
     * @return the selectetRow
     */
    public Integer getSelectetRow() {
        return selectetRow;
    }

    /**
     * remove book b by calling BookManager
     * @param b
     * @throws java.lang.Exception
     */
    public void delete(Book b) throws Exception {
        BookManager.delete(b);
        initBooks(BookManager.getAll());
        updateForm(new Book());
    }

    /**
     * reserve book b for user u by calling BookReserveManager
     * @param b book to reserve
     * @param u user to reserve book 
     */
    public void reserve(Book b, User u) {
        try {
            BookReservedManager.reserveBook(b, u);
            MainForm.info("ok reserved");
            updateForm(b);
        } catch (Exception ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
            MainForm.error("error when reserving");
        }
    }
}
