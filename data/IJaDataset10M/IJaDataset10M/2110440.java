package org.translationcomponent.api.impl.response;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import org.translationcomponent.api.Storage;
import org.translationcomponent.api.impl.response.storage.StorageByteArray;
import org.translationcomponent.api.impl.response.storage.StorageString;
import org.translationcomponent.api.impl.response.storage.StorageStringWriter;

/**
 * Stores the translation response as a byte array, char array or String.
 * Depending on which method is called first to add content.
 * 
 * 
 * @author ROB
 * 
 */
public class TranslationResponseInMemory extends TranslationResponseAbstract {

    private final int charSize;

    public TranslationResponseInMemory(final int charSize, final String characterEncoding) {
        super(characterEncoding);
        this.charSize = charSize;
    }

    public TranslationResponseInMemory(final String characterEncoding) {
        super(characterEncoding);
        this.charSize = 2048;
    }

    public void addText(String text) throws IOException {
        getStorage(Storage.TEXT).addText(text);
    }

    public OutputStream getOutputStream() throws IOException {
        return getStorage(Storage.BYTEARRAY).getOutputStream();
    }

    public Writer getWriter() throws IOException {
        return getStorage(Storage.STRINGWRITER).getWriter();
    }

    private Storage getStorage(final int type) {
        if (this.getStorage() == null) {
            switch(type) {
                case Storage.TEXT:
                    setStorage(new StorageString(this.getCharacterEncoding()));
                    break;
                case Storage.STRINGWRITER:
                    setStorage(new StorageStringWriter(charSize, this.getCharacterEncoding()));
                    break;
                case Storage.BYTEARRAY:
                    setStorage(new StorageByteArray(charSize * 2, this.getCharacterEncoding()));
                    break;
            }
            if (this.hasEnded()) {
                try {
                    this.getStorage().close(this.getEndState());
                } catch (IOException e) {
                }
            }
        }
        return getStorage();
    }
}
