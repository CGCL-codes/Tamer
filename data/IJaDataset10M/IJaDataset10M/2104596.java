package fr.univartois.cril.xtext2.ui.editor.outline;

import java.util.StringTokenizer;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.eclipse.xtext.ui.editor.model.XtextDocumentUtil;
import org.eclipse.xtext.ui.editor.utils.EditorUtils;
import edu.mit.csail.sdg.alloy4.ConstList;
import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4.Pair;
import edu.mit.csail.sdg.alloy4.SafeList;
import edu.mit.csail.sdg.alloy4compiler.ast.Command;
import edu.mit.csail.sdg.alloy4compiler.ast.Decl;
import edu.mit.csail.sdg.alloy4compiler.ast.Expr;
import edu.mit.csail.sdg.alloy4compiler.ast.Func;
import edu.mit.csail.sdg.alloy4compiler.ast.Module;
import edu.mit.csail.sdg.alloy4compiler.parser.CompModule;
import edu.mit.csail.sdg.alloy4compiler.parser.CompUtil;
import fr.univartois.cril.xtext2.alloyplugin.api.IReporter;
import fr.univartois.cril.xtext2.alloyplugin.core.ALSFile;
import fr.univartois.cril.xtext2.alloyplugin.core.ExecutableCommand;
import fr.univartois.cril.xtext2.alloyplugin.core.Reporter;
import fr.univartois.cril.xtext2.preferences.PreferenceConstants;
import fr.univartois.cril.xtext2.ui.activator.AlsActivator;

public class PredicateOutlineNodeHandler extends AbstractHandler {

    public Object execute(ExecutionEvent event) throws ExecutionException {
        IXtextDocument document;
        XtextEditor editor;
        IResource resource;
        ALSFile file;
        int line, offset;
        String content = null, predName;
        Command command = null;
        editor = EditorUtils.getActiveXtextEditor(event);
        if (editor == null) return null;
        document = XtextDocumentUtil.get(editor);
        if (editor.isSaveOnCloseNeeded()) return null;
        resource = editor.getResource();
        offset = editor.getHighlightRange().getOffset();
        line = getLine(document, offset) + 1;
        if (line == -1) return null;
        try {
            content = editor.getDocument().get(offset, editor.getDocument().getLineLength(line));
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        StringTokenizer tmp = new StringTokenizer(content, " [{");
        predName = tmp.nextToken();
        while ("pred".equals(predName) || "private".equals(predName)) {
            predName = tmp.nextToken();
        }
        if (predName.contains(".")) predName = predName.substring(predName.indexOf(".") + 1);
        file = new ALSFile(resource);
        CompModule world;
        String filename = file.getFilename();
        IReporter reporter = new Reporter(resource);
        world = getWorld(reporter, filename);
        if (world == null) return null;
        IPreferenceStore store = AlsActivator.getInstance().getPreferenceStore();
        int scope = Integer.parseInt(store.getString(PreferenceConstants.DEFAULT_LAUNCH_OPTION));
        try {
            Func f = findPredicate(world, predName);
            if (f.decls.isEmpty()) {
                command = new Command(false, scope, -1, -1, f.call());
            } else {
                int lg = 0;
                for (Decl decl : f.decls) {
                    lg += decl.names.size();
                }
                Expr[] params = new Expr[lg];
                int i = 0;
                for (Decl decl : f.decls) {
                    for (int j = 0; j < decl.names.size(); j++) {
                        params[i++] = decl.expr;
                    }
                }
                command = new Command(false, scope, -1, -1, f.call(params));
            }
        } catch (Err e) {
            e.printStackTrace();
        }
        String cmd1 = "Run " + predName;
        ExecutableCommand ex = new ExecutableCommand(file, command, 0, world, cmd1, scope);
        executeCommand(ex, reporter, null);
        return null;
    }

    private int getLine(IXtextDocument document, int offset) {
        int line = -1;
        try {
            line = document.getLineOfOffset(offset);
        } catch (BadLocationException e) {
            return -1;
        }
        return line;
    }

    private CompModule getWorld(IReporter reporter, String filename) {
        CompModule world;
        try {
            world = CompUtil.parseEverything_fromFile(reporter, null, filename);
        } catch (Err e) {
            return null;
        }
        return world;
    }

    private void executeCommand(ExecutableCommand executableCommand, IReporter reporter, IProgressMonitor monitor) {
        try {
            executableCommand.execute(reporter, monitor);
        } catch (Err e) {
        }
    }

    public Func findPredicate(Module world, String predicate) {
        SafeList<Func> l = world.getAllFunc();
        for (Func c : l) {
            if (c.toString().contains(predicate)) {
                return c;
            }
        }
        return null;
    }

    public Pair<String, Expr> findAssertion(Module world, String assertion) {
        ConstList<Pair<String, Expr>> l = world.getAllAssertions();
        for (Pair<String, Expr> c : l) {
            if (c.a.equals(assertion)) {
                return c;
            }
        }
        return null;
    }
}
