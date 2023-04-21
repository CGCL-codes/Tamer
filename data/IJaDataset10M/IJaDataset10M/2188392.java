package com.lewisshell.helpyourself.web;

import org.apache.tapestry.*;
import com.lewisshell.helpyourself.*;
import com.lewisshell.helpyourself.psa.*;

public abstract class PhotoBorder extends BaseComponent {

    public static final int BORDER_WIDTH = 3;

    public abstract ScaledImage getThumbnail();

    public abstract ImageRepository.SearchResult getSearchResult();

    public Global getKnownGlobal() {
        return ((Global) this.getPage().getGlobal());
    }

    public Image getImage() {
        return this.getThumbnail() == null ? null : this.getThumbnail().getImage();
    }

    public boolean isImageTopOfStack() {
        return !this.isStackExpansion() && this.getImage().isTopOfStack(this.getKnownGlobal().getCatalog().getFolderRepository());
    }

    public String getStackImageSrc() {
        SharedAssetService service = (SharedAssetService) this.getPage().getEngine().getService(SharedAssetService.NAME);
        return this.getSearchResult() == null || !this.getSearchResult().isStackExpansion() ? service.getLink(this.getPage().getRequestCycle(), SharedAssetGroup.IMAGE_STACK.createSharedAsset(SharedAssetSizeScheme.MEDIUM, SharedAssetGroup.STATUS_NORMAL)).getURL() : service.getLink(this.getPage().getRequestCycle(), SharedAssetGroup.IMAGE_STACK_EXPANDED.createSharedAsset(SharedAssetSizeScheme.MEDIUM, SharedAssetGroup.STATUS_NORMAL)).getURL();
    }

    public boolean isStackExpansion() {
        return this.getSearchResult() != null && this.getSearchResult().isStackExpansion();
    }

    public boolean getDisableViewStackLink() {
        return this.isStackExpansion();
    }

    public int getHeight() {
        return this.getThumbnail().getHeight();
    }

    public int height(int x) {
        return this.getHeight() + x + BORDER_WIDTH * 2;
    }

    public int getWidth() {
        return this.getThumbnail().getWidth();
    }

    public int width(int x) {
        return this.getWidth() + x + BORDER_WIDTH * 2;
    }

    public String widthAndHeight(int x, int y) {
        return "width:" + this.width(x) + "px; height:" + this.height(y) + "px;";
    }

    public String widthAndHeight(int x) {
        return this.widthAndHeight(x, x);
    }

    public String getSrc() {
        return ((ThumbnailService) this.getPage().getEngine().getService(ThumbnailService.NAME)).getLink(this.getPage().getRequestCycle(), this.getThumbnail()).getURL();
    }

    public boolean isPhotoThumbnail() {
        return this.getThumbnail() instanceof Thumbnail;
    }

    public String getPhotoTitle() {
        if (this.isPhotoThumbnail()) {
            String caption = this.getImage().getCaption();
            HitCounter.Info hitInfo = this.getKnownGlobal().getCatalog().getHitCounter().hitInfoForImage(this.getImage());
            String hitsWord = hitInfo.getHits() == 1 ? " hit, " : " hits, ";
            String downloadsWord = hitInfo.getDownloads() == 1 ? " download" : " downloads";
            String hitTitle = hitInfo.getHits() + hitsWord + hitInfo.getDownloads() + downloadsWord;
            return caption == null ? hitTitle : caption + " (" + hitTitle + ")";
        } else {
            String caption = this.getImage() == null ? null : this.getImage().getCaption();
            return caption == null ? "" : caption;
        }
    }
}
