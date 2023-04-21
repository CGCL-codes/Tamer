package com.cosylab.vdct.model.db;

import com.cosylab.vdct.model.db.actions.DBModelActions;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.filechooser.FileFilter;
import com.cosylab.logging.DebugLogger;
import com.cosylab.vdct.application.LogHandler;
import com.cosylab.vdct.application.TaskEvent;
import com.cosylab.vdct.application.TaskListener;
import com.cosylab.vdct.application.VisualDCTEngine;
import com.cosylab.vdct.model.Model;
import com.cosylab.vdct.model.ModelActions;
import com.cosylab.vdct.model.ModelSceneConnectionSupport;
import com.cosylab.vdct.model.Module;
import com.cosylab.vdct.model.db.db.DBRecordData;
import com.cosylab.vdct.model.db.dbd.DBDData;
import com.cosylab.vdct.model.db.dbd.DBDResolver;
import com.cosylab.vdct.model.db.irmis.IRMISParams;
import com.cosylab.vdct.model.db.irmis.IRMISToDBModelFactory;
import com.cosylab.vdct.visual.capfast.CapfastModelSceneConnectionSupport;
import com.cosylab.vdct.visual.capfast.RealCapfastWidgetFactory;
import com.cosylab.vdct.visual.scene.ModelScene;
import com.cosylab.vdct.visual.scene.ModelSceneRenderingHints;
import com.cosylab.vdct.visual.widget.EdgeRenderingHintsDialogAction;
import com.cosylab.vdct.visual.widget.WidgetRenderingHintsDialogAction;
import com.cosylab.vdct.visual.widget.WidgetFactory;
import com.cosylab.vdct.visual.widget.WidgetRenderingHints;
import java.util.logging.LogRecord;

/**
 * 
 * @author Janez Golob
 */
public class DBModule implements Module, TaskListener {

    private static final String DB_SUFFIX = ".db";

    private static final String VDB_SUFFIX = ".vdb";

    public final Logger debug = DebugLogger.getLogger(DBModule.class.getName(), Level.INFO);

    public static final String NAME = "EPICSv3 module";

    private static final String LOG_LEVEL = "loggingLevel";

    private static final String NAMING_CONVENTION = "namingConvention";

    private String namingConvention;

    private DBModelActions modelActions;

    private DBDData dbdData = new DBDData();

    private List<TaskListener> listeners = new ArrayList<TaskListener>();

    private WidgetFactory widgetFactory;

    private WidgetRenderingHintsDialogAction customWidgetAction;

    private EdgeRenderingHintsDialogAction edgeRenderingHintsAction;

    private VisualDCTEngine engine;

    private DBConfigAction dbconfigAction;

    private IRMISAction irmisAction;

    private FileFilter fileFilter;

    public void taskChanged(TaskEvent evt) {
        for (TaskListener listener : listeners) {
            listener.taskChanged(evt);
        }
    }

    public void addTaskListener(TaskListener listener) {
        listeners.add(listener);
    }

    private void fireTaskEvent(TaskEvent evt) {
        for (TaskListener listener : listeners) {
            listener.taskChanged(evt);
        }
    }

    public Model createModel(Object parameters) throws Exception {
        if (parameters instanceof String) {
            parameters = new File((String) parameters);
        }
        if (parameters instanceof File) {
            debug.fine("Loading db model from file.");
            Model model = DBModelFactory.createModel(this, ((File) parameters).getAbsolutePath(), ((File) parameters).getName(), dbdData);
            ((DBModelDescriptor) model.getModelDescriptor()).setNamingConvention(namingConvention);
            return model;
        }
        if (parameters instanceof IRMISParams) {
            debug.fine("Loading db model from IRMIS.");
            Model model = IRMISToDBModelFactory.createModel(this, (DBModel) createModel(), ((IRMISParams) parameters).getUri(), ((IRMISParams) parameters).getIocName());
            return model;
        }
        throw new IllegalArgumentException("parameters unrecognized");
    }

    public ModelActions createModelSceneActions(Model model) {
        if (!(model instanceof DBModel)) {
            throw new RuntimeException("DBModel instance expected");
        }
        if (modelActions == null) {
            modelActions = new DBModelActions();
        }
        return modelActions;
    }

    public Model createModel() {
        debug.fine("Creating new db model.");
        fireTaskEvent(new TaskEvent("Creating new db model.", true));
        if (dbdData == null) {
            dbdData = new DBDData();
        }
        Model model = DBModelFactory.createModel(dbdData);
        ((DBModelDescriptor) model.getModelDescriptor()).setNamingConvention(namingConvention);
        fireTaskEvent(new TaskEvent("Created new db model.", false, 1, 1));
        return model;
    }

    public Model merge(Model model, Model newModel) {
        debug.fine("Merging db models.");
        return DBModelFactory.importModel(model, newModel);
    }

    /**
         * @see com.cosylab.vdct.model.Module#createModelSceneConnectionSupport(org.netbeans.api.visual.graph.GraphPinScene)
         */
    public ModelSceneConnectionSupport createModelSceneConnectionSupport(ModelScene scene) {
        return new CapfastModelSceneConnectionSupport();
    }

    public FileFilter getFileFilter() {
        if (fileFilter == null) {
            fileFilter = new FileFilter() {

                @Override
                public String getDescription() {
                    return "DB Data (.db, .vdb)";
                }

                @Override
                public boolean accept(File f) {
                    return (f.isDirectory() || f.getName().endsWith(DB_SUFFIX) || f.getName().endsWith(VDB_SUFFIX));
                }
            };
        }
        return fileFilter;
    }

    public List<Action> getModuleActions() {
        List<Action> retVal = new ArrayList<Action>();
        retVal.add(getConfigAction());
        retVal.add(getIrmisAction());
        retVal.add(getCustomizeDialogAction());
        retVal.add(getCustomizeEdgesDialogAction());
        return retVal;
    }

    private IRMISAction getIrmisAction() {
        if (irmisAction == null) {
            irmisAction = new IRMISAction(this, engine);
        }
        return irmisAction;
    }

    private DBConfigAction getConfigAction() {
        if (dbconfigAction == null) {
            dbconfigAction = new DBConfigAction(engine, this);
        }
        return dbconfigAction;
    }

    private WidgetRenderingHintsDialogAction getCustomizeDialogAction() {
        if (customWidgetAction == null) {
            customWidgetAction = new WidgetRenderingHintsDialogAction(getWidgetFactory(), engine) {

                @Override
                public String[] getTypes() {
                    Collection<String> recordTypesCollection = new ArrayList<String>();
                    Collection<DBRecordData> records = ((DBModelDescriptor) engine.getModel().getModelDescriptor()).getDBData().getRecords().values();
                    for (DBRecordData recordData : records) {
                        if (!recordTypesCollection.contains(recordData.getRecord_type())) {
                            recordTypesCollection.add(recordData.getRecord_type());
                        }
                    }
                    String[] recordTypes = recordTypesCollection.toArray(new String[recordTypesCollection.size()]);
                    return recordTypes;
                }
            };
        }
        return customWidgetAction;
    }

    private EdgeRenderingHintsDialogAction getCustomizeEdgesDialogAction() {
        if (edgeRenderingHintsAction == null) {
            edgeRenderingHintsAction = new EdgeRenderingHintsDialogAction(getWidgetFactory(), engine) {

                @Override
                public String[] getEdgeTypes() {
                    return new String[] { "DBF_FWDLINK", "DBF_INLINK", "DBF_OUTLINK" };
                }
            };
        }
        return edgeRenderingHintsAction;
    }

    public String getName() {
        return NAME;
    }

    public void addLogHandler(LogHandler log) {
        try {
            debug.addHandler(log);
        } catch (SecurityException e) {
        }
    }

    public void saveModel(Model model) throws Exception {
        String fileName = model.getLocation();
        if (fileName == null || fileName.equals("")) {
            return;
        }
        Writer writer;
        writer = new FileWriter(fileName);
        DBDocumentFactory.createDocument(this, model, writer);
    }

    private void _saveModel(Model model, File selectedFile) throws Exception {
        Writer writer;
        writer = new FileWriter(selectedFile);
        DBDocumentFactory.createDocument(this, model, writer);
    }

    public void saveModel(Model model, Object saveParameters) throws Exception {
        if (saveParameters instanceof File) {
            File f = (File) saveParameters;
            _saveModel(model, f);
            model.setLocation(f.getAbsolutePath());
            model.setName(f.getName());
        } else {
            throw new IllegalArgumentException("saveParameters not recognized" + saveParameters);
        }
    }

    public WidgetFactory getWidgetFactory() {
        if (widgetFactory == null) {
            widgetFactory = new RealCapfastWidgetFactory();
            ModelSceneRenderingHints hints = engine.getSceneRenderingHints();
            if (hints != null) {
                WidgetRenderingHints wrh = new WidgetRenderingHints();
                wrh.foreground = hints.defaultWidgetColor;
                wrh.setBorderEditable(false);
                wrh.setBackgroundEditable(false);
                widgetFactory.setDefaultRenderingHints(wrh);
            }
        }
        return widgetFactory;
    }

    public Properties saveProperties() {
        Properties prop = new Properties();
        prop.putAll(getIrmisAction().saveProperties());
        prop.putAll(getConfigAction().saveProperties());
        prop.putAll(getCustomizeDialogAction().saveProperties());
        prop.putAll(getCustomizeEdgesDialogAction().saveProperties());
        prop.putAll(((RealCapfastWidgetFactory) getWidgetFactory()).saveProperties());
        if (namingConvention != null) {
            prop.put(NAMING_CONVENTION, namingConvention);
        }
        prop.put(LOG_LEVEL, debug.getLevel().getName());
        return prop;
    }

    public void loadProperties(Properties prop) {
        getIrmisAction().loadProperties(prop);
        getConfigAction().loadProperties(prop);
        getCustomizeDialogAction().loadProperties(prop);
        getCustomizeEdgesDialogAction().loadProperties(prop);
        ((RealCapfastWidgetFactory) getWidgetFactory()).loadProperties(prop);
        String logLevel = prop.getProperty(LOG_LEVEL);
        if (logLevel != null) {
            debug.setLevel(Level.parse(logLevel));
        }
        namingConvention = prop.getProperty(NAMING_CONVENTION);
    }

    public String getPropertiesName() {
        return this.getClass().getName();
    }

    public void setEngine(VisualDCTEngine engine) {
        this.engine = engine;
    }

    public void addDBDPath(String path) throws RuntimeException {
        dbdData = DBDResolver.resolveDBD(dbdData, path);
        if (dbdData == null) {
            throw new RuntimeException(path);
        }
        engine.reloadModel();
    }

    public void removeDBDPath(String valueAt) {
        String[] paths = getConfigAction().getFileNames();
        setDBDPaths(paths);
    }

    public void setDBDPaths(String[] paths) throws RuntimeException {
        dbdData = new DBDData();
        again: for (int i = 0; i < paths.length; i++) {
            dbdData = DBDResolver.resolveDBD(dbdData, paths[i]);
            if (dbdData == null) {
                throw new RuntimeException(paths[i]);
            }
        }
        engine.reloadModel();
    }

    public String getFileSuffix() {
        return VDB_SUFFIX;
    }

    public Collection<LogRecord> validate() {
        DBModel dbmodel = (DBModel) engine.getModel();
        DBModelDescriptor dbmdesc = (DBModelDescriptor) dbmodel.getModelDescriptor();
        return dbmdesc.checkConsistency();
    }
}
