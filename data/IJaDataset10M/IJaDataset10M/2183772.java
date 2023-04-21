package de.uni_leipzig.lots.server.services;

import org.dom4j.Document;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.io.InputStream;

/**
 * @author Alexander Kiel
 * @version $Id: RestoreService.java,v 1.5 2007/10/23 06:30:14 mai99bxd Exp $
 */
public interface RestoreService extends Service {

    void restore(@NotNull Document document) throws RestoreException;

    void restore(@NotNull File file) throws RestoreException;

    void restore(@NotNull InputStream in) throws RestoreException;
}
