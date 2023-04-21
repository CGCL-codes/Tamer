package org.jcvi.trace.sanger.chromatogram.scf.section;

import java.util.EnumMap;
import java.util.Map;
import org.jcvi.trace.sanger.chromatogram.scf.header.SCFHeader;

public class DefaultSectionCodecFactory implements SectionCodecFactory {

    private static final SectionCodec NULL_CODEC = new NullSectionCodec();

    private static final SectionCodec COMMENT_CODEC = new CommentSectionCodec();

    private static final SectionCodec PRIVATE_DATA_CODEC = new PrivateDataCodec();

    private static final Map<Section, SectionCodec> VERSION_3_MAP;

    private static final Map<Section, SectionCodec> VERSION_2_MAP;

    static {
        VERSION_3_MAP = new EnumMap<Section, SectionCodec>(Section.class);
        VERSION_2_MAP = new EnumMap<Section, SectionCodec>(Section.class);
        VERSION_3_MAP.put(Section.COMMENTS, COMMENT_CODEC);
        VERSION_3_MAP.put(Section.PRIVATE_DATA, PRIVATE_DATA_CODEC);
        VERSION_3_MAP.put(Section.SAMPLES, new Version3SampleSectionCodec());
        VERSION_3_MAP.put(Section.BASES, new Version3BasesSectionCodec());
        VERSION_2_MAP.put(Section.COMMENTS, COMMENT_CODEC);
        VERSION_2_MAP.put(Section.PRIVATE_DATA, PRIVATE_DATA_CODEC);
        VERSION_2_MAP.put(Section.SAMPLES, new Version2SampleSectionCodec());
        VERSION_2_MAP.put(Section.BASES, new Version2BasesSectionCodec());
    }

    public SectionDecoder getSectionParserFor(Section s, SCFHeader header) {
        cannotBeNull(s);
        if (header == null) {
            throw new IllegalArgumentException("header can not be null");
        }
        if (header.getVersion() < 3.0f) {
            return getSectionCodecFrom(VERSION_2_MAP, s);
        }
        return getSectionCodecFrom(VERSION_3_MAP, s);
    }

    private static SectionCodec getSectionCodecFrom(Map<Section, SectionCodec> sectionMap, Section s) {
        if (sectionMap.containsKey(s)) {
            return sectionMap.get(s);
        }
        return NULL_CODEC;
    }

    public SectionEncoder getSectionEncoderFor(Section s, float version) {
        cannotBeNull(s);
        verifySupportedVersion(version);
        if (version < 3) {
            return getSectionCodecFrom(VERSION_2_MAP, s);
        }
        return getSectionCodecFrom(VERSION_3_MAP, s);
    }

    private void verifySupportedVersion(float version) {
        if (version < 2 || version >= 4) {
            throw new IllegalArgumentException("can not encode for version < 2 or >= 4");
        }
    }

    private void cannotBeNull(Section s) {
        if (s == null) {
            throw new IllegalArgumentException("Section can not be null");
        }
    }
}
