package org.objectwiz.fxclient.renderer;

import org.objectwiz.EntityRepresentation;
import org.objectwiz.metadata.MappedClass;
import org.objectwiz.ui.HtmlParser;
import org.objectwiz.util.FileUtils;
import java.awt.Component;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JList;
import javax.swing.JTextPane;
import javax.swing.ListCellRenderer;

/**
 * @author Benoît Del Basso <benoit.delbasso at helmet.fr>
 */
public class HtmlListCellRenderer extends JTextPane implements ListCellRenderer {

    private HtmlParser parser;

    private MappedClass mappedClass;

    private Class itemsClass;

    private EntityRepresentation representation;

    private Map<Object, String> htmlCache = new HashMap();

    public HtmlListCellRenderer(MappedClass mappedClass, String templateFile) {
        if (mappedClass == null) throw new NullPointerException("mappedClass");
        init(mappedClass.getMetadata().getEntityRepresentation(), null, mappedClass, templateFile);
    }

    public HtmlListCellRenderer(Class itemsClass, String templateFile) {
        if (itemsClass == null) throw new NullPointerException("itemsClass");
        init(EntityRepresentation.POJO, itemsClass, null, templateFile);
    }

    protected void init(EntityRepresentation representation, Class itemsClass, MappedClass mappedClass, String templateFile) {
        this.itemsClass = itemsClass;
        this.mappedClass = mappedClass;
        this.representation = representation;
        InputStream in = this.getClass().getResourceAsStream("/templates/" + templateFile + ".xhtml");
        if (in == null) throw new RuntimeException("Template not found: " + templateFile);
        try {
            String template = new String(FileUtils.readInputStreamAsBytes(in));
            parser = new HtmlParser(representation, template);
        } catch (IOException e) {
            throw new RuntimeException("Error while reading template: " + templateFile, e);
        }
        setContentType("text/html");
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value == null) throw new NullPointerException("value is NULL !");
        if ((itemsClass != null && (!itemsClass.isInstance(value))) || (mappedClass != null && (!mappedClass.isInstance(value)))) {
            throw new RuntimeException("Invalid object, expected " + itemsClass == null ? mappedClass.getClassName() : itemsClass.getName() + ", got: " + value.getClass().getName());
        }
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        String html = htmlCache.get(value);
        if (html == null) {
            html = parser.parse(value);
            htmlCache.put(value, html);
        }
        setText(html);
        return this;
    }
}
