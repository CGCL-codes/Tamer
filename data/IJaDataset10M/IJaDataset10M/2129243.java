package interfaz.crearRed;

public class Paso1Bean {

    private int numUsuarios;

    private String[] perfilesSeleccionados;

    protected Paso1Bean(int i, String[] a) {
        numUsuarios = i;
        perfilesSeleccionados = a;
    }

    protected int getNumUsuarios() {
        return numUsuarios;
    }

    protected void setNumUsuarios(int numUsuarios) {
        this.numUsuarios = numUsuarios;
    }

    protected String[] getPerfilesSeleccionados() {
        return perfilesSeleccionados;
    }

    protected void setPerfilesSeleccionados(String[] perfilesSeleccionados) {
        this.perfilesSeleccionados = perfilesSeleccionados;
    }

    protected boolean hayPerfilesSeleccionados() {
        return (perfilesSeleccionados.length != 0);
    }
}
