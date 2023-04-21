package com.yilan.module;

import java.net.URI;

public interface ModuleLoader {

    Module load(URI uri);

    String getType();
}
