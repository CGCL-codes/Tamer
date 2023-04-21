package net.tygerstar.android;

import net.tygerstar.android.negocio.CadastroController;
import net.tygerstar.android.negocio.FotoController;
import net.tygerstar.android.negocio.Funcoes;
import net.tygerstar.android.negocio.ListagemController;
import net.tygerstar.android.negocio.ListagemQAController;
import net.tygerstar.android.negocio.LocalizacaoController;
import net.tygerstar.android.negocio.MapaController;
import net.tygerstar.android.negocio.SlideShowController;
import net.tygerstar.android.negocio.TransmissaoController;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AppCadCli extends Activity {

    private Button btCadastro, btListagem, btListagemQA, btLocalizacao, btFoto, btTransmissao, btMapa, btSlideShow;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);
        Funcoes.locationmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        inicializarViews();
        Funcoes.database = openOrCreateDatabase("clientes", Context.MODE_PRIVATE, null);
        String msgBanco = Funcoes.inicializarBanco();
        Funcoes.database.close();
        Funcoes.database = openOrCreateDatabase("clientes", Context.MODE_PRIVATE, null);
        AlertDialog.Builder aviso = new AlertDialog.Builder(AppCadCli.this);
        aviso.setTitle("Aviso");
        aviso.setMessage(msgBanco);
        aviso.setNeutralButton("OK", null);
        aviso.show();
        btCadastro.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(AppCadCli.this, CadastroController.class);
                startActivity(i);
            }
        });
        btLocalizacao.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(AppCadCli.this, LocalizacaoController.class);
                startActivity(i);
            }
        });
        btListagem.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(AppCadCli.this, ListagemController.class);
                startActivity(i);
            }
        });
        btListagemQA.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(AppCadCli.this, ListagemQAController.class);
                startActivity(i);
            }
        });
        btFoto.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(AppCadCli.this, FotoController.class);
                startActivity(i);
            }
        });
        btTransmissao.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(AppCadCli.this, TransmissaoController.class);
                startActivity(i);
            }
        });
        btMapa.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(AppCadCli.this, MapaController.class));
            }
        });
        btSlideShow.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(AppCadCli.this, SlideShowController.class));
            }
        });
        Funcoes.inicializarBanco();
    }

    private void inicializarViews() {
        btCadastro = (Button) findViewById(R.id.btTelaCadastro);
        btListagem = (Button) findViewById(R.id.btTelaListagem);
        btListagemQA = (Button) findViewById(R.id.btTelaListagemQA);
        btLocalizacao = (Button) findViewById(R.id.btTelaGPS);
        btFoto = (Button) findViewById(R.id.btTelaFoto);
        btTransmissao = (Button) findViewById(R.id.btTelaTransmitir);
        btMapa = (Button) findViewById(R.id.btTelaMapa);
        btSlideShow = (Button) findViewById(R.id.btTelaSlideShow);
    }
}
