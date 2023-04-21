package open.facade.ipdots;

import java.util.List;
import javax.ejb.Remote;
import open.ipdots.Transaction;

/**
 *
 * @author pfares
 */
@Remote
public interface TransactionFacadeRemote {

    void create(Transaction transaction);

    void edit(Transaction transaction);

    void remove(Transaction transaction);

    Transaction find(Object id);

    List<Transaction> findAll();
}
