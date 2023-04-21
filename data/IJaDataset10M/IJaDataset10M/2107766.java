package br.com.mcampos.controller.admin.tables.documenttype;

import br.com.mcampos.controller.commom.AbstractLisrRenderer;
import br.com.mcampos.dto.user.attributes.DocumentTypeDTO;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

public class DocumentTypeListRenderer extends AbstractLisrRenderer {

    public DocumentTypeListRenderer() {
        super();
    }

    public void render(Listitem item, Object data) throws Exception {
        DocumentTypeDTO dto = (DocumentTypeDTO) data;
        item.setValue(data);
        Listcell cellId, cellDescription, cellMask;
        createCells(item);
        cellId = (Listcell) item.getChildren().get(0);
        cellDescription = (Listcell) item.getChildren().get(1);
        cellMask = (Listcell) item.getChildren().get(2);
        if (cellId != null) cellId.setLabel(dto.getId().toString());
        if (cellDescription != null) cellDescription.setLabel(dto.getDescription());
        if (cellMask != null) cellMask.setLabel(dto.getMask());
    }
}
