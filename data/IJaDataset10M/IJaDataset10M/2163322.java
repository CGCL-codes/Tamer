package com.ixora.rms.dataengine;

import org.w3c.dom.Node;
import com.ixora.common.xml.XMLTag;
import com.ixora.common.xml.XMLText;
import com.ixora.common.xml.exception.XMLException;
import com.ixora.rms.dataengine.definitions.QueryDef;

/**
 * @author Daniel Moraru
 */
public abstract class QueryClientAbstract extends XMLTag implements QueryClient {

    private static final long serialVersionUID = -6018052555779511573L;

    protected XMLText name = new XMLText("name", true);

    protected XMLText description = new XMLText("description", true);

    /** The (unrealized) definition of the query for this client */
    protected QueryDef query = new QueryDef();

    /**
     * Constructor.
     */
    protected QueryClientAbstract() {
        super();
        name.setValue("");
        description.setValue("");
    }

    /**
     * @return definition of the query for this client
     */
    public QueryDef getQueryDef() {
        return query;
    }

    /**
     * @see com.ixora.rms.dataengine.QueryClient#getDescription()
     */
    public String getDescription() {
        return description.getValue();
    }

    /**
     * @see com.ixora.rms.dataengine.QueryClient#getName()
     */
    public String getName() {
        return name.getValue();
    }

    /**
     * @return the hardcoded name of this tag
     */
    public String getTagName() {
        return "view";
    }

    /**
     * @see com.ixora.common.xml.XMLExternalizable#fromXML(org.w3c.dom.Node)
     */
    public void fromXML(Node node) throws XMLException {
        super.fromXML(node);
        this.query.setIdentifier(this.name.getValue());
    }
}
