package tr.imports.thoughts;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import org.openide.util.NbBundle;
import org.openide.windows.WindowManager;
import tr.imports.thoughts.prefs.ImportThoughtsPrefs;
import tr.model.Data;
import tr.model.thought.Thought;
import tr.model.topic.Topic;
import tr.model.util.Manager;
import tr.data.FileFilterImpl;

/**
 * Import thoughts from a text file with a format of one thought per line. If a
 * line has a tab then the try to match the text following the last tab with a
 * topic name and set the thought topic. Set the thought description as the text
 * up to the last tab. If a line has no tab or no topic name match then put the
 * entire line as the thought description and set the topic as none.
 *
 * @author Jeremy Moore (jimoore@netspace.net.au)
 */
public class ImportThoughts {

    private static Manager<Thought> thoughts;

    private static Manager<Topic> topics;

    /**
     * Start the import thoughts action.
     * @param data The data instance to import into.
     * @return false if the import is not done.
     */
    public static final boolean doImport(Data data) throws Exception {
        assert (data != null);
        thoughts = data.getThoughtManager();
        topics = data.getTopicManager();
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle(org.openide.util.NbBundle.getMessage(ImportThoughts.class, "dialog.title"));
        String[] extns = { "txt" };
        FileFilter filter = new FileFilterImpl(org.openide.util.NbBundle.getMessage(ImportThoughts.class, "Text_Files"), extns, true);
        chooser.setFileFilter(filter);
        File def = new File(ImportThoughtsPrefs.getDefaultFilePath());
        chooser.ensureFileIsVisible(def);
        chooser.setSelectedFile(def);
        Component p = WindowManager.getDefault().getMainWindow();
        int option = chooser.showOpenDialog(p);
        if (option != JFileChooser.APPROVE_OPTION) return false;
        String path = chooser.getSelectedFile().getPath();
        File file = new File(path);
        if (file.exists() && file.isFile() && file.canRead()) {
            System.out.println("Importing thoughts from " + path);
        } else {
            throw new Exception("The file could not be opened and read.");
        }
        if (file.length() > ImportThoughtsPrefs.getWarningFileSize()) {
            String t = NbBundle.getMessage(ImportThoughts.class, "confirm.title");
            String m = NbBundle.getMessage(ImportThoughts.class, "confirm.message", file.length() / 80);
            Object[] options = { NbBundle.getMessage(ImportThoughts.class, "Yes"), NbBundle.getMessage(ImportThoughts.class, "No") };
            int n = JOptionPane.showOptionDialog(p, m, t, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
            if (n != JOptionPane.YES_OPTION) return false;
        }
        int count = 0;
        BufferedReader input = null;
        try {
            input = getReader(file);
            String line = null;
            while ((line = input.readLine()) != null) {
                if (!line.trim().equals("")) {
                    System.out.println(NbBundle.getMessage(ImportThoughts.class, "Importing_line") + ": " + line);
                    Thought thought = new Thought();
                    thought.setDescription(line);
                    int i = line.lastIndexOf('\t');
                    if (i > -1) {
                        String topicName = line.substring(i + 1);
                        Topic topic = getTopic(topicName);
                        if (topic != null) {
                            thought.setDescription(line.substring(0, i));
                            thought.setTopic(getTopic(topicName));
                        }
                    }
                    thoughts.add(thought);
                    count++;
                }
            }
            ImportThoughtsPrefs.setDefaultFilePath(file.getPath());
            notifySuccess(count);
        } catch (Exception ex) {
            notifyFailure(ex);
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException ex) {
            }
        }
        return true;
    }

    private static Topic getTopic(String name) {
        for (Topic topic : topics.list()) {
            if (topic.getName().equalsIgnoreCase(name)) {
                return topic;
            }
        }
        return null;
    }

    private static BufferedReader getReader(File file) throws Exception {
        String encoding = ImportThoughtsPrefs.getEncoding().trim();
        if (encoding == null || encoding.length() == 0 || !Charset.isSupported(encoding)) {
            return new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        } else {
            return new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));
        }
    }

    private static void notifySuccess(final int count) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                String t = NbBundle.getMessage(ImportThoughts.class, "Import_Thoughts");
                String m = NbBundle.getMessage(getClass(), "imported_n_thoughts", count) + "       ";
                Component p = WindowManager.getDefault().getMainWindow();
                JOptionPane.showMessageDialog(p, m, t, JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    private static void notifyFailure(final Exception ex) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                String t = NbBundle.getMessage(ImportThoughts.class, "Import_Thoughts");
                String m = NbBundle.getMessage(ImportThoughts.class, "error_processing_file") + " \n" + ex.getMessage();
                Component p = WindowManager.getDefault().getMainWindow();
                JOptionPane.showMessageDialog(p, m, t, JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
