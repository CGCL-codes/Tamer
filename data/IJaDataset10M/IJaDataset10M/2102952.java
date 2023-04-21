package document;

import document.softwareDocument.structure.AttributDocument;
import document.softwareDocument.structure.DocumentRepresentation;

public abstract class AbstractDocument {

    private String name;

    public AbstractDocument(String name) {
        this.name = name;
    }

    public abstract boolean addDocument(AbstractDocument doc, String startNode, AttributDocument startAttr);

    public abstract boolean write();

    public abstract boolean read();

    /**
      * Method description
      *
      *
      * @return
      */
    @Override
    public abstract String toString();

    public abstract boolean addData(DocumentRepresentation data, String startNode, AttributDocument startAttr);

    public abstract DocumentType getType();

    public String getName() {
        return this.name;
    }

    public void setName(String val) {
        this.name = val;
    }

    public abstract int size();

    public abstract boolean equalsTo(AbstractDocument doc);
}
