package org.doxla.spring.automock2.resolver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.doxla.spring.automock2.resolver.ListedInterfaceMockClassResolver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/listedInterfaceMockClassResolver-testContext.xml")
public class ListedInterfaceMockClassResolverIntTest {

    @Autowired
    private ListedInterfaceMockClassResolver listedInterfaceMockClassResolver;

    @Test
    public void testCorrectClassesResolved() throws Exception {
        assertNotNull(listedInterfaceMockClassResolver);
        assertEquals(2, listedInterfaceMockClassResolver.resolveClassesToMock(null).size());
    }
}
