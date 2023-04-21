package de.mpiwg.vspace.generation.control.internal.images;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import de.mpiwg.vspace.diagram.util.shared.DefaultIconProvider;
import de.mpiwg.vspace.generation.control.service.IPreGenmodelTransformationModelProcessor;
import de.mpiwg.vspace.metamodel.transformed.Exhibition;
import de.mpiwg.vspace.metamodel.transformed.Image;
import de.mpiwg.vspace.metamodel.transformed.ImageType;
import de.mpiwg.vspace.metamodel.transformed.Link;
import de.mpiwg.vspace.metamodel.transformed.MapContainer;
import de.mpiwg.vspace.metamodel.transformed.OverviewMap;
import de.mpiwg.vspace.metamodel.transformed.Scene;
import de.mpiwg.vspace.metamodel.transformed.TransformedFactory;
import de.mpiwg.vspace.metamodel.transformed.TransformedPackage;

public class CopyLinkIconPreGenmodelTransformationModelProcessor implements IPreGenmodelTransformationModelProcessor {

    public CopyLinkIconPreGenmodelTransformationModelProcessor() {
    }

    public Exhibition processModel(Exhibition exhibition) {
        List<Scene> scenes = exhibition.getScenes();
        List<Scene> scenesAndMaps = new ArrayList<Scene>();
        if (scenes != null) {
            Scene[] sceneArray = scenes.toArray(new Scene[scenes.size()]);
            for (Scene s : sceneArray) scenesAndMaps.add(s);
        }
        OverviewMap[] mapArray;
        if (exhibition.getMapContainer() != null) {
            if (exhibition.getMapContainer().getOverviewMaps() != null) {
                mapArray = exhibition.getMapContainer().getOverviewMaps().toArray(new OverviewMap[exhibition.getMapContainer().getOverviewMaps().size()]);
                for (OverviewMap m : mapArray) {
                    scenesAndMaps.add(m);
                }
            }
        }
        for (MapContainer con : exhibition.getClonedMapContainers()) {
            mapArray = con.getOverviewMaps().toArray(new OverviewMap[con.getOverviewMaps().size()]);
            for (OverviewMap m : mapArray) {
                scenesAndMaps.add(m);
            }
        }
        for (Scene scene : scenesAndMaps) {
            List<Link> links = scene.getLinks();
            if (links != null) {
                for (Link l : links) {
                    if ((l.getIcon() == null) && l.getShowIcon()) {
                        File iconFile = DefaultIconProvider.INSTANCE.getDefaultIconFile(l);
                        if (iconFile == null || !iconFile.exists()) continue;
                        Image iconImage = TransformedFactory.eINSTANCE.createImage();
                        iconImage.setImagePath(iconFile.getAbsolutePath());
                        iconImage.setType(ImageType.LOCAL);
                        Path pathP = new Path(iconImage.getImagePath());
                        String ext = pathP.getFileExtension();
                        iconImage.setFileextension(ext);
                        String lastSegment = pathP.lastSegment();
                        if (lastSegment != null) {
                            int indexExtDot = lastSegment.lastIndexOf(".");
                            String name = lastSegment.substring(0, indexExtDot);
                            iconImage.setFilename(name);
                        }
                        l.setIcon(iconImage);
                    }
                    if (l.getShowBackground() == null) l.setShowBackground(true);
                    if (l.getShowBackground()) {
                        if (l.getBackgroundImage() == null) {
                            File defaultBg = DefaultIconProvider.INSTANCE.getLinkDefaultBackground(l);
                            Image bgImage = TransformedFactory.eINSTANCE.createImage();
                            bgImage.setImagePath(defaultBg.getAbsolutePath());
                            bgImage.setType(ImageType.LOCAL);
                            Path pathP = new Path(bgImage.getImagePath());
                            String ext = pathP.getFileExtension();
                            bgImage.setFileextension(ext);
                            String lastSegment = pathP.lastSegment();
                            if (lastSegment != null) {
                                int indexExtDot = lastSegment.lastIndexOf(".");
                                String name = lastSegment.substring(0, indexExtDot);
                                bgImage.setFilename(name);
                            }
                            l.setBackgroundImage(bgImage);
                        }
                    }
                }
            }
        }
        return exhibition;
    }

    /**
	 * Returns the rank in the sequence of processors of this processor. Rank 0 is the first processor
	 * to be executed.
	 * Rank is 50;
	 */
    public int getRanking() {
        return 50;
    }
}
