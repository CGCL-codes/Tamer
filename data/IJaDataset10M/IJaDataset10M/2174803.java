package br.com.rnavarro.padroes.criacao.singleton;

public class ConexaoComBancoDeDados {

    private static ConexaoComBancoDeDados instancia;

    private String conexao;

    private ConexaoComBancoDeDados(String usuario, String senha) {
        System.out.println("Conex�o com banco de dados realizada");
        conexao = "Sou uma conex�o com o banco de dados";
    }

    public static ConexaoComBancoDeDados getInstancia() {
        if (instancia == null) {
            instancia = new ConexaoComBancoDeDados("root", "root");
        }
        return instancia;
    }

    public String getConexao() {
        return conexao;
    }
}
