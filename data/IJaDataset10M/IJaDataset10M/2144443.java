package net.sf.clairv.index.builder.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import net.sf.clairv.index.builder.DocumentBuilder;
import net.sf.clairv.index.builder.DocumentHandlerException;
import net.sf.clairv.index.document.Document;
import net.sf.clairv.index.document.IndexOption;
import net.sf.clairv.index.document.StoreOption;

public class PlainTextBuilder implements DocumentBuilder {

    public void buildDocument(InputStream is, Document doc) throws DocumentHandlerException {
        StringBuffer bodyText = new StringBuffer();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while ((line = br.readLine()) != null) {
                bodyText.append(line).append("\n");
            }
            br.close();
        } catch (IOException e) {
            throw new DocumentHandlerException("Cannot read the text document", e);
        }
        doc.addField("body", bodyText.toString(), StoreOption.COMPRESS, IndexOption.TOKENIZED);
    }
}
