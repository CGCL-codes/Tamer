package org.intellij.ibatis.inspections;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiType;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.xml.highlighting.DomElementAnnotationHolder;
import org.intellij.ibatis.IbatisSqlMapModel;
import org.intellij.ibatis.dom.sqlMap.Result;
import org.intellij.ibatis.dom.sqlMap.ResultMap;
import org.intellij.ibatis.dom.sqlMap.SqlMap;
import org.intellij.ibatis.provider.FieldAccessMethodReferenceProvider;
import org.intellij.ibatis.util.IbatisBundle;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import java.util.List;

/**
 * inspection for set null to primary type variable
 */
public class NullSettedToPrimaryTypeInspection extends SqlMapInspection {

    @Nls
    @NotNull
    public String getDisplayName() {
        return IbatisBundle.message("ibatis.sqlmap.inspection.nulltoprimarytype.name");
    }

    @NonNls
    @NotNull
    public String getShortName() {
        return IbatisBundle.message("ibatis.sqlmap.inspection.nulltoprimarytype.id");
    }

    @SuppressWarnings({ "ConstantConditions" })
    protected void checkResultMap(IbatisSqlMapModel sqlMapModel, SqlMap sqlMap, ResultMap resultMap, DomElementAnnotationHolder holder) {
        PsiClass psiClass = resultMap.getClazz().getValue();
        if (psiClass == null) return;
        List<Result> results = resultMap.getResults();
        for (Result result : results) {
            if (result.getXmlTag().getAttribute("nullValue") == null) {
                String propertyName = result.getProperty().getValue();
                PsiMethod setMethod = null;
                if (propertyName != null) {
                    if (!propertyName.contains(".")) {
                        PsiMethod[] methods = psiClass.findMethodsByName("set" + StringUtil.capitalize(propertyName), true);
                        if (methods.length > 0) {
                            setMethod = methods[0];
                        }
                    } else {
                        String field1 = propertyName.substring(0, propertyName.indexOf('.'));
                        String field2 = propertyName.substring(propertyName.indexOf('.') + 1);
                        PsiClass fieldClass = FieldAccessMethodReferenceProvider.findGetterMethodReturnType(psiClass, "get" + StringUtil.capitalize(field1));
                        if (fieldClass != null) {
                            PsiMethod[] methods = fieldClass.findMethodsByName("set" + StringUtil.capitalize(field2), true);
                            if (methods.length > 0) {
                                setMethod = methods[0];
                            }
                        }
                    }
                }
                if (setMethod != null) {
                    PsiParameter[] psiParameters = setMethod.getParameterList().getParameters();
                    if (psiParameters.length == 1) {
                        PsiType[] superTypes = psiParameters[0].getType().getSuperTypes();
                        if (superTypes.length < 1) {
                            holder.createProblem(result, HighlightSeverity.INFO, IbatisBundle.message("ibatis.sqlmap.inspection.nulltoprimarytype.error"), new AddNullValueForResultElementQuickFix(result));
                        }
                    }
                }
            }
        }
    }

    public class AddNullValueForResultElementQuickFix implements LocalQuickFix {

        private Result result;

        public AddNullValueForResultElementQuickFix(Result result) {
            this.result = result;
        }

        @NotNull
        public String getName() {
            return "add nullValue for result element";
        }

        @NotNull
        public String getFamilyName() {
            return "add nullValue for result element";
        }

        @SuppressWarnings({ "ConstantConditions" })
        public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor problemDescriptor) {
            try {
                result.getXmlTag().setAttribute("nullValue", "0");
            } catch (IncorrectOperationException e) {
                e.printStackTrace();
            }
        }
    }
}
