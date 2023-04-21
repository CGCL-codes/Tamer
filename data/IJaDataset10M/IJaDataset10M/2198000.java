package arbitration.gui;

import com.toedter.calendar.DefaultHolidayPolicy;
import com.toedter.calendar.HolidayPolicy;
import com.toedter.calendar.JTextFieldDateEditor;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.EtchedBorder;

/**
 * Information p
 * anel
 * @author Startsev S.S.
 * @version 1.0
 * @see JPanel
 */
public class InformationPanel extends JPanel {

    private CalcPanel calcPanel;

    private SettingsPanel settingsPanel;

    /**
     * Constructor    
     */
    public InformationPanel() {
        this.setLayout(new GridBagLayout());
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Tahoma", Font.TRUETYPE_FONT, 11));
        calcPanel = new CalcPanel();
        PaymentPeriodListener paymentPeriodListener = new PaymentPeriodListener(calcPanel);
        CalcListener calcListener = new CalcListener(calcPanel);
        calcPanel.getPaymentDays().addFocusListener(paymentPeriodListener);
        ((JTextFieldDateEditor) calcPanel.getDeliveryDate().getDateEditor()).addFocusListener(paymentPeriodListener);
        tabs.addTab("Расчеты", calcPanel);
        calcPanel.getCalc().addActionListener(calcListener);
        settingsPanel = new SettingsPanel();
        HolidayPolicy holidayPolicy = new DefaultHolidayPolicy();
        settingsPanel.getCalendar().setHolidayPolicy(holidayPolicy);
        paymentPeriodListener.setHolidayPolicy(holidayPolicy);
        calcListener.setHolidayPolicy(holidayPolicy);
        tabs.addTab("Настройки", settingsPanel);
        add(tabs, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 0, 0, 0), 0, 0));
    }
}
