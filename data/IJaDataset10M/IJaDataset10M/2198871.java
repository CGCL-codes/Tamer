package globali.registratoriCassa;

import globali.jcPostgreSQL;
import globali.jcVariabili;
import java.io.*;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Papini Sascha
 */
public class jcDataProcessSilver40 {

    private int MAX_LUNGHEZZA_NOME = 27;

    private char DIV = (char) 127;

    public void generaScontrino(int docID, int pagamento) {
        try {
            FileWriter outFile = new FileWriter(jcVariabili.REGISTRATORI_CASSA_DIR + jcVariabili.REGISTRATORI_CASSA_TXT_NAME);
            PrintWriter out = new PrintWriter(outFile);
            jcPostgreSQL.queryDB("SELECT o.listinoid, l.nome, o.nome AS nomediverso, o.qnt, l.um , o.um AS umdiversa, o.p_vendita, o.sconto, o.iva, l.qnt AS qntnegozio, " + " l.alistino_grossista, o.daordinare, o.ordine_oggettiid FROM ordini_oggetti AS o " + " LEFT JOIN listino AS l ON (o.listinoid=l.listinoid) " + " WHERE o.ordineid=" + docID + " ORDER BY o.ordine_oggettiid ");
            try {
                String REP = "";
                String NOME = "";
                Double TOT = 0.00;
                while (jcPostgreSQL.query.next()) {
                    if (jcPostgreSQL.query.getDouble("iva") == 4.0) REP = jcVariabili.REGISTRATORI_CASSA_REP_IVA_4; else if (jcPostgreSQL.query.getDouble("iva") == 10.0) REP = jcVariabili.REGISTRATORI_CASSA_REP_IVA_10; else REP = jcVariabili.REGISTRATORI_CASSA_REP_IVA_20;
                    TOT = (jcPostgreSQL.query.getDouble("p_vendita") - jcPostgreSQL.query.getDouble("sconto")) * jcPostgreSQL.query.getDouble("qnt");
                    TOT = TOT + (TOT * jcPostgreSQL.query.getDouble("iva") / 100);
                    java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
                    String TOT2 = df.format(TOT);
                    TOT2 = TOT2.replace(".", "");
                    TOT2 = TOT2.replace(",", "");
                    NOME = jcPostgreSQL.query.getInt("listinoid") == -5 ? jcPostgreSQL.query.getString("nomediverso").trim() : jcPostgreSQL.query.getString("nome").trim();
                    NOME = NOME.replace("\n", " ");
                    if (NOME.length() > MAX_LUNGHEZZA_NOME) {
                        NOME = NOME.substring(0, MAX_LUNGHEZZA_NOME - 1);
                    }
                    out.println("1" + DIV + NOME + DIV + "1000" + DIV + TOT2 + DIV + "1" + DIV + DIV + DIV + "1" + DIV + "1" + DIV);
                }
            } catch (SQLException ex) {
                Logger.getLogger(jcDitron.class.getName()).log(Level.SEVERE, null, ex);
                if (jcVariabili.DEBUG) {
                    globali.jcFunzioni.erroreSQL(ex.toString());
                }
            }
            if (jcVariabili.REGISTRATORI_CASSA_TESTO_FINALE.length() != 0) {
                String T[] = jcVariabili.REGISTRATORI_CASSA_TESTO_FINALE.split("@#@");
                for (int i = 0; i < T.length; i++) {
                    out.println("17" + DIV + "1" + DIV + Integer.toString(i + 1) + DIV + T[i] + DIV);
                }
            }
            String PAG = "1";
            if (pagamento == 0) PAG = "1";
            out.println("2" + DIV + "1" + DIV + "0" + DIV + DIV + DIV + DIV + DIV + DIV + DIV);
            out.close();
            if (jcVariabili.REGISTRATORI_CASSA_PROG_AFTER_TXT.length() != 0) {
                Process p = Runtime.getRuntime().exec(jcVariabili.REGISTRATORI_CASSA_PROG_AFTER_TXT + " " + jcVariabili.REGISTRATORI_CASSA_DIR + jcVariabili.REGISTRATORI_CASSA_TXT_NAME);
                try {
                    p.waitFor();
                } catch (InterruptedException ex) {
                    Logger.getLogger(jcDitron.class.getName()).log(Level.SEVERE, null, ex);
                    if (jcVariabili.DEBUG) {
                        globali.jcFunzioni.erroreSQL(ex.toString());
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Erroe: " + e.toString());
            if (jcVariabili.DEBUG) {
                globali.jcFunzioni.erroreSQL(e.toString());
            }
        }
    }
}
