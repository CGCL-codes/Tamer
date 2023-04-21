package de.uniwue.dltk.textmarker.internal.ui.templates;

import org.eclipse.dltk.ui.templates.ScriptTemplateAccess;
import org.eclipse.dltk.ui.templates.ScriptTemplatePreferencePage;
import org.eclipse.dltk.ui.text.ScriptSourceViewerConfiguration;
import org.eclipse.jface.text.IDocument;
import de.uniwue.dltk.textmarker.internal.ui.TextMarkerUI;
import de.uniwue.dltk.textmarker.internal.ui.text.SimpleTextMarkerSourceViewerConfiguration;
import de.uniwue.dltk.textmarker.internal.ui.text.TextMarkerTextTools;
import de.uniwue.dltk.textmarker.ui.text.TextMarkerPartitions;

/**
 * TextMarker code templates preference page
 */
public class TextMarkerCodeTemplatesPreferencePage extends ScriptTemplatePreferencePage {

    @Override
    protected ScriptSourceViewerConfiguration createSourceViewerConfiguration() {
        return new SimpleTextMarkerSourceViewerConfiguration(getTextTools().getColorManager(), getPreferenceStore(), null, TextMarkerPartitions.TM_PARTITIONING, false);
    }

    @Override
    protected void setDocumentParticioner(IDocument document) {
        getTextTools().setupDocumentPartitioner(document, TextMarkerPartitions.TM_PARTITIONING);
    }

    @Override
    protected void setPreferenceStore() {
        setPreferenceStore(TextMarkerUI.getDefault().getPreferenceStore());
    }

    @Override
    protected ScriptTemplateAccess getTemplateAccess() {
        return TextMarkerTemplateAccess.getInstance();
    }

    private TextMarkerTextTools getTextTools() {
        return TextMarkerUI.getDefault().getTextTools();
    }
}
