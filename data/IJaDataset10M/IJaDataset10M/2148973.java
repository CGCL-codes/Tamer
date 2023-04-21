package com.ek.mitapp.repository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.io.XMLWriter;
import org.dom4j.io.OutputFormat;
import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.dom4j.DocumentHelper;
import org.dom4j.XPath;
import org.jscience.economics.money.Currency;
import org.jscience.economics.money.Money;
import org.jscience.physics.measures.Measure;
import com.ek.mitapp.data.BidItem;
import com.ek.mitapp.data.BidItemRegistry;
import com.ek.mitapp.data.BidItemType;
import com.ek.mitapp.data.BidItem.Unit;
import com.ek.mitapp.system.Configuration;
import com.ek.mitapp.util.FileUtils;

/**
 * TODO: Class description.
 * <p>
 * Id: $Id: /cvsroot/jhomenet/files/src/jhomenet/ $
 * 
 * @author Dave Irwin (dirwin@ekmail.com)
 */
public class BidItemXmlDom4jRepository extends AbstractBidItemXmlRepository {

    /**
     * Define the root logger.
     */
    private static Logger logger = Logger.getLogger(BidItemXmlDom4jRepository.class.getName());

    /**
     * @param filename
     * @param configuration
     */
    public BidItemXmlDom4jRepository(String filename, Configuration configuration) {
        super(filename, configuration);
    }

    /**
     * @param configuration
     */
    public BidItemXmlDom4jRepository(Configuration configuration) {
        super(configuration);
    }

    /** 
     * @see com.ek.mitapp.repository.IBidItemRepository#load()
     */
    public BidItemRegistry load() throws ParsingException {
        BidItemRegistry bidItemRegistry = new BidItemRegistry();
        Document document = parseFile();
        buildBidItemRegistry(document.getRootElement(), bidItemRegistry);
        bidItemRegistry.addListener(this);
        return bidItemRegistry;
    }

    /** 
     * @see com.ek.mitapp.repository.IBidItemRepository#addBidItem(com.ek.mitapp.data.BidItem)
     */
    public void addBidItem(BidItem bidItem) throws ParsingException {
        Document document = parseFile();
        Element bidItemsElement = getBidItemsElement(document.getRootElement());
        Element bidItemElement = buildBidItemElement(bidItem);
        bidItemsElement.add(bidItemElement);
        write(document);
    }

    /** 
     * @see com.ek.mitapp.repository.IBidItemRepository#removeBidItem(com.ek.mitapp.data.BidItem)
     */
    public void removeBidItem(BidItem bidItem) throws ParsingException {
        Document document = parseFile();
        Element bidItemsElement = getBidItemsElement(document.getRootElement());
        Element foundBidItemElement = searchForBidItemElement(bidItemsElement, bidItem);
        if (foundBidItemElement != null) {
            bidItemsElement.remove(foundBidItemElement);
            write(document);
        } else {
        }
    }

    /** 
     * @see com.ek.mitapp.repository.IBidItemRepository#updateBidItem(com.ek.mitapp.data.BidItem)
     */
    public void updateBidItem(BidItem bidItem) throws ParsingException {
        Document document = parseFile();
        Element bidItemsElement = getBidItemsElement(document.getRootElement());
        Element foundBidItemElement = searchForBidItemElement(bidItemsElement, bidItem);
        if (foundBidItemElement != null) {
            bidItemsElement.remove(foundBidItemElement);
            Element bidItemElement = buildBidItemElement(bidItem);
            bidItemsElement.add(bidItemElement);
            write(document);
        } else {
        }
    }

    /**
     * 
     * @param doc
     */
    private void write(Document doc) {
        try {
            FileUtils.backupFile(new File(getFilename()), FileUtils.FileExtensions.Xml);
            OutputFormat format = OutputFormat.createPrettyPrint();
            XMLWriter writer = new XMLWriter(new FileWriter(getFilename()), format);
            writer.write(doc);
            writer.close();
        } catch (IOException ioe) {
            logger.error("Error while writing to repository: " + ioe.getMessage());
        }
    }

    /**
     *
     * @return
     * @throws ParsingException
     */
    private Document parseFile() throws ParsingException {
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(new File(getFilename()));
            return document;
        } catch (DocumentException de) {
            throw new ParsingException(de);
        }
    }

    /**
     * 
     * @param document
     * @param bidItemRegistry
     */
    private void buildBidItemRegistry(Element rootElement, BidItemRegistry bidItemRegistry) {
        if (rootElement.getName().equalsIgnoreCase(XMLNodes.BidItemConfiguration.getNodeName())) {
            for (Iterator i = rootElement.elementIterator(XMLNodes.BidItems.getNodeName()); i.hasNext(); ) {
                Element element = (Element) i.next();
                buildBidItemRegistry(element, bidItemRegistry);
            }
        } else if (rootElement.getName().equalsIgnoreCase(XMLNodes.BidItems.getNodeName())) {
            for (Iterator i = rootElement.elementIterator(XMLNodes.BidItem.getNodeName()); i.hasNext(); ) {
                Element element = (Element) i.next();
                buildBidItemRegistry(element, bidItemRegistry);
            }
        } else if (rootElement.getName().equalsIgnoreCase(XMLNodes.BidItem.getNodeName())) {
            bidItemRegistry.addBidItem(buildBidItem(rootElement));
        }
    }

    /**
     * 
     *
     * @param rootElement
     * @return
     */
    private Element getBidItemsElement(Element rootElement) {
        logger.debug("Retrieving the \"bidItems\" element...");
        XPath xpathSelector = DocumentHelper.createXPath("//biditem-configuration/bidItems");
        List results = xpathSelector.selectNodes(rootElement.getDocument());
        if (results != null) return (Element) results.get(0); else return null;
    }

    /**
     * 
     *
     * @param rootElement
     * @param bidItem
     * @return
     */
    private Element searchForBidItemElement(Element rootElement, BidItem bidItem) {
        logger.debug("Searching for bid item element...");
        XPath xpathSelector = DocumentHelper.createXPath("//name[text()='" + bidItem.getName() + "']");
        List results = xpathSelector.selectNodes(rootElement.getDocument());
        if (results != null) {
            logger.debug("Matching bid item element found!");
            Element e = (Element) results.get(0);
            return e.getParent();
        } else return null;
    }

    /**
     * 
     *
     * @param bidItem
     * @return
     */
    private Element buildBidItemElement(BidItem bidItem) {
        Element bidItemElement = new DefaultElement(XMLNodes.BidItem.getNodeName());
        Element nameElement = new DefaultElement(XMLNodes.BidItemName.getNodeName());
        nameElement.setText(bidItem.getName());
        Element typeElement = new DefaultElement(XMLNodes.BidItemType.getNodeName());
        typeElement.setText(bidItem.getType().toString());
        Element unitElement = new DefaultElement(XMLNodes.BidItemUnit.getNodeName());
        unitElement.setText(bidItem.getUnit().toString());
        Element unitCostElement = new DefaultElement(XMLNodes.BidItemCost.getNodeName());
        unitCostElement.setText(bidItem.getUnitCostAsString());
        bidItemElement.add(nameElement);
        bidItemElement.add(typeElement);
        bidItemElement.add(unitElement);
        bidItemElement.add(unitCostElement);
        return bidItemElement;
    }

    /**
     * Build a bid item.
     *
     * @param bidItemNode
     * @return
     */
    private BidItem buildBidItem(Element bidItemNode) {
        String name = "";
        BidItemType type = null;
        Unit unit = null;
        Measure<Money> unitCost = null;
        for (Iterator i = bidItemNode.elementIterator(); i.hasNext(); ) {
            Element element = (Element) i.next();
            String nodeName = element.getName();
            if (nodeName.equalsIgnoreCase(XMLNodes.BidItemType.getNodeName())) {
                type = new BidItemType(element.getTextTrim());
            } else if (nodeName.equalsIgnoreCase(XMLNodes.BidItemName.getNodeName())) {
                name = element.getTextTrim();
            } else if (nodeName.equalsIgnoreCase(XMLNodes.BidItemUnit.getNodeName())) {
                unit = BidItem.Unit.valueOf(element.getTextTrim());
            } else if (nodeName.equalsIgnoreCase(XMLNodes.BidItemCost.getNodeName())) {
                String[] t = element.getTextTrim().split(" ");
                if (t.length == 1) unitCost = BidItem.getMoneyObject(t[0], Currency.USD); else if (t.length == 2) unitCost = BidItem.getMoneyObject(t[0], new Currency(t[1]));
            }
        }
        return new BidItem(name, type, unit, unitCost);
    }
}
