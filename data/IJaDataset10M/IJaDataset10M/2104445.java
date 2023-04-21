package com.liferay.portal.sharepoint;

import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.User;
import com.liferay.portal.sharepoint.methods.Method;
import com.liferay.portal.sharepoint.methods.MethodFactory;
import com.liferay.portal.util.WebKeys;
import com.liferay.util.servlet.ServletResponseUtil;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <a href="SharepointServlet.java.html"><b><i>View Source</i></b></a>
 *
 * @author Bruno Farache
 *
 */
public class SharepointServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            String uri = request.getRequestURI();
            if (uri.equals("/_vti_inf.html")) {
                vtiInfHtml(response);
            }
        } catch (Exception e) {
            _log.error(e, e);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            String uri = request.getRequestURI();
            if (uri.equals("/_vti_bin/shtml.dll/_vti_rpc") || uri.equals("/sharepoint/_vti_bin/_vti_aut/author.dll")) {
                User user = (User) request.getSession().getAttribute(WebKeys.USER);
                SharepointRequest sharepointRequest = new SharepointRequest(request, response, user);
                addParams(request, sharepointRequest);
                Method method = MethodFactory.create(sharepointRequest);
                String rootPath = method.getRootPath(sharepointRequest);
                sharepointRequest.setRootPath(rootPath);
                SharepointStorage storage = SharepointUtil.getStorage(rootPath);
                sharepointRequest.setSharepointStorage(storage);
                method.process(sharepointRequest);
            }
        } catch (SharepointException se) {
            _log.error(se, se);
        }
    }

    protected void addParams(HttpServletRequest request, SharepointRequest sharepointRequest) throws SharepointException {
        String contentType = request.getContentType();
        if (!contentType.equals(SharepointUtil.VEERMER_URLENCODED)) {
            return;
        }
        try {
            InputStream is = new BufferedInputStream(request.getInputStream());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(baos);
            int c = is.read();
            while (c != -1) {
                bos.write(c);
                if (c == CharPool.NEW_LINE) {
                    break;
                }
                c = is.read();
            }
            bos.flush();
            bos.close();
            String url = new String(baos.toByteArray());
            String[] params = url.split(StringPool.AMPERSAND);
            for (String param : params) {
                String[] kvp = param.split(StringPool.EQUAL);
                String key = HttpUtil.decodeURL(kvp[0]);
                String value = StringPool.BLANK;
                if (kvp.length > 1) {
                    value = HttpUtil.decodeURL(kvp[1]);
                }
                sharepointRequest.addParam(key, value);
            }
            c = is.read();
            baos = new ByteArrayOutputStream();
            bos = new BufferedOutputStream(baos);
            while (c != -1) {
                bos.write(c);
                c = is.read();
            }
            is.close();
            bos.flush();
            bos.close();
            sharepointRequest.setBytes(baos.toByteArray());
        } catch (Exception e) {
            throw new SharepointException(e);
        }
    }

    protected void vtiInfHtml(HttpServletResponse response) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("<!-- FrontPage Configuration Information");
        sb.append(StringPool.NEW_LINE);
        sb.append(" FPVersion=\"6.0.2.9999\"");
        sb.append(StringPool.NEW_LINE);
        sb.append("FPShtmlScriptUrl=\"_vti_bin/shtml.dll/_vti_rpc\"");
        sb.append(StringPool.NEW_LINE);
        sb.append("FPAuthorScriptUrl=\"_vti_bin/_vti_aut/author.dll\"");
        sb.append(StringPool.NEW_LINE);
        sb.append("FPAdminScriptUrl=\"_vti_bin/_vti_adm/admin.dll\"");
        sb.append(StringPool.NEW_LINE);
        sb.append("TPScriptUrl=\"_vti_bin/owssvr.dll\"");
        sb.append(StringPool.NEW_LINE);
        sb.append("-->");
        ServletResponseUtil.write(response, sb.toString());
    }

    private static Log _log = LogFactory.getLog(SharepointServlet.class);
}
