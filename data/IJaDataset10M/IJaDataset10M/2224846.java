package org.eclipse.help.internal.base.remote;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import org.eclipse.help.IUAElement;
import org.eclipse.help.internal.UAElement;
import org.eclipse.help.internal.dynamic.DocumentReader;
import org.eclipse.help.internal.index.Index;
import org.eclipse.help.internal.index.IndexContribution;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class RemoteIndexParser extends DefaultHandler {

    private DocumentReader reader;

    public IndexContribution[] parse(InputStream in) throws ParserConfigurationException, SAXException, IOException {
        if (reader == null) {
            reader = new DocumentReader();
        }
        UAElement root = reader.read(in);
        IUAElement[] children = root.getChildren();
        IndexContribution[] contributions = new IndexContribution[children.length];
        for (int i = 0; i < children.length; ++i) {
            UAElement child = (UAElement) children[i];
            IndexContribution contribution = new IndexContribution();
            contribution.setId(child.getAttribute("id"));
            contribution.setLocale(child.getAttribute("locale"));
            contribution.setIndex((Index) child.getChildren()[0]);
            contributions[i] = contribution;
        }
        return contributions;
    }
}
