package com.j2biz.blogunity.web.actions.secure.blog.resources;

import java.io.File;
import org.apache.commons.lang.StringUtils;
import com.j2biz.blogunity.pojo.Blog;
import com.j2biz.blogunity.web.ActionResultFactory;
import com.j2biz.blogunity.web.IActionResult;

/**
 * @author michelson
 * @version $$
 * @since 0.1
 * 
 * 
 */
public class DeleteFileAction extends DeleteResourceAction {

    protected File getFileForResource(String path, String resource) {
        Blog blog = getBlog();
        File dir = new File(blog.getFilesDirectory(), (StringUtils.isNotEmpty(path) ? path : ""));
        return new File(dir, resource);
    }

    protected IActionResult getSuccessRedirect() {
        String params = "?id=" + getBlog().getId() + (StringUtils.isNotEmpty(getPath()) ? "&path=" + getPath() : "");
        return ActionResultFactory.buildRedirect("/listBlogFiles.secureaction" + params);
    }

    protected int getResourceType() {
        return FILE;
    }
}
