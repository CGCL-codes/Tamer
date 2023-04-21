package gallery.web.controller.pages.types;

import com.multimedia.core.pages.types.APagesType;
import com.multimedia.service.wallpaper.IWallpaperService;
import gallery.service.resolution.IResolutionService;
import gallery.web.controller.pages.filters.WallpaperResolutionFilter;
import gallery.web.controller.pages.submodules.ASubmodule;
import gallery.web.controller.pages.submodules.WallpaperRandomSubmodule;
import gallery.web.controller.pages.submodules.WallpaperTagCloudSubmodule;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public abstract class AWallpaperType extends APagesType {

    protected IWallpaperService wallpaperService;

    protected IResolutionService resolutionService;

    protected AWallpaperType() {
    }

    @Override
    public void init() {
        super.init();
        StringBuilder sb = new StringBuilder();
        common.utils.MiscUtils.checkNotNull(wallpaperService, "wallpaperService", sb);
        common.utils.MiscUtils.checkNotNull(resolutionService, "resolutionService", sb);
        if (sb.length() > 0) {
            throw new NullPointerException(sb.toString());
        }
    }

    /**
	 * get submodules that will be added to request if an appropriate module is found
	 * @return map of submodules
	 */
    protected Map<String, ASubmodule> getCommonSubmodules(HttpServletRequest request) {
        HashMap<String, ASubmodule> hs = new HashMap<String, ASubmodule>();
        hs.put(gallery.web.controller.pages.types.WallpaperRandomType.TYPE, new WallpaperRandomSubmodule(wallpaperService));
        hs.put(gallery.web.controller.pages.types.WallpaperTagCloudType.TYPE, new WallpaperTagCloudSubmodule(wallpaperService, request));
        return hs;
    }

    @Override
    public UrlBean execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        UrlBean url = new UrlBean();
        url.setSubmodules(getCommonSubmodules(request));
        WallpaperResolutionFilter sub = new WallpaperResolutionFilter(resolutionService, wallpaperService, request);
        request.setAttribute(sub.getFilterName(), sub);
        sub.enableFilters();
        process(request, response, url);
        sub.disableFilters();
        return url;
    }

    /**
	 * override this method to put some logic before disabling filters
	 * @param request
	 * @param response
	 * @return
	 */
    public abstract void process(HttpServletRequest request, HttpServletResponse response, UrlBean url) throws Exception;

    public void setWallpaperService(IWallpaperService service) {
        this.wallpaperService = service;
    }

    public void setResolutionService(IResolutionService service) {
        this.resolutionService = service;
    }
}
