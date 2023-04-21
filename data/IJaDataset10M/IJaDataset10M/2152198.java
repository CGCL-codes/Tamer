package br.ufmg.lcc.pcollecta.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import br.ufmg.lcc.arangi.commons.BasicException;
import br.ufmg.lcc.arangi.commons.NVLHelper;
import br.ufmg.lcc.arangi.dto.BasicDTO;
import br.ufmg.lcc.arangi.model.IPersistenceObject;
import br.ufmg.lcc.arangi.model.StandardBusinessObject;
import br.ufmg.lcc.pcollecta.dto.CampoDetalhado;
import br.ufmg.lcc.pcollecta.dto.ETC;
import br.ufmg.lcc.pcollecta.dto.ParametroGlobal;

public class ParametroGlobalBO extends StandardBusinessObject {

    public List buscaEtcComParametroGoblalDefinido(IPersistenceObject dao, String nomeParametroGlobal) throws BasicException {
        Boolean etcPossuiParametro = Boolean.FALSE;
        ETCBO etcBO = (ETCBO) getBusinessObject("br.ufmg.lcc.pcollecta.model.ETCBO");
        List etcList = etcBO.doLoadAll(dao, null, "br.ufmg.lcc.pcollecta.dto.ETC", "");
        Iterator etcListIterator = etcList.iterator();
        if (etcList != null) {
            while (etcListIterator.hasNext()) {
                ETC etc = (ETC) etcListIterator.next();
                CampoDetalhado campo = null;
                etcPossuiParametro = verificaParametrosGlobais(etc.getSql(), nomeParametroGlobal);
                if (!etcPossuiParametro) {
                    etcPossuiParametro = verificaParametrosGlobais(etc.getSqlExclusao(), nomeParametroGlobal);
                    if (!etcPossuiParametro) {
                        etcPossuiParametro = verificaParametrosGlobais(etc.getFiltroDescarte(), nomeParametroGlobal);
                        if (!etcPossuiParametro) {
                            Iterator it = etc.getDetalheLayoutDestino().iterator();
                            while (it.hasNext()) {
                                campo = (CampoDetalhado) it.next();
                                if (verificaParametrosGlobais(campo.getScriptMapeamento(), nomeParametroGlobal)) {
                                    etcPossuiParametro = Boolean.TRUE;
                                    break;
                                }
                            }
                        }
                    }
                }
                if (etcPossuiParametro == Boolean.FALSE) {
                    etcListIterator.remove();
                }
            }
        }
        return etcList;
    }

    private Boolean verificaParametrosGlobais(String script, String chave) throws BasicException {
        String novoScript = script;
        String valor = "";
        if (!NVLHelper.nvl(novoScript, "").equals("")) {
            int ini = novoScript.indexOf("#{");
            while (ini >= 0) {
                String aux = novoScript.substring(ini);
                int end = aux.indexOf("}") + ini;
                String parametro = novoScript.substring(ini + 2, end);
                if (parametro.equals(chave)) {
                    return Boolean.TRUE;
                }
                novoScript = novoScript.substring(0, ini) + valor + novoScript.substring(end + 1);
                ini = novoScript.indexOf("#{");
            }
        }
        return Boolean.FALSE;
    }

    public String substituiParametrosGlobais(Map mapParametros, String script) throws BasicException {
        String novoScript = script;
        int ini = novoScript.indexOf("#{");
        while (ini >= 0) {
            String aux = novoScript.substring(ini);
            int end = aux.indexOf("}") + ini;
            if (end < 0 || (end - 3) < ini) {
                throw BasicException.errorHandling("Erro na formata��o de par�metros em script", "msgErroFormatacaoParametro", new String[] {}, log);
            }
            String parametro = novoScript.substring(ini + 2, end);
            if (!mapParametros.containsKey(parametro.trim().toUpperCase())) {
                throw BasicException.errorHandling("Erro na formata��o de par�metros em script, par�metro inexistente " + parametro, "msgErroScriptParametroNaoEncontradoRepositorio", new String[] { parametro }, log);
            }
            Object valor = mapParametros.get(parametro.trim().toUpperCase());
            novoScript = novoScript.substring(0, ini) + "'" + valor + "'" + novoScript.substring(end + 1);
            ini = novoScript.indexOf("#{");
        }
        return novoScript;
    }

    public Map obtemMapParametrosGlobais(IPersistenceObject persistenceObject, BasicDTO dtoArg, String script) throws BasicException {
        List parametros = this.doLoadAll(persistenceObject, dtoArg, "br.ufmg.lcc.pcollecta.dto.ParametroGlobal", "");
        Map mapParametrosGlobais = new HashMap();
        if (parametros != null) {
            for (int i = 0; i < parametros.size(); i++) {
                ParametroGlobal parametro = (ParametroGlobal) parametros.get(i);
                mapParametrosGlobais.put(parametro.getName().trim().toUpperCase(), parametro.getValor());
            }
        }
        return mapParametrosGlobais;
    }
}
