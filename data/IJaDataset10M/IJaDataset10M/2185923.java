package org.apache.sanselan.icc;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.common.BinaryConstants;
import org.apache.sanselan.common.BinaryInputStream;

public interface IccConstants {

    public static final int IEC = (((0xff & 'I') << 24) | ((0xff & 'E') << 16) | ((0xff & 'C') << 8) | ((0xff & ' ') << 0));

    public static final int sRGB = (((0xff & 's') << 24) | ((0xff & 'R') << 16) | ((0xff & 'G') << 8) | ((0xff & 'B') << 0));

    public static final IccTagDataType descType = new IccTagDataType("descType", 0x64657363) {

        public void dump(String prefix, byte bytes[]) throws ImageReadException, IOException {
            BinaryInputStream bis = new BinaryInputStream(new ByteArrayInputStream(bytes), BinaryConstants.BYTE_ORDER_NETWORK);
            bis.read4Bytes("type_signature", "ICC: corrupt tag data");
            bis.read4Bytes("ignore", "ICC: corrupt tag data");
            int string_length = bis.read4Bytes("string_length", "ICC: corrupt tag data");
            String s = new String(bytes, 12, string_length - 1);
            System.out.println(prefix + "s: '" + s + "'");
        }
    };

    public static final IccTagDataType dataType = new IccTagDataType("dataType", 0x64617461) {

        public void dump(String prefix, byte bytes[]) throws ImageReadException, IOException {
            BinaryInputStream bis = new BinaryInputStream(new ByteArrayInputStream(bytes), BinaryConstants.BYTE_ORDER_NETWORK);
            bis.read4Bytes("type_signature", "ICC: corrupt tag data");
        }
    };

    public static final IccTagDataType multiLocalizedUnicodeType = new IccTagDataType("multiLocalizedUnicodeType", (0x6D6C7563)) {

        public void dump(String prefix, byte bytes[]) throws ImageReadException, IOException {
            BinaryInputStream bis = new BinaryInputStream(new ByteArrayInputStream(bytes), BinaryConstants.BYTE_ORDER_NETWORK);
            bis.read4Bytes("type_signature", "ICC: corrupt tag data");
        }
    };

    public static final IccTagDataType signatureType = new IccTagDataType("signatureType", ((0x73696720))) {

        public void dump(String prefix, byte bytes[]) throws ImageReadException, IOException {
            BinaryInputStream bis = new BinaryInputStream(new ByteArrayInputStream(bytes), BinaryConstants.BYTE_ORDER_NETWORK);
            bis.read4Bytes("type_signature", "ICC: corrupt tag data");
            bis.read4Bytes("ignore", "ICC: corrupt tag data");
            int thesignature = bis.read4Bytes("thesignature ", "ICC: corrupt tag data");
            System.out.println(prefix + "thesignature: " + Integer.toHexString(thesignature) + " (" + new String(new byte[] { (byte) (0xff & (thesignature >> 24)), (byte) (0xff & (thesignature >> 16)), (byte) (0xff & (thesignature >> 8)), (byte) (0xff & (thesignature >> 0)) }) + ")");
        }
    };

    public static final IccTagDataType textType = new IccTagDataType("textType", 0x74657874) {

        public void dump(String prefix, byte bytes[]) throws ImageReadException, IOException {
            BinaryInputStream bis = new BinaryInputStream(new ByteArrayInputStream(bytes), BinaryConstants.BYTE_ORDER_NETWORK);
            bis.read4Bytes("type_signature", "ICC: corrupt tag data");
            bis.read4Bytes("ignore", "ICC: corrupt tag data");
            String s = new String(bytes, 8, bytes.length - 8);
            System.out.println(prefix + "s: '" + s + "'");
        }
    };

    public static final IccTagDataType IccTagDataTypes[] = { descType, dataType, multiLocalizedUnicodeType, signatureType, textType };

    public static final IccTagType AToB0Tag = new IccTagType("AToB0Tag", "lut8Type or lut16Type or lutAtoBType", 0x41324230);

    public static final IccTagType AToB1Tag = new IccTagType("AToB1Tag", "lut8Type or lut16Type or lutAtoBType", 0x41324231);

    public static final IccTagType AToB2Tag = new IccTagType("AToB2Tag", "lut8Type or lut16Type or lutAtoBType", 0x41324232);

    public static final IccTagType blueMatrixColumnTag = new IccTagType("blueMatrixColumnTag", "XYZType", 0x6258595A);

    public static final IccTagType blueTRCTag = new IccTagType("blueTRCTag", "curveType or parametricCurveType", 0x62545243);

    public static final IccTagType BToA0Tag = new IccTagType("BToA0Tag", "lut8Type or lut16Type or lutBtoAType", 0x42324130);

    public static final IccTagType BToA1Tag = new IccTagType("BToA1Tag", "lut8Type or lut16Type or lutBtoAType", 0x42324131);

    public static final IccTagType BToA2Tag = new IccTagType("BToA2Tag", "lut8Type or lut16Type or lutBtoAType", 0x42324132);

    public static final IccTagType calibrationDateTimeTag = new IccTagType("calibrationDateTimeTag", "dateTimeType", 0x63616C74);

    public static final IccTagType charTargetTag = new IccTagType("charTargetTag", "textType", 0x74617267);

    public static final IccTagType chromaticAdaptationTag = new IccTagType("chromaticAdaptationTag", "s15Fixed16ArrayType", 0x63686164);

    public static final IccTagType chromaticityTag = new IccTagType("chromaticityTag", "chromaticityType", 0x6368726D);

    public static final IccTagType colorantOrderTag = new IccTagType("colorantOrderTag", "colorantOrderType", 0x636C726F);

    public static final IccTagType colorantTableTag = new IccTagType("colorantTableTag", "colorantTableType", 0x636C7274);

    public static final IccTagType copyrightTag = new IccTagType("copyrightTag", "multiLocalizedUnicodeType", 0x63707274);

    public static final IccTagType deviceMfgDescTag = new IccTagType("deviceMfgDescTag", "multiLocalizedUnicodeType", 0x646D6E64);

    public static final IccTagType deviceModelDescTag = new IccTagType("deviceModelDescTag", "multiLocalizedUnicodeType", 0x646D6464);

    public static final IccTagType gamutTag = new IccTagType("gamutTag", "lut8Type or lut16Type or lutBtoAType", 0x67616D74);

    public static final IccTagType grayTRCTag = new IccTagType("grayTRCTag", "curveType or parametricCurveType", 0x6B545243);

    public static final IccTagType greenMatrixColumnTag = new IccTagType("greenMatrixColumnTag", "XYZType", 0x6758595A);

    public static final IccTagType greenTRCTag = new IccTagType("greenTRCTag", "curveType or parametricCurveType", 0x67545243);

    public static final IccTagType luminanceTag = new IccTagType("luminanceTag", "XYZType", 0x6C756D69);

    public static final IccTagType measurementTag = new IccTagType("measurementTag", "measurementType", 0x6D656173);

    public static final IccTagType mediaBlackPointTag = new IccTagType("mediaBlackPointTag", "XYZType", 0x626B7074);

    public static final IccTagType mediaWhitePointTag = new IccTagType("mediaWhitePointTag", "XYZType", 0x77747074);

    public static final IccTagType namedColor2Tag = new IccTagType("namedColor2Tag", "namedColor2Type", 0x6E636C32);

    public static final IccTagType outputResponseTag = new IccTagType("outputResponseTag", "responseCurveSet16Type", 0x72657370);

    public static final IccTagType preview0Tag = new IccTagType("preview0Tag", "lut8Type or lut16Type or lutBtoAType", 0x70726530);

    public static final IccTagType preview1Tag = new IccTagType("preview1Tag", "lut8Type or lut16Type or lutBtoAType", 0x70726531);

    public static final IccTagType preview2Tag = new IccTagType("preview2Tag", "lut8Type or lut16Type or lutBtoAType", 0x70726532);

    public static final IccTagType profileDescriptionTag = new IccTagType("profileDescriptionTag", "multiLocalizedUnicodeType", 0x64657363);

    public static final IccTagType profileSequenceDescTag = new IccTagType("profileSequenceDescTag", "profileSequenceDescType", 0x70736571);

    public static final IccTagType redMatrixColumnTag = new IccTagType("redMatrixColumnTag", "XYZType", 0x7258595A);

    public static final IccTagType redTRCTag = new IccTagType("redTRCTag", "curveType or parametricCurveType", 0x72545243);

    public static final IccTagType technologyTag = new IccTagType("technologyTag", "signatureType", 0x74656368);

    public static final IccTagType viewingCondDescTag = new IccTagType("viewingCondDescTag", "multiLocalizedUnicodeType", 0x76756564);

    public static final IccTagType viewingConditionsTag = new IccTagType("viewingConditionsTag", "viewingConditionsType", 0x76696577);

    public static final IccTagType TagTypes[] = { AToB0Tag, AToB1Tag, AToB2Tag, blueMatrixColumnTag, blueTRCTag, BToA0Tag, BToA1Tag, BToA2Tag, calibrationDateTimeTag, charTargetTag, chromaticAdaptationTag, chromaticityTag, colorantOrderTag, colorantTableTag, copyrightTag, deviceMfgDescTag, deviceModelDescTag, gamutTag, grayTRCTag, greenMatrixColumnTag, greenTRCTag, luminanceTag, measurementTag, mediaBlackPointTag, mediaWhitePointTag, namedColor2Tag, outputResponseTag, preview0Tag, preview1Tag, preview2Tag, profileDescriptionTag, profileSequenceDescTag, redMatrixColumnTag, redTRCTag, technologyTag, viewingCondDescTag, viewingConditionsTag };
}
