package org.kalypso.nofdpidss.ui.application.projectmanager;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.kalypso.commons.eclipse.core.runtime.PluginImageProvider;
import org.kalypso.nofdp.idss.schema.schemata.gml.GmlConstants;
import org.kalypso.nofdpidss.ui.NofdpIDSSImages;
import org.kalypso.nofdpidss.ui.NofdpUiPlugin;
import org.kalypso.nofdpidss.ui.view.wizard.project.document.NewDocumentSettings.DOCUMENT_TYPE;
import org.kalypso.ogc.gml.FeatureUtils;
import org.kalypsodeegree.model.feature.Feature;

/**
 * @author Dirk Kuch
 */
public class DocumentTreeLabelProvider extends LabelProvider {

    /**
   * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
   */
    @Override
    public Image getImage(final Object element) {
        if (element instanceof Feature) {
            final Feature document = (Feature) element;
            final DOCUMENT_TYPE type = DOCUMENT_TYPE.getType(document.getFeatureType());
            final PluginImageProvider provider = NofdpUiPlugin.getImageProvider();
            switch(type) {
                case archive:
                    return provider.getImage(NofdpIDSSImages.DESCRIPTORS.PROJECT_INFO_DOC_ARCHIVE);
                case image:
                    return provider.getImage(NofdpIDSSImages.DESCRIPTORS.PROJECT_INFO_DOC_IMAGE);
                case pdf:
                    return provider.getImage(NofdpIDSSImages.DESCRIPTORS.PROJECT_INFO_DOC_PDF);
                case presentation:
                    return provider.getImage(NofdpIDSSImages.DESCRIPTORS.PROJECT_INFO_DOC_PRESENTATION);
                case spreadsheet:
                    return provider.getImage(NofdpIDSSImages.DESCRIPTORS.PROJECT_INFO_DOC_SPREADSHEET);
                case text:
                    return provider.getImage(NofdpIDSSImages.DESCRIPTORS.PROJECT_INFO_DOC_TEXT);
                default:
                    return provider.getImage(NofdpIDSSImages.DESCRIPTORS.PROJECT_INFO_DOC_GENERAL);
            }
        }
        return super.getImage(element);
    }

    /**
   * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
   */
    @Override
    public String getText(final Object element) {
        if (element instanceof Feature) {
            final Feature document = (Feature) element;
            return FeatureUtils.getFeatureName(GmlConstants.NS_PROJECT_INFO, document);
        }
        return super.getText(element);
    }
}
