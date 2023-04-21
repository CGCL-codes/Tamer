package scp.kernel.jsonrpc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import scp.kernel.bean.SeletiveTestLocalBean;
import scp.kernel.sa.BusinessException;
import scp.kernel.sa.MaintainSeletiveTestLocal;

/**
 * Classe acessada via JSON para Local de Concurso.
 * @author Rafael Abreu
 *
 */
public class MaintainSeletiveTestLocalJSONRPC implements Serializable {

    private static final long serialVersionUID = 1;

    /**
     * M�todo que verifica se o c�digo j� n�o existe na base. 
     * @param id 
     * @param code
     * @param idSeletiveTest 
     * @return Boolean mostrando se o nome eh ou n�o valido
     */
    public boolean validateCode(Long id, String code, Long idSeletiveTest) {
        if (id == null) id = 0L;
        boolean r = false;
        try {
            r = new MaintainSeletiveTestLocal().validateCode(id, code, idSeletiveTest);
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return r;
    }

    /**
     * M�todo que verifica se j� n�o existe configurado o local informado 
     * no concurso informado. 
     * @param id 
     * @param idSeletivetest 
     * @param idLocal 
     * @return Boolean mostrando se o nome eh ou n�o valido
     */
    public boolean validateSeletiveTestLocal(Long id, Long idSeletivetest, Long idLocal) {
        if (id == null) id = 0L;
        boolean r = false;
        try {
            r = new MaintainSeletiveTestLocal().validateSeletiveTestLocal(id, idSeletivetest, idLocal);
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return r;
    }

    /**
     * M�todo que retorna os Locais de Prova do concurso informado. 
     * @param idSeletiveTest 
     * @return list
     */
    public List<SeletiveTestLocalBean> listSeletiveTestLocalsBySeletivetest(Long idSeletiveTest) {
        List<SeletiveTestLocalBean> seletiveTestLocals = new ArrayList<SeletiveTestLocalBean>();
        try {
            seletiveTestLocals = new MaintainSeletiveTestLocal().listSeletiveTestLocals(idSeletiveTest);
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return seletiveTestLocals;
    }
}
