package net.simpleframework.my.space;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import net.simpleframework.util.IoUtils;
import net.simpleframework.web.page.IMultipartFile;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.HandleException;
import net.simpleframework.web.page.component.ui.swfupload.AbstractSwfUploadHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class SapceUploadHandle extends AbstractSwfUploadHandle {

    @Override
    public void upload(final ComponentParameter compParameter, final IMultipartFile multipartFile, final HashMap<String, Object> json) {
        try {
            IoUtils.copyFile(multipartFile.getInputStream(), new File(MySpaceUtils.getUploadDir(compParameter.getSession()).getAbsolutePath() + File.separator + multipartFile.getOriginalFilename()));
        } catch (final IOException e) {
            throw HandleException.wrapException(e);
        }
    }
}
