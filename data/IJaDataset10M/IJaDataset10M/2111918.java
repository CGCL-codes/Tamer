package pe.com.bn.sach.dao.impl;

import com.ibatis.sqlmap.client.SqlMapClient;
import pe.com.bn.sach.dao.DocumentoDAO;
import pe.com.bn.sach.domain.Bnchf15DocumentoTramite;

/**
 * @author Vilia Rios
 *
 * TODO Para cambiar la plantilla de este comentario generado, vaya a
 * Ventana - Preferencias - Java - Estilo de c�digo - Plantillas de c�digo
 */
public class DocumentoImpl implements DocumentoDAO {

    SqlMapClient sqlMap;

    public Bnchf15DocumentoTramite idDocumento() throws Exception {
        return (Bnchf15DocumentoTramite) sqlMap.queryForObject("sqlDocumento.idDocumento");
    }

    public void guardarDocumento(Bnchf15DocumentoTramite bnchf15DocumentoTramite) throws Exception {
        try {
            sqlMap.startTransaction();
            sqlMap.insert("sqlDocumento.GuardaDocumento", bnchf15DocumentoTramite);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            sqlMap.endTransaction();
        }
    }

    /**
	 * @return Devuelve sqlMap.
	 */
    public SqlMapClient getSqlMap() {
        return sqlMap;
    }

    /**
	 * @param sqlMap El sqlMap a establecer.
	 */
    public void setSqlMap(SqlMapClient sqlMap) {
        this.sqlMap = sqlMap;
    }
}
