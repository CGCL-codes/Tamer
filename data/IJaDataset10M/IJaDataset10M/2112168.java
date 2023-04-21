package org.plazmaforge.studio.dbmanager.editors;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.VerticalRuler;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

public class SQLTextViewer extends SourceViewer {

    private IPreferenceStore store;

    private IContentAssistant contentAssistant;

    private Dictionary dictionary;

    private PreferenceListener fPreferenceListener = new PreferenceListener();

    private SQLSourceViewerConfiguration configuration;

    public SQLTextViewer(Composite parent, int style, IPreferenceStore store, Dictionary dictionary, IVerticalRuler ruler, SQLSourceViewerConfiguration configuration) {
        super(parent, ruler, style);
        if (store == null) {
            throw new IllegalArgumentException("Preference Store must be not null");
        }
        this.store = store;
        this.dictionary = dictionary;
        if (configuration == null) {
            this.configuration = SQLSourceViewerConfiguration.createConfiguration(store, dictionary);
            configure(this.configuration);
        } else {
            this.configuration = configuration;
        }
        installReconciler();
        installContentAssistant();
        getControl().addDisposeListener(new DisposeListener() {

            public void widgetDisposed(DisposeEvent event) {
                disposeSQLTextTools();
                uninstallReconciler();
            }
        });
        parent.addDisposeListener(new DisposeListener() {

            public void widgetDisposed(DisposeEvent event) {
                uninstallContentAssistant();
            }
        });
        store.addPropertyChangeListener(fPreferenceListener);
        updateFont();
        initHover();
        super.activatePlugins();
    }

    protected void updateFont() {
        if (store == null) {
            return;
        }
        FontData[] fData = PreferenceConverter.getFontDataArray(store, IConstants.FONT);
        if (fData.length > 0) {
            String key = fData[0].toString();
            updateFont(key, fData);
        }
    }

    protected void updateFont(String key, FontData[] fData) {
        JFaceResources.getFontRegistry().put(key, fData);
        Font font = JFaceResources.getFontRegistry().get(key);
        setFont(font);
    }

    /**
     * Set font
     * @param font
     */
    public void setFont(Font font) {
        if (font == null) {
            return;
        }
        if (getControl() != null) getControl().setFont(font);
        if (getTextWidget() != null) getTextWidget().setFont(font);
    }

    protected SQLTextTools getSQLTextTools() {
        return configuration == null ? null : configuration.getSQLTextTools();
    }

    protected IDocumentPartitioner getDocumentPartitioner() {
        return configuration == null ? null : configuration.getDocumentPartitioner();
    }

    public void setNewDictionary(Dictionary newDictionary) {
        dictionary = newDictionary;
        uninstallContentAssistant();
        configuration = SQLSourceViewerConfiguration.createConfiguration(store, dictionary);
        installReconciler();
        installContentAssistant();
    }

    protected void installReconciler() {
        fPresentationReconciler = configuration.getPresentationReconciler(null);
        if (fPresentationReconciler != null) {
            fPresentationReconciler.install(this);
        }
    }

    protected void installContentAssistant() {
        if (dictionary == null) {
            return;
        }
        contentAssistant = configuration.getContentAssistant(null);
        if (contentAssistant == null) {
            return;
        }
        contentAssistant.install(this);
    }

    protected void uninstallReconciler() {
        if (fPresentationReconciler == null) {
            return;
        }
        fPresentationReconciler.uninstall();
    }

    protected void uninstallContentAssistant() {
        if (contentAssistant == null) {
            return;
        }
        contentAssistant.uninstall();
        contentAssistant = null;
    }

    protected void disposeSQLTextTools() {
        if (getSQLTextTools() == null) {
            return;
        }
        getSQLTextTools().dispose();
    }

    protected void initHover() {
        setAnnotationHover(new IAnnotationHover() {

            public String getHoverInfo(ISourceViewer sourceViewer, int lineNumber) {
                return "hover info";
            }
        });
        setHoverControlCreator(new IInformationControlCreator() {

            public IInformationControl createInformationControl(Shell parent) {
                return new DefaultInformationControl(parent);
            }
        });
        String[] contentTypes = { IDocument.DEFAULT_CONTENT_TYPE, IConstants.SQL_SINGLE_LINE_COMMENT, IConstants.SQL_STRING, IConstants.SQL_MULTILINE_COMMENT };
        for (int i = 0; i < contentTypes.length; i++) {
            super.setTextHover(new ITextHover() {

                public String getHoverInfo(ITextViewer textViewer, IRegion hoverRegion) {
                    return "";
                }

                public IRegion getHoverRegion(ITextViewer textViewer, int offset) {
                    return new Region(offset, 1);
                }
            }, contentTypes[i]);
        }
    }

    public SQLTextViewer(Composite parent, int style, IPreferenceStore store, final Dictionary dictionary) {
        this(parent, style, store, dictionary, new VerticalRuler(0), null);
    }

    private class PreferenceListener implements IPropertyChangeListener {

        public void propertyChange(PropertyChangeEvent event) {
            adaptToPreferenceChange(event);
        }
    }

    ;

    void adaptToPreferenceChange(PropertyChangeEvent event) {
        String property = event.getProperty();
        if (!isVisualSQLProperty(property)) {
            return;
        }
        if (IConstants.FONT.equals(property)) {
            FontData[] fData = PreferenceConverter.getFontDataArray(store, IConstants.FONT);
            String key = store.getString(IConstants.FONT);
            updateFont(key, fData);
        }
        invalidateTextPresentation();
    }

    public void showAssistance() {
    }

    public void setDocument(IDocument document, IAnnotationModel annotationModel) {
        setDocument(document);
        if (annotationModel != null && document != null) annotationModel.connect(document);
    }

    public void setDocument(IDocument document) {
        IDocument previous = this.getDocument();
        if (previous != null) {
            getDocumentPartitioner().disconnect();
        }
        super.setDocument(document);
        if (document != null) {
            getDocumentPartitioner().connect(document);
            document.setDocumentPartitioner(getDocumentPartitioner());
        }
    }

    /**
     * 
     */
    public void clearText() {
        getTextWidget().setText("");
    }

    /**
     * Return true if property is SQL preferences property.
     * @param name
     * @return
     */
    protected boolean isVisualSQLProperty(String name) {
        if (IConstants.FONT.equals(name)) {
            return true;
        }
        List<String> properties = getVisualSQLProperties();
        for (String p : properties) {
            if (p.equals(name)) {
                return true;
            }
        }
        return false;
    }

    private static List<String> VISUAL_SQL_PROPERTIES;

    private static List<String> getVisualSQLProperties() {
        if (VISUAL_SQL_PROPERTIES == null) {
            VISUAL_SQL_PROPERTIES = new ArrayList<String>();
            addVisualSQLProperty(IConstants.SQL_TABLE);
            addVisualSQLProperty(IConstants.SQL_COLUMS);
            addVisualSQLProperty(IConstants.SQL_KEYWORD);
            addVisualSQLProperty(IConstants.SQL_SINGLE_LINE_COMMENT);
            addVisualSQLProperty(IConstants.SQL_MULTILINE_COMMENT);
            addVisualSQLProperty(IConstants.SQL_STRING);
            addVisualSQLProperty(IConstants.SQL_DEFAULT);
        }
        return VISUAL_SQL_PROPERTIES;
    }

    private static void addVisualSQLProperty(String name) {
        VISUAL_SQL_PROPERTIES.add(name);
        VISUAL_SQL_PROPERTIES.add(name + IConstants.BOLD);
    }
}
