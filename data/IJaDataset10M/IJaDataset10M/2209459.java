package br.pucpr.sas.java.model.service;

import java.util.List;
import br.pucpr.sas.java.model.entity.Cliente;
import br.pucpr.sas.java.model.entity.ProtocoloRecebimento;
import br.pucpr.sas.java.model.exception.ProtocoloRecebimentoException;

/**
 * 
 * @author Wellington
 *
 */
public interface ProtocoloRecebimentoService {

    public void salvarProtocoloRecebimento(ProtocoloRecebimento protocoloRecebimento) throws ProtocoloRecebimentoException;

    public List<ProtocoloRecebimento> listarProtocoloRecebimentoByCliente(Cliente cliente) throws ProtocoloRecebimentoException;
}
