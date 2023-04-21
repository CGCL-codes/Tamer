package edu.toronto.cs.ome.eclipse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.osgi.framework.Bundle;

public class CreateExamplesAction implements IWorkbenchWindowActionDelegate {

    private IWorkbenchWindow window;

    public CreateExamplesAction() {
    }

    public void run(IAction action) {
        IWorkspace w = ResourcesPlugin.getWorkspace();
        IProject project = w.getRoot().getProject("OpenOME Examples");
        try {
            Bundle bundle = Plugin.getPlugin().getBundle();
            URL url = Platform.find(bundle, new Path("samples/manifest.txt"));
            url = Platform.resolve(url);
            Object content = url.getContent();
            InputStream ins = (InputStream) content;
            int count = ins.available();
            BufferedReader in = new BufferedReader(new InputStreamReader(ins));
            char[] cbuf = new char[count];
            in.read(cbuf);
            in.close();
            String[] lines = new String(cbuf).split("\n");
            List manifest = new ArrayList();
            for (int i = 0; i < lines.length; i++) {
                String string = lines[i];
                string = string.trim();
                if (string.length() > 0) manifest.add(string);
            }
            if (!project.exists()) project.create(null);
            project.open(null);
            String errors = "";
            for (Iterator iter = manifest.iterator(); iter.hasNext(); ) {
                try {
                    String name = (String) iter.next();
                    String[] words = name.split("/");
                    String pathName = "";
                    IFolder folder = null;
                    for (int i = 0; i < words.length - 1; i++) {
                        String string = words[i];
                        pathName = pathName + string + "/";
                        folder = project.getFolder(pathName);
                        if (!folder.exists()) folder.create(IResource.NONE, true, null);
                    }
                    IPath path = new Path("samples/" + name);
                    InputStream stream = Plugin.getPlugin().openStream(path);
                    IFile file;
                    if (folder == null) file = project.getFile(name); else file = folder.getFile(words[words.length - 1]);
                    if (!file.exists()) {
                        file.create(stream, false, null);
                    }
                    stream.close();
                } catch (CoreException x) {
                    errors += x.toString() + "\n";
                    x.printStackTrace();
                } catch (IOException x) {
                    errors += x.toString() + "\n";
                    x.printStackTrace();
                }
            }
            if (errors.length() > 0) {
                MessageDialog.openInformation(window.getShell(), "OpenOME", "Errors creating the Examples project: " + errors);
            }
        } catch (CoreException x) {
            MessageDialog.openInformation(window.getShell(), "OpenOME", "Unable to create the Examples project due to " + x);
        } catch (IOException x) {
            MessageDialog.openInformation(window.getShell(), "OpenOME", "Unable to create the Examples project due to " + x);
        } catch (Exception x) {
            x.printStackTrace();
        }
        System.out.println("Example project created");
    }

    public void selectionChanged(IAction action, ISelection selection) {
    }

    public void dispose() {
    }

    public void init(IWorkbenchWindow window) {
        this.window = window;
    }
}
