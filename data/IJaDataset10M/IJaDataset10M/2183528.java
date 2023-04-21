package test.reverspring;

import java.util.HashMap;
import java.util.List;
import junit.framework.TestCase;
import org.reverspring.engine.BeanDescriptor;
import org.reverspring.engine.BeanSet;
import org.reverspring.xml.XmlGenerator;
import test.reverspring.beans.Indirizzo;
import test.reverspring.beans.ListaDiPersone;
import test.reverspring.beans.Persona;

public class TestBeanSet extends TestCase {

    public void testAddBeanDescriptor() {
        Persona persona = new Persona();
        persona.nome = "luigi";
        persona.setCongnome("Dell'Aquila");
        persona.setIndirizzo(new Indirizzo());
        persona.getIndirizzo().setVia("via di casa mia");
        persona.getIndirizzo().setCivico(1);
        persona.getIndirizzo().setProprietario(persona);
        BeanSet set = new BeanSet();
        set.addBeanDescriptor(persona, "personaId");
        List<BeanDescriptor> beans = set.getBeans();
        assertEquals(2, beans.size());
        for (BeanDescriptor descriptor : beans) {
            assertNotNull(descriptor.getConstructor());
            if (descriptor.getBean().getClass().equals(Persona.class)) {
                assertTrue(descriptor.getProperties().contains("nome"));
                assertEquals("luigi", descriptor.getBinding("nome").getDescriptor().getBean());
                assertTrue(descriptor.getProperties().contains("indirizzo"));
                assertFalse(descriptor.getProperties().contains("cognome"));
            }
            if (descriptor.getBean().getClass().equals(Indirizzo.class)) {
                assertTrue(descriptor.getProperties().contains("via"));
                assertEquals("via di casa mia", descriptor.getBinding("via").getDescriptor().getBean());
                assertFalse(descriptor.getProperties().contains("civico"));
            }
        }
        System.out.println(XmlGenerator.getXml(set));
        System.out.println();
        System.out.println();
    }

    public void testListInBean() {
        ListaDiPersone lista = new ListaDiPersone();
        BeanSet set = new BeanSet();
        lista.mappaStringhe = new HashMap<String, String>();
        lista.mappaPersone = new HashMap<Persona, Persona>();
        lista.mappaStringhe.put("ciao", "pippo");
        Persona chiave = new Persona();
        chiave.nome = "chiave";
        chiave.setCongnome("cognomeChiave");
        Persona valore = new Persona();
        valore.nome = "valore";
        valore.setCongnome("cognomeValore");
        lista.mappaPersone.put(chiave, valore);
        set.addBeanDescriptor(lista, "persone");
        System.out.println(XmlGenerator.getXml(set));
    }
}
