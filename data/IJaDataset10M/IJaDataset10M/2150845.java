package traderjournal;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import traderjournal.stats.TradeStatistics;
import traderjournal.views.AccountListView;
import traderjournal.views.ImageView;
import traderjournal.views.TradeDetailView;
import traderjournal.views.TradeListView;
import traderjournal.views.TradeStatisticsView;

public class Perspective implements IPerspectiveFactory {

    public static final String ID = "TraderJournal.tradePerspective";

    public void createInitialLayout(IPageLayout layout) {
        defineActions(layout);
        String editorArea = layout.getEditorArea();
        layout.setEditorAreaVisible(true);
        layout.addView(TradeListView.ID, IPageLayout.LEFT, 0.2f, editorArea);
        layout.addView(ImageView.ID_VIEW, IPageLayout.BOTTOM, 0.7f, editorArea);
        layout.addView(TradeDetailView.ID_VIEW, IPageLayout.BOTTOM, 0.5f, TradeListView.ID);
        layout.addView(TradeStatisticsView.ID, IPageLayout.RIGHT, 0.2f, editorArea);
    }

    private void defineActions(IPageLayout layout) {
        layout.addShowViewShortcut(TradeListView.ID);
        layout.addShowViewShortcut(ImageView.ID_VIEW);
        layout.addShowViewShortcut(TradeDetailView.ID_VIEW);
    }
}
