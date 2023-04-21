package org.dcm4chee.archive.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.dcm4che2.data.BasicDicomObject;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.Tag;
import org.dcm4che2.data.TransferSyntax;
import org.dcm4che2.data.VR;
import org.dcm4che2.io.DicomInputStream;
import org.dcm4che2.io.DicomOutputStream;
import org.dcm4chee.archive.exceptions.BlobCorruptedException;

/**
 * @author Damien Evans <damien.daddy@gmail.com>
 * @author Justin Falk <jfalkmu@gmail.com>
 * @author Gunter Zeilinger <gunterze@gmail.com>
 * @version $Revision$ $Date$
 * @since Feb 26, 2008
 */
public class DicomObjectUtils {

    public static byte[] encode(DicomObject attrs, String tsuid) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            DicomOutputStream dos = new DicomOutputStream(baos);
            if (tsuid == null) {
                dos.writeDataset(attrs, TransferSyntax.ExplicitVRLittleEndian);
            } else {
                dos.setPreamble(null);
                attrs.putString(Tag.TransferSyntaxUID, VR.UI, tsuid);
                try {
                    dos.writeDicomFile(attrs);
                } finally {
                    attrs.remove(Tag.TransferSyntaxUID);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
        return baos.toByteArray();
    }

    public static DicomObject decode(byte[] b) {
        BasicDicomObject dest = new BasicDicomObject();
        decode(b, dest);
        return dest;
    }

    public static void decode(byte[] b, DicomObject dest) {
        try {
            new DicomInputStream(new ByteArrayInputStream(b)).readDicomObject(dest, -1);
        } catch (IOException e) {
            throw new BlobCorruptedException(e);
        }
        dest.remove(Tag.TransferSyntaxUID);
    }
}
