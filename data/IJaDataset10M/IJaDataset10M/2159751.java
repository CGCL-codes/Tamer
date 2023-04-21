package org.jrandomize.control;

import java.io.StringWriter;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.jrandomize.model.Dice;
import org.jrandomize.model.Table;
import org.jrandomize.model.Weapon;

/**
 * Uses a velocity template to generate text output
 */
public class VelocityRoller {

    public static String rollTemplate(String templateFile) throws Exception {
        VelocityEngine ve = new VelocityEngine();
        ve.init();
        Template t = ve.getTemplate(templateFile);
        VelocityContext context = new VelocityContext();
        context.put("dice", new Dice());
        context.put("table", new Table());
        context.put("name", "World");
        StringWriter writer = new StringWriter();
        t.merge(context, writer);
        return writer.toString();
    }
}
