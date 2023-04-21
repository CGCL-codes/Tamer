package org.tagunit.tagext.assertion;

import org.tagunit.test.NoAttributesTestPackage;
import org.tagunit.test.TestPackage;

/**
 * An assertion tag that tests whether a tag has no attributes.
 *
 * @author    Simon Brown
 */
public class AssertNoAttributesTag extends AssertTag {

    protected TestPackage getTestPackage() {
        return new NoAttributesTestPackage(getTagTestContext().getTagInfo());
    }
}
