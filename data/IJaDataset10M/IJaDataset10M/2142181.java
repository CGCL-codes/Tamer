package org.openthinclient.nfs;

import java.util.ArrayList;
import java.util.List;
import org.openthinclient.mountd.NFSExport;

/**
 * @author levigo
 */
public class Exports extends ArrayList<NFSExport> {

    private static final long serialVersionUID = 3257284751277963056L;

    public Exports(List<NFSExport> exports) {
        super(exports);
    }

    public Exports() {
        super();
    }
}
