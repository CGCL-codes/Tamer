package test.sql;

import java.util.HashMap;
import java.util.Map;
import org.eweb4j.config.EWeb4JConfig;
import org.eweb4j.orm.sql.DeleteSqlCreator;
import org.eweb4j.orm.sql.SqlFactory;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import test.po.Master;
import test.po.Pet;

/**
 * 测试删除sql语句创建类的所有方法
 * 
 * @author weiwei
 * 
 */
public class TestDeleteSql {

    private static DeleteSqlCreator<Pet> delete;

    private static Pet pet;

    @BeforeClass
    public static void prepare() throws Exception {
        String err = EWeb4JConfig.start("start.eweb.xml");
        if (err != null) {
            System.out.println(">>>EWeb4J Start Error --> " + err);
            System.exit(-1);
        }
        pet = new Pet();
        pet.setAge(30);
        delete = SqlFactory.getDeleteSql(pet);
    }

    @Test
    public void testMap() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("table", "t_pet");
        map.put("idColumn", "id");
        map.put("idValue", 7);
        Pet pet = new Pet();
        DeleteSqlCreator<?> delete = SqlFactory.getDeleteSql(map, pet);
        Assert.assertEquals("DELETE FROM t_pet WHERE id = '7' ;", delete.delete()[0]);
    }

    /**
	 * 删除记录(按主键)
	 * 
	 * @param ts
	 * @return
	 */
    @Test
    public void testDeleteById() {
        pet.setPetId(10L);
        String sql = delete.delete()[0];
        Assert.assertEquals("DELETE FROM t_pet WHERE id = '10' ;", sql);
    }

    /**
	 * 删除记录，按给定字段
	 * 
	 * @param columns
	 * @return
	 */
    @Test
    public void testDeleteByField() {
        String sql = delete.delete(new String[] { "age" })[0];
        Assert.assertEquals("DELETE FROM t_pet WHERE age = '30' ;", sql);
    }

    /**
	 * 测试一(多)对一关系的删除sql语句生成
	 * 
	 * @throws Exception
	 */
    @Test
    public void testDeleteByOneRelField() throws Exception {
        Master master = new Master();
        master.setId(5L);
        pet.setMaster(master);
        String[] fields = { "master" };
        String sql = delete.delete(fields)[0];
        Assert.assertEquals("DELETE FROM t_pet WHERE master_id = '5' ;", sql);
    }

    /**
	 * 删除记录，按给定字段、给定值
	 * 
	 * @param clazz
	 * @param fields
	 * @param values
	 * @return
	 */
    @Test
    public void testDeleteByFieldAndValue() {
        String sql = delete.delete(new String[] { "age" }, new String[] { "50" })[0];
        Assert.assertEquals("DELETE FROM t_pet WHERE age = '50' ;", sql);
    }

    /**
	 * 给定条件删除记录
	 * 
	 * @param clazz
	 * @param condition
	 * @return
	 */
    @Test
    public void testDeleteWhere() {
        pet.setName("weiwei");
        String sql = delete.deleteWhere("name = 'weiwei'");
        Assert.assertEquals("DELETE FROM t_pet WHERE name = 'weiwei' ;", sql);
    }

    /**
	 * 给定条件删除记录，支持？占位符
	 * 
	 * @param clazz
	 * @param condition
	 * @param args
	 * @return
	 */
    @Test
    public void testDeleteWhereByArgs() {
        String sql = delete.deleteWhere("name = ?");
        Assert.assertEquals("DELETE FROM t_pet WHERE name = ? ;", sql);
    }
}
