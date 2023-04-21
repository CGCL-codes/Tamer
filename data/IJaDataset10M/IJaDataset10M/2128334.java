package org.o14x.migale;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import org.o14x.migale.parser.MigaleComponent;
import org.o14x.migale.parser.MigaleParser;
import org.o14x.migale.renderkit.AdapterDefinition;
import org.o14x.migale.renderkit.ComponentDefinition;
import org.o14x.migale.renderkit.DefaultAdapterDefinition;
import org.o14x.migale.renderkit.DefaultComponentDefinition;
import org.o14x.migale.renderkit.DefaultRenderKit;
import org.o14x.migale.renderkit.RenderKit;
import org.o14x.migale.script.SpecialObjects;
import org.o14x.utilz.FileHelper;
import org.o14x.utilz.exception.TechException;
import org.o14x.utilz.xor.XMLObjectReader;

/**
 * Main class to use the Migale engine.
 * 
 * @author Olivier Dangr�aux
 */
public class MigaleEngine {

    private HashMap renderKitMap = new HashMap();

    private static MigaleEngine migaleEngine;

    public static MigaleEngine getInstance() {
        if (migaleEngine == null) {
            migaleEngine = new MigaleEngine();
        }
        return migaleEngine;
    }

    /**
	 * Initializes the specified render kit with the given configuration file.
	 * Calling this method is optional. It's override default configuration of the render kit.
	 * 
	 * @param configFilePath The path of the configuration file. This configuration file must be located in the classpath.
	 * 
	 * @throws TechException In case of a technical error.
	 */
    public void initRenderKit(String configFilePath) throws TechException {
        if (configFilePath != null) {
            XMLObjectReader xor = new XMLObjectReader(DefaultRenderKit.class.getName());
            xor.registerImplementation(ComponentDefinition.class.getName(), DefaultComponentDefinition.class.getName());
            xor.registerImplementation(AdapterDefinition.class.getName(), DefaultAdapterDefinition.class.getName());
            RenderKit migaleRenderKit = (RenderKit) xor.parse(FileHelper.getResourceAsStream(configFilePath));
            renderKitMap.put(migaleRenderKit.getName(), migaleRenderKit);
        }
    }

    /**
	 * Parses the specified UI definition file and render it with the given render kit.
	 * 
	 * @param uiDefinitionFilePath	The path of the ui definition file. This path can be classpath relative or physical.
	 * @param renderKitName			The name of the render kit to use.
	 * 
	 * @return An object representing the rendered representation the ui definition. The type of this parameter is depends on the render kit used.
	 * 
	 * @throws TechException In case of technical error.
	 */
    public Object render(String uiDefinitionFilePath, String renderKitName) throws TechException {
        return render(uiDefinitionFilePath, null, renderKitName);
    }

    /**
	 * Parses the specified UI definition file and render it with the appropriate render kit.<br/>
	 * The renderkit to use is supposed to be specified in the renderkit attribute of the root &lt;migale&gt; tag.
	 * 
	 * @param uiDefinitionFilePath	The path of the ui definition file. This path can be classpath relative or physical.
	 * 
	 * @return An object representing the rendered representation the ui definition. The type of this parameter is depends on the render kit used.
	 * 
	 * @throws TechException In case of technical error.
	 */
    public Object render(String uiDefinitionFilePath) throws TechException {
        return render(uiDefinitionFilePath, null, null);
    }

    /**
	 * Parse the specified UI definition file and render it with the given render kit.
	 * 
	 * @param uiDefinitionFilePath	The path of the ui definition file. This path can be classpath relative or physical.
	 * @param renderContext			The render context.
	 * @param renderKitName			The name of the render kit to use.
	 * 
	 * @return An object representing the rendered representation the ui definition. The type of this parameter is depends on the render kit used.
	 * 
	 * @throws TechException In case of technical error.
	 */
    public Object render(String uiDefinitionFilePath, RenderContext renderContext, String renderKitName) throws TechException {
        Object renderedObject = null;
        RenderKit renderKit = null;
        if (renderKitName != null) {
            renderKit = getRenderKit(renderKitName);
        }
        if (renderContext == null) {
            renderContext = Factory.getNewRenderContext(renderKit);
        } else {
            renderContext.setRenderKit(renderKit);
        }
        if (renderContext.getRenderKit() == null) {
            try {
                InputStream inputStream = FileHelper.findRessourceOrFile(uiDefinitionFilePath);
                MigaleComponent migaleComponent = new MigaleParser().parse(inputStream);
                inputStream.close();
                renderKitName = migaleComponent.getAttribute("renderkit");
                if (renderKitName == null) {
                    throw new TechException("No renderkit attribute was found in tag <migale> in file : " + uiDefinitionFilePath);
                }
                renderKit = getRenderKit(renderKitName);
                renderContext.setRenderKit(renderKit);
                renderContext.setScriptBinding(Factory.getNewScriptBinding(renderKit));
                renderContext.getScriptBinding().setObject(SpecialObjects.M_ENGINE, this);
                renderedObject = renderKit.render(migaleComponent, renderContext);
            } catch (IOException e) {
                throw new TechException("Error accessing definition file : " + uiDefinitionFilePath, e);
            }
        } else {
            renderContext.getScriptBinding().setObject(SpecialObjects.M_ENGINE, this);
            renderedObject = renderContext.getRenderKit().parse(uiDefinitionFilePath, renderContext);
        }
        return renderedObject;
    }

    /**
	 * Parse the specified UI definition file and render it with the render kit whose name is in the given render context.
	 * 
	 * @param uiDefinitionFilePath	The path of the ui definition file. This path can be classpath relative or physical.
	 * @param renderContext			The render context.
	 * 
	 * @return An object representing the rendered representation the ui definition. The type of this parameter is depends on the render kit used.
	 * 
	 * @throws TechException In case of technical error.
	 */
    public Object render(String uiDefinitionFilePath, RenderContext renderContext) throws TechException {
        return render(uiDefinitionFilePath, renderContext, null);
    }

    /**
	 * Return a new instance of the specified render kit.
	 * 
	 * @param renderKitName A render kit name.
	 * 
	 * @return A new instance of the specified render kit.
	 * 
	 * @throws TechException In case of technical error.
	 */
    public RenderKit getRenderKit(String renderKitName) throws TechException {
        RenderKit renderKit = (RenderKit) renderKitMap.get(renderKitName);
        if (renderKit == null) {
            initRenderKit("migale-" + renderKitName + "-renderkit.xml");
            renderKit = (RenderKit) renderKitMap.get(renderKitName);
        }
        return renderKit;
    }

    /**
	 * Launch method for MigaleEngine as a standalone application.
	 * 
	 * @param args The arguments passed to the program.
	 */
    public static void main(String[] args) {
        try {
            if (args.length < 1 || args.length > 2 || "-h".equals(args[0])) {
                String message = "MigaleEngine";
                message += "\nParameters : migale_ui_definition_file [render_kit_name]";
            } else {
                String uiDefinitionFilePath = args[0];
                String renderKitName = null;
                if (args.length > 1) {
                    renderKitName = args[1];
                }
                getInstance().render(uiDefinitionFilePath, renderKitName);
            }
        } catch (TechException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
