package com.google.gdt.eclipse.designer.uibinder.model.util;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gdt.eclipse.designer.GwtToolkitDescription;
import com.google.gdt.eclipse.designer.model.widgets.support.IDevModeBridge;
import com.google.gdt.eclipse.designer.uibinder.IExceptionConstants;
import com.google.gdt.eclipse.designer.uibinder.model.widgets.UIObjectInfo;
import com.google.gdt.eclipse.designer.uibinder.parser.UiBinderContext;
import com.google.gdt.eclipse.designer.uibinder.parser.UiBinderParser;
import com.google.gdt.eclipse.designer.util.Utils;
import org.eclipse.wb.core.model.ObjectInfo;
import org.eclipse.wb.core.model.broadcast.ObjectInfoDelete;
import org.eclipse.wb.core.model.broadcast.ObjectInfoPresentationDecorateText;
import org.eclipse.wb.internal.core.DesignerPlugin;
import org.eclipse.wb.internal.core.model.ObjectInfoVisitor;
import org.eclipse.wb.internal.core.model.description.CreationDescription;
import org.eclipse.wb.internal.core.model.description.CreationDescription.TypeParameterDescription;
import org.eclipse.wb.internal.core.model.description.helpers.ComponentDescriptionHelper;
import org.eclipse.wb.internal.core.model.variable.NamesManager;
import org.eclipse.wb.internal.core.model.variable.NamesManager.ComponentNameDescription;
import org.eclipse.wb.internal.core.utils.ast.AstEditor;
import org.eclipse.wb.internal.core.utils.ast.AstNodeUtils;
import org.eclipse.wb.internal.core.utils.ast.BodyDeclarationTarget;
import org.eclipse.wb.internal.core.utils.ast.DomGenerics;
import org.eclipse.wb.internal.core.utils.exception.DesignerException;
import org.eclipse.wb.internal.core.utils.execution.ExecutionUtils;
import org.eclipse.wb.internal.core.utils.execution.RunnableEx;
import org.eclipse.wb.internal.core.utils.execution.RunnableObjectEx;
import org.eclipse.wb.internal.core.utils.jdt.core.CodeUtils;
import org.eclipse.wb.internal.core.utils.reflect.ReflectionUtils;
import org.eclipse.wb.internal.core.utils.state.EditorState;
import org.eclipse.wb.internal.core.utils.xml.DocumentElement;
import org.eclipse.wb.internal.core.xml.model.XmlObjectInfo;
import org.eclipse.wb.internal.core.xml.model.broadcast.XmlObjectAdd;
import org.eclipse.wb.internal.core.xml.model.description.ComponentDescription;
import org.eclipse.wb.internal.core.xml.model.utils.XmlObjectUtils;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.ui.refactoring.RenameSupport;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Support for "ui:field" attribute and "@UiField" in Java.
 * 
 * @author scheglov_ke
 * @coverage GWT.UiBinder.model
 */
public final class NameSupport {

    /**
   * Sometimes widget has no text, so it is hard to identify in components tree. But if it has name,
   * would be nice to show it.
   */
    public static void decoratePresentationWithName(XmlObjectInfo root) {
        root.addBroadcastListener(new ObjectInfoPresentationDecorateText() {

            public void invoke(ObjectInfo object, String[] text) throws Exception {
                if (object instanceof XmlObjectInfo) {
                    XmlObjectInfo xObject = (XmlObjectInfo) object;
                    String name = getName(xObject);
                    if (name != null) {
                        text[0] = text[0] + " - " + name;
                    }
                }
            }
        });
    }

    /**
   * Deletes artifacts of widget from Java source.
   */
    public static void removeName_onDelete(XmlObjectInfo rootObject) {
        rootObject.addBroadcastListener(new ObjectInfoDelete() {

            @Override
            public void before(ObjectInfo parent, ObjectInfo child) throws Exception {
                if (child instanceof XmlObjectInfo) {
                    XmlObjectInfo object = (XmlObjectInfo) child;
                    removeName(object);
                }
            }
        });
    }

    /**
   * If widget marked with <code>"UiBinder.createFieldProvided"</code> then during creation ensure
   * <code>@UiField(provided=true)</code> using default Java creation.
   */
    public static void ensureFieldProvided_onCreate(XmlObjectInfo rootObject) {
        rootObject.addBroadcastListener(new XmlObjectAdd() {

            @Override
            public void after(ObjectInfo parent, XmlObjectInfo child) throws Exception {
                if (XmlObjectUtils.hasTrueParameter(child, "UiBinder.createFieldProvided")) {
                    NameSupport nameSupport = new NameSupport(child);
                    nameSupport.createFieldProvided();
                }
            }
        });
    }

    /**
   * @return the existing name of the widget, or <code>null</code>.
   */
    public static String getName(XmlObjectInfo object) {
        if (XmlObjectUtils.isImplicit(object)) {
            return null;
        }
        return new NameSupport(object).getName();
    }

    /**
   * @return the existing or new name of the widget, can not be <code>null</code>.
   */
    public static String ensureName(XmlObjectInfo object) throws Exception {
        if (XmlObjectUtils.isImplicit(object)) {
            throw new IllegalArgumentException();
        }
        final NameSupport nameSupport = new NameSupport(object);
        return ExecutionUtils.runObject(object, new RunnableObjectEx<String>() {

            public String runObject() throws Exception {
                return nameSupport.ensureName();
            }
        });
    }

    /**
   * Removes "@UiField" of the widget.
   */
    public static void removeName(XmlObjectInfo object) throws Exception {
        if (XmlObjectUtils.isImplicit(object)) {
            return;
        }
        final NameSupport nameSupport = new NameSupport(object);
        ExecutionUtils.run(object, new RunnableEx() {

            public void run() throws Exception {
                nameSupport.removeName();
            }
        });
    }

    /**
   * Sets new name for "@UiField" of the widget.
   */
    public static void setName(XmlObjectInfo object, final String name) throws Exception {
        if (XmlObjectUtils.isImplicit(object)) {
            throw new IllegalArgumentException();
        }
        m_renaming = true;
        try {
            final NameSupport nameSupport = new NameSupport(object);
            ExecutionUtils.run(object, new RunnableEx() {

                public void run() throws Exception {
                    nameSupport.setName(name);
                }
            });
        } finally {
            m_renaming = false;
        }
    }

    /**
   * @return the error message if given name is not valid, or <code>null</code> if this name can be
   *         used.
   */
    public static String validateName(XmlObjectInfo object, String name) throws Exception {
        return new NameSupport(object).validateName(name);
    }

    /**
   * @return the {@link XmlObjectInfo} with the given name, may be <code>null</code>.
   */
    public static XmlObjectInfo getObject(XmlObjectInfo root, final String name) {
        final XmlObjectInfo result[] = { null };
        root.accept(new ObjectInfoVisitor() {

            @Override
            public void endVisit(ObjectInfo object) throws Exception {
                if (object instanceof XmlObjectInfo) {
                    XmlObjectInfo xmlObject = (XmlObjectInfo) object;
                    if (ObjectUtils.equals(getName(xmlObject), name)) {
                        result[0] = xmlObject;
                    }
                }
            }
        });
        return result[0];
    }

    private final XmlObjectInfo m_object;

    private final UiBinderContext m_context;

    private NameSupport(XmlObjectInfo object) {
        m_object = object;
        m_context = (UiBinderContext) m_object.getContext();
    }

    /**
   * @return the existing name of the widget, or <code>null</code>.
   */
    public String getName() {
        String nameAttribute = getNameAttribute();
        return m_object.getAttribute(nameAttribute);
    }

    /**
   * @return the existing or new name of the widget, can not be <code>null</code>.
   */
    public String ensureName() throws Exception {
        String name = getName();
        if (name == null) {
            name = generateName();
            setName(name);
        }
        return name;
    }

    /**
   * Removes "@UiField" of the widget.
   */
    private void removeName() throws Exception {
        String name = getName();
        if (name != null) {
            {
                String nameAttribute = getNameAttribute();
                m_object.getElement().setAttribute(nameAttribute, null);
            }
            {
                AstEditor editor = getEditor();
                TypeDeclaration typeDeclaration = editor.getPrimaryType();
                for (MethodDeclaration methodDeclaration : typeDeclaration.getMethods()) {
                    if (EventHandlerProperty.isObjectHandler(methodDeclaration, name)) {
                        editor.removeBodyDeclaration(methodDeclaration);
                    }
                }
                VariableDeclaration variable = getBinderField(typeDeclaration, name);
                FieldDeclaration field = AstNodeUtils.getEnclosingFieldDeclaration(variable);
                editor.removeBodyDeclaration(field);
            }
        }
    }

    /**
   * Sets new name for "@UiField" of the widget.
   */
    private void setName(String name) throws Exception {
        AstEditor editor = getEditor();
        TypeDeclaration typeDeclaration = editor.getPrimaryType();
        String oldName = getName();
        VariableDeclaration oldVariable = getBinderField(typeDeclaration, oldName);
        {
            String nameAttribute = getNameAttribute();
            m_object.setAttribute(nameAttribute, name);
        }
        if (oldVariable != null) {
            IType modelType = m_context.getFormType();
            IField modelField = modelType.getField(oldName);
            RenameSupport renameSupport = RenameSupport.create(modelField, name, RenameSupport.UPDATE_REFERENCES);
            renameSupport.perform(DesignerPlugin.getShell(), DesignerPlugin.getActiveWorkbenchWindow());
            return;
        } else {
            Class<?> componentClass = m_object.getDescription().getComponentClass();
            String source = "@com.google.gwt.uibinder.client.UiField " + ReflectionUtils.getCanonicalName(componentClass) + " " + name + ";";
            BodyDeclarationTarget target = getNewFieldTarget(typeDeclaration);
            editor.addFieldDeclaration(source, target);
        }
    }

    /**
   * @return the error message if given name is not valid, or <code>null</code> if this name can be
   *         used.
   */
    private String validateName(String name) throws Exception {
        {
            IJavaProject javaProject = m_object.getContext().getJavaProject();
            String sourceLevel = javaProject.getOption(JavaCore.COMPILER_SOURCE, true);
            String complianceLevel = javaProject.getOption(JavaCore.COMPILER_COMPLIANCE, true);
            IStatus status = JavaConventions.validateFieldName(name, sourceLevel, complianceLevel);
            if (status.matches(IStatus.ERROR)) {
                return status.getMessage();
            }
        }
        {
            Set<String> existingNames = getExistingNames();
            if (existingNames.contains(name)) {
                return "Field '" + name + "' already exists.";
            }
        }
        return null;
    }

    /**
   * Creates <code>@UiField(provided=true)</code> using default Java creation.
   */
    private void createFieldProvided() throws Exception {
        if (!UiBinderParser.hasUiFieldUiFactorySupport(m_context.getJavaProject())) {
            throw new DesignerException(IExceptionConstants.UI_FIELD_FACTORY_FEATURE);
        }
        AstEditor editor = getEditor();
        EditorState.get(editor).initialize(GwtToolkitDescription.INSTANCE.getId(), m_object.getContext().getClassLoader());
        String name = generateName();
        {
            String nameAttribute = getNameAttribute();
            m_object.setAttribute(nameAttribute, name);
        }
        Class<?> componentClass = m_object.getDescription().getComponentClass();
        org.eclipse.wb.internal.core.model.description.ComponentDescription javaDescription = ComponentDescriptionHelper.getDescription(editor, componentClass);
        CreationDescription creationDescription = javaDescription.getCreation(null);
        String fieldTypeName = ReflectionUtils.getCanonicalName(componentClass);
        String creationSource = creationDescription.getSource();
        {
            Set<Entry<String, TypeParameterDescription>> entrySet = creationDescription.getTypeParameters().entrySet();
            if (!entrySet.isEmpty()) {
                fieldTypeName += "<";
                for (Entry<String, TypeParameterDescription> entry : entrySet) {
                    String typeArgument = entry.getValue().getTypeName();
                    if (!fieldTypeName.endsWith("<")) {
                        fieldTypeName += ", ";
                    }
                    fieldTypeName += typeArgument;
                    creationSource = StringUtils.replace(creationSource, "%" + entry.getKey() + "%", typeArgument);
                }
                fieldTypeName += ">";
            }
        }
        addUiFieldJava(componentClass, fieldTypeName, name, creationSource);
    }

    /**
   * Adds @UiField with given type, name and initializer.
   */
    public void addUiFieldJava(Class<?> fieldClass, String fieldTypeName, String name, String initializer) throws Exception {
        AstEditor editor = getEditor();
        TypeDeclaration typeDeclaration = editor.getPrimaryType();
        List<String> lines;
        {
            lines = Lists.newArrayList();
            String source = "@com.google.gwt.uibinder.client.UiField(provided=true) ";
            source += fieldTypeName + " " + name + " = " + initializer + ";";
            Collections.addAll(lines, StringUtils.split(source, '\n'));
        }
        BodyDeclarationTarget target = getNewFieldTarget(typeDeclaration);
        editor.addFieldDeclaration(lines, target);
        addFormJField(fieldClass, name);
    }

    /**
   * Adds <code>JField</code> with given name and @UiField annotation into "form" <code>JType</code>
   * .
   */
    private void addFormJField(Class<?> componentClass, String name) throws Exception {
        IDevModeBridge bridge = m_context.getState().getDevModeBridge();
        ClassLoader devClassLoader = bridge.getDevClassLoader();
        Object formType = bridge.findJType(m_context.getFormType().getFullyQualifiedName());
        Class<?> uiFieldAnnotationClass = devClassLoader.loadClass("com.google.gwt.uibinder.client.UiField");
        java.lang.annotation.Annotation annotation = (java.lang.annotation.Annotation) Proxy.newProxyInstance(uiFieldAnnotationClass.getClassLoader(), new Class[] { uiFieldAnnotationClass }, new InvocationHandler() {

            public Object invoke(Object proxy, Method method, Object[] args) {
                return Boolean.TRUE;
            }
        });
        Object newField;
        {
            Constructor<?> fieldConstructor;
            Class<?> fieldClass;
            if (m_context.getState().getVersion().isHigherOrSame(Utils.GWT_2_2)) {
                fieldClass = devClassLoader.loadClass("com.google.gwt.dev.javac.typemodel.JField");
                fieldConstructor = ReflectionUtils.getConstructorBySignature(fieldClass, "<init>(com.google.gwt.dev.javac.typemodel.JClassType,java.lang.String,java.util.Map)");
            } else {
                fieldClass = devClassLoader.loadClass("com.google.gwt.core.ext.typeinfo.JField");
                fieldConstructor = ReflectionUtils.getConstructorBySignature(fieldClass, "<init>(com.google.gwt.core.ext.typeinfo.JClassType,java.lang.String,java.util.Map)");
            }
            newField = fieldConstructor.newInstance(formType, name, ImmutableMap.of(uiFieldAnnotationClass, annotation));
        }
        Object widgetType = bridge.findJType(ReflectionUtils.getCanonicalName(componentClass));
        ReflectionUtils.invokeMethod(newField, "setType(com.google.gwt.core.ext.typeinfo.JType)", widgetType);
    }

    /**
   * When we change name ourself, using "UiField" property, we don't want to change
   * <code>*.ui.xml</code> file, because this causes reparsing and all related problems. So, we
   * disable template changing temporary.
   */
    private static boolean m_renaming;

    /**
   * @return the <code>true</code> if <code>*.ui.xml</code> is changing by {@link NameSupport} now.
   */
    public static boolean isRenaming() {
        return m_renaming;
    }

    /**
   * @return the {@link AstEditor} for companion Java unit.
   */
    private AstEditor getEditor() throws Exception {
        return m_context.getFormEditor();
    }

    /**
   * @return the {@link VariableDeclaration} of "@UiField" with given name, may be <code>null</code>
   *         .
   */
    public static VariableDeclaration getBinderField(TypeDeclaration typeDeclaration, final String name) {
        final VariableDeclaration[] result = { null };
        typeDeclaration.accept(new ASTVisitor() {

            @Override
            public void endVisit(FieldDeclaration node) {
                if (isBinderField(node)) {
                    for (VariableDeclaration fragment : DomGenerics.fragments(node)) {
                        if (fragment.getName().getIdentifier().equals(name)) {
                            result[0] = fragment;
                        }
                    }
                }
            }
        });
        return result[0];
    }

    /**
   * @return the {@link VariableDeclaration}s of with "@UiField(provided)".
   */
    public static List<FieldDeclaration> getUiFields(TypeDeclaration typeDeclaration) {
        final List<FieldDeclaration> fields = Lists.newArrayList();
        typeDeclaration.accept(new ASTVisitor() {

            @Override
            public void endVisit(FieldDeclaration node) {
                if (isBinderField(node)) {
                    fields.add(node);
                }
            }
        });
        return fields;
    }

    /**
   * Adds @UiField with given type, name and initializer.
   */
    public static String addUiFieldJava(UIObjectInfo contextObject, Class<?> fieldClass, String fieldTypeName, String baseName, String initializer) throws Exception {
        NameSupport nameSupport = new NameSupport(contextObject);
        String name = nameSupport.generateName(baseName);
        nameSupport.addUiFieldJava(fieldClass, fieldTypeName, name, initializer);
        return name;
    }

    /**
   * @return the {@link BodyDeclarationTarget} to add new "@UiField".
   */
    private static BodyDeclarationTarget getNewFieldTarget(TypeDeclaration typeDeclaration) {
        final FieldDeclaration[] lastUiField = { null };
        final FieldDeclaration[] binderCreate = { null };
        typeDeclaration.accept(new ASTVisitor() {

            @Override
            public void endVisit(FieldDeclaration node) {
                if (isBinderField(node)) {
                    lastUiField[0] = node;
                }
                if (isBinderCreate(node)) {
                    binderCreate[0] = node;
                }
            }
        });
        if (lastUiField[0] != null) {
            return new BodyDeclarationTarget(lastUiField[0], false);
        }
        if (binderCreate[0] != null) {
            return new BodyDeclarationTarget(binderCreate[0], false);
        }
        return new BodyDeclarationTarget(typeDeclaration, false);
    }

    /**
   * @return <code>true</code> if given {@link FieldDeclaration} is "@UiField".
   */
    public static boolean isBinderField(FieldDeclaration fieldDeclaration) {
        for (IExtendedModifier modifier : DomGenerics.modifiers(fieldDeclaration)) {
            if (modifier instanceof Annotation) {
                Annotation annotation = (Annotation) modifier;
                if (isBinderAnnotation(annotation)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
   * @return <code>true</code> if given {@link Annotation} is "@UiField".
   */
    public static boolean isBinderAnnotation(Annotation annotation) {
        return AstNodeUtils.isSuccessorOf(annotation, "com.google.gwt.uibinder.client.UiField");
    }

    /**
   * @return <code>true</code> if has initializer <code>GWT.create(UiBinder+.class)</code>.
   */
    public static boolean isBinderCreate(FieldDeclaration fieldDeclaration) {
        List<VariableDeclarationFragment> fragments = DomGenerics.fragments(fieldDeclaration);
        for (VariableDeclaration fragment : fragments) {
            if (fragment.getInitializer() instanceof MethodInvocation) {
                MethodInvocation invocation = (MethodInvocation) fragment.getInitializer();
                if (invocation.getName().getIdentifier().equals("create") && AstNodeUtils.isSuccessorOf(invocation.getExpression(), "com.google.gwt.core.client.GWT")) {
                    List<Expression> arguments = DomGenerics.arguments(invocation);
                    if (arguments.size() == 1 && arguments.get(0) instanceof TypeLiteral) {
                        TypeLiteral typeLiteral = (TypeLiteral) arguments.get(0);
                        if (AstNodeUtils.isSuccessorOf(typeLiteral.getType(), "com.google.gwt.uibinder.client.UiBinder")) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
   * @return the full name (including namespace) for "field" attribute.
   */
    private String getNameAttribute() {
        DocumentElement rootElement = m_object.getElement().getRoot();
        return rootElement.getTagNS() + "field";
    }

    /**
   * @return the generated unique name basing on settings and info in *.wbp-component.xml
   *         description.
   */
    private String generateName() throws Exception {
        String baseName = getBaseName();
        return generateName(baseName);
    }

    /**
   * @return the generated unique name based on the given.
   */
    private String generateName(String baseName) throws Exception {
        final Set<String> identifiers = getExistingNames();
        String uniqueName = CodeUtils.generateUniqueName(baseName, new Predicate<String>() {

            public boolean apply(String name) {
                return !identifiers.contains(name);
            }
        });
        return uniqueName;
    }

    /**
   * Traverses the entire hierarchy and gathers set of existing names.
   */
    private Set<String> getExistingNames() throws Exception {
        final Set<String> resultSet = Sets.newTreeSet();
        m_object.getRootXML().accept(new ObjectInfoVisitor() {

            @Override
            public void endVisit(ObjectInfo object) throws Exception {
                if (object instanceof XmlObjectInfo) {
                    XmlObjectInfo xmlObject = (XmlObjectInfo) object;
                    if (!XmlObjectUtils.isImplicit(xmlObject)) {
                        String name = getName(xmlObject);
                        if (name != null) {
                            resultSet.add(name);
                        }
                    }
                }
            }
        });
        {
            AstEditor editor = getEditor();
            TypeDeclaration typeDeclaration = editor.getPrimaryType();
            for (FieldDeclaration fieldDeclaration : typeDeclaration.getFields()) {
                for (VariableDeclarationFragment fragment : DomGenerics.fragments(fieldDeclaration)) {
                    String name = fragment.getName().getIdentifier();
                    resultSet.add(name);
                }
            }
        }
        return resultSet;
    }

    /**
   * @return the base variable name for given {@link XmlObjectInfo}.
   */
    private String getBaseName() {
        ComponentDescription description = m_object.getDescription();
        String componentClassName = description.getComponentClass().getName();
        {
            ComponentNameDescription nameDescription = NamesManager.getNameDescription(description.getToolkit(), componentClassName);
            if (nameDescription != null) {
                return nameDescription.getName();
            }
        }
        {
            String name = XmlObjectUtils.getParameter(m_object, NamesManager.NAME_PARAMETER);
            if (!StringUtils.isEmpty(name)) {
                return name;
            }
        }
        return NamesManager.getDefaultName(componentClassName);
    }
}
