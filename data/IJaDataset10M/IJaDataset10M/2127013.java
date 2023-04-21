package xdoclet.modules.borland.bes.ejb;

import java.util.*;
import xjavadoc.*;
import xdoclet.XDocletException;
import xdoclet.modules.ejb.dd.RelationTagsHandler;

/**
 * This tag handler handles tags needed for Web
 *
 * @author               <a href="mailto:mmaczka@cqs.ch">Michal Maczka</a>
 * @created              12 mei 2002
 * @xdoclet.taghandler   namespace="BesEjbRel"
 * @version              $Revision: 1.4 $
 */
public class BorlandRelationTagsHandler extends RelationTagsHandler {

    private static final String BES_RELATION = "bes.relation";

    /**
     * Describe what the method does
     *
     * @return                      Describe the return value
     * @exception XDocletException  Describe the exception
     * @doc.tag                     type="content"
     */
    public String leftTableName() throws XDocletException {
        String leftTableName = currentRelation.getLeftMethod().getDoc().getTagAttributeValue(BES_RELATION, "left-table-name", false);
        return leftTableName;
    }

    /**
     * Describe what the method does
     *
     * @return                      Describe the return value
     * @exception XDocletException  Describe the exception
     * @doc.tag                     type="content"
     */
    public String defaultLeftTableName() throws XDocletException {
        ;
        XClass clazz = currentRelation.getLeft();
        XTag persistenceTag = clazz.getDoc().getTag("ejb.persistence");
        if (persistenceTag != null) {
            String tableName = persistenceTag.getAttributeValue("table-name");
            return tableName;
        }
        return "??";
    }

    /**
     * Describe what the method does
     *
     * @param template              Describe what the parameter does
     * @exception XDocletException  Describe the exception
     * @doc.tag                     type="block"
     */
    public void ifHasLeftTableName(String template) throws XDocletException {
        if (leftTableName() != null) {
            generate(template);
        }
    }

    /**
     * Describe what the method does
     *
     * @param template              Describe what the parameter does
     * @exception XDocletException  Describe the exception
     * @doc.tag                     type="block"
     */
    public void ifDoesntHaveLeftTableName(String template) throws XDocletException {
        if (leftTableName() == null) {
            generate(template);
        }
    }

    /**
     * Describe what the method does
     *
     * @return                      Describe the return value
     * @exception XDocletException  Describe the exception
     * @doc.tag                     type="content"
     */
    public String leftColumnName() throws XDocletException {
        String leftColumnName = currentRelation.getLeftMethod().getDoc().getTagAttributeValue(BES_RELATION, "left-column-name", false);
        return leftColumnName;
    }

    /**
     * Describe what the method does
     *
     * @param template              Describe what the parameter does
     * @exception XDocletException  Describe the exception
     * @doc.tag                     type="block"
     */
    public void ifHasLeftColumnName(String template) throws XDocletException {
        if (leftColumnName() != null) {
            generate(template);
        }
    }

    /**
     * Describe what the method does
     *
     * @param template              Describe what the parameter does
     * @exception XDocletException  Describe the exception
     * @doc.tag                     type="block"
     */
    public void ifDoesntHaveLeftColumnName(String template) throws XDocletException {
        if (leftColumnName() == null) {
            generate(template);
        }
    }

    /**
     * Describe what the method does
     *
     * @return                      Describe the return value
     * @exception XDocletException  Describe the exception
     * @doc.tag                     type="content"
     */
    public String defaultLeftColumnName() throws XDocletException {
        String defaultLeftColumnName = currentRelation.getLeftMethod().getDoc().getTagAttributeValue("ejb.persistence", "column-name", false);
        return defaultLeftColumnName;
    }

    /**
     * Describe what the method does
     *
     * @return                      Describe the return value
     * @exception XDocletException  Describe the exception
     * @doc.tag                     type="content"
     */
    public String rightTableName() throws XDocletException {
        String rightTableName = currentRelation.getLeftMethod().getDoc().getTagAttributeValue(BES_RELATION, "right-table-name", false);
        return rightTableName;
    }

    /**
     * Describe what the method does
     *
     * @return                      Describe the return value
     * @exception XDocletException  Describe the exception
     * @doc.tag                     type="content"
     */
    public String defaultRightTableName() throws XDocletException {
        Collection classes = getXJavaDoc().getSourceClasses();
        for (Iterator i = classes.iterator(); i.hasNext(); ) {
            XClass clazz = (XClass) i.next();
            XTag ejbBeanTag = clazz.getDoc().getTag("ejb.bean");
            if (ejbBeanTag != null) {
                String name = ejbBeanTag.getAttributeValue("name");
                if (name != null && name.equals(rightEJBName())) {
                    XTag persistenceTag = clazz.getDoc().getTag("ejb.persistence");
                    if (persistenceTag != null) {
                        String tableName = persistenceTag.getAttributeValue("table-name");
                        return tableName;
                    } else {
                        throw new XDocletException(name + " Bean found, but it it's not having '@ejb.persistence table-name' tag");
                    }
                }
            }
        }
        throw new XDocletException("Cannot resolve defaultRightTableName. Probably '@ejb.persistence table-name' tag is not existing in right side bean");
    }

    /**
     * Describe what the method does
     *
     * @param template              Describe what the parameter does
     * @exception XDocletException  Describe the exception
     * @doc.tag                     type="block"
     */
    public void ifHasRightTableName(String template) throws XDocletException {
        if (rightTableName() != null) {
            generate(template);
        }
    }

    /**
     * Describe what the method does
     *
     * @param template              Describe what the parameter does
     * @exception XDocletException  Describe the exception
     * @doc.tag                     type="block"
     */
    public void ifDoesntHaveRightTableName(String template) throws XDocletException {
        if (rightTableName() == null) {
            generate(template);
        }
    }

    /**
     * Describe what the method does
     *
     * @return                      Describe the return value
     * @exception XDocletException  Describe the exception
     * @doc.tag                     type="content"
     */
    public String rightColumnName() throws XDocletException {
        String rightColumnName = currentRelation.getLeftMethod().getDoc().getTagAttributeValue(BES_RELATION, "right-column-name", false);
        return rightColumnName;
    }

    /**
     * Default right colum name is pk field of the right side bean..
     *
     * @return                      Describe the return value
     * @exception XDocletException  Describe the exception
     * @doc.tag                     type="content"
     */
    public String defaultRightColumnName() throws XDocletException {
        Collection classes = getXJavaDoc().getSourceClasses();
        for (Iterator i = classes.iterator(); i.hasNext(); ) {
            XClass clazz = (XClass) i.next();
            XTag ejbBeanTag = clazz.getDoc().getTag("ejb.bean");
            if (ejbBeanTag != null) {
                String name = ejbBeanTag.getAttributeValue("name");
                if (name != null && name.equals(rightEJBName())) {
                    XTag persistenceTag = clazz.getDoc().getTag("ejb.bean");
                    if (persistenceTag != null) {
                        String tableName = persistenceTag.getAttributeValue("primkey-field");
                        return tableName;
                    } else {
                        throw new XDocletException("Bean found, but does not have '@ejb.bean primkey-field' tag");
                    }
                }
            }
        }
        throw new XDocletException("Cannot resolve deafultRightColumnNam. Probably '@ejb.bean primkey-field' tag is not existing in right size bean");
    }

    /**
     * Describe what the method does
     *
     * @param template              Describe what the parameter does
     * @exception XDocletException  Describe the exception
     * @doc.tag                     type="block"
     */
    public void ifHasRightColumnName(String template) throws XDocletException {
        if (rightColumnName() != null) {
            generate(template);
        }
    }

    /**
     * Describe what the method does
     *
     * @param template              Describe what the parameter does
     * @exception XDocletException  Describe the exception
     * @doc.tag                     type="block"
     */
    public void ifDoesntHaveRightColumnName(String template) throws XDocletException {
        if (rightColumnName() == null) {
            generate(template);
        }
    }
}
