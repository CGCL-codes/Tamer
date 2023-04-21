package org.eclipsetrader.repository.local.internal.types;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.eclipsetrader.core.feed.FeedIdentifier;
import org.eclipsetrader.core.feed.FeedProperties;
import org.eclipsetrader.core.feed.IFeedIdentifier;
import org.eclipsetrader.core.feed.IFeedProperties;

@XmlRootElement(name = "identifier")
public class IdentifierType {

    @XmlAttribute(name = "symbol")
    private String symbol;

    @XmlElementWrapper(name = "properties")
    @XmlElementRef
    private List<PropertyType> properties;

    private IFeedIdentifier identifier;

    public IdentifierType() {
    }

    public IdentifierType(String symbol) {
        this.symbol = symbol;
    }

    public IdentifierType(IFeedIdentifier identifier) {
        this.identifier = identifier;
        this.symbol = identifier.getSymbol();
        IFeedProperties feedProperties = (IFeedProperties) identifier.getAdapter(IFeedProperties.class);
        if (feedProperties != null) {
            properties = new ArrayList<PropertyType>();
            for (String name : feedProperties.getPropertyIDs()) properties.add(new PropertyType(name, feedProperties.getProperty(name)));
        }
    }

    @XmlTransient
    public IFeedIdentifier getIdentifier() {
        if (identifier == null) {
            FeedProperties feedProperties = null;
            if (properties != null) {
                feedProperties = new FeedProperties();
                for (PropertyType type : properties) feedProperties.setProperty(type.getName(), type.getValue());
            }
            identifier = new FeedIdentifier(symbol, feedProperties);
        }
        return identifier;
    }

    @XmlTransient
    public String getSymbol() {
        return symbol;
    }
}
