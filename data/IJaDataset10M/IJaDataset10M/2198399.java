package javaTeste.tInterface;

import java.classe.Interface;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

public class TesteCriarInterfaceAgua implements Especificacao2 {

    Interface agua = null;

    @Before
    public void up() {
        agua = new Interface("Agua");
    }

    @Test
    public void oCodigoDaInterfaceAgua() {
        Assert.assertEquals("interface Agua{}", agua.codigoFonte());
    }
}
