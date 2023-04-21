package org.exist.eclipse.xquery.ui.internal.text;

import org.eclipse.dltk.ui.text.ScriptSourceViewerConfiguration;
import org.eclipse.dltk.ui.text.ScriptTextTools;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * Entry point for partitioning the content which is used for e.g. syntax
 * highlighting. This class is also registered on an extension.
 * 
 * @author Pascal Schmidiger
 */
public class XQueryTextTools extends ScriptTextTools {

    private static final String[] LEGAL_CONTENT_TYPES = new String[] { IXQueryPartitions.XQUERY_STRING, IXQueryPartitions.XQUERY_COMMENT };

    private IPartitionTokenScanner _partitionScanner;

    public XQueryTextTools(boolean autoDisposeOnDisplayDispose) {
        super(IXQueryPartitions.XQUERY_PARTITIONING, LEGAL_CONTENT_TYPES, autoDisposeOnDisplayDispose);
        _partitionScanner = new XQueryPartitionScanner();
    }

    public ScriptSourceViewerConfiguration createSourceViewerConfiguraton(IPreferenceStore preferenceStore, ITextEditor editor, String partitioning) {
        return new XQuerySourceViewerConfiguration(getColorManager(), preferenceStore, editor, partitioning);
    }

    public IPartitionTokenScanner getPartitionScanner() {
        return _partitionScanner;
    }
}
