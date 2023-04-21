package org.intellij.ibatis.provider;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * statement self reference for id attribute
 *
 * @author linux_china@hotmail.com
 */
public class StatementSelfReferenceProvider extends BaseReferenceProvider {

    @Override
    @NotNull
    public PsiReference[] getReferencesByElement(PsiElement psiElement) {
        final XmlAttributeValue xmlAttributeValue = (XmlAttributeValue) psiElement;
        return new PsiReference[] { new XmlAttributeValuePsiReference(xmlAttributeValue) {

            @Override
            @Nullable
            public PsiElement resolve() {
                return PsiTreeUtil.getParentOfType(getElement(), XmlTag.class);
            }

            @Override
            public boolean isSoft() {
                return true;
            }
        } };
    }
}
