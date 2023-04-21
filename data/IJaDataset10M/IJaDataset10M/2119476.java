package org.hlj.web.ajax;

import java.io.OutputStream;
import java.util.List;
import org.hlj.commons.conversion.ConversionUtil;
import org.hlj.commons.xml.Document;
import org.hlj.commons.xml.Element;
import org.hlj.commons.xml.factory.XMLFactory;
import org.hlj.param.bean.EntityBean;
import org.hlj.web.xml.ExportXML;

/**
 * Ajax使用的XML前台输出
 * @author WD
 * @since JDK5
 * @version 1.0 2009-10-30
 */
public final class AjaxExportXML extends ExportXML {

    private static final String XML_ENTITY = "entity";

    private static final String XML_KEY = "key";

    private static final String XML_VALUE = "value";

    /**
	 * 根据列表组成Map
	 * @param lists 列表
	 */
    public static final <E extends EntityBean> void exportXML(OutputStream out, List<E>... lists) {
        writeXml(out, getDocumentByLists(lists));
    }

    /**
	 * 根据列表获得文档对象
	 * @param lists 列表
	 * @return 文档
	 */
    private static <E extends EntityBean> Document getDocumentByLists(List<E>... lists) {
        Document doc = createDocument();
        Element root = doc.getRootElement();
        for (int i = 0; i < lists.length; i++) {
            root = getDocumentByList(root, i, lists[i]);
        }
        return doc;
    }

    /**
	 * 根据列表获得文档对象
	 * @param doc 文档
	 * @param num 第几个
	 * @param list 列表
	 * @return 文档
	 */
    private static <E extends EntityBean> Element getDocumentByList(Element root, int num, List<E> list) {
        E entity = null;
        Element element = null;
        int size = list.size();
        String entityKey = XML_ENTITY + num;
        for (int i = 0; i < size; i++) {
            entity = list.get(i);
            element = XMLFactory.createElement(entityKey);
            element.addElement(XML_KEY, ConversionUtil.toString(entity.getId()));
            element.addElement(XML_VALUE, entity.getName());
            root.add(element);
        }
        return root;
    }

    /**
	 * 私有构造，禁止外部实例化
	 */
    private AjaxExportXML() {
    }
}
