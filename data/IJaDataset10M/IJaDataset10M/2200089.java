package cn.vlabs.clb.ui.flex.folder;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForm;
import cn.vlabs.clb.domain.folder.FolderManager;
import cn.vlabs.clb.domain.folder.exception.FolderNotEmpty;
import cn.vlabs.clb.domain.folder.exception.PathNotFound;
import cn.vlabs.clb.ui.flex.FlexAction;
import cn.vlabs.clb.ui.flex.PathDecoder;
import cn.vlabs.clb.ui.flex.XMLMessages;

public class RmdirAction extends FlexAction {

    @Override
    protected String exec(ActionForm form, HttpServletRequest request) {
        FolderManager manager = FolderManager.getInstance();
        String path = request.getParameter("path");
        boolean force = "true".equalsIgnoreCase(request.getParameter("force"));
        String xml = "";
        if (path == null) {
            xml = XMLMessages.fail("ȱ��·��.");
        } else if (path.equals("/")) xml = XMLMessages.fail("��������ֵ�����\'/\'�ַ�.");
        try {
            path = PathDecoder.decode(path);
            manager.delete(path, force);
            xml = XMLMessages.success();
        } catch (FolderNotEmpty e) {
            xml = XMLMessages.fail("Ŀ¼�ǿ�");
        } catch (PathNotFound e) {
            xml = XMLMessages.fail(XMLMessages.Tip_PathNotFound);
        } catch (Exception e) {
            xml = XMLMessages.fail(XMLMessages.Tip_NoPermission);
        }
        return xml;
    }
}
