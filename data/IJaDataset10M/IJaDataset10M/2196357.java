package com.mgiandia.library.service;

import java.util.List;
import org.junit.*;
import com.mgiandia.library.LibraryException;
import com.mgiandia.library.dao.*;
import com.mgiandia.library.domain.core.ItemState;
import com.mgiandia.library.domain.core.Loan;
import com.mgiandia.library.jpadao.JpaDAOFactory;
import com.mgiandia.library.jpadao.JpaInitializer;

public class LoanServiceTest {

    Initializer dataHelper;

    public void setUp() {
        dataHelper.prepareData();
    }

    public void setUpMemory() {
        DAOFactoryExposer.setDaoFactoryStub(new MemoryDAOFactory());
        dataHelper = new MemoryInitializer();
        setUp();
    }

    public void setUpJpa() {
        DAOFactoryExposer.setDaoFactoryStub(new JpaDAOFactory());
        dataHelper = new JpaInitializer();
        setUp();
    }

    @Test(expected = LibraryException.class)
    public void noBorrowerMemory() {
        setUpMemory();
        noBorrower();
    }

    @Test(expected = LibraryException.class)
    public void noBorrowerJpa() {
        setUpJpa();
        noBorrower();
    }

    public void noBorrower() {
        LoanService loanService = new LoanService();
        loanService.findBorrower(99999);
        loanService.borrow(Initializer.UML_DISTILLED_ID1);
    }

    @Test
    public void testBorrowMemory() {
        setUpMemory();
        testBorrow();
    }

    @Test
    public void testBorrowJpa() {
        setUpJpa();
        testBorrow();
    }

    public void testBorrow() {
        LoanService loanService = new LoanService();
        loanService.findBorrower(Initializer.DIAMANTIDIS_ID);
        Assert.assertNotNull(loanService.borrow(Initializer.UML_DISTILLED_ID1));
        List<Loan> loanList = DAOFactory.getFactory().getLoanDAO().findAll();
        Loan loan = loanList.get(0);
        Assert.assertTrue(loan.isPending());
        Assert.assertEquals(Initializer.UML_DISTILLED_ID1, loan.getItem().getItemNumber());
        Assert.assertEquals(ItemState.LOANED, loan.getItem().getState());
    }

    @Test
    public void borrowDataBaseMemory() {
        setUpMemory();
        borrowDataBase();
    }

    @Test
    public void borrowDataBaseJpa() {
        setUpJpa();
        borrowDataBase();
    }

    public void borrowDataBase() {
        LoanService loanService = new LoanService();
        loanService.findBorrower(Initializer.DIAMANTIDIS_ID);
        Assert.assertNotNull(loanService.borrow(Initializer.UML_USER_GUIDE_ID1));
        Assert.assertNotNull(loanService.borrow(Initializer.UML_DISTILLED_ID1));
        Assert.assertNotNull(loanService.borrow(Initializer.UML_REFACTORING_ID));
        Assert.assertNotNull(loanService.borrow(Initializer.UML_USER_GUIDE_ID2));
        Assert.assertNull(loanService.borrow(Initializer.UML_DISTILLED_ID2));
    }
}
