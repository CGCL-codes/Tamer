package mx.ipn.presentacion.radioperadora;

import org.apache.axis2.Constants;
import mx.ipn.persistencia.FabricaDeDAOs;
import mx.ipn.persistencia.mysql.FabricaDeDAOsMySQL;
import mx.ipn.presentacion.radioperadora.ui.*;
import com.trolltech.qt.gui.*;
import java.util.*;
import mx.ipn.to.*;

public class DialogRegistrarEconomico extends QDialog {

    Ui_DialogAEconomico ui = new Ui_DialogAEconomico();

    FabricaDeDAOs fabrica;

    public DialogRegistrarEconomico() {
        ui.setupUi(this);
        fabrica = FabricaDeDAOs.getFabricaDeDAOs(1);
        ArrayList<PersonaTO> arregloPersonas = fabrica.getPersonaDAO().selectPersonas();
        for (PersonaTO p : arregloPersonas) ui.comboBoxEconomico.addItem(p.getApellidoPaterno() + " " + p.getApellidoMaterno() + " " + p.getNombre(), p);
    }

    public DialogRegistrarEconomico(QWidget parent) {
        ui.setupUi(this);
    }

    private void on_pushButtonOk_clicked(Boolean b) {
        EconomicoTO to = new EconomicoTO();
        to.setActivo(true);
        to.setPersonaTO((PersonaTO) ui.comboBoxEconomico.itemData(ui.comboBoxEconomico.currentIndex()));
        fabrica.getEconomicoDAO().insertEconomico(to);
    }
}
