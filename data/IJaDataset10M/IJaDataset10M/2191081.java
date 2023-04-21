package uniriotec.pm.trabalho2.api;

import uniriotec.pm.trabalho2.dto.OpcaoDTO;

/**
 *
 * @author albertoapr
 */
public interface OpcaoService {

    void createOpcao(OpcaoDTO opcao);

    void updateOpcao(OpcaoDTO opcao);

    void removeOpcao(Long opcaoid);
}
