package org.intellij.ibatis.provider;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.codeInsight.lookup.LookupValueFactory;
import org.intellij.ibatis.IbatisManager;
import org.intellij.ibatis.util.IbatisConstants;
import org.intellij.ibatis.dom.sqlMap.Delete;
import org.intellij.ibatis.dom.sqlMap.Insert;
import org.intellij.ibatis.dom.sqlMap.Procedure;
import org.intellij.ibatis.dom.sqlMap.Update;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * flushOnExecute's statement reference provider, case like <flushOnExecute statement="<caret>"/>
 *
 * @author Jacky
 */
public class CacheModelStatementReferenceProvider extends BaseReferenceProvider {

    @NotNull
    public PsiReference[] getReferencesByElement(PsiElement psiElement) {
        XmlAttributeValue xmlAttributeValue = (XmlAttributeValue) psiElement;
        XmlAttributeValuePsiReference psiReference = new XmlAttributeValuePsiReference(xmlAttributeValue) {

            public boolean isSoft() {
                return false;
            }

            @Nullable
            public PsiElement resolve() {
                String statementId = getReferenceId(getElement());
                Map<String, Delete> allDelete = IbatisManager.getInstance().getAllDelete(getElement());
                Delete delete = allDelete.get(statementId);
                if (delete != null) {
                    return delete.getXmlTag();
                }
                Map<String, Update> allUpdate = IbatisManager.getInstance().getAllUpdate(getElement());
                Update update = allUpdate.get(statementId);
                if (update != null) {
                    return update.getXmlTag();
                }
                Map<String, Insert> allInsert = IbatisManager.getInstance().getAllInsert(getElement());
                Insert insert = allInsert.get(statementId);
                if (insert != null) {
                    return insert.getXmlTag();
                }
                Map<String, Procedure> allProcedure = IbatisManager.getInstance().getAllProcedure(getElement());
                Procedure procedure = allProcedure.get(statementId);
                if (procedure != null) {
                    return procedure.getXmlTag();
                }
                return null;
            }

            public Object[] getVariants() {
                List variants = new ArrayList();
                Set<String> deletes = IbatisManager.getInstance().getAllDelete(getElement()).keySet();
                for (String key : deletes) {
                    variants.add(LookupValueFactory.createLookupValue(key, IbatisConstants.SQLMAP_DELETE));
                }
                Set<String> inserts = IbatisManager.getInstance().getAllInsert(getElement()).keySet();
                for (String key : inserts) {
                    variants.add(LookupValueFactory.createLookupValue(key, IbatisConstants.SQLMAP_INSERT));
                }
                Set<String> updates = IbatisManager.getInstance().getAllUpdate(getElement()).keySet();
                for (String key : updates) {
                    variants.add(LookupValueFactory.createLookupValue(key, IbatisConstants.SQLMAP_UPDATE));
                }
                Set<String> procedures = IbatisManager.getInstance().getAllProcedure(getElement()).keySet();
                for (String key : procedures) {
                    variants.add(LookupValueFactory.createLookupValue(key, IbatisConstants.SQLMAP_PROCEDURE));
                }
                return variants.toArray();
            }
        };
        return new PsiReference[] { psiReference };
    }
}
