package de.carne.fs.provider.macho;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import de.carne.fs.core.FileScannerResultNode;
import de.carne.fs.core.format.FormatStruct;
import de.carne.fs.core.format.IntAttribute;
import de.carne.fs.core.format.LongAttribute;
import de.carne.fs.core.format.StringAttribute;
import de.carne.fs.core.transfer.DecodeTextDataHandler;
import de.carne.fs.core.util.CharData;
import de.carne.fs.core.util.LongBytesFormatter;
import de.carne.fs.provider.util.x86.X86OpcodeDecoderTable;
import de.carne.fs.provider.util.x86.X86OpcodeDecoderTable.Mode;

class Section64 extends FormatStruct {

    public static final StringAttribute SECT_NAME = new StringAttribute("sectname", 0, 16);

    public static final StringAttribute SEG_NAME = new StringAttribute("segname", 16, 16);

    public static final LongAttribute ADDR = new LongAttribute("addr", 32, 1, false, 16);

    public static final LongAttribute SIZE = new LongAttribute("size", 40, 1, false, 10);

    public static final IntAttribute OFFSET = new IntAttribute("offset", 48, 1, false, 16);

    public static final IntAttribute ALIGN = new IntAttribute("align", 52, 1, false, 10);

    public static final IntAttribute REL_OFF = new IntAttribute("reloff", 56, 1, false, 16);

    public static final IntAttribute N_RELOC = new IntAttribute("nreloc", 60, 1, false, 16);

    public static final IntAttribute FLAGS = new IntAttribute("flags", 64, 1, false, 16);

    public static final IntAttribute RESERVED1 = new IntAttribute("reserved1", 68, 1, false, 16);

    public static final IntAttribute RESERVED2 = new IntAttribute("reserved2", 72, 1, false, 16);

    public static final IntAttribute RESERVED3 = new IntAttribute("reserved3", 76, 1, false, 16);

    {
        SIZE.setSecondaryFormatter(LongBytesFormatter.THIS);
        FLAGS.setFlagFormatters(MachO.SYM_SETION_FLAGS);
    }

    private Section64(ByteOrder order) {
        super(MachO.NAME_LC_SECTION, order, SECT_NAME, SEG_NAME, ADDR, SIZE, OFFSET, ALIGN, REL_OFF, N_RELOC, FLAGS, RESERVED1, RESERVED2, RESERVED3);
    }

    /**
	 * Structure instance (little endian).
	 */
    public static final Section64 THIS_LE = new Section64(ByteOrder.LITTLE_ENDIAN);

    /**
	 * Structure instance (big endian).
	 */
    public static final Section64 THIS_BE = new Section64(ByteOrder.BIG_ENDIAN);

    @Override
    public FileScannerResultNode decodeBuffer(FileScannerResultNode parent, long position, ByteBuffer buffer) throws IOException {
        final MachOFormatContext context = MachOFormatContext.get();
        if (context.currentSegmentNode != null) {
            long sectionStart = OFFSET.value(buffer) & 0xffffffff;
            final long sectionEnd = sectionStart + SIZE.value(buffer);
            if (sectionStart < context.dataStart) {
                sectionStart = context.dataStart;
            }
            if (sectionStart < sectionEnd && context.currentSegmentNode.start() <= sectionStart && sectionEnd <= context.currentSegmentNode.end()) {
                final String sectionName = String.format(MachO.NAME_SEGMENT_SECTION, CharData.print(SECT_NAME.value(buffer), false));
                final FileScannerResultNode sectionNode = context.currentSegmentNode.addFormat(sectionName, sectionStart, sectionEnd);
                if ((FLAGS.value(buffer) & 0x80000400) != 0) {
                    switch(context.cpuType) {
                        case 0x01000007:
                            sectionNode.addDataHandler(new DecodeTextDataHandler(sectionNode, new X86OpcodeDecoderTable(Mode.MODE64, ADDR.value(buffer))));
                            break;
                    }
                }
            }
        }
        return null;
    }
}
