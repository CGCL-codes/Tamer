package au.org.tpac.portal.repository;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import au.org.tpac.portal.domain.Category;
import au.org.tpac.portal.domain.Dataset;
import au.org.tpac.portal.domain.DlpUser;
import au.org.tpac.portal.domain.PartyIndividual;
import au.org.tpac.portal.domain.PartyOrganisation;
import au.org.tpac.portal.domain.Subject;
import au.org.tpac.portal.repository.RowMappers.DatasetMapper;
import au.org.tpac.portal.repository.RowMappers.CategoryMapper;
import au.org.tpac.portal.repository.RowMappers.PartyIndividualMapper;
import au.org.tpac.portal.repository.RowMappers.PartyOrganisationMapper;
import au.org.tpac.portal.repository.RowMappers.PartySubjectMapper;
import au.org.tpac.portal.repository.RifPartyMapper.RifPartyIndividualMapper;
import au.org.tpac.portal.repository.RifPartyMapper.RifPartyOrganisationMapper;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * The Class JdbcDlpUserDao.
 */
public class AndsPartyDao implements PartyDao, RemotePartyDao, InitializingBean {

    private RifPartyOrganisationMapper partyOrganisationMapper;

    private RifPartyIndividualMapper partyIndividualMapper;

    private AndsXmlConnection conn;

    private List<String> ignorePrefixes = new Vector<String>();

    private final int MAX_RESULTS = 10;

    private String rifcsUrl = "https://services.ands.org.au/sandbox/orca/services/getRegistryObjects.php?" + "search=&source_key=&object_group=&collections=&services=&parties=party&activities=&status=";

    private PartyDao partyStore;

    /**
	 * Constructor
	 */
    public AndsPartyDao() {
        super();
    }

    @Override
    public void addParty(final PartyIndividual party) {
    }

    @Override
    public void updateParty(final PartyIndividual party) {
    }

    @Override
    public void removeParty(final PartyIndividual party) {
    }

    private void addPartySubject(final PartyIndividual party) {
    }

    private void removePartySubject(final PartyIndividual party) {
    }

    @Override
    public void addParty(final PartyOrganisation party) {
    }

    @Override
    public void updateParty(final PartyOrganisation party) {
    }

    @Override
    public void removeParty(final PartyOrganisation party) {
    }

    private void addPartySubject(final PartyOrganisation party) {
    }

    private void removePartySubject(final PartyOrganisation party) {
    }

    @Override
    public List<PartyOrganisation> listOrganisations() {
        List<PartyOrganisation> list = new Vector<PartyOrganisation>();
        return list;
    }

    @Override
    public List<PartyIndividual> listIndividuals() {
        List<PartyIndividual> list = new Vector<PartyIndividual>();
        return list;
    }

    public List<PartyIndividual> listIndividuals(String searchString) {
        return partyStore.listIndividuals(searchString);
    }

    @Override
    public List<PartyOrganisation> listOrganisations(String searchString) {
        return this.partyStore.listOrganisations(searchString);
    }

    /**
	 * Set the URL to obtain RifCS records from.
	 * @param url
	 */
    public void setRifcsUrl(String url) {
        this.rifcsUrl = url;
    }

    public void setXmlConnection(AndsXmlConnection conn) {
        this.conn = conn;
    }

    private boolean prefixFound(String haystack, List<String> needles) {
        for (String needle : needles) {
            if (haystack.indexOf(needle) == 0) {
                return true;
            }
        }
        return false;
    }

    /**
	 * Start import of parties from URI source
	 */
    public void startImport() {
        try {
            partyStore.removeAllOrganisations();
            partyStore.removeAllIndividuals();
            List<PartyIndividual> list = conn.query("//rif:registryObject[rif:party/@type=\"person\"]", new RifPartyIndividualMapper());
            for (PartyIndividual party : list) {
                if (!prefixFound(party.getKey(), ignorePrefixes)) {
                    partyStore.addParty(party);
                }
            }
            List<PartyOrganisation> orgList = conn.query("//rif:registryObject[rif:party/@type=\"group\"]", new RifPartyOrganisationMapper());
            for (PartyOrganisation party : orgList) {
                if (!prefixFound(party.getKey(), ignorePrefixes)) {
                    partyStore.addParty(party);
                }
            }
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(conn);
        Assert.notNull(partyStore);
    }

    @Override
    public PartyIndividual getIndividual(String key) {
        return partyStore.getIndividual(key);
    }

    @Override
    public PartyOrganisation getOrganisation(String key) {
        return partyStore.getOrganisation(key);
    }

    public void setPartyDao(PartyDao partyDao) {
        this.partyStore = partyDao;
    }

    /**
	 * List of key prefixes belonging to Parties that shouldn't be imported.
	 * @param ignorePrefixes
	 */
    public void setIgnoreKeyPrefix(List<String> ignorePrefixes) {
        this.ignorePrefixes = ignorePrefixes;
    }

    @Override
    public void removeAllIndividuals() {
    }

    @Override
    public void removeAllOrganisations() {
    }
}
