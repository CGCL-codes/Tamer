package as.ide.ui.editors;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.eclipse.jface.text.reconciler.MonoReconciler;

public class ASReconciler extends MonoReconciler {

    private ASReconcilerStrategy strategy;

    public ASReconciler(ASReconcilerStrategy strategy) {
        super(strategy, false);
        this.strategy = strategy;
    }

    @Override
    public IReconcilingStrategy getReconcilingStrategy(String contentType) {
        return this.strategy;
    }

    @Override
    public void install(ITextViewer textViewer) {
        super.install(textViewer);
    }

    @Override
    public void uninstall() {
        super.uninstall();
    }
}
