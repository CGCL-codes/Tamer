package gnu.chu.controles;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import gnu.chu.interfaces.CEditable;
import gnu.chu.interfaces.CQuery;
import gnu.chu.interfaces.CContenedor;

public class CPanel extends JPanel implements CEditable, CQuery, CContenedor {

    boolean swAddFocoListen = false;

    private boolean swBuscaDisa = false;

    Hashtable htButton = new Hashtable();

    /**
    * Crea un Nuevo VPanel
    */
    public CPanel(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
    }

    /**
        *  	*	Crea un nuevo VPanel con la especificaciones LayoutManager
    */
    public CPanel(LayoutManager layout) {
        super(layout, true);
    }

    /**
        *  	*	Crea un Nuevo VPanel con un FlowLayout. Si <<code>isDoubleBuffered</code>
    * es true, el VPanel usa un doble Buffer
    */
    public CPanel(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
    }

    /**
        *  	*	Crea un VPanel con un doble Buffer y un flow layout
    **/
    public CPanel() {
        super();
    }

    /**
        *  	*	Pone enabled o disable todos los Controles que contenga
    */
    private static boolean[] listaEn = new boolean[100];

    boolean firstTime = true;

    public void setDefButton(AbstractButton b) {
        setButton(java.awt.event.KeyEvent.VK_ENTER, b);
        setButton(java.awt.event.KeyEvent.VK_F4, b);
    }

    public void setEscButton(AbstractButton b) {
        setButton(java.awt.event.KeyEvent.VK_ESCAPE, b);
    }

    public void setAltButton(AbstractButton b) {
        setButton(java.awt.event.KeyEvent.VK_F2, b);
    }

    /**
   * Cuando el Boton por defecto esta disable antes lo pulsaba.
   * Si llamamos con true, buscara al panel padre para coger el boton por Defecto.
   * A todos los efectos si swDisa=true y el boton esta disabled es como
   * si no habriamos puesto boton por defecto.
   * @default false
   */
    public void setDefButtonDisable(boolean swDisa) {
        swBuscaDisa = swDisa;
    }

    public boolean getDefButtonDisable() {
        return swBuscaDisa;
    }

    public AbstractButton getDefButton() {
        return getButton(java.awt.event.KeyEvent.VK_ENTER);
    }

    public AbstractButton getEscButton() {
        return getButton(java.awt.event.KeyEvent.VK_ESCAPE);
    }

    public AbstractButton getAltButton() {
        return getButton(java.awt.event.KeyEvent.VK_F2);
    }

    public void setButton(int tecla, AbstractButton boton) {
        if (boton == null) htButton.remove("" + tecla); else htButton.put("" + tecla, boton);
    }

    public void setEnabled(boolean b) {
        super.setEnabled(b);
        setEnabledParent(b);
    }

    public void setEnabledParent(boolean enabled) {
        if (enabled && !isEnabled()) return;
        Component[] lista = this.getComponents();
        int n = this.getComponentCount();
        for (int i = 0; i < n; i++) {
            try {
                if (lista[i] instanceof CEditable) ((CEditable) lista[i]).setEnabledParent(enabled);
            } catch (Exception k) {
            }
        }
    }

    /**
  * Funcion que me pone en blanco los controles que implementan CEditable
  * a un valor por defecto
  */
    public void resetTexto() {
        Component[] lista = this.getComponents();
        int n = this.getComponentCount();
        for (int i = 0; i < n; i++) {
            try {
                if (lista[i] instanceof CEditable) ((CEditable) lista[i]).resetTexto();
            } catch (Exception k) {
            }
        }
        return;
    }

    public void resetCambio() {
        Component[] lista = this.getComponents();
        int n = this.getComponentCount();
        for (int i = 0; i < n; i++) {
            try {
                if (lista[i] instanceof CEditable) {
                    ((CEditable) lista[i]).resetCambio();
                }
            } catch (Exception k) {
            }
        }
        return;
    }

    public boolean hasCambio() {
        Component[] lista = this.getComponents();
        int n = this.getComponentCount();
        for (int i = 0; i < n; i++) {
            try {
                if (lista[i] instanceof CEditable) {
                    if (((CEditable) lista[i]).hasCambio()) return true;
                }
            } catch (Exception k) {
            }
        }
        return false;
    }

    /**
  * Funcion que me pone en el modo query especificado todos los
  * componentes que contiene y que implementen VEditable
  **/
    public void setQuery(boolean b) {
        Component[] lista = this.getComponents();
        int n = this.getComponentCount();
        for (int i = 0; i < n; i++) {
            try {
                if (lista[i] instanceof CQuery) {
                    ((CQuery) lista[i]).setQuery(b);
                }
            } catch (Exception k) {
            }
        }
        revalidate();
        repaint();
        return;
    }

    /**
  * @return true si alguno de los componentes de el Tiene un Error.
  *         false si Ninguno esta en Error.
  */
    public Component getErrorConf() {
        Component[] lista = this.getComponents();
        int n = this.getComponentCount();
        for (int i = 0; i < n; i++) {
            try {
                if (Class.forName("gnu.chu.interfaces.CEditable").isAssignableFrom(lista[i].getClass())) {
                    if (((gnu.chu.interfaces.CEditable) lista[i]).getErrorConf() != null) return lista[i];
                }
            } catch (Exception k) {
            }
        }
        return null;
    }

    public void setText(String s) {
    }

    public String getText() {
        return "";
    }

    public String getStrQuery() {
        return "";
    }

    public boolean getQuery() {
        return false;
    }

    public AbstractButton getButton(int tecla) {
        return estatic.getButtonPanel(tecla, htButton, this);
    }

    public void getVectorQuery(Vector v) {
        Component[] lista = this.getComponents();
        int nEl = this.getComponentCount();
        for (int n = 0; n < nEl; n++) {
            try {
                if (lista[n] instanceof gnu.chu.interfaces.CQuery && lista[n].isEnabled()) if (((gnu.chu.interfaces.CQuery) lista[n]).getColumnaAlias() != null && !((gnu.chu.interfaces.CQuery) lista[n]).getColumnaAlias().equals("")) v.add(((gnu.chu.interfaces.CQuery) lista[n]).getStrQuery());
            } catch (Exception k) {
            }
        }
    }

    public String getColumnaAlias() {
        return "";
    }
}
