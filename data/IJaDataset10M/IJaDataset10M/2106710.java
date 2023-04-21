package br.com.mcampos.controller.admin.tables.core;

import br.com.mcampos.dto.core.SimpleTableDTO;
import br.com.mcampos.dto.security.AuthenticationDTO;
import java.util.List;
import org.zkoss.zul.AbstractListModel;

public class SimpleTableListModel extends AbstractListModel {

    private List<SimpleTableDTO> list;

    public SimpleTableListModel(List<SimpleTableDTO> list) {
        super();
        this.list = list;
    }

    public Object getElementAt(int index) {
        if (list == null) return null;
        if (index >= list.size()) return null;
        return list.get(index);
    }

    public int getSize() {
        return list != null ? list.size() : 0;
    }
}
