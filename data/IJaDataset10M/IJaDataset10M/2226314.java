package br.pucpr.sas.java.model.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.pucpr.sas.java.model.entity.ExecucaoAnalise;
import br.pucpr.sas.java.model.entity.ExecucaoElemento;
import br.pucpr.sas.java.model.entity.ProtocoloRecebimento;
import br.pucpr.sas.java.model.exception.ExecucaoAnaliseException;
import br.pucpr.sas.java.model.repository.ExecucaoAnaliseRepository;
import br.pucpr.sas.java.model.service.ExecucaoAnaliseService;

/**
 * 
 * @author Wellington
 *
 */
@Service(value = "execucaoAnaliseService")
@Transactional
public class ExecucaoAnaliseServiceImpl implements ExecucaoAnaliseService {

    private ExecucaoAnaliseRepository execucaoAnaliseRepository;

    @Autowired
    public void setExecucaoAnaliseRepository(ExecucaoAnaliseRepository execucaoAnaliseRepository) {
        this.execucaoAnaliseRepository = execucaoAnaliseRepository;
    }

    @Override
    public void salvarExecucaoAnalise(ExecucaoAnalise execucaoAnalise) throws ExecucaoAnaliseException {
        execucaoAnaliseRepository.salvarExecucaoAnalise(execucaoAnalise);
    }

    @Override
    public List<ExecucaoElemento> listarElementosAnalise(ProtocoloRecebimento protocoloRecebimento) throws ExecucaoAnaliseException {
        return execucaoAnaliseRepository.listarElementosAnalise(protocoloRecebimento);
    }
}
