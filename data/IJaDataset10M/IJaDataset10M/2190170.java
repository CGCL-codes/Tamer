package org.genos.gmf.resources.generic;

import org.genos.gmf.Configuration;
import org.genos.gmf.Core.PatternString;
import org.genos.gmf.form.Form;
import org.genos.gmf.form.ParameterSourceSQL;
import org.genos.gmf.form.ParameterType;
import org.genos.gmf.resources.formatters.ContainerDefaultFormatter;
import org.genos.gmf.resources.formatters.RFormatter;
import org.genos.gmf.resources.formatters.VParam;
import org.genos.gmf.resources.formatters.VParamSource.VPARAMSOURCE;

/**
 * @author vjove
 */
public class ImageGallery extends Folder {

    /**
	 * Constructor
	 * @throws Exception
	 */
    public ImageGallery() throws Exception {
        super();
        resForm = new Form();
        ParameterType fpt = new ParameterType(ParameterType.PT_TEXT);
        fpt.setFlags(ParameterType.PARAM_OBLIGATORY);
        fpt.setMaxLength(128);
        resForm.addParameter("name", new PatternString(this, "$lang:s_res_folder_name$"), null, fpt, new ParameterSourceSQL(null, "name"));
        resForm.addParameter("description", new PatternString(this, "$lang:s_res_folder_desc$"), null, new ParameterType(ParameterType.PT_TEXTAREA), new ParameterSourceSQL(null, "description"));
    }

    /**
     * Get resource description
     * @return  Description
     */
    public String getResourceDescription() throws Exception {
        return Configuration.getLocaleString(uid, "s_res_imagegallery");
    }

    /**
     * Formatter definition
     */
    public void defFormatter() throws Exception {
        super.defFormatter();
        String lang = Configuration.getUserProperty(uid, "lang");
        ContainerDefaultFormatter f = (ContainerDefaultFormatter) getFormatter();
        f.setQueryOurselves(true);
        f.setXsl(Configuration.homeXSL + Configuration.getXslFile(lang, "imagegallery"));
    }

    /**
     * Add virtual params to default formatter.
     * @param f                     Formatter.
     * @throws Exception
     */
    protected void defFormatterParams(RFormatter f) throws Exception {
        VParam vp;
        final String[] components = { "name", "description" };
        final String imgallery = "ImageGallery";
        final String iname = "Image";
        for (int i = 0; i < components.length; ++i) {
            vp = new VParam();
            vp.defComponent(VPARAMSOURCE.COMPONENT, imgallery, components[i]);
            vp.defInfo("class", "Image");
            f.defVParam("ig_" + components[i], vp);
        }
        vp = new VParam();
        vp.defComponent(VPARAMSOURCE.METHOD, iname, "getImage");
        f.defVParam("ig_image", vp);
        vp = new VParam();
        vp.defComponent(VPARAMSOURCE.METHOD, imgallery, "getLink");
        f.defVParam("ig_link", vp);
    }

    /**
     * Gets the link to this resource for contained ImageGallery
     * @return
     */
    public String getLink() {
        String name = null;
        try {
            name = (String) resForm.getPValue("name");
        } catch (Exception e) {
            Configuration.logger.error("ImageGallery.getLink(): " + e);
        }
        return buildHtmlLinkToResource(name);
    }
}
