package persistenciaTestes;

import static org.junit.Assert.assertTrue;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import persistence.GerenteDePersistencia;
import persistence.util.Coordinates;
import persistence.util.Estado;
import beans.Caso;
import beans.Foto;
import exceptions.CasoNotFoundException;
import exceptions.FotoAlreadySavedException;

public class CasoTests {

    private static GerenteDePersistencia gerente;

    private static Caso caso;

    @BeforeClass
    public static void configurarTudo() {
        gerente = GerenteDePersistencia.getInstance(true);
        caso = new Caso();
        gerente.removeAllCasos();
        gerente.removeAllFotos();
    }

    @AfterClass
    public static void zerarTudo() {
        gerente.removeAllCasos();
        gerente.removeAllFotos();
    }

    @Test
    public void testSave() {
        String estado = Estado.PE.toString();
        String cidade = "cidade";
        String bairro = "bairro";
        String rua = "rua";
        int numero = 4;
        String nome = "nome";
        float areaConst = 1;
        float areaTotal = 2;
        int vagasGaragem = 9;
        int quartos = 6;
        int suites = 7;
        int baneiros = 3;
        String tipo = "tipo";
        float preco = 5;
        String tipoNegocio = "tipo de negocio";
        Coordinates location = new Coordinates(0, 0);
        caso.setAreaConstruida(areaConst);
        caso.setAreaTotal(areaTotal);
        caso.setBanheiros(baneiros);
        caso.setBairro(bairro);
        caso.setCidade(cidade);
        caso.setEstado(estado);
        caso.setNome(nome);
        caso.setNumero(numero);
        caso.setPreco(preco);
        caso.setQuartos(quartos);
        caso.setRua(rua);
        caso.setSuites(suites);
        caso.setTipo(tipo);
        caso.setTipoNegocio(tipoNegocio);
        caso.setVagasGaragem(vagasGaragem);
        caso.setLocation(location);
        gerente.saveCaso(caso);
        if (gerente.getAllCasos().size() == 1) {
            assertTrue(true);
        } else {
            assertTrue(false);
        }
    }

    @Test
    public void testGetFotos() {
        try {
            gerente.saveFoto(caso, "path1");
        } catch (FotoAlreadySavedException e) {
            assertTrue(false);
        }
        try {
            gerente.saveFoto(caso, "path1");
        } catch (FotoAlreadySavedException e) {
            assertTrue(true);
        }
        try {
            gerente.saveFoto(caso, "path2");
        } catch (FotoAlreadySavedException e) {
            assertTrue(false);
        }
        try {
            gerente.saveFoto(caso, "path3");
        } catch (FotoAlreadySavedException e) {
            assertTrue(false);
        }
        List<Foto> fotos = gerente.getFotos(caso);
        if (fotos.size() == 3) {
            assertTrue(true);
        } else {
            assertTrue(false);
        }
    }

    @Test
    public void testUpdate() {
        caso.setCidade("differentCidade");
        try {
            gerente.updateCaso(caso);
            long id = caso.getIdCaso();
            if (gerente.getCaso(id).getCidade().equals("differentCidade")) {
                assertTrue(true);
            } else {
                assertTrue(false);
            }
        } catch (CasoNotFoundException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testRemove() {
        try {
            gerente.removeCaso(caso);
            if (gerente.getAllCasos().size() == 0) {
                assertTrue(true);
            } else {
                assertTrue(false);
            }
        } catch (CasoNotFoundException e) {
            assertTrue(false);
        }
    }
}
