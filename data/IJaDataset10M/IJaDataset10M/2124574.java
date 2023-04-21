package junit;

import junit.framework.Assert;
import it.ge.condam.comune.Comune;
import it.ge.condam.comune.service.ComuneServiceUtil;
import it.ge.condam.residenza.Residenza;
import it.ge.condam.residenza.service.ResidenzaServiceUtil;
import it.ge.condam.toponimo.Toponimo;
import it.ge.condam.toponimo.service.ToponimoServiceUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Leone
 *
 * 26/mar/2011
 */
public class ResidenzaServiceTest {

    /**
	 * @throws java.lang.Exception
	 */
    @Before
    public void setUp() throws Exception {
        ResidenzaServiceUtil.initResidenzaServiceUtil();
        ComuneServiceUtil.initComuneServiceUtil();
        ToponimoServiceUtil.initToponimoServiceUtil();
    }

    @Test
    public void testCreateResidenza() throws Exception {
        Toponimo toponimo = ToponimoServiceUtil.find_Toponimi().get(2);
        String descrizione = "fereggiano";
        String civico = "1";
        Comune luogo = ComuneServiceUtil.find_Comune("16144");
        String domicilioFiscale = "S";
        ResidenzaServiceUtil.create_Residenza(toponimo, descrizione, civico, luogo, domicilioFiscale);
    }

    @Test
    public void testFindResidenza() throws Exception {
        int id = ResidenzaServiceUtil.find_MaxIdResidenza();
        Residenza residenza = ResidenzaServiceUtil.find_Residenza(id);
        if (residenza != null) {
            System.out.println("id: " + residenza.getId());
            Toponimo toponimo = residenza.getToponimo();
            System.out.println("toponimo: " + toponimo.getDescrizione());
            System.out.println("descrizione: " + residenza.getDescrizione());
            System.out.println("civico: " + residenza.getCivico());
            Comune comune = residenza.getLuogo();
            System.out.println("comune: " + comune.getDescrizione());
            System.out.println("Domicilio_fiscale: " + residenza.getDomicilio_fiscale());
            System.out.println("-----------------------------------");
        }
    }

    @Test
    public void testFindResidenzaDescrCiv() throws Exception {
        Residenza residenza = ResidenzaServiceUtil.find_Residenza("fereggiano", "");
        Assert.assertNotNull("residenza non trovata", residenza);
        System.out.println("id: " + residenza.getId());
        Toponimo toponimo = residenza.getToponimo();
        System.out.println("toponimo: " + toponimo.getDescrizione());
        System.out.println("descrizione: " + residenza.getDescrizione());
        System.out.println("civico: " + residenza.getCivico());
        Comune comune = residenza.getLuogo();
        System.out.println("comune: " + comune.getDescrizione());
        System.out.println("Domicilio_fiscale: " + residenza.getDomicilio_fiscale());
        System.out.println("-----------------------------------");
    }
}
