package web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import web.banco.DatabaseProvider;
import web.banco.DatabaseInterface.APPROVAL_STATUS;
import dataTypes.AnswerCodes1;
import dataTypes.MethodPars;

/**
 * Servlet implementation class ApprReq
 */
public class ApprReq extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String RMI_ADDRESS = "rmi://localhost:1099/UsuarioService";

    /** Database implementation **/
    private DatabaseProvider databaseProvider = DatabaseProvider.getInstance();

    private static final String STR_APPR_REQ = MethodPars.APPR_REQ.getStr();

    /**
	 * @see HttpServlet#HttpServlet()
	 */
    public ApprReq() {
        super();
    }

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @SuppressWarnings("unchecked")
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> pMap1;
        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
        RequisicaoUtil1 req = new RequisicaoUtil1(MethodPars.APPR_REQ);
        switch(req.armazenaParams(request.getParameterMap())) {
            case OK_200:
                pMap1 = req.getPMapStr();
                try {
                    Integer id = databaseProvider.getDatabase().insertInto(APPROVAL_STATUS.ON_GOING, pMap1.get("cl"), pMap1.get("sk"));
                    pMap1.put("id", id.toString());
                    pMap1.put("status", APPROVAL_STATUS.ON_GOING.toString());
                } catch (Exception ex) {
                    System.out.println("N�o foi poss�vel inserir a approva��o no banco de dados: " + pMap1);
                    ex.printStackTrace();
                    writer.println(AnswerCodes1.SRV_ERR_500.getXml(STR_APPR_REQ));
                    writer.close();
                    return;
                }
                RMIinterface rmi = new RMIImpl();
                boolean result = rmi.solicitaAprovacao(pMap1, RMI_ADDRESS);
                if (result) {
                    req.criaResposta(pMap1, writer, AnswerCodes1.TRYING_100);
                } else {
                    System.out.println("Erro ao registrar a solicita��o de aprova��o.");
                    writer.println(AnswerCodes1.SRV_ERR_500.getXml(STR_APPR_REQ));
                }
                break;
            case BAD_REQ_400:
                writer.println(AnswerCodes1.BAD_REQ_400.getXml(STR_APPR_REQ));
                break;
            default:
                writer.println(AnswerCodes1.SRV_ERR_500.getXml(STR_APPR_REQ));
        }
        writer.close();
    }

    /**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
        writer.println(AnswerCodes1.BAD_REQ_400.getXml(STR_APPR_REQ));
        writer.close();
    }
}
