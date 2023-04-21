package ensino2;

public class Aviso extends NoticiaAviso {

    private int codProfessor;

    public Aviso(int codNotAv, int codProf, String titulo, String conteudo, long dataCriacao) {
        super(codNotAv, dataCriacao, titulo, conteudo);
        setCodProfessor(codProf);
    }

    public void setCodProfessor(int arg) {
        codProfessor = arg;
    }

    public int getCodProfessor() {
        return codProfessor;
    }
}
