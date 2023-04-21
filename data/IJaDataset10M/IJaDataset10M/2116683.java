package net.nothinginteresting.datamappers2.generate.java;

import java.io.StringWriter;
import java.util.Set;
import java.util.TreeSet;
import net.nothinginteresting.datamappers2.models.SchemaClass;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

/**
 * @author Dmitriy Gorbenko
 * 
 */
public class JavaClassGenerator {

    private final SchemaClass schemaClass;

    public JavaClassGenerator(SchemaClass schemaClass) {
        this.schemaClass = schemaClass;
    }

    public String generate() {
        VelocityContext context = new VelocityContext();
        Set<String> imports = new TreeSet<String>();
        context.put("class", schemaClass);
        context.put("imports", imports);
        context.put("generateUtils", GenerateUtils.class);
        String classDef = processTemplate("net/nothinginteresting/datamappers2/generate/java/class_def.vm", context);
        context.put("class_def", classDef);
        return processTemplate("net/nothinginteresting/datamappers2/generate/java/class.vm", context);
    }

    private String processTemplate(String templateFile, VelocityContext context) {
        Template template = null;
        try {
            template = Velocity.getTemplate(templateFile);
            StringWriter writer = new StringWriter();
            template.merge(context, writer);
            return writer.toString();
        } catch (ResourceNotFoundException rnfe) {
            System.out.println("Error : cannot find template " + templateFile);
            rnfe.printStackTrace();
        } catch (ParseErrorException pee) {
            System.out.println("Syntax error in template " + templateFile + ":" + pee);
            pee.printStackTrace();
        } catch (Exception e) {
            System.out.println("Error in template " + templateFile + ":" + e);
            e.printStackTrace();
        }
        return "";
    }
}
