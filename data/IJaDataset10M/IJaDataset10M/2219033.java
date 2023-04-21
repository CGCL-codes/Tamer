package br.com.mcampos.controller.admin.login;

import br.com.mcampos.controller.core.LoggedBaseController;
import br.com.mcampos.dto.user.login.ListLoginDTO;
import br.com.mcampos.ejb.cloudsystem.user.login.LoginFacadeSession;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Column;
import org.zkoss.zul.Grid;
import org.zkoss.zul.ListModelList;

public class AdminLoginController extends LoggedBaseController {

    private LoginFacadeSession session;

    private Grid gridData;

    private Column listHeaderCode;

    private Column listHeaderName;

    private Column headerStatus;

    AdminLoginController(char c) {
        super(c);
    }

    public AdminLoginController() {
        super();
    }

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        gridData.setRowRenderer(new LoginRowRenderer());
        setLabels();
        setSortColumns();
        refresh();
    }

    private void refresh() {
        List<ListLoginDTO> logins;
        try {
            logins = getSession().getAll(getLoggedInUser());
            ListModelList model = (ListModelList) gridData.getModel();
            if (model == null) {
                model = new ListModelList(new ArrayList<ListLoginDTO>(), true);
                gridData.setModel(model);
                model = (ListModelList) gridData.getModel();
            }
            model.clear();
            model.addAll(logins);
        } catch (Exception e) {
            showErrorMessage(e.getMessage());
        }
    }

    @Override
    protected String getPageTitle() {
        return getLabel("adminLoginController");
    }

    private LoginFacadeSession getSession() {
        if (session == null) session = (LoginFacadeSession) getRemoteSession(LoginFacadeSession.class);
        return session;
    }

    private void setLabels() {
        setLabel(listHeaderCode);
        setLabel(listHeaderName);
        setLabel(headerStatus);
    }

    private void setSortColumns() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        listHeaderCode.setSortAscending(new CodeComparator(true));
        listHeaderCode.setSortDescending(new CodeComparator(false));
        listHeaderName.setSort("auto");
        headerStatus.setSort("auto");
    }
}
