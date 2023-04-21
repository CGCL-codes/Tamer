package org.enterprise.rhtutorial.control.jsf;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.jcompany.commons.PlcArgEntity;
import org.jcompany.commons.PlcBaseContextEntity;
import org.jcompany.commons.PlcException;
import org.jcompany.commons.helper.PlcDateHelper;
import org.jcompany.config.control.collaboration.PlcConfigArgument.Format;
import org.jcompany.control.PlcConstants.PlcJsfConstants.NAVIGATION;

/**
 * Classe de controle para Caso de Uso "UC002.2 - Manter Funcion�rio"
 */
public class FuncionarioAction extends AppAction {

    @Override
    protected List<PlcArgEntity> searchWithNavigatorBefore(PlcBaseContextEntity context, List<PlcArgEntity> argumentsList) throws PlcException {
        return modificaIdadeParaDatas(argumentsList);
    }

    /**
	 * Substitui argumentos com "idade" informada por "dataNascimento", 
	 * inclusive o type do argumentos de "LONG" para "DATE".
	 */
    private List<PlcArgEntity> modificaIdadeParaDatas(List<PlcArgEntity> listaArgumentos) throws PlcException {
        for (Iterator<PlcArgEntity> iterator = listaArgumentos.iterator(); iterator.hasNext(); ) {
            PlcArgEntity argVO = iterator.next();
            if (argVO.getName().startsWith("idade") && NumberUtils.isNumber(argVO.getValue())) {
                argVO.setName(argVO.getName().replaceAll("idade", "dataNascimento"));
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                long anosInformados = new Long(argVO.getValue()).longValue();
                Date dataRelativa = new Date();
                argVO.setValue(sdf.format(dataRelativa));
                argVO.setType(Format.DATE.toString());
            }
        }
        return listaArgumentos;
    }

    @Override
    protected String editVisualizeDocumentAfter() throws PlcException {
        disponibilizaSalarioUltMes();
        return NAVIGATION.IND_SAME_PAGE;
    }

    /**
	 * Disponibiliza o �ltimo sal�rio recebido pelo usu�rio
	 */
    private void disponibilizaSalarioUltMes() throws PlcException {
        log.debug("Entrou para tentar trazer o c�lculo salarial");
    }
}
