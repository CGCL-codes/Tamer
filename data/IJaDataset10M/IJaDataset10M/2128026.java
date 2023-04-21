package edu.columbia.hypercontent.editors.image.commands;

import edu.columbia.hypercontent.editors.BaseSessionData;
import edu.columbia.hypercontent.editors.ICommand;
import edu.columbia.hypercontent.editors.image.SessionData;
import edu.columbia.hypercontent.ContentTypes;
import edu.columbia.hypercontent.util.ImageUtil;
import edu.columbia.filesystem.impl.ByteArrayDataLoader;
import java.awt.image.*;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: Sep 3, 2003
 * Time: 3:23:33 PM
 * To change this template use Options | File Templates.
 */
public class CropImage implements ICommand {

    public void execute(BaseSessionData baseSession) throws Exception {
        SessionData session = (SessionData) baseSession;
        BufferedImage buffered = ImageUtil.read(session.global.getFile());
        double scale = Double.parseDouble(baseSession.runtimeData.getParameter("cropScale")) / 100;
        int top = Integer.parseInt(baseSession.runtimeData.getParameter("cropTop"));
        int left = Integer.parseInt(baseSession.runtimeData.getParameter("cropLeft"));
        int width = Integer.parseInt(baseSession.runtimeData.getParameter("cropWidth"));
        int height = Integer.parseInt(baseSession.runtimeData.getParameter("cropHeight"));
        if (scale != 1) {
            buffered = ImageUtil.scaleToFactor(buffered, scale, true);
        }
        if ((width > 0) && (height > 0) && (buffered.getWidth() >= (left + width)) && (buffered.getHeight() >= (top + height))) {
            buffered = buffered.getSubimage(left, top, width, height);
        }
        session.data = session.writer.getBytes(buffered);
        ImageUtil.addThumbnail(buffered, session.metadata);
    }
}
