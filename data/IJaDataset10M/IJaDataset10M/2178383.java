package net.sf.jfling.codegen.builders.velocity;

import net.sf.jfling.codegen.ProcessedFieldData;
import net.sf.jfling.codegen.builders.CodeBuilder;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class FixedFieldCodeGenerator implements CodeBuilder {

    private static final String IMPORTS = "imports";

    private static final String PACKAGE = "package";

    private static final String CLASS_NAME = "className";

    private static final String NUM_COMPONENTS = "numComponents";

    private static final String FIELDS = "fields";

    static {
        Properties p = new Properties();
        p.setProperty("resource.loader", "class");
        p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        p.setProperty("classpath.resource.loader.cache", "true");
        p.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogSystem");
        try {
            Velocity.init(p);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String generateClass(List<ProcessedFieldData> fieldDataList, String packageName, String className) {
        VelocityContext context = new VelocityContext();
        String[] imports = new String[] { "net.sf.jfling.*" };
        context.put(IMPORTS, imports);
        context.put(PACKAGE, packageName);
        context.put(CLASS_NAME, className);
        context.put(NUM_COMPONENTS, fieldDataList.size());
        List<ExtendedFieldData> extFieldDataList = new ArrayList<ExtendedFieldData>();
        for (ProcessedFieldData pfd : fieldDataList) {
            extFieldDataList.add(new ExtendedFieldData(pfd));
        }
        context.put(FIELDS, extFieldDataList);
        StringWriter sw = new StringWriter();
        try {
            Template template = Velocity.getTemplate("FixedLengthClassTemplate.vsl");
            template.merge(context, sw);
        } catch (Exception e) {
            throw new RuntimeException("Could not locate Velocity template file.", e);
        }
        return sw.toString();
    }
}
