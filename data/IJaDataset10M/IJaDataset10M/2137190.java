package br.com.mcampos.controller.anoto;

import br.com.mcampos.controller.admin.tables.BasicListController;
import br.com.mcampos.controller.anoto.model.PenIdComparator;
import br.com.mcampos.controller.anoto.renderer.AnotoPenListRenderer;
import br.com.mcampos.dto.anoto.PenDTO;
import br.com.mcampos.ejb.cloudsystem.anoto.pen.facade.AnotoPenFacade;
import br.com.mcampos.exception.ApplicationException;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Textbox;

public class AnotoPenController extends BasicListController<PenDTO> {

    private Textbox editId;

    private Label recordId;

    private Textbox editDescription;

    private Label recordDescription;

    private AnotoPenFacade session;

    private Listheader headerPenId;

    private Label labelPenTitle;

    private Label labelCode;

    private Label labelDescription;

    private Label labelEditCode;

    private Label labelEditDescription;

    public AnotoPenController() {
        super();
    }

    protected AnotoPenFacade getSession() {
        if (session == null) session = (AnotoPenFacade) getRemoteSession(AnotoPenFacade.class);
        return session;
    }

    @Override
    protected void showRecord(PenDTO record) {
        if (record != null) {
            recordId.setValue(record.getId());
            recordDescription.setValue(record.getDescription());
        } else {
            recordId.setValue("");
            recordDescription.setValue("");
        }
    }

    @Override
    protected Object createNewRecord() {
        return new PenDTO();
    }

    protected PenDTO copyTo(PenDTO dto) {
        dto.setId(editId.getValue());
        dto.setDescription(editDescription.getValue().trim());
        return dto;
    }

    @Override
    public ListitemRenderer getRenderer() {
        return new AnotoPenListRenderer();
    }

    @Override
    protected List getRecordList() throws ApplicationException {
        showRecord(null);
        return getSession().getPens(0, 300);
    }

    @Override
    protected void delete(Object currentRecord) throws ApplicationException {
        getSession().delete(getLoggedInUser(), (PenDTO) currentRecord);
    }

    @Override
    protected Object saveRecord(Object record) {
        PenDTO dto = (PenDTO) record;
        copyTo(dto);
        return dto;
    }

    @Override
    protected void clearRecordInfo() {
        editId.setValue("");
        editDescription.setValue("");
    }

    @Override
    protected void prepareToInsert() {
        clearRecordInfo();
        editId.setFocus(true);
        editId.setDisabled(false);
    }

    @Override
    protected Object prepareToUpdate(Object currentRecord) {
        PenDTO dto = (PenDTO) currentRecord;
        editId.setValue(dto.getId());
        editDescription.setValue(dto.getDescription());
        editDescription.setFocus(true);
        editId.setDisabled(true);
        return dto;
    }

    @Override
    protected void persist(Object e) throws ApplicationException {
        if (isAddNewOperation()) getSession().add(getLoggedInUser(), (PenDTO) e); else getSession().update(getLoggedInUser(), (PenDTO) e);
    }

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        headerPenId.setSortAscending(new PenIdComparator(true));
        headerPenId.setSortDescending(new PenIdComparator(false));
        setLabel(headerPenId);
        setLabel(labelPenTitle);
        setLabel(labelCode);
        setLabel(labelDescription);
        setLabel(labelEditCode);
        setLabel(labelEditDescription);
    }
}
