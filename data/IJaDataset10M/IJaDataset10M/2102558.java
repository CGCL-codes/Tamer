package hudson.zipscript.resource.macrolib;

import hudson.zipscript.ResourceContainer;
import hudson.zipscript.parser.ExpressionParser;
import hudson.zipscript.parser.exception.ExecutionException;
import hudson.zipscript.parser.exception.ParseException;
import hudson.zipscript.parser.template.data.ParsingResult;
import hudson.zipscript.parser.template.data.ParsingSession;
import hudson.zipscript.parser.template.element.Element;
import hudson.zipscript.parser.template.element.directive.macrodir.MacroDirective;
import hudson.zipscript.parser.template.element.lang.TextDefaultElementFactory;
import hudson.zipscript.parser.template.element.lang.variable.adapter.VariableAdapterFactory;
import hudson.zipscript.parser.util.IOUtil;
import hudson.zipscript.resource.Resource;
import hudson.zipscript.resource.ResourceLoader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MacroManager {

    private Map macroLibraries = new HashMap();

    private ResourceContainer resourceContainer;

    private Object resourceLoaderParameter;

    public void addMacroLibrary(String namespace, String resourcePath, ResourceLoader resourceLoader, VariableAdapterFactory variableAdapterFactory) throws ParseException {
        if (null == macroLibraries) macroLibraries = new HashMap();
        Resource resource = resourceLoader.getResource(resourcePath, resourceLoaderParameter);
        MacroLibrary ml = load(namespace, resource);
        if (ml.getMacroNames().size() > 0) {
            macroLibraries.put(namespace, ml);
        }
    }

    private MacroLibrary load(String namespace, Resource resource) throws ParseException {
        String contents = IOUtil.toString(resource.getInputStream());
        ParsingResult pr = ExpressionParser.getInstance().parse(contents, resourceContainer.getComponents(), TextDefaultElementFactory.INSTANCE, 0, resourceContainer, ParsingSession.PARSING_CONTEXT_MACRO);
        List l = pr.getElements();
        MacroLibrary macroLibrary = new MacroLibrary(namespace, resource);
        for (Iterator i = l.iterator(); i.hasNext(); ) {
            Element e = (Element) i.next();
            if (e instanceof MacroDirective) {
                macroLibrary.addMacroDefinition((MacroDirective) e);
            }
        }
        return macroLibrary;
    }

    public MacroDirective reloadMacro(String name, String namespace, MacroProvider defaultMacroProvider) throws ParseException {
        if (null != namespace) {
            String path = defaultMacroProvider.getMacroImportPath(namespace);
            if (null != path) {
                macroLibraries.remove(path);
            } else {
                MacroLibrary ml = (MacroLibrary) macroLibraries.get(namespace);
                Resource r = ml.getResource();
                ml = load(namespace, r);
                macroLibraries.put(namespace, ml);
            }
        }
        return getMacro(name, namespace, defaultMacroProvider);
    }

    public MacroDirective getMacro(String name, String namespace, MacroProvider defaultMacroProvider) {
        if (null == namespace) {
            if (null != defaultMacroProvider) return defaultMacroProvider.getMacro(name); else return null;
        } else {
            String path = defaultMacroProvider.getMacroImportPath(namespace);
            if (null != path) {
                MacroLibrary lib = (MacroLibrary) macroLibraries.get(path);
                if (null == lib) {
                    try {
                        addMacroLibrary(path, path, resourceContainer.getMacroLibResourceLoader(), resourceContainer.getVariableAdapterFactory());
                    } catch (ParseException e) {
                        throw new ExecutionException(e.getMessage(), null);
                    }
                }
                lib = (MacroLibrary) macroLibraries.get(path);
                if (null != lib) return lib.getMacro(name); else return null;
            } else {
                MacroLibrary lib = (MacroLibrary) macroLibraries.get(namespace);
                if (null != lib) return lib.getMacro(name); else return null;
            }
        }
    }

    public ResourceContainer getResourceContainer() {
        return resourceContainer;
    }

    public void setResourceContainer(ResourceContainer resourceContainer) {
        this.resourceContainer = resourceContainer;
    }

    public Object getResourceLoaderParameter() {
        return resourceLoaderParameter;
    }

    public void setResourceLoaderParameter(Object resourceLoaderParameter) {
        this.resourceLoaderParameter = resourceLoaderParameter;
    }
}
