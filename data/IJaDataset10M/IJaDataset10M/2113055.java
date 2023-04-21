package net.sf.joafip.store.service.proxy;

import net.sf.joafip.store.entity.classinfo.ClassInfo;
import net.sf.joafip.store.service.classinfo.ClassInfoException;
import net.sf.joafip.store.service.objectfortest.Bob1;
import net.sf.joafip.store.service.objectfortest.Bob2;
import net.sf.joafip.store.service.objectfortest.Bob3;
import net.sf.joafip.store.service.objectio.ObjectIOClassNotFoundException;
import net.sf.joafip.store.service.objectio.ObjectIODataCorruptedException;
import net.sf.joafip.store.service.objectio.ObjectIODataRecordNotFoundException;
import net.sf.joafip.store.service.objectio.ObjectIOException;
import net.sf.joafip.store.service.objectio.ObjectIOInvalidClassException;
import net.sf.joafip.store.service.objectio.ObjectIONotSerializableException;
import net.sf.joafip.store.service.objectio.manager.IObjectIOManagerForProxyObjectIO;

public class Bob1EnhancedSrc extends Bob1 implements IProxyCallBack, Cloneable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 7325201018024556824L;

    private IProxyCallBackToImplement proxyCallBack;

    @Override
    public void setProxyCallBack$JOAFIP$(final IProxyCallBackToImplement proxyCallBack) {
        this.proxyCallBack = proxyCallBack;
    }

    @Override
    public void forceLoad$JOAFIP$() throws ObjectIOException, ObjectIODataRecordNotFoundException, ObjectIOInvalidClassException, ObjectIOClassNotFoundException, ObjectIODataCorruptedException, ObjectIONotSerializableException {
        proxyCallBack.forceLoad$JOAFIP$();
    }

    @Override
    public void unload$JOAFIP$() throws ObjectIOException, ObjectIOInvalidClassException {
        proxyCallBack.unload$JOAFIP$();
    }

    @Override
    public ClassInfo getProxiedClass$JOAFIP$() {
        return proxyCallBack.getProxiedClass$JOAFIP$();
    }

    @Override
    public void intercept$JOAFIP$() {
        proxyCallBack.intercept$JOAFIP$();
    }

    @Override
    public void methodEnd$JOAFIP$() {
        proxyCallBack.methodEnd$JOAFIP$();
    }

    @Override
    public boolean isLoaded$JOAFIP$() {
        return proxyCallBack.isLoaded$JOAFIP$();
    }

    @Override
    public void setLoaded$JOAFIP$(final boolean loaded) throws ObjectIOException {
        proxyCallBack.setLoaded$JOAFIP$(loaded);
    }

    @Override
    public long getMyFileAccessSessionIdentifier$JOAFIP$() {
        return proxyCallBack.getMyFileAccessSessionIdentifier$JOAFIP$();
    }

    @Override
    public boolean equals(final Object obj) {
        intercept$JOAFIP$();
        final boolean equals = super.equals(obj);
        methodEnd$JOAFIP$();
        return equals;
    }

    @Override
    public Bob2 getBob2() {
        intercept$JOAFIP$();
        final Bob2 bob22 = super.getBob2();
        methodEnd$JOAFIP$();
        return bob22;
    }

    @Override
    public Bob3 getBob3() {
        intercept$JOAFIP$();
        final Bob3 bob3 = super.getBob3();
        methodEnd$JOAFIP$();
        return bob3;
    }

    @Override
    public int getVal() {
        intercept$JOAFIP$();
        final int val = super.getVal();
        methodEnd$JOAFIP$();
        return val;
    }

    @Override
    public int hashCode() {
        intercept$JOAFIP$();
        final int hashCode = super.hashCode();
        methodEnd$JOAFIP$();
        return hashCode;
    }

    @Override
    public void setBob2(final Bob2 bob2) {
        intercept$JOAFIP$();
        super.setBob2(bob2);
        methodEnd$JOAFIP$();
    }

    @Override
    public void setBob3(final Bob3 bob3) {
        intercept$JOAFIP$();
        super.setBob3(bob3);
        methodEnd$JOAFIP$();
    }

    @Override
    public void setVal(final int val) {
        intercept$JOAFIP$();
        super.setVal(val);
        methodEnd$JOAFIP$();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        intercept$JOAFIP$();
        final Object clone = super.clone();
        methodEnd$JOAFIP$();
        return clone;
    }

    public Class<?> getClassDelegateJOAFIP() throws ClassInfoException {
        return getProxiedClass$JOAFIP$().getObjectClass();
    }

    @Override
    public long getLong(final Bob2 bob2) {
        intercept$JOAFIP$();
        final long long1 = super.getLong(bob2);
        methodEnd$JOAFIP$();
        return long1;
    }

    @Override
    public IObjectIOManagerForProxyObjectIO getObjectIOManager$JOAFIP$() {
        return null;
    }

    @Override
    public void setLoading$JOAFIP$(final boolean loading) {
    }

    @Override
    public IProxyCallBackToImplement getProxyCallBack$JOAFIP$() {
        return null;
    }

    @Override
    public void hasBeenLoaded$JOAFIP$(final ClassInfo classInfo) {
    }
}
