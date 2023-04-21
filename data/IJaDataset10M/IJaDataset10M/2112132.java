package br.nic.connector.acessibilidade;

import java.io.File;
import java.util.Iterator;
import br.nic.connector.dao.HostsDAO;
import br.nic.connector.database.HibernateFactory;
import br.nic.connector.database.Paginas;
import br.nic.connector.general.Constants;
import br.nic.connector.general.SimpleLog;
import br.nic.connector.generics.pages.AutomatedPageTest;
import br.nic.connector.generics.pages.GenericPageTest;

/**
 * This AutomatedTester implements tests related to the accessibility validation of the pages.
 * @author Pedro Hadek
 */
public class AutomatedAcessibilityTester extends AutomatedPageTest {

    private static AutomatedAcessibilityTester singleton;

    /**
	 * Returns the static instance of this class.
	 */
    public static AutomatedAcessibilityTester getInstance() {
        if (singleton == null) singleton = new AutomatedAcessibilityTester();
        return singleton;
    }

    /**
	 * Type is set in order to correctly print messages for this class.
	 */
    private AutomatedAcessibilityTester() {
        type = "Teste de Padrões de acessibilidade";
    }

    /**
	 * In order for the initialization of this class to proccess smoothly, a minimum of 128Mb
	 * must be available for the JVM, with 256Mb being recommended.
	 */
    @Override
    public boolean initialTestConditions() {
        if (Runtime.getRuntime().freeMemory() < Constants.DEFAULT_MINMEMORY) {
            System.err.println("AVISO! Esta rotina requer pelo menos 256 mega bytes de memória.");
            System.err.println("Recomenda-se rodar a JVM com -Xmx256m -Xms256m");
            return false;
        } else return true;
    }

    /**
	 * Returns the class responsible for implementing the test required for this AutomatedTester.
	 */
    @Override
    protected GenericPageTest getNewTestType() {
        return new TestAcessibility();
    }

    /**
	 * Writes the tested data to the DataBase. The data being written here is the accessibility
	 * test results, the page's name and the page's size.
	 */
    @Override
    protected void writeTestData(File currPage, Object returnValue) {
        pagesInfo.writeAccessibilityResult(currPage.getAbsolutePath(), currPage.length(), (Integer[]) returnValue);
    }

    /**
	 * Consolidates the accessibility data in the hosts database, indicating the acessibility
	 * of each site as a whole.
	 */
    @Override
    public void finalTestConditions() {
        HostsDAO hostsInfo = new HostsDAO(new HibernateFactory().getSession(), encrypt);
        hostsInfo.clearAcessibilityData();
        String host = null;
        for (Iterator<Object[]> it = pagesInfo.getAcessibilidadePaginas().iterator(); it.hasNext(); ) {
            try {
                Object[] errosAses = it.next();
                if (errosAses[3] != null) {
                    host = (String) errosAses[3];
                    if (errosAses[0] == null) hostsInfo.writeAccessibilidadeResults(host, "X"); else if ((Integer) errosAses[0] != 0) hostsInfo.writeAccessibilidadeResults(host, ""); else if (errosAses[1] == null) hostsInfo.writeAccessibilidadeResults(host, "AX"); else if ((Integer) errosAses[1] != 0) hostsInfo.writeAccessibilidadeResults(host, "A"); else if (errosAses[2] == null) hostsInfo.writeAccessibilidadeResults(host, "AAX"); else if ((Integer) errosAses[2] != 0) hostsInfo.writeAccessibilidadeResults(host, "AA"); else hostsInfo.writeAccessibilidadeResults(host, "AAA");
                }
            } catch (Exception e) {
                SimpleLog.getInstance().writeException(e, 3);
                if (host != null && host.equalsIgnoreCase("")) {
                    hostsInfo.writeAccessibilidadeResults(host, "X");
                }
            }
        }
    }

    /**
	 * If no date has been written, the test has not been done for that page.
	 */
    @Override
    protected String getCriticalTestType() {
        return Paginas.NAME_data_ases;
    }
}
