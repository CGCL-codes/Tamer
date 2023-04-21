package org.nakedobjects.example.library;

import org.nakedobjects.applib.AbstractFactoryAndRepository;

public class LoanFactory extends AbstractFactoryAndRepository {

    public Loan createLoan(final Book book, final Member member) {
        Loan loan = (Loan) newPersistentInstance(Loan.class);
        loan.modifyBook(book);
        loan.modifyLentTo(member);
        return loan;
    }
}
