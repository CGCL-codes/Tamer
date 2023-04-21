package net.sourceforge.jdbconverter;

/**
 * @author Volker Berlin
 */
class ScanIndexHandler implements IndexHandler {

    private StringBuilder buffer;

    public ScanIndexHandler(StringBuilder buffer) {
        this.buffer = buffer;
    }

    /**
     * {@inheritDoc}
     */
    public void handleConstraint(String indexName, StringBuilder indexDef) {
        buffer.append("\t\t<Constraint name=\"").append(indexName);
        buffer.append("\" def=\"").append(indexName).append("\"/>\n");
    }

    /**
     * {@inheritDoc}
     */
    public void handleIndex(String indexName, StringBuilder indexDef) {
        buffer.append("\t\t<Index name=\"").append(indexName);
        buffer.append("\" def=\"").append(indexName).append("\"/>\n");
    }
}
