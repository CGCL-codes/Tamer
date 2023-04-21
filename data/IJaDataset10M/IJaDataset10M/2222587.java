package cn.myapps.core.dynaform.form.ejb;

import cn.myapps.constans.Environment;
import cn.myapps.core.dynaform.PermissionType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.Item;
import cn.myapps.core.macro.runner.AbstractRunner;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.user.action.WebUser;

/**
 * 
 * 上传图片的组件,可支持所有格式的图片文件上传
 */
public class ImageUploadField extends AbstractUploadField {

    /**
	 * 
	 */
    private static final long serialVersionUID = 2295552984683189284L;

    /**
	 * @roseuid 41ECB66E012A
	 */
    private static String cssClass = "imageupload-cmd";

    public ImageUploadField() {
    }

    public ValidateMessage validate(IRunner runner, Document doc) throws Exception {
        return null;
    }

    /**
	 * @roseuid 41ECB66E0152
	 */
    public void store() {
    }

    /**
	 * 添加Script
	 * 
	 * @param attachmentName
	 *            附件名
	 * @param uploadList
	 *            上传列表
	 * 
	 * @return 字符串内容为script形式的字符串
	 */
    protected String addScript(String fieldId, String uploadList) {
        StringBuffer script = new StringBuffer();
        script.append("<script language='JavaScript'>");
        script.append("refreshImgList(document.getElementById('" + fieldId + "').value, '" + uploadList + "')");
        script.append("</script>");
        return script.toString();
    }

    /**
	 * 返回模板描述
	 * 
	 * @return java.lang.String
	 * @roseuid 41E7917A033F
	 */
    public String toTemplate() {
        StringBuffer template = new StringBuffer();
        template.append("<input type='text'");
        template.append(" className='" + this.getClass().getName() + "'");
        template.append(" id='" + getId() + "'");
        template.append(" name='" + getName() + "'");
        template.append(" formid='" + getFormid() + "'");
        template.append(" discript='" + getDiscript() + "'");
        template.append(" hiddenScript='" + getHiddenScript() + "'");
        template.append(" hiddenPrintScript='" + getHiddenPrintScript() + "'");
        template.append(">");
        return template.toString();
    }

    public void recalculate(IRunner runner, Document doc, WebUser webUser) throws Exception {
    }

    public Object runValueScript(IRunner runner, Document doc) throws Exception {
        return null;
    }

    /**
	 * 
	 * Form模版的图片上传组件(ImageUploadField)内容结合Document中的ITEM存放的值,返回字符串为重定义后的打印html文本
	 * 
	 * @param doc
	 *            Document
	 * @param runner
	 *            动态脚本执行器
	 * @param params
	 *            参数
	 * @param user
	 *            webuser
	 * 
	 * @see cn.myapps.core.dynaform.form.ejb.ImageUploadField#toHtmlTxt(Document,
	 *      AbstractRunner, WebUser)
	 * @see cn.myapps.core.macro.runner.AbstractRunner#run(String, String)
	 * @return 字符串内容为重定义后的打印html的图片上传组件标签及语法
	 * @throws Exception
	 */
    public String toPrintHtmlTxt(Document doc, IRunner runner, WebUser webUser) throws Exception {
        StringBuffer html = new StringBuffer();
        if (doc != null) {
            int displayType = getPrintDisplayType(doc, runner, webUser);
            if (displayType == PermissionType.HIDDEN) {
                return toHiddenHtml(doc);
            }
            Item item = doc.findItem(this.getName());
            String fileFullName = doc.getItemValueAsString(getName());
            String value = "";
            String url = "";
            if (item != null && item.getValue() != null) {
                value = (String) item.getValue();
                int index = value.indexOf("_");
                value = value.substring(index + 1, value.length());
                if (index != -1) {
                    String webPath = fileFullName.substring(0, index);
                    url = doc.get_params().getContextPath() + webPath;
                }
            }
            html.append("<SPAN style=\"FONT-SIZE: 9pt\">");
            html.append("<img border='0' width='100' height='40' src='" + url + "' />");
            html.append("</SPAN>");
        }
        return html.toString();
    }

    /**
	 * Form模版的图片上传组件(ImageUploadField)内容结合Document中的ITEM存放的值,返回字符串为重定义后的以PDF的形式输出
	 * 
	 * @param doc
	 *            Document(文档对象)
	 * @param runner
	 *            动态脚本执行器
	 * @param webUser
	 *            webUser
	 * @return PDF的格式的HTML的文本
	 */
    public String toPdfHtmlTxt(Document doc, IRunner runner, WebUser webUser) throws Exception {
        StringBuffer html = new StringBuffer();
        if (doc != null) {
            int displayType = getPrintDisplayType(doc, runner, webUser);
            if (displayType == PermissionType.HIDDEN) {
                return toHiddenHtml(doc);
            }
            Item item = doc.findItem(this.getName());
            String fileFullName = doc.getItemValueAsString(getName());
            String value = "";
            String url = "";
            if (item != null && item.getValue() != null) {
                value = (String) item.getValue();
                int index = value.indexOf("_");
                value = value.substring(index + 1, value.length());
                String webPath = fileFullName.substring(0, index);
                url = doc.get_params().getContextPath() + webPath;
                Environment env = Environment.getInstance();
                String filePath = env.getRealPath(webPath);
                html.append("<img border='0' width='100' height='40' src='" + filePath + "' />");
            }
        }
        return html.toString();
    }

    public String toMbXMLText(Document doc, IRunner runner, WebUser webUser) throws Exception {
        return null;
    }

    /**
	 * 获取图片上传的URL的地址，并输出HTML的文本
	 * 
	 * @param doc
	 *            Document(文档对象)
	 * @param runner
	 *            动态脚本执行器
	 * @param webUser
	 *            webUser
	 */
    public String getText(Document doc, IRunner runner, WebUser webUser) throws Exception {
        String fileFullName = doc.getItemValueAsString(getName());
        int index = fileFullName.indexOf("_");
        if (index != -1) {
            String webPath = fileFullName.substring(0, index);
            String fileName = fileFullName.substring(index + 1, fileFullName.length());
            String url = doc.get_params().getContextPath() + webPath;
            String image = "<img border='0' src='" + url + "' width='40' height='20' />";
            image += "<a href='" + url + "' target='bank'>'" + fileName + "'</a>";
            return image;
        }
        return super.getText(doc, runner, webUser);
    }

    protected String getRefreshUploadListFunction() {
        return "refreshImgList";
    }

    public String getLimitType() {
        return "image";
    }
}
