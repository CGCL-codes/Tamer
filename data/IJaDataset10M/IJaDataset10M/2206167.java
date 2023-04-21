package net.sf.brightside.stockswatcher.server.service.hibernate;

import net.sf.brightside.stockswatcher.server.core.spring.IBeansProvider;
import net.sf.brightside.stockswatcher.server.metamodel.Quote;
import net.sf.brightside.stockswatcher.server.metamodel.Stock;
import net.sf.brightside.stockswatcher.server.service.api.hibernate.IAddQuote;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

public class AddQuoteImpl extends HibernateDaoSupport implements IAddQuote {

    private IBeansProvider beansProvider;

    public IBeansProvider getBeansProvider() {
        return beansProvider;
    }

    public void setBeansProvider(IBeansProvider beansProvider) {
        this.beansProvider = beansProvider;
    }

    public Session getManager() {
        SessionFactory sf = getSessionFactory();
        return sf.getCurrentSession();
    }

    @Transactional
    public void addQuote(Stock stock, Quote quote) {
        stock.getQuotes().add(quote);
        quote.setStock(stock);
        Session session = getManager();
        session.saveOrUpdate(quote);
        session.saveOrUpdate(stock);
    }
}
