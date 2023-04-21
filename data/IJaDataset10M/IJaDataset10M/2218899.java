package org.digitall.apps.personalfiles.interfaces;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import org.digitall.apps.personalfiles.classes.Dependencia;
import org.digitall.apps.personalfiles.classes.VectorDependencia;
import org.digitall.lib.components.Advisor;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.buttons.AcceptButton;
import org.digitall.lib.components.buttons.DeleteButton;
import org.digitall.lib.components.buttons.EditButton;
import org.digitall.lib.components.inputpanels.TFInput;
import org.digitall.lib.data.DataTypes;

public class MisionesYFuncionesPanel extends BasicPanel {

    private EditButton bEdit = new EditButton();

    private DeleteButton bDelete = new DeleteButton();

    private Dependencia dependencia = null;

    private AcceptButton bAccept = new AcceptButton();

    private DependenciaTree parent = null;

    private VectorDependencia vectorDependencia = null;

    private Vector vecTiposDependenciaNombre = new Vector();

    private Vector vecTiposDependenciaId = new Vector();

    private BorderLayout borderLayout1 = new BorderLayout();

    private BasicLabel blNroPersonasDepDat = new BasicLabel();

    private BasicLabel blNroPersonasSubdepDat = new BasicLabel();

    private String paramsIn = "";

    private JTextPane tpMisiones = new JTextPane();

    private JTextPane tpFunciones = new JTextPane();

    private JScrollPane spMisiones = new JScrollPane(tpMisiones);

    private JScrollPane spFunciones = new JScrollPane(tpFunciones);

    private TFInput tfiDependencia = new TFInput(DataTypes.STRING, "Dependencia", false);

    public MisionesYFuncionesPanel() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setPreferredSize(new Dimension(790, 180));
        this.setSize(new Dimension(800, 188));
        this.setLayout(null);
        blNroPersonasSubdepDat.setBounds(new Rectangle(175, 60, 70, 15));
        spMisiones.setBorder(BorderFactory.createTitledBorder("Mision"));
        spMisiones.setBounds(new Rectangle(10, 10, 390, 135));
        spFunciones.setBorder(BorderFactory.createTitledBorder("Funciones"));
        spFunciones.setBounds(new Rectangle(405, 10, 385, 135));
        tfiDependencia.setBounds(new Rectangle(10, 145, 390, 35));
        tfiDependencia.setPreferredSize(new Dimension(390, 35));
        tfiDependencia.setSize(new Dimension(390, 35));
        tfiDependencia.setEnabled(false);
        bEdit.setBounds(new Rectangle(700, 40, 30, 20));
        bEdit.setPreferredSize(new Dimension(30, 20));
        bEdit.setSize(new Dimension(30, 20));
        bDelete.setBounds(new Rectangle(730, 40, 30, 20));
        bDelete.setPreferredSize(new Dimension(30, 20));
        bAccept.setBounds(new Rectangle(765, 160, 30, 20));
        bAccept.setSize(new Dimension(30, 20));
        bAccept.setPreferredSize(new Dimension(30, 20));
        bAccept.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                bAccept_actionPerformed(e);
            }
        });
        this.add(tfiDependencia, null);
        this.add(spFunciones, null);
        this.add(spMisiones, null);
        this.add(blNroPersonasSubdepDat, null);
        this.add(bAccept, null);
        this.add(tfiDependencia, null);
    }

    private void bAccept_actionPerformed(ActionEvent e) {
        dependencia.setMisiones(tpMisiones.getText());
        dependencia.setFunciones(tpFunciones.getText());
        if (!(dependencia.setMisionesYFunciones() > 0)) {
            Advisor.messageBox("Ocurrio un error al grabar los datos!", "Error");
        }
    }

    public void setDependencia(Dependencia _dependencia) {
        dependencia = _dependencia;
        tfiDependencia.setValue(dependencia.getNombre());
        tpMisiones.setText(dependencia.getMisiones());
        tpMisiones.setCaretPosition(0);
        tpFunciones.setText(dependencia.getFunciones());
        tpFunciones.setCaretPosition(0);
    }
}
