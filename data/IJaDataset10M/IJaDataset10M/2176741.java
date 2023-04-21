package org.xcordion.ide.intellij;

import com.intellij.codeInsight.completion.CompletionVariant;
import com.intellij.codeInsight.completion.DefaultInsertHandler;
import com.intellij.codeInsight.completion.KeywordChooser;
import com.intellij.psi.impl.source.tree.LeafPsiElement;

public class XcordionAttributeValueCompetionVariant extends CompletionVariant {

    public XcordionAttributeValueCompetionVariant() {
        super(new XcordionXmlAttributeValueFilter());
        includeScopeClass(LeafPsiElement.class, true);
        KeywordChooser keywordChooser = new XcordionAttributeValueKeywordChooser();
        setInsertHandler(new DefaultInsertHandler());
        addCompletion(keywordChooser);
    }
}
