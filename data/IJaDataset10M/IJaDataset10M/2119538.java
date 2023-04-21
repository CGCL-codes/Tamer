package com.customwars.client.io.loading;

import java.io.IOException;

/**
 * Defines objects that can load themself
 */
public interface CWResourceLoader {

    void load() throws IOException;
}
