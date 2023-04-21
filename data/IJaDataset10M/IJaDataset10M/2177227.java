package org.fudaa.dodico.rubar.io;

import java.io.IOException;
import java.util.Arrays;
import org.fudaa.ctulu.CtuluNumberFormater;
import org.fudaa.ctulu.collection.CtuluCollectionDouble;
import org.fudaa.dodico.fortran.FileOpWriterCharSimpleAbstract;
import org.fudaa.dodico.fortran.FortranWriter;
import org.fudaa.dodico.h2d.rubar.H2dRubarNumberFormatter;

/**
 * @author Fred Deniger
 * @version $Id: RubarFRTDIFWriter.java,v 1.3 2007-05-04 13:47:30 deniger Exp $
 */
public class RubarFRTDIFWriter extends FileOpWriterCharSimpleAbstract {

    CtuluNumberFormater numbFmt_ = H2dRubarNumberFormatter.createNumberFormateur(8, 3);

    public RubarFRTDIFWriter() {
    }

    public RubarFRTDIFWriter(final CtuluNumberFormater _numbFmt) {
        super();
        numbFmt_ = _numbFmt;
    }

    protected void internalWriteDouble(final CtuluCollectionDouble _o) {
        final int[] fmt = new int[10];
        Arrays.fill(fmt, 8);
        final int nbFieldByLine = fmt.length;
        final int nbElt = _o.getSize();
        int tmp = 0;
        final FortranWriter w = new FortranWriter(out_);
        w.setSpaceBefore(true);
        try {
            for (int i = 0; i < nbElt; i++) {
                if (tmp == nbFieldByLine) {
                    w.writeFields(fmt);
                    tmp = 0;
                }
                w.stringField(tmp++, numbFmt_.format(_o.getValue(i)));
            }
            w.writeFields(fmt);
        } catch (final IOException e) {
            analyze_.manageException(e);
        }
    }

    protected void internalWrite(final Object _o) {
        if (_o instanceof CtuluCollectionDouble) {
            internalWriteDouble((CtuluCollectionDouble) _o);
        } else {
            donneesInvalides(_o);
        }
    }

    public CtuluNumberFormater getNumbFmt() {
        return numbFmt_;
    }

    public void setNumbFmt(final CtuluNumberFormater _numbFmt) {
        numbFmt_ = _numbFmt;
    }
}
