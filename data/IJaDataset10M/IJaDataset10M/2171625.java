package hoi.addrbook.ui;

import hoi.addrbook.util.DeltaDate;
import hoi.addrbook.util.Localization;
import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class InfoTimerField extends JPanel implements AccessInterface {

    private static final long serialVersionUID = 3640198100387397959L;

    private InfoTextField delta = new InfoTextField();

    private JButton clear = new JButton(Localization.getLocalString("Reset"));

    private String contactKey = null;

    private String contentDate = null;

    private String dateFormat = "yyyy-MM-dd";

    public InfoTimerField(String contactKey, String compName) {
        super(new BorderLayout(1, 1));
        this.contactKey = contactKey;
        setName(compName);
        clear.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(), new EmptyBorder(new Insets(1, 7, 1, 7))));
        delta.setEditable(false);
        add(delta, BorderLayout.CENTER);
        add(clear, BorderLayout.EAST);
        clear.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setContent(new SimpleDateFormat(dateFormat).format(new Date()));
            }
        });
        setContent(new SimpleDateFormat(dateFormat).format(new Date()));
    }

    private String formatDelataDate(DeltaDate delta) {
        String syear = Localization.getLocalString(delta.year > 1 ? "Years" : "Year");
        String smonth = Localization.getLocalString(delta.month > 1 ? "Months" : "Month");
        String sday = Localization.getLocalString(delta.day > 1 ? "Days" : "Day");
        if (delta.year >= 1) {
            return delta.day >= 1 ? String.format("%d %s %d %s %d %s", delta.year, syear, delta.month, smonth, delta.day, sday) : (delta.month >= 1 ? String.format("%d %s %d %s", delta.year, syear, delta.month, smonth) : String.format("%d %s", delta.year, syear));
        } else if (delta.month >= 1) {
            return delta.day >= 1 ? String.format("%d %s %d %s", delta.month, smonth, delta.day, sday) : String.format("%d %s", delta.month, smonth);
        } else {
            return String.format("%d %s", delta.day, sday);
        }
    }

    public String getContactKey() {
        return contactKey;
    }

    public String getContent() {
        return contentDate + "; " + delta.getText();
    }

    public void setContent(Date date) {
        setContent(new SimpleDateFormat(dateFormat).format(date));
    }

    public void setContent(String content) {
        if (content == null || content.trim().equals("")) {
            setContent(new Date());
        } else {
            content = content.trim();
            if (content.length() > dateFormat.length()) content = content.substring(0, dateFormat.length());
            this.contentDate = content;
            try {
                delta.setText(formatDelataDate(new DeltaDate(new SimpleDateFormat(dateFormat).parse(contentDate), new Date())));
            } catch (ParseException e) {
                delta.setText(Localization.getLocalString("Parse Error"));
            }
        }
    }
}
