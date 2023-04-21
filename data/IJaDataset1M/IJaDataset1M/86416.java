package net.sf.brightside.xlibrary.tapestry.pages;

import java.util.List;
import org.apache.tapestry.ComponentResources;
import org.apache.tapestry.annotations.ApplicationState;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.OnEvent;
import org.apache.tapestry.beaneditor.BeanModel;
import org.apache.tapestry.ioc.annotations.Inject;
import org.apache.tapestry.services.BeanModelSource;
import net.sf.brightside.xlibrary.core.tapestry.SpringBean;
import net.sf.brightside.xlibrary.metamodel.BorrowBook;
import net.sf.brightside.xlibrary.metamodel.Student;
import net.sf.brightside.xlibrary.service.GetById;
import net.sf.brightside.xlibrary.service.notReturned.GetReturnsByStudent;

public class AllNotReturnedBooks {

    @InjectPage
    private ReturnBook returnBook;

    @ApplicationState
    private Student student;

    @Inject
    @SpringBean("net.sf.brightside.xlibrary.service.GetById")
    private GetById<BorrowBook> getByIdCommand;

    @ApplicationState
    private BorrowBook borrowBook;

    @Inject
    @SpringBean("net.sf.brightside.xlibrary.service.notReturned.GetReturnsByStudent")
    private GetReturnsByStudent getReturnsByStudent;

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @SuppressWarnings("unchecked")
    public List<BorrowBook> getAllBorrows() {
        getReturnsByStudent.setStudent(student);
        return (List<BorrowBook>) getReturnsByStudent.execute();
    }

    public BorrowBook getBorrowBook() {
        return borrowBook;
    }

    public void setBorrowBook(BorrowBook borrowBook) {
        this.borrowBook = borrowBook;
    }

    @Inject
    private BeanModelSource beanModelSource;

    @Inject
    private ComponentResources componentResources;

    private BeanModel<BorrowBook> beanModel;

    {
        beanModel = beanModelSource.create(BorrowBook.class, true, componentResources);
        beanModel.add("register", null);
    }

    public BeanModel<BorrowBook> getModel() {
        return beanModel;
    }

    @OnEvent(component = "register")
    Object onFormRegister(long id) {
        getByIdCommand.setType(BorrowBook.class);
        getByIdCommand.setId(id);
        borrowBook = getByIdCommand.execute();
        returnBook.setBorrow(borrowBook);
        return returnBook;
    }
}
