package br.com.srv.componentes.relatorio.estatisticaperimetro.service;

import java.util.List;
import br.com.srv.componentes.bloquear.service.VeiculoService;
import br.com.srv.componentes.relatorio.estatisticaperimetro.vo.EstatisticaPerimetroResultVO;
import br.com.srv.model.CercaTO;

public interface EstatisticaPerimetroService {

    public static final String SERVICE_NAME = "estatisticaPerimetroService";

    public List<EstatisticaPerimetroResultVO> gerarDadosRelatorioByVeiculo(Integer veiculoId, CercaTO cercaTO, String dataInicial, String horaInicial, String dataFinal, String horaFinal) throws Exception;

    public void setVeiculoService(VeiculoService veiculoService);

    public List<EstatisticaPerimetroResultVO> gerarDadosRelatorioByCerca(CercaTO cercaTO, String dataInicial, String horaInicial, String dataFinal, String horaFinal) throws Exception;
}
