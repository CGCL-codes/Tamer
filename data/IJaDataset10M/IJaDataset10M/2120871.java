package objetos.Citta;

public class Casa extends Objeto {

    public Casa(int posX, int posY, int posZ, int ID) {
        super(posX, posY, posZ, 2, 2, 1, ID);
        setPessoas(4);
        setEsgoto(4);
        setLixo(4);
        setAgua(4);
        setAlimento(4);
        setEnergia(4);
        setNome("Casa");
    }
}
