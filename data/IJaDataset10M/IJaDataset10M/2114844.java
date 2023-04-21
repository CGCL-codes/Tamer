package org.humboldt.cassia.zk.render;

import org.humboldt.cassia.zk.vo.TreeAtributoEstandar;
import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;

public class ArbolEstandarRender implements TreeitemRenderer {

    public void render(Treeitem treeitem, Object data) throws Exception {
        Treerow row;
        if (treeitem.getTreerow() == null) {
            row = new Treerow();
            row.setParent(treeitem);
        } else {
            row = treeitem.getTreerow();
            row.getChildren().clear();
        }
        TreeAtributoEstandar atributosEstandar = (TreeAtributoEstandar) data;
        if (treeitem.getTree().getAttribute("atributoPrincipal") != null) {
            Long idAtrPrincipal = (Long) treeitem.getTree().getAttribute("atributoPrincipal");
            if (idAtrPrincipal.longValue() == atributosEstandar.getId().longValue()) atributosEstandar.setSelected(true);
        }
        treeitem.setSelected(atributosEstandar.isSelected());
        atributosEstandar.setTreeitem(treeitem);
        treeitem.getTreerow().appendChild(new Treecell(atributosEstandar.getNombre()));
        treeitem.getTreerow().appendChild(new Treecell(atributosEstandar.getTipo()));
        Intbox textBoxMin = new Intbox((int) atributosEstandar.getMinocurrs());
        textBoxMin.setId(atributosEstandar.getId() + "_min");
        textBoxMin.setWidth("50px");
        Treecell cellTextMin = new Treecell();
        cellTextMin.appendChild(textBoxMin);
        treeitem.getTreerow().appendChild(cellTextMin);
        Intbox textBoxMax = new Intbox((int) atributosEstandar.getMaxocurrs());
        textBoxMax.setId(atributosEstandar.getId() + "_max");
        textBoxMax.setWidth("50px");
        Treecell cellTextMax = new Treecell();
        cellTextMax.appendChild(textBoxMax);
        treeitem.getTreerow().appendChild(cellTextMax);
        Combobox comboCond = new Combobox();
        comboCond.setId(atributosEstandar.getId() + "_con");
        comboCond.appendItem(Labels.getLabel("lbl_perfil_condicion_ninguna"));
        comboCond.appendItem(Labels.getLabel("lbl_perfil_condicion_obligatorio"));
        comboCond.appendItem(Labels.getLabel("lbl_perfil_condicion_condicional"));
        comboCond.appendItem(Labels.getLabel("lbl_perfil_condicion_opcional"));
        comboCond.setReadonly(true);
        comboCond.setSelectedIndex(0);
        Treecell cellTextCond = new Treecell();
        cellTextCond.appendChild(comboCond);
        treeitem.getTreerow().appendChild(cellTextCond);
    }
}
