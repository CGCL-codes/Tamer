package br.com.mcampos.controller.admin.resale;

import br.com.mcampos.controller.admin.tables.BasicListController;
import br.com.mcampos.controller.user.client.ClientListRenderer;
import br.com.mcampos.dto.resale.ResaleDTO;
import br.com.mcampos.dto.user.ClientDTO;
import br.com.mcampos.ejb.cloudsystem.resale.facade.ResaleFacade;
import br.com.mcampos.exception.ApplicationException;
import java.security.InvalidParameterException;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Textbox;

public class ResaleController extends BasicListController<ResaleDTO> {

    private ResaleFacade session;

    private Listheader listHeaderName;

    private Listbox listClientCompany;

    private Label labelCode;

    private Label labelName;

    private Label labelEditCode;

    private Label labelEditName;

    private Label recordCode;

    private Label recordName;

    private Textbox editCode;

    private Bandbox editName;

    public ResaleController() {
        super();
    }

    protected void showRecord(ResaleDTO record) {
        recordCode.setValue(record.getCode());
        recordName.setValue(record.getResale().getClient().getNickName());
    }

    protected void clearRecordInfo() {
        recordCode.setValue("");
        recordName.setValue("");
    }

    protected List getRecordList() throws ApplicationException {
        return getSession().getAll(getLoggedInUser());
    }

    protected ListitemRenderer getRenderer() {
        return new ResaleListRenderer();
    }

    protected void delete(Object currentRecord) throws ApplicationException {
        getSession().delete(getLoggedInUser(), ((ResaleDTO) currentRecord).getSequence());
    }

    protected Object saveRecord(Object currentRecord) {
        ResaleDTO dto = (ResaleDTO) currentRecord;
        dto.setCode(editCode.getValue());
        ClientDTO client = getSelectedClient();
        if (client == null) throw new InvalidParameterException("Client inválido. Selecione um cliente");
        dto.setResale(client);
        return currentRecord;
    }

    protected void prepareToInsert() {
        editCode.setValue("");
        editName.setValue("");
        listClientCompany.clearSelection();
    }

    protected Object prepareToUpdate(Object currentRecord) {
        ResaleDTO dto = (ResaleDTO) currentRecord;
        editCode.setValue(dto.getCode());
        editName.setValue(dto.getResale().getClient().getNickName());
        return dto;
    }

    protected Object createNewRecord() {
        return new ResaleDTO();
    }

    protected void persist(Object e) throws ApplicationException {
        if (isAddNewOperation()) getSession().add(getLoggedInUser(), (ResaleDTO) e); else getSession().update(getLoggedInUser(), (ResaleDTO) e);
    }

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        listHeaderName.setLabel(getLabel("resale"));
        labelEditCode.setValue(listHeaderName.getLabel());
        setLabel(labelEditName);
        labelName.setValue(labelEditName.getValue());
        labelCode.setValue(labelEditCode.getValue());
        loadClients();
    }

    private void loadClients() {
        listClientCompany.setItemRenderer(new ClientListRenderer());
        ListModelList model = (ListModelList) listClientCompany.getModel();
        if (model == null) {
            model = new ListModelList();
            listClientCompany.setModel(model);
        }
        model.clear();
        try {
            List<ClientDTO> list = getSession().getCompanies(getLoggedInUser());
            model.addAll(list);
        } catch (Exception e) {
            showErrorMessage(e.getMessage());
        }
    }

    @Override
    protected String getPageTitle() {
        return getLabel("resaleTitle");
    }

    public ResaleFacade getSession() {
        if (session == null) session = (ResaleFacade) getRemoteSession(ResaleFacade.class);
        return session;
    }

    public void onSelect$listClientCompany() {
        ClientDTO dto = getSelectedClient();
        editName.setValue(dto.getClient().getNickName());
        editName.setOpen(false);
    }

    private ClientDTO getSelectedClient() {
        if (listClientCompany.getSelectedItem() == null) return null;
        return (ClientDTO) listClientCompany.getSelectedItem().getValue();
    }
}
