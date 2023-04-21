package net.sf.poormans.tool.image;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import net.sf.poormans.exception.RenderingException;
import net.sf.poormans.livecycle.SiteHolder;
import net.sf.poormans.tool.ProcessBuilderWrapper;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Image manipulation for the commandline tool 'convert' provided by ImageMagick (http://www.imagemagick.org).
 * 
 * @version $Id: ImageManipulationImageMagick.java 2134 2011-07-17 12:14:41Z th-schwarz $
 * @author <a href="mailto:th-schwarz@users.sourceforge.net">Thilo Schwarz</a>
 */
@Component
public class ImageManipulationImageMagick {

    private static Logger logger = Logger.getLogger(ImageManipulationImageMagick.class);

    @Autowired
    private SiteHolder siteHolder;

    @Value("${imagemagick.convert.command}")
    private String basicCommand;

    @Value("${imagemagick.resolution.export}")
    private String exportResolution;

    @Value("${imagemagick.convert.parameters}")
    private String paramString;

    public void resizeImage(final File srcImageFile, final File destImageFile, final Dimension dimension) throws Exception {
        resizeImage(srcImageFile, destImageFile, dimension, true);
    }

    public void resizeImage(final File srcImageFile, final File destImageFile, final Dimension dimension, boolean useJustRenderCheck) throws Exception {
        if (useJustRenderCheck && siteHolder.containsJustRendering(destImageFile)) {
            logger.debug(" *** Already in progress: " + destImageFile.getPath());
            return;
        }
        if (useJustRenderCheck) siteHolder.addJustRendering(destImageFile);
        List<String> command = new ArrayList<String>();
        command.add(basicCommand);
        for (String additionalAttr : StringUtils.split(paramString)) command.add(additionalAttr);
        command.add(srcImageFile.getAbsolutePath());
        command.add("-resize");
        command.add(dimension.toString());
        command.add("-density");
        command.add(String.format("%sx%s", exportResolution, exportResolution));
        command.add(destImageFile.getAbsolutePath());
        logger.debug("Command list: ".concat(command.toString()));
        ProcessBuilderWrapper processBuilder = null;
        try {
            processBuilder = new ProcessBuilderWrapper(command);
            logger.debug("Process terminated with exit value: " + processBuilder.getStatus());
        } catch (Exception e) {
            throw e;
        } finally {
            if (useJustRenderCheck) siteHolder.removeJustRendering(destImageFile);
        }
        String errorOutput = processBuilder.getErrors();
        if (StringUtils.isNotBlank(errorOutput)) {
            logger.error("Error while resizing [" + srcImageFile.getName() + "]: " + errorOutput);
            throw new RenderingException("Error while resizing [" + srcImageFile.getName() + "]: " + errorOutput);
        }
    }
}
