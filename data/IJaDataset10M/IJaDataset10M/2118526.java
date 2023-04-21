package framework.interfaces;

public interface IModuloBrazo extends IModulo {

    public void brazoSubir(int presicion);

    public void brazoBajar(int presicion);

    public void brazoDerecha(int presicion);

    public void brazoIzquierda(int presicion);

    public void manoAbrir(int presicion);

    public void manoCerrar(int presicion);

    public void manoAbrirMaximo();

    public void manoCerrarMaximo();

    public int[] getPosBrazo();

    public void setPosBrazo(int[] data);

    public int getValorVertical();

    public int getValorHorizontal();

    public int getValorMano();

    public void moverVerticalSuave(int posicion);

    public void moverHorizontalSuave(int posicion);

    public void moverManoSuave(int posicion);

    public int[] getMinimos();

    public int[] getMaximos();
}
