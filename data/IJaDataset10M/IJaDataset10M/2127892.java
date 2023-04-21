package net.dromard.filesynchronizer.modules;

import static net.dromard.filesynchronizer.treenode.FileSynchronizationStatusTreeNode.SYNCHRONIZATION_FILES_EQUALS;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.dromard.filesynchronizer.treenode.FileSynchronizationStatusTreeNode;
import net.dromard.filesynchronizer.treenode.FileSynchronizerTodoTaskTreeNode;

/**
 * Un-comment implementation so as to activate this module
 *
 * @author Gabriel Dromard
 */
public class UncommentSourceModule {

    /**
     * [/] Used when the source file contains lines that begin with //.
     */
    public static int SYNCHRONIZATION_CONTAINS_COMMENTS;

    /**
     * [/] Occurred if we are parsing the root folder to be synchronize.
     */
    public static int TODO_UNCOMMENT;

    public UncommentSourceModule() {
        SYNCHRONIZATION_CONTAINS_COMMENTS = FileSynchronizationStatusTreeNode.SYNCHRONIZATION_STATUS.length;
        TODO_UNCOMMENT = FileSynchronizerTodoTaskTreeNode.TODO_TASKS.length;
    }

    public char[] retrieveTodoTasks() {
        return new char[] { '/' };
    }

    public String[] retrieveTodoTaskNames() {
        return new String[] { "Uncomment" };
    }

    public void addImageTypes(final Map<Integer, String> imageTypeByTodoTask) {
        imageTypeByTodoTask.put(TODO_UNCOMMENT, "UNCOMMENT");
    }

    public int synchronize(final int currentStatus, final File source, final File destination) {
        if (currentStatus == SYNCHRONIZATION_FILES_EQUALS) {
            if (check(source)) {
                return SYNCHRONIZATION_CONTAINS_COMMENTS;
            }
        }
        return currentStatus;
    }

    public void doneTask(final int alreadyDoneTask, final int todoTask, final File source, final File destination) {
        if (source.isFile() && source.getName().endsWith(".java")) {
            BufferedReader reader = null;
            BufferedWriter writer = null;
            try {
                File temp = File.createTempFile("Synchronizer", source.getName());
                writer = (new BufferedWriter(new FileWriter(temp)));
                reader = (new BufferedReader(new FileReader(source)));
                boolean classFound = false;
                int nbDeletedLines = 0;
                int nbLines = 0;
                String line = null;
                while ((line = reader.readLine()) != null) {
                    if (!classFound && line.contains(" class ")) {
                        classFound = true;
                    }
                    if (!classFound || !line.matches("[ \t]*//[ \t]{2,}?.*")) {
                        writer.write(line);
                        writer.append('\n');
                    } else {
                        ++nbDeletedLines;
                    }
                    ++nbLines;
                }
                writer.close();
                reader.close();
                if (source.delete()) {
                    temp.renameTo(source);
                    System.out.println("Remove [" + nbDeletedLines + "/" + nbLines + "] comments for " + source.getAbsoluteFile());
                }
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    if (writer != null) {
                        writer.close();
                    }
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public int calculateTodoTask(final int synchronizationStatus) {
        if (synchronizationStatus == SYNCHRONIZATION_CONTAINS_COMMENTS) {
            return TODO_UNCOMMENT;
        }
        throw new RuntimeException("I do not know this synchronisation status: " + synchronizationStatus + ", please use knows() methods before.");
    }

    public boolean knowsSynchronizationStatus(final int synchronizationStatus) {
        return (synchronizationStatus == SYNCHRONIZATION_CONTAINS_COMMENTS);
    }

    public boolean knowsTodoTask(final int todoTask) {
        return (todoTask == TODO_UNCOMMENT);
    }

    public List<Integer> getPossibleTasks(final int synchronizationStatus) {
        List<Integer> possibleTask = new ArrayList<Integer>();
        possibleTask.add(TODO_UNCOMMENT);
        return possibleTask;
    }

    private boolean check(final File source) {
        if (source.isFile() && source.getName().endsWith(".java")) {
            BufferedReader reader = null;
            try {
                reader = (new BufferedReader(new FileReader(source)));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    if (line.matches("[ \t]*//[ \t]{2,}?.*")) {
                        return true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}
