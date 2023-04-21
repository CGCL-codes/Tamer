package org.hip.vif.markup.serializer;

import java.io.Writer;
import java.util.Collection;
import java.util.Vector;
import org.eclipse.mylyn.wikitext.core.parser.Attributes;
import org.eclipse.mylyn.wikitext.core.parser.LinkAttributes;
import org.eclipse.mylyn.wikitext.core.parser.builder.AbstractXmlDocumentBuilder;
import org.eclipse.mylyn.wikitext.core.util.XmlStreamWriter;

/**
 * A <code>DocumentBuilder</code> that creates plain text from text containing markup.
 * 
 * @author Luthiger
 * Created: 23.05.2010
 */
public class PlainTextDocumentBuilder extends AbstractXmlDocumentBuilder {

    private static final String NL = System.getProperty("line.separator");

    private static final String LIST_ITEM = "%s%s ";

    private static final String BULLET = "*";

    private static final String URL_FORMAT = " [%s]";

    private enum ListType {

        NONE, BULLET_LIST, NUMERIC_LIST, LIST_ITEM;

        private ListType parent;

        ListType getParent() {
            return parent == null ? ListType.NONE : parent;
        }

        void setParent(ListType inParent) {
            parent = inParent;
        }
    }

    private String htmlNsUri = "";

    private ListType listType = ListType.NONE;

    private int counter = 0;

    private String href = null;

    private Collection<String> entities = new Vector<String>();

    /**
	 * @param inWriter {@link Writer}
	 */
    public PlainTextDocumentBuilder(Writer inWriter) {
        super(inWriter);
    }

    /**
	 * @param inWriter {@link XmlStreamWriter}
	 */
    public PlainTextDocumentBuilder(XmlStreamWriter inWriter) {
        super(inWriter);
    }

    @Override
    public void acronym(String inText, String inDefinition) {
        writer.writeStartElement(htmlNsUri, "acronym");
        writer.writeAttribute("title", inDefinition);
        writer.writeCharacters(inText);
        writer.writeEndElement();
    }

    @Override
    public void charactersUnescaped(String inLiteral) {
        writer.writeLiteral(inLiteral);
    }

    @Override
    public void beginBlock(BlockType inType, Attributes inAttributes) {
        switch(inType) {
            case BULLETED_LIST:
                listType = ListType.BULLET_LIST;
                break;
            case NUMERIC_LIST:
                listType = ListType.NUMERIC_LIST;
                break;
            case LIST_ITEM:
                ListType lItem = ListType.LIST_ITEM;
                lItem.setParent(listType);
                switch(listType) {
                    case NUMERIC_LIST:
                        writer.writeLiteral(String.format(LIST_ITEM, NL, ++counter));
                        listType = lItem;
                        break;
                    default:
                        writer.writeLiteral(String.format(LIST_ITEM, NL, BULLET));
                        listType = lItem;
                        break;
                }
                break;
        }
    }

    @Override
    public void endBlock() {
        listType = listType.getParent();
        if (listType == ListType.NONE) {
            counter = 0;
        }
    }

    @Override
    public void beginDocument() {
    }

    @Override
    public void endDocument() {
    }

    @Override
    public void beginHeading(int inLevel, Attributes inAttributes) {
    }

    @Override
    public void endHeading() {
    }

    @Override
    public void beginSpan(SpanType inType, Attributes inAttributes) {
        if (inType == SpanType.LINK) {
            href = ((LinkAttributes) inAttributes).getHref();
        }
    }

    @Override
    public void endSpan() {
        if (href != null) {
            writer.writeLiteral(String.format(URL_FORMAT, href));
            href = null;
        }
    }

    @Override
    public void entityReference(String inName) {
        String lEntity = String.format("'%s'", inName);
        entities.add(lEntity);
        writer.writeLiteral(lEntity);
    }

    @Override
    public void image(Attributes inAttributes, String inUrl) {
    }

    @Override
    public void imageLink(Attributes inLinkAttributes, Attributes inImageAttributes, String inHref, String inImageUrl) {
    }

    @Override
    public void lineBreak() {
        writer.writeLiteral(NL);
    }

    @Override
    public void link(Attributes inAttributes, String inHrefOrHashName, String inText) {
        writer.writeLiteral("link:");
        writer.writeLiteral(inText);
    }

    public Collection<String> getEntities() {
        return entities;
    }
}
