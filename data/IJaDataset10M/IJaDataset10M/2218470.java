package hoplugins.teamplanner.ui.tabs.players.listener;

import hoplugins.teamplanner.manager.TeamPlayerManager;
import hoplugins.teamplanner.ui.controller.calculator.Calculator;
import hoplugins.teamplanner.ui.model.OperationCell;
import hoplugins.teamplanner.ui.tabs.WeekHeader;
import hoplugins.teamplanner.util.Util;
import hoplugins.teamplanner.vo.HTWeek;
import plugins.IFutureTrainingManager;
import javax.swing.table.TableModel;

/**
 * Missing Class Documentation
 *
 * @author Draghetto
 */
public class SalaryCalculator extends Calculator {

    /**
     * Missing Method Documentation
     *
     * @param row Missing Method Parameter Documentation
     * @param model Missing Method Parameter Documentation
     */
    @Override
    public void doCalculate(int row, TableModel model) {
        for (int i = 0; i < IFutureTrainingManager.FUTUREWEEKS; i++) {
            OperationCell cell = Util.getOperationCell(model, row, i);
            HTWeek week = WeekHeader.instance().getColumnWeek(i);
            int salary = TeamPlayerManager.getSalary(week);
            cell.getOperation().getInner().setMoney(-salary);
        }
    }
}
