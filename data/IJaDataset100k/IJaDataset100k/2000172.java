package edutex;

import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import edutex.config.EdutexConfig;
import edutex.latex.DeleteTmpFileExecutor;
import edutex.latex.DviViewerExecutor;
import edutex.latex.LatexCompilationExecutor;
import edutex.pxp.PxpCompilationExecutor;
import edutex.pxp.PxpUtils;
import edutex.util.StringUtilities;

public class LatexToDviBuilder extends Builder {

    private DeleteTmpFileExecutor exec0;

    private LatexCompilationExecutor exec2;

    private DviViewerExecutor exec5;

    private File texFile;

    public LatexToDviBuilder(Console console, EdutexConfig conf) {
        super(console, conf);
        this.exec0 = new DeleteTmpFileExecutor(config);
        this.exec2 = new LatexCompilationExecutor(config);
        this.exec5 = new DviViewerExecutor(config);
    }

    /**
	 * D�finit le fichier tex
	 * @param texFile
	 * @throws Exception
	 */
    @Override
    public final void setFile(File texFile) throws Exception {
        this.exec0.setFile(texFile);
        this.exec2.setFile(texFile);
        this.exec5.setFile(texFile);
        this.texFile = texFile;
    }

    @Override
    public final void build_() throws Exception {
        this.console.clear();
        this.exec0.delete();
        BufferedReader reader = null;
        if (config.isPxp()) {
            ArrayList<File> pxps = PxpUtils.checkPxpInputFile(texFile);
            for (File pxp : pxps) {
                PxpCompilationExecutor e = new PxpCompilationExecutor(config);
                e.setFile(pxp);
                long t10 = System.currentTimeMillis();
                e.compile();
                long t20 = System.currentTimeMillis();
                String sec = StringUtilities.formatDoubleForDisplay((t20 - t10) / 1000.0, 2);
                this.console.appendLine(Console.PXP, NLSMessages.getString("Builder.EndPxpCompilation", pxp.getName(), sec));
            }
        }
        long t1 = System.currentTimeMillis();
        reader = exec2.compile();
        String line = reader.readLine();
        while (line != null) {
            if (config.isTraceAvailable()) this.console.appendLine(Console.COMPILATION, line);
            line = reader.readLine();
        }
        reader.close();
        long t2 = System.currentTimeMillis();
        String sec = StringUtilities.formatDoubleForDisplay((t2 - t1) / 1000.0, 1);
        this.console.appendLine(Console.INFO, NLSMessages.getString("Builder.EndLatexCompilation", texFile.getName(), sec));
        if (!exec2.checkDvi()) {
            this.console.appendLine(Console.ERROR, NLSMessages.getString("Builder.LatexCompilationError"));
            throw new Exception(NLSMessages.getString("Builder.GenericError"));
        }
        this.exec0.delete();
    }

    @Override
    public final void buildAndView_() throws Exception {
        this.build_();
        exec5.view();
    }
}
