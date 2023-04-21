package org.wangfy.hy.util;

import org.wangfy.hy.util.UuidGenerator;
import org.wangfy.hy.util.UuidGeneratorFactory;
import junit.framework.TestCase;

public class UuidGeneratorTest extends TestCase {

    private UuidGenerator uuidGenerator = UuidGeneratorFactory.GENERATOR;

    public void testGetUuid() {
        uuidGenerator.setPrefix("PREFIX-");
        uuidGenerator.setSuffix("-SUFFIX");
        String uuid1 = uuidGenerator.getUuid();
        String uuid2 = uuidGenerator.getUuid();
        System.out.println(uuid1);
        System.out.println(uuid2);
    }
}
