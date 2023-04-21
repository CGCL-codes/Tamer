package net.sf.ideoreport.engines.xml;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import net.sf.ideoreport.api.configuration.ConfigType;
import net.sf.ideoreport.api.configuration.exception.ConfigurationException;
import net.sf.ideoreport.api.datastructure.IDataManager;
import net.sf.ideoreport.api.datastructure.containers.data.DataContainer;
import net.sf.ideoreport.api.datastructure.containers.parameter.IParameterValues;
import net.sf.ideoreport.api.datastructure.containers.parameter.ParameterValues;
import net.sf.ideoreport.api.datastructure.exception.DataManagerException;
import net.sf.ideoreport.datamanagers.xml.XMLDataManager;

public class CompleteXMLTest extends XMLEngineGenericTest {

    /**
     * Test de bout en bout : 
     * chargement de donn�es via le xml data manager
     * et re-g�n�ration d'un XML en sortie avec le xml engine
     * @throws ConfigurationException
     * @throws DataManagerException
     */
    public void _testComplet(String pDataContainerName, String pDataProviderName, String pDataProvider2, String pExt) throws ConfigurationException, DataManagerException {
        IParameterValues vParams = new ParameterValues();
        IDataManager vXmlDataManager = XMLDataManager.getInstance("examples/xml-manager-examples.xml", ConfigType.CONFIG_TYPE_XML);
        List vDPs = new ArrayList();
        vDPs.add(vXmlDataManager.process(pDataProviderName, vParams));
        if (!StringUtils.isEmpty(pDataProvider2)) {
            vDPs.add(vXmlDataManager.process(pDataProvider2, vParams));
        }
        DataContainer vData = new DataContainer(pDataContainerName, vDPs);
        this.processReport(vParams, vData, "test-examples/" + pDataContainerName + "." + pExt, "examples/xml-engine-examples.xml");
    }

    public void testCompletCountries() throws ConfigurationException, DataManagerException {
        this._testComplet("countries_to_xml", "xml.countries", "", "xml");
    }

    public void testCompletContintents() throws ConfigurationException, DataManagerException {
        this._testComplet("continents_to_xml", "xml.continents", "", "xml");
    }

    public void testCompletContintentsToHtml() throws ConfigurationException, DataManagerException {
        this._testComplet("continents_to_html", "xml.continents", "", "html");
    }

    public void testCompletContintentsAndCountriesToHtml() throws ConfigurationException, DataManagerException {
        this._testComplet("continents_and_countries_to_html", "xml.countries", "xml.continents", "html");
    }

    public void testCompletContintentsAndCountriesMemeFichierToHtml() throws ConfigurationException, DataManagerException {
        this._testComplet("continents_and_countries_complet_to_html", "xml.complet.countries", "xml.complet.continents", "xml");
    }

    public void testCompletContintentsAndCountries2MemeFichierToHtml() throws ConfigurationException, DataManagerException {
        this._testComplet("continents_and_countries2_complet_to_html", "xml.complet.countries.traite", "xml.complet.continents", "xml");
    }
}
