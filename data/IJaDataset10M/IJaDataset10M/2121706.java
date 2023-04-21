package furniture.model.foundation;

import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import furniture.core.dao.CrudRepository;
import furniture.material.model.MaterialEntity;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-core-test.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class MaterialEntityDaoTest {

    @Autowired
    private CrudRepository crudRepository;

    private MaterialEntity board3;

    private MaterialEntityTest mock = new MaterialEntityTest();

    @Test
    @Transactional
    @Rollback(false)
    public void insert() {
        board3 = mock.getBoard3();
        mock.broad3Add6Childs(board3);
        crudRepository.create(board3);
        Assert.assertNotNull(board3);
        Assert.assertTrue(board3.hasChild());
        Assert.assertEquals(6, board3.getChilds().size());
    }

    @Test
    @Transactional
    @Rollback(false)
    public void update() {
        insert();
        MaterialEntity board3New = crudRepository.retrieve(MaterialEntity.class, board3.getId());
        Assert.assertNotNull(board3New);
        Assert.assertEquals(board3New, board3);
        Assert.assertTrue(board3New.hasChild());
        Assert.assertEquals(6, board3New.getChilds().size());
        MaterialEntity board2 = mock.getBoard2();
        board3New.addChild(board2);
        crudRepository.update(board3New);
        Assert.assertEquals(7, board3New.getChilds().size());
        String newName = "3M木板";
        board3New.setName(newName);
        crudRepository.update(board3New);
        board2.setName("2M木板");
        crudRepository.update(board2);
        board3New = crudRepository.retrieve(MaterialEntity.class, board3.getId());
        Assert.assertEquals(newName, board3New.getName());
    }

    @Test
    @Transactional
    @Rollback(false)
    public void delete() {
        insert();
        MaterialEntity board3New = crudRepository.retrieve(MaterialEntity.class, board3.getId());
        crudRepository.delete(board3New);
        board3New = crudRepository.retrieve(MaterialEntity.class, board3.getId());
        Assert.assertNull(board3New);
    }
}
