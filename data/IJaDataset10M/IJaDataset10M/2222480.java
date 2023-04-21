package net.sf.dbchanges.util;

import net.sf.dbchanges.test.DbChangesTestCase;
import org.junit.Test;

/**
 * @author olex
 */
public class NiceToStringBuilderTest extends DbChangesTestCase {

    @Test
    public void test_to_string_dont_fail() throws Exception {
        new NiceToStringBuilder(this).toString();
    }
}
