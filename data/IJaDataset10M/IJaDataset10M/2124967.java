package org.intellij.ibatis.provider;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.*;
import com.intellij.util.IncorrectOperationException;
import org.intellij.ibatis.IbatisManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Map;
import java.util.Set;

/**
 * resultMap reference provider
 */
public class ResultMapReferenceProvider extends BaseReferenceProvider {

    @NotNull
    public PsiReference[] getReferencesByElement(PsiElement psiElement) {
        XmlAttributeValue xmlAttributeValue = (XmlAttributeValue) psiElement;
        XmlAttributeValuePsiReference psiReference = new XmlAttributeValuePsiReference(xmlAttributeValue) {

            public boolean isSoft() {
                return false;
            }

            @Nullable
            public PsiElement resolve() {
                String resultMapId = getReferenceId(getElement());
                Map<String, XmlTag> allResultMap = IbatisManager.getInstance().getAllResultMap2(getElement());
                return allResultMap.get(resultMapId);
            }

            public Object[] getVariants() {
                Set<String> resultMapList = IbatisManager.getInstance().getAllResultMap2(getElement()).keySet();
                return resultMapList.toArray();
            }
        };
        return new PsiReference[] { psiReference };
    }
}
