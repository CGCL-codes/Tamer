package ist.ac.simulador.guis;

import java.text.Format;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.awt.event.KeyEvent;
import ist.ac.simulador.nucleo.SElement;
import ist.ac.simulador.nucleo.SModule;
import ist.ac.simulador.modules.ModuleInput;

/**
 *
 * @author  Nuno Afonso <nafonso@gmail.com>
 */
public class GuiInput extends ist.ac.simulador.application.Gui {

    private HexaFormat hexaFormat;

    /**
	 * Creates new form GuiInput
	 */
    public GuiInput(int inputLength) {
        super(ModuleInput.class);
        hexaFormat = new HexaFormat(inputLength);
        initComponents();
    }

    public void reset() {
        super.reset();
        hexaValue.setText("");
        actualValue.setText("Z");
    }

    private void initComponents() {
        panel = new javax.swing.JPanel();
        hexaValueLabel = new javax.swing.JLabel();
        okButton = new javax.swing.JButton();
        hexaValue = new javax.swing.JFormattedTextField(hexaFormat);
        valueLabel = new javax.swing.JLabel();
        actualValue = new javax.swing.JLabel();
        hexaValueLabel.setFont(new java.awt.Font("Lucida Grande", 1, 13));
        hexaValueLabel.setText("Set");
        okButton.setText("Ok");
        okButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        hexaValue.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        hexaValue.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyTyped(java.awt.event.KeyEvent evt) {
                hexaValueKeyTyped(evt);
            }
        });
        valueLabel.setFont(new java.awt.Font("Lucida Grande", 1, 13));
        valueLabel.setText("Value:");
        actualValue.setText("0");
        org.jdesktop.layout.GroupLayout panelLayout = new org.jdesktop.layout.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(panelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(panelLayout.createSequentialGroup().add(hexaValueLabel).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(hexaValue, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 54, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(okButton)).add(panelLayout.createSequentialGroup().add(valueLabel).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(actualValue)));
        panelLayout.setVerticalGroup(panelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(panelLayout.createSequentialGroup().add(panelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(hexaValueLabel).add(hexaValue, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(okButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(panelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(valueLabel).add(actualValue))));
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(panel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(panel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        pack();
    }

    private void hexaValueKeyTyped(java.awt.event.KeyEvent evt) {
        if ((hexaValue.getText().length() >= hexaFormat.getNumberOfChars()) && (evt.getKeyChar() != KeyEvent.VK_DELETE) && (evt.getKeyChar() != KeyEvent.VK_BACK_SPACE) && (hexaValue.getSelectedText() == null)) {
            java.awt.Toolkit.getDefaultToolkit().beep();
            evt.consume();
        }
    }

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {
        String hexa = hexaValue.getText();
        if (hexa.equals("")) {
            java.awt.Toolkit.getDefaultToolkit().beep();
            return;
        }
        if (Integer.parseInt(hexa, 16) > (int) (Math.pow(2.0, ((ModuleInput) getBaseElement()).getWordSize()) - 1)) {
            java.awt.Toolkit.getDefaultToolkit().beep();
            int aux = ((ModuleInput) getBaseElement()).getValue();
            if (aux == SElement.Z) {
                hexaValue.setText("");
                return;
            }
            hexaValue.setText(Integer.toHexString(aux));
        } else {
            ((ModuleInput) getBaseElement()).setValue(Integer.parseInt(hexaValue.getText(), 16));
            actualValue.setText(hexaValue.getText());
        }
    }

    private javax.swing.JLabel actualValue;

    private javax.swing.JFormattedTextField hexaValue;

    private javax.swing.JLabel hexaValueLabel;

    private javax.swing.JButton okButton;

    private javax.swing.JPanel panel;

    private javax.swing.JLabel valueLabel;

    class HexaFormat extends Format {

        private int numberOfChars;

        /**
		 * Constructor.
		 * @param numberOfChars N�mero de characteres em Hexadecimal.
		 *
		 */
        public HexaFormat(int numberOfChars) {
            this.numberOfChars = numberOfChars;
        }

        public int getNumberOfChars() {
            return numberOfChars;
        }

        public StringBuffer format(Object obj, StringBuffer appendTo, FieldPosition pos) {
            appendTo.append(((String) obj).toUpperCase());
            System.out.println("format: " + obj.toString());
            System.out.println(appendTo.toString());
            return appendTo;
        }

        public Object parseObject(String str, ParsePosition pos) {
            System.out.println("parseObject: " + str);
            System.out.println(pos.toString());
            if (str.length() > getNumberOfChars()) {
                pos.setErrorIndex(getNumberOfChars());
                java.awt.Toolkit.getDefaultToolkit().beep();
                return null;
            }
            for (int i = 0, limit = str.length(); i != limit; ++i) if (charValid(str.charAt(i))) pos.setIndex(i + 1); else {
                pos.setIndex(0);
                pos.setErrorIndex(i);
                java.awt.Toolkit.getDefaultToolkit().beep();
                return null;
            }
            return str;
        }

        private boolean charValid(char c) {
            if (((c >= '0') && (c <= '9')) || ((c >= 'a') && (c <= 'f')) || ((c >= 'A') && (c <= 'F'))) return true;
            return false;
        }
    }
}
