package rat.document;

import java.io.IOException;
import java.io.Reader;

public class MockArchiveDocument implements IDocument {

    public IDocumentCollection contents;

    public String name;

    public MockArchiveDocument() {
    }

    public MockArchiveDocument(String name) {
        this.name = name;
    }

    public MockArchiveDocument(String name, IDocumentCollection contents) {
        super();
        this.contents = contents;
        this.name = name;
    }

    public IDocumentCollection readArchive() throws IOException {
        return contents;
    }

    public Reader reader() throws IOException {
        throw new CompositeDocumentException();
    }

    public String getName() {
        return name;
    }
}
