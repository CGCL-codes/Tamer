package org.pagger.data.picture.geo.nmea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import org.pagger.data.picture.geo.io.WayPointReader;

/**
 * Reads the way points from the NMEA log file created by the Sony GPS-CS1
 * tracker.
 * 
 * @author Franz Wilhelmstötter
 */
public class Cs1WayPointReader implements WayPointReader<Cs1WayPoint> {

    private static final String GGA_PREFIX = "$GPGGA";

    private static final String GSA_PREFIX = "$GPGSA";

    private static final String GSV_PREFIX = "$GPGSV";

    private static final String RMC_PREFIX = "$GPRMC";

    private static final String VTG_PREFIX = "$GPVTG";

    private final BufferedReader _reader;

    private String _line = null;

    private int _lineNumber = 0;

    private Cs1WayPoint _point = null;

    private Gga _gga = null;

    private Gsa _gsa = null;

    private Gsv _gsv = null;

    private Rmc _rmc = null;

    private Vtg _vtg = null;

    private final List<String> _gsvLines = new ArrayList<String>(3);

    public Cs1WayPointReader(final Reader reader) {
        _reader = new BufferedReader(reader);
    }

    @Override
    public synchronized Cs1WayPoint read() throws IOException {
        _point = null;
        _gga = null;
        _gsa = null;
        _gsv = null;
        _rmc = null;
        _vtg = null;
        _gsvLines.clear();
        try {
            if (_line != null && !_line.isEmpty()) {
                process(_line);
            }
            while ((_line = _reader.readLine()) != null && _point == null) {
                ++_lineNumber;
                if (!_line.isEmpty()) {
                    process(_line);
                }
            }
        } catch (NmeaFormatException e) {
            throw new IOException(String.format("Invalid NMEA record in line %s: '%s'", _lineNumber, _line), e);
        }
        return _point;
    }

    private void process(final String line) {
        if (line.startsWith(GGA_PREFIX)) {
            _gga = Gga.valueOf(line);
        } else if (line.startsWith(GSA_PREFIX)) {
            _gsa = Gsa.valueOf(line);
        } else if (line.startsWith(RMC_PREFIX)) {
            _rmc = Rmc.valueOf(line);
        } else if (line.startsWith(VTG_PREFIX)) {
            _vtg = Vtg.valueOf(line);
        }
        if (line.startsWith(GSV_PREFIX)) {
            _gsvLines.add(line);
        } else if (_gsv == null && !_gsvLines.isEmpty()) {
            _gsv = Gsv.valueOf(_gsvLines.toArray(new String[0]));
            _gsvLines.clear();
        }
        if (_gga != null && _gsa != null && _gsv != null && _rmc != null && _vtg != null) {
            _point = new Cs1WayPoint(_gga, _gsa, _gsv, _rmc, _vtg);
        }
    }

    @Override
    public void close() throws IOException {
        if (_reader != null) {
            _reader.close();
        }
    }
}
