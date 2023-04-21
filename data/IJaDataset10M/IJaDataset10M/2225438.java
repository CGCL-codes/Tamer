package br.gov.sjc.socialalimenta;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;
import br.gov.sjc.export.exportarDados;

/**
 * @author rodrigo.ramos
 *
 */
public class NumeroSias {

    public static int NumeroDaFamilia;

    public static int NumeroDoMembroDaFamilia;

    public static int NumeroDaEtapa;

    public static int TecnicoResponsavel;

    public static void main(String[] args) {
        NumeroSias global = new NumeroSias();
        global.getNumeroDaFamilia();
        NumeroSias globalN = new NumeroSias();
        globalN.getNumeroDoMembroDaFamilia();
        NumeroSias globalE = new NumeroSias();
        globalE.getNumeroDaEtapa();
        NumeroSias globalT = new NumeroSias();
        globalT.getTecnicoResponsavel();
    }

    public int getTecnicoResponsavel() {
        return NumeroSias.TecnicoResponsavel;
    }

    public int getNumeroDaFamilia() {
        return NumeroSias.NumeroDaFamilia;
    }

    public int getNumeroDoMembroDaFamilia() {
        return NumeroSias.NumeroDoMembroDaFamilia;
    }

    public int getNumeroDaEtapa() {
        return NumeroSias.NumeroDaEtapa;
    }

    public void setNumeroDaFamilia(int Numero) {
        NumeroSias.NumeroDaFamilia = Numero;
    }

    public void setNumeroMembroDaFamilia(int Numero) {
        NumeroSias.NumeroDaFamilia = Numero;
    }

    public void setNumeroDaEtapa(int Numero) {
        NumeroSias.NumeroDaEtapa = Numero;
    }

    public void setTecnicoResponsavel(int Numero) {
        NumeroSias.TecnicoResponsavel = Numero;
    }

    public void addNumeroDoMembroDaFamilia() {
        int idMembro = 0;
        try {
            Log.v("NumeroSias", "addNumeroDoMembroDaFamilia() iniciou");
            Log.v("NumeroSias", "addNumeroDoMembroDaFamilia() NumeroSias.NumeroDaFamilia: " + NumeroSias.NumeroDaFamilia);
            Cursor cVerifica = null;
            SQLiteDatabase BancoDados = GrupoFamiliarActivity.BancoDados;
            String SQLQuery = "Select * from tab_SocialA_GrupoFamiliar WHERE SiasN=" + NumeroSias.NumeroDaFamilia + " Order By idMembro ASC";
            cVerifica = BancoDados.rawQuery(SQLQuery, null);
            int contaCursor = cVerifica.getCount();
            Log.v("NumeroSias", "addNumeroDoMembroDaFamilia() contaCursor: " + contaCursor);
            if (contaCursor != 0) {
                cVerifica.moveToLast();
                int numero = cVerifica.getInt(cVerifica.getColumnIndex("idMembro"));
                Log.v("NumeroSias", "addNumeroDoMembroDaFamilia() numero: " + numero);
                idMembro = contaCursor + 1;
            } else {
                idMembro = 1;
            }
            Log.i("NumeroSias", "addNumeroDoMembroDaFamilia() idMembro: " + idMembro);
        } catch (Exception E) {
            Log.e("NumeroSias", "addNumeroDoMembroDaFamilia() ERRO: " + E.getMessage());
        } finally {
            Log.v("NumeroSias", "addNumeroDoMembroDaFamilia() finally");
        }
        setNumeroDoMembroDaFamilia(idMembro);
        gravaIdMembro(getNumeroDaFamilia(), getNumeroDoMembroDaFamilia(), NumeroSias.NumeroDaFamilia + "/" + idMembro);
    }

    public void setZeroNumeroDoMembroDaFamilia() {
        NumeroSias.NumeroDoMembroDaFamilia = 1;
    }

    public void setNumeroDoMembroDaFamilia(int Num) {
        NumeroSias.NumeroDoMembroDaFamilia = Num;
        Log.v("NumeroSias", "setNumeroDoMembroDaFamilia() finally Num:" + Num);
    }

    public void gravaIdMembro(int NumSias, int idMembroSias, String Desc) {
        try {
            SQLiteDatabase BD = GrupoFamiliarActivity.BancoDados;
            String SQL = "INSERT INTO tab_SocialA_GrupoFamiliar(SiasN, idMembro ,  Desc ) VALUES (" + NumSias + ", " + idMembroSias + ",'" + Desc + "')";
            BD.execSQL(SQL);
            Log.i("NumeroSias", "gravaIdMembro passou");
        } catch (Exception errr) {
            Log.e("NumeroSias", "gravaIdMembro ERRO " + errr.getMessage());
        } finally {
        }
    }
}
