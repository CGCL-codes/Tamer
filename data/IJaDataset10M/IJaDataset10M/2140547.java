package net.sf.poormans.view.context.object;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import net.sf.poormans.Constants;
import net.sf.poormans.exception.FatalException;
import net.sf.poormans.livecycle.PojoHelper;
import net.sf.poormans.model.InstanceUtil;
import net.sf.poormans.model.domain.PoPathInfo;
import net.sf.poormans.model.domain.pojo.APoormansObject;
import net.sf.poormans.model.domain.pojo.Image;
import net.sf.poormans.model.domain.pojo.Level;
import net.sf.poormans.tool.PathTool;
import net.sf.poormans.tool.file.FileTool;
import net.sf.poormans.view.ViewMode;
import net.sf.poormans.view.context.IContextObjectCommon;
import net.sf.poormans.view.context.IContextObjectNeedPojoHelper;
import net.sf.poormans.view.context.IContextObjectNeedViewMode;
import net.sf.poormans.view.renderer.RenderData;
import net.sf.poormans.wysisygeditor.CKResourceFileType;
import net.sf.poormans.wysisygeditor.CKResourceTool;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Context object for building query strings for links. <br>
 * <pre>$linktool.addParameter('id', '1').addParameter('name', 'foo')</pre> generates: id=1&name=foo <br>
 * <br>
 * There is an additional method to get a link to a picture without the image rendering.
 * 
 * @version $Id: LinkTool.java 2115 2011-06-26 11:06:41Z th-schwarz $
 * @author <a href="mailto:th-schwarz@users.sourceforge.net">Thilo Schwarz</a>
 */
@Component("linktool")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LinkTool implements IContextObjectCommon, IContextObjectNeedPojoHelper, IContextObjectNeedViewMode {

    private String linkTo = null;

    private Map<String, String> params = new HashMap<String, String>();

    private boolean isExportView;

    private PojoHelper pojoHelper;

    private APoormansObject<?> po;

    @Autowired
    private RenderData renderData;

    @Override
    public void setPojoHelper(final PojoHelper pojoHelper) {
        this.pojoHelper = pojoHelper;
        this.po = (APoormansObject<?>) this.pojoHelper.getRenderable();
    }

    @Override
    public void setViewMode(final ViewMode viewMode) {
        isExportView = viewMode.equals(ViewMode.EXPORT);
    }

    public LinkTool addParameter(String key, String value) {
        this.params.put(key, value);
        return this;
    }

    public LinkTool addParameter(String key, Serializable value) {
        addParameter(key, value.toString());
        return this;
    }

    public LinkTool setEditView() {
        clear();
        setLinkTo("/".concat(Constants.LINK_IDENTICATOR_EDIT));
        return this;
    }

    public LinkTool setSave() {
        clear();
        setLinkTo("/".concat(Constants.LINK_IDENTICATOR_SAVE));
        return this;
    }

    /**
	 * Using pictures without the normal image procedere.
	 */
    public LinkTool getPicture(String pictureRelativeToImageDirectory) {
        clear();
        Level level;
        if (InstanceUtil.isImage(po)) level = ((Image) po).getParent().getParent(); else level = (Level) po.getParent();
        if (isExportView) {
            String link = PathTool.getURLRelativePathToRoot(level).concat(CKResourceTool.getDir(CKResourceFileType.IMAGE)).concat("/").concat(pictureRelativeToImageDirectory);
            setLinkTo(link);
            File srcDir = PoPathInfo.getSiteResourceDirectory(pojoHelper.getSite(), CKResourceFileType.IMAGE);
            File srcFile = new File(srcDir, pictureRelativeToImageDirectory);
            renderData.addCKResource(srcFile);
            File dstDir = PoPathInfo.getSiteExportResourceDirectory(pojoHelper.getSite(), CKResourceFileType.IMAGE);
            File dstFile = new File(dstDir, pictureRelativeToImageDirectory);
            if (!dstFile.getParentFile().getAbsoluteFile().exists()) dstFile.getParentFile().getAbsoluteFile().mkdirs();
            try {
                FileTool.copyFile(srcFile, dstFile);
            } catch (IOException e) {
                throw new FatalException("While copying: " + e.getMessage(), e);
            }
        } else {
            String link = PathTool.getURLFromFile(String.format("%s/%s/%s", Constants.LINK_IDENTICATOR_SITE_RESOURCE, CKResourceTool.getDir(CKResourceFileType.IMAGE), pictureRelativeToImageDirectory));
            setLinkTo(link);
        }
        return this;
    }

    @Override
    public String toString() {
        StringBuilder link = new StringBuilder();
        String paramStr = null;
        if (this.linkTo == null) return "error_in_linktool-destination_not_set";
        if (!this.params.isEmpty()) {
            String amp = "&amp;";
            link.append("?");
            for (String key : this.params.keySet()) {
                link.append(key);
                link.append("=");
                link.append(this.params.get(key));
                link.append(amp);
            }
            paramStr = link.substring(0, link.length() - amp.length());
        }
        String linkString = this.linkTo.concat(StringUtils.defaultString(paramStr));
        clear();
        this.linkTo = "";
        return linkString;
    }

    private void setLinkTo(String linkTo) {
        clear();
        this.linkTo = linkTo;
    }

    private void clear() {
        this.params.clear();
    }
}
