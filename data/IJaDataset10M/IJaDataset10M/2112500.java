package projetofinal.controle;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import projetofinal.modelo.Avaliacao;
import projetofinal.modelo.Avaliacao.Avaliacoes;
import projetofinal.modelo.AvaliacaoLugar;
import projetofinal.modelo.AvaliacaoLugar.AvaliacoesLugar;
import projetofinal.modelo.AvaliacaoUnidade;
import projetofinal.modelo.AvaliacaoUnidade.AvaliacoesUnidade;
import projetofinal.modelo.Comentario;
import projetofinal.modelo.Comentario.Comentarios;
import projetofinal.modelo.Dicionario;
import projetofinal.modelo.Dicionario.Dicionarios;
import projetofinal.modelo.Email;
import projetofinal.modelo.Email.Emails;
import projetofinal.modelo.Lugar;
import projetofinal.modelo.Lugar.Lugares;
import projetofinal.modelo.Telefone;
import projetofinal.modelo.Telefone.Telefones;
import projetofinal.modelo.Tipo;
import projetofinal.modelo.Tipo.Tipos;
import projetofinal.modelo.Unidade;
import projetofinal.modelo.Unidade.Unidades;
import projetofinal.modelo.Usuario;
import projetofinal.modelo.Usuario.Usuarios;
import projetofinal.webservice.REST;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.provider.BaseColumns;
import android.util.Log;

public class Repositorio {

    private static final String NOME_BANCO = "banco";

    private static final String TABELA_UNIDADE = "unidade";

    private static final String TABELA_LUGAR = "lugar";

    private static final String TABELA_COMENTARIO = "comentario";

    private static final String TABELA_USUARIO = "usuario";

    private static final String TABELA_TIPO = "tipo";

    private static final String TABELA_DICIONARIO = "dicionario";

    private static final String TABELA_AVALIACAO = "avaliacao";

    private static final String TABELA_AVALIACAO_UNIDADE = "avaliacaoUnidade";

    private static final String TABELA_AVALIACAO_LUGAR = "avaliacaoLugar";

    private Context context;

    protected SQLiteDatabase bd;

    public Repositorio(Context context) {
        this.context = context;
        bd = context.openOrCreateDatabase(NOME_BANCO, Context.MODE_PRIVATE, null);
    }

    protected Repositorio() {
    }

    public boolean TemConexao() {
        boolean lblnRet = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) {
                lblnRet = true;
            } else {
                lblnRet = false;
            }
        } catch (Exception e) {
        }
        return lblnRet;
    }

    /*************************************************************/
    public Cursor getCursorUnidade() {
        try {
            return bd.query(TABELA_UNIDADE, Unidade.colunas, null, null, null, null, null, null);
        } catch (SQLException e) {
            Log.e("Teste", "Erro ao buscar os centros: " + e.toString());
            return null;
        }
    }

    public Cursor getCursorUnidade(String where, long id_tipo) {
        if (buscarUnidade(where) == null) return null;
        long id_centro = buscarUnidade(where).getId();
        try {
            Cursor c;
            if (id_tipo == 0) {
                c = bd.rawQuery("SELECT * FROM unidade where id_centro=?", new String[] { "" + id_centro });
            } else {
                c = bd.rawQuery("SELECT * FROM unidade where id_centro=? and id_tipo=?", new String[] { "" + id_centro, "" + id_tipo });
            }
            return c;
        } catch (SQLException e) {
            Log.e("Teste", "Erro ao buscar as unidades WHERE: " + e.toString());
            return null;
        }
    }

    public Cursor getCursorCentro() {
        try {
            Cursor c = bd.rawQuery("SELECT * FROM unidade where id_tipo = 1 or id_tipo = 3", new String[] {});
            return c;
        } catch (SQLException e) {
            Log.e("Teste", "Erro ao buscar as unidades WHERE: " + e.toString());
            return null;
        }
    }

    public Cursor getCursorCentro(long id_tipo) {
        try {
            Cursor c = bd.rawQuery("SELECT * FROM unidade where id_tipo = 1 or id_tipo = 3", new String[] {});
            return c;
        } catch (SQLException e) {
            Log.e("Teste", "Erro ao buscar as unidades WHERE: " + e.toString());
            return null;
        }
    }

    public void atualizar(Unidade unidade, int flag_envia_servidor) {
        ContentValues values = new ContentValues();
        values.put(Unidades.NUM_AVALIACOES, unidade.getNum_avaliacoes());
        values.put(Unidades.QUANT_ESTRELAS, unidade.getQuant_estrelas());
        String _id = String.valueOf(unidade.getId());
        String where = BaseColumns._ID + "=?";
        String[] whereArgs = new String[] { _id };
        bd.update(TABELA_UNIDADE, values, where, whereArgs);
        if (flag_envia_servidor == 1) {
            boolean tem_internet = TemConexao();
            if (tem_internet) {
                REST cliREST = new REST();
                try {
                    cliREST.atualizarUnidade(unidade);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Login.unidades_atualizadas_locais.add(unidade);
                Login.flag_enviar_unidades_atualizadas_servidor = true;
            }
        }
    }

    public Unidade buscarUnidade(String nomeUnidade) {
        Cursor c = bd.rawQuery("SELECT * FROM unidade where nome=?", new String[] { nomeUnidade });
        if (c.getCount() > 0) {
            c.moveToFirst();
            Unidade unidade = new Unidade();
            unidade.setId(c.getLong(0));
            unidade.setId_tipo(c.getLong(1));
            unidade.setId_centro(c.getLong(2));
            unidade.setNome(c.getString(3));
            unidade.setSigla(c.getString(4));
            unidade.setEndereco(c.getString(5));
            unidade.setFax(c.getString(6));
            unidade.setSite(c.getString(7));
            unidade.setNum_avaliacoes(c.getInt(8));
            unidade.setQuant_estrelas(c.getInt(9));
            unidade.setLatitude(c.getFloat(10));
            unidade.setLongitude(c.getFloat(11));
            unidade.setLatitude_street_view(c.getFloat(12));
            unidade.setLongitude_street_view(c.getFloat(13));
            unidade.setLastmodified(c.getString(14));
            return unidade;
        }
        return null;
    }

    public Unidade buscarUnidade(long idUnidade) {
        Cursor c = bd.rawQuery("SELECT * FROM unidade where _id=?", new String[] { "" + idUnidade });
        if (c.getCount() > 0) {
            c.moveToFirst();
            Unidade unidade = new Unidade();
            unidade.setId(c.getLong(0));
            unidade.setId_tipo(c.getLong(1));
            unidade.setId_centro(c.getLong(2));
            unidade.setNome(c.getString(3));
            unidade.setSigla(c.getString(4));
            unidade.setEndereco(c.getString(5));
            unidade.setFax(c.getString(6));
            unidade.setSite(c.getString(7));
            unidade.setNum_avaliacoes(c.getInt(8));
            unidade.setQuant_estrelas(c.getInt(9));
            unidade.setLatitude(c.getFloat(10));
            unidade.setLongitude(c.getFloat(11));
            unidade.setLatitude_street_view(c.getFloat(12));
            unidade.setLongitude_street_view(c.getFloat(13));
            unidade.setLastmodified(c.getString(14));
            return unidade;
        }
        return null;
    }

    public List<Unidade> listarCentros() {
        Cursor c = getCursorCentro();
        List<Unidade> centros = new ArrayList<Unidade>();
        if (c.moveToFirst()) {
            int idxId = c.getColumnIndex(BaseColumns._ID);
            int idxIdTipo = c.getColumnIndex(Unidades.ID_TIPO);
            int idxIdCentro = c.getColumnIndex(Unidades.ID_CENTRO);
            int idxNome = c.getColumnIndex(Unidades.NOME);
            int idxSigla = c.getColumnIndex(Unidades.SIGLA);
            int idxEndereco = c.getColumnIndex(Unidades.ENDERECO);
            int idxFax = c.getColumnIndex(Unidades.FAX);
            int idxSite = c.getColumnIndex(Unidades.SITE);
            int idxNum_Avaliacoes = c.getColumnIndex(Unidades.NUM_AVALIACOES);
            int idxQuant_Estrelas = c.getColumnIndex(Unidades.QUANT_ESTRELAS);
            int idxLatitude = c.getColumnIndex(Unidades.LATITUDE);
            int idxLongitude = c.getColumnIndex(Unidades.LONGITUDE);
            int idxLatitudeStreetView = c.getColumnIndex(Unidades.LATITUDE_STREET_VIEW);
            int idxLongitudeStreetView = c.getColumnIndex(Unidades.LONGITUDE_STREET_VIEW);
            int idxLastmodified = c.getColumnIndex(Unidades.LASTMODIFIED);
            do {
                Unidade centro = new Unidade();
                centros.add(centro);
                centro.setId(c.getLong(idxId));
                centro.setId_tipo(c.getLong(idxIdTipo));
                centro.setId_centro(c.getLong(idxIdCentro));
                centro.setNome(c.getString(idxNome));
                centro.setSigla(c.getString(idxSigla));
                centro.setEndereco(c.getString(idxEndereco));
                centro.setFax(c.getString(idxFax));
                centro.setSite(c.getString(idxSite));
                centro.setNum_avaliacoes(c.getInt(idxNum_Avaliacoes));
                centro.setQuant_estrelas(c.getInt(idxQuant_Estrelas));
                centro.setLatitude(c.getFloat(idxLatitude));
                centro.setLongitude(c.getFloat(idxLongitude));
                centro.setLatitude_street_view(c.getFloat(idxLatitudeStreetView));
                centro.setLongitude_street_view(c.getFloat(idxLongitudeStreetView));
                centro.setLastmodified(c.getString(idxLastmodified));
            } while (c.moveToNext());
        }
        return centros;
    }

    public List<Unidade> listarUnidades(String nomeCentro, long id_tipo) {
        Cursor c;
        c = getCursorUnidade(nomeCentro, id_tipo);
        if (c == null) return null;
        List<Unidade> unidades = new ArrayList<Unidade>();
        if (c.moveToFirst()) {
            int idxId = c.getColumnIndex(BaseColumns._ID);
            int idxTipoID = c.getColumnIndex(Unidades.ID_TIPO);
            int idxCentroID = c.getColumnIndex(Unidades.ID_CENTRO);
            int idxNome = c.getColumnIndex(Unidades.NOME);
            int idxSigla = c.getColumnIndex(Unidades.SIGLA);
            int idxEndereco = c.getColumnIndex(Unidades.ENDERECO);
            int idxFax = c.getColumnIndex(Unidades.FAX);
            int idxSite = c.getColumnIndex(Unidades.SITE);
            int idxNum_Avaliacoes = c.getColumnIndex(Unidades.NUM_AVALIACOES);
            int idxQuant_Estrelas = c.getColumnIndex(Unidades.QUANT_ESTRELAS);
            int idxLatitude = c.getColumnIndex(Unidades.LATITUDE);
            int idxLongitude = c.getColumnIndex(Unidades.LONGITUDE);
            int idxLatitude_Street_View = c.getColumnIndex(Unidades.LATITUDE_STREET_VIEW);
            int idxLongitude_Street_View = c.getColumnIndex(Unidades.LONGITUDE_STREET_VIEW);
            int idxLastmodified = c.getColumnIndex(Unidades.LASTMODIFIED);
            do {
                Unidade unidade = new Unidade();
                unidades.add(unidade);
                unidade.setId(c.getLong(idxId));
                unidade.setId_tipo(c.getLong(idxTipoID));
                unidade.setId_centro(c.getLong(idxCentroID));
                unidade.setNome(c.getString(idxNome));
                unidade.setSigla(c.getString(idxSigla));
                unidade.setEndereco(c.getString(idxEndereco));
                unidade.setFax(c.getString(idxFax));
                unidade.setSite(c.getString(idxSite));
                unidade.setNum_avaliacoes(c.getInt(idxNum_Avaliacoes));
                unidade.setQuant_estrelas(c.getInt(idxQuant_Estrelas));
                unidade.setLatitude(c.getFloat(idxLatitude));
                unidade.setLongitude(c.getFloat(idxLongitude));
                unidade.setLatitude_street_view(c.getFloat(idxLatitude_Street_View));
                unidade.setLongitude_street_view(c.getFloat(idxLongitude_Street_View));
                unidade.setLastmodified(c.getString(idxLastmodified));
            } while (c.moveToNext());
        }
        return unidades;
    }

    public List<Unidade> listaUnidadesAtualizadas() {
        REST cliREST = new REST();
        List<Unidade> unidades = null;
        if (Login.idCentroAtual != 0) {
            try {
                Cursor c = bd.rawQuery("SELECT * FROM unidade where id_centro=? ORDER BY _id ASC", new String[] { "" + Login.idCentroAtual });
                c.moveToLast();
                String stringData;
                stringData = c.getString(c.getColumnIndex("lastmodified"));
                unidades = (List<Unidade>) cliREST.listarUnidadesAtualizadas(stringData);
                for (int i = 0; i < unidades.size(); i++) {
                    atualizar(unidades.get(i), 0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return unidades;
        }
        return null;
    }

    public List<Unidade> buscarUnidades(String nome, String tipo, long id_centro) {
        List<Unidade> unidades = new ArrayList<Unidade>();
        Cursor c;
        try {
            if (tipo.equals("Todas as categorias")) {
                c = bd.rawQuery("SELECT * FROM unidade where id_centro=? and (upper(nome) LIKE ? OR upper(sigla) LIKE ? )", new String[] { id_centro + "", "%" + nome.toUpperCase() + "%", "%" + nome.toUpperCase() + "%" });
            } else {
                long id_tipo = buscarTipo(tipo).getId();
                c = bd.rawQuery("SELECT * FROM unidade where id_tipo=? and id_centro=? and (upper(nome) LIKE ? OR upper(sigla) LIKE ? )", new String[] { id_tipo + "", id_centro + "", "%" + nome.toUpperCase() + "%", "%" + nome.toUpperCase() + "%" });
            }
            if (c.moveToFirst()) {
                int idxId = c.getColumnIndex(BaseColumns._ID);
                int idxTipoID = c.getColumnIndex(Unidades.ID_TIPO);
                int idxCentroID = c.getColumnIndex(Unidades.ID_CENTRO);
                int idxNome = c.getColumnIndex(Unidades.NOME);
                int idxSigla = c.getColumnIndex(Unidades.SIGLA);
                int idxEndereco = c.getColumnIndex(Unidades.ENDERECO);
                int idxFax = c.getColumnIndex(Unidades.FAX);
                int idxSite = c.getColumnIndex(Unidades.SITE);
                int idxNum_Avaliacoes = c.getColumnIndex(Unidades.NUM_AVALIACOES);
                int idxQuant_Estrelas = c.getColumnIndex(Unidades.QUANT_ESTRELAS);
                int idxLatitude = c.getColumnIndex(Unidades.LATITUDE);
                int idxLongitude = c.getColumnIndex(Unidades.LONGITUDE);
                int idxLatitude_Street_View = c.getColumnIndex(Unidades.LATITUDE_STREET_VIEW);
                int idxLongitude_Street_View = c.getColumnIndex(Unidades.LONGITUDE_STREET_VIEW);
                int idxLastmodified = c.getColumnIndex(Unidades.LASTMODIFIED);
                do {
                    Unidade unidade = new Unidade();
                    unidades.add(unidade);
                    unidade.setId(c.getLong(idxId));
                    unidade.setId_tipo(c.getLong(idxTipoID));
                    unidade.setId_centro(c.getLong(idxCentroID));
                    unidade.setNome(c.getString(idxNome));
                    unidade.setSigla(c.getString(idxSigla));
                    unidade.setEndereco(c.getString(idxEndereco));
                    unidade.setFax(c.getString(idxFax));
                    unidade.setSite(c.getString(idxSite));
                    unidade.setNum_avaliacoes(c.getInt(idxNum_Avaliacoes));
                    unidade.setQuant_estrelas(c.getInt(idxQuant_Estrelas));
                    unidade.setLatitude(c.getFloat(idxLatitude));
                    unidade.setLongitude(c.getFloat(idxLongitude));
                    unidade.setLatitude_street_view(c.getFloat(idxLatitude_Street_View));
                    unidade.setLongitude_street_view(c.getFloat(idxLongitude_Street_View));
                    unidade.setLastmodified(c.getString(idxLastmodified));
                } while (c.moveToNext());
            } else return null;
        } catch (SQLException e) {
            Log.v("Debug", "Erro ao buscar as unidades: " + e.toString());
            return null;
        }
        return unidades;
    }

    public void enviaUnidadesAtualizadasNaoSincronizadas() throws Exception {
        if (Login.flag_enviar_unidades_atualizadas_servidor == true) {
            REST cliRest = new REST();
            for (int i = 0; i < Login.unidades_atualizadas_locais.size(); i++) {
                cliRest.atualizarUnidade(Login.unidades_atualizadas_locais.get(i));
            }
            Login.lugares_atualizados_locais.clear();
            Login.flag_enviar_unidades_atualizadas_servidor = false;
        }
    }

    /*************************************************************/
    public Cursor getCursorLugar(String where, long id_tipo) {
        if (buscarUnidade(where) == null) return null;
        long id_centro = buscarUnidade(where).getId();
        try {
            Cursor c;
            if (id_tipo == 0) {
                c = bd.rawQuery("SELECT * FROM lugar where id_centro=?", new String[] { "" + id_centro });
            } else {
                c = bd.rawQuery("SELECT * FROM lugar where id_centro=? and id_tipo=?", new String[] { "" + id_centro, "" + id_tipo });
            }
            return c;
        } catch (SQLException e) {
            Log.e("Teste", "Erro ao buscar os lugares WHERE: " + e.toString());
            return null;
        }
    }

    public long inserir(Lugar lugar, int flag_enviar_servidor) {
        boolean tem_internet = TemConexao();
        if (tem_internet) {
            long id = 0;
            if (flag_enviar_servidor == 1) {
                REST cliREST = new REST();
                try {
                    id = cliREST.inserirLugar(lugar);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (id != 0 || flag_enviar_servidor == 0) {
                ContentValues values = new ContentValues();
                if (flag_enviar_servidor == 1) {
                    values.put(Lugares._ID, id);
                } else {
                    values.put(Lugares._ID, lugar.getId());
                }
                values.put(Lugares.ID_TIPO, lugar.getId_tipo());
                values.put(Lugares.ID_CENTRO, lugar.getId_centro());
                values.put(Lugares.ID_USUARIO, lugar.getId_usuario());
                values.put(Lugares.NOMELUGAR, lugar.getNomeLugar());
                values.put(Lugares.REFERENCIAL, lugar.getReferencial());
                values.put(Lugares.NUM_AVALIACOES, lugar.getNum_avaliacoes());
                values.put(Lugares.QUANT_ESTRELAS, lugar.getQuant_estrelas());
                values.put(Lugares.INFORMACOES_ADICIONAIS, lugar.getInformacoes_adicionais());
                values.put(Lugares.LASTMODIFIED, lugar.getLastmodified());
                bd.insert(TABELA_LUGAR, "", values);
                return id;
            }
        }
        return 0;
    }

    public void atualizar(Lugar lugar, int flag_envia_servidor) {
        ContentValues values = new ContentValues();
        values.put(BaseColumns._ID, lugar.getId());
        values.put(Lugares.ID_TIPO, lugar.getId_tipo());
        values.put(Lugares.ID_CENTRO, lugar.getId_centro());
        values.put(Lugares.ID_USUARIO, lugar.getId_usuario());
        values.put(Lugares.NOMELUGAR, lugar.getNomeLugar());
        values.put(Lugares.REFERENCIAL, lugar.getReferencial());
        values.put(Lugares.NUM_AVALIACOES, lugar.getNum_avaliacoes());
        values.put(Lugares.QUANT_ESTRELAS, lugar.getQuant_estrelas());
        values.put(Lugares.INFORMACOES_ADICIONAIS, lugar.getInformacoes_adicionais());
        values.put(Lugares.LASTMODIFIED, lugar.getLastmodified());
        String _id = String.valueOf(lugar.getId());
        String where = BaseColumns._ID + "=?";
        String[] whereArgs = new String[] { _id };
        bd.update(TABELA_LUGAR, values, where, whereArgs);
        if (flag_envia_servidor == 1) {
            boolean tem_internet = TemConexao();
            if (tem_internet) {
                REST cliREST = new REST();
                try {
                    cliREST.atualizarLugar(lugar);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Login.lugares_atualizados_locais.add(lugar);
                Login.flag_enviar_lugares_atualizados_servidor = true;
            }
        }
    }

    public void deletarLugar(long idLugar, int flag_envia_servidor) {
        if (flag_envia_servidor == 1) {
            Lugar lugar = buscarLugar(idLugar);
            lugar.setNomeLugar(lugar.getId() + "LugarRemovido");
            boolean tem_internet = TemConexao();
            if (tem_internet) {
                REST cliREST = new REST();
                try {
                    cliREST.atualizarLugar(lugar);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Login.lugares_atualizados_locais.add(lugar);
                Login.flag_enviar_lugares_atualizados_servidor = true;
            }
        }
        String where = BaseColumns._ID + "=?";
        String _id = String.valueOf(idLugar);
        String[] whereArgs = new String[] { _id };
        bd.delete(TABELA_LUGAR, where, whereArgs);
    }

    public Lugar buscarLugar(long idLugar) {
        Cursor c = bd.query(true, TABELA_LUGAR, Lugar.colunas, BaseColumns._ID + "=" + idLugar, null, null, null, null, null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            Lugar lugar = new Lugar();
            lugar.setId(c.getLong(0));
            lugar.setId_tipo(c.getLong(1));
            lugar.setId_centro(c.getLong(2));
            lugar.setId_usuario(c.getLong(3));
            lugar.setNomeLugar(c.getString(4));
            lugar.setReferencial(c.getString(5));
            lugar.setInformacoes_adicionais(c.getString(6));
            lugar.setNum_avaliacoes(c.getInt(7));
            lugar.setQuant_estrelas(c.getInt(8));
            lugar.setLastmodified(c.getString(9));
            return lugar;
        }
        return null;
    }

    public Lugar buscarLugar(String nomeLugar) {
        Cursor c = bd.rawQuery("SELECT * FROM lugar where nomeLugar=?", new String[] { nomeLugar });
        if (c.getCount() > 0) {
            c.moveToFirst();
            Lugar lugar = new Lugar();
            lugar.setId(c.getLong(0));
            lugar.setId_tipo(c.getLong(1));
            lugar.setId_centro(c.getLong(2));
            lugar.setId_usuario(c.getLong(3));
            lugar.setNomeLugar(c.getString(4));
            lugar.setReferencial(c.getString(5));
            lugar.setInformacoes_adicionais(c.getString(6));
            lugar.setNum_avaliacoes(c.getInt(7));
            lugar.setQuant_estrelas(c.getInt(8));
            lugar.setLastmodified(c.getString(9));
            return lugar;
        }
        return null;
    }

    public List<Lugar> listarLugares(String nomeCentro, long id_tipo) {
        Cursor c = getCursorLugar(nomeCentro, id_tipo);
        if (c == null) return null;
        List<Lugar> lugares = new ArrayList<Lugar>();
        if (c.moveToFirst()) {
            int idxId = c.getColumnIndex(Lugares._ID);
            int idxTipoID = c.getColumnIndex(Lugares.ID_TIPO);
            int idxCentroID = c.getColumnIndex(Lugares.ID_CENTRO);
            int idxUsuarioID = c.getColumnIndex(Lugares.ID_USUARIO);
            int idxNomeLugar = c.getColumnIndex(Lugares.NOMELUGAR);
            int idxReferencial = c.getColumnIndex(Lugares.REFERENCIAL);
            int idxInfoAdicionais = c.getColumnIndex(Lugares.INFORMACOES_ADICIONAIS);
            int idxNum_avalicoes = c.getColumnIndex(Lugares.NUM_AVALIACOES);
            int idxQuant_estrelas = c.getColumnIndex(Lugares.QUANT_ESTRELAS);
            int idxLastmodified = c.getColumnIndex(Lugares.LASTMODIFIED);
            do {
                Lugar lugar = new Lugar();
                lugares.add(lugar);
                lugar.setId(c.getLong(idxId));
                lugar.setId_tipo(c.getLong(idxTipoID));
                lugar.setId_centro(c.getLong(idxCentroID));
                lugar.setId_usuario(c.getLong(idxUsuarioID));
                lugar.setNomeLugar(c.getString(idxNomeLugar));
                lugar.setReferencial(c.getString(idxReferencial));
                lugar.setInformacoes_adicionais((c.getString(idxInfoAdicionais)));
                lugar.setNum_avaliacoes(c.getInt(idxNum_avalicoes));
                lugar.setQuant_estrelas((c.getInt(idxQuant_estrelas)));
                lugar.setLastmodified(c.getString(idxLastmodified));
            } while (c.moveToNext());
        } else {
            return null;
        }
        return lugares;
    }

    public List<Lugar> listarMeusLugares() {
        List<Lugar> lugares = new ArrayList<Lugar>();
        Cursor c = bd.rawQuery("SELECT * FROM lugar where id_usuario=? ", new String[] { Login.idUsuario + "" });
        if (c.moveToFirst()) {
            int idxId = c.getColumnIndex(Lugares._ID);
            int idxTipoID = c.getColumnIndex(Lugares.ID_TIPO);
            int idxCentroID = c.getColumnIndex(Lugares.ID_CENTRO);
            int idxUsuarioID = c.getColumnIndex(Lugares.ID_USUARIO);
            int idxNomeLugar = c.getColumnIndex(Lugares.NOMELUGAR);
            int idxReferencial = c.getColumnIndex(Lugares.REFERENCIAL);
            int idxInfoAdicionais = c.getColumnIndex(Lugares.INFORMACOES_ADICIONAIS);
            int idxNum_avalicoes = c.getColumnIndex(Lugares.NUM_AVALIACOES);
            int idxQuant_estrelas = c.getColumnIndex(Lugares.QUANT_ESTRELAS);
            int idxLastmodified = c.getColumnIndex(Lugares.LASTMODIFIED);
            do {
                Lugar lugar = new Lugar();
                lugares.add(lugar);
                lugar.setId(c.getLong(idxId));
                lugar.setId_tipo(c.getLong(idxTipoID));
                lugar.setId_centro(c.getLong(idxCentroID));
                lugar.setId_usuario(c.getLong(idxUsuarioID));
                lugar.setNomeLugar(c.getString(idxNomeLugar));
                lugar.setReferencial(c.getString(idxReferencial));
                lugar.setInformacoes_adicionais((c.getString(idxInfoAdicionais)));
                lugar.setNum_avaliacoes(c.getInt(idxNum_avalicoes));
                lugar.setQuant_estrelas((c.getInt(idxQuant_estrelas)));
                lugar.setLastmodified(c.getString(idxLastmodified));
            } while (c.moveToNext());
        } else return null;
        return lugares;
    }

    public ArrayList<Lugar> buscarLugaresPorNome(String nome, String nomeTipo, long id_centro) {
        ArrayList<Lugar> lugares = new ArrayList<Lugar>();
        Cursor c;
        try {
            if (nomeTipo.equals("Todas as categorias")) {
                c = bd.rawQuery("SELECT * FROM lugar where id_centro=? and (upper(nomeLugar) LIKE ?)", new String[] { id_centro + "", "%" + nome.toUpperCase() + "%" });
            } else {
                long id_tipo = buscarTipo(nomeTipo).getId();
                c = bd.rawQuery("SELECT * FROM lugar where id_tipo=? and id_centro=? and (upper(nomeLugar) LIKE ?)", new String[] { id_tipo + "", id_centro + "", "%" + nome.toUpperCase() + "%" });
            }
            if (c.moveToFirst()) {
                int idxId = c.getColumnIndex(Lugares._ID);
                int idxTipoID = c.getColumnIndex(Lugares.ID_TIPO);
                int idxCentroID = c.getColumnIndex(Lugares.ID_CENTRO);
                int idxUsuarioID = c.getColumnIndex(Lugares.ID_USUARIO);
                int idxNomeLugar = c.getColumnIndex(Lugares.NOMELUGAR);
                int idxReferencial = c.getColumnIndex(Lugares.REFERENCIAL);
                int idxInfoAdicionais = c.getColumnIndex(Lugares.INFORMACOES_ADICIONAIS);
                int idxNum_avalicoes = c.getColumnIndex(Lugares.NUM_AVALIACOES);
                int idxQuant_estrelas = c.getColumnIndex(Lugares.QUANT_ESTRELAS);
                int idxLastmodified = c.getColumnIndex(Lugares.LASTMODIFIED);
                do {
                    Lugar lugar = new Lugar();
                    lugares.add(lugar);
                    lugar.setId(c.getLong(idxId));
                    lugar.setId_tipo(c.getLong(idxTipoID));
                    lugar.setId_centro(c.getLong(idxCentroID));
                    lugar.setId_usuario(c.getLong(idxUsuarioID));
                    lugar.setNomeLugar(c.getString(idxNomeLugar));
                    lugar.setReferencial(c.getString(idxReferencial));
                    lugar.setInformacoes_adicionais((c.getString(idxInfoAdicionais)));
                    lugar.setNum_avaliacoes(c.getInt(idxNum_avalicoes));
                    lugar.setQuant_estrelas((c.getInt(idxQuant_estrelas)));
                    lugar.setLastmodified(c.getString(idxLastmodified));
                } while (c.moveToNext());
            } else return null;
        } catch (SQLException e) {
            Log.v("Debug", "Erro ao buscar o lugar pelo nome: " + e.toString());
            return null;
        }
        return lugares;
    }

    public List<Lugar> buscarMeusLugaresPorNome(String nome, String tipo) {
        List<Lugar> lugares = new ArrayList<Lugar>();
        Cursor c;
        try {
            if (tipo.equals("Todas as categorias")) {
                c = bd.rawQuery("SELECT * FROM lugar where id_usuario=? and (upper(nomeLugar) LIKE ?)", new String[] { Login.idUsuario + "", "%" + nome.toUpperCase() + "%" });
            } else {
                long id_tipo = buscarTipo(tipo).getId();
                c = bd.rawQuery("SELECT * FROM lugar where id_tipo=? and id_usuario=? and (upper(nomeLugar) LIKE ?)", new String[] { id_tipo + "", Login.idUsuario + "", "%" + nome.toUpperCase() + "%" });
            }
            if (c.moveToFirst()) {
                int idxId = c.getColumnIndex(Lugares._ID);
                int idxTipoID = c.getColumnIndex(Lugares.ID_TIPO);
                int idxCentroID = c.getColumnIndex(Lugares.ID_CENTRO);
                int idxUsuarioID = c.getColumnIndex(Lugares.ID_USUARIO);
                int idxNomeLugar = c.getColumnIndex(Lugares.NOMELUGAR);
                int idxReferencial = c.getColumnIndex(Lugares.REFERENCIAL);
                int idxInfoAdicionais = c.getColumnIndex(Lugares.INFORMACOES_ADICIONAIS);
                int idxNum_avalicoes = c.getColumnIndex(Lugares.NUM_AVALIACOES);
                int idxQuant_estrelas = c.getColumnIndex(Lugares.QUANT_ESTRELAS);
                int idxLastmodified = c.getColumnIndex(Lugares.LASTMODIFIED);
                do {
                    Lugar lugar = new Lugar();
                    lugares.add(lugar);
                    lugar.setId(c.getLong(idxId));
                    lugar.setId_tipo(c.getLong(idxTipoID));
                    lugar.setId_centro(c.getLong(idxCentroID));
                    lugar.setId_usuario(c.getLong(idxUsuarioID));
                    lugar.setNomeLugar(c.getString(idxNomeLugar));
                    lugar.setReferencial(c.getString(idxReferencial));
                    lugar.setInformacoes_adicionais((c.getString(idxInfoAdicionais)));
                    lugar.setNum_avaliacoes(c.getInt(idxNum_avalicoes));
                    lugar.setQuant_estrelas((c.getInt(idxQuant_estrelas)));
                    lugar.setLastmodified(c.getString(idxLastmodified));
                } while (c.moveToNext());
            } else return null;
        } catch (SQLException e) {
            Log.v("Debug", "Erro ao buscar o lugar pelo nome: " + e.toString());
            return null;
        }
        return lugares;
    }

    public List<Lugar> listaLugaresNaoSincronizados() {
        REST cliREST = new REST();
        List<Lugar> lugares = null;
        if (Login.idCentroAtual != 0) {
            try {
                Cursor c = bd.rawQuery("SELECT * FROM lugar where id_centro=? ORDER BY _id ASC", new String[] { "" + Login.idCentroAtual });
                c.moveToLast();
                String stringData;
                if (c.getCount() == 0) {
                    try {
                        lugares = (List<Lugar>) cliREST.listarLugares();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    stringData = c.getString(c.getColumnIndex("lastmodified"));
                    lugares = (List<Lugar>) cliREST.listarLugaresNaoSincronizados(stringData);
                }
                for (int i = 0; i < lugares.size(); i++) {
                    if ((buscarLugar(lugares.get(i).getId()) != null) && (!lugares.get(i).getNomeLugar().equals(lugares.get(i).getId() + "LugarRemovido"))) {
                        atualizar(lugares.get(i), 0);
                    } else if (lugares.get(i).getNomeLugar().equals(lugares.get(i).getId() + "LugarRemovido")) {
                        deletarLugar(lugares.get(i).getId(), 0);
                    } else {
                        inserir(lugares.get(i), 0);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return lugares;
        }
        return null;
    }

    public void enviaLugaresAtualizadosNaoSincronizados() throws Exception {
        if (Login.flag_enviar_lugares_atualizados_servidor == true) {
            REST cliRest = new REST();
            for (int i = 0; i < Login.lugares_atualizados_locais.size(); i++) {
                cliRest.atualizarLugar(Login.lugares_atualizados_locais.get(i));
            }
            Login.lugares_atualizados_locais.clear();
            Login.flag_enviar_lugares_atualizados_servidor = false;
        }
    }

    /****************************************************************/
    public Cursor getCursorComentario(long id_unidade, long id_lugar, long id_centro) {
        try {
            Cursor c;
            if (id_unidade == 0 && id_lugar == 0 && id_centro != 0) {
                c = bd.rawQuery("SELECT c.* FROM comentario c, unidade u where (c.[id_unidade] = u.[_id]) and (u.[id_unidade]=?) group by titulo " + "UNION SELECT c.* FROM comentario c, lugar l where (c.[id_lugar] = l.[_id]) and (l.[id_unidade]=?) group by titulo ", new String[] { "" + id_centro, "" + id_centro });
            } else if (id_unidade == 0 && id_lugar == 0 && id_centro == 0) {
                c = bd.rawQuery("SELECT * FROM comentario GROUP BY titulo ORDER BY _id ASC", new String[] {});
            } else if (id_unidade != 0 && id_lugar != 0) {
                c = bd.rawQuery("SELECT * FROM comentario where id_unidade= ? group by titulo " + "UNION SELECT * FROM comentario where id_lugar= ? group by titulo", new String[] { "" + id_unidade, "" + id_lugar });
            } else {
                c = bd.rawQuery("SELECT * FROM comentario where id_unidade=? and id_lugar=? group by titulo ORDER BY _id ASC", new String[] { "" + id_unidade, "" + id_lugar });
            }
            return c;
        } catch (SQLException e) {
            Log.e("Teste", "Erro ao buscar os comentarios WHERE: " + e.toString());
            return null;
        }
    }

    public long inserir(Comentario comentario, int flag_envia_servidor) {
        boolean tem_internet = TemConexao();
        if (tem_internet) {
            long id = 0;
            REST cliREST = new REST();
            if (flag_envia_servidor == 1) {
                try {
                    id = (long) cliREST.inserirComentario(comentario);
                    if (!comentario.getUrl_midia().equals("")) {
                        cliREST.enviarMidia(comentario.getUrl_midia(), Environment.getExternalStorageDirectory() + "/" + comentario.getUrl_midia());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (id != 0 || flag_envia_servidor == 0) {
                ContentValues values = new ContentValues();
                if (flag_envia_servidor == 1) {
                    values.put(Comentarios._ID, id);
                } else {
                    values.put(Comentarios._ID, comentario.getId());
                }
                values.put(Comentarios.ID_LUGAR, comentario.getId_lugar());
                values.put(Comentarios.ID_UNIDADE, comentario.getId_unidade());
                values.put(Comentarios.ID_USUARIO, comentario.getId_usuario());
                values.put(Comentarios.TITULO, comentario.getTitulo());
                values.put(Comentarios.COMENTARIO, comentario.getComentario());
                values.put(Comentarios.URL_MIDIA, comentario.getUrl_midia());
                values.put(Comentarios.FLAG_DICIONARIO, comentario.getFlag_dicionario());
                values.put(Comentarios.LASTMODIFIED, comentario.getLastmodified());
                bd.insert(TABELA_COMENTARIO, "", values);
                return id;
            }
        }
        return 0;
    }

    public long responder(Comentario comentario) {
        long id = 0;
        ContentValues values = new ContentValues();
        values.put(Comentarios.ID_LUGAR, comentario.getId_lugar());
        values.put(Comentarios.ID_UNIDADE, comentario.getId_unidade());
        values.put(Comentarios.ID_USUARIO, comentario.getId_usuario());
        values.put(Comentarios.TITULO, comentario.getTitulo());
        values.put(Comentarios.COMENTARIO, comentario.getComentario());
        values.put(Comentarios.URL_MIDIA, comentario.getUrl_midia());
        values.put(Comentarios.FLAG_DICIONARIO, comentario.getFlag_dicionario());
        values.put(Comentarios.LASTMODIFIED, comentario.getLastmodified());
        boolean tem_internet = TemConexao();
        if (tem_internet) {
            REST cliREST = new REST();
            try {
                id = cliREST.inserirComentario(comentario);
                if (!comentario.getUrl_midia().equals("")) {
                    cliREST.enviarMidia(comentario.getUrl_midia(), Environment.getExternalStorageDirectory() + "/" + comentario.getUrl_midia());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            values.put(Comentarios._ID, id);
            bd.insert(TABELA_COMENTARIO, "", values);
        } else {
            id = bd.insert(TABELA_COMENTARIO, "", values);
            Login.comentarios_locais.add(comentario);
            Login.flag_enviar_comentarios_servidor = true;
        }
        return id;
    }

    public void atualizar(Comentario comentario, int flag_envia_servidor) {
        ContentValues values = new ContentValues();
        values.put(BaseColumns._ID, comentario.getId());
        values.put(Comentarios.ID_LUGAR, comentario.getId_lugar());
        values.put(Comentarios.ID_UNIDADE, comentario.getId_unidade());
        values.put(Comentarios.ID_USUARIO, comentario.getId_usuario());
        values.put(Comentarios.COMENTARIO, comentario.getComentario());
        values.put(Comentarios.TITULO, comentario.getTitulo());
        values.put(Comentarios.URL_MIDIA, comentario.getUrl_midia());
        values.put(Comentarios.FLAG_DICIONARIO, comentario.getFlag_dicionario());
        values.put(Comentarios.LASTMODIFIED, comentario.getLastmodified());
        String _id = String.valueOf(comentario.getId());
        String where = BaseColumns._ID + "=?";
        String[] whereArgs = new String[] { _id };
        bd.update(TABELA_COMENTARIO, values, where, whereArgs);
        if (flag_envia_servidor == 1) {
            boolean tem_internet = TemConexao();
            if (tem_internet) {
                REST cliREST = new REST();
                try {
                    cliREST.atualizarComentario(comentario);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Login.comentarios_atualizados_locais.add(comentario);
                Login.flag_enviar_comentarios_atualizados_servidor = true;
            }
        }
    }

    public void deletarComentario(long idComentario, int flag_envia_servidor) {
        Comentario comentario = buscarComentario(idComentario);
        if (!comentario.getUrl_midia().equals("")) {
            deletarMidia(idComentario);
        }
        if (flag_envia_servidor == 1) {
            boolean tem_internet = TemConexao();
            comentario.setComentario(comentario.getId() + "ComentarioRemovido");
            if (tem_internet) {
                REST cliREST = new REST();
                try {
                    cliREST.atualizarComentario(comentario);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Login.comentarios_atualizados_locais.add(comentario);
                Login.flag_enviar_comentarios_atualizados_servidor = true;
            }
        }
        deletarPalavraChave(idComentario);
        String where = BaseColumns._ID + "=?";
        String _id = String.valueOf(idComentario);
        String[] whereArgs = new String[] { _id };
        bd.delete(TABELA_COMENTARIO, where, whereArgs);
    }

    public void deletarMidia(long idComentario) {
        File file = new File(Environment.getExternalStorageDirectory() + "/" + buscarComentario(idComentario).getUrl_midia());
        file.delete();
    }

    public Comentario buscarComentario(long id) {
        Cursor c = bd.query(true, TABELA_COMENTARIO, Comentario.colunas, BaseColumns._ID + "=" + id, null, null, null, null, null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            Comentario comentario = new Comentario();
            comentario.setId(c.getLong(0));
            comentario.setId_lugar(c.getLong(1));
            comentario.setId_unidade(c.getLong(2));
            comentario.setId_usuario(c.getLong(3));
            comentario.setComentario(c.getString(4));
            comentario.setTitulo(c.getString(5));
            comentario.setUrl_midia(c.getString(6));
            comentario.setFlag_dicionario(c.getInt(7));
            comentario.setLastmodified(c.getString(8));
            return comentario;
        }
        return null;
    }

    public List<Comentario> listarComentariosParaDicionario() {
        REST cliREST = new REST();
        List<Comentario> comentarios = null;
        try {
            comentarios = (List<Comentario>) cliREST.listarComentariosParaDicionario();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return comentarios;
    }

    public List<Comentario> listarComentarios(long id_unidade, long id_lugar, long id_centro) {
        Cursor c = getCursorComentario(id_unidade, id_lugar, id_centro);
        List<Comentario> comentarios = new ArrayList<Comentario>();
        if (c.moveToFirst()) {
            int idxId = c.getColumnIndex(BaseColumns._ID);
            int idxLugarID = c.getColumnIndex(Comentarios.ID_LUGAR);
            int idxUnidadeID = c.getColumnIndex(Comentarios.ID_UNIDADE);
            int idxUsuarioID = c.getColumnIndex(Comentarios.ID_USUARIO);
            int idxComentario = c.getColumnIndex(Comentarios.COMENTARIO);
            int idxTitulo = c.getColumnIndex(Comentarios.TITULO);
            int idxFlagDicionario = c.getColumnIndex(Comentarios.FLAG_DICIONARIO);
            int idxUrlMidia = c.getColumnIndex(Comentarios.URL_MIDIA);
            int idxLastmodified = c.getColumnIndex(Comentarios.LASTMODIFIED);
            do {
                Comentario comentario = new Comentario();
                comentarios.add(comentario);
                comentario.setId(c.getLong(idxId));
                comentario.setId_lugar(c.getLong(idxLugarID));
                comentario.setId_unidade(c.getLong(idxUnidadeID));
                comentario.setId_usuario(c.getLong(idxUsuarioID));
                comentario.setComentario(c.getString(idxComentario));
                comentario.setTitulo(c.getString(idxTitulo));
                comentario.setUrl_midia(c.getString(idxUrlMidia));
                comentario.setFlag_dicionario(c.getInt(idxFlagDicionario));
                comentario.setLastmodified(c.getString(idxLastmodified));
            } while (c.moveToNext());
        } else {
            return null;
        }
        return comentarios;
    }

    public List<Comentario> buscarTopicos(long idUnidade, long idLugar, String parametro) {
        List<Comentario> comentarios = new ArrayList<Comentario>();
        String consulta = "SELECT * FROM comentario where id_unidade=" + idUnidade + " and id_lugar=" + idLugar + " " + parametro;
        try {
            Cursor c = bd.rawQuery(consulta, new String[] {});
            if (c.moveToFirst()) {
                int idxId = c.getColumnIndex(BaseColumns._ID);
                int idxLugarID = c.getColumnIndex(Comentarios.ID_LUGAR);
                int idxUnidadeID = c.getColumnIndex(Comentarios.ID_UNIDADE);
                int idxUsuarioID = c.getColumnIndex(Comentarios.ID_USUARIO);
                int idxComentario = c.getColumnIndex(Comentarios.COMENTARIO);
                int idxTitulo = c.getColumnIndex(Comentarios.TITULO);
                int idxFlagDicionario = c.getColumnIndex(Comentarios.FLAG_DICIONARIO);
                int idxUrlMidia = c.getColumnIndex(Comentarios.URL_MIDIA);
                int idxLastmodified = c.getColumnIndex(Comentarios.LASTMODIFIED);
                do {
                    Comentario comentario = new Comentario();
                    comentarios.add(comentario);
                    comentario.setId(c.getLong(idxId));
                    comentario.setId_lugar(c.getLong(idxLugarID));
                    comentario.setId_unidade(c.getLong(idxUnidadeID));
                    comentario.setId_usuario(c.getLong(idxUsuarioID));
                    comentario.setComentario(c.getString(idxComentario));
                    comentario.setTitulo(c.getString(idxTitulo));
                    comentario.setUrl_midia(c.getString(idxUrlMidia));
                    comentario.setFlag_dicionario(c.getInt(idxFlagDicionario));
                    comentario.setLastmodified(c.getString(idxLastmodified));
                } while (c.moveToNext());
            } else {
                return null;
            }
        } catch (SQLException e) {
            Log.e("Teste", "Erro ao buscar topicos: " + e.toString());
            return null;
        }
        return comentarios;
    }

    public int quantidade_topicos(long id_unidade, long id_lugar) {
        int quant_topicos = 0;
        try {
            Cursor c = bd.rawQuery("SELECT COUNT(DISTINCT titulo) FROM comentario where id_unidade=? and id_lugar=?", new String[] { id_unidade + "", id_lugar + "" });
            if (c.moveToNext()) {
                quant_topicos += c.getInt(0);
            }
        } catch (SQLException e) {
            Log.e("Teste", "Erro ao buscar quantidade de topicos: " + e.toString());
        }
        return quant_topicos;
    }

    public int quantidade_comentarios_por_titulo(String titulo, long id_unidade, long id_lugar) {
        int quant_comentarios = 0;
        try {
            Cursor c = bd.rawQuery("SELECT COUNT(*) FROM comentario where id_unidade=? and id_lugar=? and titulo=?", new String[] { id_unidade + "", id_lugar + "", titulo });
            if (c.moveToNext()) {
                quant_comentarios += c.getInt(0);
            }
        } catch (SQLException e) {
            Log.e("Teste", "Erro ao buscar quantidade de coment�rios por t�tulo: " + e.toString());
        }
        return quant_comentarios;
    }

    public ArrayList<Comentario> buscarComentariosporTitulo(String titulo, long id_unidade, long id_lugar) {
        ArrayList<Comentario> comentarios = new ArrayList<Comentario>();
        try {
            Cursor c;
            c = bd.rawQuery("SELECT * FROM comentario where id_unidade=? and id_lugar=? and titulo=?", new String[] { id_unidade + "", id_lugar + "", titulo });
            if (c.moveToFirst()) {
                int idxId = c.getColumnIndex(BaseColumns._ID);
                int idxLugarID = c.getColumnIndex(Comentarios.ID_LUGAR);
                int idxUnidadeID = c.getColumnIndex(Comentarios.ID_UNIDADE);
                int idxUsuarioID = c.getColumnIndex(Comentarios.ID_USUARIO);
                int idxComentario = c.getColumnIndex(Comentarios.COMENTARIO);
                int idxTitulo = c.getColumnIndex(Comentarios.TITULO);
                int idxFlagDicionario = c.getColumnIndex(Comentarios.FLAG_DICIONARIO);
                int idxUrlMidia = c.getColumnIndex(Comentarios.URL_MIDIA);
                int idxLastmodified = c.getColumnIndex(Comentarios.LASTMODIFIED);
                do {
                    Comentario comentario = new Comentario();
                    comentarios.add(comentario);
                    comentario.setId(c.getLong(idxId));
                    comentario.setId_lugar(c.getLong(idxLugarID));
                    comentario.setId_unidade(c.getLong(idxUnidadeID));
                    comentario.setId_usuario(c.getLong(idxUsuarioID));
                    comentario.setComentario(c.getString(idxComentario));
                    comentario.setTitulo(c.getString(idxTitulo));
                    comentario.setUrl_midia(c.getString(idxUrlMidia));
                    comentario.setFlag_dicionario(c.getInt(idxFlagDicionario));
                    comentario.setLastmodified(c.getString(idxLastmodified));
                } while (c.moveToNext());
            } else {
                return null;
            }
        } catch (SQLException e) {
            Log.e("Teste", "Erro ao buscar quantidade de coment�rios por t�tulo: " + e.toString());
            return null;
        }
        return comentarios;
    }

    public ArrayList<Comentario> listarMeusComentarios() {
        ArrayList<Comentario> comentarios = new ArrayList<Comentario>();
        try {
            Cursor c = bd.rawQuery("SELECT * FROM comentario where id_usuario=? ", new String[] { Login.idUsuario + "" });
            if (c.moveToFirst()) {
                int idxId = c.getColumnIndex(BaseColumns._ID);
                int idxLugarID = c.getColumnIndex(Comentarios.ID_LUGAR);
                int idxUnidadeID = c.getColumnIndex(Comentarios.ID_UNIDADE);
                int idxUsuarioID = c.getColumnIndex(Comentarios.ID_USUARIO);
                int idxComentario = c.getColumnIndex(Comentarios.COMENTARIO);
                int idxTitulo = c.getColumnIndex(Comentarios.TITULO);
                int idxFlagDicionario = c.getColumnIndex(Comentarios.FLAG_DICIONARIO);
                int idxUrlMidia = c.getColumnIndex(Comentarios.URL_MIDIA);
                int idxLastmodified = c.getColumnIndex(Comentarios.LASTMODIFIED);
                do {
                    Comentario comentario = new Comentario();
                    comentarios.add(comentario);
                    comentario.setId(c.getLong(idxId));
                    comentario.setId_lugar(c.getLong(idxLugarID));
                    comentario.setId_unidade(c.getLong(idxUnidadeID));
                    comentario.setId_usuario(c.getLong(idxUsuarioID));
                    comentario.setComentario(c.getString(idxComentario));
                    comentario.setTitulo(c.getString(idxTitulo));
                    comentario.setUrl_midia(c.getString(idxUrlMidia));
                    comentario.setFlag_dicionario(c.getInt(idxFlagDicionario));
                    comentario.setLastmodified(c.getString(idxLastmodified));
                } while (c.moveToNext());
            } else {
                return null;
            }
        } catch (SQLException e) {
            Log.e("Teste", "Erro ao buscar coment�rios por usuario: " + e.toString());
            return null;
        }
        return comentarios;
    }

    public List<Comentario> listaComentariosNaoSincronizados() {
        REST cliREST = new REST();
        List<Comentario> comentarios = null;
        if (Login.idCentroAtual != 0) {
            try {
                Cursor c = bd.rawQuery("SELECT c.* FROM comentario c, unidade u where (c.id_unidade = u._id) " + "and (u.id_centro =?) " + " UNION SELECT c.* FROM comentario c, lugar l where (c.id_lugar = l._id) " + "and (l.id_centro =?)ORDER BY _id ASC", new String[] { "" + Login.idCentroAtual, "" + Login.idCentroAtual });
                c.moveToLast();
                String stringData;
                if (c.getCount() == 0) {
                    try {
                        comentarios = (List<Comentario>) cliREST.listarComentarios(-1, -1, Login.idCentroAtual);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    stringData = c.getString(c.getColumnIndex("lastmodified"));
                    comentarios = (List<Comentario>) cliREST.listarComentariosNaoSincronizados(stringData);
                }
                for (int i = 0; i < comentarios.size(); i++) {
                    if ((buscarComentario(comentarios.get(i).getId()) != null) && (!comentarios.get(i).getComentario().equals(comentarios.get(i).getId() + "ComentarioRemovido"))) {
                        atualizar(comentarios.get(i), 0);
                    } else if (comentarios.get(i).getComentario().equals(comentarios.get(i).getId() + "ComentarioRemovido")) {
                        deletarComentario(comentarios.get(i).getId(), 0);
                    } else {
                        inserir(comentarios.get(i), 0);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return comentarios;
    }

    public void enviaComentariosNaoSincronizados() throws Exception {
        if (Login.flag_enviar_comentarios_servidor == true) {
            REST cliRest = new REST();
            long id = 0;
            for (int i = 0; i < Login.comentarios_locais.size(); i++) {
                id = cliRest.inserirComentario(Login.comentarios_locais.get(i));
                if (!Login.comentarios_locais.get(i).getUrl_midia().equals("")) {
                    cliRest.enviarMidia(Login.comentarios_locais.get(i).getUrl_midia(), Environment.getExternalStorageDirectory() + "/" + Login.comentarios_locais.get(i).getUrl_midia());
                }
                ContentValues values = new ContentValues();
                values.put(BaseColumns._ID, id);
                String _id = String.valueOf(Login.comentarios_locais.get(i).getId());
                String where = BaseColumns._ID + "=?";
                String[] whereArgs = new String[] { _id };
                bd.update(TABELA_COMENTARIO, values, where, whereArgs);
            }
            Login.comentarios_locais.clear();
            Login.flag_enviar_comentarios_servidor = false;
        }
    }

    public void enviaComentariosAtualizadosNaoSincronizados() throws Exception {
        if (Login.flag_enviar_comentarios_atualizados_servidor == true) {
            REST cliRest = new REST();
            for (int i = 0; i < Login.comentarios_atualizados_locais.size(); i++) {
                cliRest.atualizarComentario(Login.comentarios_atualizados_locais.get(i));
            }
            Login.comentarios_atualizados_locais.clear();
            Login.flag_enviar_comentarios_atualizados_servidor = false;
        }
    }

    /**************************************************************/
    public long inserir(Usuario usuario, int flag_enviar_servidor) {
        boolean tem_internet = TemConexao();
        if (tem_internet) {
            long id = 0;
            if (flag_enviar_servidor == 1) {
                REST cliREST = new REST();
                try {
                    id = cliREST.inserirUsuario(usuario);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (id != 0 || flag_enviar_servidor == 0) {
                ContentValues values = new ContentValues();
                if (flag_enviar_servidor == 1) {
                    values.put(Usuarios._ID, id);
                } else {
                    id = usuario.getId();
                    values.put(Usuarios._ID, id);
                }
                values.put(Usuarios._ID, id);
                values.put(Usuarios.CPF, usuario.getCPF());
                values.put(Usuarios.NOME, usuario.getNome());
                values.put(Usuarios.EMAIL, usuario.getEmail());
                values.put(Usuarios.LOGIN, usuario.getLogin());
                values.put(Usuarios.SENHA, usuario.getSenha());
                values.put(Usuarios.IMEI, usuario.getImei());
                values.put(Usuarios.NUM_TELEFONE, usuario.getNum_telefone());
                values.put(Usuarios.LASTMODIFIED, usuario.getLastmodified());
                bd.insert(TABELA_USUARIO, "", values);
                return id;
            }
        }
        return 0;
    }

    public void atualizar(Usuario usuario, int flag_envia_servidor) {
        Login.flag_enviar_usuarios_atualizados_servidor = true;
        ContentValues values = new ContentValues();
        values.put(BaseColumns._ID, usuario.getId());
        values.put(Usuarios.CPF, usuario.getCPF());
        values.put(Usuarios.NOME, usuario.getNome());
        values.put(Usuarios.EMAIL, usuario.getEmail());
        values.put(Usuarios.LOGIN, usuario.getLogin());
        values.put(Usuarios.SENHA, usuario.getSenha());
        values.put(Usuarios.IMEI, usuario.getImei());
        values.put(Usuarios.NUM_TELEFONE, usuario.getNum_telefone());
        values.put(Usuarios.LASTMODIFIED, usuario.getLastmodified());
        String _id = String.valueOf(usuario.getId());
        String where = BaseColumns._ID + "=?";
        String[] whereArgs = new String[] { _id };
        bd.update(TABELA_USUARIO, values, where, whereArgs);
        if (flag_envia_servidor == 1) {
            boolean tem_internet = TemConexao();
            if (tem_internet) {
                REST cliREST = new REST();
                try {
                    cliREST.atualizarUsuario(usuario);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Login.usuarios_atualizados_locais.add(usuario);
                Login.flag_enviar_usuarios_atualizados_servidor = true;
            }
        }
    }

    public Usuario buscarUsuario(long id) {
        Cursor c = bd.query(true, TABELA_USUARIO, Usuario.colunas, BaseColumns._ID + "=" + id, null, null, null, null, null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            Usuario usuario = new Usuario();
            usuario.setId(c.getLong(0));
            usuario.setCPF(c.getString(1));
            usuario.setNome(c.getString(2));
            usuario.setEmail(c.getString(3));
            usuario.setLogin(c.getString(4));
            usuario.setSenha(c.getString(5));
            usuario.setImei(c.getString(6));
            usuario.setNum_telefone(c.getString(7));
            usuario.setLastmodified(c.getString(8));
            return usuario;
        }
        return null;
    }

    public Usuario buscarUsuario(String login) {
        Cursor c = bd.rawQuery("SELECT * FROM usuario where login=?", new String[] { login });
        Usuario usuario = null;
        if (c.getCount() > 0) {
            c.moveToFirst();
            usuario = new Usuario();
            usuario.setId(c.getLong(0));
            return usuario;
        }
        return usuario;
    }

    public int autentica_usuario(String login, String senha) {
        try {
            Cursor c = bd.rawQuery("SELECT * FROM usuario where login=? and senha=?", new String[] { login, senha });
            return c.getCount();
        } catch (Exception e) {
            return -1;
        }
    }

    public List<Usuario> listaUsuariosNaoSincronizados() {
        REST cliREST = new REST();
        List<Usuario> usuarios = null;
        try {
            Cursor c = bd.rawQuery("SELECT * FROM usuario ORDER BY _id ASC", new String[] {});
            c.moveToLast();
            String stringData;
            if (c.getCount() == 0) {
                try {
                    usuarios = (List<Usuario>) cliREST.listarUsuarios();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                stringData = c.getString(c.getColumnIndex("lastmodified"));
                usuarios = (List<Usuario>) cliREST.listarUsuariosNaoSincronizados(stringData);
            }
            for (int i = 0; i < usuarios.size(); i++) {
                if (buscarUsuario(usuarios.get(i).getId()) != null) {
                    atualizar(usuarios.get(i), 0);
                } else {
                    inserir(usuarios.get(i), 0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    public void enviaUsuariosAtualizadosNaoSincronizados() throws Exception {
        if (Login.flag_enviar_usuarios_atualizados_servidor == true) {
            REST cliRest = new REST();
            for (int i = 0; i < Login.usuarios_atualizados_locais.size(); i++) {
                cliRest.atualizarUsuario(Login.usuarios_atualizados_locais.get(i));
            }
            Login.usuarios_atualizados_locais.clear();
            Login.flag_enviar_usuarios_atualizados_servidor = false;
        }
    }

    /*****************************************************************/
    public void inserir(AvaliacaoLugar avaliacaoLugar, int flag_envia_servidor) {
        ContentValues values = new ContentValues();
        values.put(AvaliacoesLugar.ID_USUARIO, avaliacaoLugar.getId_usuario());
        values.put(AvaliacoesLugar.ID_LUGAR, avaliacaoLugar.getId_lugar());
        values.put(AvaliacoesLugar.NUM_ESTRELAS, avaliacaoLugar.getNum_estrelas());
        values.put(AvaliacoesLugar.LASTMODIFIED, avaliacaoLugar.getLastmodified());
        bd.insert(TABELA_AVALIACAO_LUGAR, "", values);
        if (flag_envia_servidor == 1) {
            boolean tem_internet = TemConexao();
            if (tem_internet) {
                REST cliREST = new REST();
                try {
                    cliREST.inserirAvaliacaoLugar(avaliacaoLugar);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Login.avaliacoesLugar_locais.add(avaliacaoLugar);
                Login.flag_enviar_avaliacoesLugar_servidor = true;
            }
        }
    }

    public boolean usuario_curtiu_lugar(long id_usuario, long id_lugar) {
        try {
            Cursor c = bd.rawQuery("SELECT * FROM avaliacaoLugar where id_usuario=? and id_lugar=?", new String[] { id_usuario + "", id_lugar + "" });
            if (c.getCount() > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            Log.e("Teste", "Erro ao buscar quantidade de coment�rios por t�tulo: " + e.toString());
            return false;
        }
    }

    public AvaliacaoLugar busca_avaliacao_lugar(long id_usuario, long id_lugar) {
        try {
            Cursor c = bd.rawQuery("SELECT * FROM avaliacaoLugar where id_usuario=? and id_lugar=?", new String[] { id_usuario + "", id_lugar + "" });
            AvaliacaoLugar avaliacaoLugar = null;
            if (c.getCount() > 0) {
                c.moveToFirst();
                avaliacaoLugar = new AvaliacaoLugar();
                avaliacaoLugar.setId_usuario(c.getLong(0));
                avaliacaoLugar.setId_lugar(c.getLong(1));
                avaliacaoLugar.setNum_estrelas(c.getInt(2));
                return avaliacaoLugar;
            }
            return avaliacaoLugar;
        } catch (SQLException e) {
            Log.e("Teste", "Erro ao buscar tupla na tabela de avalia��o: " + e.toString());
            return null;
        }
    }

    /*****************************************************************/
    public void inserir(AvaliacaoUnidade avaliacaoUnidade, int flag_envia_servidor) {
        ContentValues values = new ContentValues();
        values.put(AvaliacoesUnidade.ID_USUARIO, avaliacaoUnidade.getId_usuario());
        values.put(AvaliacoesUnidade.ID_UNIDADE, avaliacaoUnidade.getId_unidade());
        values.put(AvaliacoesUnidade.NUM_ESTRELAS, avaliacaoUnidade.getNum_estrelas());
        values.put(AvaliacoesUnidade.LASTMODIFIED, avaliacaoUnidade.getLastmodified());
        bd.insert(TABELA_AVALIACAO_UNIDADE, "", values);
        if (flag_envia_servidor == 1) {
            boolean tem_internet = TemConexao();
            if (tem_internet) {
                REST cliREST = new REST();
                try {
                    cliREST.inserirAvaliacaoUnidade(avaliacaoUnidade);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Login.avaliacoesUnidade_locais.add(avaliacaoUnidade);
                Login.flag_enviar_avaliacoesUnidade_servidor = true;
            }
        }
    }

    public boolean usuario_curtiu_unidade(long id_usuario, long id_unidade) {
        try {
            Cursor c = bd.rawQuery("SELECT * FROM avaliacaoUnidade where id_usuario=? and id_unidade=?", new String[] { id_usuario + "", id_unidade + "" });
            if (c.getCount() > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            Log.e("Teste", "Erro ao buscar quantidade de coment�rios por t�tulo: " + e.toString());
            return false;
        }
    }

    public AvaliacaoUnidade busca_avaliacao_unidade(long id_usuario, long id_unidade) {
        try {
            Cursor c = bd.rawQuery("SELECT * FROM avaliacaoUnidade where id_usuario=? and id_unidade=?", new String[] { id_usuario + "", id_unidade + "" });
            AvaliacaoUnidade avaliacaoUnidade = null;
            if (c.getCount() > 0) {
                c.moveToFirst();
                avaliacaoUnidade = new AvaliacaoUnidade();
                avaliacaoUnidade.setId_usuario(c.getLong(0));
                avaliacaoUnidade.setId_unidade(c.getLong(1));
                avaliacaoUnidade.setNum_estrelas(c.getInt(2));
                return avaliacaoUnidade;
            }
            return avaliacaoUnidade;
        } catch (SQLException e) {
            Log.e("Teste", "Erro ao buscar tupla na tabela de avalia��o: " + e.toString());
            return null;
        }
    }

    /*****************************************************************/
    public void inserir(Avaliacao avaliacao, int flag_envia_servidor) {
        ContentValues values = new ContentValues();
        values.put(Avaliacoes.ID_USUARIO, avaliacao.getId_usuario());
        values.put(Avaliacoes.ID_COMENTARIO, avaliacao.getId_comentario());
        values.put(Avaliacoes.FLAG_AVALIACAO, avaliacao.getFlag_avaliacao());
        values.put(Avaliacoes.LASTMODIFIED, avaliacao.getLastmodified());
        bd.insert(TABELA_AVALIACAO, "", values);
        if (flag_envia_servidor == 1) {
            boolean tem_internet = TemConexao();
            if (tem_internet) {
                REST cliREST = new REST();
                try {
                    cliREST.inserirAvaliacao(avaliacao);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Login.avaliacoes_locais.add(avaliacao);
                Login.flag_enviar_avaliacoes_servidor = true;
            }
        }
    }

    public void atualizar(Avaliacao avaliacao, int flag_envia_servidor) {
        ContentValues values = new ContentValues();
        values.put(Avaliacoes.ID_USUARIO, avaliacao.getId_usuario());
        values.put(Avaliacoes.ID_COMENTARIO, avaliacao.getId_comentario());
        values.put(Avaliacoes.FLAG_AVALIACAO, avaliacao.getFlag_avaliacao());
        values.put(Avaliacoes.LASTMODIFIED, avaliacao.getLastmodified());
        String where = Avaliacoes.ID_USUARIO + "=? and " + Avaliacoes.ID_COMENTARIO + "=?";
        String[] whereArgs = new String[] { avaliacao.getId_usuario() + "", avaliacao.getId_comentario() + "" };
        bd.update(TABELA_AVALIACAO, values, where, whereArgs);
        if (flag_envia_servidor == 1) {
            boolean tem_internet = TemConexao();
            if (tem_internet) {
                REST cliREST = new REST();
                try {
                    cliREST.atualizarAvaliacao(avaliacao);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Login.avaliacoes_atualizados_locais.add(avaliacao);
                Login.flag_enviar_avaliacoes_atualizados_servidor = true;
            }
        }
    }

    public int quantidade_avaliacoes_comentario(long id_comentario) {
        int num_avaliacoes = 0;
        try {
            Cursor c = bd.rawQuery("SELECT COUNT(*) FROM avaliacao where id_comentario=?", new String[] { id_comentario + "" });
            if (c.moveToNext()) {
                num_avaliacoes = c.getInt(0);
            }
        } catch (SQLException e) {
            Log.e("Teste", "Erro ao buscar quantidade de avalia��es do coment�rio: " + e.toString());
            return 0;
        }
        return num_avaliacoes;
    }

    public int quantidade_avaliacoes_positivas(long id_comentario) {
        int num_avaliacoes = 0;
        try {
            Cursor c = bd.rawQuery("SELECT COUNT(*) FROM avaliacao where id_comentario=? and flag_avaliacao = 1", new String[] { id_comentario + "" });
            if (c.moveToNext()) {
                num_avaliacoes = c.getInt(0);
            }
        } catch (SQLException e) {
            Log.e("Teste", "Erro ao buscar quantidade de avalia��es do coment�rio: " + e.toString());
            return 0;
        }
        return num_avaliacoes;
    }

    public int quantidade_avaliacoes_negativas(long id_comentario) {
        int num_avaliacoes = 0;
        try {
            Cursor c = bd.rawQuery("SELECT COUNT(*) FROM avaliacao where id_comentario=? and flag_avaliacao = 0", new String[] { id_comentario + "" });
            if (c.moveToNext()) {
                num_avaliacoes = c.getInt(0);
            }
        } catch (SQLException e) {
            Log.e("Teste", "Erro ao buscar quantidade de avalia��es do coment�rio: " + e.toString());
            return 0;
        }
        return num_avaliacoes;
    }

    public boolean usuario_curtiu_comentario(long id_usuario, long id_comentario) {
        try {
            Cursor c = bd.rawQuery("SELECT * FROM avaliacao where id_usuario=? and id_comentario=?", new String[] { id_usuario + "", id_comentario + "" });
            if (c.getCount() > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            Log.e("Teste", "Erro ao buscar quantidade de coment�rios por t�tulo: " + e.toString());
            return false;
        }
    }

    public boolean verifica_tupla_avaliacao(long id_usuario, long id_comentario) {
        try {
            Cursor c = bd.rawQuery("SELECT * FROM avaliacao where id_usuario=? and id_comentario=?", new String[] { id_usuario + "", id_comentario + "" });
            if (c.getCount() > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            Log.e("Teste", "Erro ao buscar tupla na tabela de avalia��o: " + e.toString());
            return false;
        }
    }

    public List<Avaliacao> listaAvaliacoesNaoSincronizados() {
        REST cliREST = new REST();
        List<Avaliacao> avaliacoes = null;
        if (Login.idCentroAtual != 0) {
            try {
                Cursor c = bd.rawQuery("SELECT a.* from Avaliacao a, comentario c, unidade u " + "where (a.id_comentario = c._id) and (c.id_unidade = u._id) and " + "(u.id_centro = ?) " + "UNION " + "SELECT a.* from Avaliacao a, comentario c, lugar l " + "where (a.id_comentario = c._id) and (c.id_lugar = l._id) and " + "(l.id_centro = ?) ORDER BY lastmodified DESC ", new String[] { "" + Login.idCentroAtual, "" + Login.idCentroAtual });
                c.moveToLast();
                String stringData;
                if (c.getCount() == 0) {
                    try {
                        avaliacoes = (List<Avaliacao>) cliREST.listarAvaliacoes();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    stringData = c.getString(c.getColumnIndex("lastmodified"));
                    avaliacoes = (List<Avaliacao>) cliREST.listarAvaliacoesNaoSincronizados(stringData);
                }
                for (int i = 0; i < avaliacoes.size(); i++) {
                    if (verifica_tupla_avaliacao(avaliacoes.get(i).getId_usuario(), avaliacoes.get(i).getId_comentario()) == true) {
                        atualizar(avaliacoes.get(i), 0);
                    } else {
                        inserir(avaliacoes.get(i), 0);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return avaliacoes;
        }
        return null;
    }

    public void enviaAvaliacoesNaoSincronizados() throws Exception {
        if (Login.flag_enviar_avaliacoes_servidor == true) {
            REST cliRest = new REST();
            for (int i = 0; i < Login.avaliacoes_locais.size(); i++) {
                cliRest.inserirAvaliacao(Login.avaliacoes_locais.get(i));
            }
            Login.avaliacoes_locais.clear();
            Login.flag_enviar_avaliacoes_servidor = false;
        }
    }

    public void enviaAvaliacoesAtualizadosNaoSincronizados() throws Exception {
        if (Login.flag_enviar_avaliacoes_atualizados_servidor == true) {
            REST cliRest = new REST();
            for (int i = 0; i < Login.avaliacoes_atualizados_locais.size(); i++) {
                cliRest.atualizarAvaliacao(Login.avaliacoes_atualizados_locais.get(i));
            }
            Login.avaliacoes_atualizados_locais.clear();
            Login.flag_enviar_avaliacoes_atualizados_servidor = false;
        }
    }

    /*****************************************************************/
    public Cursor getCursorDicionario(long id_comentario) {
        try {
            Cursor c = bd.rawQuery("SELECT * FROM dicionario where id_comentario=? and origem=1", new String[] { "" + id_comentario });
            return c;
        } catch (SQLException e) {
            Log.e("Teste", "Erro ao buscar as palavras-chave: " + e.toString());
            return null;
        }
    }

    public void inserir(Dicionario dicionario, int flag_envia_servidor) {
        ContentValues values = new ContentValues();
        values.put(Dicionarios.ID_COMENTARIO, dicionario.getId_comentario());
        values.put(Dicionarios.PALAVRA, dicionario.getPalavra());
        values.put(Dicionarios.ORIGEM, dicionario.getOrigem());
        values.put(Dicionarios.LASTMODIFIED, dicionario.getLastmodified());
        bd.insert(TABELA_DICIONARIO, "", values);
        if (flag_envia_servidor == 1) {
            boolean tem_internet = TemConexao();
            if (tem_internet) {
                REST cliREST = new REST();
                try {
                    cliREST.inserirDicionario(dicionario);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Login.dicionarios_locais.add(dicionario);
                Login.flag_enviar_dicionarios_servidor = true;
            }
        }
    }

    public void deletarPalavraChave(long idComentario) {
        String where = Dicionarios.ID_COMENTARIO + "=?";
        String _id = String.valueOf(idComentario);
        String[] whereArgs = new String[] { _id };
        bd.delete(TABELA_DICIONARIO, where, whereArgs);
    }

    public List<Comentario> buscarDicionario(String consulta) {
        List<Comentario> comentarios = new ArrayList<Comentario>();
        String SELECT = "SELECT c0.* FROM " + consulta;
        try {
            Cursor c = bd.rawQuery(SELECT, new String[] {});
            if (c.moveToFirst()) {
                int idxComentarioID = c.getColumnIndex(Comentarios._ID);
                do {
                    Comentario comentario = buscarComentario(c.getLong(idxComentarioID));
                    comentarios.add(comentario);
                } while (c.moveToNext());
            } else {
                return null;
            }
        } catch (SQLException e) {
            Log.e("Teste", "Erro ao buscar dicionario: " + e.toString());
            return null;
        }
        return comentarios;
    }

    public List<Dicionario> buscarPalavraChave(long id_comentario) {
        Cursor c = getCursorDicionario(id_comentario);
        List<Dicionario> dicionario = new ArrayList<Dicionario>();
        if (c.moveToFirst()) {
            int idxId = c.getColumnIndex(BaseColumns._ID);
            int idxComentarioID = c.getColumnIndex(Dicionarios.ID_COMENTARIO);
            int idxPalavra = c.getColumnIndex(Dicionarios.PALAVRA);
            int idxOrigem = c.getColumnIndex(Dicionarios.ORIGEM);
            int idxLastmodified = c.getColumnIndex(Dicionarios.LASTMODIFIED);
            do {
                Dicionario palavra_chave = new Dicionario();
                dicionario.add(palavra_chave);
                palavra_chave.setId(c.getLong(idxId));
                palavra_chave.setId_comentario(c.getLong(idxComentarioID));
                palavra_chave.setPalavra(c.getString(idxPalavra));
                palavra_chave.setOrigem(c.getInt(idxOrigem));
                palavra_chave.setLastmodified(c.getString(idxLastmodified));
            } while (c.moveToNext());
            return dicionario;
        }
        return null;
    }

    public List<Dicionario> listaDicionarioNaoSincronizados() {
        REST cliREST = new REST();
        List<Dicionario> dicionarios = null;
        try {
            Cursor c = bd.rawQuery("SELECT * FROM dicionario ORDER BY _id ASC", new String[] {});
            c.moveToLast();
            String stringData;
            if (c.getCount() == 0) {
                stringData = "2012-01-01 00:00:00";
            } else {
                stringData = c.getString(c.getColumnIndex("lastmodified"));
            }
            dicionarios = (List<Dicionario>) cliREST.listarDicionariosNaoSincronizados(stringData);
            for (int i = 0; i < dicionarios.size(); i++) {
                inserir(dicionarios.get(i), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dicionarios;
    }

    public void enviaDicionariosNaoSincronizado() throws Exception {
        if (Login.flag_enviar_dicionarios_servidor == true) {
            REST cliRest = new REST();
            for (int i = 0; i < Login.dicionarios_locais.size(); i++) {
                cliRest.inserirDicionario(Login.dicionarios_locais.get(i));
            }
            Login.dicionarios_locais.clear();
            Login.flag_enviar_dicionarios_servidor = false;
        }
    }

    public List<Dicionario> listaDicionariosNaoSincronizados() {
        REST cliREST = new REST();
        List<Dicionario> dicionarios = null;
        if (Login.idCentroAtual != 0) {
            try {
                Cursor c = bd.rawQuery("SELECT d.* FROM dicionario d, comentario c, unidade u " + "WHERE (d.id_comentario = c._id) and (c.id_unidade = u._id) and (u.id_centro = ?) " + "UNION " + "SELECT d.* FROM dicionario d, comentario c, lugar l " + "where (d.id_comentario = c._id) and (c.id_lugar = l._id) and (l.id_centro = ?) " + "ORDER BY lastmodified ASC", new String[] { "" + Login.idCentroAtual, "" + Login.idCentroAtual });
                c.moveToLast();
                String stringData;
                if (c.getCount() == 0) {
                    try {
                        dicionarios = (List<Dicionario>) cliREST.listarDicionarios();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    stringData = c.getString(c.getColumnIndex("lastmodified"));
                    dicionarios = (List<Dicionario>) cliREST.listarDicionariosNaoSincronizados(stringData);
                }
                for (int i = 0; i < dicionarios.size(); i++) {
                    inserir(dicionarios.get(i), 0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return dicionarios;
        }
        return null;
    }

    /*****************************************************************/
    public Cursor getCursorTipo() {
        try {
            return bd.query(TABELA_TIPO, Tipo.colunas, null, null, null, null, null, null);
        } catch (SQLException e) {
            Log.e("Teste", "Erro ao buscar os tipos: " + e.toString());
            return null;
        }
    }

    public Tipo buscarTipo(long id) {
        Cursor c = bd.query(true, TABELA_TIPO, Tipo.colunas, BaseColumns._ID + "=" + id, null, null, null, null, null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            Tipo tipo = new Tipo();
            tipo.setId(c.getLong(0));
            tipo.setNome_tipo(c.getString(1));
            tipo.setTipo(c.getString(2));
            return tipo;
        }
        return null;
    }

    public Tipo buscarTipo(String nomeTipo) {
        Cursor c = bd.rawQuery("SELECT * FROM Tipo where nome_tipo=?", new String[] { nomeTipo });
        if (c.getCount() > 0) {
            c.moveToFirst();
            Tipo tipo = new Tipo();
            tipo.setId(c.getLong(0));
            tipo.setNome_tipo(c.getString(1));
            tipo.setTipo(c.getString(2));
            return tipo;
        }
        return null;
    }

    public List<Tipo> listarTipos() {
        Cursor c = getCursorTipo();
        List<Tipo> tipos = new ArrayList<Tipo>();
        if (c.moveToFirst()) {
            int idxId = c.getColumnIndex(BaseColumns._ID);
            int idxNomeTipo = c.getColumnIndex(Tipos.NOME_TIPO);
            int idxTipo = c.getColumnIndex(Tipos.TIPO);
            do {
                Tipo tipo = new Tipo();
                tipos.add(tipo);
                tipo.setId(c.getLong(idxId));
                tipo.setNome_tipo(c.getString(idxNomeTipo));
                tipo.setTipo(c.getString(idxTipo));
            } while (c.moveToNext());
        }
        return tipos;
    }

    /*****************************************************************/
    public ArrayList<Email> buscarEmailsDaUnidade(String nome) {
        ArrayList<Email> emails = new ArrayList<Email>();
        Long id_centro = buscarUnidade(nome).getId();
        try {
            Cursor c = bd.rawQuery("SELECT * FROM email where id_centro=?", new String[] { id_centro + "" });
            if (c.moveToFirst()) {
                int idxId = c.getColumnIndex(BaseColumns._ID);
                int idxCentroID = c.getColumnIndex(Emails.ID_CENTRO);
                int idxEmail = c.getColumnIndex(Emails.EMAIL);
                do {
                    Email email = new Email();
                    emails.add(email);
                    email.setId(c.getLong(idxId));
                    email.setId_centro(c.getLong(idxCentroID));
                    email.setEmail(c.getString(idxEmail));
                } while (c.moveToNext());
            } else return null;
        } catch (SQLException e) {
            Log.v("Debug", "Erro ao buscar o email da unidade: " + e.toString());
            return null;
        }
        return emails;
    }

    /*****************************************************************/
    public ArrayList<Telefone> buscarTelefonesDaUnidade(String nome) {
        ArrayList<Telefone> telefones = new ArrayList<Telefone>();
        Long id_centro = buscarUnidade(nome).getId();
        try {
            Cursor c = bd.rawQuery("SELECT * FROM telefone where id_centro=?", new String[] { id_centro + "" });
            if (c.moveToFirst()) {
                int idxId = c.getColumnIndex(BaseColumns._ID);
                int idxCentroID = c.getColumnIndex(Telefones.ID_CENTRO);
                int idxNumero = c.getColumnIndex(Telefones.NUMERO);
                do {
                    Telefone telefone = new Telefone();
                    telefones.add(telefone);
                    telefone.setId(c.getLong(idxId));
                    telefone.setId_centro(c.getLong(idxCentroID));
                    telefone.setNumero(c.getString(idxNumero));
                } while (c.moveToNext());
            } else return null;
        } catch (SQLException e) {
            Log.v("Debug", "Erro ao buscar o telefone da unidade: " + e.toString());
            return null;
        }
        return telefones;
    }

    /**************************************************************/
    public void fechar() {
        if (bd != null) {
            bd.close();
        }
    }
}
