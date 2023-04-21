package sk.fiit.mitandao.modules.checkpoint;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;
import sk.fiit.mitandao.core.parameterreader.annotations.FileChooser;
import sk.fiit.mitandao.graph.GraphReader;
import sk.fiit.mitandao.graph.GraphWriter;
import sk.fiit.mitandao.modules.interfaces.OutputModule;
import edu.uci.ics.jung.graph.Graph;

public class CheckpointModule implements OutputModule {

    public CheckpointModule() {
    }

    @FileChooser
    private String filePath = null;

    private String directoryLocation = "checkpoint";

    private static long checkpointTime = 0;

    private static String restoredFile = "";

    private static Logger log = Logger.getLogger(CheckpointModule.class);

    public Graph apply(Graph graph) throws Exception {
        File dir = new File(directoryLocation);
        String fileName = "";
        if (!(graph instanceof Graph)) {
            return (graph);
        }
        if (dir.exists() == false) {
            boolean success = (new File(directoryLocation)).mkdir();
            if (success == false) {
                log.error("Cannot create directory");
                return (graph);
            }
        }
        if (restoredFile != "") {
            String[] files = dir.list();
            Arrays.sort(files);
            boolean remove = false;
            for (int i = 0; i < files.length; i++) {
                if (remove) {
                    File checkpointFile = new File(directoryLocation + "/" + files[i]);
                    if (checkpointFile.exists()) {
                        checkpointFile.delete();
                    }
                }
                if (restoredFile.equals(files[i])) {
                    remove = true;
                }
            }
        }
        fileName = directoryLocation + "/" + this.createCheckpointName();
        if (GraphWriter.writeGraph(graph, fileName) == null) {
            log.error("Cannot create checkpoint. Error during XML building.");
        }
        File newCheckpointFile = new File(fileName);
        if (newCheckpointFile.exists()) {
            newCheckpointFile.deleteOnExit();
        }
        return graph;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getName() {
        return ("Checkpoint writer");
    }

    private String createCheckpointName() {
        Date now = new Date();
        if (checkpointTime >= now.getTime()) {
            return (Long.toString(checkpointTime + 1) + ".xml");
        } else {
            checkpointTime = now.getTime();
            return (Long.toString(checkpointTime) + ".xml");
        }
    }

    public Graph restoreChecpoint(boolean previous) {
        File dir = new File(directoryLocation);
        String file = "";
        if (dir.exists() == false || dir.isDirectory() == false) {
            log.error("Directry " + directoryLocation + " with checkpoints is missing");
            return (null);
        }
        String[] files = dir.list();
        Arrays.sort(files);
        if (files.length == 0) return (null);
        if ((previous == true && restoredFile.equals(files[0])) || (previous == false && restoredFile.equals(files[files.length - 1]))) return (null);
        int i;
        for (i = 0; i < files.length && !restoredFile.equals(files[i]); i++) ;
        if (i < files.length) {
            if (previous && i > 0) {
                file = files[i - 1];
            } else if (i < (files.length - 1)) {
                file = files[i + 1];
            } else {
                return (null);
            }
        } else {
            if (restoredFile == "" && previous) {
                file = files[files.length - 1];
            } else {
                return (null);
            }
        }
        restoredFile = file;
        return (GraphReader.readGraph(directoryLocation + "/" + file));
    }
}
