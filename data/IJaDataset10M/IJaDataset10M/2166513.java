package org.sourceforge.vlibrary.user.logic;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Locale;
import org.apache.log4j.Logger;
import org.sourceforge.vlibrary.Constants;
import org.sourceforge.vlibrary.exceptions.LibraryException;
import org.sourceforge.vlibrary.user.dao.AuthorDAO;
import org.sourceforge.vlibrary.user.dao.BookDAO;
import org.sourceforge.vlibrary.user.dao.LibraryTransactionDAO;
import org.sourceforge.vlibrary.user.dao.ReaderDAO;
import org.sourceforge.vlibrary.user.dao.SubjectDAO;
import org.sourceforge.vlibrary.user.domain.Book;
import org.sourceforge.vlibrary.user.domain.Reader;
import org.sourceforge.vlibrary.user.exceptions.BookAwaitingPickupException;
import org.sourceforge.vlibrary.user.exceptions.BookNotFoundException;
import org.sourceforge.vlibrary.user.exceptions.DuplicateRequestException;
import org.sourceforge.vlibrary.user.exceptions.ReaderNotFoundException;
import org.sourceforge.vlibrary.user.valuebeans.BookMoveTransaction;
import org.sourceforge.vlibrary.user.valuebeans.LibraryTransaction;
import org.sourceforge.vlibrary.user.workers.MailWorker;
import org.sourceforge.vlibrary.util.Crypto;
import org.sourceforge.vlibrary.util.SruSrwClientInterface;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * Implementation of {@link LibraryManagerFacade} interface providing
 * the business logic functions for the Virtual Library. Data access is
 * mediated by DAO objects, which are instantiated using Spring dependency
 * injection.
 *
 */
public class LibraryManagerFacadeImpl implements LibraryManagerFacade {

    private ResourceBundleMessageSource resourceBundleMessageSource;

    /** log4j category */
    private static Logger logger = Logger.getLogger(LibraryManagerFacadeImpl.class.getName());

    private AuthorDAO authorDAO;

    private BookDAO bookDAO;

    private ReaderDAO readerDAO;

    private SubjectDAO subjectDAO;

    private LibraryTransactionDAO libraryTransactionDAO;

    private MailWorker mailWorker;

    private Crypto crypto;

    private SruSrwClientInterface sruClient;

    /**
     * Used for Spring Dependency Injection
     */
    public void setResourceBundleMessageSource(ResourceBundleMessageSource resourceBundleMessageSource) {
        this.resourceBundleMessageSource = resourceBundleMessageSource;
    }

    /**
     * @param mailWorker The mailWorker to set.
     */
    public void setMailWorker(MailWorker mailWorker) {
        this.mailWorker = mailWorker;
    }

    /**
     * @param authorDAO The authorDAO to set.
     */
    public void setAuthorDAO(AuthorDAO authorDAO) {
        this.authorDAO = authorDAO;
    }

    /**
     * @param bookDAO The bookDAO to set.
     */
    public void setBookDAO(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    /**
     * @param readerDAO The readerDAO to set.
     */
    public void setReaderDAO(ReaderDAO readerDAO) {
        this.readerDAO = readerDAO;
    }

    public ReaderDAO getReaderDAO() {
        return this.readerDAO;
    }

    /**
     * @param subjectDAO The subjectDAO to set.
     */
    public void setSubjectDAO(SubjectDAO subjectDAO) {
        this.subjectDAO = subjectDAO;
    }

    /**
     * @param libraryTransactionDAO The libraryTransactionDAO to set.
     */
    public void setLibraryTransactionDAO(LibraryTransactionDAO libraryTransactionDAO) {
        this.libraryTransactionDAO = libraryTransactionDAO;
    }

    /**
     * Used for Spring Dependency Injection
     */
    public void setCrypto(Crypto crypto) {
        this.crypto = crypto;
    }

    /**
     * Used for Spring Dependency Injection
     */
    public void setSruSrwClientInterface(SruSrwClientInterface sruClient) {
        this.sruClient = sruClient;
    }

    public ArrayList getSubjects() throws LibraryException {
        if (logger.isDebugEnabled()) logger.debug("");
        return subjectDAO.getSubjects();
    }

    public void createSubjectIds(ArrayList subjects) throws LibraryException {
        if (logger.isDebugEnabled()) logger.debug(subjects);
        subjectDAO.setSubjectIds(subjects);
    }

    public boolean isUserValidByPhone(String phoneNo) throws LibraryException {
        if (logger.isDebugEnabled()) logger.debug("phoneNo=" + phoneNo);
        Reader rd = readerDAO.retrieveByPhone(phoneNo);
        if (rd == null) {
            return false;
        } else {
            return true;
        }
    }

    public long retrieveIDByPhone(String phoneNo) throws LibraryException {
        if (logger.isDebugEnabled()) logger.debug("phoneNo=" + phoneNo);
        Reader rd = new Reader();
        rd = readerDAO.retrieveByPhone(phoneNo);
        if (rd == null) {
            return 0;
        }
        return rd.getId();
    }

    public boolean isUserIDValid(long id) throws LibraryException {
        if (logger.isDebugEnabled()) logger.debug("userId" + id);
        return readerDAO.readerExists(id);
    }

    public boolean isUserIDValid(String UID) throws LibraryException {
        if (logger.isDebugEnabled()) logger.debug("userId" + UID);
        return readerDAO.uidExists(UID);
    }

    public boolean readerExists(String firstName, String lastName) throws LibraryException {
        if (logger.isDebugEnabled()) logger.debug("firstName=" + firstName + ", lastName=" + lastName);
        return readerDAO.readerExists(firstName, lastName);
    }

    /** 
     * Determines whether or not there is a reader with the given ID.
     * 
     * @param id the ID to validate
     * @return true if there is a reader with the given ID;
     * false otherwise
     * @throws LibraryException if a data access error occurs
     */
    public boolean readerExists(long id) throws LibraryException {
        if (logger.isDebugEnabled()) logger.debug("reader=" + id);
        return readerDAO.readerExists(id);
    }

    public boolean deskPhoneExists(String deskPhone) throws LibraryException {
        if (logger.isDebugEnabled()) logger.debug("deskPhone=" + deskPhone);
        return readerDAO.deskPhoneExists(deskPhone);
    }

    public ArrayList getReaders() throws LibraryException {
        if (logger.isDebugEnabled()) logger.debug("");
        return readerDAO.getReaders();
    }

    public LibraryTransaction retrieveTransaction(long id) throws LibraryException {
        if (logger.isDebugEnabled()) logger.debug("tranId=" + id);
        return libraryTransactionDAO.retrieve(id);
    }

    public ArrayList authorSearch(String authorLastName) throws LibraryException {
        if (logger.isDebugEnabled()) logger.debug("authorLastName=" + authorLastName);
        return bookDAO.authorSearch(authorLastName);
    }

    public ArrayList subjectOrSearch(ArrayList subjects) throws LibraryException {
        if (logger.isDebugEnabled()) logger.debug(subjects);
        return bookDAO.subjectOrSearch(subjects);
    }

    public ArrayList subjectAndSearch(ArrayList subjects) throws LibraryException {
        if (logger.isDebugEnabled()) logger.debug(subjects);
        return bookDAO.subjectAndSearch(subjects);
    }

    public ArrayList ownerSearch(long owner) throws LibraryException {
        if (logger.isDebugEnabled()) logger.debug("owner=" + owner);
        return bookDAO.ownerSearch(owner);
    }

    public ArrayList titleSearch(String title) throws LibraryException {
        if (logger.isDebugEnabled()) logger.debug("bookTitle=" + title);
        return bookDAO.titleSearch(title);
    }

    public ArrayList dump() throws LibraryException {
        if (logger.isDebugEnabled()) logger.debug("");
        return bookDAO.dump();
    }

    public Reader retrieveByUid(String uid) throws LibraryException {
        if (logger.isDebugEnabled()) logger.debug("readerUid=" + uid);
        return readerDAO.retrieveByUid(uid);
    }

    public ArrayList getAuthors(Book book) throws LibraryException {
        if (logger.isDebugEnabled()) logger.debug(book.toString());
        return bookDAO.getAuthors(book);
    }

    public ArrayList getSubjects(Book book) throws LibraryException {
        if (logger.isDebugEnabled()) logger.debug(book.toString());
        return bookDAO.getSubjects(book);
    }

    /**
     * {@inheritDoc}
     * <p>Delegates to one of {@link #processCheckin(long, long)} or 
     * {@link #processCheckout(long, long)}, depending on the
     * <code>transactionType</code>, which must be either 
     * <code>Contants.CHECKIN</code> or <code>Contants.CHECKOUT</code>.
     * </p>
     */
    public BookMoveTransaction processExchange(long reader, long book, String transactionType) throws LibraryException {
        if (logger.isDebugEnabled()) {
            String message = resourceBundleMessageSource.getMessage("trans.checking.in", new Object[] { (new Long(reader)).toString(), (new Long(book)).toString() }, Locale.US);
            logger.debug(message);
        }
        Book bk = new Book();
        bk.setId(book);
        try {
            bk = bookDAO.retrieve(book);
            if (bk == null) {
                throw new Exception();
            }
        } catch (Throwable e) {
            String errString = resourceBundleMessageSource.getMessage("error.book.retrieve", new Object[] { bk == null ? "" : bk.toString() }, Locale.US);
            logger.error(errString, e);
            throw new LibraryException(errString, e);
        }
        Reader rd = new Reader();
        try {
            rd = readerDAO.retrieveById(reader);
            if (rd == null) {
                throw new Exception();
            }
        } catch (Throwable e) {
            String errString = resourceBundleMessageSource.getMessage("error.reader.retrieve", new Object[] { rd == null ? "" : rd.toString() }, Locale.US);
            logger.error(errString, e);
            throw new LibraryException(errString, e);
        }
        try {
            if (transactionType.equals(Constants.CHECKIN)) {
                processCheckin(reader, book);
            } else if (transactionType.equals(Constants.CHECKOUT)) {
                processCheckout(reader, book);
            } else {
                String errString = resourceBundleMessageSource.getMessage("error.trans.request.badrequest", new Object[] { (new Long(reader)).toString(), (new Long(book)).toString() }, Locale.US);
                logger.error(errString);
                throw new LibraryException(errString);
            }
        } catch (Throwable e) {
            String errString = resourceBundleMessageSource.getMessage("error.trans.request.badrequest", new Object[] { (new Long(reader)).toString(), (new Long(book)).toString() }, Locale.US);
            logger.error(errString, e);
            throw new LibraryException(e.getMessage(), e);
        }
        BookMoveTransaction t = new BookMoveTransaction();
        t.setReaderEmail(rd.getEmail());
        t.setReaderName(rd.getFirstName() + " " + rd.getLastName());
        t.setBookTitle(bk.getTitle());
        t.setTransaction(transactionType);
        if (logger.isDebugEnabled()) {
            String message = resourceBundleMessageSource.getMessage("trans.checkin.successful", new Object[] { (new Long(reader)).toString(), (new Long(book)).toString() }, Locale.US);
            logger.debug(message);
        }
        return t;
    }

    public boolean bookExists(long book) throws LibraryException {
        if (logger.isDebugEnabled()) logger.debug("book=" + book);
        return bookDAO.bookExists(book);
    }

    public long getReaderID(String firstName, String lastName) throws LibraryException {
        if (logger.isDebugEnabled()) logger.debug("firstname=" + firstName + ", lastName=" + lastName);
        return readerDAO.getReaderID(firstName, lastName);
    }

    public void createAuthorIds(ArrayList authors) throws LibraryException {
        if (logger.isDebugEnabled()) logger.debug(authors);
        authorDAO.setAuthorIds(authors);
    }

    public Book retrieveBook(long bookID) throws LibraryException {
        if (logger.isDebugEnabled()) logger.debug("book=" + bookID);
        return bookDAO.retrieve(bookID);
    }

    public Book retrieveBook(String bookID) throws LibraryException {
        if (logger.isDebugEnabled()) logger.debug("book=" + bookID);
        return bookDAO.retrieve(bookID);
    }

    public Reader retrieveReader(Reader rd) throws LibraryException {
        if (logger.isDebugEnabled()) logger.debug(rd.toString());
        return readerDAO.retrieve(rd);
    }

    public Reader retrieveReader(long id) throws LibraryException {
        if (logger.isDebugEnabled()) logger.debug("reader=" + id);
        return readerDAO.retrieveById(id);
    }

    public void createBookAuthors(Book book, ArrayList authors) throws LibraryException {
        if (logger.isDebugEnabled()) logger.debug(book.toString() + authors);
        bookDAO.setAuthors(book, authors);
    }

    public void createBookSubjects(Book book, ArrayList subjects) throws LibraryException {
        if (logger.isDebugEnabled()) logger.debug(book.toString() + subjects);
        bookDAO.setSubjects(book, subjects);
    }

    public Book createBook(Book book, ArrayList subjects, ArrayList authors) throws LibraryException {
        if (logger.isDebugEnabled()) logger.debug(book.toString() + subjects + authors);
        book = bookDAO.insert(book);
        bookDAO.setAuthors(book, authors);
        bookDAO.setSubjects(book, subjects);
        return book;
    }

    public void updateBook(Book book) throws LibraryException {
        if (logger.isDebugEnabled()) logger.debug(book.toString());
        bookDAO.update(book);
    }

    public void insertReader(Reader reader) throws LibraryException {
        if (logger.isDebugEnabled()) logger.debug(reader.toString());
        readerDAO.insert(reader);
    }

    public void updateReader(Reader reader) throws LibraryException {
        if (logger.isDebugEnabled()) logger.debug(reader.toString());
        readerDAO.update(reader);
    }

    /**
     * <p>Processes book checkin transaction.
     * </p>
     * <p>Records the checkin in the transaction table and sends email
     * notifications to current requestors.
     * </p>
     * <p>Allows redundant checkins - i.e., a book that is already checked in
     * can be checked in again.</p>
     *
     * @param book the id of the book being checked in
     * @param reader the id of the reader checking in the book
     * @throws LibraryException if an error occurs recording the checkin or
     * sending notifications
     */
    public void processCheckin(long reader, long book) throws LibraryException {
        if (logger.isInfoEnabled()) {
            String message = resourceBundleMessageSource.getMessage("trans.checking.in", new Object[] { (new Long(reader)).toString(), (new Long(book)).toString() }, Locale.US);
            logger.info(message);
        }
        libraryTransactionDAO.processCheckin(reader, book);
        Book bk = new Book();
        bk.setId(book);
        bk = retrieveBook(book);
        ArrayList requestors = getRequestors(book);
        for (int i = 0; i < requestors.size(); i++) {
            Reader rd = (Reader) requestors.get(i);
            try {
                mailWorker.sendReturnNotification(rd, bk);
            } catch (Exception ex) {
                String errString = resourceBundleMessageSource.getMessage("error.trans.checkin", new Object[] { (new Long(reader)).toString(), (new Long(book)).toString() }, Locale.US);
                logger.error(errString, ex);
            }
        }
        if (logger.isInfoEnabled()) {
            logger.info(resourceBundleMessageSource.getMessage("trans.checkin.successful", new Object[] { (new Long(reader)).toString(), (new Long(book)).toString() }, Locale.US));
        }
    }

    /**
     * <p>Processes book checkout transaction.
     * </p>
     * <p>Records the checkout in the transaction table and sends email
     * notifications to current requestors.
     * </p>
     *
     * @param book the id of the book being checked out
     * @param reader the id of the reader checking out the book
     * @throws LibraryException if an error occurs recording the checkout or
     * sending notifications
     */
    public void processCheckout(long reader, long book) throws LibraryException {
        if (logger.isDebugEnabled()) {
            logger.info(resourceBundleMessageSource.getMessage("trans.checking.out", new Object[] { (new Long(reader)).toString(), (new Long(book)).toString() }, Locale.US));
        }
        if (!readerDAO.readerExists(reader)) {
            String errString = resourceBundleMessageSource.getMessage("error.nonexistent.reader", new Object[] { new Long(reader) }, Locale.US);
            logger.error(errString);
            throw new ReaderNotFoundException(errString);
        }
        if (!bookExists(book)) {
            String errString = resourceBundleMessageSource.getMessage("error.nonexistent.book", new Object[] { new Long(book) }, Locale.US);
            logger.error(errString);
            throw new BookNotFoundException(errString);
        }
        Reader newPossessor = new Reader();
        newPossessor.setId(reader);
        newPossessor = readerDAO.retrieve(newPossessor);
        libraryTransactionDAO.processCheckout(reader, book);
        Book bk = bookDAO.retrieve(book);
        ArrayList requestors = getRequestors(book);
        for (int i = 0; i < requestors.size(); i++) {
            Reader requestor = (Reader) requestors.get(i);
            try {
                mailWorker.sendCheckoutNotification(requestor, newPossessor, bk);
            } catch (Exception ex) {
                String errString = resourceBundleMessageSource.getMessage("error.trans.checkout", new Object[] { (new Long(reader)).toString(), (new Long(book)).toString() }, Locale.US);
                logger.error(errString, ex);
            }
        }
        if (logger.isDebugEnabled()) {
            logger.info(resourceBundleMessageSource.getMessage("trans.checkout.successful", new Object[] { (new Long(reader)).toString(), (new Long(book)).toString() }, Locale.US));
        }
    }

    /**
     * {@inheritDoc}
     * <p>If there are no transactions associated with the book,
     * then the book's owner is assumed to have the book, so the
     * owner's ID is returned in this case.</p>
     */
    public long getPossessor(long book) throws LibraryException {
        if (logger.isDebugEnabled()) {
            logger.info(resourceBundleMessageSource.getMessage("retrieving.book.possesor", new Object[] { (new Long(book)).toString() }, Locale.US));
        }
        if (!bookExists(book)) {
            String errString = resourceBundleMessageSource.getMessage("error.nonexistent.book", new Object[] { new Long(book) }, Locale.US);
            logger.error(errString);
            throw new BookNotFoundException(errString);
        }
        long result = libraryTransactionDAO.getPossessor(book);
        if (result == Constants.CHECKED_IN || readerExists(result)) {
            return result;
        }
        Book bk = new Book();
        bk.setId(book);
        bk = bookDAO.retrieve(book);
        if (logger.isDebugEnabled()) {
            logger.info(resourceBundleMessageSource.getMessage("retrieving.book.possesor.successful", new Object[] { (new Long(book)).toString() }, Locale.US));
        }
        return bk.getOwner();
    }

    public void processRequest(long reader, long book) throws LibraryException, ReaderNotFoundException, DuplicateRequestException {
        if (logger.isDebugEnabled()) {
            logger.info(resourceBundleMessageSource.getMessage("processing.request", new Object[] { (new Long(reader)).toString(), (new Long(book)).toString() }, Locale.US));
        }
        if (!readerExists(reader)) {
            String errString = resourceBundleMessageSource.getMessage("error.nonexistent.reader", new Object[] { new Long(reader) }, Locale.US);
            logger.error(errString);
            throw new ReaderNotFoundException(errString);
        }
        if (!bookExists(book)) {
            String errString = resourceBundleMessageSource.getMessage("error.nonexistent.book", new Object[] { new Long(book) }, Locale.US);
            logger.error(errString);
            throw new BookNotFoundException(errString);
        }
        if (requestPending(reader, book)) {
            String warning = resourceBundleMessageSource.getMessage("error.trans.duplicate.request", new Object[] { (new Long(reader)).toString(), (new Long(book)).toString() }, Locale.US);
            logger.warn(warning);
            throw new DuplicateRequestException(warning);
        }
        libraryTransactionDAO.processRequest(reader, book);
        Reader possessor = new Reader();
        Reader requestor = new Reader();
        Book bk = new Book();
        bk.setId(book);
        bk = bookDAO.retrieve(book);
        long possessorID = getPossessor(book);
        if (possessorID == Constants.CHECKED_IN) {
            throw new BookAwaitingPickupException();
        }
        possessor.setId(possessorID);
        possessor = readerDAO.retrieve(possessor);
        requestor.setId(reader);
        requestor = readerDAO.retrieve(requestor);
        try {
            mailWorker.sendRequestNotification(possessor, requestor, bk);
            mailWorker.sendRequestConfirmation(possessor, requestor, bk);
        } catch (Exception ex) {
            logger.error(ex);
        }
        if (logger.isDebugEnabled()) {
            logger.info(resourceBundleMessageSource.getMessage("request.processed", new Object[] { (new Long(reader)).toString(), (new Long(book)).toString() }, Locale.US));
        }
    }

    private ArrayList getRequestors(long book) throws LibraryException {
        if (logger.isDebugEnabled()) {
            logger.info(resourceBundleMessageSource.getMessage("retrieving.requesters", new Object[] { (new Long(book)).toString() }, Locale.US));
        }
        List readerIdList = libraryTransactionDAO.getRequestors(book);
        ArrayList outList = new ArrayList();
        java.util.Iterator it = readerIdList.iterator();
        while (it.hasNext()) {
            Long readerId = (Long) it.next();
            Reader rd = new Reader();
            rd.setId(readerId);
            rd = readerDAO.retrieve(rd);
            if (rd != null) {
                outList.add(rd);
            }
        }
        if (logger.isDebugEnabled()) {
            logger.info(resourceBundleMessageSource.getMessage("requesters.retrieved", new Object[] { (new Long(book)).toString() }, Locale.US));
        }
        return outList;
    }

    /**
     * Determines whether or not a reader has a request pending for a book.
     * 
     * @param reader the ID of the reader
     * @param book the ID of the book
     * @return true if the reader with ID <code>reader</code> has a request
     * pending for the book with ID <code>book</code>; false otherwise
     * @throws LibraryException if a data access error occurs
     */
    public boolean requestPending(long reader, long book) throws LibraryException {
        if (logger.isDebugEnabled()) logger.debug("reader=" + reader + ", book=" + book);
        return libraryTransactionDAO.requestPending(reader, book);
    }

    public void cancelRequest(long reader, long book) throws LibraryException {
        if (logger.isDebugEnabled()) {
            logger.info(resourceBundleMessageSource.getMessage("cancelling.request", new Object[] { (new Long(reader)).toString(), (new Long(book)).toString() }, Locale.US));
        }
        if (!readerDAO.readerExists(reader)) {
            String errString = resourceBundleMessageSource.getMessage("error.nonexistent.reader", new Object[] { new Long(reader) }, Locale.US);
            logger.error(errString);
            throw new ReaderNotFoundException(errString);
        }
        if (!bookExists(book)) {
            String errString = resourceBundleMessageSource.getMessage("error.nonexistent.book", new Object[] { new Long(book) }, Locale.US);
            logger.error(errString);
            throw new BookNotFoundException(errString);
        }
        libraryTransactionDAO.cancelRequest(reader, book);
        Book bk = bookDAO.retrieve(book);
        Reader rd = new Reader();
        rd.setId(reader);
        rd = readerDAO.retrieve(rd);
        try {
            mailWorker.sendDeleteConfirmation(rd, bk);
        } catch (Exception ex) {
            logger.error(ex);
        }
        if (logger.isDebugEnabled()) {
            logger.info(resourceBundleMessageSource.getMessage("request.cancelled", new Object[] { (new Long(reader)).toString(), (new Long(book)).toString() }, Locale.US));
        }
    }

    public List getTransactions(long book) throws LibraryException {
        if (logger.isDebugEnabled()) {
            logger.info(resourceBundleMessageSource.getMessage("retrieving.transactions", new Object[] { (new Long(book)).toString() }, Locale.US));
        }
        if (!bookExists(book)) {
            String errString = resourceBundleMessageSource.getMessage("error.nonexistent.book", new Object[] { new Long(book) }, Locale.US);
            logger.error(errString);
            throw new BookNotFoundException(errString);
        }
        Book bk = bookDAO.retrieve(book);
        LibraryTransaction trans = new LibraryTransaction();
        trans.setBook(book);
        trans.setBookTitle(bk.getTitle());
        long reader = bk.getOwner();
        trans.setReader(reader);
        Reader rd = new Reader();
        rd.setId(reader);
        rd = readerDAO.retrieve(rd);
        if (rd != null) {
            trans.setReaderName(rd.getFirstName() + " " + rd.getLastName());
            trans.setReaderPhone(rd.getDeskPhone());
            trans.setTimeString(bk.getCreated());
            trans.setAction(Constants.ADD_ACTION);
            String action = resourceBundleMessageSource.getMessage("trans.add.label", null, Locale.US);
            trans.setActionString(action);
        }
        ArrayList result = new ArrayList();
        result.add(trans);
        result.addAll(libraryTransactionDAO.getTransactions(book));
        if (logger.isDebugEnabled()) {
            logger.info(resourceBundleMessageSource.getMessage("transactions.retrieved", new Object[] { (new Long(book)).toString() }, Locale.US));
        }
        return result;
    }

    public void resetPassword(String user, String newPwd) throws LibraryException {
        if (logger.isDebugEnabled()) logger.debug("user=" + user + ", newPwd=" + newPwd);
        readerDAO.resetPassword(user, newPwd);
    }

    public void forgotPassword(String uid, String email) throws LibraryException {
        if (logger.isDebugEnabled()) {
            logger.info(resourceBundleMessageSource.getMessage("forgotPassword.processing", new Object[] { uid, email }, Locale.US));
        }
        Reader rd = retrieveByUid(uid);
        if (rd == null || !rd.getEmail().equalsIgnoreCase(email)) {
            String errString = resourceBundleMessageSource.getMessage("error.nonexistent.reader", new Object[] { uid + ", " + email }, Locale.US);
            logger.error(errString);
            throw new LibraryException(errString);
        }
        byte[] bytes = new byte[8];
        new java.util.Random().nextBytes(bytes);
        String newPassword = crypto.convert(bytes);
        try {
            resetPassword(uid, newPassword);
            mailWorker.sendNewPasswordConfirmation(rd, newPassword);
        } catch (Exception ex) {
            String errString = resourceBundleMessageSource.getMessage("error.trans.request.badrequest", new Object[] { uid, email }, Locale.US);
            logger.error(errString);
            throw new LibraryException(errString);
        }
        if (logger.isDebugEnabled()) {
            logger.info(resourceBundleMessageSource.getMessage("forgotPassword.processed.successfully", new Object[] { uid, email }, Locale.US));
        }
    }

    public Map lookupBook(String isbn) throws LibraryException {
        if (logger.isDebugEnabled()) logger.debug("isbn=" + isbn);
        try {
            return sruClient.callSruSrwProvider(isbn);
        } catch (Throwable ex) {
            if (logger.isDebugEnabled()) logger.debug(ex.getCause());
            throw new LibraryException(ex.getMessage(), ex);
        }
    }
}
