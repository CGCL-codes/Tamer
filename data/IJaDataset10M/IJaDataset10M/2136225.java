package br.unb.gui.editPanels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import net.miginfocom.swing.MigLayout;
import br.unb.entities.signalControllers.SignalHead;
import br.unb.entities.signalControllers.Fuzzy.FuzzySignalPhase;
import br.unb.gui.editPanels.tableModels.TableModelSignals;
import br.unb.main.ModelController;

public class FuzzyPhaseEditPanel extends JPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = -1699792774151040813L;

    private FuzzySignalPhase fuzzySignalPhase;

    private ModelController modelController;

    private ResourceBundle bundle;

    private Locale locale;

    private JLabel lbYellowTime;

    private JLabel lbMinGreenTime;

    private JLabel lbMaxNumOfExtensions;

    private JLabel lbMinExtensionTime;

    private JLabel lbRedTime;

    private JLabel lbMultiplier;

    private JButton btAddSignalHead;

    private JButton btRemoveSignalHead;

    private JFormattedTextField tfYellowTime;

    private JFormattedTextField tfMinGreenTime;

    private JFormattedTextField tfMaxNumOfExtensions;

    private JFormattedTextField tfMinExtensionTime;

    private JFormattedTextField tfRedTime;

    private JFormattedTextField tfMultiplier;

    private TableModelSignals tableModelSignals;

    private JTable tableSignals;

    public FuzzyPhaseEditPanel(FuzzySignalPhase fuzzySignalPhase, ModelController modelController) {
        this.modelController = modelController;
        this.fuzzySignalPhase = fuzzySignalPhase;
        locale = new Locale("pt", "BR");
        bundle = ResourceBundle.getBundle("gui/fuzzyPhasePanelBundle", locale);
        this.setLayout(new MigLayout());
        initContentPanel();
        this.setVisible(true);
    }

    private void initContentPanel() {
        tfYellowTime = new JFormattedTextField(DecimalFormat.getInstance(locale));
        tfYellowTime.setValue(fuzzySignalPhase.getYellowTime());
        tfYellowTime.setInputVerifier(new InputVerifier() {

            Border defaultBorder = tfYellowTime.getBorder();

            public boolean verify(JComponent input) {
                try {
                    tfYellowTime.commitEdit();
                    double b = ((Number) tfYellowTime.getValue()).doubleValue();
                    if (b < 0) {
                        tfYellowTime.setBorder(BorderFactory.createLineBorder(Color.red));
                        return false;
                    } else {
                        tfYellowTime.setBorder(defaultBorder);
                        fuzzySignalPhase.setYellowTime(b);
                        return true;
                    }
                } catch (ParseException e) {
                }
                return false;
            }
        });
        tfYellowTime.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    tfYellowTime.transferFocus();
                }
            }
        });
        lbYellowTime = new JLabel(bundle.getString("lbYellowTime"));
        lbYellowTime.setToolTipText(bundle.getString("lbYellowTimeTip"));
        lbYellowTime.setLabelFor(tfYellowTime);
        tfMinGreenTime = new JFormattedTextField(DecimalFormat.getInstance(locale));
        tfMinGreenTime.setValue(fuzzySignalPhase.getMinGreenTime());
        tfMinGreenTime.setInputVerifier(new InputVerifier() {

            Border defaultBorder = tfMinGreenTime.getBorder();

            public boolean verify(JComponent input) {
                try {
                    tfMinGreenTime.commitEdit();
                    double b = ((Number) tfMinGreenTime.getValue()).doubleValue();
                    if (b < 0) {
                        tfMinGreenTime.setBorder(BorderFactory.createLineBorder(Color.red));
                        return false;
                    } else {
                        tfMinGreenTime.setBorder(defaultBorder);
                        fuzzySignalPhase.setMinGreenTime(b);
                        return true;
                    }
                } catch (ParseException e) {
                }
                return false;
            }
        });
        tfMinGreenTime.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    tfMinGreenTime.transferFocus();
                }
            }
        });
        lbMinGreenTime = new JLabel(bundle.getString("lbMinGreenTime"));
        lbMinGreenTime.setToolTipText(bundle.getString("lbMinGreenTimeTip"));
        lbMinGreenTime.setLabelFor(tfMinGreenTime);
        tfRedTime = new JFormattedTextField(DecimalFormat.getInstance(locale));
        tfRedTime.setValue(fuzzySignalPhase.getRedTime());
        tfRedTime.setInputVerifier(new InputVerifier() {

            Border defaultBorder = tfRedTime.getBorder();

            public boolean verify(JComponent input) {
                try {
                    tfRedTime.commitEdit();
                    double b = ((Number) tfRedTime.getValue()).doubleValue();
                    if (b < 0) {
                        tfRedTime.setBorder(BorderFactory.createLineBorder(Color.red));
                        return false;
                    } else {
                        tfRedTime.setBorder(defaultBorder);
                        fuzzySignalPhase.setRedTime(b);
                        return true;
                    }
                } catch (ParseException e) {
                }
                return false;
            }
        });
        tfRedTime.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    tfRedTime.transferFocus();
                }
            }
        });
        lbRedTime = new JLabel(bundle.getString("lbRedTime"));
        lbRedTime.setToolTipText(bundle.getString("lbRedTimeTip"));
        lbRedTime.setLabelFor(tfRedTime);
        tfMaxNumOfExtensions = new JFormattedTextField(DecimalFormat.getInstance(locale));
        tfMaxNumOfExtensions.setValue(fuzzySignalPhase.getMaxNumOfExtensions());
        tfMaxNumOfExtensions.setInputVerifier(new InputVerifier() {

            Border defaultBorder = tfMaxNumOfExtensions.getBorder();

            public boolean verify(JComponent input) {
                try {
                    tfMaxNumOfExtensions.commitEdit();
                } catch (ParseException e) {
                }
                int num = ((Number) tfMaxNumOfExtensions.getValue()).intValue();
                if (num < 0) {
                    tfMaxNumOfExtensions.setBorder(BorderFactory.createLineBorder(Color.red));
                    return false;
                } else {
                    tfMaxNumOfExtensions.setBorder(defaultBorder);
                    fuzzySignalPhase.setMaxNumOfExtensions(num);
                    return true;
                }
            }
        });
        tfMaxNumOfExtensions.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    tfMaxNumOfExtensions.transferFocus();
                }
            }
        });
        lbMaxNumOfExtensions = new JLabel(bundle.getString("lbMaxNumOfExtensions"));
        lbMaxNumOfExtensions.setToolTipText(bundle.getString("lbMaxNumOfExtensionsTip"));
        lbMaxNumOfExtensions.setLabelFor(tfMaxNumOfExtensions);
        tfMinExtensionTime = new JFormattedTextField(DecimalFormat.getInstance(locale));
        tfMinExtensionTime.setValue(fuzzySignalPhase.getMinExtensionTime());
        tfMinExtensionTime.setInputVerifier(new InputVerifier() {

            Border defaultBorder = tfMinExtensionTime.getBorder();

            public boolean verify(JComponent input) {
                try {
                    tfMinExtensionTime.commitEdit();
                    double b = ((Number) tfMinExtensionTime.getValue()).doubleValue();
                    if (b < 0) {
                        tfMinExtensionTime.setBorder(BorderFactory.createLineBorder(Color.red));
                        return false;
                    } else {
                        tfMinExtensionTime.setBorder(defaultBorder);
                        fuzzySignalPhase.setMinExtensionTime(b);
                        return true;
                    }
                } catch (ParseException e) {
                }
                return false;
            }
        });
        tfMinExtensionTime.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    tfMinExtensionTime.transferFocus();
                }
            }
        });
        lbMinExtensionTime = new JLabel(bundle.getString("lbMinExtensionTime"));
        lbMinExtensionTime.setToolTipText(bundle.getString("lbMinExtensionTimeTip"));
        lbMinExtensionTime.setLabelFor(tfMinExtensionTime);
        tfMultiplier = new JFormattedTextField(DecimalFormat.getInstance(locale));
        tfMultiplier.setValue(fuzzySignalPhase.getMultiplier());
        tfMultiplier.setInputVerifier(new InputVerifier() {

            Border defaultBorder = tfMultiplier.getBorder();

            public boolean verify(JComponent input) {
                try {
                    tfMultiplier.commitEdit();
                    double b = ((Number) tfMultiplier.getValue()).doubleValue();
                    if (b < 0) {
                        tfMultiplier.setBorder(BorderFactory.createLineBorder(Color.red));
                        return false;
                    } else {
                        tfMultiplier.setBorder(defaultBorder);
                        fuzzySignalPhase.setMultiplier(b);
                        return true;
                    }
                } catch (ParseException e) {
                }
                return false;
            }
        });
        tfMultiplier.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    tfMultiplier.transferFocus();
                }
            }
        });
        lbMultiplier = new JLabel(bundle.getString("lbMultiplier"));
        lbMultiplier.setToolTipText(bundle.getString("lbMultiplierTip"));
        lbMultiplier.setLabelFor(tfMultiplier);
        tableModelSignals = new TableModelSignals(fuzzySignalPhase, bundle);
        tableSignals = new JTable(tableModelSignals);
        tableSignals.setPreferredScrollableViewportSize(new Dimension(90, 100));
        tableSignals.setFillsViewportHeight(true);
        btAddSignalHead = new JButton(bundle.getString("btAddSignalHead"));
        btAddSignalHead.setToolTipText(bundle.getString("btAddSignalHeadTip"));
        btAddSignalHead.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                SignalHead signalHead = new SignalHead(0, modelController, fuzzySignalPhase);
                signalHead.setInitialPosition(fuzzySignalPhase.getSignalController().getX0() - 10, fuzzySignalPhase.getSignalController().getY0() - 10, true);
                modelController.addDrawnableEnt(signalHead);
                modelController.repaint();
                tableModelSignals.addRow(signalHead);
            }
        });
        btRemoveSignalHead = new JButton(bundle.getString("btRemoveSignalHead"));
        btRemoveSignalHead.setToolTipText(bundle.getString("btRemoveSignalHeadTip"));
        btRemoveSignalHead.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int selectedRows[] = tableSignals.getSelectedRows();
                SignalHead signalHead;
                if (selectedRows.length > 0) {
                    for (int i = selectedRows.length - 1; i >= 0; i--) {
                        signalHead = tableModelSignals.getSignalHeadAt(selectedRows[i]);
                        modelController.removeDrawnableEnt(signalHead);
                        tableModelSignals.fireTableDataChanged();
                    }
                    modelController.repaint();
                }
            }
        });
        this.add(lbMinGreenTime);
        this.add(tfMinGreenTime, "wrap, growx");
        this.add(lbYellowTime);
        this.add(tfYellowTime, "wrap, growx");
        this.add(lbRedTime);
        this.add(tfRedTime, "wrap, growx");
        this.add(lbMaxNumOfExtensions);
        this.add(tfMaxNumOfExtensions, "wrap, growx");
        this.add(lbMinExtensionTime);
        this.add(tfMinExtensionTime, "wrap, growx");
        this.add(lbMultiplier);
        this.add(tfMultiplier, "wrap, growx");
        this.add(new JScrollPane(tableSignals), "wrap");
        this.add(btAddSignalHead, "align lead");
        this.add(btRemoveSignalHead, "align lead, wrap");
    }

    public FuzzySignalPhase getFuzzySignalPhase() {
        return fuzzySignalPhase;
    }
}
