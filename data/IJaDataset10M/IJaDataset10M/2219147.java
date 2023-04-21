package pl.pyrkon.cm.server.cashier.dao;

import java.util.List;
import org.hibernate.LockMode;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.pyrkon.cm.client.cashier.data.Cashier;
import pl.pyrkon.cm.client.cashier.data.CashierRole;
import pl.pyrkon.cm.client.convention.data.Convention;

public interface CashierDao {

    @Transactional(propagation = Propagation.REQUIRED)
    public abstract Cashier getCashier(String login);

    @Transactional(propagation = Propagation.REQUIRED)
    public abstract Cashier getCashierWithLock(String login, LockMode mode);

    public abstract List<Cashier> listCashiers(Convention convention);

    public abstract List<CashierRole> listRoles();

    public abstract Cashier merge(Cashier cashier);

    public abstract void save(Cashier cashier);
}
