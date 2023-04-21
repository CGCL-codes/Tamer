package org.fudaa.fudaa.tr.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.fudaa.dodico.boony.BoonyXmlSerializer;

/**
 * @author Fred Deniger
 * @version $Id: TrXmlProjectWriter.java,v 1.3 2004-07-01 11:15:44 deniger Exp $
 */
public final class TrXmlProjectWriter {

    TrXmlData[] ds_;

    /**
   * 
   */
    public TrXmlProjectWriter(TrProjet _p) {
        ds_ = _p.getXmlDataToSave();
    }

    /**
   * @param _f
   */
    public void write(File _f) {
        if (ds_ == null) return;
        BoonyXmlSerializer xml = null;
        try {
            xml = new BoonyXmlSerializer(false);
            xml.open(new FileOutputStream(_f));
            for (int i = 0; i < ds_.length; i++) {
                xml.write(ds_[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                xml.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
