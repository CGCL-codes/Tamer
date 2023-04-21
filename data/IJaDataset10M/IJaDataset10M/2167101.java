package de.carne.fs.provider.elf;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import de.carne.fs.core.FileScannerResultNode;
import de.carne.fs.core.format.FormatStruct;
import de.carne.fs.core.format.FormatStructTransferHandler;
import de.carne.fs.core.format.IntAttribute;
import de.carne.fs.core.transfer.DecodeTextDataHandler;
import de.carne.fs.provider.util.x86.X86OpcodeDecoderTable;

class ELF32SectionHeader extends FormatStruct {

    public static final IntAttribute SH_NAME = new IntAttribute("sh_name", 0, 1, false, 16);

    public static final IntAttribute SH_TYPE = new IntAttribute("sh_type", 4, 1, false, 16);

    public static final IntAttribute SH_FLAGS = new IntAttribute("sh_flags", 8, 1, false, 16);

    public static final IntAttribute SH_ADDR = new IntAttribute("sh_addr", 12, 1, false, 16);

    public static final IntAttribute SH_OFFSET = new IntAttribute("sh_offset", 16, 1, false, 16);

    public static final IntAttribute SH_SIZE = new IntAttribute("sh_size", 20, 1, false, 16);

    public static final IntAttribute SH_LINK = new IntAttribute("sh_link", 24, 1, false, 16);

    public static final IntAttribute SH_INFO = new IntAttribute("sh_info", 28, 1, false, 16);

    public static final IntAttribute SH_ADDRALIGN = new IntAttribute("sh_addralign", 32, 1, false, 16);

    public static final IntAttribute SH_ENTSIZE = new IntAttribute("sh_entsize", 36, 1, false, 16);

    static {
        SH_TYPE.setValueSymbols(ELFImage.SYM_SH_TYPE);
        SH_FLAGS.setFlagFormatters(ELFImage.SYM_SH_FLAGS32);
    }

    private ELF32SectionHeader(ByteOrder order) {
        super(ELFImage.NAME_ELF32_SECTION_HEADER, order, SH_NAME, SH_TYPE, SH_FLAGS, SH_ADDR, SH_OFFSET, SH_SIZE, SH_LINK, SH_INFO, SH_ADDRALIGN, SH_ENTSIZE);
    }

    /**
	 * Structure instance (LSB).
	 */
    public static final ELF32SectionHeader THIS_LSB = new ELF32SectionHeader(ByteOrder.LITTLE_ENDIAN);

    /**
	 * Structure instance (MSB).
	 */
    public static final ELF32SectionHeader THIS_MSB = new ELF32SectionHeader(ByteOrder.BIG_ENDIAN);

    @Override
    public boolean validate(ByteBuffer buffer) {
        return super.validate(buffer);
    }

    @Override
    public FileScannerResultNode decodeBuffer(FileScannerResultNode parent, long position, ByteBuffer buffer) throws IOException {
        final FileScannerResultNode decoded = parent.addFormat(name(), position, position + size());
        decoded.addDataHandler(new FormatStructTransferHandler(decoded, this));
        final ELFImageFormatContext context = ELFImageFormatContext.get();
        final int offset = SH_OFFSET.value(buffer);
        final int size = SH_SIZE.value(buffer);
        final long sectionPosition = context.baseoff + offset;
        if (offset > 0 && size > 0 && SH_TYPE.value(buffer) != 8 && validateOffset(parent.input(), sectionPosition, size)) {
            if (context.shstroff == position) {
                context.strtab = allocateAndReadBuffer(parent.input(), sectionPosition, size, true);
            }
            final String sectionName = lookupSectionName(context.strtab, SH_NAME.value(buffer));
            final FileScannerResultNode sectionNode = context.baseNode.addFormat(String.format(ELFImage.NAME_ELF_SECTION, sectionName), sectionPosition, sectionPosition + size);
            final int flags = SH_FLAGS.value(buffer);
            if ((flags & 0x4) == 0x4) {
                if (context.machine == 3) {
                    sectionNode.addDataHandler(new DecodeTextDataHandler(sectionNode, new X86OpcodeDecoderTable(X86OpcodeDecoderTable.Mode.MODE32, context.mapper.map(sectionPosition))));
                }
            }
        }
        return decoded;
    }

    private static String lookupSectionName(ByteBuffer strtab, int namendx) {
        final StringBuilder buffer = new StringBuilder();
        if (strtab != null && namendx < strtab.limit()) {
            int cndx = namendx;
            char cval = (char) strtab.get(cndx);
            while (cval != '\0' && cndx < strtab.limit()) {
                buffer.append(cval);
                cval = (char) strtab.get(++cndx);
            }
        }
        return buffer.toString();
    }
}
