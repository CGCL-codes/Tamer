package AnaliseLexicaFinal.EstruturaDeDados;

public class Caractere {

    private char caractere;

    private int linha;

    public Caractere(char caractere, int linha) {
        this.caractere = caractere;
        this.linha = linha;
    }

    public static boolean isletra(char caractere) {
        if ((caractere > 64 && caractere < 91 || caractere > 96 && caractere < 123)) return true;
        return false;
    }

    public static boolean isDigito(char carcatere) {
        if (carcatere > 47 && carcatere < 58) return true;
        return false;
    }

    public char getCaractere() {
        return caractere;
    }

    public int getLinha() {
        return linha;
    }
}
