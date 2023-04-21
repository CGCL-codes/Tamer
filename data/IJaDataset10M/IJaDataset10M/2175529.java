package ws_cef;

import java.math.BigDecimal;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 *
 * @author ronald
 */
@WebService()
public class WS_CEF {

    /**
     * Operação de serviço web
     */
    @WebMethod(operationName = "getLogin")
    public Boolean getLogin(@WebParam(name = "idAgencia") int idAgencia, @WebParam(name = "idConta") int idConta, @WebParam(name = "Senha") String Senha) {
        Conta conta = new Conta(idAgencia, idConta, Senha);
        return conta.getIdConta() == idConta;
    }

    /**
     * Operação de serviço web
     */
    @WebMethod(operationName = "getDadosConta")
    public DadosConta getDadosConta(@WebParam(name = "idAgencia") int idAgencia, @WebParam(name = "idConta") int idConta, @WebParam(name = "Senha") String Senha) {
        Conta conta = new Conta(idAgencia, idConta, Senha);
        if (conta.getIdConta() == idConta) return conta.consultaDadosConta(); else return null;
    }

    /**
     * Operação de serviço web
     */
    @WebMethod(operationName = "getSaldo")
    public BigDecimal getSaldo(@WebParam(name = "idAgencia") int idAgencia, @WebParam(name = "idConta") int idConta, @WebParam(name = "Senha") String Senha) {
        Conta conta = new Conta(idAgencia, idConta, Senha);
        if (conta.getIdConta() == idConta) return conta.getSaldo(); else return null;
    }
}
