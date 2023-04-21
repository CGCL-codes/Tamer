package ru.adv.mozart.processor;

import ru.adv.util.InputOutput;
import ru.adv.mozart.framework.RequestContext;
import ru.adv.mozart.Defaults;
import ru.adv.io.UnknownIOSourceException;

/**
 * User: roma
 * Date: 26.05.2007
 * Time: 18:44:25
 */
public class ProcessorUtils {

    public static InputOutput getIO(final RequestContext context) throws UnknownIOSourceException {
        return context.createInputOutput(getTemplateURL(getXsltFile(context)));
    }

    public static InputOutput getIO(final RequestContext context, final String src) throws UnknownIOSourceException {
        return context.createInputOutput(getTemplateURL(src));
    }

    public static String getTemplateURL(final String src) {
        String result = src;
        if (result.indexOf("://") <= 0) {
            result = Defaults.PREFIX_TEMPLATES_NAME + "://" + result;
        }
        return result;
    }

    public static String getXsltFile(RequestContext context) {
        String xslfile = context.getDefaultXSL();
        if (context.getInfo().getXslt() != null) {
            xslfile = context.getInfo().getXslt();
        }
        return xslfile;
    }
}
