package com.netime.autoffice.gui.opt;

import java.util.List;
import java.util.Locale;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.TreeSelectionListener;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.netime.commons.standard.gui.AutofficeInternalFrame;
import com.netime.commons.standard.gui.AutofficeMenuBar;

public class Autofficeapplication extends AutofficeInternalFrame {

    static final int xOffset = 30, yOffset = 30;

    private JLabel lbIdOffset;

    private JTextField tfIdOffset;

    private JLabel lbLocaleLang;

    private JTextField tfLocaleLang;

    private JLabel lbLocaleCountry;

    private JTextField tfLocaleCountry;

    public Autofficeapplication(AutofficeMenuBar menuBar, Locale locale) {
        super(menuBar, locale);
        setSize(300, 300);
        setLocation(xOffset, yOffset);
    }

    protected List createMenus() {
        return null;
    }

    protected void beginLayout() {
        CellConstraints cc = new CellConstraints();
        setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0), "JFormDesigner Evaluation", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12), java.awt.Color.red), getBorder()));
        addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent e) {
                if ("border".equals(e.getPropertyName())) throw new RuntimeException();
            }
        });
        setLayout(new FormLayout("141dlu, $lcgap, 119dlu", "2*(default, $lgap), default"));
        lbIdOffset.setText("id.offset");
        add(lbIdOffset, cc.xy(1, 1));
        tfIdOffset.setText("21");
        add(tfIdOffset, cc.xy(3, 1));
        lbLocaleLang.setText("locale.lang.");
        add(lbLocaleLang, cc.xy(1, 3));
        tfLocaleLang.setText("zh");
        add(tfLocaleLang, cc.xy(3, 3));
        lbLocaleCountry.setText("locale.country");
        add(lbLocaleCountry, cc.xy(1, 5));
        tfLocaleCountry.setText("CN");
        add(tfLocaleCountry, cc.xy(3, 5));
    }

    protected void initComponents() {
        lbIdOffset = new JLabel();
        tfIdOffset = new JTextField();
        lbLocaleLang = new JLabel();
        tfLocaleLang = new JTextField();
        lbLocaleCountry = new JLabel();
        tfLocaleCountry = new JTextField();
        CellConstraints cc = new CellConstraints();
    }

    @Override
    public String getScreenId() {
        return null;
    }
}
