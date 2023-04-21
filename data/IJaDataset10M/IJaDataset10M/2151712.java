package net.sf.brightside.xlibrary.service.returnBook;

import org.hibernate.classic.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import net.sf.brightside.xlibrary.metamodel.BorrowBook;
import net.sf.brightside.xlibrary.service.Save;

public class UpdateBorrowCommandImpl extends HibernateDaoSupport implements UpdateBorrowCommand {

    private BorrowBook borrow;

    @Override
    public BorrowBook getBorrowBook() {
        return borrow;
    }

    public Session provideManger() {
        return getSessionFactory().getCurrentSession();
    }

    @Override
    public BorrowBook execute() {
        provideManger().update(borrow);
        return borrow;
    }

    @Override
    public void setBorrowBook(Object borrow) {
        this.borrow = (BorrowBook) borrow;
    }
}
