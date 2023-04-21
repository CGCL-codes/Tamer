package com.flanderra;

import static com.flanderra.BitUtils._bitsFixed88;
import static com.flanderra.BitUtils._bitsUI16;
import static com.flanderra.BitUtils._bitsUI32;
import static com.flanderra.BitUtils._bitsUI8;
import static com.flanderra.BitUtils._bytesToBits;
import static com.flanderra.BitUtils._concat;
import static com.flanderra.BitUtils._hex;
import static com.flanderra.BitUtils._parseUI32;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.InflaterInputStream;

public class SwfIO {

    private static final byte[] PROLOG_FWS = new byte[] { 0x46, 0x57, 0x53 };

    private static final byte[] PROLOG_CWS = new byte[] { 0x43, 0x57, 0x53 };

    public static Map read(BitInputStream bis) throws IOException {
        Map result = new LinkedHashMap();
        byte[] head = new byte[4];
        InputStream is = bis.getInputStream();
        is.read(head);
        boolean zipContent = head[0] == 67;
        result.put("zipContent", head[0] == 67);
        result.put("version", head[3]);
        head[0] = 0x46;
        byte[] lengthBytes = new byte[4];
        is.read(lengthBytes);
        long fileLength = _parseUI32(lengthBytes);
        result.put("fileLength", fileLength);
        InputStream swfUnzippedInputStream = null;
        if (zipContent) {
            swfUnzippedInputStream = new InflaterInputStream(is);
        } else {
            swfUnzippedInputStream = is;
        }
        BitInputStream bitInputStream = new BitInputStream(swfUnzippedInputStream);
        result.put("sizes", BasicTypesUtils.rect(bitInputStream));
        byte[] frameDelay = new byte[] { bitInputStream.read(), bitInputStream.read() };
        result.put("frameRate", BitUtils._parseFixed88(frameDelay));
        byte[] frameCount = new byte[] { bitInputStream.read(), bitInputStream.read() };
        result.put("frameCount", BitUtils._parseUI16(frameCount));
        boolean finished = false;
        List tags = new ArrayList();
        while (!finished) {
            try {
                Map tagContents = new LinkedHashMap();
                Map tagHeader = BasicTypesUtils.tagHeader(bitInputStream);
                Long tagLength = (Long) tagHeader.get("tagLength");
                Long tagCode = (Long) tagHeader.get("tagCode");
                String tagName = TagUtils.getTagNameForCode(tagCode.intValue());
                tagContents.put("tagCode", tagCode);
                tagContents.put("tagName", tagName);
                tagContents.put("tagLength", tagLength);
                if (TagUtils.getImpl(tagCode.intValue()) != null) {
                    byte[] buf = new byte[tagLength.intValue()];
                    bitInputStream.read(buf);
                    ByteArrayInputStream bais = new ByteArrayInputStream(buf);
                    tagContents.putAll(TagUtils.getImpl(tagCode.intValue()).read(new BitInputStream(bais)));
                } else {
                    ByteArrayOutputStream tagContentBaos = new ByteArrayOutputStream();
                    for (int i = 0; i < tagLength; i++) {
                        tagContentBaos.write(bitInputStream.read());
                    }
                    tagContents.put("__data__", _hex(tagContentBaos.toByteArray()));
                }
                if ((tags.size() > 2)) {
                    String prevTagName = (String) ((Map) tags.get(tags.size() - 1)).get("tagName");
                    String prevPrevTagName = (String) ((Map) tags.get(tags.size() - 2)).get("tagName");
                    if (prevTagName != null && prevPrevTagName != null && prevTagName.equals("End") && prevPrevTagName.equals("End")) {
                        finished = true;
                    }
                }
                tags.add(tagContents);
            } catch (IOException e) {
                finished = true;
            } catch (Throwable e2) {
                finished = true;
                e2.printStackTrace();
            }
        }
        result.put("tags", tags);
        return result;
    }

    public static void write(Map context, BitOutputStream bos) throws IOException {
        List tags = (List) context.get("tags");
        Bits bittags = new Bits();
        for (int i = 0; i < tags.size(); i++) {
            Map tag = (Map) tags.get(i);
            Bits bittag = null;
            Bits tagHeader = null;
            Bits tagContent = null;
            Long tagCode = TypeUtils.toLong(tag.get("tagCode"));
            Long tagLength = TypeUtils.toLong(tag.get("tagLength"));
            if (tag.get("__data__") != null) {
                tagHeader = BasicTypesUtils.tagHeader(tagCode, tagLength);
                String tagDataStr = (String) tag.get("__data__");
                byte[] tagDataBytes = _hex(tagDataStr);
                tagContent = _bytesToBits(tagDataBytes);
            } else {
                IOStruct tagImpl = TagUtils.getImpl(tagCode.intValue());
                if (tagImpl == null) {
                    continue;
                }
                tagContent = TagUtils.getImpl(tagCode.intValue()).write(tag);
                if (!tag.containsKey("tagLength")) {
                    tagLength = new Long(tagContent.getData().length);
                }
                tagHeader = BasicTypesUtils.tagHeader(tagCode, tagLength);
            }
            bittag = BitUtils._concat(tagHeader, tagContent);
            bittags = _concat(bittags, bittag);
        }
        double frameRate = TypeUtils.toDouble(context.get("frameRate"));
        long frameCount = TypeUtils.toLong(context.get("frameCount"));
        long version = TypeUtils.toLong(context.get("version"));
        Bits sizesRect = BasicTypesUtils.rect((Map) context.get("sizes"));
        int totalLength = sizesRect.getData().length + bittags.getData().length + 12;
        if (context.get("zipContent").toString().equalsIgnoreCase("true")) {
            bos.write(PROLOG_CWS);
        } else {
            bos.write(PROLOG_FWS);
        }
        bos.writeBits(_bitsUI8(version));
        bos.writeBits(_bitsUI32(totalLength));
        bos.write(sizesRect.getData());
        bos.write(_bitsFixed88(frameRate).getData());
        bos.write(_bitsUI16(frameCount).getData());
        bos.write(bittags.getData());
    }
}
