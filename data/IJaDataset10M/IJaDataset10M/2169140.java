package org.intellij.stripes.facet;

import com.intellij.facet.Facet;
import com.intellij.facet.FacetType;
import com.intellij.facet.FacetTypeId;
import com.intellij.javaee.web.facet.WebFacet;
import com.intellij.openapi.module.Module;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA. User: Mario Arias Date: 2/07/2007 Time: 10:52:45 PM
 */
public class StripesFacet extends Facet<StripesFacetConfiguration> {

    public static final FacetTypeId<StripesFacet> FACET_TYPE_ID = new FacetTypeId<StripesFacet>();

    public StripesFacet(@NotNull FacetType facetType, @NotNull Module module, String name, @NotNull StripesFacetConfiguration configuration, Facet underlyingFacet) {
        super(facetType, module, name, configuration, underlyingFacet);
    }

    public PsiFile getWebXmlPsiFile() {
        return getWebFacet().getWebXmlDescriptor().getPsiFile();
    }

    public WebFacet getWebFacet() {
        return (WebFacet) getUnderlyingFacet();
    }
}
