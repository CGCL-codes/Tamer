package projetofinal.controle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import projetofinal.modelo.Lugar;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class ListaLugares extends ListActivity {

    private ArrayList<HashMap<String, String>> lista;

    private List<Lugar> lugares;

    private static long idCentro;

    protected static final int RESULT_BUSCAR = 1;

    protected static final int BUSCAR = 2;

    protected static final int RESULT_INSERIR = 3;

    protected static final int INSERIR = 4;

    protected static final int RESULT_CADASTRO = 5;

    protected static Repositorio repositorio;

    protected static String nomeCentro;

    protected static String nomeLugar;

    protected static boolean flag_atualiza_tela = false;

    private ListView list_view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_lugares);
        list_view = (ListView) findViewById(android.R.id.list);
        Intent intent;
        repositorio = new Repositorio(getApplicationContext());
        intent = this.getIntent();
        nomeCentro = intent.getStringExtra("nomeCentro").toString();
        idCentro = repositorio.buscarUnidade(nomeCentro).getId();
        ImageButton btAtualizar = (ImageButton) findViewById(R.id.btAtualizarListaLugar);
        btAtualizar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                atualizarListaLugares();
            }
        });
        ImageButton btBuscarListaLugares = (ImageButton) findViewById(R.id.btBuscarListaLugares);
        btBuscarListaLugares.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListaLugares.this, BuscarLugar.class);
                intent.putExtra("idCentro", idCentro);
                startActivityForResult(intent, RESULT_BUSCAR);
            }
        });
        ImageButton btInserirListaLugares = (ImageButton) findViewById(R.id.btInserirListaLugares);
        if (repositorio.TemConexao()) {
            btInserirListaLugares.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (repositorio.TemConexao()) {
                        inserirLugar();
                    } else {
                        AlertDialog.Builder alerta = new AlertDialog.Builder(ListaLugares.this);
                        alerta.setIcon(R.drawable.alerta);
                        alerta.setTitle("Sem acesso � internet");
                        alerta.setMessage("Para cadastrar um novo Lugar ou An�ncio � necess�rio ter acesso � internet.");
                        alerta.setNeutralButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        alerta.show();
                    }
                }
            });
        } else {
            AlertDialog.Builder alerta = new AlertDialog.Builder(ListaLugares.this);
            alerta.setIcon(R.drawable.alerta);
            alerta.setTitle("Falha ao acessar a rede");
            alerta.setMessage("Para cadastrar um novo Lugar ou An�ncio � necess�rio ter acesso � internet.");
            alerta.setNeutralButton("OK", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alerta.show();
        }
        atualizarListaLugares();
    }

    public void atualizarListaLugares() {
        flag_atualiza_tela = false;
        lista = new ArrayList<HashMap<String, String>>();
        lugares = repositorio.listarLugares(nomeCentro, 0);
        for (int i = (lugares.size() - 1); i > -1; i--) {
            HashMap<String, String> item = new HashMap<String, String>();
            item.put("nomeLugar", lugares.get(i).getNomeLugar());
            item.put("quant_topicos", repositorio.buscarTipo(lugares.get(i).getId_tipo()).getNome_tipo() + " - " + repositorio.quantidade_topicos(0, lugares.get(i).getId()) + " t�pico(s)");
            lista.add(item);
        }
        String[] label = new String[] { "nomeLugar", "quant_topicos" };
        int[] layout_id = new int[] { android.R.id.text1, android.R.id.text2 };
        int layoutNativo = android.R.layout.two_line_list_item;
        list_view.setAdapter(new SimpleAdapter(this, lista, layoutNativo, label, layout_id));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int posicao, long id) {
        super.onListItemClick(l, v, posicao, id);
        HashMap<String, String> hash = lista.get(posicao);
        Object key = "nomeLugar";
        nomeLugar = hash.get(key);
        Intent it = new Intent(this, TelaIntermediariaLugar.class);
        it.putExtra("nomeLugar", nomeLugar);
        startActivity(it);
    }

    public void inserirLugar() {
        if (Login.estaLogado == true) {
            Intent it = new Intent(ListaLugares.this, InserirLugar.class);
            it.putExtra("idCentro", idCentro);
            startActivityForResult(it, RESULT_INSERIR);
        } else {
            fazerLoginOuCadastro();
        }
    }

    public void fazerLoginOuCadastro() {
        final CharSequence[] items = { "Login", "Cadastro" };
        AlertDialog.Builder builder = new AlertDialog.Builder(ListaLugares.this);
        builder.setTitle("Usu�rio n�o logado");
        builder.setIcon(R.drawable.alerta);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Login")) {
                    fazerLogin();
                } else {
                    Intent it = new Intent(ListaLugares.this, Cadastro.class);
                    startActivityForResult(it, RESULT_CADASTRO);
                }
            }
        });
        builder.show();
    }

    public void fazerLogin() {
        AlertDialog.Builder alert = new AlertDialog.Builder(ListaLugares.this);
        alert.setTitle("Fa�a o seu login");
        alert.setIcon(R.drawable.login);
        final LinearLayout linear_layout = new LinearLayout(ListaLugares.this);
        final EditText login = new EditText(this);
        final EditText senha = new EditText(this);
        login.setHint("Login");
        senha.setHint("Senha");
        senha.setTransformationMethod(new PasswordTransformationMethod());
        linear_layout.setOrientation(1);
        linear_layout.addView(login);
        linear_layout.addView(senha);
        alert.setView(linear_layout);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {
                String login_digitado = login.getText().toString();
                String senha_digitada = Login.hashMD5(senha.getText().toString());
                if (!(login_digitado.equals("")) && !(senha_digitada.equals(""))) {
                    if (repositorio.autentica_usuario(login_digitado, senha_digitada) == 1) {
                        Login.estaLogado = true;
                        Login.idUsuario = repositorio.buscarUsuario(login_digitado).getId();
                        Toast.makeText(ListaLugares.this, "Login realizado com sucesso.", Toast.LENGTH_LONG).show();
                        inserirLugar();
                    } else {
                        Toast.makeText(ListaLugares.this, "Usu�rio n�o cadastrado ou senha digitada incorretamente.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(ListaLugares.this, "Usu�rio e/ou senha em branco.", Toast.LENGTH_LONG).show();
                }
            }
        });
        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        alert.show();
    }

    public void fazerLogout() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Confirma��o");
        alert.setIcon(R.drawable.alerta);
        alert.setMessage("Deseja realmente fazer logout?");
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {
                Login.estaLogado = false;
                Login.idUsuario = 0;
                Toast.makeText(ListaLugares.this, "Logout realizado com sucesso.", Toast.LENGTH_LONG).show();
            }
        });
        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        alert.show();
    }

    @Override
    protected void onActivityResult(int codigo, int codigoRetorno, Intent it) {
        super.onActivityResult(codigo, codigoRetorno, it);
        if (codigo == RESULT_BUSCAR) {
            if (codigoRetorno == RESULT_OK) {
                flag_atualiza_tela = true;
                lista = new ArrayList<HashMap<String, String>>();
                ArrayList<String> nomes = it.getStringArrayListExtra("lista_nome_lugares");
                ArrayList<Lugar> lugares = new ArrayList<Lugar>();
                for (int i = 0; i < nomes.size(); i++) {
                    lugares.add(repositorio.buscarLugar(nomes.get(i)));
                }
                for (int i = 0; i < lugares.size(); i++) {
                    HashMap<String, String> item = new HashMap<String, String>();
                    item.put("nomeLugar", lugares.get(i).getNomeLugar());
                    item.put("quant_topicos", repositorio.buscarTipo(lugares.get(i).getId_tipo()).getNome_tipo() + " - " + repositorio.quantidade_topicos(0, lugares.get(i).getId()) + " t�pico(s)");
                    lista.add(item);
                }
                String[] label = new String[] { "nomeLugar", "quant_topicos" };
                int[] layout_id = new int[] { android.R.id.text1, android.R.id.text2 };
                int layoutNativo = android.R.layout.two_line_list_item;
                list_view.setAdapter(new SimpleAdapter(this, lista, layoutNativo, label, layout_id));
            } else atualizarListaLugares();
        }
        if (codigo == RESULT_INSERIR || codigo == RESULT_CADASTRO) {
            if (codigoRetorno == RESULT_OK) {
                atualizarListaLugares();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        repositorio.fechar();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onRestart() {
        super.onRestart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (flag_atualiza_tela == false) atualizarListaLugares();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
