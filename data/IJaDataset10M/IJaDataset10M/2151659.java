package bma.bricks.otter.publish.engine.bridge.util;

import bma.bricks.lang.ExceptionUtil;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateNumberModel;
import freemarker.template.TemplateScalarModel;

public class ModelDataUtil {

    /**
	 * ���obj���ַ������SimpleScalar�������ַ��ֵ�����򷵻ؿ�
	 * 
	 * @param obj
	 * @return
	 */
    public static String getAsString(Object obj) {
        if (obj instanceof TemplateScalarModel) {
            try {
                return ((TemplateScalarModel) obj).getAsString();
            } catch (TemplateModelException e) {
                throw ExceptionUtil.handle(e);
            }
        }
        if (obj instanceof String) {
            return (String) obj;
        }
        return null;
    }

    /**
	 * ��objת��������Ĭ�Ϸ���-1
	 * 
	 * @param obj
	 * @return
	 */
    public static int getAsInt(Object obj) {
        if (obj instanceof TemplateNumberModel) {
            try {
                Number num = ((TemplateNumberModel) obj).getAsNumber();
                return num.intValue();
            } catch (TemplateModelException e) {
                throw ExceptionUtil.handle(e);
            }
        }
        String str = getAsString(obj);
        if (str != null) {
            return Integer.parseInt(str);
        }
        return -1;
    }
}
