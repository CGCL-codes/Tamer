package vista.escritorio.componentes.modelos;

import javax.swing.DefaultComboBoxModel;

/**
 * Modelo para JComboBox. Evita que se añadan elementos repetidos. 
 * Ademas muestra la cadena "mantener"cuando hay varios elementos
 * en el combo.
 * 
 * @author Rodrigo Villamil Perez
 */
public class ModeloParaGFMComboBoxSinRepetidos extends DefaultComboBoxModel {

    private static final long serialVersionUID = 1L;

    private final String mantener = "<mantener>";

    private boolean cadenaMantener = false;

    public ModeloParaGFMComboBoxSinRepetidos() {
    }

    /**
     * Aniade un elemento a la lista de elementos del JComboNox sin repetir. 
     * Un elemento es igual a otro cuando su metodo toString() retorna lo mismo.
     */
    @Override
    public void addElement(Object objeto) {
        if (getIndexOf(objeto) < 0) {
            super.addElement(objeto);
            if (this.getSize() == 1) {
                cadenaMantener = false;
                setSelectedItem(objeto);
            }
            if (this.getSize() > 1 && !cadenaMantener) {
                super.insertElementAt(mantener, 0);
                setSelectedItem(mantener);
                cadenaMantener = true;
            }
        }
    }

    /**
     * Elimina todos los elementos de la lista del JComboBox.
     */
    @Override
    public void removeAllElements() {
        cadenaMantener = false;
        super.removeAllElements();
    }
}
