package prefwork.rating.datasource;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import prefwork.core.Utils;
import prefwork.rating.Rating;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SparseInstance;
import au.com.bytecode.opencsv.CSVReader;

/**
 * Contains data from IMDb in form of maps. The attributes of the film are
 * returned as Relation attributes, e.g. there is one attribute Director with a
 * list of string values.
 * 
 * @author Alan Eckhardt
 * 
 */
public class IMDbString extends IMDbRelation {

    private static Logger log = Logger.getLogger(IMDbString.class);

    protected static int[] lengths = { 4, 5, 2, 3, 1, 2, 5, 3, 1, 1 };

    public String getName() {
        return "IMDBString" + IMDBMaps.size() + (getPlot ? "Plot" : "") + (getLaserDisc ? "Laser" : "");
    }

    public IMDbString() {
        super();
    }

    public SparseInstance getMovie(Integer flixMovieId) {
        int size = IMDBMaps.size();
        if (getLaserDisc) size += 29;
        if (getPlot) size += 1;
        SparseInstance l = new SparseInstance(size);
        l.setDataset(instances);
        Integer imdbId = IMDBMapFromFlix.get(flixMovieId);
        for (int i = 0; i < IMDBMaps.size(); i++) {
            if (IMDBAttributes[i].isRelationValued()) {
                List<Integer> list = getIMBDAttribute(i, imdbId);
                if (list == null) {
                    l.setMissing(i);
                    continue;
                }
                Instances relationHeader = new Instances(instances.attribute(i).relation(), 0);
                for (int k = 0; k < list.size(); k++) {
                    Instance inst = new weka.core.SparseInstance(1);
                    inst.setDataset(relationHeader);
                    Utils.addStringValue(list.get(k).toString(), inst, relationHeader.attribute(0));
                    relationHeader.add(inst);
                }
                l.setValue(i, instances.attribute(i).addRelation(relationHeader));
            }
        }
        if (getLaserDisc) {
            LaserDisc ld = IMDBLaserDiscs.get(imdbId);
            for (int i = 0; i < 29; i++) {
                if (ld == null) l.setMissing(i + IMDBMaps.size()); else {
                    Utils.addStringValue(ld.names[i], l, IMDBAttributes[i + IMDBMaps.size()]);
                }
            }
        }
        if (getPlot) {
            Utils.addStringValue(IMDBPlotMovies.get(imdbId), l, IMDBAttributes[IMDBAttributes.length - 1]);
        }
        return l;
    }

    public Attribute[] getAttributes() {
        if (IMDBAttributes != null) return IMDBAttributes;
        int size = IMDBMaps.size();
        if (getLaserDisc) size += 29;
        if (getPlot) size += 1;
        Attribute[] attrs = new Attribute[size];
        for (int i = 0; i < IMDBMaps.size(); i++) {
            ArrayList<Attribute> list = new ArrayList<Attribute>();
            list.add(new Attribute("list", (java.util.ArrayList<String>) null));
            attrs[i] = new Attribute(files[i], new Instances("list" + (i), list, 10), i);
        }
        if (getLaserDisc) {
            for (int i = 0; i < 29; i++) {
                attrs[IMDBMaps.size() + i] = new Attribute(LaserDisc.names[i], (java.util.ArrayList<String>) null, IMDBMaps.size() + i);
            }
        }
        if (getPlot) {
            attrs[attrs.length - 1] = new Attribute("plotRel.csv", (java.util.ArrayList<String>) null, IMDBMaps.size());
        }
        IMDBAttributes = attrs;
        ArrayList<Attribute> list = new ArrayList<Attribute>();
        for (int i = 0; i < attrs.length; i++) {
            list.add(attrs[i]);
        }
        instances = new Instances("name", list, 10);
        instances.setClassIndex(0);
        return attrs;
    }
}
