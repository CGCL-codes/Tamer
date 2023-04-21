package ermbasedcodegen.generators.html;

import ermbasedcodegen.generators.AbstractGenerator;
import ermbasedcodegen.util.Configuration;
import ermbasedcodegen.types.Attribute;
import ermbasedcodegen.util.TextFileUtils;
import ermbasedcodegen.entity.Entity;
import java.io.File;

/**
 * Generator für Formulare (HTML)
 *
 * @author Stefan Engel
 * @version 2011-03-04
 */
public class FormGeneratorPHP5 extends AbstractGenerator {

    @Override
    protected String generateCode(Entity e) {
        String s = TextFileUtils.readStringFromFile(Configuration.getInstance().getAttribute(Configuration.CFGKey.TEMPLATE_PATH) + File.separator + "html" + File.separator + "form.txt");
        StringBuilder buf = new StringBuilder();
        for (Attribute a : e.getAttributeList()) {
            buf.append(this.transformAttribute(a) + "\n");
        }
        s = s.replace("<FORM_FIELDS>", buf.toString());
        return s;
    }

    @Override
    protected void createOutputFile(Entity e, String path) {
        TextFileUtils.writeStringToFile(this.generateCode(e), path + File.separator + "form_" + e.getName() + ".inc");
    }

    /**
     * Hier wird zu einem Attribut das passende Formular-Eingabefeld erzeugt.
     * @param a
     * @return
     */
    public String transformAttribute(Attribute a) {
        String template = null;
        switch(a.getType()) {
            case STRING:
                template = TextFileUtils.readStringFromFile(Configuration.getInstance().getAttribute(Configuration.CFGKey.TEMPLATE_PATH) + File.separator + "html" + File.separator + "form_element_string.txt");
                template = template.replace("<ATTRIBUTE_NAME>", a.getName());
                template = template.replace("<ATTRIBUTE_SIZE>", String.valueOf(a.getLength()));
                template = template.replace("<ATTRIBUTE_PARAMETER_NAME>", TextFileUtils.createParameterName(a));
                template = template.replace("<ATTRIBUTE_VALUE>", "<?PHP print $output->getResultValue( '" + TextFileUtils.createParameterName(a) + "' ); ?>");
                break;
            case TEXT:
                template = TextFileUtils.readStringFromFile(Configuration.getInstance().getAttribute(Configuration.CFGKey.TEMPLATE_PATH) + File.separator + "html" + File.separator + "form_element_text.txt");
                template = template.replace("<ATTRIBUTE_NAME>", a.getName());
                template = template.replace("<ATTRIBUTE_PARAMETER_NAME>", TextFileUtils.createParameterName(a));
                template = template.replace("<ATTRIBUTE_VALUE>", "<?PHP print $output->getResultValue( '" + TextFileUtils.createParameterName(a) + "' ); ?>");
                break;
            case INT:
                template = TextFileUtils.readStringFromFile(Configuration.getInstance().getAttribute(Configuration.CFGKey.TEMPLATE_PATH) + File.separator + "html" + File.separator + "form_element_integer.txt");
                template = template.replace("<ATTRIBUTE_NAME>", a.getName());
                template = template.replace("<ATTRIBUTE_PARAMETER_NAME>", TextFileUtils.createParameterName(a));
                template = template.replace("<ATTRIBUTE_VALUE>", "<?PHP print $output->getResultValue( '" + TextFileUtils.createParameterName(a) + "' ); ?>");
                break;
            case FLOAT:
                template = TextFileUtils.readStringFromFile(Configuration.getInstance().getAttribute(Configuration.CFGKey.TEMPLATE_PATH) + File.separator + "html" + File.separator + "form_element_decimal.txt");
                template = template.replace("<ATTRIBUTE_NAME>", a.getName());
                template = template.replace("<ATTRIBUTE_PARAMETER_NAME_INTEGER>", TextFileUtils.createParameterName(a) + "_integer");
                template = template.replace("<ATTRIBUTE_VALUE_INTEGER>", "<?PHP print $output->getResultValue( '" + TextFileUtils.createParameterName(a) + "_integer" + "' ); ?>");
                template = template.replace("<ATTRIBUTE_PARAMETER_NAME_DECIMAL>", TextFileUtils.createParameterName(a) + "_decimal");
                template = template.replace("<ATTRIBUTE_VALUE_DECIMAL>", "<?PHP print $output->getResultValue( '" + TextFileUtils.createParameterName(a) + "_decimal" + "' ); ?>");
                break;
            case DOUBLE:
                template = TextFileUtils.readStringFromFile(Configuration.getInstance().getAttribute(Configuration.CFGKey.TEMPLATE_PATH) + File.separator + "html" + File.separator + "form_element_decimal.txt");
                template = template.replace("<ATTRIBUTE_NAME>", a.getName());
                template = template.replace("<ATTRIBUTE_PARAMETER_NAME_INTEGER>", TextFileUtils.createParameterName(a) + "_integer");
                template = template.replace("<ATTRIBUTE_VALUE_INTEGER>", "<?PHP print $output->getResultValue( '" + TextFileUtils.createParameterName(a) + "_integer" + "' ); ?>");
                template = template.replace("<ATTRIBUTE_PARAMETER_NAME_DECIMAL>", TextFileUtils.createParameterName(a) + "_decimal");
                template = template.replace("<ATTRIBUTE_VALUE_DECIMAL>", "<?PHP print $output->getResultValue( '" + TextFileUtils.createParameterName(a) + "_decimal" + "' ); ?>");
                break;
            case DATE:
                template = TextFileUtils.readStringFromFile(Configuration.getInstance().getAttribute(Configuration.CFGKey.TEMPLATE_PATH) + File.separator + "html" + File.separator + "form_element_date.txt");
                template = template.replace("<ATTRIBUTE_NAME>", a.getName());
                template = template.replace("<ATTRIBUTE_PARAMETER_NAME_YEAR>", TextFileUtils.createParameterName(a) + "_year");
                template = template.replace("<ATTRIBUTE_VALUE_YEAR>", "<?PHP print $output->getResultValue( '" + TextFileUtils.createParameterName(a) + "_year" + "' ); ?>");
                template = template.replace("<ATTRIBUTE_PARAMETER_NAME_MONTH>", TextFileUtils.createParameterName(a) + "_month");
                template = template.replace("<ATTRIBUTE_VALUE_MONTH>", "<?PHP print $output->getResultValue( '" + TextFileUtils.createParameterName(a) + "_month" + "' ); ?>");
                template = template.replace("<ATTRIBUTE_PARAMETER_NAME_DAY>", TextFileUtils.createParameterName(a) + "_day");
                template = template.replace("<ATTRIBUTE_VALUE_DAY>", "<?PHP print $output->getResultValue( '" + TextFileUtils.createParameterName(a) + "_day" + "' ); ?>");
                break;
            case TIME:
                template = TextFileUtils.readStringFromFile(Configuration.getInstance().getAttribute(Configuration.CFGKey.TEMPLATE_PATH) + File.separator + "html" + File.separator + "form_element_time.txt");
                template = template.replace("<ATTRIBUTE_NAME>", a.getName());
                template = template.replace("<ATTRIBUTE_PARAMETER_NAME_HOUR>", TextFileUtils.createParameterName(a) + "_hour");
                template = template.replace("<ATTRIBUTE_VALUE_HOUR>", "<?PHP print $output->getResultValue( '" + TextFileUtils.createParameterName(a) + "_hour" + "' ); ?>");
                template = template.replace("<ATTRIBUTE_PARAMETER_NAME_MINUTE>", TextFileUtils.createParameterName(a) + "_minute");
                template = template.replace("<ATTRIBUTE_VALUE_MINUTE>", "<?PHP print $output->getResultValue( '" + TextFileUtils.createParameterName(a) + "_minute" + "' ); ?>");
                template = template.replace("<ATTRIBUTE_PARAMETER_NAME_SECOND>", TextFileUtils.createParameterName(a) + "_second");
                template = template.replace("<ATTRIBUTE_VALUE_SECOND>", "<?PHP print $output->getResultValue( '" + TextFileUtils.createParameterName(a) + "_second" + "' ); ?>");
                break;
            case TIMESTAMP:
                template = TextFileUtils.readStringFromFile(Configuration.getInstance().getAttribute(Configuration.CFGKey.TEMPLATE_PATH) + File.separator + "html" + File.separator + "form_element_timestamp.txt");
                template = template.replace("<ATTRIBUTE_NAME>", a.getName());
                template = template.replace("<ATTRIBUTE_PARAMETER_NAME_YEAR>", TextFileUtils.createParameterName(a) + "_year");
                template = template.replace("<ATTRIBUTE_VALUE_YEAR>", "<?PHP print $output->getResultValue( '" + TextFileUtils.createParameterName(a) + "_year" + "' ); ?>");
                template = template.replace("<ATTRIBUTE_PARAMETER_NAME_MONTH>", TextFileUtils.createParameterName(a) + "_month");
                template = template.replace("<ATTRIBUTE_VALUE_MONTH>", "<?PHP print $output->getResultValue( '" + TextFileUtils.createParameterName(a) + "_month" + "' ); ?>");
                template = template.replace("<ATTRIBUTE_PARAMETER_NAME_DAY>", TextFileUtils.createParameterName(a) + "_day");
                template = template.replace("<ATTRIBUTE_VALUE_DAY>", "<?PHP print $output->getResultValue( '" + TextFileUtils.createParameterName(a) + "_day" + "' ); ?>");
                template = template.replace("<ATTRIBUTE_NAME>", a.getName());
                template = template.replace("<ATTRIBUTE_PARAMETER_NAME_HOUR>", TextFileUtils.createParameterName(a) + "_hour");
                template = template.replace("<ATTRIBUTE_VALUE_HOUR>", "<?PHP print $output->getResultValue( '" + TextFileUtils.createParameterName(a) + "_hour" + "' ); ?>");
                template = template.replace("<ATTRIBUTE_PARAMETER_NAME_MINUTE>", TextFileUtils.createParameterName(a) + "_minute");
                template = template.replace("<ATTRIBUTE_VALUE_MINUTE>", "<?PHP print $output->getResultValue( '" + TextFileUtils.createParameterName(a) + "_minute" + "' ); ?>");
                template = template.replace("<ATTRIBUTE_PARAMETER_NAME_SECOND>", TextFileUtils.createParameterName(a) + "_second");
                template = template.replace("<ATTRIBUTE_VALUE_SECOND>", "<?PHP print $output->getResultValue( '" + TextFileUtils.createParameterName(a) + "_second" + "' ); ?>");
                break;
            case BOOL:
                template = TextFileUtils.readStringFromFile(Configuration.getInstance().getAttribute(Configuration.CFGKey.TEMPLATE_PATH) + File.separator + "html" + File.separator + "form_element_bool.txt");
                template = template.replace("<ATTRIBUTE_NAME>", a.getName());
                template = template.replace("<ATTRIBUTE_PARAMETER_NAME>", TextFileUtils.createParameterName(a));
                template = template.replace("<ATTRIBUTE_IS_TRUE>", "<?PHP if ( isset( $output->getResultValue( '" + TextFileUtils.createParameterName(a) + "' ) ) ) { print 'CHECKED'; } ?>");
                break;
        }
        return template;
    }
}
