package net.da.core.dao.spi.local;

import javax.persistence.EntityManagerFactory;
import com.google.inject.Inject;
import net.da.core.dao.Dao;

public class AbstractDao implements Dao {

    protected EntityManagerFactory emf;

    public EntityManagerFactory getEmf() {
        return emf;
    }

    @Inject
    public void setEmf(EntityManagerFactory emf) {
        this.emf = emf;
    }
}
