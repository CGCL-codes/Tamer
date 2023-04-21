package fastforward.meta.test.util;

import org.junit.Test;
import fastforward.meta.PropertyContextPath;
import fastforward.meta.util.BeanCopier;

public class BeanCopierTest extends MetadataTester {

    @Test
    public void deepCopy() {
        Bean1 bean1 = createTestBean1();
        Bean1 target = new Bean1();
        new BeanCopier(getMetadataRepository()).setIgnoreMissingPropertiesInTarget(true).copy(bean1, target);
        compareBeans(bean1, target, new PropertyContextPath(Bean1.class));
    }
}
