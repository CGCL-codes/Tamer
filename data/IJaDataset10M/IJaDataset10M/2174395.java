package iconsensus.common.volumedata;

import iconsensus.math.Point3d;
import iconsensus.math.Vector3d;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import org.w3c.dom.Element;

public class LungVolumeReader {

    private static final String ROOT_NODE = "LungVolume";

    public LungVolume readLungVolume(String volumeNameWithPath) throws IOException {
        System.out.println("In readLungVolume");
        XmlLoader loader = new XmlLoader(new URL(volumeNameWithPath));
        String serInstUID = loader.getTextValue(ROOT_NODE, "SeriesInstanceUID");
        String heightStr = loader.getTextValue(ROOT_NODE, "Height");
        int height = Integer.parseInt(heightStr);
        String widthStr = loader.getTextValue(ROOT_NODE, "Width");
        int width = Integer.parseInt(widthStr);
        String depthStr = loader.getTextValue(ROOT_NODE, "Depth");
        int depth = Integer.parseInt(depthStr);
        String numImagesStr = loader.getTextValue(ROOT_NODE, "NumImages");
        int numImages = Integer.parseInt(numImagesStr);
        String rowPixelSpacingStr = loader.getTextValue(ROOT_NODE, "RowPixelSpacing");
        double rowPixelSpacing = Double.parseDouble(rowPixelSpacingStr);
        String colPixelSpacingStr = loader.getTextValue(ROOT_NODE, "ColumnPixelSpacing");
        double colPixelSpacing = Double.parseDouble(colPixelSpacingStr);
        String depthPixelSpacingStr = loader.getTextValue(ROOT_NODE, "DepthPixelSpacing");
        double depthPixelSpacing = Double.parseDouble(depthPixelSpacingStr);
        LungVolume vol = new LungVolume();
        vol.setSeriesInstanceUID(serInstUID);
        vol.setHeight(height);
        vol.setWidth(width);
        vol.setDepth(depth);
        vol.setNumImages(numImages);
        vol.setRowPixelSpacing(rowPixelSpacing);
        vol.setColumnPixelSpacing(colPixelSpacing);
        vol.setDepthPixelSpacing(depthPixelSpacing);
        ArrayList listOfImages = loader.getAllElementsWithSpecifiedNameUnderParentElement(ROOT_NODE, "LungImage");
        for (int i = 0; i < listOfImages.size(); i++) {
            Element imgEl = (Element) listOfImages.get(i);
            LungImage lungImg = readLungImage(loader, imgEl);
            vol.addLungImage(lungImg);
        }
        return vol;
    }

    private LungImage readLungImage(XmlLoader loader, Element imageEl) {
        String imgInstUID = imageEl.getAttribute("ImageInstanceUID");
        String imgNameWithCompletePath = imageEl.getAttribute("ImageNameWithCompletePath");
        String serInstUID = loader.getTextValue(imageEl, "SeriesInstanceUID");
        String studyInstUID = loader.getTextValue(imageEl, "StudyInstanceUID");
        int height = loader.getIntValue(imageEl, "Height");
        int width = loader.getIntValue(imageEl, "Width");
        double rowPixSpacing = loader.getDoubleValue(imageEl, "RowPixelSpacing");
        double colPixSpacing = loader.getDoubleValue(imageEl, "ColumnPixelSpacing");
        int sliceOrder = loader.getIntValue(imageEl, "SliceOrder");
        Element imagePositionEl = loader.getElementUnderSpecifiedParentElement(imageEl, "ImagePosition");
        String imagePositionXStr = imagePositionEl.getAttribute("X");
        double imagePositionX = Double.parseDouble(imagePositionXStr);
        String imagePositionYStr = imagePositionEl.getAttribute("Y");
        double imagePositionY = Double.parseDouble(imagePositionYStr);
        String imagePositionZStr = imagePositionEl.getAttribute("Z");
        double imagePositionZ = Double.parseDouble(imagePositionZStr);
        Element colDirCosineEl = loader.getElementUnderSpecifiedParentElement(imageEl, "ColumnDirectionCosine");
        String colDirCosineXStr = colDirCosineEl.getAttribute("X");
        double colDirCosineX = Double.parseDouble(colDirCosineXStr);
        String colDirCosineYStr = colDirCosineEl.getAttribute("Y");
        double colDirCosineY = Double.parseDouble(colDirCosineYStr);
        String colDirCosineZStr = colDirCosineEl.getAttribute("Z");
        double colDirCosineZ = Double.parseDouble(colDirCosineZStr);
        Element rowDirCosineEl = loader.getElementUnderSpecifiedParentElement(imageEl, "RowDirectionCosine");
        String rowDirCosineXStr = rowDirCosineEl.getAttribute("X");
        double rowDirCosineX = Double.parseDouble(rowDirCosineXStr);
        String rowDirCosineYStr = rowDirCosineEl.getAttribute("Y");
        double rowDirCosineY = Double.parseDouble(rowDirCosineYStr);
        String rowDirCosineZStr = rowDirCosineEl.getAttribute("Z");
        double rowDirCosineZ = Double.parseDouble(rowDirCosineZStr);
        LungImage lungImage = new LungImage();
        lungImage.setWidth(width);
        lungImage.setHeight(height);
        lungImage.setImageNameWithCompletePath(imgNameWithCompletePath);
        lungImage.setImageInstanceUID(imgInstUID);
        lungImage.setSeriesInstanceUID(serInstUID);
        lungImage.setStudyInstanceUID(studyInstUID);
        lungImage.setImagePosition(new Point3d(imagePositionX, imagePositionY, imagePositionZ));
        lungImage.setRowDirectionCosine(new Vector3d(rowDirCosineX, rowDirCosineY, rowDirCosineZ));
        lungImage.setColumnDirectionCosine(new Vector3d(colDirCosineX, colDirCosineY, colDirCosineZ));
        lungImage.setRowPixelSpacing(rowPixSpacing);
        lungImage.setColumnPixelSpacing(colPixSpacing);
        lungImage.setSliceOrder(sliceOrder);
        return lungImage;
    }
}
