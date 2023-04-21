package com.googlecode.intelliguard.fix;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.googlecode.intelliguard.facet.GuardFacetConfiguration;
import com.googlecode.intelliguard.model.Keeper;
import com.googlecode.intelliguard.inspection.GuardInspectionBase;
import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: Ronnie
 * Date: 2009-nov-02
 * Time: 21:26:45
 */
public class RemoveKeepFix implements LocalQuickFix {

    private final GuardFacetConfiguration configuration;

    private final Keeper keeper;

    private final PsiElement element;

    public RemoveKeepFix(@NotNull final GuardFacetConfiguration configuration, @NotNull final Keeper keeper, @NotNull final PsiElement element) {
        this.configuration = configuration;
        this.keeper = keeper;
        this.element = element;
    }

    @NotNull
    public String getName() {
        return "Remove " + keeper.getType().getName() + " keeper" + (keeper.getType() == Keeper.Type.CLASS ? "" : (keeper.getClazz() == null ? " in all classes" : " in class " + keeper.getClazz()));
    }

    @NotNull
    public String getFamilyName() {
        return "Remove guard family";
    }

    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
        configuration.keepers.remove(keeper);
        GuardInspectionBase.alertGuardMarkers(element);
    }
}
