package Biblioteca.Suporte;

public interface EventoPainel {

    public static final byte confirma = 1, pergunta = 2, arquivo = 3;

    public abstract void notifica(byte origem, String conteudo);
}
