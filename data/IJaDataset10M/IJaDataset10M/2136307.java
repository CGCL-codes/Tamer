package net.sf.jfxdplugin.stubs;

/**
 *
 * @author Nathan Erwin
 * @version $Revision: 59 $ $Date: 2010-03-26 17:32:20 -0400 (Fri, 26 Mar 2010) $
 */
public class ExtracssConfigurationStub extends BaseStub {

    public ExtracssConfigurationStub() {
        super("extracss-plugin-config.xml", "extracss", "/src");
    }

    @Override
    protected String getSource() {
        return "/src/test/resources/unit/extracss/";
    }

    @Override
    protected String getTarget() {
        return "/target/test/unit/extracss/target";
    }
}
