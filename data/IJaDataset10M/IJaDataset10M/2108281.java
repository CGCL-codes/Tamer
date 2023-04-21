package net.sf.moviekebab.toolset;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import quicktime.QTException;
import quicktime.std.StdQTConstants4;
import quicktime.std.movies.Atom;
import quicktime.std.movies.AtomContainer;
import quicktime.std.movies.AtomData;
import quicktime.util.QTHandle;
import quicktime.util.QTUtils;

/**
 * QuickTime tools.
 *
 * @author Laurent Caillette
 * @author Eric Smith, Tarkvara Design Inc. for <code>dumpAtom*</code> methods.
 *     http://lists.apple.com/archives/quicktime-java/2001/Jul/msg00037.html
 */
public class QuicktimeTools {

    public static AtomContainer readExportDescriptor(Class owningClass, String resourceName) throws IOException, QTException {
        final byte[] settingsBytes = ResourceTools.readResource(owningClass, resourceName);
        final QTHandle settingsHandle = new QTHandle(settingsBytes);
        return AtomContainer.fromQTHandle(settingsHandle);
    }

    /**
   * Dumps the contents of an AtomContainer to stdout.
   */
    public static void dumpAtomContainer(PrintStream out, AtomContainer inContainer) {
        dumpAtom(out, inContainer, Atom.kParentIsContainer, "");
    }

    public static String atomContainerAsString(AtomContainer container, String indent) {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        dumpAtom(new PrintStream(outputStream), container, Atom.kParentIsContainer, indent);
        return new String(outputStream.toByteArray());
    }

    /**
   * Dumps an atom and all its children to stdout.
   */
    public static void dumpAtom(PrintStream out, AtomContainer inContainer, Atom inAtom, String inIndent) {
        try {
            out.println(inIndent + QTUtils.fromOSType(inContainer.getAtomType(inAtom)) + ":" + inContainer.getAtomID(inAtom));
            AtomData data = inContainer.getAtomData(inAtom);
            switch(data.getSize()) {
                case 1:
                    out.println(inIndent + " 1 byte of data \'" + (char) data.getByte(0) + "\' (0x" + Integer.toHexString(data.getByte(0)) + ")");
                    break;
                case 2:
                    out.println(inIndent + " 2 bytes of data (0x" + Integer.toHexString(data.getShort(0)) + ")");
                    break;
                case 4:
                    out.println(inIndent + " 4 bytes of data \'" + QTUtils.fromOSType(data.getInt(0)) + "\' (0x" + Integer.toHexString(data.getInt(0)) + ")");
                    break;
                default:
                    out.println(inIndent + " " + data.getSize() + " bytes of data");
                    break;
            }
        } catch (quicktime.std.StdQTException e1) {
            try {
                Atom child = new Atom(0);
                while ((child = inContainer.nextChildAnyType(inAtom, child)) != null) {
                    dumpAtom(out, inContainer, child, inIndent + "  ");
                }
            } catch (quicktime.std.StdQTException e2) {
                e2.printStackTrace(out);
            }
        }
    }

    public static String getProgressMessageName(int message) {
        final String messageName;
        switch(message) {
            case StdQTConstants4.movieProgressOpen:
                messageName = "movieProgressOpen";
                break;
            case StdQTConstants4.movieProgressUpdatePercent:
                messageName = "movieProgressUpdatePercent";
                break;
            case StdQTConstants4.movieProgressClose:
                messageName = "movieProgressClose";
                break;
            default:
                messageName = "unknown" + message;
        }
        return messageName + " (" + message + ")";
    }

    public static String getProgressOperationName(int whatOperation) {
        final String operationName;
        switch(whatOperation) {
            case StdQTConstants4.progressOpFlatten:
                operationName = "progressOpFlatten";
                break;
            case StdQTConstants4.progressOpInsertTrackSegment:
                operationName = "progressOpInsertTrackSegment";
                break;
            case StdQTConstants4.progressOpInsertMovieSegment:
                operationName = "progressOpInsertMovieSegment";
                break;
            case StdQTConstants4.progressOpAddMovieSelection:
                operationName = "progressOpAddMovieSelection";
                break;
            case StdQTConstants4.progressOpCopy:
                operationName = "progressOpCopy";
                break;
            case StdQTConstants4.progressOpCut:
                operationName = "progressOpCut";
                break;
            case StdQTConstants4.progressOpLoadMovieIntoRam:
                operationName = "progressOpLoadMovieIntoRam";
                break;
            case StdQTConstants4.progressOpLoadTrackIntoRam:
                operationName = "progressOpLoadTrackIntoRam";
                break;
            case StdQTConstants4.progressOpLoadMediaIntoRam:
                operationName = "progressOpLoadMediaIntoRam";
                break;
            case StdQTConstants4.progressOpImportMovie:
                operationName = "progressOpImportMovie";
                break;
            case StdQTConstants4.progressOpExportMovie:
                operationName = "progressOpExportMovie";
                break;
            default:
                operationName = "unknown";
        }
        return operationName + " (" + whatOperation + ")";
    }
}
