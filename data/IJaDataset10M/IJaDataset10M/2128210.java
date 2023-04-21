package corujao.monitor.linux;

import corujao.model.Monitoramento;
import corujao.model.ValorMonitoramento;

/**
 *
 * @author bossan
 */
public class IOWaitTask extends ShellTask {

    public void execute() {
        Monitoramento monitoramento = criaMonitoramento();
        long rst = 0;
        try {
            long inicio = System.currentTimeMillis();
            String[] result = executaShell("scripts/iowait.sh", monitoramento);
            rst = System.currentTimeMillis() - inicio;
            if (result != null && result.length > 0) {
                String valor = result[0];
                float valorIO = 0;
                try {
                    valorIO = Float.parseFloat(valor);
                } catch (NumberFormatException nfe) {
                    log.error(nfe);
                    addAlerta(monitoramento, 2, "NumberFormatException: " + valor);
                }
                monitoramento.setTempoResposta(rst);
                ValorMonitoramento vm = new ValorMonitoramento();
                vm.setLinha(1);
                vm.setColuna("1");
                vm.setIdMonitoramento(monitoramento.getIdAgenteMonitor());
                vm.setValor(valor);
                monitoramento.addValorMonitoramento(vm);
                if (valorIO > getAgenteMonitor().getCriticalValue()) {
                    addAlerta(monitoramento, 2, "IO wait ultrapassado: " + valor);
                } else if (valorIO > getAgenteMonitor().getWarningValue()) {
                    addAlerta(monitoramento, 1, "IO wait ultrapassado: " + valor);
                }
            } else {
                addAlerta(monitoramento, 2, "Sem retorno do shell");
            }
        } catch (Exception e) {
            log.error(e);
            addAlerta(monitoramento, 2, e.getMessage());
        }
    }
}
