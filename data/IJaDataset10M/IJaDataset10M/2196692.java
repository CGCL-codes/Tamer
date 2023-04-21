package org.liris.schemerger.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PartInitException;
import org.liris.schemerger.core.pattern.Chronicle;
import org.liris.schemerger.core.pattern.IChronicle;
import org.liris.schemerger.core.pattern.IEpisode;
import org.liris.schemerger.core.pattern.ITypeDec;
import org.liris.schemerger.ui.SchEmergerPlugin;
import org.liris.schemerger.ui.editors.ChronicleEditor;
import org.liris.schemerger.ui.editors.ChronicleEditorInput;
import org.liris.schemerger.utils.Factory;

public class NewChronicleHandler extends AbstractHandler implements IHandler {

    public Object execute(ExecutionEvent event) throws ExecutionException {
        IEpisode<ITypeDec> episode = Factory.getInstance().createEpisodeOfTypedDec(ITypeDec.class);
        IChronicle<ITypeDec> c = new Chronicle<ITypeDec>(episode);
        IEditorInput input = new ChronicleEditorInput(c);
        try {
            SchEmergerPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(input, ChronicleEditor.ID);
        } catch (PartInitException e) {
            e.printStackTrace();
        }
        return null;
    }
}
