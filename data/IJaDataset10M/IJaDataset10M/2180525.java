package passreminder;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;

public class Util {

    private static ImageRegistry image_registry;

    public static ImageRegistry getImageRegistry() {
        if (image_registry == null) {
            image_registry = new ImageRegistry();
            image_registry.put("default", ImageDescriptor.createFromURL(Util.class.getResource("default.gif")));
            image_registry.put("search", ImageDescriptor.createFromURL(Util.class.getResource("action/search.gif")));
            image_registry.put("timeover", ImageDescriptor.createFromURL(Util.class.getResource("timeover.gif")));
            image_registry.put("foldero", ImageDescriptor.createFromURL(Util.class.getResource("foldero.gif")));
            image_registry.put("logo", ImageDescriptor.createFromURL(Util.class.getResource("logo.gif")));
            image_registry.put("warning", ImageDescriptor.createFromURL(Util.class.getResource("warning.gif")));
            image_registry.put("trash", ImageDescriptor.createFromURL(Util.class.getResource("bin.gif")));
            image_registry.put("OK", ImageDescriptor.createFromURL(Util.class.getResource("action/synced.gif")));
            image_registry.put("KO", ImageDescriptor.createFromURL(Util.class.getResource("action/fatalerror_obj.gif")));
            image_registry.put("data", ImageDescriptor.createFromURL(Util.class.getResource("action/concat.gif")));
        }
        return image_registry;
    }

    public static void copy(FileInputStream source, FileOutputStream dest) throws IOException {
        FileChannel in = null, out = null;
        try {
            in = source.getChannel();
            out = dest.getChannel();
            long size = in.size();
            MappedByteBuffer buf = in.map(FileChannel.MapMode.READ_ONLY, 0, size);
            out.write(buf);
        } finally {
            if (in != null) in.close();
            if (out != null) out.close();
        }
    }
}
