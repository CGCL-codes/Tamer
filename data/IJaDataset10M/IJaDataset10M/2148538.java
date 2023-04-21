package utils.mediaDefinition.general;

import utils.mediaDefinition.template.InfoMapImp;
import utils.mediaInfoTool.Info;
import utils.mediaInfoTool.Parameter;
import utils.mediaInfoTool.ParametersMap;
import java.util.Arrays;
import java.util.List;

public final class VideoInfoMap extends InfoMapImp<Parameter, DefinitionValue> {

    public VideoInfoMap(Info<Parameter, DefinitionValue, ParametersMap<Parameter, DefinitionValue>> type) {
        super(type);
    }

    /**
     * Parametry strumienia Video
     *
     * @author dgornik
     */
    public static enum VideoAttribute implements Parameter {

        ID("ID"), FORMAT("Format"), FORMAT_PROFILE("Format profile"), DURATION("Duration"), CODEC_ID("Codec ID"), BIT_RATE("Bit rate"), TITLE("Title"), ENCODE_SETTINGS("Encoding settings"), WIDTH("Width"), HEIGH("Height"), ASPECT_RATIO("Display aspect ratio"), FRAME_RATE("Frame rate"), RESOLUTION("Resolution"), COLORIMETRY("Colorimetry"), WRITING_LIB("Writing library"), FORMAT_INFO("Format/Info"), MUXING_MODE("Muxing mode"), SCAN_TYPE("Scan type"), LANGUAGE("Language"), STREAM_SIZE("Stream size");

        private final String myName;

        VideoAttribute(String name) {
            myName = name;
        }

        VideoAttribute(VideoFormatAttribute format) {
            myName = format.getName();
        }

        @Override
        public String getName() {
            return myName;
        }

        @Override
        public List<Parameter> getRealm() {
            return Arrays.asList((Parameter[]) VideoAttribute.values());
        }

        @Override
        public Class<?> getValueType() {
            return VideoInfoMap.class;
        }
    }

    /**
     * Video format
     *
     * @author dgornik
     */
    public static enum VideoFormatAttribute implements Parameter {

        BVOP("BVOP"), QPel("QPel"), GMC("GMC"), Matrix("Matrix");

        private final String myName;

        VideoFormatAttribute(String name) {
            myName = "Format settings, " + name;
        }

        @Override
        public String getName() {
            return myName;
        }

        @Override
        public List<Parameter> getRealm() {
            return Arrays.asList((Parameter[]) VideoFormatAttribute.values());
        }

        @Override
        public Class<?> getValueType() {
            return VideoInfoMap.class;
        }
    }

    {
        initAttributes(VideoAttribute.values());
        initAttributes(VideoFormatAttribute.values());
    }

    @Override
    public DefinitionValue parserValue(String stringValue, Parameter def) {
        return new DefinitionValue(stringValue, def);
    }
}
