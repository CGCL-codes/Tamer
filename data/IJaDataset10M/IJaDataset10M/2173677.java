package net.sourceforge.fuge.common.protocol;

import org.testng.annotations.Test;
import java.util.*;
import net.sourceforge.symba.mapping.hibernatejaxb2.DatabaseObjectHelper;
import net.sourceforge.symba.versioning.Endurant;
import net.sourceforge.symba.service.SymbaEntityService;
import net.sourceforge.fuge.service.EntityService;
import net.sourceforge.fuge.ServiceLocator;
import net.sourceforge.fuge.common.audit.Audit;
import net.sourceforge.fuge.common.audit.AuditAction;

/**
 * This file is part of SyMBA.
 * SyMBA is covered under the GNU Lesser General Public License (LGPL).
 * Copyright (C) 2007 jointly held by Allyson Lister, Olly Shaw, and their employers.
 * To view the full licensing information for this software and ALL other files contained
 * in this distribution, please see LICENSE.txt
 * <p/>
 * $LastChangedDate: 2008-08-06 05:43:17 -0400 (Wed, 06 Aug 2008) $
 * $LastChangedRevision: 194 $
 * $Author: allysonlister $
 * $HeadURL: http://symba.svn.sourceforge.net/svnroot/symba/tags/release-8.09/symba-querying/src/test/java/net/sourceforge/fuge/common/protocol/GenericSoftwareDaoTest.java $
 */
public class GenericSoftwareDaoTest {

    @Test(groups = { "software", "hibernate" })
    public void getByNameAndVersionSimpleTest() {
        SymbaEntityService ses = ServiceLocator.instance().getSymbaEntityService();
        GregorianCalendar gc = new GregorianCalendar();
        gc.set(2000, Calendar.MARCH, 4);
        GenericSoftware one = writeGenericSoftware("getByNameAndVersionSimpleTest1", gc.getTime());
        GenericSoftware three = writeGenericSoftware("getByNameAndVersionSimpleTest3", gc.getTime());
        GenericSoftware retrievedOne = ses.getGenericSoftwareByNameAndVersion(one.getName(), one.getVersion());
        GenericSoftware retrievedThree = ses.getGenericSoftwareByNameAndVersion(three.getName(), three.getVersion());
        assert (retrievedOne != null) : "The GenericSoftware " + one.getName() + " has not been retrieved by the query";
        assert (retrievedOne.getName().equals(one.getName())) : "The GenericSoftware " + one.getName() + " name does not match the retrieved" + "name of " + retrievedOne.getName();
        assert (retrievedOne.getName().equals(one.getName())) : "The GenericSoftware " + one.getVersion() + " version does not match the retrieved" + "version of " + retrievedOne.getVersion();
        assert (retrievedThree != null) : "The GenericSoftware " + three.getName() + " has not been retrieved by the query";
        assert (retrievedThree.getName().equals(three.getName())) : "The GenericSoftware " + three.getName() + " name does not match the retrieved" + "name of " + retrievedThree.getName();
        assert (retrievedThree.getName().equals(three.getName())) : "The GenericSoftware " + three.getVersion() + " version does not match the retrieved" + "version of " + retrievedThree.getVersion();
    }

    @Test(groups = { "software", "hibernate" })
    public void getByNameAndVersionComplexTest() {
        SymbaEntityService ses = ServiceLocator.instance().getSymbaEntityService();
        GregorianCalendar gc = new GregorianCalendar();
        gc.set(2000, Calendar.MARCH, 4);
        GenericSoftware one = writeGenericSoftware("getByNameAndVersionComplexTest1", gc.getTime());
        gc.set(2005, Calendar.MARCH, 4);
        GenericSoftware two = writeGenericSoftware("getByNameAndVersionComplexTest2", gc.getTime(), one.getEndurant());
        GenericSoftware retrievedOne = ses.getGenericSoftwareByNameAndVersion(one.getName(), one.getVersion());
        GenericSoftware retrievedTwo = ses.getGenericSoftwareByNameAndVersion(two.getName(), two.getVersion());
        assert (retrievedOne == null) : "The GenericSoftware " + one.getName() + " should not be retrieved by the query as it is an " + "older version";
        assert (retrievedTwo != null) : "The GenericSoftware " + two.getName() + " has not been retrieved by the query";
        assert (retrievedTwo.getName().equals(two.getName())) : "The GenericSoftware " + two.getName() + " name does not match the retrieved" + "name of " + retrievedTwo.getName();
        assert (retrievedTwo.getName().equals(two.getName())) : "The GenericSoftware " + two.getVersion() + " version does not match the retrieved" + "version of " + retrievedTwo.getVersion();
    }

    private GenericSoftware writeGenericSoftware(String name, Date time) {
        return writeGenericSoftware(name, time, DatabaseObjectHelper.getOrLoadEndurant(null, null));
    }

    private GenericSoftware writeGenericSoftware(String name, Date time, Endurant endurant) {
        EntityService es = ServiceLocator.instance().getEntityService();
        GenericSoftware genericSoftware = (GenericSoftware) es.createIdentifiable(name + ":GenericSoftware:" + String.valueOf(Math.random() * 10000), name, "net.sourceforge.fuge.common.protocol.GenericSoftware");
        genericSoftware.setEndurant(endurant);
        genericSoftware.setVersion("some Version " + String.valueOf(Math.random() * 100));
        Set<Audit> audits = new HashSet<Audit>();
        Audit audit = (Audit) es.createDescribable("net.sourceforge.fuge.common.audit.Audit");
        audit.setDate(new java.sql.Timestamp(time.getTime()));
        audit.setAction(AuditAction.creation);
        audit = (Audit) es.create("net.sourceforge.fuge.common.audit.Audit", audit);
        audits.add(audit);
        genericSoftware.setAuditTrail(audits);
        genericSoftware = (GenericSoftware) es.create("net.sourceforge.fuge.common.protocol.GenericSoftware", genericSoftware);
        return genericSoftware;
    }
}
