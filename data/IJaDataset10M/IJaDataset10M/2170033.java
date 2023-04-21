package org.columba.ristretto.composer.mimepartrenderers;

import org.columba.ristretto.composer.MimePartRenderer;
import org.columba.ristretto.composer.MimeTreeRenderer;
import org.columba.ristretto.io.SequenceInputStream;
import org.columba.ristretto.message.MimeHeader;
import org.columba.ristretto.message.MimePart;
import org.columba.ristretto.message.StreamableMimePart;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Vector;

/**
 * MIME Multipart renderer.
 * 
 * <br>
 * <b>RFC(s):</b> 2046
 * @author Timo Stich <tstich@users.sourceforge.net>
 */
public class MultipartRenderer extends MimePartRenderer {

    /**
	 * @see org.columba.ristretto.composer.MimePartRenderer#getRegisterString()
	 */
    public String getRegisterString() {
        return "multipart";
    }

    /**
	 * @see org.columba.ristretto.composer.MimePartRenderer#render(org.columba.ristretto.message.MimePart)
	 */
    public InputStream render(MimePart part) throws Exception {
        Vector streams = new Vector(part.countChilds() * 2 + 3);
        MimeHeader header = part.getHeader();
        String boundary = createUniqueBoundary().toString();
        header.putContentParameter("boundary", boundary);
        byte[] startBoundary = ("\r\n--" + boundary + "\r\n").getBytes();
        byte[] endBoundary = ("\r\n--" + boundary + "--\r\n").getBytes();
        streams.add(header.getHeader().getInputStream());
        if (part instanceof StreamableMimePart) streams.add(((StreamableMimePart) part).getInputStream());
        for (int i = 0; i < part.countChilds(); i++) {
            streams.add(new ByteArrayInputStream(startBoundary));
            streams.add(MimeTreeRenderer.getInstance().renderMimePart(part.getChild(i)));
        }
        streams.add(new ByteArrayInputStream(endBoundary));
        return new SequenceInputStream(streams);
    }
}
