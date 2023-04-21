package org.gloudy.page.model.jdo;

import com.google.appengine.api.datastore.Key;
import org.gloudy.page.model.FlatPageListElement;
import org.gloudy.page.model.Page;
import org.gloudy.page.model.PageImpl;
import java.util.List;

/**
 *
 * @author pcorne
 */
public class StubPage implements Page {

    public List<Page> getChildPages() {
        throw new UnsupportedOperationException("StubPage! Should have used recursive page fetch!");
    }

    public Key getId() {
        throw new UnsupportedOperationException("StubPage! Should have used recursive page fetch!");
    }

    public String getName() {
        throw new UnsupportedOperationException("StubPage! Should have used recursive page fetch!");
    }

    public PageImpl getParentPage() {
        throw new UnsupportedOperationException("StubPage! Should have used recursive page fetch!");
    }

    public boolean isHomepage() {
        throw new UnsupportedOperationException("StubPage! Should have used recursive page fetch!");
    }

    public void setChildPages(List<Page> children) {
        throw new UnsupportedOperationException("StubPage! Should have used recursive page fetch!");
    }

    public void setId(Key newId) {
        throw new UnsupportedOperationException("StubPage! Should have used recursive page fetch!");
    }

    public void setName(String name) {
        throw new UnsupportedOperationException("StubPage! Should have used recursive page fetch!");
    }

    public void setParentPage(Page parent) {
        throw new UnsupportedOperationException("StubPage! Should have used recursive page fetch!");
    }

    public List<FlatPageListElement> unfoldPageTree() {
        throw new UnsupportedOperationException("StubPage! Should have used recursive page fetch!");
    }
}
