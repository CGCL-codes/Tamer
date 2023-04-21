package tags;

import groovy.lang.Closure;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.Map;
import play.modules.jqvalidate.singleton.MapSingleton;
import play.mvc.Router.ActionDefinition;
import play.templates.BaseTemplate;
import play.templates.FastTags;
import play.templates.JavaExtensions;
import play.templates.GroovyTemplate.ExecutableTemplate;

/**
 * @author Ahmed Mahmoud
 * 
 */
@FastTags.Namespace("jqv")
public final class VTags extends FastTags {

    /**
	 * Puts the scripts and styles for validating this form in
	 * {@code moreScripts} & {@code moreStyles} properties and generates html
	 * form element as specified by this tag
	 * {@link FastTags#_form(Map, Closure, PrintWriter, ExecutableTemplate, int)}
	 * 
	 * @param args
	 *            tag attributes tag attributes
	 * 
	 * @param body
	 *            tag inner body
	 * @param out
	 *            the output writer
	 * @param template
	 *            enclosing template
	 * @param fromLine
	 *            template line number where the tag is defined
	 * 
	 */
    @SuppressWarnings("unchecked")
    public static void _vform(Map<?, ?> args, Closure body, PrintWriter out, ExecutableTemplate template, int fromLine) {
        if (args.containsKey("class")) {
            ((Map<Object, Object>) args).put("class", args.remove("class") + " TBV");
        } else {
            ((Map<Object, Object>) args).put("class", "TBV");
        }
        if (args.get("edit") != null) {
            BaseTemplate.layoutData.get().put("_editObject_", args.remove("edit"));
        }
        generateFormValidationOptions(args);
        _form(args, body, out, template, fromLine);
    }

    /**
	 * TODO document generateFormValidationOptions
	 * 
	 * @param args
	 */
    @SuppressWarnings("unchecked")
    private static void generateFormValidationOptions(Map<?, ?> args) {
        Object ajax = args.get("ajax");
        if (ajax == null) {
            ajax = "";
        } else {
            if ((Boolean) ajax) {
                ajax = generateFormAjaxOptions(args);
            } else {
                ajax = "";
            }
        }
        Object onValidationComplete = args.remove("onComplete");
        Object autoPositionUpdate = args.remove("autoUpdate");
        if (onValidationComplete == null) {
            onValidationComplete = "";
        } else {
            onValidationComplete = "onValidationComplete:" + onValidationComplete + ",";
        }
        if (autoPositionUpdate == null) {
            autoPositionUpdate = "";
        } else {
            autoPositionUpdate = "autoPositionUpdate:" + autoPositionUpdate;
        }
        String options = "options = {" + ajax + onValidationComplete + autoPositionUpdate + "}";
        options = options.replace(",}", "}");
        ((Map<Object, Object>) args).put("options", options);
    }

    /**
	 * TODO document generateFormAjaxOptions()
	 * 
	 * @param args
	 * @return
	 */
    public static String generateFormAjaxOptions(Map<?, ?> args) {
        Object url = args.remove("ajaxUrl");
        Object onBeforeAjaxFormValidation = args.remove("onAjaxBefore");
        Object onAjaxFormComplete = args.remove("onAjaxAfter");
        if (url == null) {
            url = "";
        } else if (url instanceof ActionDefinition) {
            url = "ajaxFormValidationURL:'" + ((ActionDefinition) url).url + "',";
        } else {
            url = "ajaxFormValidationURL:'" + url + "',";
        }
        if (onBeforeAjaxFormValidation == null) {
            onBeforeAjaxFormValidation = "";
        } else {
            onBeforeAjaxFormValidation = "onBeforeAjaxFormValidation:'" + onBeforeAjaxFormValidation + "',";
        }
        if (onAjaxFormComplete == null) {
            onAjaxFormComplete = "";
        } else {
            onAjaxFormComplete = "onAjaxFormComplete:'" + onAjaxFormComplete + "',";
        }
        return "ajaxFormValidation:true," + url + onAjaxFormComplete + onBeforeAjaxFormValidation;
    }

    /**
	 * Generates a html input element of type password linked to a field in
	 * model and validated accordingly.
	 * 
	 * @param args
	 *            tag attributes
	 * 
	 * @param body
	 *            tag inner body
	 * @param out
	 *            the output writer
	 * @param template
	 *            enclosing template
	 * @param fromLine
	 *            template line number where the tag is defined
	 * @throws Exception
	 * 
	 */
    public static void _pass(Map<?, ?> args, Closure body, PrintWriter out, ExecutableTemplate template, int fromLine) throws Exception {
        String passInput = "<input type=\"password\" class=\"%s %s\" id=\"%s\" name=\"%s\" value=\"%s\" %s/>";
        String[] modelField = getModelField(args);
        String validation = formatValidation(modelField, args, "");
        passInput = formatHtmlElementAttributes(args, passInput, modelField, validation);
        out.print(passInput);
        if (body != null) {
            out.print(String.format(JavaExtensions.toString(body), checkForConfirmElement(body, passInput)));
        }
    }

    /**
	 * Generates a html input element of type text linked to a field whose type
	 * is Date or Calendar in model and validated accordingly. It supports time,
	 * Date, & DateTime inputs.
	 * 
	 * @param args
	 *            tag attributes
	 * 
	 * @param body
	 *            tag inner body
	 * @param out
	 *            the output writer
	 * @param template
	 *            enclosing template
	 * @param fromLine
	 *            template line number where the tag is defined
	 * @throws Exception
	 * 
	 */
    public static void _date(Map<?, ?> args, Closure body, PrintWriter out, ExecutableTemplate template, int fromLine) throws Exception {
        String dateInput = "<input type=\"text\" class=\"%s %s datepicker\" id=\"%s\" name=\"%s\" value=\"%s\" %s/>";
        String[] modelField = getModelField(args);
        String dateValidation = (String) args.remove("type");
        if (dateValidation == null) {
            dateValidation = ",custom[dateFormat]";
        } else {
            dateValidation = ",custom[" + dateValidation + "Format]";
        }
        String validation = formatValidation(modelField, args, dateValidation);
        dateInput = formatHtmlElementAttributes(args, dateInput, modelField, validation);
        out.print(dateInput);
        if (body != null) {
            out.print(String.format(JavaExtensions.toString(body), checkForConfirmElement(body, dateInput)));
        }
    }

    /**
	 * Generates a html input element of type text linked to a field in model
	 * and validated accordingly.
	 * 
	 * @param args
	 *            tag attributes
	 * 
	 * @param body
	 *            tag inner body
	 * @param out
	 *            the output writer
	 * @param template
	 *            enclosing template
	 * @param fromLine
	 *            template line number where the tag is defined
	 * @throws Exception
	 * 
	 */
    public static void _text(Map<?, ?> args, Closure body, PrintWriter out, ExecutableTemplate template, int fromLine) throws Exception {
        String textInput = "<input type=\"text\" class=\"%s %s\" id=\"%s\" name=\"%s\" value=\"%s\" %s />";
        String[] modelField = getModelField(args);
        String validation = formatValidation(modelField, args, "");
        textInput = formatHtmlElementAttributes(args, textInput, modelField, validation);
        out.print(textInput);
        if (body != null) {
            out.print(String.format(JavaExtensions.toString(body), checkForConfirmElement(body, textInput)));
        }
    }

    /**
	 * Generates a html input element of type textarea linked to a field in
	 * model and validated accordingly.
	 * 
	 * @param args
	 *            tag attributes
	 * 
	 * @param body
	 *            tag inner body
	 * @param out
	 *            the output writer
	 * @param template
	 *            enclosing template
	 * @param fromLine
	 *            template line number where the tag is defined
	 * @throws Exception
	 * 
	 */
    public static void _textarea(Map<?, ?> args, Closure body, PrintWriter out, ExecutableTemplate template, int fromLine) throws Exception {
        String textInput = "<textarea class=\"%s %s\" id=\"%s\" name=\"%s\" >%s";
        String[] modelField = getModelField(args);
        String validation = formatValidation(modelField, args, "");
        textInput = formatHtmlElementAttributes(args, textInput, modelField, validation);
        out.print(textInput);
        out.print((body == null ? "" : JavaExtensions.toString(body)));
        out.print("</textarea>");
    }

    /**
	 * Generates a html input element of type file and supports extra validation
	 * like checking for extensions, min size, or max size.
	 * 
	 * @param args
	 *            tag attributes
	 * 
	 * @param body
	 *            tag inner body
	 * @param out
	 *            the output writer
	 * @param template
	 *            enclosing template
	 * @param fromLine
	 *            template line number where the tag is defined
	 * @throws Exception
	 * 
	 */
    public static void _file(Map<?, ?> args, Closure body, PrintWriter out, ExecutableTemplate template, int fromLine) throws Exception {
        String fileInput = "<input type=\"file\" class=\"%s %s\" id=\"%s\" name=\"%s\" value=\"%s\"  %s />";
        Object acceptExtentions = args.remove("accept");
        Object rejectExtentions = args.remove("reject");
        Object minSize = args.remove("min");
        Object maxSize = args.remove("max");
        String[] modelField = getModelField(args);
        StringBuffer extraValidation = new StringBuffer();
        if (acceptExtentions != null) {
            extraValidation.append(",endsWith[.*?");
            extraValidation.append(acceptExtentions.toString().replace(",", "$|"));
            extraValidation.append("$]");
        }
        if (rejectExtentions != null) {
            extraValidation.append(",notEndsWith[.*?");
            extraValidation.append(rejectExtentions.toString().replace(",", "$|"));
            extraValidation.append("$]");
        }
        if (minSize != null) {
            extraValidation.append(",minSize[");
            extraValidation.append(minSize);
            extraValidation.append("]");
        }
        if (maxSize != null) {
            extraValidation.append(",maxSize[");
            extraValidation.append(maxSize);
            extraValidation.append("]");
        }
        String validation = formatValidation(modelField, args, extraValidation.toString());
        fileInput = formatHtmlElementAttributes(args, fileInput, modelField, validation);
        out.print(fileInput);
    }

    /**
	 * Formats the given Model.Field validation. It appends ajax, groupRequired
	 * and Custom Function validations to the auto generated validation at
	 * startup.
	 * 
	 * @param modelField
	 *            String[] consists of 2 elements which are model name & field
	 *            name respectively.
	 * @param args
	 *            the calling tag attributes
	 * @param extraValidation
	 *            extra validation provided by the calling tag.
	 * @return String representing the validation for this field to be added by
	 *         the calling tag.
	 */
    private static String formatValidation(String[] modelField, Map<?, ?> args, String extraValidation) {
        Object group = args.remove("group");
        Object customFunction = args.remove("custom");
        Object ajax = args.remove("ajax");
        String ajaxValidation = "";
        if (ajax != null) {
            if ((Boolean) ajax) {
                ajaxValidation = generateFieldAjax(args);
            }
        }
        String autoformatedValidation = null;
        try {
            if (modelField != null) {
                autoformatedValidation = MapSingleton.getClassFieldValidation().get(modelField[0]).get(modelField[1]);
            }
        } catch (Exception e) {
        }
        if (customFunction == null) {
            customFunction = "";
        } else {
            customFunction = ",funcCall[" + customFunction + "]";
        }
        if (autoformatedValidation == null) {
            autoformatedValidation = "";
        }
        if (group == null) {
            group = "";
        } else {
            group = ",groupRequired[" + group + "]";
            autoformatedValidation.replace(",required", "");
        }
        String validation = String.format("validate[%s%s%s%s%s]", autoformatedValidation, group, extraValidation, customFunction, ajaxValidation);
        if (validation.length() == 10) {
            validation = "";
        }
        return validation;
    }

    public static String generateFieldAjax(Map<?, ?> args) {
        Object ajaxUrl = args.remove("ajaxUrl");
        Object extraData = args.remove("ajaxExtraData");
        Object ajaxLoad = args.remove("ajaxLoad");
        Object ajaxError = args.remove("ajaxError");
        Object ajaxOK = args.remove("ajaxOK");
        if (ajaxUrl == null) {
            ajaxUrl = "";
        } else if (ajaxUrl instanceof ActionDefinition) {
            ajaxUrl = ((ActionDefinition) ajaxUrl).url;
        }
        if (ajaxLoad == null) {
            ajaxLoad = "";
        }
        if (ajaxOK == null) {
            ajaxOK = "";
        }
        if (ajaxError == null) {
            ajaxError = "";
        }
        if (extraData == null) {
            extraData = "";
        }
        return ",ajax[" + ajaxUrl + "," + extraData + "," + ajaxLoad + "," + ajaxError + "," + ajaxOK + "]";
    }

    /**
	 * Generates a formatted HTML element linked to a certain model field given
	 * a string representation for this element.
	 * 
	 * @param args
	 *            the calling tag attributes
	 * @param htmlElement
	 *            String representation for this element contains placeHolders
	 *            (%s) to embedd in it its attributes values.
	 * @param modelField
	 *            the return of this method {@link VTags#getModelField(Map)}
	 * @return String representing the HTML element after embedding the
	 *         attributes and the validation in it.
	 * @throws Exception
	 */
    static String formatHtmlElementAttributes(Map<?, ?> args, String htmlElement, String[] modelField, String validation) throws Exception {
        Object id = args.remove("id");
        Object _class = args.remove("class");
        Object name = args.remove("name");
        Object value = null;
        if (modelField != null) {
            value = getDefaultValue(modelField);
        }
        if (value == null) {
            value = "";
        }
        if (name == null && id == null) {
            name = (modelField == null ? "nullField" : modelField[1]);
            id = name;
        } else if (id == null) {
            id = name;
        } else if (name == null) {
            name = id;
        }
        if (_class == null) {
            _class = (modelField == null ? "nullField" : modelField[1]);
        }
        return String.format(htmlElement, validation, _class, id, name, value, serialize(args));
    }

    /**
	 * Gets the value of the field, represented by its name in the
	 * {@code modelField[1]}, from the Object specified in the
	 * {@link VTags#_vform(Map, Closure, PrintWriter, ExecutableTemplate, int)}
	 * {@code edit} argument.
	 * 
	 * @param modelField
	 *            String[] contains the name of the field in [1].
	 * @return the value of the field in an object returned from the map of
	 *         properties in the current template under the key "_editObject_"
	 * @throws Exception
	 *             see method {@link #getFieldValue(Object, String)}
	 */
    static Object getDefaultValue(String[] modelField) throws Exception {
        if (modelField != null && BaseTemplate.layoutData.get().containsKey("_editObject_")) {
            Object obj = BaseTemplate.layoutData.get().get("_editObject_");
            return getFieldValue(obj, modelField[1]);
        }
        return "";
    }

    /**
	 * gets the model name & the field name from a map of arguments by the key
	 * "field" and puts them in an array.
	 * 
	 * @param args
	 *            tag attributes
	 * @return String[] representing the Model in index 0 and the Field in index
	 *         1.
	 * @throws IllegalArgumentException
	 *             if the field argument doesn't contain the model name or the
	 *             field name
	 * 
	 */
    static String[] getModelField(Map<?, ?> args) {
        Object field = args.remove("for");
        if (field == null) {
            return null;
        }
        String[] modelField = field.toString().split("\\.");
        if (modelField.length != 2) {
            throw new IllegalArgumentException("'field' argument should be in this form 'Model.field'");
        }
        return modelField;
    }

    /**
	 * Uses reflection to get the value of a field, represented by its name,
	 * from <code>Object obj</code>
	 * 
	 * @param obj
	 *            object to get the value of the field from.
	 * @param fieldName
	 *            name of the desired field.
	 * @return the value of the field from the object
	 * @throws Exception
	 *             specified by these methods {@link Class#getField(String)} &
	 *             {@link Field#get(Object)}
	 */
    static Object getFieldValue(Object obj, String fieldName) throws Exception {
        try {
            return obj.getClass().getField(fieldName).get(obj);
        } catch (Exception e) {
            return "";
        }
    }

    private static Object checkForConfirmElement(Closure body, String html) {
        if (body != null) {
            return formatConfirmElement(html);
        } else {
            return null;
        }
    }

    private static Object formatConfirmElement(String html) {
        int startIndexOfId = html.indexOf("id=\"") + 4;
        int endIndexOfId = html.indexOf("\"", startIndexOfId);
        String id = html.substring(startIndexOfId, endIndexOfId);
        return html.replace("id=\"" + id + "\"", "id=\"" + id + "Confirm\"");
    }
}
