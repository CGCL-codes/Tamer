package com.proyecto.tropero.core.service.model.Impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import org.apache.commons.dbcp.BasicDataSource;
import com.proyecto.tropero.core.application.TroperoApplicationContext;
import com.proyecto.tropero.core.bd.DAO.model.Interface.IAlertaDAO;
import com.proyecto.tropero.core.excepciones.BusinessException;
import com.proyecto.tropero.core.service.GenericServiceImpl;
import com.proyecto.tropero.core.service.model.Interface.IAlertaService;
import com.proyecto.tropero.gui.desktop.SplashWindowAvisos;

public class AlertaServiceImpl extends GenericServiceImpl implements IAlertaService {

    public IAlertaDAO getDao() {
        return (IAlertaDAO) super.getDao();
    }

    @Override
    public void actualizarAlertas() {
        BasicDataSource dataSource = (BasicDataSource) TroperoApplicationContext.getBean("dataSource");
        Connection con = null;
        try {
            con = dataSource.getConnection();
            CallableStatement procedure = con.prepareCall("{call PKG_ALERTAS.prc_verificarAlertas()}");
            procedure.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new BusinessException("Error al actualizar las Alertas");
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        SplashWindowAvisos.getInstanceForRefresh().refrescaAvisos();
    }
}
