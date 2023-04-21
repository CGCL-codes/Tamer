package jasperdesign.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.*;
import jasperdesign.jdbc.*;
import jasperdesign.*;
import jasperdesign.util.Gbc;
import org.apache.commons.lang.StringUtils;
import net.sf.nachocalendar.CalendarFactory;
import net.sf.nachocalendar.components.DateField;

public class ParameterPromptPanel extends JPanel {

    JasperDesign design = null;

    Map fields = new HashMap();

    SimpleDateFormat dateFmt = new SimpleDateFormat("MM/dd/yyyy");

    public ParameterPromptPanel(JasperDesign d, ReportConfig rc) {
        super(new GridBagLayout());
        dateFmt.setTimeZone(TimeZone.getDefault());
        design = d;
        setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        int row = 0;
        JRParameter parm = null;
        JLabel lbl = null;
        JTextField fld = null;
        Component cmp = null;
        JRParameter parms[] = design.getParameters();
        for (int i = 0; i < parms.length; i++) {
            parm = parms[i];
            if (!parm.isSystemDefined()) {
                String prompt = parm.getDescription();
                if (prompt == null || prompt.trim().length() < 1) {
                    prompt = parm.getName();
                }
                lbl = new JLabel(prompt);
                add(lbl, Gbc.getLabelGbc(row));
                cmp = getEditorComponent(parm);
                add(cmp, Gbc.getFieldGbc(row++, 2));
                fields.put(parm.getName(), cmp);
            }
        }
    }

    Component getEditorComponent(JRParameter p) {
        Component fld = null;
        fld = new JTextField(30);
        JRExpression exp = p.getDefaultValueExpression();
        if (p.getValueClass().getName().equals("java.util.Date")) {
            fld = CalendarFactory.createDateField();
        } else if (p.getValueClass().getName().equals("java.lang.Boolean")) {
            fld = new JCheckBox();
        } else if (exp != null && exp.getText().trim().length() > 0) {
            JTextField tFld = new JTextField(30);
            fld = tFld;
            tFld.setText(StringUtils.strip(exp.getText(), "\""));
        }
        return fld;
    }

    public Object getParameterValue(JRParameter parm) {
        Component editor = (Component) fields.get(parm.getName());
        if (parm.getValueClass().getName().equals("java.lang.Integer")) {
            JTextField fld = (JTextField) editor;
            return Integer.valueOf(fld.getText());
        } else if (parm.getValueClass().getName().equals("java.lang.Boolean")) {
            JCheckBox fld = (JCheckBox) editor;
            return Boolean.valueOf(fld.isSelected());
        } else if (parm.getValueClass().getName().equals("java.util.Date")) {
            try {
                DateField fld = (DateField) editor;
                return fld.getValue();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (parm.getValueClass().getName().equals("java.lang.Double")) {
            JTextField fld = (JTextField) editor;
            return Double.valueOf(fld.getText());
        }
        JTextField fld = (JTextField) editor;
        return fld.getText();
    }

    public Map getParameters() {
        HashMap map = new HashMap();
        Iterator iter = design.getParametersMap().values().iterator();
        while (iter.hasNext()) {
            JRParameter parm = (JRParameter) iter.next();
            if (!parm.isSystemDefined()) {
                Object val = getParameterValue(parm);
                map.put(parm.getName(), val);
            }
        }
        return map;
    }
}
