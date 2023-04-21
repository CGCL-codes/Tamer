package org.fudaa.ctulu.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import com.memoire.bu.BuGridLayout;
import com.memoire.bu.BuTextField;
import com.memoire.fu.FuLib;
import java.awt.BorderLayout;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import org.fudaa.ctulu.CtuluCommandComposite;
import org.fudaa.ctulu.CtuluCommandContainer;
import org.fudaa.ctulu.CtuluExpr;
import org.fudaa.ctulu.CtuluLib;
import org.fudaa.ctulu.CtuluLibArray;
import org.fudaa.ctulu.CtuluParser;
import org.fudaa.ctulu.collection.CtuluArrayDouble;
import org.fudaa.ctulu.collection.CtuluArrayInteger;
import org.fudaa.ctulu.collection.CtuluArrayObject;
import org.fudaa.ctulu.collection.CtuluCollection;
import org.fudaa.ctulu.collection.CtuluCollectionDoubleEdit;
import org.fudaa.ctulu.editor.CtuluExprTextField;
import org.fudaa.ctulu.editor.CtuluValueEditorI;
import org.nfunk.jep.Variable;

/**
 * @author Fred Deniger
 * @version $Id: CtuluValuesCommonEditorPanel.java,v 1.13 2007-03-23 17:16:16 deniger Exp $
 */
public class CtuluValuesCommonEditorPanel extends CtuluDialogPanel {

    protected CtuluValueEditorI[] editors_;

    protected JComponent[] comps_;

    JLabel[] lbs_;

    CtuluCollection[] values_;

    protected int[] idx_;

    protected CtuluCommandContainer cmd_;

    private CtuluValuesCommonEditorVariableProvider variablesProvider;

    public CtuluValuesCommonEditorVariableProvider getVariables() {
        return variablesProvider;
    }

    public void setVariables(CtuluValuesCommonEditorVariableProvider variables) {
        this.variablesProvider = variables;
    }

    /**
   * @return le receveur de commandes
   */
    public final CtuluCommandContainer getCmd() {
        return cmd_;
    }

    /**
   * @param _cmd le receveur de commande
   */
    public final void setCmd(final CtuluCommandContainer _cmd) {
        cmd_ = _cmd;
    }

    /**
   * @param _names les noms (non null)
   * @param _idx les indices a modifier.
   * @param _editors les editeurs
   * @param _values les valeurs (meme taille que les editeurs)
   */
    public CtuluValuesCommonEditorPanel(final Object[] _names, final int[] _idx, final CtuluValueEditorI[] _editors, final CtuluCollection[] _values) {
        this(_names, _idx, _editors, _values, null);
    }

    public CtuluValuesCommonEditorPanel(final Object[] _names, final int[] _idx, final CtuluValueEditorI[] _editors, final CtuluCollection[] _values, CtuluValuesCommonEditorVariableProvider variablesProvider) {
        this.variablesProvider = variablesProvider;
        setData(_names, _idx, _editors, _values);
    }

    public CtuluValuesCommonEditorPanel(final CtuluValuesParameters _params) {
        setData(_params.getNames(), _params.getIdx(), _params.getEditors(), _params.getValues());
        this.variablesProvider = _params.getVariableProvider();
    }

    Object[] oldValues_;

    private class ChangeListener implements DocumentListener, ItemListener {

        final int i_;

        /**
     * @param _i l'indice
     */
        public ChangeListener(final int _i) {
            super();
            i_ = _i;
        }

        public void itemStateChanged(final ItemEvent _e) {
            update();
        }

        private void update() {
            final Object newVal = editors_[i_].getValue(comps_[i_]);
            final boolean equals = (newVal == oldValues_[i_]) || (newVal != null && newVal.equals(oldValues_[i_]));
            lbs_[i_].setForeground(equals ? CtuluLibSwing.getDefaultLabelForegroundColor() : Color.BLUE);
        }

        public void changedUpdate(final DocumentEvent _e) {
            update();
        }

        public void insertUpdate(final DocumentEvent _e) {
            update();
        }

        public void removeUpdate(final DocumentEvent _e) {
            update();
        }
    }

    private void setData(final Object[] _names, final int[] _idx, final CtuluValueEditorI[] _editors, final CtuluCollection[] _values) {
        editors_ = _editors;
        values_ = _values;
        idx_ = _idx;
        if (editors_.length != values_.length) {
            throw new IllegalArgumentException("same size required");
        }
        JPanel pnAttrs = new JPanel();
        pnAttrs.setLayout(new BuGridLayout(2, 5, 5));
        comps_ = new JComponent[editors_.length];
        lbs_ = new JLabel[editors_.length];
        oldValues_ = new Object[lbs_.length];
        for (int i = 0; i < comps_.length; i++) {
            comps_[i] = editors_[i].createCommonEditorComponent();
            if (comps_[i] instanceof CtuluExprTextField) {
                CtuluExprTextField tfExpr = (CtuluExprTextField) comps_[i];
                tfExpr.setFormulaMenuAsButton(true);
                if (this.variablesProvider != null) {
                    Collection<String> userVariables = variablesProvider.getVariables();
                    for (String varName : userVariables) {
                        tfExpr.getExpr().addVar(varName, varName);
                    }
                }
                tfExpr.getExpr().addVar("i", CtuluLib.getS("L'indice"));
                CtuluParser.addOldVar(tfExpr.getExpr());
            }
            final boolean isCommon = (CtuluLibArray.isEmpty(_idx) || idx_.length == 1) ? true : values_[i].isSameValues(idx_);
            if (isCommon) {
                oldValues_[i] = values_[i].getObjectValueAt(_idx == null ? 0 : _idx[0]);
                editors_[i].setValue(oldValues_[i], comps_[i]);
                if (comps_[i] instanceof BuTextField) {
                    ((BuTextField) comps_[i]).getDocument().addDocumentListener(new ChangeListener(i));
                } else if (comps_[i] instanceof JComboBox) {
                    ((JComboBox) comps_[i]).addItemListener(new ChangeListener(i));
                }
            }
            lbs_[i] = new JLabel(_names[i].toString());
            lbs_[i].setLabelFor(comps_[i]);
            pnAttrs.add(lbs_[i]);
            pnAttrs.add(comps_[i]);
        }
        setLayout(new BorderLayout(5, 5));
        if (lbs_.length > 8) {
            JScrollPane comp = new JScrollPane(pnAttrs);
            Dimension preferredSize = comp.getPreferredSize();
            preferredSize.height = Math.min(350, preferredSize.height);
            preferredSize.width = Math.max(preferredSize.width, 400);
            comp.setPreferredSize(preferredSize);
            add(comp);
        } else {
            add(pnAttrs);
        }
    }

    /**
   * Definit les indice selectionn�s
   * 
   * @param _idx Les indices
   */
    public void setIdx(int[] _idx) {
        idx_ = _idx;
        for (int i = 0; i < comps_.length; i++) {
            final boolean isCommon = (CtuluLibArray.isEmpty(_idx) || idx_.length == 1) ? true : values_[i].isSameValues(idx_);
            if (isCommon) {
                oldValues_[i] = values_[i].getObjectValueAt(_idx == null ? 0 : _idx[0]);
                editors_[i].setValue(oldValues_[i], comps_[i]);
            }
        }
    }

    protected CtuluCommandComposite internApply() {
        final CtuluCommandComposite cmp = new CtuluCommandComposite();
        for (int i = comps_.length - 1; i >= 0; i--) {
            if (!editors_[i].isEmpty(comps_[i])) {
                final Object o = editors_[i].getValue(comps_[i]);
                if (o instanceof CtuluExpr) {
                    final CtuluParser exp = ((CtuluExpr) o).getParser();
                    final org.nfunk.jep.Variable vold = exp.getVar(CtuluParser.getOldVariable());
                    final org.nfunk.jep.Variable vind = exp.getVar("i");
                    Map<String, Variable> userVariablesMap = new HashMap<String, Variable>();
                    if (this.variablesProvider != null) {
                        Collection<String> userVariables = variablesProvider.getVariables();
                        for (String string : userVariables) {
                            Variable var = exp.getVar(string);
                            if (var != null) {
                                userVariablesMap.put(string, var);
                            }
                        }
                    }
                    final Object[] newVals = new Object[idx_ == null ? values_[i].getSize() : idx_.length];
                    for (int k = newVals.length - 1; k >= 0; k--) {
                        this.variablesProvider.modifyVariablesFor(idx_[k], userVariablesMap);
                        if (vold != null) vold.setValue(values_[i].getObjectValueAt(idx_ == null ? k : idx_[k]));
                        if (vind != null) vind.setValue(new Integer(idx_[k] + 1));
                        newVals[k] = exp.getValueAsObject();
                    }
                    if (idx_ == null) {
                        values_[i].setAllObject(CtuluLibArray.fillIncremental(new int[newVals.length], 0), newVals, cmp);
                    } else {
                        values_[i].setAllObject(idx_, newVals, cmp);
                    }
                } else {
                    if (idx_ == null) {
                        values_[i].setAll(editors_[i].getValue(comps_[i]), cmp);
                    } else {
                        values_[i].setObject(idx_, editors_[i].getValue(comps_[i]), cmp);
                    }
                }
            }
        }
        return cmp;
    }

    public void apply() {
        final CtuluCommandComposite cmp = internApply();
        if (cmd_ != null) {
            cmd_.addCmd(cmp.getSimplify());
        }
    }

    public boolean isDataValid() {
        boolean r = true;
        for (int i = comps_.length - 1; i >= 0; i--) {
            if (!editors_[i].isEmpty(comps_[i])) {
                final boolean isValide = editors_[i].isValueValidFromComponent(comps_[i]);
                lbs_[i].setForeground(isValide ? Color.BLACK : Color.RED);
                if (!isValide) {
                    final String desc = editors_[i].getValidationMessage();
                    if (desc != null) {
                        comps_[i].setToolTipText(desc);
                    }
                }
                r &= isValide;
            }
        }
        return r;
    }
}
