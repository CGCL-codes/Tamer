package at.neckam.rezepte.service;

import at.neckam.rezepte.bo.ZutatenName;
import java.util.List;
import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Test;

public class ZutatenNamenServiceTest {

    public ZutatenNamenServiceTest() {
    }

    /**
   * @see ZutatenNamenService#findByName(String)
   */
    @Test
    public void testFindByNameTrueffel() {
        ZutatenNamenService zutatenNameService = new ZutatenNamenService();
        ZutatenName zutatenName = zutatenNameService.findByName("Tr�ffel");
        Assert.assertNull("Zutatenname sind null.", zutatenName);
    }

    @Test
    public void testFindByNameMehl() {
        ZutatenNamenService zutatenNameService = new ZutatenNamenService();
        ZutatenName zutatenName = zutatenNameService.findByName("Mehl");
        Assert.assertNotNull("Zutatenname ist null.", zutatenName);
        Assert.assertEquals("Mehl", zutatenName.getName());
    }
}
