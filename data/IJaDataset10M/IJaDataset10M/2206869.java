package cn.ekuma.epos.online.datalogic.define.dao;

import cn.ekuma.epos.datalogic.define.dao.ProductBomDAO;
import cn.ekuma.epos.online.bean.ClientProductBom;
import cn.ekuma.epos.online.datalogic.define.ClientBaseDAO;
import com.openbravo.data.loader.I_Session;

public class ClientProductBomDAO extends ClientBaseDAO<ClientProductBom, ProductBomDAO> {

    public ClientProductBomDAO(I_Session s) {
        super(s, new ProductBomDAO(s));
    }

    @Override
    public Class getSuportClass() {
        return ClientProductBom.class;
    }

    @Override
    protected ClientProductBom createNewBean() {
        return new ClientProductBom();
    }
}
