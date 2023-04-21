package cn.myapps.core.dynaform.form.ejb;

import java.io.Serializable;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.base.ejb.IDesignTimeProcess;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.user.action.WebUser;

/**
 * @author nicholas
 */
public abstract class IncludedElement implements Serializable {

    public IncludeField field;

    /**
	 * Form模版的动态组件内容结合Document中的ITEM存放的值,返回字符串为重定义后的生成html文本
	 * 
	 * @param doc
	 *            Document(文档对象)
	 * @param runner
	 *            动态脚本执行器
	 * @param webUser
	 *            webUser
	 * @return 重定义后的生成html文本
	 * @throws Exception
	 */
    public abstract String toHtmlTxt(Document doc, IRunner runner, WebUser webUser) throws Exception;

    /**
	 * 用于为手机平台XML串生成
	 * 
	 * @param doc
	 *            Document(文档对象)
	 * @param runner
	 *            动态脚本执行器
	 * @param webUser
	 *            webUser
	 * @return 字符串内容为重定义后的手机平台XML串
	 * @throws Exception
	 */
    public abstract String toXMLTxt(Document doc, IRunner runner, WebUser webUser) throws Exception;

    /**
	 * Form模版的动态组件内容结合Document中的ITEM存放的值,返回字符串为重定义后的打印html文本
	 * 
	 * @param doc
	 *            Document(文档对象)
	 * @param runner
	 *            动态脚本执行器
	 * @param webUser
	 *            webUser
	 * @return 重定义后的打印html的动态组件标签及语法
	 * @throws Exception
	 */
    public abstract String toPrintHtmlTxt(Document doc, IRunner runner, WebUser webUser) throws Exception;

    /**
	 * 构造方法
	 * 
	 * @param field
	 */
    public IncludedElement(IncludeField field) {
        this.field = field;
    }

    /**
	 * 获取组件执行值脚本后的返回值
	 * 
	 * @param runner
	 *            动态脚本执行器
	 * @return 执行值脚本后的返回值
	 */
    protected String getValueObjectId(IRunner runner) {
        String valueScript = field.getValueScript();
        try {
            if (valueScript != null && valueScript.trim().length() > 0) {
                Object result = runner.run(field.getScriptLable("ValueScript"), valueScript);
                if (result instanceof String) {
                    return (String) result;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    protected ValueObject getValueObject(IRunner runner) throws Exception {
        return getProcess().doView(getValueObjectId(runner));
    }

    protected abstract IDesignTimeProcess getProcess() throws Exception;
}
