package org.riverock.dbrevision.manager.config;

import org.riverock.dbrevision.manager.Config;
import java.io.InputStream;

/**
 * User: SergeMaslyukov
 * Date: 29.07.2007
 * Time: 16:53:24
 */
public interface ConfigParser {

    Config parse(InputStream inputStream);
}
