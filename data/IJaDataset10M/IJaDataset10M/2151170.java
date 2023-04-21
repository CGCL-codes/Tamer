package org.intellij.stripes.reference;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMember;
import com.intellij.psi.impl.search.AnnotatedMembersSearcher;
import com.intellij.psi.search.searches.AnnotatedMembersSearch;
import com.intellij.util.Processor;
import org.intellij.stripes.util.StripesConstants;
import java.util.HashMap;
import java.util.Map;

public class UrlBindingSearcher extends AnnotatedMembersSearcher {

    public static class UrlBindingProcessor implements Processor<PsiMember> {

        private Map<String, PsiClass> bindings = new HashMap<String, PsiClass>(8);

        public boolean process(PsiMember member) {
            if (member instanceof PsiClass) {
                PsiAnnotation ann = member.getModifierList().findAnnotation(StripesConstants.URL_BINDING_ANNOTATION);
                if (null != ann) {
                    PsiAnnotationMemberValue value = ann.findAttributeValue("value");
                    if (value != null) bindings.put(StringUtil.stripQuotesAroundValue(value.getText()), (PsiClass) member);
                }
            }
            return true;
        }

        public Map<String, PsiClass> getBindings() {
            return bindings;
        }
    }

    private AnnotatedMembersSearch.Parameters params;

    public UrlBindingSearcher(PsiClass urlBindingCls) {
        this.params = new AnnotatedMembersSearch.Parameters(urlBindingCls, urlBindingCls.getUseScope());
    }

    public Map<String, PsiClass> execute() {
        UrlBindingProcessor proc = new UrlBindingProcessor();
        try {
            super.execute(params, proc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return proc.getBindings();
    }
}
