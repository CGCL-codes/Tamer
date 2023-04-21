package org.gwt.mosaic.application.rebind;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import org.gwt.beansbinding.core.client.AutoBinding;
import org.gwt.beansbinding.core.client.BeanProperty;
import org.gwt.beansbinding.core.client.Binding;
import org.gwt.beansbinding.core.client.Bindings;
import org.gwt.mosaic.actions.client.Action;
import org.gwt.mosaic.actions.client.ActionMap;
import org.gwt.mosaic.actions.client.CommandAction;
import org.gwt.mosaic.application.client.Application;
import org.gwt.mosaic.application.client.CmdAction;
import org.gwt.mosaic.application.client.util.ApplicationFramework;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class ApplicationFrameworkGenerator extends Generator {

    protected class CmdActionDescriptor {

        public String name;

        public String description;

        public String image;

        public String enabledProperty;

        public String selectedProperty;

        public JMethod method;

        public JClassType classType;

        public CmdActionDescriptor(String name, JMethod method, JClassType classType) {
            this.name = name;
            this.method = method;
            this.classType = classType;
        }
    }

    protected List<CmdActionDescriptor> cmdActionDescriptorList = new ArrayList<CmdActionDescriptor>();

    private TreeLogger logger;

    private GeneratorContext context;

    @Override
    public String generate(TreeLogger logger, GeneratorContext context, String typeName) throws UnableToCompleteException {
        this.logger = logger;
        this.context = context;
        JClassType classType = null;
        try {
            classType = context.getTypeOracle().getType(Application.class.getName());
        } catch (NotFoundException e) {
            logger.log(TreeLogger.ERROR, "Cannot find class " + Application.class.getName(), e);
            throw new UnableToCompleteException();
        }
        JClassType[] foundClassTypes = classType.getSubtypes();
        if (foundClassTypes != null && foundClassTypes.length > 0) {
            for (JClassType t : foundClassTypes) {
                if (t.isAbstract()) {
                    continue;
                }
                examine(t);
            }
            if (cmdActionDescriptorList.size() > 0) {
                return doGenerate(typeName);
            }
        }
        return null;
    }

    private String doGenerate(String typeName) throws UnableToCompleteException {
        TypeOracle typeOracle = context.getTypeOracle();
        try {
            JClassType type = typeOracle.getType(typeName);
            String packageName = type.getPackage().getName();
            String simpleClassName = type.getSimpleSourceName();
            String className = simpleClassName + "Initializer";
            String qualifiedBeanClassName = packageName + "." + className;
            SourceWriter sourceWriter = getSourceWriter(packageName, className, type);
            if (sourceWriter == null) {
                return qualifiedBeanClassName;
            }
            sourceWriter.println("@Override");
            sourceWriter.println("public void setupActions() {");
            sourceWriter.indent();
            sourceWriter.println("final ActionMap actionMap = Application.getInstance().getContext().getActionMap();");
            sourceWriter.println("CommandAction action;");
            for (CmdActionDescriptor descriptor : cmdActionDescriptorList) {
                sourceWriter.println("// ============ CmdAction: " + descriptor.name + " ============");
                sourceWriter.println("{");
                sourceWriter.indent();
                sourceWriter.println("final " + descriptor.classType.getErasedType().getQualifiedSourceName() + " application = (" + descriptor.classType.getErasedType().getQualifiedSourceName() + ") Application.getInstance();");
                sourceWriter.println("action = new CommandAction(((" + descriptor.classType.getErasedType().getQualifiedSourceName() + "Constants) application.getContext().getConstants())." + descriptor.name + "(), new Command() {");
                sourceWriter.indent();
                sourceWriter.println("public void execute() {");
                sourceWriter.indent();
                sourceWriter.println("application." + descriptor.method.getName() + "();");
                sourceWriter.outdent();
                sourceWriter.println("}");
                sourceWriter.outdent();
                sourceWriter.println("});");
                if (descriptor.description != null) {
                    sourceWriter.println("action.putValue(Action.SHORT_DESCRIPTION, ((" + descriptor.classType.getErasedType().getQualifiedSourceName() + "Constants) application.getContext().getConstants())." + descriptor.description + "());");
                }
                if (descriptor.image != null) {
                    sourceWriter.println("action.putValue(Action.SMALL_ICON, ((" + descriptor.classType.getErasedType().getQualifiedSourceName() + "ImageBundle) application.getContext().getImageBundle())." + descriptor.image + "());");
                }
                if (descriptor.selectedProperty != null) {
                    sourceWriter.println("Binding binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, application, BeanProperty.<" + descriptor.classType.getErasedType().getQualifiedSourceName() + ", String> create(\"" + descriptor.selectedProperty + "\"), action, BeanProperty.<Action, String> create(\"selected\"));");
                    sourceWriter.println("binding.bind();");
                }
                if (descriptor.enabledProperty != null) {
                    sourceWriter.println("Binding binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, application, BeanProperty.<" + descriptor.classType.getErasedType().getQualifiedSourceName() + ", String> create(\"" + descriptor.enabledProperty + "\"), action, BeanProperty.<Action, String> create(\"enabled\"));");
                    sourceWriter.println("binding.bind();");
                }
                sourceWriter.println("actionMap.put(\"" + descriptor.name + "\", action);");
                sourceWriter.outdent();
                sourceWriter.println("}");
            }
            sourceWriter.outdent();
            sourceWriter.println("}");
            sourceWriter.commit(logger);
            return qualifiedBeanClassName;
        } catch (Exception e) {
            logger.log(TreeLogger.ERROR, "Unable to generate code for " + typeName, e);
            throw new UnableToCompleteException();
        }
    }

    protected SourceWriter getSourceWriter(String packageName, String className, JClassType superType) {
        PrintWriter printWriter = context.tryCreate(logger, packageName, className);
        if (printWriter == null) {
            return null;
        }
        ClassSourceFileComposerFactory composerFactory = new ClassSourceFileComposerFactory(packageName, className);
        composerFactory.addImport(ApplicationFramework.class.getName());
        composerFactory.addImport(Application.class.getName());
        composerFactory.addImport(Action.class.getName());
        composerFactory.addImport(ActionMap.class.getName());
        composerFactory.addImport(CommandAction.class.getName());
        composerFactory.addImport(Command.class.getName());
        composerFactory.addImport(Window.class.getName());
        composerFactory.addImport(Binding.class.getName());
        composerFactory.addImport(Bindings.class.getName());
        composerFactory.addImport(BeanProperty.class.getName());
        composerFactory.addImport(AutoBinding.class.getName());
        composerFactory.setSuperclass(ApplicationFramework.class.getName());
        return composerFactory.createSourceWriter(context, printWriter);
    }

    protected void examine(JClassType t) {
        logger.log(TreeLogger.INFO, "Examining " + t.getQualifiedSourceName());
        lookupForCmdActionAnnotations(t);
        logger.log(TreeLogger.INFO, t.getQualifiedSourceName() + " done");
    }

    protected boolean isActionMethod(JMethod method) {
        if (!method.isPublic() || !method.isAnnotationPresent(CmdAction.class)) {
            return false;
        }
        JType returnType = method.getReturnType();
        if (returnType == null || !"void".equals(returnType.getErasedType().getQualifiedSourceName())) {
            return false;
        }
        JParameter[] parameters = method.getParameters();
        if (parameters == null || parameters.length != 0) {
            return false;
        }
        return true;
    }

    protected List<JMethod> getActionMethods(JClassType type) {
        List<JMethod> list = new ArrayList<JMethod>();
        JMethod[] methods = type.getOverridableMethods();
        if (methods != null) {
            for (JMethod method : methods) {
                if (isActionMethod(method)) {
                    list.add(method);
                }
            }
        }
        return list;
    }

    protected void lookupForCmdActionAnnotations(JClassType t) {
        List<JMethod> methods = getActionMethods(t);
        if (methods != null) {
            for (JMethod method : methods) {
                CmdActionDescriptor cmdActionDescriptor = new CmdActionDescriptor(method.getName(), method, t);
                CmdAction cmdAction = method.getAnnotation(CmdAction.class);
                if (cmdAction.name() != null && cmdAction.name().length() > 0) {
                    cmdActionDescriptor.name = cmdAction.name();
                }
                if (cmdAction.description() != null && cmdAction.description().length() > 0) {
                    cmdActionDescriptor.description = cmdAction.description();
                }
                if (cmdAction.image() != null && cmdAction.image().length() > 0) {
                    cmdActionDescriptor.image = cmdAction.image();
                }
                if (cmdAction.enabledProperty() != null && cmdAction.enabledProperty().length() > 0) {
                    cmdActionDescriptor.enabledProperty = cmdAction.enabledProperty();
                }
                if (cmdAction.selectedProperty() != null && cmdAction.selectedProperty().length() > 0) {
                    cmdActionDescriptor.selectedProperty = cmdAction.selectedProperty();
                }
                cmdActionDescriptorList.add(cmdActionDescriptor);
            }
        }
    }
}
