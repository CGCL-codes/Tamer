package com.yehongyu.mansys.dao;

import java.util.List;
import javax.annotation.Resource;
import junit.framework.Assert;
import org.junit.Test;
import com.yehongyu.mansys.BaseDAOTestCase;
import com.yehongyu.mansys.dao.domain.SysMenuDO;
import com.yehongyu.mansys.dao.ibatis.SysMenuDAO;
import com.yehongyu.mansys.dao.query.SysMenuQuery;

/**
 * sys_menu������
 * @author yingyang
 * @since 2011-11-11
 */
public class SysMenuDAOTest extends BaseDAOTestCase {

    @Resource
    private SysMenuDAO sysMenuDAO;

    @Test
    public final void testInsertUpdateQueryDeleteSysMenuDO() {
        SysMenuDO sysMenuDO = new SysMenuDO();
        sysMenuDO.setMenucode("s2");
        sysMenuDO.setMenuname("s3");
        sysMenuDO.setMenuurl("s4");
        sysMenuDO.setMenulevel(5);
        sysMenuDO.setIsleaf(6);
        sysMenuDO.setParentscode("s7");
        sysMenuDO.setRootcode("s8");
        sysMenuDO.setDisplayorder("s9");
        sysMenuDO.setStatus(10);
        sysMenuDO.setIssys(11);
        Long id = null;
        try {
            id = sysMenuDAO.insertSysMenuDO(sysMenuDO);
            if (id != null) {
                sysMenuDO = new SysMenuDO();
                sysMenuDO.setId(id);
                sysMenuDO.setMenucode("ms2");
                sysMenuDO.setMenuname("ms3");
                sysMenuDO.setMenuurl("ms4");
                sysMenuDO.setMenulevel(15);
                sysMenuDO.setIsleaf(16);
                sysMenuDO.setParentscode("ms7");
                sysMenuDO.setRootcode("ms8");
                sysMenuDO.setDisplayorder("ms9");
                sysMenuDO.setStatus(110);
                sysMenuDO.setIssys(111);
                Integer rsUpdate = sysMenuDAO.updateSysMenuDO(sysMenuDO);
                Assert.assertEquals(1, rsUpdate.intValue());
                SysMenuQuery sysMenuQuery = new SysMenuQuery();
                sysMenuQuery.setId(id);
                SysMenuDO queryRs = sysMenuDAO.getSysMenuDO(sysMenuQuery);
                Assert.assertEquals("ms2", queryRs.getMenucode());
                Assert.assertEquals("ms3", queryRs.getMenuname());
                Assert.assertEquals("ms4", queryRs.getMenuurl());
                Assert.assertEquals(15, queryRs.getMenulevel().intValue());
                Assert.assertEquals(16, queryRs.getIsleaf().intValue());
                Assert.assertEquals("ms7", queryRs.getParentscode());
                Assert.assertEquals("ms8", queryRs.getRootcode());
                Assert.assertEquals("ms9", queryRs.getDisplayorder());
                Assert.assertEquals(110, queryRs.getStatus().intValue());
                Assert.assertEquals(111, queryRs.getIssys().intValue());
                Integer rsDelete = sysMenuDAO.deleteSysMenuDO(id);
                Assert.assertEquals(1, rsDelete.intValue());
            } else {
                Assert.assertFalse("happen Exception,not insert demo data", true);
            }
        } catch (Exception e) {
            logger.error("testInsertUpdateQueryDeleteSysMenuDO() error", e);
            Assert.assertFalse("happen Exception", true);
        } finally {
            if (id != null) {
                try {
                    sysMenuDAO.deleteSysMenuDO(id);
                } catch (Exception e) {
                    logger.error("testInsertUpdateQueryDeleteSysMenuDO() error at finally delete demo data", e);
                    Assert.assertFalse("happen Exception", true);
                }
            }
        }
    }

    @Test
    public final void testInsertUpdateQueryWithPageDeleteSysMenuDOList() {
        SysMenuDO sysMenuDO = new SysMenuDO();
        sysMenuDO.setMenucode("s2");
        sysMenuDO.setMenuname("s3");
        sysMenuDO.setMenuurl("s4");
        sysMenuDO.setMenulevel(5);
        sysMenuDO.setIsleaf(6);
        sysMenuDO.setParentscode("s7");
        sysMenuDO.setRootcode("s8");
        sysMenuDO.setDisplayorder("s9");
        sysMenuDO.setStatus(10);
        sysMenuDO.setIssys(11);
        Long id = null;
        try {
            id = sysMenuDAO.insertSysMenuDO(sysMenuDO);
            if (id != null) {
                sysMenuDO = new SysMenuDO();
                sysMenuDO.addIdList(id);
                sysMenuDO.setMenucode("ms2");
                sysMenuDO.setMenuname("ms3");
                sysMenuDO.setMenuurl("ms4");
                sysMenuDO.setMenulevel(15);
                sysMenuDO.setIsleaf(16);
                sysMenuDO.setParentscode("ms7");
                sysMenuDO.setRootcode("ms8");
                sysMenuDO.setDisplayorder("ms9");
                sysMenuDO.setStatus(110);
                sysMenuDO.setIssys(111);
                Integer rs = sysMenuDAO.updateSysMenuDOList(sysMenuDO);
                Assert.assertEquals(1, rs.intValue());
                SysMenuQuery sysMenuQuery = new SysMenuQuery();
                sysMenuQuery.addIdList(id);
                sysMenuQuery.orderbyId(false);
                sysMenuQuery.orderbyMenucode(false);
                sysMenuQuery.orderbyMenuname(false);
                sysMenuQuery.orderbyMenuurl(false);
                sysMenuQuery.orderbyMenulevel(false);
                sysMenuQuery.orderbyIsleaf(false);
                sysMenuQuery.orderbyParentscode(false);
                sysMenuQuery.orderbyRootcode(false);
                sysMenuQuery.orderbyDisplayorder(false);
                sysMenuQuery.orderbyStatus(false);
                sysMenuQuery.orderbyIssys(false);
                sysMenuQuery.orderbyGmtCreate(false);
                sysMenuQuery.orderbyGmtModified(false);
                List<SysMenuDO> queryRs = sysMenuDAO.getSysMenuDOList(sysMenuQuery);
                Assert.assertEquals(1, queryRs.size());
                Assert.assertEquals("ms2", queryRs.get(0).getMenucode());
                Assert.assertEquals("ms3", queryRs.get(0).getMenuname());
                Assert.assertEquals("ms4", queryRs.get(0).getMenuurl());
                Assert.assertEquals(15, queryRs.get(0).getMenulevel().intValue());
                Assert.assertEquals(16, queryRs.get(0).getIsleaf().intValue());
                Assert.assertEquals("ms7", queryRs.get(0).getParentscode());
                Assert.assertEquals("ms8", queryRs.get(0).getRootcode());
                Assert.assertEquals("ms9", queryRs.get(0).getDisplayorder());
                Assert.assertEquals(110, queryRs.get(0).getStatus().intValue());
                Assert.assertEquals(111, queryRs.get(0).getIssys().intValue());
                sysMenuQuery = new SysMenuQuery();
                sysMenuQuery.addIdList(id);
                sysMenuQuery.orderbyId(true);
                sysMenuQuery.orderbyMenucode(true);
                sysMenuQuery.orderbyMenuname(true);
                sysMenuQuery.orderbyMenuurl(true);
                sysMenuQuery.orderbyMenulevel(true);
                sysMenuQuery.orderbyIsleaf(true);
                sysMenuQuery.orderbyParentscode(true);
                sysMenuQuery.orderbyRootcode(true);
                sysMenuQuery.orderbyDisplayorder(true);
                sysMenuQuery.orderbyStatus(true);
                sysMenuQuery.orderbyIssys(true);
                sysMenuQuery.orderbyGmtCreate(true);
                sysMenuQuery.orderbyGmtModified(true);
                List<SysMenuDO> queryRsWithPage = sysMenuDAO.getSysMenuDOListWithPage(sysMenuQuery);
                Assert.assertEquals(1, queryRsWithPage.size());
                Assert.assertEquals("ms2", queryRs.get(0).getMenucode());
                Assert.assertEquals("ms3", queryRs.get(0).getMenuname());
                Assert.assertEquals("ms4", queryRs.get(0).getMenuurl());
                Assert.assertEquals(15, queryRs.get(0).getMenulevel().intValue());
                Assert.assertEquals(16, queryRs.get(0).getIsleaf().intValue());
                Assert.assertEquals("ms7", queryRs.get(0).getParentscode());
                Assert.assertEquals("ms8", queryRs.get(0).getRootcode());
                Assert.assertEquals("ms9", queryRs.get(0).getDisplayorder());
                Assert.assertEquals(110, queryRs.get(0).getStatus().intValue());
                Assert.assertEquals(111, queryRs.get(0).getIssys().intValue());
                sysMenuQuery = new SysMenuQuery();
                sysMenuQuery.addIdList(id);
                Integer rsDelete = sysMenuDAO.deleteSysMenuDOList(sysMenuQuery);
                Assert.assertEquals(1, rsDelete.intValue());
            } else {
                Assert.assertFalse("happen Exception,not insert demo data", true);
            }
        } catch (Exception e) {
            logger.error("testInsertUpdateQueryWithPageDeleteSysMenuDOList() error", e);
            Assert.assertFalse("happen Exception", true);
        } finally {
            if (id != null) {
                try {
                    sysMenuDAO.deleteSysMenuDO(id);
                } catch (Exception e) {
                    logger.error("testInsertUpdateQueryWithPageDeleteSysMenuDOList() error at finally delete demo data", e);
                    Assert.assertFalse("happen Exception", true);
                }
            }
        }
    }
}
