package hi.server.dao;

import hi.server.model.Stock;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Retrieves data from SQL database and returns them as JPA objects.
 * 
 * @author Marcin Wiankowski
 * 
 */
@Stateless
public class StockDao implements StockDaoLocal {

    @PersistenceContext(unitName = "stock_hypersonic")
    private EntityManager em;

    /**
	 * {@inheritDoc}
	 */
    @Override
    public List<Stock> getAllCurrentStockQuotes() {
        return null;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public List<Stock> getStock() {
        Query query = em.createQuery("SELECT s FROM Stock s LEFT JOIN s.quote q");
        @SuppressWarnings("unchecked") List<Stock> stockList = query.getResultList();
        return stockList;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public List<Stock> getStock(Integer id) {
        return null;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public List<Stock> getStock(Date lastUpdated) {
        return null;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public List<Stock> getStock(Integer id, Date lastUpdated) {
        return null;
    }
}
