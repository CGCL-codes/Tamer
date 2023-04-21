package org.padern.tangeerina.server.services;

import org.padern.tangeerina.client.valueobject.ArchiveVO;
import org.padern.tangeerina.server.entities.Archive;

public interface ArchiveService {

    public void add(ArchiveVO archive);

    public Archive[] getAll();
}
