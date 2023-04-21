package net.sf.iqser.plugin.web.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import com.iqser.core.exception.IQserException;
import com.iqser.core.model.Content;
import com.iqser.core.plugin.ContentProviderFacade;

public class MockContentProviderFacade implements ContentProviderFacade {

    private Collection<Content> contents = null;

    public MockContentProviderFacade() {
        contents = new ArrayList();
    }

    @Override
    public void addContent(Content arg0) throws IQserException {
        contents.add(arg0);
    }

    @Override
    public Collection<Content> getExistingContents(String arg0) throws IQserException {
        Collection<Content> col = new ArrayList();
        col.addAll(contents);
        return col;
    }

    @Override
    public boolean isExistingContent(String arg0, String arg1) throws IQserException {
        Iterator cIter = contents.iterator();
        while (cIter.hasNext()) {
            Content c = (Content) cIter.next();
            if (c.getContentUrl().equalsIgnoreCase(arg1)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void removeContent(String arg0, String arg1) throws IQserException {
        Iterator cIter = contents.iterator();
        while (cIter.hasNext()) {
            Content c = (Content) cIter.next();
            if (c.getContentUrl().equalsIgnoreCase(arg1)) {
                contents.remove(c);
                break;
            }
        }
    }

    @Override
    public void updateContent(Content arg0) throws IQserException {
        Iterator cIter = contents.iterator();
        while (cIter.hasNext()) {
            Content c = (Content) cIter.next();
            if (c.getContentUrl().equalsIgnoreCase(arg0.getContentUrl())) {
                contents.remove(c);
                contents.add(arg0);
                break;
            }
        }
    }
}
