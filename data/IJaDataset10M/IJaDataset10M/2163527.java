package neoAtlantis.utilidades.pruebas;

import neoAtlantis.utilidades.entity.ContainerEntities;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Hiryu (asl_hiryu@yahoo.com)
 */
public class ProbadorContainer {
    private ContainerEntities<Entidad> con;
    private Entidad e=new Entidad();

    public ProbadorContainer() {
        e.setIdUnico(10);

        con=new ContainerEntities<Entidad>(true);
        con.add(new Entidad());
        con.add(new Entidad());
        con.add(new Entidad());
        con.add(e);
    }

    @Test
    public void pruebaContenedor(){
        System.out.println(con);
    }

    @Test
    public void pruebaTamaño(){
        System.out.println("Tam: "+con.size());
    }

    @Test
    public void pruebaContiene(){
        System.out.println("Contiene: "+con.contains(e));
    }

    @Test
    public void pruebaRemueve(){
        con.remove(e);
        con.remove(2);
        System.out.println("Tam2: "+con.size());
    }

    @Test
    public void pruebaSwitch(){
        con.remove(1);
        con.remove(0);
        System.out.println("Antes: "+con);
        con.switchEntities(0, 1);
        System.out.println("Despues: "+con);
    }

    @Test
    public void pruebaPaginas(){
        System.out.println("Paginas: "+con.getPages());
        System.out.println("Adelante: "+con.nextPage());
        System.out.println("Atras: "+con.previousPage());
        System.out.println("Actual: "+con.getPage());
    }
}