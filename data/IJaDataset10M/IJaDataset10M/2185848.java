package mainPackage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.awt.Cursor;
import javax.swing.*;
import dbConnPackage.DBDavide;
import java.nio.charset.Charset;

/**
 * @author francesco
 *
 * Per modificare il modello associato al commento di questo tipo generato, aprire
 * Finestra&gt;Preferenze&gt;Java&gt;Generazione codice&gt;Codice e commenti
 */
public class EsportaPratiche {

    private JFrame frame = null;

    private ModelloTabella tabPratiche = null;

    private static final String vv = "\"";

    private static final String v = CostantiDavide.sepCSV;

    private static final String nl = "\n";

    EsportaPratiche(JFrame principale, ModelloTabella tab) {
        frame = principale;
        tabPratiche = tab;
    }

    void esporta() {
        try {
            ResultSet rs = tabPratiche.getModel();
            String nomeFile = CostantiDavide.selFile(frame, "csv", "File CSV");
            if (nomeFile != null) {
                if (salvaFilePratiche(nomeFile, rs)) {
                    JOptionPane.showMessageDialog(frame, Messages.getString("Generale.file_successo1") + nomeFile + Messages.getString("Generale.file_successo2"), Messages.getString("ScegliAnagrafeDaEsportare.Conferma_!_38"), JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame, Messages.getString("Generale.file_errore1") + nomeFile + Messages.getString("Generale.file_errore2"), Messages.getString("ScegliAnagrafeDaEsportare.Errore_!_41"), JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            mainPackage.CostantiDavide.msgEccezione("Eccezione in ok... di EsportaPratiche");
            e.printStackTrace();
        }
    }

    private boolean salvaFilePratiche(String nomeFile, ResultSet rs) {
        boolean retVal = true;
        FileOutputStream fos = null;
        int pos = 1;
        try {
            fos = new FileOutputStream(nomeFile, true);
            fos.write(getIntestazione().getBytes());
            String buf = "";
            pos = rs.getRow();
            rs.beforeFirst();
            frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            while (rs.next()) {
                String codicePratica = rs.getString("codice");
                String codice = vv + codicePratica + vv;
                String descrizione = vv + rs.getString("descrizione") + vv;
                String oggetto = vv + rs.getString("oggetto") + vv;
                ResultSet rsPrat = DBDavide.selPratica(codicePratica);
                String autorita = "";
                String sede = "";
                String giudice = "";
                String procedura = "";
                String domiciliatario = "";
                String stato = "";
                String note = "";
                int nRif = 0;
                String dominus = "";
                if (rsPrat.next()) {
                    autorita = DBDavide.mioGetString(rsPrat, "descraut");
                    sede = DBDavide.mioGetString(rsPrat, "sede");
                    giudice = DBDavide.mioGetString(rsPrat, "nome_giudi");
                    stato = DBDavide.mioGetString(rsPrat, "descrstato");
                    nRif = rsPrat.getInt("rif");
                    dominus = DBDavide.mioGetString(rsPrat, "NOMEDOMINUS");
                    procedura = DBDavide.mioGetString(rsPrat, "DESCR");
                    domiciliatario = DBDavide.mioGetString(rsPrat, "cognome") + " " + DBDavide.mioGetString(rsPrat, "nome");
                    note = DBDavide.mioGetString(rsPrat, "note");
                }
                rsPrat.close();
                autorita = vv + eliminaVirgolette(autorita) + vv;
                sede = vv + eliminaVirgolette(sede) + vv;
                giudice = vv + eliminaVirgolette(giudice) + vv;
                stato = vv + eliminaVirgolette(stato) + vv;
                dominus = vv + eliminaVirgolette(dominus) + vv;
                procedura = vv + eliminaVirgolette(procedura) + vv;
                domiciliatario = vv + eliminaVirgolette(domiciliatario) + vv;
                note = vv + eliminaVirgolette(note) + vv;
                buf = descrizione + v + oggetto + v + nRif + v + autorita + v + sede + v + giudice + v + dominus + v + procedura + v + domiciliatario + v + stato + v + note + nl;
                fos.write(buf.getBytes(Charset.forName("ISO-8859-1")));
            }
        } catch (Exception e) {
            mainPackage.CostantiDavide.msgEccezione("Eccezione in salvaFilePratiche(String nomeFile, ResultSet rs) di ScegliAnagrafeDaEsportare");
            e.printStackTrace();
            retVal = false;
        } finally {
            frame.setCursor(Cursor.getDefaultCursor());
            try {
                fos.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            try {
                rs.absolute(pos);
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        }
        return retVal;
    }

    /**
     *
     */
    private String getIntestazione() {
        String retVal = Messages.getString("FinestraPrincipale.Pratica_338") + v + Messages.getString("FinestraPrincipale.Oggetto_379") + v + Messages.getString("FinestraPrincipale.N._Rif._366") + v + Messages.getString("FinestraPrincipale.Autorita090") + v + Messages.getString("FinestraPrincipale.Sede_505") + v + Messages.getString("FinestraPrincipale.Giudice_301") + v + Messages.getString("FinestraPrincipale.Dominus_236") + v + Messages.getString("FinestraPrincipale.Procedura_447") + v + Messages.getString("FinestraPrincipale.Domiciliatario_232") + v + Messages.getString("FinestraPrincipale.Stato_534") + v + Messages.getString("FinestraPrincipale.Note_263") + nl;
        return retVal;
    }

    private String eliminaVirgolette(String testo) {
        return testo.replace("\"", "\"\"");
    }
}
