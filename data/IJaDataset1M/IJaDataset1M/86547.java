package org.fudaa.fudaa.commun.exetools;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.File;
import javax.swing.DefaultCellEditor;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;
import org.fudaa.ctulu.ProgressionDetailedInterface;
import org.fudaa.ctulu.gui.CtuluDialogPanel;
import org.fudaa.fudaa.commun.FudaaLib;

/**
 * Une classe d'un utilitaire ex�cutable
 * @author marchand@deltacad.fr
 */
public class FudaaExeTool {

    public String name;

    public File exePath;

    public File exeDirPath;

    private ParamI[] params_;

    /**
   * Une classe de param�tres de l'utilitaire.
   * @author marchand@deltacad.fr
   */
    public abstract static class ParamI {

        /** L'exe tool associ� */
        public FudaaExeTool exeTool;

        /** Le nom du param�tre */
        public String name = "";

        /**
     * Duplication de parametres
     * @return Le param�tre, dupliqu� en profondeur.
     */
        @Override
        public abstract ParamI clone();

        /** 
     * Execut� avant lancement de l'exe.
     * @param _prog La progression
     * @param True : La pr�paration s'est bien pass�e.
     */
        public abstract boolean prepareLaunch(ProgressionDetailedInterface _prog);

        /** Le type du param�tre, sauv� dans le fichier des pr�f�rences */
        public abstract String getType();

        /** Doit-il �tre donn� par l'utilisateur ? */
        public abstract boolean mustBeSet();

        /** Le label affich� lors de la cr�ation */
        public abstract String getLabel();

        /** Retourne le panel utilis� pour d�finir le parametre */
        public abstract CtuluDialogPanel getDefinitionPanel();

        /** Retourne le texte affich� dans le Cell du panneau de d�finition */
        public abstract String getDefinitionCellText();

        /** Retourne le texte affich� dans le Cell du panneau d'affectation */
        public abstract String getSetCellText();

        /** L'editeur de cellule pour le panneau d'affectation */
        public abstract TableCellEditor getSetCellEditor();

        /** Affecte la valeur de d�finition */
        public abstract void setValue(String _val);

        /** Retourne la valeur de d�finition */
        public abstract String getValue();

        /** Affecte la valeur donn�e par l'utilisateur */
        public abstract void setValSet(String _val);

        /** Retourne la valeur donn�e par l'utilisateur */
        public abstract String getValSet();

        /** Retourne la valeur pass�e en param�tre */
        public abstract String getValParam();

        /**
     * Ex�cut� apr�s le lancement
     * @param _prog La progression
     */
        public abstract boolean postLaunch(ProgressionDetailedInterface _prog);
    }

    public static class ParamConst extends ParamI {

        ParamConstDefinitionPanel pnDef_ = new ParamConstDefinitionPanel();

        private String value;

        @Override
        public ParamConst clone() {
            ParamConst o = new ParamConst();
            o.name = name;
            o.exeTool = exeTool;
            o.pnDef_ = pnDef_;
            o.value = value;
            return o;
        }

        @Override
        public void setValue(String _val) {
            value = _val;
        }

        @Override
        public String getValue() {
            return value;
        }

        @Override
        public void setValSet(String _val) {
            value = _val;
        }

        @Override
        public String getValSet() {
            return value;
        }

        @Override
        public String getValParam() {
            return value;
        }

        @Override
        public boolean postLaunch(ProgressionDetailedInterface _prog) {
            return true;
        }

        public class ParamConstDefinitionPanel extends CtuluDialogPanel {

            JTextField tfConstValue_ = new JTextField();

            public ParamConstDefinitionPanel() {
                setLayout(new BorderLayout(5, 0));
                add(new JLabel(FudaaLib.getS("Valeur fixe")), BorderLayout.WEST);
                add(tfConstValue_, BorderLayout.CENTER);
            }

            @Override
            public boolean isDataValid() {
                if (tfConstValue_.getText().trim().length() == 0) {
                    setErrorText(FudaaLib.getS("La valeur doit �tre d�finie"));
                    return false;
                }
                return true;
            }

            @Override
            public void setValue(Object _o) {
                FudaaExeTool.ParamI param = (FudaaExeTool.ParamI) _o;
                tfConstValue_.setText(param.getValue());
            }

            @Override
            public FudaaExeTool.ParamI getValue() {
                FudaaExeTool.ParamI param = new ParamConst();
                param.setValue(tfConstValue_.getText().trim());
                return param;
            }
        }

        @Override
        public String getDefinitionCellText() {
            String nm = "";
            if (!"".equals(name)) nm = FudaaLib.getS("Nom") + "=" + name + " ; ";
            return nm + getLabel() + "/" + FudaaLib.getS("Valeur={0}", value);
        }

        /** Not used */
        @Override
        public String getSetCellText() {
            return null;
        }

        /** Not used */
        @Override
        public TableCellEditor getSetCellEditor() {
            return null;
        }

        /** Not used */
        @Override
        public boolean prepareLaunch(ProgressionDetailedInterface _prog) {
            return true;
        }

        @Override
        public String getType() {
            return "CONST";
        }

        @Override
        public boolean mustBeSet() {
            return false;
        }

        @Override
        public CtuluDialogPanel getDefinitionPanel() {
            return pnDef_;
        }

        @Override
        public String getLabel() {
            return FudaaLib.getS("Fixe");
        }
    }

    public static class ParamVar extends ParamI {

        ParamVarSetCellEditor edSet_ = new ParamVarSetCellEditor();

        ParamVarDefinitionPanel pnDef_ = new ParamVarDefinitionPanel();

        private String value;

        private String valSet;

        @Override
        public ParamVar clone() {
            ParamVar o = new ParamVar();
            o.name = name;
            o.exeTool = exeTool;
            o.edSet_ = edSet_;
            o.pnDef_ = pnDef_;
            o.value = value;
            o.valSet = valSet;
            return o;
        }

        @Override
        public void setValue(String _val) {
            value = _val;
            valSet = _val;
        }

        @Override
        public String getValue() {
            return value;
        }

        @Override
        public void setValSet(String _val) {
            valSet = _val;
        }

        @Override
        public String getValSet() {
            return valSet;
        }

        @Override
        public String getValParam() {
            return valSet;
        }

        @Override
        public boolean postLaunch(ProgressionDetailedInterface _prog) {
            return true;
        }

        public class ParamVarSetCellEditor extends DefaultCellEditor {

            private FudaaExeTool.ParamI param;

            JTextField tf;

            public ParamVarSetCellEditor() {
                super(new JTextField());
            }

            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                tf = (JTextField) super.getTableCellEditorComponent(table, value, isSelected, row, column);
                param = (FudaaExeTool.ParamI) value;
                String text = param.getValSet();
                tf.setText(text);
                return tf;
            }

            @Override
            public Object getCellEditorValue() {
                param.setValSet(tf.getText().trim());
                return param;
            }
        }

        public class ParamVarDefinitionPanel extends CtuluDialogPanel {

            JTextField tfVarValue_ = new JTextField();

            public ParamVarDefinitionPanel() {
                tfVarValue_ = new JTextField();
                setLayout(new BorderLayout(5, 0));
                add(new JLabel(FudaaLib.getS("Valeur par d�faut")), BorderLayout.WEST);
                add(tfVarValue_, BorderLayout.CENTER);
            }

            @Override
            public boolean isDataValid() {
                if (tfVarValue_.getText().trim().length() == 0) {
                    setErrorText(FudaaLib.getS("La valeur par d�faut doit �tre d�finie"));
                    return false;
                }
                return true;
            }

            @Override
            public void setValue(Object _o) {
                FudaaExeTool.ParamI param = (FudaaExeTool.ParamI) _o;
                tfVarValue_.setText(param.getValue());
            }

            @Override
            public FudaaExeTool.ParamI getValue() {
                FudaaExeTool.ParamI param = new ParamVar();
                param.setValue(tfVarValue_.getText().trim());
                return param;
            }
        }

        @Override
        public String getDefinitionCellText() {
            String nm = "";
            if (!"".equals(name)) nm = FudaaLib.getS("Nom") + "=" + name + " ; ";
            return nm + getLabel() + "/" + FudaaLib.getS("Valeur defaut={0}", value);
        }

        @Override
        public String getSetCellText() {
            return valSet;
        }

        @Override
        public TableCellEditor getSetCellEditor() {
            return edSet_;
        }

        /** Not used */
        @Override
        public boolean prepareLaunch(ProgressionDetailedInterface _prog) {
            return true;
        }

        @Override
        public String getType() {
            return "VAR";
        }

        @Override
        public boolean mustBeSet() {
            return true;
        }

        @Override
        public CtuluDialogPanel getDefinitionPanel() {
            return pnDef_;
        }

        @Override
        public String getLabel() {
            return FudaaLib.getS("Variable");
        }
    }

    /**
   * Duplication d'un tool (Dupliqu� en profondeur).
   * @return Le tool dupliqu�.
   */
    @Override
    public FudaaExeTool clone() {
        FudaaExeTool o = new FudaaExeTool();
        o.exeDirPath = exeDirPath;
        o.exePath = exePath;
        o.name = name;
        o.params_ = new FudaaExeTool.ParamI[params_.length];
        for (int i = 0; i < o.params_.length; i++) {
            o.params_[i] = params_[i].clone();
        }
        return o;
    }

    public void setParams(ParamI[] _params) {
        for (ParamI param : _params) {
            param.exeTool = this;
        }
        params_ = _params;
    }

    public ParamI[] getParams() {
        return params_;
    }
}
