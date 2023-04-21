package tpac.lib.DAPSpider;

import tpac.lib.DigitalLibrary.*;

class CustomDataset2Provider extends AliasControlFileArrayProvider {

    public CustomDataset2Provider() {
        super();
    }

    public CustomDataset2Provider(CrawlerFile cf) {
        super(cf);
    }

    public CrawlerGrid newCrawlerGrid(DDXGrid _ddxGrid) {
        return new CustomDataset2Grid(crawlerFile, _ddxGrid);
    }
}
