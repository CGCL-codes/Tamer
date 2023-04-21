package pl.edu.agh.iosr.gamblingzone.dao;

import pl.edu.agh.iosr.gamblingzone.model.Quote;

/**
 * The Interface QuoteDAO.
 */
public interface QuoteDAO extends GenericDAO<Quote, Long> {

    /**
	 * Gets the random quote.
	 * 
	 * @return the random quote
	 */
    public Quote getRandomQuote();
}
