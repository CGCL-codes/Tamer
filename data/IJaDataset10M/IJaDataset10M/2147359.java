package uk.ac.ncl.cs.instantsoap.commandlineprocessor.templateLoader;

import uk.ac.ncl.cs.instantsoap.JobExecutionException;
import uk.ac.ncl.cs.instantsoap.commandlineprocessor.Template;
import uk.ac.ncl.cs.instantsoap.commandlineprocessor.xmlParser.TemplateExtractor;
import uk.ac.ncl.cs.instantsoap.commandlineprocessor.xmlParser.XMLParseException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

/**
 * An implementation of TemplateLoader that locates and retrieves templates using resource directories.
 * <p/>
 * The resource
 * identified by the TEMPLATE_LIST field should contain one line for each template. It should contain just the template
 * name. In the directory identified by TEMPLATES_FOLDER, there must be a corresponding templates file, formatted in
 * the appropriate template XML.
 * <p/>
 * For example, if you have a template called "Bob", you should add a line containing "Bob" to the resource at
 * TEMPLATE_LIST, and then create a file called "Bob" in the directory TEMPLATES_FOLDER.
 *
 * @author Cheng-Yang(Louis) Tang
 * @author Matthew Pocock
 */
public class TemplateLoaderImpl implements TemplateLoader {

    public static final String TEMPLATE_LIST = "META-INF/InstantSOAP/template.list";

    public static final String TEMPLATES_FOLDER = "META-INF/InstantSOAP/Templates/";

    private final TemplateExtractor templateExtractor;

    public TemplateLoaderImpl(TemplateExtractor templateExtractor) throws JobExecutionException {
        if (templateExtractor == null) {
            throw new JobExecutionException("Input argument must not be null");
        }
        this.templateExtractor = templateExtractor;
    }

    public Map<String, Template> loadTemplates() throws JobExecutionException {
        Map<String, Template> templateMap = new HashMap<String, Template>();
        Enumeration<URL> templateLists = retrieveTemplateLists(TEMPLATE_LIST);
        while (templateLists.hasMoreElements()) {
            List<String> templateNames = parseTemplateList(templateLists.nextElement());
            for (String s : templateNames) {
                Template template = null;
                try {
                    template = parseTemplate(TEMPLATES_FOLDER + s);
                } catch (XMLParseException e) {
                    throw new JobExecutionException(e);
                }
                templateMap.put(template.getName(), template);
            }
        }
        return templateMap;
    }

    private Enumeration<URL> retrieveTemplateLists(String templateListName) throws JobExecutionException {
        Enumeration<URL> templateListUrls;
        try {
            templateListUrls = TemplateLoaderImpl.class.getClassLoader().getResources(templateListName);
        } catch (IOException e) {
            throw new JobExecutionException("Unable to load " + templateListName, e);
        }
        return templateListUrls;
    }

    private List<String> parseTemplateList(URL templateList) throws JobExecutionException {
        List<String> result = new ArrayList<String>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(templateList.openConnection().getInputStream()));
            String oneLine;
            while ((oneLine = reader.readLine()) != null) {
                result.add(oneLine);
            }
            reader.close();
        } catch (IOException e) {
            throw new JobExecutionException(e);
        }
        return result;
    }

    private Template parseTemplate(String templateName) throws XMLParseException {
        return templateExtractor.extract(TemplateLoaderImpl.class.getClassLoader().getResourceAsStream(templateName));
    }
}
