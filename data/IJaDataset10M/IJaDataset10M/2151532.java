package org.cloudlet.web.service.shared.rpc;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.impl.AbstractRequestContext;
import org.cloudlet.web.service.shared.FileProxyStore;
import org.cloudlet.web.service.shared.KeyUtil;
import org.cloudlet.web.service.shared.LocalStorage;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class BaseReceiver<V> extends Receiver<V> {

    private static final Logger logger = Logger.getLogger(BaseReceiver.class.getName());

    private static final Set<AbstractRequestContext> toFires = new HashSet<AbstractRequestContext>();

    @Inject
    private static LocalStorage storage;

    @Inject
    private static FileProxyStore fileStorage;

    @Inject
    private static KeyUtil keyUtil;

    private String key;

    public void fire() {
        fire(null);
    }

    public void fire(final Receiver<Void> receiver) {
        if (key == null) {
            doFire(receiver);
            return;
        }
        if (!keyUtil.isResource(key)) {
            V value = storage.get(key);
            if (value == null) {
                doFire(receiver);
                return;
            }
            onSuccessAndCached(value);
            return;
        }
        fileStorage.get(key, new Callback<V, Object>() {

            @Override
            public void onFailure(final Object reason) {
                logger.severe("取数据时出错了");
                doFire(receiver);
                return;
            }

            @Override
            public void onSuccess(final V result) {
                if (receiver == null) {
                    doFire(receiver);
                    return;
                }
                onSuccessAndCached(result);
                return;
            }
        });
    }

    @Override
    public void onSuccess(final V response) {
        if (key == null) {
            onSuccessAndCached(response);
            return;
        }
        if (!keyUtil.isResource(key)) {
            storage.put(key, response);
            onSuccessAndCached(response);
            return;
        }
        fileStorage.put(key, (EntityProxy) response, new Callback<Void, Object>() {

            @Override
            public void onFailure(final Object reason) {
                logger.log(Level.SEVERE, "出错");
            }

            @Override
            public void onSuccess(final Void result) {
                onSuccessAndCached(response);
            }
        });
    }

    public abstract void onSuccessAndCached(final V response);

    public abstract Request<V> provideRequest();

    public BaseReceiver<V> setKey(final String key) {
        this.key = key;
        return this;
    }

    public BaseReceiver<V> setKeyForList(final EntityProxyId<?> parentId, final String listKey) {
        this.key = keyUtil.proxyListKey(parentId, listKey);
        return this;
    }

    public BaseReceiver<V> setKeyForList(final String listKey) {
        this.key = keyUtil.listKey(listKey);
        return this;
    }

    ;

    public BaseReceiver<V> setKeyForProxy(final EntityProxyId<?> proxyId) {
        this.key = keyUtil.proxy(proxyId);
        return this;
    }

    public BaseReceiver<V> setKeyForProxy(final EntityProxyId<?> parentId, final String key) {
        this.key = keyUtil.proxyKey(parentId, key);
        return this;
    }

    private void doFire(final Receiver<Void> receiver) {
        final AbstractRequestContext openContextImpl = (AbstractRequestContext) provideRequest().to(this);
        openContextImpl.setFireDisabled(true);
        toFires.add(openContextImpl);
        Scheduler.get().scheduleFinally(new ScheduledCommand() {

            @Override
            public void execute() {
                if (toFires.isEmpty()) {
                    return;
                }
                for (AbstractRequestContext ctx : toFires) {
                    if (ctx.isLocked()) {
                        logger.fine("AbstractRequestContext.fire() should have been a no-op");
                        continue;
                    }
                    ctx.setFireDisabled(false);
                    ctx.fire();
                }
                toFires.clear();
            }
        });
        if (receiver != null) {
            openContextImpl.fire(receiver);
        }
    }
}
