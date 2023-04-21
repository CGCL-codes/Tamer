package cb.jdynamite.test;

import cb.jdynamite.JDynamiTe;
import gnu.regexp.REException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

abstract class TestCase {

    public abstract void run(Context context) throws REException, IOException;

    void write(Context ctx, String contents, String suffix) throws IOException {
        String filename = ctx.getOutputPath() + '/' + getClass().getName() + suffix;
        PrintStream ps = new PrintStream(new FileOutputStream(filename));
        ps.print(contents);
        ps.close();
    }

    protected JDynamiTe _jDoc = new JDynamiTe();
}
