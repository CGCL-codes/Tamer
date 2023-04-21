package net.kodeninja.jem.server.UPnP.description.search;

import net.kodeninja.jem.server.UPnP.description.internal.MediaTree;

public interface Terminal {

    public boolean evaluate(MediaTree mt);
}
