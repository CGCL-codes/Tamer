package es.addlink.tutormates.equationEditor.Operators;

import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Control;
import es.addlink.tutormates.equationEditor.Exceptions.ComponentEditorException;
import es.addlink.tutormates.equationEditor.Exceptions.EditorException;
import es.addlink.tutormates.equationEditor.Formats.Category;
import es.addlink.tutormates.equationEditor.Manager.Manager;

/**
 * Clase encargada de añadir un exponente.
 * 
 * @author Ignacio Celaya Sesma
 */
public class Exponent extends UnaryOperator {

    /**
	 * Constructor
	 * 
	 * @param cuadriculaPadre Cuadrícula donde se aloja el componente.
	 * @throws ComponentEditorException
	 */
    public Exponent(Manager manager, GridExpression cuadriculaPadre) throws ComponentEditorException {
        super(manager, cuadriculaPadre, "exponent", 52);
        super.setTipoComponente(Category.UnaryComplexType);
        try {
            super.getCmpTodo().setLayout(new FormLayout());
            crearCuadriculaEdicion1("", true);
            setBackground(super.getColorFondo());
            super.getCuadriculaEdicion1().addControlListener(new ControlAdapter() {

                @Override
                public void controlResized(ControlEvent e) {
                    try {
                        ajustar();
                    } catch (Exception ex) {
                    }
                }
            });
            setMenuEliminar();
            super.setPosicionCentral(getPosicionCentral());
            ajustar();
        } catch (EditorException ex) {
            throw new ComponentEditorException(ex);
        } catch (Exception ex) {
            throw new ComponentEditorException("Error en la construcción de un exponente.", ex);
        }
    }

    /**
	 * Modifica el tamaño del componente. Ajusta el tamaño del símbolo y la longitud de la línea superior.
	 * 
	 * @throws ComponentEditorException
	 */
    private void ajustar() throws ComponentEditorException {
        try {
            super.getCuadriculaEdicion1().pack();
            super.getCmpTodo().pack();
            super.getCuadriculaPadre().pack();
            super.setPosicionCentral(getPosicionCentral());
        } catch (EditorException ex) {
            throw new ComponentEditorException(ex);
        } catch (Exception ex) {
            throw new ComponentEditorException("Error en el ajuste de un exponente.", ex);
        }
    }

    @Override
    protected void cambioPosicionCentralEnCuadricula() {
    }

    @Override
    public void deseleccionar() throws ComponentEditorException {
        super.deseleccionar();
    }

    @Override
    public int getPosicionCentral() throws ComponentEditorException {
        try {
            if (super.getCuadriculaEdicion1() != null) {
                Control[] children = super.getCuadriculaEdicion1().getCuadriculaSuperior().getChildren();
                int i = 0;
                boolean encontrado = false;
                while (i < children.length && !encontrado) {
                    if (((Operator) children[i]) == this) encontrado = true; else i++;
                }
                int devuelta = 0;
                if (i > 0) {
                    Operator compAnterior = (Operator) children[i - 1];
                    devuelta = this.getCmpTodo().getSize().y + (compAnterior.getPosicionCentral() - compAnterior.getLocation().y) - 8;
                }
                return devuelta;
            } else {
                return 0;
            }
        } catch (EditorException ex) {
            throw new ComponentEditorException(ex);
        } catch (Exception ex) {
            throw new ComponentEditorException("Error en el cálculo de la posición central de un exponente.", ex);
        }
    }

    @Override
    public void seleccionar() throws ComponentEditorException {
        super.seleccionar();
        super.getCuadriculaEdicion1().seleccionar(super.getColorFondoSeleccionado());
    }

    @Override
    public void setAltura(int altura) throws ComponentEditorException {
        try {
            if (altura != super.getCmpTodo().getLocation().y) {
                super.getCmpTodo().setLocation(0, altura);
            }
        } catch (Exception ex) {
            throw new ComponentEditorException("Error en el cálculo de la posición central de un exponente.", ex);
        }
    }

    @Override
    public void setMenuEliminar() throws ComponentEditorException {
        super.setMenuEliminar();
    }
}
