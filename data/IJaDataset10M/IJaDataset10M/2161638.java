package pe.com.bn.sach.dao.impl;

import java.util.List;
import com.ibatis.sqlmap.client.SqlMapClient;
import pe.com.bn.sach.domain.Bnchf48AsigChRolUsr;
import pe.com.bn.sach.dao.AsigChRolUsr48DAO;
import org.apache.log4j.Logger;

public class AsigChRolUsr48Impl implements AsigChRolUsr48DAO {

    private static Logger depurador = Logger.getLogger(AsigChRolUsr48Impl.class.getName());

    protected SqlMapClient sqlMap = null;

    public void setSqlMap(SqlMapClient sqlMap) {
        this.sqlMap = sqlMap;
    }

    public List ListaAsigUpRol(Bnchf48AsigChRolUsr bnchf48AsigChRolUsr) throws Exception {
        depurador.info(" ListaAsigUpRol");
        depurador.info(" F49_ID_UNID_PROC = " + bnchf48AsigChRolUsr.getId().getBnchf14CentroHipotecario().getF14IdHipotecario());
        return sqlMap.queryForList("sqlAsigChRolUsr.listaAsigChRolUsr", bnchf48AsigChRolUsr);
    }

    public Bnchf48AsigChRolUsr AsigRol(Bnchf48AsigChRolUsr bnchf48AsigChRolUsr) throws Exception {
        return (Bnchf48AsigChRolUsr) sqlMap.queryForObject("sqlAsigChRolUsr.buscarAsign", bnchf48AsigChRolUsr);
    }
}
