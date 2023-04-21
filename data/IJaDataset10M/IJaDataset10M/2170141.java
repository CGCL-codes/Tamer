package dbgate.utility;

import dbgate.DBClassStatus;
import dbgate.utility.support.LeafEntity;
import dbgate.utility.support.RootEntity;
import junit.framework.Assert;
import org.junit.Test;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Adipa
 * Date: Sep 12, 2010
 * Time: 9:26:30 AM
 */
public class StatusUtilityTests {

    @Test
    public void StatusManager_setStatus_withMultiLevelHirachy_shouldSetStatus() {
        RootEntity rootEntity = new RootEntity();
        LeafEntity leafEntityA = new LeafEntity();
        leafEntityA.setRootEntity(rootEntity);
        rootEntity.getLeafEntities().add(leafEntityA);
        LeafEntity leafEntityB = new LeafEntity();
        leafEntityB.setRootEntity(rootEntity);
        rootEntity.getLeafEntities().add(leafEntityB);
        LeafEntity leafEntityNotNull = new LeafEntity();
        leafEntityNotNull.setRootEntity(rootEntity);
        rootEntity.setLeafEntityNotNull(leafEntityNotNull);
        rootEntity.setLeafEntityNull(null);
        StatusUtility.setStatus(rootEntity, DBClassStatus.MODIFIED);
        Assert.assertEquals(rootEntity.getStatus(), DBClassStatus.MODIFIED);
        Assert.assertEquals(leafEntityA.getStatus(), DBClassStatus.MODIFIED);
        Assert.assertEquals(leafEntityB.getStatus(), DBClassStatus.MODIFIED);
        Assert.assertEquals(leafEntityNotNull.getStatus(), DBClassStatus.MODIFIED);
    }

    @Test
    public void StatusManager_isModified_withMultiLevelHirachy_shouldGetStatus() {
        RootEntity rootEntity = new RootEntity();
        LeafEntity leafEntityA = new LeafEntity();
        leafEntityA.setRootEntity(rootEntity);
        rootEntity.getLeafEntities().add(leafEntityA);
        LeafEntity leafEntityB = new LeafEntity();
        leafEntityB.setRootEntity(rootEntity);
        rootEntity.getLeafEntities().add(leafEntityB);
        LeafEntity leafEntityNotNull = new LeafEntity();
        leafEntityNotNull.setRootEntity(rootEntity);
        rootEntity.setLeafEntityNotNull(leafEntityNotNull);
        rootEntity.setLeafEntityNull(null);
        boolean unModifiedRoot = StatusUtility.isModified(rootEntity);
        rootEntity.setStatus(DBClassStatus.MODIFIED);
        boolean modifiedRoot = StatusUtility.isModified(rootEntity);
        rootEntity.setStatus(DBClassStatus.UNMODIFIED);
        leafEntityA.setStatus(DBClassStatus.NEW);
        boolean modifiedLeafCollection = StatusUtility.isModified(rootEntity);
        leafEntityA.setStatus(DBClassStatus.UNMODIFIED);
        leafEntityNotNull.setStatus(DBClassStatus.DELETED);
        boolean modifiedLeafSubEntity = StatusUtility.isModified(rootEntity);
        Assert.assertFalse(unModifiedRoot);
        Assert.assertTrue(modifiedRoot);
        Assert.assertTrue(modifiedLeafCollection);
        Assert.assertTrue(modifiedLeafSubEntity);
    }

    @Test
    public void StatusManager_getImmidiateChildrenAndClear_withMultiLevelHirachy_shouldGetChildren() {
        RootEntity rootEntity = new RootEntity();
        LeafEntity leafEntityA = new LeafEntity();
        leafEntityA.setRootEntity(rootEntity);
        rootEntity.getLeafEntities().add(leafEntityA);
        LeafEntity leafEntityB = new LeafEntity();
        leafEntityB.setRootEntity(rootEntity);
        rootEntity.getLeafEntities().add(leafEntityB);
        LeafEntity leafEntityNotNull = new LeafEntity();
        leafEntityNotNull.setRootEntity(rootEntity);
        rootEntity.setLeafEntityNotNull(leafEntityNotNull);
        rootEntity.setLeafEntityNull(null);
        Collection childern = StatusUtility.getImmidiateChildrenAndClear(rootEntity);
        Assert.assertTrue(childern.contains(leafEntityA));
        Assert.assertTrue(childern.contains(leafEntityB));
        Assert.assertTrue(childern.contains(leafEntityNotNull));
        Assert.assertTrue(rootEntity.getLeafEntities().size() == 0);
        Assert.assertNull(rootEntity.getLeafEntityNotNull());
    }
}
