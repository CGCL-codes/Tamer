package com.tresys.slide.plugin.editors;

import java.util.HashMap;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import com.tresys.slide.utility.policyxmlparser.Module;

public class ModuleEditorHyperlink implements IHyperlink {

    private Module m_Module;

    private int m_nLineNum;

    private String m_sSelectedWord;

    private IRegion m_region;

    public ModuleEditorHyperlink(Module i_Module, int i_nLine, String i_sWord, IRegion i_rgn) {
        super();
        m_Module = i_Module;
        m_nLineNum = i_nLine;
        m_sSelectedWord = i_sWord;
        m_region = i_rgn;
    }

    public IRegion getHyperlinkRegion() {
        return m_region;
    }

    public String getTypeLabel() {
        return null;
    }

    public String getHyperlinkText() {
        return m_sSelectedWord;
    }

    public void open() {
        if (m_Module == null) return;
        try {
            if (!m_Module.getIFFile().exists()) return;
            ModuleEditorInput input2 = new ModuleEditorInput(m_Module, ModuleEditorInput.IF_EDITOR_INDEX);
            IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
            ModuleEditor editor = (ModuleEditor) IDE.openEditor(page, input2, ModuleEditor.ID);
            if (editor != null) {
                HashMap map = new HashMap();
                map.put(IMarker.LINE_NUMBER, new Integer(m_nLineNum));
                IMarker marker = m_Module.getIFFile().createMarker(IMarker.TEXT);
                marker.setAttributes(map);
                editor.displayMarker(marker);
                marker.delete();
            }
        } catch (CoreException ce) {
            ce.printStackTrace();
        }
    }
}
