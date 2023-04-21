package com.multimedia.config.cms.wallpaper;

import common.cms.ICmsConfig;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public interface IWallpaperCmsConfig extends ICmsConfig {

    /** an jsp page where user chooses type of action */
    public String getPrepareUrl();

    /** draws just a collection with , delimiter */
    public String getAjaxListUrl();

    /** jsp for rendering one wallpaper template for ajax responce */
    public String getAjaxWallpaperUrl();

    /** draws an alternative view, only small images are showen */
    public String getViewOnlyImgUrl();

    /** form with ajax for updating wallpapers */
    public String getUpdateAjaxForm();

    /** response that is send by the server on an ajax update/insert action (when form is submited asynchronously) */
    public String getAjaxRespUrl();

    /** view for insert multi where many wallpapers may be provided for insert */
    public String getInsertMultiFormUrl();

    /** view for preparation of uploading */
    public String getUploadPrepareUrl();

    /** view for processing(when some wallpapers are allready uploading) of uploading */
    public String getUploadProcessUrl();

    /** view for result after finding duplicates */
    public String getDuplicatesShowUrl();

    /** view for preparation of optimization (selects id_pages, where to optimize wallpapers) */
    public String getOptimizationPrepareUrl();

    /** upload attribute if exists then upload is allready in progress */
    public String getUploadAttributeName();

    /** renew resolution attribute if exists then renewing resolution operation is allready in progress */
    public String getRenewResolutionAttributeName();
}
