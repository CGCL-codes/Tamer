package org.vardb.graphics;

import javax.annotation.Resource;
import org.fest.assertions.Assertions;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.vardb.AbstractTest;
import org.vardb.resources.dao.CImage;
import org.vardb.util.CFileHelper;

@ContextConfiguration(locations = { "classpath:spring-test.xml" })
public class TestGraphicsService extends AbstractTest {

    @Resource(name = "graphicsService")
    private IGraphicsService graphicsService;

    @Test
    public void testCreateSequenceImage() {
        Assertions.assertThat(graphicsService.getCacheImages()).isFalse();
        String accession = "MAL13P1.1";
        CImage image = graphicsService.findOrCreateSequenceImage(accession);
        CFileHelper.writeFile("d:/temp/sequence.svg", image.getSvg());
    }
}
