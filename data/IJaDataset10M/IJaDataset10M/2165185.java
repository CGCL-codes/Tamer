package org.fudaa.fudaa.sig;

import java.awt.Color;
import java.awt.Frame;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellEditor;
import org.geotools.data.FileDataStoreFactorySpi;
import com.memoire.bu.BuComboBox;
import com.memoire.bu.BuFileFilter;
import com.memoire.fu.FuLog;
import org.fudaa.ctulu.CtuluAnalyze;
import org.fudaa.ctulu.ProgressionInterface;
import org.fudaa.ctulu.gis.GISExportDataStoreFactory;
import org.fudaa.ctulu.gui.CtuluCellTextRenderer;
import org.fudaa.ctulu.gui.CtuluFileChooser;
import org.fudaa.dodico.dunes.io.DunesFileFormat;
import org.fudaa.dodico.ef.FileFormatGridVersion;
import org.fudaa.dodico.reflux.io.CorEleBthFileFormat;
import org.fudaa.dodico.reflux.io.INPFileFormat;
import org.fudaa.dodico.rubar.io.RubarDATFileFormat;
import org.fudaa.dodico.rubar.io.RubarMAIFileFormat;
import org.fudaa.dodico.telemac.io.SerafinFileFormat;
import org.fudaa.dodico.telemac.io.SinusxFileFormat;
import org.fudaa.dodico.trigrid.TrigridFileFormat;

/**
 * @author Fred Deniger
 * @version $Id: FSigWizardFileMng.java,v 1.12 2006-09-19 15:10:21 deniger Exp $
 */
public class FSigWizardFileMng {

    /**
   * @author fred deniger
   * @version $Id: FSigWizardFileMng.java,v 1.12 2006-09-19 15:10:21 deniger Exp $
   */
    static final class FmtRowRenderer extends CtuluCellTextRenderer {

        protected void setValue(final Object _value) {
            setForeground(_value == null ? Color.RED : Color.BLACK);
            if (_value == null) {
                setText(FSigLib.getS("Non d�fini"));
            } else {
                setText(((BuFileFilter) _value).getDescription());
            }
        }
    }

    FSigWizardFileModel model_;

    FSigFileLoadResult res_;

    public FSigWizardFileMng() {
        model_ = buildFmt();
        model_.addTableModelListener(new TableModelListener() {

            public void tableChanged(final TableModelEvent _e) {
                res_ = null;
            }
        });
    }

    private void addGridList(final List _filter, final Map _dest) {
        final List r = getGridVerions();
        for (int i = r.size() - 1; i >= 0; i--) {
            final FileFormatGridVersion vers = (FileFormatGridVersion) r.get(i);
            final BuFileFilter ft = vers.getFileFormat().createFileFilter();
            if (ft != null) {
                _filter.add(ft);
                _dest.put(ft, new FSigFileLoaderGrid(ft, vers));
            }
        }
    }

    private FSigWizardFileModel buildFmt() {
        final List r = new ArrayList();
        final Map fmtLoader = new HashMap();
        final Map gisFmt = GISExportDataStoreFactory.buildFileFilterMap(true);
        for (final Iterator it = gisFmt.entrySet().iterator(); it.hasNext(); ) {
            final Map.Entry e = (Map.Entry) it.next();
            final FileDataStoreFactorySpi spi = (FileDataStoreFactorySpi) e.getValue();
            fmtLoader.put(e.getKey(), new FSigFileLoaderGIS((BuFileFilter) e.getKey(), spi));
        }
        r.addAll(gisFmt.keySet());
        BuFileFilter ft = SinusxFileFormat.getInstance().createFileFilter();
        fmtLoader.put(ft, new FSigFileLoaderSinusX(ft));
        r.add(ft);
        ft = SinusxFileFormat.getInstance().createFileFilter();
        ft = SerafinFileFormat.getInstance().createFileFilter();
        fmtLoader.put(ft, new FSigFileLoaderSerafin());
        r.add(ft);
        ft = INPFileFormat.getInstance().createFileFilter();
        fmtLoader.put(ft, new FSigFileLoaderInp(ft));
        r.add(ft);
        final FSigFileLoaderCsv csv = new FSigFileLoaderCsv();
        final BuFileFilter csvFileFilter = csv.getFileFilter();
        r.add(csvFileFilter);
        fmtLoader.put(csvFileFilter, csv);
        addGridList(r, fmtLoader);
        final BuFileFilter[] filters = new BuFileFilter[r.size()];
        r.toArray(filters);
        final Comparator c = new Comparator() {

            public int compare(Object _o1, Object _o2) {
                return ((BuFileFilter) _o1).getDescription().compareTo(((BuFileFilter) _o2).getDescription());
            }
        };
        Arrays.sort(filters, c);
        return new FSigWizardFileModel(fmtLoader, filters, csvFileFilter);
    }

    private List getGridVerions() {
        final List r = new ArrayList();
        r.add(CorEleBthFileFormat.getInstance());
        r.add(RubarMAIFileFormat.getInstance());
        r.add(TrigridFileFormat.getInstance());
        r.add(DunesFileFormat.getInstance());
        r.add(RubarDATFileFormat.getInstance());
        return r;
    }

    FSigWizardFileModel.FileSelectResult startEditingFile(final File _f, final Frame _frame) {
        final CtuluFileChooser ch = new CtuluFileChooser(true);
        if (_f != null) {
            ch.setSelectedFile(_f);
        }
        for (int i = 0; i < model_.filters_.length; i++) {
            ch.addChoosableFileFilter(model_.filters_[i]);
        }
        ch.setMultiSelectionEnabled(_f == null ? true : false);
        ch.setDialogType(JFileChooser.OPEN_DIALOG);
        ch.setFileFilter(ch.getAcceptAllFileFilter());
        ch.showOpenDialog(_frame);
        final FSigWizardFileModel.FileSelectResult r = new FSigWizardFileModel.FileSelectResult();
        r.f_ = ch.getSelectedFiles();
        if (r.f_ != null) {
            if (ch.getFileFilter() instanceof BuFileFilter) {
                r.ft_ = (BuFileFilter) ch.getFileFilter();
            }
        }
        return r;
    }

    public void addListenerToFile(final TableModelListener _listener) {
        model_.addTableModelListener(_listener);
    }

    public final TableCellEditor getFmtRowEditor() {
        final BuComboBox cb = new BuComboBox();
        cb.setModel(new DefaultComboBoxModel(model_.filters_));
        cb.setRenderer(getFmtRowRenderer());
        return new DefaultCellEditor(cb);
    }

    public final CtuluCellTextRenderer getFmtRowRenderer() {
        return new FmtRowRenderer();
    }

    public final FSigWizardFileModel getModel() {
        return model_;
    }

    public boolean isEmpty() {
        return res_ == null || res_.isEmpty();
    }

    public boolean isLoaded() {
        return res_ != null;
    }

    public FSigFileLoadResult loadAll(final ProgressionInterface _prog, final CtuluAnalyze _analyze) {
        if (res_ == null) {
            res_ = new FSigFileLoadResult();
            for (final Iterator it = model_.fileData_.entrySet().iterator(); it.hasNext(); ) {
                final Map.Entry e = (Map.Entry) it.next();
                final File file = (File) e.getKey();
                final FSigFileLoaderI model = (FSigFileLoaderI) e.getValue();
                if (model == null) {
                    _analyze.addFatalError(FSigLib.getS("Un format n'est pas sp�cifi�"));
                    return null;
                }
                try {
                    model.setInResult(res_, file, _prog, _analyze);
                } catch (final Exception _e) {
                    _analyze.addError(_e.getMessage(), -1);
                    FuLog.warning(_e);
                }
            }
        }
        return res_;
    }
}
