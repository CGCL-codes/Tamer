package uk.org.sgj.OHCApparatus.Records;

import uk.org.sgj.OHCApparatus.*;

public final class OHCWebpagePanel extends OHCBasicPanel {

    public OHCWebpagePanel(OHCCurrentRecordPanel o) {
        super(o);
        recordDisplayData.addDisplay(FUN.authorName);
        recordDisplayData.addDisplay(FUN.articleTitle);
        recordDisplayData.addDisplay(FUN.subsArticleTitle);
        recordDisplayData.addDisplay(FUN.url);
        recordDisplayData.addDisplay(FUN.dateCited);
        panelTitle = "Web page";
        panelTip = "Creates a record for a Web Page";
        panelKey = FUN.keyWebpage;
        panelIndex = FUN.idxWebpage;
        OHCWebpageRecord.recordIndex = panelIndex;
        OHCWebpageRecord.setRecordTypeString(panelTitle);
    }

    @Override
    protected DerivePanel getDerivePanel() {
        return (null);
    }
}
