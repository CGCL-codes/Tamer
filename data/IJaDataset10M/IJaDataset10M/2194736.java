package org.sac.browse.client.util;

import java.io.File;

public class OnlyFileNameFilter extends OnlyDirNameFilter {

    public boolean accept(File dir, String name) {
        return !super.accept(dir, name);
    }
}
