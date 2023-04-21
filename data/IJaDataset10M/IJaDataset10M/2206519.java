package mecca.portal;

import org.apache.velocity.Template;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class SisLoginModule extends mecca.portal.velocity.VTemplate {

    public Template doTemplate() throws Exception {
        Template template = engine.getTemplate("vtl/sis/web/login.vm");
        return template;
    }
}
