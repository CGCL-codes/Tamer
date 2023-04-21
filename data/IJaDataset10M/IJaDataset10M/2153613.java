package edu.harvard.hul.ois.jhove.module.wave;

/**
 *  A class for holding arrays of informative strings that will go into 
 *  properties of a WAVE object. 
 *
 * @author Gary McGath
 *
 */
public class WaveStrings {

    /** A private constructor just to make sure nobody
       instantiates the class by mistake. */
    private WaveStrings() {
    }

    /** Strings for data compression formats; indexed by
     *  COMPRESSION_INDEX */
    public static final String[] COMPRESSION_FORMAT = { "Unknown or unspecified format", "PCM audio in integer format", "Microsoft adaptive PCM", "PCM audio in IEEE floating-point format", "VSELP codec for Windows CE 2.0 device", "IBM CVSD", "Microsoft ALAW", "Microsoft MULAW", "Microsoft DTS", "Microsoft Digital Rights Managed encrypted audio", "Microsoft Speech audio codec", "Windows Media RT Voice", "OKI ADPCM", "Intel ADPCM", "Videologic Systems ADPCM", "Sierra ADPCM", "Antex ADPCM", "DSP Solutions DIGISTD", "DSP Solutions DIGIFIX", "OKI ADPCM chips or firmware", "ADPCM for Jazz 16 chip set", "HP CU Codec", "HP Dynamic Voice", "Yamaha ADPCM", "Speech Compression SONARC", "DSP Group True Speech", "Echo Speech SC1", "Ahead Audio File AF36", "Audio Processing Technology APTX", "Ahead Audio File AF10", "Prosody CTI speech card", "Merging Technologies LRC", "Dolby AC2", "Microsoft GSM610", "Microsoft MSN audio codec", "Antex ADPCME", "Control Resources VQLPC", "DSP Solutions Digireal", "DSP Solutions DIGIADPCM", "Control Resources CR10", "Natural Microsystems VBXADPCM", "Roland RDAC", "Echo Speech SC3", "Rockwell ADPCM", "Rockwell DIGITALK", "Xebec Multimedia Solutions", "Antex G721 ADPCM", "Antex G728 CELP", "Microsoft MSG723", "Microsoft MSG723.1", "Microsoft MSG729", "Microsoft MSG726", "Microsoft MPEG", "InSoft RT24", "InSoft PAC", "ISO/MPEG Layer 3 format tag", "Lucent G723", "Cirrus Logic", "ESS PCM", "Voxware", "Canopus ATRACWAVEFORMAT", "APICOM G726 ADPCM", "APICOM G722 ADPCM", "Microsoft DSAT Display", "Voxware Byte Aligned", "Voxware AC8", "Voxware AC10", "Voxware AC16", "Voxware AC20", "Voxware RT24", "Voxware RT29", "Voxware RT29HW", "Voxware VR12", "Voxware VR18", "Voxware TQ40", "Voxware SC3 (7A)", "Voxware SC3 (7B)", "SoftSound", "Voxware TQ60", "Microsoft MSRT24", "AT&T G729A", "Motion Pixels MVI2", "Datafusion Systems G726", "Datafusion Systems GSM610", "Iterated Systems ISI Audio", "OnLive", "Multitude FT SX20", "G.721 ADPCM", "Convedia G729", "Congruency Audio Codec", "Siemens SBC24", "Sonic Foundry Dolby AC3 SPDIF", "MediaSonic G723", "Prosody CTI speech card", "ZyXEL ADPCM", "Philips LPCBB", "Studer Professional Audio Packed", "Phony Talk", "Racal Recorder GSM", "Racal Recorder G720.a", "Racal G723.1", "Racal Tetra ACELP", "NEC AAC", "Rhetorex ADPCM wave format type", "BeCubed IRAT", "Vivo G723", "Vivo Siren", "Philips CELP", "Philips Grundig", "DEC G723", "SANYO LD-ADPCM wave type", "Sipro Lab ACELPNET", "Sipro Lab ACELP4800", "Sipro Lab ACELP8V3", "Sipro Lab ACELPG729", "Sipro Lab ACELPG729A", "Sipro Lab Kelvin", "VoiceAge AMR", "Dictaphone G726 ADPCM", "Dictaphone CELP68", "Dictaphone CELP54", "Qualcomm Pure Voice", "Qualcomm Half Rate", "Related to GSM 6.10", "Microsoft Audio 1", "Microsoft Audio 2", "Microsoft Multichannel WMA", "WMA lossless", "WMA Pro over S/PDIF", "Unisys ADPCM", "Unisys ULAW", "Unisys ALAW", "Unisys NAP 16K", "SyCom ACM SYC008", "SyCom ACM SYC701 G726L", "SyCom ACM SYC701 CELP54", "SyCom ACM SYC701 CELP68", "Knowledge Adventure ADPCM", "Fraunhofer IIS MPEC 2AAC", "Digital Theater Systems DS", "Creative Labs ADPCM", "Fast Speech 8", "Fast Speech 10", "UHER ADPCM", "Quarterdeck", "I-Link VC", "Aureal Raw Sport", "Interactive Products HSX", "Interactive Products RPELP", "Cs2", "Sony SCX", "Sony SCY", "Sony ATRAC3", "Sony SPC", "Telum", "Telum IA", "Norcom Voice Systems ADPCM", "Fujitsu FM Towns SND", "Fujitsu (301)", "Fujitsu (302)", "Fujitsu (303)", "Fujitsu (304)", "Fujitsu (305)", "Fujitsu (306)", "Fujitsu (307)", "Fujitsu (308)", "Micronas Development", "Micronas CELP833", "Brooktree digital audio format", "QDesign Music", "AT&T VMPCM", "AT&T TPC", "Olivetti SM", "Olivetti PCM", "Olivetti CELP", "Olivetti SBC", "Olivetti OPR", "Lernout & Hauspie Codec", "Lernout & Hauspie CELP", "Lernout & Hauspie SB8", "Lernout & Hauspie SB12", "Lernout & Hauspie SB16", "Norris", "AT&T Soundspace Musicompress", "Sonic Foundry Lossless", "Innings ADPCM", "FAST Multimedia DVM", "Reserved rangle to 0x2600", "Divio's AAC", "Nokia adaptive multirate", "Divio's G726", "3Com NBX", "Adaptive multirate", "AMR with silence detection", "Comverse G723.1", "Comverse AVQSBC", "Comverse old SBC", "Symbol Technology's G729A", "Voice Age AMR WB", "Ingenient's G726", "ISO/MPEG-4 advanced audio Coding", "Encore Software Ltd's G726", "Extensible Wave format" };

    public static final int[] COMPRESSION_INDEX = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0XA, 0xB, 0X10, 0X11, 0X12, 0X13, 0X14, 0X15, 0X16, 0X17, 0X18, 0X19, 0X1A, 0X20, 0X21, 0X22, 0X23, 0X24, 0X25, 0X26, 0X27, 0X28, 0X30, 0X31, 0X32, 0X33, 0X34, 0X35, 0X36, 0X37, 0X38, 0X39, 0X3A, 0X3B, 0X3C, 0X3D, 0X40, 0X41, 0X42, 0X43, 0X44, 0X45, 0X50, 0X52, 0X53, 0X55, 0X59, 0X60, 0X61, 0X62, 0X63, 0X64, 0X65, 0X67, 0X69, 0X70, 0X71, 0X72, 0X73, 0X74, 0X75, 0X76, 0X77, 0X78, 0X79, 0X7A, 0X7B, 0X80, 0X81, 0X82, 0X83, 0X84, 0X85, 0X86, 0X88, 0X89, 0X8A, 0X8B, 0X8C, 0X8D, 0X91, 0X92, 0x93, 0X94, 0X97, 0X98, 0X99, 0XA0, 0XA1, 0XA2, 0XA3, 0XA4, 0XB0, 0X100, 0x101, 0X111, 0X112, 0X120, 0X121, 0X123, 0X125, 0X130, 0X131, 0X132, 0X133, 0X134, 0X135, 0X136, 0X140, 0X141, 0X142, 0X150, 0X151, 0x155, 0X160, 0X161, 0X162, 0x163, 0x164, 0X170, 0X171, 0X172, 0X173, 0X174, 0X175, 0X176, 0X177, 0X178, 0X180, 0X190, 0X200, 0X202, 0X203, 0X210, 0X220, 0X230, 0x240, 0x250, 0x251, 0X260, 0X270, 0X271, 0X272, 0X273, 0X280, 0X281, 0x285, 0X300, 0x301, 0x302, 0x303, 0x304, 0x305, 0x306, 0x307, 0x308, 0x350, 0x351, 0x400, 0x450, 0x680, 0x681, 0x1000, 0x1001, 0x1002, 0x1003, 0x1004, 0x1100, 0x1101, 0x1102, 0x1103, 0x1104, 0x1400, 0x1500, 0x1971, 0X1979, 0x2000, 0x2500, 0x4143, 0x4201, 0x4243, 0x7000, 0x7a21, 0x7a22, 0xa100, 0xa101, 0xa102, 0xa103, 0xa104, 0xa105, 0xa106, 0xa107, 0xfffe };

    /** Strings for SMPTE formats in the Sample Chunk */
    public static final String[] SMPTE_FORMAT = { "No SMPTE offset", "24 frames per second", "25 frames per second", "30 frames per second with frame dropping", "30 frames per second" };

    /** Indices for SMPTE formats in the Sample Chunk */
    public static final int[] SMPTE_FORMAT_INDEX = { 0, 24, 25, 29, 30 };

    /** Flags for SoundInformation bits in the MPEG chunk,
     *  "1" values */
    public static final String[] SOUND_INFORMATION_1 = { "Non-homogeneous sound data", "Padding bit always 0", "Sample frequency 22.05 or 44.1 KHz", "Free format is used" };

    /** Flags for SoundInformation bits in the MPEG chunk,
     *  "0" values */
    public static final String[] SOUND_INFORMATION_0 = { "Homogeneous sound data", "Padding bit may alternate", "", "No free format audio frame" };

    /** Flags for ancillary data definition in the MPEG chunk,
     *  "1" values
     */
    public static final String[] ANCILLARY_DEF_1 = { "Energy of left channel present", "Private byte is free for internal use" };

    /** Flags for ancillary data definition in the MPEG chunk,
     *  "0" values
     */
    public static final String[] ANCILLARY_DEF_0 = { "Energy of left channel absent", "No private byte free for internal use" };
}
