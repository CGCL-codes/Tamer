package com.dukesoftware.utils.test;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Ignore
public class CategoryTest {

    public interface SlowTests {
    }

    public static interface IntegrationTests extends SlowTests {
    }

    public static interface PerformanceTests extends SlowTests {
    }
}
