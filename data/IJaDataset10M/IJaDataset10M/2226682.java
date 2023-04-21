package org.pdfclown.tokens;

import java.text.DecimalFormat;
import java.util.Map;
import org.pdfclown.bytes.IOutputStream;
import org.pdfclown.files.File;
import org.pdfclown.files.IndirectObjects;
import org.pdfclown.objects.PdfDictionary;
import org.pdfclown.objects.PdfIndirectObject;
import org.pdfclown.objects.PdfInteger;
import org.pdfclown.objects.PdfName;
import org.pdfclown.objects.PdfReference;
import org.pdfclown.util.NotImplementedException;

/**
  PDF file writer implementing classic cross-reference table [PDF:1.6:3.4.3].

  @author Stefano Chizzolini (http://www.stefanochizzolini.it)
  @version 0.1.2, 02/04/12
*/
final class PlainWriter extends Writer {

    private static final byte[] TrailerChunk = Encoding.encode(Keyword.Trailer + Symbol.LineFeed);

    private static final String XRefChunk = Keyword.XRef + Symbol.LineFeed;

    private static final String XRefEOLChunk = "" + Symbol.CarriageReturn + Symbol.LineFeed;

    private static final DecimalFormat XRefGenerationFormatter = new DecimalFormat("00000");

    private static final DecimalFormat XRefOffsetFormatter = new DecimalFormat("0000000000");

    PlainWriter(File file, IOutputStream stream) {
        super(file, stream);
    }

    @Override
    protected void writeIncremental() {
        FileParser parser = file.getReader().getParser();
        stream.write(parser.getStream());
        int xrefSize = file.getIndirectObjects().size();
        StringBuilder xrefBuilder = new StringBuilder(XRefChunk);
        {
            StringBuilder xrefSubBuilder = new StringBuilder();
            int xrefSubCount = 0;
            int prevKey = 0;
            for (Map.Entry<Integer, PdfIndirectObject> indirectObjectEntry : file.getIndirectObjects().getModifiedObjects().entrySet()) {
                if (indirectObjectEntry.getKey() - prevKey == 1 || prevKey == 0) {
                    xrefSubCount++;
                } else {
                    appendXRefSubsection(xrefBuilder, prevKey - xrefSubCount + 1, xrefSubCount, xrefSubBuilder);
                    xrefSubBuilder.setLength(0);
                    xrefSubCount = 1;
                }
                prevKey = indirectObjectEntry.getKey();
                if (indirectObjectEntry.getValue().isInUse()) {
                    appendXRefEntry(xrefSubBuilder, indirectObjectEntry.getValue().getReference(), stream.getLength());
                    indirectObjectEntry.getValue().writeTo(stream, file);
                } else {
                    appendXRefEntry(xrefSubBuilder, indirectObjectEntry.getValue().getReference(), 0);
                }
            }
            appendXRefSubsection(xrefBuilder, prevKey - xrefSubCount + 1, xrefSubCount, xrefSubBuilder);
        }
        long startxref = stream.getLength();
        stream.write(xrefBuilder.toString());
        writeTrailer(startxref, xrefSize, parser);
    }

    @Override
    protected void writeLinearized() {
        throw new NotImplementedException();
    }

    @Override
    protected void writeStandard() {
        writeHeader();
        int xrefSize = file.getIndirectObjects().size();
        StringBuilder xrefBuilder = new StringBuilder(XRefChunk);
        {
            appendXRefSubsectionIndexer(xrefBuilder, 0, xrefSize);
            StringBuilder xrefInUseBlockBuilder = new StringBuilder();
            IndirectObjects indirectObjects = file.getIndirectObjects();
            PdfReference freeReference = indirectObjects.get(0).getReference();
            for (int index = 1; index < xrefSize; index++) {
                PdfIndirectObject indirectObject = indirectObjects.get(index);
                if (indirectObject.isInUse()) {
                    appendXRefEntry(xrefInUseBlockBuilder, indirectObject.getReference(), stream.getLength());
                    indirectObject.writeTo(stream, file);
                } else {
                    appendXRefEntry(xrefBuilder, freeReference, index);
                    xrefBuilder.append(xrefInUseBlockBuilder);
                    xrefInUseBlockBuilder.setLength(0);
                    freeReference = indirectObject.getReference();
                }
            }
            appendXRefEntry(xrefBuilder, freeReference, 0);
            xrefBuilder.append(xrefInUseBlockBuilder);
        }
        long startxref = stream.getLength();
        stream.write(xrefBuilder.toString());
        writeTrailer(startxref, xrefSize, null);
    }

    private StringBuilder appendXRefEntry(StringBuilder xrefBuilder, PdfReference reference, long offset) {
        String usage;
        switch(reference.getIndirectObject().getXrefEntry().getUsage()) {
            case Free:
                usage = Keyword.FreeXrefEntry;
                break;
            case InUse:
                usage = Keyword.InUseXrefEntry;
                break;
            default:
                throw new UnsupportedOperationException();
        }
        return xrefBuilder.append(XRefOffsetFormatter.format(offset)).append(Symbol.Space).append(XRefGenerationFormatter.format(reference.getGenerationNumber())).append(Symbol.Space).append(usage).append(XRefEOLChunk);
    }

    /**
    Appends the cross-reference subsection to the specified builder.

    @param xrefBuilder Target builder.
    @param firstObjectNumber Object number of the first object in the subsection.
    @param entryCount Number of entries in the subsection.
    @param xrefSubBuilder Cross-reference subsection entries.
  */
    private StringBuilder appendXRefSubsection(StringBuilder xrefBuilder, int firstObjectNumber, int entryCount, StringBuilder xrefSubBuilder) {
        return appendXRefSubsectionIndexer(xrefBuilder, firstObjectNumber, entryCount).append(xrefSubBuilder);
    }

    /**
    Appends the cross-reference subsection indexer to the specified builder.

    @param xrefBuilder Target builder.
    @param firstObjectNumber Object number of the first object in the subsection.
    @param entryCount Number of entries in the subsection.
  */
    private StringBuilder appendXRefSubsectionIndexer(StringBuilder xrefBuilder, int firstObjectNumber, int entryCount) {
        return xrefBuilder.append(firstObjectNumber).append(Symbol.Space).append(entryCount).append(Symbol.LineFeed);
    }

    /**
    Serializes the file trailer [PDF:1.6:3.4.4].

    @param startxref Byte offset from the beginning of the file to the beginning
      of the last cross-reference section.
    @param xrefSize Total number of entries in the file's cross-reference table,
      as defined by the combination of the original section and all update sections.
    @param parser File parser.
  */
    private void writeTrailer(long startxref, int xrefSize, FileParser parser) {
        stream.write(TrailerChunk);
        PdfDictionary trailer = file.getTrailer();
        updateTrailer(trailer, stream);
        trailer.put(PdfName.Size, new PdfInteger(xrefSize));
        if (parser == null) {
            trailer.remove(PdfName.Prev);
        } else {
            trailer.put(PdfName.Prev, new PdfInteger((int) parser.retrieveXRefOffset()));
        }
        trailer.writeTo(stream, file);
        stream.write(Chunk.LineFeed);
        writeTail(startxref);
    }
}
