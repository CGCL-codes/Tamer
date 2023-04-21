package com.thyante.thelibrarian.info.compound;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import com.thyante.thelibrarian.info.IFunction;
import com.thyante.thelibrarian.info.ISearchIndex;
import com.thyante.thelibrarian.model.Item;
import com.thyante.thelibrarian.model.Template;
import com.thyante.thelibrarian.model.specification.IItem;
import com.thyante.thelibrarian.model.specification.IMediaFactory;
import com.thyante.thelibrarian.util.XMLUtil;

/**
 * The final node in the proxy graph
 * @author Matthias-M. Christen
 */
public class OutputNode extends BasicNode {

    /**
	 * The item receiving the search result
	 */
    protected IItem m_itemResult;

    /**
	 * Constructs a new output node.
	 */
    public OutputNode(Template template) {
        super();
        m_template = template;
        m_itemResult = new Item(getTemplate().getFields(), null, null);
    }

    /**
	 * Constructs a new output node from the XML node <code>nodeXml</code>.
	 * @param nodeXml The XML node from which to construct the output node
	 */
    protected OutputNode(Node nodeXml) {
        super();
        Node nodeInputs = XMLUtil.getFirstChildByName(nodeXml, "Inputs");
        m_template = Template.fromNode(nodeInputs, false);
        m_itemResult = new Item(getTemplate().getFields(), null, null);
    }

    @Override
    protected void toNodeInternal(Document document, Node nodeXml) {
        Node nodeInputs = document.createElement("Inputs");
        nodeXml.appendChild(nodeInputs);
        getTemplate().toNode(document, nodeInputs, false);
    }

    @Override
    public void setInput(ISearchIndex index, String strValue) {
        m_itemResult.setValue(getTemplate().getFieldByName(index.getIdentifier()), strValue);
    }

    @Override
    public void findItemDetails(IMediaFactory mediaFactory, IFindItemDisplayable displayable) {
    }

    /**
	 * Returns the search result in an {@link IItem} object.
	 * @return The search result
	 */
    public IItem getResult() {
        return m_itemResult;
    }

    @Override
    public ISearchIndex[][] getSearchIndices() {
        ISearchIndex[][] rgrgIndices = new ISearchIndex[getTemplate().getFieldsCount()][];
        for (int i = 0; i < getTemplate().getFieldsCount(); i++) rgrgIndices[i] = new ISearchIndex[] { new FieldItemSearchFieldSpecification(getTemplate().getField(i), getTemplate()) };
        return null;
    }

    private static final IFunction[] FUNCTIONS = new IFunction[] {};

    @Override
    public IFunction[] getFunctions() {
        return FUNCTIONS;
    }
}
