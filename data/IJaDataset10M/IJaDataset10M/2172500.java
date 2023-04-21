package lebah.portal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public class SecurityTokenDenied extends lebah.portal.velocity.VTemplate {

    public SecurityTokenDenied() {
    }

    public SecurityTokenDenied(VelocityEngine engine, VelocityContext context, HttpServletRequest req, HttpServletResponse res) {
        super(engine, context, req, res);
    }

    public Template doTemplate() throws Exception {
        Template template = engine.getTemplate("vtl/main/security_token_denied.vm");
        return template;
    }
}
