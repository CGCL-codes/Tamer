/*
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.stuarthardy.momentum.persistence.dao.hibernate.instrument;

import java.util.List;

import junit.framework.Assert;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import org.stuarthardy.momentum.domain.instrument.Instrument;
import org.stuarthardy.momentum.domain.market.Market;
import org.stuarthardy.momentum.domain.market.MarketSector;
import org.stuarthardy.momentum.domain.market.Sector;
import org.stuarthardy.momentum.domain.types.*;
import org.stuarthardy.momentum.persistence.dao.types.ITypesDAO;

/**
 * momentum-persistence
 *
 * @author Stuart Hardy
 *
 */
public class HibernateInstrumentDAOTest extends AbstractDependencyInjectionSpringContextTests {
	private static Logger log = LoggerFactory.getLogger(HibernateInstrumentDAOTest.class);

	private static long persistedId;
	private MarketSector marketSector;
	private Market market;
	private Sector sector;
	private MarketType marketType;
	private InstrumentType instrumentType;
	private Country country;


	private HibernateInstrumentDAO instrumentDAO;
	private ITypesDAO<IType> typesDAO;

	/* (non-Javadoc)
	 * @see org.springframework.test.AbstractSingleSpringContextTests#setUp()
	 */
	protected void onSetUp() throws Exception {
		BasicConfigurator.configure();
		super.onSetUp();

		this.marketSector = this.typesDAO.saveIType(ITypesDAO.MAR, );
		this.market = this.typesDAO.saveIType(ITypesDAO.COUNTRY_TYPE, );
		this.sector = this.typesDAO.saveIType(ITypesDAO.COUNTRY_TYPE, );
		this.marketType = (MarketType) this.typesDAO.saveIType(ITypesDAO.MARKET_TYPE, new MarketType("S", "Stocks"));
		this.instrumentType = (InstrumentType) this.typesDAO.saveIType(ITypesDAO.INSTRUMENT_TYPE, new InstrumentType("Code", "Description"));
		this.country = (Country) this.typesDAO.saveIType(ITypesDAO.COUNTRY_TYPE, new Country("UK", "United Kingdom", new Region("EU", "Europe")));
	}

	/* (non-Javadoc)
	 * @see org.springframework.test.AbstractSingleSpringContextTests#getConfigLocations()
	 */
	protected String[] getConfigLocations() {
	  return new String[] {"file:src/test/resources/spring/test-persistence-appcontext.xml"};
	}

	/* (non-Javadoc)
	 * @see org.springframework.test.AbstractSingleSpringContextTests#tearDown()
	 */
	protected void onTearDown() throws Exception {
		super.onTearDown();
		BasicConfigurator.resetConfiguration();
	}

	/**
	 * Test method for {@link org.stuarthardy.momentum.persistence.dao.hibernate.instrument.HibernateInstrumentDAO#saveInstrument(org.stuarthardy.momentum.domain.instrument.Instrument)}.
	 * @throws Exception
	 */
	public final void testSaveInstrument() throws Exception {
		try {
			Instrument instrument = new Instrument("TEST", "Test Security", "A Description of the Security", this.instrumentType);
			instrument.setMarketSector(new MarketSector("", "", new Market("L", "LSE", "London Stock Exchange", this.country, ), new Sector("MI", "Mining", "Mining Stocks", null)));

			instrument = this.instrumentDAO.saveInstrument(instrument);
			Assert.assertEquals("Instrument did not get an ID: " + instrument.toString(), 1, instrument.getId());
			persistedId = instrument.getId();
		}
		catch (Exception ex) {
			log.error("Exception Caught...", ex);
			throw ex;
		}
	}

	/**
	 * Test method for {@link org.stuarthardy.momentum.persistence.dao.hibernate.instrument.HibernateInstrumentDAO#getInstrument(long)}.
	 */
	public final void testGetInstrumentById() {
		Object obj = this.instrumentDAO.getInstrument(persistedId);
		Assert.assertNotNull("Null object returned for ID: " + persistedId + ". Object returned was: " + obj, obj);
	}

	/**
	 * Test method for {@link org.stuarthardy.momentum.persistence.dao.hibernate.instrument.HibernateInstrumentDAO#getInstrument(java.lang.String)}.
	 */
	public final void testGetInstrumentByTicker() {
		List<Instrument> results = this.instrumentDAO.getInstrument("TEST");
		Assert.assertEquals("Incorrect number of results reteurned by search: " + results, 1, results.size());
		Instrument instrument = results.get(0);
		Assert.assertEquals("Invalid object returned from results: " + instrument, "Test Security", instrument.getName());
	}

	/**
	 * Test method for {@link org.stuarthardy.momentum.persistence.dao.hibernate.instrument.HibernateInstrumentDAO#getInstrument(java.lang.String, java.lang.String)}.
	 */
	public final void testGetInstrumentByTickerAndMarketCode() {
		Instrument instrument = this.instrumentDAO.getInstrument("TEST", null);
		Assert.assertNotNull(instrument);
	}

	/**
     * @param instrumentDAO the instrumentDAO property
     */
    public void setInstrumentDAO(HibernateInstrumentDAO instrumentDAO) {
    	this.instrumentDAO = instrumentDAO;
    }

	/**
     * @param typesDAO the typesDAO to set
     */
    public void setTypesDAO(ITypesDAO<IType> typesDAO) {
    	this.typesDAO = typesDAO;
    }

}
