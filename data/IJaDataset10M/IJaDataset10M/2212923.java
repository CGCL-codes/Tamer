package osmedile.intellij.util.psi;

import checkmyvars.Nullable;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMethod;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiNameValuePair;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Olivier Smedile
 * @version $Id: File Header.java 3 2008-03-11 08:52:55Z osmedile $
 */
public class AnnotationUtil {

    /**
     * Return all attributes of the specified annotation
     * If an attribute value is redefined, then this value is taken. Otherwise the default value is returned.
     *
     * @param anno annotation for which attributes and their value must be found
     *
     * @return a map containing attributes and values of the specified annotation. Key is attribute name, value is attribute value
     */
    public static Map<String, String> findAnnoAttributes(@Nullable PsiAnnotation anno) {
        if (anno == null) {
            return Collections.emptyMap();
        }
        Map<String, String> result = new HashMap<String, String>();
        if (anno.getNameReferenceElement() != null) {
            PsiElement psielement = anno.getNameReferenceElement().resolve();
            if (psielement != null) {
                PsiMethod[] methods = ((PsiClass) psielement).getMethods();
                for (PsiMethod meth : methods) {
                    if (meth instanceof PsiAnnotationMethod) {
                        if (((PsiAnnotationMethod) meth).getDefaultValue() != null) {
                            result.put(meth.getName(), "" + ((PsiAnnotationMethod) meth).getDefaultValue().getText());
                        } else {
                            result.put(meth.getName(), "null");
                        }
                    }
                }
            }
        }
        PsiNameValuePair[] params = anno.getParameterList().getAttributes();
        for (PsiNameValuePair param : params) {
            if (param.getName() == null) {
                result.put("value", param.getValue().getText());
            } else {
                result.put(param.getName(), param.getValue().getText());
            }
        }
        return result;
    }
}
