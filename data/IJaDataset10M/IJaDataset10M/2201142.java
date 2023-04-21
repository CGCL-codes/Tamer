package de.mse.mogwai.utils.erdesigner.plugins.hibernate;

import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import de.mse.mogwai.utils.erdesigner.dialogs.DialogConstants;
import de.mse.mogwai.utils.erdesigner.types.EntityContainer;
import de.mse.mogwai.utils.erdesigner.utils.DirectoryFileFilter;

/**
 * Generator form for EJB sourcec code generation.
 * 
 * @author msertic
 */
public class HibernateGeneratorEditor extends JDialog implements DialogConstants {

    private EntityContainer m_container;

    private int m_modalResult = MODAL_RESULT_CANCEL;

    private HibernateGeneratorEditorView m_view;

    /**
	 * This is the default constructor
	 */
    public HibernateGeneratorEditor() {
        super();
        initialize();
    }

    public HibernateGeneratorEditor(JFrame parent) {
        super(parent);
        initialize();
    }

    public void setContainer(EntityContainer container) {
        this.m_container = container;
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        this.m_view = new HibernateGeneratorEditorView() {

            public void handleSource_folderActionPerformed(String actionCommand) {
            }

            public void handleFindButtonActionPerformed(String actionCommand) {
                doSelectDirectory();
            }

            public void handlePackagenameActionPerformed(String actionCommand) {
            }

            public void handleIdgeneratorActionPerformed(String actionCommand) {
                doIDGeneratorSelection();
            }

            public void handleSequencetableActionPerformed(String actionCommand) {
            }

            public void handleOkButtonActionPerformed(String actionCommand) {
                doGenerate();
            }

            public void handleCancelButtonActionPerformed(String actionCommand) {
                doCancel();
            }
        };
        this.setContentPane(this.m_view);
        this.setTitle("Mogwai ER-Designer Hibernate - Generator");
        this.pack();
        Vector items = new Vector();
        items.add("assigned");
        items.add("native");
        items.add("identity");
        items.add("sequence");
        items.add("increment");
        items.add("hilo");
        items.add("uuid.hex");
        this.m_view.getIdgenerator().setModel(new DefaultComboBoxModel(items));
        this.m_view.getIdgenerator().setSelectedItem("sequence");
    }

    /**
	 * Handle source directory selection.
	 */
    private void doSelectDirectory() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new DirectoryFileFilter());
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        String source = this.m_view.getSource_folder().getText();
        if ((source != null) && (source.length() > 0)) {
            File where = new File(source);
            if (where.exists()) chooser.setSelectedFile(where);
        }
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            this.m_view.getSource_folder().setText(chooser.getSelectedFile().toString());
        }
    }

    private void doIDGeneratorSelection() {
        this.m_view.getSequencetable().setEnabled("sequence".equals(this.m_view.getIdgenerator().getSelectedItem()));
    }

    /**
	 * Handle the generate function.
	 */
    private void doGenerate() {
        this.m_modalResult = MODAL_RESULT_OK;
        this.hide();
    }

    /**
	 * Handle the cancel button.
	 */
    private void doCancel() {
        this.m_modalResult = MODAL_RESULT_CANCEL;
        this.hide();
    }

    /**
	 * Show this dialog in modal mode.
	 * 
	 */
    public int showModal() {
        this.setModal(true);
        this.m_view.getSource_folder().setText(m_container.getPropertyAsString(HibernateGenerator.HIBERNATE_SOURCEDIR));
        this.m_view.getPackagename().setText(m_container.getPropertyAsString(HibernateGenerator.HIBERNATE_PACKAGENAME));
        this.m_view.getIdgenerator().setSelectedItem((String) m_container.getPropertyAsString(HibernateGenerator.HIBERNATE_SEQUENCE_TYPE));
        String sequenceName = (String) m_container.getPropertyAsString(HibernateGenerator.HIBERNATE_SEQUENCE_NAME);
        if ((sequenceName == null) || (sequenceName.length()) == 0) sequenceName = "<TABLENAME>_SEQ";
        this.m_view.getSequencetable().setText(sequenceName);
        this.m_view.getSelectBeforeUpdate().setSelected("true".equals(m_container.getPropertyAsString(HibernateGenerator.HIBERNATE_SELECTBEFOREUPDATE)));
        JFrame parentf = this.m_container.getParentFrame();
        Dimension w2 = this.getSize();
        Dimension w1 = parentf.getSize();
        Point p = parentf.getLocation();
        this.setLocation(p.x + w1.width / 2 - w2.width / 2, p.y + w1.height / 2 - w2.height / 2);
        this.show();
        if (this.m_modalResult == MODAL_RESULT_OK) {
            this.m_container.setProperty(HibernateGenerator.HIBERNATE_SOURCEDIR, this.m_view.getSource_folder().getText());
            this.m_container.setProperty(HibernateGenerator.HIBERNATE_PACKAGENAME, this.m_view.getPackagename().getText());
            this.m_container.setProperty(HibernateGenerator.HIBERNATE_SEQUENCE_NAME, this.m_view.getSequencetable().getText());
            this.m_container.setProperty(HibernateGenerator.HIBERNATE_SELECTBEFOREUPDATE, this.m_view.getSelectBeforeUpdate().isSelected() ? "true" : "false");
            this.m_container.setProperty(HibernateGenerator.HIBERNATE_SEQUENCE_TYPE, (String) this.m_view.getIdgenerator().getSelectedItem());
        }
        return this.m_modalResult;
    }
}
