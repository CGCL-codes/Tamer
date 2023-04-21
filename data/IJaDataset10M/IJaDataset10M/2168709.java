package org.nakedobjects.viewer.skylark.util;

import org.nakedobjects.object.Naked;
import org.nakedobjects.object.NakedCollection;
import org.nakedobjects.object.NakedObject;
import org.nakedobjects.utility.Assert;
import org.nakedobjects.viewer.skylark.Content;
import org.nakedobjects.viewer.skylark.RootCollection;
import org.nakedobjects.viewer.skylark.RootObject;

public class ContentFactory {

    public Content createRootContent(Naked object) {
        Assert.assertNotNull(object);
        Content content;
        if (object instanceof NakedCollection) {
            content = new RootCollection((NakedCollection) object);
        } else if (object instanceof NakedObject) {
            content = new RootObject((NakedObject) object);
        } else {
            throw new IllegalArgumentException("Must be an object or collection: " + object);
        }
        return content;
    }
}
