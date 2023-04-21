package org.humboldt.cassia.zk.vo;

import java.util.ArrayList;
import java.util.Collection;
import org.humboldt.cassia.core.jdo.AtributosEstandar;
import org.zkoss.zul.Treeitem;

public class TreeAtributoEstandar extends AtributosEstandar {

    private Treeitem treeitem;

    private Collection<TreeAtributoEstandar> treeHijos;

    private boolean selected;

    public TreeAtributoEstandar(AtributosEstandar atributo) {
        this.setId(atributo.getId());
        this.setNombre(atributo.getNombre());
        this.setElemento(atributo.getElemento());
        this.setEstandar(atributo.getEstandar());
        this.setMaxocurrs(atributo.getMaxocurrs());
        this.setMinocurrs(atributo.getMinocurrs());
        this.setTipo(atributo.getTipo());
        if (atributo.getHijos() != null) {
            treeHijos = new ArrayList<TreeAtributoEstandar>();
            for (AtributosEstandar atributoHijo : atributo.getHijos()) {
                treeHijos.add(new TreeAtributoEstandar(atributoHijo));
            }
        }
    }

    /**
	 * @return the treeitem
	 */
    public Treeitem getTreeitem() {
        return treeitem;
    }

    /**
	 * @param treeitem the treeitem to set
	 */
    public void setTreeitem(Treeitem treeitem) {
        this.treeitem = treeitem;
    }

    /**
	 * @return the treeHijos
	 */
    public Collection<TreeAtributoEstandar> getTreeHijos() {
        return treeHijos;
    }

    /**
	 * @param treeHijos the treeHijos to set
	 */
    public void setTreeHijos(Collection<TreeAtributoEstandar> treeHijos) {
        this.treeHijos = treeHijos;
    }

    /**
	 * @return the selected
	 */
    public boolean isSelected() {
        if (treeitem != null) selected = treeitem.isSelected();
        return selected;
    }

    /**
	 * @param selected the selected to set
	 */
    public void setSelected(boolean selected) {
        this.selected = selected;
        if (treeitem != null) treeitem.setSelected(selected);
    }

    public boolean isChanged() {
        if (treeitem != null) if (selected != treeitem.isSelected()) return true;
        return false;
    }
}
