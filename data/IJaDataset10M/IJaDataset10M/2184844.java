package Nucleo.Excecao;

public class ErroExclusaoArquivo extends ErroEntradaSaida {

    public ErroExclusaoArquivo() {
        super();
    }

    public ErroExclusaoArquivo(String erro) {
        super(erro);
    }

    public ErroExclusaoArquivo(Excecao referencia) {
        super(referencia);
    }
}
