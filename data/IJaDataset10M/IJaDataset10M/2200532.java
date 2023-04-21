package com.wozgonon.random;

import org.junit.Test;
import com.wozgonon.eventstore.UseCaseManager;

public class RandomLoaderTest {

    @Test
    public final void testRandomLoader() {
        final UseCaseManager store = new UseCaseManager();
        final RandomLoader loader = new RandomLoader(store, 6);
        while (loader.next()) {
        }
    }
}
