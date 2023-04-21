package net.sf.opentranquera.orm;

/**
 * TODO Documentar
 * 
 * @author <a href="mailto:rdiegoc@gmail.com">Diego</a><br>
 * @version 1.0
 */
public class HibernateTransactionableTestCase extends HibernateTestCase {

    /**
     * Commitea o rollbackea la transaccion y cierra la conexion
     */
    protected void closeConnection() throws Exception {
        super.closeConnection();
    }

    /**
     * Abre la conexion e inicia una transaccion, de forma tal que cada testcase
     * ejecutado sea transactional
     */
    protected void openConnection() throws Exception {
        super.openConnection();
    }
}
