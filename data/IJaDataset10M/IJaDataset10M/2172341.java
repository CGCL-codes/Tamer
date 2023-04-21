package net.community.chest.io.dom;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import net.community.chest.dom.AbstractDocumentBuilder;
import net.community.chest.io.FileUtil;
import net.community.chest.io.IOCopier;
import net.community.chest.io.input.InputStreamEmbedder;
import net.community.chest.io.input.ReaderEmbedder;

/**
 * <P>Copyright 2009 as per GPLv2</P>
 *
 * <P>Attempts to resolve the {@link InputSource} to a {@link Reader} to use
 * for parsing</P>
 * 
 * @author Lyor G.
 * @since Aug 26, 2009 9:19:21 AM
 */
public abstract class AbstractIODocumentBuilder extends AbstractDocumentBuilder {

    protected AbstractIODocumentBuilder() {
        super();
    }

    public abstract Document parse(Reader r) throws SAXException, IOException;

    @Override
    public Document parse(File f) throws SAXException, IOException {
        if (null == f) throw new IOException("parse() no " + File.class.getSimpleName() + " specified");
        Reader r = null;
        try {
            r = new BufferedReader(new FileReader(f), IOCopier.DEFAULT_COPY_SIZE);
            return parse(r);
        } finally {
            FileUtil.closeAll(r);
        }
    }

    @Override
    public Document parse(InputSource is) throws SAXException, IOException {
        if (null == is) throw new IOException("parse() no " + InputSource.class.getSimpleName() + " specified");
        Reader r = is.getCharacterStream();
        if (null == r) {
            InputStream in = is.getByteStream();
            if (null == in) return parse(is.getSystemId()); else return parse(in, "");
        }
        return parse(r);
    }

    @Override
    public Document parse(InputStream is, String systemId) throws SAXException, IOException {
        if (null == is) throw new IOException("parse() no " + InputStream.class.getSimpleName() + " specified");
        InputStream in = (is instanceof BufferedInputStream) ? is : new BufferedInputStream(new InputStreamEmbedder(is, false), IOCopier.DEFAULT_COPY_SIZE);
        Reader r = new ReaderEmbedder(new InputStreamReader(in), false);
        try {
            return parse(r);
        } finally {
            FileUtil.closeAll(r);
        }
    }
}
