package octoshare.server.user;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class FileSendTask {

    private File file;

    private boolean asThumb = false;

    private ByteArrayOutputStream byteStream;

    public ByteArrayOutputStream getByteStream() {
        if (byteStream == null) byteStream = new ByteArrayOutputStream();
        return byteStream;
    }

    public boolean isAsThumb() {
        return asThumb;
    }

    public void setAsThumb(boolean asThumb) {
        this.asThumb = asThumb;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public FileSendTask(File file, boolean asThumb) {
        super();
        this.file = file;
        this.asThumb = asThumb;
    }

    public FileSendTask(File file) {
        super();
        this.file = file;
    }
}
