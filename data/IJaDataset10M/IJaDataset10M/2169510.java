package com.eteks.sweethome3d.swing;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.TexturePaint;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.imageio.ImageIO;
import javax.swing.ComboBoxEditor;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import com.eteks.sweethome3d.model.CatalogTexture;
import com.eteks.sweethome3d.model.Content;
import com.eteks.sweethome3d.model.LengthUnit;
import com.eteks.sweethome3d.model.RecorderException;
import com.eteks.sweethome3d.model.TexturesCategory;
import com.eteks.sweethome3d.model.UserPreferences;
import com.eteks.sweethome3d.tools.OperatingSystem;
import com.eteks.sweethome3d.tools.TemporaryURLContent;
import com.eteks.sweethome3d.viewcontroller.ContentManager;
import com.eteks.sweethome3d.viewcontroller.ImportedTextureWizardController;
import com.eteks.sweethome3d.viewcontroller.View;

/**
 * Wizard panel for background image choice. 
 * @author Emmanuel Puybaret
 */
public class ImportedTextureWizardStepsPanel extends JPanel implements View {

    private final ImportedTextureWizardController controller;

    private CardLayout cardLayout;

    private JLabel imageChoiceOrChangeLabel;

    private JButton imageChoiceOrChangeButton;

    private JLabel imageChoiceErrorLabel;

    private ScaledImageComponent imageChoicePreviewComponent;

    private JLabel attributesLabel;

    private JLabel nameLabel;

    private JTextField nameTextField;

    private JLabel categoryLabel;

    private JComboBox categoryComboBox;

    private JLabel widthLabel;

    private JSpinner widthSpinner;

    private JLabel heightLabel;

    private JSpinner heightSpinner;

    private ScaledImageComponent attributesPreviewComponent;

    private Executor imageLoader;

    private static BufferedImage waitImage;

    /**
   * Creates a view for texture image choice and attributes. 
   */
    public ImportedTextureWizardStepsPanel(CatalogTexture catalogTexture, String textureName, UserPreferences preferences, final ImportedTextureWizardController controller) {
        this.controller = controller;
        this.imageLoader = Executors.newSingleThreadExecutor();
        createComponents(preferences, controller);
        setMnemonics(preferences);
        layoutComponents();
        updateController(catalogTexture, preferences);
        if (textureName != null) {
            updateController(textureName, controller.getContentManager(), preferences, true);
        }
        controller.addPropertyChangeListener(ImportedTextureWizardController.Property.STEP, new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                updateStep(controller);
            }
        });
    }

    /**
   * Creates components displayed by this panel.
   */
    private void createComponents(final UserPreferences preferences, final ImportedTextureWizardController controller) {
        String unitName = preferences.getLengthUnit().getName();
        this.imageChoiceOrChangeLabel = new JLabel();
        this.imageChoiceOrChangeButton = new JButton();
        this.imageChoiceOrChangeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                String imageName = showImageChoiceDialog(preferences, controller.getContentManager());
                if (imageName != null) {
                    updateController(imageName, controller.getContentManager(), preferences, false);
                }
            }
        });
        this.imageChoiceErrorLabel = new JLabel(preferences.getLocalizedString(ImportedTextureWizardStepsPanel.class, "imageChoiceErrorLabel.text"));
        this.imageChoiceErrorLabel.setVisible(false);
        this.imageChoicePreviewComponent = new ScaledImageComponent();
        this.imageChoicePreviewComponent.setTransferHandler(new TransferHandler() {

            @Override
            public boolean canImport(JComponent comp, DataFlavor[] flavors) {
                return Arrays.asList(flavors).contains(DataFlavor.javaFileListFlavor);
            }

            @Override
            public boolean importData(JComponent comp, Transferable transferedFiles) {
                boolean success = true;
                try {
                    List<File> files = (List<File>) transferedFiles.getTransferData(DataFlavor.javaFileListFlavor);
                    String textureName = files.get(0).getAbsolutePath();
                    updateController(textureName, controller.getContentManager(), preferences, false);
                } catch (UnsupportedFlavorException ex) {
                    success = false;
                } catch (IOException ex) {
                    success = false;
                }
                if (!success) {
                    JOptionPane.showMessageDialog(SwingUtilities.getRootPane(ImportedTextureWizardStepsPanel.this), preferences.getLocalizedString(ImportedTextureWizardStepsPanel.class, "imageChoiceError"));
                }
                return success;
            }
        });
        this.imageChoicePreviewComponent.setBorder(SwingTools.getDropableComponentBorder());
        this.attributesLabel = new JLabel(preferences.getLocalizedString(ImportedTextureWizardStepsPanel.class, "attributesLabel.text"));
        this.nameLabel = new JLabel(SwingTools.getLocalizedLabelText(preferences, ImportedTextureWizardStepsPanel.class, "nameLabel.text"));
        this.nameTextField = new JTextField(10);
        if (!OperatingSystem.isMacOSXLeopardOrSuperior()) {
            SwingTools.addAutoSelectionOnFocusGain(this.nameTextField);
        }
        final Color defaultNameTextFieldColor = this.nameTextField.getForeground();
        DocumentListener nameListener = new DocumentListener() {

            public void changedUpdate(DocumentEvent ev) {
                nameTextField.getDocument().removeDocumentListener(this);
                controller.setName(nameTextField.getText().trim());
                nameTextField.getDocument().addDocumentListener(this);
            }

            public void insertUpdate(DocumentEvent ev) {
                changedUpdate(ev);
            }

            public void removeUpdate(DocumentEvent ev) {
                changedUpdate(ev);
            }
        };
        this.nameTextField.getDocument().addDocumentListener(nameListener);
        controller.addPropertyChangeListener(ImportedTextureWizardController.Property.NAME, new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent ev) {
                if (!nameTextField.getText().trim().equals(controller.getName())) {
                    nameTextField.setText(controller.getName());
                }
                updateNameTextFieldForeground(defaultNameTextFieldColor);
            }
        });
        this.categoryLabel = new JLabel(SwingTools.getLocalizedLabelText(preferences, ImportedTextureWizardStepsPanel.class, "categoryLabel.text"));
        this.categoryComboBox = new JComboBox(preferences.getTexturesCatalog().getCategories().toArray());
        this.categoryComboBox.setEditable(true);
        final ComboBoxEditor defaultEditor = this.categoryComboBox.getEditor();
        this.categoryComboBox.setEditor(new ComboBoxEditor() {

            public Object getItem() {
                String name = (String) defaultEditor.getItem();
                name = name.trim();
                if (name.length() == 0) {
                    setItem(categoryComboBox.getSelectedItem());
                }
                TexturesCategory category = new TexturesCategory(name);
                List<TexturesCategory> categories = preferences.getTexturesCatalog().getCategories();
                int categoryIndex = Collections.binarySearch(categories, category);
                if (categoryIndex >= 0) {
                    return categories.get(categoryIndex);
                }
                return category;
            }

            public void setItem(Object value) {
                if (value != null) {
                    TexturesCategory category = (TexturesCategory) value;
                    defaultEditor.setItem(category.getName());
                }
            }

            public void addActionListener(ActionListener l) {
                defaultEditor.addActionListener(l);
            }

            public Component getEditorComponent() {
                return defaultEditor.getEditorComponent();
            }

            public void removeActionListener(ActionListener l) {
                defaultEditor.removeActionListener(l);
            }

            public void selectAll() {
                defaultEditor.selectAll();
            }
        });
        this.categoryComboBox.setRenderer(new DefaultListCellRenderer() {

            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                TexturesCategory category = (TexturesCategory) value;
                return super.getListCellRendererComponent(list, category.getName(), index, isSelected, cellHasFocus);
            }
        });
        this.categoryComboBox.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent ev) {
                controller.setCategory((TexturesCategory) ev.getItem());
            }
        });
        controller.addPropertyChangeListener(ImportedTextureWizardController.Property.CATEGORY, new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent ev) {
                TexturesCategory category = controller.getCategory();
                if (category != null) {
                    categoryComboBox.setSelectedItem(category);
                }
                updateNameTextFieldForeground(defaultNameTextFieldColor);
            }
        });
        this.widthLabel = new JLabel(SwingTools.getLocalizedLabelText(preferences, ImportedTextureWizardStepsPanel.class, "widthLabel.text", unitName));
        final NullableSpinner.NullableSpinnerLengthModel widthSpinnerModel = new NullableSpinner.NullableSpinnerLengthModel(preferences, 0.1f, 100000f);
        this.widthSpinner = new NullableSpinner(widthSpinnerModel);
        widthSpinnerModel.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent ev) {
                widthSpinnerModel.removeChangeListener(this);
                controller.setWidth(widthSpinnerModel.getLength());
                widthSpinnerModel.addChangeListener(this);
            }
        });
        controller.addPropertyChangeListener(ImportedTextureWizardController.Property.WIDTH, new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent ev) {
                widthSpinnerModel.setLength(controller.getWidth());
            }
        });
        this.heightLabel = new JLabel(SwingTools.getLocalizedLabelText(preferences, ImportedTextureWizardStepsPanel.class, "heightLabel.text", unitName));
        final NullableSpinner.NullableSpinnerLengthModel heightSpinnerModel = new NullableSpinner.NullableSpinnerLengthModel(preferences, 0.1f, 100000f);
        this.heightSpinner = new NullableSpinner(heightSpinnerModel);
        heightSpinnerModel.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent ev) {
                heightSpinnerModel.removeChangeListener(this);
                controller.setHeight(heightSpinnerModel.getLength());
                heightSpinnerModel.addChangeListener(this);
            }
        });
        controller.addPropertyChangeListener(ImportedTextureWizardController.Property.HEIGHT, new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent ev) {
                heightSpinnerModel.setLength(controller.getHeight());
            }
        });
        this.attributesPreviewComponent = new ScaledImageComponent();
        PropertyChangeListener imageAttributesListener = new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent ev) {
                updateAttributesPreviewImage();
            }
        };
        controller.addPropertyChangeListener(ImportedTextureWizardController.Property.IMAGE, imageAttributesListener);
        controller.addPropertyChangeListener(ImportedTextureWizardController.Property.WIDTH, imageAttributesListener);
        controller.addPropertyChangeListener(ImportedTextureWizardController.Property.HEIGHT, imageAttributesListener);
    }

    /**
   * Sets components mnemonics and label / component associations.
   */
    private void setMnemonics(UserPreferences preferences) {
        if (!OperatingSystem.isMacOSX()) {
            this.nameLabel.setDisplayedMnemonic(KeyStroke.getKeyStroke(preferences.getLocalizedString(ImportedTextureWizardStepsPanel.class, "nameLabel.mnemonic")).getKeyCode());
            this.nameLabel.setLabelFor(this.nameTextField);
            this.categoryLabel.setDisplayedMnemonic(KeyStroke.getKeyStroke(preferences.getLocalizedString(ImportedTextureWizardStepsPanel.class, "categoryLabel.mnemonic")).getKeyCode());
            this.categoryLabel.setLabelFor(this.categoryComboBox);
            this.widthLabel.setDisplayedMnemonic(KeyStroke.getKeyStroke(preferences.getLocalizedString(ImportedTextureWizardStepsPanel.class, "widthLabel.mnemonic")).getKeyCode());
            this.widthLabel.setLabelFor(this.widthSpinner);
            this.heightLabel.setDisplayedMnemonic(KeyStroke.getKeyStroke(preferences.getLocalizedString(ImportedTextureWizardStepsPanel.class, "heightLabel.mnemonic")).getKeyCode());
            this.heightLabel.setLabelFor(this.heightSpinner);
        }
    }

    /**
   * Layouts components in 3 panels added to this panel as cards. 
   */
    private void layoutComponents() {
        this.cardLayout = new CardLayout();
        setLayout(this.cardLayout);
        JPanel imageChoiceTopPanel = new JPanel(new GridBagLayout());
        imageChoiceTopPanel.add(this.imageChoiceOrChangeLabel, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 0), 0, 0));
        this.imageChoicePreviewComponent.setPreferredSize(new Dimension(150, 150));
        imageChoiceTopPanel.add(this.imageChoiceOrChangeButton, new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        imageChoiceTopPanel.add(this.imageChoiceErrorLabel, new GridBagConstraints(0, 2, 1, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(5, 0, 0, 0), 0, 0));
        JPanel imageChoicePanel = new JPanel(new ProportionalLayout());
        imageChoicePanel.add(imageChoiceTopPanel, ProportionalLayout.Constraints.TOP);
        imageChoicePanel.add(this.imageChoicePreviewComponent, ProportionalLayout.Constraints.BOTTOM);
        JPanel attributesPanel = new JPanel(new GridBagLayout());
        attributesPanel.add(this.attributesLabel, new GridBagConstraints(0, 0, 3, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(5, 0, 10, 0), 0, 0));
        this.attributesPreviewComponent.setPreferredSize(new Dimension(150, 150));
        attributesPanel.add(this.attributesPreviewComponent, new GridBagConstraints(0, 1, 1, 5, 1, 0, GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(0, 0, 5, 5), 0, 0));
        attributesPanel.add(this.nameLabel, new GridBagConstraints(1, 1, 1, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 10, 5), 0, 0));
        attributesPanel.add(this.nameTextField, new GridBagConstraints(2, 1, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 10, 0), 0, 0));
        attributesPanel.add(this.categoryLabel, new GridBagConstraints(1, 2, 1, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 10, 5), 0, 0));
        attributesPanel.add(this.categoryComboBox, new GridBagConstraints(2, 2, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 10, 0), 0, 0));
        attributesPanel.add(this.widthLabel, new GridBagConstraints(1, 3, 1, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 10, 5), 0, 0));
        attributesPanel.add(this.widthSpinner, new GridBagConstraints(2, 3, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 10, 0), 0, 0));
        attributesPanel.add(this.heightLabel, new GridBagConstraints(1, 4, 1, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 5, 5), 0, 0));
        attributesPanel.add(this.heightSpinner, new GridBagConstraints(2, 4, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 0), 0, 0));
        attributesPanel.add(new JLabel(), new GridBagConstraints(1, 5, 2, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        add(imageChoicePanel, ImportedTextureWizardController.Step.IMAGE.name());
        add(attributesPanel, ImportedTextureWizardController.Step.ATTRIBUTES.name());
    }

    /**
   * Switches to the component card matching current step.   
   */
    public void updateStep(ImportedTextureWizardController controller) {
        ImportedTextureWizardController.Step step = controller.getStep();
        this.cardLayout.show(this, step.name());
        switch(step) {
            case IMAGE:
                this.imageChoiceOrChangeButton.requestFocusInWindow();
                break;
            case ATTRIBUTES:
                this.nameTextField.requestFocusInWindow();
                break;
        }
    }

    /**
   * Updates name text field foreground color depending on the validity
   * of the piece name.
   */
    private void updateNameTextFieldForeground(Color defaultNameTextFieldColor) {
        nameTextField.setForeground(controller.isTextureNameValid() ? defaultNameTextFieldColor : Color.RED);
    }

    /**
   * Updates controller initial values from <code>textureImage</code>. 
   */
    private void updateController(final CatalogTexture catalogTexture, final UserPreferences preferences) {
        if (catalogTexture == null) {
            setImageChoiceTexts(preferences);
            updatePreviewComponentsImage(null);
        } else {
            setImageChangeTexts(preferences);
            this.imageLoader.execute(new Runnable() {

                public void run() {
                    BufferedImage image = null;
                    try {
                        image = readImage(preferences, catalogTexture.getImage());
                    } catch (IOException ex) {
                    }
                    final BufferedImage readImage = image;
                    EventQueue.invokeLater(new Runnable() {

                        public void run() {
                            if (readImage != null) {
                                controller.setImage(catalogTexture.getImage());
                                controller.setName(catalogTexture.getName());
                                controller.setCategory(catalogTexture.getCategory());
                                controller.setWidth(catalogTexture.getWidth());
                                controller.setHeight(catalogTexture.getHeight());
                            } else {
                                controller.setImage(null);
                                setImageChoiceTexts(preferences);
                                imageChoiceErrorLabel.setVisible(true);
                            }
                        }
                    });
                }
            });
        }
    }

    /**
   * Reads image from <code>imageName</code> and updates controller values.
   */
    private void updateController(final String imageName, final ContentManager contentManager, final UserPreferences preferences, final boolean ignoreException) {
        this.imageLoader.execute(new Runnable() {

            public void run() {
                Content imageContent = null;
                try {
                    imageContent = TemporaryURLContent.copyToTemporaryURLContent(contentManager.getContent(imageName));
                } catch (RecorderException ex) {
                } catch (IOException ex) {
                }
                if (imageContent == null) {
                    if (!ignoreException) {
                        EventQueue.invokeLater(new Runnable() {

                            public void run() {
                                JOptionPane.showMessageDialog(SwingUtilities.getRootPane(ImportedTextureWizardStepsPanel.this), preferences.getLocalizedString(ImportedTextureWizardStepsPanel.class, "imageChoiceError", imageName));
                            }
                        });
                    }
                    return;
                }
                BufferedImage image = null;
                try {
                    image = readImage(preferences, imageContent);
                } catch (IOException ex) {
                }
                final BufferedImage readImage = image;
                final Content readContent = imageContent;
                EventQueue.invokeLater(new Runnable() {

                    public void run() {
                        if (readImage != null) {
                            controller.setImage(readContent);
                            setImageChangeTexts(preferences);
                            imageChoiceErrorLabel.setVisible(false);
                            controller.setName(contentManager.getPresentationName(imageName, ContentManager.ContentType.IMAGE));
                            TexturesCategory userCategory = new TexturesCategory(preferences.getLocalizedString(ImportedTextureWizardStepsPanel.class, "userCategory"));
                            for (TexturesCategory category : preferences.getTexturesCatalog().getCategories()) {
                                if (category.equals(userCategory)) {
                                    userCategory = category;
                                    break;
                                }
                            }
                            controller.setCategory(userCategory);
                            float defaultWidth = 20;
                            if (preferences.getLengthUnit() == LengthUnit.INCH) {
                                defaultWidth = LengthUnit.inchToCentimeter(8);
                            }
                            controller.setWidth(defaultWidth);
                            controller.setHeight(defaultWidth / readImage.getWidth() * readImage.getHeight());
                        } else if (isShowing()) {
                            controller.setImage(null);
                            setImageChoiceTexts(preferences);
                            JOptionPane.showMessageDialog(ImportedTextureWizardStepsPanel.this, preferences.getLocalizedString(ImportedTextureWizardStepsPanel.class, "imageChoiceFormatError"));
                        }
                    }
                });
            }
        });
    }

    /**
   * Reads image from <code>imageContent</code>. 
   * Caution : this method must be thread safe because it's called from image loader executor. 
   */
    private BufferedImage readImage(UserPreferences preferences, Content imageContent) throws IOException {
        try {
            if (waitImage == null) {
                waitImage = ImageIO.read(ImportedTextureWizardStepsPanel.class.getResource(preferences.getLocalizedString(ImportedTextureWizardStepsPanel.class, "waitIcon")));
            }
            updatePreviewComponentsImage(waitImage);
            InputStream contentStream = imageContent.openStream();
            BufferedImage image = ImageIO.read(contentStream);
            contentStream.close();
            if (image != null) {
                updatePreviewComponentsImage(image);
                return image;
            } else {
                throw new IOException();
            }
        } catch (IOException ex) {
            updatePreviewComponentsImage(null);
            throw ex;
        }
    }

    /**
   * Updates the <code>image</code> displayed by preview components.  
   */
    private void updatePreviewComponentsImage(BufferedImage image) {
        this.imageChoicePreviewComponent.setImage(image);
        this.attributesPreviewComponent.setImage(image);
    }

    /**
   * Sets the texts of label and button of image choice panel with
   * change texts. 
   */
    private void setImageChangeTexts(UserPreferences preferences) {
        this.imageChoiceOrChangeLabel.setText(preferences.getLocalizedString(ImportedTextureWizardStepsPanel.class, "imageChangeLabel.text"));
        this.imageChoiceOrChangeButton.setText(SwingTools.getLocalizedLabelText(preferences, ImportedTextureWizardStepsPanel.class, "imageChangeButton.text"));
        if (!OperatingSystem.isMacOSX()) {
            this.imageChoiceOrChangeButton.setMnemonic(KeyStroke.getKeyStroke(preferences.getLocalizedString(ImportedTextureWizardStepsPanel.class, "imageChangeButton.mnemonic")).getKeyCode());
        }
    }

    /**
   * Sets the texts of label and button of image choice panel with
   * choice texts. 
   */
    private void setImageChoiceTexts(UserPreferences preferences) {
        this.imageChoiceOrChangeLabel.setText(preferences.getLocalizedString(ImportedTextureWizardStepsPanel.class, "imageChoiceLabel.text"));
        this.imageChoiceOrChangeButton.setText(SwingTools.getLocalizedLabelText(preferences, ImportedTextureWizardStepsPanel.class, "imageChoiceButton.text"));
        if (!OperatingSystem.isMacOSX()) {
            this.imageChoiceOrChangeButton.setMnemonic(KeyStroke.getKeyStroke(preferences.getLocalizedString(ImportedTextureWizardStepsPanel.class, "imageChoiceButton.mnemonic")).getKeyCode());
        }
    }

    /**
   * Returns an image name chosen for a content chooser dialog.
   */
    private String showImageChoiceDialog(UserPreferences preferences, ContentManager contentManager) {
        return contentManager.showOpenDialog(this, preferences.getLocalizedString(ImportedTextureWizardStepsPanel.class, "imageChoiceDialog.title"), ContentManager.ContentType.IMAGE);
    }

    /**
   * Updates the image shown in attributes panel.
   */
    private void updateAttributesPreviewImage() {
        BufferedImage attributesPreviewImage = this.attributesPreviewComponent.getImage();
        final int imageSize = 150;
        if (attributesPreviewImage == null || attributesPreviewImage == this.imageChoicePreviewComponent.getImage()) {
            attributesPreviewImage = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_RGB);
            this.attributesPreviewComponent.setImage(attributesPreviewImage);
        }
        Graphics2D g2D = (Graphics2D) attributesPreviewImage.getGraphics();
        g2D.setPaint(Color.WHITE);
        g2D.fillRect(0, 0, imageSize, imageSize);
        BufferedImage textureImage = this.imageChoicePreviewComponent.getImage();
        if (textureImage != null) {
            g2D.setPaint(new TexturePaint(textureImage, new Rectangle2D.Float(0, 0, this.controller.getWidth() / 250 * imageSize, this.controller.getHeight() / 250 * imageSize)));
            g2D.fillRect(0, 0, imageSize, imageSize);
        }
        g2D.dispose();
        this.attributesPreviewComponent.repaint();
    }
}
