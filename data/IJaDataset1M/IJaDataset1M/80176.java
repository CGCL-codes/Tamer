package com.ek.mitapp.repository;

import com.ek.mitapp.data.BidItem;
import com.ek.mitapp.data.BidItemRegistry;

/**
 * This interface provides access to the underlying bid item repository. The bid item repository
 * is used to store the bid items and their associated information.
 * <br>
 * Id: $Id: $
 *
 * @author Dave Irwin (dirwin@ekmail.com)
 */
public interface IBidItemRepository {

    /**
     * Load the bid items into the <code>BidItemRegistry</code> object.
     * 
     * @return bidItemRegistry
     * @throws ParsingException
     */
    public BidItemRegistry load() throws ParsingException;

    /**
     * 
     * @param bidItem
     * @throws ParsingException
     */
    public void addBidItem(BidItem bidItem) throws ParsingException;

    /**
     * 
     * @param bidItem
     * @throws ParsingException
     */
    public void removeBidItem(BidItem bidItem) throws ParsingException;

    /**
     * @param bidItem
     * @throws ParsingException
     */
    public void updateBidItem(BidItem bidItem) throws ParsingException;
}
