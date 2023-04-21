package de.schlund.pfixxml;

import net.sf.saxon.expr.XPathContext;
import de.schlund.pfixxml.targets.TargetGenerator;
import de.schlund.pfixxml.util.ExtensionFunctionUtils;
import de.schlund.pfixxml.util.XsltContext;
import de.schlund.pfixxml.util.xsltimpl.XsltContextSaxon2;

/**
 * @author mleidig@schlund.de
 */
public class ImageThemedSrcSaxon2 {

    public static String getSrc(XPathContext context, String src, String themed_path, String themed_img, String parent_part_in, String parent_product_in, TargetGenerator targetGen, String targetKey, String module, String search, String tenant, String language) throws Exception {
        try {
            XsltContext xsltContext = new XsltContextSaxon2(context);
            return ImageThemedSrc.getSrc(xsltContext, src, themed_path, themed_img, parent_part_in, parent_product_in, targetGen, targetKey, module, search, tenant, language);
        } catch (Exception x) {
            ExtensionFunctionUtils.setExtensionFunctionError(x);
            throw x;
        }
    }
}
