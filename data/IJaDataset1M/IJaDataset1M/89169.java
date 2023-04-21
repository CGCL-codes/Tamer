package net.seagis.bean;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;
import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.apache.myfaces.custom.fileupload.UploadedFileDefaultMemoryImpl;

/**
 *
 * @author Mehdi Sidhoum.
 */
public class UploadListener implements ValueChangeListener {

    public void processValueChange(ValueChangeEvent event) throws AbortProcessingException {
        try {
            UploadedFile uploadedFile = (UploadedFile) event.getNewValue();
            UploadedFileDefaultMemoryImpl _memory = (UploadedFileDefaultMemoryImpl) uploadedFile;
            InputStream inputStream = _memory.getInputStream();
        } catch (IOException ex) {
            Logger.getLogger(UploadListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
