package pl.rzarajczyk.utils.system.notification;

import com.google.common.collect.Maps;
import com.google.common.io.ByteStreams;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Map;
import org.apache.commons.logging.Log;
import pl.rzarajczyk.utils.log.LazyLogFactory;
import pl.rzarajczyk.utils.resources.ResourceManager;
import pl.rzarajczyk.utils.resources.ResourceManagerFactory;
import pl.rzarajczyk.utils.temp.Temp;

public abstract class AbstractIconizedNotification implements NotificationWithSupportedMarker {

    private Map<NotificationType, File> images;

    private File dir;

    private Log log = LazyLogFactory.getLog(this.getClass());

    public AbstractIconizedNotification() {
        try {
            dir = Temp.volatileDir(getClass());
            ResourceManager manager = ResourceManagerFactory.getInstance();
            images = Maps.newHashMap();
            images.put(NotificationType.ERROR, copyIcon(manager, dir, "error.png"));
            images.put(NotificationType.INFO, copyIcon(manager, dir, "info.png"));
            images.put(NotificationType.WARNING, copyIcon(manager, dir, "warning.png"));
        } catch (IOException e) {
            log.warn("Could not initialize UbuntuNotification", e);
        }
    }

    private File copyIcon(ResourceManager manager, File dir, String name) throws IOException {
        File file = new File(dir, name);
        String packageName = getClass().getPackage().getName();
        packageName = packageName.replaceAll("\\.", "/");
        URL url = manager.getResourceByPath(packageName + "/" + name);
        InputStream is = manager.read(url);
        try {
            OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
            try {
                ByteStreams.copy(is, os);
            } finally {
                os.close();
            }
        } finally {
            is.close();
        }
        return file;
    }

    protected File getIcon(NotificationType type) {
        if (images == null) {
            return null;
        }
        return images.get(type);
    }
}
