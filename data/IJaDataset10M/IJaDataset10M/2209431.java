package a03.swing.plaf;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.MemoryImageSource;
import java.text.ParseException;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.MaskFormatter;
import a03.swing.widget.A03ColorPicker;

public class A03RGBColorChooserPanel extends AbstractColorChooserPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = -1719071802717162681L;

    class ColorPalettePanel extends JPanel {

        /**
		 * 
		 */
        private static final long serialVersionUID = 8268147385458917437L;

        private Image img;

        private Point selectedLocation = new Point();

        public ColorPalettePanel() {
            buildImage();
            addMouseMotionListener(new MouseMotionListener() {

                public void mouseDragged(MouseEvent e) {
                    setSelectedLocation(e.getPoint());
                }

                public void mouseMoved(MouseEvent e) {
                }
            });
            addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    setSelectedLocation(e.getPoint());
                }
            });
        }

        protected void buildImage() {
            int pix[] = new int[256 * 256];
            int index = 0;
            int red = 0;
            int green = 0;
            int blue = 0;
            int mask = (rbRadioButton.isSelected() ? 1 : 0) << 2 | (gbRadioButton.isSelected() ? 1 : 0) << 1 | (grRadioButton.isSelected() ? 1 : 0);
            int sliderValue = (Integer) slider.getValue();
            if ((mask & 1) != 0) blue = sliderValue; else if ((mask & 2) != 0) red = sliderValue; else green = sliderValue;
            for (int y = 0; y < 256; y++) {
                if ((mask & 3) != 0) green = y; else if ((mask & 4) != 0) red = y;
                for (int x = 0; x < 256; x++) {
                    if ((mask & 1) != 0) red = x; else if ((mask & 6) != 0) blue = x;
                    pix[index++] = (255 << 24) | (red << 16) | (green << 8) | blue;
                }
            }
            img = createImage(new MemoryImageSource(256, 256, pix, 0, 256));
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(262, 262);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D graphics = (Graphics2D) g.create();
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            graphics.setColor(getBackground());
            graphics.fillRect(0, 0, getWidth(), getHeight());
            graphics.drawImage(img, 3, 3, this);
            graphics.setColor(Color.WHITE);
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.drawOval(selectedLocation.x, selectedLocation.y, 6, 6);
            graphics.dispose();
        }

        public Point getSelectedLocation() {
            return selectedLocation;
        }

        public void setSelectedLocation(Point loc) {
            selectedLocation = loc;
            int mask = (rbRadioButton.isSelected() ? 1 : 0) << 2 | (gbRadioButton.isSelected() ? 1 : 0) << 1 | (grRadioButton.isSelected() ? 1 : 0);
            int sliderValue = (Integer) slider.getValue();
            int yComponent = loc.y;
            int xComponent = loc.x;
            if (xComponent < 0) {
                xComponent = 0;
            } else if (xComponent > 255) {
                xComponent = 255;
            }
            if (yComponent < 0) {
                yComponent = 0;
            } else if (yComponent > 255) {
                yComponent = 255;
            }
            int red;
            int green;
            int blue;
            if ((mask & 1) != 0) {
                red = xComponent;
                blue = sliderValue;
                green = yComponent;
            } else if ((mask & 2) != 0) {
                red = sliderValue;
                blue = xComponent;
                green = yComponent;
            } else {
                red = yComponent;
                blue = xComponent;
                green = sliderValue;
            }
            Color newColor = new Color(red, green, blue);
            getColorSelectionModel().setSelectedColor(newColor);
        }

        public void setSelectedColor(Color color) {
            int mask = (rbRadioButton.isSelected() ? 1 : 0) << 2 | (gbRadioButton.isSelected() ? 1 : 0) << 1 | (grRadioButton.isSelected() ? 1 : 0);
            if ((mask & 1) != 0) {
                selectedLocation.x = color.getRed();
                selectedLocation.y = color.getGreen();
            } else if ((mask & 2) != 0) {
                selectedLocation.x = color.getBlue();
                selectedLocation.y = color.getGreen();
            } else {
                selectedLocation.x = color.getBlue();
                selectedLocation.y = color.getRed();
            }
        }
    }

    class ColorPaletteLabelPanel extends JPanel {

        /**
		 * 
		 */
        private static final long serialVersionUID = -1557051113988990668L;

        private Image img;

        public ColorPaletteLabelPanel() {
            buildImage();
        }

        private void buildImage() {
            int w = 20;
            int h = 256;
            int pix[] = new int[20 * 256];
            int index = 0;
            int mask = (rbRadioButton.isSelected() ? 1 : 0) << 2 | (gbRadioButton.isSelected() ? 1 : 0) << 1 | (grRadioButton.isSelected() ? 1 : 0);
            int red = 0;
            int green = 0;
            int blue = 0;
            for (int y = 0; y < 256; y++) {
                if ((mask & 1) != 0) blue = y; else if ((mask & 2) != 0) red = y; else green = y;
                for (int x = 0; x < 20; x++) {
                    pix[index++] = (255 << 24) | (red << 16) | (green << 8) | blue;
                }
            }
            img = createImage(new MemoryImageSource(w, h, pix, 0, w));
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(24, 260);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D graphics = (Graphics2D) g.create();
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            graphics.setColor(getBackground());
            graphics.fillRect(0, 0, getWidth(), getHeight());
            graphics.drawImage(img, 2, 2, this);
            graphics.dispose();
        }
    }

    protected JColorChooser chooser;

    protected ColorPalettePanel colorPalettePanel;

    protected ColorPaletteLabelPanel colorPaletteLabelPanel;

    protected JSlider slider;

    protected JRadioButton rbRadioButton;

    protected JRadioButton gbRadioButton;

    protected JRadioButton grRadioButton;

    private JSpinner redSpinner;

    private JSpinner greenSpinner;

    private JSpinner blueSpinner;

    private JFormattedTextField hexField;

    private JPanel previewContainer;

    public A03RGBColorChooserPanel(JColorChooser chooser) {
        this.chooser = chooser;
        ButtonGroup bg = new ButtonGroup();
        ChangeListener radioListener = new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                colorPalettePanel.buildImage();
                colorPaletteLabelPanel.buildImage();
                Color color = getColorSelectionModel().getSelectedColor();
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();
                int mask = (rbRadioButton.isSelected() ? 1 : 0) << 2 | (gbRadioButton.isSelected() ? 1 : 0) << 1 | (grRadioButton.isSelected() ? 1 : 0);
                if ((mask & 1) != 0) slider.setValue(blue); else if ((mask & 2) != 0) slider.setValue(red); else slider.setValue(green);
                colorPalettePanel.setSelectedColor(color);
                colorPalettePanel.repaint();
                colorPaletteLabelPanel.repaint();
            }
        };
        rbRadioButton = new JRadioButton("RB");
        rbRadioButton.setSelected(true);
        rbRadioButton.addChangeListener(radioListener);
        bg.add(rbRadioButton);
        gbRadioButton = new JRadioButton("GB");
        gbRadioButton.addChangeListener(radioListener);
        bg.add(gbRadioButton);
        grRadioButton = new JRadioButton("GR");
        grRadioButton.addChangeListener(radioListener);
        bg.add(grRadioButton);
        slider = new JSlider(JSlider.VERTICAL, 0, 255, 0);
    }

    protected JComponent buildSliderPalettePanel() {
        slider.setInverted(true);
        slider.setPaintTrack(false);
        slider.setFocusable(false);
        slider.setPreferredSize(new Dimension(slider.getPreferredSize().width, 255 + 15));
        slider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                colorPalettePanel.buildImage();
                colorPaletteLabelPanel.buildImage();
                int mask = (rbRadioButton.isSelected() ? 1 : 0) << 2 | (gbRadioButton.isSelected() ? 1 : 0) << 1 | (grRadioButton.isSelected() ? 1 : 0);
                int sliderValue = (Integer) slider.getValue();
                Color color = getColorSelectionModel().getSelectedColor();
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();
                if ((mask & 1) != 0) blue = sliderValue; else if ((mask & 2) != 0) red = sliderValue; else green = sliderValue;
                Color newColor = new Color(red, green, blue);
                getColorSelectionModel().setSelectedColor(newColor);
            }
        });
        slider.setInheritsPopupMenu(true);
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setBorder(new EmptyBorder(30, 0, 0, 0));
        panel.add(slider);
        colorPaletteLabelPanel = new ColorPaletteLabelPanel();
        panel.add(colorPaletteLabelPanel);
        return panel;
    }

    @Override
    protected void buildChooser() {
        removeAll();
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(new EmptyBorder(0, 3, 0, 4));
        colorPalettePanel = new ColorPalettePanel();
        JPanel panel = new JPanel();
        FlowLayout layout = new FlowLayout();
        panel.setLayout(layout);
        panel.add(rbRadioButton);
        panel.add(gbRadioButton);
        panel.add(grRadioButton);
        JPanel panel2 = new JPanel();
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
        panel2.add(panel);
        panel2.add(colorPalettePanel);
        add(panel2);
        add(buildSliderPalettePanel());
        JPanel panel3 = new JPanel();
        GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
        gridBagConstraints8.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints8.gridy = 5;
        gridBagConstraints8.weightx = 0.01D;
        gridBagConstraints8.gridx = 1;
        gridBagConstraints8.insets = new Insets(12, 0, 0, 0);
        gridBagConstraints8.anchor = GridBagConstraints.WEST;
        GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
        gridBagConstraints7.gridy = 5;
        gridBagConstraints7.gridx = 0;
        gridBagConstraints7.weightx = 0.1;
        gridBagConstraints7.insets = new Insets(12, 0, 0, 3);
        JLabel hexLabel = new JLabel();
        hexLabel.setText("#");
        GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
        gridBagConstraints6.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints6.gridy = 4;
        gridBagConstraints6.weightx = 1.0;
        gridBagConstraints6.gridx = 1;
        gridBagConstraints6.anchor = GridBagConstraints.WEST;
        gridBagConstraints6.insets = new Insets(3, 0, 0, 0);
        GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
        gridBagConstraints5.gridx = 0;
        gridBagConstraints5.weightx = 0.1;
        gridBagConstraints5.gridy = 4;
        gridBagConstraints5.insets = new Insets(3, 0, 0, 3);
        JLabel blueLabel = new JLabel();
        blueLabel.setText("B");
        GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
        gridBagConstraints4.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints4.gridy = 3;
        gridBagConstraints4.weightx = 1.0;
        gridBagConstraints4.gridx = 1;
        gridBagConstraints4.anchor = GridBagConstraints.WEST;
        gridBagConstraints4.insets = new Insets(3, 0, 0, 0);
        GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.weightx = 0.1;
        gridBagConstraints3.gridy = 3;
        gridBagConstraints3.insets = new Insets(3, 0, 0, 3);
        JLabel greenLabel = new JLabel();
        greenLabel.setText("G");
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints2.gridy = 2;
        gridBagConstraints2.weightx = 1.0;
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.anchor = GridBagConstraints.WEST;
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.weightx = 0.1;
        gridBagConstraints1.gridy = 2;
        gridBagConstraints1.insets = new Insets(0, 0, 0, 3);
        JLabel redLabel = new JLabel();
        redLabel.setText("R");
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.insets = new Insets(0, 0, 12, 0);
        GridBagConstraints gridBagConstraintsColorPicker = new GridBagConstraints();
        gridBagConstraintsColorPicker.gridx = 0;
        gridBagConstraintsColorPicker.weightx = 1.0;
        gridBagConstraintsColorPicker.gridy = 0;
        gridBagConstraintsColorPicker.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraintsColorPicker.gridwidth = 2;
        gridBagConstraints.ipadx = 3;
        gridBagConstraintsColorPicker.insets = new Insets(0, 1, 12, 0);
        redSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 255, 1));
        redSpinner.setName("spinnerRed");
        redSpinner.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                Color color = getColorSelectionModel().getSelectedColor();
                Color newColor = new Color((Integer) redSpinner.getValue(), color.getGreen(), color.getBlue());
                getColorSelectionModel().setSelectedColor(newColor);
            }
        });
        redSpinner.setInheritsPopupMenu(true);
        greenSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 255, 1));
        greenSpinner.setName("spinnerGreen");
        greenSpinner.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                Color color = getColorSelectionModel().getSelectedColor();
                Color newColor = new Color(color.getRed(), (Integer) greenSpinner.getValue(), color.getBlue());
                getColorSelectionModel().setSelectedColor(newColor);
            }
        });
        greenSpinner.setInheritsPopupMenu(true);
        blueSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 255, 1));
        blueSpinner.setName("spinnerBlue");
        blueSpinner.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                Color color = getColorSelectionModel().getSelectedColor();
                Color newColor = new Color(color.getRed(), color.getGreen(), (Integer) blueSpinner.getValue());
                getColorSelectionModel().setSelectedColor(newColor);
            }
        });
        blueSpinner.setInheritsPopupMenu(true);
        try {
            MaskFormatter mf = new MaskFormatter("HHHHHH");
            hexField = new JFormattedTextField(mf);
            hexField.addKeyListener(new KeyAdapter() {

                public void keyReleased(KeyEvent e) {
                    if (e.getKeyChar() == java.awt.event.KeyEvent.VK_ENTER) {
                        Color newColor = Color.decode("#" + hexField.getText());
                        getColorSelectionModel().setSelectedColor(newColor);
                    }
                }
            });
            hexField.addFocusListener(new FocusListener() {

                public void focusGained(FocusEvent e) {
                }

                public void focusLost(FocusEvent e) {
                    Color newColor = Color.decode("#" + hexField.getText());
                    getColorSelectionModel().setSelectedColor(newColor);
                }
            });
            hexField.setPreferredSize(new Dimension(60, 19));
        } catch (ParseException e1) {
        }
        panel3.setLayout(new GridBagLayout());
        if (!Boolean.TRUE.equals(UIManager.getBoolean("A03.ColorChooser.noColorPicker"))) {
            A03ColorPicker colorPicker = new A03ColorPicker(chooser);
            panel3.add(colorPicker, gridBagConstraintsColorPicker);
        }
        panel3.add(previewContainer, gridBagConstraints);
        panel3.add(redLabel, gridBagConstraints1);
        panel3.add(redSpinner, gridBagConstraints2);
        panel3.add(greenLabel, gridBagConstraints3);
        panel3.add(greenSpinner, gridBagConstraints4);
        panel3.add(blueLabel, gridBagConstraints5);
        panel3.add(blueSpinner, gridBagConstraints6);
        panel3.add(hexLabel, gridBagConstraints7);
        panel3.add(hexField, gridBagConstraints8);
        add(panel3);
        ChangeListener changeListener = new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                updateControls();
            }
        };
        getColorSelectionModel().addChangeListener(changeListener);
        updateControls();
    }

    private void updateControls() {
        Color color = getColorSelectionModel().getSelectedColor();
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        redSpinner.setValue(red);
        greenSpinner.setValue(green);
        blueSpinner.setValue(blue);
        String hex = A03GraphicsUtilities.toHexString(color).substring(1);
        hexField.setText(hex);
        int mask = (rbRadioButton.isSelected() ? 1 : 0) << 2 | (gbRadioButton.isSelected() ? 1 : 0) << 1 | (grRadioButton.isSelected() ? 1 : 0);
        if ((mask & 1) != 0) slider.setValue(blue); else if ((mask & 2) != 0) slider.setValue(red); else slider.setValue(green);
        colorPalettePanel.setSelectedColor(color);
        colorPalettePanel.repaint();
        colorPaletteLabelPanel.repaint();
    }

    @Override
    public String getDisplayName() {
        return "RGB";
    }

    @Override
    public Icon getLargeDisplayIcon() {
        return null;
    }

    @Override
    public Icon getSmallDisplayIcon() {
        return null;
    }

    @Override
    public void updateChooser() {
    }

    public void setPreviewContainer(JPanel previewContainer) {
        this.previewContainer = previewContainer;
    }
}
