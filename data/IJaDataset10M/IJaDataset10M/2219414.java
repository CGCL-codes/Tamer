package com.flagstone.translate;

import java.util.LinkedHashMap;
import java.util.Map;
import com.flagstone.translate.as1.AS1Generator;

public final class GeneratorRegistry {

    private static Map<Integer, GeneratorProvider> providers = new LinkedHashMap<Integer, GeneratorProvider>();

    static {
        providers.put(1, new AS1Generator());
    }

    public static void registerProvider(final int version, final GeneratorProvider provider) {
        providers.put(version, provider);
    }

    public static Generator getGenerator(final int version) {
        if (providers.containsKey(version)) {
            return providers.get(version).newGenerator();
        } else {
            throw new IllegalArgumentException();
        }
    }

    /** Private constructor for the registry. */
    private GeneratorRegistry() {
    }
}
