package net.sf.joafip.service;

import java.util.ListIterator;
import net.sf.joafip.AbstractJoafipTestCase;
import net.sf.joafip.NotStorableClass;
import net.sf.joafip.StorableAccess;
import net.sf.joafip.TestException;
import net.sf.joafip.java.util.support.treelist.TreeListSupport;
import net.sf.joafip.store.entity.objectio.ObjectAndPersistInfo;
import net.sf.joafip.store.service.classinfo.ClassInfoFactory;
import net.sf.joafip.store.service.objectio.ObjectIOClassNotFoundException;
import net.sf.joafip.store.service.objectio.ObjectIODataCorruptedException;
import net.sf.joafip.store.service.objectio.ObjectIODataRecordNotFoundException;
import net.sf.joafip.store.service.objectio.ObjectIOException;
import net.sf.joafip.store.service.objectio.ObjectIOInvalidClassException;
import net.sf.joafip.store.service.objectio.ObjectIONotSerializableException;
import net.sf.joafip.store.service.objectio.manager.IObjectIOManagerForProxyObjectIO;
import net.sf.joafip.store.service.proxy.IProxyCallBackToImplement;
import net.sf.joafip.store.service.proxy.ProxyException;
import net.sf.joafip.store.service.proxy.StaticProxyCallBack;

@NotStorableClass
@StorableAccess
public class TestTreeListIteratorIntercept extends AbstractJoafipTestCase implements IProxyCallBackToImplement {

    private boolean intercepted;

    public TestTreeListIteratorIntercept() throws TestException {
        super();
    }

    public TestTreeListIteratorIntercept(final String name) throws TestException {
        super(name);
    }

    public void test() {
        if (javaAgentTransformerInstalled()) {
            assertTrue("interception must be enabled", JoafipCallBack.isInterceptEnabled$JOAFIP$());
            final TreeListSupport<Integer> list = new TreeListSupport<Integer>(null, false);
            final ListIterator<Integer> iterator = list.iterator();
            StaticProxyCallBack.setProxyCallBack(iterator, this);
            intercepted = false;
            iterator.hasNext();
            assertTrue("must intercept method call", intercepted);
        }
    }

    @SuppressWarnings("PMD")
    @Override
    public void initialize$JOAFIP$(IProxyCallBackToImplement proxyCallBack) throws ProxyException {
    }

    @Override
    @SuppressWarnings("PMD")
    public void intercept$JOAFIP$() {
        intercepted = true;
    }

    @Override
    @SuppressWarnings("PMD")
    public void forceLoad$JOAFIP$() throws ObjectIOException, ObjectIODataRecordNotFoundException, ObjectIOInvalidClassException, ObjectIOClassNotFoundException, ObjectIODataCorruptedException, ObjectIONotSerializableException {
    }

    @Override
    @SuppressWarnings("PMD")
    public ObjectAndPersistInfo getInstance$JOAFIP$() {
        return null;
    }

    @Override
    @SuppressWarnings("PMD")
    public long getMyFileAccessSessionIdentifier$JOAFIP$() {
        return 0;
    }

    @Override
    @SuppressWarnings("PMD")
    public IObjectIOManagerForProxyObjectIO getObjectIOManager$JOAFIP$() {
        return null;
    }

    @Override
    @SuppressWarnings("PMD")
    public boolean isLoaded$JOAFIP$() throws ObjectIOException {
        return false;
    }

    @SuppressWarnings("PMD")
    @Override
    public void constructorEnd$JOAFIP$() {
    }

    @Override
    @SuppressWarnings("PMD")
    public void methodEnd$JOAFIP$() {
    }

    @Override
    @SuppressWarnings("PMD")
    public void setIsLoaded$JOAFIP$() throws ObjectIOException {
    }

    @Override
    @SuppressWarnings("PMD")
    public void setIsLoadedNoSave$JOAFIP$() {
    }

    @Override
    @SuppressWarnings("PMD")
    public void setLoading$JOAFIP$(boolean loading) {
    }

    @Override
    @SuppressWarnings("PMD")
    public void unload$JOAFIP$() throws ObjectIOException, ObjectIOInvalidClassException {
    }

    @Override
    public ClassInfoFactory getClassInfoFactory$JOAFIP$() {
        return null;
    }

    @Override
    public int getInObjectCount$JOAFIP$() {
        return 0;
    }

    @Override
    public void initializeFileAccessSessionIdentifier(final IObjectIOManagerForProxyObjectIO objectIOManager) throws ProxyException {
    }

    @Override
    public void setInstance$JOAFIP$(final ObjectAndPersistInfo objectAndPersistInfo) {
    }

    @Override
    public boolean isAutoSaveEnabled$JOAFIP$() {
        return false;
    }
}
