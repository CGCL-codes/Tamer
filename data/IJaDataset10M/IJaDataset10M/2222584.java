package exemplo4_acesso_aleatorio;

import exemplo2_acesso_sequencial.Pessoa;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author Mathyas
 */
public class PessoaAcessoAleatorio extends Pessoa {

    public static final int TAMANHO = 94;

    public PessoaAcessoAleatorio() {
        super("", "", 0, "");
    }

    public void ler(RandomAccessFile arquivo) throws IOException {
        setNome(lerNome(arquivo));
        setSobreNome(lerNome(arquivo));
        setIdade(arquivo.readInt());
        setEndereco(lerNome(arquivo));
    }

    public String lerNome(RandomAccessFile arquivo) throws IOException {
        char nome[] = new char[15];
        char temp;
        for (int i = 0; i < nome.length; i++) {
            temp = arquivo.readChar();
            nome[i] = temp;
        }
        return new String(nome).replace('\0', ' ');
    }

    public void gravar(RandomAccessFile arquivo) throws IOException {
        gravarNome(arquivo, getNome());
        gravarNome(arquivo, getSobreNome());
        arquivo.writeInt(getIdade());
        gravarNome(arquivo, getEndereco());
    }

    public void gravarNome(RandomAccessFile arquivo, String nome) throws IOException {
        StringBuffer buffer = null;
        if (nome != null) buffer = new StringBuffer(nome); else buffer = new StringBuffer(15);
        buffer.setLength(15);
        arquivo.writeChars(buffer.toString());
    }
}
