package afk.biglog;

import java.io.*;
import java.nio.charset.*;
import java.util.logging.*;
import afk.biglog.gui.*;
import afk.lib.*;

public class BigLogViewApplication {

    final Logger log = Logger.getLogger(BigLogViewApplication.class.getName());

    private final MainViewAdapter gui;

    public BigLogViewApplication(final MainViewAdapter logView) {
        gui = logView;
    }

    public void init() {
    }

    public void openFile(final File file) {
        log.info("opening " + file);
        try {
            final byte[] bs = FileFunctions.read(file, 0, 3);
            final String encoding = selectEncodingFromBom(bs);
            final int offset = (encoding.startsWith("UTF-16")) ? 2 : (encoding.startsWith("UTF-8") ? 3 : 0);
            final FileViewAdapter guiAdapter = new FileViewAdapter(file, encoding, offset, 100, gui, new BlockFilter(), gui.getRootPane());
            guiAdapter.startIndex();
            gui.addFileViewer(guiAdapter);
        } catch (final FileNotFoundException e) {
            gui.addError("While opening file " + file, e);
        }
    }

    private String selectEncodingFromBom(final byte[] bs) {
        if (bs == null) return Charset.defaultCharset().name();
        final String encoding = FileFunctions.bomToEncoding(bs);
        if (encoding != null) return encoding;
        return Charset.defaultCharset().name();
    }
}
