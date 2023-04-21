package cn.ekuma.erp.client.ds;

import com.smartgwt.client.data.fields.DataSourceBooleanField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import cn.ekuma.epos.core.client.AbstractBeanGWTDataSource;
import cn.ekuma.epos.online.bean.ClientAppUser;
import cn.ekuma.epos.online.db.table.I_ClientAppUser;

public class ClientAppUserDS extends AbstractBeanGWTDataSource<ClientAppUser> {

    public ClientAppUserDS(String id) {
        super(id);
        DataSourceTextField pkField = new DataSourceTextField(I_ClientAppUser.ID);
        pkField.setHidden(true);
        pkField.setPrimaryKey(true);
        DataSourceTextField nameField = new DataSourceTextField(I_ClientAppUser.NAME);
        DataSourceTextField cardField = new DataSourceTextField(I_ClientAppUser.CARD);
        DataSourceTextField numberField = new DataSourceTextField(I_ClientAppUser.NUMBERID);
        DataSourceTextField roleField = new DataSourceTextField(I_ClientAppUser.ROLE);
        DataSourceBooleanField visibleField = new DataSourceBooleanField(I_ClientAppUser.VISIBLE);
        setFields(pkField, nameField, cardField, numberField, roleField, visibleField);
    }

    @Override
    public ListGridRecord coverRecordForBean(ClientAppUser bean) {
        ListGridRecord record = new ListGridRecord();
        record.setAttribute(I_ClientAppUser.ID, bean.getId());
        record.setAttribute(I_ClientAppUser.CLIENTID, bean.getClientId());
        record.setAttribute(I_ClientAppUser.NAME, bean.getName());
        record.setAttribute(I_ClientAppUser.CARD, bean.getCard());
        record.setAttribute(I_ClientAppUser.NUMBERID, bean.getNumber());
        record.setAttribute(I_ClientAppUser.ROLE, bean.getRole());
        record.setAttribute(I_ClientAppUser.VISIBLE, bean.isVisable());
        return record;
    }

    @Override
    public ClientAppUser recoredForRecord(ListGridRecord record) {
        ClientAppUser obj = new ClientAppUser();
        obj.setKey(record.getAttribute(I_ClientAppUser.ID));
        obj.setClientId(record.getAttribute(I_ClientAppUser.CLIENTID));
        obj.setM_sName(record.getAttribute(I_ClientAppUser.NAME));
        obj.setM_sCard(record.getAttribute(I_ClientAppUser.CARD));
        obj.setM_sNumber(record.getAttribute(I_ClientAppUser.NUMBERID));
        obj.setM_sRole(record.getAttribute(I_ClientAppUser.ROLE));
        obj.setIsVisable(record.getAttributeAsBoolean(I_ClientAppUser.VISIBLE));
        return obj;
    }
}
