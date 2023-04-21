package org.swiftgantt.swing.ui;

import javax.swing.UIManager;
import org.swiftgantt.swing.GanttChart;
import org.swiftgantt.core.GanttContext;

/**
 * The <code>LogoView</code> doesn't do anything except showing the product name "SwiftGantt" and version,
 * if you want to add your logo or something else, you can create your own displaying to replace
 * the default. To implement that, you could create a new swing component that shows what you want,
 * then add this component to the left corner view of the <code>GanttChart</code>. like:<BR>
 * <code>
 * &nbsp;&nbsp;&nbsp;&nbsp;context.setCorner(JScrollPane.UPPER_LEFT_CORNER, myLogoView);<BR>
 * </code>
 *
 * @author Yuxing Wang
 */
public class LogoView extends BaseView {

    private static final long serialVersionUID = 1L;

    protected LogoViewUI lvUI = null;

    private String text = "SwiftGantt";

    public LogoView(GanttContext ganttChart) {
        super(ganttChart);
        this.context = ganttChart;
        initialize();
    }

    /**
	 * This method initializes this
	 *
	 */
    private void initialize() {
        lvUI = (LogoViewUI) UIManager.getUI(this);
        super.setUI(lvUI);
    }

    /**
	 * The Class ID for <code>UIManager</code>.
	 */
    @Override
    public String getUIClassID() {
        return "LogoViewUI";
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
