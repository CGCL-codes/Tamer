package ru.arriah.servicedesk.ejb;

import java.util.Collection;
import javax.ejb.EJBLocalObject;
import ru.arriah.servicedesk.bean.ClientBean;
import ru.arriah.servicedesk.bean.ClientTypeBean;
import ru.arriah.servicedesk.bean.IntWrapper;

public interface ClientManagerLocalObject extends EJBLocalObject {

    public Collection<ClientBean> selectClients();

    public Collection<ClientBean> selectClients(String orderBy, String order, int page, int itemsPerPage, IntWrapper records);

    public Collection<ClientBean> selectClients(String orderBy, String order, int page, int itemsPerPage, IntWrapper records, String status);

    public String getUserPassword(int id);

    public ClientBean selectClientByLogin(String login);

    public boolean deleteClient(int id);

    public boolean restoreClient(int id);

    public Collection<ClientTypeBean> selectClientTypes();

    public boolean addClient(ClientBean clientBean);

    public ClientBean selectClient(int clientId);

    public Collection<ClientBean> selectClientsByOrganization(int organizationId);

    public Collection<ClientBean> selectClientsByDepartment(int departmentId);

    public void updateClient(ClientBean clientBean);

    public boolean updateClientByDispatcher(ClientBean clientBean);

    public int authorize(String login, String password);
}
