package org.xulbooster.eclipse.xb.ui.editors.xbl.internal.correction;

import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.wst.sse.ui.internal.correction.IQuickAssistProcessor;
import org.eclipse.wst.sse.ui.internal.correction.IQuickFixProcessor;
import org.eclipse.wst.sse.ui.internal.correction.StructuredCorrectionProcessor;

public class CorrectionProcessorXML extends StructuredCorrectionProcessor {

    protected IQuickAssistProcessor fQuickAssistProcessor;

    protected IQuickFixProcessor fQuickFixProcessor;

    public CorrectionProcessorXML(ISourceViewer sourceViewer) {
        super(sourceViewer);
    }

    protected IQuickAssistProcessor getQuickAssistProcessor() {
        if (fQuickAssistProcessor == null) {
            fQuickAssistProcessor = new QuickAssistProcessorXML();
        }
        return fQuickAssistProcessor;
    }

    protected IQuickFixProcessor getQuickFixProcessor() {
        if (fQuickFixProcessor == null) {
            fQuickFixProcessor = new QuickFixProcessorXML();
        }
        return fQuickFixProcessor;
    }
}
