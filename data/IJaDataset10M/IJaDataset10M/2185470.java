package com.hy.mydesktop.client.component.factory;

import java.util.ArrayList;
import java.util.List;
import com.extjs.gxt.charts.client.model.charts.PieChart;
import com.extjs.gxt.charts.client.model.charts.PieChart.Slice;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.hy.mydesktop.client.component.meta.PieChartModelEnum;
import com.hy.mydesktop.shared.persistence.domain.gxt.GxtComponentMetaNodeModel;

/**
 * 
 * <ul>
 * <li>开发作者：汤莉</li>
 * <li>设计日期：2010-9-7；时间：下午02:36:23</li>
 * <li>类型名称：PieChartFactory</li>
 * <li>设计目的：</li>
 * </ul>
 * <ul>
 * <b>修订编号：</b>
 * <li>修订日期：</li>
 * <li>修订作者：</li>
 * <li>修订原因：</li>
 * <li>修订内容：</li>
 * </ul>
 */
public class PieChartFactory {

    /**
	 * 
	 * <ul>
	 * <li>方法含义：创建PieChart</li>
	 * <li>方法作者：汤莉</li>
	 * <li>编写日期：2010-9-7；时间：下午下午02:38:24</li>
	 * </ul>
	 * <ul>
	 * <b>修订编号：</b>
	 * <li>修订日期：</li>
	 * <li>修订作者：</li>
	 * <li>修订原因：</li>
	 * <li>修订内容：</li>
	 * </ul>
	 * @param gxtComponentMetaNodeModel
	 * @return
	 */
    public static PieChart createPieChart(GxtComponentMetaNodeModel gxtComponentMetaNodeModel) {
        return configureChart(gxtComponentMetaNodeModel);
    }

    /**
	 * 
	 * <ul>
	 * <li>方法含义：配置PieChart属性</li>
	 * <li>方法作者：汤莉</li>
	 * <li>编写日期：2010-9-7；时间：下午下午02:39:32</li>
	 * </ul>
	 * <ul>
	 * <b>修订编号：</b>
	 * <li>修订日期：</li>
	 * <li>修订作者：</li>
	 * <li>修订原因：</li>
	 * <li>修订内容：</li>
	 * </ul>
	 * @param gxtComponentMetaNodeModel
	 * @return
	 */
    private static PieChart configureChart(GxtComponentMetaNodeModel gxtComponentMetaNodeModel) {
        PieChart pie = defaultCreatePieChart();
        if (gxtComponentMetaNodeModel.get(PieChartModelEnum.ALPHA1.name().toLowerCase()) != null) {
            float alpha = (Float) gxtComponentMetaNodeModel.get(PieChartModelEnum.ALPHA1.name().toLowerCase());
            pie.setAlpha(alpha);
        }
        if (gxtComponentMetaNodeModel.get(PieChartModelEnum.BORDERS2.name().toLowerCase()) != null) {
            int border = (Integer) gxtComponentMetaNodeModel.get(PieChartModelEnum.BORDERS2.name().toLowerCase());
            pie.setBorder(border);
        }
        if (gxtComponentMetaNodeModel.get(PieChartModelEnum.COLOURS.name().toLowerCase()) != null) {
            List<String> colours = (List<String>) gxtComponentMetaNodeModel.get(PieChartModelEnum.COLOURS.name().toLowerCase());
            pie.setColours(colours);
        }
        if (gxtComponentMetaNodeModel.get(PieChartModelEnum.NOLABEL.name().toLowerCase()) != null) {
            boolean nolabels = (Boolean) gxtComponentMetaNodeModel.get(PieChartModelEnum.NOLABEL.name().toLowerCase());
            pie.setNoLabels(nolabels);
        }
        if (gxtComponentMetaNodeModel.get(PieChartModelEnum.RADIUS.name().toLowerCase()) != null) {
            Integer radius = (Integer) gxtComponentMetaNodeModel.get(PieChartModelEnum.RADIUS.name().toLowerCase());
            pie.setRadius(radius);
        }
        if (gxtComponentMetaNodeModel.get(PieChartModelEnum.STARTANGLE.name().toLowerCase()) != null) {
            Integer startAngle = (Integer) gxtComponentMetaNodeModel.get(PieChartModelEnum.STARTANGLE.name().toLowerCase());
            pie.setStartAngle(startAngle);
        }
        if (gxtComponentMetaNodeModel.get(PieChartModelEnum.TOOLTIP.name().toLowerCase()) != null) {
            String tooltip = (String) gxtComponentMetaNodeModel.get(PieChartModelEnum.TOOLTIP.name().toLowerCase());
            pie.setTooltip(tooltip);
        }
        if (gxtComponentMetaNodeModel.get(PieChartModelEnum.ADDSLICES.name().toLowerCase()) != null) {
            List<BaseTreeModel> slice = (List<BaseTreeModel>) gxtComponentMetaNodeModel.get(PieChartModelEnum.ADDSLICES.name().toLowerCase());
            for (BaseTreeModel i : slice) {
                pie.addSlices(new PieChart.Slice((Number) (i.get("bili")), i.get("jiancheng").toString(), i.get("name").toString()));
            }
        }
        return pie;
    }

    /**
	 * 
	 * <ul>
	 * <li>方法含义：创建默认方法</li>
	 * <li>方法作者：汤莉</li>
	 * <li>编写日期：2010-9-7；时间：下午下午02:40:46</li>
	 * </ul>
	 * <ul>
	 * <b>修订编号：</b>
	 * <li>修订日期：</li>
	 * <li>修订作者：</li>
	 * <li>修订原因：</li>
	 * <li>修订内容：</li>
	 * </ul>
	 * @return
	 */
    private static PieChart defaultCreatePieChart() {
        PieChart pie = new PieChart();
        return pie;
    }
}
