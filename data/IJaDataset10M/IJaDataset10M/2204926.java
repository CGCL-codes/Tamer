package ensino2.dao;

import ensino2.comum.Conexao;

public abstract class NoticiaAvisoDao {

    public static final String TABELA_NOTICIA_AVISO = "es_noticia_aviso";

    public static final String COD_NOTICIA_AVISO = "cod_not_av";

    public static final String TITULO = "titulo";

    public static final String CONTEUDO = "conteudo";

    public static final String DATA_CRIACAO = "data_criacao";

    public static void salvar(boolean upd, int cod, String titulo, String conteudo, long dataCriacao) throws Exception {
        String sql;
        if (upd) {
            sql = "UPDATE " + TABELA_NOTICIA_AVISO + " SET `" + TITULO + "` = ? " + ", `" + CONTEUDO + "` = ? " + ", `" + DATA_CRIACAO + "` = ? WHERE " + COD_NOTICIA_AVISO + " = ? ;";
            Conexao.setPstmtSql(sql);
            Conexao.setPstmt(1, titulo);
            Conexao.setPstmt(2, conteudo);
            Conexao.setPstmt(3, dataCriacao);
            Conexao.setPstmt(4, cod);
        } else {
            sql = "INSERT INTO " + TABELA_NOTICIA_AVISO + " (`" + TITULO + "`, `" + CONTEUDO + "`, `" + DATA_CRIACAO + "`) VALUES (?, ?, ?);";
            Conexao.setPstmtSql(sql);
            Conexao.setPstmt(1, titulo);
            Conexao.setPstmt(2, conteudo);
            Conexao.setPstmt(3, dataCriacao);
        }
        Conexao.execUpd();
    }
}
