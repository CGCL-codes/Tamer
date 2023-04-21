package org.fudaa.fudaa.tr.post;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Writer;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import com.memoire.bu.BuResource;
import com.memoire.fu.FuLog;
import org.fudaa.ctulu.CtuluAnalyze;
import org.fudaa.ctulu.CtuluLib;
import org.fudaa.ctulu.CtuluLibFile;
import org.fudaa.ctulu.CtuluLibString;
import org.fudaa.ctulu.CtuluUI;
import org.fudaa.ctulu.ProgressionInterface;
import org.fudaa.ctulu.ProgressionUpdater;
import org.fudaa.ctulu.gui.CtuluTaskOperationGUI;
import org.fudaa.dodico.h2d.type.H2dVariableType;
import org.fudaa.dodico.h2d.type.H2dVariableTypeCreated;
import org.fudaa.fudaa.commun.impl.FudaaCommonImplementation;

/**
 * @author fred deniger
 * @version $Id: TrPostDataCreationActions.java,v 1.4 2007-01-19 13:14:11 deniger Exp $
 */
public final class TrPostDataCreationActions {

    private TrPostDataCreationActions() {
    }

    public static void showExportError(final CtuluUI _ui, final String _msg) {
        _ui.error(getExpStr(), _msg, false);
    }

    public static String getExpStr() {
        return BuResource.BU.getString("Exporter");
    }

    public static String getImpStr() {
        return BuResource.BU.getString("Importer");
    }

    public static void export(final File _dest, final H2dVariableTypeCreated[] _vars, final String[] _expr, final FudaaCommonImplementation _impl) {
        if (_vars == null || _dest == null || _expr == null || _vars.length == 0 || _expr.length != _vars.length) {
            return;
        }
        final String err = CtuluLibFile.canWrite(_dest);
        if (err != null) {
            showExportError(_impl, err);
            return;
        }
        new CtuluTaskOperationGUI(_impl, getExpStr() + CtuluLibString.ESPACE + _dest.getName()) {

            public void act() {
                exportAct(_dest, _vars, _expr, _impl.createProgressionInterface(this));
            }
        }.start();
    }

    public static void exportAct(final File _dest, final H2dVariableTypeCreated[] _vars, final String[] _expr, final ProgressionInterface _prog) {
        Writer out = null;
        try {
            final ProgressionUpdater up = new ProgressionUpdater(_prog);
            out = new BufferedWriter(new FileWriter(_dest, false));
            final String com = "#";
            out.write(com);
            out.write(CtuluLib.getS("G�n�r� par Fudaa le:"));
            out.write(CtuluLibString.ESPACE);
            out.write(DateFormat.getDateInstance().format(new Date()));
            out.write(CtuluLibString.LINE_SEP);
            out.write(com);
            out.write(CtuluLib.getS("Descriptions des variables"));
            out.write(CtuluLibString.LINE_SEP);
            out.write(com);
            out.write(CtuluLib.getS("Ligne 1: Nom de la variable"));
            out.write(CtuluLibString.LINE_SEP);
            out.write(com);
            out.write(CtuluLib.getS("Ligne 2: Nom court de la variable, utilis� pour les expressions"));
            out.write(CtuluLibString.LINE_SEP);
            out.write(com);
            out.write(CtuluLib.getS("Ligne 3: Expression utilis�e"));
            out.write(CtuluLibString.LINE_SEP);
            final int nb = _vars.length;
            up.setValue(nb, 4);
            for (int i = 0; i < nb; i++) {
                if (i > 0) {
                    out.write(CtuluLibString.LINE_SEP);
                }
                final H2dVariableType ti = _vars[i];
                out.write(ti.getName());
                out.write(CtuluLibString.LINE_SEP);
                out.write(ti.getShortName());
                out.write(CtuluLibString.LINE_SEP);
                out.write(_expr[i]);
                up.majAvancement();
            }
        } catch (final IOException e) {
            FuLog.error("file writing error " + _dest.getAbsolutePath(), e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (final IOException e) {
                    FuLog.error("can't close file " + _dest.getAbsolutePath(), e);
                }
            }
        }
    }

    public static void importAct(final File _dest, final Map _vars, final CtuluAnalyze _analyze, final ProgressionInterface _prog) {
        LineNumberReader in = null;
        try {
            in = new LineNumberReader(new FileReader(_dest));
            final ProgressionUpdater up = new ProgressionUpdater(_prog);
            up.setValue(6, 30);
            String line = null;
            final String[] nameShExpr = new String[3];
            int idx = 0;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (line.charAt(0) != '#') {
                    nameShExpr[idx++] = line;
                    if (idx > 2) {
                        final H2dVariableTypeCreated cr = new H2dVariableTypeCreated(nameShExpr[0], nameShExpr[1], null);
                        _vars.put(cr, nameShExpr[2]);
                        idx = 0;
                        Arrays.fill(nameShExpr, null);
                    }
                }
            }
            if (idx > 0) {
                final StringBuffer ignoreLine = new StringBuffer();
                ignoreLine.append(CtuluLibString.LINE_SEP);
                for (int i = 0; i < nameShExpr.length; i++) {
                    if (nameShExpr[i] == null) {
                        break;
                    }
                    ignoreLine.append(CtuluLibString.LINE_SEP).append(nameShExpr[i]);
                }
                _analyze.addWarn(CtuluLib.getS("Des lignes ont �t� ignor�es") + ignoreLine, idx);
            }
        } catch (final IOException e) {
            FuLog.error("file writing error " + _dest.getAbsolutePath(), e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (final IOException e) {
                    FuLog.error("can't close file " + _dest.getAbsolutePath(), e);
                }
            }
        }
    }
}
