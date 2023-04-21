package org.xfeep.asura.conf.jaxb;

import static org.xfeep.asura.core.reflect.TypeInfoUtil.handleThrowable;
import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.UnmarshalException;
import org.xfeep.asura.core.annotation.Activate;
import org.xfeep.asura.core.annotation.Service;

@Service
public class JAXBConfServiceImp implements JAXBConfService {

    File configHome;

    protected ConcurrentHashMap<String, Future<Object>> caches = new ConcurrentHashMap<String, Future<Object>>();

    public <C> C getConfig(final String name, final Class<C> configObjectType) {
        while (true) {
            Future<Object> f = caches.get(name);
            if (f == null) {
                Callable<Object> builder = new Callable<Object>() {

                    public Object call() throws Exception {
                        JAXBContext jctx = JAXBContext.newInstance(configObjectType);
                        return jctx.createUnmarshaller().unmarshal(new File(configHome, name + ".xml"));
                    }
                };
                FutureTask<Object> ft = new FutureTask<Object>(builder);
                f = caches.putIfAbsent(name, ft);
                if (f == null) {
                    f = ft;
                    ft.run();
                }
            }
            try {
                return (C) f.get();
            } catch (CancellationException e) {
                caches.remove(name, f);
            } catch (ExecutionException e) {
                Throwable cause = e.getCause();
                if (cause instanceof UnmarshalException) {
                    throw new RuntimeException(cause);
                }
                handleThrowable(e.getCause(), UnmarshalException.class);
                throw new RuntimeException(cause);
            } catch (InterruptedException e) {
            }
        }
    }

    public File getConfigHome() {
        return configHome;
    }

    @Activate
    public void satrt() {
        String homePath = System.getProperty(ASURA_CONFIG_JAXB_HOME);
        if (homePath == null) {
            homePath = System.getProperty("user.dir");
        }
        configHome = new File(homePath);
    }
}
