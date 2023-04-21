package org_daisy;

import java.util.Collection;
import org.daisy.paper.CustomPaperCollection;
import org.daisy.paper.Paper;
import org.daisy.paper.PaperProvider;

public class CustomPaperProvider implements PaperProvider {

    public CustomPaperProvider() {
    }

    public Collection<Paper> list() {
        return CustomPaperCollection.getInstance().list();
    }
}
