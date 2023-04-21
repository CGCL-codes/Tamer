package org.kalypso.nofdpidss.analysis.suitabilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang.NotImplementedException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.kalypso.contribs.eclipse.core.runtime.StatusUtilities;
import org.kalypso.nofdpidss.analysis.i18n.Messages;
import org.kalypso.nofdpidss.core.NofdpCorePlugin;
import org.kalypso.nofdpidss.core.base.gml.model.geodata.IGeodataCategory;
import org.kalypso.nofdpidss.core.base.gml.model.geodata.IGeodataSet;
import org.kalypso.nofdpidss.core.base.gml.model.geodata.implementation.GeodataCategoryHandler;
import org.kalypso.nofdpidss.core.common.shape.ShapeDissolverTools;
import org.kalypso.nofdpidss.core.view.map.MyAddShapeThemeCommand;
import org.kalypso.nofdpidss.geodata.comparator.IShapeResultWorkspace;
import org.kalypso.ogc.gml.CascadingLayerKalypsoTheme;
import org.kalypso.ogc.gml.IKalypsoLayerModell;
import org.kalypso.ogc.gml.IKalypsoTheme;
import org.kalypso.ogc.gml.map.IMapPanel;
import org.kalypso.ogc.gml.mapmodel.IMapModell;
import org.kalypso.ogc.gml.serialize.ShapeSerializer;
import org.kalypso.template.types.StyledLayerType;
import org.kalypso.template.types.StyledLayerType.Property;
import org.kalypso.ui.action.AddCascadingThemeCommand;
import org.kalypso.ui.action.IThemeCommand.ADD_THEME_POSITION;
import org.kalypso.ui.views.map.MapView;
import org.kalypsodeegree.model.feature.Feature;
import org.kalypsodeegree.model.feature.FeatureList;
import org.kalypsodeegree.model.feature.GMLWorkspace;
import org.kalypsodeegree.model.geometry.GM_Envelope;
import org.kalypsodeegree_impl.io.shpapi.ShapeFile;
import org.kalypsodeegree_impl.io.shpapi.dataprovider.StandardShapeDataProvider;

/**
 * @author Dirk Kuch
 */
public class SuitabilityHelper {

    public static final String LAYER_ID = "LAYER_ID";

    private static final String IS_INPUT_LAYER = "IS_INPUT_LAYER";

    public static final String IS_RESULT_LAYER = "RESULT_LAYER";

    private static int DEBUG_COUNT = 0;

    public static void writeDebug(IShapeResultWorkspace result) {
        GMLWorkspace workspace = result.getWorkspace();
        List<Feature> myFeatures = new ArrayList<Feature>();
        final FeatureList list = ShapeDissolverTools.getFeatureListFromRoot(workspace.getRootFeature());
        for (Object object : list) {
            if (object instanceof Feature) myFeatures.add((Feature) object);
        }
        try {
            IProject project = NofdpCorePlugin.getProjectManager().getActiveProject();
            IFolder folder = project.getFolder(".tmp");
            IFile file = folder.getFile(String.format("dbg_%d", DEBUG_COUNT++));
            if (file.exists()) file.delete(true, new NullProgressMonitor());
            final Feature[] array = myFeatures.toArray(new Feature[] {});
            final StandardShapeDataProvider provider = new StandardShapeDataProvider(array);
            ShapeSerializer.serializeFeatures(array, new HashMap<String, String>(), ShapeFile.GEOM, file.getLocation().toFile().getPath(), provider);
        } catch (Exception e) {
            NofdpCorePlugin.getDefault().getLog().log(StatusUtilities.statusFromThrowable(e));
        }
    }

    public static void showInputData(final GeodataCategoryHandler category, final IGeodataSet dataset, final MapView mapView) {
        final IKalypsoLayerModell mapModell = (IKalypsoLayerModell) mapView.getMapPanel().getMapModell();
        cleanMap(mapModell, category);
        final AddCascadingThemeCommand command = new AddCascadingThemeCommand(mapModell, String.format(Messages.SuitabilityHelper_1, category.getName()), ADD_THEME_POSITION.eFront);
        command.addCommand(new MyAddShapeThemeCommand(NofdpCorePlugin.getProjectManager().getActiveProject(), dataset));
        final Property prLayerId = new StyledLayerType.Property();
        prLayerId.setName(LAYER_ID);
        prLayerId.setValue(category.getId());
        final Property prInput = new StyledLayerType.Property();
        prInput.setName(IS_INPUT_LAYER);
        prInput.setValue("true");
        final Property prDel = new StyledLayerType.Property();
        prDel.setName("deleteable");
        prDel.setValue("false");
        command.addProperties(new StyledLayerType.Property[] { prLayerId, prInput, prDel });
        mapView.postCommand(command, null);
    }

    private static void cleanMap(final IKalypsoLayerModell mapModell, final IGeodataCategory category) {
        final IKalypsoTheme[] themes = mapModell.getAllThemes();
        for (final IKalypsoTheme theme : themes) {
            final String property = theme.getProperty(LAYER_ID, "");
            if (category.getId().equals(property)) mapModell.removeTheme(theme);
        }
    }

    public static void removeAllResultsAndInputs(final MapView mapView) {
        final IMapPanel mapPanel = mapView.getMapPanel();
        final IMapModell mapModell = mapPanel.getMapModell();
        final IKalypsoTheme[] themes = mapModell.getAllThemes();
        for (final IKalypsoTheme theme : themes) {
            final String input = theme.getProperty(IS_INPUT_LAYER, "false");
            final Boolean isInputLayer = Boolean.valueOf(input);
            final String result = theme.getProperty(IS_RESULT_LAYER, "false");
            final Boolean isResultLayer = Boolean.valueOf(result);
            if (Boolean.TRUE.equals(isInputLayer)) mapModell.removeTheme(theme); else if (Boolean.TRUE.equals(isResultLayer)) mapModell.removeTheme(theme);
        }
    }

    public static void removeResults(final MapView mapView) {
        final IMapPanel mapPanel = mapView.getMapPanel();
        final IMapModell mapModell = mapPanel.getMapModell();
        final IKalypsoTheme[] themes = mapModell.getAllThemes();
        for (final IKalypsoTheme theme : themes) {
            final String result = theme.getProperty(IS_RESULT_LAYER, "false");
            final Boolean isResultLayer = Boolean.valueOf(result);
            if (Boolean.TRUE.equals(isResultLayer)) mapModell.removeTheme(theme);
        }
    }

    public static void zoomToResultLayer(final MapView mapView) {
        final IMapPanel mapPanel = mapView.getMapPanel();
        final IMapModell mapModell = mapPanel.getMapModell();
        final IKalypsoTheme[] themes = mapModell.getAllThemes();
        for (final IKalypsoTheme theme : themes) {
            final String result = theme.getProperty(IS_RESULT_LAYER, "false");
            final Boolean isResultLayer = Boolean.valueOf(result);
            if (Boolean.TRUE.equals(isResultLayer) && theme instanceof CascadingLayerKalypsoTheme) {
                final CascadingLayerKalypsoTheme cascading = (CascadingLayerKalypsoTheme) theme;
                final GM_Envelope extent = cascading.getFullExtent();
                throw new NotImplementedException();
            }
        }
    }
}
