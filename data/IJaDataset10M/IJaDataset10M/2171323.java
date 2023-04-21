package extract;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.tika.extractor.DocumentSelector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.TeeContentHandler;
import org.bson.types.ObjectId;
import org.xml.sax.ContentHandler;

public class ExtractDoc {

    private ParseContext context;

    private Parser parser;

    private ImageSavingParser imageParser;

    private Metadata metadata;

    private MetadataVO vo;

    public final List<DocVO> docList;

    private static int Count = 1;

    public ExtractDoc(Parser parser) {
        this.context = new ParseContext();
        this.parser = parser;
        this.docList = new ArrayList<DocVO>();
        this.imageParser = new ImageSavingParser(parser);
        this.context.set(DocumentSelector.class, new ImageDocumentSelector());
        this.context.set(Parser.class, imageParser);
    }

    public void openFile(ArrayList<File> fileList) {
        for (File file : fileList) {
            try {
                metadata = new Metadata();
                vo = new MetadataVO();
                DocVO docVO = new DocVO(new ObjectId().toString(), file);
                TikaInputStream stream = TikaInputStream.get(file, metadata);
                if (file != null) {
                    byte[] b = new byte[(int) file.length()];
                    if (0 == b.length) {
                        throw new Exception("zeroByte");
                    } else {
                        try {
                            docVO.init(metadata, handleStream(stream, metadata).toString());
                            vo.init(metadata);
                            docVO.setMetadataVO(vo);
                        } finally {
                            docList.add(docVO);
                            stream.close();
                        }
                    }
                }
            } catch (Throwable t) {
                handleError(file.getPath(), t);
            }
        }
    }

    private StringWriter handleStream(InputStream input, Metadata md) throws Exception {
        StringWriter textBuffer = new StringWriter();
        ContentHandler handler = new TeeContentHandler(XHandler.getTextContentHandler(textBuffer));
        context.set(DocumentSelector.class, new ImageDocumentSelector());
        parser.parse(input, handler, md, context);
        System.out.println(Count++ + " => Finished: " + md.get(Metadata.RESOURCE_NAME_KEY));
        String[] names = md.names();
        Arrays.sort(names);
        for (String name : names) {
            System.out.println(name + ": " + md.get(name));
        }
        System.out.println("-----------------------------------------------");
        return textBuffer;
    }

    private void handleError(String name, Throwable t) {
        System.out.println(t.getMessage());
        if (t.getMessage().equals("zeroByte")) {
            return;
        } else {
            StringWriter writer = new StringWriter();
            writer.append("Apache Tika was unable to parse the document\n");
            writer.append("at " + name + ".\n\n");
            writer.append("The full exception stack trace is included below:\n\n");
            t.printStackTrace(new PrintWriter(writer));
            System.out.println(writer.toString());
        }
    }

    /**
	 * A {@link DocumentSelector} that accepts only images.
	 */
    private static class ImageDocumentSelector implements DocumentSelector {

        public boolean select(Metadata metadata) {
            String type = metadata.get(Metadata.CONTENT_TYPE);
            return type != null && type.startsWith("image/");
        }
    }
}
