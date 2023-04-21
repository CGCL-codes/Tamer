package com.ek.mitapp.repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;
import org.jscience.economics.money.Money;
import org.jscience.economics.money.Currency;
import org.jscience.physics.measures.Measure;
import com.ek.mitapp.data.BidItem;
import com.ek.mitapp.data.BidItemRegistry;
import com.ek.mitapp.data.BidItemType;
import com.ek.mitapp.data.BidItem.Unit;
import com.ek.mitapp.event.BidItemRegistryEvent;
import com.ek.mitapp.system.Configuration;

/**
 * This class is responsible for reading in the bid items from a text file.
 * <br>
 * Id: $Id: $
 *
 * @author Dave Irwin (dirwin@ekmail.com)
 */
public final class BidItemTxtRepository extends AbstractBidItemRepository {

    /**
     * Define the root logger.
     */
    private static Logger logger = Logger.getLogger(BidItemTxtRepository.class.getName());

    /**
     * Comment.
     */
    private static final String COMMENT = "#";

    private static final String defaultFilename = "pay_item_list.txt";

    /**
     * The default file path.
     */
    private String filename;

    /**
     * Default constructor.
     * 
     * @param configuration
     */
    public BidItemTxtRepository(Configuration configuration) {
        this(defaultFilename, configuration);
    }

    /**
     * 
     * @param filename
     * @param configuration
     */
    public BidItemTxtRepository(String filename, Configuration configuration) {
        super(configuration);
        this.filename = filename;
    }

    /**
     * @see com.ek.mitapp.repository.IBidItemRepository#load(com.ek.mitapp.data.BidItemRegistry)
     */
    public BidItemRegistry load() {
        BidItemRegistry bidItemRegistry = new BidItemRegistry();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(configuration.getConfigFolderRelative() + Configuration.SEPARATOR + filename));
            String line;
            while ((line = in.readLine()) != null) {
                processLine(bidItemRegistry, line);
            }
        } catch (IOException ioe) {
            logger.error("Error parsing construction cost pay item file: " + ioe.getMessage(), ioe);
        } finally {
            try {
                if (in != null) in.close();
            } catch (IOException ioe) {
            }
        }
        return bidItemRegistry;
    }

    /**
     * @see com.ek.mitapp.repository.IBidItemRepository#addBidItem(com.ek.mitapp.data.BidItem)
     */
    public void addBidItem(BidItem bidItem) {
    }

    /**
     * @see com.ek.mitapp.repository.IBidItemRepository#removeBidItem(com.ek.mitapp.data.BidItem)
     */
    public void removeBidItem(BidItem bidItem) {
    }

    /** 
     * @see com.ek.mitapp.repository.IBidItemRepository#updateBidItem(com.ek.mitapp.data.BidItem)
     */
    public void updateBidItem(BidItem bidItem) {
    }

    /**
     * Process a line.
     * 
     * @param line
     */
    private void processLine(BidItemRegistry bidItemRegistry, String line) {
        logger.debug("Processing line: " + line);
        if (!line.startsWith(COMMENT) && line.length() != 0) {
            StringTokenizer st = new StringTokenizer(line, ":");
            String name = st.nextToken().trim();
            BidItemType type = new BidItemType(st.nextToken().trim());
            Unit unit = BidItem.Unit.valueOf(st.nextToken().trim());
            Measure<Money> unitCost = BidItem.getMoneyObject(st.nextToken().trim(), Currency.USD);
            bidItemRegistry.addBidItem(new BidItem(name, type, unit, unitCost));
        }
    }

    /**
     * @see com.ek.mitapp.event.BidItemRegistryListener#bidItemAdded(com.ek.mitapp.event.BidItemRegistryEvent)
     */
    public void bidItemAdded(BidItemRegistryEvent eventObj) {
    }

    /**
     * @see com.ek.mitapp.event.BidItemRegistryListener#bidItemRemoved(com.ek.mitapp.event.BidItemRegistryEvent)
     */
    public void bidItemRemoved(BidItemRegistryEvent eventObj) {
    }

    /**
     * @see com.ek.mitapp.event.BidItemRegistryListener#bidItemUpdated(com.ek.mitapp.event.BidItemRegistryEvent)
     */
    public void bidItemUpdated(BidItemRegistryEvent eventObj) {
    }
}
