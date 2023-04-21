package net.disy.ogc.wps.v_1_0_0.sample;

import net.disy.ogc.wps.v_1_0_0.annotation.OutputParameter;
import net.disy.ogc.wps.v_1_0_0.annotation.Process;
import net.disy.ogc.wps.v_1_0_0.annotation.ProcessMethod;
import net.disy.ogc.wps.v_1_0_0.util.WpsUtils;
import net.disy.ogc.wpspd.v_1_0_0.Link;
import net.disy.ogc.wpspd.v_1_0_0.LinkType;
import net.disy.ogc.wpspd.v_1_0_0.ObjectFactory;
import net.disy.ogc.wpspd.v_1_0_0.WpspdUtils;

@Process(id = "dummyLinkNew", title = "Dummy Link [New]", description = "Opens homepage in a new tab")
public class DummyLinkNewAnnotatedObject {

    private final ObjectFactory objectFactory = WpspdUtils.createObjectFactory();

    @ProcessMethod
    @OutputParameter(id = "link", title = "Link", description = "Link to the homepage")
    public Link createDummyLink() {
        final LinkType link = objectFactory.createLinkType();
        link.setHref("http://www.disy.net");
        link.setShow("new");
        link.setTitle("disy Informationssysteme GmbH");
        link.setPresentationDirectiveTitle(WpsUtils.createLanguageStringType("disy Informationssysteme GmbH"));
        return objectFactory.createLink(link);
    }
}
