package org.fudaa.fudaa.tr.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import com.memoire.bu.BuInformationsSoftware;
import com.memoire.bu.BuMenu;
import org.fudaa.ctulu.fileformat.FileFormat;
import org.fudaa.dodico.ef.io.corelebth.CorEleBthFileFormat;
import org.fudaa.dodico.ef.io.dunes.DunesMAIFileFormat;
import org.fudaa.dodico.ef.io.serafin.SerafinFileFormat;
import org.fudaa.dodico.ef.io.serafin.SerafinVolumeFileFormat;
import org.fudaa.dodico.ef.io.trigrid.TrigridFileFormat;
import org.fudaa.dodico.reflux.io.INPFileFormat;
import org.fudaa.dodico.reflux.io.RefluxRefondeSolutionFileFormat;
import org.fudaa.dodico.rubar.io.RubarDATFileFormat;
import org.fudaa.dodico.rubar.io.RubarFileFormatDefault;
import org.fudaa.dodico.rubar.io.RubarMAIFileFormat;
import org.fudaa.dodico.rubar.io.RubarPARFileFormat;
import org.fudaa.dodico.telemac.TelemacDicoFileFormat;
import org.fudaa.fudaa.tr.reflux.TrRefluxImplHelper;
import org.fudaa.fudaa.tr.rubar.TrRubarImplHelper;
import org.fudaa.fudaa.tr.telemac.TrTelemacImplHelper;

/**
 * @author deniger
 * @version $Id: TrFileFormatManager.java,v 1.30 2007-06-20 12:23:38 deniger Exp $
 */
public final class TrFileFormatManager {

    public static final String EXPORT_PREF = "__EXPORT";

    public static final TrFileFormatManager INSTANCE = new TrFileFormatManager();

    public static FileFormat[] getArray(final List _l) {
        final FileFormat[] r = new FileFormat[_l.size()];
        _l.toArray(r);
        return r;
    }

    public static FileFormat getFileFormatImportForId(final String _id) {
        final FileFormat[] ftsImport = TrCourbeImporter.getFileFormatForImportEvol(null);
        for (int i = ftsImport.length - 1; i >= 0; i--) {
            if (ftsImport[i].getID().equals(_id)) {
                return ftsImport[i];
            }
        }
        return null;
    }

    public static FileFormat[] getFormatToFilter(final String _sofwareId) {
        final List r = new ArrayList();
        if (_sofwareId == TrRubarImplHelper.getID()) {
            r.add(RubarPARFileFormat.getInstance());
            r.add(RubarMAIFileFormat.getInstance());
        } else if (_sofwareId == TrTelemacImplHelper.getID()) {
            r.add(TelemacDicoFileFormat.getEmptyFormat());
            r.add(TrigridFileFormat.getInstance());
        } else if (_sofwareId == TrRefluxImplHelper.getID()) {
            r.add(INPFileFormat.getInstance());
            r.add(CorEleBthFileFormat.getInstance());
            r.add(DunesMAIFileFormat.getInstance());
        }
        r.add(SerafinFileFormat.getInstance());
        return getArray(r);
    }

    public static FileFormat[] getFormatWhithSoftware(final BuInformationsSoftware _is, final FileFormat[] _f) {
        final int n = _f.length;
        final ArrayList r = new ArrayList(n);
        for (int i = n - 1; i >= 0; i--) {
            if (_f[i].getSoftware() == _is) {
                r.add(_f[i]);
            }
        }
        return getArray(r);
    }

    /**
   * Renvoie les format du tableau dont le type est egal au parametre <code>_type</code>.
   */
    public static FileFormat[] getFormatWhithType(final Object _type, final FileFormat[] _f) {
        if (_f == null) {
            return null;
        }
        final int n = _f.length;
        final ArrayList r = new ArrayList(n);
        for (int i = n - 1; i >= 0; i--) {
            if (_f[i].getType() == _type) {
                r.add(_f[i]);
            }
        }
        return getArray(r);
    }

    public static FileFormat[] getGridFormat() {
        final List r = new ArrayList();
        r.add(TrigridFileFormat.getInstance());
        r.add(CorEleBthFileFormat.getInstance());
        r.add(SerafinFileFormat.getInstance());
        return getArray(r);
    }

    public static FileFormat getDefaultGridFormat(final String _sofwareId) {
        if (_sofwareId == TrRubarImplHelper.getID()) {
            return RubarMAIFileFormat.getInstance();
        } else if (_sofwareId == TrTelemacImplHelper.getID()) {
            return SerafinFileFormat.getInstance();
        } else if (_sofwareId == TrRefluxImplHelper.getID()) {
            return CorEleBthFileFormat.getInstance();
        }
        return SerafinFileFormat.getInstance();
    }

    public static FileFormat[] getAllGridFormat() {
        final List r = new ArrayList();
        r.add(TrigridFileFormat.getInstance());
        r.add(CorEleBthFileFormat.getInstance());
        r.add(RubarMAIFileFormat.getInstance());
        r.add(RubarDATFileFormat.getInstance());
        r.add(SerafinFileFormat.getInstance());
        r.add(SerafinVolumeFileFormat.getInstance());
        r.add(INPFileFormat.getInstance());
        r.add(DunesMAIFileFormat.getInstance());
        return getArray(r);
    }

    public static FileFormat[] getGridFormat(final String _sofwareId) {
        final List r = new ArrayList();
        if (_sofwareId == TrRubarImplHelper.getID()) {
            r.add(RubarMAIFileFormat.getInstance());
            r.add(RubarDATFileFormat.getInstance());
            r.add(SerafinFileFormat.getInstance());
        } else if (_sofwareId == TrTelemacImplHelper.getID()) {
            r.add(SerafinFileFormat.getInstance());
            r.add(TrigridFileFormat.getInstance());
            r.add(CorEleBthFileFormat.getInstance());
        } else if (_sofwareId == TrRefluxImplHelper.getID()) {
            r.add(CorEleBthFileFormat.getInstance());
            r.add(INPFileFormat.getInstance());
            r.add(DunesMAIFileFormat.getInstance());
            r.add(SerafinFileFormat.getInstance());
        }
        return getArray(r);
    }

    public static FileFormat[] getPostFormat(final String _sofwareId) {
        final List r = new ArrayList();
        if (_sofwareId == TrRubarImplHelper.getID()) {
            r.add(new RubarFileFormatDefault("TPS"));
            r.add(new RubarFileFormatDefault("TPC"));
            r.add(new RubarFileFormatDefault("TRC"));
            r.add(new RubarFileFormatDefault("ZFN"));
            r.add(new RubarFileFormatDefault("ENV"));
            r.add(SerafinFileFormat.getInstance());
            r.add(SerafinVolumeFileFormat.getInstance());
        } else if (_sofwareId == TrTelemacImplHelper.getID()) {
            r.add(SerafinFileFormat.getInstance());
            r.add(SerafinVolumeFileFormat.getInstance());
        } else if (_sofwareId == TrRefluxImplHelper.getID()) {
            r.add(RefluxRefondeSolutionFileFormat.getInstance());
            r.add(SerafinFileFormat.getInstance());
            r.add(SerafinVolumeFileFormat.getInstance());
        }
        return getArray(r);
    }

    public static FileFormat[] getProjectFormat() {
        final List r = new ArrayList();
        r.add(RubarPARFileFormat.getInstance());
        r.add(INPFileFormat.getInstance());
        r.add(TelemacDicoFileFormat.getEmptyFormat());
        return getArray(r);
    }

    private TrFileFormatManager() {
    }

    /**
   * Construit le menu export a partir des type de donnees.
   */
    public void buildExportMenu(final BuMenu _m, final TrImplementationEditorAbstract _impl) {
        final FileFormat[] fts = getGridFormat();
        final String name = TrResource.getS("Maillage");
        final BuMenu me = new BuMenu(name, name);
        final TreeMap map = new TreeMap();
        final int nb = fts.length;
        for (int j = 0; j < nb; j++) {
            map.put(fts[j].getName(), fts[j].getID());
        }
        for (final Iterator it = map.entrySet().iterator(); it.hasNext(); ) {
            final Map.Entry e = (Map.Entry) it.next();
            final String id = (String) e.getKey();
            me.addMenuItem(id, new StringBuffer(50).append(TrFileFormatManager.EXPORT_PREF).append((String) e.getValue()).toString(), _impl);
        }
        _m.addSubMenu(me, true);
    }

    public boolean isCasFile(final String _file) {
        return (_file.startsWith("cas")) || _file.endsWith(".cas");
    }

    public boolean isRefluxFile(final String _file) {
        return _file.endsWith(".inp");
    }

    public boolean isRubarFile(final String _file) {
        return _file.toLowerCase().endsWith(".par");
    }

    public boolean isRubarTPSFile(final String _file) {
        return _file.toLowerCase().endsWith(".tps");
    }

    public boolean isTxtFile(final String _file) {
        return (_file.endsWith(".txt")) || _file.endsWith(".bat") || _file.endsWith(".sh");
    }
}
