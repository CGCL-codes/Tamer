package net.sf.appomatox.control;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.text.NumberFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.appomatox.gui.taschenrechner.RechnerException;
import net.sf.appomatox.gui.taschenrechner.TaschenrechnerFenster;
import net.sf.appomatox.utils.DecimalSeparatorUtils;

public class TaschenrechnerController {

    /**
     * Logger f�r die Ausgabe von Meldungen
     */
    private static Logger s_logger = Logger.getLogger(TaschenrechnerController.class.getName());

    /**
     * @see #getGroovyShell()
     */
    private GroovyShell m_GroovyShell = null;

    public TaschenrechnerController(AppoController appoController) {
        getGroovyShell();
    }

    public void setGUI(TaschenrechnerFenster taschenrechnerFenster) {
    }

    /**
     * Name der Variablen, die das letzte Ergebnis beinhaltet
     */
    private final String m_sLastResultVariable = "last";

    /**
     * Ermittelt eine GroovyShell. Falls die Variable m_GroovyShell
     * <code>null</code> ist, wird diese initialisiert, andernfalls
     * wird my_GroovyShell zur�ckgeliefert.
     * 
     * @return (erw�hnt)
     */
    private GroovyShell getGroovyShell() {
        if (m_GroovyShell == null) {
            Binding binding = new Binding();
            binding.setVariable("last", new BigInteger("0"));
            m_GroovyShell = new GroovyShell(binding);
        }
        return m_GroovyShell;
    }

    /**
     * F�hrt Groovy-Code aus und gibt dessen Ergebnis zur�ck
     * @param code Der Groovy-Code
     * @return Das Ergebnis
     * @throws RechnerException Falls die Eingabe ung�ltig war.
     */
    public String evaluateStandard(String code) throws RechnerException {
        s_logger.entering(TaschenrechnerController.class.getName(), "evaluate(String)", code);
        code = DecimalSeparatorUtils.changeDecimalSeparatorToGroovy(code);
        code = changeMethodParameterSeparatorToGroovy(code);
        code = switchPowerXOR(code);
        code = expandDEG(code);
        code = expandMinutes(code);
        code = expandFactorial(code);
        Object value = evaluateErweitert(code);
        if (value instanceof Exception) {
            s_logger.log(Level.WARNING, "Bei der Ausf�hrung der Taschenrechnerberechnung mit Groovy ist ein Fehler aufgetreten.", (Exception) value);
            value = null;
        }
        if (value != null) {
            s_logger.fine("Ergebnistyp " + value.getClass() + " wird gecastet nach java.lang.String");
            NumberFormat nf = NumberFormat.getInstance();
            nf.setGroupingUsed(true);
            nf.setMaximumFractionDigits(16);
            String returnValue;
            if (value instanceof BigDecimal) {
                returnValue = nf.format(((BigDecimal) value).doubleValue());
            } else if (value instanceof Double) {
                returnValue = nf.format(((Double) value).doubleValue());
            } else if (value instanceof Float) {
                returnValue = nf.format(((Float) value).doubleValue());
            } else if (value instanceof Integer) {
                returnValue = nf.format(((Integer) value).longValue());
            } else if (value instanceof Long) {
                returnValue = nf.format(((Long) value).longValue());
            } else {
                returnValue = value.toString();
                char decimalSep = DecimalSeparatorUtils.getDecimalSeparator();
                returnValue = returnValue.replace('.', decimalSep);
            }
            value = returnValue;
        }
        return (String) value;
    }

    /**
     * In der Standardansicht werden Methodenparameter durch Semikolon
     * separiert, z.B. rnd(123,45;2)
     * @param code Die Eingabe
     * @return Die Groovy-konforme Ausgabe
     */
    private String changeMethodParameterSeparatorToGroovy(String code) {
        code = code.replace(";", ",");
        return code;
    }

    private String expandDEG(String code) {
        code = code.replace(" �", ".toRAD()");
        code = code.replace("�", ".toRAD()");
        return code;
    }

    private String expandMinutes(String code) {
        code = code.replace(" '", ".minToGrad()");
        code = code.replace("'", ".minToGrad()");
        return code;
    }

    /**
     * Expandiert das Fakult�ts-Zeichen ("!")
     * @param code Der Code, in dem das Zeichen expandiert werden soll
     * @return Der expandierte Code
     */
    private String expandFactorial(String code) {
        code = code.replace("!", ".factorial()");
        return code;
    }

    private String switchPowerXOR(String code) {
        final String DUMMY = "@@@@";
        code = code.replace("^", DUMMY);
        code = code.replace("**", "^");
        code = code.replace(DUMMY, "**");
        return code;
    }

    /**
     * Erweitert den Code, so dass dieser mittels Groovy ausgef�hrt werden kann
     * @param code Der zu erweiternde Code
     * @return Der erweiterte Code
     * @throws RechnerException Falls der Code nicht in Grooovy-Syntax umgewandelt werden
     * konnte.
     */
    private String addGroovyCode(String code) throws RechnerException {
        s_logger.entering(TaschenrechnerController.class.getName(), "addGroovyCode(String)", code);
        URL url = TaschenrechnerController.class.getResource("/net/sf/appomatox/control/taschenrechner.goovy");
        assert url != null;
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            BufferedInputStream bis = new BufferedInputStream((InputStream) url.getContent());
            br = new BufferedReader(new InputStreamReader(bis));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            s_logger.log(Level.SEVERE, "Taschenrechner-Code konnte nicht um Groovy-Bestandteil erweitert werden.", e);
            throw new RechnerException(e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    s_logger.log(Level.INFO, "Stream zum Einlesen des Groovy-Templates knn nicht geschlossen werden.", e);
                }
            }
        }
        int idxCode = sb.indexOf("@CODE@");
        sb.replace(idxCode, idxCode + 6, code);
        s_logger.exiting(TaschenrechnerController.class.getName(), "addGroovyCode(String)", sb.toString());
        return sb.toString();
    }

    public Object evaluateErweitert(String code) throws RechnerException {
        GroovyShell shell = getGroovyShell();
        code = addGroovyCode(code);
        s_logger.info("Evaluiere: [" + code + "]");
        Object value = null;
        try {
            value = shell.evaluate(code);
        } catch (Exception e) {
            s_logger.log(Level.INFO, "Bei der Ausf�hrung der Taschenrechnerberechnung mit Groovy ist ein Fehler aufgetreten.", e);
            return e;
        }
        merkeLetztesErgebnis(value);
        return value;
    }

    /**
     * Setzt die Variable f�r das letzte Ergebnis
     * @param ergebnis Das Ergebnis
     * @see TaschenrechnerController#m_sLastResultVariable
     */
    private void merkeLetztesErgebnis(Object ergebnis) {
        s_logger.info(String.format("Letztes Ergebnis: [%s]", ergebnis));
        if (ergebnis != null) {
            s_logger.info("Letzter Ergebnistyp: " + ergebnis.getClass().getName());
            getGroovyShell().setVariable(m_sLastResultVariable, ergebnis);
        } else {
            s_logger.info("Letztes Ergebnis war null, es wurde nicht gespeichert.");
        }
    }
}
