package br.com.nix.framework;

import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map.Entry;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import com.bric.swing.ColorPicker;
import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.uif_lite.panel.SimpleInternalFrame;

public class SimpleWindow implements ActionListener {

    public static int OK = 1;

    public static final int CANCELAR = 0;

    private JButton btnOk = new JButton("OK");

    private JButton btnCancelar = new JButton("Cancelar");

    private JButton btnFechar = new JButton("Fechar");

    private int response = CANCELAR;

    private PanelBuilder widgetBuilder = null;

    private JDialog frame;

    private String windowTitle = "";

    private String sifTitle = "";

    private ImageIcon icon = ImgUtils.getIcon(ImgUtils.IMG_LISTA);

    private boolean clean = false;

    private boolean fechar = false;

    private HashMap<String, Component> widgets = new HashMap<String, Component>();

    private HashMap<String, Object> values = new HashMap<String, Object>();

    private boolean defaultDialogBorder = true;

    public SimpleWindow(String colunas, String linhas) {
        this.frame = new JDialog();
        this.initialize(colunas, linhas);
    }

    public SimpleWindow(JDialog parent, String colunas, String linhas) {
        this.frame = new JDialog(parent);
        this.initialize(colunas, linhas);
    }

    public SimpleWindow(JFrame parent, String colunas, String linhas) {
        this.frame = new JDialog(parent);
        this.initialize(colunas, linhas);
    }

    private void initialize(String colunas, String linhas) {
        this.setLayout(colunas, linhas);
        this.addWidgetsListeners();
        this.setWidgetsProperties();
    }

    private void setWidgetsProperties() {
        this.btnOk.setMnemonic('O');
        this.btnCancelar.setMnemonic('C');
        this.btnFechar.setMnemonic('F');
        this.btnOk.setIcon(ImgUtils.getIcon(ImgUtils.IMG_OK));
        this.btnCancelar.setIcon(ImgUtils.getIcon(ImgUtils.IMG_CANCELAR));
        this.btnFechar.setIcon(ImgUtils.getIcon(ImgUtils.IMG_FECHAR));
    }

    public void addFramelessWidget(String key, Component widget) {
        widgets.put(key, widget);
    }

    public void addValue(String key, Object value) {
        values.put(key, value);
    }

    public void addScrolledWidget(String key, Component widget, int x, int y, int w, int h, int sizeW, int sizeH) {
        setCommonProperties(widget);
        widgets.put(key, widget);
        CellConstraints cc = new CellConstraints();
        widgetBuilder.add(GUIUtils.betterScroll(widgets.get(key), sizeW, sizeH), cc.xywh(x, y, w, h));
    }

    private void setCommonProperties(Component widget) {
        if (widget instanceof JTextArea) {
            JTextArea textArea = (JTextArea) widget;
            textArea.addKeyListener(new KeyAdapter() {

                public void keyPressed(KeyEvent ke) {
                    if (ke.getKeyCode() == KeyEvent.VK_TAB) {
                        ke.consume();
                        KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent();
                    }
                    if (ke.getKeyCode() == KeyEvent.VK_TAB && ke.isShiftDown()) {
                        ke.consume();
                        KeyboardFocusManager.getCurrentKeyboardFocusManager().focusPreviousComponent();
                    }
                }
            });
        }
        widget.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent fe) {
                if (fe.getOppositeComponent() == null || fe.getOppositeComponent() == fe.getComponent()) return;
                if (fe.getSource() instanceof JTextField) ((JTextField) fe.getSource()).selectAll(); else if (fe.getSource() instanceof JTextArea) ((JTextArea) fe.getSource()).selectAll();
            }

            @Override
            public void focusLost(FocusEvent arg0) {
            }
        });
    }

    public void addWidget(String key, Component widget, int x, int y, int w, int h) {
        setCommonProperties(widget);
        widgets.put(key, widget);
        CellConstraints cc = new CellConstraints();
        widgetBuilder.add(widgets.get(key), cc.xywh(x, y, w, h));
    }

    public void addWidget(String key, Component widget, int x, int y, int w) {
        this.addWidget(key, widget, x, y, w, 1);
    }

    public void addWidget(String key, Component widget, int x, int y) {
        this.addWidget(key, widget, x, y, 1, 1);
    }

    public void addLabel(String texto, int x, int y, int w, int h) {
        CellConstraints cc = new CellConstraints();
        widgetBuilder.addLabel(texto, cc.xywh(x, y, w, h));
    }

    public void addLabel(String texto, int x, int y, int w) {
        this.addLabel(texto, x, y, w, 1);
    }

    public void addLabel(String texto, int x, int y) {
        this.addLabel(texto, x, y, 1, 1);
    }

    public void addSeparator(String texto, int x, int y) {
        this.addSeparator(texto, x, y, 1, 1);
    }

    public void addSeparator(String texto, int x, int y, int w) {
        this.addSeparator(texto, x, y, w, 1);
    }

    public void addSeparator(String texto, int x, int y, int w, int h) {
        CellConstraints cc = new CellConstraints();
        widgetBuilder.addSeparator(texto, cc.xywh(x, y, w, h));
    }

    private void setLayout(String colunas, String linhas) {
        FormLayout layout = new FormLayout(colunas, linhas);
        this.widgetBuilder = new PanelBuilder(layout);
    }

    private Object getValueOf(Component widget) {
        Object retorno = null;
        if (widget instanceof JTextField) {
            retorno = ((JTextField) widget).getText();
        } else if (widget instanceof JTextArea) {
            retorno = ((JTextArea) widget).getText();
        } else if (widget instanceof JComboBox) {
            retorno = ((JComboBox) widget).getSelectedItem();
        } else if (widget instanceof JList) {
            retorno = ((JList) widget).getSelectedValues();
        } else if (widget instanceof JPasswordField) {
            retorno = ((JPasswordField) widget).getPassword();
        } else if (widget instanceof JRadioButton) {
            retorno = ((JRadioButton) widget).isSelected();
        } else if (widget instanceof JCheckBox) {
            retorno = ((JCheckBox) widget).isSelected();
        } else if (widget instanceof JLabel) {
            retorno = ((JLabel) widget).getText();
        } else if (widget instanceof ColorPicker) {
            retorno = ((ColorPicker) widget).getColor();
        } else retorno = null;
        return retorno;
    }

    private HashMap<String, Object> getValues() {
        HashMap<String, Object> retorno = new HashMap<String, Object>();
        for (Entry<String, Component> entry : this.widgets.entrySet()) {
            retorno.put(entry.getKey(), this.getValueOf(entry.getValue()));
        }
        for (Entry<String, Object> entry : this.values.entrySet()) {
            retorno.put(entry.getKey(), entry.getValue());
        }
        return retorno;
    }

    public HashMap<String, Object> show() {
        buildOnly();
        return showOnly();
    }

    public void buildOnly() {
        this.frame.setTitle(windowTitle);
        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        FormLayout layout;
        if (clean) layout = new FormLayout("f:p:g", "p, 9dlu, f:p:g, 9dlu, p"); else layout = new FormLayout("f:p:g", "f:p:g, 9dlu, p");
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();
        ButtonBarBuilder btnBuilder = ButtonBarBuilder.createLeftToRightBuilder();
        btnBuilder.addGlue();
        if (fechar) btnBuilder.addGriddedButtons(new JButton[] { this.btnFechar }); else btnBuilder.addGriddedButtons(new JButton[] { this.btnOk, this.btnCancelar });
        if (clean) {
            builder.addSeparator(sifTitle, cc.xy(1, 1));
            builder.add(this.widgetBuilder.getPanel(), cc.xy(1, 3));
        } else {
            SimpleInternalFrame sif = new SimpleInternalFrame(sifTitle);
            sif.add(this.widgetBuilder.getPanel());
            sif.setFrameIcon(this.icon);
            builder.add(sif, cc.xy(1, 1));
        }
        if (clean) builder.add(btnBuilder.getPanel(), cc.xy(1, 5)); else builder.add(btnBuilder.getPanel(), cc.xy(1, 3));
        this.frame.getContentPane().add(builder.getPanel());
        this.frame.getRootPane().setDefaultButton(this.btnOk);
        if (fechar) GUIUtils.registerEscapeKey(this.frame.getRootPane(), this.btnFechar); else GUIUtils.registerEscapeKey(this.frame.getRootPane(), this.btnCancelar);
    }

    public HashMap<String, Object> showOnly() {
        if (this.defaultDialogBorder) this.widgetBuilder.setDefaultDialogBorder();
        this.frame.pack();
        this.frame.setLocationRelativeTo(null);
        this.frame.setModal(true);
        this.frame.setVisible(true);
        this.frame.dispose();
        if (this.response == OK) return this.getValues(); else return null;
    }

    private void addWidgetsListeners() {
        this.btnOk.addActionListener(this);
        this.btnCancelar.addActionListener(this);
        this.btnFechar.addActionListener(this);
    }

    public void actionPerformed(ActionEvent event) {
        Object theButton = event.getSource();
        if (theButton == this.btnOk) {
            this.response = OK;
            this.frame.setVisible(false);
        } else if (theButton == this.btnCancelar || theButton == this.btnFechar) {
            this.response = CANCELAR;
            this.frame.setVisible(false);
        }
    }

    public void setSifTitle(String sifTitle) {
        this.sifTitle = sifTitle;
    }

    public void setWindowTitle(String windowTitle) {
        this.windowTitle = windowTitle;
    }

    public void setTitle(String title) {
        this.setSifTitle(title);
        this.setWindowTitle(title);
    }

    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }

    public void setClean(boolean clean) {
        this.clean = clean;
    }

    public void setFechar(boolean fechar) {
        this.fechar = fechar;
    }

    public void setDefaultDialogBorder(boolean defaultDialogBorder) {
        this.defaultDialogBorder = defaultDialogBorder;
    }
}
