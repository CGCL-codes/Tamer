package crl.blackberry.pdfwriter;

public class Stream extends EnclosedContent {

    public Stream() {
        super();
        setBeginKeyword("stream", false, true);
        setEndKeyword("endstream", false, true);
    }
}
