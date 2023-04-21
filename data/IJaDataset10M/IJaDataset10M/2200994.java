package br.gov.sjc.socialalimenta;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author rodrigo.ramos
 *
 */
public class IndSegAlimentarActivity extends Activity {

    public static SQLiteDatabase BancoDados = null;

    public static String nomeBanco = "SocialAlimenta";

    public void openDB() {
        try {
            BancoDados = openOrCreateDatabase(nomeBanco, MODE_WORLD_READABLE, null);
            Log.v("IndSegAlimentarActivity", "BancoDados.openOrCreateDatabase();");
        } catch (Exception E) {
            Log.e("IndSegAlimentarActivity", "Erro em BancoDados.close();" + E.getMessage());
        }
    }

    public void closeDB() {
        BancoDados.close();
        Log.v("IndSegAlimentarActivity", "BancoDados.close();");
    }

    EditText tbIntoOutra;

    CheckBox cbGestante, cbAcompMedGest, cbAcompNutGest, cbNutriz, cbAcompMedNutriz;

    CheckBox cbAcompNutNutriz, cbIntolerancia, cbAcompMedInto, cbAcompNutInto, cbIntoLactose;

    CheckBox cbIntoGluten, cbIntoProteina, cBancoDadosiabetes, cbAcompMedDiabetes, cbAcompNutDiabetes;

    CheckBox cbAnemia, cbAcompMedAnemia, cbAcompNutAnemia, cbHipertensao, cbAcompMedHipert;

    CheckBox cbAcompNutHipert, cbObesidade, cbAcompMedObesidade, cbAcompNutObesidade;

    NumeroSias ns = new NumeroSias();

    int NumSias = ns.getNumeroDaFamilia();

    int ID;

    Cursor cVerifica;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.indicadorsegurancaalimentar);
        openDB();
        cbGestante = (CheckBox) findViewById(R.indsegalimentar.cbGestante);
        cbAcompMedGest = (CheckBox) findViewById(R.indsegalimentar.cbAcompMedGest);
        cbAcompNutGest = (CheckBox) findViewById(R.indsegalimentar.cbAcompNutGest);
        cbNutriz = (CheckBox) findViewById(R.indsegalimentar.cbNutriz);
        cbAcompMedNutriz = (CheckBox) findViewById(R.indsegalimentar.cbAcompMedNutriz);
        cbAcompNutNutriz = (CheckBox) findViewById(R.indsegalimentar.cbAcompNutNutriz);
        cbIntolerancia = (CheckBox) findViewById(R.indsegalimentar.cbIntolerancia);
        cbAcompMedInto = (CheckBox) findViewById(R.indsegalimentar.cbAcompMedInto);
        cbAcompNutInto = (CheckBox) findViewById(R.indsegalimentar.cbAcompNutInto);
        cbIntoLactose = (CheckBox) findViewById(R.indsegalimentar.cbIntoLactose);
        cbIntoGluten = (CheckBox) findViewById(R.indsegalimentar.cbIntoGluten);
        cbIntoProteina = (CheckBox) findViewById(R.indsegalimentar.cbIntoProteina);
        cBancoDadosiabetes = (CheckBox) findViewById(R.indsegalimentar.cbDiabetes);
        cbAcompMedDiabetes = (CheckBox) findViewById(R.indsegalimentar.cbAcompMedDiabetes);
        cbAcompNutDiabetes = (CheckBox) findViewById(R.indsegalimentar.cbAcompNutDiabetes);
        cbAnemia = (CheckBox) findViewById(R.indsegalimentar.cbAnemia);
        cbAcompMedAnemia = (CheckBox) findViewById(R.indsegalimentar.cbAcompMedAnemia);
        cbAcompNutAnemia = (CheckBox) findViewById(R.indsegalimentar.cbAcompNutAnemia);
        cbHipertensao = (CheckBox) findViewById(R.indsegalimentar.cbHipertensao);
        cbAcompMedHipert = (CheckBox) findViewById(R.indsegalimentar.cbAcompMedHipert);
        cbAcompNutHipert = (CheckBox) findViewById(R.indsegalimentar.cbAcompNutHipert);
        cbObesidade = (CheckBox) findViewById(R.indsegalimentar.cbObesidade);
        cbAcompMedObesidade = (CheckBox) findViewById(R.indsegalimentar.cbAcompMedObesidade);
        cbAcompNutObesidade = (CheckBox) findViewById(R.indsegalimentar.cbAcompNutObesidade);
        tbIntoOutra = (EditText) findViewById(R.indsegalimentar.tbIntoOutra);
        if (verificaRegistro(NumSias)) {
            setarDados(ID);
            Log.v("IndSegAlimentarActivity", "onCreate() Com dados do SiasN: " + NumSias + "ID: " + ID);
        } else {
            Log.v("IndSegAlimentarActivity", "onCreate() Com dados vazios ");
        }
        Button btnISAVoltar = (Button) findViewById(R.indsegalimentar.btnVoltar);
        btnISAVoltar.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent ittISAVoltar = new Intent(getApplicationContext(), GrupoFamiliarActivity.class);
                closeDB();
                startActivity(ittISAVoltar);
            }
        });
        Button btnISAPx = (Button) findViewById(R.indsegalimentar.btnPx);
        btnISAPx.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                if (verificaRegistro(NumSias)) {
                    alteraRegistro(ID);
                } else {
                    salvaDados(NumSias, cbGestante.isChecked(), cbAcompMedGest.isChecked(), cbAcompNutGest.isChecked(), cbNutriz.isChecked(), cbAcompMedNutriz.isChecked(), cbAcompNutNutriz.isChecked(), cbIntolerancia.isChecked(), cbIntoLactose.isChecked(), cbIntoGluten.isChecked(), cbIntoProteina.isChecked(), tbIntoOutra.getText().toString(), cbAcompMedInto.isChecked(), cbAcompNutInto.isChecked(), cBancoDadosiabetes.isChecked(), cbAcompMedDiabetes.isChecked(), cbAcompNutDiabetes.isChecked(), cbAnemia.isChecked(), cbAcompMedAnemia.isChecked(), cbAcompNutAnemia.isChecked(), cbHipertensao.isChecked(), cbAcompMedHipert.isChecked(), cbAcompNutHipert.isChecked(), cbObesidade.isChecked(), cbAcompMedObesidade.isChecked(), cbAcompNutObesidade.isChecked());
                }
                Intent ittISAPx = new Intent(getApplicationContext(), AcessoAlimentacaoActivity.class);
                closeDB();
                startActivity(ittISAPx);
            }
        });
    }

    /** 
     * tab_SocialA_IndSegAlimen(_id INTEGER PRIMARY KEY, 
     * SiasN INT, Gestante TEXT,GestanteAcompMedico TEXT,GestanteAcompNutric TEXT,Nutriz TEXT,NutrizAcompMedico TEXT,NutrizAcompNutric TEXT,
     * Intolerancia TEXT,IntoleranciaLactose TEXT,IntoleranciaGluten TEXT,IntoleranciaProteina TEXT,IntoleranciaOutra TEXT,
     * IntoleranciaAcompMedico TEXT,IntoleranciaAcompNutric TEXT,Diabete TEXT,DiabeteAcompMedico TEXT,DiabeteAcompNutric TEXT,Anemia TEXT,
     * AnemiaAcompMedico TEXT,AnemiaAcompNutric TEXT,Hipertensao TEXT,HipertensaoAcompMedico TEXT,HipertensaoAcompNutric TEXT,Obesidade TEXT,
     * ObesidadeAcompMedico TEXT,ObesidadeAcompNutric TEXT
     * @param string SiasN INT
     * @param checked  Gestante TEXT
     * @param checked2 GestanteAcompMedico TEXT
     * @param checked3 GestanteAcompNutric TEXT
     * @param checked4 Nutriz TEXT
     * @param checked5 NutrizAcompMedico TEXT
     * @param checked6 NutrizAcompNutric TEXT
     * @param checked7 Intolerancia TEXT
     * @param checked8 IntoleranciaLactose TEXT
     * @param checked9 IntoleranciaGluten TEXT
     * @param checked10 IntoleranciaProteina TEXT
     * @param checked11 IntoleranciaOutra TEXT
     * @param checked12 IntoleranciaAcompMedico TEXT
     * @param checked13 IntoleranciaAcompNutric TEXT
     * @param checked14 Diabete TEXT
     * @param checked15 DiabeteAcompMedico TEXT
     * @param checked16 DiabeteAcompNutric TEXT
     * @param checked17 Anemia TEXT
     * @param checked18 AnemiaAcompMedico TEXT
     * @param checked19 AnemiaAcompNutric TEXT
     * @param checked20 Hipertensao TEXT
     * @param checked21 HipertensaoAcompMedico TEXT
     * @param checked22 HipertensaoAcompNutric TEXT
     * @param checked23 Obesidade TEXT
     * @param checked24 ObesidadeAcompMedico TEXT
     * @param checked25 ObesidadeAcompNutric TEXT
     */
    public void salvaDados(int string, boolean checked, boolean checked2, boolean checked3, boolean checked4, boolean checked5, boolean checked6, boolean checked7, boolean checked8, boolean checked9, boolean checked10, String checked11, boolean checked12, boolean checked13, boolean checked14, boolean checked15, boolean checked16, boolean checked17, boolean checked18, boolean checked19, boolean checked20, boolean checked21, boolean checked22, boolean checked23, boolean checked24, boolean checked25) {
        try {
            String SQL = "INSERT INTO tab_SocialA_IndSegAlimen(SiasN, Gestante ,GestanteAcompMedico ,GestanteAcompNutric ,Nutriz ,NutrizAcompMedico ,NutrizAcompNutric ,Intolerancia ,IntoleranciaLactose ,IntoleranciaGluten,IntoleranciaProteina,IntoleranciaOutra,IntoleranciaAcompMedico ,IntoleranciaAcompNutric ,Diabete ,DiabeteAcompMedico,DiabeteAcompNutric,Anemia,AnemiaAcompMedico ,AnemiaAcompNutric,Hipertensao,HipertensaoAcompMedico,HipertensaoAcompNutric,Obesidade,ObesidadeAcompMedico,ObesidadeAcompNutric ) " + "VALUES (" + string + ", '" + checked + "','" + checked2 + "','" + checked3 + "','" + checked4 + "','" + checked5 + "','" + checked6 + "','" + checked7 + "','" + checked8 + "','" + checked9 + "','" + checked10 + "','" + checked11 + "','" + checked12 + "','" + checked13 + "','" + checked14 + "','" + checked15 + "','" + checked16 + "','" + checked17 + "','" + checked18 + "','" + checked19 + "','" + checked20 + "','" + checked21 + "','" + checked22 + "','" + checked23 + "','" + checked24 + "','" + checked25 + "')";
            BancoDados.execSQL(SQL);
            Toast.makeText(this, "Gravando Dados da Fam�lia:" + NumSias, Toast.LENGTH_LONG).show();
        } catch (Exception erro) {
            mensagemAlerta("Erro", "Erro Ao Gravar ");
            Log.e("IndSegAlimentarActivity", "Erro em BancoDados.close();" + erro.getMessage());
        }
    }

    public boolean verificaRegistro(int NumeroSias) {
        boolean Retorno = false;
        try {
            String SQLQuery = "Select _id from tab_SocialA_IndSegAlimen WHERE SiasN=" + NumeroSias + " ";
            cVerifica = BancoDados.rawQuery(SQLQuery, null);
            if (cVerifica.getCount() != 0) {
                cVerifica.moveToFirst();
                ID = cVerifica.getInt(cVerifica.getColumnIndex("_id"));
                Retorno = true;
                Log.i("IndSegAlimentarActivity", "verificaRegistro() TRUE ");
            }
        } catch (Exception Err) {
            mensagemAlerta("Erro", "Erro Encontrado");
            Log.e("IndSegAlimentarActivity", "verificaRegistro() " + Err.getMessage());
        }
        return Retorno;
    }

    public void setarDados(int Posicao) {
        try {
            String SQL2 = "SELECT * FROM tab_SocialA_IndSegAlimen WHERE _id=" + Posicao + " ";
            Log.v("IndSegAlimentarActivity", "setarDados() Posi��o: " + Posicao);
            Cursor c = BancoDados.rawQuery(SQL2, null);
            c.moveToFirst();
            do {
                String Gestante = c.getString(c.getColumnIndex("Gestante"));
                if (Gestante.equals("true")) {
                    cbGestante.setChecked(true);
                } else {
                    cbGestante.setChecked(false);
                }
                String GestanteAcompMedico = c.getString(c.getColumnIndex("GestanteAcompMedico"));
                if (GestanteAcompMedico.equals("true")) {
                    cbAcompMedGest.setChecked(true);
                } else {
                    cbAcompMedGest.setChecked(false);
                }
                String GestanteAcompNutric = c.getString(c.getColumnIndex("GestanteAcompNutric"));
                if (GestanteAcompNutric.equals("true")) {
                    cbAcompNutGest.setChecked(true);
                } else {
                    cbAcompNutGest.setChecked(false);
                }
                String Nutriz = c.getString(c.getColumnIndex("Nutriz"));
                if (Nutriz.equals("true")) {
                    cbNutriz.setChecked(true);
                } else {
                    cbNutriz.setChecked(false);
                }
                String NutrizAcompMedico = c.getString(c.getColumnIndex("NutrizAcompMedico"));
                if (NutrizAcompMedico.equals("true")) {
                    cbAcompMedNutriz.setChecked(true);
                } else {
                    cbAcompMedNutriz.setChecked(false);
                }
                String NutrizAcompNutric = c.getString(c.getColumnIndex("NutrizAcompNutric"));
                if (NutrizAcompNutric.equals("true")) {
                    cbAcompNutNutriz.setChecked(true);
                } else {
                    cbAcompNutNutriz.setChecked(false);
                }
                String Intolerancia = c.getString(c.getColumnIndex("Intolerancia"));
                if (Intolerancia.equals("true")) {
                    cbIntolerancia.setChecked(true);
                } else {
                    cbIntolerancia.setChecked(false);
                }
                String IntoleranciaAcompMedico = c.getString(c.getColumnIndex("IntoleranciaAcompMedico"));
                if (IntoleranciaAcompMedico.equals("true")) {
                    cbAcompMedInto.setChecked(true);
                } else {
                    cbAcompMedInto.setChecked(false);
                }
                String IntoleranciaAcompNutric = c.getString(c.getColumnIndex("IntoleranciaAcompNutric"));
                if (IntoleranciaAcompNutric.equals("true")) {
                    cbAcompNutInto.setChecked(true);
                } else {
                    cbAcompNutInto.setChecked(false);
                }
                String IntoleranciaLactose = c.getString(c.getColumnIndex("IntoleranciaLactose"));
                if (IntoleranciaLactose.equals("true")) {
                    cbIntoLactose.setChecked(true);
                } else {
                    cbIntoLactose.setChecked(false);
                }
                String IntoleranciaGluten = c.getString(c.getColumnIndex("IntoleranciaGluten"));
                if (IntoleranciaGluten.equals("true")) {
                    cbIntoGluten.setChecked(true);
                } else {
                    cbIntoGluten.setChecked(false);
                }
                String IntoleranciaProteina = c.getString(c.getColumnIndex("IntoleranciaProteina"));
                if (IntoleranciaProteina.equals("true")) {
                    cbIntoProteina.setChecked(true);
                } else {
                    cbIntoProteina.setChecked(false);
                }
                tbIntoOutra.setText(c.getString(c.getColumnIndex("IntoleranciaOutra")));
                String Diabetes = c.getString(c.getColumnIndex("Diabetes"));
                if (Diabetes.equals("true")) {
                    cBancoDadosiabetes.setChecked(true);
                } else {
                    cBancoDadosiabetes.setChecked(false);
                }
                String DiabetesAcompMedico = c.getString(c.getColumnIndex("DiabetesAcompMedico"));
                if (DiabetesAcompMedico.equals("true")) {
                    cbAcompMedDiabetes.setChecked(true);
                } else {
                    cbAcompMedDiabetes.setChecked(false);
                }
                String DiabetesAcompNutric = c.getString(c.getColumnIndex("DiabetesAcompNutric"));
                if (DiabetesAcompNutric.equals("true")) {
                    cbAcompNutDiabetes.setChecked(true);
                } else {
                    cbAcompNutDiabetes.setChecked(false);
                }
                String Anemia = c.getString(c.getColumnIndex("Anemia"));
                if (Anemia.equals("true")) {
                    cbAnemia.setChecked(true);
                } else {
                    cbAnemia.setChecked(false);
                }
                String AnemiaAcompMedico = c.getString(c.getColumnIndex("AnemiaAcompMedico"));
                if (AnemiaAcompMedico.equals("true")) {
                    cbAcompMedAnemia.setChecked(true);
                } else {
                    cbAcompMedAnemia.setChecked(false);
                }
                String AnemiaAcompNutric = c.getString(c.getColumnIndex("AnemiaAcompNutric"));
                if (AnemiaAcompNutric.equals("true")) {
                    cbAcompNutAnemia.setChecked(true);
                } else {
                    cbAcompNutAnemia.setChecked(false);
                }
                String Hipertensao = c.getString(c.getColumnIndex("Hipertensao"));
                if (Hipertensao.equals("true")) {
                    cbHipertensao.setChecked(true);
                } else {
                    cbHipertensao.setChecked(false);
                }
                String HipertensaoAcompMedico = c.getString(c.getColumnIndex("HipertensaoAcompMedico"));
                if (HipertensaoAcompMedico.equals("true")) {
                    cbAcompMedHipert.setChecked(true);
                } else {
                    cbAcompMedHipert.setChecked(false);
                }
                String HipertensaoAcompNutric = c.getString(c.getColumnIndex("HipertensaoAcompNutric"));
                if (HipertensaoAcompNutric.equals("true")) {
                    cbAcompNutHipert.setChecked(true);
                } else {
                    cbAcompNutHipert.setChecked(false);
                }
                String Obesidade = c.getString(c.getColumnIndex("Obesidade"));
                if (Obesidade.equals("true")) {
                    cbObesidade.setChecked(true);
                } else {
                    cbObesidade.setChecked(false);
                }
                String ObesidadeAcompMedico = c.getString(c.getColumnIndex("ObesidadeAcompMedico"));
                if (ObesidadeAcompMedico.equals("true")) {
                    cbAcompMedObesidade.setChecked(true);
                } else {
                    cbAcompMedObesidade.setChecked(false);
                }
                String ObesidadeAcompNutric = c.getString(c.getColumnIndex("ObesidadeAcompNutric"));
                if (ObesidadeAcompNutric.equals("true")) {
                    cbAcompNutObesidade.setChecked(true);
                } else {
                    cbAcompNutObesidade.setChecked(false);
                }
            } while (c.moveToNext());
            Log.i("IndSegAlimentarActivity", "setarDados() TRUE ");
        } catch (Exception E) {
            mensagemAlerta("Erro", "Ocorreu um erro inesperado");
            Log.e("IndSegAlimentarActivity", "Erro em setarDados() " + E.getMessage());
        }
    }

    public void alteraRegistro(int id) {
        try {
            String uSQL = "UPDATE tab_SocialA_IndSegAlimen SET " + " SiasN='" + NumSias + "'," + " Gestante='" + cbGestante.isChecked() + "'," + " GestanteAcompMedico='" + cbAcompMedGest.isChecked() + "'," + " GestanteAcompNutric='" + cbAcompNutGest.isChecked() + "'," + " Nutriz='" + cbNutriz.isChecked() + "'," + " NutrizAcompMedico='" + cbAcompMedNutriz.isChecked() + "'," + " NutrizAcompNutric='" + cbAcompNutNutriz.isChecked() + "'," + " Intolerancia='" + cbIntolerancia.isChecked() + "'," + " IntoleranciaLactose='" + cbIntoLactose.isChecked() + "'," + " IntoleranciaGluten='" + cbIntoGluten.isChecked() + "'," + " IntoleranciaProteina='" + cbIntoProteina.isChecked() + "'," + " IntoleranciaOutra='" + tbIntoOutra.getText().toString() + "'," + " IntoleranciaAcompMedico='" + cbAcompMedInto.isChecked() + "'," + " IntoleranciaAcompNutric='" + cbAcompNutInto.isChecked() + "'," + " Diabete='" + cBancoDadosiabetes.isChecked() + "'," + " DiabeteAcompMedico='" + cbAcompMedDiabetes.isChecked() + "'," + " DiabeteAcompNutric='" + cbAcompNutDiabetes.isChecked() + "'," + " Anemia='" + cbAnemia.isChecked() + "'," + " AnemiaAcompMedico='" + cbAcompMedAnemia.isChecked() + "'," + " AnemiaAcompNutric='" + cbAcompNutAnemia.isChecked() + "'," + " Hipertensao='" + cbHipertensao.isChecked() + "'," + " HipertensaoAcompMedico='" + cbAcompMedHipert.isChecked() + "'," + " HipertensaoAcompNutric='" + cbAcompNutHipert.isChecked() + "'," + " Obesidade='" + cbObesidade.isChecked() + "'," + " ObesidadeAcompMedico='" + cbAcompMedObesidade.isChecked() + "'," + " ObesidadeAcompNutric='" + cbAcompNutObesidade.isChecked() + "' " + " WHERE _id=" + id + " ";
            BancoDados.execSQL(uSQL);
            Toast.makeText(IndSegAlimentarActivity.this, "Alterado", Toast.LENGTH_LONG).show();
            Log.i("IndSegAlimentarActivity", "alteraRegistro() realizado com sucesso ");
        } catch (Exception Err) {
            mensagemAlerta("Erro", "Ocorreu um erro inesperado");
            Log.e("IndSegAlimentarActivity", "alteraRegistro() erro: " + Err.getMessage());
        }
    }

    public void mensagemAlerta(String AlertTitle, String MSGAlert) {
        AlertDialog.Builder Mensagem = new AlertDialog.Builder(IndSegAlimentarActivity.this);
        Mensagem.setTitle(AlertTitle);
        Mensagem.setMessage(MSGAlert);
        Mensagem.setNeutralButton("Ok", null);
        Mensagem.show();
    }
}
