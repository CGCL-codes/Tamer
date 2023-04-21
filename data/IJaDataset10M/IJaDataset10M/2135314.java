package org.humboldt.cassia.zk.vo;

import java.util.ArrayList;
import java.util.Collection;
import org.humboldt.cassia.core.jdo.AtributosEstandar;
import org.humboldt.cassia.core.jdo.Estandar;

public class TreeEstandar extends Estandar {

    private Collection<TreeAtributoEstandar> treeAtributos;

    public TreeEstandar(Estandar estandar) {
        this.setId(estandar.getId());
        this.setNombre(estandar.getNombre());
        if (estandar.getAtributos() != null) {
            treeAtributos = new ArrayList<TreeAtributoEstandar>();
            for (AtributosEstandar atributoHijo : estandar.getAtributos()) {
                TreeAtributoEstandar treeatributo = new TreeAtributoEstandar(atributoHijo);
                treeAtributos.add(treeatributo);
            }
        }
    }

    /**
	 * @return the treeAtributos
	 */
    public Collection<TreeAtributoEstandar> getTreeAtributos() {
        return treeAtributos;
    }

    /**
	 * @param treeAtributos the treeAtributos to set
	 */
    public void setTreeAtributos(Collection<TreeAtributoEstandar> treeAtributos) {
        this.treeAtributos = treeAtributos;
    }
}
