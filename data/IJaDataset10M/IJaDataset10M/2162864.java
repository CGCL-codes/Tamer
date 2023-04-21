package camadaSO;

public interface IServidorClienteRMI extends java.rmi.Remote {

    public void passaListaPCliente(java.util.List<String> nomes, java.util.List<String> senhas, java.util.List<Integer> pontuacoes) throws java.rmi.RemoteException;

    public void limpaListaAtual() throws java.rmi.RemoteException;

    public void apresentarMensagemDesafio(String desafiante) throws java.rmi.RemoteException;

    public void apresentarRespostaDesafio(boolean resposta, String desafiado) throws java.rmi.RemoteException;

    public void atualizarPecasJogador(int xpeca1, int ypeca1, int xpeca2, int ypeca2, String movimento) throws java.rmi.RemoteException;

    public void atualizarPecasJogadorAposCombate(int xpeca1, int ypeca1, int xpeca2, int ypeca2, String vencedor, String resultado) throws java.rmi.RemoteException;

    public void habilitaTabuleiro(String adversario) throws java.rmi.RemoteException;

    public void finalizaPartida(boolean ganhou) throws java.rmi.RemoteException;

    public void atualizaTela(int posicaoY, int posicaoX, int posicaoY2, int posicaoX2) throws java.rmi.RemoteException;
}
