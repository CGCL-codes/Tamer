package org.digitall.lib.components;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.text.MaskFormatter;
import org.digitall.deprecatedlibs.Proced;
import org.digitall.lib.calendar.SelectorFecha;
import org.digitall.lib.components.basic.BasicTextInput;
import org.digitall.lib.environment.Environment;

public class JTDate extends BasicTextInput implements KeyListener, FocusListener {

    private int minLimit = 100;

    private int maxLimit = 100;

    public static String NULLDATE = "  /  /    ";

    public JTDate() throws ParseException {
        this(100, 100);
    }

    public JTDate(int _minLimit, int _maxLimit) throws ParseException {
        super(new JTDateFormatter());
        minLimit = _minLimit;
        maxLimit = _maxLimit;
        setToolTipText("<html>F4: 01/01/" + Environment.currentYear + "<br>" + "F5: " + Proced.setFormatDate(Environment.currentDate, true) + "<br>" + "Doble click: Abrir almanaque<br>" + "Click derecho: Borrar fecha</html");
        setHorizontalAlignment(CENTER);
        setToday();
        addKeyListener(this);
        addFocusListener(this);
        setInputVerifier(new InputVerifier() {

            public boolean verify(JComponent input) {
                boolean returns = false;
                String _trydate = getText();
                GregorianCalendar _past = new GregorianCalendar();
                _past.add(GregorianCalendar.YEAR, -minLimit);
                GregorianCalendar _future = new GregorianCalendar();
                _future.add(GregorianCalendar.YEAR, maxLimit);
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                if (!_trydate.equalsIgnoreCase(NULLDATE)) {
                    try {
                        Date date = formatter.parse(getText());
                        String fecharet = formatter.format(date);
                        formatter.applyPattern("dd/MM/yyyy");
                        fecharet = formatter.format(date);
                        returns = fecharet.equalsIgnoreCase(_trydate) && date.after(_past.getTime()) && date.before(_future.getTime());
                    } catch (ParseException e) {
                    }
                } else {
                    returns = true;
                }
                if (!returns) {
                    Advisor.messageBox("Fecha no v�lida, debe estar comprendida entre " + formatter.format(_past.getTime()) + " y " + formatter.format(_future.getTime()), "Error");
                    setValue(null);
                }
                return returns;
            }
        });
        addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (isEnabled()) {
                    if (e.getClickCount() == 2 && e.getButton() == e.BUTTON1) {
                        BasicTextInput _textInput = new BasicTextInput();
                        _textInput.setValue(getText());
                        SelectorFecha _selectorFecha = new SelectorFecha(_textInput);
                        ComponentsManager.centerWindow(_selectorFecha);
                        _selectorFecha.setModal(true);
                        _selectorFecha.setVisible(true);
                        setText(_textInput.getText());
                    } else if (e.getClickCount() == 1 && e.getButton() == e.BUTTON3) {
                        setValue(null);
                    }
                }
            }
        });
    }

    public void keyTyped(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            setValue(null);
        }
    }

    public void keyPressed(KeyEvent e) {
    }

    public void setToday() {
        setValue(new Date());
    }

    public void setFirstDayOfYear() {
        setValue("01/01/" + Environment.currentYear);
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            setValue(null);
        } else if (e.getKeyCode() == KeyEvent.VK_F5) {
            setToday();
        } else if (e.getKeyCode() == KeyEvent.VK_F4) {
            setFirstDayOfYear();
        }
    }

    public void focusGained(FocusEvent e) {
    }

    public void focusLost(FocusEvent e) {
    }

    @Override
    public void setValue(Object _object) {
        if (_object instanceof Date) {
            super.setValue(_object);
        } else {
            String _value = "";
            try {
                _value += _object.toString();
            } catch (Exception e) {
            }
            if (_value.length() < 10) {
                super.setValue(null);
            } else {
                super.setValue(_value);
            }
        }
    }
}

class JTDateFormatter extends MaskFormatter {

    public JTDateFormatter() throws ParseException {
        super("##/##/####");
    }

    private SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

    public Object stringToValue(String text) throws ParseException {
        return formato.parseObject(text);
    }

    public String valueToString(Object value) throws ParseException {
        if (value instanceof Date) {
            return formato.format((Date) value);
        } else if (value instanceof String) {
            return formato.format(formato.parse((String) value));
        }
        if (value != null) {
            return formato.format(new Date());
        } else {
            return JTDate.NULLDATE;
        }
    }
}
