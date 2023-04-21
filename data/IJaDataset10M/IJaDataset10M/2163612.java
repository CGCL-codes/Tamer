package org.sourceforge.library.user.workers;

import org.sourceforge.library.user.domain.Book;
import org.sourceforge.library.user.domain.Reader;

/**
 * @author Tony Ambrozie 2006
 *
 */
public interface MailWorker {

    public void sendRequestConfirmation(Reader possessor, Reader requestor, Book bk) throws Exception;

    public void sendRequestNotification(Reader possessor, Reader requestor, Book bk) throws Exception;

    public void sendReturnNotification(Reader rd, Book bk) throws Exception;

    public void sendDeleteConfirmation(Reader rd, Book bk) throws Exception;

    public void sendCheckoutNotification(Reader requestor, Reader newPossessor, Book bk) throws Exception;

    public void sendNewPasswordConfirmation(Reader requestor, String newPassword) throws Exception;
}
