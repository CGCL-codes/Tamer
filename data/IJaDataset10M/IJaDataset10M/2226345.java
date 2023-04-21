package it.freax.fpm.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Map;

/**
 * @author kLeZ-hAcK
 */
public class GrammarFilter<K, V> implements FilenameFilter {

    private final Map<K, V> supported_langs;

    public GrammarFilter(Map<K, V> supported_langs) {
        this.supported_langs = supported_langs;
    }

    @Override
    public boolean accept(File dir, String name) {
        return supported_langs.containsKey(name) && name.endsWith(".g");
    }
}
