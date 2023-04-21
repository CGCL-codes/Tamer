package backend;

import java.math.BigDecimal;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 *
 * @author Ronald Jenner Norat
 */
@WebService()
public class WS_BackEnd {

    private int idBanco, idAgencia, idConta;

    private Banco banco = new Banco();

    /**
     * Operação de serviço web
     */
    @WebMethod(operationName = "getBanco")
    public Banco getBanco(@WebParam(name = "idBanco") int idBanco) {
        return banco.consultaBanco(idBanco);
    }

    /**
     * Operação de serviço web
     */
    @WebMethod(operationName = "getLogin")
    public java.lang.String getLogin(@WebParam(name = "login") String login, @WebParam(name = "senha") String senha, @WebParam(name = "banco") String banco) {
        try {
            montaconta(login);
            this.idBanco = Integer.parseInt(banco);
            switch(this.idBanco) {
                case 1:
                    {
                        ws_bancobrasil.WSBancoBrasilService service = new ws_bancobrasil.WSBancoBrasilService();
                        ws_bancobrasil.WSBancoBrasil porta = service.getWSBancoBrasilPort();
                        java.lang.Boolean teste = porta.getLogin(this.idAgencia, this.idConta, senha);
                        if (teste) {
                            System.out.println(teste);
                            ws_bancobrasil.DadosConta dConta = porta.getDadosConta(this.idAgencia, this.idConta, senha);
                            return dConta.getNomeCliente();
                        } else return null;
                    }
                case 103:
                    {
                        ws_cef.WSCEFService service = new ws_cef.WSCEFService();
                        ws_cef.WSCEF porta = service.getWSCEFPort();
                        if (porta.getLogin(this.idAgencia, this.idConta, senha)) {
                            ws_cef.DadosConta dConta = porta.getDadosConta(this.idAgencia, this.idConta, senha);
                            return dConta.getNomeCliente();
                        } else return null;
                    }
                default:
                    return null;
            }
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Operação de serviço web
     */
    @WebMethod(operationName = "getDadosConta")
    public DadosConta getDadosConta(@WebParam(name = "idConta") String idConta, @WebParam(name = "senha") String senha) {
        try {
            montaconta(idConta);
            switch(this.idBanco) {
                case 1:
                    {
                        ws_bancobrasil.WSBancoBrasilService service = new ws_bancobrasil.WSBancoBrasilService();
                        ws_bancobrasil.WSBancoBrasil porta = service.getWSBancoBrasilPort();
                        ws_bancobrasil.DadosConta dConta = porta.getDadosConta(this.idAgencia, this.idConta, senha);
                        DadosConta dadosConta = new DadosConta();
                        dadosConta.setIdAgencia(dConta.getIdAgencia());
                        dadosConta.setIdConta(dConta.getIdConta());
                        dadosConta.setNomeAgencia(dConta.getNomeAgencia());
                        dadosConta.setNomeCliente(dConta.getNomeCliente());
                        return dadosConta;
                    }
                case 103:
                    {
                        ws_cef.WSCEFService service = new ws_cef.WSCEFService();
                        ws_cef.WSCEF porta = service.getWSCEFPort();
                        ws_cef.DadosConta dConta = porta.getDadosConta(this.idAgencia, this.idConta, senha);
                        DadosConta dadosConta = new DadosConta();
                        dadosConta.setIdAgencia(dConta.getIdAgencia());
                        dadosConta.setIdConta(dConta.getIdConta());
                        dadosConta.setNomeAgencia(dConta.getNomeAgencia());
                        dadosConta.setNomeCliente(dConta.getNomeCliente());
                        return dadosConta;
                    }
                default:
                    return null;
            }
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Operação de serviço web
     */
    @WebMethod(operationName = "getSaldo")
    public BigDecimal getSaldo(@WebParam(name = "idConta") String idConta, @WebParam(name = "senha") String senha) {
        try {
            montaconta(idConta);
            switch(this.idBanco) {
                case 1:
                    {
                        ws_bancobrasil.WSBancoBrasilService service = new ws_bancobrasil.WSBancoBrasilService();
                        ws_bancobrasil.WSBancoBrasil porta = service.getWSBancoBrasilPort();
                        return porta.getSaldo(this.idAgencia, this.idConta, senha);
                    }
                case 103:
                    {
                        ws_cef.WSCEFService service = new ws_cef.WSCEFService();
                        ws_cef.WSCEF porta = service.getWSCEFPort();
                        return porta.getSaldo(this.idAgencia, this.idConta, senha);
                    }
                default:
                    return null;
            }
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Operação de serviço web
     */
    @WebMethod(operationName = "getPagarTitulo")
    public Boolean getPagarTitulo(@WebParam(name = "codigoBarras") String codigoBarras) {
        System.out.println(codigoBarras);
        return true;
    }

    private void montaconta(String conta) {
        String campo = conta;
        int ini = 0;
        int posi = campo.indexOf(';');
        this.idAgencia = Integer.parseInt(campo.substring(0, posi));
        ini = posi + 1;
        campo = campo.substring(ini);
        this.idConta = Integer.parseInt(campo);
    }
}
