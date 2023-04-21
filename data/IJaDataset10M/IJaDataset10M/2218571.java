package net.benojt.iterator;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.math.BigDecimal;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.benojt.context.Context;
import net.benojt.tools.BigDecimalComplex;
import net.benojt.tools.BoundingBox;
import net.benojt.tools.Complex;
import net.benojt.ui.DoubleTextField;
import net.benojt.ui.IntegerSpinner;
import net.benojt.ui.NumberTextField;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * an iterator for the spider fractal.
 * @author frank
 *
 */
public class Spider extends AbstractParameterIterator {

    static final String XMLNodeExpZ = "expZ";

    static final String XMLNodeDivCN = "divCN";

    static final String XMLNodeInitZ = "initZ";

    static final String XMLNodeInitC = "initC";

    static final String XMLNodeUseC = "useC";

    int expZ = 2;

    int expZmin = 2;

    double divCN = 2;

    int initZ = 1;

    int initC = 1;

    boolean useC;

    public Spider() {
        super();
        this.bb = new BoundingBox("-1.5", "-1.5", "1.5", "1.5");
        usebdComplex = true;
    }

    @Override
    public void buildXML() {
        super.buildXML();
        this.xmlContent.addProperty(XMLNodeExpZ, new Integer(this.expZ));
        this.xmlContent.addProperty(XMLNodeInitZ, new Integer(this.initZ));
        this.xmlContent.addProperty(XMLNodeInitC, new Integer(this.initC));
        this.xmlContent.addProperty(XMLNodeDivCN, new Double(this.divCN));
        this.xmlContent.addProperty(XMLNodeUseC, new Boolean(this.useC));
    }

    @Override
    public String loadConfig(NodeList nodes) {
        String errors = super.loadConfig(nodes);
        for (int i = 0; i < nodes.getLength(); i++) {
            Node n = nodes.item(i);
            if (n.getNodeName().equals("property")) {
                String valueStr = n.getTextContent();
                Node propName = n.getAttributes().getNamedItem("name");
                try {
                    if (propName != null) {
                        String propVal = propName.getNodeValue();
                        if (propVal.equals(XMLNodeExpZ)) {
                            this.expZ = new Integer(valueStr);
                        } else if (propVal.equals(XMLNodeInitZ)) {
                            this.initZ = new Integer(valueStr);
                        } else if (propVal.equals(XMLNodeInitC)) {
                            this.initC = new Integer(valueStr);
                        } else if (propVal.equals(XMLNodeDivCN)) {
                            this.divCN = new Double(valueStr);
                        } else if (propVal.equals(XMLNodeUseC)) {
                            this.useC = new Boolean(valueStr);
                        }
                    }
                } catch (Exception ex) {
                    errors += "Could not read iterator property" + propName + ".\n";
                }
            }
        }
        return errors;
    }

    @Override
    public int iterPoint(BigDecimal[] coords) {
        BigDecimalComplex _z = new BigDecimalComplex(coords);
        switch(initZ) {
            case 0:
                _z = new BigDecimalComplex(0, 0);
                break;
            case 1:
                break;
            case 2:
                _z = c.toBigDecimal();
                break;
        }
        BigDecimalComplex cn = new BigDecimalComplex(coords);
        switch(initC) {
            case 0:
                cn = new BigDecimalComplex(0, 0);
                break;
            case 1:
                break;
            case 2:
                cn = c.toBigDecimal();
                break;
        }
        value = 0d;
        iter = 0;
        while (iter++ <= maxIter && value <= maxValue) {
            _z = _z.exp(expZ).add(cn);
            if (useC) _z = _z.add(c);
            cn = cn.div(divCN).add(_z);
            value = _z.mod();
        }
        z = _z.toDouble();
        return iter > maxIter ? -1 : iter;
    }

    @Override
    public int iterPoint(double[] coords) {
        Complex locZ = this.z;
        switch(initZ) {
            case 0:
                locZ.setTo(Complex.ZERO);
                break;
            case 1:
                locZ.setTo(coords);
                break;
            case 2:
                locZ.setTo(c);
                break;
        }
        Complex cn = null;
        switch(initC) {
            case 0:
                cn = new Complex(0);
                break;
            case 1:
                cn = new Complex(coords);
                break;
            case 2:
                cn = new Complex(c);
                break;
            default:
                cn = new Complex();
        }
        value = 0d;
        iter = 0;
        while (iter++ <= maxIter && value <= maxValue) {
            locZ = locZ.exp_(expZ).add_(cn);
            if (useC) locZ = locZ.add_(c);
            cn = cn.div_(divCN).add_(locZ);
            value = locZ.mod();
        }
        this.z = locZ;
        return iter > maxIter ? -1 : iter;
    }

    public class ConfigDlg extends AbstractParameterIterator.ConfigDlg {

        IntegerSpinner expZSP;

        DoubleTextField divJTF;

        JCheckBox useCCB;

        JComboBox initZCB, initCCB;

        JLabel formLabel;

        DocumentListener dl;

        public ConfigDlg(java.awt.Frame frame) {
            super(frame);
        }

        @Override
        protected void uiInit() {
            super.uiInit();
            this.formLabel = new JLabel();
            this.addContent(this.formLabel, new GridBagConstraints(0, 1, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(15, 0, 5, 0), 0, 0));
            this.addContent("<HTML><NOBR>z<sub>0</sub> (init):", NEW_LINE);
            this.initZCB = new JComboBox();
            this.initZCB.addItem("zero");
            this.initZCB.addItem("point");
            this.initZCB.addItem("param");
            this.addContent(this.initZCB);
            this.initZCB.setFont(Context.getDlgFont());
            this.addContent("<HTML><NOBR>c<sub>0</sub> (init):", NEW_LINE);
            this.initCCB = new JComboBox();
            this.initCCB.addItem("zero");
            this.initCCB.addItem("point");
            this.initCCB.addItem("param");
            this.addContent(this.initCCB);
            this.initCCB.setFont(Context.getDlgFont());
            this.expZSP = new IntegerSpinner("exp for z:");
            this.addContent(expZSP, NEW_LINE);
            this.divJTF = new DoubleTextField(NumberTextField.PAT3, "<HTML><NOBR>div for c<sub>n</sub>:");
            this.addContent(divJTF, NEW_LINE);
            this.useCCB = new JCheckBox("add Parameter c");
            this.addContent(this.useCCB, NEW_LINE, COL_SPAN(2));
            ChangeListener formulaListener2 = new ChangeListener() {

                public void stateChanged(ChangeEvent e) {
                    setFormula();
                }
            };
            dl = new DocumentListener() {

                public void changedUpdate(DocumentEvent e) {
                    setFormula();
                }

                public void insertUpdate(DocumentEvent e) {
                    setFormula();
                }

                public void removeUpdate(DocumentEvent e) {
                    setFormula();
                }
            };
            this.useCCB.addActionListener(this);
            this.expZSP.addChangeListener(formulaListener2);
        }

        @Override
        public void dataInit() {
            this.divJTF.getDocument().removeDocumentListener(dl);
            super.dataInit();
            this.expZSP.setNumber(expZ);
            this.divJTF.setNumber(divCN);
            this.useCCB.setSelected(useC);
            this.initZCB.setSelectedIndex(initZ);
            this.initCCB.setSelectedIndex(initC);
            this.setFormula();
            this.divJTF.getDocument().addDocumentListener(dl);
        }

        private void setFormula() {
            double dn = 0;
            try {
                dn = this.divJTF.getNumber();
            } catch (Exception ex) {
            }
            if (dn == 0) dn = divCN;
            int ez = Math.max(expZmin, this.expZSP.getNumber());
            String formString = "<HTML><center><NOBR>z<sub>n+1</sub>=z<sub>n</sub><sup>" + ez + "</sup>+c<sub>n</sub>" + (this.useCCB.isSelected() ? "+c" : "") + "<br>c<sub>n+1</sub>=c<sub>n</sub>/" + divCN + "+z<sub>n</sub>";
            formString += "</NOBR></center></HTML>";
            this.formLabel.setText(formString);
        }

        @Override
        protected void applyBT_action(java.awt.event.ActionEvent e) {
            int ez = this.expZSP.getNumber();
            Double dn = Double.NaN;
            try {
                dn = this.divJTF.getNumber();
            } catch (Exception ex) {
            }
            if (ez < expZmin) ez = expZ;
            if (dn == 0 || dn.isNaN()) dn = divCN;
            if (ez != expZ || dn != divCN || useC != this.useCCB.isSelected() || initZ != this.initZCB.getSelectedIndex() || initC != this.initCCB.getSelectedIndex()) {
                expZ = Math.max(2, ez);
                divCN = dn;
                useC = this.useCCB.isSelected();
                initZ = this.initZCB.getSelectedIndex();
                initC = this.initCCB.getSelectedIndex();
                mustRerender = true;
            }
            super.applyBT_action(e);
        }

        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            if (e.getSource() == this.useCCB) {
                setFormula();
            } else super.actionPerformed(e);
        }
    }

    @Override
    public String getInfoMessage() {
        return "A spider with configurable parameters and high precision support." + "The base formula is:<BR>" + "<center>z<sub>n+1</sub> = z<sub>n</sub><sup>ex</sup> + c<sub>n</sub><BR>" + "c<sub>n+1</sub> = c<sub>n</sub>/dv + z<sub>n</sub></center>" + "<P>The exponent ex, divisor dv and the initial values z<sub>0</sub> and c<sub>0</sub>" + "are configurable.";
    }
}
