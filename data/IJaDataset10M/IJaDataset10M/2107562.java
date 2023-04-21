package org.jz.decliner.russian.tests.division;

import java.sql.*;
import javax.swing.*;
import java.awt.Component;
import org.jz.decliner.russian.language.Gender;
import org.jz.decliner.russian.name.*;
import org.jz.decliner.russian.division.*;
import org.jz.decliner.russian.utils.*;

public class DivisionDecliner {

    public DivisionDecliner() throws Exception {
    }

    public void finalize() {
    }

    public static void logSample(Component _owner, int _Id, String _patroname, int _gender, int _DeclensionInfo) {
        try {
            if ((_patroname == null) || (_patroname.equals(""))) {
                JOptionPane.showMessageDialog(_owner, "Nominative case of the division must be entered", "Division Decliner", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Paradigm p = Division.getDivisionParadigm(_patroname, _gender, _DeclensionInfo);
            ParadigmUtils.printParadigm(p);
            if (JOptionPane.showConfirmDialog(_owner, ParadigmUtils.getHtmlParadigm(p), "Log this Division Sample?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                JdbcParadigmPersistence.storeParadigm(_Id, p, JdbcLogger.getConnection(), "division");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(_owner, "<html>Error decline : " + e.getMessage(), "Division Decliner", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static String compareStrings(String _Case1, String _Case2) {
        String res = "";
        for (int i = 0; i < _Case1.length(); i++) {
            if (i < _Case2.length()) {
                if (_Case1.toLowerCase().charAt(i) == _Case2.toLowerCase().charAt(i)) {
                    res = res + _Case1.charAt(i);
                } else {
                    res = res + "<font color=red>" + _Case1.charAt(i) + "</font>";
                }
            } else {
                res = res + _Case1.charAt(i);
            }
        }
        return res;
    }

    public static String makeComparisonRow(String _Caption, String _Case1, String _Case2) {
        String res = "<tr><td>" + _Caption + "</td><td>" + compareStrings(_Case1, _Case2) + "</td><td>" + compareStrings(_Case2, _Case1) + "</td><td>";
        if (_Case1.equalsIgnoreCase(_Case2)) {
            res = res + "ok</td></tr>";
        } else {
            res = res + "<font color=red>MISM</font></td></tr>";
        }
        return res;
    }

    public static String makeComparisonTable(Name _Name1, Name _Name2) {
        String res = "<table border cellspacing=0>";
        res = res + "<tr><th>Case</th><th>Stored</th><th>Calculated</th><th>St</th></tr>";
        res = res + makeComparisonRow("Nominative", _Name1.getNominative(), _Name2.getNominative());
        res = res + makeComparisonRow("Genetive", _Name1.getGenetive(), _Name2.getGenetive());
        res = res + makeComparisonRow("Dative", _Name1.getDative(), _Name2.getDative());
        res = res + makeComparisonRow("Accusative", _Name1.getAccusative(), _Name2.getAccusative());
        res = res + makeComparisonRow("Instrumental", _Name1.getInstrumental(), _Name2.getInstrumental());
        res = res + makeComparisonRow("Prepositional", _Name1.getPrepositional(), _Name2.getPrepositional());
        res = res + "</table>";
        return res;
    }

    public static void checkAllSamples(Component _owner) {
        try {
            Connection con = JdbcLogger.getConnection();
            PreparedStatement stms = con.prepareStatement("select * " + "from Division_samples " + "order by Division_dt, Division_name");
            ResultSet sample = stms.executeQuery();
            while (sample.next()) {
                int id = sample.getInt("division_id");
                String name = sample.getString("division_name");
                int declensionInfo = sample.getInt("division_dt");
                System.out.println(id + ". " + name + " " + declensionInfo);
                Division pn = null;
                pn = new Division(name.trim(), 0, declensionInfo);
                JdbcName jn = new JdbcName(JdbcLogger.getConnection(), "division", id, 0, declensionInfo);
                if (!jn.isFound()) {
                    logSample(_owner, id, name, 0, declensionInfo);
                } else {
                    if (!ParadigmUtils.areParadigmsEqualIgnoreCase(pn, jn)) {
                        ParadigmUtils.printParadigm(pn);
                        JOptionPane.showMessageDialog(_owner, "<html>Invalid declension result (" + name + ")<p>" + makeComparisonTable(jn, pn), "Division name decliner", JOptionPane.WARNING_MESSAGE);
                        System.err.println(name);
                    }
                }
            }
            sample.close();
            stms.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            String[] samples = {};
            for (int i = 0; i < samples.length; i++) {
                Paradigm d = Division.getDivisionParadigm(samples[i], 0, 0);
                ParadigmUtils.printParadigm(d);
            }
            checkAllSamples(new JFrame());
            JdbcLogger.flushConnection();
            System.gc();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
