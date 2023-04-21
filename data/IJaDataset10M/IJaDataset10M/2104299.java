package com.mgiandia.library.dao;

import com.mgiandia.library.domain.core.Borrower;

public class BorrowerDAOMemory extends GenericDAOMemory<Borrower> implements BorrowerDAO {

    public Borrower find(int borrowerNo) {
        for (Borrower borrower : entities) {
            if (borrower.getBorrowerNo() == borrowerNo) {
                return borrower;
            }
        }
        return null;
    }
}
