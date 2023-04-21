package org.humboldt.cassia.zk.model;

import java.util.ArrayList;
import org.humboldt.cassia.core.FacadeConsultasUsuarioCassiaCore;
import org.humboldt.cassia.core.jdo.RecursosUsuario;
import org.humboldt.cassia.core.jdo.Usuario;
import org.humboldt.cassia.zk.util.SessionUtil;
import org.zkoss.zul.AbstractTreeModel;

public class RecursosUsuariosModel extends AbstractTreeModel {

    ArrayList cllRecursos = new ArrayList();

    public RecursosUsuariosModel(Object root) {
        super(root);
        FacadeConsultasUsuarioCassiaCore facadeUsuarios = SessionUtil.getFacadeConsultasUsuarioCassiaCore();
        cllRecursos.addAll(facadeUsuarios.consultarRecursosUsuario(((Usuario) root).getId()));
    }

    public Object getChild(Object parent, int index) {
        if (parent instanceof RecursosUsuario) {
            return null;
        } else if (parent instanceof Usuario) {
            return cllRecursos;
        } else {
            return cllRecursos.get(index);
        }
    }

    public int getChildCount(Object parent) {
        if (parent instanceof RecursosUsuario) {
            return 0;
        } else if (parent instanceof Usuario) {
            return 1;
        }
        return cllRecursos.size();
    }

    public boolean isLeaf(Object node) {
        if (node instanceof RecursosUsuario && ((RecursosUsuario) node).getConjunto() != null) {
            return true;
        }
        return false;
    }
}
