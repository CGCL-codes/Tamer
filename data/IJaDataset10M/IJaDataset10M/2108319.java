package test.cascade;

import java.util.List;
import org.eweb4j.config.EWeb4JConfig;
import org.eweb4j.orm.dao.DAOException;
import org.eweb4j.orm.dao.DAOFactory;
import org.eweb4j.orm.jdbc.transaction.Trans;
import org.eweb4j.orm.jdbc.transaction.Transaction;
import org.junit.Test;
import test.po.Master;
import test.po.Pet;

public class TestCascadeDAO {

    public static void testOneSelect() {
        List<Pet> petList;
        try {
            petList = DAOFactory.getSelectDAO().selectAll(Pet.class);
            if (petList != null) {
                for (Pet p : petList) {
                    System.out.println(p + "|" + p.getMaster());
                    DAOFactory.getCascadeDAO().select(p, "master");
                    System.out.println(p.getMaster());
                }
            }
        } catch (DAOException e) {
            e.printStackTrace();
        }
    }

    public static void testOneUpdate() {
        Transaction.execute(new Trans() {

            @Override
            public void run(Object... args) throws Exception {
                Master master = new Master();
                master.setId(1L);
                DAOFactory.getCascadeDAO().update(master, "pets", 2);
            }
        });
    }

    public static void testManyInsert() {
        Master master = new Master();
        master.setName("小日主人1");
        master.setGender("boy");
        long id = (Integer) DAOFactory.getInsertDAO().insert(master);
        master.setId(id);
        Pet pet = new Pet();
        pet.setName("小日1");
        pet.setType("dog");
        master.getPets().add(pet);
        pet = new Pet();
        pet.setName("小日2");
        pet.setType("cat");
        master.getPets().add(pet);
        DAOFactory.getCascadeDAO().insert(master);
    }

    public static void testManySelect() {
        List<Master> masterList;
        try {
            masterList = DAOFactory.getSelectDAO().selectAll(Master.class);
            if (masterList != null) {
                for (Master m : masterList) {
                    System.out.println(m + "|" + m.getPets());
                    DAOFactory.getCascadeDAO().select(m);
                    System.out.println(m.getPets());
                }
            }
        } catch (DAOException e) {
            e.printStackTrace();
        }
    }

    public static void testManyDelete() {
        List<Master> masterList;
        try {
            masterList = DAOFactory.getSelectDAO().selectAll(Master.class);
            if (masterList != null) {
                for (Master m : masterList) {
                    System.out.println(m + "|" + m.getPets());
                    DAOFactory.getCascadeDAO().delete(m, "pets");
                }
            }
        } catch (DAOException e) {
            e.printStackTrace();
        }
    }

    public static void testManyManyInsert() {
        Master master = new Master();
        master.setName("日本人");
        master.setGender("boy");
        Pet pet = new Pet();
        pet.setName("小日1");
        pet.setType("dog");
        pet.setPetId(6490L);
        master.getPets().add(pet);
        pet = new Pet();
        pet.setName("小日2");
        pet.setType("cat");
        master.getPets().add(pet);
        DAOFactory.getCascadeDAO().insert(master);
    }

    /**
	 * 测试多对多更新
	 */
    public static void testManyManyUpdate() {
        Master master = new Master().find().first();
        master.cascade().refresh(999, "pets");
    }

    public static void testManyManySelect() {
        List<Master> masterList = DAOFactory.getSelectDAO().selectAll(Master.class);
        if (masterList != null) {
            for (Master m : masterList) {
                System.out.println(m + "|" + m.getPets());
                DAOFactory.getCascadeDAO().select(m);
                System.out.println(m + "|" + m.getPets());
                break;
            }
        }
    }

    public static void testManyManyDelete() {
        List<Master> masterList;
        try {
            masterList = DAOFactory.getSelectDAO().selectAll(Master.class);
            if (masterList != null) {
                for (Master m : masterList) {
                    System.out.println(m + "|" + m.getPets());
                    Pet p = new Pet();
                    p.setPetId(5L);
                    m.getPets().add(p);
                    DAOFactory.getCascadeDAO().delete(m, "pets");
                    break;
                }
            }
        } catch (DAOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("fuck===============");
        String err = EWeb4JConfig.start("start.eweb.xml");
        if (err != null) throw new Exception(err);
        Master master = new Master().find().first();
        System.out.println("=============== " + master);
        master.cascade().refresh(777, "pet");
    }

    @Test
    public void testManyOne() {
    }
}
