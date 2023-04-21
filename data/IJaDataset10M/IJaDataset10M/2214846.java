package app;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import org.jchmlib.ChmFile;
import org.jchmlib.ChmUnitInfo;

public class ChmLibTest {

    public static void main(String[] argv) throws IOException {
        ChmFile chmFile = null;
        ChmUnitInfo ui = null;
        ByteBuffer buffer = null;
        PrintStream out = null;
        int gotLen;
        if (argv.length < 3) {
            System.out.println("Usage: ChmLibTest <chmfile> <filename> <destfile>");
            return;
        }
        chmFile = new ChmFile(argv[0]);
        System.out.println("Resolving " + argv[1]);
        ui = chmFile.resolveObject(argv[1]);
        if (ui != null) {
            System.out.println("Extracting to " + argv[2]);
            buffer = chmFile.retrieveObject(ui, 0, ui.length);
            if (buffer == null) {
                System.out.println("    extract failed on " + ui.path);
                return;
            }
            out = new PrintStream(argv[2]);
            if (out == null) {
                System.out.println("    create failed on " + ui.path);
            }
            gotLen = buffer.limit() - buffer.position();
            byte[] bytes = new byte[gotLen];
            buffer.mark();
            while (buffer.hasRemaining()) {
                buffer.get(bytes);
                out.write(bytes, 0, gotLen);
            }
            buffer.reset();
            out.close();
            System.out.println("   finished");
        } else {
            System.out.println("   failed");
        }
    }
}
