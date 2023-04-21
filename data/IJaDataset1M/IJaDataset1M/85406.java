package org.xmlhammer.gui.xslt;

import java.net.URI;
import java.util.List;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.bounce.image.ImageLoader;
import org.bounce.util.URIUtils;
import org.xml.sax.SAXException;
import org.xmlhammer.Module;
import org.xmlhammer.PreferencesHandler;
import org.xmlhammer.ResultModel;
import org.xmlhammer.gui.ProjectView;
import org.xmlhammer.gui.ProjectsView;
import org.xmlhammer.gui.input.InputPage;
import org.xmlhammer.gui.output.SourceBuilder;
import org.xmlhammer.gui.parser.ParserPage;
import org.xmlhammer.gui.preferences.PropertiesDialog;
import org.xmlhammer.gui.util.JAXPSettings;
import org.xmlhammer.model.jaxp.Attribute;
import org.xmlhammer.model.jaxp.Feature;
import org.xmlhammer.model.jaxp.JAXPDocumentBuilderFactory;
import org.xmlhammer.model.jaxp.JAXPSAXParserFactory;
import org.xmlhammer.model.jaxp.JAXPSchemaFactory;
import org.xmlhammer.model.jaxp.JAXPTransformerFactory;
import org.xmlhammer.model.jaxp.Settings;
import org.xmlhammer.model.project.Filter;
import org.xmlhammer.model.project.Parameter;
import org.xmlhammer.model.project.ParameterisedSource;
import org.xmlhammer.model.project.Project;
import org.xmlhammer.model.tools.xslt.Associated;
import org.xmlhammer.model.tools.xslt.OutputProperty;
import org.xmlhammer.model.tools.xslt.XSLT;
import org.xmlhammer.model.tools.xslt.XSLT.Transform;

/**
 * The UI representation of a Project.
 * 
 * @version $Revision$, $Date$
 * @author Edwin Dankert <edankert@gmail.com>
 */
public class XSLTProjectView extends ProjectView {

    private static final long serialVersionUID = 4121138017445755189L;

    private static final ImageIcon ICON = ImageLoader.get().getImage("/org/xmlhammer/gui/icons/etool16/transform.gif");

    private PropertiesDialog propertiesDialog = null;

    private SourceBuilder source = null;

    private StylesheetsPage stylesheetsPage = null;

    private OutputPage outputPage = null;

    /**
	 * @param parent the projects view parent.
	 * @param uri the uri for the project.
	 */
    public XSLTProjectView(ProjectsView parent, URI uri) {
        super(parent, uri);
        source = new SourceBuilder();
        initPages();
    }

    /**
	 * @return the underlying project.
	 */
    public Project getProject(URI base) {
        Project project = super.getProject(base);
        XSLT xslt = new XSLT();
        project.setXSLT(xslt);
        if (getStylesheetsPage() != null && getOutputPage() != null) {
            xslt.setOutputProperties(getOutputPage().getOutputProperties(base));
            xslt.setTransform(getStylesheetsPage().getTransform(base));
        }
        return project;
    }

    public void setProject(Project project) {
        if (getStylesheetsPage() != null && getOutputPage() != null) {
            if (project != null) {
                getOutputPage().setOutputProperties(getURI(), project.getXSLT().getOutputProperties());
                getStylesheetsPage().setTransform(getURI(), project.getXSLT().getTransform());
            } else {
                getOutputPage().setOutputProperties(getURI(), null);
                getStylesheetsPage().setTransform(getURI(), null);
            }
        }
        super.setProject(project);
    }

    protected void createPages() {
        addPage(createInputPage());
        addPage(createParserPage());
        addPage(createStylesheetsPage());
        addPage(createOutputPage());
    }

    protected InputPage createInputPage() {
        inputPage = new InputPage(this, true);
        return inputPage;
    }

    private StylesheetsPage createStylesheetsPage() {
        stylesheetsPage = new StylesheetsPage(this);
        return stylesheetsPage;
    }

    private StylesheetsPage getStylesheetsPage() {
        return stylesheetsPage;
    }

    private OutputPage createOutputPage() {
        outputPage = new OutputPage(this);
        return outputPage;
    }

    private OutputPage getOutputPage() {
        return outputPage;
    }

    /**
	 * Show the properties dialog.
	 */
    public void showPropertiesDialog() {
        Settings settings = getProject(null).getJAXPSettings();
        if (propertiesDialog == null) {
            propertiesDialog = new PropertiesDialog(getProjectsView().getRoot(), true, true, false, true, true);
        }
        JAXPSAXParserFactory saxFactory = settings.getJAXPSAXParserFactory();
        if (saxFactory == null) {
            saxFactory = new JAXPSAXParserFactory();
        }
        propertiesDialog.setSAXParserFactory(saxFactory);
        JAXPDocumentBuilderFactory documentBuilderFactory = settings.getJAXPDocumentBuilderFactory();
        if (documentBuilderFactory == null) {
            documentBuilderFactory = new JAXPDocumentBuilderFactory();
        }
        propertiesDialog.setDocumentBuilderFactory(documentBuilderFactory);
        JAXPSchemaFactory schemaFactory = settings.getJAXPSchemaFactory();
        if (schemaFactory == null) {
            schemaFactory = new JAXPSchemaFactory();
        }
        propertiesDialog.setSchemaFactory(schemaFactory);
        JAXPTransformerFactory transformerFactory = settings.getJAXPTransformerFactory();
        if (transformerFactory == null) {
            transformerFactory = new JAXPTransformerFactory();
        }
        propertiesDialog.setTransformerFactory(transformerFactory);
        if (propertiesDialog.open() == PropertiesDialog.OK_OPTION) {
            settings.setJAXPSAXParserFactory(propertiesDialog.getSAXParserFactory());
            settings.setJAXPDocumentBuilderFactory(propertiesDialog.getDocumentBuilderFactory());
            settings.setJAXPSchemaFactory(propertiesDialog.getSchemaFactory());
            settings.setJAXPTransformerFactory(propertiesDialog.getTransformerFactory());
        }
    }

    /**
     * @return the result model.
     */
    public ResultModel getResult() {
        URI dir = null;
        if (getInputPage() != null) {
            Filter filter = getInputPage().getInput(null).getFilter();
            if (filter != null) {
                dir = URIUtils.createURI(filter.getDir());
            }
        }
        return getResultPanel().createModel(dir);
    }

    public SourceBuilder getSource() {
        StringBuilder builder = new StringBuilder();
        if (getStylesheetsPage() != null && getOutputPage() != null) {
            builder.append("\n\n");
            appendJavaImports(builder);
            getParserPage().appendJavaImports(builder);
            builder.append("\n");
            getParserPage().appendJavaxImports(builder);
            builder.append("\n");
            if (getParserPage().isDOMSelected()) {
                appendDOMImports(builder);
            } else {
                appendSAXImports(builder);
            }
            builder.append("\n");
            getParserPage().appendSAXImports(builder);
            builder.append("\n");
            SourceBuilder.appendReserved(builder, "public class");
            builder.append(" XMLTransformer {\n\n");
            SourceBuilder.appendReserved(builder, "    public void");
            builder.append(" transform(URI uri, File file) {\n");
            getParserPage().appendSource(builder, true);
            if (getParserPage().isDOMSelected()) {
                appendDOMSource(builder);
            } else {
                appendSAXSource(builder);
            }
            builder.append("    }\n\n");
            ParserPage.appendErrorHandler(builder);
            builder.append("}\n");
        }
        source.reset();
        source.addCode(builder.toString());
        source.appendDisclaimer();
        return source;
    }

    /**
	 * @return the module to run.
	 */
    public Module getModule() {
        try {
            return new XSLTModule(PreferencesHandler.getInstance().getPreferences(), getProject(null), getLogger());
        } catch (SAXException e) {
            showInitializationErrorMessage(e);
        } catch (ParserConfigurationException e) {
            showInitializationErrorMessage(e);
        } catch (TransformerConfigurationException e) {
            showInitializationErrorMessage(e);
        } catch (TransformerException e) {
            showInitializationErrorMessage(e);
        }
        return null;
    }

    public void dispose() {
        if (getInputPage() != null) {
            getInputPage().dispose();
        }
        if (getParserPage() != null) {
            getParserPage().dispose();
        }
        if (getStylesheetsPage() != null) {
            getStylesheetsPage().dispose();
        }
        if (getOutputPage() != null) {
            getOutputPage().dispose();
        }
    }

    public Icon getIcon() {
        return ICON;
    }

    public void appendJavaImports(StringBuilder builder) {
        SourceBuilder.appendImport(builder, "java.io.File");
        SourceBuilder.appendImport(builder, "java.io.FileOutputStream");
        if (getStylesheetsPage().isStylesheetsSelected()) {
            SourceBuilder.appendImport(builder, "java.io.ByteArrayOutputStream");
            SourceBuilder.appendImport(builder, "java.io.ByteArrayInputStream");
        }
    }

    public void appendDOMImports(StringBuilder builder) {
        if (getStylesheetsPage().isAssociatedSelected()) {
            SourceBuilder.appendImport(builder, "javax.xml.transform.Source");
        }
        SourceBuilder.appendImport(builder, "javax.xml.transform.Transformer");
        SourceBuilder.appendImport(builder, "javax.xml.transform.TransformerException");
        SourceBuilder.appendImport(builder, "javax.xml.transform.TransformerFactory");
        SourceBuilder.appendImport(builder, "javax.xml.transform.dom.DOMSource");
        SourceBuilder.appendImport(builder, "javax.xml.transform.stream.StreamResult");
        if (getStylesheetsPage().isStylesheetsSelected()) {
            SourceBuilder.appendImport(builder, "javax.xml.transform.stream.StreamSource");
        }
    }

    public void appendSAXImports(StringBuilder builder) {
        if (getStylesheetsPage().isAssociatedSelected()) {
            SourceBuilder.appendImport(builder, "javax.xml.transform.Source");
        }
        SourceBuilder.appendImport(builder, "javax.xml.transform.Transformer");
        SourceBuilder.appendImport(builder, "javax.xml.transform.TransformerException");
        SourceBuilder.appendImport(builder, "javax.xml.transform.TransformerFactory");
        SourceBuilder.appendImport(builder, "javax.xml.transform.sax.SAXSource");
        SourceBuilder.appendImport(builder, "javax.xml.transform.stream.StreamResult");
        if (getStylesheetsPage().isStylesheetsSelected()) {
            SourceBuilder.appendImport(builder, "javax.xml.transform.stream.StreamSource");
        }
    }

    public void appendDOMSource(StringBuilder builder) {
        if (getProject().getJAXPSettings() != null) {
            builder.append("\n");
            JAXPSettings jaxp = new JAXPSettings(PreferencesHandler.getInstance().getPreferences().getJAXPSettings(), getProject().getJAXPSettings());
            JAXPTransformerFactory.Settings settings = jaxp.getTransformerFactorySettings();
            if (settings.getValue() != null) {
                builder.append("        System.setProperty(");
                SourceBuilder.appendConstant(builder, "\"javax.xml.transform.TransformerFactory\"");
                builder.append(", ");
                SourceBuilder.appendConstant(builder, "\"" + settings.getValue() + "\"");
                builder.append(");\n\n");
            }
            SourceBuilder.appendReserved(builder, "        try ");
            builder.append("{\n");
            builder.append("            TransformerFactory transformerFactory = TransformerFactory.newInstance();\n\n");
            List<Feature> features = jaxp.getTransformerFactoryFeatures();
            for (Feature feature : features) {
                builder.append("            transformerFactory.setFeature(");
                SourceBuilder.appendConstant(builder, "\"" + feature.getName() + "\"");
                builder.append(", ");
                SourceBuilder.appendConstant(builder, feature.isEnabled());
                builder.append(");\n");
            }
            List<Attribute> attributes = jaxp.getTransformerFactoryAttributes();
            for (Attribute attribute : attributes) {
                builder.append("            transformerFactory.setAttribute(");
                SourceBuilder.appendConstant(builder, "\"" + attribute.getName() + "\"");
                builder.append(", ");
                SourceBuilder.appendConstant(builder, "\"" + attribute.getValue() + "\"");
                builder.append(");\n");
            }
            Transform transform = getStylesheetsPage().getTransform(null);
            if (transform.getAssociated() != null) {
                Associated associated = transform.getAssociated();
                builder.append("            DOMSource source = ");
                SourceBuilder.appendReserved(builder, "new");
                builder.append(" DOMSource(builder.parse(");
                SourceBuilder.appendReserved(builder, "new");
                builder.append(" InputSource(uri.toString())));\n");
                builder.append("            Source style = transformerFactory.getAssociatedStylesheet(source, ");
                if (associated.getMedia() != null) {
                    SourceBuilder.appendConstant(builder, "\"" + associated.getMedia() + "\"");
                } else {
                    SourceBuilder.appendConstant(builder, "null");
                }
                builder.append(", ");
                if (associated.getTitle() != null) {
                    SourceBuilder.appendConstant(builder, "\"" + associated.getTitle() + "\"");
                } else {
                    SourceBuilder.appendConstant(builder, "null");
                }
                builder.append(", ");
                if (associated.getCharset() != null) {
                    SourceBuilder.appendConstant(builder, "\"" + associated.getCharset() + "\"");
                } else {
                    SourceBuilder.appendConstant(builder, "null");
                }
                builder.append(");\n");
                builder.append("            Transformer transformer = transformerFactory.newTransformer(style);\n");
                List<Parameter> parameters = associated.getParameter();
                for (Parameter parameter : parameters) {
                    if (parameter.isActive()) {
                        builder.append("            transformer.setParameter(");
                        SourceBuilder.appendConstant(builder, "\"" + parameter.getName() + "\"");
                        builder.append(", ");
                        SourceBuilder.appendConstant(builder, "\"" + parameter.getValue() + "\"");
                        builder.append(");\n");
                    }
                }
                builder.append("            StreamResult result = ");
                SourceBuilder.appendReserved(builder, "new");
                builder.append(" StreamResult(");
                SourceBuilder.appendReserved(builder, "new");
                builder.append(" FileOutputStream(file));\n");
                List<OutputProperty> properties = getOutputPage().getOutputProperties(null).getOutputProperty();
                for (OutputProperty property : properties) {
                    if (property.isActive()) {
                        builder.append("            transformer.setOutputProperty(");
                        SourceBuilder.appendConstant(builder, "\"" + property.getName() + "\"");
                        builder.append(", ");
                        SourceBuilder.appendConstant(builder, "\"" + property.getValue() + "\"");
                        builder.append(");\n");
                    }
                }
                builder.append("            transformer.transform(source, result);\n\n");
            } else if (transform.getStylesheets() != null) {
                List<ParameterisedSource> stylesheets = transform.getStylesheets().getParameterisedSource();
                for (ParameterisedSource stylesheet : stylesheets) {
                    builder.append("            Transformer transformer" + stylesheets.indexOf(stylesheet) + " = transformerFactory.newTransformer(");
                    SourceBuilder.appendReserved(builder, "new");
                    builder.append(" StreamSource(");
                    SourceBuilder.appendConstant(builder, "\"" + stylesheet.getSrc() + "\"");
                    builder.append("));\n");
                    List<Parameter> parameters = stylesheet.getParameter();
                    for (Parameter parameter : parameters) {
                        if (parameter.isActive()) {
                            builder.append("            transformer" + stylesheets.indexOf(stylesheet) + ".setParameter(");
                            SourceBuilder.appendConstant(builder, "\"" + parameter.getName() + "\"");
                            builder.append(", ");
                            SourceBuilder.appendConstant(builder, "\"" + parameter.getValue() + "\"");
                            builder.append(");\n");
                        }
                    }
                    if (stylesheets.get(0) == stylesheet) {
                        builder.append("            DOMSource source" + stylesheets.indexOf(stylesheet) + " = ");
                        SourceBuilder.appendReserved(builder, "new");
                        builder.append(" DOMSource(builder.parse(");
                        SourceBuilder.appendReserved(builder, "new");
                        builder.append(" InputSource(uri.toString())));\n");
                    } else {
                        builder.append("            DOMSource source" + stylesheets.indexOf(stylesheet) + " = ");
                        SourceBuilder.appendReserved(builder, "new");
                        builder.append(" DOMSource(builder.parse(");
                        SourceBuilder.appendReserved(builder, "new");
                        builder.append(" InputSource(");
                        SourceBuilder.appendReserved(builder, "new");
                        builder.append(" ByteArrayInputStream(((ByteArrayOutputStream)result" + (stylesheets.indexOf(stylesheet) - 1) + ".getOutputStream()).toByteArray()))));\n");
                    }
                    if (stylesheets.get(stylesheets.size() - 1) == stylesheet) {
                        builder.append("            StreamResult result" + stylesheets.indexOf(stylesheet) + " = ");
                        SourceBuilder.appendReserved(builder, "new");
                        builder.append(" StreamResult(");
                        SourceBuilder.appendReserved(builder, "new");
                        builder.append(" FileOutputStream(file));\n");
                        List<OutputProperty> properties = getOutputPage().getOutputProperties(null).getOutputProperty();
                        for (OutputProperty property : properties) {
                            if (property.isActive()) {
                                builder.append("            transformer" + stylesheets.indexOf(stylesheet) + ".setOutputProperty(");
                                SourceBuilder.appendConstant(builder, "\"" + property.getName() + "\"");
                                builder.append(", ");
                                SourceBuilder.appendConstant(builder, "\"" + property.getValue() + "\"");
                                builder.append(");\n");
                            }
                        }
                    } else {
                        builder.append("            StreamResult result" + stylesheets.indexOf(stylesheet) + " = ");
                        SourceBuilder.appendReserved(builder, "new");
                        builder.append(" StreamResult(");
                        SourceBuilder.appendReserved(builder, "new");
                        builder.append(" ByteArrayOutputStream());\n");
                    }
                    builder.append("            transformer" + stylesheets.indexOf(stylesheet) + ".transform(source" + stylesheets.indexOf(stylesheet) + ", result" + stylesheets.indexOf(stylesheet) + ");\n\n");
                }
            } else {
                builder.append("            Transformer transformer = transformerFactory.newTransformer();\n");
                builder.append("            DOMSource source = ");
                SourceBuilder.appendReserved(builder, "new");
                builder.append(" DOMSource(builder.parse(");
                SourceBuilder.appendReserved(builder, "new");
                builder.append(" InputSource(uri.toString())));\n");
                builder.append("            StreamResult result = ");
                SourceBuilder.appendReserved(builder, "new");
                builder.append(" StreamResult(");
                SourceBuilder.appendReserved(builder, "new");
                builder.append(" FileOutputStream(file));\n");
                List<OutputProperty> properties = getOutputPage().getOutputProperties(null).getOutputProperty();
                for (OutputProperty property : properties) {
                    if (property.isActive()) {
                        builder.append("            transformer.setOutputProperty(");
                        SourceBuilder.appendConstant(builder, "\"" + property.getName() + "\"");
                        builder.append(", ");
                        SourceBuilder.appendConstant(builder, "\"" + property.getValue() + "\"");
                        builder.append(");\n");
                    }
                }
                builder.append("            transformer.transform(source, result);\n\n");
            }
            SourceBuilder.appendCatch(builder, "        ", "TransformerException");
            SourceBuilder.appendCatch(builder, "        ", "SAXException");
            SourceBuilder.appendCatch(builder, "        ", "IOException");
            builder.append("        }\n");
        }
    }

    public void appendSAXSource(StringBuilder builder) {
        if (getProject().getJAXPSettings() != null) {
            builder.append("\n");
            JAXPSettings jaxp = new JAXPSettings(PreferencesHandler.getInstance().getPreferences().getJAXPSettings(), getProject().getJAXPSettings());
            JAXPTransformerFactory.Settings settings = jaxp.getTransformerFactorySettings();
            if (settings.getValue() != null) {
                builder.append("        System.setProperty(");
                SourceBuilder.appendConstant(builder, "\"javax.xml.transform.TransformerFactory\"");
                builder.append(", ");
                SourceBuilder.appendConstant(builder, "\"" + settings.getValue() + "\"");
                builder.append(");\n\n");
            }
            SourceBuilder.appendReserved(builder, "        try ");
            builder.append("{\n");
            builder.append("            TransformerFactory transformerFactory = TransformerFactory.newInstance();\n\n");
            List<Feature> features = jaxp.getTransformerFactoryFeatures();
            for (Feature feature : features) {
                builder.append("            transformerFactory.setFeature(");
                SourceBuilder.appendConstant(builder, "\"" + feature.getName() + "\"");
                builder.append(", ");
                SourceBuilder.appendConstant(builder, feature.isEnabled());
                builder.append(");\n");
            }
            List<Attribute> attributes = jaxp.getTransformerFactoryAttributes();
            for (Attribute attribute : attributes) {
                builder.append("            transformerFactory.setAttribute(");
                SourceBuilder.appendConstant(builder, "\"" + attribute.getName() + "\"");
                builder.append(", ");
                SourceBuilder.appendConstant(builder, "\"" + attribute.getValue() + "\"");
                builder.append(");\n");
            }
            Transform transform = getStylesheetsPage().getTransform(null);
            if (transform.getAssociated() != null) {
                Associated associated = transform.getAssociated();
                builder.append("            SAXSource source = ");
                SourceBuilder.appendReserved(builder, "new");
                builder.append(" SAXSource(reader, ");
                SourceBuilder.appendReserved(builder, "new");
                builder.append(" InputSource(uri.toString()));\n");
                builder.append("            Source style = transformerFactory.getAssociatedStylesheet(source, ");
                if (associated.getMedia() != null) {
                    SourceBuilder.appendConstant(builder, "\"" + associated.getMedia() + "\"");
                } else {
                    SourceBuilder.appendConstant(builder, "null");
                }
                builder.append(", ");
                if (associated.getTitle() != null) {
                    SourceBuilder.appendConstant(builder, "\"" + associated.getTitle() + "\"");
                } else {
                    SourceBuilder.appendConstant(builder, "null");
                }
                builder.append(", ");
                if (associated.getCharset() != null) {
                    SourceBuilder.appendConstant(builder, "\"" + associated.getCharset() + "\"");
                } else {
                    SourceBuilder.appendConstant(builder, "null");
                }
                builder.append(");\n");
                builder.append("            Transformer transformer = transformerFactory.newTransformer(style);\n");
                List<Parameter> parameters = associated.getParameter();
                for (Parameter parameter : parameters) {
                    if (parameter.isActive()) {
                        builder.append("            transformer.setParameter(");
                        SourceBuilder.appendConstant(builder, "\"" + parameter.getName() + "\"");
                        builder.append(", ");
                        SourceBuilder.appendConstant(builder, "\"" + parameter.getValue() + "\"");
                        builder.append(");\n");
                    }
                }
                builder.append("            StreamResult result = ");
                SourceBuilder.appendReserved(builder, "new");
                builder.append(" StreamResult(");
                SourceBuilder.appendReserved(builder, "new");
                builder.append(" FileOutputStream(file));\n");
                List<OutputProperty> properties = getOutputPage().getOutputProperties(null).getOutputProperty();
                for (OutputProperty property : properties) {
                    if (property.isActive()) {
                        builder.append("            transformer.setOutputProperty(");
                        SourceBuilder.appendConstant(builder, "\"" + property.getName() + "\"");
                        builder.append(", ");
                        SourceBuilder.appendConstant(builder, "\"" + property.getValue() + "\"");
                        builder.append(");\n");
                    }
                }
                builder.append("            transformer.transform(source, result);\n\n");
            } else if (transform.getStylesheets() != null) {
                List<ParameterisedSource> stylesheets = transform.getStylesheets().getParameterisedSource();
                for (ParameterisedSource stylesheet : stylesheets) {
                    builder.append("            Transformer transformer" + stylesheets.indexOf(stylesheet) + " = transformerFactory.newTransformer(");
                    SourceBuilder.appendReserved(builder, "new");
                    builder.append(" StreamSource(");
                    SourceBuilder.appendConstant(builder, "\"" + stylesheet.getSrc() + "\"");
                    builder.append("));\n");
                    List<Parameter> parameters = stylesheet.getParameter();
                    for (Parameter parameter : parameters) {
                        if (parameter.isActive()) {
                            builder.append("            transformer" + stylesheets.indexOf(stylesheet) + ".setParameter(");
                            SourceBuilder.appendConstant(builder, "\"" + parameter.getName() + "\"");
                            builder.append(", ");
                            SourceBuilder.appendConstant(builder, "\"" + parameter.getValue() + "\"");
                            builder.append(");\n");
                        }
                    }
                    if (stylesheets.get(0) == stylesheet) {
                        builder.append("            SAXSource source" + stylesheets.indexOf(stylesheet) + " = ");
                        SourceBuilder.appendReserved(builder, "new");
                        builder.append(" SAXSource(reader, ");
                        SourceBuilder.appendReserved(builder, "new");
                        builder.append(" InputSource(uri.toString()));\n");
                    } else {
                        builder.append("            SAXSource source" + stylesheets.indexOf(stylesheet) + " = ");
                        SourceBuilder.appendReserved(builder, "new");
                        builder.append(" SAXSource(reader, ");
                        SourceBuilder.appendReserved(builder, "new");
                        builder.append(" InputSource(");
                        SourceBuilder.appendReserved(builder, "new");
                        builder.append(" ByteArrayInputStream(((ByteArrayOutputStream)result" + (stylesheets.indexOf(stylesheet) - 1) + ".getOutputStream()).toByteArray())));\n");
                    }
                    if (stylesheets.get(stylesheets.size() - 1) == stylesheet) {
                        builder.append("            StreamResult result" + stylesheets.indexOf(stylesheet) + " = ");
                        SourceBuilder.appendReserved(builder, "new");
                        builder.append(" StreamResult(");
                        SourceBuilder.appendReserved(builder, "new");
                        builder.append(" FileOutputStream(file));\n");
                        List<OutputProperty> properties = getOutputPage().getOutputProperties(null).getOutputProperty();
                        for (OutputProperty property : properties) {
                            if (property.isActive()) {
                                builder.append("            transformer" + stylesheets.indexOf(stylesheet) + ".setOutputProperty(");
                                SourceBuilder.appendConstant(builder, "\"" + property.getName() + "\"");
                                builder.append(", ");
                                SourceBuilder.appendConstant(builder, "\"" + property.getValue() + "\"");
                                builder.append(");\n");
                            }
                        }
                    } else {
                        builder.append("            StreamResult result" + stylesheets.indexOf(stylesheet) + " = ");
                        SourceBuilder.appendReserved(builder, "new");
                        builder.append(" StreamResult(");
                        SourceBuilder.appendReserved(builder, "new");
                        builder.append(" ByteArrayOutputStream());\n");
                    }
                    builder.append("            transformer" + stylesheets.indexOf(stylesheet) + ".transform(source" + stylesheets.indexOf(stylesheet) + ", result" + stylesheets.indexOf(stylesheet) + ");\n\n");
                }
            } else {
                builder.append("            Transformer transformer = transformerFactory.newTransformer();\n");
                builder.append("            SAXSource source = ");
                SourceBuilder.appendReserved(builder, "new");
                builder.append(" SAXSource(reader, ");
                SourceBuilder.appendReserved(builder, "new");
                builder.append(" InputSource(uri.toString()));\n");
                builder.append("            StreamResult result = ");
                SourceBuilder.appendReserved(builder, "new");
                builder.append(" StreamResult(");
                SourceBuilder.appendReserved(builder, "new");
                builder.append(" FileOutputStream(file));\n");
                List<OutputProperty> properties = getOutputPage().getOutputProperties(null).getOutputProperty();
                for (OutputProperty property : properties) {
                    if (property.isActive()) {
                        builder.append("            transformer.setOutputProperty(");
                        SourceBuilder.appendConstant(builder, "\"" + property.getName() + "\"");
                        builder.append(", ");
                        SourceBuilder.appendConstant(builder, "\"" + property.getValue() + "\"");
                        builder.append(");\n");
                    }
                }
                builder.append("            transformer.transform(source, result);\n\n");
            }
            SourceBuilder.appendCatch(builder, "        ", "TransformerException");
            SourceBuilder.appendCatch(builder, "        ", "IOException");
            builder.append("        }\n");
        }
    }

    @Override
    public String getHelpID() {
        return "project.trax";
    }
}
