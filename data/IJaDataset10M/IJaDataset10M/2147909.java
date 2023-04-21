package ytex.weka;

import java.io.IOException;
import ytex.kernel.KernelContextHolder;

public class ExportGramMatrix {

    /**
	 * @param args
	 * @throws IOException
	 */
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("usage: java ytex.kernel.ExportGramMatrix <property file>");
        } else {
            GramMatrixExporter g = (GramMatrixExporter) KernelContextHolder.getApplicationContext().getBean("gramMatrixExporter");
            g.exportGramMatrix(args[0]);
        }
    }
}
