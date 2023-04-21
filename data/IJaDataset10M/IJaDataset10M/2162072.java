package net.sourceforge.ivi.ui.views;

import java.io.InputStream;
import java.util.Vector;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import net.sourceforge.ivi.core.CorePlugin;
import net.sourceforge.ivi.core.IDFAAMgrEventListener;
import net.sourceforge.ivi.core.iviDFAAMgr;
import net.sourceforge.ivi.core.iviException;
import net.sourceforge.ivi.core.ddb.DDBProviderUtils;
import net.sourceforge.ivi.core.ddb.IDDBSignalObj;
import net.sourceforge.ivi.core.dfaa.IDFAA;
import net.sourceforge.ivi.core.dfaa.IDFAAEventListener;
import net.sourceforge.ivi.core.dfio.IDFIO;
import net.sourceforge.ivi.core.dfio.IDFIOTrace;
import net.sourceforge.ivi.ui.DFIOTraceRenderers.IDFIOTraceRenderer;
import net.sourceforge.ivi.ui.trace_renderers.iviDFIOLogicTraceConfigWrapper;
import net.sourceforge.ivi.waveview.ui.IWaveDisplay;
import net.sourceforge.ivi.waveview.ui.IWaveTrace;
import net.sourceforge.ivi.waveview.ui.IWaveTraceRenderer;
import net.sourceforge.ivi.waveview.ui.IWaveviewEditor;
import net.sourceforge.ivi.waveview.ui.IWaveviewOpenCloseListener;
import net.sourceforge.ivi.waveview.ui.WaveviewPlugin;
import net.sourceforge.ivi.waveview.ui.format.FormatFileWrapper;
import net.sourceforge.ivi.waveview.ui.format.FormatFileXMLParser;
import net.sourceforge.ivi.waveview.ui.format.IFormatEntity;
import net.sourceforge.ivi.waveview.ui.format.WaveTraceWrapper;

public class iviDFAAWaveViewMgr implements IWaveviewOpenCloseListener, IDFAAMgrEventListener {

    public static final String DFAAConfigData = "DFAAConfigData";

    public iviDFAAWaveViewMgr(IWorkbench workbench) {
        d_simTraceList = new Vector();
        CorePlugin.getDefault().getDFAAMgr().addDFAAListChangeListener(this);
        WaveviewPlugin.getDefault().addOpenCloseListener(this);
        ScanAllWaveViews(workbench);
    }

    /** 
     * When the workbench GUI starts up, not all wave-view editors are 
     * constructed. However, we need to know upfront the list of traces
     * from simulation that must be opened. This procedure scan  
     * @param workbench
     */
    public void ScanAllWaveViews(IWorkbench workbench) {
        IWorkbenchWindow windows[] = workbench.getWorkbenchWindows();
        for (int i = 0; i < windows.length; i++) {
            IWorkbenchPage pages[] = windows[i].getPages();
            for (int j = 0; j < pages.length; j++) {
                IEditorReference refs[] = pages[j].getEditorReferences();
                for (int x = 0; x < refs.length; x++) {
                    if (refs[x].getId().equals("net.sourceforge.ivi.ui.waveview")) {
                        IEditorPart ed = refs[x].getEditor(true);
                        IFile file = ((IFileEditorInput) ed.getEditorInput()).getFile();
                        InputStream input_stream = null;
                        try {
                            file.refreshLocal(0, null);
                            input_stream = file.getContents();
                        } catch (CoreException e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                        FormatFileXMLParser parser = new FormatFileXMLParser();
                        IFormatEntity entity = parser.parse(input_stream);
                        if (entity == null) {
                            continue;
                        }
                        FormatFileWrapper fw = new FormatFileWrapper(entity);
                        WaveTraceWrapper twl[] = fw.getTraces();
                        for (int y = 0; y < twl.length; y++) {
                            IFormatEntity rc = twl[y].getRendererConfig();
                            if (rc != null) {
                                iviDFIOLogicTraceConfigWrapper cw = new iviDFIOLogicTraceConfigWrapper(rc);
                                String dfaa_name = cw.getDFAAName();
                                if (dfaa_name != null && dfaa_name.equals("Sim")) {
                                    addSimTraceInfo(dfaa_name, cw.getTracePath(), (IWaveviewEditor) ed);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void dispose() {
        CorePlugin.getDefault().getDFAAMgr().removeDFAAListChangeListener(this);
        WaveviewPlugin.getDefault().removeOpenCloseListener(this);
    }

    public void waveviewOpened(IWaveviewEditor ed) {
        scanWaveView();
    }

    public void waveviewClosed(IWaveviewEditor ed) {
        scanWaveView();
    }

    public void dfaaAdded(IDFAA newDFAA) {
        scanWaveView();
    }

    public void dfaaRemoved(IDFAA newDFAA) {
        scanWaveView();
    }

    private void scanWaveView() {
        iviDFAAMgr df_mgr = CorePlugin.getDefault().getDFAAMgr();
        IDFAA dfaa_l[] = df_mgr.getDFAAList();
        IWaveviewEditor we_l[] = WaveviewPlugin.getDefault().getOpenEditors();
        for (int i = 0; i < d_simTraceList.size(); i++) {
            SimTraceInfo ti = (SimTraceInfo) d_simTraceList.elementAt(i);
            IDFAA dfaa = findDFAA(dfaa_l, ti.d_dfaaName);
            if (dfaa != null) {
                IDFIOTrace t = findDFAATrace(dfaa, ti.d_tracePath);
            }
        }
        for (int ed_i = 0; ed_i < we_l.length; ed_i++) {
            IWaveTrace r_l[] = we_l[ed_i].getTraceList();
            for (int r_i = 0; r_i < r_l.length; r_i++) {
                IWaveTraceRenderer r = r_l[r_i].getRenderer();
                if (r instanceof IDFIOTraceRenderer) {
                    IDFIOTrace trace = ((IDFIOTraceRenderer) r).getDFIOTrace();
                    if (trace == null) {
                        IFormatEntity rc = r_l[r_i].getRendererConfigData();
                        iviDFIOLogicTraceConfigWrapper cw = new iviDFIOLogicTraceConfigWrapper(rc);
                        String dfaa_name = cw.getDFAAName();
                        IDFAA dfaa = findDFAA(dfaa_l, dfaa_name);
                        if (dfaa != null) {
                            trace = findDFAATrace(dfaa, cw.getTracePath());
                            ((IDFIOTraceRenderer) r).init(trace);
                        }
                    } else {
                        IFormatEntity rc = r_l[r_i].getRendererConfigData();
                        iviDFIOLogicTraceConfigWrapper cw = new iviDFIOLogicTraceConfigWrapper(rc);
                        String dfaa_name = cw.getDFAAName();
                        IDFAA dfaa = findDFAA(dfaa_l, dfaa_name);
                        if (dfaa == null) {
                            ((IDFIOTraceRenderer) r).init((IDFIOTrace) null);
                        }
                    }
                }
            }
        }
    }

    private IDFAA findDFAA(IDFAA dfaa_l[], String dfaa_name) {
        IDFAA ret = null;
        for (int i = 0; i < dfaa_l.length; i++) {
            if (dfaa_l[i].getName().equals(dfaa_name)) {
                ret = dfaa_l[i];
                break;
            }
        }
        return ret;
    }

    private IDFIOTrace findDFAATrace(IDFAA dfaa, String name) {
        IDDBSignalObj ddb_s = DDBProviderUtils.findSignal(dfaa.getAvailableStruct(), name);
        if (!dfaa.isTraced(ddb_s)) {
            try {
                dfaa.traceSignal(ddb_s);
            } catch (iviException e) {
                System.out.println("ERROR: cannot trace signal \"" + name + "\": " + e.getMessage());
            }
        }
        IDFIOTrace ret = null;
        IDFIOTrace traces[] = dfaa.getDFIO().getTraces();
        for (int i = 0; i < traces.length; i++) {
            if (traces[i].getName().equals(name)) {
                ret = traces[i];
                break;
            }
        }
        return ret;
    }

    private void addSimTraceInfo(String dfaa_name, String trace_path, IWaveviewEditor editor) {
        SimTraceInfo ti = null;
        for (int i = 0; i < d_simTraceList.size(); i++) {
            SimTraceInfo tti = (SimTraceInfo) d_simTraceList.elementAt(i);
            if (tti.d_dfaaName.equals(dfaa_name) && tti.d_tracePath.equals(trace_path)) {
                ti = tti;
                break;
            }
        }
        if (ti == null) {
            ti = new SimTraceInfo();
            ti.d_dfaaName = dfaa_name;
            ti.d_tracePath = trace_path;
            d_simTraceList.add(ti);
        }
        if (!ti.d_refWaveList.contains(editor)) {
            ti.d_refWaveList.add(editor);
        }
    }

    private void removeSimTraceInfo(String dfaa_name, String trace_path, IWaveviewEditor editor) {
        SimTraceInfo ti = null;
        for (int i = 0; i < d_simTraceList.size(); i++) {
            SimTraceInfo tti = (SimTraceInfo) d_simTraceList.elementAt(i);
            if (tti.d_dfaaName.equals(dfaa_name) && tti.d_tracePath.equals(trace_path)) {
                ti = tti;
                break;
            }
        }
        if (ti != null) {
            ti.d_refWaveList.remove(editor);
            if (ti.d_refWaveList.size() == 0) {
                d_simTraceList.remove(ti);
            }
        }
    }

    private class SimTraceInfo {

        SimTraceInfo() {
            d_refWaveList = new Vector();
        }

        String d_dfaaName;

        String d_tracePath;

        Vector d_refWaveList;
    }

    private Vector d_simTraceList;
}
