package webx.struts.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.struts.util.MessageResourcesFactory;
import org.apache.struts.util.PropertyMessageResources;

public class WebxPropertyMessageResources extends PropertyMessageResources {

    public WebxPropertyMessageResources(MessageResourcesFactory factory, String config, boolean returnNull) {
        super(factory, config, returnNull);
        check();
    }

    protected synchronized void loadLocale(String localeKey) {
        if (locales.get(localeKey) != null) {
            return;
        } else {
            locales.put(localeKey, localeKey);
        }
        Properties props = new Properties();
        doLoad(props, localeKey);
        if (props.size() < 1) {
            return;
        }
        putMessagesOperation(props, localeKey);
    }

    /**
	 * 
	 * 写入消息
	 * 
	 * @param props
	 * @param localeKey
	 */
    private void putMessagesOperation(Properties props, String localeKey) {
        synchronized (messages) {
            Iterator names = props.keySet().iterator();
            while (names.hasNext()) {
                String key = (String) names.next();
                if (log.isTraceEnabled()) {
                    log.trace("  Saving message key '" + messageKey(localeKey, key));
                }
                messages.put(messageKey(localeKey, key), props.getProperty(key));
            }
        }
    }

    /**
	 * 
	 * 更新消息
	 * 
	 */
    private void refreshMessagesOperation() {
        String[] filesPath = WebxPropertyMessageResourcesUtil.getFilesPath();
        for (String filePath : filesPath) {
            InputStream is = getClassLoader().getResourceAsStream(filePath);
            try {
                String localeKey = WebxPropertyMessageResourcesUtil.getLocaleKey(filePath);
                Properties props = new Properties();
                props.load(is);
                putMessagesOperation(props, localeKey);
                WebxPropertyMessageResourcesUtil.refreshCache();
                props = null;
            } catch (IOException e) {
                e.printStackTrace();
                log.error("refreshMessagesOperation()", e);
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    log.error("refreshMessagesOperation()", e);
                }
            }
        }
    }

    /**
	 * 
	 * 完成Properties load动作,装入消息,注意struts配置文件中的写法:
	 * 
	 * <pre>
	 * &lt;message-resources parameter=&quot;classpath*:modules/*.properties&quot; factory=&quot;webx.struts.config.WebxPropertyMessageResourcesFactory&quot;&gt;
	 * </pre>
	 * 
	 * @param props
	 * @param localeKey
	 */
    private void doLoad(Properties props, String localeKey) {
        String[] filesPath = WebxPropertyMessageResourcesUtil.getFilesPath(this.config, localeKey);
        for (String filePath : filesPath) {
            InputStream is = getClassLoader().getResourceAsStream(filePath);
            try {
                props.load(is);
                WebxPropertyMessageResourcesUtil.addCache(filePath);
            } catch (IOException e) {
                log.error("doLoad()", e);
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    log.error("doLoad()", e);
                }
            }
        }
    }

    /**
	 * 
	 * 获取当前ClassLoader
	 * 
	 * @return
	 */
    private ClassLoader getClassLoader() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = this.getClass().getClassLoader();
        }
        return classLoader;
    }

    private final long defaultExpire = 20;

    /**
	 * 
	 * 检查文件更新，注意这块只是检查更新文件，如果文件被删除破损无法读取等则不进行更新消息操作
	 * 
	 */
    private void check() {
        Executors.newScheduledThreadPool(1).scheduleWithFixedDelay(this.doCheck(), this.defaultExpire / 2, this.defaultExpire, TimeUnit.SECONDS);
    }

    /**
	 * 
	 * 执行检查动作
	 * 
	 */
    private final Runnable doCheck() {
        return new Runnable() {

            public void run() {
                refreshMessagesOperation();
            }
        };
    }
}
