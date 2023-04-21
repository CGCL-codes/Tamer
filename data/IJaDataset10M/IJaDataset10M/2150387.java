package cdu.computer.hxl.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Map;
import javax.swing.JPanel;
import cdu.computer.hxl.factory.ObjectFactory;
import cdu.computer.hxl.service.IncomeService;
import cdu.computer.hxl.util.ThreadExecutorUtils;

public class IncomeAllocationChartUI extends BaseJPanel {

    private static final long serialVersionUID = -8114364422893235600L;

    private static final JPanel contentPanel = new JPanel();

    private static final IncomeService inService = (IncomeService) ObjectFactory.getInstance("incomeService");

    public IncomeAllocationChartUI() {
        contentPanel.setPreferredSize(new Dimension(405, 570));
        this.add(contentPanel);
    }

    @Override
    protected void init() {
        contentPanel.setLayout(new GridLayout(2, 1, 5, 5));
        new ThreadExecutorUtils() {

            @Override
            protected void task() {
                contentPanel.removeAll();
                addComponent(createAllocationPanel());
                addComponent(createIncomeMoneyAllocationPanel());
            }
        }.exec();
    }

    protected JPanel createIncomeMoneyAllocationPanel() {
        Map<String, Double> data = inService.statistiIncomeForMoneyallocation();
        Bar3DChartUI barChart = new Bar3DChartUI();
        JPanel panel = barChart.createBar3DChartPanel("�ʽ�������״ͼ", data);
        panel.setPreferredSize(new Dimension(400, 280));
        return panel;
    }

    protected JPanel createAllocationPanel() {
        Map<String, Object> data = inService.statistiIncome();
        PieChartUI pie = new PieChartUI();
        return pie.createPieChartPanel("����ֲ�ͼ", data);
    }

    protected void addComponent(Component comp) {
        contentPanel.add(comp);
        validate();
    }
}
