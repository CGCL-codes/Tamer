package org.weasis.dicom.codec;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map.Entry;
import org.weasis.core.api.gui.util.AbstractProperties;
import org.weasis.core.api.media.data.MediaReader;
import org.weasis.core.api.media.data.Series;
import org.weasis.core.api.media.data.TagElement;

public class DicomVideo extends Series<DicomVideoElement> {

    private int width = 256;

    private int height = 256;

    private int frames = 0;

    public DicomVideo(String subseriesInstanceUID) {
        super(TagElement.SubseriesInstanceUID, subseriesInstanceUID, TagElement.SubseriesInstanceUID);
    }

    public DicomVideo(DicomSeries dicomSeries) {
        super(TagElement.SubseriesInstanceUID, dicomSeries.getTagValue(TagElement.SubseriesInstanceUID), TagElement.SubseriesInstanceUID);
        Iterator<Entry<TagElement, Object>> iter = dicomSeries.getTagEntrySetIterator();
        while (iter.hasNext()) {
            Entry<TagElement, Object> e = iter.next();
            setTag(e.getKey(), e.getValue());
        }
    }

    @Override
    public void addMedia(MediaReader mediaLoader) {
        if (mediaLoader instanceof DicomMediaIO) {
            DicomMediaIO dicomImageLoader = (DicomMediaIO) mediaLoader;
            frames = dicomImageLoader.getMediaElementNumber();
            byte[] mpeg = null;
            try {
                width = dicomImageLoader.getWidth(0);
                height = dicomImageLoader.getHeight(0);
                dicomImageLoader.readPixelData();
                mpeg = dicomImageLoader.getDicomObject().get(TagElement.PixelData.getId()).getFragment(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (mpeg != null) {
                OutputStream tempFileStream = null;
                try {
                    File videoFile = File.createTempFile("video_", ".mpg", AbstractProperties.APP_TEMP_DIR);
                    tempFileStream = new BufferedOutputStream(new FileOutputStream(videoFile));
                    tempFileStream.write(mpeg);
                    DicomVideoElement dicom = (DicomVideoElement) dicomImageLoader.getMediaElement();
                    dicom.setVideoFile(videoFile);
                    medias.add(dicom);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (tempFileStream != null) {
                        try {
                            tempFileStream.close();
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public String getToolTips() {
        StringBuffer toolTips = new StringBuffer();
        toolTips.append("<html>");
        addToolTipsElement(toolTips, Messages.getString("DicomSeries.pat"), TagElement.PatientName);
        addToolTipsElement(toolTips, Messages.getString("DicomSeries.mod"), TagElement.Modality);
        addToolTipsElement(toolTips, Messages.getString("DicomSeries.series_nb"), TagElement.SeriesNumber);
        addToolTipsElement(toolTips, Messages.getString("DicomSeries.study"), TagElement.StudyDescription);
        addToolTipsElement(toolTips, Messages.getString("DicomSeries.series"), TagElement.SeriesDescription);
        toolTips.append(Messages.getString("DicomSeries.date") + getDate() + "<br>");
        toolTips.append(Messages.getString("DicomVideo.video_l"));
        toolTips.append("</html>");
        return toolTips.toString();
    }

    @Override
    public String toString() {
        return (String) getTagValue(TagElement.SubseriesInstanceUID);
    }

    public String getDate() {
        Date seriesDate = (Date) getTagValue(TagElement.SeriesDate);
        if (seriesDate != null) {
            return new SimpleDateFormat("dd/MM/yyyy").format(seriesDate);
        }
        return "";
    }

    @Override
    public String getMimeType() {
        return DicomMediaIO.VIDEO_MIMETYPE;
    }
}
