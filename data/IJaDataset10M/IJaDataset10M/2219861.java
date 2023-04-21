package bufferings.ktr.wjr.server.logic;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import java.lang.reflect.Method;
import java.util.List;
import org.junit.Test;
import bufferings.ktr.wjr.server.fortest.ForTestJUnit3;
import bufferings.ktr.wjr.server.fortest.ForTestJUnit3Inherit;
import bufferings.ktr.wjr.server.logic.WjrJUnit3StoreLoader;
import bufferings.ktr.wjr.shared.model.WjrClassItem;
import bufferings.ktr.wjr.shared.model.WjrMethodItem;
import bufferings.ktr.wjr.shared.model.WjrStore;

public class WjrJUnit3StoreLoaderTest {

    private WjrJUnit3StoreLoader storeLoader = new WjrJUnit3StoreLoader();

    @Test
    public void checkAndStoreTestClass_CanStoreTest_WithCommonClass_JUnit3() {
        WjrStore store = new WjrStore();
        storeLoader.checkAndStoreTestClass(store, ForTestJUnit3.class);
        List<WjrClassItem> classItems = store.getClassItems();
        assertThat(classItems.size(), is(1));
        assertThat(classItems.get(0).getClassName(), is(ForTestJUnit3.class.getName()));
        List<WjrMethodItem> methodItems = store.getMethodItems(ForTestJUnit3.class.getName());
        assertThat(methodItems.size(), is(4));
        assertThat(methodItems.get(0).getMethodName(), is("testErrorMethod"));
        assertThat(methodItems.get(1).getMethodName(), is("testFailureMethod"));
        assertThat(methodItems.get(2).getMethodName(), is("testOverQuotaExceptionMethod"));
        assertThat(methodItems.get(3).getMethodName(), is("testSuccessMethod"));
    }

    @Test
    public void checkAndStoreTestClass_CanStoreTest_WithInheritClass_JUnit3() {
        WjrStore store = new WjrStore();
        storeLoader.checkAndStoreTestClass(store, ForTestJUnit3Inherit.class);
        List<WjrClassItem> classItems = store.getClassItems();
        assertThat(classItems.size(), is(1));
        assertThat(classItems.get(0).getClassName(), is(ForTestJUnit3Inherit.class.getName()));
        List<WjrMethodItem> methodItems = store.getMethodItems(ForTestJUnit3Inherit.class.getName());
        assertThat(methodItems.size(), is(4));
        assertThat(methodItems.get(0).getMethodName(), is("testErrorMethod"));
        assertThat(methodItems.get(1).getMethodName(), is("testFailureMethod"));
        assertThat(methodItems.get(2).getMethodName(), is("testOverQuotaExceptionMethod"));
        assertThat(methodItems.get(3).getMethodName(), is("testSuccessMethod"));
    }

    @Test
    public void checkAndStoreTestClass_CanStoreTest_WithInnerStaticClass_JUnit3() {
        WjrStore store = new WjrStore();
        storeLoader.checkAndStoreTestClass(store, ForTestJUnit3.ForTestJUnit3InnerStatic.class);
        List<WjrClassItem> classItems = store.getClassItems();
        assertThat(classItems.size(), is(1));
        assertThat(classItems.get(0).getClassName(), is(ForTestJUnit3.ForTestJUnit3InnerStatic.class.getName()));
        List<WjrMethodItem> methodItems = store.getMethodItems(ForTestJUnit3.ForTestJUnit3InnerStatic.class.getName());
        assertThat(methodItems.size(), is(1));
        assertThat(methodItems.get(0).getMethodName(), is("testSuccessMethod"));
    }

    @Test
    public void isJUnit3TargetClass_WillReturnTrue_WithCommonClass() throws Exception {
        assertThat(storeLoader.isJUnit3TargetClass(ForTestJUnit3.class), is(true));
    }

    @Test
    public void isJUnit3TargetClass_WillReturnTrue_WithInnerStaticClass() throws Exception {
        assertThat(storeLoader.isJUnit3TargetClass(ForTestJUnit3.ForTestJUnit3InnerStatic.class), is(true));
    }

    @Test
    public void isJUnit3TargetClass_WillReturnFlase_WithInnerNotStaticClass() throws Exception {
        assertThat(storeLoader.isJUnit3TargetClass(ForTestJUnit3.ForTestJUnit3InnerNotStatic.class), is(false));
    }

    @Test
    public void isJUnit3TargetMethod_WillReturnTrue_WithTestPrefix() throws Exception {
        Method m = ForTestJUnit3.class.getMethod("testSuccessMethod");
        assertThat(storeLoader.isJUnit3TargetMethod(m), is(true));
    }

    @Test
    public void isJUnit3TargetMethod_WillReturnFalse_WithoutTestPrefix() throws Exception {
        Method m = ForTestJUnit3.class.getMethod("ignoreMethod");
        assertThat(storeLoader.isJUnit3TargetMethod(m), is(false));
    }

    @Test
    public void isJUnit3TargetMethod_WillReturnFalse_WithReturnValue() throws Exception {
        Method m = ForTestJUnit3.class.getMethod("testHasReturnMethod");
        assertThat(storeLoader.isJUnit3TargetMethod(m), is(false));
    }

    @Test
    public void isJUnit3TargetMethod_WillReturnFalse_WithParameters() throws Exception {
        Method m = ForTestJUnit3.class.getMethod("testHasParamMethod", int.class);
        assertThat(storeLoader.isJUnit3TargetMethod(m), is(false));
    }
}
