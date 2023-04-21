package org.qtitools.validatr.panel;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import org.qtitools.validatr.model.AssessmentDocument;
import org.qtitools.validatr.model.AssessmentDocumentAdapter;
import org.qtitools.validatr.model.AssessmentDocumentEvent;
import org.qtitools.validatr.model.ValidatorModel;
import org.qtitools.validatr.model.ValidatorModelAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This panel handles source code of assessment document.
 */
public class SourcePanel extends AbstractPanel {

    private static final long serialVersionUID = 1L;

    /** Logger. */
    protected Logger logger = LoggerFactory.getLogger(SourcePanel.class);

    private SourceEditor sourceEditor;

    /**
	 * Constructs panel.
	 *
	 * @param model model of application
	 * @param viewType type of view for constructed panel
	 */
    public SourcePanel(ValidatorModel model, ViewType viewType) {
        super(model, viewType);
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        sourceEditor = new SourceEditor();
        sourceEditor.setLineWrap(true);
        sourceEditor.setWrapStyleWord(true);
        add(new JScrollPane(sourceEditor));
        initModelListeners();
        initViewListeners();
    }

    @Override
    public void setViewType(ViewType type) {
        super.setViewType(type);
        update(false);
    }

    @Override
    public void update(boolean deep) {
        updateSelf();
    }

    private void initModelListeners() {
        getModel().addValidatorModelListener(new ValidatorModelAdapter() {

            @Override
            protected void selectionChanged(AssessmentDocument oldSelection, AssessmentDocument newSelection) {
                updateSelf();
            }
        });
        getModel().addAssessmentDocumentListener(this, new AssessmentDocumentAdapter() {

            @Override
            protected void modified(AssessmentDocumentEvent event) {
                updateSelf();
            }

            @Override
            public void nodeSelected(AssessmentDocumentEvent event) {
                if (getViewType() == ViewType.NODE) updateSelf();
            }
        });
    }

    private void updateSelf() {
        sourceEditor.setActualNode(getActualNode());
    }

    private void initViewListeners() {
        sourceEditor.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent event) {
                if (getSelectedDocument() != null && event.getKeyCode() == KeyEvent.VK_ENTER && event.isControlDown()) changeNode();
            }
        });
    }

    public void changeNode() {
        try {
            String changeValue = sourceEditor.getText();
            if (changeValue.contains("baseValue baseType=\"string\"")) {
                changeValue = changeValue.replace("baseValue baseType=\"string\">", "baseValue baseType=\"string\"><![CDATA[");
                changeValue = changeValue.replace("</baseValue>", "]]></baseValue>");
            }
            System.out.println(changeValue);
            getSelectedDocument().loadNode(SourcePanel.this, sourceEditor.getActualNode(), changeValue);
        } catch (Throwable ex) {
            String message = ex.getMessage();
            if (message == null || message.length() == 0) message = "Error while changing node.";
            logger.error(message, ex);
            JOptionPane.showMessageDialog(SourcePanel.this, message, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
