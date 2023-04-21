package servidor.ejecutor.xql;

import servidor.conexion.FabricaServidorTcp;
import servidor.conexion.ServidorTcp;
import servidor.ejecutor.Resultado;
import servidor.excepciones.VictimaDeadlockRuntimeException;
import servidor.transaccion.Estado;
import servidor.transaccion.FabricaTransactionManager;
import servidor.transaccion.TransactionManager;

/**
 * Clase decoradora que aborta todas las transacciones del thread en caso que este sea elegido como victima de deadlock
 * por el Algoritmo de Prevencion.
 */
public class XStatement_ManejadorDeadlock_Decorador extends AbstractXStatementDecorador {

    /**
     * Variable con el Servidor de conexiones para saber si el motor esta siendo apagado (comun o crash).
     */
    private ServidorTcp servidorTcp;

    /**
	 * Constructor de la clase.
	 * @param statement el XStatement a decorar.
	 */
    public XStatement_ManejadorDeadlock_Decorador(XStatement statement) {
        super(statement);
        this.servidorTcp = FabricaServidorTcp.dameInstancia();
    }

    /**
	 * @see servidor.ejecutor.xql.AbstractXStatementDecorador#execute()
	 */
    @Override
    public Resultado execute() {
        try {
            return super.execute();
        } catch (RuntimeException e) {
            if (e instanceof VictimaDeadlockRuntimeException) {
                TransactionManager transactionManager = FabricaTransactionManager.dameInstancia();
                if (transactionManager.estadoActual() == Estado.EN_CURSO && !this.servidorTcp.crash()) {
                    transactionManager.abortarTransaccionesDelThread();
                }
            }
            throw e;
        } catch (Error e) {
            TransactionManager transactionManager = FabricaTransactionManager.dameInstancia();
            if (transactionManager.estadoActual() == Estado.EN_CURSO && !this.servidorTcp.crash()) {
                transactionManager.abortarTransaccionesDelThread();
            }
            throw e;
        }
    }
}
