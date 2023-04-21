package pl.rzarajczyk.utils.system.notification;

import com.google.common.io.ByteStreams;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import pl.rzarajczyk.utils.exec.Exec;
import pl.rzarajczyk.utils.resources.ResourceManager;
import pl.rzarajczyk.utils.resources.ResourceManagerFactory;
import pl.rzarajczyk.utils.system.os.OperatingSystem;
import pl.rzarajczyk.utils.temp.Temp;

/**
 * Uses the system tool /usr/bin/notify-send (available in Ubuntu Linux) to
 * display notifications.
 */
class UbuntuNotification extends AbstractIconizedNotification {

    @Override
    public void show(String title, String text, NotificationType type) throws IOException {
        StringBuilder commandLine = new StringBuilder();
        commandLine.append("/usr/bin/notify-send ");
        commandLine.append("\"" + title + "\"");
        commandLine.append(" ");
        commandLine.append("\"" + text + "\"");
        File file = getIcon(type);
        if (file != null) {
            commandLine.append(" -i ");
            commandLine.append(file.getAbsolutePath());
        }
        Exec.runAndWait(commandLine.toString());
    }

    @Override
    public boolean isSupported() {
        try {
            return OperatingSystem.isUnix() && Exec.runAndWait("ls /usr/bin/notify-send").trim().equals("/usr/bin/notify-send");
        } catch (IOException e) {
            return false;
        }
    }
}
