package net.sourceforge.jgeocoder.tiger;

import java.io.File;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import net.sourceforge.jgeocoder.AddressComponent;
import net.sourceforge.jgeocoder.GeocodeAcuracy;
import net.sourceforge.jgeocoder.JGeocodeAddress;
import net.sourceforge.jgeocoder.us.AddressParser;
import net.sourceforge.jgeocoder.us.AddressStandardizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.EntityCursor;

/**
 * TODO javadocs me
 * @author jliang
 *
 */
public class JGeocoder {

    private static final Log LOGGER = LogFactory.getLog(JGeocoder.class);

    private ZipCodesDb _zipDb;

    private ZipCodeDAO _zipDao;

    private TigerLineDao _tigerDao;

    public JGeocoder() {
        this(JGeocoderConfig.DEFAULT);
    }

    private TigerLineHit getTigerLineHit(Map<AddressComponent, String> normalizedAddr) throws DatabaseException {
        if (normalizedAddr.get(AddressComponent.ZIP) != null) {
            try {
                _zipDao.fillInCSByZip(normalizedAddr, normalizedAddr.get(AddressComponent.ZIP));
            } catch (DatabaseException e) {
                LOGGER.warn("Unable to query zip code", e);
            }
            try {
                return _tigerDao.getTigerLineHit(normalizedAddr);
            } catch (TigerQueryFailedException e) {
                LOGGER.warn("Tiger/Line DB query failed, street level geocoding will be skipped: " + e.getMessage());
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("", e);
                }
                return null;
            }
        }
        Location loc = new Location();
        loc.setCity(normalizedAddr.get(AddressComponent.CITY).replaceAll("\\s+", ""));
        loc.setState(normalizedAddr.get(AddressComponent.STATE));
        EntityCursor<ZipCode> zips = null;
        List<TigerLineHit> hits = new ArrayList<TigerLineHit>();
        try {
            zips = _zipDao.getZipCodeByLocation().subIndex(loc).entities();
            for (ZipCode zip : zips) {
                normalizedAddr.put(AddressComponent.ZIP, zip.getZip());
                List<TigerLineHit> zipHits;
                try {
                    zipHits = _tigerDao.getTigerLineHits(normalizedAddr);
                } catch (TigerQueryFailedException e) {
                    LOGGER.warn("Tiger/Line DB query failed, street level geocoding will be skipped: " + e.getMessage());
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("", e);
                    }
                    normalizedAddr.remove(AddressComponent.ZIP);
                    return null;
                }
                hits.addAll(zipHits);
            }
        } finally {
            if (zips != null) {
                zips.close();
            }
        }
        if (hits.size() > 0) {
            return TigerLineDao.findBest(normalizedAddr, hits);
        }
        normalizedAddr.remove(AddressComponent.ZIP);
        return null;
    }

    public JGeocodeAddress geocodeAddress(String addrLine) {
        JGeocodeAddress ret = new JGeocodeAddress();
        Map<AddressComponent, String> m = AddressParser.parseAddress(addrLine);
        ret.setParsedAddr(m);
        if (m == null) return ret;
        m = AddressStandardizer.normalizeParsedAddress(m);
        ret.setNormalizedAddr(m);
        if (m.get(AddressComponent.ZIP) == null && (m.get(AddressComponent.STATE) == null || m.get(AddressComponent.CITY) == null)) {
            return ret;
        }
        GeocodeAcuracy acuracy = GeocodeAcuracy.STREET;
        m = new EnumMap<AddressComponent, String>(m);
        TigerLineHit hit = null;
        try {
            hit = getTigerLineHit(m);
        } catch (DatabaseException e) {
            throw new RuntimeException("Unable to query tiger/line database " + e.getMessage());
        }
        if (hit != null) {
            acuracy = GeocodeAcuracy.STREET;
            Geo geo = Geocoder.geocodeFromHit(Integer.parseInt(hit.streetNum), hit);
            m.put(AddressComponent.ZIP, String.valueOf(geo.zip));
            m.put(AddressComponent.PREDIR, hit.fedirp);
            m.put(AddressComponent.POSTDIR, hit.fedirs);
            m.put(AddressComponent.TYPE, hit.fetype);
            m.put(AddressComponent.TLID, String.valueOf(hit.tlid));
            m.put(AddressComponent.LAT, String.valueOf(geo.lat));
            m.put(AddressComponent.LON, String.valueOf(geo.lon));
            ret.setGeocodedAddr(m);
        } else if (_zipDao.geocodeByZip(m)) {
            acuracy = GeocodeAcuracy.ZIP;
            ret.setGeocodedAddr(m);
        } else if (_zipDao.geocodeByCityState(m)) {
            acuracy = GeocodeAcuracy.CITY_STATE;
            ret.setGeocodedAddr(m);
        } else {
            return ret;
        }
        if (ret.getGeocodedAddr() != null && ret.getGeocodedAddr().get(AddressComponent.COUNTY) == null && ret.getGeocodedAddr().get(AddressComponent.ZIP) != null) {
            try {
                _zipDao.fillInCSByZip(ret.getGeocodedAddr(), ret.getGeocodedAddr().get(AddressComponent.ZIP));
            } catch (DatabaseException e) {
                LOGGER.warn("Unable to query zip code", e);
            }
        }
        ret.setAcuracy(acuracy);
        return ret;
    }

    public JGeocoder(JGeocoderConfig config) {
        _zipDb = new ZipCodesDb();
        _tigerDao = new TigerLineDao(config.getTigerDataSource());
        try {
            _zipDb.init(new File(config.getJgeocoderDataHome()), false, false);
            _zipDao = new ZipCodeDAO(_zipDb.getStore());
        } catch (Exception e) {
            throw new RuntimeException("Unable to create zip db, make sure your system property 'jgeocoder.data.home' is correct" + e.getMessage());
        }
    }

    public void cleanup() {
        if (_zipDb != null) {
            try {
                _zipDb.shutdown();
            } catch (DatabaseException e) {
                throw new RuntimeException("Unable to shutdown zip db, " + e.getMessage());
            }
            _zipDb = null;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        cleanup();
    }
}
