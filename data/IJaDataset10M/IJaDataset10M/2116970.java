package ru.ksu.niimm.cll.mocassin.crawl.parser.latex;

import java.util.List;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import ru.ksu.niimm.cll.mocassin.crawl.parser.impl.adapters.NodeAdapter;

/**
 * 
 * Document part
 * 
 * @author nzhiltsov
 * 
 */
@XmlJavaTypeAdapter(NodeAdapter.class)
public interface Node {

    String getId();

    String getName();

    boolean equals(Object o);

    int hashCode();

    String getLabelText();

    void setLabelText(String labelText);

    List<String> getContents();

    void addContents(String... text);

    int getBeginLine();

    int getEndLine();

    int getOffset();

    boolean isEnvironment();

    String getTitle();

    void setTitle(String title);

    boolean isNumbered();

    /**
	 * returns the start page number in the generated PDF file, on which a node
	 * is located
	 * 
	 * @return
	 */
    int getPdfPageNumber();

    void setPdfPageNumber(int pageNumber);
}
