package br.gov.demoiselle.escola.persistence.dao.implementation;

import static org.junit.Assert.fail;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.inject.Inject;
import junit.framework.Assert;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import br.gov.demoiselle.escola.bean.Aluno;
import br.gov.demoiselle.escola.persistence.dao.IAlunoDAO;

@RunWith(Arquillian.class)
public class AlunoDAOTest {

    @Deployment
    public static JavaArchive createTestArchive() throws IllegalArgumentException, IOException {
        return ShrinkWrap.create(JavaArchive.class, "test.jar").addManifestResource(new File("src/test/resources/beans.xml"), ArchivePaths.create("beans.xml")).addClasses(AlunoDAO.class);
    }

    @Inject
    IAlunoDAO alunoDAO;

    @Test
    public void testListarAluno() {
        List<Aluno> result = alunoDAO.listarAluno();
        Assert.assertNotNull("Deve retornar no mínimo uma lista vazia;", result);
    }

    @Test
    public void testListarAlunoPage() {
        fail("Not yet implemented");
    }

    @Test
    public void testBuscarAlunoAluno() {
        fail("Not yet implemented");
    }

    @Test
    public void testFiltrarAlunoAluno() {
        fail("Not yet implemented");
    }

    @Test
    public void testFiltrarAlunoAlunoPage() {
        fail("Not yet implemented");
    }

    @Test
    public void testAlterarDetalhe() {
        fail("Not yet implemented");
    }

    @Test
    public void testBuscarAlunoUsuario() {
        fail("Not yet implemented");
    }

    @Test
    public void testBuscarPorUsuario() {
        fail("Not yet implemented");
    }
}
