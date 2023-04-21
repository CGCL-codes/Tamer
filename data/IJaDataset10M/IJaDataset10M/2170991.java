package objetos.Citta;

public class Fazenda extends Objeto {

    public Fazenda(int posX, int posY, int posZ, int ID) {
        super(posX, posY, posZ, 4, 8, 1, ID);
        setPessoas(10);
        setEsgoto(10);
        setLixo(50);
        setAgua(10);
        setAlimento(-400);
        setEnergia(25);
        setNome("Fazenda");
    }
}
