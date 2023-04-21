package org.antlride.debug.ui;

import org.antlride.bridge.core.BuildMessage;
import org.antlride.bridge.core.Location;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.ui.console.FileLink;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleFactory;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IOConsole;
import org.eclipse.ui.console.IPatternMatchListener;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.PatternMatchEvent;
import org.eclipse.ui.console.TextConsole;

public class ConsoleFactory implements IConsoleFactory {

    private static final String ANTLR_CONSOLE = "ANTLR";

    @Override
    public void openConsole() {
        IConsoleManager manager = ConsolePlugin.getDefault().getConsoleManager();
        manager.showConsoleView(getConsole());
    }

    public static MessageConsole getConsole() {
        IConsoleManager manager = ConsolePlugin.getDefault().getConsoleManager();
        IConsole[] existing = manager.getConsoles();
        MessageConsole console = null;
        for (IConsole element : existing) {
            if (ANTLR_CONSOLE.equals(element.getName())) {
                console = (MessageConsole) element;
            }
        }
        if (console == null) {
            DebugPlugin plugin = DebugPlugin.getInstance();
            console = new MessageConsole(ANTLR_CONSOLE, plugin.getImageDescriptor(Images.CONSOLE));
            console.addPatternMatchListener(new IPatternMatchListener() {

                @Override
                public void matchFound(PatternMatchEvent event) {
                    try {
                        IOConsole console = (IOConsole) event.getSource();
                        IDocument document = console.getDocument();
                        int offset = event.getOffset();
                        int length = event.getLength();
                        String text = document.get(offset, length);
                        BuildMessage message = (BuildMessage) console.getAttribute(text.trim());
                        if (message == null) {
                            return;
                        }
                        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
                        IPath sourcePath = Path.fromOSString(message.getSourcePath());
                        IFile[] files = root.findFilesForLocationURI(sourcePath.toFile().toURI());
                        if (files != null && files.length > 0 && files[0].exists()) {
                            Location location = message.getLocations()[0];
                            FileLink filelink = new FileLink(files[0], "org.antlride.ui.antlrEditor", location.getStart(), location.getEnd() - location.getStart(), location.getLine());
                            console.addHyperlink(filelink, offset, text.indexOf(":"));
                        }
                    } catch (BadLocationException ex) {
                        DebugPlugin.getInstance().error(ex, "Unexpected error");
                    }
                }

                @Override
                public void disconnect() {
                }

                @Override
                public void connect(TextConsole console) {
                }

                @Override
                public String getPattern() {
                    return "(warning|error)\\(\\d+\\).*";
                }

                @Override
                public String getLineQualifier() {
                    return null;
                }

                @Override
                public int getCompilerFlags() {
                    return 0;
                }
            });
            manager.addConsoles(new IConsole[] { console });
        }
        return console;
    }
}
