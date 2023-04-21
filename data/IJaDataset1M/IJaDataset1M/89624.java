package ru.adv.test.repository.triggers;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.annotation.NotTransactional;
import ru.adv.db.base.MObject;
import ru.adv.db.base.MValue;
import ru.adv.db.base.Null;
import ru.adv.db.config.trigger.AISystemTrigger;
import ru.adv.db.config.trigger.TriggerContext;
import ru.adv.db.handler.HandlerI;

public class AITriggerTest extends AbstractTriggerTest {

    private static final int _12 = 12;

    private static final int _11 = 11;

    @Test
    @NotTransactional
    public void testLogTables() throws Exception {
        logger.info("testLogTables");
        AISystemTrigger trigger = new AISystemTrigger();
        testLogTablesModificationsOn_D_table(trigger);
    }

    @Test
    @NotTransactional
    public void testRootTreeInsert() throws Exception {
        logger.info("testRootTreeInsert");
        AISystemTrigger trigger = new AISystemTrigger();
        HandlerI handler = getRepository().getHandler(DB_NAME);
        MObject mObject = createMObject(handler, "i", _11);
        mObject.getAttribute("tree").setDBValue(new MValue(Null.INSTANCE));
        TriggerContext ctx = new TriggerContext(handler, trigger.getEvents().iterator().next(), mObject, null, SECURITY_OPTIONS);
        this.simpleJdbcTemplate.update("insert into i (id,tree,title,k) values (?,?,?,?)", new Object[] { _11, null, "my parent is NULL", 1 });
        executeTriger(trigger, ctx);
        Assert.assertEquals("i_tree has no inserted row", 0, this.simpleJdbcTemplate.queryForInt("select count(*) from i_tree where id=? OR ancestor=?", new Object[] { _11, _11 }));
    }

    @Test
    @NotTransactional
    public void testTreeInsert() throws Exception {
        logger.info("testTreeInsert");
        AISystemTrigger trigger = new AISystemTrigger();
        HandlerI handler = getRepository().getHandler(DB_NAME);
        MObject mObject = createMObject(handler, "i", _11);
        mObject.getAttribute("tree").setDBValue(new MValue(_1));
        TriggerContext ctx = new TriggerContext(handler, trigger.getEvents().iterator().next(), mObject, null, SECURITY_OPTIONS);
        this.simpleJdbcTemplate.update("insert into i (id,tree,title,k) values (?,?,?,?)", new Object[] { _11, _1, "my parant is 1", 1 });
        executeTriger(trigger, ctx);
        Assert.assertEquals("i_tree has inserted row", 1, this.simpleJdbcTemplate.queryForInt("select count(*) from i_tree where id=? and ancestor=?", new Object[] { _11, _1 }));
        mObject = createMObject(handler, "i", _12);
        mObject.getAttribute("tree").setDBValue(new MValue(_11));
        ctx = new TriggerContext(handler, trigger.getEvents().iterator().next(), mObject, null, SECURITY_OPTIONS);
        this.simpleJdbcTemplate.update("insert into i (id,tree,title,k) values (?,?,?,?)", new Object[] { _12, _11, "my parant is 11", 1 });
        executeTriger(trigger, ctx);
        Assert.assertEquals("12 is child of 11", 1, this.simpleJdbcTemplate.queryForInt("select count(*) from i_tree where id=? and ancestor=?", new Object[] { _12, _11 }));
        Assert.assertEquals("12 is descendant of 1", 1, this.simpleJdbcTemplate.queryForInt("select count(*) from i_tree where id=? and ancestor=?", new Object[] { _12, _1 }));
    }
}
