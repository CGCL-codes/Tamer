package org.adapit.wctoolkit.fomda.graphinterfaces.feature.transformationdescriptor;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.jar.JarFile;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileFilter;
import org.adapit.wctoolkit.infrastructure.DefaultApplicationFrame;
import org.adapit.wctoolkit.infrastructure.util.ResourceMessage;
import org.adapit.wctoolkit.infrastructure.util.SpringResourceMessage;
import org.adapit.wctoolkit.infrastructure.util.components.SwingWorker;
import org.adapit.wctoolkit.models.config.ApplicationReport;
import org.adapit.wctoolkit.models.config.ClassLoaderInformation;
import org.adapit.wctoolkit.models.config.JarClassLoader;
import org.adapit.wctoolkit.uml.ext.core.Expression;
import org.adapit.wctoolkit.uml.ext.fomda.metamodel.annotations.TransformationLanguage;
import org.adapit.wctoolkit.uml.ext.fomda.metamodel.features.TransformationFeatureElement;
import org.adapit.wctoolkit.uml.ext.fomda.metamodel.transformation.TransformationDescriptor;
import org.adapit.wctoolkit.uml.ext.fomda.metamodel.transformation.Transformer;

@SuppressWarnings("serial")
public class TransformationDescriptorPane extends JPanel {

    protected JLabel nameLabel, descLabel, srcLabel, classNameLabel, versLabel, langVersLabel, writerLabel, languageLabel, fieldReturnTypeLabel;

    protected JTextField nameTextField, srcTextField, algorithmVersionTextField, languageVersionTextField, languageTextField, fieldReturnTypeTextField;

    protected JTextPane writerDataTextPane;

    protected JTextPane descriptionTextPane;

    protected JTextField jarPathTextField;

    protected JButton jarBt;

    protected JButton okButton;

    protected JComboBox transformerClassComboBox;

    private JButton browseSrcButton;

    private JCheckBox interpretedCheckBox;

    protected TransformationDescriptor transformationDescriptor;

    private JFileChooser fc;

    protected TransformationFeatureElement observer;

    protected TransformationDescriptorFrame tdFrame;

    private TreeMap<String, ClassLoaderInformation> templateTransformers = new TreeMap<String, ClassLoaderInformation>();

    private ResourceMessage messages = SpringResourceMessage.getInstance();

    public org.adapit.wctoolkit.fomda.features.i18n.I18N_FomdaProfile messages2 = org.adapit.wctoolkit.fomda.features.i18n.I18N_FomdaProfile.getInstance();

    public TransformationDescriptorPane(TransformationDescriptor td, TransformationFeatureElement observer, TransformationDescriptorFrame tdFrame) {
        super();
        this.tdFrame = tdFrame;
        if (td != null) this.transformationDescriptor = td; else this.transformationDescriptor = new TransformationDescriptor(observer);
        this.observer = observer;
        initialize();
    }

    public TransformationDescriptorPane(TransformationFeatureElement observer, TransformationDescriptorFrame tdFrame) {
        super();
        this.tdFrame = tdFrame;
        this.observer = observer;
        transformationDescriptor = new TransformationDescriptor(observer);
        initialize();
    }

    public TransformationDescriptorPane(TransformationDescriptorFrame tdFrame, TransformationDescriptor td) {
        this.tdFrame = tdFrame;
        transformationDescriptor = td;
        initialize();
    }

    private void initialize() {
        this.setLayout(null);
        this.setSize(710, 390);
        this.add(getJarLabel());
        this.add(getBrowseJarButton());
        this.add(getJarPathTextField());
        this.add(getNameLabel());
        this.add(getNameTextField());
        this.add(getDescLabel());
        this.add(getDescriptionScrollPane());
        this.add(getSrcLabel());
        this.add(getSrcTextField());
        this.add(getBrowseSrcButton());
        this.add(getClassNameLabel());
        this.add(getTransformerClassComboBox());
        this.add(getLanguageLabel());
        this.add(getInterpreterCheckBox());
        this.add(getLanguageTextField());
        this.add(getTdVersionPanel());
        this.add(getWriterLabel());
        this.add(getWriterJsp());
        this.add(getTestTransformerButton(), null);
        this.add(getFieldReturnTypeLabel());
        this.add(getFieldReturnTypeTextField());
    }

    public JLabel getDescLabel() {
        if (descLabel == null) {
            descLabel = new JLabel("DescLabel");
            descLabel.setText(messages2.getMessage("Description") + ":");
            descLabel.setHorizontalAlignment(SwingConstants.LEFT);
            descLabel.setBounds(10, 85, 140, 22);
        }
        return descLabel;
    }

    public JTextPane getDescriptionTextPane() {
        if (descriptionTextPane == null) {
            descriptionTextPane = new JTextPane();
            descriptionTextPane.setBounds(110, 85, 400, 60);
            if (transformationDescriptor.getDescription() != null && !transformationDescriptor.getDescription().equals("")) {
                descriptionTextPane.setText(transformationDescriptor.getDescription());
            }
        }
        return descriptionTextPane;
    }

    public JLabel getLanguageLabel() {
        if (languageLabel == null) {
            languageLabel = new JLabel();
            languageLabel.setText(messages2.getMessage("Language") + ":");
            languageLabel.setHorizontalAlignment(SwingConstants.LEFT);
            languageLabel.setBounds(10, 150, 140, 22);
        }
        return languageLabel;
    }

    public JTextField getLanguageTextField() {
        if (languageTextField == null) {
            languageTextField = new JTextField();
            languageTextField.setBounds(150, 150, 100, 22);
            if (transformationDescriptor.getLanguage() != null && !transformationDescriptor.getLanguage().equals("")) {
                languageTextField.setText(transformationDescriptor.getLanguage().name().toUpperCase());
            }
        }
        return languageTextField;
    }

    public JLabel getLangVersLabel() {
        if (langVersLabel == null) {
            langVersLabel = new JLabel();
            langVersLabel.setText(messages2.getMessage("Version") + ":");
            langVersLabel.setHorizontalAlignment(SwingConstants.CENTER);
            langVersLabel.setBounds(310, 200, 200, 22);
        }
        return langVersLabel;
    }

    public JTextField getLanguageVersionTextField() {
        if (languageVersionTextField == null) {
            languageVersionTextField = new JTextField();
            languageVersionTextField.setBounds(385, 220, 45, 22);
            if (transformationDescriptor.getLanguageVersion() != null && !transformationDescriptor.getLanguageVersion().equals("")) {
                languageVersionTextField.setText(transformationDescriptor.getLanguageVersion());
            }
        }
        return languageVersionTextField;
    }

    public JLabel getNameLabel() {
        if (nameLabel == null) {
            nameLabel = new JLabel();
            nameLabel.setText(messages2.getMessage("Name") + ":");
            nameLabel.setHorizontalAlignment(SwingConstants.LEFT);
            nameLabel.setBounds(10, 60, 140, 22);
        }
        return nameLabel;
    }

    public JTextField getNameTextField() {
        if (nameTextField == null) {
            nameTextField = new JTextField();
            nameTextField.setBounds(150, 60, 517, 24);
            nameTextField.setHorizontalAlignment(SwingConstants.LEFT);
            if (transformationDescriptor.getName() != null && !transformationDescriptor.getName().equals("")) {
                nameTextField.setText(transformationDescriptor.getName());
            }
        }
        return nameTextField;
    }

    public JLabel getSrcLabel() {
        if (srcLabel == null) {
            srcLabel = new JLabel();
            srcLabel.setText(messages2.getMessage("Algorithm_Src") + ":");
            srcLabel.setHorizontalAlignment(SwingConstants.LEFT);
            srcLabel.setBounds(10, 175, 140, 22);
        }
        return srcLabel;
    }

    public JTextField getSrcTextField() {
        if (srcTextField == null) {
            srcTextField = new JTextField();
            srcTextField.setBounds(150, 175, 360, 24);
            if (transformationDescriptor != null) {
                srcTextField.setEnabled(transformationDescriptor.isTransformationInterpreter());
                if (!transformationDescriptor.isTransformationInterpreter()) {
                    srcTextField.setText(messages2.getMessage("Embedded_into_such_transformer"));
                } else srcTextField.setText(messages2.getMessage("Please_select_the_algorithm_file"));
            } else {
                srcTextField.setEnabled(false);
                srcTextField.setText(messages2.getMessage("Embedded_into_such_transformer"));
            }
            if (transformationDescriptor.getSrc() != null && !transformationDescriptor.getSrc().equals("")) {
                srcTextField.setText(transformationDescriptor.getSrc());
            }
        }
        return srcTextField;
    }

    public JLabel getClassNameLabel() {
        if (classNameLabel == null) {
            classNameLabel = new JLabel();
            classNameLabel.setText(messages2.getMessage("Class_Name") + ":");
            classNameLabel.setHorizontalAlignment(SwingConstants.LEFT);
            classNameLabel.setBounds(10, 35, 140, 22);
        }
        return classNameLabel;
    }

    public JTextField getAlgorithmVersionTextField() {
        if (algorithmVersionTextField == null) {
            algorithmVersionTextField = new JTextField();
            algorithmVersionTextField.setBounds(185, 220, 45, 22);
            if (transformationDescriptor.getVersion() != null && !transformationDescriptor.getVersion().equals("")) {
                algorithmVersionTextField.setText(transformationDescriptor.getVersion());
            }
        }
        return algorithmVersionTextField;
    }

    public JLabel getWriterLabel() {
        if (writerLabel == null) {
            writerLabel = new JLabel();
            writerLabel.setText(messages2.getMessage("Author_Data") + ":");
            writerLabel.setHorizontalAlignment(SwingConstants.LEFT);
            writerLabel.setBounds(10, 205, 140, 22);
        }
        return writerLabel;
    }

    public JTextPane getWriterDataTextPane() {
        if (writerDataTextPane == null) {
            writerDataTextPane = new JTextPane();
            writerDataTextPane.setBounds(110, 205, 400, 60);
            if (transformationDescriptor.getWriter() != null && !transformationDescriptor.getWriter().equals("")) {
                writerDataTextPane.setText(transformationDescriptor.getWriter());
            }
        }
        return writerDataTextPane;
    }

    private JButton getBrowseSrcButton() {
        if (browseSrcButton == null) {
            browseSrcButton = new JButton();
            browseSrcButton.setBounds(515, 175, 30, 24);
            browseSrcButton.setIcon(getIcon("/img/browse.png"));
            if (transformationDescriptor != null) {
                browseSrcButton.setEnabled(transformationDescriptor.isTransformationInterpreter());
            } else browseSrcButton.setEnabled(false);
            browseSrcButton.addMouseListener(new MouseAdapter() {

                public void mouseReleased(MouseEvent e) {
                    fc = new JFileChooser();
                    int returnVal = fc.showOpenDialog(TransformationDescriptorPane.this);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        srcTextField.setText(fc.getSelectedFile().getAbsolutePath());
                        transformationDescriptor.setSrc(fc.getSelectedFile().getAbsolutePath());
                    }
                }
            });
        }
        return browseSrcButton;
    }

    private JScrollPane descriptionScrollPane;

    private JScrollPane getDescriptionScrollPane() {
        if (descriptionScrollPane == null) {
            descriptionScrollPane = new JScrollPane();
            descriptionScrollPane.setBounds(150, 87, 550, 60);
            descriptionScrollPane.add(getDescriptionTextPane());
            descriptionScrollPane.setViewportView(getDescriptionTextPane());
        }
        return descriptionScrollPane;
    }

    private JCheckBox getInterpreterCheckBox() {
        if (interpretedCheckBox == null) {
            interpretedCheckBox = new JCheckBox(messages2.getMessage("Is_Interpreter"));
            interpretedCheckBox.setBounds(280, 150, 268, 22);
            if (transformationDescriptor != null) {
                interpretedCheckBox.setSelected(transformationDescriptor.isTransformationInterpreter());
            }
            interpretedCheckBox.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    JCheckBox jcb = (JCheckBox) evt.getSource();
                    if (jcb.isSelected()) {
                        srcTextField.setEnabled(true);
                        browseSrcButton.setEnabled(true);
                        srcTextField.setText(messages2.getMessage("Please_select_the_algorithm_file"));
                    } else {
                        srcTextField.setEnabled(false);
                        browseSrcButton.setEnabled(false);
                        srcTextField.setText(messages2.getMessage("Embedded_into_such_transformer"));
                    }
                }
            });
        }
        return interpretedCheckBox;
    }

    private JPanel tdVersionPanel;

    private JPanel getTdVersionPanel() {
        if (tdVersionPanel == null) {
            tdVersionPanel = new JPanel();
            java.awt.GridLayout gl = new java.awt.GridLayout(2, 2);
            gl.setHgap(4);
            gl.setVgap(3);
            tdVersionPanel.setLayout(gl);
            tdVersionPanel.setBounds(550, 150, 150, 50);
            tdVersionPanel.add(getLangVersLabel());
            tdVersionPanel.add(getLanguageVersionTextField());
            tdVersionPanel.add(getVersLabel());
            tdVersionPanel.add(getAlgorithmVersionTextField());
        }
        return tdVersionPanel;
    }

    private JScrollPane writerJsp;

    private JScrollPane getWriterJsp() {
        if (writerJsp == null) {
            writerJsp = new JScrollPane();
            writerJsp.setBounds(150, 205, 550, 60);
            writerJsp.add(getWriterDataTextPane());
            writerJsp.setViewportView(getWriterDataTextPane());
        }
        return writerJsp;
    }

    private JLabel getFieldReturnTypeLabel() {
        if (fieldReturnTypeLabel == null) {
            fieldReturnTypeLabel = new JLabel();
            fieldReturnTypeLabel.setText(messages2.getMessage("Return_Type") + ":");
            fieldReturnTypeLabel.setHorizontalAlignment(SwingConstants.LEFT);
            fieldReturnTypeLabel.setBounds(10, 270, 150, 22);
        }
        return fieldReturnTypeLabel;
    }

    private JTextField getFieldReturnTypeTextField() {
        if (fieldReturnTypeTextField == null) {
            fieldReturnTypeTextField = new JTextField();
            fieldReturnTypeTextField.setBounds(150, 270, 550, 22);
            if (transformationDescriptor.getReturnType() != null && !transformationDescriptor.getReturnType().equals("")) {
                fieldReturnTypeTextField.setText(transformationDescriptor.getReturnType());
            }
        }
        return fieldReturnTypeTextField;
    }

    public JComboBox getTransformerClassComboBox() {
        if (transformerClassComboBox == null) {
            transformerClassComboBox = new JComboBox();
            transformerClassComboBox.setBounds(150, 33, 550, 24);
            transformerClassComboBox.setEditable(true);
            if (transformationDescriptor.getClassName() != null && !transformationDescriptor.getClassName().equals("")) {
                transformerClassComboBox.addItem(transformationDescriptor.getClassName());
            }
            transformerClassComboBox.addItemListener(new ItemListener() {

                public void itemStateChanged(ItemEvent evt) {
                    try {
                        if (evt.getStateChange() == ItemEvent.SELECTED) {
                            if (transformerClassComboBox.getItemCount() > 0) try {
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        return transformerClassComboBox;
    }

    public JLabel getVersLabel() {
        if (versLabel == null) {
            versLabel = new JLabel();
            versLabel.setText(messages2.getMessage("Version") + ":");
            versLabel.setHorizontalAlignment(SwingConstants.CENTER);
            versLabel.setBounds(110, 200, 200, 22);
        }
        return versLabel;
    }

    public JTextField getJarPathTextField() {
        if (jarPathTextField == null) {
            jarPathTextField = new JTextField();
            jarPathTextField.setBounds(150, 6, 517, 24);
            jarPathTextField.setHorizontalAlignment(SwingConstants.LEFT);
            jarPathTextField.setText(transformationDescriptor.getJarUrl());
        }
        return jarPathTextField;
    }

    private JLabel jarLabel;

    private JLabel getJarLabel() {
        if (jarLabel == null) {
            jarLabel = new JLabel();
            jarLabel.setText(messages2.getMessage("Jar_File") + ":");
            jarLabel.setHorizontalAlignment(SwingConstants.LEFT);
            jarLabel.setBounds(10, 6, 140, 22);
        }
        return jarLabel;
    }

    private JButton browseJarButton;

    private JButton testTransformerButton = null;

    private JButton getBrowseJarButton() {
        if (browseJarButton == null) {
            browseJarButton = new JButton();
            browseJarButton.setHorizontalAlignment(SwingConstants.CENTER);
            browseJarButton.setBounds(670, 6, 30, 24);
            browseJarButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    fc = new javax.swing.JFileChooser();
                    fc.setFileFilter(new FormFilter());
                    fc.setDialogTitle(messages.getMessage("org.adapit.wctoolkit.fomda.diagram.guieditor.GUITemplateDialog.Loading_Jar_File"));
                    int returnVal = fc.showOpenDialog(null);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        final SwingWorker s = new SwingWorker() {

                            @SuppressWarnings("unchecked")
                            public Object construct() {
                                File inputFile = fc.getSelectedFile();
                                getJarPathTextField().setText(inputFile.getAbsolutePath());
                                transformationDescriptor.setJarUrl(inputFile.getAbsolutePath());
                                try {
                                    TreeMap tm = JarClassLoader.listClassByTypesInsideJarFile(Transformer.class, new JarFile(inputFile), inputFile);
                                    Iterator<ClassLoaderInformation> it = tm.values().iterator();
                                    getTransformerClassComboBox().removeAllItems();
                                    while (it.hasNext()) {
                                        ClassLoaderInformation cn = it.next();
                                        getTransformerClassComboBox().addItem(cn.getClassName());
                                    }
                                    getTransformerClassComboBox().updateUI();
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                                return null;
                            }
                        };
                        s.start();
                    } else {
                        ApplicationReport.getInstance().getLogTextArea().append(messages.getMessage("org.adapit.wctoolkit.fomda.diagram.guieditor.GUITemplateDialog.Opencommandcancelledbyuser") + '\n');
                    }
                    ApplicationReport.getInstance().getLogTextArea().setCaretPosition(ApplicationReport.getInstance().getLogTextArea().getDocument().getLength());
                }
            });
            browseJarButton.setIcon(getIcon("/img/browse.png"));
        }
        return browseJarButton;
    }

    public TransformationDescriptor getTransformationDescriptor() {
        return transformationDescriptor;
    }

    public void setTransformationDescriptor(TransformationDescriptor td) {
        this.transformationDescriptor = td;
    }

    public TransformationFeatureElement getObserver() {
        return observer;
    }

    public void setObserver(TransformationFeatureElement observer) {
        this.observer = observer;
    }

    public javax.swing.Icon getIcon(String name) {
        try {
            java.net.URL imURL = getClass().getResource(name);
            if (imURL != null) {
                java.awt.Image image = new javax.swing.ImageIcon(imURL).getImage();
                if (image != null) {
                    image = image.getScaledInstance(18, 18, java.awt.Image.SCALE_SMOOTH);
                    javax.swing.Icon icon = new javax.swing.ImageIcon(image);
                    return icon;
                }
            }
        } catch (java.lang.StackOverflowError e) {
            e.printStackTrace();
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private class FormFilter extends FileFilter {

        public boolean accept(File f) {
            if (f.isDirectory()) return true;
            int index = f.getName().indexOf(".");
            String ext = f.getName().substring(index + 1, f.getName().length());
            if (ext.equalsIgnoreCase("jar")) return true;
            return false;
        }

        @Override
        public String getDescription() {
            return "jar";
        }
    }

    public TransformationDescriptor reverseBind() {
        transformationDescriptor = new TransformationDescriptor(observer);
        transformationDescriptor.setName(this.nameTextField.getText());
        transformationDescriptor.setDescription(this.descriptionTextPane.getText());
        transformationDescriptor.setSrc(this.srcTextField.getText());
        transformationDescriptor.setJarUrl((String) this.getJarPathTextField().getText());
        transformationDescriptor.setClassName((String) this.transformerClassComboBox.getSelectedItem());
        transformationDescriptor.setVersion(this.algorithmVersionTextField.getText());
        transformationDescriptor.setLanguageVersion(this.languageVersionTextField.getText());
        transformationDescriptor.setWriter(this.writerDataTextPane.getText());
        Expression exp = new Expression();
        exp.setLanguage(this.languageTextField.getText());
        transformationDescriptor.setLanguage(TransformationLanguage.valueOf(this.languageTextField.getText().toUpperCase()));
        transformationDescriptor.setReturnType(this.fieldReturnTypeTextField.getText());
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
        transformationDescriptor.setTransformationInterpreter(interpretedCheckBox.isSelected());
        return transformationDescriptor;
    }

    public TransformationDescriptor bind() {
        transformationDescriptor.setName(this.nameTextField.getText());
        transformationDescriptor.setDescription(this.descriptionTextPane.getText());
        transformationDescriptor.setSrc(this.srcTextField.getText());
        transformationDescriptor.setJarUrl((String) this.getJarPathTextField().getText());
        transformationDescriptor.setClassName((String) this.transformerClassComboBox.getSelectedItem());
        transformationDescriptor.setVersion(this.algorithmVersionTextField.getText());
        transformationDescriptor.setLanguageVersion(this.languageVersionTextField.getText());
        transformationDescriptor.setWriter(this.writerDataTextPane.getText());
        Expression exp = new Expression();
        exp.setLanguage(this.languageTextField.getText());
        transformationDescriptor.setLanguage(TransformationLanguage.valueOf(this.languageTextField.getText().toUpperCase()));
        transformationDescriptor.setReturnType(this.fieldReturnTypeTextField.getText());
        try {
            transformationDescriptor.setJarUrl(this.getJarPathTextField().getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
        transformationDescriptor.setTransformationInterpreter(interpretedCheckBox.isSelected());
        return transformationDescriptor;
    }

    public void refresh(TransformationDescriptor td) {
        this.nameTextField.setText(td.getName());
        this.descriptionTextPane.setText(td.getDescription());
        this.srcTextField.setText(td.getSrc());
        if (td.getJarUrl() != null && !td.getJarUrl().equals("")) this.getJarPathTextField().setText(td.getJarUrl());
        int numItens = this.transformerClassComboBox.getItemCount();
        for (int i = 0; i < numItens; i++) {
            String value = (String) this.transformerClassComboBox.getItemAt(i);
            if (value.equals(td.getClassName())) {
                this.transformerClassComboBox.setSelectedIndex(i);
                break;
            }
        }
        this.algorithmVersionTextField.setText(td.getVersion());
        this.languageVersionTextField.setText(td.getLanguageVersion());
        this.writerDataTextPane.setText(td.getWriter());
        this.fieldReturnTypeTextField.setText(td.getReturnType());
        this.languageTextField.setText(td.getLanguage().name().toUpperCase());
        this.tdFrame.inputParameterPane.refresh(transformationDescriptor);
        this.tdFrame.sharedVarPane.refresh(transformationDescriptor);
        this.tdFrame.specializationPointPane.refresh(transformationDescriptor);
        interpretedCheckBox.setSelected(transformationDescriptor.isTransformationInterpreter());
        updateUI();
    }

    private JButton getTestTransformerButton() {
        if (testTransformerButton == null) {
            testTransformerButton = new JButton();
            testTransformerButton.setBounds(new Rectangle(670, 60, 30, 24));
            testTransformerButton.setToolTipText(messages2.getMessage("Test_if_the_transformer_is_created"));
            testTransformerButton.setIcon(new ImageIcon(getClass().getResource("/img/TransformationTask.png")));
            testTransformerButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    TransformationDescriptor td = bind();
                    try {
                        td.testTransformerInstantiation(false);
                        JOptionPane.showMessageDialog(DefaultApplicationFrame.getInstance(), messages2.getMessage("transformer_has_been_created_successfully"), messages2.getMessage("Test_if_the_transformer_is_created"), JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(DefaultApplicationFrame.getInstance(), messages2.getMessage("transformer_has_not_been_created"), messages2.getMessage("Test_if_the_transformer_is_created"), JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            });
        }
        return testTransformerButton;
    }

    protected void generateTransformationDescriptor() {
        if (transformationDescriptor == null) {
            transformationDescriptor = new TransformationDescriptor(this.observer);
        }
        try {
            transformationDescriptor.setClassName((String) this.transformerClassComboBox.getSelectedItem());
            ClassLoaderInformation cn = (ClassLoaderInformation) templateTransformers.get(transformationDescriptor.getClassName());
            Transformer t = (Transformer) JarClassLoader.getInstance(cn);
            transformationDescriptor.setTransformer(t);
            t.setTransformationDecriptor(transformationDescriptor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
