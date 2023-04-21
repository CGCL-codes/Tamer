package chsec.gui.rep;

import chsec.util.ExtListModel;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 *Provides year selection interface component.
 * @author Oleks Yaremenko
 */
public class YearParamPanelImp extends JPanel implements ParamPanel {

    private static final long serialVersionUID = 1L;

    private ExtListModel<Integer> yearCBModel;

    private String paramName;

    private JLabel paramNameL;

    private JComboBox yearCB;

    public YearParamPanelImp(int history) {
        initGUI(history);
    }

    public boolean hasDefVal() {
        return true;
    }

    public String getParamName() {
        return paramName;
    }

    public Object getParamValue() {
        return yearCBModel.getSelectedItem();
    }

    public boolean isValError() {
        return false;
    }

    public String getErrorMessage() {
        return "Should not be an error";
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public void setParamExtName(String paramExtName) {
        paramNameL.setText(paramExtName);
    }

    private void initGUI(int history) {
        Calendar cal = Calendar.getInstance();
        int currYear = cal.get(Calendar.YEAR);
        int yr = currYear;
        ArrayList<Integer> years = new ArrayList<Integer>();
        for (int i = history; i >= 0; i--, yr--) {
            years.add(yr);
        }
        yearCBModel = new ExtListModel<Integer>();
        yearCBModel.setMyModel(years);
        this.setPreferredSize(new java.awt.Dimension(200, 40));
        {
            paramNameL = new JLabel();
            this.add(paramNameL);
            paramNameL.setText("Report Year");
            paramNameL.setSize(100, 14);
        }
        yearCB = new JComboBox(yearCBModel);
        yearCB.setSize(40, 20);
        this.add(yearCB);
        yearCBModel.setSelectedIdx(0);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.getContentPane().add(new YearParamPanelImp(4));
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
