package com.googlecode.intelliguard.model;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiClass;
import com.googlecode.intelliguard.util.PsiUtils;
import java.text.MessageFormat;

/**
 * Created by IntelliJ IDEA.
 * User: Ronnie
 * Date: 2009-okt-27
 * Time: 20:49:32
 */
public class Keeper {

    public enum Type {

        CLASS {

            public String getName() {
                return "class";
            }
        }
        , METHOD {

            public String getName() {
                return "method";
            }
        }
        , FIELD {

            public String getName() {
                return "field";
            }
        }
        ;

        public abstract String getName();
    }

    private Type type;

    private String name;

    private String clazz;

    public String toAntElement() {
        switch(type) {
            case CLASS:
                return MessageFormat.format("<{0} name=\"{1}\" />", getType().getName(), getName());
            default:
                return getClazz() == null ? MessageFormat.format("<{0} name=\"{1}\" />", getType().getName(), getName()) : MessageFormat.format("<{0} name=\"{1}\" class=\"{2}\" />", getType().getName(), getName(), getClazz());
        }
    }

    public boolean satisfies(PsiElement element) {
        switch(type) {
            case CLASS:
                if (element instanceof PsiClass) {
                    PsiClass psiClass = (PsiClass) element;
                    if (getName() != null) {
                        return getName().equals(PsiUtils.getKeeperName(psiClass));
                    }
                }
                return false;
            case FIELD:
                if (element instanceof PsiField) {
                    PsiField psiField = (PsiField) element;
                    if (psiField.getName().equals(getName())) {
                        return getClazz() == null || getClazz().equals(PsiUtils.getKeeperName(psiField.getContainingClass()));
                    }
                }
                return false;
            case METHOD:
                if (element instanceof PsiMethod) {
                    PsiMethod psiMethod = (PsiMethod) element;
                    String signature = PsiUtils.getSignatureString(psiMethod);
                    if (signature.equals(getName())) {
                        if (getClazz() == null || getClazz().equals(PsiUtils.getKeeperName(psiMethod.getContainingClass()))) {
                            return true;
                        }
                        PsiMethod[] superMethods = psiMethod.findDeepestSuperMethods();
                        for (PsiMethod superMethod : superMethods) {
                            if (getClazz().equals(PsiUtils.getKeeperName(superMethod.getContainingClass()))) {
                                return true;
                            }
                        }
                    }
                }
                return false;
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String toText() {
        return toAntElement();
    }
}
