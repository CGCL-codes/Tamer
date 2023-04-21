package org.fudaa.fudaa.tr;

import java.io.File;
import java.io.IOException;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import org.geotools.data.DataStore;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.FeatureType;
import org.fudaa.ctulu.CtuluAnalyze;
import org.fudaa.ctulu.CtuluLibArray;
import org.fudaa.ctulu.CtuluLibFile;
import org.fudaa.ctulu.CtuluLibString;
import org.fudaa.ctulu.CtuluVariable;
import org.fudaa.ctulu.gis.mif.MIFDataStoreFactory;
import org.fudaa.ctulu.interpolation.InterpolationVectorContainer;
import org.fudaa.dodico.ef.EfData;
import org.fudaa.dodico.ef.EfDataNode;
import org.fudaa.dodico.ef.EfElement;
import org.fudaa.dodico.ef.EfGridData;
import org.fudaa.dodico.ef.EfGridInterface;
import org.fudaa.dodico.ef.EfNode;
import org.fudaa.dodico.ef.impl.EfGridArray;
import org.fudaa.dodico.h2d.type.H2dVariableType;
import org.fudaa.fudaa.meshviewer.export.MvExportActDataStoreSrc;

/**
 * @author Fred Deniger
 * @version $Id: TestExportMapInfo.java,v 1.13 2007-06-11 13:08:24 deniger Exp $
 */
public class TestExportMapInfo implements EfGridData {

    public static EfGridInterface build() {
        final EfNode[] nodes = new EfNode[13];
        int idx = 0;
        nodes[idx++] = new EfNode(0, 0, idx);
        nodes[idx++] = new EfNode(1, 0, idx);
        nodes[idx++] = new EfNode(0, 2, idx);
        nodes[idx++] = new EfNode(1, 2, idx);
        nodes[idx++] = new EfNode(3, 2, idx);
        nodes[idx++] = new EfNode(3, 4, idx);
        nodes[idx++] = new EfNode(0, 4, idx);
        nodes[idx++] = new EfNode(1, 4, idx);
        nodes[idx++] = new EfNode(1, 6, idx);
        nodes[idx++] = new EfNode(0, 6, idx);
        nodes[idx++] = new EfNode(3, 6, idx);
        nodes[idx++] = new EfNode(3, 8, idx);
        nodes[idx++] = new EfNode(0, 8, idx);
        idx = 0;
        final EfElement[] elt = new EfElement[9];
        elt[idx++] = new EfElement(new int[] { 0, 1, 2 });
        elt[idx++] = new EfElement(new int[] { 1, 3, 2 });
        elt[idx++] = new EfElement(new int[] { 2, 4, 5 });
        elt[idx++] = new EfElement(new int[] { 2, 5, 7 });
        elt[idx++] = new EfElement(new int[] { 2, 7, 6 });
        elt[idx++] = new EfElement(new int[] { 6, 7, 8 });
        elt[idx++] = new EfElement(new int[] { 6, 8, 9 });
        elt[idx++] = new EfElement(new int[] { 9, 10, 11 });
        elt[idx++] = new EfElement(new int[] { 9, 11, 12 });
        return new EfGridArray(nodes, elt);
    }

    final EfGridInterface grid_;

    final double[] hauteur_;

    final double[] bathy_;

    DefaultListModel varList_;

    final H2dVariableType[] vars_ = new H2dVariableType[] { H2dVariableType.HAUTEUR_EAU, H2dVariableType.BATHYMETRIE };

    public TestExportMapInfo() {
        super();
        grid_ = build();
        hauteur_ = new double[grid_.getPtsNb()];
        bathy_ = new double[grid_.getPtsNb()];
        for (int i = hauteur_.length - 1; i >= 0; i--) {
            hauteur_[i] = 100 + i;
            bathy_[i] = i;
        }
        varList_ = new DefaultListModel();
        varList_.addElement(vars_[0]);
        varList_.addElement(vars_[1]);
    }

    public boolean isDefined(final CtuluVariable _var) {
        return CtuluLibArray.findObject(vars_, _var) >= 0;
    }

    public double getData(final CtuluVariable _var, final int _timeIdx, final int _idxObjet) throws IOException {
        if (_var == H2dVariableType.HAUTEUR_EAU) {
            return hauteur_[_idxObjet];
        }
        return bathy_[_idxObjet];
    }

    public EfData getData(final CtuluVariable _var, final int _timeIdx) throws IOException {
        if (_var == H2dVariableType.HAUTEUR_EAU) {
            return new EfDataNode(hauteur_);
        }
        return new EfDataNode(bathy_);
    }

    public EfGridInterface getGrid() {
        return grid_;
    }

    public ListModel getTimeListModel() {
        return null;
    }

    public ListModel getVarListModel() {
        return varList_;
    }

    public double getTimeStep(final int _idx) {
        return 0;
    }

    public boolean isElementVar(final CtuluVariable _idxVar) {
        return false;
    }

    public static void main(final String[] _args) {
        if (_args == null || _args[0] == null) {
            System.err.println("preciser un fichier");
            return;
        }
        final File f = new File(_args[0]).getAbsoluteFile();
        System.out.println("Ecriture sur le fichier " + f.getAbsolutePath());
        final String err = CtuluLibFile.canWrite(f);
        if (err != null) {
            System.err.println("Erreur " + err);
            return;
        }
        final MvExportActDataStoreSrc src = new MvExportActDataStoreSrc(new MIFDataStoreFactory());
        src.setSrc(new TestExportMapInfo(), new InterpolationVectorContainer());
        final CtuluAnalyze anal = new CtuluAnalyze();
        final String[] messages = new String[6];
        src.actExport(null, new File[] { f, new File(CtuluLibFile.getSansExtension(f.getAbsolutePath()) + "_ele.mif"), null }, anal, messages);
        System.out.println("Resultats: ");
        if (!anal.isEmpty()) {
            anal.printResume("Analyze: ");
        }
        System.out.println(CtuluLibString.arrayToString(messages));
        System.out.println("\n\nReading");
        try {
            final DataStore dataStore = new MIFDataStoreFactory().createDataStore(f.toURL());
            final String[] name = dataStore.getTypeNames();
            if (name == null || name.length == 0) {
                throw new IOException("name not found");
            }
            final FeatureSource features = dataStore.getFeatureSource(name[0]);
            if (features == null) {
                System.out.println("features are null");
                return;
            }
            final FeatureType type = features.getSchema();
            System.out.println("type= " + type);
            final FeatureCollection fsShape = features.getFeatures();
            System.out.println("nb geometries " + fsShape.size());
            final FeatureIterator it = fsShape.features();
            int idx = 0;
            while (it.hasNext()) {
                System.out.println((++idx) + "= " + it.next());
            }
            it.close();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
