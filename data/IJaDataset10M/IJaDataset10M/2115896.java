package org.softmed.rest.editor.comps.full;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import net.miginfocom.swing.MigLayout;
import org.softmed.rest.editor.EditorUtil;
import org.softmed.rest.editor.FilePathAware;
import org.softmed.rest.editor.FilePathProvider;
import org.softmed.rest.editor.commons.SwingUtil;
import org.softmed.rest.editor.commons.URIProvider;
import org.softmed.rest.editor.comps.HttpMethodEditor;
import org.softmed.rest.editor.comps.utils.DeletePanel;
import org.softmed.rest.editor.comps.utils.FieldPanel;
import org.softmed.rest.editor.comps.utils.list.ListFieldPanel;
import org.softmed.rest.editor.comps.utils.list.showListFieldDetails;

public class HttpMethodEditorFull extends HttpMethodEditor implements getDeletePanel, showListFieldDetails, FilePathAware {

    DeletePanel deletePanel = new DeletePanel(this);

    FieldPanel field = new FieldPanel("Handler", "handler", HandlerEditorFull.class, this);

    ListFieldPanel list = new ListFieldPanel("Mime Handlers", "mime-type-handlers", MimeTypeHandlerEditorFull.class, this);

    int width = 602;

    public HttpMethodEditorFull(URIProvider uriProvider) throws Throwable {
        super(uriProvider);
        setup();
    }

    public void setFullBackground(Color color) {
        setBackground(color);
        list.setFullBackground(color);
        active.setBackground(color);
        field.setFullBackground(color);
        deletePanel.setBackground(color);
        maxMinPanel.setBackground(color);
    }

    public void setup() {
        setFullBackground(new Color(187, 240, 141));
        setBorder(BorderFactory.createLineBorder(new Color(111, 147, 106)));
        active.setText("Active");
        SwingUtil.setMinSize(this, width, 60);
        MigLayout layout = new MigLayout("left, insets " + EditorUtil.smallestInsets + ", gap 0");
        setLayout(layout);
        JLabel title = new JLabel("HttpMethod");
        title.setFont(SwingUtil.editorTitleFont);
        add(title, "w 74!");
        add(id, "w 25!");
        add(active, "w 70!");
        add(new JLabel("Type"), "w 33!");
        add(name, "w 70!, h 20!");
        add(Box.createHorizontalStrut(210));
        add(maxMinPanel);
        add(deletePanel, "wrap");
        add(field, "span 10,w 596!, wrap");
        add(list, "span 10, wrap");
        minimize();
    }

    public void setEntity(Object entity) throws Throwable {
        super.setEntity(entity);
        field.setEntity(httpMethod.getHandler());
    }

    @Override
    public DeletePanel getDeletePanel() {
        return deletePanel;
    }

    @Override
    public void hideListFieldDetails() {
        list.hideListFieldDetails();
    }

    @Override
    public void showListFieldDetails() throws Throwable {
        list.showListFieldDetails();
    }

    @Override
    public void setFilePathProvider(FilePathProvider provider) {
        field.setFilePathProvider(provider);
        list.setFilePathProvider(provider);
    }

    @Override
    public void customMaximize() {
        field.setVisible(true);
        list.setVisible(true);
        SwingUtil.setMinSize(this, width, 60);
    }

    @Override
    public void customMinimize() {
        field.setVisible(false);
        list.setVisible(false);
        SwingUtil.setSize(this, width, 30);
    }
}
