package org.bs.sm.outputers.dot;

import org.bs.sm.StateMachine;
import org.bs.sm.outputers.SMDOM;
import org.bs.sm.outputers.XMLContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.*;
import java.util.List;

/**
 * Support for  Graphviz library
 * <p/>
 * User: Boaz Nahum
 * Date: Feb 20, 2006
 * Time: 7:40:38 PM
 */
public class SMDot {

    private SMDot() {
    }

    public static void createDotFile(@NotNull SMGraphvizEngine graphvizEngine, StateMachine sm, File file) {
        final SMDotContext dotContext = new SMDotContext(graphvizEngine);
        XMLContext xmlContext = new XMLContext(dotContext);
        Document smDoc = SMDOM.createSMDocument(xmlContext, sm);
        Element rootElement = smDoc.getDocumentElement();
        PrintWriter pw;
        try {
            pw = new PrintWriter(new FileWriter(file));
        } catch (IOException e) {
            throw new RuntimeException("Can't create file:" + file, e);
        }
        try {
            dotContext.convertDotElement(rootElement, pw, 0);
        } finally {
            pw.close();
        }
    }

    /**
     * @param sm
     * @param imageFileName - if null then a temporary file is created
     * @param dotFileName
     * @param graphvizEngine if null then default is used.see {@link SMGraphvizConfig#getDefaultConfiguration()}
     * @throws RuntimeException if graphviz is not available
     */
    public static void createImageFile(@Nullable SMGraphvizEngine graphvizEngine, StateMachine sm, String imageFileName, String dotFileName) {
        graphvizEngine = getDefault(graphvizEngine);
        File imageFile = new File(imageFileName);
        createImageFile(graphvizEngine, sm, imageFile, dotFileName == null ? null : new File(dotFileName));
    }

    private static SMGraphvizEngine getDefault(SMGraphvizEngine graphvizEngine) {
        if (graphvizEngine == null) {
            return SMGraphvizConfig.getDefaultConfiguration();
        } else {
            return graphvizEngine;
        }
    }

    /**
     * Create jpg file
     *
     * @param sm
     * @param dotFile if null then temporary file is created and deleted at end of processing
     * @param imageFile
     * @throws RuntimeException if graphviz is not available
     */
    public static void createImageFile(SMGraphvizEngine graphvizEngine, StateMachine sm, File imageFile, File dotFile) {
        graphvizEngine = getDefault(graphvizEngine);
        boolean tempDotFile;
        if (dotFile == null) {
            tempDotFile = true;
            try {
                dotFile = File.createTempFile("state_machine_dot_file", ".dot");
            } catch (IOException e) {
                throw new RuntimeException("Can't create temp file", e);
            }
        } else {
            tempDotFile = false;
        }
        createDotFile(graphvizEngine, sm, dotFile);
        try {
            runDotCommand(graphvizEngine, dotFile, imageFile.getAbsolutePath());
        } finally {
            if (tempDotFile) {
                dotFile.delete();
            }
        }
        if (SMGraphvizConfig.LOG) {
            System.out.println("Image file is:" + imageFile.getAbsoluteFile());
        }
    }

    private static void runDotCommand(SMGraphvizEngine configuration, File dotFile, String imageFileName) {
        String dotCommand = getDotExeCommand(configuration);
        if (dotCommand == null) {
            throw new RuntimeException("Graphviz is not available");
        }
        String arg1 = dotFile.getAbsolutePath();
        String arg2 = "-Tjpg";
        String arg3 = "-o";
        String arg4 = new File(imageFileName).getAbsolutePath();
        ProcessBuilder pb = new ProcessBuilder(dotCommand, "-Grankdir=" + configuration.getRankDir(), arg1, arg2, arg3, arg4);
        if (SMGraphvizConfig.LOG) {
            System.out.println("Dot file is:" + dotFile.getAbsoluteFile());
            System.out.println("param: USE_DOT_VS_FDP=" + configuration.getUseEngine());
            System.out.println("param: USE_DUMMY_FOR_NODE_CLUSTER=" + !configuration.isSupportingNodeToClusterArrow());
            System.out.println("param: USE_DUMMY_FOR_CLUSTER_CLUSTER=" + !configuration.isSupportingClusterToClusterArrow());
            System.out.println("Running command:");
            List<String> command = pb.command();
            for (String c : command) {
                System.out.println("  " + c);
            }
        }
        ProcessHelper.runProcess(pb);
        if (SMGraphvizConfig.LOG) {
            System.out.println("--------------------------------------------------------");
        }
    }

    /**
     * Return null if dot graphviz is not available
     */
    @Nullable
    private static String getDotExeCommand(SMGraphvizEngine configuration) {
        if (!SMGraphvizConfig.isGraphvizAvailable()) {
            return null;
        }
        return SMGraphvizConfig.calcExeCommand(configuration.getUseEngine());
    }

    public static boolean isAvailable() {
        return SMGraphvizConfig.isGraphvizAvailable();
    }
}
