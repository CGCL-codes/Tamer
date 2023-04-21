package formateadores;

/**
 * Modifica los datos, en general para representar tamaños de ficheros en
 * formato XX,YYY Mb.
 * 
 * @author Rodrigo Villamil Perez
 */
public class FormateadorDeTamanioEnMb implements IFormateadorDeDatos {

    private static final long serialVersionUID = 1L;

    public FormateadorDeTamanioEnMb() {
    }

    /**
     * Formatea el objeto 'valor' retornando un string.
     */
    public Object formatea(Object valor) {
        long Kb = (Long) valor / 1024;
        long Mb = Kb / 1024;
        Kb = Kb % 1024;
        return String.format("%d,%02d Mb.", Mb, Kb);
    }
}
