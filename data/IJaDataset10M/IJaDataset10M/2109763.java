package cn.geodata.dataview.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import javax.xml.transform.TransformerException;
import org.apache.log4j.Logger;
import org.geotools.feature.FeatureCollection;
import org.geotools.gml.producer.FeatureTransformer;
import org.xml.sax.SAXException;
import cn.geodata.dataview.util.Util;

public class PositionGML {

    private static Logger Log = Logger.getLogger(PositionGML.class);

    private ArrayList<int[]> pathRow;

    private FeatureCollection fs;

    public InputStream getGml() throws IOException, SAXException, TransformerException, URISyntaxException {
        ByteArrayOutputStream _stream = new ByteArrayOutputStream();
        FeatureTransformer _transformer = new FeatureTransformer();
        _transformer.setIndentation(4);
        _transformer.getFeatureNamespaces().declarePrefix("view", "http://www.geodata.cn/dataview");
        _transformer.transform(this.fs, _stream);
        return new ByteArrayInputStream(_stream.toByteArray());
    }

    public void setPathRow(String[] prs) throws Exception {
        ArrayList<int[]> _pathRow = new ArrayList<int[]>();
        for (String pr : prs) {
            String[] _parts = pr.trim().split(",");
            if (_parts.length != 2) {
                throw new Exception("Path and row parameters are incorrect");
            }
            _pathRow.add(new int[] { Integer.parseInt(_parts[0]), Integer.parseInt(_parts[1]) });
        }
        pathRow = _pathRow;
    }

    public String execute() throws Exception {
        if (this.pathRow == null) {
            throw new NullPointerException("Not found avaliable path and row parameter");
        }
        this.fs = Util.locatePathRows(this.pathRow);
        return "success";
    }
}
