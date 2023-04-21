package de.carne.fs.provider.elf;

import java.util.HashMap;
import java.util.Map;
import de.carne.fs.core.transfer.IntFlagFormatter;
import de.carne.fs.core.transfer.LongFlagFormatter;
import de.carne.fs.core.transfer.StaticIntFlagFormatter;
import de.carne.fs.core.transfer.StaticLongFlagFormatter;

final class ELFImage {

    public static String NAME = "ELF Image";

    public static String NAME_ELF_IDENT = "ELF ident";

    public static String NAME_ELF32_HEADER = "ELF-32 header";

    public static String NAME_ELF32_PROGRAM_HEADER_TABLE = "ELF-32 program header table";

    public static String NAME_ELF32_PROGRAM_HEADER = "ELF-32 program header";

    public static String NAME_ELF32_SECTION_HEADER_TABLE = "ELF-32 section header table";

    public static String NAME_ELF32_SECTION_HEADER = "ELF-32 section header";

    public static String NAME_ELF64_HEADER = "ELF-64 header";

    public static String NAME_ELF64_PROGRAM_HEADER_TABLE = "ELF-64 program header table";

    public static String NAME_ELF64_PROGRAM_HEADER = "ELF-64 program header";

    public static String NAME_ELF64_SECTION_HEADER_TABLE = "ELF-64 section header table";

    public static String NAME_ELF64_SECTION_HEADER = "ELF-64 section header";

    public static String NAME_ELF_SECTION = "section [%s]";

    public static int MAGIC = 0x464C457F;

    public static final Map<Byte, String> SYM_EI_CLASS = new HashMap<Byte, String>();

    static {
        SYM_EI_CLASS.put((byte) 0x01, "ELFCLASS32");
        SYM_EI_CLASS.put((byte) 0x02, "ELFCLASS64");
    }

    public static final Map<Byte, String> SYM_EI_DATA = new HashMap<Byte, String>();

    static {
        SYM_EI_DATA.put((byte) 1, "ELFDATA2LSB");
        SYM_EI_DATA.put((byte) 2, "ELFDATA2MSB");
    }

    public static final Map<Byte, String> SYM_EI_VERSION = new HashMap<Byte, String>();

    static {
        SYM_EI_VERSION.put((byte) 0x01, "EV_CURRENT");
    }

    public static final Map<Byte, String> SYM_EI_OSABI = new HashMap<Byte, String>();

    static {
        SYM_EI_OSABI.put((byte) 0x00, "ELFOSABI_SYSV");
        SYM_EI_OSABI.put((byte) 0x01, "ELFOSABI_HPUX");
        SYM_EI_OSABI.put((byte) 0xff, "ELFOSABI_STANDALONE");
    }

    public static final Map<Short, String> SYM_E_TYPE = new HashMap<Short, String>();

    static {
        SYM_E_TYPE.put((short) 1, "ET_REL");
        SYM_E_TYPE.put((short) 2, "ET_EXEC");
        SYM_E_TYPE.put((short) 3, "ET_DYN");
        SYM_E_TYPE.put((short) 4, "ET_CORE");
    }

    public static final Map<Short, String> SYM_E_MACHINE = new HashMap<Short, String>();

    static {
        SYM_E_MACHINE.put((short) 1, "EM_M32");
        SYM_E_MACHINE.put((short) 2, "EM_SPARC");
        SYM_E_MACHINE.put((short) 3, "EM_386");
        SYM_E_MACHINE.put((short) 4, "EM_68K");
        SYM_E_MACHINE.put((short) 5, "EM_88K");
        SYM_E_MACHINE.put((short) 7, "EM_860");
        SYM_E_MACHINE.put((short) 8, "EM_MIPS");
        SYM_E_MACHINE.put((short) 10, "EM_MIPS_RS4_BE");
        SYM_E_MACHINE.put((short) 18, "EM_SPARC32PLUS");
        SYM_E_MACHINE.put((short) 40, "EM_ARM");
        SYM_E_MACHINE.put((short) 43, "EM_SPARCV9");
        SYM_E_MACHINE.put((short) 62, "EM_AMD64");
    }

    public static final Map<Integer, String> SYM_SH_TYPE = new HashMap<Integer, String>();

    static {
        SYM_SH_TYPE.put(0, "SHT_NULL");
        SYM_SH_TYPE.put(1, "SHT_PROGBITS");
        SYM_SH_TYPE.put(2, "SHT_SYMTAB");
        SYM_SH_TYPE.put(3, "SHT_STRTAB");
        SYM_SH_TYPE.put(4, "SHT_RELA");
        SYM_SH_TYPE.put(5, "SHT_HASH");
        SYM_SH_TYPE.put(6, "SHT_DYNAMIC");
        SYM_SH_TYPE.put(7, "SHT_NOTE");
        SYM_SH_TYPE.put(8, "SHT_NOBITS");
        SYM_SH_TYPE.put(9, "SHT_REL");
        SYM_SH_TYPE.put(10, "SHT_SHLIB");
        SYM_SH_TYPE.put(11, "SHT_DYNSYM");
    }

    public static final IntFlagFormatter[] SYM_SH_FLAGS32 = { new StaticIntFlagFormatter(0x1, "SHF_WRITE"), new StaticIntFlagFormatter(0x2, "SHF_ALLOC"), new StaticIntFlagFormatter(0x4, "SHF_EXECINSTR") };

    public static final LongFlagFormatter[] SYM_SH_FLAGS64 = { new StaticLongFlagFormatter(0x1, "SHF_WRITE"), new StaticLongFlagFormatter(0x2, "SHF_ALLOC"), new StaticLongFlagFormatter(0x4, "SHF_EXECINSTR") };

    public static final Map<Integer, String> SYM_P_TYPE = new HashMap<Integer, String>();

    static {
        SYM_P_TYPE.put(0, "PT_NULL");
        SYM_P_TYPE.put(1, "PT_LOAD");
        SYM_P_TYPE.put(2, "PT_DYNAMIC");
        SYM_P_TYPE.put(3, "PT_INTERP");
        SYM_P_TYPE.put(4, "PT_NOTE");
        SYM_P_TYPE.put(5, "PT_SHLIB");
        SYM_P_TYPE.put(6, "PT_PHDR");
    }

    public static final IntFlagFormatter[] SYM_P_FLAGS = { new StaticIntFlagFormatter(0x1, "PF_X"), new StaticIntFlagFormatter(0x2, "PF_W"), new StaticIntFlagFormatter(0x4, "PF_R") };
}
