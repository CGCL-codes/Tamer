package parser;

import java.util.Iterator;
import java.util.List;
import model.Book;
import model.Item;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

public class BookParser {

    public Book parse(String responseGetBody) {
        HtmlCleaner htmlCleaner = new HtmlCleaner();
        TagNode node = htmlCleaner.clean(responseGetBody);
        List divsList1 = node.getElementListByName("div", true);
        Iterator iterDivs = divsList1.iterator();
        Book book = new Book();
        while (iterDivs.hasNext()) {
            TagNode divNode = (TagNode) iterDivs.next();
            String classValue = divNode.getAttributeByName("class");
            if (classValue != null) {
                if (classValue.equals("avlBlk")) {
                    List<TagNode> divChilds = divNode.getChildren();
                    Iterator iterDivChilds = divChilds.iterator();
                    while (iterDivChilds.hasNext()) {
                        TagNode child = null;
                        child = (TagNode) iterDivChilds.next();
                        if (child.hasAttribute("id")) {
                            if ((child.getAttributeByName("id").equals("ttable1")) || (child.getAttributeByName("id").equals("ttable2"))) {
                                ItemParser itemParser = new ItemParser();
                                Item item = new Item();
                                item = itemParser.parse(child);
                                if (item.type.equals(ParserConstants.IDA)) {
                                    book.setFromItem(item);
                                } else {
                                    book.setToItem(item);
                                }
                            }
                        }
                    }
                }
            }
        }
        return book;
    }
}
