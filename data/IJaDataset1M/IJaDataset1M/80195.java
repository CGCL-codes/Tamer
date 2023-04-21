package org.jaudio.help.file;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.tree.DefaultMutableTreeNode;
import org.jaudio.help.HelpViewer;

/**
 * SourceFile
 *
 * @author Daniel McEnnis
 */
public class SourceFile extends FileNode {

    File base;

    boolean isDirectory;

    Filter filter = new Filter();

    public SourceFile(File root) {
        super(root);
        base = root;
        isDirectory = base.isDirectory();
    }

    @Override
    public void setPage(HelpViewer viewer) {
        if (isDirectory) {
            String header = "<html><body><ul>";
            String footer = "</ul></body></html>";
            StringBuffer content = new StringBuffer();
            File[] children = base.listFiles();
            if ((children != null) && (children.length > 0)) {
                for (int i = 0; i < children.length; ++i) {
                    content.append("<li>" + children[i].getName() + "</li>");
                }
            }
            viewer.set(header + content.toString() + footer);
        } else {
            FileReader stream;
            try {
                stream = new FileReader(base);
                char[] buffer = new char[10240];
                StringBuffer fileBuffer = new StringBuffer();
                int read = 0;
                while ((read = stream.read(buffer)) > 0) {
                    fileBuffer.append(buffer, 0, read);
                }
                String file = fileBuffer.toString();
                Pattern lt = Pattern.compile("<");
                Matcher match = lt.matcher(file);
                file = match.replaceAll("&lt;");
                Pattern gt = Pattern.compile(">");
                match = gt.matcher(file);
                file = match.replaceAll("&gt;");
                Pattern amp = Pattern.compile("&");
                match = amp.matcher(file);
                file = match.replaceAll("&amp;");
                viewer.set("<html><body><pre>" + file + "</pre></body></html>");
            } catch (FileNotFoundException e) {
                viewer.set("<html><body>ERROR: Help file '" + base.getAbsolutePath() + "' not found</body></html>");
                e.printStackTrace();
            } catch (IOException e) {
                viewer.set("<html><body>ERROR: Help file '" + base.getAbsolutePath() + "' had an IO error</body></html>");
                e.printStackTrace();
            }
        }
    }

    public class Filter implements FileFilter {

        public boolean accept(File pathname) {
            if (pathname.isDirectory()) {
                return true;
            } else if (pathname.getName().endsWith(".java")) {
                return true;
            } else if (pathname.getName().endsWith(".xml")) {
                return true;
            }
            return false;
        }
    }

    @Override
    public void traverseFileSystem(File root, int depth) {
        if (depth < 512) {
            File[] children = root.listFiles();
            if ((children != null) && (children.length != 0)) {
                for (int i = 0; i < children.length; ++i) {
                    if (filter.accept(children[i])) {
                        SourceFile childNode = new SourceFile(children[i]);
                        this.add(childNode);
                        childNode.setParent(this);
                        System.out.println(childNode.toString());
                        childNode.traverseFileSystem(children[i], depth + 1);
                    }
                }
            }
        }
    }
}
