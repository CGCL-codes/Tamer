package org.code4flex.codegenerators.velocity.actionscript;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.tools.generic.EscapeTool;
import org.code4flex.codegenerators.velocity.VelocityCodeGenerator;
import org.code4flex.generators.model.actionscript.cairngorm.classes.FlexCairngormControllerClass;

/**
 * 
 * @author Facundo Merighi
 * @version $Revision: 1.1 $
 */
public class ActionScriptFlexCairngormControllerCodeGenerator extends VelocityCodeGenerator {

    private FlexCairngormControllerClass controller;

    private String finalPath;

    @Override
    public void generate() {
        try {
            this.initVelocityTemplate();
            String namespacePath = getNamespacePath(controller);
            this.setFinalPath(this.mainGenerator.getProyectDestPath() + File.separatorChar + "src" + File.separatorChar + namespacePath);
            createPathIfDontExist();
            FileWriter fwriter = new FileWriter(getFinalPath() + File.separator + controller.getClassName() + ".as");
            VelocityContext context = new VelocityContext();
            context.put("controller", controller);
            context.put("package", controller.getNamespace());
            context.put("esc", new EscapeTool());
            Template template = this.getVelocityTemplate();
            template.merge(context, fwriter);
            fwriter.close();
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        } catch (ParseErrorException e) {
            e.printStackTrace();
        } catch (MethodInvocationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setFinalPath(String finalPath) {
        this.finalPath = finalPath;
    }

    @Override
    public String getFinalPath() {
        return finalPath;
    }

    public void setController(FlexCairngormControllerClass controller) {
        this.controller = controller;
    }
}
