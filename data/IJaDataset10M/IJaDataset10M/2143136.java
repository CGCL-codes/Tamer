package com.whstudio.util.mvc.handler;

import java.util.Iterator;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.whstudio.util.mvc.action.ActionUtil;
import com.whstudio.util.string.WhiteSpaceHandler;
import com.whstudio.util.xml.IDocument;
import com.whstudio.util.xml.XmlUtil;

/**
 * ����Handler�࣬���һЩ���õķ���
 * Map map=ms.getMap();
 * HttpServletRequest request=ms.getRequest();
 * AuthorizeVO userVO = ((AuthorizeVO) request.getSession(true).getAttribute(GlobalParameters.SESSION_INFO));
 * BusinessAcceptBO businessAcceptBO=(BusinessAcceptBO)map.get("businessAcceptBO");
 * @author OnTheRoad
 *
 */
public abstract class AbstractHandler implements IHandler {

    public MessageStruct ms = null;

    protected IDocument docSqlSegments = null;

    /**
	 * �������õ���SQL��������ļ���uri��
	 * �����ļ��Ĵ���������Ϊһ��������Ҫ��ȡSQL���ഴ��һ��������ļ�
	 * Ŀǰ��handler�ĳ�����
	 */
    protected static final String URI_SQL_SEGMENTS = ActionUtil.userDir + "custcontact/group/common/xml/handlers/GrpMemberAcceptHandler.xml";

    /**
     * �������б�ת��Ϊ�ַ���Ϊ�����еļ�ֵ
     * @param argList
     * @return
     */
    public String List2String(List argList) {
        if (argList == null) return "";
        StringBuffer sArg = new StringBuffer();
        for (Iterator iterator = argList.iterator(); iterator.hasNext(); ) {
            String arg = (String) iterator.next();
            sArg.append(arg + "`");
        }
        return sArg.toString();
    }

    /**
	 * ���»�ȡSql�����ļ�
	 */
    protected void recoverSqlFile() {
        this.docSqlSegments = XmlUtil.getXmlFromUri(URI_SQL_SEGMENTS);
    }

    /**
	 * @param method
	 * @return
	 * @throws Exception
	 */
    public String getSqlText(String method) throws Exception {
        if (isXMLDocDeepNull(docSqlSegments)) {
            recoverSqlFile();
        }
        List list = docSqlSegments.getElementsByNameAttr(method);
        if (list.size() == 0) {
            throw new Exception("Sql Segment have not been configued.");
        }
        Element root = (Element) list.get(0);
        Node node = getCDATASectionOf(root);
        if (node == null) {
            throw new Exception("there is no CDATA section in the SQL node.");
        }
        Node dNode = (Node) new WhiteSpaceHandler().bind(node);
        return dNode.getNodeValue();
    }

    public boolean isXMLDocDeepNull(Document doc) {
        return doc == null || doc.getDocumentElement() == null;
    }

    /**
	 * ��ȡһ��Element�ڵ������CDATA
	 * @param e
	 * @return
	 */
    public Node getCDATASectionOf(Element e) {
        NodeList nl = e.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i).getNodeType() == Node.CDATA_SECTION_NODE) {
                return nl.item(i);
            }
        }
        return null;
    }

    public IDocument getDocSqlSegments() {
        return docSqlSegments;
    }
}
