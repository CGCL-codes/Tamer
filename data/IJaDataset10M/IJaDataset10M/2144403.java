package com.ncs.order;

import com.ncs.order.dao.ImageDao;
import com.ncs.order.service.ImageService;
import com.ncs.order.to.EmployeeTo;
import com.ncs.order.to.ImageTo;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import javax.annotation.Resource;
import java.util.Set;

@ContextConfiguration(locations = { "classpath:spring/test-config.xml" })
public class TestImage extends AbstractTransactionalJUnit4SpringContextTests {

    private static final String TEST_PATH = "123";

    @Resource
    private ImageService imageService;

    @Test
    public void test1SaveAndGet() {
        createAndSaveImage(TEST_PATH, "1");
        ImageTo image = imageService.get("path", TEST_PATH);
        Assert.assertEquals(TEST_PATH, image.getPath());
        Assert.assertEquals("1", image.getSeq());
    }

    private void createAndSaveImage(String path, String seq) {
        ImageTo image = new ImageTo();
        image.setPath(path);
        image.setSeq(seq);
        imageService.save(image);
    }

    @Test
    public void test3Update() {
        createAndSaveImage(TEST_PATH, "1");
        ImageTo image = imageService.get("path", TEST_PATH);
        Assert.assertEquals(TEST_PATH, image.getPath());
        Assert.assertEquals("1", image.getSeq());
        image.setSeq("2");
        imageService.update(image);
        ImageTo image2 = imageService.get("path", TEST_PATH);
        Assert.assertEquals(TEST_PATH, image2.getPath());
        Assert.assertEquals("2", image2.getSeq());
    }

    @Test
    public void test4Delete() {
        createAndSaveImage(TEST_PATH, "1");
        Set set1 = imageService.getList("path", TEST_PATH);
        Assert.assertEquals(1, set1.size());
        ImageTo image = imageService.get("path", TEST_PATH);
        imageService.delete(image);
        Set set2 = imageService.getList("path", TEST_PATH);
        Assert.assertEquals(0, set2.size());
    }
}
