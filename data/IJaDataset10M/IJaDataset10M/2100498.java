package org.escapek.core.services.interfaces;

import javax.ejb.Local;
import org.escapek.core.exceptions.SecurityException;
import org.escapek.core.internal.model.registry.RegistryNode;
import org.escapek.core.internal.model.security.Ticket;

@Local
public interface ILocalRegistryService {

    public RegistryNode getRegistryNode(Long id);

    public RegistryNode getRegistryNode(String path);

    public RegistryNode createRegistryNode(Ticket t, RegistryNode aNode) throws SecurityException;
}
