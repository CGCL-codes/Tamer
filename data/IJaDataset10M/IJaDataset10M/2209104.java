package com.ivis.xprocess.ui.editors.dynamic.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.JellyException;
import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.Script;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.parser.XMLParser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.xml.sax.SAXException;
import com.ivis.xprocess.ui.datawrappers.IElementWrapper;
import com.ivis.xprocess.ui.editors.ElementEditor;
import com.ivis.xprocess.ui.editors.util.GenerateEditorXML;
import com.ivis.xprocess.ui.perspectives.simpleproject.ElementView;

public class JellyEditorHelper {

    private JellyContext jellyContext;

    private EditorContext editorContext;

    private Script script;

    /**
     * This constructor does not require an XML schema
     * because the IElementWrapper is used to generate
     * XML based on its properties.
     *
     * @param elementWrapper
     */
    public JellyEditorHelper(IElementWrapper elementWrapper) {
        jellyContext = new JellyContext();
        XMLParser parser = new XMLParser();
        parser.setContext(jellyContext);
        try {
            GenerateEditorXML.createXML(elementWrapper.getElement());
            Document doc = GenerateEditorXML.getDoc();
            Format format = Format.getPrettyFormat().setEncoding("UTF-8");
            XMLOutputter xo = new XMLOutputter(new XMLOutputter(format));
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            xo.output(doc, bo);
            ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
            parser.setValidating(false);
            script = parser.parse(bi);
            script = script.compile();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (JellyException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param xmlFile - the URL of the XML schema file
     */
    public JellyEditorHelper(URL xmlFile) {
        jellyContext = new JellyContext();
        XMLParser parser = new XMLParser();
        parser.setContext(jellyContext);
        try {
            parser.setValidating(false);
            script = parser.parse(xmlFile);
            script = script.compile();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (JellyException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialize and run Jelly over the XML schema in the context of an ElementEditor.
     *
     * @param parent
     * @param formToolkit
     * @param elementEditor
     */
    public void initialize(Composite parent, FormToolkit formToolkit, ElementEditor elementEditor) {
        try {
            editorContext = new EditorContext();
            editorContext.setLocalTransientElement(elementEditor.getLocalTransientElement());
            editorContext.setElementEditor(elementEditor);
            editorContext.setFormToolkit(formToolkit);
            editorContext.setContainer(elementEditor.getContainer());
            jellyContext.setVariable("editorContext", editorContext);
            script.run(jellyContext, XMLOutput.createXMLOutput(System.out));
            editorContext.getEditor().initialize(editorContext, parent);
        } catch (JellyTagException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialize and run Jelly over the XML Schema in the context of an ElementView.
     *
     * @param parent
     * @param formToolkit
     * @param elementView
     */
    public void initialize(Composite parent, FormToolkit formToolkit, ElementView elementView) {
        try {
            editorContext = new EditorContext();
            editorContext.setLocalTransientElement(elementView.getElementWrapper().getElement());
            editorContext.setFormToolkit(formToolkit);
            editorContext.setContainer(parent);
            jellyContext.setVariable("editorContext", editorContext);
            script.run(jellyContext, XMLOutput.createXMLOutput(System.out));
            editorContext.getEditor().initialize(editorContext, parent);
        } catch (JellyTagException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public EditorContext getEditorContext() {
        return editorContext;
    }
}
