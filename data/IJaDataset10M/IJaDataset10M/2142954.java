package net.sf.fractopt.test.core.entity;

import junit.framework.TestCase;
import net.sf.fractopt.core.entity.Player;
import net.sf.fractopt.core.entity.Community;
import net.sf.fractopt.core.entity.Merchandise;
import net.sf.fractopt.core.entity.MerchandiseEnum;
import net.sf.fractopt.core.entity.Producer;
import net.sf.fractopt.core.entity.Stock;
import net.sf.fractopt.util.Date;

public class TestProducteur extends TestCase {

    Producer producteurToTest;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(TestProducteur.class);
    }

    @Override
    protected void setUp() throws Exception {
        producteurToTest = new Community("Toto", new Player("FractOpt"));
        Stock res = new Stock(producteurToTest, new Date(12).getLunes());
        res.addMarchandise(new Merchandise(MerchandiseEnum.EAU, 10));
        res.addMarchandise(new Merchandise(MerchandiseEnum.NOURRITURE, 5));
        res.addMarchandise(new Merchandise(MerchandiseEnum.EAU, 2));
        Stock res2 = new Stock(producteurToTest, new Date(13).getLunes());
        res2.addMarchandise(new Merchandise(MerchandiseEnum.EAU, 6));
        res2.addMarchandise(new Merchandise(MerchandiseEnum.NOURRITURE, 3));
        res2.addMarchandise(new Merchandise(MerchandiseEnum.EAU, 1));
        producteurToTest.addReserve(res);
        producteurToTest.addReserve(res2);
    }

    public void testToString() {
        System.out.println(producteurToTest.toString());
    }
}
