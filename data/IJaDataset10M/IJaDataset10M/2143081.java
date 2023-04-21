package org.fudaa.fudaa.meshviewer.profile;

import com.vividsolutions.jts.geom.LineString;
import org.fudaa.ctulu.CtuluTaskDelegate;
import org.fudaa.ctulu.CtuluUI;
import org.fudaa.ctulu.CtuluVariable;
import org.fudaa.ctulu.ProgressionInterface;
import org.fudaa.dodico.commun.DodicoLib;
import org.fudaa.dodico.ef.operation.EfLineIntersectionsCorrectionTester;
import org.fudaa.dodico.ef.operation.EfLineIntersectionsResultsI;
import org.fudaa.ebli.calque.ZEbliCalquesPanel;
import org.fudaa.fudaa.commun.FudaaLib;
import org.fudaa.fudaa.meshviewer.MvResource;
import org.fudaa.fudaa.meshviewer.export.MvExportChooseVarAndTime;

public class MvProfileBuilderFromLine extends MvProfileBuilder {

    public MvProfileBuilderFromLine(final MvProfileTarget _data, final CtuluUI _ui, final LineString _line, final ZEbliCalquesPanel _panel, EfLineIntersectionsCorrectionTester _tester) {
        super(_data, _ui, _panel, _tester);
        selectedLine_ = _line;
    }

    protected void actFirstTest(final ProgressionInterface _prog) {
        final EfLineIntersectionsResultsI res = getDefaultRes((CtuluVariable) initVar_.getElementAt(0), _prog);
        if (res.isEmpty()) {
            showNoIntersectionFound();
        } else {
            final MvExportChooseVarAndTime export = createVarTimeChooser(getHelpForVarTime());
            if (export.afficheModaleOk(ui_.getParentComponent(), MvResource.getS("Profils spatiaux"), export.getListRunnable())) {
                final CtuluTaskDelegate task = ui_.createTask(FudaaLib.getS("Construction des courbes"));
                task.start(new Runnable() {

                    public void run() {
                        MvProfileBuilderFromLine.this.actBuildGroup(task.getStateReceiver(), export);
                    }
                });
            }
        }
        close();
    }

    protected void stepOne() {
        final CtuluTaskDelegate task = ui_.createTask(DodicoLib.getS("Recherche intersections"));
        task.start(new Runnable() {

            public void run() {
                actFirstTest(task.getStateReceiver());
            }
        });
    }
}
