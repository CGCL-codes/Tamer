package classes_apresentacao;

public class CargoApresentacao extends ClasseApresentacao {

    public static final String COLUNA_NOME = "nome";

    @Override
    public String[] getAtributos() {
        return new String[] { COLUNA_NOME };
    }
}
