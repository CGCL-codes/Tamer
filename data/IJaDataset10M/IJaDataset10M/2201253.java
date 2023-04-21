package se.su.it.smack.pubsub.elements;

public class ItemsElement extends PubSubElement {

    public ItemsElement() {
        super();
    }

    public ItemsElement(String node) {
        super(node);
    }

    @Override
    public String getName() {
        return "items";
    }

    @Override
    public String toXML() {
        return "  <items node='" + this.getNode() + "'/>\n";
    }
}
