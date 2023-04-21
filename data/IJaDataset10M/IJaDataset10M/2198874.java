package de.mpiwg.vspace.metamodel.sitemap.helper.share;

import de.mpiwg.vspace.metamodel.transformed.Image;

public interface IVSpaceElementSitemapHelper {

    public final String SITEMAP_IMAGE_PREFIX = "SitemapIMG_";

    public Image getSitemapImage();

    public String getSitemapText();
}
