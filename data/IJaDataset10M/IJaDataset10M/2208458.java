package edu.pku.dao;

import java.sql.SQLException;
import java.util.List;
import edu.pku.data.ProductCategory;
import junit.framework.TestCase;

public class ProductCategoryDaoTest extends TestCase {

    private CompanyDao cd;

    private ProductCategoryDao pcd;

    @Override
    protected void setUp() throws Exception {
        cd = new CompanyDao();
        pcd = new ProductCategoryDao();
        super.setUp();
    }

    public void test1() {
        try {
            ProductCategory pc = new ProductCategory();
            pc.setCompanyName("yuan");
            pc.setName("yuanProductCategory");
            pc.setDescription("yuanProductCategory");
            pcd.addProductCategory(pc);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void test2() {
        try {
            System.out.println(pcd.isNameExist("yuan", "yuanProductCategory"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void test3() {
        try {
            List<ProductCategory> list = pcd.getProductCategorys("yuan");
            for (ProductCategory pc : list) {
                pc.setCompanyName("yuan");
                System.out.println(pc.getId());
                System.out.println(pc.getCompanyName());
                System.out.println(pc.getName());
                System.out.println(pc.getDescription());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void test4() {
        try {
            List<ProductCategory> list = pcd.getProductCategorys("yuan");
            for (ProductCategory pc : list) {
                pc.setCompanyName("yuan");
                System.out.println(pc.getId());
                System.out.println(pc.getCompanyName());
                System.out.println(pc.getName());
                System.out.println(pc.getDescription());
                pcd.deleteProductCategory(pc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
