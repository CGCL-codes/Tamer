package websiteschema.vips.extraction;

import websiteschema.analyzer.context.BrowserContext;

/**
 *
 * @author ray
 */
public class BlockExtractorFactory {

    private static final BlockExtractorFactory instance = new BlockExtractorFactory();

    public static BlockExtractorFactory getInstance() {
        return instance;
    }

    public BlockExtractor create(BrowserContext context, String referrer, double pageSize, double threshold) {
        VipsBlockExtractor extractor = new VipsBlockExtractor();
        extractor.setPageSize(pageSize);
        extractor.setContext(context);
        extractor.setReferrer(referrer);
        extractor.setThreshold(threshold);
        extractor.setConfigure(BrowserContext.getConfigure());
        return extractor;
    }
}
