package com.celiosilva.simbanc.controller.util;

import com.celiosilva.simbanc.beans.enums.EstadoFederal;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author celio@celiosilva.com
 */
public class ModelosComboBox {

    public static final ComboBoxModel getModelEstadoFederal() {
        return new DefaultComboBoxModel(EstadoFederal.values());
    }
}
