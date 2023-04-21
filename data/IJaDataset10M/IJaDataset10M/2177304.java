package traderjournal;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import traderjournal.views.AccountListView;
import traderjournal.views.ImageView;
import traderjournal.views.ReportView;
import traderjournal.views.TradeDetailView;
import traderjournal.views.TradeListView;
import traderjournal.views.TradeReportListView;

public class ReportPerspective implements IPerspectiveFactory {

    public static final String ID = "TraderJournal.ReportPerspective";

    @Override
    public void createInitialLayout(IPageLayout layout) {
        defineActions(layout);
        String editorArea = layout.getEditorArea();
        layout.setEditorAreaVisible(false);
        layout.addView(TradeReportListView.ID, IPageLayout.LEFT, 0.2f, editorArea);
        layout.addView(ReportView.ID, IPageLayout.RIGHT, 0.2f, TradeListView.ID);
    }

    private void defineActions(IPageLayout layout) {
        layout.addShowViewShortcut(AccountListView.ID);
    }
}
