package com.wideplay.warp.persist.db4o;

import com.google.inject.Guice;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.wideplay.warp.persist.PersistenceService;
import com.wideplay.warp.persist.UnitOfWork;
import com.wideplay.warp.persist.dao.Finder;
import org.testng.annotations.Test;

/**
 * @author Robbie Vanbrabant
 */
@Test(suiteName = "db4o")
public class Db4oEdslTest {

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void dynamicFindersNotSupported() {
        PersistenceService.usingDb4o().across(UnitOfWork.TRANSACTION).addAccessor(Accessor.class).buildModule();
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void dynamicFindersCheckedInStageDevelopment() {
        Module module = PersistenceService.usingDb4o().across(UnitOfWork.TRANSACTION).buildModule();
        Guice.createInjector(module).getInstance(AccessorImpl.class).find();
    }

    @Test()
    public void dynamicFindersNotCheckedInStageProduction() {
        Module module = PersistenceService.usingDb4o().across(UnitOfWork.TRANSACTION).buildModule();
        Guice.createInjector(Stage.PRODUCTION, module).getInstance(AccessorImpl.class).find();
    }

    public interface Accessor {

        @Finder()
        Object find();
    }

    static class AccessorImpl implements Accessor {

        @Finder()
        public Object find() {
            return null;
        }
    }
}
