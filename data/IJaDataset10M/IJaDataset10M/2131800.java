package corujao.monitor.oracle;

import corujao.model.Monitoramento;
import corujao.model.ValorMonitoramento;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Eduardo_Rangel
 */
public class SqlValue extends OracleTask {

    private void executaSql(Monitoramento monitoramento, String comando) throws SQLException {
        String result = null;
        Connection conn = getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(comando);
            int linha = 0;
            while (rs.next()) {
                linha++;
                ValorMonitoramento vm = null;
                vm = new ValorMonitoramento();
                vm.setLinha(linha);
                vm.setColuna(rs.getString(1));
                vm.setIdMonitoramento(monitoramento.getIdAgenteMonitor());
                vm.setValor(rs.getString(2));
                monitoramento.addValorMonitoramento(vm);
                if (compara(getAgenteMonitor().getCriticalOperator(), rs.getInt(2), getAgenteMonitor().getCriticalValue())) {
                    addAlerta(monitoramento, 2, rs.getString(1) + " => " + rs.getString(2));
                } else if (compara(getAgenteMonitor().getWarningOperator(), rs.getInt(2), getAgenteMonitor().getWarningValue())) {
                    addAlerta(monitoramento, 1, rs.getString(1) + " => " + rs.getString(2));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (Exception e) {
                log.error("rs.close", e);
            }
            try {
                stmt.close();
            } catch (Exception e) {
                log.error("stmt.close", e);
            }
            try {
                conn.close();
            } catch (Exception e) {
                log.error("conn.close", e);
            }
        }
    }

    public void execute() {
        Monitoramento monitoramento = criaMonitoramento();
        long rst = 0;
        try {
            long inicio = System.currentTimeMillis();
            executaSql(monitoramento, getAgenteMonitor().getComando());
            rst = System.currentTimeMillis() - inicio;
            monitoramento.setTempoResposta(rst);
        } catch (Exception e) {
            log.error("Execute: ", e);
            addAlerta(monitoramento, 2, e.getMessage());
        }
    }
}
